package com.example.test2;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;


public class AppleFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    //to make our alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    AppleFragment context;
    PendingIntent pending_intent;
    int choose_sound;

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
        Toolbar toolbar =  v.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        alarm_timepicker =  v.findViewById(R.id.timePicker);
        update_text =  v.findViewById(R.id.update_text);

        // create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        // create an intent to the Alarm Receiver class
        final Intent my_intent = new Intent(getContext(), AppleFragment.class);

        // initialize start button
        Button alarm_on =  v.findViewById(R.id.alarm_on);

        // create an onClick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // setting calendar instance with the hour and minute that we picked
                // on the time picker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

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
                set_alarm_text("Alarm set to " + hour_string + ":" + minute_string);

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
                alarm_manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);
                Toast.makeText(getContext(), "Alarm is set", Toast.LENGTH_SHORT).show();
            }

        });


        Button alarm_off =  v.findViewById(R.id.alarm_off);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_alarm_text("Alarm off!");

                alarm_manager.cancel(pending_intent);
                pending_intent.cancel();
                Toast.makeText(getContext(), "Alarm is unset", Toast.LENGTH_SHORT).show();

                


                sendBroadcast(my_intent);


            }
        });



        return v;


    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void sendBroadcast(Intent my_intent) {
        my_intent.putExtra("extra", "alarm off");
        my_intent.putExtra("choice", choose_sound);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public class Alarm_Receiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("We are in the receiver.", "Yay!");
            String get_your_string = Objects.requireNonNull(intent.getExtras()).getString("extra");
            Log.e("What is the key? ", get_your_string);
            Integer get_your_choice = intent.getExtras().getInt("choice");
            Log.e("The choice is ", get_your_choice.toString());
            Intent service_intent = new Intent(context, RingtonePlayingService.class);

            service_intent.putExtra("extra", get_your_string);
            service_intent.putExtra("choice", get_your_choice);
            context.startService(service_intent);

        }

    }

    @SuppressLint("Registered")
    public class RingtonePlayingService extends Service {

        MediaPlayer media_song;
        int startId;
        boolean isRunning = true;

        public RingtonePlayingService() {
            alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i("LocalService", "Received start id " + startId + ": " + intent);

            String state = Objects.requireNonNull(intent.getExtras()).getString("extra");
            int sound_choice = intent.getExtras().getInt("sound_choice");

            Log.e("Ringtone extra is ", state);
            Log.e("sound choice is ", Integer.toString(sound_choice));


            NotificationManager notify_manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                    intent_main_activity, 0);

            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("An alarm is Ringing!")
                    .setContentText("Click me!")
                    .setSmallIcon(R.drawable.alarm4)
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true)
                    .build();



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


            if (!this.isRunning && startId == 1) {
                Log.e("there is no music, ", "and you want start");

                this.isRunning = true;
                this.startId = 0;

                notify_manager.notify(0, notification_popup);

                if (sound_choice == 0) {

                    int minimum_number = 1;
                    int maximum_number = 8;

                    Random random_number = new Random();
                    int number = random_number.nextInt(maximum_number + minimum_number);
                    Log.e("random number is " , String.valueOf(number));


                    if (number == 1) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_1);
                        media_song.start();
                    }
                    else if (number == 2) {
                        // create an instance of the media player
                        media_song = MediaPlayer.create(this, R.raw.vishal_2);
                        // start the ringtone
                        media_song.start();
                    }
                    else if (number == 3) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_3);
                        media_song.start();
                    }
                    else if (number == 4) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_4);
                        media_song.start();
                    }
                    else if (number == 5) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_5);
                        media_song.start();
                    }
                    else if (number == 6) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_6);
                        media_song.start();
                    }
                    else if (number == 7) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_7);
                        media_song.start();
                    }
                    else if (number == 8) {
                        media_song = MediaPlayer.create(this, R.raw.vishal_8);
                        media_song.start();
                    }
                    else  {
                        media_song = MediaPlayer.create(this, R.raw.vishal_9);
                        media_song.start();
                    }


                }
                else if (sound_choice == 1) {
                    // create an instance of the media player
                    media_song = MediaPlayer.create(this, R.raw.vishal_1);
                    // start the ringtone
                    media_song.start();
                }
                else if (sound_choice == 2) {
                    // create an instance of the media player
                    media_song = MediaPlayer.create(this, R.raw.vishal_2);
                    // start the ringtone
                    media_song.start();
                }
                else if (sound_choice == 3) {
                    media_song = MediaPlayer.create(this, R.raw.vishal_3);
                    media_song.start();
                }
                else if (sound_choice == 4) {
                    media_song = MediaPlayer.create(this, R.raw.vishal_4);
                    media_song.start();
                }
                else if (sound_choice == 5) {
                    media_song = MediaPlayer.create(this, R.raw.vishal_5);
                    media_song.start();
                }
                else if (sound_choice == 6) {
                    media_song = MediaPlayer.create(this, R.raw.vishal_6);
                    media_song.start();
                }
                else if (sound_choice == 7) {
                    media_song = MediaPlayer.create(this, R.raw.vishal_7);
                    media_song.start();
                }
                else if (sound_choice == 8) {
                    media_song = MediaPlayer.create(this, R.raw.vishal_8);
                    media_song.start();
                }
                else  {
                    media_song = MediaPlayer.create(this, R.raw.vishal_9);
                    media_song.start();
                }

            }

            else if (this.isRunning && startId == 0) {
                Log.e("there is music, ", "and you want end");

                media_song.stop();
                media_song.reset();

                this.isRunning = false;
                this.startId = 0;
            }

            else if (!this.isRunning) {
                Log.e("there is no music, ", "and you want end");

                this.isRunning = false;
                this.startId = 0;

            }

            else {
                Log.e("there is music, ", "and you want start");

                this.isRunning = true;
                this.startId = 1;

            }

            return START_STICKY;
        }

        @Override
        public void onDestroy() {

            Log.e("on Destroy called", "ta da");

            super.onDestroy();
            this.isRunning = false;
        }
    }
}
