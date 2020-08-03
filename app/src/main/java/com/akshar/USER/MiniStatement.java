package com.akshar.USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.akshar.mobilebanking.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MiniStatement extends AppCompatActivity {

    ListView lv;
    ArrayAdapter adapter;
    FundTransfer ft;
    String myAccountNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement);
        lv = findViewById(R.id.lv_mini_statement);
        ft = new FundTransfer();
        getAccountFromIntent();

        try {
            updateMiniStatement(myAccountNum);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getAccountFromIntent() {
        Intent intent = getIntent();
        myAccountNum = intent.getStringExtra("account");
    }


    private void updateMiniStatement(String mAccount) throws IOException {
        FileInputStream fis = null;
        try{
            fis = openFileInput(mAccount+".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine()) != null) {
                sb.append(text);
            }

            String[] textS = sb.toString().split(",");
            Collections.reverse(Arrays.asList(textS)); // to get the latest transactions on top
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,textS);
            lv.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(fis!=null)
                fis.close();
        }

    }


}
