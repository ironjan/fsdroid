package de.upb.fsmi.news;

import org.springframework.http.converter.feed.AbstractWireFeedHttpMessageConverter;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;

public class FsmiRssChannelHttpMessageConverter extends AbstractWireFeedHttpMessageConverter<Channel>{

	public FsmiRssChannelHttpMessageConverter() {
		super(org.springframework.http.MediaType.TEXT_XML);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return Channel.class.isAssignableFrom(clazz);
	}}
