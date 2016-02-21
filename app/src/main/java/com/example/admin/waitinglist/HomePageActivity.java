package com.example.admin.waitinglist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {
    static String TAG="Home Page Activity";
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        /*toolBarActionImplementation();*/
        Button btnadd=(Button)(findViewById(R.id.buttonAdd));
        btnadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext(), AddWaitingCustomerActivity.class);
                startActivity(addIntent);
            }

        });
        Button btnview=(Button)(findViewById(R.id.buttonView));
        btnview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent viewIntent = new Intent(getApplicationContext(),ViewWaitingCustomersActivity.class);
                startActivity(viewIntent);
            }

        });
    }

    private void toolBarActionImplementation()
    {
        /*final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("Waiting List Customer");
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("Waiting List Customer");
        }
        mToolbar = toolbar;*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
