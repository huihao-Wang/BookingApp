package com.example.bookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class historyDateAdapter extends RecyclerView.Adapter<historyDateAdapter.historyViewHolder> {
    private Context context;
    private ArrayList<Court> mCourt;

    public historyDateAdapter(Context c, ArrayList<Court> court){
        context = c;
        mCourt = court;
    }

    @NonNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history, parent, false);
        return new historyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull historyViewHolder holder, int position) {
        holder.tvDate.setText(mCourt.get(position).getDate());
        holder.badminton_hall_img.setImageResource(mCourt.get(position).getCourtImg());
        holder.tvBadmintonHall.setText(mCourt.get(position).getCourtName());
        holder.tvPrice.setText("RM " + mCourt.get(position).getPrice());
        holder.tvTime.setText(mCourt.get(position).getTime() + "");
    }

    @Override
    public int getItemCount() {
        return mCourt.size();
    }

    public class historyViewHolder extends RecyclerView.ViewHolder {
        private ImageView badminton_hall_img;
        private TextView tvBadmintonHall, tvPrice, tvTime, tvDate;

        public historyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.textViewDate);
            badminton_hall_img = itemView.findViewById(R.id.history_badmintonHall_image);
            tvBadmintonHall = itemView.findViewById(R.id.history_badminton_hall);
            tvPrice = itemView.findViewById(R.id.history_product_price);
            tvTime = itemView.findViewById(R.id.history_time);

        }

    }
}
