package com.mindpin.android.kcroundprogressbar.samples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.mindpin.android.kcroundprogressbar.KCRoundProgressBar;

/**
 * Created by dd on 14-7-20.
 */
public class Mission1 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission1);
        KCRoundProgressBar progressBar = (KCRoundProgressBar) findViewById(R.id.progressBar);
        progressBar.set_width_px(100);
        progressBar.set_current(35);
        progressBar.set_fg_color(Color.parseColor("#87CEEB"));
        progressBar.set_bg_color(Color.parseColor("#EEEEEE"));
        progressBar.set_text_display(false);
        progressBar.set_thickness(0.3f);
    }


}