package com.example.gift;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class    Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    String name, emailid;
    String plants = "Plants", flowers = "Flowers", sweet = "Sweet", custom = "Customized", toys = "Toys & Games", cake = "Cake", homedecoration = "Home Decoration", jewelry = "Jewelry";
    TextView Name, username;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        b1 = (Button) findViewById(R.id.flowers);
        b2 = (Button) findViewById(R.id.custom);
        b3 = (Button) findViewById(R.id.cake);
        b4 = (Button) findViewById(R.id.plants);
        b5 = (Button) findViewById(R.id.toys);
        b6 = (Button) findViewById(R.id.sweet);
        b8 = (Button) findViewById(R.id.home);
        b9 = (Button) findViewById(R.id.jewelry);
        DB_Conn obj = new DB_Conn();
        obj.execute();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        Name = (TextView) headerView.findViewById(R.id.name);
        username = (TextView) headerView.findViewById(R.id.userid);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void onClick(View v) {
        if (v.getId() == R.id.flowers) {
            Intent i = new Intent(Home.this, ViewProduct.class);
            i.putExtra("category", flowers);
            startActivity(i);
        } else if (v.getId() == R.id.custom) {
            Intent i = new Intent(Home.this, ViewProduct.class);
            i.putExtra("category", custom);
            startActivity(i);
        } else if (v.getId() == R.id.cake) {
            Intent i = new Intent(this, ViewProduct.class);
            i.putExtra("category", cake);
            startActivity(i);
        } else if (v.getId() == R.id.plants) {
            Intent i = new Intent(this, ViewProduct.class);
            i.putExtra("category", plants);
            startActivity(i);
        } else if (v.getId() == R.id.sweet) {
            Intent i = new Intent(this, ViewProduct.class);
            i.putExtra("category", sweet);
            startActivity(i);
        } else if (v.getId() == R.id.toys) {
            Intent i = new Intent(this, ViewProduct.class);
            i.putExtra("category", toys);
            startActivity(i);
        } else if (v.getId() == R.id.home) {
            Intent i = new Intent(this, ViewProduct.class);
            i.putExtra("category", homedecoration);
            startActivity(i);
        } else if (v.getId() == R.id.jewelry) {
            Intent i = new Intent(this, ViewProduct.class);
            i.putExtra("category", jewelry);
            startActivity(i);
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cart:
                Intent i = new Intent(this, Cart.class);
                startActivity(i);
                break;
            case R.id.search:
                Intent i2 = new Intent(this, SearchProduct.class);
                startActivity(i2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.order) {
            Intent i = new Intent(this, MyOrders.class);
            startActivity(i);
        } else if (id == R.id.ch_pass) {
            Intent i = new Intent(this, ChangePassword.class);
            startActivity(i);

        } else if (id == R.id.home) {
            Intent i = new Intent(this, Home.class);
            startActivity(i);

        }
        else if (id == R.id.feedback) {
            Intent i = new Intent(this, Feedback.class);
            startActivity(i);

        }
        else if (id == R.id.contact) {
            Intent i = new Intent(this, Contact.class);
            startActivity(i);

        }else if (id == R.id.logout) {
            SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.clear();
            ed.commit();
            Intent i = new Intent(this, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    class DB_Conn extends AsyncTask<Void, Void, String> {

        @Override
        public String doInBackground(Void... arg) //compulsory to implement
        {
            PreparedStatement pst;
            String r = "";
            try {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                String userid = sp.getString("userid", null);
                Connection con = DB_Connection.get_DBConnection();

                pst = con.prepareStatement("select * from user where emailid=?");
                pst.setString(1, userid);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    name = rs.getString("name");
                    emailid = rs.getString("emailid");
                    r = "success";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;

        }

        public void onPostExecute(String result) {
            if (result.equals("success")) {
                Name.setText(name);
                username.setText(emailid);

            }
        }

    }
}



