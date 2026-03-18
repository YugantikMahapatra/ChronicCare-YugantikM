package com.example.chroniccare;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionsActivity extends AppCompatActivity {

    private LinearLayout step1, step2, step3;
    private ImageView icon1, icon2, icon3;
    private Button btn1, btn2, btn3, btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        initializeViews();
        updateUI();
    }

    private void initializeViews() {
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        
        icon1 = findViewById(R.id.iconStep1);
        icon2 = findViewById(R.id.iconStep2);
        icon3 = findViewById(R.id.iconStep3);
        
        btn1 = findViewById(R.id.btnGrant1);
        btn2 = findViewById(R.id.btnGrant2);
        btn3 = findViewById(R.id.btnGrant3);
        btnDone = findViewById(R.id.btnDonePermissions);

        btn1.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        });

        btn2.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            startActivity(intent);
        });

        btnDone.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        boolean hasOverlay = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasOverlay = Settings.canDrawOverlays(this);
        }
        
        boolean hasAlarm = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            hasAlarm = alarmManager.canScheduleExactAlarms();
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isBatteryUnrestricted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isBatteryUnrestricted = pm.isIgnoringBatteryOptimizations(getPackageName());
        }

        updateStep(hasOverlay, icon1, btn1);
        updateStep(hasAlarm, icon2, btn2);
        updateStep(isBatteryUnrestricted, icon3, btn3);

        if (hasOverlay && hasAlarm && isBatteryUnrestricted) {
            btnDone.setEnabled(true);
            btnDone.setText("All Set! Continue");
        } else {
            btnDone.setEnabled(false);
            btnDone.setText("Please Grant All Permissions");
        }
    }

    private void updateStep(boolean granted, ImageView icon, Button btn) {
        if (granted) {
            icon.setImageResource(R.drawable.ic_checked);
            btn.setVisibility(View.GONE);
        } else {
            icon.setImageResource(R.drawable.ic_unchecked);
            btn.setVisibility(View.VISIBLE);
        }
    }
}
