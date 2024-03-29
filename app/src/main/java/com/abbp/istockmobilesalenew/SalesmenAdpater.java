package com.abbp.istockmobilesalenew;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abbp.istockmobilesalenew.tvsale.sale_entry_tv;

import java.util.ArrayList;

public class SalesmenAdpater extends RecyclerView.Adapter<SalesmenAdpater.MultiViewHolder> {

    private Context context;
    private ArrayList<Salesmen> data;
    public static String formname;//added by YLT
    String contextString = "";

    public SalesmenAdpater(Context context, ArrayList<Salesmen> data) {
        this.context = context;
        this.data = data;
        formname = "Sale";
        contextString = context.getClass().toString().split("com.abbp.istockmobilesalenew.")[1];
        Log.i("Customer new", contextString);
        switch (contextString) {
            case "sale_entry":
                if (sale_entry.SaleVouSalesmen.size() > 0) {
                    for (int i = 0; i < sale_entry.SaleVouSalesmen.size(); i++) {
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getSalesmen_Id() == sale_entry.SaleVouSalesmen.get(i).getSalesmen_Id()) {
                                data.get(j).setChecked(true);
                            }
                        }
                    }
                    setSalesmen(data);
                }
                break;

            default:
                if (sale_entry_tv.SaleVouSalesmen.size() > 0) {
                    for (int i = 0; i < sale_entry_tv.SaleVouSalesmen.size(); i++) {
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getSalesmen_Id() == sale_entry_tv.SaleVouSalesmen.get(i).getSalesmen_Id()) {
                                data.get(j).setChecked(true);
                            }
                        }
                    }
                    setSalesmen(data);
                }
                break;
        }

    }

    public SalesmenAdpater(Context context, ArrayList<Salesmen> data, String frm) {
        this.context = context;
        this.data = data;
        formname = frm;
        contextString = context.getClass().toString().split("com.abbp.istockmobilesalenew.")[1];
        Log.i("Customer new", contextString);
        switch (contextString){

            case "saleorder_entry":
                if (saleorder_entry.SaleVouSalesmen.size() > 0) {
                    for (int i = 0; i < saleorder_entry.SaleVouSalesmen.size(); i++) {
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getSalesmen_Id() == saleorder_entry.SaleVouSalesmen.get(i).getSalesmen_Id()) {
                                data.get(j).setChecked(true);
                            }
                        }
                    }
                    setSalesmen(data);
                }
                break;
            case "returnin_entry":
                if (returnin_entry.SaleVouSalesmen.size() > 0) {
                    for (int i = 0; i < returnin_entry.SaleVouSalesmen.size(); i++) {
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getSalesmen_Id() == returnin_entry.SaleVouSalesmen.get(i).getSalesmen_Id()) {
                                data.get(j).setChecked(true);
                            }
                        }
                    }
                    setSalesmen(data);
                }
                break;

        }

    }

    public void setSalesmen(ArrayList<Salesmen> data) {
        this.data = new ArrayList<>();
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.salesmenitem, viewGroup, false);
        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private RelativeLayout rlCheck;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtCheck);
            imageView = itemView.findViewById(R.id.imgCheck);
            rlCheck = itemView.findViewById(R.id.rlCheck);
        }

        void bind(final Salesmen data) {
            imageView.setVisibility(data.isChecked() ? View.VISIBLE : View.GONE);
            textView.setText(data.getSalesmen_Name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.setChecked(!data.isChecked());
                    imageView.setVisibility(data.isChecked() ? View.VISIBLE : View.GONE);
                    getSelected();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setChecked(!data.isChecked());
                    imageView.setVisibility(data.isChecked() ? View.VISIBLE : View.GONE);
                    getSelected();
                }
            });
            rlCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setChecked(!data.isChecked());
                    imageView.setVisibility(data.isChecked() ? View.VISIBLE : View.GONE);
                    getSelected();
                }
            });
        }
    }

    public ArrayList<Salesmen> getAll() {
        return data;
    }

    public void getSelected() {
        switch (contextString) {
            case "sale_entry":
                if (sale_entry.SaleVouSalesmen.size() > 0)
                    sale_entry.SaleVouSalesmen.clear();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked()) {
                        sale_entry.SaleVouSalesmen.add(data.get(i));
                    }
                }
                break;
            case "saleorder_entry":
                if (saleorder_entry.SaleVouSalesmen.size() > 0)
                    saleorder_entry.SaleVouSalesmen.clear();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked()) {
                        saleorder_entry.SaleVouSalesmen.add(data.get(i));
                    }
                }
                break;
            case "returnin_entry":
                if (returnin_entry.SaleVouSalesmen.size() > 0)
                    returnin_entry.SaleVouSalesmen.clear();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked()) {
                        returnin_entry.SaleVouSalesmen.add(data.get(i));
                    }
                }
                break;
            default:
                if (sale_entry_tv.SaleVouSalesmen.size() > 0)
                    sale_entry_tv.SaleVouSalesmen.clear();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked()) {
                        sale_entry_tv.SaleVouSalesmen.add(data.get(i));
                    }
                }
                break;
        }
        /*if(formname =="SaleOrder")
        {
            if (saleorder_entry.SaleVouSalesmen.size() > 0)
                saleorder_entry.SaleVouSalesmen.clear();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isChecked()) {
                    saleorder_entry.SaleVouSalesmen.add(data.get(i));
                }
            }
        }
        else if(formname =="ReturnIn")
        {
            if (returnin_entry.SaleVouSalesmen.size() > 0)
                returnin_entry.SaleVouSalesmen.clear();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isChecked()) {
                    returnin_entry.SaleVouSalesmen.add(data.get(i));
                }
            }
        }
        else {
            if (sale_entry.SaleVouSalesmen.size() > 0)
                sale_entry.SaleVouSalesmen.clear();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isChecked()) {
                    sale_entry.SaleVouSalesmen.add(data.get(i));
                }
            }
        }*/
    }
}