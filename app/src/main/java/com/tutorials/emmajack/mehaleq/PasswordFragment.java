package com.tutorials.emmajack.mehaleq;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TechSolutions2 on 8/23/2016.
 */
public class PasswordFragment extends Fragment {
    public interface OnPasswrodEnteredListener{
        public void OnPasswordEntered(String password, String transaction);
    }
    final static String ARG_BANK_ID = "bankId";
    OnPasswrodEnteredListener mPasswordEnteredCallback;
    int mSelectedBank = -1;
    String mAccount = "";
    Button mBtnPay;
    EditText mPassword;
    EditText mTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(savedInstanceState != null){
            mSelectedBank = savedInstanceState.getInt(ARG_BANK_ID);
        }
        View passwordView = inflater.inflate(R.layout.password_view, container, false);
        mBtnPay = (Button)passwordView.findViewById(R.id.pay);
        mPassword = (EditText)passwordView.findViewById(R.id.password);
        mTransaction = (EditText)passwordView.findViewById(R.id.transaction);
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassword.getText().toString();
                String transaction = mTransaction.getText().toString();
                if(password.isEmpty()){
                    Toast.makeText(getActivity(), "Password can't be empty! Try again.", Toast.LENGTH_LONG).show();
                } else if(transaction.isEmpty()) {
                    Toast.makeText(getActivity(), "Password can't be empty! Try again.", Toast.LENGTH_LONG).show();
                }
                else {
                    mPasswordEnteredCallback.OnPasswordEntered(password, transaction);
                }
            }
        });
        return passwordView;
    }

    @Override
    public void onStart(){
        super.onStart();
        Bundle args = getArguments();
        if(args != null){
            updatePasswordView(args.getInt(ARG_BANK_ID));
        } else {
            updatePasswordView(mSelectedBank);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_BANK_ID, mSelectedBank);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            Activity hostActivity = (Activity)context;
            try {
                mPasswordEnteredCallback = (OnPasswrodEnteredListener) hostActivity;
            } catch (ClassCastException e) {
                throw new ClassCastException(hostActivity.toString()
                        + " must implement OnPasswordEnteredListener");
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " Invalid context");
        }
    }
    public void updatePasswordView(int bankId){
        BankOpenHelper db = new BankOpenHelper(this.getActivity());
        Cursor cursor = db.getBankAccount(bankId);
        cursor.moveToFirst();
        String bank = cursor.getString(cursor.getColumnIndex("bank_name"));
        mAccount = cursor.getString(cursor.getColumnIndex("acc_number"));
        Activity activity = getActivity();
        TextView bankText = (TextView)activity.findViewById(R.id.bank);
        bankText.setText(bank);
        TextView accountText = (TextView)activity.findViewById(R.id.account);
        accountText.setText(mAccount);
        mSelectedBank = bankId;
    }
}
