package org.teiath.teiesc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.teiath.teiesc.dataitems.Lecture;
import org.teiath.teiesc.dataitems.LectureLessonPack;
import org.teiath.teiesc.dataitems.Lesson;
import org.teiath.teiesc.dataitems.TimeUtil;

import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LectureLessonAdapter extends BaseAdapter
{
    public static class RowItems
    {
        public TextView lessonName;
        public TextView timeText;
        public TextView roomName;
    }

    private LayoutInflater mInflater;
    private List<LectureLessonPack> mDataList;
    private SparseIntArray mColors;

    private static final int COLORS[] = { Color.rgb(34, 34, 34),
            Color.rgb(45, 45, 45) };

    public LectureLessonAdapter(LayoutInflater inflater)
    {
        this.mInflater = inflater;
        this.mDataList = new ArrayList<LectureLessonPack>();
        this.mColors = new SparseIntArray();
    }

    private int getColorFor(int index)
    {
        int toGet = 0;
        if (index != 0)
        {
            toGet = mColors.get(index - 1);
            if (mDataList.get(index).getLecture().getTimeBegin() != 
                mDataList.get(index - 1).getLecture().getTimeBegin())
            {
                toGet = (++toGet) % 2;
            }

            mColors.put(index, toGet);
        }

        mColors.put(index, toGet);
        return COLORS[mColors.get(index)];
    }

    private boolean displayTime(int index)
    {
        if (mDataList.size() == (index + 1))
        {
            return true;
        }

        return mDataList.get(index).getLecture().getTimeBegin() != mDataList
                .get(index + 1).getLecture().getTimeBegin();

    }

    public void notifyChanges()
    {
        this.notifyDataSetChanged();
    }

    public List<LectureLessonPack> getList()
    {
        return this.mDataList;
    }

    public void clearData(boolean notifyChanges)
    {
        this.mDataList.clear();
        if (notifyChanges) notifyChanges();
    }

    public void addAll(Collection<LectureLessonPack> cl)
    {
        addAll(cl, false);
    }

    public void addAll(Collection<LectureLessonPack> cl, boolean notifyChanges)
    {
        this.mDataList.addAll(cl);
        sort();
        if (notifyChanges) notifyChanges();
    }

    public void add(LectureLessonPack item, boolean notifyChanges)
    {
        this.mDataList.add(item);
        sort();
        if (notifyChanges) notifyChanges();
    }

    private void sort()
    {
        Collections.sort(this.mDataList);
    }

    @Override
    public int getCount()
    {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get view
        RowItems viewItems = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.list_row, null);

            viewItems = new RowItems();
            viewItems.lessonName = (TextView) convertView.findViewById(R.id.lessonName);
            viewItems.roomName = (TextView) convertView.findViewById(R.id.roomText);
            viewItems.timeText = (TextView) convertView.findViewById(R.id.timeText);

            convertView.setTag(viewItems);
        }
        else
        {
            viewItems = (RowItems) convertView.getTag();
        }

        // Set Values
        putValuesToViews(viewItems, position);

        // Set Color Background
        convertView.setBackgroundColor(getColorFor(position));

        return convertView;
    }

    private void putValuesToViews(RowItems viewItems, int position)
    {
        if (position >= mDataList.size() || position < 0) return;

        LectureLessonPack rowData = mDataList.get(position);
        Lesson ls = rowData.getLesson();
        Lecture lc = rowData.getLecture();

        if (ls == null) ls = new Lesson();
        if (lc == null) lc = new Lecture();

        viewItems.lessonName.setText(titleText(lc, ls));
        viewItems.roomName.setText(lc.getRoomId());
        viewItems.timeText.setText(displayTime(position) ? timeText(lc) : "");
    }

    private String titleText(Lecture lec, Lesson ls)
    {
        StringBuilder b = new StringBuilder();

        b.append(ls.getTitle());

        if (lec.isLab())
        {
            b.append(" (");
            b.append(lec.getSection());
            b.append(")");
        }

        return b.toString();
    }

    private String timeText(Lecture lec)
    {
        StringBuilder b = new StringBuilder();
        b.append(TimeUtil.formatUTCTime(lec.getTimeBegin()));
        b.append(" - ");
        b.append(TimeUtil.formatUTCTime(lec.getTimeBegin() + lec.getDur()));
        ;
        return b.toString();
    }

}
