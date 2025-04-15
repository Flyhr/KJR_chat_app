package com.example.kjr_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kjr_app.sampledata.UserDbHelper;


public class LoginActivity extends AppCompatActivity {

    private EditText et_phone;
    private EditText et_password;
    private SharedPreferences mSharedPreferences;
    private boolean is_login;
    private String userPhone;
    private String password;
    private CheckBox checkbox;
    private int selectedLoginType; // 保存当前选中的登录类型

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 获取 SharedPreferences
        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        //拿到记住密码的状态
        boolean is_login = mSharedPreferences.getBoolean("is_login",false);
        String userPhone = mSharedPreferences.getString("userPhone",null);
        String password = mSharedPreferences.getString("userPassword",null);

        // 初始化控件,获取对应组件id
        et_phone = findViewById(R.id.phone);
        et_password = findViewById(R.id.password);
        RadioGroup radioGroup = findViewById(R.id.login_type_radio_group);
        checkbox = findViewById(R.id.checkbox);
        // 设置默认选项
        selectedLoginType = R.id.user_login_button; // 默认用户登录

        // 设置 RadioGroup 的监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedLoginType = checkedId; // 记录当前选中项
            }
        });
//        //check点击事件
//        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                is_login = isChecked;
//            }
//        });

        // 登录按钮的点击事件
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // '还未注册'点击跳转
        findViewById(R.id.to_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // 根据选择的单选框执行不同的登录逻辑
    private void handleLogin() {
        String userPhone = et_phone.getText().toString();
        String userPassword = et_password.getText().toString();

        if (TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(LoginActivity.this, "请输入手机号或密码", Toast.LENGTH_SHORT).show();
        }else if (selectedLoginType==R.id.user_login_button) {
            try {
                //使用手机号查询用户是否存在数据库中
                UserInfo login = UserDbHelper.getInstance(LoginActivity.this).user_login(userPhone);
                if (login == null){
                    Toast.makeText(LoginActivity.this, "用户不存在，请先注册", Toast.LENGTH_SHORT).show();
                }else if (userPhone.equals(login.getPhone()) && userPassword.equals(login.getPassword())) {
                    //记住密码
                    SharedPreferences.Editor edit = mSharedPreferences.edit();
                    edit.putBoolean("is_login", is_login);
                    edit.commit();
                    //登入成功
                    to_main();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e) {
                Log.e("UserDbHelper", "Login query error: " + e.getMessage());
            }
        } else if (selectedLoginType==R.id.doctor_login_button) {
            try {
                //使用手机号查询用户是否存在数据库中
                DocInfo doc_login = UserDbHelper.getInstance(LoginActivity.this).doc_login(userPhone);
                if (doc_login == null){
                    Toast.makeText(LoginActivity.this, "医生用户不存在，请先注册", Toast.LENGTH_SHORT).show();
                }else if (userPhone.equals(doc_login.getPhone()) && userPassword.equals(doc_login.getPassword())) {
                    //记住密码
                    SharedPreferences.Editor edit = mSharedPreferences.edit();
                    edit.putBoolean("is_login", is_login);
                    edit.commit();
                    //登入成功
                    to_main();
                } else {
                    Toast.makeText(LoginActivity.this, "医生用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e) {
                Log.e("UserDbHelper", "Doctor Login query error: " + e.getMessage());
            }
        }
    }
private void to_main(){
    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    }
}
