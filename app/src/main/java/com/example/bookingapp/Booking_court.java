package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Booking_court extends AppCompatActivity {

    private TextView tvDate;
    private String dateCombine, userID;
    private ArrayList<Court> mCourt;
    private RecyclerView recyclerView;
    private BookingCourtAdapter bookingCourtAdapter;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_court);

        tvDate = findViewById(R.id.tvDate);
        recyclerView = findViewById(R.id.badminton_recyclerView);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        int day = intent.getIntExtra("Day",0);
        int month = intent.getIntExtra("Month",0);
        int year = intent.getIntExtra("Year",0);

        dateCombine = day + " " + month + " " + year;
        tvDate.setText(date);

        //Create calendar checking
        DocumentReference calendar_checking = db.collection("Calendar Checking").document(dateCombine)
                .collection("Badminton Court").document("Badminton Hall Check");
        Map<String, Object> check_Calendar = new HashMap<>();
        check_Calendar.put("Status", "Yes");
        calendar_checking.set(check_Calendar);

        //Create Calendar
        DocumentReference documentReference = db.collection("Calendar").document(dateCombine);
        Map<String, Object> calendar = new HashMap<>();
        calendar.put("Status", "Yes");
        documentReference.set(calendar);

        //Set the Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the ArrayLIst that will contain the data
        mCourt = new ArrayList<>();

        bookingCourtAdapter = new BookingCourtAdapter(this, mCourt, date, dateCombine);
        recyclerView.setAdapter(bookingCourtAdapter);

        //Get the data
        initializeData();

    } // End onCreate

    private void initializeData() {
        //Get the resources from the XML file
        String[] courtName = getResources().getStringArray(R.array.court_name);
        TypedArray courtResImg = getResources().obtainTypedArray(R.array.court_img);

        //Clear the existing data (to avoid duplication)
        mCourt.clear();

        //Create the ArrayList of Sports objects with the titles and information about each sport
        for (int i=0; i<courtName.length; i++){
            mCourt.add(new Court(courtName[i], "", dateCombine, "", courtResImg.getResourceId(i, 0), 0, 0));
        }

        courtResImg.recycle();

        //Notify the adapter of the change
        bookingCourtAdapter.notifyDataSetChanged();
    }
}