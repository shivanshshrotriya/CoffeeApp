package com.shivansh.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivansh.coffeeapp.Adapter.Dashboard_adapter;
import com.shivansh.coffeeapp.Model.OrderCoffeeModel;

import java.util.ArrayList;
import java.util.Calendar;

public class DashBoard extends AppCompatActivity {

    Button datepicker;
    RecyclerView recyclerView;
    int year,month,dayofmonth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,nameRef,allRef;
    ArrayList<OrderCoffeeModel> arrayList = new ArrayList<>();
    ArrayList<String> arrayList1 = new ArrayList<>();
    ImageView header_imageView;
    TextView header_textView,dateText;
    public static TextView total;
    Dialog progressDialog ;
    Spinner spinner;
    String selectedName,selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Init();
        showdiolog();
        fetchNames();
        dateText.setText(Calendar.getInstance().getTime().toString());
        fetchAllData();
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DashBoard.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthString="",dayOfMonthString="";
                        if((month+1)<10)
                        {monthString = ""+0+""+(month+1);}
                        if(dayOfMonth<10)
                        { dayOfMonthString = ""+0+dayOfMonth;}
                        dateText.setText("Date : "+ dayOfMonth+"-"+monthString+"-"+year);
                        selectedDate = dayOfMonthString+"-"+monthString+"-"+year;
                        fetchData(selectedDate,selectedName);
                    }
                },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedName = spinner.getSelectedItem().toString();
                fetchData(selectedDate,selectedName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void Init()
    {
        datepicker = findViewById(R.id.pick_date_button);
        recyclerView = findViewById(R.id.recycler_view);
        total = findViewById(R.id.total_amt_tv);
        spinner = findViewById(R.id.spinner);
        dateText = findViewById(R.id.date_text);
        header_textView = findViewById(R.id.text_view_header);
        header_imageView = findViewById(R.id.image_view_header);
        header_textView.setText("DashBoard");
        Glide.with(getApplicationContext()).
                load(R.drawable.source).into(header_imageView);
    }

    private void fetchData(String date,String name)
    {
        if(date==null || name==null)
        {
            Toast.makeText(this, "Please Select Date And Name", Toast.LENGTH_SHORT).show();
        }

        else {
            arrayList.clear();
            myRef = database.getReference().child(date).child(name).child("Orderdetails");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        OrderCoffeeModel orderCoffeeModel = postSnapshot.getValue(OrderCoffeeModel.class);
                        arrayList.add(orderCoffeeModel);
                    }
                    progressDialog.dismiss();
                    recyclerView.setAdapter(new Dashboard_adapter(DashBoard.this,arrayList));
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(DashBoard.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void fetchNames()
    {
        arrayList1.clear();
        arrayList1.add("Select Name");
        nameRef = database.getReference();
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot post: postSnapshot.getChildren())
                    {
                        if(!arrayList1.contains(post.getKey()))
                        {
                            arrayList1.add(post.getKey());
                        }

                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrayList1);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchAllData()
    {
        arrayList.clear();
         allRef = database.getReference();
         allRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                     for(DataSnapshot postSnapshot1: postSnapshot.getChildren())
                     {
                         for(DataSnapshot postSnapshot2: postSnapshot1.getChildren())
                         {
                             for(DataSnapshot postSnapshot3:postSnapshot2.getChildren())
                             {
                                 OrderCoffeeModel orderCoffeeModel = postSnapshot3.getValue(OrderCoffeeModel.class);
                                 arrayList.add(orderCoffeeModel);
                             }
                         }
                     }
                 }
                 recyclerView.setAdapter(new Dashboard_adapter(DashBoard.this,arrayList));
                 progressDialog.dismiss();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

    }

    private void showdiolog()
    {
        progressDialog = new Dialog(this);
        progressDialog.setCancelable(false);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.diolog);
        ImageView imageView = progressDialog.findViewById(R.id.image_view_dialog);
        DrawableImageViewTarget drawableImageViewTarget = new DrawableImageViewTarget(imageView);
        Glide.with(getApplicationContext()).
                load(R.drawable.source).into(drawableImageViewTarget);
        progressDialog.show();
    }
}
