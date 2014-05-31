package org.teiath.teiesc.provider.dbtransaction;

import org.teiath.teiesc.dataitems.Lecture;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;
import org.teiath.teiesc.provider.dbmetadata.TableLectures;

import android.content.ContentValues;
import android.database.Cursor;

public class DbLecturesTr extends ADbTransaction<Lecture>
{
    public DbLecturesTr(TableLectures table)
    {
        super(table);
    }
    public DbLecturesTr(ADbTableMetaData table)
    {
        super(table);
    }

    @Override
    public ContentValues convert(Lecture item)
    {
        ContentValues ret = new ContentValues();
        
        ret.put( TableLectures._DAY,        item.getDay()       );
        ret.put( TableLectures._LESSON_ID,  item.getLessId()    );
        ret.put( TableLectures._ROOM_ID,    item.getRoomId()    );
        ret.put( TableLectures._SECTION,    item.getSection()   );
        // time
        ret.put( TableLectures._TIME_START, item.getTimeBegin() );
        ret.put( TableLectures._DURATION,   item.getDur()       );
        
        return ret;
    }

    @Override
    public Lecture extract(Cursor c)
    {
        Lecture ret = new Lecture();

        ret.setDay       ( gI(c, TableLectures._DAY)        );
        ret.setDur       ( gI(c, TableLectures._DURATION)   );
        ret.setLessId    ( gS(c, TableLectures._LESSON_ID)  );
        ret.setRoomId    ( gS(c, TableLectures._ROOM_ID)    );
        ret.setSection   ( gS(c, TableLectures._SECTION)    );
        ret.setTimeBegin ( gI(c, TableLectures._TIME_START) );

        return ret;
    }

}
