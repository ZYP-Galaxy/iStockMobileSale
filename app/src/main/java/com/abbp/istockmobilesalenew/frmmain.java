package com.abbp.istockmobilesalenew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abbp.istockmobilesalenew.tvsale.sale_entry_tv;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class frmmain extends AppCompatActivity implements View.OnClickListener {
    private CardView cardsale;
    private CardView cardsaleOrder;
    private CardView cardsalelist;
    private CardView cardstock;
    private ImageView cardlogout;
    private CardView cardsaleOrderlist;
    private CardView cardStockstatuslist;
    private CardView cardoutstandlist;
    private CardView cardreturnin;
    private CardView cardreturninlist;
    AlertDialog showmsg;

    private RequestQueue requestQueue;
    public static int qty_places = 0;
    public static int price_places = 0;
    public static String withoutclass = "true";
    public static int use_pic = 0;
    public static boolean use_multicurrency;
    public static int defunit = 1;
    SharedPreferences sh_ip;
    SharedPreferences sh_port;
    Intent intent;
    public static int CCount = 0;
    public static boolean usedefaultcurrency;

    public static boolean Allow_Oustand;
    public static boolean Allow_StockStatus;
    public static boolean Allow_Sale;
    public static boolean Allow_SaleOrder;
    public static boolean All_Users;
    AlertDialog dialog, msg, downloadAlert;
    public static double exg_rate, div_rate = 1;
    public static String currencyshort = "MMK";
    public static Typeface font;
    public static String inClass = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmmain);
        SetUI();
        sh_ip = getSharedPreferences("ip", MODE_PRIVATE);
        sh_port = getSharedPreferences("port", MODE_PRIVATE);
        getSystemSetting();
        GetAppSetting getAppSetting = new GetAppSetting("Use_DefaultCurrency");
        usedefaultcurrency = getAppSetting.getSetting_Value().toLowerCase().equals("y");
        //added by KLM to calculate Currency 24062022
        isusemulticurrency();
        if (use_multicurrency && !frmlogin.isTVMode) {
            GetExg_Rate();
        }
        getUserClass();
        GlobalClass.ChangeLanguage((ViewGroup) findViewById(R.id.lmenu), this, 14, font);

    }
    private void getUserClass() {//YLT
        //Modify by KLM to Show usrcode associated with user class 21032022
        inClass = "0";
        try {
            Cursor cursor = DatabaseHelper.rawQuery("select distinct class from userclass where userid=" + frmlogin.LoginUserid);
            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {

                        int classid = cursor.getInt(cursor.getColumnIndex("class"));
                        inClass += "," + classid;
                        //notinClass+="class="+classid+" or ";4
                        //DatabaseHelper.execute("delete from usr_code where class <>"+classid);

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            // DatabaseHelper.execute("delete from usr_code where class not in("+notinClass+")");
        } catch (Exception ee) {

        }
    }
    private void GetExg_Rate() {
        String sqlString = "select exg_rate,div_rate,short from Currency where currency=" + frmlogin.def_currency;
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    exg_rate = cursor.getInt(cursor.getColumnIndex("exg_rate"));
                    div_rate = cursor.getInt(cursor.getColumnIndex("div_rate"));
                    currencyshort = cursor.getString(cursor.getColumnIndex("short"));
                } while (cursor.moveToNext());

            }
            cursor.close();
        }
    }

    private void getSystemSetting() {
        Cursor cursor = DatabaseHelper.rawQuery("select Allow_Oustand,Allow_StockStatus,Allow_Sale,Allow_SaleOrder,all_users,allow_delete,allow_so_update from Posuser where userid=" + frmlogin.LoginUserid);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Allow_Oustand = cursor.getInt(cursor.getColumnIndex("Allow_Oustand")) == 1;
                    Allow_StockStatus = cursor.getInt(cursor.getColumnIndex("Allow_StockStatus")) == 1;
//                    All_Users=cursor.getInt(cursor.getColumnIndex("all_users"))==1?true:false;
//                    if(!(cursor.getInt(cursor.getColumnIndex("all_users"))==0&&cursor.getInt(cursor.getColumnIndex("allow_delete"))==0&&cursor.getInt(cursor.getColumnIndex("allow_so_update"))==0)){
//                        Allow_Sale=true;
//                    }//Added by KLM
//                    //Allow_Sale=cursor.getInt(cursor.getColumnIndex("all_users"))==0&&cursor.getInt(cursor.getColumnIndex("allow_delete"))==0&&cursor.getInt(cursor.getColumnIndex("allow_so_update"))==0?false:true;
                    Allow_Sale = cursor.getInt(cursor.getColumnIndex("Allow_Sale")) == 1;
                    Allow_SaleOrder = cursor.getInt(cursor.getColumnIndex("Allow_SaleOrder")) == 1;

                } while (cursor.moveToNext());
            }
        }
    }

    private void SetUI() {

        cardsale = findViewById(R.id.cardsale);
        cardsaleOrder = findViewById(R.id.cardsaleOrder);//added by YLT on [20-04-2020]
        cardsaleOrderlist = findViewById(R.id.cardsaleOrderlist);//added by YLT on [24-04-2020]
        cardStockstatuslist = findViewById(R.id.cardStockstatuslist);//added by YLT on [24-07-2020]
        cardoutstandlist = findViewById(R.id.cardOutstandlist);//added by YLT on [09-08-2020]
        cardsalelist = findViewById(R.id.cardsalelist);
        cardstock = findViewById(R.id.cardstock);
        cardlogout = findViewById(R.id.cardlogout);
        cardreturnin = findViewById(R.id.cardreturnin);
        cardreturninlist = findViewById(R.id.cardreturninlist);
        TextView shopName = findViewById(R.id.shopname);
        shopName.setText(GlobalClass.GetSystemSetting("title"));
        TextView txtUsername = findViewById(R.id.txt_username);
        txtUsername.setText(frmlogin.username);
        RelativeLayout headerLayout = findViewById(R.id.layout_header);

        cardsale.setOnClickListener(this);
        cardsaleOrder.setOnClickListener(this);//added by YLT on [20-04-2020]
        cardsaleOrderlist.setOnClickListener(this);//added by YLT on [24-04-2020]
        cardStockstatuslist.setOnClickListener(this);//added by YLT on [24-07-2020]
        cardoutstandlist.setOnClickListener(this);//added by YLT on [09-082020]
        cardsalelist.setOnClickListener(this);
        cardstock.setOnClickListener(this);
        cardlogout.setOnClickListener(this);
        cardreturnin.setOnClickListener(this);
        cardreturninlist.setOnClickListener(this);
        getdecimal();
        getclassview();
        getpic();
        getdefunittype();
        getCustid();
        GetAppSetting getAppSetting = new GetAppSetting("AndroidModule");
        String value = getAppSetting.getSetting_Value();
        if (value.equals("1")) {
            cardoutstandlist.setVisibility(View.GONE);
            cardStockstatuslist.setVisibility(View.GONE);
        }

        if (frmlogin.isTVMode) {
            cardsaleOrder.setVisibility(View.GONE);
            cardsaleOrderlist.setVisibility(View.GONE);
            cardstock.setVisibility(View.GONE);
            cardoutstandlist.setVisibility(View.GONE);
            cardStockstatuslist.setVisibility(View.GONE);
            cardreturnin.setVisibility(View.GONE);
            cardreturninlist.setVisibility(View.GONE);
            frmmain.withoutclass = "false";
            headerLayout.setVisibility(View.VISIBLE);
        } else {
            headerLayout.setVisibility(View.GONE);
        }

    }

    private void isusemulticurrency() {

        Cursor cursor = DatabaseHelper.rawQuery("select use_multicurrency from SystemSetting");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    use_multicurrency = cursor.getInt(cursor.getColumnIndex("use_multicurrency")) == 1;
                } while (cursor.moveToNext());
            }

        }
    }

    private void getCustid() {
        Cursor cursor = DatabaseHelper.rawQuery("select Max(customerid)as custc from Customer");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    CCount = cursor.getInt(cursor.getColumnIndex("custc"));
                } while (cursor.moveToNext());
            }
        }
    }


    private void getdefunittype() {
        Cursor cursor = DatabaseHelper.rawQuery("select  use_unit_type from menu_user where groupid=1 and subgroupid=1 and userid=" + frmlogin.LoginUserid);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    defunit = cursor.getInt(cursor.getColumnIndex("use_unit_type"));
                } while (cursor.moveToNext());
            }
            //test for default uint type
            //defunit=3;
        }
    }


    private void getpic() {

        Cursor cursor = DatabaseHelper.rawQuery("select use_pic from SystemSetting");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    use_pic = cursor.getInt(cursor.getColumnIndex("use_pic"));
                } while (cursor.moveToNext());
            }

        }
    }


    private void getclassview() {
        Cursor cursor = DatabaseHelper.rawQuery("select Setting_Value from AppSetting where Setting_Name='Withoutclass'");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    withoutclass = cursor.getString(cursor.getColumnIndex("Setting_Value"));
                } while (cursor.moveToNext());
            }

        }
    }

    private void getdecimal() {
        Cursor cursor = DatabaseHelper.rawQuery("select qty_decimal_places,price_decimal_places from SystemSetting");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    qty_places = cursor.getInt(cursor.getColumnIndex("qty_decimal_places"));
                    price_places = cursor.getInt(cursor.getColumnIndex("price_decimal_places"));
                } while (cursor.moveToNext());
            }

        }
    }


    public static boolean UserRight(int userid, int groupid, int subgroupid) {
        boolean allow = false;
        Cursor cursor = DatabaseHelper.rawQuery("SELECT count(userid) allow FROM menu_user WHERE userid=" + userid + " AND groupid = " + groupid + " AND subgroupid = " + subgroupid);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    allow = cursor.getInt(cursor.getColumnIndex("allow")) > 0;

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return allow;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cardsale:
                //if( Allow_Sale) {
                if (UserRight(frmlogin.LoginUserid, 1, 1)) {
                    if (frmlogin.isTVMode) {
                        if (frmlogin.UseOffline == 1) {
                            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        } else {
                            intent = new Intent(frmmain.this, sale_entry_tv.class);
                        }
                    } else {
                        intent = new Intent(frmmain.this, sale_entry.class);
                    }
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(this, "This User have no Permission for Sale", Toast.LENGTH_LONG).show();
                }
                break;
            //added by YLT on 20/04/2020
            case R.id.cardsaleOrder:
                if (frmlogin.UseOffline == 1) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    // if(Allow_SaleOrder) {
                    if (UserRight(frmlogin.LoginUserid, 1, 2)) {
                        intent = new Intent(frmmain.this, saleorder_entry.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "This User have no Permission for SaleOrder", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            //added by YLT on 20/04/2020

            //added by YLT on 24/04/2020
            case R.id.cardsaleOrderlist:
                if (frmlogin.UseOffline == 1) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    //if(Allow_SaleOrder) {
                    if (UserRight(frmlogin.LoginUserid, 1, 2)) {
                        intent = new Intent(frmmain.this, frmsaleorderlist.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "This User have no Permission for SaleOrderList", Toast.LENGTH_LONG).show();
                    }
                }
                break;


            //added by YLT on 24/04/2020

            //added by YLT on 27/04/2020
            case R.id.cardStockstatuslist:
                if (frmlogin.UseOffline == 1) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    if (Allow_StockStatus) {
                        intent = new Intent(frmmain.this, frmstockstatuslist.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "This User have no Permission for StockList", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            //added by YLT on 24/04/2020

            //added by YLT on 27/04/2020
            case R.id.cardOutstandlist:
                if (frmlogin.UseOffline == 1) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    if (Allow_Oustand) {
                        intent = new Intent(frmmain.this, frmoustandlist.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "This User have no Permission for OustandList", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            //added by YLT on 24/04/2020
            case R.id.cardsalelist:
                //if (Allow_Sale) {
                if (UserRight(frmlogin.LoginUserid, 1, 2)) {
                    intent = new Intent(frmmain.this, frmsalelist.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(this, "This User have no Permission for SaleList", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.cardlogout:
                onBackPressed();
                break;


            case R.id.cardstock:
                if (frmlogin.UseOffline == 1) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(frmmain.this, stock_balance.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.cardreturnin:
                if (frmlogin.UseOffline == 1) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    if (UserRight(frmlogin.LoginUserid, 1, 3)) {
                        intent = new Intent(frmmain.this, returnin_entry.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "This User have no Permission for SaleList", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.cardreturninlist:
                if (frmlogin.UseOffline == 1) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    if (UserRight(frmlogin.LoginUserid, 1, 3)) {
                        intent = new Intent(frmmain.this, frmreturninlist.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "This User have no Permission for SaleList", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    private void UnLockUser(int Userid) {
        String ip = sh_ip.getString("ip", "Localhost");
        String port = sh_port.getString("port", "80");
        String Device = frmlogin.Device_Name.replace(" ", "%20");
        String Url = "http://" + ip + "/api/DataSync/LockUser?userid=" + frmlogin.LoginUserid + "&hostname=" + Device + "&locked=" + false;

        requestQueue = Volley.newRequestQueue(this);

        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DatabaseHelper.execute("delete from Login_User where userid=" + frmlogin.LoginUserid);
            }
        };

        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(frmmain.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        StringRequest req = new StringRequest(Request.Method.GET, Url, listener, error);
        requestQueue.add(req);
    }

    @Override
    public void onBackPressed() {
        UnLockUser(frmlogin.LoginUserid);
        intent = new Intent(frmmain.this, frmlogin.class);
        startActivity(intent);
        finish();
    }
}

