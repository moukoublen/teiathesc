package org.teiath.teiesc.provider.dbtransaction;

import android.content.ContentValues;
import android.database.Cursor;

import org.teiath.teiesc.dataitems.Room;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;
import org.teiath.teiesc.provider.dbmetadata.TableRooms;

public class DbRoomsTr extends ADbTransaction<Room>
{
    public static int IT_IS_LAB = 1;
    
    public DbRoomsTr(TableRooms table)
    {
        super(table);
    }
    public DbRoomsTr(ADbTableMetaData table)
    {
        super(table);
    }

    @Override
    public ContentValues convert(Room item)
    {
        ContentValues ret = new ContentValues();
        
        ret.put( TableRooms._CODE_NAME, item.getCodeName() );
        ret.put( TableRooms._IS_LAB,    item.getIsIsLab()  );
        
        return ret;
    }

    @Override
    public Room extract(Cursor c)
    {
        Room r = new Room();
        
        r.setCodeName ( gS(c, TableRooms._CODE_NAME)           );
        r.setIsLab    ( gI(c, TableRooms._IS_LAB) == IT_IS_LAB );
        
        return r;
    }

}
