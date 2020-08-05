package com.example.inclass11;

import java.util.Date;
import java.util.HashMap;

public class Expense {
    private String expenseName;
    private String category;
    private double amount;
    private Date date;
    HashMap<String, Object> hashmap;

    public Expense(){}

    public Expense(String expenseName, String category, double amount, Date date) {
        this.expenseName = expenseName;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseName='" + expenseName + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }

    public HashMap<String, Object> createHashMap(){
        hashmap = new HashMap<>();
        hashmap.put("expenseName",getExpenseName());
        hashmap.put("category",getCategory());
        hashmap.put("amount",getAmount());
        hashmap.put("date",getDate());
        return this.hashmap;
    }
}
