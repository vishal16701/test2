package com.example.test2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class OrangeFragment extends Fragment {

    private TextView textTimeZone, txtCurrentTime, txtTimeZoneTime;
    private long miliSeconds;
    private SimpleDateFormat sdf;
    private Date resultdate;

    public OrangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_orange, container, false);


        Spinner spinnerAvailableID =  v.findViewById(R.id.availableID);
        textTimeZone =  v.findViewById(R.id.timezone);
        txtCurrentTime = v.findViewById(R.id.txtCurrentTime);
        txtTimeZoneTime = v.findViewById(R.id.txtTimeZoneTime);

        String[] idArray = TimeZone.getAvailableIDs();

        sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");

        ArrayAdapter<String> idAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, idArray);

        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvailableID.setAdapter(idAdapter);

        getGMTTime();

        spinnerAvailableID
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        getGMTTime();
                        String selectedId = (String) (parent
                                .getItemAtPosition(position));

                        TimeZone timezone = TimeZone.getTimeZone(selectedId);
                        String TimeZoneName = timezone.getDisplayName();

                        int TimeZoneOffset = timezone.getRawOffset()
                                / (60 * 1000);

                        int hrs = TimeZoneOffset / 60;
                        int mins = TimeZoneOffset % 60;

                        miliSeconds = miliSeconds + timezone.getRawOffset();

                        resultdate = new Date(miliSeconds);
                        System.out.println(sdf.format(resultdate));

                        textTimeZone.setText(TimeZoneName + " : GMT " + hrs + "." + mins);
                        txtTimeZoneTime.setText("" + sdf.format(resultdate));
                        miliSeconds = 0;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

        return v;

    }

    @SuppressLint("SetTextI18n")
    private void getGMTTime() {
        Calendar current = Calendar.getInstance();
        txtCurrentTime.setText("" + current.getTime());

        miliSeconds = current.getTimeInMillis();

        TimeZone tzCurrent = current.getTimeZone();
        int offset = tzCurrent.getRawOffset();
        if (tzCurrent.inDaylightTime(new Date())) {
            offset = offset + tzCurrent.getDSTSavings();
        }

        miliSeconds = miliSeconds - offset;

        resultdate = new Date(miliSeconds);
        System.out.println(sdf.format(resultdate));
    }
}




    // Convert Local Time into GMT time


