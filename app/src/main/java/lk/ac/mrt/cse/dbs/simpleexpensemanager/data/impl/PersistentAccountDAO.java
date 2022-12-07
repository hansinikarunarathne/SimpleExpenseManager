package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends DataBaseHelper implements AccountDAO {
    private List<String> accountNumbers;
    private List<Account> accounts;

    public PersistentAccountDAO(Context context) {
        super(context);
        this.accountNumbers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    @Override
    public List<String> getAccountNumbersList() {
        this.accountNumbers = new ArrayList<>();

        String queryString = "SELECT " + COLUMN_ACCOUNT_NO + " FROM " + ACCOUNT_TABLE ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                this.accountNumbers.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();
        Log.i("w", String.valueOf(this.accountNumbers.size()));

        return this.accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        this.accounts = new ArrayList<>();

        String queryString = "SELECT * FROM " + ACCOUNT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                String accountNo = cursor.getString(0);
                String BankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);

                Account newAccount = new Account(accountNo,BankName,accountHolderName,balance);
                this.accounts.add(newAccount);

            }while(cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return this.accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String queryString = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NO + "='"+ accountNo +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        Account account ;

        String BankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        Double balance = cursor.getDouble(3);

        account = new Account(accountNo,BankName,accountHolderName,balance);
        cursor.close();
        db.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NO,account.getAccountNo());
        cv.put(COLUMN_BANK_NAME,account.getBankName());
        cv.put(COLUMN_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        cv.put(COLUMN_BALANCE,account.getBalance());

        db.insert(ACCOUNT_TABLE, null, cv);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "DELETE FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NO + "= '"+ accountNo +"';";
        db.execSQL(queryString);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "SELECT " + COLUMN_BALANCE + " FROM " + ACCOUNT_TABLE + " WHERE " + COLUMN_ACCOUNT_NO + "= '"+ accountNo +"';";
        Cursor cursor = db.rawQuery(queryString,null);
        cursor.moveToFirst();

        double balanceAmount = cursor.getDouble(0);
        switch (expenseType) {
            case EXPENSE:
                balanceAmount= balanceAmount -amount;
                break;
            case INCOME:
                balanceAmount= balanceAmount + amount;
                break;
        }

        SQLiteStatement updateQueryString = db.compileStatement("UPDATE " + ACCOUNT_TABLE + " SET " + COLUMN_BALANCE + "= ? WHERE " + COLUMN_ACCOUNT_NO + " = ? ;");
        updateQueryString.bindDouble(1, balanceAmount);
        updateQueryString.bindString(2,accountNo);
        updateQueryString.executeUpdateDelete();
        updateQueryString.close();
        cursor.close();
        db.close();
    }
}
