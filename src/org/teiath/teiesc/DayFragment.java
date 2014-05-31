package org.teiath.teiesc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DayFragment extends Fragment
{
    private static final String POSITION_KEY = "position";
    private ListView mListView;
    private LectureLessonAdapter mAdapter;
    private int position;

    public static DayFragment getInstance(int position, LectureLessonAdapter adapter)
    {
        DayFragment fragment = new DayFragment();
        fragment.position = position;
        fragment.mAdapter = adapter;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        getPosition(savedInstanceState);
        
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list);
        
        mListView.setAdapter(mAdapter);
        return rootView;
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_KEY, position);
        
    }
    
    private void getPosition(Bundle state)
    {
        if(null != state)
        {
            this.position = state.getInt(POSITION_KEY);
        }
    }
    
    public void setListAdapter(ListAdapter adapter)
    {
        if(null != mListView)
        {
            mListView.setAdapter(adapter);
        }
    }
}