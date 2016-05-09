package com.example.higallery;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.higallery.adapter.PictureAdapter;
import com.example.higallery.model.Item;
import com.example.higallery.util.Utils;
import com.example.higallery.view.MyScrollGridView;
import com.example.higallery.view.MyScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String GET_PICTURE_JSON =
            "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=7e6e9d48f956872e102cfee488d5c912&tags=daybreak&format=json&per_page=30";

    public List<Item> items;

    private PictureAdapter adapter;

    private ListView listView;
    private View moreView;

    private int page,pages,total,count;

//    private GridView gridView;

    private int lastItem;

    private boolean flag = false;

    private LinearLayout footer;
    private MyScrollView scrollView;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0x0000) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                page++;
                loadMoreData();
                adapter.notifyDataSetChanged();
//                moreView.setVisibility(View.GONE);
                footer.setVisibility(View.GONE);

                if(count > total){
                    Toast.makeText(MainActivity.this, "No more data！", Toast.LENGTH_LONG).show();
//                    listView.removeFooterView(moreView);

                }

            }
            else if(msg.what == 0x0001){
                String jsonDataRaw = (String) msg.obj;
                String jsonData = jsonDataRaw.substring("jsonFlickrApi".length()+1,jsonDataRaw.length()-1);
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONObject("photos").getJSONArray("photo");
                    if(flag == false){
                        page = Integer.valueOf(jsonObject.getJSONObject("photos").getString("page"));
                        pages = Integer.valueOf(jsonObject.getJSONObject("photos").getString("pages"));
                        total = Integer.valueOf(jsonObject.getJSONObject("photos").getString("total"));
                        flag = true;
                    }
                    count = count + 30;
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        String farm_id = object.getString("farm");
                        String server_id = object.getString("server");
                        String id = object.getString("id");
                        String secret = object.getString("secret");
                        String imageUrl = "https://farm"+farm_id+".staticflickr.com/"+server_id+"/"+id+"_"+secret+".jpg";
                        items.add(new Item(imageUrl));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        listView = (ListView) findViewById(R.id.list_detail);
//        moreView = getLayoutInflater().inflate(R.layout.load, null);
//        gridView = (GridView) findViewById(R.id.grid_detail);

        scrollView = (MyScrollView) findViewById(R.id.sv);
        footer = (LinearLayout) findViewById(R.id.footer);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        final MyScrollGridView ngv = (MyScrollGridView) findViewById(R.id.ngv);

        items = new ArrayList<>();

        adapter = new PictureAdapter(this,items);

        ngv.setAdapter(adapter);

//        listView.addFooterView(moreView);

//        listView.setAdapter(adapter);
//
//        listView.setOnScrollListener(this);

        Utils.getPictureJSON(GET_PICTURE_JSON,handler);

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            private int lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lastY = scrollView.getScrollY();
                    if (lastY == (ll.getHeight() - scrollView.getHeight())) {
                        footer.setVisibility(View.VISIBLE);
                        handler.sendEmptyMessage(0x0000);
                    }
                }
                return false;
            }
        });
    }

//    @Override
//    public void onScrollStateChanged(AbsListView absListView, int i) {
//
//        if(lastItem == count  && i == this.SCROLL_STATE_IDLE){
//
//            moreView.setVisibility(View.VISIBLE);
//            handler.sendEmptyMessage(0x0000);
//        }
//
//    }
//
//    @Override
//    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        lastItem = firstVisibleItem + visibleItemCount - 1;
//    }

    private void loadMoreData(){
        Log.v("====Tag====",GET_PICTURE_JSON+"&page="+String.valueOf(page));
        Utils.getPictureJSON(GET_PICTURE_JSON+"&page="+String.valueOf(page),handler);
    }
}
