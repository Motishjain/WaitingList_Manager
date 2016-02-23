package com.example.admin.waitinglist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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


public class ViewWaitingCustomersActivity extends OrmLiteBaseActivity<DBHelper> {

    Dao<com.example.admin.database.WaitingCustomer, Integer> waitingCustomerDao;

    QueryBuilder<com.example.admin.database.WaitingCustomer, Integer> queryBuilder;
    ArrayList<WaitingCustomer> items;
    RecyclerView waitingCustomersView;
    private static ViewWaitingCustomersActivity inst;

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public static ViewWaitingCustomersActivity instance() {
        return inst;
    }

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
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadItems();
                        swipe.setRefreshing(false);
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
            queryBuilder.where().eq("deleted", false);
            queryBuilder.orderBy("createdTs", true);
            final List<com.example.admin.database.WaitingCustomer> waitingCustomerList = queryBuilder.query();
            items = new ArrayList<>();
            if (waitingCustomerList.size() > 0) {
                int i = 0;
                for (; i < waitingCustomerList.size(); i++) {
                    addRow(waitingCustomerList.get(i), i);
                }
            }
            WaitingCustomersAdapter adpt = new WaitingCustomersAdapter(items, waitingCustomerDao);
            waitingCustomersView = (RecyclerView) findViewById(R.id.view_cont);
            waitingCustomersView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
            waitingCustomersView.setAdapter(adpt);
        } catch (SQLException e) {
            //TODO logging
            e.printStackTrace();
        }
    }

    public void replyReceived(String contactNumber, String response) {
        int i = 0;
        for(WaitingCustomer waitingCustomer:items) {
            if(contactNumber.contains(waitingCustomer.getContactNumber()) && response!=null && response.trim().equals("1")){
                try {
                    UpdateBuilder<WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                    updateBuilder.where().eq("id", waitingCustomer.getId());
                    updateBuilder.updateColumnValue("confirmed", true);
                    updateBuilder.update();
                    waitingCustomer.setConfirmed(true);
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                waitingCustomersView.getAdapter().notifyItemChanged(i);
            }
            i++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addButtonListener(View v) {
        Intent addScreen = new Intent(this, AddWaitingCustomerActivity.class);
        startActivity(addScreen);
    }

    public void addRow(final com.example.admin.database.WaitingCustomer waitingCustomer, int index) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Timestamp(Calendar.getInstance().getTime().getTime());
        Date date = waitingCustomer.getCreatedTs();
        long min = (date1.getTime() - date.getTime()) / (60 * 1000);
        items.add(new WaitingCustomer(waitingCustomer.getId(),
                waitingCustomer.getName(),
                waitingCustomer.getContactNumber(),
                waitingCustomer.getTotalPeople(),
                waitingCustomer.getEstWaitingTime(),
                ""+min,
                waitingCustomer.getNotes()
        ));
    }
}