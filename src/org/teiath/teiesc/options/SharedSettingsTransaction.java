package org.teiath.teiesc.options;

import android.content.Context;
import android.content.SharedPreferences;


public final class SharedSettingsTransaction
{
    public static final String PREFS_NAME     = "TeiEscPrefs";
    
    public static final String PREF_USERNAME  = "username";
    public static final String PREF_PASSWORD  = "password";
    public static final String PREF_ONLY_WIFI = "onlywifi";
    
    public static String TICKET = "";
    
    private SharedSettingsTransaction() { }
    
    public static void setUsernameAndPassword(Context c, String usernameValue, String passwordValue)
    {
        e(c).putString(PREF_USERNAME, usernameValue)
        .putString(PREF_PASSWORD, passwordValue).commit();
    }
    public static void setUsername(Context c, String usernameValue)
    {
        e(c).putString(PREF_USERNAME, usernameValue).commit();
    }
    public static void setPassword(Context c, String passwordValue)
    {
        e(c).putString(PREF_PASSWORD, passwordValue).commit();
    }
    public static void setOnlyWifi(Context c, boolean value)
    {
        e(c).putBoolean(PREF_ONLY_WIFI, value);
    }
    
    public static String getUsername(Context c)
    {
        return p(c).getString(PREF_USERNAME, "");
    }
    public static String getPassword(Context c)
    {
        return p(c).getString(PREF_PASSWORD, "");
    }
    public static boolean getOnlyWiFi(Context c)
    {
        return p(c).getBoolean(PREF_ONLY_WIFI, false);
    }

    public static void reset(Context c)
    {
        setOnlyWifi(c, false);
        setUsername(c, "");
        setPassword(c, "");
    }
    
    public static boolean hasUsernameAndPassword(Context c)
    {
        SharedPreferences sh = p(c);
        String un = sh.getString(PREF_USERNAME, "");
        String pw = sh.getString(PREF_PASSWORD, "");
        return !("".equals(un) && "".equals(pw));
    }
    
    public static void deleteAllSettings(Context c)
    {
        e(c).clear().commit();
    }
    
    
    public static SharedPreferences p(Context c)
    {
        return c.getSharedPreferences(PREFS_NAME, 0);
    }
    public static SharedPreferences.Editor e(Context c)
    {
        return p(c).edit();
    }
}
