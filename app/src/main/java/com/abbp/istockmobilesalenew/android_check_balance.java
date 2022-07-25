package com.abbp.istockmobilesalenew;

public class android_check_balance {
    private  int userid;
    long locationid;
    long code;
    int sr;
    double qty;
    double balance;

    public android_check_balance(int userid, long locationid, long code, int sr, double qty, double balance) {

        this.userid = userid;
        this.locationid = locationid;
        this.code = code;
        this.sr = sr;
        this.qty = qty;
        this.balance = balance;

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public long getLocationid() {
        return locationid;
    }

    public void setLocationid(long locationid) {
        this.locationid = locationid;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public int getSr() {
        return sr;
    }

    public void setSr(int sr) {
        this.sr = sr;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
