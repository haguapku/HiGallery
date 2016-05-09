package com.example.higallery.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.higallery.R;

/**
 * Created by MarkYoung on 16/5/9.
 */
public class ViewCache {

    private View baseView;

    private ImageView imageView;

    public ViewCache(View baseView) {
        this.baseView = baseView;
    }

    public ImageView getImageView() {
        if(imageView == null){
            imageView = (ImageView) baseView.findViewById(R.id.row_image);
        }
        return imageView;
    }

}
