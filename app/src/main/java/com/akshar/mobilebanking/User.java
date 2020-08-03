package com.akshar.mobilebanking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akshar.DB.MyDB;
import com.akshar.USER.AddBalance;
import com.akshar.USER.ChangePassword;
import com.akshar.USER.FundTransfer;
import com.akshar.USER.MiniStatement;
import com.akshar.USER.MyProfile;

import java.io.IOException;

public class User extends AppCompatActivity {
    public String myAccountNum, myName;
    AddBalance AB;
    TextView username;
    int bal;
    MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        username = findViewById(R.id.textGrid2);
        getIntentData();
        AB = new AddBalance();
        db = new MyDB(this);

    }

    public void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        myAccountNum = bundle.getString("account");
        myName = bundle.getString("name");
        username.setText("Welcome: "+ myName + " (Acc: "+myAccountNum +")");
    }

    public void get_bal_user(View view) {
        bal = db.getBalance(myAccountNum);
        Toast.makeText(this, "Balance: "+ bal, Toast.LENGTH_SHORT).show();
    }

    public void quickFundTransfer_user(View view) {
        Intent intent = new Intent(User.this, FundTransfer.class);
        intent.putExtra("account",myAccountNum);
        startActivity(intent);
    }

    public void get_mini_statement(View view) {
        Intent intent = new Intent(User.this, MiniStatement.class);
        intent.putExtra("account",myAccountNum);
        startActivity(intent);
    }
    public void add_bal_user(View view) {
       AB.alertDialogMoneyAdd(myAccountNum,db,this);
    }


    public void update_password_user(View view) {
        Intent intent = new Intent(User.this, ChangePassword.class);
        intent.putExtra("account",myAccountNum);
        startActivity(intent);
    }

    public void myprofile_user(View view) {
        Intent intent = new Intent(User.this, MyProfile.class);
        intent.putExtra("account",myAccountNum);
        startActivity(intent);
    }
}
