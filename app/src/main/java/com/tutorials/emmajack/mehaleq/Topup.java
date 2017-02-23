package com.tutorials.emmajack.mehaleq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Topup extends AppCompatActivity {
    static int RECEIVE_SMS_PERMISSION_CODE = 10;

    final static String ARG_INVOICE_DATA = "invoiceData";
    final static String ARG_INVOICE = "invoice";
    float mAmount=5.00f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.CALL_PHONE
            },RECEIVE_SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantedResults){
        if(requestCode == RECEIVE_SMS_PERMISSION_CODE){
            if((grantedResults.length > 0)&&(grantedResults[0]==PackageManager.PERMISSION_GRANTED)&&(grantedResults[1]==PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "SMS Send/Receive is granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
            BankOpenHelper db = new BankOpenHelper(this);
            Cursor cursor = db.getAllBanks();
            if(cursor.getCount() == 0) {
                Intent intent = new Intent(this, AccountSettings.class);
                startActivity(intent);
            }
        }
    }

    public void launch(View view){
        ImageButton btn = (ImageButton)view;

        float amount = 5;

        switch(btn.getId()){
            case R.id.topup_5:
                amount = 5.00f;
                break;
            case R.id.topup_10:
                amount = 10.00f;
                break;
            case R.id.topup_15:
                amount = 15.00f;
                break;
            case R.id.topup_25:
                amount = 25.00f;
                break;
            case R.id.topup_50:
                amount = 50.00f;
                break;
            case R.id.topup_100:
                amount = 100.00f;
                break;
        }
        String strAmount = (new Float(amount)).toString();
        Toast.makeText(this, "About to send " + strAmount + " Birr!", Toast.LENGTH_LONG).show();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date now = new Date();
        InvoiceData invoice = new InvoiceData(df.format(now.getTime()), "0001");
        invoice.AddInvoiceItem("Mobile Card", 1, amount, false, 0);
        String invoiceJson = invoice.getJson();
        Log.d("Topup", invoiceJson);
        Intent intent = new Intent(this, Invoice.class);
        Bundle extraBundle = new Bundle();
        extraBundle.putString(ARG_INVOICE, invoiceJson);
        intent.putExtra(ARG_INVOICE_DATA, extraBundle);
        startActivity(intent);
    }

}
