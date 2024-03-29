
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.activities.HomeActivity;
import net.lzzy.practiceapi.activities.LoginActivity;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.models.Practice;
import net.lzzy.practiceapi.models.student.SOption;
import net.lzzy.practiceapi.models.student.SQuestion;
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

public class PracticeActivity extends AppCompatActivity {
    private List<Practice> practices = new ArrayList<>();
    private GenericAdapter<Practice> practiceAdapter;
    private ListView practiceLv;
    private SwipeRefreshLayout swipe;
    private String courseName = "";
    private Integer courseId;
    private Student student = AppUtils.getStudent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        swipe = findViewById(R.id.activity_practice_swipe);
        practiceLv = findViewById(R.id.activity_practice_lv);
        View view = LayoutInflater.from(this).inflate(R.layout.empty_data, null);
        practiceLv.setEmptyView(view);
        courseId = getIntent().getIntExtra("courseId",-1);
        courseName = getIntent().getStringExtra("courseName");
        TextView tvCourseName = findViewById(R.id.activity_practice_tv_courseName);
        tvCourseName.setText(courseName);
        practiceAdapter = new GenericAdapter<Practice>(this,
                R.layout.item_activity_practice, practices) {
            @Override
            public void populate(ViewHolder holder, Practice practice) {
                holder.setTextView(R.id.activity_practice_item_tv_name, practice.getName());
                Button btnOutlines = holder.getView(R.id.activity_practice_item_btn_outlines);
                TextView hint = holder.getView(R.id.activity_practice_item_tv_hint);
                hint.setText(practice.getOutlines());
                btnOutlines.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hint.getVisibility() == View.VISIBLE) {
                            btnOutlines.setText("展开要点");
                            hint.setVisibility(View.GONE);
                        } else {
                            btnOutlines.setText("收起要点");
                            hint.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }

            @Override
            public boolean persistInsert(Practice practice) {
                return false;
            }

            @Override
            public boolean persistDelete(Practice practice) {
                return false;
            }
        };
        practiceLv.setAdapter(practiceAdapter);
        practiceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pid = practices.get(position).getId();
                    AlertDialog dialog = new AlertDialog.Builder(PracticeActivity.this)
                            .setMessage("是否下载题目")
                            .setPositiveButton("下载", null)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            JSONObject object = new JSONObject();
                            try {
                                object.put("courseId", practices.get(position).getCourseId());
                                object.put("practiceId", pid);
                                object.put("key", ApiConstants.getKey());
                                new RequestThread<PracticeActivity>(PracticeActivity.this,
                                        ApiConstants.getQuestionUrl(),
                                        object.toString()) {
                                    @Override
                                    protected void onPostExecute(String s, PracticeActivity activity) {
                                        try {
                                            s = StudentKeyUtils.decodeResponse(s).first;
                                            JSONObject object1=new JSONObject(s);
                                            if (object1.getString("RESULT").equals("S")) {
                                                Gson gson=new Gson();
                                                JSONArray jsonArray=object1.getJSONArray("questions");
                                                List<SQuestion> sQuestions=new ArrayList<>();
                                                for (int i=0;i<jsonArray.length();i++){
                                                    JSONObject object2=jsonArray.getJSONObject(i);
                                                    JSONArray opions=object2.getJSONArray("options");
                                                    object2.remove("options");
                                                    SQuestion question=gson.fromJson(object2.toString(),SQuestion.class);
                                                    List<SOption> options=new ArrayList<>();
                                                    for (int j=0;j<opions.length();j++){
                                                        SOption option=gson.fromJson(opions.getString(i),SOption.class);
                                                        options.add(option);
                                                    }
                                                    question.setOptions(options);
                                                    sQuestions.add(question);
                                                }
                                                AppUtils.setsQuestions(sQuestions);
                                                Intent intent=new Intent(PracticeActivity.this,QuestionActivity.class);
                                                startActivity(intent);
                                                dialog.dismiss();
                                            }
                                            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    });

            }
        });
        //下拉更新所有老师
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPractice(courseId);
            }

        });
        if (!"".equals(courseId) && !"".equals(courseName)) {
            getPractice(courseId);
        }
    }
    private void getPractice(Integer courseId) {
        AlertDialog dialog = new AlertDialog.Builder(PracticeActivity.this).
                setMessage("正在获取练习。。")
                .show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("courseId", courseId);
            jsonObject.put("studentId",student.getStudentId() );
            jsonObject.put("key", ApiConstants.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RequestThread<PracticeActivity>(PracticeActivity.this,
                ApiConstants.getPracticeUrl(), jsonObject.toString()) {
            @Override
            protected void onPostExecute(String s, PracticeActivity activity) {
                dialog.dismiss();
                try {
                    s = StudentKeyUtils.decodeResponse(s).first;
                    if (s.contains("登录超时")) {
                        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity, HomeActivity.class));
                        activity.finish();
                    } else {
                        if (s.contains("RESULT\":\"F\"")) {
                            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
                        }else if (s.contains("RESULT\":\"S\"")){
                            Gson gson=new Gson();
                            JSONObject object=new JSONObject(s);
                            List<Practice> practices1=gson.fromJson(object.getString("practices"),new TypeToken<LinkedList<Practice>>() {}.getType());
                            activity.practices.clear();
                            activity.practices.addAll(practices1);
                            activity.practiceAdapter.notifyDataSetChanged();

                            Toast.makeText(activity, "获取到："+practices1.size()+"个练习", Toast.LENGTH_SHORT).show();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                activity.swipe.setRefreshing(false);
            }
        }.execute();
    }
}
