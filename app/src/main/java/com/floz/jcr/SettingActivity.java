package com.floz.jcr;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity);
        TextView tmp=(TextView) findViewById(R.id.textView);
        tmp.setText("庄博尔\n梁汐然");
    }
}