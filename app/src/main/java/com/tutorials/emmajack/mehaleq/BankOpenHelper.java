package com.tutorials.emmajack.mehaleq;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TechSolutions2 on 8/13/2016.
 */
public class BankOpenHelper extends SQLiteOpenHelper {

    public static final String BANK_ID = "id";
    public static final String BANK_NAME = "name";
    public static final String BANK_USSD = "ussd";
    public static final String TRANSFER_OPTION = "transfer_option";
    public static final String MEHALEQ_ACCOUNT = "mehaleq_account";
    private static final String DATABASE_NAME = "mehaleq.db";
    private static final int DATABASE_VERSION = 2;
    private static final String BANK_TABLE_NAME = "bank";
    private static final String ACCOUNT_TABLE_NAME = "account";
    private static final String BANK_TABLE_CREATE =
            "CREATE TABLE " + BANK_TABLE_NAME + " (" +
                    BANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BANK_NAME + " TEXT, " +
                    BANK_USSD + " TEXT, " +
                    TRANSFER_OPTION + " TEXT, " +
                    MEHALEQ_ACCOUNT + " TEXT" +
                    ");";
    private static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE " + ACCOUNT_TABLE_NAME + " (" +
                    "bank_id INTEGER, " +
                    "acc_number TEXT, " +
                    "balance REAL, " +
                    "FOREIGN KEY (bank_id) REFERENCES bank (id) ON DELETE CASCADE);";
    BankOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(BANK_TABLE_CREATE);
        db.execSQL(ACCOUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BANK_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertBank(String name, String ussd, String transferOption, String mehaleqAccount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("ussd", ussd);
        contentValues.put("transfer_option", transferOption);
        contentValues.put("mehaleq_account", mehaleqAccount);
        db.insert(BANK_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertAccount(Integer bankId, String accountNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bank_id", bankId);
        contentValues.put("acc_number", accountNumber);
        contentValues.put("balance", 0);
        db.insert(ACCOUNT_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getBankData(Integer bankId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from bank where id="+bankId+"", null);
        return cursor;
    }

    public Cursor getAllBanks(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from bank", null);
        return  cursor;
    }

    public Cursor getAllAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from account", null);
        return cursor;
    }
    public Cursor getBankAccount(Integer bankId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select b.name as bank_name, b.ussd as ussd, b.transfer_option as transfer_option, b.mehaleq_account as mehaleq_account, a.acc_number as acc_number, a.balance as balance " +
                "from bank b inner join account a on b.id = a.bank_id where a.bank_id="+bankId+"", null);
        return  cursor;
    }

    public Cursor getBankAccount(String bank){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select b.name as bank_name, b.ussd as ussd, b.transfer_option as transfer_option, b.mehaleq_account as mehaleq_account, a.acc_number as acc_number, a.balance as balance " +
                "from bank b inner join account a on b.id = a.bank_id where b.name='" + bank + "'", null);
        return cursor;
    }

    public void resetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BANK_TABLE_NAME);
        onCreate(db);
    }
}
