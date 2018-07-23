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

import com.george.exposureladder.classes.Challenge;
import com.george.exposureladder.R;

import java.util.List;

/**
 * Created by georg on 28/02/2018.
 */

public class ActiveChallengeListAdapter extends RecyclerView.Adapter<ActiveChallengeListAdapter.ChallengeViewHolder> {

    private List<Challenge> mDataSet;

    public ActiveChallengeListAdapter(List<Challenge> dataSet, Context context) {
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
                .inflate(R.layout.item_challenges_active_list, parent, false);
        ChallengeViewHolder vh = new ChallengeViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ChallengeViewHolder holder, int position) {
        Challenge challenge = mDataSet.get(position);
        holder.challengeListStepTV.setText(challenge.getStep().getDesc());
        holder.challengeListScoreTV.setText(String.valueOf(challenge.getPreDifficulty()));

        // span the item if active
//        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
//            StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
//            sglp.setFullSpan(challenge.isActive());
//            holder.itemView.setLayoutParams(sglp);
//        }

        // highlight the item if selected

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

        private Challenge mChallenge;

        public ChallengeViewHolder(View itemView) {
            super(itemView);

            challengeListStepTV = itemView.findViewById(R.id.challengesActiveListStep);
            challengeListScoreTV = itemView.findViewById(R.id.challengesActiveListScore);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ChallengeCompleteActivity.class);
            intent.putExtra("challenge", mChallenge);
            Activity activity = (Activity) v.getContext();
            activity.startActivityForResult(intent, ChallengeCompleteActivity.REQUEST_EXIT);
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
