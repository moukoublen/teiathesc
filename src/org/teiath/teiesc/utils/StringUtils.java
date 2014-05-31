package org.teiath.teiesc.utils;

public class StringUtils
{   
    private StringUtils () {}
    
    public static String join(String delim, String... strs)
    {
        StringBuilder b = new StringBuilder();
        boolean isFirst = true;
        for(String f : strs)
        {
            if(isFirst)
                isFirst = false;
            else
                b.append(delim);
            
            b.append(f);
        }
        return b.toString();
    }
    
    public interface StringFunc<T>
    {
        public String get(T item);
    }
    public static <T> String join(String delim, StringFunc<T> func, T... items)
    {
        StringBuilder b = new StringBuilder();
        boolean isFirst = true;
        for(T f : items)
        {
            if(isFirst)
                isFirst = false;
            else
                b.append(delim);
            
            b.append( func.get(f) );
        }
        return b.toString();
    }
    public static <T> String join(String delim, T... items)
    {
        StringBuilder b = new StringBuilder();
        boolean isFirst = true;
        for(T f : items)
        {
            if(isFirst)
                isFirst = false;
            else
                b.append(delim);
            
            b.append( f.toString() );
        }
        return b.toString();
    }
}
