package com.example.gift;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewCustomPicture_Adapter extends RecyclerView.Adapter<ViewCustomPicture_Adapter.MyViewHolder>  {

    private ArrayList<HashMap<String,String>> arrayList;
    private static Context c;

    public ViewCustomPicture_Adapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        this.c=c;
        this.arrayList = arrayList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photolist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            HashMap<String, String> m = arrayList.get(position);
            holder.txt_userid.setText("Userid: " + m.get("userid"));
            holder.txt_name.setText("Name: " + m.get("name"));
            holder.txt_no.setText( m.get("no"));
            holder.txt_date.setText("Date: " + new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(m.get("date"))));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_userid,txt_date,txt_no,txt_name;
        Button b1,b2;

        public MyViewHolder(View view) {
            super(view);

            txt_userid = (TextView) view.findViewById(R.id.userid);
            txt_no= (TextView) view.findViewById(R.id.no);
            txt_date = (TextView) view.findViewById(R.id.date);
            txt_name = (TextView) view.findViewById(R.id.name);


            b1=(Button)view.findViewById(R.id.button5);
            b2=(Button)view.findViewById(R.id.button19);

            b1.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){

                    Intent i=new Intent(c,DisplayPhoto.class);
                    i.putExtra("no",txt_no.getText().toString());
                    c.startActivity(i);

                }

            });

            b2.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    final Handler handler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            download(txt_no.getText().toString());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(c, "File Downloaded", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();



                }

            });


        }

    }
    public static void download(String r1)
    {
        try
        {
            Connection con = DB_Connection.get_DBConnection();
            PreparedStatement pst = con.prepareStatement("select * from custom_photo where no=?");
            pst.setString(1, r1);
            ResultSet rs = pst.executeQuery();
            rs.next();
            Blob blob = rs.getBlob("pic");
            InputStream is = blob.getBinaryStream();
            File f = new File(c.getExternalFilesDir(null),rs.getString("userid")+".jpg");
            FileOutputStream fos = new FileOutputStream(f);

            int b = 0;
            while ((b = is.read()) != -1)
            {
                fos.write(b);
            }
            fos.flush();
            fos.close();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



}
