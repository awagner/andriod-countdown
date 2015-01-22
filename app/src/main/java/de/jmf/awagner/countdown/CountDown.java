package de.jmf.awagner.countdown;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class CountDown extends ActionBarActivity {

    // Viewobjekte
    private TextView display;
    private Button button;
    private ImageView smilie;

    // "Fertige" Objekte aus Java Paketen
    private CountDownTimer timer;
    private MediaPlayer player;
    private Vibrator v;

    // Zustandsvariable
    private int zustand = 0;

    // Countdown Value
    private final int STARTVALUE = 5;
    private long countdownvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        // View-Objekte verknüpfen.
        this.findViewObjects();
    }

    /** holt die Referenzen auf Objekte der View */
    protected void findViewObjects() {

        display = (TextView) this.findViewById(R.id.display);
        button = (Button) this.findViewById(R.id.startstop);
        smilie = (ImageView) this.findViewById(R.id.smilie);
    }

    /** den Mediaplayer erzeugen und initialisieren */
    protected void onStart() {
        super.onStart();
        player = MediaPlayer.create(CountDown.this, R.raw.lotusfloeteschnell);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    /** Ereignisbehandlung (siehe Zustandsdiagramm),  wird vom Countdown-Timer (sourceid == 2)
     * und vom Button (sourceid ==1) aufgerufen.
     *
     * @param sourceid
     */
    public void handleEvent(int sourceid) {

        switch (zustand) {
            case 0:
                if (sourceid == 1) {
                    startGame();
                    zustand = 1;
                }
                break;
            case 1:
                if (sourceid == 1) {
                    stopGame();
                    zustand = 0;
                }
                if (sourceid == 2) {
                    afterCountDown();
                    stopGame();
                    zustand = 0;
                }
                break;
        }
    }

    /** ausgelöste Aktion startGame, starte Time und setze Buttonbeschriftung*/
    public void startGame() {

        smilie.setVisibility(ImageView.INVISIBLE);
        countdownvalue = STARTVALUE;

        // starte Timer.
        timer = new CountDownTimer(countdownvalue * 1000, 10) {

            public void onTick(long millisUntilFinished) {
                countdownvalue = millisUntilFinished / 1000;
                display.setText(String.valueOf(countdownvalue));
            }

            public void onFinish() {
                handleEvent(2);
            }
        }.start();

        button.setText("Stop");

    }

    /** ausgelöste Aktion StopGame, Timer stoppen und Button zurücksetzen */
    public void stopGame() {
        timer.cancel();
        button.setText("Start");
    }

    private void afterCountDown() {
        player.seekTo(0);
        player.start();
        v.vibrate(500);
        smilie.setVisibility(ImageView.VISIBLE);
    }

    /** Ereignisbehandlung des Start/Stop Buttons
     *
     * @param v Referenz auf die View.
     */
    public void onClickStartStop(View v) {
        handleEvent(1); // id für StartStopButton ist 1.
    }

    /** Mediaplayer stoppen () */
    public void onStop() {
        super.onStop();
        player.stop();
        player.reset();//It requires again setDataSource for player object.
        player.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_count_dowm, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
