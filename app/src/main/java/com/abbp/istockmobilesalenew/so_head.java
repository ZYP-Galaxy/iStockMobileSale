package com.abbp.istockmobilesalenew;

public class so_head {
    long locationid;
    int cash_id;
    long so_id;
    int pay_type;
    double discount;
    double discount_per;
    String remark;
    double adv_pay_per;
    double advance_pay;

    public so_head(long so_id, int pay_type, double discount, double discount_per) {
        this.so_id = so_id;
        this.pay_type = pay_type;
        this.discount = discount;
        this.discount_per = discount_per;
    }

    public so_head(long locationid,int cash_id,long so_id, int pay_type, double discount, double discount_per,String remark) {
        this.locationid=locationid;
        this.cash_id=cash_id;
        this.so_id = so_id;
        this.pay_type = pay_type;
        this.discount = discount;
        this.discount_per = discount_per;
        this.remark=remark;
    }

    public so_head(long locationid,int cash_id,long so_id, int pay_type, double discount, double discount_per,String remark,double adv_pay_per,double advance_pay) {
        this.locationid=locationid;
        this.cash_id=cash_id;
        this.so_id = so_id;
        this.pay_type = pay_type;
        this.discount = discount;
        this.discount_per = discount_per;
        this.remark=remark;
        this.adv_pay_per=adv_pay_per;
        this.advance_pay=advance_pay;
    }


    public long getSo_id() {
        return so_id;
    }

    public void setSo_id(long so_id) {
        this.so_id = so_id;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscount_per() {
        return discount_per;
    }

    public void setDiscount_per(double discount_per) {
        this.discount_per = discount_per;
    }

    public long getLocationid() {
        return locationid;
    }

    public void setLocationid(long locationid) {
        this.locationid = locationid;
    }

    public int getCash_id() {
        return cash_id;
    }

    public void setCash_id(int cash_id) {
        this.cash_id = cash_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getAdv_pay_per() {
        return adv_pay_per;
    }

    public void setAdv_pay_per(double adv_pay_per) {
        this.adv_pay_per = adv_pay_per;
    }

    public double getAdvance_pay() {
        return advance_pay;
    }

    public void setAdvance_pay(double advance_pay) {
        this.advance_pay = advance_pay;
    }
}
