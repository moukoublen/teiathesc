package org.teiath.teiesc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Selector
{
    public static interface Condition<T>
    {
        public boolean isAccepted(final T item);
    }
    
    public static interface Select<Input, Return>
    {
        public Return get(Input item);
    }
    
    
    public static <Input, Return> List<Return> select(
            final List<Input> lst,
            final Select<Input, Return> selector)
    {
        ArrayList<Return> ret = new ArrayList<Return>();
        select(lst, ret, selector);
        return ret;
    }

    public static <Input, Return> void select(
            final List<Input> lst,
                  List<Return> toAppend, 
            final Select<Input, Return> selector)
    {
        for (Input i : lst)
        {
            toAppend.add(selector.get(i));
        }
    }

    public static <T> List<T> where(List<T> lst, Condition<T> cond)
    {
        ArrayList<T> ret = new ArrayList<T>();
        where(lst, ret, cond);
        return ret;
    }

    public static <T> void where(
            List<T>      lst,
            List<T> toAppend,
            Condition<T> cond
        )
    {
        for (T o : lst)
        {
            if (cond.isAccepted(o))
                toAppend.add(o);
        }
    }

    public static <T> boolean isNotIn(T item, T... its)
    {
        return !isIn(item, its);
    }

    public static <T> boolean isIn(T item, T... its)
    {
        boolean ret = false;

        for (T e : its)
        {
            if (item == e)
            {
                ret = true;
                break;
            }
        }

        return ret;
    }
    
    public static <T, K> boolean atLeastOneSatisfies(final List<T> lst, final Condition<T> condition)
    {
        boolean ret = false;
        
        for(int i = 0; i < lst.size(); i++)
        {
            if(condition.isAccepted(lst.get(i)))
            {
                ret = true;
                break;
            }
        }
        
        return ret;
    }

    public static <T> int count(Condition<T> cond, T... items)
    {
        int ret = 0;
        for (int i = 0; i < items.length; i++)
        {
            if(cond.isAccepted(items[i]))
            {
                ret++;
            }
        }
        return ret;
    }

    public static <T> int count(Condition<T> cond, Collection<T> items)
    {
        int ret = 0;
        for (T t : items)
        {
            if (cond.isAccepted(t))
            {
                ret++;
            }
        }
        return ret;

    }

    public static <T> T getFirst(Collection<T> cl, Condition<T> cond)
    {
        T ret = null;

        for (T e : cl)
        {
            if (cond.isAccepted(e))
            {
                ret = e;
                break;
            }
        }

        return ret;
    }

}
