package org.teiath.teiesc.provider.dbtransaction;

import org.teiath.teiesc.dataitems.Lesson;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;
import org.teiath.teiesc.provider.dbmetadata.TableLessons;

import android.content.ContentValues;
import android.database.Cursor;

public class DbLessonsTr extends ADbTransaction<Lesson>
{

    public DbLessonsTr(TableLessons table)
    {
        super(table);
    }
    public DbLessonsTr(ADbTableMetaData table)
    {
        super(table);
    }

    @Override
    public ContentValues convert(Lesson item)
    {
        ContentValues ret = new ContentValues();
        
        ret.put( TableLessons._ID,    item.getID()    );
        ret.put( TableLessons._TITLE, item.getTitle() );
        
        return ret;
    }

    @Override
    public Lesson extract(Cursor c)
    {
        Lesson ret = new Lesson();
        
        ret.setID    ( gS(c, TableLessons._ID)    );
        ret.setTitle ( gS(c, TableLessons._TITLE) );
        
        return ret;
    }

}
