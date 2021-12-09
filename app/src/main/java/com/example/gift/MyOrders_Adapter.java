package com.example.gift;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOrders_Adapter extends RecyclerView.Adapter<MyOrders_Adapter.MyViewHolder>  {

    private ArrayList<HashMap<String,String>> arrayList;
    private Context c;

    public MyOrders_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        this.c=c;
        this.arrayList = arrayList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String,String> m = arrayList.get(position);

        holder.txt_name.setText(m.get("name"));
        holder.txt_date.setText(m.get("date"));
        holder.txt_oid.setText(m.get("id"));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name,txt_date,txt_oid;
        Button b1;

        public MyViewHolder(View view) {
            super(view);

            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_oid = (TextView) view.findViewById(R.id.txt_oid);
            b1=(Button)view.findViewById(R.id.details);
            b1.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    Intent i=new Intent(c,OrderDetails.class);
                    i.putExtra("oid", txt_oid.getText().toString());
                    i.putExtra("date", txt_date.getText().toString());
                    i.putExtra("name", txt_name.getText().toString());
                    c.startActivity(i);
                }

            });


        }


    }
}

