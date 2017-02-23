package com.tutorials.emmajack.mehaleq;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountSettings extends AppCompatActivity {

    Integer mSelectedBankId=-1;
    TextView mBankAccount;
    Button mBtnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);
        mBankAccount = (TextView)findViewById(R.id.bank_account);
        mBtnSave = (Button)findViewById(R.id.save);
        final BankOpenHelper db = new BankOpenHelper(this);
        Cursor cursor = db.getAllBanks();
        if(cursor.getCount() == 0) {
            BankData data = new BankData();
            ArrayList<BankData.BankRecord> list = data.getBankData("url");
            for (int i = 0; i < list.size(); i++) {
                db.insertBank(list.get(i).getBankName(), list.get(i).getUssd(), list.get(i).getTransferOption(), list.get(i).getMehaleqAccount());
            }
            cursor = db.getAllBanks();
        }

        cursor.moveToFirst();
        final ArrayList<Pair<Integer, String>> values = new ArrayList<Pair<Integer, String>>();
        while(!cursor.isAfterLast()){
            String name=cursor.getString(cursor.getColumnIndex("name"));
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            values.add(new Pair<Integer, String>(id,name));
            cursor.moveToNext();
        }

        final ArrayAdapter<Pair<Integer, String>> adapter = new ArrayAdapter<Pair<Integer, String>>(this,android.R.layout.simple_list_item_1, values){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                Pair<Integer, String> data = values.get(position);
                TextView row;
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
                }else{
                    row = (TextView) convertView;
                }
                ((TextView)row).setText(data.second);
                return row;
            }
        };
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankAccount = mBankAccount.getText().toString();
                if(!bankAccount.equals("")) {
                    db.insertAccount(mSelectedBankId, bankAccount);
                    finish();
                } else {
                    Toast.makeText(v.getContext(), "Please enter you bank account!", Toast.LENGTH_LONG).show();
                }
            }
        });
        ListView listView = (ListView)findViewById(R.id.account_banks);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
                int count = parent.getChildCount();
                for(int i=0; i<count; i++){
                    parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                }
                Pair<Integer, String> data = (Pair<Integer, String>)parent.getItemAtPosition(position);
                mSelectedBankId = data.first;
                final View itemView = parent.getChildAt(position);
                itemView.setBackgroundColor(Color.RED);
                mBtnSave.setEnabled(true);
                mBankAccount.setEnabled(true);
            }
        });
    }
}
