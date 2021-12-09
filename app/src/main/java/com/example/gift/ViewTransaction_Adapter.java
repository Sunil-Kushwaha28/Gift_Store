package com.example.gift;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewTransaction_Adapter extends RecyclerView.Adapter<ViewTransaction_Adapter.MyViewHolder>  {

    private ArrayList<HashMap<String,String>> arrayList;
    private Context c;

    public ViewTransaction_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        this.c=c;
        this.arrayList = arrayList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactionlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String,String> m = arrayList.get(position);

        holder.txt_ono.setText(m.get("oid"));
        holder.txt_odate.setText("Date: "+m.get("odate"));
        holder.txt_address.setText("Address: "+m.get("address"));
        holder.txt_mode.setText("Mode: "+m.get("mode"));
        holder.txt_userid.setText("UserID: "+m.get("userid"));
        holder.txt_total.setText("Amount: "+m.get("total"));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_ono,txt_odate,txt_address,txt_mode,txt_userid,txt_total;
        Button b1;

        public MyViewHolder(View view) {
            super(view);

            txt_ono = (TextView) view.findViewById(R.id.oid_number);
            txt_odate = (TextView) view.findViewById(R.id.o_date);
            txt_address = (TextView) view.findViewById(R.id.address);
            txt_mode = (TextView) view.findViewById(R.id.mode);
            txt_userid = (TextView) view.findViewById(R.id.userid);
            txt_total = (TextView) view.findViewById(R.id.total);
            b1 = (Button) view.findViewById(R.id.show_product);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(c,ShowProduct.class);
                    i.putExtra("no", txt_ono.getText().toString());
                    c.startActivity(i);

                }
            });


        }
    }

}
