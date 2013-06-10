package com.github.ironjan.fsupb.fragments;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import android.util.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.github.ironjan.fsupb.*;
import com.googlecode.androidannotations.annotations.*;

@EFragment(R.layout.fragment_test)
@OptionsMenu(R.menu.menu_main)
public class TestFragment extends SherlockFragment {

	private static final String TAG = TestFragment.class.getSimpleName();
	@ViewById
	TextView txtDownload, txtDate, txtStatus;
	private MenuItem abRefresh;

	@AfterViews
	@Background
	void afterViews() {
		long start = System.currentTimeMillis();
		int loopCounter = 0;
		while (abRefresh == null) {
			loopCounter++;
		}

		long time = System.currentTimeMillis() - start;
		Log.v(TAG, "Counted to " + loopCounter + " while waiting " + time
				+ " ms for optionsMenu");
		refresh();
	}

	@Background
	void refresh() {
		showProgressInActionBar();
		try {
			final String die_fachschaft = "http://fsmi.uni-paderborn.de/";
			File file = download(die_fachschaft);
			parseDate(file);
			final String status = "http://karo-kaffee.upb.de/fsmi/status";
			file = download(status);
			parseStatus(file);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (SAXException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (XPathExpressionException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		hideProgressInActionBar();
	}

	@UiThread
	void showProgressInActionBar() {
		Log.v(TAG, "showProgressInActionBar");
		SherlockFragmentActivity activity = getSherlockActivity();
		activity.setProgressBarIndeterminate(true);
		activity.setProgressBarIndeterminateVisibility(true);

		Log.v(TAG, "abRefresh: " + abRefresh);
		if (abRefresh != null) {
			abRefresh.setVisible(false);
		}
	}

	@UiThread
	void hideProgressInActionBar() {
		Log.v(TAG, "hideProgressInActionBar");
		SherlockFragmentActivity activity = getSherlockActivity();
		activity.setProgressBarIndeterminate(false);
		activity.setProgressBarIndeterminateVisibility(false);

		Log.v(TAG, "abRefresh: " + abRefresh);
		if (abRefresh != null) {
			abRefresh.setVisible(true);
		}
	}

	private void parseStatus(File file) {
		try {
			Scanner sc = new Scanner(file);
			updateStatus((sc.nextInt() == 1));
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	@UiThread
	void updateStatus(boolean open) {
		String statusString = (open) ? "Offen" : "zu";
		txtStatus.setText(statusString);
	}

	void parseDate(File file) throws SAXException, IOException,
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

		Date date = new Date(0);
		try {
			date = format.parse(result);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		updateDate(date);
	}

	File download(final String die_fachschaft) throws MalformedURLException,
			IOException, ProtocolException, FileNotFoundException {
		URL url = new URL(die_fachschaft);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(true);
		urlConnection.connect();
		File file = File.createTempFile("fsupb", ".html");
		FileOutputStream fileOutput = new FileOutputStream(file);
		InputStream inputStream = urlConnection.getInputStream();

		int totalSize = urlConnection.getContentLength();
		int downloadedSize = 0;
		byte[] buffer = new byte[1024];
		int bufferLength = 0;

		while ((bufferLength = inputStream.read(buffer)) > 0) {
			fileOutput.write(buffer, 0, bufferLength);
			downloadedSize += bufferLength;
			updateProgress(downloadedSize, totalSize);
		}
		fileOutput.close();
		inputStream.close();
		return file;
	}

	@UiThread
	void updateDate(Date date) {
		DateFormat df = DateFormat.getDateTimeInstance();
		txtDate.setText(df.format(date));
	}

	@UiThread
	void updateProgress(int downloadedSize, int totalSize) {
		txtDownload.setText(downloadedSize + "/" + totalSize);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu != null) {
			this.abRefresh = menu.findItem(R.id.ab_refresh);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@OptionsItem(R.id.ab_refresh)
	void actionRefresh() {
		txtDate.setText("");
		txtStatus.setText("");
		refresh();
	}
}
