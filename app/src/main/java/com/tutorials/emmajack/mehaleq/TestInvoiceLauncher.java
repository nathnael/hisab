package com.tutorials.emmajack.mehaleq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TestInvoiceLauncher extends AppCompatActivity {

    static int RECEIVE_SMS_PERMISSION_CODE = 10;

    final static String ARG_INVOICE_DATA = "invoiceData";
    final static String ARG_SERVICE_NO = "serviceNo";
    final static String ARG_INVOICE_DATE = "invoiceDate";
    final static String ARG_DRIVER = "driver";
    final static String ARG_DRIVER_BANK = "driverBank";
    final static String ARG_DRIVER_ACCOUNT = "driverAccount";
    final static String ARG_PASSENGER = "passenger";
    final static String ARG_DISTANCE = "distance";
    final static String ARG_AMOUNT = "amount";
    final static String ARG_TAX = "tax";
    final static String ARG_COMMISSION = "commission";
    int mServiceNo = 101;
    String mInvoiceDate = "31-08-2016 17:15";
    String mDriver = "Hailu Kasaye";
    String mDriverBank = "Wegagen Bank";
    String mDriverAccount = "1739/so1/5644";
    String mPassenger = "Ainalem Tesemma";
    float mDistance = 21.0f;
    float mAmount = 5.00f;
    float mTax = 2.50f;
    float mCommission = 1.25f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_invoice_launcher);



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS},RECEIVE_SMS_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantedResults){
        if(requestCode == RECEIVE_SMS_PERMISSION_CODE){
            if((grantedResults.length > 0)&&(grantedResults[0]== PackageManager.PERMISSION_GRANTED)&&(grantedResults[1]==PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "SMS Send/Receive is granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    public  void read(View view){
        TextView v = (TextView) findViewById(R.id.adresses);
        Log.d("Inbox", "View found");
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        String addressFrom = "";
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {

                addressFrom += " " + cursor.getString(cursor.getColumnIndexOrThrow("address")) + "\t";

                // use msgData
            } while (cursor.moveToNext());
        }
        v.setText(addressFrom);

    }
    public void launch(View view) {
        Toast.makeText(this, "hello!", Toast.LENGTH_LONG);
        Intent intent = new Intent(this, Invoice.class);
        Bundle extraBundle = new Bundle();
        extraBundle.putInt(ARG_SERVICE_NO, mServiceNo);
        extraBundle.putString(ARG_INVOICE_DATE, mInvoiceDate);
        extraBundle.putString(ARG_DRIVER, mDriver);
        extraBundle.putString(ARG_DRIVER_BANK, mDriverBank);
        extraBundle.putString(ARG_DRIVER_ACCOUNT, mDriverAccount);
        extraBundle.putString(ARG_PASSENGER, mPassenger);
        extraBundle.putFloat(ARG_DISTANCE, mDistance);
        extraBundle.putFloat(ARG_AMOUNT, mAmount);
        extraBundle.putFloat(ARG_TAX, mTax);
        extraBundle.putFloat(ARG_COMMISSION, mCommission);
        intent.putExtra(ARG_INVOICE_DATA, extraBundle);
        startActivity(intent);
    }
}
