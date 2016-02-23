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

    private ArrayList<WaitingCustomer> itemList;
    public Context mContext;
    Dao<com.example.admin.database.WaitingCustomer,Integer> waitingCustomerDao;

    public WaitingCustomersAdapter(ArrayList<WaitingCustomer
            > itemList, Dao<com.example.admin.database.WaitingCustomer, Integer> waitingCustomerDao) {
        this.itemList = itemList;
        this.waitingCustomerDao = waitingCustomerDao;
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
        final WaitingCustomer currentWaitingCustomer = itemList.get(position);

        recordHolder.setName(currentWaitingCustomer.getName());
        recordHolder.setTotalPeopleView(currentWaitingCustomer.getTotalPeople());
        recordHolder.setEstWaitingTimeView(currentWaitingCustomer.getEstWaitingTime());
        recordHolder.setTotalWaitingTimeView(currentWaitingCustomer.getTotalWaitingTime());
        recordHolder.setNotesView(currentWaitingCustomer.getNotes());

        if(currentWaitingCustomer.isConfirmed()) {
        recordHolder.getWaitingCustomerItemLayout().setBackgroundColor(Color.GREEN);
        }
        else if(currentWaitingCustomer.isNotified()) {
            recordHolder.getWaitingCustomerItemLayout().setBackgroundColor(Color.YELLOW);
        }
        else if(currentWaitingCustomer.isDelayed()) {
            recordHolder.getWaitingCustomerItemLayout().setBackgroundColor(Color.RED);
        }

        recordHolder.notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNumber = currentWaitingCustomer.getContactNumber();
                String message = "Hi" + currentWaitingCustomer.getName() + ",\\n Greetings from Urban Tadka." +
                        " We are ready to serve you. Please reply 1 to confirm your table.";
                SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage(contactNumber, null, message, null, null);
                try {
                    UpdateBuilder<WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                    updateBuilder.where().eq("id", currentWaitingCustomer.getId());
                    updateBuilder.updateColumnValue("notified", true);
                    updateBuilder.update();
                    currentWaitingCustomer.setNotified(true);
                    notifyItemChanged(position);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        recordHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                             builder.setTitle("Do you want to delete this record?");
                                                             builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     try {
                                                                         UpdateBuilder<com.example.admin.database.WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                                                                         updateBuilder.where().eq("id", currentWaitingCustomer.getId());
                                                                         updateBuilder.updateColumnValue("deleted", true);
                                                                         updateBuilder.update();
                                                                         currentWaitingCustomer.setDeleted(true);
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
                                                                             itemList.remove(position);
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
        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String newTotalWaitingTime = String.valueOf(Long.parseLong(currentWaitingCustomer.getTotalWaitingTime()) + 1);
                    currentWaitingCustomer.setTotalWaitingTime(newTotalWaitingTime);
                    try {
                        if (Integer.parseInt(newTotalWaitingTime) > Integer.parseInt(currentWaitingCustomer.getEstWaitingTime()) && !currentWaitingCustomer.isDelayed()) {
                            recordHolder.getWaitingCustomerItemLayout().setBackgroundColor(Color.RED);
                            currentWaitingCustomer.setDelayed(true);
                        }
                    }
                    catch (Exception e) {

                    }
                    notifyDataSetChanged();
                    handler.postDelayed(this, 60 * 1000);

                } catch (Exception e) {

                } finally {
                    handler.postDelayed(this,60 * 1000);
                }
            }
        };

        handler.postDelayed(runnable,60 * 1000);

        }


    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }
}