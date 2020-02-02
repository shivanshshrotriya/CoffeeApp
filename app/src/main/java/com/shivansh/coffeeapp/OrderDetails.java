package com.shivansh.coffeeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivansh.coffeeapp.Adapter.Order_history_adapter;
import com.shivansh.coffeeapp.Model.OrderCoffeeModel;
import com.shivansh.coffeeapp.Shared.SharedName;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,nameRef;
    ArrayList<OrderCoffeeModel> arrayList;
    ArrayList<String> arrayList1;
    ImageView header_imageView;
    TextView header_textView,nameText;
    Dialog progressDialog;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Init();
        showdiolog();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot snapshot: postSnapshot.child(SharedName.getUserId(OrderDetails.this)).child("Orderdetails").getChildren())
                    {
                        OrderCoffeeModel orderCoffeeModel = snapshot.getValue(OrderCoffeeModel.class);
                        arrayList.add(orderCoffeeModel);
                    }
                }
                progressDialog.dismiss();
                recyclerView.setAdapter(new Order_history_adapter(OrderDetails.this,arrayList));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OrderDetails.this, "Failed", Toast.LENGTH_SHORT).show();
            }
         });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void Init()
    {
        recyclerView = findViewById(R.id.recycler_view);
        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        header_textView = findViewById(R.id.text_view_header);
        searchView = findViewById(R.id.searchview);
        nameText = findViewById(R.id.name_text);
        if(!SharedName.getUserId(OrderDetails.this).equals(""))
        {
            nameText.setText("Showing Result for '" +SharedName.getUserId(OrderDetails.this)+"'");
        }
        header_imageView = findViewById(R.id.image_view_header);
        header_textView.setText("Order History");
        Glide.with(getApplicationContext()).
                load(R.drawable.source).into(header_imageView);
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

    void filter(String text){
        ArrayList<OrderCoffeeModel> temp = new ArrayList();
        for(OrderCoffeeModel d: arrayList){
            if(d.getCoffee_type().toLowerCase().contains(text.toLowerCase())|| d.getTime().toLowerCase().contains(text.toLowerCase()) ||  d.getCup_size().toLowerCase()
                    .contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        recyclerView.setAdapter(new Order_history_adapter(OrderDetails.this,temp));
    }

}
