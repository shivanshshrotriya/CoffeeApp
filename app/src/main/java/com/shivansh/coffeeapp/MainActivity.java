package com.shivansh.coffeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    FrameLayout orderNow,orderHistory,dashboard;
    ImageView header_imageView;
    TextView header_textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NewOrder.class));
            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OrderDetails.class));
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DashBoard.class));
            }
        });
    }

    private void Init()
    {
        orderNow = findViewById(R.id.orderNow);
        orderHistory = findViewById(R.id.orderHistory);
        dashboard = findViewById(R.id.dashboard);
        header_textView= findViewById(R.id.text_view_header);
        header_imageView = findViewById(R.id.image_view_header);
        header_textView.setText("Order History");
        Glide.with(getApplicationContext()).
                load(R.drawable.source).into(header_imageView);
    }
}
