package com.jayantxie.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.pojo.User;
import com.jayantxie.utils.CheckInput;
import com.jayantxie.utils.Http;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener {


    private EditText editName, editPassword, editPasswordRepeat, editNickName;
    private Button registerButton, nameClearButton, nickNameClearButton, passwordClearButton, passwordVisibleButton,
            repeatPasswordClearButton, repeatPasswordVisibleButton, backToLoginButton, registerBackButton;
    private TextWatcher nameWatcher, nickNameWatcher, passwordWatcher, repeatPasswordWatcher;

    private RegisterHandler mRegisterHandler = new RegisterHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initEvent();
        initWatcher();
        editName.addTextChangedListener(nameWatcher);
        editNickName.addTextChangedListener(nickNameWatcher);
        editPassword.addTextChangedListener(passwordWatcher);
        editPasswordRepeat.addTextChangedListener(repeatPasswordWatcher);
    }

    private void initView() {
        editName = (EditText) findViewById(R.id.username_register);
        editNickName = (EditText) findViewById(R.id.nickname_register);
        editPassword = (EditText) findViewById(R.id.password_register);
        editPasswordRepeat = (EditText) findViewById(R.id.password_register_repeat);

        registerButton = (Button) findViewById(R.id.register_button);
        registerBackButton = (Button) findViewById(R.id.register_back_button);
        nameClearButton = (Button) findViewById(R.id.bt_username_clear_register);
        nickNameClearButton = (Button) findViewById(R.id.bt_nickname_clear_register);
        passwordClearButton = (Button) findViewById(R.id.bt_pwd_clear_register);
        passwordVisibleButton = (Button) findViewById(R.id.bt_pwd_eye_register);
        repeatPasswordClearButton = (Button) findViewById(R.id.bt_pwd_clear_register_repeat);
        repeatPasswordVisibleButton = (Button) findViewById(R.id.bt_pwd_eye_register_repeat);
        backToLoginButton = (Button) findViewById(R.id.back_to_login);
    }

    private void initEvent() {
        registerButton.setOnClickListener(this);
        nameClearButton.setOnClickListener(this);
        nickNameClearButton.setOnClickListener(this);
        passwordClearButton.setOnClickListener(this);
        passwordVisibleButton.setOnClickListener(this);
        repeatPasswordClearButton.setOnClickListener(this);
        repeatPasswordVisibleButton.setOnClickListener(this);
        backToLoginButton.setOnClickListener(this);
        registerBackButton.setOnClickListener(this);

        editName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(editName.getText().toString().length()>0)
                        nameClearButton.setVisibility(View.VISIBLE);
                    else
                        nameClearButton.setVisibility(View.INVISIBLE);
                } else {
                    nameClearButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        editNickName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(editNickName.getText().toString().length()>0)
                        nickNameClearButton.setVisibility(View.VISIBLE);
                    else
                        nickNameClearButton.setVisibility(View.INVISIBLE);
                } else {
                    nickNameClearButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(editPassword.getText().toString().length()>0)
                        passwordClearButton.setVisibility(View.VISIBLE);
                    else
                        passwordClearButton.setVisibility(View.INVISIBLE);
                    passwordVisibleButton.setVisibility(View.VISIBLE);
                } else {
                    passwordClearButton.setVisibility(View.INVISIBLE);
                    passwordVisibleButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        editPasswordRepeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(editPasswordRepeat.getText().toString().length()>0)
                        repeatPasswordClearButton.setVisibility(View.VISIBLE);
                    else
                        repeatPasswordClearButton.setVisibility(View.INVISIBLE);
                    repeatPasswordVisibleButton.setVisibility(View.VISIBLE);
                } else {
                    repeatPasswordClearButton.setVisibility(View.INVISIBLE);
                    repeatPasswordVisibleButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initWatcher() {
        nameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    nameClearButton.setVisibility(View.VISIBLE);
                } else {
                    nameClearButton.setVisibility(View.INVISIBLE);
                }
            }
        };

        nickNameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    nickNameClearButton.setVisibility(View.VISIBLE);
                } else {
                    nickNameClearButton.setVisibility(View.INVISIBLE);
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
                if (s.toString().length() > 0) {
                    passwordClearButton.setVisibility(View.VISIBLE);
                } else {
                    passwordClearButton.setVisibility(View.INVISIBLE);
                }
            }
        };

        repeatPasswordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    repeatPasswordClearButton.setVisibility(View.VISIBLE);
                } else {
                    repeatPasswordClearButton.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    //注册结果的UI更新
    public class RegisterHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            String mesg = (String)msg.obj;
            Toast.makeText(RegisterActivity.this,mesg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.register_button:
                if (CheckInput.check(editName, editNickName, editPassword, editPasswordRepeat, this)) {
                    //注册
                    User user = new User();
                    user.setName(editName.getText().toString());
                    user.setNickname(editNickName.getText().toString());
                    user.setPassword(editPassword.getText().toString());
                    Http.register(user,mRegisterHandler);
                }
                break;
            case R.id.register_back_button:
                this.finish();
                break;
            case R.id.back_to_login:
                this.finish();
                break;
            case R.id.bt_username_clear_register:
                editName.setText("");
                break;
            case R.id.bt_nickname_clear_register:
                editNickName.setText("");
                break;
            case R.id.bt_pwd_clear_register:
                editPassword.setText("");
                break;
            case R.id.bt_pwd_clear_register_repeat:
                editPasswordRepeat.setText("");
                break;
            case R.id.bt_pwd_eye_register:
                if (editPassword.getInputType() ==
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    passwordVisibleButton.setBackgroundResource(R.drawable.button_eye_s);
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    passwordVisibleButton.setBackgroundResource(R.drawable.button_eye_n);
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editPassword.setSelection(editPassword.getText().toString().length());
                break;
            case R.id.bt_pwd_eye_register_repeat:
                if (editPasswordRepeat.getInputType() ==
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    repeatPasswordVisibleButton.setBackgroundResource(R.drawable.button_eye_s);
                    editPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    repeatPasswordVisibleButton.setBackgroundResource(R.drawable.button_eye_n);
                    editPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editPasswordRepeat.setSelection(editPasswordRepeat.getText().toString().length());
                break;
        }
    }

}
