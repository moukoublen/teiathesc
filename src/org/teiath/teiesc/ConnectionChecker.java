package org.teiath.teiesc;

import org.teiath.teiesc.options.SharedSettingsTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectionChecker
{
    private ConnectionChecker() {}
    
    public static boolean isConnected(Context c)
    {
        NetworkInfo info = getNetworkInfo(c);
        if(info == null) return false;
        return isConnected(info) && (isWiFi(info) || !SharedSettingsTransaction.getOnlyWiFi(c));
    }
    
    private static boolean isConnected(NetworkInfo info)
    {
        return info.isConnectedOrConnecting();
    }
    private static boolean isWiFi(NetworkInfo info)
    {
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }
    
    private static NetworkInfo getNetworkInfo(Context c)
    {
        ConnectivityManager manager = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }
}
