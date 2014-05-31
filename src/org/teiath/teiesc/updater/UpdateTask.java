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
    
    public void update(String url, ContentResolver cr)
            throws LogInException, JSONException, IllegalStateException, IOException
    {
        update(new RestWsHandler(url), cr);
    }

    public abstract void update(RestWsHandler ws, ContentResolver cr)
            throws LogInException, JSONException, IllegalStateException, IOException;
    
    
    public static synchronized <T> void update(RestWsHandler ws, ContentResolver cr, ADbTransaction<T> dbTran, JSONObjectExtractor<T> jsonExtractor)
            throws LogInException, JSONException, IllegalStateException, IOException
    {
        Collection<T> items = WsTransaction.getAllFromWs(ws, jsonExtractor);
        update(items, cr, dbTran);
    }
    
    public static synchronized <T> void update(Collection<T> items, ContentResolver cr, ADbTransaction<T> dbTran)
            throws IllegalStateException, IOException
    {
        dbTran.deleteAll(cr);
        dbTran.storeAll(cr, items);
    }
    
    public abstract DataItemReference getRef();
    
    @Override
    public String toString()
    {
        return getRef().toString();
    }
}
