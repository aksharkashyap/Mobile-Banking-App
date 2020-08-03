package com.akshar.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;

public class SearchUser extends AppCompatActivity {

    LinearLayout layout;
    EditText acc_et;
    String mAcc;
    Button show_btn;
    ListView lv;
    ArrayAdapter adpater;
    MyDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        layout = findViewById(R.id.search_user_layout);
        acc_et = findViewById(R.id.search_user_acc);
        show_btn = findViewById(R.id.search_user_btnShow);
        lv = findViewById(R.id.search_user_lv);
        db = new MyDB(this);
    }

    public boolean isValidated(String mAcc){
        if(mAcc.isEmpty())
            return false;
        return true;
    }
    public void search_user_getUser(View view) {
        String mAcc = acc_et.getText().toString();
        if(isValidated(mAcc) && db.isAccountExist(mAcc)){
              // code to get the details using join query
              String[] list = db.readSearchDB(acc_et.getText().toString());
              adpater = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
              lv.setAdapter(adpater);

        }
        else Toast.makeText(this, "Enter correct account number!!", Toast.LENGTH_SHORT).show();
    }
}
