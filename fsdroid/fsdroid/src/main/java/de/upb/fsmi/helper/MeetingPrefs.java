package de.upb.fsmi.helper;

import com.googlecode.androidannotations.annotations.sharedpreferences.*;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value = Scope.UNIQUE)
public interface MeetingPrefs {

	@DefaultLong(0L)
	public long lastMeetingUpdateInMillis();

	@DefaultLong(-1L)
	public long nextMeetingInMillis();

	@DefaultInt(0)
	public int lastStatus();

	@DefaultLong(0L)
	public long lastStatusUpdateInMillis();
}
