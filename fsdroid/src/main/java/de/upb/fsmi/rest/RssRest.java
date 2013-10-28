package de.upb.fsmi.rest;

import org.springframework.web.client.*;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.*;
import com.googlecode.androidannotations.annotations.rest.*;

/**
 * Interface to generate a "REST" client which is used to download the RSS feed
 * of fsmi.
 */
@Rest(rootUrl = "https://fsmi.uni-paderborn.de/", converters = { FsmiRssChannelHttpMessageConverter.class })
interface RssRest {

	RestTemplate getRestTemplate();

	void setRestTemplate(RestTemplate restTemplate);

	@Get("neuigkeiten/?type=100")
	Channel getNews();
}
