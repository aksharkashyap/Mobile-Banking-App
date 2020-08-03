package com.akshar.mobilebanking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ImageView;

import com.akshar.ADMIN.AddUser;
import com.akshar.ADMIN.GetAllUser;
import com.akshar.ADMIN.GetLoginTable;
import com.akshar.ADMIN.SearchUser;
import com.akshar.ADMIN.UpdateUser;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Admin extends AppCompatActivity {

    ImageView updatebtn, adduserbtn, searchButn, getAllBtn, getLoginTable, calendar_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        updatebtn = findViewById(R.id.admin_update_user);
        adduserbtn = findViewById(R.id.admin_add_user);
        searchButn = findViewById(R.id.admin_search_user);
        getAllBtn = findViewById(R.id.admin_get_all_user_getBTn);
        getLoginTable = findViewById(R.id.admin_getlogin_table);
        calendar_btn = findViewById(R.id.calendar_admin);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, UpdateUser.class);
                startActivity(intent);
            }
        });

        adduserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AddUser.class);
                startActivity(intent);
            }
        });

        searchButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, SearchUser.class);
                startActivity(intent);
            }
        });

        getAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, GetAllUser.class);
                startActivity(intent);
            }
        });

        getLoginTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, GetLoginTable.class);
                startActivity(intent);
            }
        });

        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = new GregorianCalendar();
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, 2);
                long time = cal.getTime().getTime();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                builder.appendPath(Long.toString(time));
                Intent intent = new Intent(Intent.ACTION_VIEW, builder.build());
                startActivity(intent);

            }
        });
    }
}
