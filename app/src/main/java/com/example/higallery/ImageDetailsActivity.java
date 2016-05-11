package com.example.higallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.example.higallery.view.ZoomImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import libcore.io.DiskLruCache;

/**
 * Created by MarkYoung on 16/5/11.
 */
public class ImageDetailsActivity extends Activity {

    static final int DISK_CACHE_DEFAULT_SIZE = 10 * 1024 * 1024;

    private ZoomImageView zoomImageView;
    private BitmapLoadTask bitmapLoadTask;

    private DiskLruCache diskLruCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_details);
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
        zoomImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.default_image));

        String imagePath = getIntent().getStringExtra("image_path");

        bitmapLoadTask = new BitmapLoadTask(zoomImageView);
        bitmapLoadTask.execute(imagePath);
    }

    private class BitmapLoadTask extends AsyncTask<String, Void, Bitmap>{

        private ZoomImageView zoomImageView;

        public BitmapLoadTask(ZoomImageView zoomImageView) {
            this.zoomImageView = zoomImageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            String url = strings[0];
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            zoomImageView.setImageBitmap(bitmap);
        }
    }
}
