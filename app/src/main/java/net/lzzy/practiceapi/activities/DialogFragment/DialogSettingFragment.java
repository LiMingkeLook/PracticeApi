package net.lzzy.practiceapi.activities.DialogFragment;


import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogSettingFragment extends DialogFragment {

    private EditText edIp;
    private EditText edPort;

    public interface SettingIPListener {
        void onSettingIPInputComplete(String ip, String port);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_setting, null);
        edIp = view.findViewById(R.id.fragment_setting_ed_ip);
        edPort = view.findViewById(R.id.fragment_setting_ed_port);
        AlertDialog  dialog= new AlertDialog.Builder(getActivity())
        .setPositiveButton("设置IP",null)
                .setNegativeButton("取消",null)
                .setView(view)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = edIp.getText().toString();
                String port = edPort.getText().toString();
                if ("".equals(ip)) {
                    Toast.makeText(getActivity(), "请输入IP", Toast.LENGTH_SHORT).show();
                } else if ("".equals(port)) {
                    Toast.makeText(getActivity(), "请输入PORT", Toast.LENGTH_SHORT).show();
                } else {
                    SettingIPListener listener = (SettingIPListener) getActivity();
                    listener.onSettingIPInputComplete(ip, port);
                    ApiConstants.setIpDelimiterPort(ip , port);
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }

}
