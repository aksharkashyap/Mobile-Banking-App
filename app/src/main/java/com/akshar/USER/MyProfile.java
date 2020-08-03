package com.akshar.USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akshar.DB.MyDB;
import com.akshar.mobilebanking.R;

import java.util.List;

public class MyProfile extends AppCompatActivity {

    ListView myprofile_lv;
    String myAccountNum;
    ArrayAdapter<String> adapter;
    MyDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        myprofile_lv = findViewById(R.id.listview_myprofile);
        db = new MyDB(this);
        getIntentData();
        getDataFromDB();
    }

    private void getDataFromDB(){
        /*
        * photo
        * all data from UserData
        * */
        List<String> mylist =  db.getUserData(myAccountNum);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mylist);
        myprofile_lv.setAdapter(adapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        myAccountNum = intent.getStringExtra("account");
    }
}
