package net.lzzy.practiceapi.fragments;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.EnrolledStudentThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnrolledStudentFragment extends BaseFragment {


    public EnrolledStudentFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_enrolled_student;
    }

    @Override
    protected void populate() {
        EditText hint = find(R.id.fragment_enrolled_student_ed_hint);
        Button button = find(R.id.fragment_enrolled_student_btn_on);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip= ApiConstants.getIpDelimiterPort();

                String studentId="20170313007";
                String name="jjj";
                String email="164646@qq.com";
                String password="111";
                String iphone="17777581666";
                String gender="男";
                JSONObject object=new JSONObject();
                try {
                    object.put("studentId",studentId);
                    object.put("name",name);
                    object.put("email",email);
                    object.put("password",password);
                    object.put("iphone",iphone);
                    object.put("gender",gender);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new EnrolledStudentThread<EnrolledStudentFragment>(EnrolledStudentFragment.this, ip, object.toString()) {
                    @Override
                    protected void onPostExecute(String s, EnrolledStudentFragment enrolledStudentFragment) {
                        try {
                           /* s= KeyUtils.decodeJSon(s);*/
                            hint.setText(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                            hint.setText(e.getMessage());
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
            }
        });
    }


}
