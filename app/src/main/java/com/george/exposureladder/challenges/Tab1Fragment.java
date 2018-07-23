package com.george.exposureladder.challenges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.george.exposureladder.classes.Challenge;
import com.george.exposureladder.R;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by georg on 12/02/2018.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    // views
    private RecyclerView mRecyclerView;

    // other variables
    private ActiveChallengeListAdapter mListAdapter;
    private FeedReaderDbHelper mDbHelper;
    private List<Challenge> mChallengeList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        // connect view
        mRecyclerView = view.findViewById(R.id.tab1RecyclerView);

        // setup recyclerview
        mDbHelper = FeedReaderDbHelper.getInstance(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mChallengeList = mDbHelper.getChallengeDataList(FeedReaderDbHelper.DATE_SET_ASC, FeedReaderDbHelper.ACTIVE);
        mListAdapter = new ActiveChallengeListAdapter(mChallengeList, this.getContext());

        mRecyclerView.setAdapter(mListAdapter);


        return view;
    }

    // TODO possibly change to only notify when we know changes are made and add new item instead of clearing
    @Override
    public void onResume() {
        super.onResume();

        refreshData();
    }

    private void refreshData() {
        mChallengeList.clear();
        mChallengeList.addAll(mDbHelper.getChallengeDataList(FeedReaderDbHelper.DATE_SET_ASC, FeedReaderDbHelper.ACTIVE));
        mListAdapter.notifyDataSetChanged();
        Log.d("Exposure", "Active Challenges Refreshed");
    }

}
