package jp.co.fuller.fullermezamashi;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button btnSet = (Button)findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker pkrTime = (TimePicker)findViewById(R.id.timePicker);
                wakeSoon( pkrTime );
            }
        });

        TimePicker timepkrAlarm = (TimePicker)findViewById(R.id.timePicker);
        timepkrAlarm.setIs24HourView(true);

    }

    private class timerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //MediaPlayer mp =
        }
    }

    public void wakeSoon(TimePicker picker) {
        Integer hrs = picker.getCurrentHour();
        Integer min = picker.getCurrentMinute();
        PowerManager.WakeLock wakelock;
        KeyguardManager keyguard;
        KeyguardManager.KeyguardLock keylock;
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Toast.makeText(SettingActivity.this, "アラームは" + hrs.toString() + "時" + min.toString() + "分" + "にセットされています",
                    Toast.LENGTH_SHORT).show();

        Time time = new Time("Asia/Tokyo");
        time.setToNow();
        //while(time.hour != hrs && time.minute != min) {
        //}

        vib.vibrate(100);

        /*
        wakelock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                                |PowerManager.ACQUIRE_CAUSES_WAKEUP
                                |PowerManager.ON_AFTER_RELEASE, "disableLock");
        wakelock.acquire();

        keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keylock = keyguard.newKeyguardLock("disableLock");
        keylock.disableKeyguard();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }
    
}
