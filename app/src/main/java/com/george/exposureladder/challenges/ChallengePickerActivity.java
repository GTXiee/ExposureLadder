package com.george.exposureladder.challenges;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.george.exposureladder.R;
import com.george.exposureladder.classes.Step;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;

import java.util.List;

public class ChallengePickerActivity extends AppCompatActivity {

    private FeedReaderDbHelper mDbHelper;
    private RecyclerView mRecyclerView;
    private ChallengePickerListAdapter mListAdapter;

    private List<Step> mStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_picker);

        mDbHelper = FeedReaderDbHelper.getInstance(this);

        mRecyclerView = findViewById(R.id.challengePickerRecyclerView);



        // setup recyclerview
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStepList = mDbHelper.getAllStepDataList(FeedReaderDbHelper.DIFFICULTY_ASC, FeedReaderDbHelper.NON_ARCHIVED);
        // Collections.sort(mStepList);
        mListAdapter = new ChallengePickerListAdapter(mStepList);
        mRecyclerView.setAdapter(mListAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // this activity finishes when the child finishes
        if (requestCode == ChallengePickerListAdapter.REQUEST_EXIT) {
            if(resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
}
