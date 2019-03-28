package com.mbtex.palpay.Tabs;

public class TabTransaction {
    private String imageSource;
    private String username;
    private String status;
    private String _user_transaction_status;
    private String date;
    private String type;
    private float amount;
    private int _user_transaction_id;
    private int _transaction_id;

    public static String APPROVED = "APPROVED";
    public static String PENDING = "PENDING";
    public static String DECLINED = "DECLINED";

    public TabTransaction(String imageSource, String username, String status, String userTransactionStatus,String date, String type, float amount, int transaction_id, int user_transaction_id) {
        this.imageSource = imageSource;
        this.username = username;
        this.status = status;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this._transaction_id = transaction_id;
        this._user_transaction_id = user_transaction_id;
        this._user_transaction_status = userTransactionStatus;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public float getAmount() {
        return amount;
    }

    public int getUserTransactionId() {
        return _user_transaction_id;
    }

    public int getTransactionId() {return this._transaction_id;}

    public String getUserTransactionStatus() {
        return this._user_transaction_status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getTransactionText(String type) {

        switch (type) {
            case "WITHDRAW":
                return "B";
            case "DEPOSIT":
                return "P";
            default:
                return "?";
        }
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public void updateTransactionStatus(String status) {
        this._user_transaction_status = status;
        this.status = status;
    }
}
