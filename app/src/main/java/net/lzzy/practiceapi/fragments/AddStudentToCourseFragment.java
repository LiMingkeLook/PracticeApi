package net.lzzy.practiceapi.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.activities.student.TeacherCourseActivity;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.models.Student;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddStudentToCourseFragment extends BaseFragment {

    public static final String STUDENTS = "students";
    public static final String COURSE_ID = "courseId";
    private List<Student> students = new ArrayList<>();
    private int courseId;
    private GenericAdapter<Student> studentGenericAdapter;
    private ListView listView;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_add_student_to_course;
    }

    public AddStudentToCourseFragment(){}
    public static AddStudentToCourseFragment getInstance(int courseId) {
        AddStudentToCourseFragment fragment = new AddStudentToCourseFragment();
        Bundle args = new Bundle();
        args.putInt(COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getInt(COURSE_ID, -1);
        }
    }

    @Override
    protected void populate() {
        if (courseId != -1) {
            listView = find(R.id.fragment_add_student_to_course_lv);
            studentGenericAdapter = new GenericAdapter<Student>(getContext(),
                    R.layout.item_fragment_add_student_to_course, students) {
                @Override
                public void populate(ViewHolder viewHolder, Student student) {
                    viewHolder.setTextView(R.id.item_fragment_add_student_to_course_tv_studentName, student.getName());
                    viewHolder.setTextView(R.id.item_fragment_add_student_to_course_tv_studentId, student.getStudentId());
                    viewHolder.setImageResource(R.id.item_fragment_add_student_to_course_img_studentImg,R.drawable.no);
                    CheckBox checkBox=viewHolder.getView(R.id.item_fragment_add_student_to_course_cb);
                    if (student.getTakeEffect()==1){
                        checkBox.setChecked(true);
                        checkBox.setText("已授权");
                        checkBox.setTextColor(Color.parseColor("#38B31D"));
                    }else {
                        checkBox.setChecked(false);
                        checkBox.setText("未授权");
                        checkBox.setTextColor(Color.parseColor("#CA1919"));
                    }
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("courseId", courseId);
                                    object.put("studentId", student.getStudentId());
                                    object.put("key", AppUtils.getKey());
                                    new RequestThread<AddStudentToCourseFragment>(AddStudentToCourseFragment.this,
                                            ApiConstants.getCourseAppliedUrl(),
                                            object.toString()) {
                                        @Override
                                        protected void onPostExecute(String s, AddStudentToCourseFragment fragment) {
                                            try {
                                                s = StudentKeyUtils.decodeResponse(s).first;
                                                if (s.contains("\"RESULT\":\"S\"")) {
                                                }
                                                Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_LONG).show();

                                            } catch (Exception e) {
                                                Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {

                            }
                        }
                    });
                }

                @Override
                public boolean persistInsert(Student student) {
                    return false;
                }

                @Override
                public boolean persistDelete(Student student) {
                    return false;
                }
            };
            listView.setAdapter(studentGenericAdapter);
            gwtAllStudents();
        }
    }

    private void gwtAllStudents() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("teacherId","8888");
            jsonObject.put("key", AppUtils.getKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new RequestThread<AddStudentToCourseFragment>(AddStudentToCourseFragment.this,
                ApiConstants.getAllStudentUrl(), jsonObject.toString()) {
            @Override
            protected void onPostExecute(String s, AddStudentToCourseFragment fragment) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("RESULT").equals("S")) {
                        Gson gson = new Gson();
                        fragment.students.clear();
                        List<Student> students = gson.fromJson(jsonObject.getString("students"),
                                new TypeToken<LinkedList<Student>>() {
                                }.getType());
                        fragment.students.addAll(students);
                        fragment.studentGenericAdapter.notifyDataSetChanged();
                        Toast.makeText(fragment.getActivity(), s,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(fragment.getActivity(), "获取学生失败。。\n" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}
