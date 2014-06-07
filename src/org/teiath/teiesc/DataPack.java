package org.teiath.teiesc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.teiath.teiesc.dataitems.Lecture;
import org.teiath.teiesc.dataitems.LectureLessonPack;
import org.teiath.teiesc.dataitems.Lesson;
import org.teiath.teiesc.dataitems.Registration;
import org.teiath.teiesc.dataitems.Room;
import org.teiath.teiesc.dataitems.UpdateEntry;
import org.teiath.teiesc.options.ViewOptions;
import org.teiath.teiesc.utils.Selector;
import org.teiath.teiesc.utils.Selector.Condition;
import org.teiath.teiesc.utils.Selector.Select;

public class DataPack
{
    private ArrayList<Lecture>      mLectures;
    private ArrayList<Lesson>       mLessons;
    private ArrayList<Registration> mRegistrations;
    private ArrayList<Room>         mRooms;
    private ArrayList<UpdateEntry>  mUpdates;
    
    public DataPack()
    {
        this.mLectures      = new ArrayList<Lecture>();
        this.mLessons       = new ArrayList<Lesson>();
        this.mRegistrations = new ArrayList<Registration>();
        this.mRooms         = new ArrayList<Room>();
        this.mUpdates       = new ArrayList<UpdateEntry>();
    }

    public List<Lecture> getLectures()
    {
        return mLectures;
    }
    public List<Lesson> getLessons()
    {
        return mLessons;
    }
    public List<Registration> getRegistrations()
    {
        return mRegistrations;
    }
    public List<Room> getRooms()
    {
        return mRooms;
    }
    public List<UpdateEntry> getUpdates()
    {
        return mUpdates;
    }
    public void clearAll()
    {
        this.mLectures.clear();
        this.mLessons.clear();
        this.mRegistrations.clear();
        this.mRooms.clear();
        this.mUpdates.clear();
    }

    public List<Lecture> getLecturesWhere(Condition<Lecture> condition)
    {
        return Selector.where(this.mLectures, condition);
    }
    public List<Lesson> getLessonsWhere(Condition<Lesson> condition)
    {
        return Selector.where(this.mLessons, condition);
    }
    public List<Registration> getRegistrationsWhere(Condition<Registration> condition)
    {
        return Selector.where(this.mRegistrations, condition);
    }
    public List<Room> getRoomsWhere(Condition<Room> condition)
    {
        return Selector.where(this.mRooms, condition);
    }
    public List<UpdateEntry> getUpdatesWhere(Condition<UpdateEntry> condition)
    {
        return Selector.where(this.mUpdates, condition);
    }
    
    // Sorting
    public static void sortRegisteredLectures( List<LectureLessonPack> lst )
    {
        Collections.sort(lst);
    }
    
    // Συνδυάζει τις δυο λίστες (Lectures και Registrations) και 
    // επιστρέφει τα Lecture (μαζί με το lesson) που είναι registered
    public List<LectureLessonPack> getRegisteredLecturesAndLessons()
    {
        return getLectureLessonPack(false, true, -1);
    }
    public List<LectureLessonPack> getRegisterLecturesAndLessonsByDay(int day)
    {
        return getLectureLessonPack(true, true, day);
    }
    public List<LectureLessonPack> getLecturesLessonsByDay(int day)
    {
        return getLectureLessonPack(false, true, day);
    }
    private List<LectureLessonPack> getLectureLessonPack(boolean isRegistered, 
            boolean checkDay, int day)
    {
        List<LectureLessonPack> lst = new ArrayList<LectureLessonPack>();
        
        for (final Lecture l : mLectures)
        {
            boolean registerCheck = isLectureRegistered(l) || !isRegistered;
            boolean dayCheck      = l.getDay() == day      || !checkDay;
            
            if (registerCheck && dayCheck)
            {
                Lesson less = getLessonById(l.getLessId());
                lst.add(new LectureLessonPack(l, less));
            }
        }

        return lst;
    }
    public List<LectureLessonPack> getLectureLessonPack(ViewOptions options, int day)
    {
        List<LectureLessonPack> lst = new ArrayList<LectureLessonPack>();
        
        for(final Lecture l : mLectures)
        {
            if(day != l.getDay())
            {
                continue;
            }
            
            if(options.getShowOnlyRegistered())
            {
                if(!isLectureRegistered(l))
                {
                    continue;
                }
            }
            
            if(!options.getShowLabs() && l.isLab())
            {
                continue;
            }
            
            if(!options.getShowTheories() && l.isTheory())
            {
                continue;
            }
            
            if(!options.getShowAllEras())
            {
                if(!options.getShowEra(l.getZeroBasedEra()))
                {
                    continue;
                }
            }
            
            //Add to list
            Lesson less = getLessonById(l.getLessId());
            lst.add(new LectureLessonPack(l, less));
        }
        
        return lst;
    }
    
    public static List<String> getLessonsIds(final List<Lecture> lst)
    {
        Select<Lecture, String> s = new Select<Lecture, String>()
        {
            @Override
            public String get(Lecture item)
            {
                return item.getLessId();
            }
        };
        
        return Selector.select(lst, s);
    }
    
    public Lesson getLessonById(final String id)
    {
        Condition<Lesson> condition = new Condition<Lesson>()
        {
            @Override
            public boolean isAccepted(Lesson item)
            {
                final String d = id;
                return item.getID().equals(d);
            }
        };

        return Selector.getFirst(mLessons, condition);
    }

    private boolean isLectureRegistered(final Lecture lec)
    {
        Condition<Registration> condition = new Condition<Registration>()
        {
            @Override
            public boolean isAccepted(final Registration item)
            {
                Lecture l = lec;
                
                return item.getLessID().equals(l.getLessId())
                        && item.getSection().equals(l.getSection());
            }

        };

        return Selector.atLeastOneSatisfies(mRegistrations, condition);
    }
}
