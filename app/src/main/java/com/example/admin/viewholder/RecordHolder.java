package com.example.admin.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.waitinglist.R;

/**
 * Created by ali on 3/2/16.
 */
public class RecordHolder extends RecyclerView.ViewHolder{
        TextView name,contact,people,est_wait,tot_wait;
        View card;
        public Button cancel;
        public Context context;
        public RecordHolder(Context context,final View itemView,TextView name,TextView contact,TextView people,TextView est_wait,TextView tot_wait,Button cancel)
        {

            super(itemView);
            this.context = context;
            card = itemView;
            this.name = name;
            this.people= people;
            this.contact= contact;
            this.est_wait = est_wait;
            this.tot_wait=tot_wait;
            this.cancel = cancel;
        }
        public static RecordHolder newInstance(Context context,View parent)
        {
            /*Typeface font = Typeface.createFromAsset(context.getAssets(),"segoe_ui.ttf");*/

            TextView name = (TextView) parent.findViewById(R.id.name);
            TextView contact_no = (TextView) parent.findViewById(R.id.contact_no);
            TextView people = (TextView) parent.findViewById(R.id.people);
            TextView est_wait = (TextView) parent.findViewById(R.id.est_wait);
            TextView tot_wait = (TextView) parent.findViewById(R.id.tot_wait);
            Button cancel = (Button) parent.findViewById(R.id.delete);

            /*name.setTypeface(font);
            contact_no.setTypeface(font);
            people.setTypeface(font);
            est_wait.setTypeface(font);
            tot_wait.setTypeface(font);*/

            return new RecordHolder(context,parent,name,contact_no,people,est_wait,tot_wait,cancel);
        }

    public void setName(CharSequence name)
    {
        this.name.setText(name);
    }
    public void setContact(CharSequence contact)
    {
        this.contact.setText(contact);
    }
    public void setPeople(CharSequence people)
    {
        this.people.setText(people);
    }
    public void setEst_wait(CharSequence est_wait)
    {
        this.est_wait.setText(est_wait);
    }
    public void setTot_wait(CharSequence tot_wait)
    {
        this.tot_wait.setText(tot_wait);
    }

}