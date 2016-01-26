package com.example.admin.waitinglist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.database.DBHelper;
import com.example.admin.database.WaitingCustomer;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.Timestamp;
import java.util.Calendar;

public class AddWaitingCustomerActivity extends OrmLiteBaseActivity<DBHelper> {


    EditText name;
    EditText cellNumber;
    EditText totalPeople;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_waiting_customer);
        name = (EditText)findViewById(R.id.name);
        cellNumber = (EditText)findViewById(R.id.cellNumber);
        totalPeople = (EditText)findViewById(R.id.totalPeople);
        try {
            waitingCustomerDao = getHelper().getWaitingCustomerDao();
        }

        catch(Exception e) {
            e.printStackTrace();
            //TODO logging
        }
        Button nextButton = (Button) findViewById(R.id.next_button);
        Button resetButton = (Button) findViewById(R.id.reset_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Add Activity", "Add Button was clicked");
                WaitingCustomer waitingCustomer = new WaitingCustomer();
                waitingCustomer.setName(name.getText().toString());
                waitingCustomer.setCellNumber(cellNumber.getText().toString());
                waitingCustomer.setTotalPeople(totalPeople.getText().toString());
                //TODO add a new screen to enter waiting time
                waitingCustomer.setEstimatedWaitingTime("30");
                waitingCustomer.setCreatedTs(new Timestamp(Calendar.getInstance().getTime().getTime()));
                waitingCustomer.setDeleted(false);
                try {
                    waitingCustomerDao.create(waitingCustomer);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    //TODO logging
                }
                Intent viewScreen = new Intent(AddWaitingCustomerActivity.this, ViewWaitingCustomersActivity.class);
                viewScreen.putExtra("waitingCustomer", waitingCustomer);
                startActivity(viewScreen);
            }
        });
    }

    Dao<WaitingCustomer,Integer> waitingCustomerDao;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO: what does this line do??
        //getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

}
