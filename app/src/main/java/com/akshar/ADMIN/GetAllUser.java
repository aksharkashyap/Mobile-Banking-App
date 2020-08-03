package com.akshar.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;

import java.util.LinkedList;

public class GetAllUser extends AppCompatActivity {

    Button getBtn;
    MyDB db;
    ListView lv_name,lv_acc,lv_balance;
    ArrayAdapter<String> adapter1,adapter2,adapter3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_user);
        getBtn = findViewById(R.id.get_all_user_getBTn);
        db = new MyDB(this);
        lv_name = findViewById(R.id.get_all_user_lv1);
        lv_acc = findViewById(R.id.get_all_user_lv2);
        lv_balance = findViewById(R.id.get_all_user_lv3);
    }


    public void getAllUser(View view) {
        LinkedList<String>[] list;
        list = db.getAllUserDB();
        adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list[0]);
        lv_name.setAdapter(adapter1);
        adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list[1]);
        lv_acc.setAdapter(adapter2);
        adapter3 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list[2]);
        lv_balance.setAdapter(adapter3);
    }



}
