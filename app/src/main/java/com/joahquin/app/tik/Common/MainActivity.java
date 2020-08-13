package com.joahquin.app.tik.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.joahquin.app.tik.R;
import com.joahquin.app.tik.Utils.Services.ShowNotificationJob;
import com.joahquin.app.tik.WaterMonitor.WaterMonitorHomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBackground));

        Intent intent = new Intent(this, WaterMonitorHomeActivity.class);
        startActivity(intent);
        finish();
    }
}