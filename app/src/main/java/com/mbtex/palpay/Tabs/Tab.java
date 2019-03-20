package com.mbtex.palpay.Tabs;

public class Tab {

    private String name;
    private String status;
    private float balance;
    private int _id;

    public Tab(String name, String status, float balance, int id) {
        this.name = name;
        this.status = status;
        this.balance = balance;
        this._id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public float getBalance() {
        return this.balance;
    }

    public int getId() {
        return this._id;
    }
}
