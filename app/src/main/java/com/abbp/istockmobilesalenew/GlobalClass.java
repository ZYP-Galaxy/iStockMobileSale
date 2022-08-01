package com.abbp.istockmobilesalenew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.net.URL;
import java.net.URLConnection;

public class GlobalClass {

    public static String saleEntry="sale_entry";
    public static String saleOrdEntry="saleorder_entry";
    public static String returnInEntry="returnin_entry";
    public static String saleEntryTV="sale_entry_tv";


    public static void showToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public static void showAlertDialog(Context ctx, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx, R.style.AlertDialogTheme);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", (dialog1, which) -> {
        });
        dialog.create().show();
    }

    static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context, R.style.AlertDialogTheme);
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isConnectedToServer(String url) {
        try {
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }

    public static String GetAppSetting(String SettingName) {
        String SettingValue = "";
        String sqlString = "select Setting_No,Setting_Name,Setting_Value from AppSetting where Setting_Name='" + SettingName + "'";
        Cursor cursor = DatabaseHelper.rawQuery(sqlString);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    SettingValue = cursor.getString(cursor.getColumnIndex("Setting_Value"));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return SettingValue;
    }

    public static String GetSystemSetting(String ColumnName) {
        String value = "";
        Cursor cursor = DatabaseHelper.rawQuery("select * from SystemSetting");
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getColumnIndex(ColumnName) > -1)
                        value = cursor.getString(cursor.getColumnIndex(ColumnName));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return value;
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

    /**
     * dip to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Bitmap TrimBitmap(Bitmap bmp) {
        int imgHeight = bmp.getHeight();
        int imgWidth  = bmp.getWidth();


        //TRIM WIDTH - LEFT
        int startWidth = 0;
        for(int x = 0; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startWidth = x;
                        break;
                    }
                }
            } else break;
        }


        //TRIM WIDTH - RIGHT
        int endWidth  = 0;
        for(int x = imgWidth - 1; x >= 0; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endWidth = x;
                        break;
                    }
                }
            } else break;
        }



        //TRIM HEIGHT - TOP
        int startHeight = 0;
        for(int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startHeight = y;
                        break;
                    }
                }
            } else break;
        }



        //TRIM HEIGHT - BOTTOM
        int endHeight = 0;
        for(int y = imgHeight - 1; y >= 0; y--) {
            if (endHeight == 0 ) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        endHeight = y;
                        break;
                    }
                }
            } else break;
        }


        return Bitmap.createBitmap(
                bmp,
                startWidth,
                startHeight,
                endWidth - startWidth,
                endHeight - startHeight
        );

    }

}


