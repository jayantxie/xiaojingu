package com.jayantxie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.pojo.Friend;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.Http;
import com.jayantxie.utils.JsonUtil;

public class NewFriendActivity extends AppCompatActivity {
    private TextView friendName;
    private TextView friendNickname;
    private Button back;
    private Button addFriend;
    private Button seeInfo;
    private Button waitConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        friendName = (TextView) findViewById(R.id.f_name);
        friendNickname = (TextView) findViewById(R.id.f_nickname);
        back = (Button) findViewById(R.id.new_friend_back);
        addFriend = (Button) findViewById(R.id.add_this_friend);
        seeInfo = (Button) findViewById(R.id.see_friend_info);
        waitConfirm = (Button) findViewById(R.id.wait_for_confirm);

        User user = (User) JsonUtil.stringToObject(getIntent().getStringExtra("user"),User.class);
        friendName.setText(user.getName());
        friendNickname.setText(user.getNickname());
        int flag = getIntent().getIntExtra("state",-1);
        if(flag == 0)
            addFriend.setVisibility(View.VISIBLE);
        else if(flag == 1)
            waitConfirm.setVisibility(View.VISIBLE);
        else if(flag == 2)
            seeInfo.setVisibility(View.VISIBLE);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Friend friend = new Friend();
                String name = getSharedPreferences("user", Context.MODE_PRIVATE).getString("name","");
                friend.setUserName(name);
                friend.setFriendName(friendName.getText().toString());
                Http.addFriend(friend,mAddFriendHandler);
            }
        });
        seeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(NewFriendActivity.this,EvaluationListActivity.class);
                startActivity(intent);
                */
                //查找十条评价数据，类朋友圈布局
            }
        });
        waitConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewFriendActivity.this,"等待确认好友请求！",Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public AddFriendHandler mAddFriendHandler = new AddFriendHandler();
    public class AddFriendHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                Toast.makeText(NewFriendActivity.this, "好友请求发送成功！", Toast.LENGTH_SHORT).show();
                addFriend.setVisibility(View.INVISIBLE);
                waitConfirm.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(NewFriendActivity.this,"请求发送失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
