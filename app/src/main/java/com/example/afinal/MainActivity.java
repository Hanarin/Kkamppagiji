package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnDatabaseCallback {
    private static final String TAG = "MainActivity";

    TimelineFragment timelineFragment;
    BelongingFragment belongingFragment;

    LocalDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        timelineFragment = new TimelineFragment();
        belongingFragment = new BelongingFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, belongingFragment).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.belongingTab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, belongingFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.timelineTab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, timelineFragment).commit();
                return true;
            }
            return false;
        });

        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = LocalDatabase.getInstance(this);
        boolean isOpenDatabase = database.open();
        if (isOpenDatabase) {
            Log.d(TAG, "Database is open.");
        } else {
            Log.d(TAG, "Database is not open.");
        }
    }

    protected void onDestroy() {
        // close database
        if (database != null) {
            database.close();
            database = null;
        }
        super.onDestroy();
    }

    @Override
    public int insertBelongingRecord(String name) {
        return database.insertBelongingRecord(name);
    }

    @Override
    public void updateBelonging(int id, String name) {
        database.updateBelonging(id, name);
    }

    @Override
    public void deleteBelonging(int id) {
        database.deleteBelonging(id);
    }

    @Override
    public ArrayList<BelongingInfo> selectAllBelonging() {
        return database.selectAllBelonging();
    }

    @Override
    public BelongingInfo selectBelonging(int id) {
        return database.selectBelonging(id);
    }

    @Override
    public int getMaxBelongingId() {
        return database.getMaxBelongingId();
    }

    @Override
    public int insertTimelineRecord(double x, double y) {
        return database.insertTimelineRecord(x, y);
    }

    @Override
    public int insertTimelineRecord(double x, double y, String datetime) {
        return database.insertTimelineRecord(x, y, datetime);
    }
    @Override
    public int insertTimelineRecord(double x, double y, int year, int month, int date, int hour, int minute, int second) {
        return database.insertTimelineRecord(x, y, year, month, date, hour, minute, second);
    }

    @Override
    public int insertTimelineRecord(double x, double y, Date datetime) {
        return database.insertTimelineRecord(x, y, datetime);
    }

    @Override
    public ArrayList<TimelineInfo> selectAllTimeline() {
        return database.selectAllTimeline();
    }

    @Override
    public TimelineInfo selectTimeline(int id) {
        return database.selectTimeline(id);
    }

    @Override
    public int getMaxTimelineId() {
        return database.getMaxTimelineId();
    }

    @Override
    public ArrayList<Date> getDateList() {
        return database.getDateList();
    }

    @Override
    public ArrayList<TimelineInfo> getTimelineFromDate(Date date) {
        return database.getTimelineFromDate(date);
    }

    @Override
    public void insertPossessionRecord(int belongingId, int timelineId, boolean possession) {
        database.insertPossessionRecord(belongingId, timelineId, possession);
    }

    @Override
    public ArrayList<PossessionInfo> selectAllPossession() {
        return database.selectAllPossession();
    }

    @Override
    public PossessionInfo selectPossession(int belongingId, int timelineId) {
        return database.selectPossession(belongingId, timelineId);
    }

    @Override
    public ArrayList<PossessionInfo> selectPossessionFromTimeline(int timelineId) {
        return database.selectPossessionFromTimeline(timelineId);
    }

    @Override
    public void updatePossession(int belongingId, int timelineId, boolean possession) {
        database.updatePossession(belongingId, timelineId, possession);
    }
}