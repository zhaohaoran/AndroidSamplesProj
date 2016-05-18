package net.haoranzhao.androidsamplesproj;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitREST extends AppCompatActivity {
    //private static final String API_URL = "http://jsonplaceholder.typicode.com/";
    private static final String API_URL = "http://api.open-notify.org/";

    private TextView showDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rest);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        showDataView = (TextView)findViewById(R.id.showData_retrofit_tv);
        String respose = "";
        Button getDataBtn = (Button)findViewById(R.id.getData_retrofit_btn);
        if (getDataBtn != null) {
            getDataBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Create a very simple REST adapter which points the GitHub API.
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    // Create an instance of our GitHub API interface.
                    OpenNotigy openNotigy = retrofit.create(OpenNotigy.class);

                    // Create a call instance for looking up Retrofit contributors.
                    Call<Sample> call = openNotigy.getSample();

                    call.enqueue(new Callback<Sample>() {
                        @Override
                        public void onResponse(Call<Sample> call, Response<Sample> response) {
                            //response.toString()
                                //sampleJSON[0] = call.execute().body();
                            Log.v("retrofit", response.body().toString());

                            Sample sample= response.body();
                            //sample.m_iss_position.latitude;
                           // sample.m_iss_position.longitude;

                            Log.v("retrofit", sample.message);
                            Log.v("retrofit", sample.timestamp+"");
                            Log.v("retrofit", sample.iss_position.latitude+"");

                            showDataView.setText("ISS Location: " +
                                    "("+sample.iss_position.latitude+", "+sample.iss_position.longitude+")\n" +
                                    "Time: "+new java.util.Date((long)sample.timestamp*1000));
                        }

                        @Override
                        public void onFailure(Call<Sample> call, Throwable t) {
                            t.getCause();
                            showDataView.setText(t.getCause()+"");

                        }
                    });

                }
            });
        }
    }



//    {
//        "iss_position": {
//        "latitude": 46.66480863601092,
//                "longitude": -179.52376323888802
//    },
//        "message": "success",
//            "timestamp": 1463069031
//    }
    public static class Sample{
        public final Iss_position iss_position;
        public final String message;
        public final int timestamp;
        public Sample(Iss_position iss_position,String message,int timestamp) {
            this.iss_position = iss_position;
            this.message = message;
            this.timestamp = timestamp;
        }
    }

    public static class Iss_position{
        public final double latitude;
        public final double longitude;
        public Iss_position(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

    }
    public interface OpenNotigy{
        @GET("iss-now.json")
        Call<Sample> getSample();

    }
}
