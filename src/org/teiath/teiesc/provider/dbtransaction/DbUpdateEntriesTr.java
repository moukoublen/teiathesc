package org.teiath.teiesc.provider.dbtransaction;

import org.teiath.teiesc.dataitems.UpdateEntry;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;
import org.teiath.teiesc.provider.dbmetadata.TableUpdates;

import android.content.ContentValues;
import android.database.Cursor;

public class DbUpdateEntriesTr extends ADbTransaction<UpdateEntry>
{

    public DbUpdateEntriesTr(TableUpdates table)
    {
        super(table);
    }
    public DbUpdateEntriesTr(ADbTableMetaData table)
    {
        super(table);
    }

    @Override
    public ContentValues convert(UpdateEntry item)
    {
        ContentValues ret = new ContentValues();
        
        ret.put( TableUpdates._TABLE_NAME,  item.getTableName()  );
        ret.put( TableUpdates._LAST_UPDATE, item.getLastUpdate() );
        
        return ret;
    }

    @Override
    public UpdateEntry extract(Cursor c)
    {
        UpdateEntry r = new UpdateEntry();
        
        r.setTableName  ( gS(c, TableUpdates._TABLE_NAME)  );
        r.setLastUpdate ( gL(c, TableUpdates._LAST_UPDATE) );
        
        return r;
    }

}
