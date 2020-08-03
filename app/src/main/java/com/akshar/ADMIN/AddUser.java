package com.akshar.ADMIN;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;

import java.util.Random;

public class AddUser extends AppCompatActivity {

    private EditText name, email, father, dob, address, phone;
    private CheckBox service_sms, service_atm, service_netbanking;
    private RadioGroup gender_rb, accType_rb;
    private RadioButton gender_m, gender_f, accSaving, accCurrent;
    private String gender, acc, password;
    private StringBuilder services;
    private int account;
    private MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        //------------DB--------------------
        db = new MyDB(this);
        //----------------------------------

        //-------------referencing of variables------------------
        name = findViewById(R.id.add_user_name);
        email = findViewById(R.id.add_user_email);
        father = findViewById(R.id.add_user_father);
        dob = findViewById(R.id.add_user_dob);
        address = findViewById(R.id.add_user_address);
        phone = findViewById(R.id.add_user_mobile);
        gender_rb = findViewById(R.id.add_user_gender);
        gender_m = findViewById(R.id.add_user_rb_male);
        gender_f = findViewById(R.id.add_user_rb_female);
        service_sms = findViewById(R.id.add_user_cb_sms);
        service_atm = findViewById(R.id.add_user_cb_atm);
        service_netbanking = findViewById(R.id.add_user_cb_netbanking);
        accType_rb = findViewById(R.id.add_user_account_type);
        accSaving = findViewById(R.id.add_user_savingAcc);
        accCurrent = findViewById(R.id.add_user_currentAcc);

    }

    public void generateAccountInfo() {
        //------------------Generate Account Number---------------------------
        Random rand = new Random();
        account = 10000000 + rand.nextInt(90000000);
        acc = String.valueOf(account);
        //------------------------Password------------------------------------
        int pass = 10000 + rand.nextInt(40000);
        password = String.valueOf(pass);
    }


    public void resetFieldsData(EditText mEmail, EditText mPhone, EditText mName,
                                EditText mFather, EditText mAddress, RadioGroup mGender, EditText mDob,
                                CheckBox mService_atm, CheckBox mService_sms, CheckBox mService_netbanking, RadioGroup mAccType) {
        mEmail.setText("");
        mPhone.setText("");
        mName.setText("");
        mFather.setText("");
        mAddress.setText("");
        mDob.setText("");

        //----------CheckBoc and Radio button

        mGender.clearCheck();
        mAccType.clearCheck();
        //-----------

        if (mService_atm.isChecked())
            mService_atm.setChecked(false);
        if (mService_netbanking.isChecked())
            mService_netbanking.setChecked(false);
        if (mService_sms.isChecked())
            mService_sms.setChecked(false);
    }

    public String get_gender(RadioGroup gender_rb) {
        int id = gender_rb.getCheckedRadioButtonId();
        RadioButton gender_b = gender_rb.findViewById(id);
        String g = gender_b.getText().toString().toUpperCase();
        return g;
    }

    public String get_account_type(RadioGroup accType_rb) {
        int id = accType_rb.getCheckedRadioButtonId();
        RadioButton type = accType_rb.findViewById(id);
        String g = type.getText().toString().toUpperCase();
        return g;
    }

    public String get_checked_Services(CheckBox service_atm, CheckBox service_sms, CheckBox service_netbanking) {
        services = new StringBuilder();
        if (service_atm.isChecked())
            services.append(service_atm.getText().toString());
        if (service_sms.isChecked())
            services.append("," + service_sms.getText().toString());
        if (service_netbanking.isChecked())
            services.append("," + service_netbanking.getText().toString());

        return services.toString().toUpperCase();
    }

    public boolean validate_entries() {
        // is empty validation
        if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || father.getText().toString().isEmpty()
                || dob.getText().toString().isEmpty() || address.getText().toString().isEmpty()) {
            return false;
        }
        // radiobutton validate - gender
        if (!gender_f.isChecked() && !gender_m.isChecked())
            return false;

        // radiobutton validate - account type
        if (accType_rb.getCheckedRadioButtonId() == -1)
            return false;
        //checkbox validate - services
        if (!service_atm.isChecked() && !service_netbanking.isChecked() && !service_sms.isChecked())
            return false;

        return true;
    }


    public void aletDialogShow(String mAc, String mPass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Customer Acc: " + mAc + " (Password:- " + mPass + ")")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        resetFieldsData(email, phone, name, father, address, gender_rb, dob, service_atm,
                                service_sms, service_netbanking, accType_rb);
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Success!!");
        alert.show();
    }


    public void add_user_submit(View view) {

        //validate
        boolean isValidated = validate_entries();

        if (isValidated) {
            String mGender = get_gender(gender_rb);
            String mAccountType = get_account_type(accType_rb);
            String mServices = get_checked_Services(service_atm, service_sms, service_netbanking);
            // GET THE STRING FROM THE EDIT TEXT FIELDS IN UPPER CASE
            String NAME_str = name.getText().toString().toUpperCase();
            String EMAIL_str = email.getText().toString().toUpperCase();
            String PHONE_str = phone.getText().toString();
            String FATHER_str = father.getText().toString().toUpperCase();
            String ADDR_str = address.getText().toString().toUpperCase();
            String DOB_str = dob.getText().toString();

            //AccountInfo
            generateAccountInfo();

            //insert into DB
            db.insertUserData(EMAIL_str, PHONE_str, NAME_str, FATHER_str, acc, ADDR_str, mGender, DOB_str, mServices);
            //--------------initial deposit amount--------------------------
            db.insertTransactData(acc, 2000, mAccountType);
            //--------------------------------------------------------------
            db.insertLoginData(acc, password);
            //-------------insert loginData into login table----------------


            //----------------------Alert Diaglog---------------------------
            aletDialogShow(acc, password);

        } else Toast.makeText(this, "Please Fill all the required info", Toast.LENGTH_SHORT).show();
    }


}
