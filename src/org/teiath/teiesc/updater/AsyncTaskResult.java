package org.teiath.teiesc.updater;

public class AsyncTaskResult<T>
{
    private T result;
    private Exception exception;
    
    public AsyncTaskResult(T result)
    {
        this.result = result;
        this.exception = null;
    }
    
    public AsyncTaskResult(Exception exception)
    {
        this.exception = exception;
        this.result = null;
    }
    
    public T getResult()
    {
        return result;
    }
    public Exception getException()
    {
        return exception;
    }
    
    public boolean hasError()
    {
        return null != exception;
    }
    
}
