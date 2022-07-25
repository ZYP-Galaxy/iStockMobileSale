package com.abbp.istockmobilesalenew;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class outstandlistAdp extends RecyclerView.Adapter<outstandlistAdp.MyViewHolder> {
    public static String url;
    public static SharedPreferences sh_ip,sh_port;
    Context context;
    ArrayList<outstandlist> data;
    RequestQueue requestQueue;
    AlertDialog payment;
    ProgressDialog pd;
    AlertDialog msg;
    public outstandlistAdp(Context context, ArrayList<outstandlist> data) {
        this.context = context;
        this.data = data;
        sh_ip=context.getSharedPreferences("ip",MODE_PRIVATE);
        sh_port=context.getSharedPreferences("port",MODE_PRIVATE);
        pd=new ProgressDialog(context);
    }
    @Override
    public outstandlistAdp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf= LayoutInflater.from(parent.getContext());
        View v=lf.inflate(R.layout.outstandlistitem,parent,false);

        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull outstandlistAdp.MyViewHolder myViewHolder, int position) {

        myViewHolder.Customer.setText(" "+data.get(position).getCustomer());
        myViewHolder.Currency.setText(" "+data.get(position).getCurrency());

        String curformat= String.format("%,."+frmmain.price_places+"f",data.get(position).getOpening());
        myViewHolder.Opening.setText(curformat);

        String curformatSale= String.format("%,."+frmmain.price_places+"f",data.get(position).getSale());
        myViewHolder.Sale.setText(curformatSale);

        String curformatRetunIn= String.format("%,."+frmmain.price_places+"f",data.get(position).getReturnIn());
        myViewHolder.ReturnIn.setText(curformatRetunIn);

        String curformatDiscount= String.format("%,."+frmmain.price_places+"f",data.get(position).getDiscount());
        myViewHolder.Discount.setText(curformatDiscount);

        String curformatPaid= String.format("%,."+frmmain.price_places+"f",data.get(position).getPaid());
        myViewHolder.Paid.setText(curformatPaid);

        String curformatClosing= String.format("%,."+frmmain.price_places+"f",data.get(position).getBalance());
        myViewHolder.Balance.setText(curformatClosing);

       // myViewHolder.Balance.setText(" "+String.valueOf(data.get(position).getBalanceQty()));
      //  myViewHolder.Location.setText(" "+data.get(position).getLocation());
//        myViewHolder.Balance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Show_PaymentForm(position);
//            }
//        });
        myViewHolder.imgpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show_PaymentForm(position);
            }
        });

    }
    private void Show_PaymentForm(int position) {
        AlertDialog.Builder bd=new AlertDialog.Builder(context);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.outstandpayment,null);
        TextView txtTile=view.findViewById(R.id.caption);
        txtTile.setText(data.get(position).getCustomer());
        EditText etdPayment=view.findViewById(R.id.txtPayment);
        ImageView imgSave=view.findViewById(R.id.imgSave);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    pd.setTitle("Saving");
                    pd.setMessage("Please Wait...");
                    pd.setCancelable(true);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setIndeterminate(true);
                    pd.show();
                    String sqlUrl="";
                    String ip = sh_ip.getString("ip", "empty");
                    String port = sh_port.getString("port", "empty");
                    sqlUrl = "http://" +ip+"/api/DataSync/CustomerPayment?userid="+frmlogin.LoginUserid+"&payment="
                            +etdPayment.getText().toString()+"&customerid="+data.get(position).getCustomerid();
                    requestQueue = Volley.newRequestQueue(context);
                    final Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            AlertDialog.Builder mg=new AlertDialog.Builder(context);
                            mg.setTitle("iStock");
                            mg.setMessage(response);
                            mg.setCancelable(false);
                            mg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    frmoustandlist.ShowResult();
                                    pd.dismiss();
                                    payment.dismiss();
                                }
                            });
                            msg=mg.create();
                            msg.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    msg.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                                }
                            });
                            msg.show();

                        }

                    };

                    final Response.ErrorListener error = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            pd.dismiss();
                            payment.dismiss();
                        }
                    };
                    StringRequest req = new StringRequest(Request.Method.GET, sqlUrl, listener, error);
                    requestQueue.add(req);
                }
                catch (Exception ee)
                {
                    pd.dismiss();
                    payment.dismiss();
                }



            }
        });
        bd.setView(view);
        payment=bd.create();
        payment.show();


    }


    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Customer,Currency,Opening,Sale,ReturnIn,Discount,Paid,Balance;
        ImageView imgpayment;

        public MyViewHolder(View itemView) {
            super(itemView);
            Customer=itemView.findViewById(R.id.txtCustomer);
            Currency=itemView.findViewById(R.id.txtCurrency);
            Opening=itemView.findViewById(R.id.txtOpening);
            Sale=itemView.findViewById(R.id.txtSale);
            ReturnIn=itemView.findViewById(R.id.txtReturnIn);
            Discount=itemView.findViewById(R.id.txtDiscount);
            Paid=itemView.findViewById(R.id.txtPaid);
            Balance=itemView.findViewById(R.id.txtBalance);
            imgpayment=itemView.findViewById(R.id.imgpayment);



        }


    }
}
