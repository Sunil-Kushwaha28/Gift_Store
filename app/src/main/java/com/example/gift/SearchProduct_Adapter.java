package com.example.gift;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchProduct_Adapter extends RecyclerView.Adapter<SearchProduct_Adapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> arrayList;
    private ArrayList<HashMap<String, Bitmap>> arrayList1;
    private Context c;


    public SearchProduct_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList, ArrayList<HashMap<String, Bitmap>> arrayList1, int searchlist, String[] from, int[] to) {
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
        holder.txt_price.setText("Rs " + m.get("price"));
        holder.txt_category.setText(m.get("category"));
        holder.iv.setImageBitmap(m1.get("image"));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                HashMap<String, String> arrayList;
                arrayList = (HashMap<String,String>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                Filter.FilterResults results = new FilterResults();
                ArrayList<String> FilteredArrayNames = new ArrayList<>();
                HashMap<String, String> mDatabaseOfNames = null;

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < mDatabaseOfNames.size(); i++) {
                    String dataNames = mDatabaseOfNames.get(i);
                    if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(dataNames);
                    }
                }
                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
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
                        c.startActivity(i);
                    }
                    else {
                        Intent i = new Intent(c, Product.class);
                        i.putExtra("pno", txt_pno.getText().toString());
                        c.startActivity(i);
                    }
                }
            });

        }
    }
}




