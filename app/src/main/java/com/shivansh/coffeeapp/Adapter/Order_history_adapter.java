package com.shivansh.coffeeapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shivansh.coffeeapp.Model.OrderCoffeeModel;
import com.shivansh.coffeeapp.R;

import java.util.List;

public class Order_history_adapter extends RecyclerView.Adapter<Order_history_adapter.ProductViewHolder> {


    private Context mCtx;
    private List<OrderCoffeeModel> orderCoffeeModelList;

    public Order_history_adapter(Context mCtx, List<OrderCoffeeModel> orderCoffeeModelList) {
        this.mCtx = mCtx;
        this.orderCoffeeModelList = orderCoffeeModelList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_history_view,null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int i) {
        final OrderCoffeeModel orderCoffeeModel = orderCoffeeModelList.get(i);
        holder.coffee_type.setText(orderCoffeeModel.getCoffee_type());
        holder.coffee_size.setText(orderCoffeeModel.getCup_size());
        holder.amount.setText(""+orderCoffeeModel.getAmount()+" INR");
        holder.datetime.setText(orderCoffeeModel.getTime());
    }

    @Override
    public int getItemCount() {
        return orderCoffeeModelList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView coffee_type,coffee_size,amount,datetime;

        public ProductViewHolder(View itemView)  {
            super(itemView);
            coffee_type= itemView.findViewById(R.id.coffee_type_tv);
            coffee_size = itemView.findViewById(R.id.coffee_size_tv);
            amount = itemView.findViewById(R.id.amount_tv);
            datetime = itemView.findViewById(R.id.date_time_tv);
        }
    }
    public void updateList(List<OrderCoffeeModel> list){
        orderCoffeeModelList = list;
        notifyDataSetChanged();
    }


}
