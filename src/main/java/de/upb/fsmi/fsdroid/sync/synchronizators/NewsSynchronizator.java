package de.upb.fsmi.fsdroid.sync.synchronizators;

import android.content.*;
import android.util.*;

import org.slf4j.*;
import org.xmlpull.v1.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.db.*;
import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;


public class NewsSynchronizator implements Synchronizator {
    public static final String SYNC_FINISHED = "SYNC_FINISHED";
    private static final String TAG = NewsSynchronizator.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
    public static final String NEWS_URL = "http://fsmi.uni-paderborn.de/neuigkeiten/?type=100";
    private final Context context;
    /**
     * Fri, 10 Jan 2014 17:24:00 +0100
     */
    final SimpleDateFormat SDF = new SimpleDateFormat("dd MM yyy HH:mm:ss zzzzz");

    private NewsSynchronizator(Context context) {
        this.context = context;
    }

    public static Synchronizator getInstance(Context context) {
        return new NewsSynchronizator(context);
    }

    @Override
    public void executeSync() throws IOException, XmlPullParserException {
        if (BuildConfig.DEBUG) LOGGER.debug("executeSync()");


        InputStream in = downloadFile();


        LOGGER.debug("Got connection, start parsing");
        List<NewsItem> list = new ArrayList<NewsItem>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            final List<NewsItem> entries = readFeed(parser);
            if (entries.size() > 0) {
                clearNews();
                persist(entries);
            }
        } finally {
            in.close();
        }


        broadcastSyncEnd();

        if (BuildConfig.DEBUG) LOGGER.debug("executeSync()");
    }

    private void clearNews() {
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        databaseManager.clearNews();
    }

    private List<NewsItem> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        int totalItems = 0;

        List<NewsItem> entries = new ArrayList<NewsItem>();

        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            LOGGER.trace("Got name={}", name);
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                entries.add(readEntry(parser));
                totalItems++;
            } else if (name.equals("channel")) {
//                mustn't get skipped, we want to enter channel tag
            } else {
                skip(parser);
            }


        }
        return entries;
    }

    private void persist(List<NewsItem> list) {
        final NewsItemContentValuesConverter converter = new NewsItemContentValuesConverter();

        final int size = list.size();
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            values[i] = converter.convert(list.get(i));
        }

        final ContentResolver cr = context.getContentResolver();
        cr.bulkInsert(NewsItemContract.NEWS_URI, values);
        cr.notifyChange(NewsItemContract.NEWS_URI, null);

        list.clear();
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    NewsItem readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, null, "item");
        String title = null;
        String description = null;
        String link = null;
        Date pubDate = null;
        String encodedContent = null;
        String author = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("author")) {
                author = readAuthor(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("pubDate")) {
                pubDate = readDate(parser);
            } else if (name.equals("content:encoded")) {
                encodedContent = readEncodedContent(parser);
            } else {
                skip(parser);
            }
        }
        return new NewsItem(author, encodedContent, description, link, title, pubDate);
    }

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "author");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "author");
        return author;
    }

    private String readEncodedContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "content:encoded");
        String encodedContent = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "content:encoded");
        return encodedContent;
    }

    private Date readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pubDate");

        String correctedDateString = date.substring(5)
                .replace("Jan", "01")
                .replace("Feb", "02")
                .replace("Mar", "03")
                .replace("Apr", "04")
                .replace("May", "05")
                .replace("Jun", "06")
                .replace("Jul", "07")
                .replace("Aug", "08")
                .replace("Sep", "09")
                .replace("Oct", "10")
                .replace("Nov", "11")
                .replace("Dec", "12");

        try {
            return SDF.parse(correctedDateString);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return title;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "description");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return title;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private InputStream downloadFile() throws IOException {
        LOGGER.debug("downloadFile()");

        URL url = new URL(NEWS_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        LOGGER.debug("downloadFile() done");
        return conn.getInputStream();

    }

    private void broadcastSyncEnd() {
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd()");

        Intent i = new Intent(SYNC_FINISHED);
        context.sendBroadcast(i);
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd() done");
    }

}
