package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String ACCOUNT_TABLE = "ACCOUNT";
    public static final String COLUMN_ACCOUNT_NO = "ACCOUNT_NO";
    public static final String COLUMN_BANK_NAME = " BANK_NAME";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
    public static final String COLUMN_BALANCE = "BALANCE";
    public static final String TRANSACTION_TABLE = "TRANSACTION_TABLE";
    public static final String COLUMN_TRANSACTION_ID = "TRANSACTION_ID";
    public static final String COLUMN_DATE = " DATE";
    public static final String COLUMN_EXPENSE_TYPE = "EXPENSE_TYPE";
    public static final String COLUMN_AMOUNT = "AMOUNT";

    public DataBaseHelper(Context context) {
        super(context, "200295J.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("sa","m");
        String createTableAccount = "CREATE TABLE " + ACCOUNT_TABLE + " ( " + COLUMN_ACCOUNT_NO + " TEXT PRIMARY KEY ," + COLUMN_BANK_NAME + " TEXT , " + COLUMN_ACCOUNT_HOLDER_NAME + " TEXT, " + COLUMN_BALANCE + " REAL )";
        db.execSQL(createTableAccount);

        String createTableTransaction = "CREATE TABLE " + TRANSACTION_TABLE + " ( " + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE + " TEXT , " + COLUMN_EXPENSE_TYPE + " TEXT , " + COLUMN_ACCOUNT_NO + " TEXT ," + COLUMN_AMOUNT + " REAL , FOREIGN KEY ( " + COLUMN_ACCOUNT_NO + ") REFERENCES " + ACCOUNT_TABLE + "(" + COLUMN_ACCOUNT_NO + "))";
        db.execSQL(createTableTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
