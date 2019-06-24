package net.lzzy.practiceapi.Thread;

import android.os.AsyncTask;

import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public abstract class EnrolledStudentThread<T> extends AsyncTask<Integer,Integer, String> {
    private final WeakReference<T> context;
    private String studentjson="";
    private String ip="127.0.0.1:8080";
    protected EnrolledStudentThread(T context,String ip, String studentjson) {
        this.context = new WeakReference<>(context);
        this.studentjson=studentjson;
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
        try {
            return ApiService.okRequest("http://"+ip+"/Practice/api/registerOfStudent"
                    ,KeyUtils.encryptionJson(studentjson));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
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
