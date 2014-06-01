package org.teiath.teiesc.updater;


import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.teiath.teiesc.dataitems.DataItemReference;
import org.teiath.teiesc.provider.dbtransaction.ADbTransaction;
import org.teiath.teiesc.ws.RestWsHandler;
import org.teiath.teiesc.ws.WsTransaction;
import org.teiath.teiesc.ws.JSONExtractor.JSONObjectExtractor;

import android.content.ContentResolver;

public abstract class UpdateTask
{
    public static class LogInException extends Exception
    {
        private static final long serialVersionUID = -3587467253895286607L;
        
        public LogInException(String message)
        {
            super(message);
        }
        public LogInException()
        {
            super();
        }
    }
    
    public abstract void update(RestWsHandler ws, ContentResolver cr)
            throws LogInException, JSONException, IllegalStateException, IOException;
    
    public abstract DataItemReference getRef();
    
    
    public static synchronized <T> void update(RestWsHandler ws, ContentResolver cr, 
            ADbTransaction<T> dbTran, JSONObjectExtractor<T> jsonExtractor)
            throws LogInException, JSONException, IllegalStateException, IOException
    {
        Collection<T> items = WsTransaction.getAllFromWs(ws, jsonExtractor);
        updateDatabase(items, cr, dbTran);
    }
    
    public static synchronized <T> void updateDatabase(Collection<T> items, ContentResolver cr, 
            ADbTransaction<T> dbTran)
            throws IllegalStateException, IOException
    {
        dbTran.deleteAll(cr);
        dbTran.storeAll(cr, items);
    }
    
    
    
    @Override
    public String toString()
    {
        return getRef().toString();
    }
}
