package com.akshar.USER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;
import com.akshar.mobilebanking.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class FundTransfer extends AppCompatActivity {

    EditText name, transfer_account, amount;
    // int day,month,year;
    String myAccountNum;
    Button transferBtn;
    MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        name = findViewById(R.id.name_fund_transfer);
        transfer_account = findViewById(R.id.account_fund_transfer);
        amount = findViewById(R.id.amount_fund_transfer);
        transferBtn = findViewById(R.id.transfer_btn_fund_transfer);
        db = new MyDB(this);
        getIntentData();
    }

    public void getIntentData() {
        Intent intent = getIntent();
        myAccountNum = intent.getStringExtra("account");
    }

    protected String getDateToday() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH) + 1;
        int year = cldr.get(Calendar.YEAR);
        String mDate = day + "/" + month + "/" + year;
        return mDate;
    }

    private boolean verifyFields(String mName, String mAccount, String mAmount) {
        if (mName.isEmpty())
            return false;
        if (mAccount.isEmpty())
            return false;
        if (mAmount.isEmpty())
            return false;
        return true;
    }

    private boolean verifyBalance(int myBalance, int transerAmount) {
        // check balance------ if( (db.getBal() - transferamount >=0)
        if (myBalance - transerAmount >= 0)
            return true;
        Toast.makeText(this, "Insufficent balance", Toast.LENGTH_SHORT).show();
        return false;
    }

    protected void putIntoInternalStorage(String mAcc, String mTransAcc, String mName, int mAmmount, String status, String mDate, int bal, Context context) throws IOException {

        //mAcc - FileName as key

        String text = mTransAcc + " - " + mName + " - (" + mAmmount + "Rs)- " + status + " - " + mDate + " - " + "Balance:" + bal + "," + "\n";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(mAcc + ".txt", MODE_APPEND);
            fos.write(text.getBytes());
          //  Toast.makeText(this, "saved to " + getFilesDir() + "/" + mAcc + ".txt", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Transaction successful- Mini statement updated!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                fos.close();
        }
    }


    public void transfer_btn_fund_transfer(View view){
        final String mName = name.getText().toString(); // benificiary name
        final String transfer_acc = transfer_account.getText().toString(); // transfer acc
        // verify field
        boolean isFieldVerified = verifyFields(mName, transfer_acc, amount.getText().toString());
        if (!isFieldVerified)
            Toast.makeText(this, "Please Fill all the fields!", Toast.LENGTH_SHORT).show();
        else if (!db.isAccountExist(transfer_acc)) { // if beneficiary does not exist
            Toast.makeText(this, "Account number does not exist in the DB!", Toast.LENGTH_SHORT).show();
        } else if (myAccountNum.equals(transfer_acc)) {
            Toast.makeText(this, "Kindly enter beneficiary's account number", Toast.LENGTH_SHORT).show();
        } else {

        //------------------------------------------------------

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Fund Transfer of "+amount.getText().toString() +" to :" + transfer_acc)
                    .setCancelable(false)
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    int transfer_amount = Integer.parseInt(amount.getText().toString()); // transfer amount
                    int transferAccountBalance = db.getBalance(transfer_acc); //get the balance of the benificiary account (for the updation purpose)
                    int totalUpdationBal = transfer_amount + transferAccountBalance;
                    int myBal = db.getBalance(myAccountNum);
                    String mDate = getDateToday();

                    //verify balance and proceed
                    boolean isBalVerified = verifyBalance(myBal, transfer_amount);
                    if (isBalVerified) {//
                        db.updateBalance(transfer_acc, totalUpdationBal); // call the db function // update the balance of the receiver
                        db.updateBalance(myAccountNum, myBal - transfer_amount); // update the balance of the sender
                        //   aletDialogShow(transfer_acc);
                        try {
                            putIntoInternalStorage(myAccountNum, transfer_acc, mName, transfer_amount, "Debit", mDate, myBal - transfer_amount, getApplicationContext()); // internal storage--- put the date and other fields
                            putIntoInternalStorage(transfer_acc, myAccountNum, mName, transfer_amount, "Credit", mDate, totalUpdationBal, getApplicationContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Are you sure?");
            alert.show();
            //----------------------------------------------

        }
    }
}

