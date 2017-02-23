package com.tutorials.emmajack.mehaleq;

/**
 * Created by TechSolutions2 on 1/25/2017.
 */
public class InvoiceItemData {
    private String mDescription;
    private int mQuantity;
    private float mUnitPrice;
    private boolean mTaxable;
    private float mTaxRate;

    public InvoiceItemData(String description, int quantity, float unitPrice, boolean taxable, float taxRate){
        mDescription = description;
        mQuantity = quantity;
        mUnitPrice = unitPrice;
        mTaxable = taxable;
        mTaxRate = taxRate;
    }

    public String getDescription(){
        return mDescription;
    }

    public int getQuantity(){
        return mQuantity;
    }

    public  float getUnitPrice(){
        return mUnitPrice;
    }

    public boolean getTaxable(){
        return mTaxable;
    }

    public float getTaxRate(){
        return mTaxRate;
    }
}
