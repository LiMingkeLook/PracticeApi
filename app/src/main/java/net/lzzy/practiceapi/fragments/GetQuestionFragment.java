package net.lzzy.practiceapi.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.GetQuestionThread;
import net.lzzy.practiceapi.Thread.RequestThread;
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
public class GetQuestionFragment extends BaseFragment {

    private Button btnGetQuestion;
    private EditText hint;
    private EditText edIp;
    private EditText edPractice;
    private EditText edStudentId;
    private EditText edCourseId;

    public GetQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_get_question;
    }

    @Override
    protected void populate() {
        initView();
        edIp.setText(ApiConstants.getQuestionUrl());
        btnGetQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pId = edPractice.getText().toString();
                String ip = edIp.getText().toString();
                String courseId=edCourseId.getText().toString();
                String studentId=edStudentId.getText().toString();
                if (courseId.equals("")){
                    Toast.makeText(getContext(), "请输入课程ID", Toast.LENGTH_SHORT).show();
                }else if (studentId.equals("")){
                    Toast.makeText(getContext(), "请输入学号ID", Toast.LENGTH_SHORT).show();
                }else if (pId.equals("")) {
                    Toast.makeText(getContext(), "请输入练习ID", Toast.LENGTH_SHORT).show();
                } else if (ip.equals("")) {
                    Toast.makeText(getContext(), "请输入IP:post", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog=new AlertDialog.Builder(getContext()).
                            setMessage("正在获取题目。。")
                            .show();
                    hint.setText("正在获取题目。。");
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("practiceId",pId);
                        jsonObject.put("key", AppUtils.getKey());
                        jsonObject.put("studentId",studentId);
                        jsonObject.put("courseId",courseId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new RequestThread<GetQuestionFragment>(GetQuestionFragment.this, ip,jsonObject.toString()) {
                        @Override
                        protected void onPostExecute(String s, GetQuestionFragment getQuestionFragment) {
                            dialog.dismiss();
                            try {
                                s= StudentKeyUtils.decodeResponse(s).first;
                                if (s.contains("登录超时")){
                                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().finish();
                                }else {
                                    hint.setText(s);
                                    if (s.contains("RESULT\":\"F\"")){
                                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace(); hint.setText(e.getMessage());
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    }.execute();
                }
            }
        });
    }

    private void initView() {
        btnGetQuestion = find(R.id.fragment_get_question_bt_getQuestion);
        hint = find(R.id.fragment_get_question_ed_hint);
        edIp = find(R.id.fragment_get_question_ed_ip);
        edPractice = find(R.id.fragment_get_question_ed_practiceId);
        edStudentId = find(R.id.fragment_get_question_ed_studentId);
        edCourseId = find(R.id.fragment_get_question_ed_courseId);
    }


}
