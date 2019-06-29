package net.lzzy.practiceapi.activities;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.Thread.LoginThread;
import net.lzzy.practiceapi.Thread.RequestThread;
import net.lzzy.practiceapi.activities.admin.AdminActivity;
import net.lzzy.practiceapi.activities.student.MyTeacherActivity;
import net.lzzy.practiceapi.activities.teacher.MyCourseActivity;
import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.activities.DialogFragment.DialogSettingFragment;
import net.lzzy.practiceapi.models.Admin;
import net.lzzy.practiceapi.models.student.Student;
import net.lzzy.practiceapi.models.Teacher;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.DialogSettingUtil;
import net.lzzy.practiceapi.utils.StudentKeyUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements DialogSettingFragment.SettingIPListener {
    private EditText edId;
    private EditText edname;
    private EditText edemail;
    private EditText edpassword;
    private EditText edpassword2;
    private EditText ediphone;
    private RadioGroup rgGender;
    private String user;
    private String rg;
    private TextView tvIp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvIp = findViewById(R.id.login_ip);
        tvIp.setOnClickListener(v -> DialogSettingUtil.showLoginDialog(getSupportFragmentManager()));
        tvIp.setText("当前ip："+ApiConstants.getIpDelimiterPort()+" 触摸设置ip");
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
        user = getIntent().getStringExtra("user");
        Button register = findViewById(R.id.register);
        register.setText("注册 " + user);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_register, null);
                edId = view.findViewById(R.id.studentId);
                edname = view.findViewById(R.id.name);
                edemail = view.findViewById(R.id.email);
                edpassword = view.findViewById(R.id.paw);
                edpassword2 = view.findViewById(R.id.paw2);
                ediphone = view.findViewById(R.id.iphone);
                rgGender = view.findViewById(R.id.rg);

                if (tvIp.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "ip端口未输入", Toast.LENGTH_SHORT).show();
                } else {
                    if (user.equals("student")) {
                        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("注册学生")
                                .setPositiveButton("注册", null)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setView(view).create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = edId.getText().toString();
                                String name = edname.getText().toString();
                                String email = edemail.getText().toString();
                                String password = edpassword.getText().toString();
                                String password2 = edpassword2.getText().toString();
                                String iphone = ediphone.getText().toString();
                                rg = "男";
                                if (rgGender.getCheckedRadioButtonId() == R.id.nan) {
                                    rg = "男";
                                } else {
                                    rg = "女";
                                }
                                if (id  .equals("")) {
                                    Toast.makeText(LoginActivity.this, "学号为空", Toast.LENGTH_SHORT).show();
                                } else if (name .equals("")) {
                                    Toast.makeText(LoginActivity.this, "姓名为空", Toast.LENGTH_SHORT).show();
                                } else if (email  .equals("")) {
                                    Toast.makeText(LoginActivity.this, "邮箱为空", Toast.LENGTH_SHORT).show();
                                } else if (password  .equals("")) {
                                    Toast.makeText(LoginActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                                } else if (password2 .equals("")) {
                                    Toast.makeText(LoginActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                                } else if (!password.equals(password2)){
                                    Toast.makeText(LoginActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                                }else if (iphone.equals("")) {
                                    Toast.makeText(LoginActivity.this, "手机为空", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("role", user);
                                        object.put("studentId", id);
                                        object.put("name", name);
                                        object.put("email", email);
                                        object.put("password", password);
                                        object.put("iphone", iphone);
                                        object.put("gender", rg);
                                        new RequestThread<LoginActivity>(LoginActivity.this,
                                                ApiConstants.getRegisterUrl(),
                                                object.toString()) {
                                            @Override
                                            protected void onPostExecute(String s, LoginActivity loginActivity) {
                                                try {
                                                    s = StudentKeyUtils.decodeResponse(s).first;
                                                    if (s.contains("\"RESULT\":\"S\"")) {
                                                        dialog.dismiss();
                                                    }
                                                    Toast.makeText(loginActivity, s, Toast.LENGTH_LONG).show();

                                                } catch (Exception e) {

                                                }
                                            }
                                        }.execute();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("注册教师")
                                .setPositiveButton("注册", null)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setView(view)
                                .create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = edId.getText().toString();
                                String name = edname.getText().toString();
                                String email = edemail.getText().toString();
                                String password = edpassword.getText().toString();
                                String password2 = edpassword2.getText().toString();
                                String iphone = ediphone.getText().toString();
                                rg = "男";
                                if (rgGender.getCheckedRadioButtonId() == R.id.nan) {
                                    rg = "男";
                                } else {
                                    rg = "女";
                                }
                                if ("".equals(id)) {
                                    Toast.makeText(LoginActivity.this, "学号为空", Toast.LENGTH_SHORT).show();
                                } else if ("".equals(name)) {
                                    Toast.makeText(LoginActivity.this, "姓名为空", Toast.LENGTH_SHORT).show();
                                } else if ("".equals(email)) {
                                    Toast.makeText(LoginActivity.this, "邮箱为空", Toast.LENGTH_SHORT).show();
                                } else if ("".equals(password)) {
                                    Toast.makeText(LoginActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                                } else if ("".equals(password2)) {
                                    Toast.makeText(LoginActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                                } else if (!password.equals(password2)){
                                    Toast.makeText(LoginActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                                }else if ("".equals(iphone)) {
                                    Toast.makeText(LoginActivity.this, "手机为空", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("role", user);
                                        object.put("teacherId", id);
                                        object.put("name", name);
                                        object.put("email", email);
                                        object.put("password", password);
                                        object.put("iphone", iphone);
                                        object.put("gender", rg);
                                        new RequestThread<LoginActivity>(LoginActivity.this,
                                                ApiConstants.getRegisterUrl(),
                                                object.toString()) {
                                            @Override
                                            protected void onPostExecute(String s, LoginActivity loginActivity) {
                                                try {
                                                    s = StudentKeyUtils.decodeResponse(s).first;
                                                    if (s.contains("\"RESULT\":\"S\"")) {
                                                        dialog.dismiss();
                                                    }
                                                    Toast.makeText(loginActivity, s, Toast.LENGTH_LONG).show();
                                                } catch (Exception e) {

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

                }
            }
        });
        if (user.equals("admin")){
            register.setVisibility(View.GONE);
        }
        findViewById(R.id.skip_login).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user.equals("student")) {
                            startActivity(new Intent(LoginActivity.this, MyTeacherActivity.class));
                        } else if (user.equals("teacher")){
                            startActivity(new Intent(LoginActivity.this, MyCourseActivity.class));
                        }else if (user.equals("admin")){
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        }else {
                            Toast.makeText(LoginActivity.this, "请从新选择角色", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        loginButton.setText(user + " Login");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iphone = usernameEditText.getText().toString();
                String paw = passwordEditText.getText().toString();
                if (iphone.equals("") || paw.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名密码", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).
                            setMessage("正在登录。。")
                            .show();
                    //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    new LoginThread<LoginActivity>(LoginActivity.this, user,
                            ApiConstants.getIpDelimiterPort(), iphone, paw) {
                        @Override
                        protected void onPostExecute(String s, LoginActivity loginActivity) {
                            dialog.dismiss();
                            try {
                                JSONObject object = new JSONObject(s);
                                if (object.getString("RESULT").equals("S")) {
                                    Gson gson = new Gson();
                                    ApiConstants.setKey(object.getString("key"));
                                    if (user.equals("student")) {
                                        Student student = gson.fromJson(object.getString("student"), Student.class);
                                        AppUtils.setStudent(student);
                                        Intent intent=new Intent(LoginActivity.this, MyTeacherActivity.class);
                                        intent.putExtra("teachers",object.getString("teachers"));
                                        startActivity(intent);
                                    } else if (user.equals("teacher")){
                                        Teacher teacher = gson.fromJson(object.getString("teacher"), Teacher.class);
                                        AppUtils.setTeacher(teacher);
                                        Intent intent=new Intent(LoginActivity.this, MyCourseActivity.class);
                                        intent.putExtra("courses",object.getString("courses"));
                                        startActivity(intent);
                                    }else if (user.equals("admin")){
                                        Admin admin = gson.fromJson(object.getString("admin"), Admin.class);
                                        AppUtils.setAdmin(admin);
                                        Intent intent=new Intent(LoginActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                    }

                                } else {
                                    Toast.makeText(LoginActivity.this, object.getString("ERRMSG"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, e.getMessage() + "\n" + s, Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute();
                }
            }
        });
    }

    @Override
    public void onSettingIPInputComplete(String ip, String port) {
        tvIp.setText("当前ip："+ip+":"+port+" 触摸设置ip");
    }
}
