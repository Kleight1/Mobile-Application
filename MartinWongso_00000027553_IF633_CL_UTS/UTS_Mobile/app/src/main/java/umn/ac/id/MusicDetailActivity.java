package umn.ac.id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MusicDetailActivity extends AppCompatActivity {
    static MediaPlayer mp;//assigning memory loc once or else multiple songs will play at once
    int position;
    SeekBar sb;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    Button pause,next,previous;
    TextView songNameText;

    String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Now Playing");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        songNameText = (TextView) findViewById(R.id.txtSongLabel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Now Playing");

        pause = (Button)findViewById(R.id.pause);

        previous = (Button)findViewById(R.id.previous);
        next = (Button)findViewById(R.id.next);

        sb=(SeekBar)findViewById(R.id.seekBar);

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                //loadNextSong();
                synchronized (this){
                    int totDuration = mp.getDuration();
                    int curPos = 0;


                    while(curPos < totDuration){
                        try {
                            sleep(1000);
                            curPos = mp.getCurrentPosition();
                            sb.setProgress(curPos);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
                //Log.v(curPos, totDuration);
            }
        };

        if(mp != null){
            mp.stop();
            mp.release();
        }
        Intent i = getIntent();
        Bundle b = i.getExtras();

        mySongs = (ArrayList) b.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        String SongName = i.getStringExtra("songname");
        songNameText.setText(SongName);
        songNameText.setSelected(true);

        position = b.getInt("pos",0);
        Uri u = Uri.parse(mySongs.get(position).toString());

        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();
        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i,
                                        boolean b) {
          }
          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {
          }
          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
              mp.seekTo(seekBar.getProgress());

          }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());
                if(mp.isPlaying()){
                    pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    mp.pause();

                }
                else {
                    pause.setBackgroundResource(R.drawable.pause);
                    mp.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                position=((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get( position).toString());
                // songNameText.setText(getSongName);
                mp = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songNameText.setText(sname);

                sb.setMax(mp.getDuration());
                new Thread(updateSeekBar).start();

                try{
                    mp.start();
                }catch(Exception e){}
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //songNameText.setText(getSongName);
                mp.stop();
                mp.release();

                position=((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString();
                songNameText.setText(sname);

                sb.setMax(mp.getDuration());
                new Thread(updateSeekBar).start();

                mp.start();
            }
        });

        /*
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Thread.currentThread().interrupted();
                //updateSeekBar = null;
                mp.stop();
                mp.release();
                position = ((position + 1)%mySongs.size());

                Uri uri = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),uri);

                sname = mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", "");
                songNameText.setText(sname);

                sb.setMax(mp.getDuration());
                new Thread(updateSeekBar).start();
                mp.start();
            }
        });
        */
    }
}