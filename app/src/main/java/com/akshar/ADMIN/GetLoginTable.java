package com.akshar.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;

import java.util.LinkedList;

public class GetLoginTable extends AppCompatActivity {
    ListView lv_acc,lv_pass;
    ArrayAdapter<String> adapter1,adapter2;
    MyDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_login_table);
        lv_acc = findViewById(R.id.listview_getlogin_table_acc);
        lv_pass = findViewById(R.id.listview_getlogin_table_pass);
        db = new MyDB(this);
        getLoginTableFunc();
    }

    public void getLoginTableFunc() {
        LinkedList<String>[] list;
        list = db.getLoginTableDB();
        adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list[0]);
        lv_acc.setAdapter(adapter1);
        adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list[1]);
        lv_pass.setAdapter(adapter2);
    }

}
