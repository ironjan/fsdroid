package de.upb.fsmi.rest;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom RSS converter since FSMI provides the RSS feed as text/xml and not application/rss etc.
 */
public class FsmiRssChannelHttpMessageConverter extends RssChannelHttpMessageConverter {

    Logger LOGGER = LoggerFactory.getLogger(FsmiRssChannelHttpMessageConverter.class.getSimpleName());

    public FsmiRssChannelHttpMessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_RSS_XML);
        mediaTypes.add(MediaType.TEXT_XML);
        setSupportedMediaTypes(mediaTypes);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        LOGGER.debug("supports({})", clazz);
        return Channel.class.isAssignableFrom(clazz);
    }


}
