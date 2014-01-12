package de.upb.fsmi.rest;

import org.springframework.http.*;
import org.springframework.http.converter.feed.*;

import java.util.*;

/**
 * Custom RSS converter since FSMI provides the RSS feed as text/xml and not application/rss etc.
 */
public class FsmiRssChannelHttpMessageConverter extends RssChannelHttpMessageConverter {

    public FsmiRssChannelHttpMessageConverter() {
        super();

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.TEXT_XML);
        setSupportedMediaTypes(mediaTypes);
    }


}
