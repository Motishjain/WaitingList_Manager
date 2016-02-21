package com.example.admin.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.waitinglist.R;

/**
 * Created by ali on 3/2/16.
 */
public class WaitingCustomerRecordHolder extends RecyclerView.ViewHolder {
    TextView name, totalPeopleView, estWaitingTimeView, totalWaitingTimeView, notesView;
    View card;
    LinearLayout waitingCustomerItem;
    public Button cancelButton;
    public Button notifyButton;
    public Context context;

    public WaitingCustomerRecordHolder(LinearLayout waitingCustomerItem, Context context, final View itemView, TextView name, TextView totalPeopleView, TextView estWaitingTimeView, TextView totalWaitingTimeView, TextView notesView, Button notifyButton, Button cancelButton) {

        super(itemView);
        this.waitingCustomerItem = waitingCustomerItem;
        this.context = context;
        card = itemView;
        this.name = name;
        this.totalPeopleView = totalPeopleView;
        this.estWaitingTimeView = estWaitingTimeView;
        this.totalWaitingTimeView = totalWaitingTimeView;
        this.notesView = notesView;
        this.cancelButton = cancelButton;
    }

    public static WaitingCustomerRecordHolder newInstance(Context context, View parent) {
        LinearLayout waitingCustomerItem = (LinearLayout) parent.findViewById(R.id.waitingCustomerItem);
        TextView name = (TextView) parent.findViewById(R.id.name);
        TextView people = (TextView) parent.findViewById(R.id.totalPeople);
        TextView estWaitingTime = (TextView) parent.findViewById(R.id.estWaitingTime);
        TextView totalWaitingTime = (TextView) parent.findViewById(R.id.totalWaitingTime);
        TextView notes = (TextView) parent.findViewById(R.id.notes);
        Button notifyButton = (Button) parent.findViewById(R.id.notify);
        Button cancelButton = (Button) parent.findViewById(R.id.delete);
        return new WaitingCustomerRecordHolder(waitingCustomerItem, context, parent, name, people, estWaitingTime, totalWaitingTime, notes,notifyButton, cancelButton);
    }

    public void startAnimation(Animation animation) {
        name.startAnimation(animation);
        totalPeopleView.startAnimation(animation);
        estWaitingTimeView.startAnimation(animation);
        totalWaitingTimeView.startAnimation(animation);
        notesView.startAnimation(animation);
        notifyButton.startAnimation(animation);
        cancelButton.startAnimation(animation);
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getTotalPeopleView() {
        return totalPeopleView;
    }

    public void setTotalPeopleView(TextView totalPeopleView) {
        this.totalPeopleView = totalPeopleView;
    }

    public TextView getEstWaitingTimeView() {
        return estWaitingTimeView;
    }

    public void setEstWaitingTimeView(TextView estWaitingTimeView) {
        this.estWaitingTimeView = estWaitingTimeView;
    }

    public TextView getTotalWaitingTimeView() {
        return totalWaitingTimeView;
    }

    public void setTotalWaitingTimeView(TextView totalWaitingTimeView) {
        this.totalWaitingTimeView = totalWaitingTimeView;
    }

    public View getCard() {
        return card;
    }

    public void setCard(View card) {
        this.card = card;
    }

    public LinearLayout getWaitingCustomerItem() {
        return waitingCustomerItem;
    }

    public void setWaitingCustomerItem(LinearLayout waitingCustomerItem) {
        this.waitingCustomerItem = waitingCustomerItem;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setName(CharSequence name) {
        this.name.setText(name);
    }

    public void setTotalPeopleView(CharSequence totalPeopleView) {
        this.totalPeopleView.setText(totalPeopleView);
    }

    public void setEstWaitingTimeView(CharSequence estWaitingTimeView) {
        this.estWaitingTimeView.setText(estWaitingTimeView);
    }

    public void setTotalWaitingTimeView(CharSequence totalWaitingTimeView) {
        this.totalWaitingTimeView.setText(totalWaitingTimeView);
    }

    public TextView getNotesView() {
        return notesView;
    }

    public void setNotesView(String notesView) {
        this.notesView.setText(notesView);
    }

    public void setNotesView(TextView notesView) {
        this.notesView = notesView;
    }

    public Button getNotifyButton() {
        return notifyButton;
    }

    public void setNotifyButton(Button notifyButton) {
        this.notifyButton = notifyButton;
    }
}