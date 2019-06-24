package net.lzzy.practiceapi.Thread;

import android.os.AsyncTask;

import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;

import java.lang.ref.WeakReference;

public abstract class AddCouseFragmentThread<T> extends AsyncTask<Integer,Integer, String> {
    private final WeakReference<T> context;
    private String courseJsonArray="";
    private String ip="127.0.0.1:8080";
    protected AddCouseFragmentThread(T context, String ip, String courseJsonArray) {
        this.context = new WeakReference<>(context);
        this.courseJsonArray=courseJsonArray;
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
            return ApiService.okRequest("http://"+ip+"/Practice/api/post_add_course"
                    ,KeyUtils.encryptionJson(courseJsonArray));
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
