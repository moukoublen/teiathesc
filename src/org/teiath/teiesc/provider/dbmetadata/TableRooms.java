package org.teiath.teiesc.provider.dbmetadata;

import static org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.SQLiteDataTypes.*;

public class TableRooms extends ADbTableMetaData
{
    public static final String _CODE_NAME = "CodeName";
    public static final String _IS_LAB    = "IsLab";
    
    public TableRooms() {}
    
    @Override
    public String getTableName()
    {
        return "Rooms";
    }
    
    @Override
    protected NameType[] getColumns()
    {
        return array
        (
            nt( _CODE_NAME, F_TXT, F_PK ),
            nt( _IS_LAB,    F_INT       )
        );
    }
}
