package net.lzzy.practiceapi.activities.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.models.Course;

public class ManagementCourseActivity extends AppCompatActivity {
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_course);

        course = getIntent().getParcelableExtra("course");
        if (course == null) {
            Toast.makeText(this, "未获取到课程信息，请重新进入", Toast.LENGTH_LONG).show();
        } else {
            findViewById(R.id.activity_management_course_bt_manageStudents).
                    setOnClickListener(v -> {
                        if (course == null) {
                            Toast.makeText(ManagementCourseActivity.this,
                                    "未获取到课程信息，请重新进入", Toast.LENGTH_LONG).show();
                        }else {
                            Intent intent=new Intent(ManagementCourseActivity.this,
                                    ManageStudentsActivity.class);
                            intent.putExtra("course",course);
                            startActivity(intent);
                        }
                    });
            findViewById(R.id.activity_management_course_bt_managePractices).setOnClickListener(v -> {
                if (course == null) {
                    Toast.makeText(ManagementCourseActivity.this,
                            "未获取到课程信息，请重新进入", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent=new Intent(ManagementCourseActivity.this,
                            ManagePracticesActivity.class);
                    intent.putExtra("course",course);
                    startActivity(intent);
                }
            });
        }
    }
}
