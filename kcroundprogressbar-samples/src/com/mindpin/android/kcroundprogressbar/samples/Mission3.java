package com.mindpin.android.kcroundprogressbar.samples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.mindpin.android.kcroundprogressbar.KCRoundProgressBar;

/**
 * Created by dd on 14-7-28.
 */
public class Mission3 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission3);
        KCRoundProgressBar progressBar = (KCRoundProgressBar) findViewById(R.id.progressBar);
        progressBar.set_width_px(130);
        progressBar.set_text_display(true);
        progressBar.set_border_display(true);
//        progressBar.set_text_size(40);
        progressBar.set_current(39);
        progressBar.set_fg_color(Color.parseColor("#FFEC03"));
        progressBar.set_bg_color(Color.parseColor("#00000000"));
        progressBar.set_thickness(0.3f);
    }
}