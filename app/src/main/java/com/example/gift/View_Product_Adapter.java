package com.example.gift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View_Product_Adapter extends RecyclerView.Adapter<View_Product_Adapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> arrayList;
    private ArrayList<HashMap<String, Bitmap>> arrayList1;
    private Context c;


    public View_Product_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList, ArrayList<HashMap<String, Bitmap>> arrayList1) {
        this.c = c;
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productllist, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String, String> m = arrayList.get(position);
        HashMap<String, Bitmap> m1 = arrayList1.get(position);
        holder.txt_pno.setText( m.get("pno"));
        holder.txt_name.setText(m.get("name"));
        holder.txt_price.setText(m.get("price"));
        holder.txt_category.setText(m.get("category"));
        holder.iv.setImageBitmap(m1.get("image"));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_pno, txt_price, txt_name,txt_category;
        ImageView iv;
        Button b1;

        public MyViewHolder(View view) {
            super(view);

            txt_pno = (TextView) view.findViewById(R.id.txt_userid);
            txt_name = (TextView) view.findViewById(R.id.pro_name);
            txt_price = (TextView) view.findViewById(R.id.price);
            txt_category=(TextView)view.findViewById(R.id.category);
            iv = (ImageView) view.findViewById(R.id.img1);

            b1 = (Button) view.findViewById(R.id.add_cart);

            b1.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if(txt_category.getText().toString().equals("Customized")){
                        Intent i=new Intent(c,CustomProduct.class);
                        i.putExtra("pno", txt_pno.getText().toString());
                        i.putExtra("amt", txt_price.getText().toString());
                        c.startActivity(i);
                    }
                    else {
                        Intent i = new Intent(c, Product.class);
                        i.putExtra("pno", txt_pno.getText().toString());
                        i.putExtra("amt", txt_price.getText().toString());
                        c.startActivity(i);
                    }


                    }
            });

        }
    }
}




