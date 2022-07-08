package com.abbp.istockmobilesalenew;

import java.nio.channels.Pipe;

public class outstandlist {
    private  long customerid;
    private  String Customer;
    private  String Currency;
    private double Opening;
    private double Sale;
    private double ReturnIn;
    private double Discount;
    private double Paid;
    private double Balance;

    public outstandlist( long customerid,String Customer,String Currency, double Opening, double Sale,double ReturnIn,double Discount,double Paid,double Balance)

    {
        this.customerid=customerid;
        this.Customer=Customer;
        this.Currency=Currency;
        this.Opening=Opening;
        this.Sale=Sale;
        this.ReturnIn=ReturnIn;
        this.Discount=Discount;
        this.Paid= Paid;
        this.Balance=Balance;
    }


    public long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(long customerid) {
        this.customerid = customerid;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public double getOpening() {
        return Opening;
    }

    public void setOpening(double opening) {
        Opening = opening;
    }

    public double getSale() {
        return Sale;
    }

    public void setSale(double sale) {
        Sale = sale;
    }

    public double getReturnIn() {
        return ReturnIn;
    }

    public void setReturnIn(double returnIn) {
        ReturnIn = returnIn;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getPaid() {
        return Paid;
    }

    public void setPaid(double paid) {
        Paid = paid;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }
}
