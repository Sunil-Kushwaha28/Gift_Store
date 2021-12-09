package com.example.gift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class View_Feedback_Adapter extends RecyclerView.Adapter<View_Feedback_Adapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> arrayList;
    private Context c;

    public View_Feedback_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        this.c = c;
        this.arrayList = arrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedbacklist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String, String> m = arrayList.get(position);

        holder.txt_name.setText(m.get("name"));
        holder.txt_date.setText("Date: " + m.get("date"));
        holder.txt_feedback.setText(m.get("feedback"));
        holder.txt_rate.setText("Rating : " + m.get("rate"));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_date, txt_feedback, txt_rate;

        public MyViewHolder(View view) {
            super(view);

            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_feedback = (TextView) view.findViewById(R.id.txt_feedback);
            txt_rate = (TextView) view.findViewById(R.id.rate);

        }

    }
}



