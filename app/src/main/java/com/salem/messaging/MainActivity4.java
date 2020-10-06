package com.salem.messaging;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class MainActivity4 extends AppCompatActivity {

    Button date;
    Button exit;
    TextView viewTime;
    TextView viewDate;
    EditText title;
    final Calendar c = Calendar.getInstance();
    private int mYear, mMonth, mDay, mHour, mMinute;

    public void returnTime(View v){
        if(title.getText().toString().matches("")){
            Toast.makeText(this, "You Have to complete all data!!", Toast.LENGTH_LONG).show();
        }else{
            if (mMonth!=0&&mYear!=0){
                Intent intent= new Intent(MainActivity4.this,MainActivity2.class);
                intent.putExtra("day",String.valueOf(mDay));

                String[] shortMonths = new DateFormatSymbols().getShortMonths();
                intent.putExtra("month",shortMonths[mMonth]);

                intent.putExtra("year",String.valueOf(mYear));
                intent.putExtra("hour",String.valueOf(mHour));
                intent.putExtra("minute",String.valueOf(mMinute));
                intent.putExtra("title",title.getText().toString());
                startActivity(intent);
            }else{
                Toast.makeText(this, "You Have to complete all data!!", Toast.LENGTH_LONG).show();
            }
        }
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
        setContentView(R.layout.activity_main4);

        date=findViewById(R.id.dateButton);
        exit=findViewById(R.id.exit);
        viewDate=findViewById(R.id.date);
        viewTime=findViewById(R.id.time);
        title=findViewById(R.id.title);


    }

}