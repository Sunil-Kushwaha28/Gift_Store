package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeliveryAddress extends AppCompatActivity {
    Button b1;
    TextView t1;
    String pno;
    String page;
    String qty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        final String amt = getIntent().getStringExtra("amt");
        final String total = getIntent().getStringExtra("total");
        pno = getIntent().getStringExtra("pno");
        page = getIntent().getStringExtra("page");
        qty=getIntent().getStringExtra("qty");
        t1 = (TextView) findViewById(R.id.editText);

        b1 = (Button) findViewById(R.id.make_payment);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!t1.getText().toString().equals("")) {
                    if (page.equals("cart")) {
                        Intent i = new Intent(DeliveryAddress.this, Payment1.class);
                        i.putExtra("address", t1.getText().toString());
                        i.putExtra("amt", amt);
                        i.putExtra("pno", pno);
                        startActivity(i);
                    }
                    else if(page.equals("product"))
                        {
                            Intent i = new Intent(DeliveryAddress.this, Payment.class);
                            i.putExtra("address", t1.getText().toString());
                            i.putExtra("amt", amt);
                            i.putExtra("pno", pno);
                            i.putExtra("qty", qty);
                            i.putExtra("total", total);
                            startActivity(i);
                        }
                }
                else
                    {
                    t1.setError("Value is required");
                }
            }
        });
    }
}
