package com.example.admin.waitinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.admin.database.DBHelper;
import com.example.admin.database.WaitingCustomer;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;


public class ViewWaitingCustomersActivity extends OrmLiteBaseActivity<DBHelper> {

    Dao<WaitingCustomer,Integer> waitingCustomerDao;
    QueryBuilder<WaitingCustomer,Integer> queryBuilder;
    TableLayout waitingCustomersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_waiting_customers);
        if(savedInstanceState == null) {
            try {
                waitingCustomerDao = getHelper().getWaitingCustomerDao();
            } catch (Exception e) {
                e.printStackTrace();
                //TODO logging
            }
            queryBuilder = waitingCustomerDao.queryBuilder();
            Calendar calendar = Calendar.getInstance();
            Timestamp currentTs = new Timestamp(calendar.getTime().getTime());
            calendar.add(Calendar.DATE, -1);
            Timestamp yestTs = new Timestamp(calendar.getTime().getTime());
            waitingCustomersTable = (TableLayout) findViewById(R.id.waitingCustomersTable);
            try {
                queryBuilder.where().between("createdTs", currentTs, yestTs);
                queryBuilder.where().eq("isDeleted", false);
                queryBuilder.orderBy("createdTs", true);
                final List<WaitingCustomer> waitingCustomerList = queryBuilder.query();
                TableRow tableRow = new TableRow(this);
                TextView nameText = new TextView(this);
                nameText.setText("Name");
                TextView cellPhoneText = new TextView(this);
                cellPhoneText.setText("Total people");
                TextView waitingTimeText = new TextView(this);
                waitingTimeText.setText("Waiting time");
                TextView estWaitingTimeText = new TextView(this);
                estWaitingTimeText.setText("Estimated Waiting");

                tableRow.addView(nameText);
                tableRow.addView(cellPhoneText);
                tableRow.addView(waitingTimeText);
                tableRow.addView(estWaitingTimeText);
                waitingCustomersTable.addView(tableRow);

                if (waitingCustomerList.size() > 0) {
                    int i = 0;
                    for (; i < waitingCustomerList.size(); i++) {
                        addRow(waitingCustomerList.get(i),i);
                    }
                }
            } catch (SQLException e) {
                //TODO logging
                e.printStackTrace();
            }
        }
        Intent i = getIntent();
        if(i.getExtras()!=null) {
            try {
                WaitingCustomer newWaitingCustomer = (WaitingCustomer) i.getExtras().getSerializable("waitingCustomer");
                addRow(newWaitingCustomer, waitingCustomersTable.getChildCount() + 1);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_view_waiting_customers, menu);
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

    public void addButtonListener(View v) {
        Intent addScreen = new Intent(this, AddWaitingCustomerActivity.class);
        startActivity(addScreen);
    }

    public void addRow(final WaitingCustomer waitingCustomer, int index) {
        TableRow tableRow = new TableRow(this);
        tableRow.setId(index);
        TextView nameValue = new TextView(this);
        nameValue.setText(waitingCustomer.getName());
        TextView totalPeopleValue = new TextView(this);
        totalPeopleValue.setText(waitingCustomer.getCellNumber());
        TextView waitingTimeValue = new TextView(this);
        waitingTimeValue.setText("0.0");
        TextView estWaitingTimeValue = new TextView(this);
        estWaitingTimeValue.setText(waitingCustomer.getEstimatedWaitingTime());
        ImageButton notifyImage = new ImageButton(this);
        notifyImage.setImageResource(R.drawable.msg_icon);
        notifyImage.setMaxHeight(2);
        notifyImage.setMaxWidth(2);
        Button removeButton = new Button(this);
        removeButton.setText("Remove");
        removeButton.setId(index);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UpdateBuilder<WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                    updateBuilder.where().eq("id", waitingCustomer.getId());
                    updateBuilder.updateColumnValue("isDeleted", true);
                    int rowCountUpdated = updateBuilder.update();
                    //TODO log rowCount
                } catch (SQLException e) {
                    e.printStackTrace();
                    //TODO logging
                }
                TableRow rowToBeRemoved = (TableRow) waitingCustomersTable.findViewById(view.getId());
                rowToBeRemoved.startAnimation(AnimationUtils.loadAnimation(ViewWaitingCustomersActivity.this, android.R.anim.fade_out));
                rowToBeRemoved.setVisibility(View.GONE);
                waitingCustomersTable.removeViewAt(view.getId());
            }
        });

        tableRow.addView(nameValue);
        tableRow.addView(totalPeopleValue);
        tableRow.addView(waitingTimeValue);
        tableRow.addView(estWaitingTimeValue);
        tableRow.addView(notifyImage);
        tableRow.addView(removeButton);

        waitingCustomersTable.addView(tableRow);
    }
}
