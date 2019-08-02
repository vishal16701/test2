package com.example.test2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class GrapesFragment extends Fragment implements View.OnClickListener {


    private long timeCountInMilliSeconds = 60000;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    public GrapesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
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

            imageViewReset.setOnClickListener(this);
            imageViewStartStop.setOnClickListener(this);



//        initListeners();
        return v;
    }

}