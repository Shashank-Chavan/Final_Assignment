package com.example.quizapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>implements Recycler {

private List<Quiz> quiz;
private Context context;
private final Recycler recyclerviewinterface;
    public MyAdapter(List<Quiz> quiz, Recycler recyclerviewinterface){
        this.quiz = quiz;
        this.recyclerviewinterface =  recyclerviewinterface;
    }
        void updateQuizList(List<Quiz> quiz) {
        this.quiz.clear();
        this.quiz= quiz;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.card,parent,false);
        return new MyViewHolder(view,recyclerviewinterface);

    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Quiz q = quiz.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.question.setText(q.getQuestion());
        myViewHolder.options.setText(q.getOption());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                QuestionFragment fragment = new QuestionFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.mail_container,fragment).addToBackStack(null).commit();
                /*Bundle bundle = new Bundle();
                bundle.putString("que", q.getQuestion().toString());
                bundle.putString("opt", q.getOption().toString());*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return quiz.size();
    }

    @Override
    public void onItemClick(int position) {
        Fragment fragment = new QuestionFragment();
       FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
       fragmentManager.beginTransaction().replace(R.id.mail_container,fragment).addToBackStack(null).commit();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView options;
        public MyViewHolder(@NonNull View itemView, Recycler recyclerviewinterface) {
            super(itemView);
            question = itemView.findViewById(R.id.Questions);
            options = itemView.findViewById(R.id.Options);
            QuestionFragment fragment = new QuestionFragment();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* AppCompatActivity activity = (AppCompatActivity)view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.mail_container,new QuestionFragment()).addToBackStack(null).commit();
                    TextView Q1 = view.findViewById(R.id.Question1);
                    TextView O1 = view.findViewById(R.id.Options1);
                    String Q = String.valueOf(question.getText());*/
                if(MyAdapter.this.recyclerviewinterface !=null)
                {
                    int pos = getAdapterPosition();
                }
                recyclerviewinterface.onItemClick(getAdapterPosition());

                }
            });
        }

    }
}
