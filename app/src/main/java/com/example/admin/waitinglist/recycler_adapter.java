package com.example.admin.waitinglist;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.database.WaitingCustomer;
import com.example.admin.waitinglist.bean_item;

import com.example.admin.waitinglist.R;
import com.example.admin.viewholder.RecordHolder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.logging.Handler;

public class recycler_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<bean_item> mItemList;
    public Context mContext;
    public bean_item menu_pointer1;
    Dao<WaitingCustomer,Integer> waitingCustomerDao;

    public recycler_adapter(ArrayList<bean_item> itemList,Dao<WaitingCustomer,Integer> waitingCustomerDao) {
        mItemList = itemList;
        this.waitingCustomerDao=waitingCustomerDao;
    }

    public View view;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return RecordHolder.newInstance(mContext,view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        final RecordHolder recordHolder = (RecordHolder) holder;
        final bean_item menu_pointer = mItemList.get(position);


        String name = menu_pointer.getName();
        String contact_no = menu_pointer.getContact();
        String people = menu_pointer.getPeople();
        String est_wait = menu_pointer.getEst_wait();
        String tot_wait = menu_pointer.getTot_wait();

        recordHolder.setName(name);
        recordHolder.setContact(contact_no);
        recordHolder.setPeople(people);
        recordHolder.setEst_wait(est_wait);
        recordHolder.setTot_wait(tot_wait);
        recordHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Do you want to delete this record?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {

                            QueryBuilder<WaitingCustomer, Integer> queryBuilder;
                            queryBuilder = waitingCustomerDao.queryBuilder();
                            UpdateBuilder<WaitingCustomer, Integer> updateBuilder = waitingCustomerDao.updateBuilder();
                            updateBuilder.where().eq("id", menu_pointer.getId());
                            updateBuilder.updateColumnValue("isDeleted", true);
                            int rowCountUpdated = updateBuilder.update();
                        } catch (Exception e) {
                        }
                        Animation anim = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
                        anim.setDuration(1000);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mItemList.remove(position);
                                notifyItemRemoved(position);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        recordHolder.cancel.startAnimation(anim);
                        /*recordHolder.itemView.setVisibility(View.GONE);*/
                        /*anim.setFillAfter(true);*/



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
                    menu_pointer1 = mItemList.get(position);
                    menu_pointer1.setTot_wait(String.valueOf(Long.parseLong(menu_pointer1.getTot_wait()) + 1));
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
        return mItemList == null ? 0 : mItemList.size();
    }


}

