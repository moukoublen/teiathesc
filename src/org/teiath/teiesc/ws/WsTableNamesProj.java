package org.teiath.teiesc.ws;

import java.util.HashMap;

import org.teiath.teiesc.dataitems.IItemRefReverseDictionary;
import org.teiath.teiesc.dataitems.DataItemReference;

import static org.teiath.teiesc.dataitems.DataItemReference.*;

public final class WsTableNamesProj implements IItemRefReverseDictionary<String>
{
    // Το όνομα που αποθηκεύονται τα tables στον πινακα των updates του server και local.
    public static final String WS_LECTURES = "Paradwseis";
    public static final String WS_LESSONS  = "Mathimata";
    public static final String WS_REGIST   = "Dilwseis";
    public static final String WS_ROOMS    = "Aithouses";
    public static final String WS_UPDATE   = "updates";
    
    private HashMap<String, DataItemReference> mWsRefMap;
    
    public WsTableNamesProj()
    {
        mWsRefMap = new HashMap<String, DataItemReference>(5);
        
        mWsRefMap.put(WS_LECTURES, LEC);
        mWsRefMap.put(WS_LESSONS,  LES);
        mWsRefMap.put(WS_REGIST,   REG);
        mWsRefMap.put(WS_ROOMS,    ROO);
        mWsRefMap.put(WS_UPDATE,   UPD);
    }
    
    public boolean isNameValid(String wsTableName)
    {
        return mWsRefMap.containsKey(wsTableName);
    }

    @Override
    public DataItemReference get(String item)
    {
        if(!isNameValid(item)) return null;
        return mWsRefMap.get(item);
    }

    @Override
    public boolean isValid(String item)
    {
        return mWsRefMap.containsKey(item);
    }
}
