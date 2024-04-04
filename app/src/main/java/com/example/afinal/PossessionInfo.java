package com.example.afinal;

public class PossessionInfo {
    private int belongingId;
    private int timelineId;
    private boolean possession;

    public PossessionInfo() {
        this.belongingId = -1;
        this.timelineId = -1;
        this.possession = false;
    }

    public PossessionInfo(int belongingId, int timelineId) {
        this.belongingId = belongingId;
        this.timelineId = timelineId;
        this.possession = false;
    }

    public PossessionInfo(int belongingId, int timelineId, boolean possession) {
        this.belongingId = belongingId;
        this.timelineId = timelineId;
        this.possession = possession;
    }

    public int getBelongingId() {
        return belongingId;
    }

    public void setBelongingId(int belongingId) {
        this.belongingId = belongingId;
    }

    public int getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(int timelineId) {
        this.timelineId = timelineId;
    }

    public boolean getPossession() {
        return possession;
    }

    public void setPossession(boolean possession) {
        this.possession = possession;
    }

    @Override
    public String toString() {
        return "PossessionInfo{" +
                "belongingId=" + belongingId +
                ", timelineId=" + timelineId +
                ", possession=" + possession +
                '}';
    }
}
