package com.example.assignment_to_do_list.assignment_to_do_list;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    public databaseHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb =new databaseHelper(this);
    }
}
