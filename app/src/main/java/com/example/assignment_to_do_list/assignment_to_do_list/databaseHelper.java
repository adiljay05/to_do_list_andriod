package com.example.assignment_to_do_list.assignment_to_do_list;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="list.db";
    private static final String TABLE_NAME="data";
    private static final String TABLE2_NAME="tasks";
//    private static final String data_col1="ID";
    private static final String data_col2="Name";
//    private static final String tasks_col1="ID";
//    private static final String tasks_col2="dataID";
//    private static final String tasks_col3="description";
//    private static final String tasks_col4="isCompleted";


    databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)");
        sqLiteDatabase.execSQL("create table "+TABLE2_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, dataID INTEGER, description TEXT, isCompleted INTEGER,FOREIGN KEY(dataID) REFERENCES data(ID))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE2_NAME);
        onCreate(sqLiteDatabase);
    }

    Boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        return result != -1;
    }


    Boolean insertTask(String name,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("description",name);
        contentValues.put("dataID",id);
        contentValues.put("isCompleted",0);
        long result1 = db.insert(TABLE2_NAME,null ,contentValues);
        if (result1 != -1) return true;
        else return false;
    }

    int updateTask(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=  db.rawQuery("select * from tasks where description='"+name+"'",null);
        int isChecked=-1;
        if (res.moveToFirst()) {
            isChecked=Integer.parseInt(res.getString(3));
        }
        int n=0;
        if(isChecked==0)n=1;
        else n=0;
            db.execSQL("update tasks set isCompleted="+n+" where description='"+name+"'");
        return 0;
    }
    int getID(String name1)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("select * from data where Name='"+name1+"'",null);
        int id1=-1;
        if (result.moveToFirst()) {
            id1=Integer.parseInt(result.getString(0));
        }
        return id1;
    }

    Cursor getTasks(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("select * from tasks where dataID='"+id+"'",null);
        return result;
    }

    Cursor getData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("select * from data",null);
        return result;
    }
}
