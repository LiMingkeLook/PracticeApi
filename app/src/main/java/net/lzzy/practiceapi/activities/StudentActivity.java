package net.lzzy.practiceapi.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

public class StudentActivity extends AppCompatActivity {

    private FragmentPagerAdapter vpAdapter;
    private List<String> apis=new ArrayList<>();
    private ArrayAdapter<String> spAdapter;
    private SparseArray<Fragment> fragmentArray=new SparseArray<>();
    private StaticViewPager pager;
    private int onSp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student);

    }


}
