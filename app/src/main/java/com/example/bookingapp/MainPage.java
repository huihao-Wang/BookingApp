package com.example.bookingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.DatePicker;

import com.example.bookingapp.ui.main.SectionsPagerAdapter;

import java.util.Calendar;

public class MainPage extends AppCompatActivity{

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fabAddress = findViewById(R.id.fab_address);

        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Booking App");
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Set to the max date
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.DAY_OF_MONTH, day + 14);
        maxCalendar.set(Calendar.MONTH, month);
        maxCalendar.set(Calendar.YEAR, year);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainPage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //Store the data in string
                        String date = dayOfMonth + "/" + month + "/" + year;

                        Intent intent = new Intent(MainPage.this, Booking_court.class);
                        intent.putExtra("Day", dayOfMonth);
                        intent.putExtra("Month", month);
                        intent.putExtra("Year", year);
                        intent.putExtra("Date", date);
                        startActivity(intent);

                    }
                },year,month,day
                );
                //Disable past date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                //Disable future date
                datePickerDialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());

                //Show date picker dialog
                datePickerDialog.show();

            }
        });

        fabAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this, map.class));
            }
        });
    }

}