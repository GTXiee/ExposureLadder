package com.george.exposureladder.challenges;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.george.exposureladder.DialogHelper;
import com.george.exposureladder.R;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;

public class ChallengeNewActivity extends AppCompatActivity {

    public static final int RESULT_EXIT = 0;

    private TextView mStepDescTV;
    private SeekBar mSeekBar;
    private TextView mDifficultyTV;

    private int mStepId;
    private String mStepDesc;
    private int mStepScore;

    private FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_new);

        mDbHelper = FeedReaderDbHelper.getInstance(this);

        // retrieve data for step
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mStepId = bundle.getInt("stepId");
            mStepDesc = bundle.getString("stepDesc");
            mStepScore = bundle.getInt("stepScore");
        }

        // load in views
        mStepDescTV = findViewById(R.id.challengeNewStep);
        mSeekBar = findViewById(R.id.challengeDifficultySeekbar);
        mDifficultyTV = findViewById(R.id.challengeDifficultyTV);

        // set fields
        mStepDescTV.setText(mStepDesc);
        mSeekBar.setProgress(mStepScore - 1);
        mDifficultyTV.setText(String.valueOf(mStepScore));
        mSeekBar.setOnSeekBarChangeListener(new DialogHelper.CustomOnSeekBarChangeListener(mDifficultyTV));
    }

    public void handleSetButton(View view) {
        int difficulty = Integer.parseInt(mDifficultyTV.getText().toString());
        if (mDbHelper.insertNewChallengeData(mStepId, difficulty) != -1) {
            Toast.makeText(this, R.string.add_successful, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, R.string.problem_occurred, Toast.LENGTH_SHORT).show();
        }

    }


}
