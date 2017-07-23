package com.jayantxie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.pojo.EvaluationData;
import com.jayantxie.utils.Http;

import java.util.ArrayList;
import java.util.List;

public class EvaluationListActivity extends AppCompatActivity {
    private ListView myListView;
    private Button back;
    private TextView topHint;
    private Button refresh;
    private String nickname;
    private String name;
    private Integer number = 0;
    private List<EvaluationData> evaluationDataList = new ArrayList<>();
    private TitleListAdapter mAdapter;
    private boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_list);

        refresh = (Button) findViewById(R.id.evaluation_refresh);
        topHint = (TextView) findViewById(R.id.evaluation_list_top_text);
        myListView = (ListView) findViewById(R.id.evaluation_list_view);
        back = (Button) findViewById(R.id.evaluation_list_back);

        if(getIntent().getStringExtra("name").equals("")){
            topHint.setText("我的评价数据列表栏");
            name = getSharedPreferences("user", Context.MODE_PRIVATE).getString("name","");
        }else{
            name = getIntent().getStringExtra("name");
            nickname = getIntent().getStringExtra("nickname");
            topHint.setText(nickname + "的评价数据列表栏");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Http.queryTenTitle(name,number,mQueryTenTitleHandler);
        number += 10;
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Http.queryTenTitle(name,number,mQueryTenTitleHandler);
                number += 10;
            }
        });
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EvaluationListActivity.this,EvaluationDetailActivity.class);
                intent.putExtra("id",evaluationDataList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public QueryTenTitleHandler mQueryTenTitleHandler = new QueryTenTitleHandler();
    public class QueryTenTitleHandler extends Handler{
        //去除警告
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                if(!isRefresh){
                    evaluationDataList = (List<EvaluationData>) msg.obj;
                    mAdapter = new TitleListAdapter(EvaluationListActivity.this);
                    myListView.setAdapter(mAdapter);
                    isRefresh = true;
                }else{
                    mAdapter.refresh((List<EvaluationData>) msg.obj);
                }
            }else{
                if(!isRefresh)
                    myListView.setVisibility(View.GONE);
                Toast.makeText(EvaluationListActivity.this,"未找到新的评价列表！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class TitleListAdapter extends BaseAdapter{
        private LayoutInflater mInflator;

        public TitleListAdapter(Context context){
            this.mInflator = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return evaluationDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return evaluationDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView ==null){
                convertView = mInflator.inflate(R.layout.evaluation_item,null);
                holder = new ViewHolder();
                holder.picture = (ImageView) convertView.findViewById(R.id.evaluation_pic);
                holder.scores = (TextView) convertView.findViewById(R.id.evaluation_scores);
                holder.beginTime = (TextView)convertView.findViewById(R.id.evaluation_begin_time);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            int scoresContent = evaluationDataList.get(position).getScores();
            String benginTimeContent = evaluationDataList.get(position).getBeginTime().toString();
            if(scoresContent < 60){
                holder.picture.setImageResource(R.mipmap.bad_pic);
            }
            holder.scores.setText("评价得分："+scoresContent+"。");
            holder.beginTime.setText(benginTimeContent);
            return convertView;
        }

        public void refresh(List<EvaluationData> newList){
            evaluationDataList.addAll(newList);
            notifyDataSetChanged();
        }

        public class ViewHolder{
            public ImageView picture;
            public TextView scores;
            public TextView beginTime;
        }
    }
}
