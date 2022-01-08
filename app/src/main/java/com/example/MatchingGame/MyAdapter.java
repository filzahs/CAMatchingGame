package com.example.MatchingGame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.File;

public class MyAdapter extends ArrayAdapter<Object> {
    private final Context context;
    protected String[] imgArray;

    public MyAdapter(Context context, String[] imgArray) {
        super(context, R.layout.rows);
        this.context = context;
        this.imgArray = imgArray;

        addAll(new Object[imgArray.length]);
    }

    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rows, parent, false);
            view.setMinimumHeight(MainActivity.height/6); //additional 1 more row, to get the gridview to fill the screen
        }

        ImageView imageView = view.findViewById(R.id.imageView);
        String fileName = imgArray[pos]; //get the file path from toons[pos]

        Bitmap bitmap = BitmapFactory.decodeFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + fileName);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        }, 3000); //wait for 3 sec before setting bitmap

        return view;
    }
}
