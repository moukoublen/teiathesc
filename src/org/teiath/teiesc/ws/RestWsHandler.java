package org.teiath.teiesc.ws;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.json.JSONObject;

public class RestWsHandler
{
    public enum HttpMethod
    {
        GET, POST
    };

    private URL mWsurl = null;
    private HttpURLConnection mConnection = null;

    public RestWsHandler(String url, HttpMethod method)
            throws IllegalStateException, IOException
    {
        this.mWsurl = new URL(url);
        this.configConnection(method);
    }
    
    public RestWsHandler(String url)
            throws IllegalStateException, IOException
    {
        this(url, HttpMethod.GET);
    }
    
    
    private void configConnection(HttpMethod method) throws IOException
    {
        mConnection = (HttpURLConnection) this.mWsurl.openConnection();
        mConnection.setReadTimeout(10000);
        mConnection.setConnectTimeout(15000);
        try
        {
            mConnection.setRequestMethod(method.toString());
        } catch (ProtocolException e) { }
        mConnection.setRequestProperty("Content-Type", "application/json");
        mConnection.setDoInput(true);
        
    }
    
    public void setPostData(JSONObject obj)
    {
        try
        {
            OutputStream os = this.mConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(obj.toString());
            writer.flush();
            writer.close();
            os.close();
        }
        catch (IOException e)
        {
            
        }
    }

    public String getRawData() 
            throws IllegalStateException, IOException
    {
        return getRawData("UTF-8");
    }
    
    public String getRawData(String encoding) 
            throws IOException
    {

        BufferedReader reader;

        try
        {
            this.mConnection.connect();
        }
        catch (IOException e) 
        {
            this.mConnection.disconnect();
            throw e;
        }
        
        InputStreamReader isr = 
                new InputStreamReader(mConnection.getInputStream(), encoding);
        reader = new BufferedReader(isr);
        
        StringBuilder b = new StringBuilder();
        String s;
        while ((s = reader.readLine()) != null)
        {
            b.append(s);
        }

        reader.close();
        isr.close();
        this.mConnection.disconnect();

        return b.toString();
    }
}
