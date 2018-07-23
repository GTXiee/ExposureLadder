package com.george.exposureladder.challenges;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.george.exposureladder.R;
import com.george.exposureladder.classes.Challenge;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by georg on 28/02/2018.
 */

public class CompletedChallengeListAdapter extends RecyclerView.Adapter<CompletedChallengeListAdapter.ChallengeViewHolder> {

    private List<Challenge> mDataSet;

    public CompletedChallengeListAdapter(List<Challenge> dataSet, Context context) {
        try {
            this.mDataSet = dataSet;
        } catch (ClassCastException e) {
            Log.d("Exposure", "Activity must implement AdapterCallback");
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }

    @Override
    public ChallengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_challenges_completed_list, parent, false);
        ChallengeViewHolder vh = new ChallengeViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ChallengeViewHolder holder, int position) {
        Challenge challenge = mDataSet.get(position);
        holder.challengeListStepTV.setText(challenge.getStep().getDesc());
        holder.challengeListScoreTV.setText(String.valueOf(challenge.getPostDifficulty()));
        Log.d("Exposure", "Date to format: " + challenge.getDateComplete());
        if (challenge.getDateComplete() != null) {
            String dateString = "Completed on " +
                    DateFormat.getDateInstance(DateFormat.SHORT).format(challenge.getDateComplete()) +
                    " at " + DateFormat.getTimeInstance(DateFormat.SHORT).format(challenge.getDateComplete());
            holder.challengeListDateTV.setText(dateString);
        }

        holder.updatePosition(position);
        holder.updateChallenge(challenge);
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ChallengeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView challengeListStepTV;
        private TextView challengeListScoreTV;
        private TextView challengeListDateTV;

        private Challenge mChallenge;

        public ChallengeViewHolder(View itemView) {
            super(itemView);

            challengeListStepTV = itemView.findViewById(R.id.challengesCompletedListStep);
            challengeListDateTV = itemView.findViewById(R.id.challengesCompletedListDate);
            challengeListScoreTV = itemView.findViewById(R.id.challengesCompletedListScore);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(v.getContext(), ChallengeDetailActivity.class);
//            intent.putExtra("challenge", mChallenge);
//            Activity activity = (Activity) v.getContext();
//            activity.startActivity(intent);
        }

        private void updatePosition(int pos) {
        }

        private void updateChallenge(Challenge challenge) {
            this.mChallenge = challenge;
        }
    }

}
