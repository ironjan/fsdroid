package de.upb.fsmi.fsdroid.sync.synchronizators;

import org.xmlpull.v1.*;

import java.io.*;

public interface Synchronizator {
    void executeSync() throws IOException, XmlPullParserException;
}
