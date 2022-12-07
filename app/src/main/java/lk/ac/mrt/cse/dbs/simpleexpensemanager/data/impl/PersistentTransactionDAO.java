package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends DataBaseHelper implements TransactionDAO {
    private List<Transaction> transactionLogList;
    private List<Transaction> paginatedTransactionLogs;

    public PersistentTransactionDAO(Context context) {
        super(context);
        List<Transaction> transactionLogList = new ArrayList<>();
        List<Transaction> paginatedTransactionLogs = new ArrayList<>();

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String Date = formatter.format(date);

        cv.put(COLUMN_DATE,Date);
        cv.put(COLUMN_EXPENSE_TYPE, String.valueOf(expenseType));
        cv.put(COLUMN_AMOUNT,amount);
        cv.put(COLUMN_ACCOUNT_NO,accountNo);

        db.insert(TRANSACTION_TABLE,null,cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs()  {
        this.transactionLogList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTION_TABLE ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do {

                String date = cursor.getString(1);
                Date transDate=null;
                try {
                    transDate = new SimpleDateFormat("MM/dd/yyyy").parse(date);
                } catch (Exception e) {

                }

                String typeExpense = cursor.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf(typeExpense.toUpperCase());

                double amount = cursor.getDouble(4);
                String accountNo = cursor.getString(3);

                Transaction newTransaction = new Transaction(transDate,accountNo,expenseType,amount);
                this.transactionLogList.add(newTransaction);


            }while(cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return this.transactionLogList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        this.paginatedTransactionLogs = new ArrayList<>();
        paginatedTransactionLogs = getAllTransactionLogs();
        int size = paginatedTransactionLogs.size();
        if (size <= limit) {
            return paginatedTransactionLogs;
        }
        // return the last <code>limit</code> number of transaction logs
        return paginatedTransactionLogs.subList(size - limit, size);
    }
}
