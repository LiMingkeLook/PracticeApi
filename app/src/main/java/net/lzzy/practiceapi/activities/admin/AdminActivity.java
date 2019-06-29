package net.lzzy.practiceapi.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.activities.BaseActivity;
import net.lzzy.practiceapi.fragments.admin.ManagementOfTeachersFragment;

public class AdminActivity extends BaseActivity {


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_admin;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_admin_Container;
    }

    @Override
    protected Fragment createFragment() {
        return new ManagementOfTeachersFragment();
    }
}
