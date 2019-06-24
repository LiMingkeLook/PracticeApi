package net.lzzy.practiceapi.Thread;

import android.os.AsyncTask;

import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.AppUtils;
import net.lzzy.practiceapi.utils.KeyUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public abstract class PracticeResultThread<T> extends AsyncTask<Void,Void, String> {
    private final WeakReference<T> context;
    private String ip="39.108.251.68:8080";
    private String json="";
    protected PracticeResultThread(T context, String ip, String json) {
        this.context = new WeakReference<>(context);
        this.ip=ip;
        this.json=json;
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
                return ApiService.okRequest("http://"+ip+"/Practice/api/post_PracticeResult",
                        KeyUtils.encryptionJson(json));
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
