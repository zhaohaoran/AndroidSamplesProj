package net.haoranzhao.androidsamplesproj;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.haoranzhao.androidsamplesproj.util.DividerGridItemDecoration;
import net.haoranzhao.androidsamplesproj.util.HttpMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class RetrofitWithRxJava extends AppCompatActivity {

    private static final String API_URL = "http://gank.io/api/";


    //butterknife view injection
    @BindView(R.id.my_toolbar_retrofit_rxjava)
    Toolbar myToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    private HomeAdapter mAdapter;
    private int currentPage = 1;
    private int itemsNum = 10;
    private boolean loading = false;
    private LinearLayoutManager linearLayoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;


    private List<HttpMethods.Result> GankDatas;

    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_ESSAY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_with_rx_java);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);

        //myToolbar.setOnMenuItemClickListener(onMenuItemClick);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        GankDatas = new ArrayList<HttpMethods.Result>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter = new HomeAdapter());

        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading)
                    {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = true;
                            Log.v("...", "Last Item Wow !");
                            Toast.makeText(RetrofitWithRxJava.this,"Loading...",Toast.LENGTH_SHORT);
                            //Do pagination.. i.e. fetch new data
                            currentPage++;
                            getGankData(itemsNum,currentPage);

                        }
                    }
                }
            }
        });
        //currentPage = 1;
        getGankData(itemsNum,currentPage);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_ESSAY:
                    return new TxtViewHolder(LayoutInflater.from(
                            RetrofitWithRxJava.this).inflate(R.layout.recycler_view_item, parent,
                            false));
                case TYPE_IMAGE:
                    return new ImageViewHolder(LayoutInflater.from(
                            RetrofitWithRxJava.this).inflate(R.layout.recycler_view_pic_item, parent,
                            false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if(TYPE_ESSAY == getItemViewType(position)){
                ((TxtViewHolder)holder).idNum.setText(GankDatas.get(position).desc);
            }else if(TYPE_IMAGE == getItemViewType(position)){
                if (GankDatas.get(position) != null) {
                    Glide.with(RetrofitWithRxJava.this)
                            .load(GankDatas.get(position).url)
                            .into(((ImageViewHolder)holder).pic);
                } else {
                    // make sure Glide doesn't load anything into this view until told otherwise
                    Glide.clear(((ImageViewHolder)holder).pic);
                    // remove the placeholder (optional); read comments below
                    ((ImageViewHolder)holder).pic.setImageDrawable(null);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (GankDatas.get(position).type.equals("福利")) {
                return TYPE_IMAGE;
            } else {
                return TYPE_ESSAY;
            }
        }

        @Override
        public int getItemCount() {
            return GankDatas.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.pic)
            ImageView pic;

            public ImageViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public class TxtViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.id_num)
            TextView idNum;

            public TxtViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

    }

    //进行网络请求
    private void getGankData(int itemsNum, int pageNum) {

        Subscriber subscriber = new Subscriber<HttpMethods.GankEntity>() {
            @Override
            public void onCompleted() {
                loading=false;
                Toast.makeText(RetrofitWithRxJava.this, "Get Gank Data Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                // resultTV.setText(e.getMessage());
                Toast.makeText(RetrofitWithRxJava.this, "Get Gank Data error: " + e, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpMethods.GankEntity gankEntity) {
                //deal with the data entity here
                GankDatas.addAll(gankEntity.results);
                mAdapter.notifyDataSetChanged();
            }
        };

        HttpMethods.getInstance().getAllGankData(subscriber, itemsNum, currentPage);
    }


}
