package org.teiath.teiesc.dataitems;


public class Room implements IDataItem
{
    private String codeName;
    private boolean isLab;
    private String comment;

    public Room(String codeName, boolean isLab, String comment)
    {
        this.codeName = codeName;
        this.isLab = isLab;
        this.comment = comment;
    }

    public Room()
    {
        this("", false, "");
    }

    public String getCodeName()
    {
        return codeName;
    }

    public String getComment()
    {
        return comment;
    }

    public boolean getIsIsLab()
    {
        return isLab;
    }

    public void setCodeName(String codeName)
    {
        this.codeName = codeName;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public void setIsLab(boolean isLab)
    {
        this.isLab = isLab;
    }

}
