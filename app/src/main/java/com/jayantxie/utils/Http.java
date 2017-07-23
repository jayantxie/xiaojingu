package com.jayantxie.utils;


import com.jayantxie.activity.AddFriendActivity.FriendInfoHandler;
import com.jayantxie.activity.NewFriendListActivity.DoneAddFriendHandler;
import com.jayantxie.activity.NewFriendListActivity.RefuseHandler;
import com.jayantxie.activity.RegisterActivity.RegisterHandler;
import com.jayantxie.activity.LoginActivity.LoginHandler;
import com.jayantxie.activity.UploadActivity.DoneUploadHandler;
import com.jayantxie.fragment.MeFragment.RequestFriendListHandler;
import com.jayantxie.fragment.MeFragment.SaveInfoHandler;
import com.jayantxie.activity.NewFriendActivity.AddFriendHandler;
import com.jayantxie.activity.AddFriendActivity.AreFriendHandler;
import com.jayantxie.fragment.FindFragment.FriendListHandler;
import com.jayantxie.activity.EvaluationListActivity.QueryTenTitleHandler;

import com.jayantxie.pojo.EvaluationData;
import com.jayantxie.pojo.Friend;
import com.jayantxie.pojo.User;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 天亮就出发 on 2017/5/1.
 */

public class Http {
    public static String baseUrl = "http://123.206.218.41:8080/web-ssm/";



    //注册逻辑
    private static RegisterHandler mRegisterHandler;
    private static User registerUser;
    public static void register(User user, RegisterHandler registerHandler){
        mRegisterHandler = registerHandler;
        registerUser = user;

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl+"user/register";
                String user = JsonUtil.objectToString(registerUser);
                Map<String,String> params = new HashMap<>();
                params.put("user",user);
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendPost(url,str);
                String mesg = "";
                try {
                    JSONObject result = new JSONObject(response);
                    mesg = result.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if(mesg.equals(""))
                        mesg = "注册失败！";
                    Message msg = new Message();
                    msg.obj = mesg;
                    mRegisterHandler.sendMessage(msg);
                    mRegisterHandler = null;
                    registerUser = null;
                }
            }
        }).start();
    }


    //登录逻辑
    private static LoginHandler mLoginHandler;
    private static User loginUser;
    public static void login(User user, LoginHandler loginHandler){
        mLoginHandler = loginHandler;
        loginUser = user;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "user/login";
                String user = JsonUtil.objectToString(loginUser);
                Map<String,String> params = new HashMap<>();
                params.put("user",user);
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendPost(url,str);
                Message mesg = new Message();
                try {
                    JSONObject result = new JSONObject(response);
                    mesg.what = Integer.parseInt(result.getString("status"));
                    mesg.obj = result.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mLoginHandler.sendMessage(mesg);
                    mLoginHandler = null;
                    loginUser = null;
                }
            }
        }).start();
    }


    //获取用户信息
    private static String myName;
    private static SaveInfoHandler mSaveInfoHandler;
    private static FriendInfoHandler mFriendInfoHandler;
    public static void getUserInfo(String name,Handler handler,int a){
        if(a == 0)
            mSaveInfoHandler = (SaveInfoHandler)handler;
        else
            mFriendInfoHandler = (FriendInfoHandler)handler;
        myName = name;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "user/showUser";
                Map<String,String> params = new HashMap<>();
                params.put("name",myName);
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendGet(url,str);
                Message mesg = new Message();
                mesg.what =1;
                try {
                    JSONObject result = new JSONObject(response);
                    Log.d("getUserInfo",result.getString("user"));
                    if(result.getString("status").equals("1"))
                        mesg.what = 1;
                    else{
                        mesg.what = 0;
                        mesg.obj = JsonUtil.stringToObject(result.getString("user"),User.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if(mFriendInfoHandler == null){
                        mSaveInfoHandler.sendMessage(mesg);
                        mSaveInfoHandler = null;
                    }else{
                        mFriendInfoHandler.sendMessage(mesg);
                        mFriendInfoHandler = null;
                    }
                    myName = null;
                }
            }
        }).start();
    }


    //上传评价表数据
    private static DoneUploadHandler mDoneUploadHandler;
    private static EvaluationData mEvaluationData;
    private static String mDistractionTimes;
    public static void uploadData(EvaluationData evaluationData, String distractionTimes, DoneUploadHandler doneUploadHandler){
        mDoneUploadHandler = doneUploadHandler;
        mEvaluationData = evaluationData;
        mDistractionTimes = distractionTimes;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl +"evaluationData/release";
                String data = JsonUtil.objectToString(mEvaluationData);
                Map<String,String> params = new HashMap<>();
                params.put("evaluationData",data);
                params.put("distractionTimeList",mDistractionTimes);
                String str = MapToUrl.getUrlParamsByMapDs(params);
                Log.d("uploadSTR",str);
                String response = HttpRequest.sendPost(url,str);
                Log.d("response",response);
                int mesg = 1;
                try {
                    JSONObject result = new JSONObject(response);
                    mesg = Integer.parseInt(result.getString("status"));
                    Log.d("uploadmsg",result.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mDoneUploadHandler.sendEmptyMessage(mesg);
                    mDoneUploadHandler = null;
                    mEvaluationData = null;
                    mDistractionTimes = null;
                }
            }
        }).start();
    }


    //请求添加好友
    private static Friend mFriend;
    private static AddFriendHandler mAddFriendHandler;
    public static void addFriend(Friend friend,AddFriendHandler addFriendHandler){
        mFriend = friend;
        mAddFriendHandler = addFriendHandler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "friend/requestAdd";
                String friendString = JsonUtil.objectToString(mFriend);
                Map<String,String> params = new HashMap<>();
                params.put("friend",friendString);
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendPost(url,str);
                int mesg = 1;
                try{
                    JSONObject result = new JSONObject(response);
                    mesg = Integer.parseInt(result.getString("status"));
                }catch (JSONException e){
                    e.printStackTrace();
                }finally {
                    mAddFriendHandler.sendEmptyMessage(mesg);
                    mAddFriendHandler = null;
                    mFriend = null;
                }
            }
        }).start();
    }


    //查看是否为好友 state
    private static Friend aFriend;
    private static AreFriendHandler mAreFriendHandler;
    public static void areFriend(Friend friend, AreFriendHandler areFriendHandler){
        aFriend = friend;
        mAreFriendHandler = areFriendHandler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "friend/friendState";
                String friendString = JsonUtil.objectToString(aFriend);
                Map<String,String> params = new HashMap<>();
                params.put("friend",friendString);
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendPost(url,str);
                Log.d("response",response);
                int state = 0;
                try {
                    JSONObject result = new JSONObject(response);
                    state = Integer.parseInt(result.getString("state"));
                    Log.d("state",state+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mAreFriendHandler.sendEmptyMessage(state);
                    aFriend = null;
                    mAreFriendHandler = null;
                }
            }
        }).start();
    }


    //查询对于state = 1关系的好友列表
    private static String mName;
    private static Integer mState = 1;
    private static RequestFriendListHandler mRequestFriendListHandler;
    public static void queryFriendList(String name, int state, RequestFriendListHandler requestFriendListHandler){
        mName = name;
        mState = state;
        mRequestFriendListHandler = requestFriendListHandler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "friend/queryAll";
                Map<String,String> params = new HashMap<>();
                params.put("name",mName);
                params.put("state",mState+"");
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendGet(url,str);
                Message mesg = new Message();
                try {
                    JSONObject result = new JSONObject(response);
                    if(Integer.parseInt(result.getString("status")) == 0){   //成功
                        String listString = result.getString("userList").substring(1);
                        //List<User> 对象
                        mesg.obj = JsonUtil.stringToList(listString,User.class);
                        mesg.what = 0;
                    }else {
                        Log.d("access","this is" + mState);
                        mesg.what = 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mRequestFriendListHandler.sendMessage(mesg);
                    mRequestFriendListHandler = null;
                    mState = 1;
                    mName = null;
                }
            }
        }).start();
    }



    //查询对于state = 2关系的好友列表
    private static String theName;
    private static Integer theState = 2;
    private static FriendListHandler mFriendListHandler;
    public static void queryMyFriendList(String name,int state,FriendListHandler friendListHandler){
        mFriendListHandler = friendListHandler;
        theName = name;
        theState = state;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "friend/queryAll";
                Map<String,String> params = new HashMap<>();
                params.put("name",theName);
                params.put("state",theState+"");
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendGet(url,str);
                Message mesg = new Message();
                try {
                    JSONObject result = new JSONObject(response);
                    if(Integer.parseInt(result.getString("status")) == 0){   //成功
                        String listString = result.getString("userList").substring(1);
                        //List<User> 对象
                        mesg.obj = JsonUtil.stringToList(listString,User.class);
                        mesg.what = 0;
                    }else {
                        mesg.what = 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mFriendListHandler.sendMessage(mesg);
                    mFriendListHandler = null;
                    theState = 2;
                    theName = null;
                }
            }
        }).start();
    }

    //完成添加好友
    private static Friend nFriend;
    private static DoneAddFriendHandler mDoneAddFriendHandler;
    private static Integer dPositon;
    public static void doneAddFriend(Friend friend,DoneAddFriendHandler doneAddFriendHandler,
                                     int position){
        mDoneAddFriendHandler = doneAddFriendHandler;
        nFriend = friend;
        dPositon = position;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "friend/doneAdd";
                Map<String,String> params = new HashMap<>();
                params.put("friend", JsonUtil.objectToString(nFriend));
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendPost(url,str);
                Message mesg =new Message();
                mesg.what = 1;
                try {
                    JSONObject result = new JSONObject(response);
                    mesg.what = Integer.parseInt(result.getString("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mesg.obj = dPositon;
                    mDoneAddFriendHandler.sendMessage(mesg);
                    mDoneAddFriendHandler = null;
                    dPositon = 0;
                    nFriend = null;
                }
            }
        }).start();
    }


    //拒绝添加好友
    private static Friend lFriend;
    private static RefuseHandler mRefuseHandler;
    private static Integer rPosition;
    public static void refuseAddFriend(Friend friend,RefuseHandler refuseHandler,int position){
        mRefuseHandler = refuseHandler;
        lFriend = friend;
        rPosition = position;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "friend/delete";
                Map<String,String> params = new HashMap<>();
                params.put("friend", JsonUtil.objectToString(lFriend));
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendPost(url,str);
                Message mesg = new Message();
                mesg.what = 1;
                try {
                    JSONObject result = new JSONObject(response);
                    mesg.what = Integer.parseInt(result.getString("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mesg.obj = rPosition;
                    mRefuseHandler.sendMessage(mesg);
                    mRefuseHandler = null;
                    rPosition = 0;
                    lFriend = null;
                }
            }
        }).start();
    }


    //查看用户的评价数据列表
    private static String mapName;
    private static Integer queryNumber = 0;
    private static QueryTenTitleHandler mQueryTenTitleHandler;
    public static void queryTenTitle(String name,Integer number,QueryTenTitleHandler queryTenTitleHandler){
        mapName = name;
        queryNumber = number;
        mQueryTenTitleHandler = queryTenTitleHandler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = baseUrl + "evaluationData/showTenTitles";
                Map<String,String> params = new HashMap<>();
                params.put("number",queryNumber+"");
                params.put("userName",mapName);
                String str = MapToUrl.getUrlParamsByMapDs(params);
                String response = HttpRequest.sendGet(url,str);
                Message mesg = new Message();
                mesg.what = 1;
                try {
                    JSONObject result = new JSONObject(response);
                    mesg.what = Integer.parseInt(result.getString("status"));
                    if(mesg.what == 0){
                        mesg.obj = JsonUtil.stringToList(result.getString("list").substring(1),
                                EvaluationData.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    mQueryTenTitleHandler.sendMessage(mesg);
                    mQueryTenTitleHandler = null;
                    queryNumber = 0;
                    mapName = null;
                }
            }
        }).start();
    }

}
