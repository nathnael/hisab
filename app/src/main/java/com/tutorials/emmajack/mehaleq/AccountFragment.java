package com.tutorials.emmajack.mehaleq;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
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

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Created by TechSolutions2 on 8/22/2016.
 */
public class AccountFragment extends Fragment {
    public interface OnBankAccountSelectedListener{
        public void OnBankAccountSelected(int bankId);
    }
    OnBankAccountSelectedListener accountSelectedCallBack;
    Button btnSelect;
    Integer selectBankId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View accountView = inflater.inflate(R.layout.bank_account, container, false);
        ListView listView = (ListView)accountView.findViewById(R.id.banks);
        btnSelect = (Button)accountView.findViewById(R.id.select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSelectedCallBack.OnBankAccountSelected(selectBankId);
            }
        });
        BankOpenHelper db = new BankOpenHelper(this.getActivity());
        Cursor cursor = db.getAllBanks();
        cursor.moveToFirst();
        final ArrayList<Pair<Integer, String>> values = new ArrayList<Pair<Integer, String>>();
        while(!cursor.isAfterLast()){
            String name=cursor.getString(cursor.getColumnIndex("name"));
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            values.add(new Pair<Integer, String>(id,name));
            cursor.moveToNext();
        }

        final ArrayAdapter<Pair<Integer, String>> adapter = new ArrayAdapter<Pair<Integer, String>>(this.getActivity(),android.R.layout.simple_list_item_1, values){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                Pair<Integer, String> data = values.get(position);
                TextView row;
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
                }else{
                    row = (TextView) convertView;
                }
                ((TextView)row).setText(data.second);
                return row;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
                int count = parent.getChildCount();
                for(int i=0; i<count; i++){
                    parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                }
                Pair<Integer, String> data = (Pair<Integer, String>)parent.getItemAtPosition(position);
                selectBankId = data.first;
                final View itemView = parent.getChildAt(position);
                itemView.setBackgroundColor(Color.RED);
                btnSelect.setEnabled(true);
            }
        });
        return accountView;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            Activity hostActivity = (Activity)context;
            try {
                accountSelectedCallBack = (OnBankAccountSelectedListener) hostActivity;
            } catch (ClassCastException e) {
                throw new ClassCastException(hostActivity.toString()
                        + " must implement OnBankAccountSelectedListener");
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " Invalid context");
        }
    }
}
