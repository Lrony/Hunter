package com.example.hunter.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hunter.R;
import com.example.hunter.base.BaseActivity;
import com.example.hunter.mode.HunterUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    private Button mBtnRegister;
    private EditText mEdtUsername, mEdtPassword, medtEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
    }

    private void initView() {
        mBtnRegister = findViewById(R.id.register);
        mEdtPassword = findViewById(R.id.edt_password);
        mEdtUsername = findViewById(R.id.edt_username);
        medtEmail = findViewById(R.id.edt_email);
    }

    private void initListener() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEdtUsername.getText().toString();
                String password = mEdtPassword.getText().toString();
                String email = medtEmail.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
                    BmobUser bu = new BmobUser();
                    bu.setUsername(username);
                    bu.setPassword(password);
                    bu.setEmail(email);
                    //注意：不能用save方法进行注册
                    bu.signUp(new SaveListener<HunterUser>() {
                        @Override
                        public void done(HunterUser s, BmobException e) {
                            if (e == null) {
                                showToast("注册成功:" + s.toString());
                            } else {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    });
                } else {
                    showToast("have data empty!");
                }
            }
        });
    }
}
