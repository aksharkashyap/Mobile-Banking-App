package com.akshar.USER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;

import java.io.IOException;
import java.util.Calendar;

public class AddBalance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
    }

    //---------------------BALANCE-------------------------------------------------------------------
    private boolean isAmountValidated(String amount) {
        if (amount.isEmpty())
            return false;
        return true;
    }

    public void add_balance(String amount, MyDB mDB, String myAcc, Context context) throws IOException {
        FundTransfer FT = new FundTransfer();
        int mAmount = Integer.parseInt(amount);
        //get user's previous balance
        int myBal = mDB.getBalance(myAcc);
        int updationBal = mAmount + myBal;
        //update balance
        mDB.updateBalance(myAcc, updationBal);
        //update ministatement
        String mDate = FT.getDateToday();
        FT.putIntoInternalStorage(myAcc, myAcc, "Self", mAmount, "Credit", mDate, updationBal,context);
    }

    public void alertDialogMoneyAdd(final String myAcc, final MyDB mDB, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Balance:");

        final EditText input = new EditText(context);
        input.setHint("     Enter Amount");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isAmountValidated(input.getText().toString())) {
                    try {
                        add_balance(input.getText().toString(), mDB, myAcc, context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Sucess!! Amount " + input.getText().toString() + " has been added", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Please add amount!!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    //----------------------------------BALANCE-------------------------------------------------------------
}
