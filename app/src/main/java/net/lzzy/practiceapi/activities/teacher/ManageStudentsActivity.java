package net.lzzy.practiceapi.activities.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.activities.DialogFragment.DialogAddStudentToCourseFragment;
import net.lzzy.practiceapi.models.Course;
import net.lzzy.practiceapi.models.student.Student;
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

public class ManageStudentsActivity extends AppCompatActivity {
    private List<Student> students = new ArrayList<>();
    private GenericAdapter<Student> studentGenericAdapter;
    private ListView studentLv;
    private SwipeRefreshLayout swipe;
    private Course course;
    private TextView edCourseName;
    private FragmentManager manager=getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        course = getIntent().getParcelableExtra("course");
        if (course == null) {
            Toast.makeText(this, "未获取到课程信息，请重新进入", Toast.LENGTH_LONG).show();
        } else {
            swipe = findViewById(R.id.activity_manage_students_swipe);
            studentLv = findViewById(R.id.activity_manage_students_lv);
            edCourseName = findViewById(R.id.activity_manage_students_tv_courseName);
            View view = LayoutInflater.from(this).inflate(R.layout.empty_data, null);
            studentLv.setEmptyView(view);
            edCourseName.setText(course.getName());
            findViewById(R.id.activity_manage_students_bt_add_student).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogAddStudentToCourseFragment dialog=DialogAddStudentToCourseFragment.getInstance(course.getId());
                    dialog.show(manager,"课程内添加删除学生");
                }
            });
            studentGenericAdapter = new GenericAdapter<Student>(this,
                    R.layout.item_activity_manage_students, students) {
                @Override
                public void populate(ViewHolder viewHolder, Student student) {
                    viewHolder.setTextView(R.id.item_activity_manage_students_tv_studentName, student.getName());
                    viewHolder.setTextView(R.id.item_activity_manage_students_tv_studentId, student.getStudentId());
                    viewHolder.setImageResource(R.id.item_activity_manage_students_img_studentImg,R.drawable.no);
                    RadioGroup rg=viewHolder.getView(R.id.item_activity_manage_students_rg);
                    RadioButton on=viewHolder.getView(R.id.item_activity_manage_students_rb_on);
                    RadioButton off=viewHolder.getView(R.id.item_activity_manage_students_rb_off);
                    if (student.getTakeEffect()==1){
                        on.setChecked(true);
                    }else {
                        off.setChecked(true);
                    }
                    on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                int takeEffect=1;
                                courseAppliedByTeacher(takeEffect, student);
                            }
                        }
                    });
                    off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                int takeEffect=0;
                                courseAppliedByTeacher(takeEffect, student);
                            }
                        }
                    });
                }
                @Override
                public boolean persistInsert(Student course) {
                    return false;
                }

                @Override
                public boolean persistDelete(Student course) {
                    return false;
                }
            };
            studentLv.setAdapter(studentGenericAdapter);
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getMyStudent();
                }
            });
            getMyStudent();
        }
    }

    private void courseAppliedByTeacher(int takeEffect, Student student) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("courseId", course.getId());
            /*jsonObject.put("teacherId", AppUtils.getTeacher().getTeacherId());*/
            /*jsonObject.put("user","teacher");*/
            jsonObject.put("takeEffect",takeEffect);
            jsonObject.put("studentIds",new JSONArray().put(student.getStudentId()));
            jsonObject.put("key", ApiConstants.getKey());
            new RequestThread<ManageStudentsActivity>(ManageStudentsActivity.this,
                    ApiConstants.getCourseAppliedUrl(),
                    jsonObject.toString()) {
                @Override
                protected void onPostExecute(String s, ManageStudentsActivity activity) {
                    try {
                        s = StudentKeyUtils.decodeResponse(s).first;
                        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                    }
                    getMyStudent();
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMyStudent(){
        swipe.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("courseId",course.getId());
            /*jsonObject.put("user", "teacher");*/
            jsonObject.put("key", ApiConstants.getKey());
            new RequestThread<ManageStudentsActivity>(ManageStudentsActivity.this,
                    ApiConstants.getStudentByCourseId(),
                    jsonObject.toString()) {
                @Override
                protected void onPostExecute(String s, ManageStudentsActivity activity) {
                    try {
                        Gson gson=new Gson();
                        s = StudentKeyUtils.decodeResponse(s).first;
                        JSONObject object=new JSONObject(s);
                        if (object.optString("RESULT","").equals("S")) {
                            List<Student > students=gson.fromJson(object.getString("students"),new TypeToken<LinkedList<Student>>() {}.getType());
                            activity.students.clear();
                            activity.students.addAll(students);
                            activity.studentGenericAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(activity, "刷新学生列表成功，数量："+students.size(), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();

                    }
                    activity.swipe.setRefreshing(false);
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
