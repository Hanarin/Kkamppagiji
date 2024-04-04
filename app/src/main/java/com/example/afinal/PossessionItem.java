package com.example.afinal;

public class PossessionItem {
    private PossessionInfo possession;
    private int resId;

    public PossessionItem() {}
    public PossessionItem(PossessionInfo possession) {
        this.possession = possession;
        if (possession.getPossession()) {
            this.resId = R.drawable.check_mark;
        }
        else {
            this.resId = R.drawable.x_mark;
        }
    }
    public PossessionItem(PossessionInfo possession, int resId) {
        this.possession = possession;
        this.resId = resId;
    }

    public PossessionInfo getPossession() {
        return possession;
    }

    public void setPossession(PossessionInfo possession) {
        this.possession = possession;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
