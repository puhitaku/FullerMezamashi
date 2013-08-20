package jp.co.fuller.fullermezamashi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class SettingActivity extends Activity {
    private class timerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaPlayer mp = MediaPlayer.create(SettingActivity.this, R.raw.ryori);
            try {
                mp.start();
            } catch (Exception e) {

            }
        }
    }

    private AlarmService service;
    private final  timerReceiver receiver = new timerReceiver();
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = ((AlarmService.AlarmServiceBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TimePicker timepkrAlarm = (TimePicker)findViewById(R.id.timePicker);
        timepkrAlarm.setIs24HourView(true);

        Button btnSet = (Button)findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker pkrTime = (TimePicker)findViewById(R.id.timePicker);
                service.ringAlarm(pkrTime);
                moveTaskToBack(true);
            }
        });

        /* Start service */
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
        IntentFilter filter = new IntentFilter(service.RING);
        registerReceiver(receiver, filter);

        /* Bind the service */
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        /* Re-bind after unbind */
        unbindService(connection);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(receiver);
        service.stopSelf();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }
    
}
