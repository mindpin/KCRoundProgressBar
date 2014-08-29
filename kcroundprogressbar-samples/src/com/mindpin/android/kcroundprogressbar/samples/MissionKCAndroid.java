package com.mindpin.android.kcroundprogressbar.samples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.mindpin.android.kcroundprogressbar.KCRoundProgressBar;

/**
 * Created by dd on 14-7-28.
 */
public class MissionKCAndroid extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission4);
        KCRoundProgressBar progressBar = (KCRoundProgressBar) findViewById(R.id.progressBar);
        progressBar.set_current(44);

        progressBar.set_width_dp(30);
        progressBar.set_fg_color(Color.parseColor("#4CC85E"));
        progressBar.set_bg_color(Color.parseColor("#00000000"));
        progressBar.set_text_size_dp(9);
        progressBar.set_thickness(0.2f);
        progressBar.set_internal_bg_color(Color.parseColor("#E4F7E7"));
        progressBar.set_text_color(Color.parseColor("#333333"));
        progressBar.set_text_show_type(KCRoundProgressBar.TextShowType.PERCENTAGE);
        progressBar.set_text_display(true);

    }
}