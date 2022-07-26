package com.abbp.istockmobilesalenew.tvsale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
import com.abbp.istockmobilesalenew.saleorder_entry;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class UsrcodeAdapter extends RecyclerView.Adapter<UsrcodeAdapter.MyViewHolder> {

    Context context;
    File directory;
    ArrayList<usr_code> data = new ArrayList<>();
    ArrayList<category> categories = new ArrayList<>();
    ArrayList<brand> brands = new ArrayList<>();
    String usr_code;
    RecyclerView rv;
    int Use_Tax = 0;
    public static String formname = "";//added by YLT

    static long specialPrice = 0;

    public UsrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv, ArrayList<category> categories) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        this.categories = categories;
        formname = "Sale";
    }

    //added by YLT
    public UsrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv, ArrayList<category> categories, String frm) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        this.categories = categories;
        formname = frm;//added by YLT
    }


    public UsrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        formname = "Sale";
    }

    //added by YLT
    public UsrcodeAdapter(Context context, ArrayList<usr_code> data, RecyclerView rv, String frm) {
        this.context = context;
        this.data = data;
        this.rv = rv;
        formname = frm;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v;
        if (frmmain.use_pic == 1) {
            v = lf.inflate(R.layout.itemwithpic, parent, false);
        } else {
            v = lf.inflate(R.layout.tv_item_usrcode, parent, false);
        }
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

            String[] pathnew = path.split("\\\\");
            String imgUrl = "http://" + ip + "/api/DataSync/GetImage?imgName=" + (pathnew.length > 3 ? pathnew[3] : path);
            Log.i("imgUrl", imgUrl);
            Picasso.get().load(imgUrl)
                    .placeholder(R.drawable.ic_image_search)
                    .error(R.drawable.ic_image_search)
                    .into(holder.iv);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addData(data.get(position).getUsr_code());
                }
            });
            holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ItemDetail_Dialog(position, imgUrl);
                    return false;
                }
            });

        } //use_pic
        else {
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

                        } //Back
                        else {
                            addData(data.get(position).getUsr_code());
                        }
                    } catch (Exception ex) {
                        System.out.println("UsrcodeAdapter : " + ex.getMessage());

                    }
                }
            });
        }

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
        String plvl = level == 0 ? "" : String.valueOf(level);
        String sql = "select discount" + plvl + ",dis_price" + plvl + " from Pdis where code=" + code + " and unit_type=" + unit_type +
                " and locationid=" + locationid;
        Cursor cursor = DatabaseHelper.rawQuery(sql);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    discount = cursor.getDouble(cursor.getColumnIndex("discount" + plvl));
                    dis_price = cursor.getDouble(cursor.getColumnIndex("dis_price" + plvl));

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

        } else if (dis_price > 0) {
            sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).setDis_type(5);
            //double disprice = sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).getSale_price() - dis_price;
            sale_entry_tv.sd.get(sale_entry_tv.sd.size() - 1).setDis_price(dis_price);
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
                    useUserpricelevel = cursor.getInt(cursor.getColumnIndex("use_user_pricelevel")) == 1;
                    useCustpricelevel = cursor.getInt(cursor.getColumnIndex("use_cust_pricelevel")) == 1;
                    useSpecialPrice = cursor.getInt(cursor.getColumnIndex("use_specialprice")) == 1;
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

    private void ItemDetail_Dialog(int position, String imgUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemdetail, null);
        builder.setView(view);
        TextView itemname = view.findViewById(R.id.itemname);
        TextView txtdesc = view.findViewById(R.id.txtdesc);
        TextView txtsprice = view.findViewById(R.id.txtsprice);
        ImageView imageView = view.findViewById(R.id.imgview);
        //itemname.setTypeface(font);
        itemname.setText(data.get(position).getUsr_code());
        String numberAsString = String.format("%,." + frmmain.price_places + "f", data.get(position).getSaleprice());
        txtsprice.setText(numberAsString);
        txtdesc.setText(data.get(position).getDescription());
        Picasso.get().load(imgUrl)
                .placeholder(R.drawable.ic_image_search)
                .error(R.drawable.ic_image_search)
                .into(imageView);
        final Dialog dialog = builder.create();
        dialog.show();

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
