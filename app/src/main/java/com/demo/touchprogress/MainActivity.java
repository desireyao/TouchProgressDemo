package com.demo.touchprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.touchprogresslib.widget.TouchProgressLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TouchProgressLayout mTouchProgressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTouchProgressLayout = findViewById(R.id.touchProgressLayout);
        mTouchProgressLayout.setOnProgressChangedLisnener(new TouchProgressLayout.OnProgressChangedLisnener() {
            @Override
            public void onScrollChanged(int progress) {
                Log.e(TAG, " onScrollChanged---> progress: " + progress);
            }

            @Override
            public void onStopChanged(int progress) {
                Log.e(TAG, " onStopChanged---> progress: " + progress);
            }
        });
    }
}
