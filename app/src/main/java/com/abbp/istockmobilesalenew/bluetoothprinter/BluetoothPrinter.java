package com.abbp.istockmobilesalenew.bluetoothprinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import com.abbp.istockmobilesalenew.BaseApplication;
import com.rt.printerlibrary.bean.Position;
import com.rt.printerlibrary.bean.PrinterStatusBean;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscCmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.enumerate.BmpPrintMode;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.rt.printerlibrary.setting.TextSetting;
import com.rt.printerlibrary.utils.PrintListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BluetoothPrinter {

    public static final int PRINT_80MM = 80;
    public static final int PRINT_48MM = 48;

    private static final String mChartsetName = "UTF-8";
    private static final byte[] FEED_PAPER_AND_CUT = {0x1D, 0x56, 66, 0x00};

    Context context;
    RTPrinter rtPrinter;

    public BluetoothPrinter(Context context, RTPrinter rtPrinter) {
        this.context = context;
        this.rtPrinter = rtPrinter;
    }

    public String escTextPrint(String printStr, int textSize) throws UnsupportedEncodingException {
        final String[] callback = {""};
        if (rtPrinter != null) {
            EscFactory escFac = new EscFactory();
            EscCmd escCmd = escFac.create();
            escCmd.append(escCmd.getHeaderCmd());//?????????, Initial
            //    escCmd.append(((EscCmd)escCmd).getPageMode(true));//??????????????????
            //    escCmd.append(((EscCmd)escCmd).getPageArea(10,10,200,200));//?????????????????????????????????
            //      escCmd.append(((EscCmd)escCmd).getSetLeftStartSpacing(10*8));//???????????? Leave white on the left(Unit: Point 1mm=8 points)
//            escCmd.append(((EscCmd)escCmd).getSetAreaWidth(76*8));//??????????????????Set the print area (Unit: Point 1mm=8 points)
//            escCmd.append(((EscCmd)escCmd).getSetAreaWidth(104*8));//TODO  by FZP ??????????????????????????????RP410c?????????????????????????????????????????????????????????????????????
            escCmd.setChartsetName(mChartsetName);

            CommonSetting commonSetting = new CommonSetting();
            Position txtposition = new Position(0, 0);

            TextSetting textSetting = new TextSetting();
            textSetting.setIsEscSmallCharactor(SettingEnum.Disable);
            textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
            textSetting.setTxtPrintPosition(txtposition);//???????????????X?????????????????????????????????
            textSetting.setCpclFontSize(textSize);

            commonSetting.setEscLineSpacing(30);
            escCmd.append(escCmd.getCommonSettingCmd(commonSetting));
            escCmd.append(escCmd.getTextCmd(textSetting, printStr));
            escCmd.append(escCmd.getLFCRCmd());
            //???????????????X?????????????????????
            //????????????txtposition.x>0,?????????????????????????????????????????????
//            txtposition.x = 160;//????????????160*0.125=20mm,txtposition.y???????????????
//            textSetting.setTxtPrintPosition(txtposition); //????????????getSetXPosition????????? escCmd.append(((EscCmd)escCmd).getSetXPosition(160));
//            escCmd.append(escCmd.getTextCmd(textSetting, printStr));
//            escCmd.append(escCmd.getLFCRCmd());
            //   escCmd.append(((EscCmd)escCmd).getPageEnd(true));
            //escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getHeaderCmd());//?????????, Initial
            escCmd.append(escCmd.getLFCRCmd());
            rtPrinter.writeMsgAsync(escCmd.getAppendCmds());

            /*************for RPP02N  call back status  ????????????????????????  Change status to active reporting********************/
//            if (BaseApplication.getInstance().getCurrentConnectType() == BaseEnum.CON_USB) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        byte[] bytes = rtPrinter.readMsg();
//                        PrinterStatusBean StatusBean = PrinterStatusPareseUtils.parsePrinterStatusResult(bytes);
//                        String msg = FuncUtils.PrinterStatusToStr(StatusBean);
//                        if (!msg.isEmpty())
//                            ToastUtil.show(TextPrintESCActivity.this, "print status???" + msg);
//                    }
//                });
//
//            } else {
            rtPrinter.setPrintListener(new PrintListener() {
                @Override
                public void onPrinterStatus(PrinterStatusBean StatusBean) {
                    String msg = FuncUtils.PrinterStatusToStr(StatusBean);
                    if (!msg.isEmpty())
//                            ToastUtil.show(TextPrintESCActivity.this, "print status???" + msg);
                        callback[0] = "print status???" + msg;

                }
            });
//            }
//            /**************************************************************************************/

//            String stcCmds = "AT+DISC\r\n";
//            escCmd.clear();
//            byte[] bytes = stcCmds.getBytes();
//            escCmd.append(bytes);
//            rtPrinter.writeMsgAsync(escCmd.getAppendCmds());
            //DisconnectByATDISC();
        }

        return callback[0];

    }

    private static int getInputLineSpacing() {
//        String strLineSpacing = et_linespacing.getText().toString();
//        if (TextUtils.isEmpty(strLineSpacing)) {
//            strLineSpacing = "30";
//            et_linespacing.setText(strLineSpacing);
//        }
        int n = Integer.parseInt("30");
        if (n > 255) {
            n = 255;
        }
        return n;
    }

    public void printFromView(View rootView) {
        ViewTreeObserver vto = rootView.getViewTreeObserver();
        View finalView = rootView;
        AtomicInteger viewWidth = new AtomicInteger(rootView.getWidth());
        AtomicInteger viewHeight = new AtomicInteger(rootView.getMeasuredHeight());
        vto.addOnGlobalLayoutListener(() -> {
            viewWidth.set(finalView.getWidth());
            viewHeight.set(finalView.getMeasuredHeight());
        });
        new Handler().postDelayed(() -> {
            try {
                loadBitmapAndPrint(rootView, viewWidth.get(), viewHeight.get());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SdkException e) {
                e.printStackTrace();
            }
        }, 500);

    }

    private void loadBitmapAndPrint(View view, int viewWidth, int viewHeight) throws IOException, SdkException {
        Bitmap bmp = loadBitmapFromView(view, viewWidth, viewHeight);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                escImagePrint(rtPrinter, bmp, PRINT_80MM);
            } catch (SdkException e) {
                e.printStackTrace();
            }
        });
    }

    private Bitmap loadBitmapFromView(View view, int viewWidth, int viewHeight) {
        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bitmap;
    }

    public void escImagePrint(RTPrinter rtPrinter, Bitmap mBitmap, int bmpPrintWidth) throws SdkException {
        new Thread(new Runnable() {
            @Override
            public void run() {

//                showProgressDialog("Loading...");

                CmdFactory cmdFactory = new EscFactory();
                Cmd cmd = cmdFactory.create();
                cmd.append(cmd.getHeaderCmd());

                CommonSetting commonSetting = new CommonSetting();
                commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                cmd.append(cmd.getCommonSettingCmd(commonSetting));

                BitmapSetting bitmapSetting = new BitmapSetting();

                /**
                 * MODE_MULTI_COLOR - ????????????????????????<br/> Suitable for multi-level grayscale printing<br/>
                 * MODE_SINGLE_COLOR-????????????????????????<br/>Suitable for printing black and white paper
                 */
                bitmapSetting.setBmpPrintMode(BmpPrintMode.MODE_SINGLE_COLOR);
//                bitmapSetting.setBmpPrintMode(BmpPrintMode.MODE_MULTI_COLOR);


//                if (bmpPrintWidth > 72) {
//                    bmpPrintWidth = 72;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            et_pic_width.setText(bmpPrintWidth + "");
//                        }
//                    });
//                }
                bitmapSetting.setBimtapLimitWidth(bmpPrintWidth * 8);
                try {
                    cmd.append(cmd.getBitmapCmd(bitmapSetting, mBitmap));
                } catch (SdkException e) {
                    e.printStackTrace();
                }

                cmd.append(FEED_PAPER_AND_CUT);
//                cmd.append(cmd.getLFCRCmd());
//                cmd.append(cmd.getLFCRCmd());
//                cmd.append(cmd.getLFCRCmd());
//                cmd.append(cmd.getLFCRCmd());
//                cmd.append(cmd.getLFCRCmd());
//                cmd.append(cmd.getLFCRCmd());
                if (rtPrinter != null) {
                    rtPrinter.writeMsg(cmd.getAppendCmds());//Sync Write
                }

//                hideProgressDialog();
            }
        }).start();


        //??????????????????bin???????????????????????????sd????????????
//        final byte[] btToFile = cmd.getAppendCmds();
//        TonyUtils.createFileWithByte(btToFile, "Esc_imageCmd.bin");
//        TonyUtils.saveFile(FuncUtils.ByteArrToHex(btToFile), "Esc_imageHex");

    }

}
