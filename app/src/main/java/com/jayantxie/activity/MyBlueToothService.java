package com.jayantxie.activity;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.jayantxie.R;
import com.jayantxie.pojo.DistractionTime;
import com.jayantxie.pojo.EvaluationData;
import com.jayantxie.pojo.InitialSettings;
import com.jayantxie.utils.DoubleDatesToMinutes;
import com.jayantxie.utils.JsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 天亮就出发 on 2017/4/21.
 */

public class MyBlueToothService extends Service {

    private InitialSettings mInitialSettings = new InitialSettings();

    private static EvaluationData mEvaluationData = new EvaluationData();

    private static List<DistractionTime> distractionTimes = new ArrayList<>();

    private MyHandler mMyHandler = new MyHandler();

    private Intent mIntent = new Intent("MyFinishReceiver");

    public class MyBinder extends Binder {
        InputStream inputStream;
        OutputStream outputStream;

        public void setInOutputStream(InputStream in, OutputStream out){
            inputStream = in;
            outputStream = out;
        }

        public void setInitialSettings(InitialSettings initial){
            mInitialSettings = initial;
            //mInitialSettings转mEvaluationData
            mEvaluationData.setBeginTime(mInitialSettings.getBeginTime());
            mEvaluationData.setEndTime(mInitialSettings.getEndTime());
            mEvaluationData.setRelaxMinutes(mInitialSettings.getRelaxTime());
            mEvaluationData.setDistractionTimes(0);
            mEvaluationData.setRelaxTimes(mInitialSettings.getRelaxTimes());
        }

        public void beginListen(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] buff = new byte[1];
                    while(true){
                        try{
                            inputStream.read(buff);
                            Character toSend = (char)buff[0];
                            Log.d("receive",toSend+"");
                            Message msg = mMyHandler.obtainMessage();
                            msg.obj = toSend;
                            mMyHandler.sendMessage(msg);
                        }catch (IOException e){
                            break;
                        }
                    }
                }
            }).start();
        }
        //设置方法用于启动内部MyTimerTask类的timerTask方法

        MyTimerTask mMyTimerTask = new MyTimerTask();
        public void beginTimerTask(){
            mMyTimerTask.timerTask();
        }

        public void stopTimerTask(){ mMyTimerTask.timer.cancel();}

        public void beginEndTimerTask(){
            new EndTimerTask().setEndTimer();
        }

        public class MyTimerTask{
            long timeWork;
            long timeRelax;
            //boolean为true时，标记为当前计时为time1，即工作时间的一次计时,
            //time2为休息时间的一次计时,记为false
            boolean flag = true;
            Timer timer = new Timer();
            public class TheTimerTask extends TimerTask {
                @Override
                public void run() {
                    if(flag){  //工作线程
                        flag = false;
                        handler.sendEmptyMessage(1);
                    }else{ //休息线程
                        flag = true;
                        handler.sendEmptyMessage(0);
                    }
                }
            }
            public void timerTask(){
                int minutes = (int)DoubleDatesToMinutes.translate(mInitialSettings.getBeginTime(),
                        mInitialSettings.getEndTime());
                //单段工作时间
                Log.d("minutes",minutes+"");
                Log.d("relaxTime",mInitialSettings.getRelaxTime()+"");
                Log.d("relaxTimes",mInitialSettings.getRelaxTimes()+"");
                timeWork = (long)(((float)(minutes-mInitialSettings.getRelaxTime())/
                        (mInitialSettings.getRelaxTimes()+1))*60000);
                Log.d("timeWork",timeWork+"");
                //单段休息时间
                timeRelax = (long)(((float)mInitialSettings.getRelaxTime()/
                        mInitialSettings.getRelaxTimes())*60000);
                Log.d("timeRelax",timeRelax+"");
                //开启一个线程用于第一次工作时间的定时
                timer.schedule(new TheTimerTask(),timeWork);
            }
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case 1:
                            //收到1代表工作定时到点，此时发送‘h'使设备暂停工作
                            write('h');
                            //重新设置timer，用于休息时间的定时
                            timer.schedule(new TheTimerTask(),timeRelax);
                            break;
                        case 0:
                            //收到0代表工作定时到点，此时发送‘d'使设备重新工作
                            write('d');
                            //重新设置timer，用于工作时间的定时
                            timer.schedule(new TheTimerTask(),timeWork);
                            break;
                        default:
                            break;
                    }
                    super.handleMessage(msg);
                }
            };
        }

        public void write(char a){
            try{
                outputStream.write(a);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public class EndTimerTask{
            private Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 1:
                            //这里执行结束工作任务
                            write('i');
                            Log.d("mEvaluationData",mEvaluationData.getEndTime().toString()
                                    +mEvaluationData.getDistractionTimes().toString());
                            mIntent.putExtra("mEvaluationData",JsonUtil.objectToString(mEvaluationData));
                            if(distractionTimes.size() == 0)
                                mIntent.putExtra("distractionTimes", "");
                            else
                                mIntent.putExtra("distractionTimes", JsonUtil.objectToString(distractionTimes));
                            sendBroadcast(mIntent);
                            stopTimerTask();
                            break;
                        default:
                            break;
                    }
                }
            };
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
            Timer timer = new Timer(true);
            public void setEndTimer(){
                timer.schedule(task,mInitialSettings.getEndTime());
            }
        }
    }

    public static class MyHandler extends Handler {
        DistractionTime distractionTime = new DistractionTime();

        @Override
        public void handleMessage(Message msg){
            char a = (Character) msg.obj;
            switch (a){
                case 'e':
                    //接收到e
                    distractionTime = new DistractionTime();
                    distractionTime.setsTime(new Date());
                    break;
                case 'f':
                    //接收到f
                    distractionTime.seteTime(new Date());
                    distractionTimes.add(distractionTime);
                    mEvaluationData.setDistractionTimes(
                            mEvaluationData.getDistractionTimes()+1);
                    break;
                default:
                    break;
            }
        }
    }

    private MyBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder =
                new Notification.Builder(MyBlueToothService.this);
        builder.setContentText("智能学习监控中...");
        builder.setContentTitle("学习管家");
        builder.setSmallIcon(R.drawable.icon);
        builder.setTicker("智能学习监控系统启动！");
        builder.setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
