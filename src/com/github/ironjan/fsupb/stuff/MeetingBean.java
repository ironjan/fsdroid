package com.github.ironjan.fsupb.stuff;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import android.annotation.*;
import android.util.*;

import com.github.ironjan.fsupb.fragments.*;
import com.googlecode.androidannotations.annotations.*;

@EBean
public class MeetingBean {
	private static final String TAG = MeetingBean.class.getSimpleName();

	@Background
	public void refreshDate(TestFragment fragment) {
		try {
			final String die_fachschaft = "http://fsmi.uni-paderborn.de/";
			File file = Downloader.download(die_fachschaft);
			Date date = parseDate(file);
			fragment.updateDate(date);
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
	}

	@SuppressLint("SimpleDateFormat")
	private static Date parseDate(File file) throws SAXException, IOException,
			ParserConfigurationException, FileNotFoundException,
			XPathExpressionException {
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new FileReader(file)));

		XPathExpression xpath = XPathFactory.newInstance().newXPath()
				.compile("//*[@id=\"c109\"]/div[2]/p[1]");

		Node node = (Node) xpath.evaluate(doc, XPathConstants.NODE);
		String result = node.getAttributes().getNamedItem("title")
				.getNodeValue();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		Date date = null;
		try {
			date = format.parse(result);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return date;
	}

	void logError(Exception e) {
	}
}
