package com.example.test2;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import android.widget.ImageView;
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
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    TimerStatus timerStatus = TimerStatus.STOPPED;
    ProgressBar progressBarCircle;
    EditText editTextMinute;
    TextView textViewTime;
    ImageView imageViewReset;
    ImageView imageViewStartStop;
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
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
                break;
            case R.id.listview2:
                ListElementsArrayList.add(textViewTime.getText().toString());
                adapter.notifyDataSetChanged();
                break;
        }
    }


    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }



    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {


            setTimerValues();

            setProgressBarValues();

            imageViewReset.setVisibility(View.VISIBLE);

            imageViewStartStop.setImageResource(R.drawable.icon_stop);

            editTextMinute.setEnabled(false);

            timerStatus = TimerStatus.STARTED;

            startCountDownTimer();

        } else {


            imageViewReset.setVisibility(View.GONE);

            imageViewStartStop.setImageResource(R.drawable.icon_start);

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

                imageViewReset.setVisibility(View.GONE);

                imageViewStartStop.setImageResource(R.drawable.icon_start);

                editTextMinute.setEnabled(true);

                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }


    private void stopCountDownTimer() {
        countDownTimer.cancel();
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
        imageViewReset = v.findViewById(R.id.imageViewReset);
        imageViewStartStop = v.findViewById(R.id.imageViewStartStop);
        lap2 = v.findViewById(R.id.button5);
        listView = v.findViewById(R.id.listview2);

            imageViewReset.setOnClickListener(this);
            imageViewStartStop.setOnClickListener(this);

            handler = new Handler();

            ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

            adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()),
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