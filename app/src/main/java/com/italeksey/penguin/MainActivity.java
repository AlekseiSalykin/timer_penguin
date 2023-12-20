package com.italeksey.penguin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.italeksey.penguin.model.GameBalance;

public class MainActivity extends AppCompatActivity {
    private static final String SAVED = "conditions_";
    private int setImage;
    private MediaPlayer mediaPlayer;
    private Button shop_button, startButton, stopButton;
    private TextView money, textClock;
    private ImageView imageView2;
    private static final long TIMER_INTERVAL = 1000;
    private EditText timeEditText;
    private CountDownTimer countDownTimer;
    SharedPreferences saveImage;
    SharedPreferences.Editor editor;
    GameBalance gameBalance = new GameBalance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shop_button = findViewById(R.id.shop_button);
        money = findViewById(R.id.money_id);
        imageView2 = findViewById(R.id.imageView2);
        textClock = findViewById(R.id.textClock);
        timeEditText = findViewById(R.id.timeEditText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        mediaPlayer = MediaPlayer.create(this, R.raw.meditation);

        gameBalance.loadBalance(this);
        updateBalance();


        Intent intent = getIntent();
        if (intent.getBooleanExtra("change_image", false)) {
            changeImage(intent.getIntExtra("index", 1));
        }
        retImageState(setImage);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeInput = timeEditText.getText().toString();
                long timeInMillis = Long.parseLong(timeInput) * 60000;

                startTimer(timeInMillis);
                playMusic(mediaPlayer);

                timeEditText.getText().clear();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                stopMusic(mediaPlayer);
            }
        });

        shop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(v);
            }
        });
    }

    public void playMusic(MediaPlayer player){
        if(player.isPlaying()){
            player.stop();
        }
        player.start();
        player.setLooping(true);
    }

    public void stopMusic(MediaPlayer player){
        if (player.isPlaying()) {
            player.pause();
        }
    }
    public void startTimer(long timeInMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timeInMillis, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                textClock.setText(timeLeftFormatted);

                if(seconds % 10 == 0 && seconds != 0) {
                    increaseBalance(10);
                }
            }

            @Override
            public void onFinish() {
                textClock.setText("00:00");
            }
        };

        countDownTimer.start();
    }

    public void stopTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
            textClock.setText("00:00");
        }
    }

    public void changeImage(int index) {
        imageView2.setImageResource(R.drawable.penguin_svgrepo_com1);
        setImage = 1;
        if (index == 2) {
            imageView2.setImageResource(R.drawable.penguin_svgrepo_com__1_);
            setImage = index;
        }
        else if (index == 3) {
            imageView2.setImageResource(R.drawable.penguin_svgrepo_com__2_);
            setImage = index;
        }
        else if (index == 4) {
            imageView2.setImageResource(R.drawable.penguin_svgrepo_com__3_);
            setImage = index;
        }
        saveImageState(setImage);
    }

    public void retImageState(int index){
        saveImage = getSharedPreferences(SAVED, MODE_PRIVATE);
        index = saveImage.getInt("CurrentIndex ", index);
        if (index == 1)
            imageView2.setImageResource(R.drawable.penguin_svgrepo_com1);
        else if (index == 2)
            imageView2.setImageResource(R.drawable.penguin_svgrepo_com__1_);
        else if (index == 3)
            imageView2.setImageResource(R.drawable.penguin_svgrepo_com__2_);
        else if (index == 4)
            imageView2.setImageResource(R.drawable.penguin_svgrepo_com__3_);
    }

    public void saveImageState(int index) {
        saveImage = getSharedPreferences(SAVED, MODE_PRIVATE);
        editor = saveImage.edit();
        editor.putInt("CurrentIndex ", index);
        editor.apply();
    }

    public void increaseBalance(int amount){
        int balance = Integer.parseInt(money.getText().toString()) + amount;
        gameBalance.setBalance(balance);
        updateBalance();
        Toast toast = Toast.makeText(getApplicationContext(), "Баланс монет увеличен на 10",
                Toast.LENGTH_LONG );
        toast.show();
    }

    private void updateBalance() {
        int balance = gameBalance.getBalance();
        money.setText(String.valueOf(balance));
    }

    public void startNewActivity(View v){
        Intent intent = new Intent(this, ActivityShop.class);
        intent.putExtra("balanceNew", money.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onPause(){
        super.onPause();
        gameBalance.saveBalance(this);
    }
    @Override
    public void onStop(){
        super.onStop();
        gameBalance.saveBalance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameBalance.saveBalance(this);
    }
}