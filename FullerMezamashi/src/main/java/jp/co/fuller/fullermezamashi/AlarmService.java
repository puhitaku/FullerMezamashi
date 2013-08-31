package jp.co.fuller.fullermezamashi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.widget.Toast;

import java.util.Calendar;
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

    public long getTimeInSecond(Time t) {
        return t.second + t.minute * 60 + t.hour * 3600;
    }

    public void ringAlarm(Calendar time) {
        long wTime;
        Calendar now = Calendar.getInstance();

        if (timer != null) {    //新しくタイマー生成
            timer.cancel();
        }
        timer = new Timer();

        //時間が来た時にする動作（音を鳴らす）
        TimerTask tTask = new TimerTask() { //タイマー発動時のタスク
            @Override
            public void run() {
                sendBroadcast(new Intent(RING));
            }
        };

        wTime = time.getTimeInMillis() - now.getTimeInMillis();

        Toast.makeText(getApplicationContext(),
                "time= " + String.valueOf(time.getTimeInMillis()) + "timeNow= " + String.valueOf(now.getTimeInMillis()), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),
                "Alarm will ring after " + String.valueOf(wTime/1000) + " seconds",
                Toast.LENGTH_LONG).show();

        timer.schedule(tTask, wTime);
    }
}

