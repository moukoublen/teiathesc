package org.teiath.teiesc.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.teiath.teiesc.dataitems.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import static org.teiath.teiesc.ws.JSONWsFields.*;

public class JSONExtractor
{
    public static interface JSONObjectExtractor<T>
    {
        public T extract(JSONObject obj) throws JSONException;
    }
    
    private static String notObject(String name)
    {
        return "Not " + name + " Object";
    }

    // EXTRACTORS
    public static final JSONObjectExtractor<Room> ROOM_JSON_EXTRACTOR = new JSONObjectExtractor<Room>()
    {
        @Override
        public Room extract(JSONObject obj) throws JSONException
        {
            Room ret = new Room();
            String failMsg = notObject("Room");

            checkOrThrow(obj, failMsg, F_Room.CODE_NAME, F_Room.COMMENT, F_Room.IS_LAB);

            try
            {
                ret.setCodeName(obj.getString(F_Room.CODE_NAME));
                ret.setComment(obj.getString(F_Room.COMMENT));
                ret.setIsLab(obj.getBoolean(F_Room.IS_LAB));
            }
            catch (JSONException e)
            {
                throw new JSONException(failMsg);
            }

            return ret;
        }
    };
    public static final JSONObjectExtractor<Registration> REGISTRATION_JSON_EXTRACTOR = new JSONObjectExtractor<Registration>()
    {
        @Override
        public Registration extract(JSONObject obj) throws JSONException
        {
            Registration ret = new Registration();
            String failMsg = notObject("Registration");

            checkOrThrow(obj, failMsg, F_Registration.LESS_ID,
                    F_Registration.SECTION);

            try
            {
                ret.setMathID(obj.getString(F_Registration.LESS_ID));
                ret.setSection(obj.getString(F_Registration.SECTION));
            }
            catch (JSONException e)
            {
                throw new JSONException(failMsg);
            }

            return ret;
        }
    };
    public static final JSONObjectExtractor<Lesson> LESSON_JSON_EXTRACTOR = new JSONObjectExtractor<Lesson>()
    {
        @Override
        public Lesson extract(JSONObject obj) throws JSONException
        {
            Lesson ret = new Lesson();
            String failMsg = notObject("Lesson");

            checkOrThrow(obj, failMsg, F_Lessons.ID, F_Lessons.TITLE);

            try
            {
                ret.setID(obj.getString(F_Lessons.ID));
                ret.setTitle(obj.getString(F_Lessons.TITLE));
            }
            catch (JSONException e)
            {
                throw new JSONException(failMsg);
            }

            return ret;
        }
    };
    public static final JSONObjectExtractor<Lecture> LECTURE_JSON_EXTRACTOR = new JSONObjectExtractor<Lecture>()
    {
        @Override
        public Lecture extract(JSONObject obj) throws JSONException
        {
            Lecture ret = new Lecture();
            String failMsg = notObject("Lecture");

            checkOrThrow(obj, failMsg, F_Lecture.ROOM_ID, F_Lecture.DAY,
                    F_Lecture.DURATION, F_Lecture.LESS_ID, F_Lecture.TIME,
                    F_Lecture.SECTION);

            try
            {
                ret.setRoomId(obj.getString(F_Lecture.ROOM_ID));
                ret.setDay(obj.getInt(F_Lecture.DAY));
                ret.setSection(obj.getString(F_Lecture.SECTION));
                ret.setLessId(obj.getString(F_Lecture.LESS_ID));
                
                ret.setTimeBegin(obj.getString(F_Lecture.TIME));
                ret.setDur(obj.getString(F_Lecture.DURATION));
                //ret.setTimeBegin(obj.getLong(F_Lecture.TIME));
                //ret.setDur(obj.getLong(F_Lecture.DURATION));
            }
            catch (JSONException e)
            {
                throw new JSONException(failMsg);
            }

            return ret;
        }
    };
    public static final JSONObjectExtractor<UpdateEntry> UPDATE_JSON_EXTRACTOR = new JSONObjectExtractor<UpdateEntry>()
    {
        @Override
        public UpdateEntry extract(JSONObject obj) throws JSONException
        {
            UpdateEntry ret = new UpdateEntry();
            String failMsg = notObject("UpdateEntry");

            checkOrThrow(obj, failMsg, F_Update.LAST_UPDATE, F_Update.TABLE_NAME);

            try
            {
                ret.setLastUpdate(obj.getLong(F_Update.LAST_UPDATE));
                ret.setTableName(obj.getString(F_Update.TABLE_NAME));
            }
            catch (JSONException e)
            {
                throw new JSONException(failMsg);
            }
            
            return ret;
        }
    };
    //END EXTRACTORS

    // ---> Collection Extractors
    public static <T> Collection<T> extractAll(JSONArray jsonarr, JSONObjectExtractor<T> objExtractor)
    {
        Collection<T> ret = null;
        try
        { 
            ret = extractAll(jsonarr, objExtractor, false);
        }
        catch (Exception e) { }
        return ret;
        // Αφού το onErrorThrowException = false, αποκλείεται να είναι null το ret...
    }
    public static <T> Collection<T> extractAll(JSONArray jsonarr, JSONObjectExtractor<T> objExtractor, boolean onErrorThrowException) 
            throws JSONException
    {
        ArrayList<T> lst = new ArrayList<T>();

        for (int i = 0; i < jsonarr.length(); i++)
        {
            T f = null;
            try
            {
                JSONObject o = jsonarr.getJSONObject(i);
                f = objExtractor.extract(o);
            }
            catch (JSONException e)
            {
                if(onErrorThrowException)
                    throw e;
            }

            if (f != null)
                lst.add(f);
        }

        return lst;
    }
    
    public static <T> Collection<T> getAllFromStrData(String strData,
            JSONExtractor.JSONObjectExtractor<T> extractor) 
    throws JSONException, IllegalStateException, IOException
    {
        JSONArray jsonarr = new JSONArray(strData);
        Collection<T> lst = null;
        lst = JSONExtractor.extractAll(jsonarr, extractor);
        return lst;
    }

    private static void checkOrThrow(JSONObject obj, String fallbackMsg, String... fields) 
            throws JSONException
    {   
        if (!containsFields(obj, fields))
        {
            throw new JSONException(fallbackMsg);
        }
    }
    private static boolean containsFields(JSONObject obj, String... fields)
    {
        boolean ret = true;
        for(String f : fields)
        {
            if(!obj.has(f))
            {
                ret = false;
                break;
            }
        }
        return ret;
    }
}
