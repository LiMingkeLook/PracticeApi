package net.lzzy.practiceapi.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.fragments.AddCourseFragment;
import net.lzzy.practiceapi.fragments.EnrolledStudentFragment;
import net.lzzy.practiceapi.fragments.EnrolledTeacherFragment;
import net.lzzy.practiceapi.fragments.GetPracticeFragment;
import net.lzzy.practiceapi.fragments.GetQuestionFragment;
import net.lzzy.practiceapi.fragments.PostPracticeResultFragment;
import net.lzzy.practiceapi.utils.StaticViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter vpAdapter;
    private List<String> apis=new ArrayList<>();
    private ArrayAdapter<String> spAdapter;
    private SparseArray<Fragment> fragmentArray=new SparseArray<>();
    private StaticViewPager pager;
    private int onSp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView api =findViewById(R.id.activity_main_tv_api);
        pager=findViewById(R.id.activity_main_vp);
        apis.add("获取练习题");
        apis.add("获取题目");
        apis.add("提交练习结果");
        apis.add("注册学生");
        apis.add("注册教师");
        apis.add("添加课程");
        vpAdapter =new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return apis.size();
            }

            @Override
            public Fragment getItem(int position) {
                Fragment fragment=fragmentArray.get(position);
                if (fragment!=null){
                    return fragment;
                }else {
                    switch (position){
                        case 0:
                            fragment=new GetPracticeFragment();
                            fragmentArray.append(position,fragment);
                            break;
                        case 1:
                           fragment=new GetQuestionFragment();
                            fragmentArray.append(position,fragment);
                            break;
                        case 2:
                           fragment=new PostPracticeResultFragment();
                            fragmentArray.append(position,fragment);
                            break;
                        case 3:
                            fragment=new EnrolledStudentFragment();
                            fragmentArray.append(position,fragment);
                            break;
                        case 4:
                            fragment=new EnrolledTeacherFragment();
                            fragmentArray.append(position,fragment);
                            break;
                        case 5:
                            fragment=new AddCourseFragment();
                            fragmentArray.append(position,fragment);
                        default:
                            break;
                    }
                }
                return fragment;
            }
        };
        api.setText(apis.get(0));
        api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选择接口");
                String[] items = new String[apis.size()];
                apis.toArray(items);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onSp= which;
                        api.setText(items[which]);
                        pager.setCurrentItem(which);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        pager.setAdapter(vpAdapter);
    }


}
