package com.akshar.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyDB extends SQLiteOpenHelper {

    static final String DBNAME ="MYDB";
    static final int DB_VERSION=2;
    SQLiteDatabase dbWrite,dbRead;
    ContentValues values;
    Context c;


    public MyDB(Context context){
        super(context,DBNAME,null,DB_VERSION);
        c = context;
    }

    private void dbWrite_init(){
        dbWrite = getWritableDatabase();
    }

    private void dbRead_init(){
        dbRead = getReadableDatabase();
    }

    private void dbContent_values_init(){
        values = new ContentValues();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            //-----------user table-----------------------------
            String USER_TABLE ="create table user(email text, phnum text, name text, father text, account text primary key, address text, gender text, dob text, services text)";
            //-----------transaction table-----------------------
            String TransactionTable = "create table transact(account text PRIMARY KEY, balance integer, type text)";
            //------------LoginData table-----------------------------------------------
            String LOGIN_TABLE = "create table logindata(account text primary key, password text not null)";

            db.execSQL(TransactionTable);
            db.execSQL(USER_TABLE);
            db.execSQL(LOGIN_TABLE);
    }

    public void insertUserData(String email, String phnum, String name, String father, String account, String address, String gender, String dob, String services){
        dbWrite_init();
        dbContent_values_init();
        values.put("email",email);
        values.put("phnum",phnum);
        values.put("name",name);
        values.put("father",father);
        values.put("account",account);
        values.put("address",address);
        values.put("gender",gender);
        values.put("dob",dob);
        values.put("services",services);
        dbWrite.insert("user",null,values);
        //Toast.makeText(c, "Data inserted", Toast.LENGTH_SHORT).show();
    }

    public void insertTransactData(String account, int balance, String type){
        dbWrite_init();
        dbContent_values_init();
        values.put("account",account);
        values.put("balance",balance);
        values.put("type",type);
        dbWrite.insert("transact",null,values);
    }

    public void insertLoginData(String account, String password){
        dbWrite_init();
        dbContent_values_init();
        values.put("account",account);
        values.put("password",password);
        dbWrite.insert("logindata",null,values);
    }

    // check login credentials(passed by the user) exist or not

    public boolean getLoginData(String account,String password){
        dbRead_init();
        String arr[] = {account,password};
        String query = "select * from logindata where account = ? and password = ?";
        Cursor cursor = dbRead.rawQuery(query,arr);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<String> getUserData(String account){
        List<String> data = new ArrayList<>();
        dbRead_init();
        String[] ac = {account};
        String query = "SELECT * FROM user where account = ?";
        Cursor cursor = dbRead.rawQuery(query,ac);
        String c1,c2,c3,c4,c5,c6,c7,c8,c9;
        if(cursor!=null){
            cursor.moveToFirst();
            c1 = cursor.getString(0);data.add("Email: "+c1);
            c2 = cursor.getString(1);data.add("Mobile: "+c2);
            c3 = cursor.getString(2);data.add("Name: "+c3);
            c4 = cursor.getString(3);data.add("Father: "+c4);
            c5 = cursor.getString(4);data.add("Account: "+c5);
            c6 = cursor.getString(5);data.add("Address: "+c6);
            c7 = cursor.getString(6);data.add("Gender: "+c7);
            c8 = cursor.getString(7);data.add("DOB: "+c8);
            c9 = cursor.getString(8);data.add("Services: "+c9.replaceFirst(",",""));
        }
        return data;
    }

    public String[] readUserDB(String accountNum) {
        dbRead_init();
        String s = "select a.email, a.phnum, a.name, a.father, a.address, a.gender, a.dob, a.services, b.type  from user a inner join transact b where b.account = ?";
        String arr[] = {accountNum};
        Cursor cursor = dbRead.rawQuery(s,arr);
        String name = "",email = "",phnum="",father="",dob="",address="",get_gender = "",get_services="",actype="";

        //-------------Fetching the rows--------------------------
           if (cursor != null) {
               cursor.moveToFirst();

                   email = cursor.getString(0);
                   phnum = cursor.getString(1);
                   name = cursor.getString(2);
                   father = cursor.getString(3);
                   address = cursor.getString(4);
                   get_gender = cursor.getString(5);
                   dob = cursor.getString(6);
                   get_services = cursor.getString(7);
                   actype = cursor.getString(8);
           }

        String arrData[] = {name, email, phnum, father, dob, address,get_gender,get_services,actype};
        return arrData;
    }

    public LinkedList[] getAllUserDB(){
        dbRead_init();
        LinkedList<String>[] list = new LinkedList[3];

        for(int i=0;i<3;i++)
        {
            list[i] = new LinkedList<>();
        }

        String query = "select a.name, a.account, b.balance from user a inner join transact b on a.account=b.account";
        Cursor cursor = dbRead.rawQuery(query,null);
        String name,acc,bal;
        while(cursor.moveToNext())
        {
            name = cursor.getString(0);
            acc = cursor.getString(1);
            bal = cursor.getString(2);
            list[0].add(name);
            list[1].add(acc);
            list[2].add(bal);
        }

        return list;
    }

    public String[] readSearchDB(String accountNum) {
        dbRead_init();
        // join two tables---------------
        String s = "select a.name,a.email,a.father,a.gender,a.dob,a.services,b.account, b.balance from user a inner join transact b on a.account = b.account where a.account = ?";
        //-------------------------------
        String arr[] = {accountNum};
        Cursor cursor = dbRead.rawQuery(s,arr);
        String name = "",email = "",father="",acc="",bal="",gender="",dob="",services="";

        //-------------Fetching the rows--------------------------
        if (cursor != null) {
            cursor.moveToFirst();

            name = cursor.getString(0);
            email = cursor.getString(1);
            father = cursor.getString(2);
            gender = cursor.getString(3);
            dob = cursor.getString(4);
            services = cursor.getString(5).replace(","," ");
            acc = cursor.getString(6);
            bal = cursor.getString(7);

        }

        String arrData[] = {"Name: "+name,"Account: "+acc,"Balance: "+bal ,"Email: "+email,"Father: "+father,"Gender: "+gender, "Birth Date: "+dob, "Services: "+ services.trim()};
        return arrData;
    }

    public void deleteUserData(String account){
        dbWrite_init();
        String whereAcc[] = {account};
        dbWrite.delete("user","account = ?",whereAcc);
    }
    public void deleteTransactData(String account){
        dbWrite_init();
        String whereAcc[] = {account};
        dbWrite.delete("transact","account = ?",whereAcc);
    }
    public void deleteLoginData(String account){
        dbWrite_init();
        String whereAcc[] = {account};
        dbWrite.delete("logindata","account = ?",whereAcc);
    }



    public void updateData(String account,String email, String phone, String name, String father, String address,
                           String gender, String dob, String services){
        dbWrite_init();
        dbContent_values_init();
        values.put("email",email);
        values.put("phnum",phone);
        values.put("name",name);
        values.put("father",father);
        values.put("address",address);
        values.put("gender",gender);
        values.put("dob",dob);
        values.put("services",services);
        String whereAccount[] = {account};
        dbWrite.update("user",values,"account = ?",whereAccount);
    }

    public void updateData(String account, String type){
        dbWrite_init();
        dbContent_values_init();
        values.put("type",type);
        String whereAccount[] = {account};
        dbWrite.update("transact",values,"account = ?",whereAccount);
    }

    //-----------------TASK FROM THE USER ACTIVITY---------------

    public int getBalance(String account){
        dbRead_init();
        String[] arr = {account};
        String bal = "select balance from transact where account = ?";
        Cursor cursor = dbRead.rawQuery(bal,arr);
        int getBal=0;
        cursor.moveToFirst();
            getBal = cursor.getInt(0);

        return getBal;
    }

    public void updateBalance(String account, int balance){
        dbWrite_init();
        ContentValues v = new ContentValues();
        v.put("balance",balance);
        String[] arr = {account};
        dbWrite.update("transact",v,"account = ?",arr);
    }

    // check account number(passed by the user) exist or not

    public boolean isAccountExist(String account){
        dbRead_init();
        String arr[] = {account};
        String query = "select * from user where account = ?";
        Cursor cursor = dbRead.rawQuery(query,arr);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public LinkedList[] getLoginTableDB(){
        dbRead_init();
        LinkedList<String>[] list = new LinkedList[2];

        for(int i=0;i<2;i++)
        {
            list[i] = new LinkedList<>();
        }

        String query = "select account,password from logindata";
        Cursor cursor = dbRead.rawQuery(query,null);
        String acc,pass;
        while(cursor.moveToNext())
        {
            acc = cursor.getString(0);
            pass = cursor.getString(1);
            list[0].add(acc);
            list[1].add(pass);
        }

        return list;
    }

    // use this function when their is no chance of excption
    public String getUserName(String acc){
        dbRead_init();
        String accNum[] = {acc};
        String query = "select name from user where account=?";
        Cursor cursor = dbRead.rawQuery(query,accNum);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void updatePassword(String account, String pass){
        dbWrite_init();
        dbContent_values_init();
        values.put("password",pass);
        String whereAccount[] = {account};
        dbWrite.update("logindata",values,"account = ?",whereAccount);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
