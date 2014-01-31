package de.upb.fsmi.fsdroid.helper;

import org.androidannotations.annotations.sharedpreferences.*;
import org.androidannotations.annotations.sharedpreferences.SharedPref.*;

/**
 * Preferences interface
 */
@SharedPref(value = Scope.UNIQUE)
public interface MeetingPrefs {


    /**
     * the time of the last meeting update in milliseconds
     *
     * @return a pref editor
     */
    @DefaultLong(0L)
    public long lastMeetingUpdateInMillis();

    /**
     * the time of the next meeting in milliseconds
     *
     * @return a pref editor
     */
    @DefaultLong(-1L)
    public long nextMeetingInMillis();

    /**
     * The last known status
     *
     * @return a pref editor
     */
    @DefaultInt(0)
    public int lastStatus();

    /**
     * time of the last update in milliseconds
     *
     * @return a pref editor
     */
    @DefaultLong(0L)
    public long lastStatusUpdateInMillis();

    /**
     * time of the last news update in millis
     *
     * @return a pref editor
     */
    @DefaultLong(0L)
    public long lastNewsUpdateInMillis();
}
