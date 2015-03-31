package com.example.beknazar.stello;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity  {

    private SoundPool soundPool;
    private int soundID;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    private boolean asleep;

    private int delayTime;
    private String host;

    public Runnable mMyRunnableHide = new Runnable()
    {

        public void run()
        {
            // Do something after 5s = 5000ms

            new SudoRunOff().execute(host);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the layout of the Activity
        setContentView(R.layout.activity_main);

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //Hardware buttons setting to adjust the media sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // the counter will help us recognize the stream id of the sound played  now
        counter = 0;

        // Load the sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.beep, 1);

        asleep = false;

        delayTime = 10000;



    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        host = prefs.getString("hostName", "192.168.43.138");
    }

    public void playSound(View v) {
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            counter = counter++;
            Toast.makeText(this, "Played sound", Toast.LENGTH_SHORT).show();
            plays = true;



            final Handler handler = new Handler();
            handler.postDelayed(mMyRunnableHide, delayTime);

            RelativeLayout rlt = (RelativeLayout) findViewById(R.id.mainLayout);
            rlt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           handler.removeCallbacks(mMyRunnableHide);
                                       }
                                   }

            );

        }
    }


    public void stopSound(View v) {
        if (plays) {
            soundPool.stop(soundID);
            soundID = soundPool.load(this, R.raw.beep, counter);
            Toast.makeText(this, "Stop sound", Toast.LENGTH_SHORT).show();
            plays = false;
        }
    }

    public void imAwake(View v) {
        new SudoRun().execute(host);
    }

    public void imAsleep(View v) {
        new SudoRunOff().execute(host);
    }

    private class SudoRun extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... st) {
            Sudo sd = new Sudo();
            String otvet = sd.ledOn(st[0]);
            return otvet;
        }


        protected void onPostExecute(String result) {
            TextView tv = (TextView) findViewById(R.id.helloText);
            tv.setText(result);
        }
    }

    private class SudoRunOff extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... st) {
            Sudo sd = new Sudo();
            String otvet = sd.ledOff(st[0]);
            return otvet;
        }


        protected void onPostExecute(String result) {
            TextView tv = (TextView) findViewById(R.id.helloText);
            tv.setText(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
