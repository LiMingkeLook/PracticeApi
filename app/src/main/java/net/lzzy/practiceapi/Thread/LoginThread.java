package net.lzzy.practiceapi.Thread;

import android.os.AsyncTask;

import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.KeyUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public abstract class LoginThread<T> extends AsyncTask<Void,Void, String> {
    private final WeakReference<T> context;
    private String ip="39.108.251.68:8080";
    private String iphone="";
    private String paw="";
    private String role="student";
    protected LoginThread(T context, String role, String ip, String iphone, String paw) {
        this.context = new WeakReference<>(context);
        this.ip=ip;
        this.iphone=iphone;
        this.paw=paw;
        this.role=role;
    }

    /**
     * 执行线程前
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("iphone",iphone);
                jsonObject.put("password",paw);
                jsonObject.put("user", role);
                //jsonObject.put("loginTime", System.currentTimeMillis());
                return ApiService.okRequest("http://"+ip+"/Practice/api/login"
                        , StudentKeyUtils.encryptionRequest(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
                return "错误："+e.getMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "错误："+e.getMessage();
        }
    }

    /**
     * 获取到数据后
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        T t = context.get();
            /*onPostExecute(StudentKeyUtils.decodeResponse(s).first, t);*/
        onPostExecute(s, t);
    }

    protected abstract void onPostExecute(String s, T t);
}
