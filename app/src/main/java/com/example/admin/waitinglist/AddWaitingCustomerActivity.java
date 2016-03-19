package com.example.admin.waitinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.database.WaitingCustomer;

import com.example.admin.database.DBHelper;
import com.example.admin.formatter.PickerFormatter;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.Timestamp;
import java.util.Calendar;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class AddWaitingCustomerActivity extends OrmLiteBaseActivity<DBHelper> {

    EditText name, contactNumber, totalPeople, notes;
    ImageView home_btn;
    Toolbar mToolbar;
    String est = "";
    biz.kasual.materialnumberpicker.MaterialNumberPicker numberPicker;
    TextInputLayout inputNameLayout, inputContactNumberLayout, inputTotalPeopleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_waiting_customer);
        name = (EditText) findViewById(R.id.inputNameText);
        contactNumber = (EditText) findViewById(R.id.inputContactNumberText);
        totalPeople = (EditText) findViewById(R.id.inputTotalPeopleText);
        notes = (EditText) findViewById(R.id.inputNotesText);
        home_btn = (ImageView) findViewById(R.id.add_home);
        inputNameLayout = (TextInputLayout) findViewById(R.id.inputNameLayout);
        inputContactNumberLayout = (TextInputLayout) findViewById(R.id.inputContactNumberLayout);
        inputTotalPeopleLayout = (TextInputLayout) findViewById(R.id.inputTotalPeopleLayout);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homePage = new Intent(AddWaitingCustomerActivity.this,HomePageActivity.class);
                startActivity(homePage);
            }
        });

        TextView numberPickerHeader = new TextView(getApplicationContext());
        numberPickerHeader.setTranslationX(2);
        numberPickerHeader.setText("Set Estimated waiting time");
        numberPickerHeader.setTextSize(25);
        numberPickerHeader.setTextColor(Color.WHITE);
        numberPickerHeader.setBackgroundColor(getResources().getColor(R.color.PrimaryColor));

        //After clicking on Next button, a popup appears to enter estimated waiting time
        numberPicker = new MaterialNumberPicker.Builder(getApplicationContext())
                .minValue(1)
                .maxValue(200)
                .defaultValue(10).formatter(new PickerFormatter())
                .textColor(Color.WHITE).
                        backgroundColor(getResources().getColor(R.color.PrimaryColor)).
                        separatorColor(getResources().getColor(R.color.white))
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        final LinearLayout layout = new LinearLayout(AddWaitingCustomerActivity.this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(getResources().getColor(R.color.PrimaryColor));
        layout.addView(numberPickerHeader);
        layout.addView(numberPicker);

        try {
            waitingCustomerDao = getHelper().getWaitingCustomerDao();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO logging
        }
        Button nextButton = (Button) findViewById(R.id.next_button);
        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPeople.setText("");
                contactNumber.setText("");
                name.setText("");
                notes.setText("");
                inputNameLayout.setError(null);
                inputContactNumberLayout.setError(null);
                inputTotalPeopleLayout.setError(null);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if (totalPeople.getText().toString().equals("")) {
                    inputTotalPeopleLayout.setError("Please enter total people");
                    totalPeople.findFocus();
                    flag = true;
                }
                if (contactNumber.getText().toString().equals("") || contactNumber.getText().toString().length()<10) {
                    inputContactNumberLayout.setError("Please enter contact number(10 digits)");
                    contactNumber.findFocus();
                    flag = true;
                }
                if (name.getText().toString().equals("")) {
                    inputNameLayout.setError("Please enter name");
                    name.findFocus();
                    flag = true;
                }
                if (!flag) {
                    new AlertDialog.Builder(AddWaitingCustomerActivity.this)
                            .setInverseBackgroundForced(true)
                            .setView(layout)
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    est = "" + numberPicker.getValue();
                                    WaitingCustomer waitingCustomer = new WaitingCustomer();
                                    waitingCustomer.setName(name.getText().toString());
                                    waitingCustomer.setContactNumber(contactNumber.getText().toString());
                                    waitingCustomer.setTotalPeople(totalPeople.getText().toString());
                                    //TODO add a new screen to enter waiting time
                                    waitingCustomer.setEstWaitingTime(est);
                                    waitingCustomer.setCreatedTs(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                    waitingCustomer.setDeleted(0);
                                    waitingCustomer.setNotes(notes.getText().toString());
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

    Dao<WaitingCustomer, Integer> waitingCustomerDao;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void toolBarActionImplementation() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("Waiting List Customer");
        mToolbar = toolbar;
    }
}
