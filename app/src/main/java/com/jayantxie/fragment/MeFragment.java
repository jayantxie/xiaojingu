package com.jayantxie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.activity.EvaluationListActivity;
import com.jayantxie.activity.LoginActivity;
import com.jayantxie.activity.MainActivity;
import com.jayantxie.activity.NewFriendListActivity;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.Http;
import com.jayantxie.utils.JsonUtil;

import java.util.List;


public class MeFragment extends Fragment
        implements android.view.View.OnClickListener{
    private MainActivity mMainActivity;
    private ImageView headPic;
    private TextView mNickname;
    private TextView mName;
    private TextView requestFriendNumber;
    private RelativeLayout showEvaDataList;
    private FrameLayout newFriendRequest;
    private Button quitLogin;
    private SharedPreferences preferences;
    private List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);
        mMainActivity = (MainActivity)getActivity();
        headPic = (ImageView) rootView.findViewById(R.id.head_pic);
        mNickname = (TextView) rootView.findViewById(R.id.nickname);
        mName = (TextView) rootView.findViewById(R.id.name);
        requestFriendNumber = (TextView) rootView.findViewById(R.id.request_friend_num);
        showEvaDataList = (RelativeLayout) rootView.findViewById(R.id.show_evadata_list);
        newFriendRequest = (FrameLayout) rootView.findViewById(R.id.new_friend_request);
        quitLogin = (Button) rootView.findViewById(R.id.quit_login);

        preferences = mMainActivity.getSharedPreferences("user", Context.MODE_PRIVATE);

        showEvaDataList.setOnClickListener(this);
        quitLogin.setOnClickListener(this);
        newFriendRequest.setOnClickListener(this);

        //加载信息
        initInfo();

        return rootView;
    }

    public void initInfo(){
        String name = preferences.getString("name","");
        Http.getUserInfo(name,mSaveInfoHandler,0);
        //确认服务器是否包含未确认的好友请求
        //1代表目前出于待确认添加好友状态
        Http.queryFriendList(name,1,mRequestFriendListHandler);
    }

    public SaveInfoHandler mSaveInfoHandler = new SaveInfoHandler();
    public class SaveInfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            User user = (User) msg.obj;
            mName.setText(user.getName());
            mNickname.setText(user.getNickname());
        }
    }

    public RequestFriendListHandler mRequestFriendListHandler = new RequestFriendListHandler();
    public class RequestFriendListHandler extends Handler{
        //去除警告
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                //产生警告
                userList = (List<User>) msg.obj;
                SharedPreferences.Editor editor = preferences.edit();
                //请求的新好友列表
                editor.putString("userList",JsonUtil.objectToString(userList));
                editor.apply();
                requestFriendNumber.setText(userList.size()+"");
                requestFriendNumber.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_evadata_list:
                Intent intent2 = new Intent(mMainActivity, EvaluationListActivity.class);
                intent2.putExtra("name","");
                startActivity(intent2);
                break;
            case R.id.quit_login:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("isLogined","false");
                editor.putString("name","");
                editor.putString("password","");
                editor.apply();
                Intent intent1 = new Intent();
                intent1.setClass(mMainActivity, LoginActivity.class);
                startActivity(intent1);
                mMainActivity.finish();
                break;
            case R.id.new_friend_request:
                requestFriendNumber.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(mMainActivity, NewFriendListActivity.class);
                String userListString = preferences.getString("userList","");
                if(!userListString.equals(""))
                    intent.putExtra("userList", userListString);
                else
                    intent.putExtra("userList", "");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
