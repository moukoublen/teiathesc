package org.teiath.teiesc.provider.dbmetadata;

import static org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.SQLiteDataTypes.*;

public class TableUpdates extends ADbTableMetaData
{
    public static final String _TABLE_NAME  = "TableName";
    public static final String _LAST_UPDATE = "LastUpdate";
    
    public TableUpdates() {}

    @Override
    public String getTableName()
    {
        return "Updates";
    }
    
    @Override
    protected NameType[] getColumns()
    {
        return array
        (
            nt( _TABLE_NAME,  F_TXT, F_PK ),
            nt( _LAST_UPDATE, F_LNG       )
        );
    }
}
