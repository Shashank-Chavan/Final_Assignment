package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle  i = getIntent().getExtras();

                //if(i.equals(0))
                // {
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();
               /* }
                else{
                    startActivity(new Intent(Splash_activity.this,MainActivity.class));
                    finish();
                }*/

            }
        },1500);
    }
}
