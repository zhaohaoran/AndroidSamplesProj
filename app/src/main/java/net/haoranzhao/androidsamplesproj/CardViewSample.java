package net.haoranzhao.androidsamplesproj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.haoranzhao.androidsamplesproj.util.News;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CardViewSample extends AppCompatActivity {

    CardView cardView;
    ImageView news_photo;
    TextView news_title;
    TextView news_desc;
    Button share;
    Button readMore;
    News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_view_sample);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //myToolbar.setOnMenuItemClickListener(onMenuItemClick);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        news = new News();
        news.setTitle("news title");
        news.setDesc("don't know this: desc");

        news.setPhotoId(R.mipmap.news_pic_sample);


        cardView= (CardView)findViewById(R.id.card_view);
        news_photo= (ImageView)findViewById(R.id.news_photo);
        news_title= (TextView)findViewById(R.id.news_title);
        news_desc= (TextView)findViewById(R.id.news_desc);
        share= (Button)findViewById(R.id.btn_share);
        readMore= (Button)findViewById(R.id.btn_more);
        //设置TextView背景为半透明
        news_title.setBackgroundColor(Color.argb(20, 0, 0, 0));


        Glide.with(this)
                .load("http://ichef-1.bbci.co.uk/news/624/cpsprodpb/DD86/production/_89601765_e36c8c30-b9f4-4766-8e81-52ed0359c2a6.jpg")
                .fitCenter()
                .into(news_photo);


        //HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
        //httpAsyncTask.execute("http://ichef-1.bbci.co.uk/news/624/cpsprodpb/DD86/production/_89601765_e36c8c30-b9f4-4766-8e81-52ed0359c2a6.jpg");

        news_title.setText(news.getTitle());
        news_desc.setText(news.getDesc());

        //为btn_share btn_readMore cardView设置点击事件
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    // this is the http async task of the stop function, it will run after the return button is clicked
    private class HttpAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.mipmap.news_pic_sample);;
            try {
                bmp =  getBmpFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Bitmap bmp) {
            news_photo.setImageBitmap(bmp);;
        }
    }

    public Bitmap getBmpFromUrl(String Uri) throws IOException,MalformedURLException
    {//"http://image10.bizrate-images.com/resize?sq=60&uid=2216744464"
        URL url = null;
        url = new URL(Uri);
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        return bmp;
    }


//    // For a simple image list:
//    @Override
//    public View getView(int position, View recycled, ViewGroup container) {
//        final ImageView myImageView;
//        if (recycled == null) {
//            myImageView = (ImageView) inflater.inflate(R.layout.my_image_view, container, false);
//        } else {
//            myImageView = (ImageView) recycled;
//        }
//
//        String url = myUrls.get(position);
//
//        Glide
//                .with(myFragment)
//                .load(url)
//                .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
//                .crossFade()
//                .into(myImageView);
//
//        return myImageView;
//    }

}
