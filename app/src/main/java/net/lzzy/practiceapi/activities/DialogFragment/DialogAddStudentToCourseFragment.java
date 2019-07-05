package net.lzzy.practiceapi.activities.DialogFragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.models.teacher.Student;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;
import net.lzzy.practiceapi.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DialogAddStudentToCourseFragment extends DialogFragment {

    private static final String COURSE_ID = "courseId";
    private List<Student> students = new ArrayList<>();
    private int courseId;
    private GenericAdapter<Student> studentGenericAdapter;
    private ListView listView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_add_student_to_course, null);
        View empty = inflater.inflate(R.layout.empty_data, null);
        listView = view.findViewById(R.id.dialog_fragment_add_student_to_course_lv);
        listView.setEmptyView(empty);
        SearchView search = view.findViewById(R.id.dialog_fragment_add_student_to_course_search);
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandlerRadio() {
            @Override
            public void handleQuery(String kw) {
                if ("".equals(kw)) {
                    getAllStudents();
                } else {
                    searchStudentByKey(kw);
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ("".equals(newText)) {
                    getAllStudents();
                    return true;
                } else {
                    return false;
                }
            }
        });
        studentGenericAdapter = new GenericAdapter<Student>(getContext(),
                R.layout.item_fragment_add_student_to_course, students) {
            @Override
            public void populate(ViewHolder viewHolder, Student student) {
                viewHolder.setTextView(R.id.item_fragment_add_student_to_course_tv_studentName, student.getName());
                viewHolder.setTextView(R.id.item_fragment_add_student_to_course_tv_studentId, student.getStudentId());
                viewHolder.setImageResource(R.id.item_fragment_add_student_to_course_img_studentImg, R.drawable.no);
                CheckBox checkBox = viewHolder.getView(R.id.item_fragment_add_student_to_course_cb);
                if (student.getMyStudent()) {
                    checkBox.setChecked(true);
                    checkBox.setTag(true);
                    checkBox.setText("已添加");
                    checkBox.setTextColor(Color.parseColor("#38B31D"));
                } else {
                    checkBox.setChecked(false);
                    checkBox.setTag(false);
                    checkBox.setText("未添加");
                    checkBox.setTextColor(Color.parseColor("#CA1919"));
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int takeEffect = 1;
                        if (!isChecked) {
                            takeEffect = -1;
                        }
                        getCourseApplied(takeEffect, student);
                    }
                });
            }

            @Override
            public boolean persistInsert(Student student) {
                students.add(student);
                return false;
            }

            @Override
            public boolean persistDelete(Student student) {
                students.remove(student);
                return false;
            }
        };
        listView.setAdapter(studentGenericAdapter);
        getAllStudents();
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage("课程内添加删除学生")
                .setNegativeButton("关闭", null)
                .setView(view)
                .create();
        dialog.show();
        return dialog;
    }

    private void searchStudentByKey(String kw) {
        JSONObject object = new JSONObject();
        try {
            object.put("searchKey", kw);
            object.put("key", ApiConstants.getKey());
            new RequestThread<DialogAddStudentToCourseFragment>(DialogAddStudentToCourseFragment.this,
                    ApiConstants.getSearchStudentByAllUrl(),
                    object.toString()) {
                @Override
                protected void onPostExecute(String s, DialogAddStudentToCourseFragment fragment) {
                    try {
                        s = StudentKeyUtils.decodeResponse(s).first;
                        JSONObject object1 = new JSONObject(s);
                        if (s.contains("\"RESULT\":\"S\"")) {
                            Gson gson = new Gson();
                            List<Student> students = gson.fromJson(object1.getString("students"),
                                    new TypeToken<LinkedList<Student>>() {
                                    }.getType());
                            fragment.studentGenericAdapter.clear();
                            fragment.studentGenericAdapter.addAll(students);
                        }
                        Toast.makeText(fragment.getActivity(), s + students.size(), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param takeEffect 1:添加学生到课程，-1：从课程移除学生
     * @param student
     */
    private void getCourseApplied(int takeEffect, Student student) {
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            object.put("takeEffect", takeEffect);
            object.put("courseId", courseId);
            /*object.put("teacherId", AppUtils.getTeacher().getTeacherId());*/
            object.put("studentIds", jsonArray.put(student.getStudentId()));
            object.put("key", ApiConstants.getKey());
            new RequestThread<DialogAddStudentToCourseFragment>(DialogAddStudentToCourseFragment.this,
                    ApiConstants.getCourseAppliedUrl(),
                    object.toString()) {
                @Override
                protected void onPostExecute(String s, DialogAddStudentToCourseFragment fragment) {
                    try {
                        s = StudentKeyUtils.decodeResponse(s).first;
                        if (s.contains("\"RESULT\":\"S\"")) {
                            if (takeEffect == -1) {
                                Toast.makeText(fragment.getActivity(), "删除成功", Toast.LENGTH_LONG).show();
                            } else if (takeEffect == 1) {
                                Toast.makeText(fragment.getActivity(), "添加成功", Toast.LENGTH_LONG).show();
                            }
                            //getAllStudents();
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
    }

    public static DialogAddStudentToCourseFragment getInstance(int courseId) {
        DialogAddStudentToCourseFragment fragment = new DialogAddStudentToCourseFragment();
        Bundle args = new Bundle();
        args.putInt(COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getInt(COURSE_ID, -1);
        }
    }


    private void getAllStudents() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("courseId", courseId);
            jsonObject.put("key", ApiConstants.getKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new RequestThread<DialogAddStudentToCourseFragment>(DialogAddStudentToCourseFragment.this,
                ApiConstants.getAllStudentAndIsTeacherCoursesStudent(), jsonObject.toString()) {
            @Override
            protected void onPostExecute(String s, DialogAddStudentToCourseFragment fragment) {
                try {
                    JSONObject jsonObject = new JSONObject(StudentKeyUtils.decodeResponse(s).first);
                    if (jsonObject.getString("RESULT").equals("S")) {
                        Gson gson = new Gson();
                        List<Student> students = gson.fromJson(jsonObject.getString("students"),
                                new TypeToken<LinkedList<Student>>() {
                                }.getType());
                        fragment.studentGenericAdapter.clear();
                        fragment.studentGenericAdapter.addAll(students);
                        Toast.makeText(fragment.getActivity(), "获取全部学生成功，数量：" + students.size(),
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
