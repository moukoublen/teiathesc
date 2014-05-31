package org.teiath.teiesc.ws;

import java.io.IOException;
import java.util.HashMap;

import org.teiath.teiesc.R;
import org.teiath.teiesc.dataitems.IItemRefDictionary;
import org.teiath.teiesc.dataitems.DataItemReference;
import org.teiath.teiesc.ws.RestWsHandler.HttpMethod;

import static org.teiath.teiesc.dataitems.DataItemReference.*;
import android.content.Context;

public class RestWsHandlerFactory implements IItemRefDictionary<RestWsHandler>
{
    private HashMap<DataItemReference, String> mp = 
            new HashMap<DataItemReference, String>(5);

    public RestWsHandlerFactory(Context c)
    {
        mp.put(LEC, c.getString(R.string.ws_lectures));
        mp.put(LES, c.getString(R.string.ws_lessons));
        mp.put(REG, c.getString(R.string.ws_registrations));
        mp.put(ROO, c.getString(R.string.ws_rooms));
        mp.put(UPD, c.getString(R.string.ws_updates));
    }
    
    public String getUrl(DataItemReference n)
    {
        if(mp.containsKey(n))
        {
            return mp.get(n);
        }
        return "";
    }

    @Override
    public RestWsHandler get(DataItemReference n)
    {
        if (!this.mp.containsKey(n)) return null;
        RestWsHandler ret;
        try
        {
            ret = new RestWsHandler(this.mp.get(n), n == REG ? HttpMethod.POST
                    : HttpMethod.GET);
        }
        catch (IllegalStateException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        return ret;
    }
}
