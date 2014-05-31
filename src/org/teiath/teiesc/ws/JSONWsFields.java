package org.teiath.teiesc.ws;


public class JSONWsFields
{
    public static class F_Room
    {
        public static final String CODE_NAME = "codeName";
        public static final String COMMENT   = "comment";
        public static final String IS_LAB    = "isLab";
    }

    public static class F_Registration
    {
        public static final String LESS_ID = "lessID";
        public static final String SECTION = "section";
    }

    public static class F_Lessons
    {
        public static final String ID    = "id";
        public static final String TITLE = "title";
    }

    public static class F_Lecture
    {
        public static final String ROOM_ID  = "roomId";
        public static final String DURATION = "duration";
        public static final String LESS_ID  = "lessId";
        public static final String DAY      = "day";
        public static final String SECTION  = "section";
        public static final String TIME     = "timeBegin";
    }

    public static class F_Update
    {
        public static final String LAST_UPDATE = "lastUpdate";
        public static final String TABLE_NAME  = "tableName";
    }
    
    public static class F_Login
    {
        public static final String TICKET = "ticket";
    }
    public static class F_Ticket
    {
        public static final String IS_VALID = "isValid";
    }
    
    public static class F_Login_Out
    {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}
