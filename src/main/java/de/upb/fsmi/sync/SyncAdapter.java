package de.upb.fsmi.sync;

import android.accounts.*;
import android.annotation.*;
import android.content.*;
import android.os.*;
import android.util.*;

import org.slf4j.*;
import org.xmlpull.v1.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import de.upb.fsmi.*;
import de.upb.fsmi.db.*;


/**
 * SyncAdapter to implement the synchronization.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String SYNC_FINISHED = "SYNC_FINISHED";
    private static final String TAG = SyncAdapter.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
    private static SyncAdapter instance = null;
    private static Object lock = new Object();
    /**
     * Fri, 10 Jan 2014 17:24:00 +0100
     */
    final SimpleDateFormat SDF = new SimpleDateFormat("dd MM yyy HH:mm:ss zzzzz");
    private final Context mContext;

    private SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        if (BuildConfig.DEBUG) LOGGER.debug("Created SyncAdapter({},{})", context, autoInitialize);
        this.mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.mContext = context;
        if (BuildConfig.DEBUG)
            LOGGER.debug("Created SyncAdapter({},{},{})", new Object[]{context, autoInitialize, allowParallelSyncs});
    }

    public static SyncAdapter getInstance(Context context) {
        synchronized (lock) {
            if (instance == null) {
                instance = createSyncAdapterSingleton(context);
            }

            return instance;
        }
    }

    private static SyncAdapter createSyncAdapterSingleton(Context context) {
        Context applicationContext = context.getApplicationContext();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            return new SyncAdapter(applicationContext, true, false);
        } else {
            return new SyncAdapter(applicationContext, true);
        }
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformeSync({},{},{},{},{})", new Object[]{account, bundle, authority, contentProviderClient, syncResult});

        try {
            executeSync(false);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XmlPullParserException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformeSync({},{},{},{},{}) done", new Object[]{account, bundle, authority, contentProviderClient, syncResult});
    }

    public void executeSync(boolean force) throws IOException, XmlPullParserException {
        if (force)
            LOGGER.warn("executeSync({})", force);
        else if (BuildConfig.DEBUG) LOGGER.debug("executeSync({})", force);

        InputStream in = downloadFile();

        LOGGER.debug("Got connection, start parsing");
        List<NewsItem> list = new ArrayList<NewsItem>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            list = readFeed(parser);
        } finally {
            in.close();
        }

        LOGGER.debug("Read {} items", list.size());


        DatabaseManager databaseManager = DatabaseManager.getInstance(getContext());

        for (NewsItem item : list) {
            databaseManager.createOrUpdate(item);
        }

        broadcastSyncEnd();

        if (force)
            LOGGER.warn("executeSync({}) done", force);
        else if (BuildConfig.DEBUG) LOGGER.debug("executeSync({})", force);
    }

    private List<NewsItem> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
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
            } else if (name.equals("channel")) {
//                 do nothing?
            } else {
                skip(parser);
            }
        }
        return entries;
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
                .replace("Oct", "1")
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

        URL url = new URL("http://fsmi.uni-paderborn.de/neuigkeiten/?type=100");
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
        getContext().sendBroadcast(i);
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd() done");
    }

}
