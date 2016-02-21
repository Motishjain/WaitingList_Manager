package com.example.admin.waitinglist;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.admin.database.WaitingCustomer;
import com.example.admin.viewholder.WaitingCustomerRecordHolder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
        recordHolder.notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNumber = currentWaitingCustomer.getContactNumber();
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost("http://smsgateway.me/api/v3/messages/send");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", "motishj@ymail.com"));
                nameValuePairs.add(new BasicNameValuePair("password", "sangam123"));
                nameValuePairs.add(new BasicNameValuePair("device", "18283"));
                nameValuePairs.add(new BasicNameValuePair("number", contactNumber));
                nameValuePairs.add(new BasicNameValuePair("message", "Hi" + currentWaitingCustomer.getName() + ",\\n Greetings from Urban Tadka." +
                        " We are ready to serve you. Please reply 1 to confirm your table."));
                try {
                    postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    httpclient.execute(postMethod, new ResponseHandler<HttpResponse>() {
                        @Override
                        public HttpResponse handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                            StatusLine statusLine = httpResponse.getStatusLine();
                            if (statusLine.getStatusCode() == 200) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                alert.setMessage("Success");
                                alert.setMessage("Message Sent Successfully!");
                                alert.setPositiveButton("Ok", null);
                                alert.show();
                            }
                            else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                alert.setMessage("Failure");
                                alert.setMessage("Failed to send message!");
                                alert.setPositiveButton("Ok", null);
                                alert.show();
                            }
                            return httpResponse;
                        }
                    });

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
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
                            updateBuilder.updateColumnValue("isDeleted", true);
                            updateBuilder.update();
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
                        if (Integer.parseInt(newTotalWaitingTime) > Integer.parseInt(currentWaitingCustomer.getEstWaitingTime())) {
                            recordHolder.getWaitingCustomerItem().setBackgroundColor(Color.RED);
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