package com.mindpin.android.kcroundprogressbar.samples;

import android.app.Activity;
import android.os.Bundle;
import com.mindpin.android.kcroundprogressbar.KCRoundProgressBar;

/**
 * Created by dd on 14-7-28.
 */
public class Smooth99 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission1);
        KCRoundProgressBar progressBar = (KCRoundProgressBar) findViewById(R.id.progressBar);
        progressBar.set_current_smooth(99);
        progressBar.set_text_display(true);
    }
}