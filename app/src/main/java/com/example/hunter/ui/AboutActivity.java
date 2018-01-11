package com.example.hunter.ui;

import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hunter.R;
import com.example.hunter.adapter.AboutAdapter;
import com.example.hunter.base.AboutListItemDecoration;
import com.example.hunter.base.BaseActivity;
import com.example.hunter.mode.HunterAbout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AboutActivity extends BaseActivity {

    private static final String TAG = "AboutActivity";

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private RecyclerView mRecycler;

    private AboutAdapter mAdapter;

    private ArrayList<HunterAbout> mHunterAbouts = new ArrayList<HunterAbout>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
        initListener();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        BmobQuery<HunterAbout> query = new BmobQuery<HunterAbout>();
        query.setLimit(50);
        query.findObjects(new FindListener<HunterAbout>() {
            @Override
            public void done(List<HunterAbout> object, BmobException e) {
                if (e == null) {
                    mHunterAbouts.clear();
                    for (HunterAbout hunterAbout : object) {
                        mHunterAbouts.add(hunterAbout);
                    }
                    if (mHunterAbouts.size() <= 0) {
                        showToast("空空如也");
                    }
                } else {
                    Log.i(TAG, "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecycler = findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new AboutListItemDecoration(20));
        mAdapter = new AboutAdapter(this, mHunterAbouts);
        mRecycler.setAdapter(mAdapter);
    }

    private void initListener() {
//        mAdapter.setOnItemClickListener(new AboutAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(AboutActivity.this, "" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
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
