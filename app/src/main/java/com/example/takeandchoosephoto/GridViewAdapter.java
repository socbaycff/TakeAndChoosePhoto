package com.example.takeandchoosephoto;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    ArrayList<Bitmap> bitmapList = new ArrayList<>();


    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Object getItem(int position) {
        return bitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        view.setImageBitmap(bitmapList.get(position));

        return view;
    }

    public void add(Bitmap item) {
        bitmapList.add(0,item);
        notifyDataSetChanged();
    }
}
