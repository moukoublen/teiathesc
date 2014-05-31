package org.teiath.teiesc.utils;

public final class Utils
{
    private Utils()
    {}

    public static <T> T[] asArray(T... params)
    {
        return params;
    }

    // Safe cast. Returns null if the object is not
    // instance of T.
    public static <T> T as(Class<T> clazz, Object o)
    {
        if (clazz == null)
            return null;

        if (clazz.isInstance(o))
        {
            return clazz.cast(o);
        }

        return null;
    }
    
    
}
