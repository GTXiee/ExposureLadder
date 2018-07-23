package com.george.exposureladder.datamodel;

import android.provider.BaseColumns;

/**
 * Created by georg on 28/01/2018.
 */

final class FeedReaderContract {

    // constructor private so class is not instantiated accidentally
    private FeedReaderContract() {
    }

    public static final String SQL_CREATE_STEPS_TABLE =
            "CREATE TABLE " + StepEntries.TABLE_NAME + " (" +
                    StepEntries._ID + " INTEGER PRIMARY KEY," +
                    StepEntries.COLUMN_NAME_STEP + " TEXT NOT NULL," +
                    StepEntries.COLUMN_NAME_SCORE + " INTEGER NOT NULL," +
                    StepEntries.COLUMN_NAME_ARCHIVED + " INTEGER)";

    public static final String SQL_CREATE_CHALLENGES_TABLE =
            "CREATE TABLE " + ChallengeEntries.TABLE_NAME + " (" +
                    ChallengeEntries._ID + " INTEGER PRIMARY KEY," +
                    ChallengeEntries.COLUMN_NAME_COMPLETED + " INTEGER NOT NULL," +
                    ChallengeEntries.COLUMN_NAME_DATE_SET + " TEXT NOT NULL," +
                    ChallengeEntries.COLUMN_NAME_DATE_COMPLETE + " TEXT," +
                    ChallengeEntries.COLUMN_NAME_PRE_DIFFICULTY + " INTEGER NOT NULL," +
                    ChallengeEntries.COLUMN_NAME_POST_DIFFICULTY + " INTEGER," +
                    ChallengeEntries.COLUMN_NAME_NOTES + " TEXT," +
                    ChallengeEntries.COLUMN_NAME_STEP_ID + " INTEGER NOT NULL)";

    public static final String SQL_DELETE_TABLE_STEPS =
            "DROP TABLE IF EXISTS " + StepEntries.TABLE_NAME;

    public static final String SQL_DELETE_TABLE_CHALLENGES =
            "DROP TABLE IF EXISTS " + ChallengeEntries.TABLE_NAME;

    // TODO change table name below
    // inner classes defining the table contents
    public static class StepEntries implements BaseColumns {
        public static final String TABLE_NAME = "ladder_details";
        public static final String COLUMN_NAME_STEP = "step";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COLUMN_NAME_ARCHIVED = "archived";
    }

    public static class ChallengeEntries implements BaseColumns {
        public static final String TABLE_NAME = "challenge_details";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_DATE_SET = "date_set";
        public static final String COLUMN_NAME_DATE_COMPLETE = "date_complete";
        public static final String COLUMN_NAME_PRE_DIFFICULTY = "pre_difficulty";
        public static final String COLUMN_NAME_POST_DIFFICULTY = "post_difficulty";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_STEP_ID = "step_id";
    }
}


