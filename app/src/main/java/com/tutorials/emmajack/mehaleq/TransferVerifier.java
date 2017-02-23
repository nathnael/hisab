package com.tutorials.emmajack.mehaleq;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by TechSolutions2 on 10/8/2016.
 */
public class TransferVerifier extends AccessibilityService {

    public void TransferVerifier(){

    }
    public static String TAG = "MEHALEQ";


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.d(TAG, "onAccessibilityEvent");
        String text = event.getText().toString();

        if(event.getClassName().equals("android.app.AlertDialog")){
            //performGlobalAction(GLOBAL_ACTION_BACK);
            Log.d(TAG, text);
            Intent intent = new Intent("com.android.phone.action.REFERESH");
            intent.putExtra("message", text);
        }
    }

    @Override
    public void onInterrupt(){

    }

    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);

    }
}
