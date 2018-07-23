package com.george.exposureladder.classes;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by georg on 19/02/2018.
 */

public class Challenge implements Serializable {

    private int mId;
    private Step mStep;
    private Boolean mIsComplete;
    private Date mDateSet;
    private Date mDateComplete;
    private int mPreDifficulty;
    private int mPostDifficulty;
    private String mNotes;

    public Challenge(Step step, int preDifficulty) {
        mStep = step;
        mPreDifficulty = preDifficulty;
        mDateSet = Calendar.getInstance().getTime();
    }

    public Challenge(int id, Date dateSet, int preDifficulty, Step step) {
        this.mId = id;
        mDateSet = dateSet;
        mPreDifficulty = preDifficulty;
        mStep = step;
    }

    public int getId() {
        return mId;
    }

    public Step getStep() {
        return mStep;
    }

    public Boolean getComplete() {
        return mIsComplete;
    }

    public Date getDateSet() {
        return mDateSet;
    }

    public Date getDateComplete() {
        return mDateComplete;
    }

    public int getPreDifficulty() {
        return mPreDifficulty;
    }

    public int getPostDifficulty() {
        return mPostDifficulty;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setComplete(Boolean complete) {
        mIsComplete = complete;
    }

    public void setPostDifficulty(int postDifficulty) {
        mPostDifficulty = postDifficulty;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public void setDateComplete(Date dateComplete) {
        mDateComplete = dateComplete;
    }

    public void markComplete(Date dateComplete, int postDifficulty, String notes) {
        mIsComplete = true;
        mPostDifficulty = postDifficulty;
        mNotes = notes;
        mDateComplete = dateComplete;
    }
}
