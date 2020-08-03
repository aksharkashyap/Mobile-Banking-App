package com.akshar.mobilebanking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akshar.DB.MyDB;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    EditText user_acc,user_pass;
    MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_acc = findViewById(R.id.account_ma);
        user_pass = findViewById(R.id.password_ma);
        db = new MyDB(this);
    }

    private boolean verifyInfo(String acc, String pass){
        if(acc.isEmpty())
            return false;
        if(pass.isEmpty())
            return false;
        return true;
    }


    public void user_login_fun(View view) {

        String enterd_acc = user_acc.getText().toString();
        String enterd_pas = user_pass.getText().toString();

        if(!verifyInfo(enterd_acc,enterd_pas))
            Toast.makeText(this, "Please fill the required/Valid Info", Toast.LENGTH_SHORT).show();
        else{
            // check if account and password exist in the logindata db or not
            boolean verify = db.getLoginData(enterd_acc,enterd_pas);
            if(verify) {
                Intent intent = new Intent(MainActivity.this, User.class);
                Bundle bundle = new Bundle();
                bundle.putString("account", enterd_acc);
                bundle.putString("name",db.getUserName(enterd_acc));
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else
                Toast.makeText(this, "Details did not match", Toast.LENGTH_SHORT).show();
            }

    }


    public void admin_login_fun(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText pass = new EditText(this);

        builder.setTitle("Admin Login:");
        pass.setHint("     Enter Password");
        pass.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pass.setLayoutParams(lp);
        builder.setView(pass);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (pass.getText().toString().equals("akshar")) {
                    startActivity(new Intent(MainActivity.this,Admin.class));
                } else
                    Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();


    }



}
