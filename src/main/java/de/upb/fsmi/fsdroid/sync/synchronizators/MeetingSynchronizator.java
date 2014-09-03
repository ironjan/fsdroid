package de.upb.fsmi.fsdroid.sync.synchronizators;

import android.annotation.*;
import android.content.*;

import org.slf4j.*;
import org.w3c.dom.*;
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
import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;

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

    private final SimpleDateFormat germanFormat = new SimpleDateFormat("dd.MM.yyyy");

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
            // sync date via landing page and XPATH
            File lp = Downloader.download(context, "https://fsmi.uni-paderborn.de/");
            Scanner sc = new Scanner(lp);


            String dateString = null;
            boolean notFinished = true;
            while (sc.hasNextLine() && notFinished) {
                String s = sc.nextLine();
                if (s.contains("chste <b>FSR-Sitzung</b> in<br />")) {
                    // date is in this line
                    Scanner scanner = new Scanner(s);
                    scanner.useDelimiter("\"");
                    while (scanner.hasNext() && notFinished) {
                        String potMatch = scanner.next();
                        if (potMatch.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}")) {
                            dateString = potMatch;
                            notFinished = false;
                        }
                    }
                }
            }
            if (dateString != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date date = simpleDateFormat.parse(dateString);
                persistNewDate(date);
            }
            lp.deleteOnExit();
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void persistNewDate(Date downloadedDate) {
        if (downloadedDate == null) {
            LOGGER.warn("downloadedDate was null. Nothing to persist.");
            return;
        }

        long currentTime = System.currentTimeMillis();

        ContentValues cvs = new ContentValues();
        cvs.put(MeetingDate.COLUMN_LAST_UPDATE, currentTime);
        cvs.put(MeetingDate.COLUMN_VALUE, germanFormat.format(downloadedDate));

        context.getContentResolver().insert(FSDroidContentProvider.MEETING_DATE_URI, cvs);
        context.getContentResolver().notifyChange(FSDroidContentProvider.MEETING_DATE_URI, null);
    }

    @SuppressWarnings("nls")
    @SuppressLint("SimpleDateFormat")
    private static Date parseDate(File file) throws SAXException, IOException,
            ParserConfigurationException,
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
