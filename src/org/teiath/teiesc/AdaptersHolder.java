package org.teiath.teiesc;

import android.view.LayoutInflater;

public class AdaptersHolder
{
    public final static int FRAGMENTS_COUNT = 5;
    private LectureLessonAdapter[] mAdapters;
    
    public AdaptersHolder(LayoutInflater inflater)
    {
        this.mAdapters = new LectureLessonAdapter[FRAGMENTS_COUNT];
        
        for(int i = 0; i < FRAGMENTS_COUNT; i++)
        {
            mAdapters[i] = new LectureLessonAdapter(inflater);
        }
    }
    
    public LectureLessonAdapter get(int position)
    {
        if(!inLimits(position))
            return null;
        return mAdapters[position];
    }
    
    private boolean inLimits(int position)
    {
        return (position >= 0) && (position < FRAGMENTS_COUNT);
    }
    
    public void clearAll(boolean notifyChanges)
    {
        for(int i = 0; i < FRAGMENTS_COUNT; i++)
        {
            mAdapters[i].clearData(notifyChanges);
        }
    }
    
    public void notifyChanges()
    {
        for(int i = 0; i < FRAGMENTS_COUNT; i++)
        {
            mAdapters[i].notifyChanges();
        }
    }
}
