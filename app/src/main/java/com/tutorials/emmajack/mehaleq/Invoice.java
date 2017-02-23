package com.tutorials.emmajack.mehaleq;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Invoice extends FragmentActivity
    implements AccountFragment.OnBankAccountSelectedListener,
        PasswordFragment.OnPasswrodEnteredListener,
        InvoiceFragment.OnPayInvoiceListener {
    final static String ARG_INVOICE_DATA = "invoiceData";
    final static String ARG_INVOICE ="invoice";
    InvoiceData mInvoice;
    String mInvoiceJson;
    int mBankId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view);
        if(savedInstanceState != null){
            //Being restored from a previous instance.
            return;
        }
        //Extracting data from intent
        Bundle intentBundle = getIntent().getBundleExtra(ARG_INVOICE_DATA);
        if(intentBundle != null) {
            mInvoiceJson = intentBundle.getString(ARG_INVOICE);
            mInvoice = InvoiceData.parse(mInvoiceJson);
        }
        BankOpenHelper db = new BankOpenHelper(this);
        //db.resetDatabase();
        Cursor cursor = db.getAllBanks();
        if(cursor.getCount() == 0) {
            BankData data = new BankData();
            ArrayList<BankData.BankRecord> list = data.getBankData("url");
            for (int i = 0; i < list.size(); i++) {
                db.insertBank(list.get(i).getBankName(), list.get(i).getUssd(), list.get(i).getTransferOption(), list.get(i).getMehaleqAccount());
            }
        }
        //if no bank account propmt user to setup atleast one bank account
        cursor = db.getAllAccounts();
        if(cursor.getCount() == 0){
            db.insertAccount(1, "1030416265713011");
        }

        if (findViewById(R.id.mehaleq_container) != null) {

            // Create an instance of InvoiceFragment
            InvoiceFragment invoiceFragment = new InvoiceFragment();

            //Pass arguments
            Bundle args = new Bundle();
            args.putString(ARG_INVOICE, mInvoiceJson);
            invoiceFragment.setArguments(args);

            // Add the fragment to the 'mehaleq_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mehaleq_container, invoiceFragment).commit();
        }
    }

    @Override
    public void OnBankAccountSelected(int bankId) {
        mBankId = bankId;
        PasswordFragment passwordFragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putInt(passwordFragment.ARG_BANK_ID, bankId);
        passwordFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.mehaleq_container, passwordFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void OnPasswordEntered(String password, String transaction){
        BankOpenHelper db = new BankOpenHelper(this);
        Cursor cursor = db.getBankData(mBankId);
        cursor.moveToFirst();
        String ussd = cursor.getString(cursor.getColumnIndex("ussd"));
        String option = cursor.getString(cursor.getColumnIndex("transfer_option"));
        String mehaleqAccount = cursor.getString(cursor.getColumnIndex("mehaleq_account"));
        String encodedHash = Uri.encode("#");
        int iAmount = (int)mInvoice.getInvoiceAmount();
        String ussdMessage = "*" + ussd + "*" + password + "*" + option + "*" + mehaleqAccount + "*" + iAmount + "*" + "99" + "*" + transaction + "*" + 1 + encodedHash;
        Log.d("Invoice", ussdMessage);
        startActivity(new Intent("android.intent.action.CALL",
                Uri.parse("tel:"+ ussdMessage)));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Toast.makeText(this,"hello!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnPayInvoice(){
        AccountFragment accountFragment = new AccountFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.mehaleq_container, accountFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }
}
