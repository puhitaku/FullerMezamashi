package jp.co.fuller.fullermezamashi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by puhitaku on 13/08/18.
 */
public class AlarmService extends Service {

    public static final String RING = "Alarm Service";
    private Timer timer;

    class AlarmServiceBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "AlarmService: onCreate()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "AlarmService: onDestroy()", Toast.LENGTH_SHORT).show();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "AlarmService: onBind()", Toast.LENGTH_SHORT).show();
        return new AlarmServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(getApplicationContext(), "AlarmService: onUnbind()", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void ringAlarm(long rtime) {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        TimerTask tTask = new TimerTask() {

            @Override
            public void run() {
                sendBroadcast(new Intent(RING));
            }
        };

        timer.schedule(tTask, rtime);
    }
}

