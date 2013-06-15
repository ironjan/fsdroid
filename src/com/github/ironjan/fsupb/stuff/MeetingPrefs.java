package com.github.ironjan.fsupb.stuff;

import com.googlecode.androidannotations.annotations.sharedpreferences.*;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value = Scope.UNIQUE)
public interface MeetingPrefs {

	@DefaultLong(0L)
	public long lastMeetingUpdateInMillis();

	@DefaultLong(-1L)
	public long nextMeetingInMillis();
}
