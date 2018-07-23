package com.george.exposureladder.classes;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by georg on 30/01/2018.
 */

public class Step implements Comparable<Step>, Serializable {

    private int mId;
    private String mDesc;
    private int mScore;
    private boolean mArchived;

    public Step(String desc, int score) {
        this.mDesc = desc;
        this.mScore = score;
        this.mArchived = false;
    }

    public Step(String desc, int score, int id) {
        this.mDesc = desc;
        this.mScore = score;
        this.mId = id;
        this.mArchived = false;
    }

    public int getId() {
        return mId;
    }

    public String getDesc() {
        return mDesc;
    }

    public int getScore() {
        return mScore;
    }

    public void setDesc(String desc) {
        this.mDesc = desc;
    }

    public void setScore(int score) {
        this.mScore = score;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public boolean isArchived() {
        return mArchived;
    }

    public void setArchived(boolean archived) {
        mArchived = archived;
    }

    @Override
    public int compareTo(@NonNull Step o) {

        if (mScore > o.getScore()) {
            return 1;
        } else if (mScore < o.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }
}
