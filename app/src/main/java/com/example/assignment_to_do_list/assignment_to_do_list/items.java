package com.example.assignment_to_do_list.assignment_to_do_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class items extends Activity {

    public databaseHelper mydb;
    public String m_Text = "";
    ArrayList<Integer> ids=new ArrayList<>();
    ArrayList<String> arr=new ArrayList<>();
    public String d;
    public int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        String str = getIntent().getStringExtra("name");
        d=str;
        mydb=new databaseHelper(getApplicationContext());
        //id=mydb.getID("jawad");

        //Toast.makeText(this,""+id,Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,Integer.parseInt(id.getString(0)),Toast.LENGTH_SHORT).show();
        int id1=mydb.getID(d);

        Button b=findViewById(R.id.newItemButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogue();
            }
        });

        Cursor data=mydb.getTasks(id1);
        if(data.getCount() == 0) {
            //Toast.makeText(this,"empty database",Toast.LENGTH_SHORT).show();
            //return;
        }
        else {
            while (data.moveToNext()) {
                arr.add(data.getString(2));
                ids.add(Integer.valueOf(data.getString(0)));
            }
            Toast.makeText(this,arr.toString(),Toast.LENGTH_SHORT).show();
        }
        ListView l=findViewById(R.id.listView);
        final ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arr);
        l.setAdapter(aa);

//        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Toast.makeText(getApplicationContext(),aa.getItem(i),Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    public void openDialogue()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter list Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(m_Text=="")
                    return;
                //Toast.makeText(getApplicationContext(), m_Text, Toast.LENGTH_SHORT).show();
                mydb=new databaseHelper(getApplicationContext());
                int id1=mydb.getID(d);
                //Toast.makeText(getApplicationContext(),id.toString(),Toast.LENGTH_SHORT).show();
                if(id1==-1){
                    Toast.makeText(getApplicationContext(),"No id found",Toast.LENGTH_SHORT).show();
                }
                Boolean n=mydb.insertTask(m_Text,id1);
                if(n){
                    Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot Enter the data",Toast.LENGTH_SHORT).show();
                }
                arr.add(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(),"Canceled by user", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
}
