package com.tutorials.emmajack.mehaleq;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by TechSolutions2 on 9/22/2016.
 */
public class BankData {

    public class BankRecord {
        private Integer mId;
        private String mBankName;
        private String mUssd;
        private String mTransferOption;
        private String mMehaleqAccount;

        public void setId(Integer id){
            mId = id;
        }

        public void setBankName(String bankName){
            mBankName = bankName;
        }

        public void setUssd(String ussd){
            mUssd = ussd;
        }

        public void setTransferOption(String transferOption){
            mTransferOption = transferOption;
        }

        public void setMehaleqAccount(String mehaleqAccount){
            mMehaleqAccount = mehaleqAccount;
        }

        public Integer getId(){
            return mId;
        }

        public String getBankName(){
            return mBankName;
        }

        public String getUssd(){
            return mUssd;
        }

        public String getTransferOption(){
            return mTransferOption;
        }

        public String getMehaleqAccount(){
            return mMehaleqAccount;
        }
    }

    public ArrayList<BankRecord> getBankData(String url){
        String response = callWebService(url);
        return parseJson(response);
    }

    private String callWebService(String url){
        String response = "[{id:1;name:'United Bank';ussd:'811';transfer_option:'4';mehaleq_account:'1030416265713011'}]";
        try {
            URL serviceUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)serviceUrl.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return response;
    }

    private ArrayList<BankRecord> parseJson(String jsonString){
        ArrayList<BankRecord> bankRecords = new ArrayList<BankRecord>();
        if((jsonString != null) || (jsonString != "empty.")){
            try {
                JSONArray bankList = new JSONArray(jsonString);
                for(int i=0; i<bankList.length();i++){
                    BankRecord record = new BankRecord();
                    JSONObject jsonBank = bankList.getJSONObject(i);
                    record.setId(jsonBank.getInt(BankOpenHelper.BANK_ID));
                    record.setBankName(jsonBank.getString(BankOpenHelper.BANK_NAME));
                    record.setUssd((jsonBank.getString(BankOpenHelper.BANK_USSD)));
                    record.setTransferOption((jsonBank.getString(BankOpenHelper.TRANSFER_OPTION)));
                    record.setMehaleqAccount((jsonBank.getString(BankOpenHelper.MEHALEQ_ACCOUNT)));
                    bankRecords.add(record);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return bankRecords;
    }
}

