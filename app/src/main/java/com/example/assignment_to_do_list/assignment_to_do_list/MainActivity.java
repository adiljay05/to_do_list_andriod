package com.example.assignment_to_do_list.assignment_to_do_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public String m_Text = "";
    public databaseHelper mydb;
    ArrayList<String> arr=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=findViewById(R.id.newItemButton);
        mydb=new databaseHelper(getApplicationContext());

        Cursor data=mydb.getData();
        if(data.getCount() == 0) {
            Toast.makeText(this,"empty database",Toast.LENGTH_SHORT).show();
            //return;
        }
        else {
            while (data.moveToNext()) {
                arr.add(data.getString(1));
            }
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogue();
            }
        });
        mydb =new databaseHelper(this);
        ListView l=findViewById(R.id.listView);
        final ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, arr);
        l.setAdapter(aa);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //set a new intent here to start a new activity and show the items of a list.
                Intent in=new Intent(getApplicationContext(),items.class);
                in.putExtra("name",aa.getItem(i).toString());
                startActivity(in);
                Toast.makeText(getApplicationContext(),aa.getItem(i),Toast.LENGTH_SHORT).show();
            }
        });
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
                if(arr.contains(m_Text))
                    Toast.makeText(getApplicationContext(),"name already exists",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), m_Text, Toast.LENGTH_SHORT).show();
                    arr.add(m_Text);
                    mydb=new databaseHelper(getApplicationContext());
                    Boolean n=mydb.insertData(m_Text);
                    if(n)
                    {
                        Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Name Already Exists",Toast.LENGTH_SHORT).show();
                    }
                }
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
