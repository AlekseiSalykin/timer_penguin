package com.italeksey.penguin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.italeksey.penguin.model.GameBalance;


public class ActivityShop extends AppCompatActivity {
    private static final String SAVED = "condition_btns";

    private Button main_button_second, buy_button2, buy_button3, buy_button4;
    private ImageButton penguin1, penguin2, penguin3, penguin4;
    private TextView money, cost2, cost3, cost4;
    private String message = "Успешно купили.", message2 = "Не хватает денег.";

    SharedPreferences saveInstance;
    SharedPreferences.Editor editor;
    GameBalance gameBalance = new GameBalance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        money = findViewById(R.id.money_id);
        cost2 = findViewById(R.id.cost2);
        cost3 = findViewById(R.id.cost3);
        cost4 = findViewById(R.id.cost4);
        buy_button2 = findViewById(R.id.buy_button2);
        buy_button3 = findViewById(R.id.buy_button3);
        buy_button4 = findViewById(R.id.buy_button4);
        main_button_second = findViewById(R.id.main_button_second);
        penguin1 = findViewById(R.id.image_penguin1);
        penguin2 = findViewById(R.id.image_penguin2);
        penguin3 = findViewById(R.id.image_penguin3);
        penguin4 = findViewById(R.id.image_penguin4);

        gameBalance.loadBalance(this);
        updateBalance();
        retButtonState(buy_button2, penguin2, 1);
        retButtonState(buy_button3, penguin3, 2);
        retButtonState(buy_button4, penguin4, 3);
        checkImageButtons(buy_button2, penguin2);
        checkImageButtons(buy_button3, penguin3);
        checkImageButtons(buy_button4, penguin4);

        buy_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int balance = Integer.parseInt(money.getText().toString());
                int cost = Integer.parseInt(cost2.getText().toString());
                buyPenguin(balance, cost, buy_button2, penguin2);
                saveButtonState(buy_button2.isEnabled(), buy_button2.getText().toString(),
                        buy_button2.getBackgroundTintList().getDefaultColor(),
                        penguin2.isClickable(), 1);
            }
        });
        buy_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int balance = Integer.parseInt(money.getText().toString());
                int cost = Integer.parseInt(cost3.getText().toString());
                buyPenguin(balance, cost, buy_button3, penguin3);
                saveButtonState(buy_button3.isEnabled(), buy_button3.getText().toString(),
                        buy_button3.getBackgroundTintList().getDefaultColor(),
                        penguin3.isClickable(), 2);
            }
        });
        buy_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int balance = Integer.parseInt(money.getText().toString());
                int cost = Integer.parseInt(cost4.getText().toString());
                buyPenguin(balance, cost, buy_button4, penguin4);
                saveButtonState(buy_button4.isEnabled(), buy_button4.getText().toString(),
                        buy_button4.getBackgroundTintList().getDefaultColor(),
                        penguin4.isClickable(), 3);
            }
        });

        penguin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                newImagePenguin(1);
            }
        });

        penguin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newImagePenguin(2);
            }
        });

        penguin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newImagePenguin(3);
            }
        });

        penguin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newImagePenguin(4);
            }
        });


        main_button_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(v);
            }
        });
    }

    public void retButtonState(Button button, ImageButton imageButton, int index){
        saveInstance = getSharedPreferences(SAVED, MODE_PRIVATE);
        boolean isEnable = saveInstance.getBoolean("CurrentIndex " + index,
                button.isEnabled());
        String buttonText = saveInstance.getString("buttonText " + index,
                button.getText().toString());
        int buttonBackgroundColor = saveInstance.getInt("buttonBackgroundColor " + index,
                button.getBackgroundTintList().getDefaultColor());
        boolean isClickable = saveInstance.getBoolean("imageButton" + index,
                imageButton.isClickable());
        button.setEnabled(isEnable);
        button.setText(buttonText);
        button.setBackgroundTintList(ColorStateList.valueOf(buttonBackgroundColor));
        imageButton.setClickable(isClickable);
    }

    public void saveButtonState(boolean isEnabled, String text, int backgroundColor,
                                boolean isClickable, int index) {
        saveInstance = getSharedPreferences(SAVED, MODE_PRIVATE);
        editor = saveInstance.edit();
        editor.putBoolean("CurrentIndex " + index, isEnabled);
        editor.putString("buttonText " + index, text);
        editor.putInt("buttonBackgroundColor " + index, backgroundColor);
        editor.putBoolean("imageButton" + index, isClickable);
        editor.apply();
    }

    public void checkImageButtons(Button button, ImageButton imageButton){
        if(button.isEnabled()){
            imageButton.setEnabled(false);
        }
    }

    public void buyPenguin(int moneys, int cost, Button button, ImageButton imageButton) {
        if (moneys >= cost) {
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
            moneys = moneys - cost;
            gameBalance.setBalance(moneys);
            updateBalance();
            button.setEnabled(false);
            button.setText("Куплено");
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.col1));
            imageButton.setEnabled(true);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), message2, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void updateBalance() {
        int balance = gameBalance.getBalance();
        money.setText(String.valueOf(balance));
    }

    public void startNewActivity(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("balanceNew", money.getText().toString());
        startActivity(intent);
    }

    public void newImagePenguin(int index){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("change_image", true);
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
        gameBalance.saveBalance(this); // Сохранение баланса при выходе из приложения
    }
}