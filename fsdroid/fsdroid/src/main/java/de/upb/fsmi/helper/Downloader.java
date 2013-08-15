package de.upb.fsmi.helper;

import java.io.*;
import java.net.*;
import java.net.ProtocolException;

import org.apache.http.*;
import org.apache.http.client.methods.*;

import android.content.*;
import android.net.http.*;
import android.os.*;
import android.util.*;

public class Downloader {

	public static File download(Context context, String uri)
			throws MalformedURLException, IOException, ProtocolException,
			FileNotFoundException {

		disableKeepAliveIfNecessary();

		File dir = context.getCacheDir();
		File file = File.createTempFile("fsupb", "tmp", dir);
		FileOutputStream fileOutput = new FileOutputStream(file);

		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		downloadPostGingerbread(uri, fileOutput);
		// } else {
		// downloadPreGingerbreag(uri, fileOutput);
		// }
		fileOutput.close();
		return file;
	}

	private static void disableKeepAliveIfNecessary() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	private static void downloadPreGingerbreag(String uri,
			FileOutputStream fileOutput) throws IOException {
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		response.getEntity().writeTo(fileOutput);
		client.close();
	}

	private static void downloadPostGingerbread(String uri,
			FileOutputStream fileOutput) throws MalformedURLException,
			IOException, ProtocolException {
		URL url = new URL(uri);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(true);
		urlConnection.connect();
		InputStream inputStream = urlConnection.getInputStream();

		final int size = urlConnection.getContentLength();
		byte[] buffer = new byte[1024];
		int bufferLength = 0;

		while ((bufferLength = inputStream.read(buffer)) > 0) {
			fileOutput.write(buffer, 0, bufferLength);
			Log.v("Downloader", bufferLength + "/" + size);
		}
		fileOutput.close();
		inputStream.close();
	}

}
