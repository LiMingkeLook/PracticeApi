package net.lzzy.practiceapi.activities.teacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.models.Course;
import net.lzzy.practiceapi.models.Teacher;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyCourseActivity extends AppCompatActivity {


    private List<Course> courses = new ArrayList<>();
    private GenericAdapter<Course> courseAdapter;
    private ListView courseLv;
    private SwipeRefreshLayout swipe;
    private String teacherId = "";
    private String teacherName = "";
    private Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        swipe = findViewById(R.id.activity_my_course_swipe);
        courseLv = findViewById(R.id.activity_my_course_lv);
        View view = LayoutInflater.from(this).inflate(R.layout.empty_data, null);
        courseLv.setEmptyView(view);
        teacher = AppUtils.getTeacher();
        teacherId = teacher.getTeacherId();
        teacherName = teacher.getName();
        findViewById(R.id.activity_my_course_bt_add_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(MyCourseActivity.this).inflate(R.layout.dialog_add_course, null);
                EditText edName=view.findViewById(R.id.dialog_add_course_ed_name);
                EditText edIntro=view.findViewById(R.id.dialog_add_course_ed_intro);
                AlertDialog edDialog = new AlertDialog.Builder(MyCourseActivity.this)
                        .setMessage("添加课程")
                        .setPositiveButton("确认添加", null)
                        .setNegativeButton("取消", null)
                        .setView(view).create();
                edDialog.show();
                edDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=edName.getText().toString();
                        String intro=edIntro.getText().toString();
                        if ("".equals(name)){
                            Toast.makeText(MyCourseActivity.this,
                                    "请填写课程名称", Toast.LENGTH_SHORT).show();
                        }else {
                            JSONObject jsonObject=new JSONObject();
                            JSONArray jsonArray=new JSONArray();
                            JSONObject object=new JSONObject();
                            try {
                                /*object.put("teacherId", teacher.getTeacherId());*/
                                object.put("name", name);
                                object.put("intro", intro);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(object);
                            try {
                                jsonObject.put("courses",jsonArray);
                                /*jsonObject.put("teacherId",teacher.getTeacherId());*/
                                /*jsonObject.put("user","teacher");*/
                                jsonObject.put("key",ApiConstants.getKey());
                                new RequestThread<MyCourseActivity>(MyCourseActivity.this,
                                        ApiConstants.addCourseUrl(),
                                        jsonObject.toString()) {
                                    @Override
                                    protected void onPostExecute(String s, MyCourseActivity activity) {
                                        try {
                                            s = StudentKeyUtils.decodeResponse(s).first;
                                            if (s.contains("\"RESULT\":\"S\"")) {
                                                getCourse();
                                            }
                                            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();

                                        } catch (Exception e) {
                                            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                                        }
                                        edDialog.dismiss();
                                    }
                                }.execute();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        courseAdapter = new GenericAdapter<Course>(this,
                R.layout.item_activity_teacher_course, courses) {
            @Override
            public void populate(ViewHolder viewHolder, Course course) {
                viewHolder.setTextView(R.id.item_activity_teacher_course_tv_courseName, course.getName());
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
                Course course=courses.get(position);
                Intent intent=new Intent(MyCourseActivity.this,ManagementCourseActivity.class);
                intent.putExtra("course",course);
                startActivity(intent);
            }
        });
        courseLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = courses.get(position);
                TextView textView = new TextView(MyCourseActivity.this);
                textView.setText("操作：“" + course.getName() + "”?");
                AlertDialog dialog = new AlertDialog.Builder(MyCourseActivity.this)
                        .setMessage("提示")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    /*jsonObject.put("teacherId", teacherId);
                                    jsonObject.put("user", "teacher");*/
                                    jsonObject.put("courseId", course.getId());
                                    jsonObject.put("key", ApiConstants.getKey());
                                    new RequestThread<MyCourseActivity>(MyCourseActivity.this,
                                            ApiConstants.getDeleteCourseUrl(),
                                            jsonObject.toString()) {
                                        @Override
                                        protected void onPostExecute(String s, MyCourseActivity activity) {
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
                        })
                        .setNegativeButton("编辑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                View view = LayoutInflater.from(MyCourseActivity.this).inflate(R.layout.dialog_add_course, null);
                                EditText edName=view.findViewById(R.id.dialog_add_course_ed_name);
                                edName.setText(course.getName());
                                EditText edIntro=view.findViewById(R.id.dialog_add_course_ed_intro);
                                edIntro.setText(course.getIntro());
                                AlertDialog edDialog = new AlertDialog.Builder(MyCourseActivity.this)
                                        .setMessage("编辑课程")
                                        .setPositiveButton("确定修改", null)
                                        .setNegativeButton("取消", null)
                                        .setView(textView).create();
                                edDialog.setView(view);
                                edDialog.show();
                                edDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String name=edName.getText().toString();
                                        String intro=edIntro.getText().toString();
                                        if ("".equals(name)){
                                            Toast.makeText(MyCourseActivity.this,
                                                    "请填写课程名称", Toast.LENGTH_SHORT).show();
                                        }else {
                                            JSONObject jsonObject = new JSONObject();
                                            course.setName(name);
                                            course.setIntro(intro);
                                            try {
                                                jsonObject.put("courseId", course.getId());
                                                /*jsonObject.put("teacherId", teacherId);
                                                jsonObject.put("user","teacher");*/
                                                jsonObject.put("course",course.toJSON());
                                                jsonObject.put("key", ApiConstants.getKey());
                                                new RequestThread<MyCourseActivity>(MyCourseActivity.this,
                                                        ApiConstants.getUpdateCourseUrl(),
                                                        jsonObject.toString()) {
                                                    @Override
                                                    protected void onPostExecute(String s, MyCourseActivity activity) {
                                                        try {
                                                            s = StudentKeyUtils.decodeResponse(s).first;
                                                            if (s.contains("\"RESULT\":\"S\"")) {
                                                                getCourse();
                                                            }
                                                            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();

                                                        } catch (Exception e) {
                                                            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                }.execute();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .setView(textView).create();
                dialog.show();
                return true;
            }
        });
        //下拉更新所有老师
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCourse();
            }
        });
        if (!teacherId.equals("") && !teacherName.equals("")) {
            getCourse();
        }
    }

    private void getCourse() {
        JSONObject jsonObject = new JSONObject();
        try {
          /*  jsonObject.put("teacherId", teacherId);
            jsonObject.put("user", "teacher");*/
            jsonObject.put("key", ApiConstants.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RequestThread<MyCourseActivity>(MyCourseActivity.this,
                ApiConstants.getCourseUrl(), jsonObject.toString()) {
            @Override
            protected void onPostExecute(String s, MyCourseActivity activity) {

                try {
                    s = StudentKeyUtils.decodeResponse(s).first;
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("RESULT").equals("S")) {
                        Gson gson = new Gson();
                        activity.courses.clear();
                        List<Course> courses = gson.fromJson(jsonObject.getString("courses"),
                                new TypeToken<LinkedList<Course>>() {
                                }.getType());
                        activity.courses.addAll(courses);
                        activity.courseAdapter.notifyDataSetChanged();
                        Toast.makeText(activity, "获取成功！\n" + "获取到" + courses.size() + "条数据",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "获取失败。。\n" + s, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "获取失败。。\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                activity.swipe.setRefreshing(false);
            }
        }.execute();
    }
}
