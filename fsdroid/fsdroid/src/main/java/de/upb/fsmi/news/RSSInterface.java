package de.upb.fsmi.news;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest(rootUrl = "https://fsmi.uni-paderborn.de/", converters = {
		FsmiRssChannelHttpMessageConverter.class})
public interface RSSInterface {

	@Get("neuigkeiten/?type=100")
	public Channel getNews();
}
