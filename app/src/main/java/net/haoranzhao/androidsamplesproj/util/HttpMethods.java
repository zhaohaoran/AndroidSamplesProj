package net.haoranzhao.androidsamplesproj.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpMethods {

    public static final String BASE_URL = "http://gank.io/api/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private GankService gankService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        gankService = retrofit.create(GankService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     * @param subscriber 由调用者传过来的观察者对象
     * @param itemsNum 起始位置
     * @param pageNum 获取长度
     */
    public void getAllGankData(Subscriber<GankEntity> subscriber, int itemsNum, int pageNum){
        gankService.getGankData(itemsNum, pageNum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //data/all/[#itemsNum]/[#pageNum]
    public interface GankService {
        @GET("data/all/{itemsNum}/{pageNum}")
        Observable<GankEntity> getGankData(@Path("itemsNum") int itemsNum, @Path("pageNum") int pageNum);
    }

    /*
     *{
  "error": false,
  "results": [
    {
      "_id": "573af68f6776591ca532824f",
      "createdAt": "2016-05-17T18:46:39.251Z",
      "desc": "\u5c0f\u5de7\u7684\u5e72\u8d27\u96c6\u4e2d\u8425\u7684Mac\u7248\u672c\u5ba2\u6237\u7aef\uff08The missing Mac OS X application for gank.io\uff09",
      "publishedAt": "2016-05-19T12:09:29.617Z",
      "source": "web",
      "type": "\u62d3\u5c55\u8d44\u6e90",
      "url": "https://github.com/hujiaweibujidao/Gank-for-Mac",
      "used": true,
      "who": "\u6f47\u6da7"
    },
    ...]
     */
    public static class GankEntity{

        public final boolean error;
        public final List<Result> results;
        public GankEntity(boolean error, List<Result> results) {
            this.error = error;
            this.results = results;
        }
    }
    public static class Result{
        public final String url;
        public final String type;
        public final String desc;
        public final String createdAt;

        public Result(String url, String type, String desc,String createdAt) {
            this.url = url;
            this.type = type;
            this.desc = desc;
            this.createdAt = createdAt;
        }
    }
}
