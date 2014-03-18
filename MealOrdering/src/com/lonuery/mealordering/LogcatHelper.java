package com.lonuery.mealordering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 将日志保存在文件中的帮助类
 * */
public class LogcatHelper {
	public static boolean startedFlag = false;
	private static LogcatHelper INSTANCE = null;  
    private static String PATH_LOGCAT;  
    private LogDumper mLogDumper = null;  
    private int mPId;  	   
   
    /** 
     * 初始化目录 
     * */  
    public void init(Context context) {  
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中   
            PATH_LOGCAT = Environment.getExternalStorageDirectory()  
                    .getAbsolutePath() + File.separator + "MealOrdering";  
        } else {// 如果SD卡不存在，就保存到本应用的目录下   
            PATH_LOGCAT = context.getFilesDir().getAbsolutePath()  
                    + File.separator + "MealOrdering";  
        }  
        File file = new File(PATH_LOGCAT);  
        if (!file.exists()){  
            file.mkdirs();  
        }
        Log.i("LogcatHelper", "创建logsPICC文件夹");
    }  
   
    public static LogcatHelper getInstance(Context context) {
        if (INSTANCE == null){  
            INSTANCE = new LogcatHelper(context); 
            Log.i("LogcatHelper", "启动日志记录");
        }  
        return INSTANCE;  
    }  
   
    private LogcatHelper(Context context) {  
        init(context);  
        mPId = android.os.Process.myPid();  
    }  
   
    public void start() {  
        if (mLogDumper == null)  
            mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);  
        mLogDumper.start();
        LogcatHelper.startedFlag = true;
    }  
   
    public void stop() {  
        if (mLogDumper != null) {  
            mLogDumper.stopLogs();  
            mLogDumper = null;  
        }
        LogcatHelper.startedFlag = false;
    }  
   
    private class LogDumper extends Thread {  
   
        private Process logcatProc;  
        private BufferedReader mReader = null;  
        private boolean mRunning = true;  
        String cmds = null;  
        private String mPID;  
        private FileOutputStream out = null;  
   
        public LogDumper(String pid, String dir) {  
            mPID = pid;
            //deleteFile(dir);
            try{  
            	File file = new File(dir,MyDate.getDateName()+".log");
                out = new FileOutputStream(file);
                Log.i("LogcatHelper", "创建日志记录文件");
            }catch (FileNotFoundException e){   
                e.printStackTrace();  
            }   
            /** 
             *  
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s 
             *  
             * 显示当前mPID程序的 E和W等级的日志. 
             *  
             * */     
            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";   
            // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息   
            // cmds = "logcat -s way";//打印标签过滤信息  
            cmds = "logcat *:e *:i -v time | grep \"(" + mPID + ")\""; 
            //cmds = "logcat *:e -v time | grep \"(" + mPID + ")\"";
           // cmds = "logcat *:i | grep \"(" + mPID + ")\"";  
        }  
   
        public void stopLogs(){  
            mRunning = false;  
        }  
   
        @Override  
        public void run(){  
            try{
                logcatProc = Runtime.getRuntime().exec(cmds);  
                mReader = new BufferedReader(new InputStreamReader(
                        logcatProc.getInputStream()), 1024);  
                String line = null;  
                while (mRunning && (line = mReader.readLine()) != null){
                    if (line.length() == 0){  
                        continue;  
                    }
                    String content =  MyDate.getDateEN()+line + "\n";
                    if (out != null && line.contains(mPID)) {  
                        out.write(content.getBytes());  
                    }  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally {  
                if (logcatProc != null) {  
                    logcatProc.destroy();  
                    logcatProc = null;  
                }  
                if (mReader != null){  
                    try {  
                        mReader.close();  
                        mReader = null;  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
                if (out != null) {  
                    try {  
                        out.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                    out = null;  
                }    
            }    
        }     
    }
    
    public void deleteFile(String path){
    	File file = new File(path);
    	
    	File fileList[] = file.listFiles();
    	for (File file2 : fileList){
			String name = file2.getName();
			if(name.length()!=18||!name.endsWith("log")){
				file2.delete();
			}else{
				String date = name.substring(0, 8);
				SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
				try{
					Date startDate = sim.parse(date);
					Date endDate = sim.parse(sim.format(new Date(System.currentTimeMillis())));
					if(getDaysBetween(startDate, endDate)>3||getDaysBetween(startDate, endDate)<0){
						file2.delete();
					}
				}catch (ParseException e){
					e.printStackTrace();
				}
			}
		}
    }
    
    public Long getDaysBetween(Date startDate, Date endDate) {  
        Calendar fromCalendar = Calendar.getInstance();  
        fromCalendar.setTime(startDate);  
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        fromCalendar.set(Calendar.MINUTE, 0);  
        fromCalendar.set(Calendar.SECOND, 0);  
        fromCalendar.set(Calendar.MILLISECOND, 0);  
  
        Calendar toCalendar = Calendar.getInstance();  
        toCalendar.setTime(endDate);  
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        toCalendar.set(Calendar.MINUTE, 0);  
        toCalendar.set(Calendar.SECOND, 0);  
        toCalendar.set(Calendar.MILLISECOND, 0);  
  
        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);  
    }
}
