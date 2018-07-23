package com.george.exposureladder.challenges;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.george.exposureladder.classes.Challenge;
import com.george.exposureladder.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class ChallengeDetailActivity extends AppCompatActivity {

    private Challenge mChallenge;

    private TextView mChallengeDesc;
    private TextView mChallengeDate;
    private TextView mChallengeScore;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        // retrieve data for step
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mChallenge = (Challenge) bundle.getSerializable("challenge");
        }

        // link views
        mChallengeDesc = findViewById(R.id.challengeDetailDesc);
        mChallengeDate = findViewById(R.id.challengeDetailDate);
        mChallengeScore = findViewById(R.id.challengeDetailScore);
        mFAB = findViewById(R.id.challengeDetailFAB);

        // set view values
        mChallengeDesc.setText(mChallenge.getStep().getDesc());
        SimpleDateFormat writeDate = new SimpleDateFormat("dd MMM yy, HH:mm", Locale.getDefault());
        writeDate.setTimeZone(TimeZone.getDefault());
        mChallengeDate.setText(writeDate.format(mChallenge.getDateSet()));
        mChallengeScore.setText(String.valueOf(mChallenge.getPreDifficulty()));
    }

    public void handleCompleteFAB(View v) {
        Intent intent = new Intent(this, ChallengeCompleteActivity.class);
        intent.putExtra("challenge", mChallenge);
        startActivityForResult(intent, ChallengeCompleteActivity.REQUEST_EXIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // this activity finishes when the child finishes
        if (requestCode == ChallengeCompleteActivity.REQUEST_EXIT) {
            if(resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
}
