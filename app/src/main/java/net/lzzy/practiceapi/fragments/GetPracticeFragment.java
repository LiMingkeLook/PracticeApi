package net.lzzy.practiceapi.fragments;


import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.GetPracticeThread;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.activities.HomeActivity;
import net.lzzy.practiceapi.activities.LoginActivity;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetPracticeFragment extends BaseFragment {
    private EditText edCourseId;
    private EditText edStudentId;
    private Button btGetPractice;
    private EditText hint;
    private EditText edIp;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_get_practice;
    }

    @Override
    protected void populate() {
        initView();
        edIp.setText(ApiConstants.getPracticeUrl());
        btGetPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t1 = edCourseId.getText().toString();
                String t2 = edStudentId.getText().toString();
                String ip = edIp.getText().toString();
                if ("".equals(ip)) {
                    Toast.makeText(getContext(), "请输入地址", Toast.LENGTH_SHORT).show();
                } else if (!"".equals(t1) && !"".equals(t2)) {
                    AlertDialog dialog = new AlertDialog.Builder(getContext()).
                            setMessage("正在获取练习。。")
                            .show();
                    hint.setText("正在获取练习。。");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("courseId", Integer.valueOf(t1));
                        jsonObject.put("studentId", t2);
                        jsonObject.put("key", AppUtils.getKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new RequestThread<GetPracticeFragment>(GetPracticeFragment.this, ip, jsonObject.toString()) {
                        @Override
                        protected void onPostExecute(String s, GetPracticeFragment fragment) {
                            dialog.dismiss();
                            try {
                                s = StudentKeyUtils.decodeResponse(s).first;
                                if (s.contains("登录超时")) {
                                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();
                                } else {
                                    hint.setText(s);
                                    if (s.contains("RESULT\":\"F\"")) {
                                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hint.setText(e.getMessage());
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute();
                } else {
                    Toast.makeText(getContext(), "请输入参数", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        edIp = find(R.id.activity_main_et_getPractice_ip);
        edCourseId = find(R.id.activity_main_et_getPractice_CourseId);
        edStudentId = find(R.id.activity_main_et_getPractice_studentId);
        btGetPractice = find(R.id.activity_main_bt_getPractice);
        hint = find(R.id.activity_main_ed_hint);
    }


}
