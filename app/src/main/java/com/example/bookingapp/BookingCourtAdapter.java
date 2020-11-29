package com.example.bookingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class BookingCourtAdapter extends RecyclerView.Adapter<BookingCourtAdapter.BookingCourtViewHolder> {
    private Context context;
    private ArrayList<Court> mCourt;
    private String date, dateCombine, userID;
    private Boolean checkCalendar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public BookingCourtAdapter(Context c, ArrayList<Court> court, String d, String dCombine){
        context = c;
        mCourt = court;
        date = d;
        dateCombine = dCombine;
    }

    @NonNull
    @Override
    public BookingCourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.booking_court, parent, false);
        return new BookingCourtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingCourtAdapter.BookingCourtViewHolder holder, int position) {
        holder.tv_courtName.setText(mCourt.get(position).getCourtName());
        holder.img_court.setImageResource(mCourt.get(position).getCourtImg());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userID = firebaseAuth.getCurrentUser().getUid();

                //Check the badminton court has been created or not
                CollectionReference collectCalendar = db.collection("Calendar Checking").document(dateCombine)
                        .collection("Badminton Court");
                collectCalendar.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            String badmintonHall = documentSnapshots.getId();

                            checkCalendar = false;

                            if (badmintonHall.equals(mCourt.get(position).getCourtName())){
                                checkCalendar = true;
                                break;
                            }

                        } //End for loop

                        //if not then create a new badminton court time
                        createNewCalendar(checkCalendar, position);
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(context, BookingCourt_details.class);
                            intent.putExtra("CourtName", mCourt.get(position).getCourtName());
                            intent.putExtra("CourtImg", mCourt.get(position).getCourtImg());
                            intent.putExtra("Date", date);
                            intent.putExtra("DateCombine", dateCombine);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourt.size();
    }

    public class BookingCourtViewHolder extends RecyclerView.ViewHolder{
        TextView tv_courtName;
        ImageView img_court;
        ConstraintLayout mainLayout;

        public BookingCourtViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_courtName = itemView.findViewById(R.id.courtName);
            img_court = itemView.findViewById(R.id.imageViewCourt);
            mainLayout = itemView.findViewById(R.id.court_mainLayout);
        }
    }

    public void createNewCalendar(Boolean check, int position) {
        if (!check) {
            //Create badminton court time
            DocumentReference documentReference = db.collection("Calendar").document(dateCombine)
                    .collection("Badminton Court").document(mCourt.get(position).getCourtName());
            Map<String, Object> calendarStatus = new HashMap<>();
            calendarStatus.put("10AM - 11AM", "");
            calendarStatus.put("11AM - 12PM", "");
            calendarStatus.put("12PM - 1 PM", "");
            calendarStatus.put("1 PM - 2 PM", "");
            calendarStatus.put("2 PM - 3 PM", "");
            calendarStatus.put("3 PM - 4 PM", "");
            calendarStatus.put("4 PM - 5 PM", "");
            calendarStatus.put("5 PM - 6 PM", "");
            calendarStatus.put("6 PM - 7 PM", "");
            calendarStatus.put("7 PM - 8 PM", "");
            calendarStatus.put("8 PM - 9 PM", "");
            calendarStatus.put("9 PM - 10PM", "");
            documentReference.set(calendarStatus);

            //Create checking badminton court
            DocumentReference calendar_checking = db.collection("Calendar Checking")
                    .document(dateCombine).collection("Badminton Court").document(mCourt.get(position).getCourtName());
            Map<String, Object> checkCalendar = new HashMap<>();
            checkCalendar.put("Status", "Yes");
            calendar_checking.set(checkCalendar);

        }

    }
}
