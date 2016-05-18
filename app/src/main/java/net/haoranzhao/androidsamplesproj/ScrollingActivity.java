package net.haoranzhao.androidsamplesproj;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.haoranzhao.androidsamplesproj.util.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ScrollingActivity extends AppCompatActivity {
    private static final String API_URL = "http://gank.io/api/";

    @BindView(R.id.my_toolbar_scrolling)Toolbar myToolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.scroll_recycler_view) RecyclerView recyclerView;

    private List<String> mDatas;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_scrolling);
        setSupportActionBar(myToolbar);

        //myToolbar.setOnMenuItemClickListener(onMenuItemClick);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        //initData();
        mDatas = new ArrayList<String>();

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(mAdapter = new HomeAdapter());

        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //mDatas.clear();
        //loadData();

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDatas.clear();
                    loadData();

                }
            });
        }
    }


    protected void loadData()
    {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        RandomDataFuli20 randomDataFuli20 = retrofit.create(RandomDataFuli20.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<Sample> call = randomDataFuli20.getSample();

        call.enqueue(new Callback<Sample>() {
            @Override
            public void onResponse(Call<Sample> call, Response<Sample> response) {
                Log.v("retrofit", response.body().toString());
                Sample sample= response.body();
                List<Result> results = sample.results;
                Iterator<Result> itr = results.iterator();
                while(itr.hasNext()){
                    mDatas.add(itr.next().url);
                }
                Toast.makeText(getApplicationContext(), "get Data success",
                        Toast.LENGTH_SHORT).show();

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Sample> call, Throwable t) {
                t.getCause();
                Toast.makeText(getApplicationContext(), "get Data failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ScrollingActivity.this).inflate(R.layout.recycler_view_pic_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            if (mDatas.get(position) != null) {
                Glide.with(ScrollingActivity.this)
                        .load(mDatas.get(position))
                        //.placeholder(R.drawable.ic_refresh_black_24dp)
                        .into(holder.pic);
            } else {
                // make sure Glide doesn't load anything into this view until told otherwise
                Glide.clear(holder.pic);
                // remove the placeholder (optional); read comments below
                holder.pic.setImageDrawable(null);
            }



        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

//        public void addData(int position) {
//            mDatas.add(position, "Insert One");
//            notifyItemInserted(position);
//        }
//
//        public void removeData(int position) {
//            mDatas.remove(position);
//            notifyItemRemoved(position);
//        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.pic)
            ImageView pic;

            public MyViewHolder(View view)
            {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


/*
{
  "error": false,
  "results": [
    {
      "_id": "56cc6d1d421aa95caa7075c5",
      "createdAt": "2015-05-20T02:08:17.148Z",
      "desc": "5.20\u3002\n520\u7231\u4f60\uff0c\u5c31\u7ed9\u4f60\u751c\u751c\u7684\u7b11\u3002\u4eca\u65e5\u7279\u63a8\uff01~~\uff08\u3065\uffe33\uffe3\uff09\u3065\u256d\u2764\uff5e",
      "publishedAt": "2015-05-21T10:05:06.527Z",
      "type": "\u798f\u5229",
      "url": "http://ww1.sinaimg.cn/large/7a8aed7bgw1esahpyv86sj20hs0qomzo.jpg",
      "used": true,
      "who": "\u5f20\u6db5\u5b87"
    }
    ]
 }
 */
    public static class Sample{
        public final boolean error;
        public final List<Result> results;
        public Sample(boolean error, List<Result> results) {
            this.error = error;
            this.results = results;
        }
    }

    public static class Result{
        public final String url;
        public Result(String url) {
            this.url = url;
        }

    }
    public interface RandomDataFuli20{
        @GET("random/data/福利/20")
        Call<Sample> getSample();

    }

}
