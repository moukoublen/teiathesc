package org.teiath.teiesc.dataitems;


public class Registration implements IDataItem
{
    private String lessId;
    private String section;

    public Registration()
    {
        this("", "");
    }

    public Registration(String mathID, String tmima)
    {
        this.setMathID(mathID);
        this.setSection(tmima);
    }

    public String getLessID()
    {
        return lessId;
    }

    public String getSection()
    {
        return section;
    }

    public final void setMathID(String lessId)
    {
        this.lessId = lessId;
    }

    public final void setSection(String tmima)
    {
        this.section = tmima;
    }

}
