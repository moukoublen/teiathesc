package org.teiath.teiesc.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class ViewOptionsTransaction
{
    public static String KEY_SHOW_ONLY_REGISTERED = "Show_Only_Registered";
    public static String KEY_SHOW_LABS            = "Show_Labs";
    public static String KEY_SHOW_THEORIES        = "Show_Theories";
    public static String KEY_SHOW_ALL_ERAS        = "Show_All_Eras";
    public static String KEY_ERAS                 = "Show_Eras";
    public static String KEY_ERA                  = "ERA_";
    
    
    public static void putInBundle(Bundle toPut, ViewOptions opts)
    {
        toPut.putBoolean(KEY_SHOW_ONLY_REGISTERED, opts.getShowOnlyRegistered());
        toPut.putBoolean(KEY_SHOW_LABS, opts.getShowLabs());
        toPut.putBoolean(KEY_SHOW_THEORIES, opts.getShowTheories());
        toPut.putBoolean(KEY_SHOW_ALL_ERAS, opts.getShowAllEras());
        toPut.putBooleanArray(KEY_ERAS, opts.getEras());
    }
    
    public static void getFromBundle(Bundle toGet, ViewOptions toPut)
    {
        if(null == toGet) return;
        toPut.setShowOnlyRegistered(toGet.getBoolean(KEY_SHOW_ONLY_REGISTERED, true));
        toPut.setShowLabs(toGet.getBoolean(KEY_SHOW_LABS, true));
        toPut.setShowTheories(toGet.getBoolean(KEY_SHOW_THEORIES, true));
        toPut.setShowAllEras(toGet.getBoolean(KEY_SHOW_ALL_ERAS, true));
        toPut.setEras(toGet.getBooleanArray(KEY_ERAS));
    }
    
    
    public static void storeInSharedSettings(Context c, ViewOptions o)
    {
        Editor e = SharedSettingsTransaction.e(c);
        
        e.putBoolean(KEY_SHOW_ONLY_REGISTERED, o.getShowOnlyRegistered());
        e.putBoolean(KEY_SHOW_LABS, o.getShowLabs());
        e.putBoolean(KEY_SHOW_THEORIES, o.getShowTheories());
        e.putBoolean(KEY_SHOW_ALL_ERAS, o.getShowAllEras());
        
        for(int i=0; i<ViewOptions.ERAS; i++)
        {
            e.putBoolean(KEY_ERA + i, o.getShowEra(i));
        }
        
        e.commit();
    }
    public static void readFromSharedSettings(Context c, ViewOptions o)
    {
        SharedPreferences s = SharedSettingsTransaction.p(c);
        
        o.setShowOnlyRegistered(s.getBoolean(KEY_SHOW_ONLY_REGISTERED, true));
        o.setShowLabs(s.getBoolean(KEY_SHOW_LABS, true));
        o.setShowTheories(s.getBoolean(KEY_SHOW_THEORIES, true));
        o.setShowAllEras(s.getBoolean(KEY_SHOW_ALL_ERAS, true));
        
        for(int i=0; i<ViewOptions.ERAS; i++)
        {
            o.setEra(i, s.getBoolean(KEY_ERA + i, true));
        }
    }
}
