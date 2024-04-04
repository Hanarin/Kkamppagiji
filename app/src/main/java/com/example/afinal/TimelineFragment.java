package com.example.afinal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

public class TimelineFragment extends Fragment {
    private static final String TAG = "TimelineFragment";
    OnDatabaseCallback callback;
    ExpandableListView timelineListView;
    TimelineAdapter timelineAdapter;
    ArrayList<Date> dateList = new ArrayList<>(); //부모 리스트
    ArrayList<ArrayList<TimelineItem>> timelineList = new ArrayList<>(); //자식 리스트
    ArrayList<BelongingInfo> belongingData = new ArrayList<>();
    ArrayList<TimelineInfo> timelineData = new ArrayList<>();
    ArrayList<ArrayList<PossessionInfo>> possessionData = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnDatabaseCallback) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.timeline_fragment, container, false);

        // get data from database
        belongingData = callback.selectAllBelonging();
        timelineData = callback.selectAllTimeline();
        for (int i = 0; i < callback.getMaxTimelineId(); i++) {
            ArrayList<PossessionInfo> tempList = new ArrayList<>();
            possessionData.add(tempList);
            for (int j = 0; j < callback.getMaxBelongingId(); j++) {
                PossessionInfo temp = new PossessionInfo();
                possessionData.get(i).add(temp);
            }
        }

        for (int i = 0; i < timelineData.size(); i++) {
            for (int j = 0; j < belongingData.size(); j++) {
                possessionData.get(timelineData.get(i).getId()-1).set(belongingData.get(j).getId()-1, callback.selectPossession(belongingData.get(j).getId(), timelineData.get(i).getId()));
            }
        }
        Log.d(TAG, "possessionData=" + possessionData);

        // set ExpandableListView
        timelineListView = (ExpandableListView) rootView.findViewById(R.id.timelineListView);
        timelineAdapter = new TimelineAdapter(callback);
        timelineAdapter.dateItems = dateList;
        timelineAdapter.timelineItems = timelineList;
        timelineListView.setAdapter(timelineAdapter);
        timelineListView.setGroupIndicator(null);

        updateListData();

        return rootView;
    }

    public void updateListData() {
        timelineAdapter.dateItems.clear();
        timelineAdapter.timelineItems.clear();
        dateList = callback.getDateList();
        timelineAdapter.dateItems = dateList;
        for (int i = 0; i < timelineAdapter.dateItems.size(); i++) {
            timelineAdapter.timelineItems.add(new ArrayList<TimelineItem>());
            ArrayList<TimelineInfo> timelineFromDate = callback.getTimelineFromDate(timelineAdapter.dateItems.get(i));
            Log.d(TAG, "timelineFromDate at i=" + i + ": " + timelineFromDate);
            for (int j = 0; j < timelineFromDate.size(); j++) {
                TimelineItem timelineItem = new TimelineItem(timelineFromDate.get(j));
                for (int k = 0; k < belongingData.size(); k++) {
                    PossessionInfo possessionInfo = possessionData.get(timelineFromDate.get(j).getId()-1).get(belongingData.get(k).getId()-1);
                    PossessionItem possessionItem = new PossessionItem(possessionInfo);
                    timelineItem.addPossessionItem(possessionItem);
                }
                timelineAdapter.addItem(i, timelineItem);
                Log.d(TAG, "timelineItem at i=" + i + ": " + timelineItem.toString());
            }
        }
        Log.d(TAG, "timelineItems=" + timelineAdapter.timelineItems.toString());
        timelineAdapter.notifyDataSetChanged();
    }
}
