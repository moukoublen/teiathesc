package org.teiath.teiesc.dataitems;

public interface IItemRefReverseDictionary<T>
{
    public DataItemReference get(T item);
    public boolean isValid(T item);
}
