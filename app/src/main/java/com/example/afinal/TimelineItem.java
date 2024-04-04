package com.example.afinal;

import java.util.ArrayList;
import java.util.Date;

public class TimelineItem {
    private TimelineInfo timeline;
    private ArrayList<PossessionItem> possessionItems;

    public TimelineItem(TimelineInfo timeline) {
        this.timeline = timeline;
        this.possessionItems = new ArrayList<>();
    }
    public TimelineItem(TimelineInfo timeline, ArrayList<PossessionItem> possessionItems) {
        this.timeline = timeline;
        this.possessionItems = possessionItems;
    }

    public TimelineInfo getTimeline() {
        return timeline;
    }

    public void setTimeline(TimelineInfo timeline) {
        this.timeline = timeline;
    }

    public ArrayList<PossessionItem> getPossessionItems() {
        return possessionItems;
    }

    public void setPossessionItems(ArrayList<PossessionItem> possessionItems) {
        this.possessionItems = possessionItems;
    }

    public void addPossessionItem(PossessionItem possessionItem) {
        possessionItems.add(possessionItem);
    }
}
