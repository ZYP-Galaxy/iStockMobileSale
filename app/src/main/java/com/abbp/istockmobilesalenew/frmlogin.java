package com.abbp.istockmobilesalenew;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abbp.istockmobilesalenew.bluetoothprinter.BaseEnum;
import com.abbp.istockmobilesalenew.bluetoothprinter.BluetoothDeviceChooseDialog;
import com.abbp.istockmobilesalenew.bluetoothprinter.BluetoothPrinter;
import com.abbp.istockmobilesalenew.sunmiprinter.SunmiPrintHelper;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rt.printerlibrary.bean.BluetoothEdrConfigBean;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.factory.connect.BluetoothFactory;
import com.rt.printerlibrary.factory.connect.PIFactory;
import com.rt.printerlibrary.factory.printer.PrinterFactory;
import com.rt.printerlibrary.factory.printer.UniversalPrinterFactory;
import com.rt.printerlibrary.observer.PrinterObserver;
import com.rt.printerlibrary.observer.PrinterObserverManager;
import com.rt.printerlibrary.printer.RTPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.myatminsoe.mdetect.MDetect;

public class frmlogin extends AppCompatActivity implements View.OnClickListener, PrinterObserver {

    private Button btnlogin, btnexit, btnok, btncancel, btnrgok;
    private TextView btnconnect, btnposdown, btnRegister, txtTable, txtProgress, txtSetting, useredit, btnupload, btnBT;
    private EditText passedit, edtserver, edtport, edtkey;
    private boolean checkofflinedata = false;
    private ListView lv;
    AlertDialog dialog, msg, downloadAlert;
    AlertDialog.Builder builder;
    SharedPreferences sh_ip;
    SharedPreferences sh_port;
    SharedPreferences RegisterID;
    SharedPreferences bt_printer;
    private DatabaseHelper dataBaseHelper;
    private static final String DB_NAME = "iStock.db";
    ArrayList<String> TableName = new ArrayList<>();
    String jsonValue = "";
    String returnJson = "";
    String sqlString = "";
    String url;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    ArrayList<posuser> aryUsers = new ArrayList<>();
    public static int LoginUserid = -1;
    public static String username = "";
    public static int def_currency = 1;
    public static int det_locationid;
    public static int def_branchid;
    public static int def_payment;
    public static int Confirm_PrintVou;
    public static int use_oil;
    public static int allow_priceLevel;
    public static int select_location;
    public static int select_customer;
    public static int change_date;
    public static int change_price;
    public static int tax;
    public static int discount;
    public static int Allow_Over_Credit_Limit;
    public static int def_cashid;
    public static int hide_sale_summary;
    public static String Device_Name = "";
    public static String Cashier_Printer;
    public static int Cashier_PrinterType;
    public static String Font_Language;
    public static int allow_so_update;
    public static int languageid;
    SharedPreferences sh_printer, sh_ptype;
    public static ArrayList<String> Printers = new ArrayList<>();
    public static ArrayList<Printer_Type> ptype = new ArrayList<>();

    JSONArray jarr;
    ProgressDialog pb;
    String ip;
    String port;
    int count = 0;
    int progress = 0;
    AlertDialog showmsg;
    Context context;
    ProgressBar pbDownload;
    ArrayList<String> tableNames = new ArrayList<>();
    JSONArray data;
    private int printer_type_id = -1;
    public static int UseOffline;
    private ArrayList<offline_sale_head_main> shm = new ArrayList<>();
    private ArrayList<off_sale_det> sd = new ArrayList<>();
    private ArrayList<Sale_Head_Tmp_Mp> smp = new ArrayList<>();
    private ArrayList<SalesVoucher_Salesmen_Tmp> sm = new ArrayList<>();
    private String importjson;
    DateFormat dateFormat;
    private boolean isData;
    private boolean isOnline;

    //region BluetoothPrinter
    //Voucher 3 inches
    private final int REQUEST_ENABLE_BT = 101;
    TextView tv_device_selected;
    private Object configObj;
    private RTPrinter rtPrinter = null;
    private PrinterFactory printerFactory;
    private ArrayList<PrinterInterface> printerInterfaceArrayList = new ArrayList<>();
    private PrinterInterface curPrinterInterface = null;
    public static boolean isTVMode = false;
    //    public static double exg_rate,div_rate=0;
    public static String lastDownloadedDateTime = "2000-01-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MDetect.INSTANCE.init(this);
        setContentView(R.layout.frmlogin);
        isOnline = true;
        dateFormat = dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dataBaseHelper = DatabaseHelper.getInstance(this, DB_NAME);
        sh_ip = getSharedPreferences("ip", MODE_PRIVATE);
        sh_port = getSharedPreferences("port", MODE_PRIVATE);
        RegisterID = getSharedPreferences("register", MODE_PRIVATE);
        sh_printer = getSharedPreferences("printer", MODE_PRIVATE);
        sh_ptype = getSharedPreferences("ptype", MODE_PRIVATE);
        bt_printer = getSharedPreferences("bt", MODE_PRIVATE);
        SwitchCompat switchCompat = findViewById(R.id.switchBtn);

        isTVMode = sh_printer.getBoolean("isTVMode", false);
        lastDownloadedDateTime = sh_printer.getString("DateTime", "2000-01-01");
        Log.i("lastDownloadedDateTime", lastDownloadedDateTime);

        //added by KLM  for auto Detect if Device is TV or Tablet 25052022
        if (checkIsTelevision() || SunmiPrintHelper.getInstance().checkSunmiPrinter()) {
            switchCompat.setVisibility(View.GONE);
            isTVMode = true;
            SharedPreferences.Editor editor = sh_printer.edit();
            editor.putBoolean("isTVMode", isTVMode);
            editor.apply();
        }
        switchCompat.setChecked(isTVMode);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTVMode = switchCompat.isChecked();
                SharedPreferences.Editor editor = sh_printer.edit();
                editor.putBoolean("isTVMode", switchCompat.isChecked());
                editor.apply();
            }
        });
        setUI();
        Detect_Font();

        CheckConnection();

        context = this;
        isRegister();
        GetDeviceName();

        //region BluetoothPrinter
        BaseApplication.instance.setCurrentCmdType(BaseEnum.CMD_ESC);
        BaseApplication.instance.setCurrentConnectType(BaseEnum.CON_BLUETOOTH);
        printerFactory = new UniversalPrinterFactory();
        rtPrinter = printerFactory.create();
        PrinterObserverManager.getInstance().add(this);
        //added by KLM to calculate Currency 24062022

        //endregion


    }

    private boolean checkIsTelevision() {
        int uiMode = getApplicationContext().getResources().getConfiguration().uiMode;
        System.out.println("tablet device");
        return (uiMode & Configuration.UI_MODE_TYPE_MASK) == Configuration.UI_MODE_TYPE_TELEVISION;

    }


    private void Detect_Font() {
        frmlogin.Font_Language = MDetect.INSTANCE.isUnicode() ? "Unicode" : "Zawgyi";
    }
    //getting tablenames to download

    //set up UI
    private void setUI() {
        btnlogin = findViewById(R.id.login);
        btnexit = findViewById(R.id.exit);
        useredit = findViewById(R.id.useredit);
        passedit = findViewById(R.id.passedit);
        btnconnect = findViewById(R.id.btnconnect);
        btnRegister = findViewById(R.id.btnRegister);
        btnposdown = findViewById(R.id.btnposdown);
        btnupload = findViewById(R.id.btnUpload);
        txtSetting = findViewById(R.id.btnSetting);
        btnBT = findViewById(R.id.btnBT);
        btnBT.setVisibility(View.GONE);
        btnBT.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        btnexit.setOnClickListener(this);
        useredit.setOnClickListener(this);
        pb = new ProgressDialog(frmlogin.this, R.style.AlertDialogTheme);
        pb.setTitle("Data Downloading");
        pb.setMessage("Please a few minute..");
        pb.setCancelable(false);
        pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pb.setIndeterminate(true);
        btnconnect.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnposdown.setOnClickListener(this);
        btnupload.setOnClickListener(this);
        txtSetting.setOnClickListener(this);
    }

    private boolean isRegister() {
        String reg = RegisterID.getString("register", "empty");
        boolean check = reg.equals("empty") ? false : true;
        return check;
    }

    private void GetDeviceName() {
        try {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            if (myDevice != null) {
                Device_Name = myDevice.getName();
            } else {
                Device_Name = Build.MANUFACTURER + Build.MODEL;
            }
        } catch (Exception ee) {
            Device_Name = Build.MANUFACTURER + Build.MODEL;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSetting:
                try {

                    String printer_name = sh_printer.getString("printer", "");
                    String id = sh_ptype.getString("ptype", "-1");
                    printer_type_id = Integer.parseInt(id);
                    AlertDialog.Builder bd = new AlertDialog.Builder(this);
                    View view = getLayoutInflater().inflate(R.layout.devicesetting, null);
                    bd.setView(view);
                    Button btnPrinter = view.findViewById(R.id.btnPrinter);
                    CardView bluetoothprintersetting = view.findViewById(R.id.btn_printer_settings);
                    TextView connectedTo = view.findViewById(R.id.tv_printer_info);
                    CardView btnprintertest = view.findViewById(R.id.btn_printer_test);
                    if (!printer_name.trim().equals("")) {
                        btnPrinter.setText(printer_name);
                    } else {
                        btnPrinter.setText("Choose");
                    }
                    //added by KLM to Satisfy Bluetooth permission in android 12 21092022
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Log.i("SDK", Build.VERSION.SDK_INT + "");
                        checkPermissions();
                    }

                    if (isTVMode) {
                        LinearLayout layoutBTprinter = view.findViewById(R.id.layout_btprinter);
                        LinearLayout layoutSunmiprinter = view.findViewById(R.id.layout_sunmi_printer);
                        RelativeLayout rlPrinter = view.findViewById(R.id.rlprinter);
                        RelativeLayout rlPrinterType = view.findViewById(R.id.rlprintertype);
                        rlPrinter.setVisibility(View.GONE);
                        rlPrinterType.setVisibility(View.GONE);
                        if (SunmiPrintHelper.getInstance().checkSunmiPrinter()) {
                            layoutSunmiprinter.setVisibility(View.VISIBLE);

                            layoutBTprinter.setVisibility(View.GONE);

                        } else {
                            layoutSunmiprinter.setVisibility(View.GONE);

                            layoutBTprinter.setVisibility(View.VISIBLE);
                        }

                        TextView sunmi_printerstatus = view.findViewById(R.id.txt_sunmi_printerstatus);
                        sunmi_printerstatus.setText(String.format("Sunmi POS Printer (%s)", SunmiPrintHelper.getInstance().showPrinterStatus(frmlogin.this)));

                        CardView btnSunmiPrintStatus = view.findViewById(R.id.btn_sunmi_printerstatus);
                        btnSunmiPrintStatus.setOnClickListener(v1 -> {
                            sunmi_printerstatus.setText(String.format("Sunmi POS Printer (%s)", SunmiPrintHelper.getInstance().showPrinterStatus(frmlogin.this)));
                        });

                        CardView btnSunmiPrintTest = view.findViewById(R.id.btn_sunmi_printertest);
                        btnSunmiPrintTest.setOnClickListener(v1 -> {
//                        SunmiPrintHelper.getInstance().printText("Galaxy Software\niStock Mobile TV Sale\n\n\nSunmi Printer Testing!",
//                                36, false, false, null);
                            SunmiPrintHelper.getInstance().printTest(context);
                        });

                        CardView btnSunmiPrintFeed = view.findViewById(R.id.btn_sunmi_printerfeed);
                        btnSunmiPrintFeed.setOnClickListener(v1 -> {
                            SunmiPrintHelper.getInstance().feedPaper();
                        });

                        CardView btnSunmiPrintCut = view.findViewById(R.id.btn_sunmi_printercut);
                        btnSunmiPrintCut.setOnClickListener(v1 -> {
                            SunmiPrintHelper.getInstance().cutpaper();
                        });
                    }
                    //endregion

                    bluetoothprintersetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPrinterList(connectedTo, btnprintertest);
                        }
                    });
                    getSavedPrinter(connectedTo);
                    btnPrinter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(frmlogin.this);
                            View view = getLayoutInflater().inflate(R.layout.showposuser, null);
                            builder.setView(view);
                            lv = (ListView) view.findViewById(R.id.lsvposuer);
                            Printer_Type_Adpater ad = new Printer_Type_Adpater(Printers, frmlogin.this);
                            lv.setAdapter(ad);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    btnPrinter.setText(Printers.get(position).toString());
                                    if (btnPrinter.getText().toString().trim().equals("")) {
                                        btnPrinter.setText("Choose");

                                    }
                                    msg.dismiss();

                                }
                            });
                            msg = builder.create();
                            msg.show();

                        }
                    });
                    Button btnPrinterType = view.findViewById(R.id.btnType);
                    if (printer_type_id != -1) {
                        btnPrinterType.setText(ptype.get((printer_type_id)).getName());
                    } else {
                        btnPrinterType.setText("Choose");
                    }
                    btnPrinterType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(frmlogin.this);
                            View view = getLayoutInflater().inflate(R.layout.showposuser, null);
                            builder.setView(view);
                            lv = (ListView) view.findViewById(R.id.lsvposuer);
                            Printer_Type_Adpater ad = new Printer_Type_Adpater(frmlogin.this, ptype);
                            lv.setAdapter(ad);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    btnPrinterType.setText(ptype.get(position).getName().toString());
                                    if (btnPrinterType.getText().toString().trim().equals("")) {
                                        btnPrinterType.setText("Choose");
                                    }

                                    printer_type_id = ptype.get(position).getPrinter_type_id();
                                    msg.dismiss();
                                }
                            });
                            msg = builder.create();
                            msg.show();
                        }
                    });
                    ImageButton ImgSave = view.findViewById(R.id.imgSave);
                    ImgSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SharedPreferences.Editor editor = sh_printer.edit();
                            editor.remove("printer");
                            editor.commit();

                            editor = sh_printer.edit();
                            editor.putString("printer", btnPrinter.getText().toString().trim());
                            editor.commit();

                            SharedPreferences.Editor editor1 = sh_ptype.edit();
                            editor1.remove("ptype");
                            editor1.commit();

                            editor1 = sh_ptype.edit();
                            editor1.putString("ptype", String.valueOf(printer_type_id));
                            editor1.commit();

                            dialog.dismiss();
                        }
                    });

                    //region BluetoothPrinter
                    CardView btPrinterSetting = view.findViewById(R.id.btn_btprinter_settings);
                    CardView btnBTprintertest = view.findViewById(R.id.btn_btprinter_test);
                    tv_device_selected = view.findViewById(R.id.tv_device_selected);

                    btPrinterSetting.setOnClickListener(v1 -> {
                        showConnectDialog();
                    });

                    btnBTprintertest.setOnClickListener(v1 -> {
                        try {
                            BluetoothPrinter bluetoothPrinter = new BluetoothPrinter(frmlogin.this, rtPrinter);
                            if (isInConnectList(configObj)) {
                                final String testPrintString = "Printer is connected!\n";
                                String callback = bluetoothPrinter.escTextPrint(testPrintString, 30);
                                if (!callback.isEmpty()) {
                                    showToast(callback);
                                }
                            } else {
                                showToast("Please connect to Printer!");
                            }

                        } catch (UnsupportedEncodingException e) {
                            showToast(e.getMessage());
                            e.printStackTrace();
                        }
                    });

                    //endregion

                    dialog = bd.create();
                    dialog.show();
                } catch (Exception ee) {
                    String s = ee.getMessage();


                }
                break;
            case R.id.btnconnect:
                ip = sh_ip.getString("ip", "empty");
                port = sh_port.getString("port", "empty");
                showIpBox(ip, port);
                break;
            case R.id.btnUpload:
                checkofflinedata = false;
                pb.setTitle("Importing Data");
                pb.setMessage("Please Wait a Few minute");
                UploadData();
                break;
            case R.id.btnposdown:
                if (isRegister()) {
                    GetTableNames();
                    GetDownloading();
                } else {

                    new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                            .setTitle("iStock")
                            .setMessage("Please, Register Before Downloading!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();

                }
                break;
            case R.id.login:
                SignIn();
                break;
            case R.id.exit:
                finish();
                break;
            case R.id.useredit:
                showPosuser();
                break;
            case R.id.btnRegister:
                final String id = RegisterID.getString("register", "0");
                if (isRegister()) {


                    new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                            .setTitle("iStock")
                            .setMessage("Your tablet has been already registerd!Do you register next again?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setRegister(id);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();

                } else {
                    setRegister(id);
                }
                break;
        }


    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private void checkPermissions() {
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }

    private void getSavedPrinter(TextView connectedTo) {
        BluetoothDevice connectedPrinter = Printama.with(this).getConnectedPrinter();
        if (connectedPrinter != null) {
            String text = "Connected to : " + connectedPrinter.getName();
            connectedTo.setText(text);
        }
    }

    private void showPrinterList(TextView connectedTo, CardView btnprintertest) {
        Printama.showPrinterList(this, R.color.colorBlue, printerName -> {
            //Toast.makeText(this, printerName, Toast.LENGTH_SHORT).show();
            //TextView connectedTo = findViewById(R.id.tv_printer_info);
            String text = "Connected to : " + printerName;
            connectedTo.setText(text);
            if (!printerName.contains("failed")) {
                btnprintertest.setVisibility(View.VISIBLE);
                btnprintertest.setOnClickListener(v -> testPrinter());
            }
        });
    }

    private void testPrinter() {
        String s = "Your Printer is Connected.";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 5, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color
        Printama.with(this).printTest(ss1.toString());
    }


    private void UploadData() {
        try {

            isData = true;
            getSaleHeadMain();
            getSaleDet();
            getSaleHeadTmpMP();
            getsalemen();
            if (isData) {
                importjson = GetJson();
                ImportingData();
            } else {
                AlertDialog.Builder bd = new AlertDialog.Builder(frmlogin.this);
                bd.setMessage("No Data To Upload");
                bd.setTitle("iStock");
                bd.setCancelable(false);
                bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        msg.dismiss();
                    }
                });
                msg = bd.create();
                msg.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        msg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    }
                });
                msg.show();
            }

        } catch (Exception ee) {
            Toast.makeText(frmlogin.this, ee.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void getsalemen() {
        String sqlString = "select * from SalesVoucher_Salesmen_Tmp";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    long Sales_TranID = cursor.getLong(cursor.getColumnIndex("Sales_TranID"));
                    long Salesmen_ID = cursor.getLong(cursor.getColumnIndex("Salesmen_ID"));
                    int rmt_copy = cursor.getInt(cursor.getColumnIndex("rmt_copy"));
                    int userid = cursor.getInt(cursor.getColumnIndex("userid"));
                    sm.add(new SalesVoucher_Salesmen_Tmp(Sales_TranID, Salesmen_ID, rmt_copy, userid));


                } while (cursor.moveToNext());
            }

        }
        cursor.close();
    }

    private void getSaleHeadTmpMP() {
        String sqlString = "select * from Sale_Head_Tmp_Mp";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    long mpTranid = cursor.getLong(cursor.getColumnIndex("tranid"));
                    int currency = cursor.getInt(cursor.getColumnIndex("currency"));
                    double exg_rate = cursor.getDouble(cursor.getColumnIndex("exg_rate"));
                    double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                    double change = cursor.getDouble(cursor.getColumnIndex("change"));
                    smp.add(new Sale_Head_Tmp_Mp(mpTranid, currency, exg_rate, amount, change));

                } while (cursor.moveToNext());
            }

        }
        cursor.close();
    }

    private void ImportingData() {
        String ip = sh_ip.getString("ip", "empty");
        String port = sh_port.getString("port", "empty");
//        String sqlUrl = "http://" + ip + ":" + port + "/api/DataSync/SaveData?userid=" + frmlogin.LoginUserid;
        String sqlUrl = "http://" + ip + "/api/DataSync/UploadData?userid=" + frmlogin.LoginUserid;
        new ImportingData().execute(sqlUrl);
    }

    private String GetJson() {
        String json = "";
        try {

            Gson gson = new Gson();
            String shmjson = gson.toJson(shm);
            String sdjson = gson.toJson(sd);
            String smpjson = gson.toJson(smp);
            String smjson = gson.toJson(sm);
            json = "[{\"data\":";
            json = json + "[{\"sale_head_main\":" + shmjson + "," +
                    "\"sale_det\":" + sdjson + "," +
                    "\"SalesVoucher_Salesmen_Tmp\":" + smjson + "," +
                    "\"sale_head_tmp_mp\":" + smpjson;
            json = json + "}]}]";

        } catch (Exception ee) {

        }

        return json;
    }

    private void getSaleHeadMain() {


        String sqlString = "select * from Sale_Head_Main where tranid in(select tranid from Sale_Det) order by date asc,tranid desc";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    long tranid = cursor.getLong(cursor.getColumnIndex("tranid"));
                    int userid = cursor.getInt(cursor.getColumnIndex("userid"));
                    String vou = cursor.getString(cursor.getColumnIndex("docid"));
                    String voudate = cursor.getString(cursor.getColumnIndex("date"));
                    try {
                        voudate = new SimpleDateFormat("yyyy-MM-dd").format(dateFormat.parse(String.valueOf(voudate)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String inv_no = cursor.getString(cursor.getColumnIndex("invoice_no"));
//                    inv_no=inv_no.equals("")?"":"N'"+inv_no+"'";
                    Boolean deliver = (cursor.getInt(cursor.getColumnIndex("deliver")) == 1 ? true : false);
                    String remark = cursor.getString(cursor.getColumnIndex("Remark"));
//                    remark=remark.equals("")?"":"N'"+remark+"'";
                    long locationid = cursor.getLong(cursor.getColumnIndex("locationid"));
                    long customerid = cursor.getLong(cursor.getColumnIndex("customerid"));
                    int cashid = cursor.getInt(cursor.getColumnIndex("cash_id"));
                    int pay_type = cursor.getInt(cursor.getColumnIndex("pay_type"));
                    int due_indays = cursor.getInt(cursor.getColumnIndex("due_indays"));
                    int currency = cursor.getInt(cursor.getColumnIndex("currency"));
                    double discount = cursor.getDouble(cursor.getColumnIndex("discount"));
                    double paid_amount = cursor.getDouble(cursor.getColumnIndex("paid_amount"));
                    double invoice_amount = cursor.getDouble(cursor.getColumnIndex("invoice_amount"));
                    double invoice_qty = cursor.getDouble(cursor.getColumnIndex("invoice_qty"));
                    double foc_amount = cursor.getDouble(cursor.getColumnIndex("foc_amount"));
                    double itemdis_amount = cursor.getDouble(cursor.getColumnIndex("itemdis_amount"));
                    double discount_per = cursor.getDouble(cursor.getColumnIndex("discount_per"));
                    double tax_amount = cursor.getDouble(cursor.getColumnIndex("tax_amount"));
                    double tax_per = cursor.getDouble(cursor.getColumnIndex("tax_percent"));
                    double exg_rate = cursor.getDouble(cursor.getColumnIndex("exg_rate"));
                    double net_amount = cursor.getDouble(cursor.getColumnIndex("net_amount"));
                    shm.add(new offline_sale_head_main(tranid, userid,
                            vou, voudate, inv_no, deliver, remark,
                            locationid, customerid, cashid,
                            pay_type, due_indays, currency,
                            discount, paid_amount, invoice_amount, invoice_qty,
                            foc_amount, itemdis_amount, discount_per,
                            tax_amount, tax_per, net_amount, exg_rate));

                } while (cursor.moveToNext());

            }

        } else {
            isData = false;
        }
        cursor.close();


    }

    private void getSaleDet() {
        String sqlString = "select * from Sale_Det order by date asc,tranid desc";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    long tranid = cursor.getLong(cursor.getColumnIndex("tranid"));
                    int sr = cursor.getInt(cursor.getColumnIndex("sr"));
                    int srno = cursor.getInt(cursor.getColumnIndex("srno"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    double unit_qty = cursor.getDouble(cursor.getColumnIndex("unit_qty"));

                    double qty = cursor.getDouble(cursor.getColumnIndex("qty"));
                    double sale_price = cursor.getDouble(cursor.getColumnIndex("sale_price"));
                    double dis_price = cursor.getDouble(cursor.getColumnIndex("dis_price"));
                    long dis_type = cursor.getLong(cursor.getColumnIndex("dis_type"));
                    double dis_percent = cursor.getDouble(cursor.getColumnIndex("dis_percent"));
                    String remark = cursor.getString(cursor.getColumnIndex("remark"));
                    int unit_type = cursor.getInt(cursor.getColumnIndex("unit_type"));
                    long code = cursor.getLong(cursor.getColumnIndex("code"));
                    String Pricelevel = cursor.getString(cursor.getColumnIndex("PriceLevel"));
                    double sqty = cursor.getDouble(cursor.getColumnIndex("SQTY"));
                    double sprice = cursor.getDouble(cursor.getColumnIndex("SPrice"));
                    int org_curr = cursor.getInt(cursor.getColumnIndex("org_curr"));
                    double org_exgrate = cursor.getDouble(cursor.getColumnIndex("org_exgrate"));
                    sd.add(new off_sale_det(tranid, sr, srno, date, unit_qty, qty, unit_type, sale_price, dis_price, dis_type, dis_percent, remark, code, Pricelevel, sqty, sprice, org_curr, org_exgrate));

                } while (cursor.moveToNext());
            }

        } else {
            isData = false;
        }
        cursor.close();

    }


    private void setRegister(final String id) {
        AlertDialog.Builder pass = new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme);
        View password = getLayoutInflater().inflate(R.layout.registerbox, null);
        pass.setView(password);
        final EditText etdpass = password.findViewById(R.id.edtkey);
        Button btn = password.findViewById(R.id.btnrgok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                int PlusResult = day + month + year;

                String divResult = String.valueOf(day) + String.valueOf(month) + String.valueOf(year);
                String Result = String.valueOf(Long.parseLong(divResult) / PlusResult);
                if (etdpass.getText().toString().contentEquals(Result)) {
                    String Device_ID = Build.ID;
                    String Build_Number = Build.DISPLAY;
                    String sqlstring = "RegisterID=" + id + "&ID=" + Device_ID + "&Build=" + Build_Number + "&Name=" + Device_Name;
                    Register(sqlstring);
                    showmsg.dismiss();
                }
            }
        });
        showmsg = pass.create();
        showmsg.show();
    }

    private void Register(String sqlString) {
        ip = sh_ip.getString("ip", "empty");
        port = sh_port.getString("port", "empty");
        try {
            sqlString = URLEncoder.encode(sqlString, "UTF-8").replace("+", "%20")
                    .replace("%26", "&").replace("%3D", "=")
                    .replace("%2C", ",")
            ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String url = "http://" + ip + ":" + port + "/api/DataSync/GetData?" + sqlString + "&register=true";
        String url = "http://" + ip + "/api/DataSync/Register?" + sqlString + "&register=true";
        RequestQueue request = Volley.newRequestQueue(context);
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                lastDownloadedDateTime = "2000-01-01";
                ClearData();

                final String[] result = response.toString().split("/");
                AlertDialog.Builder bd = new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme);
                bd.setMessage(result[0]);
                bd.setTitle("iStock");

                bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!result[1].isEmpty()) {

                            SharedPreferences.Editor editor = RegisterID.edit();
                            editor.remove("register");
                            editor.commit();

                            editor = RegisterID.edit();
                            editor.putString("register", result[1]);
                            editor.commit();
                            isRegister();
                            showmsg.dismiss();
                        }
                        dialog.dismiss();
                        showmsg.dismiss();

                    }
                });
                dialog = bd.create();
                dialog.show();
            }
        };

        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bd = new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme);
                bd.setMessage("Process is Fail!Check your Network Connection");
                bd.setTitle("iStock");

                bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showmsg.dismiss();
                    }
                });
                dialog = bd.create();
                dialog.show();
            }
        };
        StringRequest req = new StringRequest(Request.Method.GET, url, listener, error);
        request.add(req);
    }

    private void GetTableNames() {
        if (tableNames.size() > 0) tableNames.clear();
        tableNames.add("Posuser");
        tableNames.add("Customer");
        tableNames.add("Location");
        tableNames.add("Usr_Code");
        tableNames.add("Pay_Type");
        tableNames.add("Dis_Type");
        tableNames.add("System Setting");
        tableNames.add("AppSetting");
        tableNames.add("Salesmen");
        tableNames.add("Alias_Code");
        tableNames.add("usr_code_img");
        tableNames.add("menu_user");
        tableNames.add("S_Sprice");
        tableNames.add("Pdis");
        tableNames.add("cash");
        tableNames.add("currency");
        tableNames.add("Dictionary");
        tableNames.add("userclass");
        tableNames.add("delivery_setup");//Added by KNO for Delivery (24-10-2022)
        tableNames.add("Salesmen_Type");//Added by KNO for Delivery (24-10-2022)
    }

    private void GetDownloading() {
        checkofflinedata = false;
        if (CheckOfflineData()) {
            checkofflinedata = true;
            AlertDialog.Builder ch = new AlertDialog.Builder(frmlogin.this);
            ch.setTitle("iStock");
            ch.setMessage("Do you want to upload offline sale data?");
            ch.setCancelable(false);
            ch.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    msg.dismiss();
                    UploadData();

                }
            });
            ch.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    msg.dismiss();
                    GetData();
                }
            });
            msg = ch.create();
            msg.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    msg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    msg.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                }
            });
            msg.show();
        } else {
            GetData();

        }


    }

    private void ClearData() {
        sqlString = "delete from Customer";
        DatabaseHelper.execute(sqlString);
        sqlString = "delete from usr_code";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Posuser";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Location";
        DatabaseHelper.execute(sqlString);


        sqlString = "delete from SystemSetting";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Payment_Type";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Dis_Type";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from AppSetting";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Salesmen";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Alias_Code";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from usr_code_img";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from menu_user";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from S_Sprice";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Sale_Head_Main";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Sale_Det";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Tranid";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Sale_Head_Tmp_Mp";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from SalesVoucher_Salesmen_Tmp";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from cash";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Pdis";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from S_SalePrice";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Currency";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Dictionary";//added by KNO for language 22-08-2022
        DatabaseHelper.execute(sqlString);

    }

    private void ResetData() {
//        sqlString = "delete from Customer";
//        DatabaseHelper.execute(sqlString);
        //sqlString = "delete from usr_code";
        //DatabaseHelper.execute(sqlString);

        sqlString = "delete from Posuser";
        DatabaseHelper.execute(sqlString);

//        sqlString = "delete from Location";
//        DatabaseHelper.execute(sqlString);


//        sqlString = "delete from SystemSetting";
//        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Payment_Type";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Dis_Type";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from AppSetting";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Salesmen";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Alias_Code";
        DatabaseHelper.execute(sqlString);

//        sqlString = "delete from usr_code_img";
//        DatabaseHelper.execute(sqlString);

        sqlString = "delete from menu_user";
        DatabaseHelper.execute(sqlString);

//        sqlString = "delete from S_Sprice";
//        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Sale_Head_Main";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Sale_Det";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Tranid";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Sale_Head_Tmp_Mp";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from SalesVoucher_Salesmen_Tmp";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from cash";
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Pdis";
        DatabaseHelper.execute(sqlString);

//        sqlString = "delete from S_SalePrice";
//        DatabaseHelper.execute(sqlString);

//        sqlString = "delete from Currency";
//        DatabaseHelper.execute(sqlString);

        sqlString = "delete from userclass"; //Added by KNO for UserClass
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from delivery_setup"; //Added by KNO for Delivery (24-10-2022)
        DatabaseHelper.execute(sqlString);

        sqlString = "delete from Salesmen_Type"; //Added by KNO for Delivery (24-10-2022)
        DatabaseHelper.execute(sqlString);
    }

    private boolean CheckOfflineData() {
        boolean check = false;
        String sqlString = "select * from Sale_Head_Main where tranid in(select tranid from Sale_Det)";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            check = true;
        }
        return check;
    }

    private void GetData() {
        try {

            ResetData();
            AlertDialog.Builder bdProgress = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            View view = getLayoutInflater().inflate(R.layout.downloadprocess, null);
            pbDownload = view.findViewById(R.id.progressDownload);
            txtProgress = view.findViewById(R.id.txtProgress);
            txtTable = view.findViewById(R.id.txtTable);


            txtProgress.setText("0/" + tableNames.size());
            //  txtProgress.setText("0/0");
            pbDownload.setMax(tableNames.size());
            bdProgress.setCancelable(false);
            bdProgress.setView(view);
            downloadAlert = bdProgress.create();
            context = this;
            downloadAlert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    ip = sh_ip.getString("ip", "empty");
                    port = sh_port.getString("port", "empty");
//                    String url = "http://" + ip + ":" + port + "/api/DataSync/GetData?download=true&language=" + frmlogin.Font_Language;
                    String url = "http://" + ip + "/api/DataSync/DownloadData?download=true&language=" + frmlogin.Font_Language + "&lastDownloadedDatetime=" + lastDownloadedDateTime;
                    Log.i("LOGIN", url);
                    RequestQueue request = Volley.newRequestQueue(context);
                    final Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONArray jarr = null;
                            try {
                                jarr = new JSONArray(response);
                                data = jarr.getJSONObject(0).getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        for (progress = 0; progress < tableNames.size(); progress++) {

                                            insertingData(tableNames.get(progress), progress);
                                            pbDownload.setProgress(progress + 1);
                                            try {
                                                Thread.sleep(5);
                                            } catch (Exception ex) {
                                            }
                                        }
                                        dialog.dismiss();
                                    } catch (Exception ee) {
                                        Toast.makeText(context, ee.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            }).start();

                        }

                    };

                    final Response.ErrorListener error = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(frmlogin.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    };
                    StringRequest req = new StringRequest(Request.Method.GET, url, listener, error);
                    req.setRetryPolicy(new DefaultRetryPolicy(100000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    request.add(req);


                }

            });
            downloadAlert.show();
        } catch (Exception ee) {
            Toast.makeText(this, ee.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void insertingData(String table, final int progress) {
        try {

            switch (table) {
                //Added by KNO for UserClass
                case "userclass":
                    JSONArray userclass = null;
                    userclass = data.getJSONObject(0).getJSONArray("userclass");
                    for (int uclass = 0; uclass < userclass.length(); uclass++) {
                        JSONObject userclassobj = userclass.getJSONObject(uclass);
                        int userid = userclassobj.getInt("userID");
                        int classid = userclassobj.getInt("class");
                        String sql = "insert into userclass(userid,class)" +
                                " values(" + userid + "," + classid + ")";
                        DatabaseHelper.execute(sql);

                    }
                    break;

                case "cash":
                    JSONArray cash = null;
                    cash = data.getJSONObject(0).getJSONArray("cash");
                    for (int pdiscount = 0; pdiscount < cash.length(); pdiscount++) {
                        JSONObject cashobj = cash.getJSONObject(pdiscount);
                        int cash_id = cashobj.getInt("cash_id");
                        String name = cashobj.getString("name");
                        int userid = cashobj.getInt("userid");
                        String sql = "insert into cash(cash_id,name,userid)" +
                                " values(" + cash_id + ",'" + name + "'," + userid + ")";
                        DatabaseHelper.execute(sql);

                    }
                    break;

                case "Pdis":
                    JSONArray pdis = null;
                    pdis = data.getJSONObject(0).getJSONArray("Pdis");
                    for (int pdiscount = 0; pdiscount < pdis.length(); pdiscount++) {
                        JSONObject sspriceobj = pdis.getJSONObject(pdiscount);
                        long code = sspriceobj.getLong("code");
                        long locationid = sspriceobj.getLong("locationid");
                        int unit_type = sspriceobj.getInt("unit_type");
                        double discount = sspriceobj.optDouble("discount", 0);
                        double discount1 = sspriceobj.optDouble("discount1", 0);
                        double discount2 = sspriceobj.optDouble("discount2", 0);
                        double discount3 = sspriceobj.optDouble("discount3", 0);
                        double dis_price = sspriceobj.optDouble("dis_price", 0);
                        double dis_price1 = sspriceobj.optDouble("dis_price1", 0);
                        double dis_price2 = sspriceobj.optDouble("dis_price2", 0);
                        double dis_price3 = sspriceobj.optDouble("dis_price3", 0);
                        String sql = "insert into Pdis(code,locationid,unit_type,discount,discount1,discount2,discount3,dis_price,dis_price1,dis_price2,dis_price3)" +
                                " values(" + code + "," + locationid + "," + unit_type + "," + discount + "," + discount1 + "," + discount2 + "," + discount3 + "," + dis_price + "," + dis_price1 + "," + dis_price2 + "," + dis_price3 + ")";
                        DatabaseHelper.execute(sql);
                    }
                    break;

                case "S_Sprice":
                    JSONArray ssprice = null;
                    ssprice = data.getJSONObject(0).getJSONArray("S_Sprice");
                    for (int sspricecount = 0; sspricecount < ssprice.length(); sspricecount++) {
                        JSONObject sspriceobj = ssprice.getJSONObject(sspricecount);
                        long code = sspriceobj.getLong("code");
                        sqlString = "delete from S_SalePrice where code=" + code;
                        DatabaseHelper.execute(sqlString);
                        if (sspriceobj.getBoolean("inactive")) {

                            continue;
                        }
                        String usr_code = sspriceobj.getString("usr_code");
                        int unit_type = sspriceobj.getInt("unit_type");
                        double min_qty = sspriceobj.getDouble("min_qty");
                        double max_qty = sspriceobj.getDouble("max_qty");
                        double price_level = sspriceobj.getDouble("price_level");
                        double Sale_Price = sspriceobj.getDouble("Sale_Price");
                        String sql = "insert into S_SalePrice(code,usr_code,unit_type,min_qty,max_qty,price_level,Sale_Price)" +
                                " values(" + code + ",'" + usr_code + "'," + unit_type + "," + min_qty + "," + max_qty + "," + price_level + "," + Sale_Price + ")";
                        DatabaseHelper.execute(sql);
                    }
                    break;

                case "AppSetting":
                    JSONArray app = null;
                    app = data.getJSONObject(0).getJSONArray("AppSetting");
                    for (int appcount = 0; appcount < app.length(); appcount++) {
                        JSONObject appobj = app.getJSONObject(appcount);
                        int Setting_No = appobj.getInt("Setting_No");
                        String Setting_Name = appobj.getString("Setting_Name");
                        String Setting_Value = appobj.getString("Setting_Value");
                        String Remark = appobj.getString("Remark");
                        String sql = "insert into AppSetting(Setting_No,Setting_Name,Setting_Value,Remark)" +
                                " values(" + Setting_No + ",'" + Setting_Name + "','" + Setting_Value + "','" + Remark + "')";
                        DatabaseHelper.execute(sql);
                        GetAppSetting getAppSetting = new GetAppSetting("lastDownloadedDatetime");
                        SharedPreferences.Editor editor = sh_printer.edit();
                        editor.putString("DateTime", getAppSetting.getSetting_Value());
                        editor.apply();

                    }
                    break;
                case "Posuser":
                    JSONArray user = null;
                    user = data.getJSONObject(0).getJSONArray("posuser");
                    for (int usercount = 0; usercount < user.length(); usercount++) {
                        JSONObject postobj = user.getJSONObject(usercount);
                        int userid = postobj.getInt("userid");
                        String name = postobj.optString("name", "null");
                        String shortdes = postobj.optString("short", "null");
                        String knockcode = postobj.optString("knockcode", "null");
                        long defloc = postobj.optInt("def_locationid", 1);
                        int defbrid = postobj.optInt("def_branchid", 0);
                        int Confirm_PrintVou = postobj.optBoolean("Confirm_PrintVou", false) == true ? 1 : 0;
                        int saleprice_level = postobj.optInt("saleprice_level", 0);
                        int select_location = postobj.optBoolean("select_location", false) == true ? 1 : 0;
                        int select_customer = postobj.optBoolean("select_customer", false) == true ? 1 : 0;
                        ;
                        int change_price = postobj.optBoolean("change_price", false) == true ? 1 : 0;
                        int change_date = postobj.optBoolean("change_date", false) == true ? 1 : 0;
                        int allow_pricelevel = postobj.optBoolean("allow_pricelevel", false) == true ? 1 : 0;
                        int tax = postobj.optBoolean("tax", false) == true ? 1 : 0;
                        int discount = postobj.optBoolean("discount", false) == true ? 1 : 0;
                        int def_payment = postobj.optInt("def_payment", 1);
                        int Allow_Over_Credit_Limit = postobj.optBoolean("Allow_Over_Credit_Limit", false) == true ? 1 : 0;
                        int def_cashid = postobj.optInt("def_cashid", 1);
                        String Cashier_Printer = postobj.optString("Cashier_Printer", "null");
                        int Cashier_PrinterType = postobj.optInt("Cashier_PrinterType", -1);
                        int Allow_Oustand = postobj.optBoolean("Allow_Oustand", false) == true ? 1 : 0;
                        int Allow_StockStatus = postobj.optBoolean("Allow_StockStatus", false) == true ? 1 : 0;

                        int Allow_Sale = postobj.optBoolean("Allow_Sale", false) == true ? 1 : 0;
                        int Allow_SaleOrder = postobj.optBoolean("Allow_SaleOrder", false) == true ? 1 : 0;
                        int use_offline = postobj.optBoolean("use_offline", false) == true ? 1 : 0;
                        int so_update = postobj.optBoolean("allow_so_update", false) == true ? 1 : 0;
                        int all_users = postobj.optBoolean("all_users", false) == true ? 1 : 0;
                        int allow_delete = postobj.optBoolean("allow_delete", false) == true ? 1 : 0;
                        int use_petrol = postobj.optBoolean("use_oil", false) == true ? 1 : 0;
                        int hide_sale_summary = postobj.optBoolean("hide_sale_summary", false) == true ? 1 : 0;
                        int def_currency = postobj.optInt("def_currency", 1);//added by KLM for currency 24062022
                        int languageid = postobj.optInt("languageid", 1); // added by KNO for language 22-08-2022
                        // int Allow_Oustand=postobj.optInt("Allow_Oustand", 1);
                        // int Allow_StockStatus=postobj.optInt("Allow_StockStatus", 1);
                        sqlString = "insert into Posuser(userid,name,knockcode,Confirm_PrintVou,def_locationid,short,saleprice_level,select_location,select_customer," +
                                " change_price,change_date,tax,discount,allow_pricelevel,def_payment,Allow_Over_Credit_Limit,def_cashid,Cashier_Printer,Cashier_PrinterType,Allow_Oustand,Allow_StockStatus,Allow_Sale,Allow_SaleOrder,use_offline, allow_so_update,all_users,allow_delete,use_oil,def_branchid,hide_sale_summary,def_currency,languageid)" +
                                " values(" + userid + ",'" + name + "','" + knockcode + "'," + Confirm_PrintVou + "," + defloc + ",'" + shortdes + "'," +
                                saleprice_level + "," + select_location + "," + select_customer + "," + change_price + "," + change_date + "," + tax + "," + discount + "," + allow_pricelevel + "," + def_payment + ","
                                + Allow_Over_Credit_Limit + "," + def_cashid + ",'" + Cashier_Printer + "'," + Cashier_PrinterType + "," + Allow_Oustand + ","
                                + Allow_StockStatus + "," + Allow_Sale + "," + Allow_SaleOrder + "," + use_offline + "," + so_update + "," + all_users + "," + allow_delete + "," + use_petrol + "," + defbrid + "," + hide_sale_summary + "," + def_currency + "," + languageid + ")";

                        DatabaseHelper.execute(sqlString);

                    }
                    break;
                case "Customer":
                    JSONArray cust = data.getJSONObject(0).getJSONArray("customer");

                    for (int custcount = 0; custcount < cust.length(); custcount++) {
                        JSONObject custobj = cust.getJSONObject(custcount);
                        long customerid = custobj.getLong("customerid");
                        sqlString = "delete from Customer where customerid=" + customerid;
                        DatabaseHelper.execute(sqlString);
                        if (custobj.getBoolean("inactive")) {

                            continue;
                        }
                        String customername = custobj.optString("customername", "null");
                        String customercode = custobj.optString("customercode", "null");
                        int credit = custobj.optBoolean("credit", false) == true ? 1 : 0;
                        long townshipid = custobj.getLong("Townshipid");
                        String townshipname = custobj.optString("Townshipname", "null");
                        String townshipcode = custobj.optString("TownshipCode", "null");
                        long CustGroupCodeid = custobj.getLong("CustGroupID");
                        String CustGroupCodeName = custobj.optString("CustGroupname", "null");
                        String CustGroupCodeCode = custobj.optString("CustGroupCode", "null");
                        int pricelevel = custobj.optInt("pricelevel", 0);
                        double custdis = custobj.optDouble("Custdiscount", 0);
                        int due_in_days = custobj.optInt("due_in_days", 0);
                        double credit_limit = custobj.optDouble("credit_limit", 0);
                        sqlString = "insert into Customer(customerid,customer_name,customer_code,credit,CustGroupID,CustGroupname,CustGroupCode,Townshipid,Townshipname,TownshipCode,pricelevel,Custdiscount,due_in_days,credit_limit)" +
                                " values(" + customerid + ",'" + customername + "','" + customercode + "'," + credit + "," +
                                CustGroupCodeid + ",'" + CustGroupCodeName + "','" + CustGroupCodeCode + "'," +
                                townshipid + ",'" + townshipname + "','" + townshipcode + "'," + pricelevel + "," + custdis + "," + (due_in_days == 0 ? "NULL" : due_in_days) + "," + credit_limit + ")";
                        DatabaseHelper.execute(sqlString);

                    }
                    break;
                case "Location":
                    JSONArray loc = data.getJSONObject(0).getJSONArray("location");
                    for (int loccount = 0; loccount < loc.length(); loccount++) {

                        JSONObject locobj = loc.getJSONObject(loccount);
                        long locationid = locobj.getLong("locationid");
                        sqlString = "delete from Location where locationid=" + locationid;
                        DatabaseHelper.execute(sqlString);

                        String locationname = locobj.optString("Location_Name", "null");
                        String locationcode = locobj.optString("Location_Short", "null");
                        long locgroupid = locobj.getLong("LocGroupID");
                        String locgroupname = locobj.optString("LocGroup_Name", "null");
                        String locgroupcode = locobj.optString("LocGroup_Short", "null");
                        long branchid = locobj.getLong("branchID");
                        String branchName = locobj.optString("Branch_Name", "null");
                        String branchCode = locobj.optString("Branch_short", "null");
                        sqlString = "insert into Location(locationid,Location_Name,Location_Short,LocGroupID,LocGroup_Name,LocGroup_Short,branchID,Branch_Name,Branch_short)" +
                                " values(" + locationid + ",'" + locationname + "','" + locationcode + "'," +
                                locgroupid + ",'" + locgroupname + "','" + locgroupcode + "'," +
                                branchid + ",'" + branchName + "','" + branchCode + "')";
                        DatabaseHelper.execute(sqlString);
                    }
                    break;
                case "Usr_Code":
                    JSONArray usr_code = data.getJSONObject(0).getJSONArray("usr_code");
                    for (int codecount = 0; codecount < usr_code.length(); codecount++) {

                        JSONObject codeobj = usr_code.getJSONObject(codecount);
                        long code = codeobj.getLong("code");
                        int unit_type = codeobj.optInt("unit_type", 1);
                        sqlString = "delete from Usr_Code where code=" + code + " and unit_type=" + unit_type;
                        DatabaseHelper.execute(sqlString);
                        if (codeobj.getBoolean("inactive") || codeobj.getBoolean("deleted")) {

                            continue;
                        }
                        String usrcode = codeobj.getString("usr_code");
                        String description = codeobj.optString("description", "null");
                        String shortdes = codeobj.optString("shortdes", "null");
                        long classid = codeobj.getLong("class");
                        String classname = codeobj.optString("classname", "null");
                        long categoryid = codeobj.getLong("category");
                        String categoryname = codeobj.optString("categoryname", "null");
                        double sale_price = codeobj.optDouble("sale_price", 0);
                        double sale_price1 = codeobj.optDouble("sale_price1", 0);
                        double sale_price2 = codeobj.optDouble("sale_price2", 0);
                        double sale_price3 = codeobj.optDouble("sale_price3", 0);
                        int open = codeobj.optBoolean("open_price", false) == true ? 1 : 0;
                        int sale_cur = codeobj.optInt("sale_curr", 1);

                        int unit = codeobj.optInt("unit", 0);
                        String unitname = codeobj.optString("unitname", "null");
                        String unitshort = codeobj.optString("unitshort", "null");
                        double smallest_unit_qty = codeobj.optDouble("smallest_unit_qty", 1);
                        int CalNoTax = codeobj.optBoolean("CalNoTax", false) == true ? 1 : 0;
                        String sortcode = codeobj.optString("sortcode");
                        long BrandID = codeobj.optLong("BrandID", 0);
                        String BrandName = codeobj.optString("BrandName", "");
                        sqlString = "insert into Usr_Code(code,usr_code,description,class,classname,category,categoryname,sale_price,sale_price1,sale_price2,sale_price3,open_price,sale_curr,unit_type,unit,unitname,unitshort,smallest_unit_qty,CalNoTax,sortcode,BrandID,BrandName,shortdes)" +
                                " values(" + code + ",'" + usrcode + "','" + description + "'," + classid + ",'" + classname + "'," +
                                categoryid + ",'" + categoryname + "'," + sale_price + "," + sale_price1 + "," + sale_price2 + "," + sale_price3 + "," + open + "," + sale_cur + "," + unit_type + "," + unit + ",'" + unitname + "','" + unitshort + "'," + smallest_unit_qty + "," + CalNoTax + ",'" + sortcode + "'," + BrandID + ",'" + BrandName + "','" + shortdes + "')";
                        DatabaseHelper.execute(sqlString);

                    }
                    break;
                case "Pay_Type":
                    JSONArray pay = data.getJSONObject(0).getJSONArray("payment_type");

                    for (int paycount = 0; paycount < pay.length(); paycount++) {
                        JSONObject paytobj = pay.getJSONObject(paycount);
                        long pay_type = paytobj.getLong("pay_type");
                        String name = paytobj.optString("name", "null");
                        String shortdes = paytobj.optString("short", "null");
                        sqlString = "insert into Payment_Type(pay_type,name,short)" +
                                " values(" + pay_type + ",'" + name + "','" + shortdes + "')";

                        DatabaseHelper.execute(sqlString);

                    }
                    break;
                case "Dis_Type":
                    JSONArray dis = data.getJSONObject(0).getJSONArray("dis_type");

                    for (int discount = 0; discount < dis.length(); discount++) {
                        JSONObject distobj = dis.getJSONObject(discount);
                        long pay_type = distobj.getLong("dis_type");
                        String name = distobj.optString("name", "null");
                        String shortdes = distobj.optString("short", "null");
                        int paid = distobj.optBoolean("paid", false) == true ? 1 : 0;
                        int disamt = distobj.optInt("discount", 0);
                        sqlString = "insert into Dis_Type(dis_type,name,short,paid,discount)" +
                                " values(" + pay_type + ",'" + name + "','" + shortdes + "'," + paid + "," + disamt + ")";

                        DatabaseHelper.execute(sqlString);

                    }
                    break;
                case "System Setting":
                    JSONArray sys = data.getJSONObject(0).getJSONArray("systemsetting");

                    for (int syscount = 0; syscount < sys.length(); syscount++) {
                        DatabaseHelper.execute("delete from SystemSetting");
                        JSONObject systobj = sys.getJSONObject(syscount);
                        int Use_Tax = systobj.optBoolean("Use_Tax", false) == true ? 1 : 0;
                        double default_tax_percent = systobj.optDouble("default_tax_percent", 0);
                        int taxCal = systobj.getInt("taxCal");
                        int use_user_pricelevel = systobj.optBoolean("use_user_pricelevel", false) == true ? 1 : 0;
                        int use_cust_pricelevel = systobj.optBoolean("use_cust_pricelevel", false) == true ? 1 : 0;
                        int use_multipricelvl = systobj.optBoolean("use_multipricelvl", false) == true ? 1 : 0;
                        int use_unit = systobj.optBoolean("use_unit", false) == true ? 1 : 0;
                        int use_location = systobj.optBoolean("use_location", false) == true ? 1 : 0;
                        int use_customerGroup = systobj.optBoolean("use_customerGroup", false) == true ? 1 : 0;
                        int use_township = systobj.optBoolean("use_township", false) == true ? 1 : 0;
                        int use_salesperson = systobj.optBoolean("use_salesperson", false) == true ? 1 : 0;
                        int qty_places = systobj.optInt("qty_decimal_places");
                        int price_places = systobj.optInt("price_decimal_places");
                        int use_pic = systobj.optBoolean("use_pic", false) == true ? 1 : 0;
                        int Use_Delivery = systobj.optBoolean("Use_Delivery", false) == true ? 1 : 0;
                        int use_specialprice = systobj.optBoolean("use_specialprice", false) == true ? 1 : 0;
                        int SmallestQtyPrice = systobj.optBoolean("SmallestQtyPrice", false) == true ? 1 : 0;
                        int use_Category2 = systobj.optBoolean("use_category2", false) == true ? 1 : 0;
                        int pdis_bycode = systobj.optBoolean("pdis_bycode", false) == true ? 1 : 0;
                        int use_duedate = systobj.optBoolean("use_duedate", false) == true ? 1 : 0;
                        int use_multicurrency = systobj.optBoolean("use_multicurrency", false) == true ? 1 : 0;
                        String title = systobj.optString("title", "iStock");
                        sqlString = "insert into SystemSetting(Use_Tax,default_tax_percent,taxCal,use_user_pricelevel,use_cust_pricelevel,use_multipricelvl,use_unit,use_location,use_customerGroup,use_township,use_salesperson,qty_decimal_places,price_decimal_places,SmallestQtyPrice,use_pic,Use_Delivery,use_specialprice,use_Category2,title,pdis_bycode,use_duedate,use_multicurrency)" +
                                "values(" + Use_Tax + "," + default_tax_percent + "," + taxCal + "," + use_user_pricelevel + "," + use_cust_pricelevel + "," + use_multipricelvl + "," + use_unit + "," + use_location + "," + use_customerGroup + "," + use_township + "," + use_salesperson + "," + qty_places +
                                "," + price_places + "," + SmallestQtyPrice + "," + use_pic + "," + Use_Delivery + "," + use_specialprice + "," + use_Category2
                                + ",'" + title + "'," + pdis_bycode + "," + use_duedate + "," + use_multicurrency + ")";
                        DatabaseHelper.execute(sqlString);

                    }
                    break;
                case "Salesmen":
                    JSONArray salesmen = null;
                    salesmen = data.getJSONObject(0).getJSONArray("salesmen");
                    for (int salesmencount = 0; salesmencount < salesmen.length(); salesmencount++) {
                        JSONObject salesmenobj = salesmen.getJSONObject(salesmencount);
                        int id = salesmenobj.getInt("Salesmen_id");
                        sqlString = "delete from Salesmen where Salesmen_id=" + id;
                        DatabaseHelper.execute(sqlString);
                        if (salesmenobj.getBoolean("inactive")) {

                            continue;
                        }
                        String name = salesmenobj.optString("Salesmen_Name", "null");
                        String shortdes = salesmenobj.optString("short", "null");
                        int branchid = salesmenobj.getInt("branchid");
                        int Salesmen_type = salesmenobj.getInt("Salesmen_type");
                        sqlString = "insert into Salesmen(Salesmen_id,Salesmen_Name,short,branchid,Salesmen_type)values(" + id + ",'" + name + "','" + shortdes + "'," + branchid + "," + Salesmen_type + ")";
                        DatabaseHelper.execute(sqlString);
                    }
                    break;

                case "Alias_Code":
                    JSONArray Alias_Code = null;
                    Alias_Code = data.getJSONObject(0).getJSONArray("alias_code");
                    for (int aliascount = 0; aliascount < Alias_Code.length(); aliascount++) {
                        JSONObject aliasobj = Alias_Code.getJSONObject(aliascount);
                        String aliascode = aliasobj.optString("al_code");
                        String usrcode = aliasobj.optString("usr_code");
                        sqlString = "insert into Alias_Code(al_code,usr_code) values('" + aliascode + "','" + usrcode + "')";
                        DatabaseHelper.execute(sqlString);
                    }
                    break;

                case "usr_code_img":
                    JSONArray usrimg = null;
                    usrimg = data.getJSONObject(0).getJSONArray("usr_code_img");
                    for (int usrimgcount = 0; usrimgcount < usrimg.length(); usrimgcount++) {
                        JSONObject usrimgobj = usrimg.getJSONObject(usrimgcount);
                        String usrcode = usrimgobj.optString("usr_code");
                        // DeleteRecord("usr_code_img","usr_code",usrcode);
                        DatabaseHelper.execute("delete from usr_code_img where usr_code='" + usrcode + "'");
                        String codeimg = usrimgobj.optString("code_img");
                        String path = usrimgobj.optString("path");
                        sqlString = "insert into usr_code_img(usr_code,code_img,path) values('" + usrcode + "','" + codeimg + "','" + path + "')";
                        DatabaseHelper.execute(sqlString);
                    }
                    break;

                case "menu_user":
                    JSONArray menuusr = null;
                    menuusr = data.getJSONObject(0).getJSONArray("menu_user");
                    for (int menucount = 0; menucount < menuusr.length(); menucount++) {
                        JSONObject menuusrobj = menuusr.getJSONObject(menucount);
                        int userid = menuusrobj.getInt("userid");
                        int groupid = menuusrobj.optInt("groupid");
                        int subgroupid = menuusrobj.getInt("subgroupid");
                        int use_unit_type = menuusrobj.optInt("use_unit_type", 1);
                        sqlString = "insert into menu_user(userid,groupid,subgroupid,use_unit_type) values(" + userid + "," + groupid + "," + subgroupid + "," + use_unit_type + ")";
                        DatabaseHelper.execute(sqlString);
                    }
                    break;
                case "currency":
                    JSONArray currencyArr = null;
                    currencyArr = data.getJSONObject(0).getJSONArray("currency");
                    for (int count = 0; count < currencyArr.length(); count++) {
                        JSONObject menuusrobj = currencyArr.getJSONObject(count);
                        int currency = menuusrobj.getInt("currency");
                        DatabaseHelper.execute("delete from Currency where currency=" + currency);
                        String name = menuusrobj.optString("name", "");
                        String shortCur = menuusrobj.optString("short", "");
                        double exg_rate = menuusrobj.optDouble("exg_rate", 0.0);
                        int roundTo = menuusrobj.optInt("roundTo");
                        double div_rate = menuusrobj.optDouble("div_rate", 0);

                        sqlString = "insert into Currency(currency,name,short,exg_rate,roundTo,div_rate) values("
                                + currency + ",'" + name + "','" + shortCur + "'," + exg_rate + "," + roundTo + "," + div_rate + ")";
                        DatabaseHelper.execute(sqlString);
                    }
                    break;
                //Added by KNO for Language
                case "Dictionary":
                    JSONArray dic = data.getJSONObject(0).getJSONArray("Dictionary");

                    for (int diccount = 0; diccount < dic.length(); diccount++) {
                        JSONObject dictobj = dic.getJSONObject(diccount);
                        String language1 = dictobj.optString("language1");
                        String language2 = dictobj.optString("language2");
                        String language3 = dictobj.optString("language3");
                        sqlString = "insert into Dictionary(language1,language2,language3) values('" + language1 + "','" + language2 + "','" + language3 + "')";
                        DatabaseHelper.execute(sqlString);

                    }
                    break;

                //Added by KNO for Delivery (24-10-2022)
                case "Salesmen_Type":
                    JSONArray salementypeArr = data.getJSONObject(0).getJSONArray("salesmen_type");

                    for (int salementypecount = 0; salementypecount < salementypeArr.length(); salementypecount++) {
                        JSONObject salementypeobj = salementypeArr.getJSONObject(salementypecount);
                        int Salesmen_type = salementypeobj.getInt("Salesmen_type");
                        int delivery_system = salementypeobj.optBoolean("delivery_system", false) == true ? 1 : 0;
                        int delivery_charges = salementypeobj.optBoolean("delivery_charges", false) == true ? 1 : 0;
                        sqlString = "insert into Salesmen_Type(Salesmen_type,delivery_system,delivery_charges)" +
                                " values(" + Salesmen_type + ",'" + delivery_system + "','" + delivery_charges + "')";

                        DatabaseHelper.execute(sqlString);

                    }
                    break;

                //Added by KNO for Delivery (24-10-2022)
                case "delivery_setup":
                    JSONArray deliArr = data.getJSONObject(0).getJSONArray("delivery_setup");

                    for (int delicount = 0; delicount < deliArr.length(); delicount++) {
                        JSONObject deliobj = deliArr.getJSONObject(delicount);
                        int Townshipid = deliobj.getInt("Townshipid");
                        double delivery_charges = deliobj.getDouble("delivery_charges");
                        double free_range = deliobj.getDouble("free_range");
                        sqlString = "insert into delivery_setup(Townshipid,delivery_charges,free_range)" +
                                " values(" + Townshipid + ",'" + delivery_charges + "','" + free_range + "')";

                        DatabaseHelper.execute(sqlString);

                    }
                    break;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtProgress.setText((progress + 1) + "/" + tableNames.size());

                    /*if(progress<5){
                        txtProgress.setText(((progress+1)*10)+" %");
                    }else if(progress<7) {
                        txtProgress.setText(((progress+0.5)*10)+" %");
                    }else if(progress<9){
                        txtProgress.setText(((progress+0.5)*10)+" %");
                    }else  if(progress>10){
                        txtProgress.setText("100 %");}else {
                        txtProgress.setText("100 %");
                    }
*/
                    txtTable.setText(tableNames.get(progress));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void DeleteRecord(String tableName, String whereClause, String value) {
        DatabaseHelper.execute("delete from " + tableName + " where " + whereClause + "='" + value + "'");
    }

    private void SignIn() {
        String username = useredit.getText().toString();
        String password = passedit.getText().toString();
        if (password.isEmpty()) {
            password = "null";
        }
        int userid = 0;
        String name = "";
        String pass = "";
        sqlString = "select userid,name,knockcode from posuser where userid=" + frmlogin.LoginUserid;
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    userid = cursor.getInt(cursor.getColumnIndex("userid"));
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    pass = cursor.getString(cursor.getColumnIndex("knockcode"));

                } while (cursor.moveToNext());

            }

        }
        cursor.close();
        if (username.equals(name) && password.equals(pass)) {
            try {
                if (frmlogin.UseOffline == 1) {
                    Intent intent = new Intent(frmlogin.this, frmmain.class);
                    startActivity(intent);
                    finish();
                } else {
                    isInternetAcess();
                }


            } catch (Exception e) {

            }

        } else {
            Toast.makeText(this, "Username and Password does not match", Toast.LENGTH_LONG).show();
        }
    }

    private void LockUser(int userid, Boolean locked) {
        DatabaseHelper.execute("delete from Login_User where userid=" + frmlogin.LoginUserid);//added by KLM for QA-220779 25082022
        String ip = sh_ip.getString("ip", "Localhost");
        String port = sh_port.getString("port", "80");
        String Device = frmlogin.Device_Name.replace(" ", "%20");
//        String Url = "http://" + ip + ":" + port + "/api/DataSync/GetData?userid=" + frmlogin.LoginUserid + "&hostname=" + Device + "&locked=" + locked;
        String Url = "http://" + ip + "/api/DataSync/LockUser?userid=" + frmlogin.LoginUserid + "&hostname=" + Device + "&locked=" + locked;
        requestQueue = Volley.newRequestQueue(this);

        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("True")) {

                    DatabaseHelper.execute("insert into Login_User(userid,hostname)values(" + frmlogin.LoginUserid + ",'" + frmlogin.Device_Name + "')");
                    Intent intent = new Intent(frmlogin.this, frmmain.class);
                    startActivity(intent);
                    finish();
                } else {
                    String hostname = "empty";
                    Cursor cursor = DatabaseHelper.rawQuery("select hostname from Login_user where userid=" + frmlogin.LoginUserid);
                    if (cursor != null && cursor.getCount() != 0) {
                        if (cursor.moveToFirst()) {
                            do {

                                hostname = cursor.getString(cursor.getColumnIndex("hostname"));


                            } while (cursor.moveToNext());

                        }

                    }
                    cursor.close();

                    if (!hostname.equals(frmlogin.Device_Name)) {
                        AlertDialog.Builder bd = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        bd.setTitle("iStock");
                        bd.setMessage("The User is already Login");
                        bd.setCancelable(false);
                        bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                msg.dismiss();
                            }
                        });
                        msg = bd.create();
                        msg.show();
                    } else {
                        Intent intent = new Intent(frmlogin.this, frmmain.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }


        };

        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(frmlogin.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };


        StringRequest req = new StringRequest(Request.Method.GET, Url, listener, error);
        requestQueue.add(req);
    }

    private void showPosuser() {

        aryUsers.clear();
        builder = new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.showposuser, null);
        //builder.setCancelable(false);
        builder.setView(view);
        lv = (ListView) view.findViewById(R.id.lsvposuer);
        sqlString = "select  * from Posuser";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    int userid = cursor.getInt(cursor.getColumnIndex("userid"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String pass = cursor.getString(cursor.getColumnIndex("knockcode"));
                    int locid = cursor.getInt(cursor.getColumnIndex("def_locationid"));
                    int Confirm_PrintVou = cursor.getInt(cursor.getColumnIndex("Confirm_PrintVou"));
                    int allow_priceLevel = cursor.getInt(cursor.getColumnIndex("allow_pricelevel"));
                    int select_location = cursor.getInt(cursor.getColumnIndex("select_location"));
                    int select_customer = cursor.getInt(cursor.getColumnIndex("select_customer"));
                    int change_price = cursor.getInt(cursor.getColumnIndex("change_price"));
                    int change_date = cursor.getInt(cursor.getColumnIndex("change_date"));
                    int tax = cursor.getInt(cursor.getColumnIndex("tax"));
                    int discount = cursor.getInt(cursor.getColumnIndex("discount"));
                    int def_payment = cursor.getInt(cursor.getColumnIndex("def_payment"));
                    int Allow_Over_Credit_Limit = cursor.getInt((cursor.getColumnIndex("Allow_Over_Credit_Limit")));
                    int def_cashid = cursor.getInt(cursor.getColumnIndex("def_cashid"));
                    String Cashier_Printer = cursor.getString(cursor.getColumnIndex("Cashier_Printer"));
                    int CAshier_PrinterType = cursor.getInt(cursor.getColumnIndex("Cashier_PrinterType"));
                    int use_offline = cursor.getInt(cursor.getColumnIndex("use_offline"));
                    int allow_so_update = cursor.getInt(cursor.getColumnIndex("allow_so_update"));
                    int use_petrol = cursor.getInt(cursor.getColumnIndex("use_oil"));
                    int branchid = cursor.getInt(cursor.getColumnIndex("def_branchid"));
                    int hide_sale_summary = cursor.getInt(cursor.getColumnIndex("hide_sale_summary"));
                    int def_currency = cursor.getInt(cursor.getColumnIndex("def_currency")) == 0 ? 1 : cursor.getInt(cursor.getColumnIndex("def_currency"));
                    int languageid = cursor.getInt(cursor.getColumnIndex("languageid"));
                    aryUsers.add(new posuser(userid, name, Confirm_PrintVou, allow_priceLevel, select_location, select_customer, change_date, tax, discount, change_price, pass, locid, def_payment, Allow_Over_Credit_Limit, def_cashid, Cashier_Printer, CAshier_PrinterType, use_offline, allow_so_update, use_petrol, branchid, hide_sale_summary, def_currency, languageid));
                } while (cursor.moveToNext());

            }

        }
        cursor.close();
        posuserAdapter ad = new posuserAdapter(frmlogin.this, aryUsers);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                useredit.setText(aryUsers.get(position).getName());
                frmlogin.username = aryUsers.get(position).getName();
                frmlogin.LoginUserid = aryUsers.get(position).getUserid();
                frmlogin.det_locationid = aryUsers.get(position).getDef_locationid();
                frmlogin.Confirm_PrintVou = aryUsers.get(position).getConfirm_PrintVou();
                frmlogin.allow_priceLevel = aryUsers.get(position).getAllow_priceLevel();
                frmlogin.change_price = aryUsers.get(position).getChange_price();
                frmlogin.change_date = aryUsers.get(position).getChange_date();
                frmlogin.discount = aryUsers.get(position).getDiscount();
                frmlogin.tax = aryUsers.get(position).getTax();
                frmlogin.select_customer = aryUsers.get(position).getSelect_customer();
                frmlogin.select_location = aryUsers.get(position).getSelect_location();
                frmlogin.def_payment = aryUsers.get(position).getDef_payment();
                frmlogin.Allow_Over_Credit_Limit = aryUsers.get(position).getAllow_Over_Credit_Limit();
                frmlogin.def_cashid = aryUsers.get(position).getDef_cashid();
                frmlogin.Cashier_Printer = aryUsers.get(position).getCashier_Printer();
                frmlogin.Cashier_PrinterType = aryUsers.get(position).getCashier_PrinterType();
                frmlogin.UseOffline = aryUsers.get(position).getUse_offline();
                frmlogin.allow_so_update = aryUsers.get(position).getAllow_so_update();
                frmlogin.use_oil = aryUsers.get(position).getUse_oil();
                frmlogin.def_branchid = aryUsers.get(position).getDef_branchid();
                hide_sale_summary = aryUsers.get(position).getHide_sale_summary();
                frmlogin.languageid = aryUsers.get(position).getLanguageid();
                //def_currency=aryUsers.get(position).getDef_currency()==0?1:aryUsers.get(position).getDef_currency();
//                Toast.makeText(getApplicationContext()," currency "+def_currency,Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        if (aryUsers.size() > 0) {
            dialog.show();
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        } else {
            Toast.makeText(frmlogin.this, "Please Download Posuser!!", Toast.LENGTH_LONG).show();

        }

    }


    private void showIpBox(String ip, String port) {
        builder = new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.ipaddressbox, null);
        builder.setView(view);
        builder.setCancelable(false);
        edtserver = view.findViewById(R.id.edtserver);
        edtport = view.findViewById(R.id.edtport);

        if (!ip.equals("empty")) {
            edtserver.setText(ip);
        }
        if (!port.equals("empty")) {
            edtport.setText(port);
        }
        btnok = (Button) view.findViewById(R.id.btnok);
        btncancel = (Button) view.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //serveraddr trim() in login form modified by ABBP
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!edtserver.getText().toString().trim().isEmpty() || edtserver.getText().toString().trim() != "")) {
                    SharedPreferences.Editor editor = sh_ip.edit();
                    editor.remove("ip");
                    editor.commit();

                    editor = sh_ip.edit();
                    editor.putString("ip", edtserver.getText().toString().trim());
                    editor.commit();

                    SharedPreferences.Editor editor_port = sh_port.edit();
                    editor_port.remove("port");
                    editor_port.commit();
                    editor_port = sh_port.edit();
                    if (edtport.getText().toString().trim().isEmpty() || edtport.getText().toString().trim() == "") {
                        edtport.setText("80");
                    }
                    editor_port.putString("port", edtport.getText().toString().trim());
                    editor_port.commit();
                    dialog.dismiss();
                    CheckConnection();
                } else {
                    Toast.makeText(frmlogin.this, "Fill Server Information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


    }

    private void isInternetAcess() {
        try {
            String ip = sh_ip.getString("ip", "Localhost");
            String port = sh_port.getString("port", "80");
//            String Url = "http://" + ip + ":" + port + "/api/DataSync/GetData";
            String Url = "http://" + ip + "/api/DataSync/CheckConnection";
            requestQueue = Volley.newRequestQueue(this);

            final Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    isOnline = true;
                    LockUser(frmlogin.LoginUserid, true);
                }


            };

            final Response.ErrorListener error = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isOnline = false;
                    btnconnect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wifidis, 0, 0);
                    if (frmlogin.isTVMode == false) {
                        new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme)
                                .setTitle("iStock")
                                .setMessage("Your Network Condition is unavailable!Do you want to change offline?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        frmlogin.UseOffline = 1;
                                        Intent intent = new Intent(frmlogin.this, frmmain.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.dismiss();
                                    }
                                })

                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    } else {
                        GlobalClass.showAlertDialog(frmlogin.this, "iStock", "No Internet Connection!");
                    }
                }
            };
            StringRequest req = new StringRequest(Request.Method.GET, Url, listener, error);
            requestQueue.add(req);
        } catch (Exception ee) {
            isOnline = true;
            new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme)
                    .setTitle("iStock")
                    .setMessage("Error:" + ee.getMessage())
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            frmlogin.UseOffline = 1;
                            Intent intent = new Intent(frmlogin.this, frmmain.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }


    }

    private void CheckConnection() {

        if (sh_ip.getString("ip", "empty").equals("empty") || sh_port.getString("port", "empty").equals("empty")) {
            //btnconnect.setImageResource(R.drawable.disconnect);
            btnconnect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wifidis, 0, 0);
            btnlogin.setEnabled(false);
            useredit.setEnabled(false);
            Toast.makeText(frmlogin.this, "Please Connect to server", Toast.LENGTH_LONG).show();
            return;
        }

        String ip = sh_ip.getString("ip", "Localhost");
        String port = sh_port.getString("port", "80");
//        String Url = "http://" + ip + ":" + port + "/api/DataSync/GetData";
        String Url = "http://" + ip + "/api/DataSync/CheckConnection";
        requestQueue = Volley.newRequestQueue(this);

        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //btnconnect.setImageResource(R.drawable.connect);
                btnconnect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wificonnect, 0, 0);
                //btnLogin.setEnabled(true);
                useredit.setEnabled(true);
                btnlogin.setEnabled(true);
                if (ptype.size() > 0) ptype.clear();
                if (Printers.size() > 0) Printers.clear();
                Binding_PrinterSetting();
                //Toast.makeText(frmlogin.this, "Server is Connected", Toast.LENGTH_LONG).show();
            }


        };

        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(frmlogin.this, error.getMessage(), Toast.LENGTH_LONG).show();
                btnconnect.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wifidis, 0, 0);
            }
        };


        StringRequest req = new StringRequest(Request.Method.GET, Url, listener, error);
        requestQueue.add(req);

    }

    private void Binding_PrinterSetting() {
        if (Printers.size() > 0) Printers.clear();
        if (ptype.size() > 0) ptype.clear();
        ip = sh_ip.getString("ip", "empty");
        port = sh_port.getString("port", "empty");
//        String url = "http://" + ip + ":" + port + "/api/DataSync/GetPrinter?&printer=true";
        String url = "http://" + ip + "/api/DataSync/GetPrinter?&printer=true";
        RequestQueue request = Volley.newRequestQueue(context);
        Response.Listener listener = new Response.Listener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Object response) {
                String[] str = response.toString().split("&&");
                String[] prt = str[0].split(",");
                String[] pty = str[1].split(",");
                for (int i = 0; i < prt.length; i++) {
                    Printers.add(prt[i]);
                }
                for (int i = 0; i < pty.length; i++) {
                    if (i == 0) {
                        ptype.add(new Printer_Type((-1), pty[i]));
                    } else {
                        ptype.add(new Printer_Type((i), pty[i]));
                    }
                }
            }
        };
        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bd = new AlertDialog.Builder(frmlogin.this, R.style.AlertDialogTheme);
                bd.setMessage("Process is Fail!Check your Network Connection");
                bd.setTitle("iStock");

                bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = bd.create();
                dialog.show();
            }
        };

        StringRequest req = new StringRequest(Request.Method.GET, url, listener, error);
        request.add(req);
    }

    public class ImportingData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            StringBuffer response = new StringBuffer();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "text/plain");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                if (importjson != null) {
                    //connection.setRequestProperty("Content-Length", Integer.toString(sqlstring.length()));
                    connection.getOutputStream().write(importjson.getBytes("UTF8"));
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String inputLine;


                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.dismiss();
            AlertDialog.Builder ab = new AlertDialog.Builder(frmlogin.this);
            ab.setMessage(s);
            ab.setCancelable(false);
            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (s.equals("Importing Data is successfull!!")) {
                        sqlString = "delete from Sale_Head_Main";
                        DatabaseHelper.execute(sqlString);

                        sqlString = "delete from Sale_Det";
                        DatabaseHelper.execute(sqlString);

                        sqlString = "delete from Tranid";
                        DatabaseHelper.execute(sqlString);

                        sqlString = "delete from Sale_Head_Tmp_Mp";
                        DatabaseHelper.execute(sqlString);

                        sqlString = "delete from SalesVoucher_Salesmen_Tmp";
                        DatabaseHelper.execute(sqlString);

                        if (checkofflinedata) {
                            GetData();
                        }
                    }
                    msg.dismiss();
                }
            });
            msg = ab.create();
            msg.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    msg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                }
            });
            msg.show();
        }
    }


    public static String GetSystemSetting(String columnName) {
        Cursor cursor = DatabaseHelper.DistinctSelectQuery("SystemSetting", new String[]{columnName});
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToNext()) {
//                return cursor.getColumnName(0).toString();
                return String.valueOf(cursor.getInt(cursor.getColumnIndex(columnName)));
            }

        }
        return "";
    }

    //region BluetoothPrinter

    private void showConnectDialog() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) { //蓝牙未开启，则开启蓝牙--
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            showBluetoothDeviceChooseDialog();
        }
    }

    private void showBluetoothDeviceChooseDialog() {
        BluetoothDeviceChooseDialog bluetoothDeviceChooseDialog = new BluetoothDeviceChooseDialog();
        bluetoothDeviceChooseDialog.setOnDeviceItemClickListener(new BluetoothDeviceChooseDialog.onDeviceItemClickListener() {
            @Override
            public void onDeviceItemClick(BluetoothDevice device) {
                try {
                    if (TextUtils.isEmpty(device.getName())) {
                        tv_device_selected.setText(device.getAddress());
                    } else {
                        tv_device_selected.setText(device.getName());// + " [" + device.getAddress() + "]");
                    }
                    configObj = new BluetoothEdrConfigBean(device);
                    tv_device_selected.setTag(BaseEnum.HAS_DEVICE);
                    doConnect();
                } catch (Exception ex) {
                    showToast(ex.getMessage());
                }

            }
        });
        bluetoothDeviceChooseDialog.show(frmlogin.this.getFragmentManager(), null);
    }

    private void doConnect() {
        BluetoothEdrConfigBean bluetoothEdrConfigBean = (BluetoothEdrConfigBean) configObj;
        connectBluetooth(bluetoothEdrConfigBean);
    }

    private void connectBluetooth(BluetoothEdrConfigBean bluetoothEdrConfigBean) {
        try {
            PIFactory piFactory = new BluetoothFactory();
            PrinterInterface printerInterface = piFactory.create();
            printerInterface.setConfigObject(bluetoothEdrConfigBean);
            rtPrinter.setPrinterInterface(printerInterface);
            BaseApplication.getInstance().setRtPrinter(rtPrinter);
            rtPrinter.connect(bluetoothEdrConfigBean);
        } catch (Exception e) {
            showToast(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isInConnectList(Object configObj) {
        if (configObj == null) return false;
        boolean isInList = false;
        for (int i = 0; i < printerInterfaceArrayList.size(); i++) {
            PrinterInterface printerInterface = printerInterfaceArrayList.get(i);
            if (configObj.toString().equals(printerInterface.getConfigObject().toString())) {
                if (printerInterface.getConnectState() == ConnectStateEnum.Connected) {
                    isInList = true;
                    break;
                }
            }
        }

        return isInList;
    }

    @Override
    public void printerObserverCallback(PrinterInterface printerInterface, int state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (state) {
                        case CommonEnum.CONNECT_STATE_SUCCESS:
                            showToast(printerInterface.getConfigObject().toString() + " - connect success");
                            tv_device_selected.setText(printerInterface.getConfigObject().toString());
                            tv_device_selected.setTag(BaseEnum.HAS_DEVICE);
                            curPrinterInterface = printerInterface;//设置为当前连接， set current Printer Interface
                            printerInterfaceArrayList.add(printerInterface);//多连接-添加到已连接列表
                            rtPrinter.setPrinterInterface(printerInterface);

                            break;
                        case CommonEnum.CONNECT_STATE_INTERRUPTED:
                            if (printerInterface != null && printerInterface.getConfigObject() != null) {
                                showToast(printerInterface.getConfigObject().toString() + " disconnected");
                            } else {
                                showToast("disconnected");
                            }
                            tv_device_selected.setTag(BaseEnum.NO_DEVICE);
                            curPrinterInterface = null;
                            printerInterfaceArrayList.remove(printerInterface);//多连接-从已连接列表中移除

                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("disconnected");
                }
            }
        });
    }

    @Override
    public void printerReadMsgCallback(PrinterInterface printerInterface, byte[] bytes) {

    }

    private void showToast(String message) {
        Toast.makeText(frmlogin.this, message, Toast.LENGTH_LONG).show();
    }

    //endregion

}