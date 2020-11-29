package com.example.bookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class imgAdapter extends RecyclerView.Adapter<imgAdapter.imgViewHolder> {
    private Context context;
    private int[] product_image;

    public imgAdapter(Context c, int[] img){
        context = c;
        product_image = img;
    }

    @NonNull
    @Override
    public imgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.img_recycler, parent, false);
        return new imgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull imgViewHolder holder, int position) {
        holder.img.setImageResource(product_image[position]);
    }

    @Override
    public int getItemCount() {
        return product_image.length;
    }

    public class imgViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public imgViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.show_image);
        }
    }
}
