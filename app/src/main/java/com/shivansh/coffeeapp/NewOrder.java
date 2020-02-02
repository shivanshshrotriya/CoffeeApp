package com.shivansh.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shivansh.coffeeapp.Model.OrderCoffeeModel;
import com.shivansh.coffeeapp.Shared.SharedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewOrder extends AppCompatActivity {

    RadioButton radioButton,radioButton2;
    RadioGroup radioGroup_coffee_type,radioGroup_cup_size;
    Button place_order;
    String coffee_type,cup_size;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    String date;
    TextView amount;
    EditText name;
    ImageView header_imageView;
    TextView header_textView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        Init();
        date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());


      radioGroup_coffee_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @SuppressLint("SetTextI18n")
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
              radioButton = (RadioButton) findViewById(checkedId);
              coffee_type = radioButton.getText().toString();
              amount.setText(""+getAmount(radioButton,radioButton2));
          }
      });

        radioGroup_cup_size.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton2 = (RadioButton) findViewById(checkedId);
                cup_size = radioButton2.getText().toString();
                amount.setText(""+getAmount(radioButton,radioButton2));
            }
        });

        place_order.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                if (name.getText().length()<1)
                {
                    Toast.makeText(NewOrder.this, "Please provide us your name to order", Toast.LENGTH_SHORT).show();
                }

                else if(radioButton==null)
                {
                    Toast.makeText(NewOrder.this, "Please Choose Your Coffee Type", Toast.LENGTH_SHORT).show();
                }
                else if(radioButton2==null)
                {
                    Toast.makeText(NewOrder.this, "Please Choose Size of your Coffee", Toast.LENGTH_SHORT).show();
                }
                
                else {
                    SharedName.setUserId(NewOrder.this,name.getText().toString());
                    OrderCoffeeModel orderCoffeeModel = new OrderCoffeeModel();
                    orderCoffeeModel.setAmount(Double.parseDouble(getAmount(radioButton,radioButton2)));
                    orderCoffeeModel.setCoffee_type(coffee_type);
                    orderCoffeeModel.setCup_size(cup_size);
                    orderCoffeeModel.setTime(new SimpleDateFormat("dd-MM-yyy HH:mm:ss").format(Calendar.getInstance().getTime()));
                    Bundle bundle = new Bundle();
                    bundle.putString("name", SharedName.getUserId(NewOrder.this));
                    bundle.putString("coffee_type", coffee_type);
                    bundle.putString("coffee_size",cup_size);
                    mFirebaseAnalytics.logEvent("place_order", bundle);
                    pushdata(myRef,orderCoffeeModel);
                }

            }
        });


    }

    private void Init() {
        place_order = findViewById(R.id.place_order_btn);
        radioGroup_coffee_type = findViewById(R.id.coffee_type_rg);
        radioGroup_cup_size = findViewById(R.id.cup_size_rg);
        amount = findViewById(R.id.total_amt_tv);
        myRef = database.getReference();
        name = findViewById(R.id.name_et);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(NewOrder.this);
        if(!SharedName.getUserId(NewOrder.this).equals(""))
        {
            name.setText(SharedName.getUserId(NewOrder.this));
        }
        header_textView = findViewById(R.id.text_view_header);
        header_imageView = findViewById(R.id.image_view_header);
        header_textView.setText("Order New");
        Glide.with(getApplicationContext()).
                load(R.drawable.source).into(header_imageView);
    }


    private void pushdata(DatabaseReference myRef, OrderCoffeeModel orderCoffeeModel)
    {
        myRef.child(date).child(name.getText().toString()).child("Orderdetails").push().setValue(orderCoffeeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NewOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewOrder.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("DefaultLocale")
    private String getAmount(RadioButton r1 , RadioButton r2)
    {
        if(r1==null || r2==null)
        {
            return ""+0;
        }

        double coffee_type=0,cup_size=0;
        switch(r1.getText().toString())
        {
            case "Espresso": coffee_type = 70; break;
            case "Latte" : coffee_type = 50; break;
            case "Capetuno" : coffee_type = 110; break;
        }

        switch (r2.getText().toString())
        {
            case "Regular": cup_size = 1.1; break;
            case "Medium" : cup_size = 2.5; break;
            case "Large" : cup_size = 3.5; break;
        }

        return  String.format("%.2f",cup_size*coffee_type) ;
    }


}
