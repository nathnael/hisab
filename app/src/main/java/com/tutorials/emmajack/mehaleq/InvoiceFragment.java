package com.tutorials.emmajack.mehaleq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by TechSolutions2 on 8/30/2016.
 */
public class InvoiceFragment extends Fragment {
    public interface OnPayInvoiceListener{
        public void OnPayInvoice();
    }

    final static String ARG_INVOICE = "invoice";

    OnPayInvoiceListener mOnPayInvoiceCallback;
    InvoiceData mInvoice;
    String mInvoiceJson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(savedInstanceState != null){
            mInvoiceJson = savedInstanceState.getString(ARG_INVOICE);
            mInvoice = InvoiceData.parse(mInvoiceJson);
        }
        View invoiceView = inflater.inflate(R.layout.invoice_view, container, false);
        WebView invoiceReport = (WebView)invoiceView.findViewById(R.id.invoice_report);

        invoiceReport.getSettings().setJavaScriptEnabled(true);
        //loading the invoice report html
        invoiceReport.loadUrl("file:///android_asset/invoice.html");
        //set invoice data and items using a javascript call
        invoiceReport.setWebViewClient( new WebViewClient(){
            @Override
            public void onPageFinished(WebView web, String url){
                String jsFunction = "(function(){setInvoice('" + mInvoice.getInvoiceDate() + "','" + mInvoice.getInvoiceNumber() + "');";
                ArrayList<InvoiceItemData> items = mInvoice.getInvoiceItems();
                for(int i= 0; i < items.size(); i++){
                    InvoiceItemData item = items.get(i);
                    jsFunction += "addItem('" + item.getDescription() + "', " +
                            item.getQuantity() + ", " +
                            item.getUnitPrice() + ", " +
                            (item.getTaxable()? "true" : "false") + ", " +
                            item.getTaxRate() + ");";
                }
                jsFunction += "})();";
                web.loadUrl("javascript:" + jsFunction);
            }
        });

        ((Button)invoiceView.findViewById(R.id.pay_invoice)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPayInvoiceCallback.OnPayInvoice();
            }
        });
        return invoiceView;
    }


    @Override
    public void onStart(){
        super.onStart();
        Bundle args = getArguments();
        if(args != null){
            mInvoiceJson = args.getString(ARG_INVOICE);
            mInvoice = InvoiceData.parse(mInvoiceJson);
        }
        updateInvoiceView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(ARG_INVOICE, mInvoiceJson);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            Activity hostActivity = (Activity)context;
            try {
                mOnPayInvoiceCallback = (OnPayInvoiceListener) hostActivity;
            } catch (ClassCastException e) {
                throw new ClassCastException(hostActivity.toString()
                        + " must implement OnPayInvoiceListener");
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " Invalid context");
        }
    }

    public void updateInvoiceView(){
//        Activity activity = getActivity();
//        ((TextView)activity.findViewById(R.id.inv_service_no)).setText(Integer.toString(mServiceNo));
//        ((TextView)activity.findViewById(R.id.inv_date)).setText(mInvoiceDate);
//        ((TextView)activity.findViewById(R.id.inv_driver)).setText(mDriver);
//        ((TextView)activity.findViewById(R.id.inv_passanger)).setText(mPassenger);
//        ((TextView)activity.findViewById(R.id.inv_distance)).setText(Float.toString(mDistance));
//        ((TextView)activity.findViewById(R.id.inv_amount)).setText(Float.toString(mAmount));
//        ((TextView)activity.findViewById(R.id.inv_tax)).setText(Float.toString(mTax));
//        ((TextView)activity.findViewById(R.id.inv_commission)).setText(Float.toString(mCommission));
//        ((TextView)activity.findViewById(R.id.inv_total)).setText(Float.toString(mAmount+mTax+mCommission));
    }
}
