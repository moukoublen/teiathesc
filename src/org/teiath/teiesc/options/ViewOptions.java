package org.teiath.teiesc.options;

import java.util.Arrays;

public class ViewOptions
{   
    public static final int ERAS = 7;
    private boolean   mShowOnlyRegistered;
    private boolean   mShowLabs;
    private boolean   mShowTheories;
    private boolean   mShowAllEras;
    private boolean[] mEras;
    
    public ViewOptions()
    {
        mEras = new boolean[ERAS];
        mShowOnlyRegistered = mShowLabs = 
        mShowTheories = mShowAllEras = true;
        setAllEras(true);
    }

    public void setShowOnlyRegistered(boolean showOnlyRegistered)
    {
        this.mShowOnlyRegistered = showOnlyRegistered;
    }
    public void setShowTheories(boolean showTheories)
    {
        this.mShowTheories = showTheories;
    }
    public void setShowLabs(boolean showLabs)
    {
        this.mShowLabs = showLabs;
    }
    public void setShowAllEras(boolean showAllEras)
    {
        this.mShowAllEras = showAllEras;
    }
    
    public boolean getShowTheories()
    {
        return mShowTheories;
    }
    public boolean getShowLabs()
    {
        return mShowLabs;
    }
    public boolean getShowOnlyRegistered()
    {
        return mShowOnlyRegistered;
    }
    public boolean getShowAllEras()
    {
        return this.mShowAllEras;
    }

    public boolean getEra(int era)
    {
        if(era >= 0 && era < ERAS)
        {
            return mEras[era];
        }
        return false;
    }
    
    public boolean[] getEras()
    {
        return Arrays.copyOf(this.mEras, ERAS);
    }
    
    public void setEra(int era, boolean showEra)
    {
        if(era >= 0 && era < 7)
        {
            mEras[era] = showEra;
        }
    }
    
    public void setAllEras(boolean show)
    {
        for(int i=0; i<ERAS; i++)
        {
            this.mEras[i] = show;
        }
    }
    
    public void setEras(boolean[] eras)
    {
        System.arraycopy(eras, 0, this.mEras, 0, ERAS);
    }
    
    public void copy(ViewOptions opt)
    {
        this.mShowLabs = opt.mShowLabs;
        this.mShowOnlyRegistered = opt.mShowOnlyRegistered;
        this.mShowTheories = opt.mShowTheories;
        this.mShowAllEras = opt.mShowAllEras;
        System.arraycopy(opt.mEras, 0, mEras, 0, mEras.length);
    }
}
