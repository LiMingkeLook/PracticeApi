package net.lzzy.practiceapi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.lzzy.practiceapi.R;

public class HomeActivity extends AppCompatActivity {

    public static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button btStudent=findViewById(R.id.activity_home_student);
        Button btTeacher=findViewById(R.id.activity_home_teacher);
        Button btAdmin=findViewById(R.id. activity_home_admin);

        Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
        btStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(USER,"student");
                startActivity(intent);
            }
        });
        btTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(USER,"teacher");
                startActivity(intent);
            }
        });
        btAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(USER,"admin");
                startActivity(intent);
            }
        });
    }
}
