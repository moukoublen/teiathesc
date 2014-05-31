package org.teiath.teiesc.ws;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;
import org.teiath.teiesc.ws.JSONWsFields.F_Login;
import org.teiath.teiesc.ws.JSONWsFields.F_Login_Out;
import org.teiath.teiesc.ws.RestWsHandler.HttpMethod;

public final class WsTransaction
{
    private WsTransaction() {}
    
    public static <T> Collection<T> getAllFromWs(RestWsHandler ws,
            JSONExtractor.JSONObjectExtractor<T> extractor) 
    throws JSONException, IllegalStateException, IOException
    {
        String data = ws.getRawData();
        return JSONExtractor.getAllFromStrData(data, extractor);
    }
    
    
    
    public static final String ERROR_MSG = "!";
    
    public static JSONObject getLoginJSONObject(String username, String password)
    {
        JSONObject dataOut = new JSONObject();
        try
        {
            dataOut.put(F_Login_Out.USERNAME, username);
            dataOut.put(F_Login_Out.PASSWORD, password);
        }
        catch (JSONException e)
        {
            
        }
        return dataOut;
    }
    
    public static String login(String url, String username, String password) 
            throws IOException, JSONException
    {
        JSONObject dataOut = getLoginJSONObject(username, password);
        
        RestWsHandler ws = new RestWsHandler(url, HttpMethod.POST);
        
        ws.setPostData(dataOut);
        
        String jsonStr = ws.getRawData();
        
        JSONObject ob = new JSONObject(jsonStr);
        
        String ret = ob.getString(F_Login.TICKET);
        
        return ret;
    }
    
    public static boolean isTicketCorrect(String ticket)
    {
        if(ticket == null) return false;
        return !ticket.startsWith(ERROR_MSG);
    }
}
