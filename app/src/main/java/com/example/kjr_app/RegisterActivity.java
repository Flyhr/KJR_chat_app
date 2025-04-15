package com.example.kjr_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kjr_app.sampledata.UserDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText et__regis_phone;
    private EditText password_1;
    private EditText password_2;
    private int selectedRegisterType; // 保存当前选中的注册类型

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        //初始化控件
        et__regis_phone = findViewById(R.id.phone);
        password_1 = findViewById(R.id.password_1);
        password_2 = findViewById(R.id.password_2);

        RadioGroup radioGroup = findViewById(R.id.register_type_radio_group);

        // 设置默认选项
        selectedRegisterType = R.id.user_register_button; // 默认用户注册

        // 设置 RadioGroup 的监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRegisterType = checkedId; // 记录当前选中项
            }
        });
        // 注册按钮的点击事件
        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        //左上角返回登入界面
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//销毁注册界面,不需要跳转到登入界面
            }
        });
        //“已有帐号”返回登入界面
        findViewById(R.id.back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });


    }
    // 根据选择的单选框执行不同的登录逻辑
    private void register() {
        String userPhone = et__regis_phone.getText().toString();
        String userPassword1 = password_1.getText().toString();
        String userPassword2 = password_2.getText().toString();
        if (TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(userPassword1) || TextUtils.isEmpty(userPassword2)) {
            Toast.makeText(RegisterActivity.this, "请输入手机号或密码", Toast.LENGTH_SHORT).show();
        }else if(userPassword1.equals(userPassword2))
        {
            if (selectedRegisterType==R.id.user_register_button) {
                try {
                    Long row = UserDbHelper.getInstance(RegisterActivity.this).user_register(userPhone, userPassword1, "注册测试");
                     if (row == -1  || row ==null) {
                         //注册失败
                         fail_register();
                     } else {
                         //注册成功，返回登入界面
                         to_login_win();
                    }
                }catch (Exception e) {
                    Log.e("UserDbHelper", "Register query error: " + e.getMessage());
                }

            } else if (selectedRegisterType==R.id.doctor_register_button) {
                try {
                    Long row = UserDbHelper.getInstance(RegisterActivity.this).doc_register(userPhone, userPassword1, "注册测试");
                    if (row == -1  || row ==null) {
                        fail_register();
                    } else {
                        //注册成功，返回登入界面
                        to_login_win();
                    }
                }catch (Exception e) {
                    Log.e("UserDbHelper", "Doctor Register query error: " + e.getMessage());
                }
            }
        }else {
            Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
        }
    }
    private  void fail_register(){
        Toast.makeText(RegisterActivity.this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();

    }
    //注册成功，返回登入界面
    private void to_login_win(){
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        // 创建 Handler 对象
        Handler handler = new Handler();
        // 使用 postDelayed 方法延迟 2 秒
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();//销毁注册界面
            }
        }, 1500);
    }

}