package com.jayantxie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jayantxie.R;

public class AddOptionsActivity extends Activity
        implements android.view.View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_options);
        TextView addFriend = (TextView) findViewById(R.id.add_friend);
        TextView releaseLearningLog = (TextView) findViewById(R.id.release_learning_log);

        addFriend.setOnClickListener(this);
        releaseLearningLog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_friend:
                Intent intent = new Intent();
                intent.setClass(this,AddFriendActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.release_learning_log:
                Toast.makeText(this,"暂未开启此功能ರ_ರ...",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
