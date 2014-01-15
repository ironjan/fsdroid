package de.upb.fsmi.rest;

import android.util.*;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.*;
import org.springframework.http.client.*;
import org.springframework.web.client.*;

@EBean(scope = EBean.Scope.Singleton)
public class RestBean implements RssRest {

	private static final String TAG = RestBean.class.getSimpleName();

	@RestService
	RssRest rss;

	private static final int INTERVAL_10_SECONDS = 10 * 1000;
	
	private static final int CONNECT_TIMEOUT = INTERVAL_10_SECONDS;

	@AfterInject
	void updateTimeot() {
		ClientHttpRequestFactory requestFactory = rss.getRestTemplate()
				.getRequestFactory();
		
		if (requestFactory instanceof SimpleClientHttpRequestFactory) {
			Log.d("HTTP", "HttpUrlConnection is used");
			((SimpleClientHttpRequestFactory) requestFactory)
					.setConnectTimeout(CONNECT_TIMEOUT);
			((SimpleClientHttpRequestFactory) requestFactory)
					.setReadTimeout(CONNECT_TIMEOUT);
		} else if (requestFactory instanceof HttpComponentsClientHttpRequestFactory) {
			Log.d("HTTP", "HttpClient is used");
			((HttpComponentsClientHttpRequestFactory) requestFactory)
					.setReadTimeout(CONNECT_TIMEOUT);
			((HttpComponentsClientHttpRequestFactory) requestFactory)
					.setConnectTimeout(CONNECT_TIMEOUT);
		}


        Log.v(TAG, "timeout refreshed.");
	}

	@Override
	public Channel getNews() {
		Log.v(TAG, "getNews()");
		try{
			Channel news = rss.getNews();
			return news;
		}
		catch(Exception e){
			Log.e(TAG, e.getMessage(),e);
		}
		Log.v(TAG, "getNews() completed");
		return null;
//		return news;
	}

	@Override
	public RestTemplate getRestTemplate() {
		return null;
	}

	@Override
	public void setRestTemplate(RestTemplate pRestTemplate) {
		return;
	}

}
