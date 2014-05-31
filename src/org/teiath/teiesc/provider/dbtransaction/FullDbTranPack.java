package org.teiath.teiesc.provider.dbtransaction;

import org.teiath.teiesc.dataitems.IItemRefDictionary;
import org.teiath.teiesc.provider.dbmetadata.ADbTableMetaData;


import android.content.ContentResolver;
import static org.teiath.teiesc.dataitems.DataItemReference.*;

public class FullDbTranPack
{
    private DbLecturesTr lectures;
    private DbLessonsTr lessons;
    private DbRegistrationsTr registrations;
    private DbRoomsTr rooms;
    private DbUpdateEntriesTr updates;
    
    private IItemRefDictionary<ADbTableMetaData> tables;
    
    
    public FullDbTranPack(IItemRefDictionary<ADbTableMetaData> tables)
    {
        this.tables = tables;
        init();
    }
    public FullDbTranPack(DbLecturesTr lectures, DbLessonsTr lessons,
            DbRegistrationsTr registrations, DbRoomsTr rooms,
            DbUpdateEntriesTr updates)
    {
        this.lectures = lectures;
        this.lessons = lessons;
        this.registrations = registrations;
        this.rooms = rooms;
        this.updates = updates;
    }
    
    private void init()
    {
        this.lectures = 
                new DbLecturesTr(tables.get(LEC));
        this.lessons = 
                new DbLessonsTr(tables.get(LES));
        this.registrations = 
                new DbRegistrationsTr(tables.get(REG));
        this.rooms = 
                new DbRoomsTr(tables.get(ROO));
        this.updates = 
                new DbUpdateEntriesTr(tables.get(UPD));
    }
    
    public void deleteAll(ContentResolver cr)
    {
        lectures.deleteAll(cr);
        lessons.deleteAll(cr);
        registrations.deleteAll(cr);
        rooms.deleteAll(cr);
        updates.deleteAll(cr);
    }
    
    public final DbLecturesTr getLectures()
    {
        return lectures;
    }
    public final DbLessonsTr getLessons()
    {
        return lessons;
    }
    public final DbRegistrationsTr getRegistrations()
    {
        return registrations;
    }
    public final DbRoomsTr getRooms()
    {
        return rooms;
    }
    public final DbUpdateEntriesTr getUpdates()
    {
        return updates;
    }
}
