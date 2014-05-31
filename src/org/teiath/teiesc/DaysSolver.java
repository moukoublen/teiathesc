package org.teiath.teiesc;

import android.content.Context;

public final class DaysSolver
{
    Context mContex;
    public DaysSolver(Context contx)
    {
        this.mContex = contx;
    }
    public String getDay(int num)
    {
        switch (num % 7)
        {// this is the standard
        case 1: //Sunday
            return this.mContex.getString(R.string.day_sun);
        case 2:
            return this.mContex.getString(R.string.day_mon);
        case 3:
            return this.mContex.getString(R.string.day_tue);
        case 4:
            return this.mContex.getString(R.string.day_wed);
        case 5:
            return this.mContex.getString(R.string.day_thu);
        case 6:
            return this.mContex.getString(R.string.day_fri);
        case 7:
            return this.mContex.getString(R.string.day_sat);
        default:
            return "";
        }
    }
}
