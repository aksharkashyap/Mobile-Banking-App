package com.akshar.USER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.MainActivity;
import com.akshar.mobilebanking.R;

import java.io.IOException;

public class ChangePassword extends AppCompatActivity {
    String myAccountNum;
    EditText old_et,new_et,confirm_et;
    MyDB db;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        old_et  = findViewById(R.id.old_password_pass_change);
        new_et = findViewById(R.id.new_pass_change);
        confirm_et = findViewById(R.id.reenter_pass_change);
        pb = findViewById(R.id.progressBar_changePass);
        getIntentData();
        db = new MyDB(this);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        myAccountNum = intent.getStringExtra("account");
    }

    private boolean isFieldVerified(String old_pass, String new_pass, String confirm_pass){
        if(old_pass.isEmpty() || new_pass.isEmpty() || confirm_pass.isEmpty()) {
            Toast.makeText(this, "Empty fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isPassMatch(String newPass, String confirmPAss) {
        if(!newPass.equals(confirmPAss)) {
            Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    public void change_btn_password_change(View view){
        String oldPass  = old_et.getText().toString();
        String newPass = new_et.getText().toString();
        String confirmPAss = confirm_et.getText().toString();
        boolean isFieldVerified = isFieldVerified(oldPass,newPass,confirmPAss);
        boolean isPassMatch = isPassMatch(newPass,confirmPAss);
        boolean isOldPassValid = db.getLoginData(myAccountNum,oldPass);
        if(isFieldVerified && isPassMatch){
            if(!isOldPassValid) Toast.makeText(this, "Wrong old password", Toast.LENGTH_SHORT).show();
            else changePassDialog(myAccountNum,newPass);
        }

    }

    private void changePassDialog(final String mAcc, final String mNewPass) {

        //------------------------------------------------------

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Password change request")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.updatePassword(mAcc,mNewPass);
                //
                showProgressBar();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle("Are you sure?");
        alert.show();
        //----------------------------------------------
    }

    private void showProgressBar(){
        pb.setVisibility(View.VISIBLE);
        // thread redirect to login
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                startActivity(intent);
            }
        }).start();
    }

}
