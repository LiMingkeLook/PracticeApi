package net.lzzy.practiceapi.fragments;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.AddCouseFragmentThread;
import net.lzzy.practiceapi.Thread.EnrolledStudentThread;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCourseFragment extends BaseFragment {


    public AddCourseFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_add_course;
    }

    @Override
    protected void populate() {
        EditText hint = find(R.id.fragment_add_course_ed_on_ed_hint);
        Button button = find(R.id.fragment_add_course_btn_on);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip=ApiConstants.getIpDelimiterPort();
                JSONObject jsonObject=new JSONObject();
                JSONArray jsonArray=new JSONArray();
                JSONObject object=new JSONObject();
                try {
                    object.put("teacherId", 8888);
                    object.put("name", "hfdisahfianf");
                    object.put("intro", "afasfa");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(object);
                try {
                    jsonObject.put("courses",jsonArray);
                    jsonObject.put("teacherId","8888");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new AddCouseFragmentThread<AddCourseFragment>(AddCourseFragment.this, ip, jsonObject.toString()) {
                    @Override
                    protected void onPostExecute(String s, AddCourseFragment enrolledStudentFragment) {
                        try {
                            /*s= KeyUtils.decodeJSon(s);*/
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
