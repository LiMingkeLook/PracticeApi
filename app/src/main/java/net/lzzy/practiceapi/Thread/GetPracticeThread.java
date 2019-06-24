package net.lzzy.practiceapi.Thread;

import android.os.AsyncTask;


import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.KeyUtils;
import net.lzzy.practiceapi.utils.StudentKeyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public abstract class GetPracticeThread<T> extends AsyncTask<Integer,Integer, String> {
    private final WeakReference<T> context;
    private String ip="127.0.0.1:8080";
    private String content="{}";
    protected GetPracticeThread(T context,String ip,String content) {
        this.context = new WeakReference<>(context);
        this.ip=ip;
    }

    /**
     * 执行线程前
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Integer... integers) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("courseId",integers[0]);
                jsonObject.put("studentId",integers[1]);
                jsonObject.put("key",AppUtils.getKey());
                return ApiService.okRequest("http://"+ip+"/Practice/api/get_practice"
                        , StudentKeyUtils.encryptionRequest(jsonObject.toString()));
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
