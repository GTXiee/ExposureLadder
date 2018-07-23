package com.george.exposureladder.ladders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.george.exposureladder.DialogHelper;
import com.george.exposureladder.R;
import com.george.exposureladder.classes.Step;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;

import java.util.List;


public class LadderActivity extends Fragment {

    private FeedReaderDbHelper mDbHelper;

    private LadderListAdapter mAdapter;
    private List<Step> mStepList;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFAB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_ladder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDbHelper = FeedReaderDbHelper.getInstance(getContext());

        mRecyclerView = view.findViewById(R.id.ladderRecyclerView);
        mFAB = view.findViewById(R.id.ladder_fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showNewChallengeDialog(getContext(), mStepList);
            }
        });

        // setup recyclerview
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepList = mDbHelper.getAllStepDataList(FeedReaderDbHelper.DIFFICULTY_ASC, FeedReaderDbHelper.NON_ARCHIVED);
        mAdapter = new LadderListAdapter(mStepList, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Ladders");
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ladder);
//
//        Toolbar toolbar = findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        mDbHelper = FeedReaderDbHelper.getInstance(this);
//
//        mRecyclerView = findViewById(R.id.ladderRecyclerView);
//
//        // setup recyclerview
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mStepList = mDbHelper.getAllStepDataList(FeedReaderDbHelper.DIFFICULTY_ASC);
//        mAdapter = new LadderListAdapter(mStepList, this);
//        mRecyclerView.setAdapter(mAdapter);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getContext().getMenuInflater().inflate(R.menu.activity_format, menu);
//        return true;
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_format, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_new:
                // User clicks new button
                DialogHelper.showNewChallengeDialog(getContext(), mStepList);
                // could be quicker to add newstep into list and just notify
                // find a way to retrieve the newstep from onClick
                // could pass the list through
                mAdapter.notifyDataSetChanged();


            default:
                // User's action was not recognized
                return super.onOptionsItemSelected(item);
        }
    }
}
