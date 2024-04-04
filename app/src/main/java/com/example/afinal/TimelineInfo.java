package com.example.afinal;

import java.util.Date;

public class TimelineInfo {

    private int id;
    private double x;
    private double y;
    private Date datetime;

    public TimelineInfo(int id, double x, double y, Date datetime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "TimelineInfo{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", datetime=" + datetime.toString() +
                '}';
    }
}
