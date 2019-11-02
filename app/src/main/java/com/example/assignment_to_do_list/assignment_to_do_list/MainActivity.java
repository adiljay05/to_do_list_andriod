package com.example.assignment_to_do_list.assignment_to_do_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    public String m_Text = "";
    public databaseHelper mydb;
    ArrayList<data> array1=new ArrayList<>();
    ArrayList<String> arr=new ArrayList<>();
    CustomAdapter ad;
    task taskObject=new task();

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
                int idOfCurrentList=mydb.getID(data.getString(1));
                Cursor task=mydb.getTasks(idOfCurrentList);
                if(task.getCount()!=0)
                {
                    Calendar calendar = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dmy = new SimpleDateFormat("ddmmyyyy");
                    String strDate = dmy.format(calendar.getTime());
                    task.moveToNext();
                    taskObject.itemid=task.getString(0);
                    taskObject.dataId=task.getString(1);
                    taskObject.description=task.getString(2);
                    taskObject.isCompleted=task.getString(3);
                    taskObject.dueDate=task.getString(4);
                    while (task.moveToNext()) {
                        getSmallestTime(strDate, task.getString(4),task);
                    }
                }
                data obj=new data();
                obj.name=data.getString(1);
                obj.time=taskObject.description;
                array1.add(obj);
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
        //aa= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arr);
        ad=new CustomAdapter(this,array1);

        l.setAdapter(ad);

        registerForContextMenu(l);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent in=new Intent(getApplicationContext(),items.class);
            in.putExtra("name",array1.get(i).name);

            startActivity(in);
            //Toast.makeText(getApplicationContext(),aa.getItem(i),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getSmallestTime(String currentTime,String tasktime,Cursor task1)
    {

        Double t=Double.parseDouble(currentTime);
        Double t1=Double.parseDouble(tasktime);
        if(t1>=t && t1<Double.parseDouble(taskObject.dueDate)){
            taskObject.itemid=task1.getString(0);
            taskObject.dataId=task1.getString(1);
            taskObject.description=task1.getString(2);
            taskObject.isCompleted=task1.getString(3);
            taskObject.dueDate=task1.getString(4);
        }
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
                    mydb.updateData(m_Text,str);
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
                if(m_Text.equals(""))
                    return;
                if(arr.contains(m_Text))
                    Toast.makeText(getApplicationContext(),"name already exists",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), m_Text, Toast.LENGTH_SHORT).show();
                    arr.add(m_Text);
                    data obj=new data();
                    obj.name=m_Text;
                    array1.add(obj);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater n=getMenuInflater();
        n.inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.update:
                String str = arr.get(info.position);
                openUpdateDialogue(str);
                ad.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }
}

class data{
    public String name;
    public String time;
    public String getName()
    {
        return name;
    }
    public String getTime()
    {
        return time;
    }
}

class task{
    public String itemid;
    public String dataId;
    public String description;
    public String isCompleted;
    public String dueDate;

}


class CustomAdapter extends BaseAdapter {
    Context con;
    List<data> l;

    public CustomAdapter (Context con, List<data> mContact) {
        this.con = con;
        this.l = mContact;
    }

    @Override
    public int getCount() {
        return l.size();
    }

    @Override
    public Object getItem(int position) {
        return l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view=View.inflate(con,R.layout.list_items_main,null);
        TextView t1= view.findViewById(R.id.main);
        TextView t2= view.findViewById(R.id.subItem);
        t1.setText(l.get(position).name);
        t2.setText(l.get(position).time);

        view.setTag(l.get(position).name);
        return view;
    }

}


