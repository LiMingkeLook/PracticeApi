package net.lzzy.practiceapi.Thread;

import android.os.AsyncTask;

import net.lzzy.practiceapi.connstants.ApiConstants;
import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public abstract class GetQuestionThread<T> extends AsyncTask<Void,Void, String> {
    private final WeakReference<T> context;
    private String ip="127.0.0.1:8080";
    private int pid=2;
    protected GetQuestionThread(T context,int pid, String ip) {
        this.context = new WeakReference<>(context);
        this.ip=ip;
        this.pid=pid;
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
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("practiceId",pid);
                jsonObject.put("key", ApiConstants.getKey());
                jsonObject.put("studentId","111");
                jsonObject.put("courseId","111");
                return ApiService.okRequest("http://"+ip+"/Practice/api/get_all_question",
                        KeyUtils.encryptionJson(jsonObject.toString()));
            } catch (Exception e) {
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
        onPostExecute(s, t);
    }

    protected abstract void onPostExecute(String s, T t);
}
