package de.upb.fsmi.fsdroid.sync.synchronizators;

import android.content.*;

import org.slf4j.*;
import org.xmlpull.v1.*;

import java.io.*;
import java.net.*;
import java.util.*;

import de.upb.fsmi.fsdroid.helper.*;

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
        File file = null;
        try {
            final String statusURL = STATUS_URL;
            file = Downloader.download(context, statusURL);
            this.status = parseStatus(file) + 1;

            final long currentTime = System.currentTimeMillis();
            meetingPrefs.edit().lastStatus().put(status)
                    .lastStatusUpdateInMillis().put(currentTime).apply();

            LOGGER.debug("Status refreshed, new status: {} ", status);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    private int parseStatus(File file) {
        try {
            Scanner sc = new Scanner(file);
            status = sc.nextInt();
            return status;
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return 0;
    }
}
