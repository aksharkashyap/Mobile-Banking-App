package com.akshar.ADMIN;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;

import java.util.ArrayList;

public class UpdateUser extends AppCompatActivity{

    private EditText name,email,father,dob,address,phone;
    private CheckBox service_sms, service_atm, service_netbanking;
    private RadioGroup gender_rb, acc_type_rb;
    private RadioButton gender_m,gender_f, acc_type_saving, acc_type_current;
    private MyDB db;
    private EditText enterAcc;
    private Button getDataBtn, updateDataBtn,resetGetData,deleteButton;
    AddUser user;
    ArrayList<String> seen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        //------------DB--------------------
        db = new MyDB(this);
        //----------------------------------
        user = new AddUser();
        seen = new ArrayList<>();
        //-------------referencing of variables------------------
        name = findViewById(R.id.update_user_name);
        email = findViewById(R.id.update_user_email);
        father = findViewById(R.id.update_user_father);
        dob = findViewById(R.id.update_user_dob);
        address = findViewById(R.id.update_user_address);
        phone = findViewById(R.id.update_user_mobile);
        gender_rb = findViewById(R.id.update_user_gender);
        gender_m = findViewById(R.id.update_user_rb_male);
        gender_f = findViewById(R.id.update_user_rb_female);
        service_sms = findViewById(R.id.update_user_cb_sms);
        service_atm = findViewById(R.id.update_user_cb_atm);
        service_netbanking = findViewById(R.id.update_user_cb_netbanking);
        enterAcc = findViewById(R.id.update_user_getAcc);
        getDataBtn = findViewById(R.id.update_user_get_data);
        updateDataBtn = findViewById(R.id.update_user_submit);
        resetGetData = findViewById(R.id.update_user_resetGetdata);
        deleteButton = findViewById(R.id.update_user_delete);
        //----------------------------------------------------
        acc_type_rb = findViewById(R.id.update_user_account_type);
        acc_type_saving = findViewById(R.id.update_user_savingAcc);
        acc_type_current = findViewById(R.id.update_user_currentAcc);

    }

    public void setRadioCheckBoxFields(String gender, String services, String mAccType){
        String trimm = services.replaceAll(","," ").trim();
        String arr[] = trimm.split(" ");
        for(int i=0;i<arr.length;i++)
        {
            seen.add(arr[i]);
        }

        String str="";
        for(String s: seen){
            str+= "\n" +s;
        }

        //--------Gender--------------------
        if(gender_f.getText().toString().equals(gender))
            gender_f.setChecked(true);
        else gender_m.setChecked(true);
        //--------Services-------------------
        if(seen.contains(service_atm.getText().toString()))
            service_atm.setChecked(true);
        if(seen.contains("NET"))
            service_netbanking.setChecked(true);
        if(seen.contains(service_sms.getText().toString()))
            service_sms.setChecked(true);
        //----------acc type-------------------
        if(acc_type_saving.getText().toString().equals(mAccType))
            acc_type_saving.setChecked(true);
        else acc_type_current.setChecked(true);
        //--------------------------------------
        seen.clear();
    }

    public void update_user_getData(View view) {
        String ac = enterAcc.getText().toString();
        if(ac.isEmpty())
            Toast.makeText(this, "Please enter your account", Toast.LENGTH_SHORT).show();
        else if(!db.isAccountExist(ac))
            Toast.makeText(this, "Account Does not Exist!!", Toast.LENGTH_SHORT).show();
        else{

                String dataArr[] = db.readUserDB(ac);

                //--------Setting Required fields
                name.setText(dataArr[0]);
                email.setText(dataArr[1]);
                phone.setText(dataArr[2]);
                father.setText(dataArr[3]);
                dob.setText(dataArr[4]);
                address.setText(dataArr[5]);
                //Set Radio & checkbox fields
                setRadioCheckBoxFields(dataArr[6],dataArr[7],dataArr[8]);
                Toast.makeText(this, "Got The data", Toast.LENGTH_SHORT).show();
                getDataBtn.setEnabled(false);
                getDataBtn.setBackground(getDrawable(R.drawable.bg_ui2));
                resetGetData.setEnabled(true);
                resetGetData.setVisibility(View.VISIBLE);
                updateDataBtn.setEnabled(true);
                deleteButton.setEnabled(true);
                deleteButton.setBackground(getDrawable(R.drawable.bg_ui));

        }
    }

    public void update_user_submit(View view) {
        //validation
        if(getDataBtn.isEnabled()) {
            Toast.makeText(this, "Please Get The Data First", Toast.LENGTH_SHORT).show();
            return;
        }
        //if validated then get the strings from the edit text fields and send it to the updateDB
        String mName,mEmail,mPhone,mFather,mDob,mAddress,mAccount;
        mName = name.getText().toString().toUpperCase();
        mEmail = email.getText().toString();
        mPhone = phone.getText().toString();
        mFather = father.getText().toString().toUpperCase();
        mDob = dob.getText().toString();
        mAddress = address.getText().toString().toUpperCase();
        mAccount = enterAcc.getText().toString();
        //Get the gender and services
        String mGender = user.get_gender(gender_rb);
        String mServices = user.get_checked_Services(service_atm,service_sms,service_netbanking);
        String mType = user.get_account_type(acc_type_rb);
        db.updateData(mAccount,mEmail, mPhone, mName, mFather, mAddress, mGender, mDob, mServices);
        db.updateData(mAccount,mType); // updating account type
        aletDialogShow(mAccount," Has been updated");
    }

    public void update_user_resetGetDataFunc(View view) {
        getDataBtn.setEnabled(true);
        getDataBtn.setBackground(getDrawable(R.drawable.bg_ui));
        resetGetData.setEnabled(false);
        resetGetData.setVisibility(View.INVISIBLE);
        updateDataBtn.setEnabled(false);
        deleteButton.setBackground(getDrawable(R.drawable.bg_ui2));
        deleteButton.setEnabled(false);
        user.resetFieldsData(email,phone,name,father,address,gender_rb,dob,
                service_atm,service_sms,service_netbanking,acc_type_rb);
    }

    public void delete_user(View view) {
        if(!getDataBtn.isEnabled()) {
            String acc = enterAcc.getText().toString();
            db.deleteUserData(acc);
            db.deleteTransactData(acc);
            db.deleteLoginData(acc);
            update_user_resetGetDataFunc(view);
            aletDialogShow(acc," Has been deleted.");
        }
    }


    public void aletDialogShow(String mAc,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Customer Acc: " + mAc + msg)
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Success!!");
        alert.show();
    }

}
