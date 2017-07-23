package com.jayantxie.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.pojo.InitialSettings;
import com.jayantxie.utils.DoubleDatesToMinutes;


import java.util.Date;

public class SettingsActivity extends Activity
        implements android.view.View.OnClickListener{
    private TimePicker mTimePicker;
    private InitialSettings mInitialSettings = new InitialSettings();
    private EditText mRelaxTime;
    private EditText mRelaxTimes;
    private Button mDoneSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mRelaxTime = (EditText) findViewById(R.id.relax_time);
        mRelaxTimes = (EditText)findViewById(R.id.relax_times);
        mDoneSettings = (Button) findViewById(R.id.done_settings);

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Date date = new Date();
                date.setHours(hourOfDay);
                date.setMinutes(minute);
                date.setSeconds(0);
                mInitialSettings.setEndTime(date);
            }
        });
        //设置时间输入范围
        mRelaxTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    int a = 0;
                    try {
                        a = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        a = 0;
                    }
                    if (a > 60)
                        mRelaxTime.setText("60");
                    else if(a < 1)
                        mRelaxTime.setText("1");
                    return;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (start > 1) {
                    int num = Integer.parseInt(s.toString());
                    if (num > 60)
                        s = "60";
                    else if(num < 1)
                        s = "1";
                    return;
                }
            }
        });
        //设置次数输入范围
        mRelaxTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    int a = 0;
                    try {
                        a = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        a = 0;
                    }
                    if (a > 60)
                        mRelaxTimes.setText("5");
                    else if(a < 1)
                        mRelaxTimes.setText("1");
                    return;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (start > 1) {
                    int num = Integer.parseInt(s.toString());
                    if (num > 60)
                        s = "5";
                    else if(num < 1)
                        s = "1";
                    return;
                }
            }
        });

        mDoneSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0){
        switch (arg0.getId()) {
            case R.id.done_settings:
                if(mRelaxTime.getText().toString().equals(""))
                    Toast.makeText(this,"请输入时间！",Toast.LENGTH_SHORT).show();
                else if(mRelaxTimes.getText().toString().equals(""))
                    Toast.makeText(this,"请输入次数！",Toast.LENGTH_SHORT).show();
                else{
                    int relaxTime = Integer.parseInt(mRelaxTime.getText().toString());
                    int relaxTimes = Integer.parseInt(mRelaxTimes.getText().toString());
                    Date nowDate = new Date();
                    int minutes = (int)DoubleDatesToMinutes.translate(nowDate,mInitialSettings.getEndTime());
                    //调节工作时间的要求
                    if(minutes<=(relaxTime))
                        Toast.makeText(SettingsActivity.this,"设置学习时间过短！请重新设置！",Toast.LENGTH_SHORT).show();
                    else{
                        mInitialSettings.setRelaxTime(relaxTime);
                        mInitialSettings.setRelaxTimes(relaxTimes);
                        Intent intent = new Intent();
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("initialSettings",mInitialSettings);
                        intent.putExtras(mBundle);
                        SettingsActivity.this.setResult(1,intent);
                        //马上关闭Activity
                        this.finish();
                    }
                }
                break;
            default:
                break;
        }
    }
}
