package de.upb.fsmi.fsdroid.sync.synchronizators;

import android.content.*;

import org.slf4j.*;
import org.xmlpull.v1.*;

import java.io.*;
import java.net.*;
import java.util.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.helper.*;
import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;

public class StatusSynchronizator implements Synchronizator {
    @SuppressWarnings("nls")
    private static final String STATUS_URL = "https://fsmi.uni-paderborn.de/zeugs/buzzer/status";

    private final Logger LOGGER = LoggerFactory.getLogger(StatusSynchronizator.class.getSimpleName());
    private Context context;
    private int status = 0;


    MeetingPrefs_ meetingPrefs;

    private StatusSynchronizator(Context context) {
        this.context = context;
        meetingPrefs = new MeetingPrefs_(context);
    }

    public static Synchronizator getInstance(Context context) {
        return new StatusSynchronizator(context);
    }

    @Override
    public void executeSync() throws IOException, XmlPullParserException {
        if (BuildConfig.DEBUG) LOGGER.debug("executeSync()");

        File file = null;
        try {
            final String statusURL = STATUS_URL;
            file = Downloader.download(context, statusURL);
            this.status = parseStatus(file) + 1;

            final long currentTime = System.currentTimeMillis();

            ContentValues cvs = new ContentValues();
            cvs.put(Status.COLUMN_LAST_UPDATE, currentTime);
            cvs.put(Status.COLUMN_VALUE, status);
            context.getContentResolver().insert(FSDroidContentProvider.STATUS_URI, cvs);
            context.getContentResolver().notifyChange(FSDroidContentProvider.STATUS_URI, null);
            LOGGER.debug("Status refreshed, new status: {} ", status);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (file != null) {
                file.delete();
            }
            if (BuildConfig.DEBUG) LOGGER.debug("file deleted");
        }

        if (BuildConfig.DEBUG) LOGGER.debug("executeSync() done");
    }

    private int parseStatus(File file) {
        if (BuildConfig.DEBUG) LOGGER.debug("parseStatus({})", file);

        try {
            Scanner sc = new Scanner(file);
            status = sc.nextInt();

            if (BuildConfig.DEBUG) LOGGER.debug("parseStatus({}) -> {}", file, status);
            return status;
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (BuildConfig.DEBUG) LOGGER.debug("parseStatus({}) -> 0", file);
        return 0;
    }
}
