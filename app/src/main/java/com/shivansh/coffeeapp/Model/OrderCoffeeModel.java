package com.shivansh.coffeeapp.Model;

public class OrderCoffeeModel {
      String  coffee_type,cup_size,time;
      double amount;

      public OrderCoffeeModel()
      {

      }

      public OrderCoffeeModel(String coffee_type, String cup_size, String time, double amount) {
            this.coffee_type = coffee_type;
            this.cup_size = cup_size;
            this.time = time;
            this.amount = amount;
      }

      public String getCoffee_type() {
            return coffee_type;
      }

      public void setCoffee_type(String coffee_type) {
            this.coffee_type = coffee_type;
      }

      public String getCup_size() {
            return cup_size;
      }

      public void setCup_size(String cup_size) {
            this.cup_size = cup_size;
      }

      public String getTime() {
            return time;
      }

      public void setTime(String time) {
            this.time = time;
      }

      public double getAmount() {
            return amount;
      }

      public void setAmount(double amount) {
            this.amount = amount;
      }
}
