package org.teiath.teiesc.updater;

import static org.teiath.teiesc.dataitems.DataItemReference.UPD;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.teiath.teiesc.dataitems.IItemRefDictionary;
import org.teiath.teiesc.dataitems.IItemRefReverseDictionary;
import org.teiath.teiesc.dataitems.DataItemReference;
import org.teiath.teiesc.dataitems.UpdateEntry;
import org.teiath.teiesc.provider.dbtransaction.FullDbTranPack;
import org.teiath.teiesc.updater.UpdateTask.LogInException;
import org.teiath.teiesc.utils.Selector;
import org.teiath.teiesc.ws.JSONExtractor;
import org.teiath.teiesc.ws.RestWsHandler;
import org.teiath.teiesc.ws.WsTransaction;

import android.content.ContentResolver;
import android.os.AsyncTask;

public class Updater
{
    public static interface OnUpdateListener 
    {
        public void onJobFinished(UpdateResult results); 
    }
    public static interface OnUpdateCheckListener
    {
        public void onCheckFinished(Collection<DataItemReference> tables);
    }
    public static class UpdateResult extends AsyncTaskResult<Boolean>
    {
        public UpdateResult(Exception exception)
        {
            super(exception);
        }
        public UpdateResult(Boolean result)
        {
            super(result);
        }
    }
    
    private class AsyncUpdateOnly extends AsyncTask<DataItemReference, Object, UpdateResult>  
    {
        @Override
        protected UpdateResult doInBackground(DataItemReference... params)
        {
            asyncJustUpdate = null;
            boolean ret = true;
            try
            {
                updateOnlyJob(params);
            }
            catch (LogInException e)
            { return new UpdateResult(e); }
            catch (IllegalStateException e)
            { return new UpdateResult(e); }
            catch (JSONException e)
            { return new UpdateResult(e); }
            catch (IOException e)
            { return new UpdateResult(e); }
            
            return new UpdateResult(ret);
        }
        @Override
        protected void onPostExecute(UpdateResult result)
        {
            if(null != onUpdateFinish)
            {
                onUpdateFinish.onJobFinished(result);
            }
        }
    }
    private class AsyncUpdateCheck extends AsyncTask<Void, Object, Collection<DataItemReference>>
    {
        @Override
        protected Collection<DataItemReference> doInBackground(Void... params)
        {
            asyncUpdateCheck = null;
            Collection<DataItemReference> ret = null;
            try
            {
                ret = updateCheck();
            }
            catch (JSONException e)
            { }
            catch (IOException e)
            { }
            
            return ret;
        }
        @Override
        protected void onPostExecute(Collection<DataItemReference> result)
        {
            if(null != onUpdateCheck)
            {
                onUpdateCheck.onCheckFinished(result);
            }
        }
        
    }
    
    
    private OnUpdateListener      onUpdateFinish;
    private OnUpdateCheckListener onUpdateCheck;
    
    private AsyncUpdateCheck asyncUpdateCheck = null;
    private AsyncUpdateOnly  asyncJustUpdate  = null;
    
    private WeakReference<ContentResolver> wcr;
    private FullDbTranPack dbTranPack;
    
    private IItemRefReverseDictionary<String>    wsObjectNamesResolve;
    private IItemRefDictionary<RestWsHandler>    conGet;
    //private IItemRefsDictionary<ADbTableMetaData> tables;
    private UpdatesTasks                          tasks;
    
    // Login data
    private String mUsername;
    private String mPassword;
    
    public Updater(ContentResolver cr, FullDbTranPack trPack, 
            IItemRefDictionary<RestWsHandler> conGet, 
            IItemRefReverseDictionary<String> wsResolve,
            String username, String password)
    {
        this.wcr = new WeakReference<ContentResolver>(cr);
        
        this.dbTranPack           = trPack;
        this.wsObjectNamesResolve = wsResolve;
        this.conGet               = conGet;
        //this.tables               = dbTranPack.getTables();
        
        this.onUpdateFinish = null;
        this.onUpdateCheck  = null;
        this.mUsername      = username;
        this.mPassword      = password;
        this.tasks          = new UpdatesTasks(dbTranPack, mUsername, mPassword);
    }
    
    public boolean hasOnUpdateFinishListenerSetted()
    {
        return this.onUpdateFinish != null;
    }
    public boolean hasOnUpdateCheckListenerSetted()
    {
        return this.onUpdateCheck != null;
    }
    public void setOnUpdateFinishListener(OnUpdateListener onUpdateFinish)
    {
        this.onUpdateFinish = onUpdateFinish;
    }
    public void setOnUpdateCheckListener(OnUpdateCheckListener onUpdateCheck)
    {
        this.onUpdateCheck = onUpdateCheck;
    }

    public void startUpdate(DataItemReference... itemsToUpdate)
    {
        if (this.asyncJustUpdate != null)
            return;
        this.asyncJustUpdate = new AsyncUpdateOnly();
        this.asyncJustUpdate.execute(itemsToUpdate);
    }
    
    public void checkForUpdate()
    {
        if(this.asyncUpdateCheck != null)
            return;
        this.asyncUpdateCheck = new AsyncUpdateCheck();
        this.asyncUpdateCheck.execute((Void) null);
    }
    
    

    //         -------- UPDATES FUNCTIONS --------
    //              Τρέχουν μέσα στο async task
    
    // Επιστρέφει τα τοπικά ονόματα των πινάκων που θέλουν update.
    private Collection<DataItemReference> updateCheck() 
            throws JSONException, IOException 
    {
        RestWsHandler uws = this.conGet.get(UPD); // Connection fro Update ws
        
        // Τα UpdateEntry από το web service
        Collection<UpdateEntry> wsUpdates = 
                WsTransaction.getAllFromWs(uws, JSONExtractor.UPDATE_JSON_EXTRACTOR);

        // Τα UpdateEntry από τον τοπικό πινακα
        ContentResolver c = gcr();
        List<UpdateEntry> localUpdates = new ArrayList<UpdateEntry>();
        dbTranPack.getUpdates().getAll(c, localUpdates);
        c = null;

        // Γίνεται η σύγκριση και επιστρέφονται (ItemRef) μονο αυτά που χρειάζονται update
        Collection<DataItemReference> toUpd;
        toUpd = getLocalTableNamesToUpdate(wsUpdates, localUpdates);

        return toUpd;
    }
    
    private void updateOnlyJob(DataItemReference[] items)
            throws LogInException, JSONException, IllegalStateException, IOException
    {
        if(items.length == 0) return;
        Collection<UpdateTask> tasksToExe = tasks.getUpdateTasks(items);
        
        // Αν υπάρχουν updates που πρέπει να γίνουν τότε 
        // πρέπει να αναβαθμιστεί και ο ίδιος ο πινακας των updates 
        // (αν δεν έχει ήδη μπει στην προς update λίστα)
        if(!tasksToExe.contains(tasks.get(UPD))) 
        {
            tasksToExe.add(tasks.get(UPD));
        }
        
        ContentResolver c = gcr();
        for (UpdateTask task : tasksToExe)
        {
            RestWsHandler ws = this.conGet.get(task.getRef());
            if(ws == null)
                continue;
            task.update(ws, c);
        }
        c = null;
    }
    //         --------/UPDATES FUNCTIONS --------

    private Collection<DataItemReference> 
    getLocalTableNamesToUpdate(Collection<UpdateEntry> remote, Collection<UpdateEntry> local)
    {
        ArrayList<DataItemReference> ret = new ArrayList<DataItemReference>();

        for (UpdateEntry wse : remote)
        {
            String remoteTableName = wse.getTableName();
            
            if(!this.wsObjectNamesResolve.isValid(remoteTableName))
            {
                continue;
            }

            UpdateEntry localUpdate = get(local, wse);

            if (localUpdate != null)
            {
                if (localUpdate.getLastUpdate() == wse.getLastUpdate())
                {
                    continue;
                }
            }
            
            DataItemReference rf = wsObjectNamesResolve.get(remoteTableName);
            ret.add(rf);
        }

        return ret;
    }
    
    private static UpdateEntry 
    get(Collection<UpdateEntry> lst, final UpdateEntry selected)
    {
        Selector.Condition<UpdateEntry> cond = new Selector.Condition<UpdateEntry>()
        {
            @Override
            public boolean isAccepted(UpdateEntry item)
            {
                String tb = selected.getTableName();
                return item.getTableName().equals(tb);
            }
        };
        
        return Selector.getFirst(lst, cond);
    }
    private ContentResolver gcr()
    {
        ContentResolver ret = wcr.get();
        if(ret == null)
            throw new NullPointerException("Null ContentResolver");
        return ret;
    }
}
