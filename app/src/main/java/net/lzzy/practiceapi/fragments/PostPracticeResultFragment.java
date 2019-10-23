package net.lzzy.practiceapi.fragments;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.PracticeResultThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.models.student.Student;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostPracticeResultFragment extends BaseFragment {


    private EditText edPracticeId;
    private EditText edHint;
    private EditText edIp;
    private EditText edWrongTpye;
    private EditText edQuestionCount;
    private EditText edStudentId;
    private EditText edStudentAnswer;
    private Button btPost;
    private EditText edQuestionId;

    public PostPracticeResultFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_post_practice_result;
    }

    @Override
    protected void populate() {

        initView();
        edIp.setText(ApiConstants.getIpDelimiterPort());

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = edIp.getText().toString();
                String wrongTpye = edWrongTpye.getText().toString();
                String practiceId = edPracticeId.getText().toString();
                String studentId = edStudentId.getText().toString();
                String studentAnswer = edStudentAnswer.getText().toString();
                String questionId = edQuestionId.getText().toString();
                if (ip.equals("")) {
                    Toast.makeText(getContext(), "请输入ip:post", Toast.LENGTH_LONG).show();
                } else if (wrongTpye.equals("")) {
                    Toast.makeText(getContext(), "请输入错误类型", Toast.LENGTH_LONG).show();
                } else if (practiceId.equals("")) {
                    Toast.makeText(getContext(), "请输入practiceId", Toast.LENGTH_LONG).show();
                } else if (studentId.equals("")) {
                    Toast.makeText(getContext(), "请输入学生id", Toast.LENGTH_LONG).show();
                } else if (studentAnswer.equals("")) {
                    Toast.makeText(getContext(), "请输入studentAnswer", Toast.LENGTH_LONG).show();
                } else if (questionId.equals("")) {
                    Toast.makeText(getContext(), "请输入questionId", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(getContext()).
                            setMessage("正在上传练习结果。。")
                            .show();
                    edHint.setText("正在上传练习结果。。");
                    JSONObject object = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("practiceId", Integer.valueOf(practiceId));
                        jsonObject.put("questionId", Float.valueOf(questionId));
                        jsonObject.put("wrongType", wrongTpye);
                        jsonObject.put("studentAnswer",studentAnswer);
                        jsonArray.put(jsonObject);
                        object.put("Results", jsonArray);
                        object.put("key", ApiConstants.getKey());
                        new PracticeResultThread<PostPracticeResultFragment>(
                                PostPracticeResultFragment.this, ip, object.toString()) {
                            @Override
                            protected void onPostExecute(String s
                                    , PostPracticeResultFragment postPracticeResultFragment) {
                                dialog.dismiss();
                                try {
                                 /*   s = StudentKeyUtils.decodeResponse(s).first;*/
                                    edHint.setText(s);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    edHint.setText(e.getMessage());
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

        });
    }

    private void initView() {
        edQuestionId = find(R.id.fragment_post_practice_result_ed_questionId);
        edPracticeId = find(R.id.fragment_post_practice_result_ed_practiceId);
        edHint = find(R.id.fragment_post_practice_result_ed_hint);
        edIp = find(R.id.fragment_post_practice_result_ed_ip);
        edWrongTpye = find(R.id.fragment_post_practice_result_ed_wrongTpye);
        edStudentId = find(R.id.fragment_post_practice_result_ed_studentId);
        edStudentAnswer = find(R.id.fragment_post_practice_result_ed_studentAnswer);
        btPost = find(R.id.fragment_post_practice_result_bt_post);
    }


}
