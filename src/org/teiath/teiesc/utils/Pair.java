package org.teiath.teiesc.utils;

public class Pair <T, K>
{
    public final T item0;
    public final K item1;
    
    public Pair(T item0, K item1)
    {
        this.item0 = item0;
        this.item1 = item1;
    }
    
    public static <T, K> Pair<T, K> getNew(T item0, K item1)
    {
        return new Pair<T, K>(item0, item1);
    }
}
