package de.upb.fsmi.rest;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

/**
 * Interface to generate a "REST" client which is used to download the RSS feed of fsmi.
 */
@Rest(rootUrl = "https://fsmi.uni-paderborn.de/", converters = {
		FsmiRssChannelHttpMessageConverter.class})
public interface RssRest {

	@Get("neuigkeiten/?type=100")
	public Channel getNews();
}
