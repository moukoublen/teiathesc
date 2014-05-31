package org.teiath.teiesc.provider.dbmetadata;

import static org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.SQLiteDataTypes.*;

import android.provider.BaseColumns;

public class TableLessons extends ADbTableMetaData implements BaseColumns
{
    public static final String _TITLE = "Title";

    public TableLessons() { }
    
    @Override
    public String getTableName()
    {
        return "LESSONS";
    }
    
    @Override
    protected NameType[] getColumns()
    {
        return array
        (
            nt( _ID,    F_TXT, F_PK ),
            nt( _TITLE, F_TXT       )
        );
    }
    
}
