package com.jayantxie.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 天亮就出发 on 2017/3/20.
 */

public class CheckInput {

    private static final String emailCheck = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static final String passwordCheck = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
    private static Pattern regexPassword = Pattern.compile(passwordCheck);
    private static Pattern regexEmail = Pattern.compile(emailCheck);

    public static boolean check(EditText editName, EditText editPassword, Context context){
        if(editName.getText().toString().equals("")) {
            Toast.makeText(context, "请输入邮箱！", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(editPassword.getText().toString().equals("")) {
            Toast.makeText(context, "请输入密码！", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            Matcher matcherEmail = regexEmail.matcher(editName.getText().toString());
            Matcher matcherPassword = regexPassword.matcher(editPassword.getText().toString());
            if(!matcherEmail.matches()){
                Toast.makeText(context, "请输入正确的邮箱格式！", Toast.LENGTH_SHORT).show();
                return false;
            }else if(!matcherPassword.matches()){
                Toast.makeText(context, "请输入正确的密码格式！包含数字和字母！长度为8-16！", Toast.LENGTH_SHORT).show();
                return false;
            }else return true;
        }
    }

    public static boolean check(EditText editName, EditText editNickName, EditText editPassword, EditText editPasswordRepeat,Context context){
        String p1 = editPassword.getText().toString();
        String p2 = editPasswordRepeat.getText().toString();
        if(editName.getText().toString().equals("")) {
            Toast.makeText(context, "请输入邮箱！", Toast.LENGTH_SHORT).show();
            return false;
        }else if(editNickName.getText().toString().equals("")){
            Toast.makeText(context, "请输入昵称！", Toast.LENGTH_SHORT).show();
            return false;
        }else if(p1.equals("")) {
            Toast.makeText(context, "请输入密码！", Toast.LENGTH_SHORT).show();
            return false;
        }else if(p2.equals("")){
            Toast.makeText(context, "请输入二次密码！", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            Matcher matcherEmail = regexEmail.matcher(editName.getText().toString());
            Matcher matcherPassword = regexPassword.matcher(p1);
            if(!matcherEmail.matches()){
                Toast.makeText(context, "请输入正确的邮箱格式！", Toast.LENGTH_SHORT).show();
                return false;
            }else if(!matcherPassword.matches()){
                Toast.makeText(context, "请输入正确的密码格式！包含数字和字母！长度为8-16！", Toast.LENGTH_SHORT).show();
                return false;
            }else if(!p1.equals(p2)){
                Toast.makeText(context, "两次密码输入不一致，请检查！", Toast.LENGTH_SHORT).show();
                return false;
            }else return true;
        }
    }
}
