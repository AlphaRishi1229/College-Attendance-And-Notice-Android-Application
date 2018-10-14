package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {
    Button btnattendance, btnnotice;
    TextView tname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Bundle bundle = getIntent().getExtras();
        final String TName = bundle.getString("TeacherName");
        tname = (TextView)findViewById(R.id.tname);
        tname.setText(TName);

        btnattendance = (Button) findViewById(R.id.btnattendance);
        btnnotice = (Button) findViewById(R.id.btnnotice);
        btnnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OptionsActivity.this, NoticeActivity.class);
                startActivity(myIntent);
            }
        });
        btnattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OptionsActivity.this, AttendanceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TeacherName2",TName);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });
    }
}

