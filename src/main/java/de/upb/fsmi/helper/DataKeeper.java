package de.upb.fsmi.helper;

import android.annotation.*;
import android.content.*;
import android.util.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.sharedpreferences.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import de.upb.fsmi.widget.StatusAppWidgetProvider.*;

@EBean(scope = EBean.Scope.Singleton)
public class DataKeeper {

    @SuppressWarnings("nls")
    private static final String MEETING_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ",
            STATUS_URL = "https://fsmi.uni-paderborn.de/zeugs/buzzer/status";

    private static final int _5_MINUTES = 1000 * 60 * 5;

    @SuppressWarnings("nls")
    public static final String ACTION_DATA_REFRESH_STARTED = "ACTION_DATA_REFRESH_STARTED",
            ACTION_DATA_REFRESH_COMPLETED = "ACTION_DATA_REFRESH_COMPLETED";

    private static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    private static final String TAG = DataKeeper.class.getSimpleName();

    @RootContext
    Context context;

    @Pref
    MeetingPrefs_ meetingPrefs;

    @Bean
    ConnectionBean connectionBean;

    private Date date = null;

    private int status = 0;

    private boolean isRefreshing = false;

    @SuppressWarnings("nls")
    public Date getNextMeetingDate() {
        if (date == null && hasRecentDate()) {
            Log.v(TAG,
                    "date == null and recent date available, fetching from prefs.");
            long nextMeetingInMillis = meetingPrefs.nextMeetingInMillis().get();
            date = new Date(nextMeetingInMillis);
        }
        DateFormat df = DateFormat.getDateTimeInstance();
        Log.v(TAG, "Date of next meeting: "
                + (date != null ? df.format(date) : "null"));
        return date;
    }

    @SuppressWarnings("nls")
    public Integer getFsmiState() {
        if (hasRecentStatus()) {
            status = meetingPrefs.lastStatus().get();
        }
        Log.v(TAG, "Most recent status: " + status);
        return Integer.valueOf(status);
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    @SuppressWarnings("nls")
    public synchronized void refresh(boolean byUser)
            throws NoAvailableNetworkException {
        Log.d(TAG, "Refresh requested.");
        if (isRefreshing) {
            return;
        }

        if (connectionBean.isNetworkAvailable()) {
            Log.d(TAG, "Network is available, we're trying to refresh.");
            isRefreshing = true;
            executeRefresh(byUser);
        } else {
            Log.d(TAG, "No network, no refresh");
            sendBroadcast(ACTION_DATA_REFRESH_COMPLETED);
            throw new NoAvailableNetworkException();
        }
    }

    @Background
    public void refresh(Call c) {
        refreshStatus();
        c.setStatus(status);
    }

    @SuppressWarnings("nls")
    private boolean hasRecentStatus() {
        final long currentTime = System.currentTimeMillis();
        final long lastStatusUpdateInMillis = meetingPrefs
                .lastStatusUpdateInMillis().get();

        Log.d(TAG,
                "Last status update: "
                        + DateFormat.getDateTimeInstance().format(
                        new Date(lastStatusUpdateInMillis))
                        + ". recent = \"<5min\"");
        return (currentTime - lastStatusUpdateInMillis) < _5_MINUTES;
    }

    @SuppressWarnings("nls")
    private void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
        Log.d(TAG, "sent broadcast: " + action + " @"
                + context.getClass().getSimpleName());
    }

    private void executeRefresh(boolean byUser) {
        sendBroadcast(ACTION_DATA_REFRESH_STARTED);

        refreshStatus();
        refreshDate(byUser);
        sendBroadcast(ACTION_DATA_REFRESH_COMPLETED);
        isRefreshing = false;

    }

    @SuppressWarnings("nls")
    private void refreshStatus() {
        try {
            final String statusURL = STATUS_URL;
            File file = Downloader.download(context, statusURL);
            this.status = parseStatus(file) + 1;

            final long currentTime = System.currentTimeMillis();
            meetingPrefs.edit().lastStatus().put(status)
                    .lastStatusUpdateInMillis().put(currentTime).apply();

            Log.d(TAG, "Status refreshed, new status: " + status);
            file.deleteOnExit();
        } catch (MalformedURLException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        }

    }

    private static int parseStatus(File file) {
        try {
            Scanner sc = new Scanner(file);
            int status = sc.nextInt();
            return status;
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (InputMismatchException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return 0;
    }

    @SuppressWarnings("nls")
    private void refreshDate(boolean byUser) {
        Log.d(TAG, "Refreshing date, requestedByUser=" + byUser);
        DateFormat df = DateFormat.getDateTimeInstance();
        if (byUser || !hasRecentDate()) {
            Log.d(TAG, "Requested by user or cached date too old.");
            Date downloadDate = downloadDate();
            date = (downloadDate != null) ? downloadDate : date;
            Log.d(TAG, "New date is : "
                    + (date != null ? df.format(date) : "null"));
        } else {
            long nextMeetingInMillis = meetingPrefs.nextMeetingInMillis().get();
            date = new Date(nextMeetingInMillis);
            Log.d(TAG, "Cached date is " + df.format(date));
        }
    }

    @SuppressWarnings("nls")
    private boolean hasRecentDate() {
        final long lastMeetingUpdate = meetingPrefs.lastMeetingUpdateInMillis()
                .get();
        final long currentTime = System.currentTimeMillis();

        final long diff = currentTime - lastMeetingUpdate;

        DateFormat df = DateFormat.getDateTimeInstance();
        Log.d(TAG,
                "lastMeetingUpdate (" + df.format(new Date(lastMeetingUpdate))
                        + ") too old: " + (diff / 1000) + "s > 1 day ("
                        + DAY_IN_MILLIS + ").");
        return diff < DAY_IN_MILLIS;
    }

    @SuppressWarnings("nls")
    private Date downloadDate() {
        Log.d(TAG, "Downloading new date..");
        Date downloadedDate = null;
        try {
            final String die_fachschaft = "http://fsmi.uni-paderborn.de/";
            File file = Downloader.download(context, die_fachschaft);
            downloadedDate = parseDate(file);
            if (downloadedDate != null) {
                long currentTime = System.currentTimeMillis();
                meetingPrefs.edit().lastMeetingUpdateInMillis()
                        .put(currentTime).nextMeetingInMillis()
                        .put(downloadedDate.getTime()).apply();
            }
            file.deleteOnExit();
        } catch (MalformedURLException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } catch (SAXException e) {
            logError(e);
        } catch (ParserConfigurationException e) {
            logError(e);
        } catch (XPathExpressionException e) {
            logError(e);
        }
        return downloadedDate;
    }

    @SuppressWarnings("nls")
    @SuppressLint("SimpleDateFormat")
    private static Date parseDate(File file) throws SAXException, IOException,
            ParserConfigurationException, FileNotFoundException,
            XPathExpressionException {
        final FileReader fileReader = new FileReader(file);
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new InputSource(fileReader));

        final XPathFactory newInstance = XPathFactory.newInstance();
        final XPath newXPath = newInstance.newXPath();
        final XPathExpression compile = newXPath
                .compile("//*[@id=\"c109\"]/div[2]/p[1]");
        XPathExpression xpath = compile;

        Node node = (Node) xpath.evaluate(doc, XPathConstants.NODE);

        Date parsedDate = null;
        if (node != null) {
            String result = node.getAttributes().getNamedItem("title")
                    .getNodeValue();

            SimpleDateFormat format = new SimpleDateFormat(MEETING_DATE_FORMAT);

            try {
                parsedDate = format.parse(result);
                DateFormat df = DateFormat.getDateTimeInstance();
                Log.d(TAG, "Parsed downloaded date: " + result + " -> "
                        + (parsedDate != null ? df.format(parsedDate) : "null"));
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        fileReader.close();
        return parsedDate;
    }

    private static void logError(Exception e) {
        Log.e(TAG, e.getMessage(), e);
    }
}
