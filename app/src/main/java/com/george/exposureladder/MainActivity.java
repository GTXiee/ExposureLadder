package com.george.exposureladder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.Stetho;
import com.george.exposureladder.challenges.ChallengesActivity;
import com.george.exposureladder.datamodel.FeedReaderDbHelper;
import com.george.exposureladder.ladders.LadderActivity;

public class MainActivity extends AppCompatActivity {

    private FeedReaderDbHelper mDbHelper;

    private Button mLadderButton;
    private Button mChallengesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Stetho initialization
        Stetho.initializeWithDefaults(this);

        mDbHelper = FeedReaderDbHelper.getInstance(this);

        mLadderButton = findViewById(R.id.ladder_button);
        mChallengesButton = findViewById(R.id.challenges_button);

        mLadderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LadderActivity.class);
                startActivity(intent);
            }
        });

        mChallengesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
