package net.lzzy.practiceapi.fragments.admin;


import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.fragments.BaseFragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagementOfTeachersFragment extends BaseFragment {

    private SearchView searchView;
    private ListView lv;
    private GenericAdapter<Teacher> adapter;
    private List<Teacher> teachers =new ArrayList<>();
    private SwipeRefreshLayout swipe;
    public ManagementOfTeachersFragment(){

    }
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_management_of_teachers;
    }

    @Override
    protected void populate() {
        searchView = find(R.id.fragment_management_of_teachers_SearchView);
        lv = find(R.id.fragment_management_of_teachers_lv);
        swipe = find(R.id.fragment_management_of_teachers_swipe);
        adapter = new GenericAdapter<Teacher>(getContext(),
                R.layout.item_fragment_management_of_teachers, teachers) {
            @Override
            public void populate(ViewHolder viewHolder, Teacher teacher) {
                viewHolder.setTextView(R.id.item_fragment_management_of_teachers_tv_studentName, teacher.getName());
                viewHolder.setTextView(R.id.item_fragment_management_of_teachers_tv_studentId, teacher.getTeacherId());
                viewHolder.setImageResource(R.id.item_fragment_management_of_teachers_img_studentImg,R.drawable.no);
                CheckBox checkBox=viewHolder.getView(R.id.item_fragment_management_of_teachers_cb);
                if (teacher.getValid()==1){
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
                        int valid=0;
                        if (isChecked){
                            valid=1;
                        }
                        authorization_teacher(valid, "admin",teacher.getTeacherId());
                    }
                });
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
        lv.setAdapter(adapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTeachers();
            }
        });
        getTeachers();
    }

    private void getTeachers() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("user","admin");
            jsonObject.put("key", ApiConstants.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RequestThread<ManagementOfTeachersFragment>(ManagementOfTeachersFragment.this,
                ApiConstants.getTeacherUrl(), jsonObject.toString()) {
            @Override
            protected void onPostExecute(String s, ManagementOfTeachersFragment fragment) {

                try {
                    s= StudentKeyUtils.decodeResponse(s).first;
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("RESULT").equals("S")){
                        Gson gson=new Gson();
                        fragment.teachers.clear();
                        List<Teacher> teachers=gson.fromJson(jsonObject.getString("teachers"),
                                new TypeToken<LinkedList<Teacher>>() {}.getType());
                        fragment.teachers.addAll(teachers);
                        fragment.adapter.notifyDataSetChanged();
                        Toast.makeText(fragment.getActivity(), "获取成功！\n"+"获取到老师"+teachers.size()+"条数据",
                                Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(fragment.getActivity(), "获取失败。。\n"+s, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(fragment.getActivity(), "获取失败。。\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                fragment.swipe.setRefreshing(false);
            }
        }.execute();
    }

    private void authorization_teacher(int valid, String admin,String teacherId) {
        JSONObject object = new JSONObject();
        try {
            object.put("valid", valid);
            object.put("teacherIds", new JSONArray().put(teacherId));
            //object.put("user", "admin");
            object.put("key", ApiConstants.getKey());
            new RequestThread<ManagementOfTeachersFragment>(ManagementOfTeachersFragment.this,
                    ApiConstants.getAuthorization_teacher(),
                    object.toString()) {
                @Override
                protected void onPostExecute(String s, ManagementOfTeachersFragment fragment) {
                    try {
                        s = StudentKeyUtils.decodeResponse(s).first;
                        JSONObject object1=new JSONObject(s);
                        if (s.contains("\"RESULT\":\"S\"")) {

                        }
                        getTeachers();
                        Toast.makeText(fragment.getActivity(), s+ teachers.size(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getAllTeacher( String admin) {
        JSONObject object = new JSONObject();
        try {
            object.put("user", admin);
            object.put("key", ApiConstants.getKey());
            new RequestThread<ManagementOfTeachersFragment>(ManagementOfTeachersFragment.this,
                    ApiConstants.getSearchStudentByAllUrl(),
                    object.toString()) {
                @Override
                protected void onPostExecute(String s, ManagementOfTeachersFragment fragment) {
                    try {
                        s = StudentKeyUtils.decodeResponse(s).first;
                        JSONObject object1=new JSONObject(s);
                        if (s.contains("\"RESULT\":\"S\"")) {
                        }
                        Toast.makeText(fragment.getActivity(), s+ teachers.size(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
