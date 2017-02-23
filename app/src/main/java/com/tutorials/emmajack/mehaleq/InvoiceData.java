package com.tutorials.emmajack.mehaleq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TechSolutions2 on 1/25/2017.
 */
public class InvoiceData {
    private String mInvoiceDate;
    private String mInvoiceNumber;
    private ArrayList<InvoiceItemData> mInvoiceItems;
    public InvoiceData(String invoiceDate, String invoiceNumber){
        mInvoiceDate = invoiceDate;
        mInvoiceNumber = invoiceNumber;
        mInvoiceItems = new ArrayList<InvoiceItemData>();
    }

    public String getInvoiceDate(){
        return mInvoiceDate;
    }

    public String getInvoiceNumber(){
        return mInvoiceNumber;
    }

    public void AddInvoiceItem(String description, int quantity, float unitPrice, boolean taxable, float taxRate){
        mInvoiceItems.add(new InvoiceItemData(
                description, quantity, unitPrice, taxable, taxRate
        ));
    }
    public ArrayList<InvoiceItemData> getInvoiceItems(){
        return mInvoiceItems;
    }

    public float getInvoiceAmount(){
        float invoiceAmount = 0;
        for(int i=0; i<mInvoiceItems.size(); i++){
            InvoiceItemData item = mInvoiceItems.get(i);
            invoiceAmount += (item.getQuantity()*item.getUnitPrice())*(1+(item.getTaxable()?item.getTaxRate():0));
        }
        return invoiceAmount;
    }

    public String getJson(){
        String json = "{InvoiceDate:'" + mInvoiceDate + "';InvoiceNumber:'" + mInvoiceNumber + "';InvoiceItems:[";
        for(int i = 0; i<mInvoiceItems.size(); i++){
            InvoiceItemData itemData = mInvoiceItems.get(i);
            String itemJson = "{Description:'" + itemData.getDescription() + "';"
                    + "Quantity:" + itemData.getQuantity() +";"
                    + "UnitPrice:" + itemData.getUnitPrice() +";"
                    + "Taxable:" + itemData.getTaxable() +";"
                    + "TaxRate:" + itemData.getTaxRate() +"}";
            json += (i==0?"":",") + itemJson;
        }
        json += "]}";
        return json;
    }
    public static InvoiceData parse(String jsonString){
        InvoiceData invoiceData = null;
        if((jsonString != null) || (jsonString != "empty.")){
            try {
                JSONObject invoiceJson = new JSONObject(jsonString) ;
                invoiceData = new InvoiceData(invoiceJson.getString("InvoiceDate"),
                    invoiceJson.getString("InvoiceNumber"));
                JSONArray itemList = invoiceJson.getJSONArray("InvoiceItems");
                for(int i=0; i<itemList.length();i++){
                    JSONObject itemJson = itemList.getJSONObject(i);
                    invoiceData.AddInvoiceItem(
                            itemJson.getString("Description"),
                            itemJson.getInt("Quantity"),
                            (float)itemJson.getDouble("UnitPrice"),
                            itemJson.getBoolean("Taxable"),
                            (float)itemJson.getDouble("TaxRate")
                    );
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return invoiceData;

    }
}
