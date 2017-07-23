package com.jayantxie.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.activity.EvaluationListActivity;
import com.jayantxie.activity.MainActivity;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.Http;

import java.util.List;


public class FindFragment extends Fragment {
    private ListView myListView;
    private Button refresh;
    private MainActivity mMainActivity;
    private List<User> friendList;
    private MyFriendListAdapter mAdapter;
    private boolean isRefresh = false;
    private String choosedNickname;
    private String choosedName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        mMainActivity = (MainActivity) getActivity();
        myListView = (ListView) rootView.findViewById(R.id.my_friend_list_view);
        refresh = (Button) rootView.findViewById(R.id.friend_refresh);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosedName = friendList.get(position).getName();
                choosedNickname = friendList.get(position).getNickname();
                Intent intent = new Intent(mMainActivity, EvaluationListActivity.class);
                intent.putExtra("name",choosedName);
                intent.putExtra("nickname",choosedNickname);
                startActivity(intent);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = true;
                Http.queryMyFriendList(mMainActivity.getSharedPreferences("user", Context.MODE_PRIVATE).
                        getString("name",""),2,mFriendListHandler);
            }
        });
        init();
        return rootView;
    }

    public void init(){
        Http.queryMyFriendList(mMainActivity.getSharedPreferences("user", Context.MODE_PRIVATE).
                getString("name",""),2,mFriendListHandler);
        Log.d("access","OKOK");
    }

    public FriendListHandler mFriendListHandler = new FriendListHandler();
    public class FriendListHandler extends Handler{
        //去除警告
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                Log.d("access","OKOKOK");
                friendList = (List<User>) msg.obj;
                mAdapter = new MyFriendListAdapter(mMainActivity);
                myListView.setAdapter(mAdapter);
                if(isRefresh)
                    Toast.makeText(mMainActivity,"刷新好友列表成功！",Toast.LENGTH_SHORT).show();
            }else
                myListView.setVisibility(View.GONE);
        }
    }

    private class MyFriendListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;

        public MyFriendListAdapter(Context context){
            this.mInflator = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return friendList.size();
        }

        @Override
        public Object getItem(int position) {
            return friendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflator.inflate(R.layout.my_friend_item,null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.my_friend_name);
                holder.nickName = (TextView) convertView.findViewById(R.id.my_friend_nickname);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(friendList.get(position).getName());
            holder.nickName.setText(friendList.get(position).getNickname());

            return convertView;
        }
        //后续添加头像
        public class ViewHolder{
            public TextView name;
            public TextView nickName;
        }
    }
}
