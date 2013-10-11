package de.upb.fsmi.rest;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

public class MyRestTemplate extends RestTemplate {
	private static final int INTERVAL_30_SECONDS = 30 * 1000;
	private static final int CONNECT_TIMEOUT = INTERVAL_30_SECONDS;

	@SuppressWarnings("nls")
	public MyRestTemplate() {
		if (getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
			Log.d("HTTP", "HttpUrlConnection is used");
			((SimpleClientHttpRequestFactory) getRequestFactory())
					.setConnectTimeout(CONNECT_TIMEOUT);
			((SimpleClientHttpRequestFactory) getRequestFactory())
					.setReadTimeout(CONNECT_TIMEOUT);
		} else if (getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
			Log.d("HTTP", "HttpClient is used");
			((HttpComponentsClientHttpRequestFactory) getRequestFactory())
					.setReadTimeout(CONNECT_TIMEOUT);
			((HttpComponentsClientHttpRequestFactory) getRequestFactory())
					.setConnectTimeout(CONNECT_TIMEOUT);
		}
	}
}