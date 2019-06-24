package net.lzzy.practiceapi.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import net.lzzy.practiceapi.models.Student;
import net.lzzy.practiceapi.models.Teacher;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lzzy_gxy on 2019/3/11.
 * Description:
 */
public class AppUtils extends Application {
    private static WeakReference<Context> wContext;
    private static List<AppCompatActivity> activities = new LinkedList<>();
    private static String runningActivity;

    private static String ip="";
    private static String key="";
    public static final String NONE_IP="192.168.1.101:8080";

    public static String getKey() {
            return key;
    }

    public static void setKey(String key) {
        AppUtils.key = key;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        AppUtils.ip = ip;
    }
    private static Teacher teacher;

    public static Teacher getTeacher() {
        return teacher;
    }

    public static void setTeacher(Teacher teacher) {
        AppUtils.teacher = teacher;
    }

    private static Student student;
    public static Student getStudent() {
        return student;
    }

    public static void setStudent(Student student) {
        student.setEmail(student.getEmail());
        student.setGender(student.getGender().equals("1")?"男":"女");
        student.setIphone(student.getIphone());
        student.setPassword(student.getPassword());
        student.setName(student.getName());
        student.setStudentId(student.getStudentId());
        AppUtils.student = student;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        wContext = new WeakReference<Context>(this);
    }


    //region 1、Activity相关
    public static Context getContext() {
        return wContext.get();
    }
    public static void remoreActivity(AppCompatActivity activity) {
        activities.remove(activity);

    }

    public static void addActivity(AppCompatActivity activity) {
        activities.add(activity);
    }

    public static void setRunningActivity(String runingActivity) {
        AppUtils.runningActivity = runingActivity;
    }

    public static void setStopped(String stoppedActivity) {
        if (stoppedActivity.equals(AppUtils.runningActivity)) {
            AppUtils.runningActivity = "";
        }
    }



    public static AppCompatActivity getRunningActivity() {
        for (AppCompatActivity activity : activities) {
            String name = activity.getLocalClassName();
            if (AppUtils.runningActivity.equals(name)) {
                return activity;
            }
        }
        return null;
    }
    //endregion

    //region 2、Network网络相关
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }
    /** 检查网络是否可用 **/
    public static boolean isNetworkAvailable(){
        ConnectivityManager manager= (ConnectivityManager) getContext()
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo():null;
        return info != null && info.isConnected();
    }
    //endregion

    //region 3、server服务相关
    /**
     * 判断某服务能否连通
     *
     * @param host host
     * @param port port
     * @return boolean
     */
    public static boolean isRunning(String host, int port) {
        Socket sClient = null;
        try {
            SocketAddress saAdd = new InetSocketAddress(host.trim(), port);
            sClient = new Socket();
            sClient.connect(saAdd, 1000);
        } catch (UnknownHostException e) {
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            System.out.println("错误："+e.toString());
            Toast.makeText(getRunningActivity(),e.toString(),Toast.LENGTH_LONG).show();
            return false;
        } finally {
            try {
                if (sClient != null) {
                    sClient.close();
                }
            } catch (Exception e) {
                System.out.println("错误："+e.toString());
            }
        }
        return true;
    }
    /**
     * 判断某服务能否连通
     *
     * @param address host  port
     */
    public static void tryConnectServer(String address) throws Exception{
        URL url=new URL(address);
       HttpURLConnection connection= (HttpURLConnection) url.openConnection();
       connection.setConnectTimeout(5000);
       connection.getContent();
    }

    //endregion

    //region 4、线程池的创建
    //cpu数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //调用cpu数量的最小值、最大值
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    //最大线程
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    //保持运行的时间
    private static final int KEEP_ALIVE_SECONDS = 30;
    //自定义线程创建
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "thread #" + count.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> POOL_QUEUE = new LinkedBlockingQueue<>(128);

    public static ThreadPoolExecutor getExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, POOL_QUEUE, THREAD_FACTORY);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
    //endregion

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     *@param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    public Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取各类网络的mac地址
     *
     * @return 包括wifi及移动数据的mac地址
     */
    public static List<String> getMacAddress(){
        try {
            Enumeration<NetworkInterface> interfaces=NetworkInterface.getNetworkInterfaces();
            List<String> items=new ArrayList<>();
            while (interfaces.hasMoreElements()){
                NetworkInterface ni=interfaces.nextElement();
                byte[] address=ni.getHardwareAddress();
                if (address==null||address.length==0){
                    continue;
                }
                StringBuilder builder=new StringBuilder();
                for (byte a:address){
                    builder.append(String.format("%02X:",a));
                }
                if (builder.length()>0){
                    builder.deleteCharAt(builder.length()-1);
                }
                if (ni.isUp()){
                    items.add(ni.getName()+":"+builder.toString());
                }
            }
            return items;
        } catch (SocketException e) {
            return new ArrayList<>();
        }
    }
    public static Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
    /** 退出方法**/
    public static void exit(){
        for (AppCompatActivity activity:activities){
            if (activity!=null){
                activity.finish();
            }
        }
        System.exit(0);
    }

}
