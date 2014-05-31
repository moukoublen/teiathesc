package org.teiath.teiesc.provider.dbtransaction;

import android.database.Cursor;

public interface ICursorExtractor<T>
{
    public abstract T extract(Cursor c);
}
