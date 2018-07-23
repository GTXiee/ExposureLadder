package com.george.exposureladder.ladders;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.george.exposureladder.DialogHelper;
import com.george.exposureladder.R;
import com.george.exposureladder.classes.Step;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;

import java.util.List;

/**
 * Created by georg on 29/01/2018.
 */

public class LadderListAdapter extends RecyclerView.Adapter<LadderListAdapter.ViewHolder> {

    private List<Step> mDataset;
    private Context mContext;

    public LadderListAdapter(List<Step> dataset, Context context) {
        mDataset = dataset;
        mContext = context;
    }

    public void refreshData() {
        mDataset = FeedReaderDbHelper.getInstance(mContext)
                .getAllStepDataList(FeedReaderDbHelper.DIFFICULTY_ASC, FeedReaderDbHelper.NON_ARCHIVED);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ladder_list, parent, false);

        ViewHolder vh = new ViewHolder(v, mContext, this);

        return vh;
    }

    // TODO Can impact performance being in this method, can be moved
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Step step = mDataset.get(position);
        holder.mTextViewStep.setText(step.getDesc());
        holder.mTextViewScore.setText(String.valueOf(step.getScore()));
        holder.updateStep(step);


//        holder.mTextViewOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popupMenu = new PopupMenu(mContext, holder.mTextViewOptions);
//                popupMenu.inflate(R.menu.adapter_ladder_list_popup);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//
//                            case R.id.menu_edit:
//                                Step stepToEdit = mDataset.get(position);
//                                DialogHelper.showEditChallengeDialog(mContext, stepToEdit, LadderListAdapter.this);
//                                Log.d("Exposure", "Notifying item changed");
//                                break;
//
//                            case R.id.menu_delete:
//
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {

        public TextView mTextViewStep;
        public TextView mTextViewScore;
        public CardView mCardView;
        public Step currentStep;
        public Context mContextVH;
        public LadderListAdapter mAdapter;
//        public ImageView mTextViewOptions;

        public ViewHolder(View itemView, Context context, LadderListAdapter adapter) {
            super(itemView);

            mContextVH = context;
            mAdapter = adapter;
            mTextViewStep = itemView.findViewById(R.id.textViewStep);
            mTextViewScore = itemView.findViewById(R.id.textViewScore);
            mCardView = itemView.findViewById(R.id.ladderListCardview);
//            mTextViewOptions = itemView.findViewById(R.id.ladder_item_more);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.menu_edit:
//                    Toast.makeText(mContextVH, R.string.nav_header_title, Toast.LENGTH_SHORT).show();
                    DialogHelper.showEditChallengeDialog(mContextVH, currentStep, mAdapter);
                    Log.d("Exposure", "Notifying item changed");
                    return true;

                case R.id.menu_delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContextVH);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Delete this step?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            FeedReaderDbHelper.getInstance(mContextVH).archiveStep(currentStep.getId());
                            currentStep.setArchived(true);
                            FeedReaderDbHelper.getInstance(mContextVH).updateStep(currentStep);
                            mAdapter.refreshData();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return true;
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.RIGHT);
            popup.getMenuInflater().inflate(R.menu.adapter_ladder_list_popup, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
//            final PopupMenu popupMenu = new PopupMenu(v.getContext(), mCardView);
//            popupMenu.inflate(R.menu.adapter_ladder_list_popup);
//            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//
//                        case R.id.menu_edit:
//                            DialogHelper.showEditChallengeDialog(, currentStep, this);
//                            Log.d("Exposure", "Notifying item changed");
//                            break;
//
//                        case R.id.menu_delete:
//
//                            break;
//                    }
//                    return false;
//                }
//            });
//            popupMenu.show();
            return true;
        }

        private void updateStep(Step step) {
            currentStep = step;
        }


    }




}
