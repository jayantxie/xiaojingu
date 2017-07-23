package com.jayantxie.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.pojo.Friend;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.Http;
import com.jayantxie.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewFriendListActivity extends AppCompatActivity {
    private ListView myListView;
    private Button back;
    private List<User> userList;
    private NewFriendListAdapter mAdapter;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend_list);

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
        myListView = (ListView) findViewById(R.id.new_friend_list_view);
        back = (Button) findViewById(R.id.new_friend_list_top_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String userListString = getIntent().getStringExtra("userList");
        if(!userListString.equals("")){
            userList = JsonUtil.stringToList(userListString,User.class);
            mAdapter = new NewFriendListAdapter(this);
            myListView.setAdapter(mAdapter);
        }else
            myListView.setVisibility(View.GONE);
    }

    private class NewFriendListAdapter extends BaseAdapter{
        private LayoutInflater mInflator;

        public NewFriendListAdapter(Context context){
            this.mInflator = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflator.inflate(R.layout.new_friend_item,null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.new_friend_name);
                holder.nickName = (TextView) convertView.findViewById(R.id.new_friend_nickname);
                holder.accept = (Button) convertView.findViewById(R.id.accept_friend);
                holder.ignore = (Button) convertView.findViewById(R.id.ignore);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(userList.get(position).getName());
            holder.nickName.setText(userList.get(position).getNickname());
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Friend friend = new Friend();
                    friend.setUserName(userList.get(position).getName());
                    friend.setFriendName(preferences.getString("name",""));
                    Http.doneAddFriend(friend,mDoneAddFriendHandler,position);
                }
            });

            holder.ignore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Friend friend = new Friend();
                    friend.setUserName(userList.get(position).getName());
                    friend.setFriendName(preferences.getString("name",""));
                    Http.refuseAddFriend(friend,mRefuseHandler,position);
                }
            });

            return convertView;
        }
        //后续添加头像
        public class ViewHolder{
            public TextView name;
            public TextView nickName;
            public Button accept;
            public Button ignore;
        }
        public void delete(int position){
            userList.remove(position);
            notifyDataSetChanged();
            editor.putString("userList",JsonUtil.objectToString(userList));
            editor.apply();
        }
    }

    public DoneAddFriendHandler mDoneAddFriendHandler = new DoneAddFriendHandler();
    public class DoneAddFriendHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                mAdapter.delete((Integer) msg.obj);
                Toast.makeText(NewFriendListActivity.this,"已添加好友！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public RefuseHandler mRefuseHandler = new RefuseHandler();
    public class RefuseHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                mAdapter.delete((Integer) msg.obj);
                Toast.makeText(NewFriendListActivity.this,"已忽略此好友申请！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
