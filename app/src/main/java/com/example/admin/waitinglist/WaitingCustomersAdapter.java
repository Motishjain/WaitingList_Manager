package com.example.admin.waitinglist;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.admin.database.WaitingCustomer;
import com.example.admin.viewholder.WaitingCustomerRecordHolder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

public class WaitingCustomersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<WaitingCustomer> waitingCustomerList;
    public Context mContext;
    Dao<WaitingCustomer,Integer> waitingCustomerDao;

    public WaitingCustomersAdapter(final ArrayList<WaitingCustomer> waitingCustomerList, final Dao<WaitingCustomer, Integer> waitingCustomerDao) {
        this.waitingCustomerList = waitingCustomerList;
        this.waitingCustomerDao = waitingCustomerDao;
        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int counter =0; counter< waitingCustomerList.size();counter++) {
                    WaitingCustomer waitingCustomer = waitingCustomerList.get(counter);
                    String newTotalWaitingTime = String.valueOf(Long.parseLong(waitingCustomer.getTotalWaitingTime()) + 1);
                    waitingCustomer.setTotalWaitingTime(newTotalWaitingTime);
                    try {
                        if (Integer.parseInt(newTotalWaitingTime) > Integer.parseInt(waitingCustomer.getEstWaitingTime()) && (waitingCustomer.getNotified() == 0 && waitingCustomer.getConfirmed() == 0 && waitingCustomer.getDelayed() == 0)) {
                            UpdateBuilder<WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                            updateBuilder.where().eq("id", waitingCustomer.getId());
                            updateBuilder.updateColumnValue("delayed", 1);
                            updateBuilder.updateColumnValue("totalWaitingTime", 1);
                            updateBuilder.update();
                            waitingCustomer.setDelayed(1);
                            notifyItemChanged(counter);
                        }
                    } catch (Exception e) {
                        //TODO handle error
                    }
                }
            }
        };

        handler.postDelayed(runnable,60 * 1000);
    }

    public View view;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.waiting_customer_item, parent, false);
        return WaitingCustomerRecordHolder.newInstance(mContext, view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        final WaitingCustomerRecordHolder recordHolder = (WaitingCustomerRecordHolder) holder;
        final WaitingCustomer currentWaitingCustomer = waitingCustomerList.get(position);

        recordHolder.setName(currentWaitingCustomer.getName());
        recordHolder.setTotalPeopleView(currentWaitingCustomer.getTotalPeople());
        recordHolder.setEstWaitingTimeView(currentWaitingCustomer.getEstWaitingTime());
        recordHolder.setTotalWaitingTimeView(currentWaitingCustomer.getTotalWaitingTime());
        recordHolder.setNotesView(currentWaitingCustomer.getNotes());

        if(currentWaitingCustomer.getConfirmed()==1) {
            recordHolder.getWaitingCustomerItemLayout().setBackgroundColor(Color.GREEN);
        } else if(currentWaitingCustomer.getNotified()==1) {
            recordHolder.getWaitingCustomerItemLayout().setBackgroundColor(Color.YELLOW);
        }
        else if(currentWaitingCustomer.getDelayed()==1) {
            recordHolder.getWaitingCustomerItemLayout().setBackgroundColor(0xFF0000);
        }

        recordHolder.notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNumber = currentWaitingCustomer.getContactNumber();
                String message = "Hi " + currentWaitingCustomer.getName() + ", Greetings from Urban Tadka." +
                        " We are ready to serve you. Please reply 1 to confirm your table.";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contactNumber, null, message, null, null);
                try {
                    UpdateBuilder<WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                    updateBuilder.where().eq("id", currentWaitingCustomer.getId());
                    updateBuilder.updateColumnValue("notified", 1);
                    updateBuilder.update();
                    currentWaitingCustomer.setNotified(1);
                    notifyItemChanged(position);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        recordHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                             builder.setTitle("Do you want to delete this record?");
                                                             builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     try {
                                                                         UpdateBuilder<WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                                                                         updateBuilder.where().eq("id", currentWaitingCustomer.getId());
                                                                         updateBuilder.updateColumnValue("deleted", 1);
                                                                         updateBuilder.update();
                                                                         currentWaitingCustomer.setDeleted(1);
                                                                     } catch (Exception e) {
                                                                         e.printStackTrace();
                                                                     }
                                                                     Animation anim = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
                                                                     anim.setDuration(1000);
                                                                     anim.setAnimationListener(new Animation.AnimationListener() {
                                                                         @Override
                                                                         public void onAnimationStart(Animation animation) {

                                                                         }

                                                                         @Override
                                                                         public void onAnimationEnd(Animation animation) {
                                                                             waitingCustomerList.remove(position);
                                                                             notifyItemRemoved(position);
                                                                         }

                                                                         @Override
                                                                         public void onAnimationRepeat(Animation animation) {

                                                                         }
                                                                     });
                                                                     recordHolder.startAnimation(anim);
                                                                 }
                                                             });
                                                             builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(DialogInterface dialog, int which) {

                                                                 }
                                                             });
                                                             builder.show();
                                                         }
                                                     }
        );


    }


    @Override
    public int getItemCount() {
        return waitingCustomerList == null ? 0 : waitingCustomerList.size();
    }
}