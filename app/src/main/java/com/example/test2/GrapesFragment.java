package com.example.test2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class GrapesFragment extends Fragment implements View.OnClickListener {

    private  TextView countdownTimerText;
    private  EditText minutes;
    private  Button startTimer, resetTimer;
    private  CountDownTimer countDownTimer;

    public GrapesFragment() {
        // Required empty public constructor
    }

    private void setListeners() {
        startTimer.setOnClickListener(this);
        resetTimer.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTimer:

                if (countDownTimer == null) {
                    String getMinutes = minutes.getText().toString();

                    if (!getMinutes.equals("") && getMinutes.length() > 0) {
                        int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;

                        startTimer(noOfMinutes);
                        startTimer.setText(getString(R.string.stop_timer));

                    } else
                        Toast.makeText(getContext(), "Please enter no. of Minutes.", Toast.LENGTH_SHORT).show();
                } else {

                    stopCountdown();
                    startTimer.setText(getString(R.string.start_timer));
                }
                break;
            case R.id.resetTimer:
                stopCountdown();
                startTimer.setText(getString(R.string.start_timer));
                countdownTimerText.setText(getString(R.string.timer));
                break;
        }
    }

    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;

                @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                countdownTimerText.setText(hms);//set text
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {

                countdownTimerText.setText("TIME'S UP!!");
                countDownTimer = null;
                startTimer.setText(getString(R.string.start_timer));
            }
        }.start();

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_grapes, container, false);
        countdownTimerText = (TextView) v.findViewById(R.id.countdownText);
        minutes = (EditText) v.findViewById(R.id.enterMinutes);
        startTimer = (Button) v.findViewById(R.id.startTimer);
        resetTimer = (Button) v.findViewById(R.id.resetTimer);

        setListeners();
        return v;
    }
}