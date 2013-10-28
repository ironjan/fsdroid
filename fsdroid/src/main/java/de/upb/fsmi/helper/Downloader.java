package de.upb.fsmi.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Log;

/**
 * This class is used to download files from the internet.
 */
public class Downloader {

	@SuppressWarnings("nls")
	private static final String TMP_FILE_EXTENSION = "tmp", 
			TMP_FILE_PREFIX = "fsupb";
	private static final String TAG = Downloader.class.getSimpleName();

	/**
	 * Downloads the file located at uri to the local device
	 * 
	 * @param context
	 * @param uri
	 *            the file's internet uri
	 * @return the downloaded file
	 * @throws MalformedURLException
	 *             not a correct url
	 * @throws IOException
	 *             error when writing file
	 * @throws ProtocolException
	 *             ?
	 * @throws FileNotFoundException
	 *             could not found the file
	 */
	public static File download(Context context, String uri)
			throws MalformedURLException, IOException, ProtocolException,
			FileNotFoundException {
		disableKeepAliveIfNecessary();

		File dir = context.getCacheDir();
		File file = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_EXTENSION,
				dir);
		FileOutputStream fileOutput = new FileOutputStream(file);

		executeDownload(uri, fileOutput);

		fileOutput.close();
		return file;
	}

	private static void executeDownload(String uri, FileOutputStream fileOutput)
			throws MalformedURLException, IOException, ProtocolException {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			executeDownloadPostGingerbread(uri, fileOutput);
		} else {
			executeDownloadPreGingerbreag(uri, fileOutput);
		}
	}

	private static void disableKeepAliveIfNecessary() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private static void executeDownloadPreGingerbreag(String uri,
			FileOutputStream fileOutput) throws IOException {
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android"); //$NON-NLS-1$
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		response.getEntity().writeTo(fileOutput);
		client.close();
	}

	private static void executeDownloadPostGingerbread(String uri,
			FileOutputStream fileOutput) throws MalformedURLException,
			IOException, ProtocolException {
		URL url = new URL(uri);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		urlConnection.setRequestMethod("GET"); //$NON-NLS-1$
		urlConnection.setDoOutput(true);
		urlConnection.connect();
		InputStream inputStream = urlConnection.getInputStream();

		final int size = urlConnection.getContentLength();
		byte[] buffer = new byte[1024];
		int bufferLength = 0;

		while ((bufferLength = inputStream.read(buffer)) > 0) {
			fileOutput.write(buffer, 0, bufferLength);
			Log.v(TAG, bufferLength + "/" + size); //$NON-NLS-1$
		}
		fileOutput.close();
		inputStream.close();
	}

}
