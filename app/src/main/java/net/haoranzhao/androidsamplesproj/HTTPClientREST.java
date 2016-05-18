package net.haoranzhao.androidsamplesproj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HTTPClientREST extends AppCompatActivity {
    private static final String API_URL = "http://api.open-notify.org/iss-now.json";

    private TextView showDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httpclient_rest);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        String respose = "";
        Button getDataBtn = (Button)findViewById(R.id.getData_httpclient_btn);
        if (getDataBtn != null) {
            getDataBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
                    httpAsyncTask.execute(API_URL);
                }
            });
        }


        showDataView = (TextView)findViewById(R.id.showData_httpclient_tv);

    }

    // this is the http async task of the stop function, it will run after the return button is clicked
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return makeGetRequest(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            showDataView.setText(result);
        }
    }


    private String makeGetRequest(String url) {

        String res="error";
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        // replace with your url

        HttpResponse response = null;

        try {
            response = client.execute(request);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                res = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
            }
           // Log.d("Response of GET request", response.getEntity().getContent().toString());

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }


}