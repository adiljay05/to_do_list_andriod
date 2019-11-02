package com.example.assignment_to_do_list.assignment_to_do_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="list.db";
    private static final String TABLE_NAME="data";
    private static final String TABLE2_NAME="tasks";

    databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)");
        sqLiteDatabase.execSQL("create table "+TABLE2_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, dataID INTEGER, description TEXT, isCompleted INTEGER,dueDate TEXT,FOREIGN KEY(dataID) REFERENCES data(ID))");

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

    Boolean insertTask(String name,int id,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("description",name);
        contentValues.put("dataID",id);
        contentValues.put("isCompleted",0);
        contentValues.put("dueDate",date);
        long result1 = db.insert(TABLE2_NAME,null ,contentValues);
        if (result1 != -1) return true;
        else return false;
    }

    void updateData(String newName,String str)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME+" set Name='"+newName+"' where Name='"+str+"'");
    }

    void updateTasks(String newName,String str,String currentList)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int id=getID(currentList);
        db.execSQL("update "+TABLE2_NAME+" set description='"+newName+"' where description='"+str+"' and ID='"+id+"'");
    }

    void updateDueDate(String description,String date,String currentList)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int id=getID(currentList);
        db.execSQL("update "+TABLE2_NAME+" set dueDate='"+date+"' where description='"+description+"' and ID='"+id+"'");
    }
    void removeItem(String description,String listName)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int id=getID(listName);
        db.execSQL("delete from "+TABLE2_NAME+" WHERE description='"+description+"' and dataID='"+id+"'");
    }
    int updateTask(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=  db.rawQuery("select * from tasks where description='"+name+"'",null);
        int isChecked=-1;
        if (res.moveToFirst()) {
            isChecked=Integer.parseInt(res.getString(3));
        }
        int n;
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
    void moveItem(String newName,String item)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int newIdOfList=getID(newName);
        db.execSQL("update "+TABLE2_NAME+" SET dataID='"+newIdOfList+"' where description='"+item+"'");


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
