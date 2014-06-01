package org.teiath.teiesc.updater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.teiath.teiesc.dataitems.IItemRefDictionary;
import org.teiath.teiesc.dataitems.DataItemReference;
import org.teiath.teiesc.dataitems.Registration;
import org.teiath.teiesc.provider.dbtransaction.FullDbTranPack;
import org.teiath.teiesc.ws.JSONExtractor;
import org.teiath.teiesc.ws.RestWsHandler;
import org.teiath.teiesc.ws.WsTransaction;

import android.content.ContentResolver;
import static org.teiath.teiesc.dataitems.DataItemReference.*;

public final class UpdatesTasks implements IItemRefDictionary<UpdateTask>
{
    private FullDbTranPack trPack;
    
    private UpdateTask lecture;
    private UpdateTask lessons;
    private UpdateTask rooms;
    private UpdateTask regist;
    private UpdateTask updates;
    
    private String mUsername;
    private String mPassword;
    public UpdatesTasks(FullDbTranPack dbTr, String username, String password)
    {
        this.trPack    = dbTr;
        this.mUsername = username;
        this.mPassword = password;
        initTasks();
    }
    
    @Override
    public final UpdateTask get(DataItemReference table)
    {
        switch (table)
        {
        case LEC:
            return lecture;
        case LES:
            return lessons;
        case REG:
            return regist;
        case ROO:
            return rooms;
        case UPD:
            return updates;
        default:
            return null;
        }
    }
    
    private void initTasks()
    {
        this.lecture = new UpdateTask()
        {
            @Override
            public void update(RestWsHandler ws, ContentResolver cr)
                    throws LogInException, JSONException, IllegalStateException, IOException
            {
                UpdateTask.update(ws, cr, trPack.getLectures(),
                        JSONExtractor.LECTURE_JSON_EXTRACTOR);
            }
            @Override
            public DataItemReference getRef()
            {
                return LEC;
            }
        };
        
        this.lessons = new UpdateTask()
        {
            @Override
            public void update(RestWsHandler ws, ContentResolver cr)
                    throws LogInException, JSONException, IllegalStateException, IOException
            {
                UpdateTask.update(ws, cr, trPack.getLessons(),
                        JSONExtractor.LESSON_JSON_EXTRACTOR);
            }
            @Override
            public DataItemReference getRef()
            {
                return LES;
            }
        };
        
        this.regist = new UpdateTask()
        {
            @Override
            public void update(RestWsHandler ws, ContentResolver cr)
                    throws LogInException, JSONException, IllegalStateException, IOException
            {
                JSONObject dataOut =  WsTransaction.getLoginJSONObject(mUsername, mPassword);
                ws.setPostData(dataOut);
                
                String dataStr = ws.getRawData();
                
                // έλεγχος an το login έγινε εντάξει 
                if(dataStr.contains("\"login\":\"failed\"")){
                    throw new LogInException();
                }
                
                Collection<Registration> items = JSONExtractor.getAllFromStrData(dataStr, JSONExtractor.REGISTRATION_JSON_EXTRACTOR);
                
                UpdateTask.updateDatabase(items, cr, trPack.getRegistrations());
            }
            @Override
            public DataItemReference getRef()
            {
                return REG;
            }
        };
        
        this.rooms = new UpdateTask()
        {
            @Override
            public void update(RestWsHandler ws, ContentResolver cr)
                    throws LogInException, JSONException, IllegalStateException, IOException
            {
                UpdateTask.update(ws, cr, trPack.getRooms(),
                        JSONExtractor.ROOM_JSON_EXTRACTOR);
            }
            @Override
            public DataItemReference getRef()
            {
                return ROO;
            }
        };
        
        this.updates = new UpdateTask()
        {
            @Override
            public void update(RestWsHandler ws, ContentResolver cr)
                    throws LogInException, JSONException, IllegalStateException, IOException
            {
                UpdateTask.update(ws, cr, trPack.getUpdates(),
                        JSONExtractor.UPDATE_JSON_EXTRACTOR);
            }
            @Override
            public DataItemReference getRef()
            {
                return UPD;
            }
        };
    }
    
    public List<UpdateTask> getUpdateTasks(Collection<DataItemReference> cl)
    {
        ArrayList<UpdateTask> ret = new ArrayList<UpdateTask>();
        
        for(DataItemReference item : cl)
        {
            ret.add(this.get(item));
        }
        
        return ret;
    }
    public List<UpdateTask> getUpdateTasks(DataItemReference[] cl)
    {
        ArrayList<UpdateTask> ret = new ArrayList<UpdateTask>();
        
        for(DataItemReference item : cl)
        {
            ret.add(this.get(item));
        }
        
        return ret;
    }
}
