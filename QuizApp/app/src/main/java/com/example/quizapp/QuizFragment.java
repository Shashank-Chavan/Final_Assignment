package com.example.quizapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment implements Recycler {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private QuizFragmmentListener listener;
    public interface QuizFragmmentListener{
        void sendTime(int m,int s);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private int mins = 9;
    private int secs = 60;
    private MyAdapter myAdapter;
    ProgressDialog spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Button button;

        View view =  inflater.inflate(R.layout.fragment_quiz, container, false);
        button = (Button) view.findViewById(R.id.Submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder AD = new AlertDialog.Builder(view.getContext());
                AD.setTitle("Submit");
                AD.setMessage("Are you sure you really want to submit the test");
                AD.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.mail_container,new SummaryFragment()).commit();
                        /*QuizFragment fragment = new QuizFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("mins",7);
                        bundle.putInt("secs",30);
                        fragment.setArguments(bundle);*/
                        listener.sendTime(mins,secs);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });
        final TextView min = view.findViewById(R.id.Minutes);
        final TextView sec = view.findViewById(R.id.Seconds);
        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long l) {
                secs--;
                min.setText(String.valueOf(mins));
                if(secs==0){
                    mins--;
                    min.setText(String.valueOf(mins));
                    secs = 59;
                    QuizFragment fragment = new QuizFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("mins",7);
                    bundle.putInt("secs",30);
                    fragment.setArguments(bundle);
                }
                else {
                    sec.setText(String.valueOf(secs));
                    QuizFragment fragment = new QuizFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("mins",7);
                    bundle.putInt("secs",30);
                    fragment.setArguments(bundle);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

        List<Quiz> quiz = new ArrayList<>();

        RecyclerView recyclerView  = view.findViewById(R.id.Recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myAdapter = new MyAdapter(quiz,  this);
        recyclerView.setAdapter(myAdapter);
        new QuizAsyncTask().execute("https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/db.json");
        return view;

    }
    public void Execute(){
        new QuizAsyncTask().execute("https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/db.json");
    }

    @Override
    public void onItemClick(int position) {

    }

    private class QuizAsyncTask extends AsyncTask<String,String,List<Quiz>>{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*spinner = new ProgressDialog(this);
        spinner.setMessage("Please wait...It is downloading");
        spinner.setIndeterminate(false);
        spinner.setCancelable(false);
        spinner.show();*/
    }

    @Override
    protected List<Quiz> doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        List<Quiz> quiz = new ArrayList<>();
        try {
            URL url = new URL(strings[0]);
            urlConnection =(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br =new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder json  = new StringBuilder();
            String line;
            while((line = br.readLine())!=null){
                json.append(line);
            }

            JSONObject jsonObject = new JSONObject(json.toString());
            JSONArray jsonArray =jsonObject.getJSONArray("questions");
            int count = 0;
            String q,o;
            while(count<jsonArray.length())
            {
                JSONObject QuizObject = jsonArray.getJSONObject(count);
                q = QuizObject.getString("question");
                o = QuizObject.getString("options");

                Quiz quiz1 = new Quiz(q,o);
                quiz.add(quiz1);
                count++;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return quiz;
    }

    @Override
    protected void onPostExecute(List<Quiz> quizzes) {
        super.onPostExecute(quizzes);
        if(quizzes != null) {
           // spinner.hide();
            myAdapter.updateQuizList(quizzes);
        }else {
            spinner.show();
        }
    }
}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (QuizFragmmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}