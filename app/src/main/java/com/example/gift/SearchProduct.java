package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchProduct extends AppCompatActivity{
    RecyclerView r;
    EditText editText;
    String filter;
    SearchProduct_Adapter bd;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        r = (RecyclerView) findViewById(R.id.rec);
        editText=(EditText)findViewById(R.id.query);
        r.setLayoutManager(new LinearLayoutManager(this));
        gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter = editText.getText().toString();
                DB_Conn obj = new DB_Conn();
                obj.execute();
            }
        });
    }



    class DB_Conn extends AsyncTask<String, Void, String> {
        String[] from={"name","category"};
        int[] to={R.id.pro_name,R.id.category};
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        ArrayList<HashMap<String, Bitmap>> arrayList1 = new ArrayList<>();

        @Override
        public String doInBackground(String... arg)
        {
            String r = "";
            PreparedStatement pst;
            ResultSet rs;
            Bitmap bitmap;

            try {

                Connection con=DB_Connection.get_DBConnection();

                pst = con.prepareStatement("select * from product where name like ? or category like ?");
                pst.setString(1,"%"+filter+"%");
                pst.setString(2,"%"+filter+"%");
                rs = pst.executeQuery();
                while(rs.next())
                {
                        HashMap<String, String> hashMap = new HashMap<>();
                        HashMap<String, Bitmap> hashMap1 = new HashMap<>();
                        hashMap.put("pno", rs.getString("pno"));
                        hashMap.put("name", rs.getString("name"));
                        hashMap.put("price", rs.getString("price"));
                        hashMap.put("category", rs.getString("category"));
                        byte b[] = rs.getBytes("pic");
                        bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        hashMap1.put("image", bitmap);
                        arrayList.add(hashMap);
                        arrayList1.add(hashMap1);

                    r = "success";
                }
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        public void onProgressUpdate(Void... arg0) //optional
        {

        }

        @Override
        public void onPostExecute(String result) //optional
        {
            if (result.equals("success")) {

                bd = new SearchProduct_Adapter(SearchProduct.this, arrayList,arrayList1,R.layout.searchlist,from,to);
                r.setLayoutManager(gridLayoutManager);
                r.setAdapter(bd);

            }

        }

    }
}