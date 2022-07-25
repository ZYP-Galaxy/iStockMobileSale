package com.abbp.istockmobilesalenew.tvsale;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abbp.istockmobilesalenew.DatabaseHelper;
import com.abbp.istockmobilesalenew.R;
import com.abbp.istockmobilesalenew.brand;
import com.abbp.istockmobilesalenew.category;
import com.abbp.istockmobilesalenew.frmlogin;
import com.abbp.istockmobilesalenew.frmmain;
import com.abbp.istockmobilesalenew.sale_det;
import com.abbp.istockmobilesalenew.sale_entry_tv;
import com.abbp.istockmobilesalenew.saleorder_entry;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsrcodeAdapter extends RecyclerView.Adapter<UsrcodeAdapter.MyViewHolder> {

    Context context;
    File directory;
    ArrayList<com.abbp.istockmobilesalenew.usr_code> data = new ArrayList<>();
    ArrayList<category> categories = new ArrayList<>();
    ArrayList<brand> brands = new ArrayList<>();
    String usr_code;
    RecyclerView rv;
    int Use_Tax = 0;
    public static String formname = "";//added by YLT

    static long specialPrice = 0;

    public UsrcodeAdapter(Context context, ArrayList<com.abbp.istockmobilesalenew.usr_code> data, RecyclerView rv, ArrayList<category> categories) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        this.categories = categories;
        formname = "Sale";
    }

    //added by YLT
    public UsrcodeAdapter(Context context, ArrayList<com.abbp.istockmobilesalenew.usr_code> data, RecyclerView rv, ArrayList<category> categories, String frm) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        this.categories = categories;
        formname = frm;//added by YLT
    }


    public UsrcodeAdapter(Context context, ArrayList<com.abbp.istockmobilesalenew.usr_code> data, RecyclerView rv) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        formname = "Sale";
    }

    //added by YLT
    public UsrcodeAdapter(Context context, ArrayList<com.abbp.istockmobilesalenew.usr_code> data, RecyclerView rv, String frm) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        formname = frm;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v = null;
        if (frmmain.use_pic == 1) {
            v = lf.inflate(R.layout.itemwithpic, parent, false);
        } else {
            v = lf.inflate(R.layout.tv_item_usrcode, parent, false);
        }
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

       

        holder.txtName.setText(String.format(" %s", data.get(position).getDescription()));

        String saleprice = String.format("%,." + frmmain.price_places + "f", data.get(position).getSaleprice());
        holder.txtPrice.setText(saleprice);
        holder.layoutItem.setBackgroundResource(R.drawable.usercodegradiant);
        if (frmmain.withoutclass.equals("true")) {
            if (position == 0) {
                holder.layoutItem.setBackgroundResource(R.drawable.categorygradiant);
            } else {
                holder.layoutItem.setBackgroundResource(R.drawable.usercodegradiant);
            }
        }
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (data.get(position).getUsr_code().equals("Back")) {
                        if (formname.equals("SaleOrder"))//added by YLT
                        {
                            saleorder_entry.imgFilterCode.setVisibility(View.GONE);
                            saleorder_entry.fitercode = "Category";
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

                                cursor.close();
                            }

                            CategoryAdapter ad = new CategoryAdapter(context, categories, rv, "SaleOrder");
                            rv.setAdapter(ad);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), 4);
                            rv.setLayoutManager(gridLayoutManager);
                        } else {

                            sale_entry_tv.imgFilterCode.setVisibility(View.GONE);
                            sale_entry_tv.fitercode = "Category";
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
                                cursor.close();
                            }

                            CategoryAdapter ad = new CategoryAdapter(context, categories, rv);
                            rv.setAdapter(ad);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context.getApplicationContext(), 4);
                            rv.setLayoutManager(gridLayoutManager);
                        }

                    } else {



                        addData(data.get(position).getUsr_code());

                    }
                } catch (Exception ex) {
                    System.out.println("UsrcodeAdapter : " + ex.getMessage());

                }
            }
        });
    }


    //Added by abbp barcode scanner on 19/6/2019
    public static void scanner(String usr_code) {
        addData(usr_code);
    }

    private static void addData(String usr_code) {


        int defUnit = frmmain.defunit;
        int[] ut = new int[3];
        int i = 0;
        Cursor cc = DatabaseHelper.rawQuery("select unit_type from Usr_Code where usr_code='" + usr_code + "'");
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
                String sqlString = "select uc.unit_type,code,description," + sale_price + " as sale_price,open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                        " where uc.unit_type=" + defUnit + " and uc.usr_code='" + usr_code + "'";
                Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {

                            long code = cursor.getLong(cursor.getColumnIndex("code"));
                            double price = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                            int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                            double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                            int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                            String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                            String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                            String desc = cursor.getString(cursor.getColumnIndex("description"));
                            int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            sale_entry_tv.sd.add(new sale_det(
                                    sale_entry_tv.tranid,
                                    sale_entry_tv.sd.size() + 1,
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
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
//                sale_entry_tv.itemAdapter.notifyDataSetChanged();
//                sale_entry_tv.entrygrid.smoothScrollToPosition(sale_entry_tv.sd.size());
//                sale_entry_tv.getSummary();

            } else {
                int utt = 2;
                if (defUnit == 2 || defUnit == 3) {
                    utt = 2;
                } else if (defUnit == 1) {
                    utt = 1;
                }
                specialPrice = GetPriceLevel();
                String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
                String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
                String sqlString = "select uc.unit_type,code,description," + sale_price + " as sale_price,open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                        " where uc.unit_type=" + utt + " and uc.usr_code='" + usr_code + "'";
                Cursor cursor = DatabaseHelper.rawQuery(sqlString);
                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {

                            long code = cursor.getLong(cursor.getColumnIndex("code"));
                            double price = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                            int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                            double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                            int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                            String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                            String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                            String desc = cursor.getString(cursor.getColumnIndex("description"));
                            int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            sale_entry_tv.sd.add(new sale_det(
                                    sale_entry_tv.tranid,
                                    sale_entry_tv.sd.size() + 1,
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
                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                }
//                sale_entry_tv.itemAdapter.notifyDataSetChanged();
//                sale_entry_tv.entrygrid.smoothScrollToPosition(sale_entry_tv.sd.size());
//                sale_entry_tv.getSummary();
            }

        } else if (i == 1 || defUnit == 1) {

            specialPrice = GetPriceLevel();
            String sale_price = specialPrice == 0 ? "uc.sale_price" : "uc.sale_price" + specialPrice;
            String SP = specialPrice == 0 ? "SP" : "SP" + specialPrice;
            String sqlString = "select uc.unit_type,code,description," + sale_price + " as sale_price,open_price,smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                    " where uc.unit_type = 1 and uc.usr_code='" + usr_code + "'";
            Cursor cursor = DatabaseHelper.rawQuery(sqlString);
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {

                        long code = cursor.getLong(cursor.getColumnIndex("code"));
                        double price = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                        int open_price = cursor.getInt(cursor.getColumnIndex("open_price"));
                        double smallest_unit_qty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                        int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                        String unit_short = cursor.getString(cursor.getColumnIndex("unitshort")).equals("null") ? "" : cursor.getString(cursor.getColumnIndex("unitshort"));
                        String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                        String desc = cursor.getString(cursor.getColumnIndex("description"));
                        int CalNoTax = cursor.getInt(cursor.getColumnIndex("CalNoTax"));
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        sale_entry_tv.sd.add(new sale_det(
                                sale_entry_tv.tranid,
                                sale_entry_tv.sd.size() + 1,
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
                    } while (cursor.moveToNext());
                }

                cursor.close();
            }
//            sale_entry_tv.itemAdapter.notifyDataSetChanged();
//            sale_entry_tv.entrygrid.smoothScrollToPosition(sale_entry_tv.sd.size());
//            sale_entry_tv.getSummary();
        }

        if (formname.equals("Sale")) {
            GetPdis();
        }

        sale_entry_tv.itemAdapter.notifyDataSetChanged();
        sale_entry_tv.entrygrid.smoothScrollToPosition(sale_entry_tv.sd.size());
        sale_entry_tv.getSummary();

    }

    private static void GetPdis() {

        long code = sale_entry_tv.sd.get((sale_entry_tv.sd.size() - 1)).getCode();
        long locationid = sale_entry_tv.sh.get(0).getLocationid();
        int unit_type = sale_entry_tv.sd.get((sale_entry_tv.sd.size() - 1)).getUnit_type();
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
            cursor.close();
        }

        if (discount > 0) {
            int dis_type = 5;
            sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).setDis_type(dis_type);
            sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).setDis_percent(discount);
            double disprice = sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).getSale_price() - (sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).getSale_price() * (discount / 100));
            sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).setDis_price(disprice);
//            sale_entry_tv.itemAdapter.notifyDataSetChanged();
//            sale_entry_tv.entrygrid.smoothScrollToPosition(sale_entry_tv.sd.size());
//            sale_entry_tv.getSummary();

        } else if (dis_price > 0) {
            sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).setDis_type(5);
            //double disprice = sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).getSale_price() - dis_price;
            sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).setDis_price(dis_price);
//            sale_entry_tv.itemAdapter.notifyDataSetChanged();
//            sale_entry_tv.entrygrid.smoothScrollToPosition(sale_entry_tv.sd.size());
//            sale_entry_tv.getSummary();
        }

    }

    private static long GetPriceLevel() {
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
            cursor.close();
        }

        if (useCustpricelevel) {
            if (formname.equals("SaleOrder")) {
                String sSql = "select pricelevel from customer where customerid =" + saleorder_entry.sh.get(0).getCustomerid();//added by YLT
                cursor = DatabaseHelper.rawQuery(sSql);
            } else {
                String sSql = "select pricelevel from customer where customerid =" + sale_entry_tv.sh.get(0).getCustomerid();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutItem;
        TextView txtName;
        TextView txtPrice;
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
                layoutItem = itemView.findViewById(R.id.layout_item_usrcode);
                txtName = itemView.findViewById(R.id.info_text);
                txtPrice = itemView.findViewById(R.id.txt_price);
            }

        }
    }
}
