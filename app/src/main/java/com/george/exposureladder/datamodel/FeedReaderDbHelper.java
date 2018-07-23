package com.george.exposureladder.datamodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.george.exposureladder.classes.Challenge;
import com.george.exposureladder.classes.Step;
import com.george.exposureladder.datamodel.FeedReaderContract.StepEntries;
import com.george.exposureladder.datamodel.FeedReaderContract.ChallengeEntries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by georg on 28/01/2018.
 */

public final class FeedReaderDbHelper extends SQLiteOpenHelper {

    private static FeedReaderDbHelper sInstance;

    // constants
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DIFFICULTY_ASC = StepEntries.COLUMN_NAME_SCORE + " ASC";
    public static final String DATE_SET_ASC = ChallengeEntries.COLUMN_NAME_DATE_SET + " ASC";
    public static final String DATE_COMPLETE_DESC = ChallengeEntries.COLUMN_NAME_DATE_COMPLETE + " DESC";
    private static final int FALSE = 0;
    private static final int TRUE = 1;
    public static final int ACTIVE = 0;
    public static final int COMPLETE = 1;
    public static final int NON_ARCHIVED = 0;
    public static final int ARCHIVED = 1;

    // TODO Change db name
    // if database schema is changed, increment the database version
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "FeedReader.db";

    private static final String UPDATE_WHERE = StepEntries._ID + " = ";

    private FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // thread safe access to the dbhelper
    public static synchronized FeedReaderDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FeedReaderDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(FeedReaderContract.SQL_CREATE_STEPS_TABLE);
            db.execSQL(FeedReaderContract.SQL_CREATE_CHALLENGES_TABLE);
        } catch (Exception e) {
            Log.e("Exposure", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // for now, discard data and start over
        db.execSQL(FeedReaderContract.SQL_DELETE_TABLE_STEPS);
        db.execSQL(FeedReaderContract.SQL_DELETE_TABLE_CHALLENGES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    // TODO Catch exceptions with insertOrThrow
    public long insertNewChallengeData(int stepId, int preDifficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // convert local time to UTC to be stored
        Date localTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateSet = dateFormat.format(localTime);

        values.put(ChallengeEntries.COLUMN_NAME_COMPLETED, FALSE);
        values.put(ChallengeEntries.COLUMN_NAME_DATE_SET, dateSet);
        values.put(ChallengeEntries.COLUMN_NAME_PRE_DIFFICULTY, preDifficulty);
        values.put(ChallengeEntries.COLUMN_NAME_STEP_ID, stepId);

        try {
            long result = db.insertOrThrow(ChallengeEntries.TABLE_NAME, null, values);
            if(result == -1) {
                Log.d("Exposure", "Insert challenge failed");
                return result;
            } else {
                Log.d("Exposure", "Inserted new challenge successfully");
                return result;
            }
        } catch (SQLException e) {
            Log.e("Exposure", e.getMessage());
        } finally {
            db.close();
        }
        return -1;
    }


    public boolean insertStepData(Step newStep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String step = newStep.getDesc();
        int score = newStep.getScore();
        values.put(StepEntries.COLUMN_NAME_STEP, step);
        values.put(StepEntries.COLUMN_NAME_SCORE, score);
        values.put(StepEntries.COLUMN_NAME_ARCHIVED, newStep.isArchived());
        long result = db.insert(StepEntries.TABLE_NAME,null, values);
        if(result == -1) {
            Log.d("Exposure", "Insert step failed");
            db.close();
            return false;
        } else {
            Log.d("Exposure", "Inserted new step successfully");
            db.close();
            return true;
        }
    }

    private boolean updateStepWithDb(Step step, SQLiteDatabase db) {
        int idToChange = step.getId();
        String newStep = step.getDesc();
        int newScore = step.getScore();
        ContentValues values = new ContentValues();
        values.put(StepEntries.COLUMN_NAME_STEP, newStep);
        values.put(StepEntries.COLUMN_NAME_SCORE, newScore);
        values.put(StepEntries.COLUMN_NAME_ARCHIVED, step.isArchived());
        int result = db.update(StepEntries.TABLE_NAME, values,
                UPDATE_WHERE + idToChange, null);
        if(result == -1) {
            Log.d("Exposure", "Update step failed");
            return false;
        } else {
            Log.d("Exposure", "Updated step successfully");
            return true;
        }
    }

    public boolean updateStep(Step step) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(updateStepWithDb(step, db)) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public boolean updateSteps(List<Step> changedStepList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for(Step step: changedStepList) {
            // if a step fails to update - rollback
            if(!updateStepWithDb(step, db)) {
                db.endTransaction();
                db.close();
                Log.d("Exposure", "One of the steps failed to update - rolling back");
                return false;
            } else { // if succeeds, update list

            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return true;
    }

    // TODO Contain all cursors in try-finally blocks
    private Step getStepFromId(int stepId, SQLiteDatabase db) {
        String[] projection = {
                StepEntries._ID,
                StepEntries.COLUMN_NAME_STEP,
                StepEntries.COLUMN_NAME_SCORE
        };
        String selection = StepEntries._ID + "=?";
        String[] selectionArgs = {String.valueOf(stepId)};

        Step step = null;

        Cursor cursor = db.query(StepEntries.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null);

        try {
            cursor.moveToNext();
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(StepEntries._ID)
            );
            String desc = cursor.getString(
                    cursor.getColumnIndexOrThrow(StepEntries.COLUMN_NAME_STEP)
            );
            int difficulty = cursor.getInt(
                    cursor.getColumnIndexOrThrow(StepEntries.COLUMN_NAME_SCORE)
            );
            step = new Step(desc, difficulty, id);
        } catch (Exception e) {
            Log.e("Exposure", e.getMessage());
        } finally {
            if(cursor != null) cursor.close();
        }
        return step;
    }

    public List<Challenge> getChallengeDataList(String orderBy, int status) {
        List<Challenge> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            ChallengeEntries._ID,
            ChallengeEntries.COLUMN_NAME_COMPLETED,
            ChallengeEntries.COLUMN_NAME_DATE_SET,
            ChallengeEntries.COLUMN_NAME_DATE_COMPLETE,
            ChallengeEntries.COLUMN_NAME_PRE_DIFFICULTY,
            ChallengeEntries.COLUMN_NAME_POST_DIFFICULTY,
            ChallengeEntries.COLUMN_NAME_NOTES,
            ChallengeEntries.COLUMN_NAME_STEP_ID
        };
        String selection = ChallengeEntries.COLUMN_NAME_COMPLETED + "=?";
        String[] selectionArgs = {String.valueOf(status)};
        
        Cursor cursor = db.query(ChallengeEntries.TABLE_NAME, projection, selection, selectionArgs, 
                null, null, orderBy);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(ChallengeEntries._ID)
                );
                String dateSetString = cursor.getString(
                        cursor.getColumnIndexOrThrow(ChallengeEntries.COLUMN_NAME_DATE_SET)
                );
                Date dateSet = fromUTCStringToDate(dateSetString);
                int preDifficulty = cursor.getInt(
                        cursor.getColumnIndexOrThrow(ChallengeEntries.COLUMN_NAME_PRE_DIFFICULTY)
                );
                int stepId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(ChallengeEntries.COLUMN_NAME_STEP_ID)
                );
                Step step = getStepFromId(stepId, db);
                Challenge challenge = new Challenge(id, dateSet, preDifficulty, step);

                // extras to retrieve if challenge complete
                if (status == COMPLETE) {
                    String dateCompleteString = cursor.getString(
                            cursor.getColumnIndexOrThrow(ChallengeEntries.COLUMN_NAME_DATE_COMPLETE)
                    );
                    Date dateComplete = fromUTCStringToDate(dateCompleteString);
                    int postDiff = cursor.getInt(
                            cursor.getColumnIndexOrThrow(ChallengeEntries.COLUMN_NAME_POST_DIFFICULTY)
                    );
                    String notes = cursor.getString(
                            cursor.getColumnIndexOrThrow(ChallengeEntries.COLUMN_NAME_NOTES)
                    );
                    challenge.setComplete(true);
                    challenge.setDateComplete(dateComplete);
                    challenge.setPostDifficulty(postDiff);
                    challenge.setNotes(notes);
                }
                items.add(challenge);


            }
        } catch (Exception e) {
            Log.e("Exposure", e.getMessage());
        } finally {
            if (cursor!= null) cursor.close();
            db.close();
        }
        return items;
    }

    public List<Step> getAllStepDataList(String orderBy, int status) {
        List<Step> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                StepEntries._ID,
                StepEntries.COLUMN_NAME_STEP,
                StepEntries.COLUMN_NAME_SCORE,
                StepEntries.COLUMN_NAME_ARCHIVED
        };

        String selection = StepEntries.COLUMN_NAME_ARCHIVED + " =?";
        String[] selectionArgs = {String.valueOf(status)};

        Cursor cursor = db.query(StepEntries.TABLE_NAME, projection, selection, selectionArgs,
                null, null, orderBy);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(StepEntries._ID)
                );
                String itemStep = cursor.getString(
                        cursor.getColumnIndexOrThrow(StepEntries.COLUMN_NAME_STEP)
                );
                int itemScore = cursor.getInt(
                        cursor.getColumnIndexOrThrow(StepEntries.COLUMN_NAME_SCORE)
                );
                int archived = cursor.getInt(
                        cursor.getColumnIndexOrThrow(StepEntries.COLUMN_NAME_ARCHIVED)
                );
                Step newStep = new Step(itemStep, itemScore, id);
                if (archived == TRUE) {
                    newStep.setArchived(true);
                }
                items.add(newStep);
            }
        } catch (Exception e){
            Log.e("Exposure", e.getMessage());
        } finally {
            if(cursor != null) cursor.close();
            db.close();
        }
        return items;
    }

//    public boolean deleteStep(int stepId) {
//        String DELETE_WHERE = StepEntries._ID + " = " + String.valueOf(stepId);
//        SQLiteDatabase db = getWritableDatabase();
//        if (db.delete(StepEntries.TABLE_NAME,DELETE_WHERE, null) > 0) {
//            Log.d("Exposure", "Step deleted");
//            return true;
//        } else {
//            return false;
//        }
//    }

    public boolean archiveStep(int stepId) {
        String UPDATE_WHERE = StepEntries._ID + " = " + stepId;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StepEntries.COLUMN_NAME_ARCHIVED, 1);
        if (db.update(StepEntries.COLUMN_NAME_ARCHIVED, values, UPDATE_WHERE, null) < 1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean completeChallenge(int challengeId, Date dateComplete, int postDiff, String notes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChallengeEntries.COLUMN_NAME_COMPLETED, TRUE);
        SimpleDateFormat sdf = new SimpleDateFormat(DB_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateCompleteStr = sdf.format(dateComplete);
        values.put(ChallengeEntries.COLUMN_NAME_DATE_COMPLETE, dateCompleteStr);
        values.put(ChallengeEntries.COLUMN_NAME_POST_DIFFICULTY, postDiff);
        if (notes.trim().isEmpty()) {
            values.putNull(ChallengeEntries.COLUMN_NAME_NOTES);
        }
        values.put(ChallengeEntries.COLUMN_NAME_NOTES, notes);

        String selection = ChallengeEntries._ID + " = " + String.valueOf(challengeId);

        int result = db.update(ChallengeEntries.TABLE_NAME, values, selection, null);
        if (result < 0) {
            Log.e("Exposure", "Update challenge failed");
            return false;
        } else {
            Log.d("Exposure", "Updated challenge successfully");
            return true;
        }

    }

    private Date fromUTCStringToDate(String utcString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(utcString);
        } catch (ParseException e) {
            Log.e("Exposure", e.getMessage());
        }
        return date;
    }

}
