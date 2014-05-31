package org.teiath.teiesc.provider.dbmetadata;

import org.teiath.teiesc.utils.StringUtils;

public abstract class ADbTableMetaData
{
    public static class NameType
    {
        public final String name;
        public final String type;
        public NameType(String name, String type)
        {
            this.name = name;
            this.type = type;
        }
        public NameType(String name, String type, String adds)
        {
            this.name = name;
            this.type = type + " " + adds;
        }
        
        @Override
        public String toString()
        {
            return name + " " + type;
        }
    }
    
    public static class SQLiteDataTypes
    {
        public static final String F_TXT = "TEXT";
        public static final String F_INT = "INTEGER";
        public static final String F_LNG = "UNSIGNED BIG INT";
        public static final String F_PK  = "PRIMARY KEY";
    }
    
    public static enum QueryTypes
    {
        CREATE, DROP
    }
    
    
    // Abstract
    public    abstract String     getTableName();
    protected abstract NameType[] getColumns(); 

    //Non abstract
    public String getSelectPart()
    {
        return joinSelectStyle(getColumns());
    }

    public String getCreatePart()
    {
        return joinCreateStyle(getColumns());
    }
    
    public String getCreateQuery()
    {
        return String.format
            (
                "CREATE TABLE %s ( %s );", 
                getTableName(),
                getCreatePart()
            );
    }

    public String getDropQuery()
    {
        return String.format
            (
                "DROP TABLE IF EXISTS %s;", 
                getTableName()
            );
    }
    
    public String getQuery(QueryTypes take)
    {
        switch (take)
        {
        case CREATE:
            return this.getCreateQuery();
        case DROP:
            return this.getDropQuery();
        default:
            return "";
        }
    }

    protected static NameType nt(String name, String type)
    {
        return new NameType(name, type);
    }
    protected static NameType nt(String name, String type, String ads)
    {
        return new NameType(name, type, ads);
    }
    protected static NameType[] array(NameType... nameTypes)
    {
        return nameTypes;
    }
    
    protected static String joinSelectStyle(NameType[] cols)
    {
        StringUtils.StringFunc<NameType> fn = 
                new StringUtils.StringFunc<ADbTableMetaData.NameType>()
        {
            @Override
            public String get(NameType item)
            {
                return item.name;
            }
        };
        
        return StringUtils.join(", ", fn, cols);
    }
    protected static String joinCreateStyle(NameType[] cols)
    {
        return StringUtils.join(",  ", cols);
    }
}
