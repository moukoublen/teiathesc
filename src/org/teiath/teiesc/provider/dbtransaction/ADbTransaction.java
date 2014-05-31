package org.teiath.teiesc.provider.dbtransaction;

import java.util.Collection;
import java.util.List;

import org.teiath.teiesc.provider.TeiathDbProviderMetaData;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public abstract class ADbTransaction<T> 
    implements IContentValuesConverter<T>, ICursorExtractor<T>
{
    protected ADbTableMetaData mTable;
    
    public ADbTransaction(ADbTableMetaData table)
    {
        this.setTable(table);
    }
    
    // --- setters - getters ---
    public final ADbTableMetaData getTable()
    {
        return mTable;
    }
    
    protected final void setTable(ADbTableMetaData mTable)
    {
        this.mTable = mTable;
    }
    // --- /setters - getters ---
    
    // implemented 
    public final void deleteAll(ContentResolver cr)
    {
        CheckTable();
        deleteAll(cr, getTable());
    }
    
    public final void getAll(ContentResolver cr, List<T> dest)
    {
        CheckTable();
        getAll(cr, getTable(), this, dest);
    }

    public final Uri store(ContentResolver cr, T item)
    {
        CheckTable();
        return store(cr, getTable(), item, this);
    }

    public final void storeAll(ContentResolver cr, Collection<T> items)
    {
        CheckTable();
        storeAll(cr, getTable(), items, this);
    }
    

    //                 >>>--- Static Transaction ---<<<
    //    ***Delete All***
    public static final void deleteAll(ContentResolver cr, ADbTableMetaData table)
    {
        Uri uri = TeiathDbProviderMetaData.getTableContentUri(table);
        deleteAll(cr, uri);
    }

    public static final void deleteAll(ContentResolver cr, Uri uri)
    {
        cr.delete(uri, null, null);
    }
    
    //    ***Get All***         (take list and fill)
    public static final <E> void getAll(ContentResolver cr,
            ADbTableMetaData table, ICursorExtractor<E> extractor, List<E> dest)
    {
        Uri uri = TeiathDbProviderMetaData.getTableContentUri(table);
        getAll(cr, uri, extractor, dest);
    }
    
    public static final <E> void getAll(ContentResolver cr, Uri uri,
            ICursorExtractor<E> extractor, List<E> dest)
    {
        Cursor c = cr.query(uri, null, null, null, null);
        extractAll(c, extractor, dest);
    }
    
    public static final <E> void extractAll(Cursor c,
            ICursorExtractor<E> extractor, List<E> dest)
    {
        if (c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                E toAdd = extractor.extract(c);
                dest.add(toAdd);
            }
        }
    }

    //    ***Store***
    public static final <E> Uri store(ContentResolver cr,
            ADbTableMetaData table, E item, 
            IContentValuesConverter<E> converter)
    {
        Uri uri = TeiathDbProviderMetaData.getTableContentUri(table);
        return store(cr, uri, item, converter);
    }

    public static final <E> Uri store(ContentResolver cr, Uri uri, 
            E item, IContentValuesConverter<E> converter)
    {
        Uri insertedUri = cr.insert(uri, converter.convert(item));
        return insertedUri;
    }

    //    ***Store All***
    public static final <E> void storeAll(ContentResolver cr,
            ADbTableMetaData table, Collection<E> items,
            IContentValuesConverter<E> converter)
    {
        Uri uri = TeiathDbProviderMetaData.getTableContentUri(table);
        storeAll(cr, uri, items, converter);
    }

    public static final <E> void storeAll(ContentResolver cr, Uri uri,
            Collection<E> items, IContentValuesConverter<E> converter)
    {
        for (E item : items)
        {
            cr.insert(uri, converter.convert(item));
        }
    }
    //                 >>>--- /Static Transaction ---<<<
    
    
    //  --- Tools ---
    private void CheckTable()
    {
        if(mTable == null)
            throw new NullPointerException("Null Table");
    }
    
    // Get string from cursor by columnName
    protected static String gS(Cursor c, String field) 
    {
        return c.getString(c.getColumnIndex(field));
    }

    // Get int from cursor by columnName
    protected static int gI(Cursor c, String field) 
    {
        return c.getInt(c.getColumnIndex(field));
    }
    
    // Get long from cursor by columnName
    protected static long gL(Cursor c, String field) 
    {
        return c.getLong(c.getColumnIndex(field));
    }
    //  --- /Tools ---
}
