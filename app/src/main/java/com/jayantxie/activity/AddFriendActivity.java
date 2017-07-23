package com.jayantxie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.pojo.Friend;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.Http;
import com.jayantxie.utils.JsonUtil;

public class AddFriendActivity extends AppCompatActivity {
    private Button back;
    private EditText inputName;
    private TextView progressHint;
    private ProgressBar searchProgress;
    private User searchingUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        back = (Button) findViewById(R.id.add_friend_back);
        progressHint = (TextView) findViewById(R.id.progress_hint);
        searchProgress = (ProgressBar) findViewById(R.id.search_progress);
        inputName = (EditText) findViewById(R.id.search_friend_text);
        inputName.requestFocus();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inputName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchProgress.setVisibility(View.VISIBLE);
                    progressHint.setVisibility(View.VISIBLE);
                    Http.getUserInfo(inputName.getText().toString(),mFriendInfoHandler,1);
                    return true;
                }
                return false;
            }
        });
    }

    public FriendInfoHandler mFriendInfoHandler = new FriendInfoHandler();
    public class FriendInfoHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1) {
                Toast.makeText(AddFriendActivity.this, "未找到用户！", Toast.LENGTH_SHORT).show();
                searchProgress.setVisibility(View.GONE);
                progressHint.setVisibility(View.GONE);
            }
            else {
                //这里的searchingUser就是查找的好友的用户名
                searchingUser = (User) msg.obj;
                if(searchingUser.getName().equals(getSharedPreferences("user",
                        Context.MODE_PRIVATE).getString("name",""))) {
                    Toast.makeText(AddFriendActivity.this, "不能添加自己哟！", Toast.LENGTH_SHORT).show();
                    searchProgress.setVisibility(View.GONE);
                    progressHint.setVisibility(View.GONE);
                }
                else{
                    Friend friend = new Friend();
                    friend.setUserName(getSharedPreferences("user",
                            Context.MODE_PRIVATE).getString("name",""));
                    friend.setFriendName(searchingUser.getName());
                    Http.areFriend(friend,mAreFriendHandler);
                }
            }
        }
    }

    public AreFriendHandler mAreFriendHandler = new AreFriendHandler();
    public class AreFriendHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            searchProgress.setVisibility(View.GONE);
            progressHint.setVisibility(View.GONE);
            Intent intent = new Intent(AddFriendActivity.this,NewFriendActivity.class);
            intent.putExtra("user", JsonUtil.objectToString(searchingUser));
            intent.putExtra("state", msg.what);
            startActivity(intent);
        }
    }
}
