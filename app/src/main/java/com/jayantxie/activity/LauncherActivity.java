package com.jayantxie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jayantxie.R;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.Http;

public class LauncherActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        /*
        imageView = (ImageView) findViewById(R.id.launcher_image);
        int screenWidth = getScreenWidth(this);
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imageView.setLayoutParams(lp);
        imageView.setMaxWidth(screenWidth);
        imageView.setMaxHeight(screenWidth * 5);
        */
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if(preferences.getString("isLogined","false").equals("true")){ //已登录
            new Handler().postDelayed(stored,500);
        }else{
            new Handler().postDelayed(unStored, 500);// 0.5秒后关闭，并跳转到主页面
        }
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    Runnable stored = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.setClass(LauncherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    Runnable unStored = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.setClass(LauncherActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
