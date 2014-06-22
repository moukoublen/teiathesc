package org.teiath.teiesc.provider;

import java.util.Locale;

import org.teiath.teiesc.dataitems.DataItemReference;
import org.teiath.teiesc.provider.dbmetadata.*;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.QueryTypes;
import org.teiath.teiesc.utils.Selector;

import static org.teiath.teiesc.dataitems.DataItemReference.*;
import static org.teiath.teiesc.provider.TeiathDbProviderMetaData.*;
import static org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.QueryTypes.CREATE;
import static org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.QueryTypes.DROP;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

public class TeiathDbProvider extends ContentProvider
{
    private static final String LOGTAG_DB = "Db_Log";
    private static final void logd(String message)
    {
        Log.d(LOGTAG_DB, message);
    }
    
    public static class DatabaseHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME     = "teiathsched.db";
        private static final int    DATABASE_VESRSION = 1;

        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null,
                    DATABASE_VESRSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            logd("DatabaseHelper onCreate()");
            executeForAllTables(db, CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            logd(String.format(Locale.US, 
                    "DatabaseHelper onUpdate() version %d to %d", 
                    oldVersion, newVersion));
            
            executeForAllTables(db, DROP);
            onCreate(db);
        }

        private void executeForAllTables(SQLiteDatabase db, QueryTypes take)
        {
            for (ADbTableMetaData tb : TBLS.getAll())
            {
                db.execSQL(tb.getQuery(take));
            }
        }
    }
    
    private DatabaseHelper dbHelper;
    
    
    private final String ss = "/#";
    private final String sc = "/*";
    private final int IN_LECTURES      = 1;
    private final int IN_LESSONS       = 2;
    private final int IN_LESSONS_ID    = 3;
    private final int IN_REGISTRATIONS = 4;
    private final int IN_ROOMS         = 5;
    private final int IN_UPDATES       = 6;
    private final int IN_UPDATES_TID   = 7;
    private final UriMatcher sUriMatcher;
    private final SparseArray<DataItemReference> uMap;
    
    public TeiathDbProvider()
    {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uMap = new SparseArray<DataItemReference>(7);
        
        addUri(TBLS.get(LEC),     IN_LECTURES);
        addUri(TBLS.get(LES),     IN_LESSONS);
        addUri(TBLS.get(LES), sc, IN_LESSONS_ID);
        addUri(TBLS.get(REG),     IN_REGISTRATIONS);
        addUri(TBLS.get(ROO),     IN_ROOMS);
        addUri(TBLS.get(UPD),     IN_UPDATES);
        addUri(TBLS.get(UPD), ss, IN_UPDATES_TID);
        
        uMap.append(IN_LECTURES,      LEC);
        uMap.append(IN_LESSONS,       LES);
        uMap.append(IN_LESSONS_ID,    LES);
        uMap.append(IN_REGISTRATIONS, REG);
        uMap.append(IN_ROOMS,         ROO);
        uMap.append(IN_UPDATES,       UPD);
        uMap.append(IN_UPDATES_TID,   UPD);
    }
    private void addUri(ADbTableMetaData table, String add, final int a)
    {
        sUriMatcher.addURI(AUTHORITY, table.getTableName() + add, a);
    }
    private void addUri(ADbTableMetaData table, final int a)
    {
        sUriMatcher.addURI(AUTHORITY, table.getTableName(), a);
    }

    

    @Override
    public boolean onCreate()
    {
        logd("TeiathDbProvider onCreate()");
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        
        qb.setTables(getTableName(uri));
        
        switch (sUriMatcher.match(uri))
        {
        case IN_LESSONS_ID:
            qb.appendWhere(TableLessons._ID + 
                    "='" + uri.getPathSegments().get(1) + "'");
            break;
        case IN_UPDATES_TID:
            qb.appendWhere(TableUpdates._TABLE_NAME + 
                    "='" + uri.getPathSegments().get(1) + "'");
            break;
        }
        
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor c = qb.query(db, projection, selection, 
                selectionArgs, null, null, sortOrder);
        
        // Tell the cursor what uri to watch, 
        // so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        //db.close();
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        if( Selector.isNotIn(sUriMatcher.match(uri), 
                IN_LECTURES, 
                IN_LESSONS, 
                IN_REGISTRATIONS, 
                IN_ROOMS, 
                IN_UPDATES) )
        {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        String tableName  = getTableName(uri);
        String columnHack = getNullColumnHack(uri); //an empty row can't be inserted.
        
        long rowId = db.insert(tableName, columnHack, values);
        
        if(rowId < 0)
            throw new SQLException("Failed to insert row into " + uri);
        
        Uri ret = ContentUris.withAppendedId(getPlainContentUri(uri), rowId);
        
        // de thymamai giati, alla to lene.
        getContext().getContentResolver().notifyChange(ret, null);
        //db.close();
        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        String table = getTableName(uri);
        
        int c = db.delete(table, selection, selectionArgs);
        //db.close();
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs)
    {
        return -1;
    }
    
    @Override
    public String getType(Uri uri)
    {
        return "";
    }
    
    
    private String getTableName(Uri uri)
    {
        int key = sUriMatcher.match(uri);
        return TBLS.getTableName(uMap.get(key));
    }
    private Uri getPlainContentUri(Uri uri)
    {
        int key = sUriMatcher.match(uri);
        return TeiathDbProviderMetaData.getTableContentUri(TBLS.get(uMap.get(key)));
        //return TBLS.get(uMap.get(key)).getContentUri();
    }
    private String getNullColumnHack(Uri uri)
    {
        String ret;
        switch (sUriMatcher.match(uri))
        {
        case IN_LECTURES:
            ret = TableLectures._LESSON_ID;
            break;
        case IN_LESSONS:
            ret = TableLessons._ID;
            break;
        case IN_REGISTRATIONS:
            ret = TableRegistrations._LESSON_ID;
            break;
        case IN_ROOMS:
            ret = TableRooms._CODE_NAME;
            break;
        case IN_UPDATES:
            ret = TableUpdates._TABLE_NAME;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return ret;
    }
}
