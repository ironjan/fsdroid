package de.upb.fsmi.fsdroid.db;

import android.content.*;

public interface ContentValuesConverter<T> {
    public ContentValues convert(T t);

    public ContentValues convertAddId(T t);
}
