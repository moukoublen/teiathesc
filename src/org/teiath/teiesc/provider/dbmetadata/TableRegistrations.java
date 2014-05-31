package org.teiath.teiesc.provider.dbmetadata;

import static org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData.SQLiteDataTypes.*;

public class TableRegistrations extends ADbTableMetaData
{
    public static final String _LESSON_ID = "LesID";
    public static final String _SECTION   = "Section";
    
    public TableRegistrations() {}
    
    @Override
    public String getTableName()
    {
        return "Registrations";
    }
    
    @Override
    protected NameType[] getColumns()
    {
        return array
        (
            nt( _LESSON_ID, F_TXT ),
            nt( _SECTION,   F_TXT )
        );
    }
}
