package com.abbp.istockmobilesalenew;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CodeFindListViewAdapter extends ArrayAdapter<usr_code> {
    Context context;

    List<usr_code> data;



    public CodeFindListViewAdapter(Context context, int resource, List<usr_code> objects) {
        super(context, resource, objects);
        this.context=context;
        this.data=objects;
    }



    /*private view holder class*/
    private class ViewHolder {
        TextView code;
        TextView description;
        TextView price;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        usr_code item = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.codefinditemlist, null);
        holder = new ViewHolder();
        holder.code = (TextView) convertView.findViewById(R.id.txtScode);
        holder.description = (TextView) convertView.findViewById(R.id.txtSdes);
        holder.price = (TextView) convertView.findViewById(R.id.txtSprice);

        holder.code.setText(item.getUsr_code());
        holder.description.setText(item.getDescription());
        SummaryFormat(holder.price,item.getSale_price());
        return convertView;
    }
    private static void SummaryFormat(TextView source,double value) {
        String numberAsString = String.format("%,."+frmmain.price_places+"f", value);
        if(source!=null){
            source.setText(numberAsString);
        }
    }


}
