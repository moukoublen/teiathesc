package org.teiath.teiesc.provider.dbtransaction;

import org.teiath.teiesc.dataitems.Registration;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;
import org.teiath.teiesc.provider.dbmetadata.TableRegistrations;

import android.content.ContentValues;
import android.database.Cursor;

public class DbRegistrationsTr extends ADbTransaction<Registration>
{

    public DbRegistrationsTr(TableRegistrations table)
    {
        super(table);
    }
    public DbRegistrationsTr(ADbTableMetaData table)
    {
        super(table);
    }

    @Override
    public ContentValues convert(Registration item)
    {
        ContentValues ret = new ContentValues();
        
        ret.put( TableRegistrations._LESSON_ID, item.getLessID()  );
        ret.put( TableRegistrations._SECTION,   item.getSection() );
        
        return ret;
    }

    @Override
    public Registration extract(Cursor c)
    {
        Registration ret = new Registration();
        
        ret.setMathID  ( gS(c, TableRegistrations._LESSON_ID) );
        ret.setSection ( gS(c, TableRegistrations._SECTION)   );
        
        return ret;
    }

}
