package org.teiath.teiesc.provider.dbmetadata;

import static org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.SQLiteDataTypes.*;


public class TableLectures extends ADbTableMetaData
{
    public static final String _LESSON_ID  = "LesID";
    public static final String _ROOM_ID    = "RoomID";
    public static final String _DAY        = "Day";
    public static final String _DURATION   = "Dur";
    public static final String _TIME_START = "TimeStart";
    public static final String _SECTION    = "Section";
    
    public TableLectures() { }
    
    @Override
    public String getTableName()
    {
        return "Lectures";
    }
    
    @Override
    protected NameType[] getColumns()
    {
        return array
        (
            nt( _LESSON_ID,  F_TXT ),
            nt( _ROOM_ID,    F_TXT ),
            nt( _DAY,        F_INT ),
            nt( _DURATION,   F_INT ),
            nt( _TIME_START, F_INT ),
            nt( _SECTION,    F_TXT )
        );
    }
}
