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
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;

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
    private EditText edIphoneNo;
    private EditText edQuestionCount;
    private EditText edScroreRatio;
    private EditText edWrongQuestionIds;
    private Button btPost;

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
                String ip=edIp.getText().toString();
                String iphoneNo= edIphoneNo.getText().toString();
                String practiceId= edPracticeId.getText().toString();
                String scroreRatio= edScroreRatio.getText().toString();
                String wrongQuestionIds= edWrongQuestionIds.getText().toString();
                if (ip.equals("")){
                    Toast.makeText(getContext(), "请输入ip:post", Toast.LENGTH_LONG).show();
                }else if (iphoneNo.equals("")){
                    Toast.makeText(getContext(), "请输入手机号或设备mac", Toast.LENGTH_LONG).show();
                }else if (practiceId.equals("")){
                    Toast.makeText(getContext(), "请输入practiceId", Toast.LENGTH_LONG).show();
                }else if (scroreRatio.equals("")){
                    Toast.makeText(getContext(), "请输入scroreRatio", Toast.LENGTH_LONG).show();
                }else if (wrongQuestionIds.equals("")){
                    Toast.makeText(getContext(), "请输入wrongQuestionIds", Toast.LENGTH_LONG).show();
                }else {
                    AlertDialog dialog=new AlertDialog.Builder(getContext()).
                            setMessage("正在上传练习结果。。")
                            .show();
                    edHint.setText("正在上传练习结果。。");
                    JSONObject object=new JSONObject();
                    JSONArray jsonArray=new JSONArray();
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("practiceId",Integer.valueOf(practiceId));
                        jsonObject.put("phoneNo",iphoneNo);
                        jsonObject.put("scroreRatio",Float.valueOf(scroreRatio));
                        jsonObject.put("wrongQuestionIds",wrongQuestionIds);
                        jsonArray.put(jsonObject);
                        object.put("Results",jsonArray);
                        new PracticeResultThread<PostPracticeResultFragment>(
                                PostPracticeResultFragment.this, ip,object.toString()) {
                            @Override
                            protected void onPostExecute(String s
                                    , PostPracticeResultFragment postPracticeResultFragment) {
                                dialog.dismiss();
                                try {
                                    s= KeyUtils.decodeJSon(s);
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
        edPracticeId = find(R.id.fragment_post_practice_result_ed_practiceId);
        edHint = find(R.id.fragment_post_practice_result_ed_hint);
        edIp = find(R.id.fragment_post_practice_result_ed_ip);
        edIphoneNo = find(R.id.fragment_post_practice_result_ed_phoneNo);
        edScroreRatio = find(R.id.fragment_post_practice_result_ed_scroreRatio);
        edWrongQuestionIds = find(R.id.fragment_post_practice_result_ed_wrongQuestionIds);
        btPost = find(R.id.fragment_post_practice_result_bt_post);
    }


}
