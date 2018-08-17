package com.example.arex.mytvtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashManager implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infos;
    private MyApplication application;

    public CrashManager(MyApplication application) {
        //获取系统默认的UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
        StringBuffer sb = new StringBuffer();
        sb.append("------------------crash----------------------");
        sb.append("\r\n");
        String sdcardPath = Environment.getExternalStorageDirectory().getPath();
//            String filePath = sdcardPath + "//Urmytch/crash/";
        String filePath = sdcardPath + "/allsenseLog";
        writeLog(sb.toString(), filePath);
        Log.d("TvTest", "test");
    }

    private boolean handleException(final Throwable exc) {
        if (exc == null) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Log.i("Urmytch", "崩溃正在写入日志");
                flushBufferedUrlsAndReturn();
                //处理崩溃
                collectDeviceAndUserInfo(application);
                writeCrash(exc);
                Looper.loop();
            }
        }).start();
        return true;
    }

    /**
     * 把未存盘的url和返回数据写入日志文件
     */
    private void flushBufferedUrlsAndReturn() {
        //TODO 可以在请求网络时把url和返回xml或json数据缓存在队列中，崩溃时先写入以便查明原因
    }

    /**
     * 采集设备和用户信息
     *
     * @param context 上下文
     */
    private void collectDeviceAndUserInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        infos = new HashMap<String, String>();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("crashTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Urmytch", e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }
        } catch (IllegalAccessException e) {
            Log.e("Urmytch", e.getMessage());
        }
    }

    /**
     * 采集崩溃原因
     *
     * @param exc 异常
     */

    private void writeCrash(Throwable exc) {
        StringBuffer sb = new StringBuffer();
        sb.append("------------------crash----------------------");
        sb.append("\r\n");
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        exc.printStackTrace(pw);
        Throwable excCause = exc.getCause();
        while (excCause != null) {
            excCause.printStackTrace(pw);
            excCause = excCause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        sb.append("\r\n");
        sb.append("-------------------end-----------------------");
        sb.append("\r\n");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcardPath = Environment.getExternalStorageDirectory().getPath();
//            String filePath = sdcardPath + "//Urmytch/crash/";
            String filePath = sdcardPath + "/allsenseLog";
            writeLog(sb.toString(), filePath);
        }
    }

    /**
     * @param log  文件内容
     * @param name 文件路径
     * @return 返回写入的文件路径
     * 写入Log信息的方法，写入到SD卡里面
     */
    private String writeLog(String log, String name) {
        String filename = name + "mycrash" + ".log";
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            Log.i("Urmytch", "新建文件");
            file.getParentFile().mkdirs();
        }
        if (file != null && file.exists() && file.length() + log.length() >= 64 * 1024) {
            //控制日志文件大小
            file.delete();
        }
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            //写入相关Log到文件
            bw.write(log);
            bw.newLine();
            bw.close();
            fw.close();
            return filename;
        } catch (IOException e) {
            Log.w("Urmytch", e.getMessage());
            return null;
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exc) {
        if (!handleException(exc) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, exc);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.w("Urmytch", e.getMessage());
            }
            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    application.getApplicationContext(), 0, intent,
                    0);
            //退出程序
            AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
            //1秒后重启应用
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
