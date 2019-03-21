package com.mbtex.palpay.Tabs;

public class Tab {

    private String name;
    private String tab_status;
    private String user_tab_status;
    private float balance;
    private int _id;

    public Tab(String name, String status, float balance, int id, String user_tab_status) {
        this.name = name;
        this.tab_status = status;
        this.balance = balance;
        this._id = id;
        this.user_tab_status = user_tab_status;
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.tab_status;
    }

    public float getBalance() {
        return this.balance;
    }

    public int getId() {
        return this._id;
    }
}
