package com.abbp.istockmobilesalenew.tvsale;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abbp.istockmobilesalenew.DatabaseHelper;
import com.abbp.istockmobilesalenew.R;

import com.abbp.istockmobilesalenew.frmlogin;
import com.abbp.istockmobilesalenew.frmmain;

import com.abbp.istockmobilesalenew.saleorder_entry;
import com.abbp.istockmobilesalenew.unitforcode;

import static com.abbp.istockmobilesalenew.tvsale.sale_entry_tv.itemPosition;

import java.util.ArrayList;


public class ItemAdapter extends BaseAdapter {
    Context context;
    boolean isqty = false;
    boolean isSalePrice = false;
    boolean isgallon = false;
    boolean isAmount = false;
    String keynum = "";
    AlertDialog msg, dialog, da;
    ArrayList<unitforcode> uc = new ArrayList<>();
    public static TextView txtamt;
    TextView tv4;
    TextView tv5;
    boolean startOpen;
    public static ItemAdapter itemAd;
    public static double disamt;
    public static String formname;//added by YLT

    public ItemAdapter(Context context) {
        this.context = context;
        formname = "Sale";

    }

    public ItemAdapter(Context context, String frm) {//added by YLT
        this.context = context;
        formname = frm;//added by YLT

    }

    @Override


    public int getCount() {
        if (formname == "SaleOrder") {
            return saleorder_entry.sd.size();//added by YLT
        } else {
            return sale_entry_tv.sd.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = lf.inflate(R.layout.dataitem, null, false);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tv_dataitem, parent, false);
        }
        TextView tvSr = convertView.findViewById(R.id.sr);
        TextView tvDesc = convertView.findViewById(R.id.desc);
        TextView tvQty = convertView.findViewById(R.id.qty);
        TextView tvUnit = convertView.findViewById(R.id.unit);
        TextView tvGallon = convertView.findViewById(R.id.gallon);

        ImageView btnMinus = convertView.findViewById(R.id.img_qty_minus);
        ImageView btnPlus = convertView.findViewById(R.id.img_qty_plus);

        if (formname == "Sale") {

            if (frmlogin.use_oil == 1) {
                tvGallon.setVisibility(View.VISIBLE);
                isgallon = false;
            } else {
                tvGallon.setVisibility(View.GONE);
                isgallon = false;
            }

            String qtyAsString = String.format("%." + frmmain.qty_places + "f", sale_entry_tv.sd.get(position).getGallon());
            tvGallon.setText(qtyAsString);

            tvGallon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isqty = false;
                    isSalePrice = false;
                    isgallon = true;
                    keynum = tvQty.getText().toString();
                    showKeyPad(tvGallon, tvGallon, position);
                    itemPosition = -1;
                }
            });

            btnMinus.setOnClickListener(v -> {
                double unit_qty = Double.parseDouble(tvQty.getText().toString());
                if (unit_qty > 1) {
                    unit_qty = unit_qty - 1;
//                    tv2.setText(String.format("%." + frmmain.qty_places + "f", unit_qty));

                    CalculateQty(tvQty, position, unit_qty);

                }

            });

            btnPlus.setOnClickListener(v -> {
                double unit_qty = Double.parseDouble(tvQty.getText().toString());
                unit_qty = unit_qty + 1;
//                tv2.setText(String.format("%." + frmmain.qty_places + "f", unit_qty));
                CalculateQty(tvQty, position, unit_qty);

            });

        } else {
            tvGallon.setVisibility(View.GONE);
            isgallon = false;
        }

        boolean use_unit = false;
        Cursor cursorplvl = DatabaseHelper.rawQuery("select use_unit from SystemSetting");
        if (cursorplvl != null && cursorplvl.getCount() != 0) {
            if (cursorplvl.moveToFirst()) {
                do {
                    use_unit = cursorplvl.getInt(cursorplvl.getColumnIndex("use_unit")) == 1 ? true : false;
                } while (cursorplvl.moveToNext());
            }
            cursorplvl.close();
        }

        tvUnit.setVisibility(use_unit == true ? View.VISIBLE : View.GONE);
        tv4 = (TextView) convertView.findViewById(R.id.amt);
        tv5 = convertView.findViewById(R.id.txtDelete);

        if (formname == "SaleOrder")//added by YLT
        {
            tvSr.setText(String.valueOf(saleorder_entry.sd.get(position).getSr()));
            tvDesc.setText(saleorder_entry.sd.get(position).getDesc());
            Double unit_qty = saleorder_entry.sd.get(position).getUnit_qty();
            String qtyAsString = String.format("%." + frmmain.qty_places + "f", unit_qty);
            tvQty.setText(qtyAsString);
            tvUnit.setText(saleorder_entry.sd.get(position).getUnit_short());
            double amt = saleorder_entry.sd.get(position).getSale_price() * saleorder_entry.sd.get(position).getUnit_qty();
            String numberAsString = String.format("%,." + frmmain.price_places + "f", amt);
            tv4.setText(String.valueOf(numberAsString));
        } else {
            tvSr.setText(String.valueOf(sale_entry_tv.sd.get(position).getSr()));
            tvDesc.setText(sale_entry_tv.sd.get(position).getDesc());
            Double unit_qty = sale_entry_tv.sd.get(position).getUnit_qty();
            String qtyAsString = String.format("%." + frmmain.qty_places + "f", unit_qty);
            tvQty.setText(qtyAsString);
            tvUnit.setText(sale_entry_tv.sd.get(position).getUnit_short());
            double amt = sale_entry_tv.sd.get(position).getSale_price() * sale_entry_tv.sd.get(position).getUnit_qty();
            String numberAsString = String.format("%,." + frmmain.price_places + "f", amt);
            tv4.setText(numberAsString);
        }


        tvQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isqty = true;
                isSalePrice = false;
                isgallon = false;
                keynum = tvQty.getText().toString();
                showKeyPad(tvQty, tvQty, position);
                itemPosition = -1;
            }
        });

        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder bd = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                bd.setTitle("iStock");
                bd.setMessage("Are you sure want to delete this item?");
                bd.setCancelable(false);
                bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        sale_entry_tv.sd.remove(position);
                        itemPosition = -1;
                        for (int i = 0; i < sale_entry_tv.sd.size(); i++) {
                            sale_entry_tv.sd.get(i).setSr(i + 1);
                        }
                        //sale_entry_tv.getData();
                        sale_entry_tv.itemAdapter.notifyDataSetChanged();
                        sale_entry_tv.getSummary();
                        if (sale_entry_tv.sd.size() == 0) {
                            String tax = "Tax" + (sale_entry_tv.getTax() > 0 ? "( " + sale_entry_tv.getTax() + "% )" : "");
                            sale_entry_tv.txttax.setText(tax);
                            sale_entry_tv.sh.get(0).setTax_per(sale_entry_tv.getTax());
                            sale_entry_tv.sh.get(0).setTax_amount(0.0);
                            sale_entry_tv.sh.get(0).setDiscount(0.0);
                            sale_entry_tv.sh.get(0).setDiscount_per(0);
                            sale_entry_tv.sh.get(0).setPaid_amount(0);
                            sale_entry_tv.txtvoudis.setText("0");
                            sale_entry_tv.txtpaid.setText("0");
                            sale_entry_tv.getSummary();
                        }
                        dialog.dismiss();

                    }
                });

                bd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                bd.create().show();

            }
        });


        tvUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String sqlString;
                    int unitcount = 0;
                    String filter;
                    Cursor cursor = null;
                    if (uc.size() > 0) uc.clear();
                    AlertDialog.Builder bd = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.changeheadervalue, null);
                    bd.setCancelable(false);
                    bd.setView(view);
                    RecyclerView rv = view.findViewById(R.id.rcvChange);
                    ImageButton imgClose = view.findViewById(R.id.imgNochange);
                    da = bd.create();
                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            da.dismiss();
                        }
                    });
                    EditText etdSearch = view.findViewById(R.id.etdSearch);
                    ImageButton imgSearch = view.findViewById(R.id.imgSearch);
                    etdSearch.setVisibility(View.GONE);
                    imgSearch.setVisibility(View.GONE);
                    if (formname == "SaleOrder")//added by YLT
                    {
                        sqlString = "select * from Usr_Code where unit>0 and code=" + saleorder_entry.sd.get(position).getCode() + " order by unit_type";
                        cursor = DatabaseHelper.rawQuery(sqlString);
                    } else {
                        sqlString = "select * from Usr_Code where unit>0 and code=" + sale_entry_tv.sd.get(position).getCode() + " order by unit_type";
                        cursor = DatabaseHelper.rawQuery(sqlString);
                    }
                    unitcount = cursor.getCount();
                    if (cursor != null && cursor.getCount() != 0) {
                        if (cursor.moveToFirst()) {
                            do {
                                int code = cursor.getInt(cursor.getColumnIndex("code"));
                                String usr_code = cursor.getString(cursor.getColumnIndex("usr_code"));
                                int unit_type = cursor.getShort(cursor.getColumnIndex("unit_type"));
                                int unit = cursor.getInt(cursor.getColumnIndex("unit"));
                                String unitname = cursor.getString(cursor.getColumnIndex("unitname"));
                                String shortdes = cursor.getString(cursor.getColumnIndex("unitshort"));
                                double saleprice = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                                double saleprice1 = cursor.getDouble(cursor.getColumnIndex("sale_price1"));
                                double saleprice2 = cursor.getDouble(cursor.getColumnIndex("sale_price2"));
                                double saleprice3 = cursor.getDouble(cursor.getColumnIndex("sale_price3"));
                                double sqty = cursor.getDouble((cursor.getColumnIndex("smallest_unit_qty")));
                                uc.add(new unitforcode(code, usr_code, unit_type, unit, unitname, shortdes, saleprice, sqty, saleprice1, saleprice2, saleprice3));

                            } while (cursor.moveToNext());

                        }
                    }

                    cursor.close();

                    UnitAdapter ad = new UnitAdapter(context, uc, position, itemAd, da, formname);
                    rv.setAdapter(ad);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
                    rv.setLayoutManager(gridLayoutManager);
                    if (unitcount > 0) {
                        da.show();
                    }
                    cursor = null;
                    itemPosition = -1;


                } catch (Exception e) {
                    da.dismiss();
                }
            }
        });

        return convertView;
    }

    private void showKeyPad(TextView txt, TextView source, int itemposition) {

        startOpen = true;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.keypad, null);
        float density = context.getResources().getDisplayMetrics().density;
        final PopupWindow pw = new PopupWindow(layout, (int) density * 310, (int) density * 500, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.showAsDropDown(txt);
        Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnc, btndec, btndel, btnenter, btnper;
        btn1 = layout.findViewById(R.id.txt1);
        btn2 = layout.findViewById(R.id.txt2);
        btn3 = layout.findViewById(R.id.txt3);
        btn4 = layout.findViewById(R.id.txt4);
        btn5 = layout.findViewById(R.id.txt5);
        btn6 = layout.findViewById(R.id.txt6);
        btn7 = layout.findViewById(R.id.txt7);
        btn8 = layout.findViewById(R.id.txt8);
        btn9 = layout.findViewById(R.id.txt9);
        btn0 = layout.findViewById(R.id.txt0);
        btnc = layout.findViewById(R.id.txtc);
        btndec = layout.findViewById(R.id.txtdec);
        btnenter = layout.findViewById(R.id.txtenter);
        btndel = layout.findViewById(R.id.txtdel);
        btnper = layout.findViewById(R.id.btnpercent);
        TextView txtNum = layout.findViewById(R.id.txtNum);
        btnper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btnper.getText());
                keynum = txtNum.getText().toString();
            }
        });
        txtNum.setText(String.valueOf(keynum));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                if (txtNum.getText().equals("0")) {
                    txtNum.setText(btn1.getText());
                    keynum = txtNum.getText().toString();
                } else {
                    txtNum.setText(keynum + btn1.getText());
                    keynum = txtNum.getText().toString();
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn2.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn3.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn4.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn5.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn6.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn7.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn8.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn9.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btn0.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText("0");
                keynum = txtNum.getText().toString();
            }
        });
        btndec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startOpen == true) {
                    keynum = "";
                    startOpen = false;
                }
                txtNum.setText(keynum + btndec.getText());
                keynum = txtNum.getText().toString();
            }
        });
        btndel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (keynum.length() != 0) {
                    keynum = keynum.substring(0, keynum.length() - 1);
                    txtNum.setText(keynum);

                }
                if (keynum.length() == 0) {
                    keynum = "0";
                    txtNum.setText(keynum);
                }
                startOpen = false;

            }
        });
        btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double check = Double.parseDouble(keynum);
                    if (isqty) {


                        check = check > 0 ? check : 1;
                        sale_entry_tv.sd.get(itemposition).setUnit_qty(check);
                        source.setText(String.valueOf(check));
                        GetSpecialPrice(itemposition);

                        Cursor cursor = DatabaseHelper.rawQuery("select smallest_unit_qty from usr_code where code=" + sale_entry_tv.sd.get(itemposition).getCode() +
                                " and unit_type=" + sale_entry_tv.sd.get(itemposition).getUnit_type()
                        );
                        if (cursor != null && cursor.getCount() != 0) {
                            if (cursor.moveToFirst()) {
                                do {
                                    double sqty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                    double gallon = sale_entry_tv.sd.get(itemposition).getUnit_qty() * sqty;
                                    sale_entry_tv.sd.get(itemposition).setGallon(gallon);

                                } while (cursor.moveToNext());

                            }
                            cursor.close();
                        }

//                            sale_entry_tv.entrygrid.setAdapter(itemAd);
//                            sale_entry_tv.entrygrid.setSelection(itemposition);
                        sale_entry_tv.itemAdapter.notifyDataSetChanged();
                        sale_entry_tv.getSummary();

                    } else if (isgallon) {
                        check = check > 0 ? check : 1;
                        sale_entry_tv.sd.get(itemposition).setGallon(check);
                        source.setText(String.valueOf(check));
                        Cursor cursor = DatabaseHelper.rawQuery("select smallest_unit_qty from usr_code where code=" + sale_entry_tv.sd.get(itemposition).getCode() +
                                " and unit_type=" + sale_entry_tv.sd.get(itemposition).getUnit_type()
                        );
                        if (cursor != null && cursor.getCount() != 0) {
                            if (cursor.moveToFirst()) {
                                do {
                                    double sqty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                    double unit_qty = sale_entry_tv.sd.get(itemposition).getGallon() / sqty;
                                    sale_entry_tv.sd.get(itemposition).setUnit_qty(unit_qty);

                                } while (cursor.moveToNext());

                            }
                            cursor.close();
                        }


                        sale_entry_tv.entrygrid.setAdapter(itemAd);
                        sale_entry_tv.entrygrid.setSelection(itemposition);
                        sale_entry_tv.getSummary();
                    } else if (isSalePrice) {
                        if (formname.equals("SaleOrder")) //added by YLT
                        {
                            check = check > 0 ? check : 0;
                            saleorder_entry.sd.get(itemposition).setSale_price(check);
                            Double amt = check * saleorder_entry.sd.get(itemposition).getUnit_qty();
                            String numberAsString = String.format("%,." + frmmain.price_places + "f", amt);
                            txtamt.setText(numberAsString);
                            source.setText(String.format("%,." + frmmain.price_places + "f", check));
                            //sale_entry_tv.getSummary();

                        } else {
                            check = check > 0 ? check : 0;
                            if (!isAmount) {
                                sale_entry_tv.sd.get(itemposition).setSale_price(check);
                                Double amt = check * sale_entry_tv.sd.get(itemposition).getUnit_qty();
                                String numberAsString = String.format("%,." + frmmain.price_places + "f", amt);
                                txtamt.setText(numberAsString);

                            } else {
                                double unit_qty = check / sale_entry_tv.sd.get(itemposition).getSale_price();
                                sale_entry_tv.sd.get(itemposition).setUnit_qty(unit_qty);
                                double sale_price = check / unit_qty;
                                sale_entry_tv.sd.get(itemposition).setSale_price(sale_price);
                                txt.setText(String.format("%,." + frmmain.price_places + "f", sale_price));

                                Cursor cursor = DatabaseHelper.rawQuery("select smallest_unit_qty from usr_code where code=" + sale_entry_tv.sd.get(itemposition).getCode() +
                                        " and unit_type=" + sale_entry_tv.sd.get(itemposition).getUnit_type()
                                );
                                if (cursor != null && cursor.getCount() != 0) {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            double sqty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                                            double gallon = unit_qty * sqty;
                                            sale_entry_tv.sd.get(itemposition).setGallon(gallon);

                                        } while (cursor.moveToNext());

                                    }
                                    cursor.close();
                                }
                            }

                            source.setText(String.format("%,." + frmmain.price_places + "f", check));

                            //sale_entry_tv.getSummary();
                        }
                        isAmount = false;
                    }

                    isqty = false;
                    startOpen = false;
                    pw.dismiss();

                } catch (Exception e) {
                    keynum = "0";
                    startOpen = true;
                    if (isqty) {
                        keynum = "1";
                    }
                    txtNum.setText(keynum);
                    AlertDialog.Builder bd = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    bd.setTitle("iStock");
                    bd.setMessage("Incorrect Number Format!");
                    bd.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                    msg = bd.create();
                    msg.show();
                    pw.dismiss();
                    isqty = false;
                }


            }
        });


    }

    public void GetSpecialPrice(int position) {
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

        if (formname == "SaleOrder")//added by YLT
        {
            double discount = saleorder_entry.sd.get(position).getSale_price() - saleorder_entry.sd.get(position).getDis_price();
            if (useSpecialPrice) {
                String sql = "select sale_price,price_level from S_SalePrice where code=" + saleorder_entry.sd.get(position).getCode() +
                        " and unit_type=" + saleorder_entry.sd.get(position).getUnit_type() + " and ( " +
                        saleorder_entry.sd.get(position).getUnit_qty() + " between min_qty and max_qty)";
                cursor = DatabaseHelper.rawQuery(sql);

                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            double sale_price = cursor.getDouble(cursor.getColumnIndex("Sale_Price"));
                            level = cursor.getInt(cursor.getColumnIndex("price_level"));
                            String price_level = level == 0 ? "SP" : level == 1 ? "SP1" : level == 2 ? "SP2" : "SP3";
                            saleorder_entry.sd.get(position).setSale_price(sale_price);
                            saleorder_entry.sd.get(position).setPriceLevel(price_level);
                            CalculateItemDiscount(position, discount);
                        } while (cursor.moveToNext());

                    }

                } else {
                    level = 0;
                    saleorder_entry.sd.get(position).setSale_price(getSalePrice("SP", position));
                    saleorder_entry.sd.get(position).setPriceLevel("SP");
                    CalculateItemDiscount(position, discount);
                }
                cursor.close();

            }
        } else {
            double discount = sale_entry_tv.sd.get(position).getSale_price() - sale_entry_tv.sd.get(position).getDis_price();
            if (useSpecialPrice) {
                String sql = "select sale_price,price_level from S_SalePrice where code=" + sale_entry_tv.sd.get(position).getCode() +
                        " and unit_type=" + sale_entry_tv.sd.get(position).getUnit_type() + " and ( " +
                        sale_entry_tv.sd.get(position).getUnit_qty() + " between min_qty and max_qty)";
                cursor = DatabaseHelper.rawQuery(sql);

                if (cursor != null && cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            double sale_price = cursor.getDouble(cursor.getColumnIndex("Sale_Price"));
                            level = cursor.getInt(cursor.getColumnIndex("price_level"));
                            String price_level = level == 0 ? "SP" : level == 1 ? "SP1" : level == 2 ? "SP2" : "SP3";
                            sale_entry_tv.sd.get(position).setSale_price(sale_price);
                            sale_entry_tv.sd.get(position).setPriceLevel(price_level);
                            CalculateItemDiscount(position, discount);
                        } while (cursor.moveToNext());

                    }

                } else {
                    level = 0;
                    sale_entry_tv.sd.get(position).setSale_price(getSalePrice("SP", position));
                    sale_entry_tv.sd.get(position).setPriceLevel("SP");
                    CalculateItemDiscount(position, discount);
                }
                cursor.close();

            }
        }


    }

    private double getSalePrice(String pricelevel, int itemposistion) {

        double sale_price = 0;
        String level = "";
        switch (pricelevel) {
            case "SP":
                level = "uc.sale_price";
                break;
            case "SP1":
                level = "uc.sale_price1";
                break;
            case "SP2":
                level = "uc.sale_price2";
                break;
            case "SP3":
                level = "uc.sale_price3";
                break;
        }
        if (formname == "SaleOrder") //added by YLT
        {
            String sqlString = "select uc.unit_type,code,description," + level + ",smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                    " where code=" + saleorder_entry.sd.get(itemposistion).getCode() + " and unit_type=" + saleorder_entry.sd.get(itemposistion).getUnit_type();
            Cursor cursor = DatabaseHelper.rawQuery(sqlString);


            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        switch (pricelevel) {
                            case "SP":
                                level = "sale_price";
                                break;
                            case "SP1":
                                level = "sale_price1";
                                break;
                            case "SP2":
                                level = "sale_price2";
                                break;
                            case "SP3":
                                level = "sale_price3";
                                break;
                        }
                        sale_price = cursor.getDouble(cursor.getColumnIndex(level));

                    } while (cursor.moveToNext());


                }

            }
            cursor.close();
            return sale_price;
        } else {
            String sqlString = "select uc.unit_type,code,description," + level + ",smallest_unit_qty,unitname,unitshort,CalNoTax from Usr_Code uc " +
                    " where code=" + sale_entry_tv.sd.get(itemposistion).getCode() + " and unit_type=" + sale_entry_tv.sd.get(itemposistion).getUnit_type();
            Cursor cursor = DatabaseHelper.rawQuery(sqlString);


            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        switch (pricelevel) {
                            case "SP":
                                level = "sale_price";
                                break;
                            case "SP1":
                                level = "sale_price1";
                                break;
                            case "SP2":
                                level = "sale_price2";
                                break;
                            case "SP3":
                                level = "sale_price3";
                                break;
                        }
                        sale_price = cursor.getDouble(cursor.getColumnIndex(level));

                    } while (cursor.moveToNext());


                }

            }
            cursor.close();
            return sale_price;
        }

    }

    public void CalculateItemDiscount(int itemPosition, double dis) {


        if (sale_entry_tv.sd.get(itemPosition).getDis_type() == 3
                || sale_entry_tv.sd.get(itemPosition).getDis_type() == 4
                || sale_entry_tv.sd.get(itemPosition).getDis_type() == 6
                || sale_entry_tv.sd.get(itemPosition).getDis_type() == 7) {
            sale_entry_tv.sd.get(itemPosition).setDis_percent(0);
            sale_entry_tv.sd.get(itemPosition).setDis_price(sale_entry_tv.sd.get(itemPosition).getSale_price());
        } else if (sale_entry_tv.sd.get(itemPosition).getDis_type() == 1
                || sale_entry_tv.sd.get(itemPosition).getDis_type() == 2) {
            double dispercent = sale_entry_tv.sd.get(itemPosition).getDis_type() == 1 ? 0.05 : 0.1;
            double discount = sale_entry_tv.sd.get(itemPosition).getDis_type() == 1 ? 5 : 10;
            sale_entry_tv.sd.get(itemPosition).setDis_percent(discount);
            double dis_price = sale_entry_tv.sd.get(itemPosition).getSale_price() - (sale_entry_tv.sd.get(itemPosition).getSale_price() * (dispercent));
            sale_entry_tv.sd.get(itemPosition).setDis_price(dis_price);
        } else if (sale_entry_tv.sd.get(itemPosition).getDis_type() == 5) {
            if (sale_entry_tv.sd.get(itemPosition).getDis_percent() > 0) {
                double dis_percent = sale_entry_tv.sd.get(itemPosition).getDis_percent();
                sale_entry_tv.sd.get(itemPosition).setDis_percent(dis_percent);
                double dis_price = sale_entry_tv.sd.get(itemPosition).getSale_price() - (sale_entry_tv.sd.get(itemPosition).getSale_price() * (dis_percent / 100));
                sale_entry_tv.sd.get(itemPosition).setDis_price(dis_price);


            } else {
                double dis_percent = sale_entry_tv.sd.get(itemPosition).getDis_percent();
                sale_entry_tv.sd.get(itemPosition).setDis_percent(dis_percent);
                double dis_price = sale_entry_tv.sd.get(itemPosition).getSale_price() - dis;
                sale_entry_tv.sd.get(itemPosition).setDis_price(dis_price);
            }
        }


    }

    public void getItemAdpater(ItemAdapter itemAdapter) {
        itemAd = itemAdapter;
    }

    private String ClearFormat(String s) {
        return s.replace(",", "");
    }

    private void CalculateQty(TextView textView, int position, Double value) {
        sale_entry_tv.sd.get(position).setUnit_qty(value);
        textView.setText(String.format("%." + frmmain.qty_places + "f", value));
        GetSpecialPrice(position);

        Cursor cursor = DatabaseHelper.rawQuery("select smallest_unit_qty from usr_code where code=" + sale_entry_tv.sd.get(position).getCode() +
                " and unit_type=" + sale_entry_tv.sd.get(position).getUnit_type()
        );
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    double sqty = cursor.getDouble(cursor.getColumnIndex("smallest_unit_qty"));
                    double gallon = sale_entry_tv.sd.get(position).getUnit_qty() * sqty;
                    sale_entry_tv.sd.get(position).setGallon(gallon);

                } while (cursor.moveToNext());

            }
            cursor.close();
        }

//        sale_entry_tv.entrygrid.setAdapter(itemAd);
//        itemAd.notifyDataSetChanged();
        sale_entry_tv.itemAdapter.notifyDataSetChanged();
        sale_entry_tv.getSummary();
    }

}

