package com.example.assignment_to_do_list.assignment_to_do_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class items extends Activity implements DatePickerDialog.OnDateSetListener{

    public databaseHelper mydb;
    public String m_Text = "";
    String date="";
    String oldDate="";
    ArrayList<Integer> ids=new ArrayList<>();
    ArrayList<String> arr=new ArrayList<>();
    ArrayList<Integer> checkedItems=new ArrayList<>();
    ArrayList<String> date1=new ArrayList<>();
    ArrayAdapter<String> aa;
    TextView t,t1;
    public String d;
    public int id;
    public int checkedCounter=0,totalCounter=0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        d= getIntent().getStringExtra("name");
        mydb=new databaseHelper(getApplicationContext());
        //id=mydb.getID("jawad");

        //Toast.makeText(this,""+id,Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,Integer.parseInt(id.getString(0)),Toast.LENGTH_SHORT).show();
        int id1=mydb.getID(d);

        Button b=findViewById(R.id.newItemButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldDate=date;
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
                checkedItems.add(Integer.valueOf(data.getString(3)));
                date1.add(data.getString(4));
            }
            Toast.makeText(this,arr.toString(),Toast.LENGTH_SHORT).show();
        }
        int size=checkedItems.size();
        int checkedIndex=arr.size();
        for(int i=0;i<size;i++)
        {
            if(checkedItems.get(i)==1)
            {
                checkedCounter++;
                String n=arr.get(i);
                int num=checkedItems.get(i);
                int id=ids.get(i);
                ids.set(i,ids.get(checkedIndex-1));
                checkedItems.set(i,checkedItems.get(checkedIndex-1));
                arr.set(i,arr.get(checkedIndex-1));
                ids.set(checkedIndex-1,id);
                arr.set(checkedIndex-1,n);
                checkedItems.set(checkedIndex-1,num);
                i--;
                size--;
                checkedIndex--;
            }
            totalCounter++;

        }
        t=findViewById(R.id.checkedCounter);
        t1=findViewById(R.id.totalCounter);
        t.setText("Total: "+totalCounter);
        t1.setText("Completed: "+checkedCounter);

        ListView l=findViewById(R.id.listView);

        aa = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, arr);
        l.setChoiceMode(l.CHOICE_MODE_MULTIPLE);
        registerForContextMenu(l);
        l.setItemsCanFocus(false);
        l.setAdapter(aa);
        for(int i=0;i<checkedItems.size();i++)
        {
            if(checkedItems.get(i)==1) {
                //Toast.makeText(this,i+"",Toast.LENGTH_SHORT).show();
                l.setItemChecked(i, true);

                //aa.notifyDataSetChanged();
                //l.getItemIdAtPosition(--checkedIndex);
                //l.getItemAtPosition(--checkedIndex);
            }
        }

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                checkedCounter++;
                totalCounter++;
                mydb.updateTask(aa.getItem(i));
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(),checkedItems.get(i),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openDialogue()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter list Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        final int[] id1 = new int[1];
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(m_Text.equals(""))
                    return;
                //Toast.makeText(getApplicationContext(), m_Text, Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                    items.this,
                    items.this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE ){
                            //Toast.makeText(getApplicationContext(),"Date Selected",Toast.LENGTH_SHORT).show();
                            mydb=new databaseHelper(getApplicationContext());
                            id1[0] =mydb.getID(d);
                            //Toast.makeText(getApplicationContext(),id.toString(),Toast.LENGTH_SHORT).show();
                            if(id1[0] ==-1){
                                Toast.makeText(getApplicationContext(),"No id found",Toast.LENGTH_SHORT).show();
                            }
                            Boolean n=mydb.insertTask(m_Text, id1[0],date);
                            if(n){
                                Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Cannot Enter the data",Toast.LENGTH_SHORT).show();
                            }
                            arr.add(m_Text);
                            totalCounter++;
                            t.setText("Total: "+totalCounter);
                        }
                    }
                });
                //while(date.equals(oldDate)){}

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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        date= String.valueOf(i2)+String.valueOf(i1)+String.valueOf(i);
    }

    public void openUpdateDialogue(final String str)
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
                if(m_Text.equals(""))
                    return;
                if(arr.contains(m_Text))
                    Toast.makeText(getApplicationContext(),"name already exists",Toast.LENGTH_SHORT).show();
                else {
                    mydb=new databaseHelper(getApplicationContext());
                    mydb.updateTasks(m_Text,str);
                    mydb.close();
                    Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
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

    public void getNewDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                items.this,
                items.this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater n=getMenuInflater();
        n.inflate(R.menu.item_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.update:
                String str = arr.get(info.position);
                openUpdateDialogue(str);
                aa.notifyDataSetChanged();
                break;
            case R.id.updateDueDate:
                //update due date
                getNewDate();
                String str1 = arr.get(info.position);
                mydb=new databaseHelper(getApplicationContext());
                //Toast.makeText(this,date,Toast.LENGTH_SHORT).show();
                mydb.updateDueDate(str1,date);
                mydb.close();
                aa.notifyDataSetChanged();
                break;

            case R.id.remove:
                String str2 = arr.get(info.position);
                mydb=new databaseHelper(getApplicationContext());
                arr.remove(info.position);
                mydb.removeItem(str2);
                mydb.close();
                aa.notifyDataSetChanged();
                break;
            case R.id.move:
                Intent in=new Intent(getApplicationContext(),moveApplications.class);
                in.putExtra("item",arr.get(info.position));

                startActivity(in);
                break;

        }
        return super.onContextItemSelected(item);
    }
}

