package com.example.gift;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewProductAdmin_Adapter extends RecyclerView.Adapter<ViewProductAdmin_Adapter.MyViewHolder>  {

    private ArrayList<HashMap<String,String>> arrayList;
    private ArrayList<HashMap<String, Bitmap>> arrayList1;
    private Context c;


    public ViewProductAdmin_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList, ArrayList<HashMap<String, Bitmap>> arrayList1) {
        this.c=c;
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productlist_admin, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String,String> m = arrayList.get(position);
        HashMap<String, Bitmap> m1 = arrayList1.get(position);
        holder.txt_name.setText(m.get("name"));
        holder.txt_price.setText("₹"+m.get("price"));
        holder.txt_mno.setText(m.get("pno"));
        holder.txt_company.setText("Category:  "+m.get("category"));
        holder.iv.setImageBitmap(m1.get("image"));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_mno,txt_price,txt_name,txt_company;
        Button b1;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);

            txt_mno = (TextView) view.findViewById(R.id.number);
            txt_name = (TextView) view.findViewById(R.id.name);
            txt_price = (TextView) view.findViewById(R.id.price);
            txt_company = (TextView) view.findViewById(R.id.category);
            iv = (ImageView) view.findViewById(R.id.pro_image);
            b1=(Button)view.findViewById(R.id.button5);

            b1.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){

                    arrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            delete(txt_mno.getText().toString());
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();


                }

            });


        }

    }
    public static void delete(String r1)
    {
        try
        {
            Connection con = DB_Connection.get_DBConnection();
            PreparedStatement pst = con.prepareStatement("delete from product where pno=?");
            pst.setString(1, r1);
            pst.executeUpdate();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}

