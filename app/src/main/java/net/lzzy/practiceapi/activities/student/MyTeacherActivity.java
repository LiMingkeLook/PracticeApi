package net.lzzy.practiceapi.activities.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.models.student.Student;
import net.lzzy.practiceapi.models.Teacher;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyTeacherActivity extends AppCompatActivity {

    private List<Teacher> teachers=new ArrayList<>();
    private GenericAdapter<Teacher> teacherGenericAdapter;
    private ListView teacherLv;
    private SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teacher);
                swipe = findViewById(R.id.activity_my_teacher_swipe);
        teacherLv = findViewById(R.id.activity_my_teacher_lv);
        View view= LayoutInflater.from(this).inflate(R.layout.empty_data, null);
        teacherLv.setEmptyView(view);
        Gson gson=new Gson();
        try {
            teachers.addAll(gson.fromJson(getIntent().getStringExtra("teachers"),
                    new TypeToken<LinkedList<Teacher>>() {}.getType()));
        }catch (Exception e){
            Toast.makeText(this, "进入预览模式", Toast.LENGTH_SHORT).show();
        }

        teacherGenericAdapter = new GenericAdapter<Teacher>(this,
                R.layout.item_activity_my_teacher,teachers) {
            @Override
            public void populate(ViewHolder viewHolder, Teacher teacher) {
                viewHolder.setTextView(R.id.item_activity_my_teacher_tv_teacherName,teacher.getName());
                viewHolder.setImageResource(R.id.item_activity_my_teacher_img_isMyTeacher,
                        teacher.isMyTeacher()?R.drawable.is_my_teacher:R.drawable.no_my_teacher);
            }

            @Override
            public boolean persistInsert(Teacher teacher) {
                return false;
            }

            @Override
            public boolean persistDelete(Teacher teacher) {
                return false;
            }
        };
        teacherLv.setAdapter(teacherGenericAdapter);
        teacherLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Teacher teacher= teachers.get(position);
                Intent intent=new Intent(MyTeacherActivity.this,TeacherCourseActivity.class);
                intent.putExtra("teacherId",teacher.getTeacherId());
                intent.putExtra("teacherName",teacher.getName());
                startActivity(intent);
            }
        });
        //下拉更新所有老师
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Student student=AppUtils.getStudent();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("user","student");
                    jsonObject.put("studentId",student.getStudentId());
                    jsonObject.put("key", ApiConstants.getKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new RequestThread<MyTeacherActivity>(MyTeacherActivity.this,
                        ApiConstants.getTeacherUrl(), jsonObject.toString()) {
                    @Override
                    protected void onPostExecute(String s, MyTeacherActivity myTeacherActivity) {

                        try {
                            s=StudentKeyUtils.decodeResponse(s).first;
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("RESULT").equals("S")){
                                Gson gson=new Gson();
                                myTeacherActivity.teachers.clear();
                                List<Teacher> teachers=gson.fromJson(jsonObject.getString("teachers"),
                                        new TypeToken<LinkedList<Teacher>>() {}.getType());
                                myTeacherActivity.teachers.addAll(teachers);
                                myTeacherActivity.teacherGenericAdapter.notifyDataSetChanged();
                                Toast.makeText(myTeacherActivity, "获取成功！\n"+"获取到"+teachers.size()+"条数据",
                                        Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(myTeacherActivity, "获取失败。。\n"+s, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(myTeacherActivity, "获取失败。。\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        myTeacherActivity.swipe.setRefreshing(false);
                    }
                }.execute();
            }
        });
    }


}
