package jp.co.fuller.fullermezamashi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

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

    Calendar today = Calendar.getInstance();
    final Calendar ring = Calendar.getInstance();
    int year, month, day, hour, minute;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        //today.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        //ring.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        today.setTimeInMillis(System.currentTimeMillis());
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day = today.get(Calendar.DAY_OF_MONTH);
        hour = today.get(Calendar.HOUR_OF_DAY);
        minute = today.get(Calendar.MINUTE) + 3;
        ring.set(year, month, day, hour, minute, 0);

        final Button btnDate = (Button)findViewById(R.id.btnDate);
        final Button btnTime = (Button)findViewById(R.id.btnTime);
        ImageButton btnSet = (ImageButton)findViewById(R.id.btnSet);

        final TextView txtTime = (TextView)findViewById(R.id.txtTime);

        //Pickers' event listener
        DatePickerDialog.OnDateSetListener datepkrListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int _year, int _month, int _day) {
                txtTime.setText(
                        String.valueOf(_year) + "年" +
                        String.valueOf(_month + 1) + "月" +
                        String.valueOf(_day) + "日\n" +
                        String.valueOf(hour) + ":" +
                        String.valueOf(minute)
                );
                ring.set(_year, _month, _day);
                year = _year;
                month = _month;
                day = _day;
            }
        };

        TimePickerDialog.OnTimeSetListener timepkrListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker picker, int _hour, int _minute) {
                txtTime.setText(
                        String.valueOf(year) + "年" +
                        String.valueOf(month + 1) + "月" +
                        String.valueOf(day) + "日\n" +
                        String.valueOf(_hour) + ":" +
                        String.valueOf(_minute)
                );
                ring.set(year, month, day, _hour, _minute);
                hour = _hour;
                minute = _minute;
            }
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, datepkrListener, year, month, day);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, timepkrListener, hour, minute, true);

        //Buttons' event listener
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                today = Calendar.getInstance();
                if(ring.after(today)) {
                    service.ringAlarm(ring);
                    moveTaskToBack(true);
                } else {
                    Toast.makeText(ctx, "正しい日付と時間をセットしてください！", Toast.LENGTH_LONG).show();
                }
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
