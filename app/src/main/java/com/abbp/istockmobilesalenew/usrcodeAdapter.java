package com.abbp.istockmobilesalenew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class usrcodeAdapter extends RecyclerView.Adapter<usrcodeAdapter.MyViewHolder> {

    Context context;
    File directory;
    ArrayList<usr_code> data = new ArrayList<>();
    ArrayList<category> categories = new ArrayList<>();
    ArrayList<brand> brands = new ArrayList<>();
    String usr_code;
    RecyclerView rv;
    int Use_Tax = 0;
    public static Typeface font;
    public static String formname = "";//added by YLT

    static long specialPrice = 0;

    public usrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv, ArrayList<category> categories) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        this.categories = categories;
        formname = "Sale";
    }

    //added by YLT
    public usrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv, ArrayList<category> categories, String frm) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        this.categories = categories;
        formname = frm;//added by YLT
    }


    public usrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        formname = "Sale";
    }

    //added by YLT
    public usrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv, String frm) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        formname = frm;
    }


    @Override
    public usrcodeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v = null;
        if (frmmain.use_pic == 1) {
            v = lf.inflate(R.layout.itemwithpic, parent, false);
        } else {
            v = lf.inflate(R.layout.itembinding, parent, false);
        }
        return new usrcodeAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

       /* if(frmmain.use_pic==1){

           directory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String file=data.get(position).getPath();
            if(!file.equals("") && !file.equals("NULL")){
                file=data.get(position).getPath().substring(20);

                Uri ufile=Uri.fromFile(new File(directory,"/"+file));
                Picasso.with(context).load(ufile).placeholder(R.drawable.deficon).into(holder.iv);
                Picasso.with(context).load(new File(directory+"/"+file)).into(holder.iv);
                String path=directory+"/"+file;
                holder.iv.setImageDrawable(Drawable.createFromPath(path));

            }
            holder.tv.setText(" "+data.get(position).getDescription());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    specialPrice=GetPriceLevel();
                    String sale_price=specialPrice==0?"uc.sale_price":"uc.sale_price"+specialPrice;
                    String SP=specialPrice==0?"SP":"SP"+specialPrice;
                    String sqlString="select uc.unit_type,code,description,"+sale_price+",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                            " where uc.unit_type=1 and uc.usr_code='"+data.get(position).getUsr_code()+"'";
                    Cursor cursor=DatabaseHelper.rawQuery(sqlString);
                    if(cursor!=null&&cursor.getCount()!=0)
                    {
                        if(cursor.moveToFirst())
                        {
                            do {

                                long code= cursor.getLong(cursor.getColumnIndex("code"));
                                double price=cursor.getDouble(cursor.getColumnIndex(sale_price));
                                int open_price=cursor.getInt(cursor.getColumnIndex("open_price"));
                                double smallest_unit_qty=cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                int unit_type=cursor.getInt(cursor.getColumnIndex("unit_type"));
                                String unit_short=cursor.getString(cursor.getColumnIndex("unitshort")).equals("null")?"":cursor.getString(cursor.getColumnIndex("unitshort"));
                                String unitname=cursor.getString(cursor.getColumnIndex("unitname"));
                                String desc=cursor.getString(cursor.getColumnIndex("description"));
                                int CalNoTax=cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
                                sale_entry.sd.add(new sale_det(
                                        sale_entry.tranid,
                                        sale_entry.sd.size()+1,
                                        df.format(new Date()),
                                        1,
                                        open_price,
                                        smallest_unit_qty,
                                        unit_type,
                                        price,
                                        price,
                                        0,
                                        0,
                                        "",
                                        code,unit_short,desc,CalNoTax,SP));
                            }while (cursor.moveToNext());
                        }

                    }
                    cursor.close();
                    sale_entry.itemAdapter.notifyDataSetChanged();
                    sale_entry.entrygrid.setSelection(sale_entry.sd.size());
                    sale_entry.getSummary();
                }
            });
        }else {
       */
        if (frmmain.use_pic == 1) {
            holder.tv.setText(String.format(" %s", data.get(position).getDescription()));
            SharedPreferences sh_ip = context.getSharedPreferences("ip", MODE_PRIVATE);
            SharedPreferences sh_port = context.getSharedPreferences("port", MODE_PRIVATE);
            String ip = sh_ip.getString("ip", "empty");
            String port = sh_port.getString("port", "empty");
            String sql = "select path from usr_code_img where usr_code = '" + data.get(position).getUsr_code() + "'";
            Cursor cursor = DatabaseHelper.rawQuery(sql);
            String path = "";
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        path = cursor.getString(cursor.getColumnIndex("path"));
                    } while (cursor.moveToNext());
                }
            }
            if (data.get(position).getUsr_code() == "Back") {
                holder.cv.setCardBackgroundColor(R.drawable.categorygradiant);
                holder.iv.setVisibility(View.GONE);
                holder.tv.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.cv.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.iv.setVisibility(View.VISIBLE);
                holder.tv.setTextColor(Color.parseColor("#000000"));
                String[] pathnew = path.split("\\\\");
                String imgUrl = "http://" + ip + "/api/DataSync/GetImage?imgName=" + (pathnew.length > 3 ? pathnew[3] : path);
                Log.i("imgUrl", imgUrl);
                Picasso.get().load(imgUrl)
                        .placeholder(R.drawable.ic_image_search)
                        .error(R.drawable.ic_image_search)
                        .into(holder.iv);
            }
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Item_Click(position);
                }
            });
            holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ItemDetail_Dialog(position);
                    return false;
                }
            });

        } else {
            holder.btn.setText(String.format(" %s", data.get(position).getDescription()));
            holder.btn.setBackgroundResource(R.drawable.usercodegradiant);
            if (frmmain.withoutclass.equals("true")) {
                if (data.get(position).getUsr_code() == "Back") {
                    holder.btn.setBackgroundResource(R.drawable.categorygradiant);
                } else {
                    holder.btn.setBackgroundResource(R.drawable.usercodegradiant);
                }
            }
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item_Click(position);
                }
            });

            holder.btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ItemDetail_Dialog(position);
                    return false;
                }
            });
        }
    }

    private void Item_Click(int position) {
        try {
            if (data.get(position).getUsr_code() == "Back") {
                if (formname == "SaleOrder")//added by YLT
                {
                    saleorder_entry.imgFilterCode.setVisibility(View.GONE);
                    saleorder_entry.fitercode = "Category";
                    if (categories.size() > 0) {
                        categories.clear();
                    }
                    if (frmmain.withoutclass.equals("false")) {
                        categories.add(new category("Back"));
                    }
                    String sqlstr = "";
                    if (frmmain.inClass.length() > 1) {
                        sqlstr = "select distinct category,categoryname,class from Usr_Code where class in (" + frmmain.inClass + ") order by classname";
                    } else {
                        sqlstr = "select distinct category,categoryname,class from Usr_Code order by classname";
                    }

                    Cursor cursor = DatabaseHelper.rawQuery(sqlstr);
                    if (cursor != null && cursor.getCount() != 0) {
                        if (cursor.moveToFirst()) {
                            do {
                                long categoryid = cursor.getLong(cursor.getColumnIndex("category"));
                                String name = cursor.getString(cursor.getColumnIndex("categoryname"));
                                long classid = cursor.getLong(cursor.getColumnIndex("class"));
                                categories.add(new category(categoryid, classid, name));
                            } while (cursor.moveToNext());

                        }

                    }
                    cursor.close();

                    categoryAdapter ad = new categoryAdapter(context, categories, rv, "SaleOrder");
                    rv.setAdapter(ad);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), 4);
                    rv.setLayoutManager(gridLayoutManager);
                } else if (formname == "ReturnIn") {
                    returnin_entry.imgFilterCode.setVisibility(View.GONE);
                    returnin_entry.fitercode = "Category";
                    if (categories.size() > 0) {
                        categories.clear();
                    }
                    if (frmmain.withoutclass.equals("false")) {
                        categories.add(new category("Back"));
                    }
                    Cursor cursor = DatabaseHelper.DistinctCategorySelectQuery("Usr_Code", new String[]{"category", "categoryname", "class"}, "sortcode,categoryname");
                    // Cursor cursor = DatabaseHelper.DistinctSelectQuery("Usr_Code",new String[]{"category","categoryname","class"});
                    if (cursor != null && cursor.getCount() != 0) {
                        if (cursor.moveToFirst()) {
                            do {
                                long categoryid = cursor.getLong(cursor.getColumnIndex("category"));
                                String name = cursor.getString(cursor.getColumnIndex("categoryname"));
                                long classid = cursor.getLong(cursor.getColumnIndex("class"));
                                categories.add(new category(categoryid, classid, name));
                            } while (cursor.moveToNext());

                        }

                    }
                    cursor.close();

                    categoryAdapter ad = new categoryAdapter(context, categories, rv, "ReturnIn");
                    rv.setAdapter(ad);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), 4);
                    rv.setLayoutManager(gridLayoutManager);
                } else {

                    sale_entry.imgFilterCode.setVisibility(View.GONE);
                    sale_entry.fitercode = "Category";
                    if (categories.size() > 0) {
                        categories.clear();
                    }
                    if (frmmain.withoutclass.equals("false")) {
                        categories.add(new category("Back"));
                    }
                    Cursor cursor = DatabaseHelper.DistinctCategorySelectQuery("Usr_Code", new String[]{"category", "categoryname", "class"}, "sortcode,categoryname");
                    // Cursor cursor = DatabaseHelper.DistinctSelectQuery("Usr_Code",new String[]{"category","categoryname","class"});
                    if (cursor != null && cursor.getCount() != 0) {
                        if (cursor.moveToFirst()) {
                            do {
                                long categoryid = cursor.getLong(cursor.getColumnIndex("category"));
                                String name = cursor.getString(cursor.getColumnIndex("categoryname"));
                                long classid = cursor.getLong(cursor.getColumnIndex("class"));
                                categories.add(new category(categoryid, classid, name));
                            } while (cursor.moveToNext());

                        }

                    }
                    cursor.close();

                    categoryAdapter ad = new categoryAdapter(context, categories, rv);
                    rv.setAdapter(ad);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), 4);
                    rv.setLayoutManager(gridLayoutManager);
                }

            } else {

                if (formname == "SaleOrder")//added by YLT
                {
                    int defunit = 1;
                    defunit = frmmain.defunit;
                    int[] ut = new int[3];
                    int i = 0;
                    Cursor cc = DatabaseHelper.rawQuery("select unit_type from Usr_Code where usr_code='" + data.get(position).getUsr_code() + "'");
                    if (cc != null && cc.getCount() != 0) {
                        if (cc.moveToFirst()) {
                            do {
                                int utt = cc.getInt(cc.getColumnIndex("unit_type"));
                                ut[i] = utt;
                                i++;
                            } while (cc.moveToNext());
                        }
                    }
                    if (i > 1) {

                        if (i == 3) {
                            specialPrice = GetPriceLevel();
                            String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                            String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                            String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                    " where uc.unit_type=" + defunit + " and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                            if (cursor != null && cursor.getCount() != 0) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                                        double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                        //added by KLM to correct dis_price while use multicurrency 08072022
                                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                        price = GetCurrencyPrice(price, sale_curr);

                                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        saleorder_entry.sd.add(new sale_det(
                                                saleorder_entry.tranid,
                                                saleorder_entry.sd.size() + 1,
                                                df.format(new Date()),
                                                1,
                                                open_price,
                                                smallest_unit_qty,
                                                unit_type,
                                                price,
                                                price,
                                                0,
                                                0,
                                                "",
                                                code, unit_short, desc, CalNoTax, SP));
                                        saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setChangeSalePrice(true);
                                        saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                    } while (cursor.moveToNext());
                                }

                            }
                            cursor.close();
                            saleorder_entry.itemAdapter.notifyDataSetChanged();
                            saleorder_entry.entrygrid.setSelection(saleorder_entry.sd.size());
                            saleorder_entry.getSummary();
                        } else {
                            int utt = 2;
                            if (defunit == 2 || defunit == 3) {
                                utt = 2;
                            } else if (defunit == 1) {
                                utt = 1;
                            }
                            specialPrice = GetPriceLevel();
                            String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                            String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                            String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                    " where uc.unit_type=" + utt + " and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                            if (cursor != null && cursor.getCount() != 0) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                                        double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                        //added by KLM to correct dis_price while use multicurrency 08072022
                                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                        price = GetCurrencyPrice(price, sale_curr);

                                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        saleorder_entry.sd.add(new sale_det(
                                                saleorder_entry.tranid,
                                                saleorder_entry.sd.size() + 1,
                                                df.format(new Date()),
                                                1,
                                                open_price,
                                                smallest_unit_qty,
                                                unit_type,
                                                price,
                                                price,
                                                0,
                                                0,
                                                "",
                                                code, unit_short, desc, CalNoTax, SP));
                                        saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setChangeSalePrice(true);
                                        saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                    } while (cursor.moveToNext());
                                }

                            }
                            cursor.close();
                            saleorder_entry.itemAdapter.notifyDataSetChanged();
                            saleorder_entry.entrygrid.setSelection(saleorder_entry.sd.size());
                            saleorder_entry.getSummary();
                        }

                    } else if (i == 1 || defunit == 1) {

                        specialPrice = GetPriceLevel();
                        String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                        String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                        String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                " where uc.unit_type=1 and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                        if (cursor != null && cursor.getCount() != 0) {
                            if (cursor.moveToFirst()) {
                                do {

                                    long code = cursor.getLong(cursor.getColumnIndex("code"));
                                    double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                    //added by KLM to correct dis_price while use multicurrency 08072022
                                    int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                    price = GetCurrencyPrice(price, sale_curr);

                                    int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                    double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                    int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                    String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                    String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                    String desc = cursor.getString(cursor.getColumnIndex("description"));
                                    int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    saleorder_entry.sd.add(new sale_det(
                                            saleorder_entry.tranid,
                                            saleorder_entry.sd.size() + 1,
                                            df.format(new Date()),
                                            1,
                                            open_price,
                                            smallest_unit_qty,
                                            unit_type,
                                            price,
                                            price,
                                            0,
                                            0,
                                            "",
                                            code, unit_short, desc, CalNoTax, SP));
                                    saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setChangeSalePrice(true);
                                    saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                } while (cursor.moveToNext());
                            }

                        }
                        cursor.close();
                        saleorder_entry.itemAdapter.notifyDataSetChanged();
                        saleorder_entry.entrygrid.setSelection(saleorder_entry.sd.size());
                        saleorder_entry.getSummary();
                    }
                } else if (formname == "ReturnIn") {
                    int defunit = 1;
                    defunit = frmmain.defunit;
                    int[] ut = new int[3];
                    int i = 0;
                    Cursor cc = DatabaseHelper.rawQuery("select unit_type from Usr_Code where usr_code='" + data.get(position).getUsr_code() + "'");
                    if (cc != null && cc.getCount() != 0) {
                        if (cc.moveToFirst()) {
                            do {
                                int utt = cc.getInt(cc.getColumnIndex("unit_type"));
                                ut[i] = utt;
                                i++;
                            } while (cc.moveToNext());
                        }
                    }
                    if (i > 1) {

                        if (i == 3) {
                            specialPrice = GetPriceLevel();
                            String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                            String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                            String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                    " where uc.unit_type=" + defunit + " and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                            if (cursor != null && cursor.getCount() != 0) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                                        double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                        //added by KLM to correct dis_price while use multicurrency 08072022
                                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                        price = GetCurrencyPrice(price, sale_curr);

                                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        returnin_entry.sd.add(new sale_det(
                                                returnin_entry.tranid,
                                                returnin_entry.sd.size() + 1,
                                                df.format(new Date()),
                                                1,
                                                open_price,
                                                smallest_unit_qty,
                                                unit_type,
                                                price,
                                                price,
                                                0,
                                                0,
                                                "",
                                                code, unit_short, desc, CalNoTax, SP));
                                        returnin_entry.sd.get(returnin_entry.sd.size() - 1).setChangeSalePrice(true);
                                        returnin_entry.sd.get(returnin_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                    } while (cursor.moveToNext());
                                }

                            }
                            cursor.close();
                            returnin_entry.itemAdapter.notifyDataSetChanged();
                            returnin_entry.entrygrid.setSelection(returnin_entry.sd.size());
                            returnin_entry.getSummary();
                        } else {
                            int utt = 2;
                            if (defunit == 2 || defunit == 3) {
                                utt = 2;
                            } else if (defunit == 1) {
                                utt = 1;
                            }
                            specialPrice = GetPriceLevel();
                            String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                            String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                            String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                    " where uc.unit_type=" + utt + " and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                            if (cursor != null && cursor.getCount() != 0) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                                        double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                        //added by KLM to correct dis_price while use multicurrency 08072022
                                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                        price = GetCurrencyPrice(price, sale_curr);

                                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        returnin_entry.sd.add(new sale_det(
                                                returnin_entry.tranid,
                                                returnin_entry.sd.size() + 1,
                                                df.format(new Date()),
                                                1,
                                                open_price,
                                                smallest_unit_qty,
                                                unit_type,
                                                price,
                                                price,
                                                0,
                                                0,
                                                "",
                                                code, unit_short, desc, CalNoTax, SP));
                                        returnin_entry.sd.get(returnin_entry.sd.size() - 1).setChangeSalePrice(true);
                                        returnin_entry.sd.get(returnin_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                    } while (cursor.moveToNext());
                                }

                            }
                            cursor.close();
                            returnin_entry.itemAdapter.notifyDataSetChanged();
                            returnin_entry.entrygrid.setSelection(saleorder_entry.sd.size());
                            returnin_entry.getSummary();
                        }

                    } else if (i == 1 || defunit == 1) {

                        specialPrice = GetPriceLevel();
                        String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                        String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                        String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                " where uc.unit_type=1 and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                        if (cursor != null && cursor.getCount() != 0) {
                            if (cursor.moveToFirst()) {
                                do {

                                    long code = cursor.getLong(cursor.getColumnIndex("code"));
                                    double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                    //added by KLM to correct dis_price while use multicurrency 08072022
                                    int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                    price = GetCurrencyPrice(price, sale_curr);

                                    int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                    double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                    int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                    String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                    String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                    String desc = cursor.getString(cursor.getColumnIndex("description"));
                                    int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    returnin_entry.sd.add(new sale_det(
                                            returnin_entry.tranid,
                                            returnin_entry.sd.size() + 1,
                                            df.format(new Date()),
                                            1,
                                            open_price,
                                            smallest_unit_qty,
                                            unit_type,
                                            price,
                                            price,
                                            0,
                                            0,
                                            "",
                                            code, unit_short, desc, CalNoTax, SP));
                                    returnin_entry.sd.get(returnin_entry.sd.size() - 1).setChangeSalePrice(true);
                                    returnin_entry.sd.get(returnin_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                } while (cursor.moveToNext());
                            }

                        }
                        cursor.close();
                        returnin_entry.itemAdapter.notifyDataSetChanged();
                        returnin_entry.entrygrid.setSelection(saleorder_entry.sd.size());
                        returnin_entry.getSummary();
                    }
                } else {

                    int defunit = 1;
                    defunit = frmmain.defunit;
                    int[] ut = new int[3];
                    int i = 0;
                    Cursor cc = DatabaseHelper.rawQuery("select unit_type from Usr_Code where usr_code='" + data.get(position).getUsr_code() + "'");
                    if (cc != null && cc.getCount() != 0) {
                        if (cc.moveToFirst()) {
                            do {
                                int utt = cc.getInt(cc.getColumnIndex("unit_type"));
                                ut[i] = utt;
                                i++;
                            } while (cc.moveToNext());
                        }
                    }
                    if (i > 1) {

                        if (i == 3) {
                            specialPrice = GetPriceLevel();
                            String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                            String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                            String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                    " where uc.unit_type=" + defunit + " and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                            if (cursor != null && cursor.getCount() != 0) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                                        double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                        //added by KLM to correct dis_price while use multicurrency 08072022
                                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                        price = GetCurrencyPrice(price, sale_curr);

                                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        sale_entry.sd.add(new sale_det(
                                                sale_entry.tranid,
                                                sale_entry.sd.size() + 1,
                                                df.format(new Date()),
                                                1,
                                                open_price,
                                                smallest_unit_qty,
                                                unit_type,
                                                price,
                                                price,
                                                0,
                                                0,
                                                "",
                                                code, unit_short, desc, CalNoTax, SP, smallest_unit_qty));
                                        sale_entry.sd.get(sale_entry.sd.size() - 1).setChangeSalePrice(true);
                                        sale_entry.sd.get(sale_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                    } while (cursor.moveToNext());
                                }

                            }
                            cursor.close();
                            sale_entry.itemAdapter.notifyDataSetChanged();
                            sale_entry.entrygrid.setSelection(sale_entry.sd.size());
                            sale_entry.getSummary();
                        } else {
                            int utt = 2;
                            if (defunit == 2 || defunit == 3) {
                                utt = 2;
                            } else if (defunit == 1) {
                                utt = 1;
                            }
                            specialPrice = GetPriceLevel();
                            String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                            String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                            String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                    " where uc.unit_type=" + utt + " and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                            if (cursor != null && cursor.getCount() != 0) {
                                if (cursor.moveToFirst()) {
                                    do {

                                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                                        double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                        //added by KLM to correct dis_price while use multicurrency 08072022
                                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                        price = GetCurrencyPrice(price, sale_curr);

                                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        sale_entry.sd.add(new sale_det(
                                                sale_entry.tranid,
                                                sale_entry.sd.size() + 1,
                                                df.format(new Date()),
                                                1,
                                                open_price,
                                                smallest_unit_qty,
                                                unit_type,
                                                price,
                                                price,
                                                0,
                                                0,
                                                "",
                                                code, unit_short, desc, CalNoTax, SP, smallest_unit_qty));
                                        sale_entry.sd.get(sale_entry.sd.size() - 1).setChangeSalePrice(true);
                                        sale_entry.sd.get(sale_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                    } while (cursor.moveToNext());
                                }

                            }
                            cursor.close();
                            sale_entry.itemAdapter.notifyDataSetChanged();
                            sale_entry.entrygrid.setSelection(sale_entry.sd.size());
                            sale_entry.getSummary();
                        }

                    } else if (i == 1 || defunit == 1) {

                        specialPrice = GetPriceLevel();
                        String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                        String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                        String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                                " where uc.unit_type=1 and uc.usr_code='" + data.get(position).getUsr_code() + "'";
                        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                        if (cursor != null && cursor.getCount() != 0) {
                            if (cursor.moveToFirst()) {
                                do {

                                    long code = cursor.getLong(cursor.getColumnIndex("code"));
                                    double price = cursor.getDouble(cursor.getColumnIndex(sale_price));
                                    //added by KLM to correct dis_price while use multicurrency 08072022
                                    int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                                    price = GetCurrencyPrice(price, sale_curr);

                                    int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                                    double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                    int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                                    String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                                    String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                    String desc = cursor.getString(cursor.getColumnIndex("description"));
                                    int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    sale_entry.sd.add(new sale_det(
                                            sale_entry.tranid,
                                            sale_entry.sd.size() + 1,
                                            df.format(new Date()),
                                            1,
                                            open_price,
                                            smallest_unit_qty,
                                            unit_type,
                                            price,
                                            price,
                                            0,
                                            0,
                                            "",
                                            code, unit_short, desc, CalNoTax, SP, smallest_unit_qty));
                                    sale_entry.sd.get(sale_entry.sd.size() - 1).setChangeSalePrice(true);
                                    sale_entry.sd.get(sale_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                                } while (cursor.moveToNext());
                            }

                        }
                        cursor.close();
                        sale_entry.itemAdapter.notifyDataSetChanged();
                        sale_entry.entrygrid.setSelection(sale_entry.sd.size());
                        sale_entry.getSummary();
                    }


                }
                if (formname == "Sale") {

                    GetPdis();
                }
            }
        } catch (Exception ee) {
            System.out.println(ee + "this is exception");

        }
    }

    public static double GetCurrencyPrice(double price, int sale_curr) {
        double codeOrgExgRate = 0;
        if (frmlogin.def_currency != sale_curr) {
            codeOrgExgRate = itemAdapter.GetExgRate(sale_curr);
            price = price * codeOrgExgRate / frmmain.exg_rate;
        }
        return price;
    }

    private void ItemDetail_Dialog(int position) {
        if (data.get(position).getUsr_code() != "Back") {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.itemdetail, null);
            builder.setView(view);
            SharedPreferences sh_ip = context.getSharedPreferences("ip", MODE_PRIVATE);
            SharedPreferences sh_port = context.getSharedPreferences("port", MODE_PRIVATE);
            TextView itemname = view.findViewById(R.id.itemname);
            TextView txtdesc = view.findViewById(R.id.txtdesc);
            TextView txtsprice = view.findViewById(R.id.txtsprice);
            ImageView imageView = view.findViewById(R.id.imgview);
            itemname.setTypeface(font);
            itemname.setText(data.get(position).getUsr_code());
            //txtsprice.setText(String.format("%s", data.get(position).getSale_price()));
            String numberAsString = String.format("%,." + frmmain.price_places + "f", data.get(position).getSale_price());
            txtsprice.setText(numberAsString);
            txtdesc.setText(data.get(position).getDescription());
            String ip = sh_ip.getString("ip", "empty");
            String port = sh_port.getString("port", "empty");
            String sql = "select path from usr_code_img where usr_code = '" + data.get(position).getUsr_code() + "'";
            Cursor cursor = DatabaseHelper.rawQuery(sql);
            String path = "";
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        path = cursor.getString(cursor.getColumnIndex("path"));
                    } while (cursor.moveToNext());
                }
            }
            String[] pathnew = path.split("\\\\");
            Picasso.get().load("http://" + ip + "/api/DataSync/GetImage?imgName=" + (pathnew.length > 3 ? pathnew[3] : path))
                    .placeholder(R.drawable.ic_image_search)
                    .error(R.drawable.ic_image_search)
                    .into(imageView);
            final Dialog dialog = builder.create();
            dialog.show();

        }

    }

    private void GetPdis() {
        long code = sale_entry.sd.get((sale_entry.sd.size() - 1)).getCode();
        long locationid = sale_entry.sh.get(0).getLocationid();
        int unit_type = sale_entry.sd.get((sale_entry.sd.size() - 1)).getUnit_type();
        long level = GetPriceLevel();
        double discount = 0;
        double dis_price = 0;
        String sql = "select discount" + (level == 0 ? "" : String.valueOf(level)) + ",dis_price" + (level == 0 ? "" : String.valueOf(level)) + " from Pdis where code=" + code + " and unit_type=" + unit_type +
                " and locationid=" + locationid;
        Cursor cursor = DatabaseHelper.rawQuery(sql);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    discount = cursor.getDouble(cursor.getColumnIndex("discount" + (level == 0 ? "" : String.valueOf(level))));
                    dis_price = cursor.getDouble(cursor.getColumnIndex("dis_price" + (level == 0 ? "" : String.valueOf(level))));

                } while (cursor.moveToNext());

            }

        }
        cursor.close();

        if (discount > 0) {
            long dis_type = 5;
            sale_entry.sd.get(sale_entry.sd.size() - 1).setDis_type(dis_type);
            sale_entry.sd.get(sale_entry.sd.size() - 1).setDis_percent(discount);
            double disprice = sale_entry.sd.get(sale_entry.sd.size() - 1).getSale_price() - (sale_entry.sd.get(sale_entry.sd.size() - 1).getSale_price() * (discount / 100));
            sale_entry.sd.get(sale_entry.sd.size() - 1).setDis_price(disprice);
            sale_entry.itemAdapter.notifyDataSetChanged();
            sale_entry.entrygrid.setSelection(sale_entry.sd.size());
            sale_entry.getSummary();


        } else if (dis_price > 0) {
            sale_entry.sd.get(sale_entry.sd.size() - 1).setDis_type(5);
//            double disprice = sale_entry.sd.get(sale_entry.sd.size() - 1).getSale_price() - dis_price;
            sale_entry.sd.get(sale_entry.sd.size() - 1).setDis_price(dis_price);
            sale_entry.itemAdapter.notifyDataSetChanged();
            sale_entry.entrygrid.setSelection(sale_entry.sd.size());
            sale_entry.getSummary();
        }


    }

    //Added by abbp barcode scanner on 19/6/2019
    public static void scanner(String usr_code) {
        adddata(usr_code);
    }

    private static void adddata(String usr_code) {

        specialPrice = GetPriceLevel();
        String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
        String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
        String sqlString = "select uc.sale_curr,uc.unit_type,code,description," + sale_price + ",open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                " where uc.unit_type=1 and uc.usr_code='" + usr_code + "'";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);

        if (formname == "SaleOrder")//added by YLT
        {
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {

                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                        double price = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                        //added by KLM to correct dis_price while use multicurrency 08072022
                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                        price = GetCurrencyPrice(price, sale_curr);

                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        saleorder_entry.sd.add(new sale_det(
                                saleorder_entry.tranid,
                                saleorder_entry.sd.size() + 1,
                                df.format(new Date()),
                                1,
                                open_price,
                                smallest_unit_qty,
                                unit_type,
                                price,
                                price,
                                0,
                                0,
                                "",
                                code, unit_short, desc, CalNoTax, SP));
                        saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setChangeSalePrice(true);
                        saleorder_entry.sd.get(saleorder_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                    } while (cursor.moveToNext());
                }

            }
            cursor.close();
            saleorder_entry.itemAdapter.notifyDataSetChanged();
            saleorder_entry.entrygrid.setSelection(saleorder_entry.sd.size());
            saleorder_entry.getSummary();
        } else if (formname == "ReturnIn") {
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {

                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                        double price = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                        //added by KLM to correct dis_price while use multicurrency 08072022
                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                        price = GetCurrencyPrice(price, sale_curr);

                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        returnin_entry.sd.add(new sale_det(
                                returnin_entry.tranid,
                                returnin_entry.sd.size() + 1,
                                df.format(new Date()),
                                1,
                                open_price,
                                smallest_unit_qty,
                                unit_type,
                                price,
                                price,
                                0,
                                0,
                                "",
                                code, unit_short, desc, CalNoTax, SP));
                        returnin_entry.sd.get(returnin_entry.sd.size() - 1).setChangeSalePrice(true);
                        returnin_entry.sd.get(returnin_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                    } while (cursor.moveToNext());
                }

            }
            cursor.close();
            returnin_entry.itemAdapter.notifyDataSetChanged();
            returnin_entry.entrygrid.setSelection(saleorder_entry.sd.size());
            returnin_entry.getSummary();
        } else {
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {

                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                        double price = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                        //added by KLM to correct dis_price while use multicurrency 08072022
                        int sale_curr = cursor.getInt(cursor.getColumnIndex("sale_curr"));
                        price = GetCurrencyPrice(price, sale_curr);

                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        sale_entry.sd.add(new sale_det(
                                sale_entry.tranid,
                                sale_entry.sd.size() + 1,
                                df.format(new Date()),
                                1,
                                open_price,
                                smallest_unit_qty,
                                unit_type,
                                price,
                                price,
                                0,
                                0,
                                "",
                                code, unit_short, desc, CalNoTax, SP));
                        sale_entry.sd.get(sale_entry.sd.size() - 1).setChangeSalePrice(true);
                        sale_entry.sd.get(sale_entry.sd.size() - 1).setOrgSalePrice(price);//added by KLM for org_currency to calculate currency in itemAdapter 27062022
                    } while (cursor.moveToNext());
                }

            }
            cursor.close();
            sale_entry.itemAdapter.notifyDataSetChanged();
            sale_entry.entrygrid.setSelection(sale_entry.sd.size());
            sale_entry.getSummary();
        }
    }

    public static long GetPriceLevel() {
        long level = 0;
        boolean useUserpricelevel = false;
        boolean useCustpricelevel = false;
        boolean useSpecialPrice = false;
        Cursor cursor = DatabaseHelper.rawQuery("select use_user_pricelevel,use_cust_pricelevel,use_specialprice from SystemSetting");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    useUserpricelevel = cursor.getInt(cursor.getColumnIndex("use_user_pricelevel")) == 1 ? true : false;
                    useCustpricelevel = cursor.getInt(cursor.getColumnIndex("use_cust_pricelevel")) == 1 ? true : false;
                    useSpecialPrice = cursor.getInt(cursor.getColumnIndex("use_specialprice")) == 1 ? true : false;
                } while (cursor.moveToNext());

            }

        }
        cursor.close();

        if (useCustpricelevel) {
            if (formname == "SaleOrder") {
                String sSql = "select pricelevel from customer where customerid =" + saleorder_entry.sh.get(0).getCustomerid();//added by YLT
                cursor = DatabaseHelper.rawQuery(sSql);
            } else if (formname == "ReturnIn") {
                String sSql = "select pricelevel from customer where customerid =" + returnin_entry.sh.get(0).getCustomerid();//added by YLT
                cursor = DatabaseHelper.rawQuery(sSql);
            } else {
                String sSql = "select pricelevel from customer where customerid =" + sale_entry.sh.get(0).getCustomerid();
                cursor = DatabaseHelper.rawQuery(sSql);
            }

            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        level = cursor.getInt(cursor.getColumnIndex("pricelevel"));
                    } while (cursor.moveToNext());


                }

            }
            cursor.close();

        } else if (useUserpricelevel) {
            String sSql = "select saleprice_level from posuser where userid=" + frmlogin.LoginUserid;
            cursor = DatabaseHelper.rawQuery(sSql);
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        level = cursor.getInt(cursor.getColumnIndex("saleprice_level"));
                    } while (cursor.moveToNext());


                }

            }
            cursor.close();
        } else {
            level = 0;
        }


        return level;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        CardView cv;
        ImageView iv;
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);

            if (frmmain.use_pic == 1) {
                cv = itemView.findViewById(R.id.cardsale);
                iv = itemView.findViewById(R.id.itempic);
                tv = itemView.findViewById(R.id.info_text);
            } else {
                btn = itemView.findViewById(R.id.info_text);
            }

        }
    }

}
