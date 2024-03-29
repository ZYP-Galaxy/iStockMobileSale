package com.abbp.istockmobilesalenew;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.abbp.istockmobilesalenew.tvsale.sale_entry_tv;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    Context context;
    ArrayList<Location> data = new ArrayList<>();
    Button btn;
    androidx.appcompat.app.AlertDialog da;
    String contextString = "";
    public static String formname;//added by YLT

    public LocationAdapter(Context context, ArrayList<Location> data, Button btn, androidx.appcompat.app.AlertDialog da) {
        this.data = data;
        this.context = context;
        this.btn = btn;
        this.da = da;
        formname = "Sale";
        contextString = context.getClass().toString().split("com.abbp.istockmobilesalenew.")[1];
        Log.i("Customer new", contextString);
    }

    public LocationAdapter(Context context, ArrayList<Location> data, Button btn, androidx.appcompat.app.AlertDialog da, String frm) {
        this.data = data;
        this.context = context;
        this.btn = btn;
        this.da = da;
        formname = frm;
        contextString = context.getClass().toString().split("com.abbp.istockmobilesalenew.")[1];
        Log.i("Customer new", contextString);
    }

    @Override
    public LocationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v = lf.inflate(R.layout.headerbinding, parent, false);
        return new LocationAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LocationAdapter.MyViewHolder holder, int position) {
        holder.btn.setText(" " + data.get(position).getName());
        holder.btn.setBackgroundResource(R.drawable.usercodegradiant);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText(data.get(position).getName());
                switch (contextString) {
                    case "sale_entry":
                        sale_entry.sh.get(0).setLocationid(data.get(position).getLocationid());
                        da.dismiss();
                        break;
                    case "saleorder_entry":
                        saleorder_entry.sh.get(0).setLocationid(data.get(position).getLocationid());
                        da.dismiss();
                        break;
                    case "returnin_entry":
                        returnin_entry.sh.get(0).setLocationid(data.get(position).getLocationid());
                        da.dismiss();
                        break;
                    default:
                        sale_entry_tv.sh.get(0).setLocationid(data.get(position).getLocationid());
                        da.dismiss();
                        break;
                }
               /*if(formname=="SaleOrder")
               {
                   saleorder_entry.sh.get(0).setLocationid(data.get(position).getLocationid());
                   da.dismiss();
               }
               else if(formname=="ReturnIn")
                {
                    returnin_entry.sh.get(0).setLocationid(data.get(position).getLocationid());
                    da.dismiss();
                }
               else {
                   sale_entry.sh.get(0).setLocationid(data.get(position).getLocationid());
                   da.dismiss();
               }*/

            }
        });
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
