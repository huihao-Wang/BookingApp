package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingCourt_details extends AppCompatActivity {

    private String courtName, date, dateCombine, userID;
    private String time_10AM = "", time_11AM = "", time_12PM = "",
                   time_1PM = "", time_2PM = "", time_3PM = "",
                   time_4PM = "", time_5PM = "", time_6PM = "",
                   time_7PM = "", time_8PM = "", time_9PM = "";
    private int courtImg, price = 0, hour = 0, historyNum;
    private int count10AM = 0, count11AM = 0, count12PM = 0, count1PM = 0, count2PM = 0, count3PM = 0, count4PM = 0,
                count5PM = 0, count6PM = 0, count7PM = 0, count8PM = 0, count9PM = 0;
    private TextView tvCourtName, tvCourtDate, tvPrice;
    private ImageView imgCourt, img10AM_11AM, img11AM_12PM, img12PM_1PM, img1PM_2PM, img2PM_3PM, img3PM_4PM, img4PM_5PM,
            img5PM_6PM, img6PM_7PM, img7PM_8PM, img8PM_9PM, img9PM_10PM;
    private Button btnBook;
    private ProgressBar progressBar;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_court_details);

        tvCourtName = findViewById(R.id.tvCourtName);
        tvCourtDate = findViewById(R.id.tvDate);
        tvPrice = findViewById(R.id.tvPrice);
        imgCourt = findViewById(R.id.imgCourt);

        img10AM_11AM = findViewById(R.id.img_tick_10AM_11AM);
        img11AM_12PM = findViewById(R.id.img_tick_11AM_12PM);
        img12PM_1PM = findViewById(R.id.img_tick_12PM_1PM);
        img1PM_2PM = findViewById(R.id.img_tick_1PM_2PM);
        img2PM_3PM = findViewById(R.id.img_tick_2PM_3PM);
        img3PM_4PM = findViewById(R.id.img_tick_3PM_4PM);
        img4PM_5PM = findViewById(R.id.img_tick_4PM_5PM);
        img5PM_6PM = findViewById(R.id.img_tick_5PM_6PM);
        img6PM_7PM = findViewById(R.id.img_tick_6PM_7PM);
        img7PM_8PM = findViewById(R.id.img_tick_7PM_8PM);
        img8PM_9PM = findViewById(R.id.img_tick_8PM_9PM);
        img9PM_10PM = findViewById(R.id.img_tick_9PM_10PM);

        btnBook = findViewById(R.id.btnBook);

        progressBar = findViewById(R.id.progressBar_getBookingImg);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        getData();
        setData();

        DocumentReference history_1 = db.collection("Users").document(userID);
        history_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String n = documentSnapshot.getString("History");

                historyNum = Integer.parseInt(n);
            }
        });

        DocumentReference documentReference = db.collection("Calendar Checking").document(dateCombine)
                .collection("Badminton Court").document(courtName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentReference documentReference_1 = db.collection("Calendar").document(dateCombine)
                            .collection("Badminton Court").document(courtName);

                    documentReference_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            progressBar.setVisibility(View.GONE);

                            String time = null;

                            time = documentSnapshot.getString("10AM - 11AM");
                            img10AM_11AM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img10AM_11AM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("11AM - 12PM");
                            img11AM_12PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img11AM_12PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("12PM - 1 PM");
                            img12PM_1PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img12PM_1PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("1 PM - 2 PM");
                            img1PM_2PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img1PM_2PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("2 PM - 3 PM");
                            img2PM_3PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img2PM_3PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("3 PM - 4 PM");
                            img3PM_4PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img3PM_4PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("4 PM - 5 PM");
                            img4PM_5PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img4PM_5PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("5 PM - 6 PM");
                            img5PM_6PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img5PM_6PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("6 PM - 7 PM");
                            img6PM_7PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img6PM_7PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("7 PM - 8 PM");
                            img7PM_8PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img7PM_8PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("8 PM - 9 PM");
                            img8PM_9PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img8PM_9PM.setEnabled(false);
                            }

                            time = documentSnapshot.getString("9 PM - 10PM");
                            img9PM_10PM.setImageResource(setBookingImage(time));
                            if (!time.equals("")){
                                img9PM_10PM.setEnabled(false);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookingCourt_details.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(BookingCourt_details.this, "Data get unsuccessful", Toast.LENGTH_SHORT).show();
                }

            }
        });

        img10AM_11AM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count10AM = (count10AM+1) % 2;

                if (count10AM == 1){
                    img10AM_11AM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_10AM = userID;
                }

                else{
                    img10AM_11AM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_10AM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();
            }
        });

        img11AM_12PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count11AM = (count11AM+1) % 2;

                if (count11AM == 1){
                    img11AM_12PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_11AM = userID;
                }

                else{
                    img11AM_12PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_11AM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img12PM_1PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count12PM = (count12PM+1) % 2;

                if (count12PM == 1){
                    img12PM_1PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_12PM = userID;
                }

                else{
                    img12PM_1PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_12PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img1PM_2PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count1PM = (count1PM+1) % 2;

                if (count1PM == 1){
                    img1PM_2PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_1PM = userID;
                }

                else{
                    img1PM_2PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_1PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img2PM_3PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2PM = (count2PM+1) % 2;

                if (count2PM == 1){
                    img2PM_3PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_2PM = userID;
                }

                else{
                    img2PM_3PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_2PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img3PM_4PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count3PM = (count3PM+1) % 2;

                if (count3PM == 1){
                    img3PM_4PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_3PM = userID;
                }

                else{
                    img3PM_4PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_3PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img4PM_5PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count4PM = (count4PM+1) % 2;

                if (count4PM == 1){
                    img4PM_5PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_4PM = userID;
                }

                else{
                    img4PM_5PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_4PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img5PM_6PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count5PM = (count5PM+1) % 2;

                if (count5PM == 1){
                    img5PM_6PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_5PM = userID;
                }

                else{
                    img5PM_6PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_5PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img6PM_7PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count6PM = (count6PM+1) % 2;

                if (count6PM == 1){
                    img6PM_7PM.setImageResource(R.drawable.free_booking_green);
                    price+=10;
                    hour+=1;
                    time_6PM = userID;
                }

                else{
                    img6PM_7PM.setImageResource(R.drawable.free_booking);
                    price-=10;
                    hour-=1;
                    time_6PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img7PM_8PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count7PM = (count7PM+1) % 2;

                if (count7PM == 1){
                    img7PM_8PM.setImageResource(R.drawable.free_booking_green);
                    price+=15;
                    hour+=1;
                    time_7PM = userID;
                }

                else{
                    img7PM_8PM.setImageResource(R.drawable.free_booking);
                    price-=15;
                    hour-=1;
                    time_7PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img8PM_9PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count8PM = (count8PM+1) % 2;

                if (count8PM == 1){
                    img8PM_9PM.setImageResource(R.drawable.free_booking_green);
                    price+=15;
                    hour+=1;
                    time_8PM = userID;
                }

                else{
                    img8PM_9PM.setImageResource(R.drawable.free_booking);
                    price-=15;
                    hour-=1;
                    time_8PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        img9PM_10PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count9PM = (count9PM+1) % 2;

                if (count9PM == 1){
                    img9PM_10PM.setImageResource(R.drawable.free_booking_green);
                    price+=15;
                    hour+=1;
                    time_9PM = userID;
                }

                else{
                    img9PM_10PM.setImageResource(R.drawable.free_booking);
                    price-=15;
                    hour-=1;
                    time_9PM = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update the history number
                DocumentReference history = db.collection("Users").document(userID);
                history.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String history_number = documentSnapshot.getString("History");
                        historyNum= Integer.parseInt(history_number);

                        historyNum++;
                        addHistoryNum(historyNum + "");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BookingCourt_details.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                //Update the time booking to hold userID
                DocumentReference Update = db.collection("Calendar").document(dateCombine)
                        .collection("Badminton Court").document(courtName);

                ArrayList<String> getTime = new ArrayList<>();

                if (!time_10AM.equals("")){
                    Update.update("10AM - 11AM", time_10AM);
                    getTime.add("10AM - 11AM");
                }

                if (!time_11AM.equals("")){
                    Update.update("11AM - 12PM", time_11AM);
                    getTime.add("11AM - 12PM");
                }

                if (!time_12PM.equals("")){
                    Update.update("12PM - 1 PM", time_12PM);
                    getTime.add("12PM - 1 PM");
                }

                if (!time_1PM.equals("")){
                    Update.update("1 PM - 2 PM", time_1PM);
                    getTime.add("1 PM - 2 PM");
                }

                if (!time_2PM.equals("")){
                    Update.update("2 PM - 3 PM", time_2PM);
                    getTime.add("2 PM - 3 PM");
                }

                if (!time_3PM.equals("")){
                    Update.update("3 PM - 4 PM", time_3PM);
                    getTime.add("3 PM - 4 PM");
                }

                if (!time_4PM.equals("")){
                    Update.update("4 PM - 5 PM", time_4PM);
                    getTime.add("4 PM - 5 PM");
                }

                if (!time_5PM.equals("")){
                    Update.update("5 PM - 6 PM", time_5PM);
                    getTime.add("5 PM - 6 PM");
                }

                if (!time_6PM.equals("")){
                    Update.update("6 PM - 7 PM", time_6PM);
                    getTime.add("6 PM - 7 PM");
                }

                if (!time_7PM.equals("")){
                    Update.update("7 PM - 8 PM", time_7PM);
                    getTime.add("7 PM - 8 PM");
                }

                if (!time_8PM.equals("")){
                    Update.update("8 PM - 9 PM", time_8PM);
                    getTime.add("8 PM - 9 PM");
                }

                if (!time_9PM.equals("")){
                    Update.update("9 PM - 10PM", time_9PM);
                    getTime.add("9 PM - 10PM");
                }


                //Clear all the booking time
                time_10AM = "";
                time_11AM = "";
                time_12PM = "";
                time_1PM  = "";
                time_2PM  = "";
                time_3PM  = "";
                time_4PM  = "";
                time_5PM  = "";
                time_6PM  = "";
                time_7PM  = "";
                time_8PM  = "";
                time_9PM  = "";


                //Get the time booking to set the booking image and enable
                DocumentReference documentReference_2 = db.collection("Calendar").document(dateCombine)
                        .collection("Badminton Court").document(courtName);

                documentReference_2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String time = null;

                        time = documentSnapshot.getString("10AM - 11AM");
                        img10AM_11AM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img10AM_11AM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("11AM - 12PM");
                        img11AM_12PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img11AM_12PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("12PM - 1 PM");
                        img12PM_1PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img12PM_1PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("1 PM - 2 PM");
                        img1PM_2PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img1PM_2PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("2 PM - 3 PM");
                        img2PM_3PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img2PM_3PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("3 PM - 4 PM");
                        img3PM_4PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img3PM_4PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("4 PM - 5 PM");
                        img4PM_5PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img4PM_5PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("5 PM - 6 PM");
                        img5PM_6PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img5PM_6PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("6 PM - 7 PM");
                        img6PM_7PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img6PM_7PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("7 PM - 8 PM");
                        img7PM_8PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img7PM_8PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("8 PM - 9 PM");
                        img8PM_9PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img8PM_9PM.setEnabled(false);
                        }

                        time = documentSnapshot.getString("9 PM - 10PM");
                        img9PM_10PM.setImageResource(setBookingImage(time));
                        if (!time.equals("")){
                            img9PM_10PM.setEnabled(false);
                        }

                    }
                });

                //Store the time booking to check history
                DocumentReference storeCheckHistory = db.collection("Check History").document(userID)
                        .collection("History").document(dateCombine);
                Map<String, Object> status = new HashMap<>();
                status.put("Status", "Yes");
                storeCheckHistory.set(status);

                //Store the time booking to user history
                DocumentReference storeHistory = db.collection("Users").document(userID)
                        .collection("History").document("history " + historyNum);
                Map<String, Object> historyData = new HashMap<>();
                historyData.put("Badminton Hall", courtName);
                historyData.put("Badminton Image", courtImg + "");
                historyData.put("Date", date);
                historyData.put("Calendar Date", dateCombine);
                historyData.put("Price", price + "");
                historyData.put("Hour", hour + "");

                for (int i=0; i<getTime.size(); i++){
                    int count = i;

                    historyData.put("Time " + ++count, getTime.get(i));

                }

                storeHistory.set(historyData);

                getTime.clear();

                price = 0;
                hour  = 0;

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

                Toast.makeText(BookingCourt_details.this, "Book Successful", Toast.LENGTH_LONG).show();
            }
        });

    }// End onCreate

    public void getData(){
        courtName = getIntent().getStringExtra("CourtName");
        courtImg = getIntent().getIntExtra("CourtImg", 1);
        date = getIntent().getStringExtra("Date");
        dateCombine = getIntent().getStringExtra("DateCombine");
    }

    public void setData(){
        tvCourtName.setText(courtName);
        imgCourt.setImageResource(courtImg);
        tvCourtDate.setText(date);
    }

    private void addHistoryNum(String historyCount){
        DocumentReference documentReference = db.collection("Users").document(userID);
        documentReference.update("History", historyCount);
    }

    public int setBookingImage(String time){
        int img;

        if (time.equals(userID)){
            img = R.drawable.booking_blue;
        }

        else if(time.equals("")){
            img = R.drawable.free_booking;
        }

        else {
            img = R.drawable.booking_red;
        }

        return img;
    }

    public void setBtnBookEnable(){
        if (price == 0){
            btnBook.setEnabled(false);
            btnBook.setBackgroundResource(R.drawable.custom_button_grey);
            btnBook.setTextColor(Color.parseColor("#B2BABB"));
        }

        else{
            btnBook.setEnabled(true);
            btnBook.setBackgroundResource(R.drawable.custom_sign_up_button);
            btnBook.setTextColor(Color.parseColor("#FF018786"));
        }
    }
}