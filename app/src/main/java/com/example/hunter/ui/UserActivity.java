package com.example.hunter.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hunter.R;
import com.example.hunter.base.BaseActivity;
import com.example.hunter.mode.HunterUser;

import cn.bmob.v3.BmobUser;

public class UserActivity extends BaseActivity {

    private static final String TAG = "UserActivity";

    private Toolbar mToolbar;
    private TextView mTvUser;
    private Button mBtnExit, mBtnModify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initToolbar();
        initView();
        initListener();
        initUserInfo();
    }

    private void initUserInfo() {
        HunterUser userInfo = BmobUser.getCurrentUser(HunterUser.class);
        if (userInfo != null) {
            mTvUser.setText("当前用户：" + userInfo.getUsername());
        } else {
            showToast(R.string.login_please_login);
        }
    }

    private void initListener() {
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                showToast(R.string.login_exit);
                finish();
            }
        });

        // TODO: 18-1-10 modify user paddword
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(R.string.login_modify);
            }
        });
    }

    private void initView() {
        mTvUser = findViewById(R.id.tv_user);
        mBtnExit = findViewById(R.id.btn_exit);
        mBtnModify = findViewById(R.id.btn_modify);
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.action_tile_user);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
