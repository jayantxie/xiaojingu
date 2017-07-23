package com.jayantxie.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jayantxie.R;
import com.jayantxie.pojo.DistractionTime;
import com.jayantxie.pojo.EvaluationData;
import com.jayantxie.utils.Http;
import com.jayantxie.utils.JsonUtil;

public class UploadActivity extends Activity {

    private Button finishButton;
    private TextView uploadState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        EvaluationData mEvaluationData = (EvaluationData)JsonUtil.stringToObject(
                getIntent().getStringExtra("mEvaluationData"),EvaluationData.class);
        String distractionTimes = getIntent().getStringExtra("distractionTimes");
        finishButton = (Button) findViewById(R.id.finish_button);
        uploadState = (TextView) findViewById(R.id.upload_state);
        //添加用户名、分数信息
        int scores = 100-mEvaluationData.getDistractionTimes()*5;
        if(scores<0)
            scores = 0;
        Log.d("evadata",mEvaluationData.getEndTime().toString());
        mEvaluationData.setScores(scores);
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = preferences.getString("name","");
        mEvaluationData.setUserName(name);
        //发送Http请求，上传数据
        Http.uploadData(mEvaluationData,distractionTimes,mDoneUploadHandler);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public DoneUploadHandler mDoneUploadHandler = new DoneUploadHandler();
    public class DoneUploadHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){ //发布成功！
            }else{   //发布失败！
                uploadState.setText("已完成本次学习任务，但是...数据上传失败惹...");
            }
        }
    }
}
