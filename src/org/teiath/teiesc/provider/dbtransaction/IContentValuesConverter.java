package org.teiath.teiesc.provider.dbtransaction;

import android.content.ContentValues;

public interface IContentValuesConverter<T>
{
    public ContentValues convert(T item);
}
