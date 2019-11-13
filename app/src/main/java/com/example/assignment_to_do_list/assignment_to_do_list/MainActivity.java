package com.example.assignment_to_do_list.assignment_to_do_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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

    public String calculateYear(String strDate)
    {
        return String.valueOf(strDate.charAt(strDate.length()-4))+String.valueOf(strDate.charAt(strDate.length()-3))+
                String.valueOf(strDate.charAt(strDate.length()-2))+String.valueOf(strDate.charAt(strDate.length()-1));
    }
    public String calculateMonth(String strDate)
    {
        return String.valueOf(strDate.charAt(strDate.length()-6))+String.valueOf(strDate.charAt(strDate.length()-5));
    }
    public String calculateDay(String strDate)
    {
        if(strDate.length()==8)
        {
            return String.valueOf(strDate.charAt(strDate.length()-8))+ String.valueOf(strDate.charAt(strDate.length()-7));
        }
        else {
            return String.valueOf(strDate.charAt(strDate.length()-7));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().getStringExtra("check")!=null){
            getIntent().removeExtra("check");
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(intent);
            finish();
        }
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
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    String strDate = dayOfMonth+""+month+""+year;

                    while(task.moveToNext()) {
                        taskObject.itemid = task.getString(0);
                        taskObject.dataId = task.getString(1);
                        taskObject.description = task.getString(2);
                        taskObject.isCompleted = task.getString(3);
                        taskObject.dueDate = task.getString(4);
                        taskObject.lowestTime = task.getString(4);
                        taskObject.lowestName = "Nill";
                        if(taskObject.isCompleted.equals("0"))
                            break;
                    }
                    while (task.moveToNext()) {
                        getSmallestTime(strDate, task.getString(4),task);
                        getOverAllSmallestTime(task.getString(4),task);
                    }
                }
                else{
                    taskObject.itemid="";
                    taskObject.dataId="";
                    taskObject.description="No Items";
                    taskObject.isCompleted="";
                    taskObject.dueDate="";
                    taskObject.lowestTime="";
                    taskObject.lowestName="*Nill*";
                }
                data obj=new data();
                obj.name=data.getString(1);
                String year1="",month1="",day10="",dateFormated;
                if(!taskObject.dueDate.equals("")) {
                    year1 = calculateYear(taskObject.dueDate);
                    month1 = calculateMonth(taskObject.dueDate);
                    day10 = calculateDay(taskObject.dueDate);
                    dateFormated=day10+"/"+month1+"/"+year1;
                }
                else
                {
                    dateFormated="No Dates";
                }
                obj.itemName="Task: "+taskObject.description+"  Date: "+dateFormated+"\t\t* Past Item: "+taskObject.lowestName;
                obj.time=taskObject.dueDate;
                obj.lowestTimeEver=taskObject.lowestTime;
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
            finish();
            //Toast.makeText(getApplicationContext(),aa.getItem(i),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getOverAllSmallestTime(String tasktime,Cursor task1)
    {
        Double t1=Double.parseDouble(tasktime);
        if(t1<Double.parseDouble(taskObject.dueDate) && task1.getString(3).equals("0")){
            taskObject.lowestTime=task1.getString(4);
            taskObject.lowestName=task1.getString(2);
        }
    }
    public void getSmallestTime(String currentTime,String tasktime,Cursor task1)
    {
        Double t=Double.parseDouble(currentTime);
        Double t1=Double.parseDouble(tasktime);
        if(t1>=t && t1<Double.parseDouble(taskObject.dueDate) && task1.getString(3).equals("0")){
            Log.i("Complete status",task1.getString(3));
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
                overridePendingTransition(0, 0);
                startActivity(intent);
                finish();
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
                    overridePendingTransition(0, 0);
                    recreate();
                    overridePendingTransition(0, 0);
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
    public String itemName;
    public String time;
    public String lowestTimeEver;
}

class task{
    public String itemid;
    public String dataId;
    public String description;
    public String isCompleted;
    public String dueDate;
    public String lowestTime;
    public String lowestName;

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

    public String calculateYear(String strDate)
    {
        return String.valueOf(strDate.charAt(strDate.length()-4))+String.valueOf(strDate.charAt(strDate.length()-3))+
                String.valueOf(strDate.charAt(strDate.length()-2))+String.valueOf(strDate.charAt(strDate.length()-1));
    }
    public String calculateMonth(String strDate)
    {
        return String.valueOf(strDate.charAt(strDate.length()-6))+String.valueOf(strDate.charAt(strDate.length()-5));
    }
    public String calculateDay(String strDate)
    {
        if(strDate.length()==8)
        {
            return String.valueOf(strDate.charAt(strDate.length()-8))+ String.valueOf(strDate.charAt(strDate.length()-7));
        }
        else {
            return String.valueOf(strDate.charAt(strDate.length()-7));
        }
    }
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view=View.inflate(con,R.layout.list_items_main,null);
        TextView t1= view.findViewById(R.id.main);
        TextView t2= view.findViewById(R.id.subItem);
        t1.setText(l.get(position).name);
        t2.setText(l.get(position).itemName);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String y1,m1,d1,y2="",m2="",d2="";
        if(l.get(position).time!=null)
        {
            y1=String.valueOf(year);
            m1=String.valueOf(month+1);
            d1=String.valueOf(dayOfMonth);
//            Log.i("Year ",y1);
//            Log.i("Month",m1);
//            Log.i("Day",d1);
            if(!l.get(position).time.equals(""))
            {
                y2=calculateYear(l.get(position).time);
                m2=calculateMonth(l.get(position).time);
                d2=calculateDay(l.get(position).time);
            }
//            Log.i("Date",l.get(position).time);
//            Log.i("Year ",y2);
//            Log.i("Month",m2);
//            Log.i("Day",d2);

            if(y1.equals(y2) && m1.equals(m2) && d1.equals(d2)) {
                view.setBackgroundColor(Color.YELLOW);
                //view.setBackground(Drawable.createFromPath(String.valueOf(R.drawable.border)));
                view.setBackgroundResource(R.drawable.border_with_yellow_background);
            }

        }
        if(l.get(position).lowestTimeEver != null)
            if(!l.get(position).time.equals(""))
            {
                y1=String.valueOf(year);
                m1=String.valueOf(month+1);
                d1=String.valueOf(dayOfMonth);
                Double selectedTime;
                Double currentTime=Double.parseDouble(d1+m1+y1);
//                Log.i("newDate",currentTime.toString());
                if(!l.get(position).lowestTimeEver.equals("")) {
                    selectedTime = Double.parseDouble(l.get(position).lowestTimeEver);
                    if (selectedTime < currentTime)
                        view.setBackgroundResource(R.drawable.border_with_red_background);
                }
            }

        view.setTag(l.get(position).name);
        return view;
    }

}


