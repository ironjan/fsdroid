package de.upb.fsmi.fsdroid.sync.entities;

import android.provider.*;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = MeetingDate.TABLE)
public class MeetingDate {
    public static final String TABLE = "meeting_date";

    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_LAST_UPDATE = "lastUpdate";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long _id;
    @DatabaseField(columnName = COLUMN_VALUE)
    private String value;

    public MeetingDate() {
    }

    @DatabaseField(columnName = COLUMN_LAST_UPDATE)
    long lastUpdate;

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
