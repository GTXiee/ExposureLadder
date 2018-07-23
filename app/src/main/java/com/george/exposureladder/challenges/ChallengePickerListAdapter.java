package com.george.exposureladder.challenges;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.george.exposureladder.R;
import com.george.exposureladder.classes.Step;

import java.util.List;

/**
 * Created by georg on 13/02/2018.
 */

public class ChallengePickerListAdapter extends RecyclerView.Adapter<ChallengePickerListAdapter.ChallengePickerViewHolder> {

    public static final int REQUEST_EXIT = 0;

    private List<Step> mDataSet;

    public ChallengePickerListAdapter(List<Step> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ChallengePickerViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_picker_list, parent, false);

        ChallengePickerViewHolder vh = new ChallengePickerViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ChallengePickerViewHolder holder, int position) {
        Step step = mDataSet.get(position);
        holder.mChallengeTextView.setText(step.getDesc());
        holder.mChallengeScoreTextView.setText(String.valueOf(step.getScore()));
        holder.updateStep(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ChallengePickerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mChallengeTextView;
        public TextView mChallengeScoreTextView;

        private Step mStep;

        public ChallengePickerViewHolder(final View itemView) {
            super(itemView);

            mChallengeTextView = itemView.findViewById(R.id.challengeListStepTV);
            mChallengeScoreTextView = itemView.findViewById(R.id.challengeListScoreTV);

            itemView.setOnClickListener(this);
        }

        // TODO The ChallengePicker activity is never closed
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ChallengeNewActivity.class);
            intent.putExtra("stepId", mStep.getId());
            intent.putExtra("stepDesc", mStep.getDesc());
            intent.putExtra("stepScore", mStep.getScore());
            Activity activity = (Activity) v.getContext();
            activity.startActivityForResult(intent, REQUEST_EXIT);
        }

        public void updateStep(Step step) {
            this.mStep = step;
        }
    }

}
