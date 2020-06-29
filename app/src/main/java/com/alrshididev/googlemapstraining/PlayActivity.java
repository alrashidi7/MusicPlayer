package com.alrshididev.googlemapstraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.alrshididev.googlemapstraining.databinding.ActivityPlayBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static com.alrshididev.googlemapstraining.MainActivity.musicFilesArrayList;

public class PlayActivity extends AppCompatActivity {
    ActivityPlayBinding binding;
    int position = -1;
    Handler handler;
    int currentPosition;
    static ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer = new MediaPlayer();
    Thread playBtn, nextBtn, prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        binding = DataBindingUtil.setContentView(PlayActivity.this, R.layout.activity_play);
        position = getIntent().getIntExtra("position", -1);
        musicFiles = musicFilesArrayList;
        binding.songName.setText(musicFiles.get(position).getTitle());
        binding.artistName.setText(musicFiles.get(position).getArtist());
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        handler = new Handler();

        PlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(currentPosition);
                    binding.durationPlayed.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        if (musicFiles != null) {
            binding.playPause.setImageResource(R.drawable.ic_baseline_pause);
            uri = Uri.parse(musicFiles.get(position).getPath());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);


        playBtn = new Thread() {
            @Override
            public void run() {
                super.run();
                whenClickPlayButton();
            }
        };
        playBtn.start();
        nextBtn = new Thread() {
            @Override
            public void run() {
                super.run();
                whenClickNextButton();
            }
        };
        nextBtn.start();
        prevBtn = new Thread() {
            @Override
            public void run() {
                super.run();
                whenClickPrevButton();
            }
        };
        prevBtn.start();
        binding.repeatSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
            }
        });
        binding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }


    private void whenClickPrevButton() {
        binding.prevSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position - 1));
                uri = Uri.parse(musicFiles.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                metaData(uri);
                binding.songName.setText(musicFiles.get(position).getTitle());
                binding.artistName.setText(musicFiles.get(position).getArtist());
                binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                PlayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                            binding.seekBar.setProgress(currentPosition);
                        }
                        handler.postDelayed(this, 1000);
                    }
                });

            }
        });
    }

    private void whenClickNextButton() {
        binding.nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position + 1));
                uri = Uri.parse(musicFiles.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                metaData(uri);
                binding.songName.setText(musicFiles.get(position).getTitle());
                binding.artistName.setText(musicFiles.get(position).getArtist());
                binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                PlayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null){
                            currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                            binding.seekBar.setProgress(currentPosition);
                        }
                        handler.postDelayed(this,1000);
                    }
                });


            }
        });
    }

    private void whenClickPlayButton() {
        binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    binding.playPause.setImageResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                    binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    PlayActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                binding.seekBar.setProgress(currentPosition);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                } else {
                    binding.playPause.setImageResource(R.drawable.ic_baseline_pause);
                    mediaPlayer.start();
                    binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    PlayActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                binding.seekBar.setProgress(currentPosition);
                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                }
            }
        });
    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(musicFiles.get(position).getDuration());
        binding.durationTotal.setText(formattedTime(durationTotal / 1000));
        byte[] artist = retriever.getEmbeddedPicture();
        if (artist != null) {
            Glide.with(getApplicationContext()).asBitmap().load(artist).into(binding.trackImage);
        } else {
            Glide.with(getApplicationContext()).asDrawable().load(R.drawable.ic_music).into(binding.trackImage);
        }
    }

    private String formattedTime(int currentPosition) {
        String totalOut = "";
        String totalNew = "";
        String minutes = String.valueOf(currentPosition / 60);
        String secaonds = String.valueOf(currentPosition % 60);
        totalOut = minutes + ":" + secaonds;
        totalNew = minutes + ":" + "0" + secaonds;
        if (secaonds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}