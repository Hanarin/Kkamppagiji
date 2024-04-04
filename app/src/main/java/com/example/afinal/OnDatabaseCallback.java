package com.example.afinal;

import java.util.ArrayList;
import java.util.Date;

public interface OnDatabaseCallback {
    // SQL of table BELONGING_INFO
    public int insertBelongingRecord(String name);
    public void updateBelonging(int id, String name);
    public void deleteBelonging(int id);
    public ArrayList<BelongingInfo> selectAllBelonging();
    public BelongingInfo selectBelonging(int id);
    public int getMaxBelongingId();

    // SQL of table TIMELINE_INFO
    public int insertTimelineRecord(double x, double y);
    public int insertTimelineRecord(double x, double y, String datetime);
    public int insertTimelineRecord(double x, double y, int year, int month, int date, int hour, int minute, int second);
    public int insertTimelineRecord(double x, double y, Date datetime);
    public ArrayList<TimelineInfo> selectAllTimeline();
    public TimelineInfo selectTimeline(int id);
    public int getMaxTimelineId();
    public ArrayList<Date> getDateList();
    public ArrayList<TimelineInfo> getTimelineFromDate(Date date);

    // SQL of table POSSESSION_INFO
    public void insertPossessionRecord(int belongingId, int timelineId, boolean possession);
    public ArrayList<PossessionInfo> selectAllPossession();
    public PossessionInfo selectPossession(int belongingId, int timelineId);
    public ArrayList<PossessionInfo> selectPossessionFromTimeline(int timelineId);
    public void updatePossession(int belongingId, int timelineId, boolean possession);
}
