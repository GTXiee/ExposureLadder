package com.george.exposureladder.challenges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.george.exposureladder.R;
import com.george.exposureladder.classes.Challenge;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;

import java.util.List;

/**
 * Created by georg on 12/02/2018.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    // views
    private RecyclerView mRecyclerView;

    // other variables
    private CompletedChallengeListAdapter mListAdapter;
    private FeedReaderDbHelper mDbHelper;
    private List<Challenge> mChallengeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        // connect view
        mRecyclerView = view.findViewById(R.id.tab2RecyclerView);

        // setup recyclerview
        mDbHelper = FeedReaderDbHelper.getInstance(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mChallengeList = mDbHelper.getChallengeDataList(FeedReaderDbHelper.DATE_COMPLETE_DESC, FeedReaderDbHelper.COMPLETE);
        mListAdapter = new CompletedChallengeListAdapter(mChallengeList, this.getContext());

        mRecyclerView.setAdapter(mListAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mChallengeList.clear();
        mChallengeList.addAll(mDbHelper.getChallengeDataList(FeedReaderDbHelper.DATE_COMPLETE_DESC, FeedReaderDbHelper.COMPLETE));
        mListAdapter.notifyDataSetChanged();
    }
}
