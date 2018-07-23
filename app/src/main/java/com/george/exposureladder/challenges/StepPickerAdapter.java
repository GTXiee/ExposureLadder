package com.george.exposureladder.challenges;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.george.exposureladder.classes.Challenge;
import com.george.exposureladder.R;

import java.util.List;

/**
 * Created by georg on 27/02/2018.
 */

public class StepPickerAdapter extends RecyclerView.Adapter<StepPickerAdapter.StepPickerViewHolder> {

    private List<Challenge> mDataSet;

    public StepPickerAdapter(List<Challenge> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public StepPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_picker_list, parent, false);

        StepPickerViewHolder vh = new StepPickerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StepPickerViewHolder holder, int position) {
        Challenge challenge = mDataSet.get(position);
        holder.mChallengeTextView.setText(challenge.getStep().getDesc());
        holder.mChallengeScoreTextView.setText(challenge.getPreDifficulty());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class StepPickerViewHolder extends RecyclerView.ViewHolder {

        public TextView mChallengeTextView;
        public TextView mChallengeScoreTextView;

        public StepPickerViewHolder(View itemView) {
            super(itemView);

            mChallengeTextView = itemView.findViewById(R.id.challengeListStepTV);
            mChallengeScoreTextView = itemView.findViewById(R.id.challengeListScoreTV);

        }
    }
}