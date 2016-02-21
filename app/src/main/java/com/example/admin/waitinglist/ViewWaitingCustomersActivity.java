package com.example.admin.waitinglist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.database.DBHelper;
import com.example.admin.database.WaitingCustomer;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.admin.waitinglist.recycler_adapter;


public class ViewWaitingCustomersActivity extends OrmLiteBaseActivity<DBHelper> {

    Dao<WaitingCustomer, Integer> waitingCustomerDao;
    QueryBuilder<WaitingCustomer, Integer> queryBuilder;
    ArrayList<bean_item> items;
    TableLayout waitingCustomersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_waiting_customers);

        ImageView add_btn = (ImageView) findViewById(R.id.fab_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddWaitingCustomerActivity.class);
                startActivity(i);
            }
        });
        CardView card = (CardView)findViewById(R.id.title_bar);
        card.setPreventCornerOverlap(false);
        final SwipeRefreshLayout swipe = (SwipeRefreshLayout)findViewById(R.id.swip_bar);
/*        swipe.setEnabled(false);*/
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadItems();
                        swipe.setRefreshing(false);
                        /*swipe.setEnabled(false);*/
                    }
                }, 3000);
            }
        });
        swipe.setColorSchemeColors(getResources().getColor(R.color.refresh_progress_1),
                getResources().getColor(R.color.refresh_progress_2),
                getResources().getColor(R.color.refresh_progress_3));
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.view_cont);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 1) {
                    swipe.setEnabled(true);
                } else {
                    swipe.setEnabled(false);
                }
            }
        });
        try {
            waitingCustomerDao = getHelper().getWaitingCustomerDao();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO logging
        }

        ImageView back_btn = (ImageView) findViewById(R.id.view_back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewWaitingCustomersActivity.this.finish();
            }
        });

        loadItems();
        /*
            Intent i = getIntent();
            if(i.getExtras()!=null) {
            try {
                WaitingCustomer newWaitingCustomer = (WaitingCustomer) i.getExtras().getSerializable("waitingCustomer");
                addRow(newWaitingCustomer, waitingCustomersTable.getChildCount() + 1);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public void loadItems(){
        queryBuilder = waitingCustomerDao.queryBuilder();
        Calendar calendar = Calendar.getInstance();
        Timestamp currentTs = new Timestamp(calendar.getTime().getTime());
        calendar.add(Calendar.DATE, -1);
        Timestamp yestTs = new Timestamp(calendar.getTime().getTime());
        /*waitingCustomersTable = (TableLayout) findViewById(R.id.waitingCustomersTable);*/
        try {
            queryBuilder.where().between("createdTs", currentTs, yestTs);
            queryBuilder.where().eq("isDeleted", false);
            queryBuilder.orderBy("createdTs", true);
            final List<WaitingCustomer> waitingCustomerList = queryBuilder.query();
/*

            TableRow tableRow = new TableRow(this);

            CardView cardView = new CardView(this);
            cardView.setCardBackgroundColor(android.R.color.white);

            TextView nameText = new TextView(this);
            nameText.setText("Name");
            TextView cellPhoneText = new TextView(this);
            cellPhoneText.setText("Total people");
            TextView waitingTimeText = new TextView(this);
            waitingTimeText.setText("Waiting time");
            TextView estWaitingTimeText = new TextView(this);
            estWaitingTimeText.setText("Estimated Waiting");

            cardView.addView(nameText);
            cardView.addView(cellPhoneText);
            cardView.addView(waitingTimeText);
            cardView.addView(estWaitingTimeText);
            tableRow.addView(cardView);
            waitingCustomersTable.addView(tableRow);*/

            items = new ArrayList<>();
            if (waitingCustomerList.size() > 0) {
                int i = 0;
                for (; i < waitingCustomerList.size(); i++) {
                    addRow(waitingCustomerList.get(i), i);
                }
            }

            recycler_adapter adpt = new recycler_adapter(items, waitingCustomerDao);
            RecyclerView r_view = (RecyclerView) findViewById(R.id.view_cont);
            r_view.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
            r_view.setAdapter(adpt);
        } catch (SQLException e) {
            //TODO logging
            e.printStackTrace();
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Timestamp(Calendar.getInstance().getTime().getTime());
        Date date = waitingCustomer.getCreatedTs();
        /*Date date1 = new Date();*/
        long min = (date1.getTime() - date.getTime()) / (60 * 1000);
        items.add(new bean_item("" + waitingCustomer.getId(),
                waitingCustomer.getName(),
                waitingCustomer.getCellNumber(),
                waitingCustomer.getTotalPeople(),
                waitingCustomer.getEstimatedWaitingTime(),
                ""+min
        ));
        /*TableRow tableRow = new TableRow(this);
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

                waitingCustomersTable.removeView(view);
            }
        });

        tableRow.addView(nameValue);
        tableRow.addView(totalPeopleValue);
        tableRow.addView(waitingTimeValue);
        tableRow.addView(estWaitingTimeValue);
        tableRow.addView(notifyImage);
        tableRow.addView(removeButton);

        waitingCustomersTable.addView(tableRow);
    */
    }
}