package net.lzzy.practiceapi.Thread;

import android.os.AsyncTask;

import net.lzzy.practiceapi.network.ApiService;
import net.lzzy.practiceapi.utils.StudentKeyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public abstract class RequestThread<T> extends AsyncTask<Integer,Integer, String> {
    private final WeakReference<T> context;
    private String url="http://127.0.0.1:8080/";
    private String content="{}";
    protected RequestThread(T context,String url,String content) {
        this.context = new WeakReference<>(context);
        this.url=url;
        this.content=content;
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
            return ApiService.okRequest(url, StudentKeyUtils.encryptionRequest(content));
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

