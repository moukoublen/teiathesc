package org.teiath.teiesc.dataitems;

public class Lesson implements IDataItem
{
    private String id;
    private String title;

    public void setID(String id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getID()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }
    
    public boolean isLab()
    {
        return isIdLab(id);
    }
    
    public boolean isTheory()
    {
        return !isLab();
    }
    
    public int getZeroBasedEra()
    {
        return getZeroBasedEra(this.id);
    }
    
    public static boolean isIdLab(String lessId)
    {
        return lessId.endsWith("-Ε");
    }
    public static boolean isIdTheory(String lessId)
    {
        return !isIdLab(lessId);
    }
    public static int getZeroBasedEra(String lessId)
    {
        if(lessId.length() < 4) return -1;
        
        char c_era = lessId.charAt(3);
        
        int era = -1;
        try
        {
            era = Integer.parseInt(Character.toString(c_era));
            era--;
        }
        catch(NumberFormatException ex)
        {
            
        }
        return era;
    }

    @Override
    public String toString()
    {
        return this.id + " " + this.title;
    }
}
