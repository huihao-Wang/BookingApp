package com.example.bookingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;

public class history extends AppCompatActivity {
    private TextView tvNoHistoryData;
    private ArrayList<Court> mCourt = new ArrayList<>();
    private ArrayList<String> spinnerDate = new ArrayList<>();
    private RecyclerView recyclerView;
    private historyDateAdapter historyDateAdapter;
    private Spinner spinner;
    private int count;
    private String userId;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvNoHistoryData = findViewById(R.id.tvNoHistoryData);
        recyclerView = findViewById(R.id.history_recyclerView);
        spinner = findViewById(R.id.spinner);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        spinnerDate.add("All History");

        //get date from firebase
        CollectionReference collectionReference = db.collection("Check History").document(userId)
                .collection("History");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String date = documentSnapshot.getId().trim();

                    addSpinnerDate(date);
                }
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerDate);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner.getSelectedItem().toString().equals("All History")){

                    //to clear the recycler view
                    for (int i=0; i<count; i++){
                        mCourt.remove(0);
                        historyDateAdapter.notifyItemRemoved(0);
                    }

                    count = 0;

                    getAllHistory();

                }

                else{

                    //to clear the recycler view
                    for (int i=0; i<count; i++){
                        mCourt.remove(0);
                        historyDateAdapter.notifyItemRemoved(0);
                    }

                    count = 0;

                    DocumentReference documentReference = db.collection("Users").document(userId);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String count = documentSnapshot.getString("History");
                            int getCount = Integer.parseInt(count);

                            for (int i=--getCount; i>=0; i--){
                                DocumentReference documentReference_1 = db.collection("Users").document(userId)
                                        .collection("History").document("history " + i);
                                documentReference_1.addSnapshotListener(history.this, new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                        String badminton_calendar_date = value.getString("Calendar Date");

                                        if (badminton_calendar_date.equals(spinnerDate.get(position))){
                                            addCount();

                                            String badminton_hall = value.getString("Badminton Hall");
                                            String badminton_img = value.getString("Badminton Image");
                                            String badminton_date = value.getString("Date");
                                            String badminton_hour = value.getString("Hour");
                                            String badminton_price = value.getString("Price");
                                            String badminton_time = "";

                                            int img = Integer.parseInt(badminton_img);
                                            int price = Integer.parseInt(badminton_price);
                                            int hour_count = Integer.parseInt(badminton_hour);

                                            for (int x=1; x<=hour_count; x++){
                                                badminton_time += value.getString("Time " + x) + "\n";
                                            }

                                            addAdapter(badminton_hall, badminton_time, badminton_date, badminton_calendar_date,
                                                    img, price, hour_count);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }//End onCreate

    public void addCount(){
        count++;
    }

    public void getAllHistory(){
        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String historyNum = documentSnapshot.getString("History");
                int count = Integer.parseInt(historyNum);

                if (count<=0){
                    tvNoHistoryData.setVisibility(View.VISIBLE);
                }

                else {
                    tvNoHistoryData.setVisibility(View.GONE);

                    for (int i=--count; i>=0; i--){
                        addCount();

                        DocumentReference documentReference_1 = db.collection("Users").document(userId)
                                .collection("History").document("history " + i);
                        documentReference_1.addSnapshotListener(history.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value.exists()){
                                    String badminton_hall = value.getString("Badminton Hall");
                                    String badminton_img = value.getString("Badminton Image");
                                    String badminton_date = value.getString("Date");
                                    String badminton_calendar_date = value.getString("Calendar Date");
                                    String badminton_hour = value.getString("Hour");
                                    String badminton_price = value.getString("Price");
                                    String badminton_time = "";

                                    int img = Integer.parseInt(badminton_img);
                                    int price = Integer.parseInt(badminton_price);
                                    int hour_count = Integer.parseInt(badminton_hour);

                                    for (int x=1; x<=hour_count; x++){
                                        badminton_time += value.getString("Time " + x) + "\n";
                                    }

                                    addAdapter(badminton_hall, badminton_time, badminton_date, badminton_calendar_date, img, price, hour_count);
                                }

                                else {
                                    Log.d("tag", "onEvent: Document do not exists");
                                }

                            }
                        });
                    }
                }
            }
        });
    }

    public void addAdapter(String courtName, String time, String date, String calendar_date, int courtResImg, int price, int hour){
        mCourt.add(new Court(courtName, time, date, calendar_date, courtResImg, price, hour));

        historyDateAdapter = new historyDateAdapter(history.this, mCourt);
        recyclerView.setAdapter(historyDateAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(history.this));

    }

    public void addSpinnerDate(String date){
        spinnerDate.add(date);
    }

}