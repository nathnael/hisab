package com.tutorials.emmajack.mehaleq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PaymentConfirmationReceiver extends BroadcastReceiver {

    static String TAG = PaymentConfirmationReceiver.class.getSimpleName();
    static String TOPUP_REQUEST_TARGET = "+251911621749";
    static String BANK_CONFIRMATION_SOURCE = "United Bank";
    static String BANK_USSD_SHORT_CODE = "811";

    public PaymentConfirmationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Get the sms data bound  to intent
        Log.d(TAG, "Message received.");
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        String str = "";

        if (bundle != null) {
            //Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            Calendar cl = Calendar.getInstance();
            //For every SMS message received
            for (int i = 0; i < msgs.length; i++) {
                //Covert Object array;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = bundle.getString("format");
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                String messageBody = msgs[i].getDisplayMessageBody();
                String messageAddress = msgs[i].getDisplayOriginatingAddress();
                if(BANK_CONFIRMATION_SOURCE.equals(messageAddress)){
                    Log.d(TAG, messageBody);
                    Pattern mPattern = Pattern.compile("^Funds\\stransfer\\ssuccessful.\\sAmount\\stransfered\\sETB:(\\d{1,3}(?:,\\d{3})*(?:.\\d*))");
                    Matcher matcher = mPattern.matcher(messageBody);
                    if(matcher.find()){
                        String ammount = matcher.group(1);
                        String requestBody = "Topup " + BANK_USSD_SHORT_CODE + " " + ammount;
                        Log.d(TAG,TOPUP_REQUEST_TARGET + ": " +requestBody);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(TOPUP_REQUEST_TARGET, null, requestBody, null, null);
                    }
                    break;
                }
                if(msgs[1].getDisplayOriginatingAddress().equals(TOPUP_REQUEST_TARGET)){
                    Log.d(TAG, messageBody);
                    Pattern mPattern = Pattern.compile("^Topup\\s(\\+2519\\d{8})\\s(\\+2519\\d{8})(\\d{14})");
                    Matcher matcher = mPattern.matcher(messageBody);
                    if(matcher.find()){
                        String thisPhone = matcher.group(1);
                        String beneficiaryPhone = matcher.group(2);
                        String cardNo = matcher.group(3);
                        String encodedHash = Uri.encode("#");
                        String ussdMessage = "*805*" + cardNo + encodedHash;

                        if(thisPhone.equals(beneficiaryPhone)){
                            Log.d("Topup", ussdMessage);
                            context.startActivity(new Intent("android.intent.action.CALL",
                                    Uri.parse("tel:"+ ussdMessage)));
                        }
                    }
                }
            }
        }
    }
}
