package com.george.exposureladder;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.george.exposureladder.classes.Step;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;
import com.george.exposureladder.ladders.LadderListAdapter;

import java.util.List;

/**
 * Created by georg on 05/02/2018.
 */

public class DialogHelper {

    public static void showNewChallengeDialog(final Context context, final List<Step> mStepData) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.dialog_new_step, null);
        final EditText editText = view.findViewById(R.id.challengeEText);
        final SeekBar seekBar = view.findViewById(R.id.difficultySeekbar);
        final TextView scoreTV = view.findViewById(R.id.scoreTV);
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(50)});
        scoreTV.setText(String.valueOf(seekBar.getProgress() + 1));
        Button button = view.findViewById(R.id.addChallengeButton);

        final AlertDialog dialog;
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedReaderDbHelper dbHelper = FeedReaderDbHelper.getInstance(context);
                if((!editText.getText().toString().isEmpty())) {
                    String step = editText.getText().toString().trim();
                    int score = seekBar.getProgress() + 1;
                    Step newStep = new Step(step, score);
                    if(dbHelper.insertStepData(newStep)) {
                        Toast.makeText(context,
                                R.string.add_successful, Toast.LENGTH_SHORT).show();
                        mStepData.add(newStep);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context,
                                R.string.problem_occurred, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context,
                            R.string.fill_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new CustomOnSeekBarChangeListener(scoreTV));
    }

    public static void showEditChallengeDialog(final Context context, final Step stepToUpdate,
                                               final LadderListAdapter adapter) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.dialog_new_step, null);
        final EditText editText = view.findViewById(R.id.challengeEText);
        final SeekBar seekBar = view.findViewById(R.id.difficultySeekbar);
        final TextView scoreTV = view.findViewById(R.id.scoreTV);
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(50)});
        editText.setText(stepToUpdate.getDesc());
        seekBar.setProgress(stepToUpdate.getScore() - 1);
        scoreTV.setText(String.valueOf(stepToUpdate.getScore()));
        Button button = view.findViewById(R.id.addChallengeButton);
        button.setText(R.string.save);

        final AlertDialog dialog;
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedReaderDbHelper dbHelper = FeedReaderDbHelper.getInstance(context);
                if((!editText.getText().toString().isEmpty())) {
                    String step = editText.getText().toString().trim();
                    int score = seekBar.getProgress() + 1;
                    int id = stepToUpdate.getId();
                    Step newStep = new Step(step, score, id);
                    if(dbHelper.updateStep(newStep)) {
                        Toast.makeText(context,
                                R.string.saved, Toast.LENGTH_SHORT).show();
                        stepToUpdate.setDesc(step);
                        stepToUpdate.setScore(score);
                        adapter.refreshData();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context,
                                R.string.problem_occurred, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context,
                            R.string.fill_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new CustomOnSeekBarChangeListener(scoreTV));
    }




    public static class CustomOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private TextView scoreTV;

        public CustomOnSeekBarChangeListener(TextView scoreTV) {
            this.scoreTV = scoreTV;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            scoreTV.setText(String.valueOf(progress + 1));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

}
