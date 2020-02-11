package com.example.takeandchoosephoto;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageHolder> {
    ArrayList<Bitmap> bitmapList = new ArrayList<>();

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        ((ImageView)holder.itemView).setImageBitmap(bitmapList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    // active method
    public void add(Bitmap bitmap) {
        bitmapList.add(0,bitmap);
        notifyItemInserted(0);
    }


    class ImageHolder extends RecyclerView.ViewHolder {

        public ImageHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "click", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
