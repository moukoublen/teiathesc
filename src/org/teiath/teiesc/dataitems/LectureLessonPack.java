package org.teiath.teiesc.dataitems;

import org.teiath.teiesc.utils.Pair;

public class LectureLessonPack extends Pair<Lecture, Lesson> 
implements Comparable<LectureLessonPack>
{

    public LectureLessonPack(Lecture lec, Lesson less)
    {
        super(lec, less);
    }
    
    public Lecture getLecture() 
    {
        return this.item0;
    }
    public Lesson getLesson()
    {
        return this.item1;
    }
    
    @Override
    public int compareTo(LectureLessonPack another)
    {
        return this.getLecture().compareTo(another.getLecture());
    }    
}
