package com.example.test2;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Objects;
import java.util.Random;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.content.Context.ALARM_SERVICE;

public class AppleFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    //to make our alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    AppleFragment context;
    PendingIntent pending_intent;
    int choose_sound;
    Button Unset;


    public AppleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_apple, container, false);

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        this.context = this;

        // initialize our alarm manager
        alarm_manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        Unset = v.findViewById(R.id.alarm_off);


        //initialize our timepicker
        alarm_timepicker = v.findViewById(R.id.timePicker);

        //initialize our text update box
        update_text = v.findViewById(R.id.update_text);

        // create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        // create an intent to the Alarm Receiver class
        final Intent my_intent = new Intent(getContext(), AlarmReceiver.class);


        // create the spinner in the main UI
        Spinner spinner = v.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set an onclick listener to the onItemSelected method
        spinner.setOnItemSelectedListener(this);

        Unset.setEnabled(false);

        // initialize start button
        Button alarm_on = v.findViewById(R.id.alarm_on);

        // create an onClick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // setting calendar instance with the hour and minute that we picked
                // on the time picker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
                 Log.d("njvk","rhhb");
                // get the int values of the hour and minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // convert the int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                // convert 24-hour time to 12-hour time
                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //10:7 --> 10:07
                    minute_string = "0" + minute;
                }

                // method that changes the update text Textbox
                set_alarm_text("Alarm set for " + hour_string + ":" + minute_string);

                // put in extra string into my_intent
                // tells the clock that you pressed the "alarm on" button
                my_intent.putExtra("extra", "alarm on");

                // put in an extra int into my_intent
                // tells the clock that you want a certain value from the drop-down menu/spinner
                my_intent.putExtra("choice", choose_sound);
                Log.e("The sound id is" , String.valueOf(choose_sound));

                // create a pending intent that delays the intent
                // until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(getContext(), 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);

                Unset.setEnabled(true);
                Toast.makeText(getContext(), "Alarm is set", Toast.LENGTH_SHORT).show();


            }


        });



        // initialize the stop button
        Button alarm_off = v.findViewById(R.id.alarm_off);
        // create an onClick listener to stop the alarm or undo an alarm set

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mcsj","rjiuuc");
                // method that changes the update text Textbox
                set_alarm_text("Alarm off!");

                // cancel the alarm
               alarm_manager.cancel(pending_intent);

                // put extra string into my_intent
                // tells the clock that you pressed the "alarm off" button
                my_intent.putExtra("extra", "alarm off");
                // also put an extra int into the alarm off section
                // to prevent crashes in a Null Pointer Exception
                my_intent.putExtra("choice", choose_sound);

             Log.e("fetg","egr");
            // stop the ringtone
                Objects.requireNonNull(getActivity()).sendBroadcast(my_intent);

              Toast.makeText(getContext(), "Alarm is unset", Toast.LENGTH_SHORT).show();



            }
        });


        return v;

    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        // outputting whatever id the user has selected
        //Toast.makeText(parent.getContext(), "the spinner item is "
        //        + id, Toast.LENGTH_SHORT).show();
        choose_sound = (int) id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



    public static class AlarmReceiver  extends BroadcastReceiver{

        public AlarmReceiver() {
         Log.e("AlarmReceiver","abghf");
            // No args constructor
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("We are in the receiver.", "Yay!");

            // fetch extra strings from the intent
            // tells the app whether the user pressed the alarm on button or the alarm off button
            String get_your_string = Objects.requireNonNull(intent.getExtras()).getString("extra");

            Log.e("What is the key? ", get_your_string);

            // fetch the extra longs from the intent
            // tells the app which value the user picked from the drop down menu/spinner
            Integer get_your_choice = intent.getExtras().getInt("choice");

            Log.e("The choice is ", get_your_choice.toString());

            // create an intent to the ringtone service
            Intent service_intent = new Intent(context, RingtonePlayingService.class);

            // pass the extra string from Receiver to the Ringtone Playing Service
            service_intent.putExtra("extra", get_your_string);
            // pass the extra integer from the Receiver to the Ringtone Playing Service
            service_intent.putExtra("choice", get_your_choice);

            // start the ringtone service
            context.startService(service_intent);

        }

    }

    public static class RingtonePlayingService extends Service {

        MediaPlayer media_song;
        int startId;
        boolean isRunning;

        public RingtonePlayingService() {
            // No args constructor
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i("LocalService", "Received start id " + startId + ": " + intent);

            // fetch the extra string from the alarm on/alarm off values
            String state = Objects.requireNonNull(intent.getExtras()).getString("extra");
            // fetch the choice integer values
            int sound_choice = intent.getExtras().getInt("choice");

            Log.e("Ringtone extra is ", state);
            Log.e("choice is ", Integer.toString(sound_choice));

            // put the notification here, test it out
            Log.d("cvbch","dhgcc");
            // notification
            // set up the notification service
            NotificationManager notify_manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            // set up an intent that goes to the Apple Activity
            Intent intent_main_activity = new Intent(this.getApplicationContext(), AppleFragment.class);
            // set up a pending intent
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                    intent_main_activity, 0);

            // make the notification parameters
            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    .setSmallIcon(R.drawable.alarm4)
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true)
                    .build();


            // this converts the extra strings from the intent
            // to start IDs, values 0 or 1
            assert state != null;
            switch (state) {
                case "alarm on":
                    startId = 1;
                    break;
                case "alarm off":
                    startId = 0;
                    Log.e("Start ID is ", state);
                    break;
                default:
                    startId = 0;
                    break;
            }


            // if there is no music playing, and the user pressed "alarm on"
            // music should start playing
            if (!this.isRunning && startId == 1) {
                Log.e("there is no music, ", "and you want start");

                this.isRunning = true;
                this.startId = 0;

                // set up the start command for the notification
                notify_manager.notify(0, notification_popup);


                // play the sound depending on the passed choice id

                if (sound_choice == 0) {
                    // play a randomly picked audio file
Log.d("vbfjsh","eyuah");
                    int minimum_number = 1;
                    int maximum_number = 3;

                    Random random_number = new Random();
                    int number = random_number.nextInt(maximum_number + minimum_number);
                    Log.e("random number is ", String.valueOf(number));


                    if (number == 1) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_1);
                        media_song.start();
                    } else if (number == 2) {
                        // create an instance of the media player
                        media_song = MediaPlayer.create(this, R.raw.vishal_2);
                        // start the ringtone
                        media_song.start();
                    } else if (number == 3) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_3);
                        media_song.start();
                    } else if (number == 4) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_4);
                        media_song.start();
                    }


                } else if (sound_choice == 1) {
                    // create an instance of the media player
                    media_song = MediaPlayer.create(this, R.raw.vishal_1);
                    // start the ringtone
                    media_song.start();
                } else if (sound_choice == 2) {
                    // create an instance of the media player
                    media_song = MediaPlayer.create(this, R.raw.vishal_2);
                    // start the ringtone
                    media_song.start();
                } else if (sound_choice == 3) {
                    media_song = MediaPlayer.create(this, R.raw.vishal_3);
                    media_song.start();
                } else {
                    media_song = MediaPlayer.create(this, R.raw.vishal_4);
                    media_song.start();
                }


            }

            // if there is music playing, and the user pressed "alarm off"
            // music should stop playing
            else if (this.isRunning && startId == 0) {
                Log.e("there is music, ", "and you want end");
                Log.d("chskc","cgu6tf");
                // stop the ringtone
                media_song.stop();
                media_song.reset();

                this.isRunning = false;
                this.startId = 0;
            }

            // these are if the user presses random buttons
            // just to bug-proof the app
            // if there is no music playing, and the user pressed "alarm off"
            // do nothing
            else if (!this.isRunning) {
                Log.e("there is no music, ", "and you want end");

                this.isRunning = false;
                this.startId = 0;

            }

            // if there is music playing and the user pressed "alarm on"
            // do nothing
            else {
                Log.e("there is music, ", "and you want start");

                this.isRunning = true;
                this.startId = 1;

            }


            return START_NOT_STICKY;
        }

        @Override
        public void onDestroy() {
            // Tell the user we stopped.
            Log.e("on Destroy called", "destroyed");

            super.onDestroy();
            this.isRunning = false;
        }


    }

}