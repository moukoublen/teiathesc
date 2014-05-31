package org.teiath.teiesc.dataitems;


import java.sql.Date;

public class UpdateEntry implements IDataItem
{
    private String tableName;
    private long lastUpdate;

    public long getLastUpdate()
    {
        return lastUpdate;
    }
    public Date getLastUpdateDate()
    {
        return new Date(this.lastUpdate);
    }

    public String getTableName()
    {
        return tableName;
    }
    
    public void setLastUpdate(Date lastUpdate)
    {
        this.setLastUpdate(lastUpdate.getTime());
    }

    public void setLastUpdate(long lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public UpdateEntry()
    {
        this("", 0);
    }

    public UpdateEntry(String tableName, long lastUpdate)
    {
        this.tableName = tableName;
        this.lastUpdate = lastUpdate;
    }
    
    @Override
    public String toString()
    {
        return tableName + " " + lastUpdate;
    }
}
