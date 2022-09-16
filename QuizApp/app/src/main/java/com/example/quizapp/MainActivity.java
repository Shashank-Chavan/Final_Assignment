package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements QuizFragment.QuizFragmmentListener {
    private SummaryFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.mail_container,new Setup()).commit();
        fragment = new SummaryFragment();
    }

    @Override
    public void sendTime(int m, int s) {
        fragment.updateTime(m,s);
    }
}