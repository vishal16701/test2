package com.example.test2;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.CountDownTimer;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GrapesFragment extends Fragment implements View.OnClickListener {


    private long timeCountInMilliSeconds = 60000;
    MediaPlayer mediaPlayer;
    Button startpause,reset2;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    TimerStatus timerStatus = TimerStatus.STOPPED;
    ProgressBar progressBarCircle;
    EditText editTextMinute;
    TextView textViewTime;
    CountDownTimer countDownTimer;
    Handler handler;
    Button lap2;
    ListView listView;
    String[] ListElements = new String[]{};
    List<String> ListElementsArrayList;
    ArrayAdapter<String> adapter;

    public GrapesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startpause:
                startStop();
                break;
            case R.id.reset2:
                reset();
                break;
            case R.id.button5:
                ListElementsArrayList.add(textViewTime.getText().toString());
                adapter.notifyDataSetChanged();
                break;
        }
    }



    private void reset() {
        if (timerStatus == TimerStatus.STOPPED) {
            editTextMinute.setEnabled(true);
            ListElementsArrayList.clear();
            textViewTime.setText("00:01:00");
            progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
            progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
        }

        else {
            stopCountDownTimer();
            textViewTime.setText("00:01:00");
            progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
            progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
            set_text("Start");
            editTextMinute.setEnabled(true);
            ListElementsArrayList.clear();
        }

    }
    private void set_text(String output) {
        startpause.setText(output);
    }




    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {


            setTimerValues();

            setProgressBarValues();

            set_text("Pause");

            editTextMinute.setEnabled(false);

            timerStatus = TimerStatus.STARTED;

            startCountDownTimer();

        } else {


            set_text("start");

            editTextMinute.setEnabled(true);

            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }


    private void setTimerValues() {
        int time = 0;
        if (!editTextMinute.getText().toString().isEmpty()) {

            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {

            Toast.makeText(getContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
        }

        timeCountInMilliSeconds = time * 60 * 1000;
    }


    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));


            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));

                setProgressBarValues();

                set_text("Start");


//                mediaPlayer = MediaPlayer.create(getContext(), R.raw.vishal_1);
                mediaPlayer.start();

                editTextMinute.setEnabled(true);

                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }


    private void stopCountDownTimer() {
        countDownTimer.cancel();
        timerStatus= TimerStatus.STOPPED;


    }

    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }



    private String hmsTimeFormatter(long milliSeconds) {

        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }
        @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_grapes, container, false);
        progressBarCircle = v.findViewById(R.id.progressBarCircle);
        editTextMinute = v.findViewById(R.id.editTextMinute);
        textViewTime = v.findViewById(R.id.textViewTime);
        startpause = v.findViewById(R.id.startpause);
        reset2 = v.findViewById(R.id.reset2);
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.vishal_5);


            lap2 = v.findViewById(R.id.button5);
        listView = v.findViewById(R.id.listview2);

            reset2.setOnClickListener(this);
            startpause.setOnClickListener(this);

            handler = new Handler();

            ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));

            adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                    android.R.layout.simple_list_item_1,
                    ListElementsArrayList);

            listView.setAdapter(adapter);


            lap2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ListElementsArrayList.add(textViewTime.getText().toString());

                    adapter.notifyDataSetChanged();

                }
            });


//        initListeners();
        return v;
    }

}