package com.example.admin.waitinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.database.DBHelper;
import com.example.admin.database.WaitingCustomer;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Handler;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class AddWaitingCustomerActivity extends OrmLiteBaseActivity<DBHelper> {


    EditText name,cellNumber,totalPeople;
    ImageView back_btn;
    Toolbar mToolbar;
    String est = "";
    biz.kasual.materialnumberpicker.MaterialNumberPicker numberPicker;
    TextInputLayout namelbl,celllbl,peoplelbl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_waiting_customer);
        name = (EditText)findViewById(R.id.name);
        cellNumber = (EditText)findViewById(R.id.cellNumber);
        totalPeople = (EditText)findViewById(R.id.totalPeople);
        back_btn = (ImageView)findViewById(R.id.add_back);
        namelbl = (TextInputLayout)findViewById(R.id.namelbl);
        celllbl = (TextInputLayout)findViewById(R.id.celllbl);
        peoplelbl = (TextInputLayout)findViewById(R.id.peoplelbl);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWaitingCustomerActivity.this.finish();
            }
        });


         numberPicker = new MaterialNumberPicker.Builder(getApplicationContext())
                .minValue(1)
                .maxValue(200)
                .defaultValue(1)
                .textColor(Color.WHITE).
                 backgroundColor(getResources().getColor(R.color.PrimaryColor)).
                 separatorColor(getResources().getColor(R.color.white))
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        final LinearLayout layout = new LinearLayout(AddWaitingCustomerActivity.this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(getResources().getColor(R.color.PrimaryColor));
        layout.addView(numberPicker);

        try {
            waitingCustomerDao = getHelper().getWaitingCustomerDao();
        }

        catch(Exception e) {
            e.printStackTrace();
            //TODO logging
        }
        Button nextButton = (Button) findViewById(R.id.next_button);
        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                totalPeople.setText("");
                cellNumber.setText("");
                name.setText("");
                namelbl.setError(null);
                celllbl.setError(null);
                peoplelbl.setError(null);

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if(totalPeople.getText().toString().equals("")){
                    peoplelbl.setError("Please enter total people");
                    totalPeople.findFocus();
                    flag=true;
                }
                if(cellNumber.getText().toString().equals("")){
                    celllbl.setError("Please enter contact no");
                    cellNumber.findFocus();
                    flag =true;
                }
                if(name.getText().toString().equals("")){
                    namelbl.setError("Please enter name");
                    name.findFocus();
                    flag = true;
                }
                if(!flag) {

                    new AlertDialog.Builder(AddWaitingCustomerActivity.this)
                            /*.setTitle("Please Select Estimated Time")*/
                            .setInverseBackgroundForced(true)
                            .setView(layout)
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    est = ""+numberPicker.getValue();
                                    WaitingCustomer waitingCustomer = new WaitingCustomer();
                                    waitingCustomer.setName(name.getText().toString());
                                    waitingCustomer.setCellNumber(cellNumber.getText().toString());
                                    waitingCustomer.setTotalPeople(totalPeople.getText().toString());
                                    //TODO add a new screen to enter waiting time
                                    waitingCustomer.setEstimatedWaitingTime(est);
                                    waitingCustomer.setCreatedTs(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                    waitingCustomer.setDeleted(false);
                                    try {
                                        waitingCustomerDao.create(waitingCustomer);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        //TODO logging
                                    }
                                    Intent viewScreen = new Intent(AddWaitingCustomerActivity.this, ViewWaitingCustomersActivity.class);
                                    viewScreen.putExtra("waitingCustomer", waitingCustomer);
                                    startActivity(viewScreen);
                                }
                            }).
                            setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();

                }
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
    private void toolBarActionImplementation()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        /*setSupportActionBar(toolbar);*/
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("Waiting List Customer");
        /*final ActionBar actionBar = getSupportActionBar();*/
        /*if (actionBar != null)
        {
            actionBar.setTitle("Waiting List Customer");
        }*/
        mToolbar = toolbar;
    }
}
