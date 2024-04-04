package com.example.afinal;

public class BelongingItem {
    private int viewType;
    private BelongingInfo belonging;

    public BelongingItem(int viewType) {
        this.viewType = viewType;
        this.belonging = null;
    }

    public BelongingItem(int viewType, BelongingInfo belonging) {
        this.viewType = viewType;
        this.belonging = belonging;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public BelongingInfo getBelonging() {
        return belonging;
    }

    public void setBelonging(BelongingInfo belonging) {
        this.belonging = belonging;
    }
}
