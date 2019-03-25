package com.mbtex.palpay.Tabs;

class TabTransaction {
    private String imageSource;
    private String username;
    private String status;
    private String date;
    private String type;
    private float amount;
    private int _id;

    public static String TAB_APPROVED = "APPROVED";
    public static String TAB_PENDING = "PENDING";
    public static String TAB_INACTIVE = "INACTIVE";

    public TabTransaction(String imageSource, String username, String status, String date, String type, float amount, int _id) {
        this.imageSource = imageSource;
        this.username = username;
        this.status = status;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this._id = _id;
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

    public int get_id() {
        return _id;
    }


    public void setStatus(String status) {

        this.status = status;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}
