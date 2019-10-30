package com.example.assignment_to_do_list.assignment_to_do_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class items extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        String d = getIntent().getStringExtra("name");
        TextView t=findViewById(R.id.text);                                                                                               
        t.setText(d);
    }
}
