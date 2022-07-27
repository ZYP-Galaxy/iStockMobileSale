package com.abbp.istockmobilesalenew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.abbp.istockmobilesalenew.tvsale.sale_entry_tv;

import java.util.ArrayList;

public class CustGroupAdapter extends RecyclerView.Adapter<CustGroupAdapter.MyViewHolder> {

    Context context;
    ArrayList<customergroup> data = new ArrayList<>();
    Button btn;
    public static boolean create_customer;
    androidx.appcompat.app.AlertDialog da;
    public static String formname;//added by YLT
    String contextString = "";

    public CustGroupAdapter(Context context, ArrayList<customergroup> data, Button btn, androidx.appcompat.app.AlertDialog da) {
        this.data = data;
        this.context = context;
        this.btn = btn;
        this.da = da;
        formname = "Sale";
        create_customer = false;
        contextString = context.getClass().toString().split("com.abbp.istockmobilesalenew.")[1];
        Log.i("Customer new", contextString);
    }

    public CustGroupAdapter(Context context, ArrayList<customergroup> data, Button btn, androidx.appcompat.app.AlertDialog da, String frm) {
        this.data = data;
        this.context = context;
        this.btn = btn;
        this.da = da;
        formname = frm;
        create_customer = false;
        contextString = context.getClass().toString().split("com.abbp.istockmobilesalenew.")[1];
        Log.i("Customer new", contextString);
    }

    @Override
    public CustGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v = lf.inflate(R.layout.headerbinding, parent, false);
        return new CustGroupAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustGroupAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.btn.setText(" " + data.get(position).getName());
        holder.btn.setBackgroundResource(R.drawable.usercodegradiant);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText(data.get(position).getName());
                switch (contextString) {
                    case "sale_entry":
                        sale_entry.selected_custgroupid = data.get(position).getCustgroupid();
                        break;
                    case "saleorder_entry":
                        saleorder_entry.selected_custgroupid = data.get(position).getCustgroupid();
                        break;
                    case "returnin_entry":
                        returnin_entry.selected_custgroupid = data.get(position).getCustgroupid();
                        break;
                    default:
                        sale_entry_tv.selected_custgroupid = data.get(position).getCustgroupid();
                        break;
                }
                /*if(formname =="SaleOrder")
                {
                    saleorder_entry.selected_custgroupid = data.get(position).getCustgroupid();
                }
                else if(formname=="ReturnIn")
                {
                    returnin_entry.selected_custgroupid = data.get(position).getCustgroupid();
                }
                else {
                    sale_entry.selected_custgroupid = data.get(position).getCustgroupid();
                }*/
                BindMaxCustTownship();
                da.dismiss();
            }
        });
    }

    private void BindMaxCustTownship() {
        String sqlString = "";
        Cursor cursor;
        switch (contextString) {
            case "sale_entry":
                sqlString = "select customerid,customer_name,Townshipid,Townshipname,credit from customer where customerid=(select min(customerid)customerid " +
                        " from customer" +
                        " where CustGroupID=" + sale_entry.selected_custgroupid +
                        " group by CustGroupID)";
                cursor = DatabaseHelper.rawQuery(sqlString);
                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                            String customername = cursor.getString(cursor.getColumnIndex("customer_name"));
                            long townshipid = cursor.getLong(cursor.getColumnIndex("Townshipid"));
                            String townshipname = cursor.getString(cursor.getColumnIndex("Townshipname"));
                            boolean credit = cursor.getInt(cursor.getColumnIndex("credit")) == 1 ? true : false;
                            if (create_customer) {
                                sale_entry.imgCustomerTownship.setText(townshipname);
                                create_customer = false;
                            } else {
                                sale_entry.btntownship.setText(townshipname);
                                sale_entry.btncustomer.setText(customername);
                                sale_entry.selected_townshipid = townshipid;
                                sale_entry.creditcustomer = credit;
                                sale_entry.isCreditcustomer = credit;
                                String pay_type = credit == false ? "Cash Down" : "Credit";
                                sale_entry.btnpaytype.setText(pay_type);
                                sale_entry.sh.get(0).setTownshipid(townshipid);
                                sale_entry.sh.get(0).setCustomerid(customerid);
                                sale_entry.sh.get(0).setPay_type(credit == false ? 1 : 2);
                            }


                        } while (cursor.moveToNext());

                    }

                }
                cursor = null;
                break;
            case "saleorder_entry":
                sqlString = "select customerid,customer_name,Townshipid,Townshipname,credit from customer where customerid=(select min(customerid)customerid " +
                        " from customer" +
                        " where CustGroupID=" + saleorder_entry.selected_custgroupid +
                        " group by CustGroupID)";
                cursor = DatabaseHelper.rawQuery(sqlString);
                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                            String customername = cursor.getString(cursor.getColumnIndex("customer_name"));
                            long townshipid = cursor.getLong(cursor.getColumnIndex("Townshipid"));
                            String townshipname = cursor.getString(cursor.getColumnIndex("Townshipname"));
                            boolean credit = cursor.getInt(cursor.getColumnIndex("credit")) == 1 ? true : false;
                            if (create_customer) {
                                saleorder_entry.imgCustomerTownship.setText(townshipname);
                                create_customer = false;
                            } else {
                                saleorder_entry.btntownship.setText(townshipname);
                                saleorder_entry.btncustomer.setText(customername);
                                saleorder_entry.selected_townshipid = townshipid;
                                saleorder_entry.creditcustomer = credit;
                                saleorder_entry.isCreditcustomer = credit;
                                String pay_type = credit == false ? "Cash Down" : "Credit";
                                saleorder_entry.btnpaytype.setText(pay_type);
                                saleorder_entry.sh.get(0).setTownshipid(townshipid);
                                saleorder_entry.sh.get(0).setCustomerid(customerid);
                                saleorder_entry.sh.get(0).setPay_type(credit == false ? 1 : 2);
                            }


                        } while (cursor.moveToNext());

                    }

                }
                cursor = null;
                break;
            case "returnin_entry":
                sqlString = "select customerid,customer_name,Townshipid,Townshipname,credit from customer where customerid=(select min(customerid)customerid " +
                        " from customer" +
                        " where CustGroupID=" + returnin_entry.selected_custgroupid +
                        " group by CustGroupID)";
                cursor = DatabaseHelper.rawQuery(sqlString);
                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                            String customername = cursor.getString(cursor.getColumnIndex("customer_name"));
                            long townshipid = cursor.getLong(cursor.getColumnIndex("Townshipid"));
                            String townshipname = cursor.getString(cursor.getColumnIndex("Townshipname"));
                            boolean credit = cursor.getInt(cursor.getColumnIndex("credit")) == 1 ? true : false;
                            if (create_customer) {
                                returnin_entry.imgCustomerTownship.setText(townshipname);
                                create_customer = false;
                            } else {
                                returnin_entry.btntownship.setText(townshipname);
                                returnin_entry.btncustomer.setText(customername);
                                returnin_entry.selected_townshipid = townshipid;
                                returnin_entry.creditcustomer = credit;
                                returnin_entry.isCreditcustomer = credit;
                                String pay_type = credit == false ? "Cash Down" : "Credit";
                                returnin_entry.btnpaytype.setText(pay_type);
                                returnin_entry.sh.get(0).setTownshipid(townshipid);
                                returnin_entry.sh.get(0).setCustomerid(customerid);
                                returnin_entry.sh.get(0).setPay_type(credit == false ? 1 : 2);
                            }


                        } while (cursor.moveToNext());

                    }

                }
                cursor = null;
                break;
            default:
                sqlString = "select customerid,customer_name,Townshipid,Townshipname,credit from customer where customerid=(select min(customerid)customerid " +
                        " from customer" +
                        " where CustGroupID=" + sale_entry_tv.selected_custgroupid +
                        " group by CustGroupID)";
                cursor = DatabaseHelper.rawQuery(sqlString);
                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                            String customername = cursor.getString(cursor.getColumnIndex("customer_name"));
                            long townshipid = cursor.getLong(cursor.getColumnIndex("Townshipid"));
                            String townshipname = cursor.getString(cursor.getColumnIndex("Townshipname"));
                            boolean credit = cursor.getInt(cursor.getColumnIndex("credit")) == 1 ? true : false;
                            if (create_customer) {
                                sale_entry_tv.imgCustomerTownship.setText(townshipname);
                                create_customer = false;
                            } else {
                                sale_entry_tv.btntownship.setText(townshipname);
                                sale_entry_tv.btncustomer.setText(customername);
                                sale_entry_tv.selected_townshipid = townshipid;
                                sale_entry_tv.creditcustomer = credit;
                                sale_entry_tv.isCreditcustomer = credit;
                                String pay_type = credit == false ? "Cash Down" : "Credit";
                                sale_entry_tv.btnpaytype.setText(pay_type);
                                sale_entry_tv.sh.get(0).setTownshipid(townshipid);
                                sale_entry_tv.sh.get(0).setCustomerid(customerid);
                                sale_entry_tv.sh.get(0).setPay_type(credit == false ? 1 : 2);
                            }


                        } while (cursor.moveToNext());

                    }

                }
                cursor = null;
                break;
        }
        /*if(formname =="SaleOrder")
        {
            String sqlString = "select customerid,customer_name,Townshipid,Townshipname,credit from customer where customerid=(select min(customerid)customerid " +
                    " from customer" +
                    " where CustGroupID=" + saleorder_entry.selected_custgroupid +
                    " group by CustGroupID)";
            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                        String customername = cursor.getString(cursor.getColumnIndex("customer_name"));
                        long townshipid = cursor.getLong(cursor.getColumnIndex("Townshipid"));
                        String townshipname = cursor.getString(cursor.getColumnIndex("Townshipname"));
                        boolean credit = cursor.getInt(cursor.getColumnIndex("credit")) == 1 ? true : false;
                        if(create_customer)
                        {
                            saleorder_entry.imgCustomerTownship.setText( townshipname);
                            create_customer=false;
                        }
                        else {
                            saleorder_entry.btntownship.setText( townshipname);
                            saleorder_entry.btncustomer.setText(customername);
                            saleorder_entry.selected_townshipid = townshipid;
                            saleorder_entry.creditcustomer = credit;
                            saleorder_entry.isCreditcustomer=credit;
                            String pay_type = credit == false ? "Cash Down" : "Credit";
                            saleorder_entry.btnpaytype.setText(pay_type);
                            saleorder_entry.sh.get(0).setTownshipid(townshipid);
                            saleorder_entry.sh.get(0).setCustomerid(customerid);
                            saleorder_entry.sh.get(0).setPay_type(credit == false ? 1 : 2);
                        }


                    } while (cursor.moveToNext());

                }

            }
            cursor = null;
        }
        else if(formname=="ReturnIn")
        {
            String sqlString = "select customerid,customer_name,Townshipid,Townshipname,credit from customer where customerid=(select min(customerid)customerid " +
                    " from customer" +
                    " where CustGroupID=" + returnin_entry.selected_custgroupid +
                    " group by CustGroupID)";
            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                        String customername = cursor.getString(cursor.getColumnIndex("customer_name"));
                        long townshipid = cursor.getLong(cursor.getColumnIndex("Townshipid"));
                        String townshipname = cursor.getString(cursor.getColumnIndex("Townshipname"));
                        boolean credit = cursor.getInt(cursor.getColumnIndex("credit")) == 1 ? true : false;
                        if(create_customer)
                        {
                            returnin_entry.imgCustomerTownship.setText( townshipname);
                            create_customer=false;
                        }
                        else {
                            returnin_entry.btntownship.setText( townshipname);
                            returnin_entry.btncustomer.setText(customername);
                            returnin_entry.selected_townshipid = townshipid;
                            returnin_entry.creditcustomer = credit;
                            returnin_entry.isCreditcustomer=credit;
                            String pay_type = credit == false ? "Cash Down" : "Credit";
                            returnin_entry.btnpaytype.setText(pay_type);
                            returnin_entry.sh.get(0).setTownshipid(townshipid);
                            returnin_entry.sh.get(0).setCustomerid(customerid);
                            returnin_entry.sh.get(0).setPay_type(credit == false ? 1 : 2);
                        }


                    } while (cursor.moveToNext());

                }

            }
            cursor = null;
        }
        else {
            String sqlString = "select customerid,customer_name,Townshipid,Townshipname,credit from customer where customerid=(select min(customerid)customerid " +
                    " from customer" +
                    " where CustGroupID=" + sale_entry.selected_custgroupid +
                    " group by CustGroupID)";
            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                        String customername = cursor.getString(cursor.getColumnIndex("customer_name"));
                        long townshipid = cursor.getLong(cursor.getColumnIndex("Townshipid"));
                        String townshipname = cursor.getString(cursor.getColumnIndex("Townshipname"));
                        boolean credit = cursor.getInt(cursor.getColumnIndex("credit")) == 1 ? true : false;
                        if(create_customer)
                        {
                            sale_entry.imgCustomerTownship.setText(townshipname);
                            create_customer=false;
                        }
                        else {
                            sale_entry.btntownship.setText(townshipname);
                            sale_entry.btncustomer.setText(customername);
                            sale_entry.selected_townshipid = townshipid;
                            sale_entry.creditcustomer = credit;
                            sale_entry.isCreditcustomer=credit;
                            String pay_type = credit == false ? "Cash Down" : "Credit";
                            sale_entry.btnpaytype.setText(pay_type);
                            sale_entry.sh.get(0).setTownshipid(townshipid);
                            sale_entry.sh.get(0).setCustomerid(customerid);
                            sale_entry.sh.get(0).setPay_type(credit == false ? 1 : 2);
                        }


                    } while (cursor.moveToNext());

                }

            }
            cursor = null;
        }*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.info_text);
        }
    }
}
