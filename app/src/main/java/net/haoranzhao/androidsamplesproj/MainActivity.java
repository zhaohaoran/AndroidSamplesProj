package net.haoranzhao.androidsamplesproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_regular_rest:
                Intent intent = new Intent(MainActivity.this,HTTPClientREST.class);
                startActivity(intent);
                return true;
            case R.id.action_retrofit_rest:
                Intent intent2 = new Intent(MainActivity.this,RetrofitREST.class);
                startActivity(intent2);
                return true;
            case R.id.action_recycler_view_sample:
                Intent intent3 = new Intent(MainActivity.this,RecyclerViewSample.class);
                startActivity(intent3);
                return true;
            case R.id.action_card_view_sample:
                Intent intent4 = new Intent(MainActivity.this,CardViewSample.class);
                startActivity(intent4);
                return true;
            case R.id.action_activity_scrolling:
                Intent intent5 = new Intent(MainActivity.this,ScrollingActivity.class);
                startActivity(intent5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
