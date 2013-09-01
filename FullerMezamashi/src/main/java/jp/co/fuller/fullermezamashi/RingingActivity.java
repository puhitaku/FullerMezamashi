package jp.co.fuller.fullermezamashi;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by puhitaku on 13/09/01.
 */
public class RingingActivity extends Activity {
    Calendar now = Calendar.getInstance();

    public String formatMinute(int _min) {
        String min = String.valueOf(_min);

        if(min.length() == 1) {
            min = "0" + min;
        }
        return min;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ringing);

        final MediaPlayer mp = MediaPlayer.create(RingingActivity.this, R.raw.ryori);
        try {
            mp.start();
        } catch (Exception e) {
        }

        now.setTimeInMillis(System.currentTimeMillis());
        TextView txtRingTime = (TextView)findViewById(R.id.txtRingTime);

        txtRingTime.setText(now.get(Calendar.HOUR_OF_DAY) + ":" + formatMinute(now.get(Calendar.MINUTE)));

        Button btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                finish();
            }
        });
    }
}
