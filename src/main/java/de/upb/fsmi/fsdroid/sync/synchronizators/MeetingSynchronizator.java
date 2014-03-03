package de.upb.fsmi.fsdroid.sync.synchronizators;

import android.annotation.*;
import android.content.*;

import org.slf4j.*;
import org.xml.sax.*;
import org.xmlpull.v1.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import de.upb.fsmi.fsdroid.helper.*;

/**
 * Created by ljan on 03.03.14.
 */
public class MeetingSynchronizator implements Synchronizator {

    private final Context context;
    private static final String TAG = MeetingSynchronizator.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);

    public static final Pattern DATE_PATTERN = Pattern.compile(".*?(\\d\\d\\d\\d-\\d\\d-\\d\\d).*$");
    public static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String MEETING_DATE_URL = "https://fsmi.uni-paderborn.de/?eID=fsmi_sitzung";


    MeetingPrefs_ meetingPrefs;

    private MeetingSynchronizator(Context context) {
        this.context = context;
        meetingPrefs = new MeetingPrefs_(context);
    }

    public static Synchronizator getInstance(Context context) {
        return new MeetingSynchronizator(context);
    }

    @Override
    public void executeSync() throws IOException, XmlPullParserException {
        LOGGER.debug("Downloading new date..");
        Date downloadedDate = null;
        try {
            File file = Downloader.download(context, MEETING_DATE_URL);
            downloadedDate = parseDate(file);
            if (downloadedDate != null) {
                long currentTime = System.currentTimeMillis();
                meetingPrefs.edit().lastMeetingUpdateInMillis()
                        .put(currentTime).nextMeetingInMillis()
                        .put(downloadedDate.getTime()).apply();
            }
            file.deleteOnExit();
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SAXException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XPathExpressionException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("nls")
    @SuppressLint("SimpleDateFormat")
    private static Date parseDate(File file) throws SAXException, IOException,
            ParserConfigurationException, FileNotFoundException,
            XPathExpressionException {
        Date parsedDate = null;

        Scanner sc = new Scanner(file);
        StringBuffer content = new StringBuffer();

        while (sc.hasNextLine()) {
            content.append(sc.nextLine());
        }

        Matcher m = DATE_PATTERN.matcher(content);
        if (m.find()) {
            try {
                parsedDate = API_DATE_FORMAT.parse(m.group(1));
            } catch (ParseException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            LOGGER.error("no date found");
        }
        sc.close();

        return parsedDate;
    }
}
