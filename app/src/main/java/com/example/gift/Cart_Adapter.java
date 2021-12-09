package com.example.gift;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> arrayList;
    private ArrayList<HashMap<String, Bitmap>> arrayList1;
    private Context c;


    public Cart_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList, ArrayList<HashMap<String, Bitmap>> arrayList1) {
        this.c = c;
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HashMap<String, String> m = arrayList.get(position);
        HashMap<String, Bitmap> m1 = arrayList1.get(position);
        holder.txt_pno.setText(m.get("pno"));
        holder.txt_name.setText(m.get("name"));
        holder.txt_price.setText(m.get("price"));
        holder.qty.setText(m.get("qty"));
        holder.iv.setImageBitmap(m1.get("image"));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_pno, txt_name, txt_price,qty,total;
        Button b1;
        public ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            final Cart vc = (Cart) c;

            txt_pno = (TextView) view.findViewById(R.id.txt_pno);
            txt_name = (TextView) view.findViewById(R.id.name);
            txt_price = (TextView) view.findViewById(R.id.price);
            qty = (TextView) view.findViewById(R.id.qty);
            b1 = (Button) view.findViewById(R.id.Button);
            iv = (ImageView) view.findViewById(R.id.pro_image);


            b1.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    arrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    if(arrayList.size()<0)
                    {
                        vc.iv.setImageResource(R.drawable.box);
                        vc.b1.setEnabled(false);
                        vc.b1.setBackgroundResource(R.drawable.disable_button);
                    }
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            delete(txt_pno.getText().toString());
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();
                    int total=0;
                    for( HashMap<String,String> m:arrayList)
                    {
                        total=total+(Integer.parseInt(m.get("price"))*Integer.parseInt(m.get("qty")));
                    }
                    vc.t1.setText(total+"");

                }

            });


        }

    }
    public static void delete(String r1)
    {
        try
        {
            Connection con = DB_Connection.get_DBConnection();
            PreparedStatement pst = con.prepareStatement("delete from cart where pno=?");
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



