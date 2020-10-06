package com.salem.messaging;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.salem.messaging.Module.ScheduledModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EditScheduled extends AppCompatActivity {

    Button time,date,finish;
    TextView viewTime,title,viewDate;
    EditText editText;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    public static String x;
    int mYear, mMonth, mDay, mHour, mMinute;

    Intent intent;

    public void setTime(View v){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        viewTime.setText(hourOfDay + ":" + minute);
                        mHour=hourOfDay;
                        mMinute=minute;
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void setDate(View v){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        viewDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mYear=year;
                        mDay=dayOfMonth;
                        mMonth=monthOfYear;
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scheduled);

        time= findViewById(R.id.scheduledtimeButton);
        date=findViewById(R.id.dateButton);
        finish=findViewById(R.id.scheduledFinish);
        viewTime=findViewById(R.id.scheduledtime);
        viewDate=findViewById(R.id.date);
        editText=findViewById(R.id.scheduledMSG);
        title=findViewById(R.id.scheduledTitle);

        intent=getIntent();

        firebaseDatabase= FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        databaseReference=firebaseDatabase.getReference().child(Objects.requireNonNull(user.getDisplayName())).child("Messages")
                .child(Objects.requireNonNull(intent.getStringExtra("number1"))).child("Scheduled");

        viewDate.setText(String.format("%s - %s", intent.getStringExtra("day1"), intent.getStringExtra("month1")));
        title.setText(intent.getStringExtra("title1"));


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c= Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH,mDay);
                c.set(Calendar.MONTH,mMonth);
                c.set(Calendar.YEAR,mYear);
                c.set(Calendar.HOUR,mHour);
                c.set(Calendar.MINUTE,mMinute);
                c.set(Calendar.SECOND,0);
                Intent intent3 = new Intent(EditScheduled.this,MyReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,intent3,0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

                databaseReference.push().setValue(new ScheduledModel(viewDate.getText().toString(),viewTime.getText().toString(),editText.getText().toString()
                ,title.getText().toString()));

                x= editText.getText().toString();

                startActivity(intent3);

            }
        });

    }
}