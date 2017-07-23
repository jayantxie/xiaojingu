package com.jayantxie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.CheckInput;
import com.jayantxie.utils.Http;


public class LoginActivity extends AppCompatActivity
        implements android.view.View.OnClickListener{

    private EditText editName,editPassword;
    private Button loginButton,loginErrorButton,registerButton,getInfoButton,
            nameClearButton,passwordClearButton,passwordVisibleButton;
    private TextWatcher userNameWatcher,passwordWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();
        initWatcher();
        editName.addTextChangedListener(userNameWatcher);
        editPassword.addTextChangedListener(passwordWatcher);
    }

    private void initView(){
        editName = (EditText) findViewById(R.id.username);
        editPassword = (EditText) findViewById(R.id.password);

        nameClearButton = (Button) findViewById(R.id.bt_username_clear);
        passwordClearButton = (Button) findViewById(R.id.bt_pwd_clear);
        passwordVisibleButton = (Button) findViewById(R.id.bt_pwd_eye);
        loginButton = (Button) findViewById(R.id.login);
        loginErrorButton = (Button) findViewById(R.id.login_error);
        registerButton = (Button) findViewById(R.id.register);
        getInfoButton = (Button) findViewById(R.id.getInfo);
    }

    private void initEvent(){
        nameClearButton.setOnClickListener(this);
        passwordClearButton.setOnClickListener(this);
        passwordVisibleButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        loginErrorButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        getInfoButton.setOnClickListener(this);

        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(editName.getText().toString().length()>0)
                        nameClearButton.setVisibility(View.VISIBLE);
                    else
                        nameClearButton.setVisibility(View.INVISIBLE);
                }else{
                    nameClearButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(editPassword.getText().toString().length()>0)
                        nameClearButton.setVisibility(View.VISIBLE);
                    else
                        nameClearButton.setVisibility(View.INVISIBLE);
                    passwordVisibleButton.setVisibility(View.VISIBLE);
                }else{
                    passwordClearButton.setVisibility(View.INVISIBLE);
                    passwordVisibleButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initWatcher(){
        userNameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                editPassword.setText("");
                if(s.toString().length()>0){
                    nameClearButton.setVisibility(View.VISIBLE);
                }else{
                    nameClearButton.setVisibility(View.INVISIBLE);
                }
            }
        };

        passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    passwordClearButton.setVisibility(View.VISIBLE);
                }else{
                    passwordClearButton.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    //登录结果的UI更新
    public class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){ //登录成功
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("isLogined","true");
                editor.putString("name",editName.getText().toString());
                editor.putString("token",(String) msg.obj);
                editor.apply();
                finish();
            }else{  //失败
                Toast.makeText(LoginActivity.this,"登录失败！请检查邮箱和密码！",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public LoginHandler mLoginHandler = new LoginHandler();
    @Override
    public void onClick(View arg0){
        switch (arg0.getId()){
            case R.id.login:
                if(CheckInput.check(editName,editPassword,this)){
                    User user = new User();
                    user.setName(editName.getText().toString());
                    user.setPassword(editPassword.getText().toString());
                    Http.login(user,mLoginHandler);
                }
                break;
            case R.id.login_error:
                Intent intent1 = new Intent();
                intent1.setClass(LoginActivity.this,ForgetCodeActivity.class);
                startActivity(intent1);
                break;
            case R.id.register:
                Intent intent2 = new Intent();
                intent2.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent2);
                break;
            case R.id.bt_username_clear:
                editName.setText("");
                editPassword.setText("");
                break;
            case R.id.bt_pwd_clear:
                editPassword.setText("");
                break;
            case R.id.bt_pwd_eye:
                if(editPassword.getInputType() ==
                        (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                    passwordVisibleButton.setBackgroundResource(R.drawable.button_eye_s);
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
                }else{
                    passwordVisibleButton.setBackgroundResource(R.drawable.button_eye_n);
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editPassword.setSelection(editPassword.getText().toString().length());
                break;
        }
    }
}
