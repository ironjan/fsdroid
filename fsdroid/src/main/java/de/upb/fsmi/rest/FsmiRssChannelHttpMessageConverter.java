package de.upb.fsmi.rest;

import org.springframework.http.converter.feed.AbstractWireFeedHttpMessageConverter;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;

/**
 * Custom RSS Converter since FSMI provides the RSS feed as text/xml and not application/rss etc.
 */
public class FsmiRssChannelHttpMessageConverter extends AbstractWireFeedHttpMessageConverter<Channel>{

	public FsmiRssChannelHttpMessageConverter() {
		super(org.springframework.http.MediaType.TEXT_XML);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return Channel.class.isAssignableFrom(clazz);
	}}
