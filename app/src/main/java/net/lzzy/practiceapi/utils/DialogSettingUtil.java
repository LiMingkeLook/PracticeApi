package net.lzzy.practiceapi.utils;

import androidx.fragment.app.FragmentManager;

import net.lzzy.practiceapi.activities.DialogFragment.DialogSettingFragment;

public class DialogSettingUtil {
    public static void showLoginDialog(FragmentManager manager) {
        DialogSettingFragment dialog = new DialogSettingFragment();
        dialog.show(manager, "显示IP设置");
    }
}
