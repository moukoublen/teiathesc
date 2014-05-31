package org.teiath.teiesc.provider;

import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;
import org.teiath.teiesc.provider.dbmetadata.FullTablesPack;

import android.net.Uri;


public class TeiathDbProviderMetaData
{
    public static final String AUTHORITY = "org.teiath.teiesc.provider.TeiathDbProvider";
    
    public static final FullTablesPack TBLS = new FullTablesPack();
    
    public static Uri getTableContentUri(ADbTableMetaData dbData)
    {
        return Uri.parse(String.format("content://%s/%s", 
                AUTHORITY, dbData.getTableName()));
    }
}
