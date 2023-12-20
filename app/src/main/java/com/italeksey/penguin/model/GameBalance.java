package com.italeksey.penguin.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class GameBalance{
    private static final String BALANCE = "condition_balance";
    private int balance;
    public int getBalance() {
        return balance;
    }
    public void setBalance(int value) {
        balance = value;
    }

    public void saveBalance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BALANCE",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BALANCE, balance);
        editor.apply();
    }

    public void loadBalance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BALANCE",
                Context.MODE_PRIVATE);
        balance = sharedPreferences.getInt(BALANCE, 1000);
    }
}

