package com.github.ironjan.fsupb.stuff;

import java.io.*;
import java.net.*;
import java.util.*;

import android.util.*;

import com.github.ironjan.fsupb.fragments.*;
import com.googlecode.androidannotations.annotations.*;

@EBean
public class StatusBean {
	private static final String TAG = StatusBean.class.getSimpleName();

	@Background
	public void refreshStatus(TestFragment fragment) {
		try {
			final String statusURL = "http://karo-kaffee.upb.de/fsmi/status";
			File file = Downloader.download(statusURL);
			fragment.setStatus(parseStatus(file));
		} catch (MalformedURLException e) {
			fu(e);
		} catch (IOException e) {
			fu(e);
		}
	}

	private static int parseStatus(File file) {
		try {
			Scanner sc = new Scanner(file);
			int status = sc.nextInt();
			return status;
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IllegalStateException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (InputMismatchException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (NoSuchElementException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return 0;
	}

	void fu(Exception e) {
		Log.e(TAG, e.getMessage(), e);
	}
}
