package com.github.ironjan.fsupb.stuff;

import java.io.*;
import java.net.*;

public class Downloader {

	public static File download(String uri) throws MalformedURLException,
			IOException, ProtocolException, FileNotFoundException {
		URL url = new URL(uri);
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
			// TODO log progress
		}
		fileOutput.close();
		inputStream.close();
		return file;
	}
}
