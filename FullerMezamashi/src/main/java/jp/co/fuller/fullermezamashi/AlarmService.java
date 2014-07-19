package jp.co.fuller.fullermezamashi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
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
        //Toast.makeText(getApplicationContext(), "AlarmService: onCreate()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(getApplicationContext(), "AlarmService: onDestroy()", Toast.LENGTH_SHORT).show();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Toast.makeText(getApplicationContext(), "AlarmService: onBind()", Toast.LENGTH_SHORT).show();
        return new AlarmServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //Toast.makeText(getApplicationContext(), "AlarmService: onUnbind()", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void ringAlarm(Calendar time) {
        long wTime;
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        //now.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        //now.set(_now.year, _now.month, _now.monthDay, _now.hour, _now.minute, _now.second);

        if (timer != null) {    //新しくタイマー生成
            timer.cancel();
        }
        timer = new Timer();

        //時間が来た時にする動作（音を鳴らす）
        TimerTask tTask = new TimerTask() { //タイマー発動時のタスク
            @Override
            public void run() {
                sendBroadcast(new Intent(RING));
                Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);
            }
        };

        //wTime = time.getTimeInMillis()+time.getTimeZone().getOffset(time.getTimeInMillis()) - now.getTimeInMillis()+now.getTimeZone().getOffset(now.getTimeInMillis());
        wTime = time.getTimeInMillis() - now.getTimeInMillis();

        //Toast.makeText(getApplicationContext(),
        //        "time= " + String.valueOf(time.getTime()) + "\ntimeNow= " + String.valueOf(now.getTime()), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),
                "アラームは" + String.valueOf(wTime/1000/60) + "分" + String.valueOf(wTime/1000%60) + "秒後にセットされています",
                Toast.LENGTH_LONG).show();

        timer.schedule(tTask, wTime);
    }
}

