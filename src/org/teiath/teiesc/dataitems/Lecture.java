package org.teiath.teiesc.dataitems;


public class Lecture implements IDataItem, Comparable<Lecture>
{
    private String mLessId;
    private String mRoomId;
    private String mSection;
    private int    mDay;
    
    //UTC timestamp
    private long   mTimeBegin;
    private long   mDur;

    public String getRoomId()
    {
        return mRoomId;
    }

    public long getDur()
    {
        return mDur;
    }

    public String getLessId()
    {
        return mLessId;
    }

    public int getDay()
    {
        return mDay;
    }

    public String getSection()
    {
        return mSection;
    }

    public long getTimeBegin()
    {
        return mTimeBegin;
    }

    public void setRoomId(String roomId)
    {
        this.mRoomId = roomId;
    }

    public void setDur(long time)
    {
        this.mDur = time;
    }
    
    public void setDur(String time)
    {
        this.mDur = getFromTime(time);
    }

    public void setLessId(String lessId)
    {
        this.mLessId = lessId;
    }

    public void setDay(int day)
    {
        this.mDay = day;
    }

    public void setSection(String section)
    {
        this.mSection = section;
    }

    public void setTimeBegin(long timeBegin)
    {
        this.mTimeBegin = timeBegin;
    }
    
    public void setTimeBegin(String time)
    {
        this.mTimeBegin = getFromTime(time);
    }
    
    
    private static long getFromTime(String timeString)
    {
        return TimeUtil.toUTCTime(timeString);
    }

    public Lecture()
    {
        this.mLessId = "";
        this.mRoomId = "";
        this.mDay = 0;
        this.mSection = "";

        this.mDur = 0;
        this.mTimeBegin = 0;
    }
    
    public boolean isLab()
    {
        return Lesson.isIdLab(mLessId);
    }
    public boolean isTheory()
    {
        return !isLab();
    }
    public int getZeroBasedEra()
    {
        return Lesson.getZeroBasedEra(this.mLessId);
    }
    
    @Override
    public String toString()
    {
        return this.mLessId + " " + this.mRoomId + " day: " + this.getDay() + " " + TimeUtil.formatUTCTime(mTimeBegin);
    }

    @Override
    public int compareTo(Lecture another)
    {
        //ascending order
        int d = this.mDay - another.getDay();
        if(d != 0) return d;
        long t = this.mTimeBegin - another.getTimeBegin();
        t = t/1000;
        return (int) t;
    }
}
