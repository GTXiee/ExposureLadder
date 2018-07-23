package com.george.exposureladder.challenges;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.george.exposureladder.R;
import com.george.exposureladder.classes.Challenge;
import com.george.exposureladder.DialogHelper.CustomOnSeekBarChangeListener;
import com.george.exposureladder.Utility.CharCountTextWatcher;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChallengeCompleteActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final int REQUEST_EXIT = 0;

    private TextView mCompleteDateTV;
    private TextView mCompleteTimeTV;
    private SeekBar mDifficultySeekBar;
    private TextView mDifficultyTV;
    private EditText mNotesET;
    private TextView mCharCount;
    private LinearLayout mLinearLayout;
    private TextView mStepDesc;

    private final int CHAR_LIMIT = 150;
    private final String LIMIT_STRING = "0/" + String.valueOf(CHAR_LIMIT);

    private Challenge mChallenge;
    private Date mCompleteDate = new Date();
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_complete);


        // setup toolbar
        Toolbar toolbar = findViewById(R.id.challengeCompleteToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // get challenge object
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mChallenge = (Challenge) bundle.getSerializable("challenge");
        }

        // link views
        mCompleteDateTV = findViewById(R.id.challengeCompleteDate);
        mDifficultySeekBar = findViewById(R.id.challengeCompleteDifficultySeek);
        mDifficultyTV = findViewById(R.id.challengeCompleteDifficultyTV);
        mNotesET = findViewById(R.id.challengeCompleteNotesET);
        mCharCount = findViewById(R.id.challengeCompleteCharCount);
        mLinearLayout = findViewById(R.id.challengeCompleteLinearLayout);
        mCompleteTimeTV = findViewById(R.id.challengeCompleteTime);
        mStepDesc = findViewById(R.id.challengeCompleteStepDesc);

//        mCompleteDateTV.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));

        mStepDesc.setText(mChallenge.getStep().getDesc());
        mDifficultyTV.setText(String.valueOf(mChallenge.getPreDifficulty()));

        mDifficultySeekBar.setProgress(mChallenge.getPreDifficulty() - 1);
        mDifficultySeekBar.setOnSeekBarChangeListener(new CustomOnSeekBarChangeListener(mDifficultyTV));

        // TODO: change the limit
        mCharCount.setText(LIMIT_STRING);
        CharCountTextWatcher tw = new CharCountTextWatcher(mCharCount, CHAR_LIMIT);
        mNotesET.addTextChangedListener(tw);
        mNotesET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CHAR_LIMIT)});
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_challenge_complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.challengeCompleteEarlierDate:
                mCalendar = Calendar.getInstance();
                mCompleteDateTV.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(mCalendar.getTime()));
                mCompleteTimeTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(mCalendar.getTime()));
                if (mLinearLayout.getVisibility() == View.GONE) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
                return true;
        }
        return false;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Log.d("Exposure", "Time before format (Date): " + mCalendar.getTime());

        if (mCalendar.getTime().before(new Date())) {
            mCompleteDate = mCalendar.getTime();
            mCompleteDateTV.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(mCompleteDate));
            mCompleteTimeTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(mCompleteDate));
        } else {
            Toast.makeText(this, "Please set a time before now", Toast.LENGTH_SHORT).show();
        }

//        Date date = null;
//        try {
//            date = sdf.parse(formattedDate);
//        } catch (ParseException e) {
//            Log.e("Exposure", "Could not parse string " + formattedDate);
//        }
//        Log.d("Exposure", "After parse (Date): " + date.toString());
//        // check date is before now
//        if (date.before(new Date())) {
//            mCompleteDate = date;
//            String localeDate = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
//            Log.d("Exposure", "After: " + localeDate);
//            mCompleteDateTV.setText(localeDate);
//        } else {
//            Toast.makeText(this, "Please choose an earlier date", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mCalendar.set(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth());
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(datePicker.getYear(),
//                datePicker.getMonth(),
//                datePicker.getDayOfMonth());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        String formattedDate = sdf.format(calendar.getTime());
//        Date date = null;
//        try {
//            date = sdf.parse(formattedDate);
//        } catch (ParseException e) {
//            Log.e("Exposure", "Could not parse string " + formattedDate);
//        }
//        Log.d("Exposure", "Before: " + date.toString());
//        // check date is before now
//        if (date.before(new Date())) {
//            mCompleteDate = date;
//            String localeDate = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
//            Log.d("Exposure", "After: " + localeDate);
//            mCompleteDateTV.setText(localeDate);
//        } else {
//            Toast.makeText(this, "Please choose an earlier date", Toast.LENGTH_SHORT).show();
//        }

        DialogFragment df = new TimePickerFragment();
        df.show(getSupportFragmentManager(), "timePicker");

    }

    public void handleCompleteButton(View view) {
        if (FeedReaderDbHelper.getInstance(this).completeChallenge(
                mChallenge.getId(),
                mCompleteDate,
                mDifficultySeekBar.getProgress() + 1,
                mNotesET.getText().toString().trim())) {

            // TODO is this necessary if all db data is being retrieved?
            mChallenge.markComplete(
                    mCompleteDate,
                    mDifficultySeekBar.getProgress() + 1,
                    mNotesET.getText().toString().trim()
            );
            setResult(RESULT_OK);
            finish();
        } else {
            Log.e("Exposure", "Complete challenge failed");
        }


    }

}
