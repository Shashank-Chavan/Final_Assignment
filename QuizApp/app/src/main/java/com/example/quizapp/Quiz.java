package com.example.quizapp;

public class Quiz {
private String Question;
private String Option;
Quiz(String Question,String Option){
this.Question = Question;
this.Option = Option;
}
    String getQuestion(){return Question;}
    String getOption(){return Option;}
}
