package com.abbp.istockmobilesalenew.tvsale;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.abbp.istockmobilesalenew.GlobalClass;
import com.abbp.istockmobilesalenew.R;

import com.abbp.istockmobilesalenew.saleorder_entry;

import java.util.ArrayList;

public class DisTypeAdapter extends RecyclerView.Adapter<DisTypeAdapter.MyViewHolder> {

    Context context;
    ArrayList<Dis_Type> data = new ArrayList<>();
    int itemposition;
    Button btn;
    String keynum = "";
    TextView source;
    Boolean startOpen = true;
    AlertDialog msg;
    private String formname;
    AlertDialog da;

    public DisTypeAdapter(Context context, ArrayList<Dis_Type> data, Button btn, AlertDialog da, int itemposition, TextView source) {
        this.data = data;
        this.context = context;
        this.btn = btn;
        this.da = da;
        this.itemposition = itemposition;
        this.source = source;
        this.formname = "Sale";
    }

    public DisTypeAdapter(Context context, ArrayList<Dis_Type> data, Button btn, AlertDialog da, int itemposition, TextView source, String formname) {
        this.data = data;
        this.context = context;
        this.btn = btn;
        this.da = da;
        this.itemposition = itemposition;
        this.source = source;
        this.formname = formname;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View v = lf.inflate(R.layout.headerbinding, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.btn.setText(data.get(position).getName());
        holder.btn.setBackgroundResource(R.drawable.usercodegradiant);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).dis_type != 5) {
                    btn.setText(data.get(position).getName());
                } else {
                    btn.setText("0.0");
                }

                
                    //sale_entry_tv.sd.get(itemposition).setDis_type(data.get(position).dis_type);
                    sale_entry_tv.selected_distype = data.get(position).dis_type;
                

                if (data.get(position).getDis_type() == 5) {
                    startOpen = true;
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.keypad, null);
                    float density = context.getResources().getDisplayMetrics().density;
                    final PopupWindow pw = new PopupWindow(layout, (int) density * 310, (int) density * 500, true);
                    pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pw.setOutsideTouchable(false);
                    pw.showAsDropDown(source);
                    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnc, btndec, btndel, btnenter, btnpercent;
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
                    TextView txtNum = layout.findViewById(R.id.txtNum);
                    btnpercent = layout.findViewById(R.id.btnpercent);
                    btnpercent.setVisibility(View.VISIBLE);
                    txtNum.setText(String.valueOf(keynum));
                    btnpercent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (startOpen) {
                                keynum = "";
                                startOpen = false;
                            }
                            txtNum.setText(keynum + btnpercent.getText());
                            keynum = txtNum.getText().toString();
                        }
                    });
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (startOpen) {
                                keynum = "";
                                startOpen = false;
                            }
                            txtNum.setText(keynum + btn1.getText());
                            keynum = txtNum.getText().toString();
                        }
                    });
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (startOpen) {
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
                            txtNum.setText(keynum + btn3.getText());
                            keynum = txtNum.getText().toString();
                        }
                    });
                    btn4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (startOpen) {
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
                            if (startOpen) {
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
                            if (startOpen) {
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
                            if (startOpen) {
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
                            if (startOpen) {
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
                            if (startOpen) {
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
                            if (startOpen) {
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
                            if (startOpen) {
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
                            if (startOpen) {
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
                                startOpen = false;

                            }

                        }
                    });
                    btnenter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (formname.equals("Sale Order")) {
                                    if (keynum.length() > 0) {
                                        if (keynum.contains("%")) {
                                            keynum = keynum.substring(0, keynum.length() - 1);
                                            saleorder_entry.dis_percent = Double.parseDouble(keynum);
                                            saleorder_entry.dis_typepercent = true;
                                            if (saleorder_entry.dis_percent > 99) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                                                builder.setTitle("iStock");
                                                builder.setMessage("Discount Percent should be 0 to 99");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        saleorder_entry.dis_percent = 0.0;
                                                        btn.setText(String.valueOf(saleorder_entry.dis_percent) + "%");
                                                        msg.dismiss();

                                                    }
                                                });
                                                msg = builder.create();
                                                msg.show();
                                            } else {
                                                btn.setText(String.valueOf(saleorder_entry.dis_percent) + "%");
                                            }
                                        } else {

                                            saleorder_entry.disamt = Double.parseDouble(keynum);
                                            saleorder_entry.dis_typepercent = true;
                                            saleorder_entry.dis_percent = 0;
                                            btn.setText(String.valueOf(saleorder_entry.disamt));
                                            saleorder_entry.dis_typepercent = false;
                                        }
                                    }
                                    saleorder_entry.getSummary();
                                }   //end sale_order
                                else {
                                    if (keynum.length() > 0) {
                                        if (keynum.contains("%")) {
                                            keynum = keynum.replace("%", "");//.substring(0, keynum.length() - 1);
                                            sale_entry_tv.dis_percent = Double.parseDouble(keynum);
                                            sale_entry_tv.dis_typepercent = true;
                                            if (sale_entry_tv.dis_percent > 99) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                                                builder.setTitle("iStock");
                                                builder.setMessage("Discount Percent should be 0 to 99!");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        sale_entry_tv.dis_percent = 0.0;
                                                        btn.setText(sale_entry_tv.dis_percent + "%");
                                                        msg.dismiss();

                                                    }
                                                });
                                                msg = builder.create();
                                                msg.show();
                                            } else {
                                                btn.setText(sale_entry_tv.dis_percent + "%");
                                            }

                                        } else {
                                            double disAmount = Double.parseDouble(keynum);
                                            if (disAmount < sale_entry_tv.sd.get(itemposition).getSale_price()) {
                                                sale_entry_tv.disamt = disAmount;
                                                btn.setText(String.valueOf(sale_entry_tv.disamt));
                                            } else {
                                                GlobalClass.showAlertDialog(context,"iStock","Discount amount is greater than sale price!");
                                            }
                                            sale_entry_tv.dis_percent = 0;
                                            sale_entry_tv.dis_typepercent = false;
                                        }

                                    } else {
                                        sale_entry_tv.disamt = 0;
                                        sale_entry_tv.dis_percent = 0;
                                        btn.setText("0.0");
                                    }

                                    sale_entry_tv.getSummary();
                                }
                                startOpen = false;
                                pw.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
                startOpen = false;
                da.dismiss();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.info_text);
        }
    }
}
