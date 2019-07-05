package net.lzzy.practiceapi.activities.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.models.Course;
import net.lzzy.practiceapi.models.student.Student;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TeacherCourseActivity extends AppCompatActivity {

    private List<Course> courses =new ArrayList<>();
    private GenericAdapter<Course> courseAdapter;
    private ListView courseLv;
    private SwipeRefreshLayout swipe;
    private String teacherId="";
    private String teacherName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course);
        swipe = findViewById(R.id.activity_teacher_course_swipe);
        courseLv = findViewById(R.id.activity_teacher_course_lv);
        View view= LayoutInflater.from(this).inflate(R.layout.empty_data, null);
        courseLv.setEmptyView(view);
        teacherId=getIntent().getStringExtra("teacherId");
        teacherName =getIntent().getStringExtra("teacherName");
        TextView tvTeacherName=findViewById(R.id.activity_teacher_course_teacherName);
        tvTeacherName.setText(teacherName+"的课程");
        courseAdapter = new GenericAdapter<Course>(this,
                R.layout.item_activity_teacher_course, courses) {
            @Override
            public void populate(ViewHolder viewHolder, Course course) {
               ImageView status=viewHolder.getView(R.id.item_activity_teacher_course_img_isMyCourse);
               TextView statusName=viewHolder.getView(R.id.item_activity_teacher_course_tv_Course);
                switch (course.getApplicationStatus()){
                    case -1:
                        statusName.setText("未申请该课程");
                        status.setImageResource(R.drawable.apply);
                        break;
                    case 0:
                        statusName.setText("等待老师批准");
                        status.setImageResource(R.drawable.ing);
                        break;
                    case 1:
                        statusName.setText("我的课程");
                        status.setImageResource(R.drawable.is_my_teacher);
                        break;
                    case 2:
                        statusName.setText("拒绝了你的申请");
                        status.setImageResource(R.drawable.negate);
                        break;
                        default:
                            break;
                }
                viewHolder.setTextView(R.id.item_activity_teacher_course_tv_courseName,course.getName());
            }
            @Override
            public boolean persistInsert(Course course) {
                return false;
            }

            @Override
            public boolean persistDelete(Course course) {
                return false;
            }
        };
        courseLv.setAdapter(courseAdapter);
        courseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course= courses.get(position);
                Student student=AppUtils.getStudent();
                if (course.getApplicationStatus()==1){
                    Intent intent=new Intent(TeacherCourseActivity.this,PracticeActivity.class);
                    intent.putExtra("courseId",course.getId());
                    intent.putExtra("courseName",course.getName());
                    startActivity(intent);
                }else {
                    TextView textView=new TextView(TeacherCourseActivity.this);
                    textView.setText("是否申请课程：“"+course.getName()+"”?");
                    AlertDialog dialog = new AlertDialog.Builder(TeacherCourseActivity.this)
                            .setMessage("是否申请该课程？")
                            .setPositiveButton("申请", null)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setView(textView).create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("courseId", course.getId());
                                    /*object.put("studentId", student.getStudentId());*/
                                    object.put("key", ApiConstants.getKey());
                                    new RequestThread<TeacherCourseActivity>(TeacherCourseActivity.this,
                                            ApiConstants.getCourseAppliedUrl(),
                                            object.toString()) {
                                        @Override
                                        protected void onPostExecute(String s, TeacherCourseActivity activity) {
                                            try {
                                                s = StudentKeyUtils.decodeResponse(s).first;
                                                if (s.contains("\"RESULT\":\"S\"")) {
                                                    getCourse();
                                                    dialog.dismiss();
                                                }
                                                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();

                                            } catch (Exception e) {
                                                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    }.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                    });
                }

            }
        });
        //下拉更新所有老师
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCourse();
            }
        });
        if (!teacherId.equals("")&&!teacherName.equals("")){
            getCourse();
        }
    }

    private void getCourse() {
        Student student= AppUtils.getStudent();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("teacherId",teacherId);
            jsonObject.put("key", ApiConstants.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RequestThread<TeacherCourseActivity>(TeacherCourseActivity.this,
                ApiConstants.getCourseUrl(), jsonObject.toString()) {
            @Override
            protected void onPostExecute(String s, TeacherCourseActivity activity) {

                try {
                    s= StudentKeyUtils.decodeResponse(s).first;
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("RESULT").equals("S")){
                        Gson gson=new Gson();
                        activity.courses.clear();
                        List<Course> courses=gson.fromJson(jsonObject.getString("courses"),
                                new TypeToken<LinkedList<Course>>() {}.getType());
                        activity.courses.addAll(courses);
                        activity.courseAdapter.notifyDataSetChanged();
                        Toast.makeText(activity, "获取成功！\n"+"获取到"+courses.size()+"条数据",
                                Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(activity, "获取失败。。\n"+s, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "获取失败。。\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                activity.swipe.setRefreshing(false);
            }
        }.execute();
    }
}
