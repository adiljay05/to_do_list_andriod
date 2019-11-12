package com.example.assignment_to_do_list.assignment_to_do_list;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class moveApplications extends Activity {

    public String item="";
    public databaseHelper mydb;
    ArrayList<String> arr=new ArrayList<>();
    ArrayList<Integer> ids=new ArrayList<>();
    ArrayList<String> newListData=new ArrayList<>();
    ArrayAdapter<String> aa ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_to);
        mydb =new databaseHelper(this);
        item=getIntent().getStringExtra("item");
        Cursor data=mydb.getData();
        if(data.getCount() == 0) {
            Toast.makeText(this,"empty database",Toast.LENGTH_SHORT).show();
            //return;
        }
        else {
            while (data.moveToNext()) {
                arr.add(data.getString(1));
                ids.add(Integer.valueOf(data.getString(0)));
            }
        }



        ListView l=findViewById(R.id.listView);
        aa= new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arr);

        l.setAdapter(aa);
        registerForContextMenu(l);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String newList=aa.getItem(i);
                mydb=new databaseHelper(getApplicationContext());
                Cursor data=mydb.getTasks(mydb.getID(newList));
                if(data.getCount() == 0) {
                    Toast.makeText(getApplicationContext(),"empty database",Toast.LENGTH_SHORT).show();
                    //return;
                }
                else {
                    while (data.moveToNext()) {
                        newListData.add(data.getString(2));
                        //ids.add(Integer.valueOf(data.getString(0)));
                    }
                }
                //Toast.makeText(getApplicationContext(),item,Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),newListData.toString(),Toast.LENGTH_SHORT).show();
                if(!newListData.contains(item))
                {
                    mydb.moveItem(newList,item);
                    Toast.makeText(getApplicationContext(),"Moved",Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(getApplicationContext(),items.class);
                    in.putExtra("name",newList);
                    overridePendingTransition(0, 0);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(in);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Name already exists.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
