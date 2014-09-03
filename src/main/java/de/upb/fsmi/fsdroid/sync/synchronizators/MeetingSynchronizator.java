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

    /** Matches date as given in homepage */
    private static final String DATE_EXPRESSION = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}";
    /** date is between two quotes, therefore quote as delimiter */
    private static final String DATE_DELIMITER = "\"";
    public static final String DATE_LINE_IDENTIFIER = "N&auml;chste <b>FSR-Sitzung</b> in<br />";
    private final Context context;
    private static final String TAG = MeetingSynchronizator.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);

    public static final Pattern DATE_PATTERN = Pattern.compile(".*?(\\d\\d\\d\\d-\\d\\d-\\d\\d).*$");
    public static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String MEETING_DATE_URL = "https://fsmi.uni-paderborn.de/?eID=fsmi_sitzung";

    private final SimpleDateFormat germanFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

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
        try {
            File lp = Downloader.download(context, "https://fsmi.uni-paderborn.de/");
            Date date = extractDateFromFile(lp);
            persistNewDate(date);
            lp.deleteOnExit();
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Extracts the date from the given file
     * @param lp the landing page file
     * @return either a date or null if something went wrong
     * @throws ParseException
     * @throws FileNotFoundException
     */
    private Date extractDateFromFile(File lp) throws ParseException, FileNotFoundException {
        String dateLine = findLineContainingDate(lp);
        String dateString = extractDateStringFromLine(dateLine);
        Date date = convertStringToDate(dateString);
        return date;
    }

    private Date convertStringToDate(String dateString) throws ParseException {
        Date date = null;
        if (dateString != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = simpleDateFormat.parse(dateString);
        }
        return date;
    }

    private String extractDateStringFromLine(String dateLine) {
        if (dateLine == null) {
            return null;
        }

        String dateString = null;
        Scanner scanner = new Scanner(dateLine);

        scanner.useDelimiter(DATE_DELIMITER);
        while (scanner.hasNext()) {
            String potMatch = scanner.next();
            if (potMatch.matches(DATE_EXPRESSION)) {
                dateString = potMatch;
                break;
            }
        }
        scanner.close();
        return dateString;
    }

    private String findLineContainingDate(File lp) throws FileNotFoundException {
        Scanner sc = new Scanner(lp);
        String dateLine = null;
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            if (s.contains(DATE_LINE_IDENTIFIER)) {
                dateLine = s;
                break;
            }
        }
        sc.close();
        return dateLine;
    }

    private void persistNewDate(Date downloadedDate) {
        if (downloadedDate == null) {
            LOGGER.warn("downloadedDate was null. Nothing to persist.");
            return;
        }

        long currentTime = System.currentTimeMillis();

        String formattedString = germanFormat.format(downloadedDate);

        ContentValues cvs = new ContentValues();
        cvs.put(MeetingDate.COLUMN_LAST_UPDATE, currentTime);
        cvs.put(MeetingDate.COLUMN_VALUE, formattedString);

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
