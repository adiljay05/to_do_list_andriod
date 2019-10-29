package com.example.assignment_to_do_list.assignment_to_do_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="list.db";
    private static final String TABLE_NAME="data";
    private static final String TABLE2_NAME="tasks";
    private static final String data_col1="ID";
    private static final String data_col2="Name";
    private static final String tasks_col1="ID";
    private static final String tasks_col2="dataID";
    private static final String tasks_col3="description";
    private static final String tasks_col4="isCompleted";


    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
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
}
