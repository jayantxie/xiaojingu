package com.jayantxie.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.fragment.FindFragment;
import com.jayantxie.fragment.FirstPageFragment;
import com.jayantxie.fragment.MeFragment;
import com.jayantxie.utils.MyFragmentPagerAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements android.view.View.OnClickListener {

    private ViewPager viewPager;   //界面切换
    private MyFragmentPagerAdapter mAdapter;  //初始化适配器
    private ArrayList<Fragment> fragmentList;//存放碎片列表
    //包含文字与图片的底部按钮
    private LinearLayout tabFirstPage;
    private LinearLayout tabFind;
    private LinearLayout tabMe;
    //三个按钮
    private ImageButton firstPageImage;
    private ImageButton findImage;
    private ImageButton meImage;
    //添加好友
    private ImageButton addImage;

    @Override
    protected void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_main);
        initView();
        initViewPage();
        initEvent();
    }

    private void initView(){
        viewPager = (ViewPager) findViewById(R.id.id_viewPage);
        //初始化三个LinearLayout
        tabFirstPage = (LinearLayout) findViewById(R.id.id_tab_firstPage);
        tabFind = (LinearLayout) findViewById(R.id.id_tab_find);
        tabMe = (LinearLayout) findViewById(R.id.id_tab_settings);
        //初始化三个按钮
        this.firstPageImage = (ImageButton) findViewById(R.id.id_tab_firstPage_img);
        this.findImage = (ImageButton) findViewById(R.id.id_tab_find_img);
        this.meImage = (ImageButton) findViewById(R.id.id_tab_settings_img);
        //添加好友
        this.addImage = (ImageButton) findViewById(R.id.top_add);
    }

    private void initViewPage(){
        this.fragmentList = new ArrayList<Fragment>();
        Fragment fragment01 = new FirstPageFragment();
        Fragment fragment02 = new FindFragment();
        Fragment fragment03 = new MeFragment();
        fragmentList.add(fragment01);
        fragmentList.add(fragment02);
        fragmentList.add(fragment03);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
    }

    private void initEvent(){
        tabFirstPage.setOnClickListener(this);
        tabFind.setOnClickListener(this);
        tabMe.setOnClickListener(this);

        addImage.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem){
                    case 0:
                        resetImg();
                        firstPageImage.setImageResource(R.drawable.tab_first_page_pressed);
                        break;
                    case 1:
                        resetImg();
                        findImage.setImageResource(R.drawable.tab_find_pressed);
                        break;
                    case 2:
                        resetImg();
                        meImage.setImageResource(R.drawable.tab_settings_pressed);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View arg0){
        switch (arg0.getId()){
            case R.id.id_tab_firstPage:
                viewPager.setCurrentItem(0);
                resetImg();
                firstPageImage.setImageResource(R.drawable.tab_first_page_pressed);
                break;
            case R.id.id_tab_find:
                viewPager.setCurrentItem(1);
                resetImg();
                findImage.setImageResource(R.drawable.tab_find_pressed);
                break;
            case R.id.id_tab_settings:
                viewPager.setCurrentItem(2);
                resetImg();
                meImage.setImageResource(R.drawable.tab_settings_pressed);
                break;
            case R.id.top_add:
                Intent intent = new Intent(this,AddOptionsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void resetImg(){
        firstPageImage.setImageResource(R.drawable.tab_first_page_normal);
        findImage.setImageResource(R.drawable.tab_find_normal);
        meImage.setImageResource(R.drawable.tab_settings_normal);
    }


}
