package com.mindpin.android.kcroundprogressbar.samples;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(
                new SimpleAdapter(
                        this, getData(), android.R.layout.simple_list_item_1, new String[]{"title"},
                        new int[]{android.R.id.text1}
                )
        );
        getListView().setScrollbarFadingEnabled(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        Intent intent = new Intent(this, (Class<? extends Activity>) map.get("activity"));
        startActivity(intent);
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        addItem(data, "Simple", Simple.class);
        addItem(data, "Mission 1", Mission1.class);
        addItem(data, "Mission 2", Mission2.class);
        addItem(data, "Mission 3", Mission3.class);
        addItem(data, "Mission 4-1", Mission41.class);
        addItem(data, "Mission 4-2", Mission42.class);
        addItem(data, "set_current_smooth(99)", Smooth99.class);
        addItem(data, "Mission KC Android", MissionKCAndroid.class);

        return data;
    }

    private void addItem(List<Map<String, Object>> data, String title,
                         Class<? extends Activity> activityClass) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", data.size() + ". " + title);
        map.put("activity", activityClass);
        data.add(map);
    }
}