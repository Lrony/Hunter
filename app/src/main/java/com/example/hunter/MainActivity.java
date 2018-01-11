package com.example.hunter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.hunter.adapter.MainListAdapter;
import com.example.hunter.base.BaseActivity;
import com.example.hunter.base.MainListItemDecoration;
import com.example.hunter.control.HunterControl;
import com.example.hunter.mode.HunterData;
import com.example.hunter.mode.HunterData;
import com.example.hunter.mode.HunterUser;
import com.example.hunter.ui.AboutActivity;
import com.example.hunter.ui.ImageActivity;
import com.example.hunter.ui.LoginActivity;
import com.example.hunter.ui.UserActivity;
import com.example.hunter.utils.DateUtils;
import com.example.hunter.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    private Toolbar mToolbar;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mRefresh;

    private MainListAdapter mAdapter;

    private ArrayList<HunterData> mHunterDatas = new ArrayList<HunterData>();

    private HunterUser mHunterUser;

    BmobRealTimeData mData = new BmobRealTimeData();

    private MyHandler mHandler = new MyHandler();

    private HunterControl mHunterCtl;

    private long mFirstTime = 0;
    private static boolean misInitData = false;

    private static final String TABLE_HUNTER = "HunterData";

    private static final int MSG_TOOLBAR_OFFLIN = 0;
    private static final int MSG_TOOLBAR_CONNECTION = 1;
    private static final int MSG_TOOLBAR_OK = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initView();
        initListener();
        if (!checkLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            initData();
        }

        mHunterCtl = new HunterControl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkLogin()) {
            Log.d(TAG, "onResume: checkLogin = false");
            mHandler.sendEmptyMessage(MSG_TOOLBAR_OFFLIN);
            if (misInitData && mData != null) {
                misInitData = false;
                mData.unsubTableUpdate(TABLE_HUNTER);
            }
            mHunterDatas.clear();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mData != null) {
            mData.unsubTableUpdate(TABLE_HUNTER);
        }
    }

    private boolean checkLogin() {
        mHunterUser = BmobUser.getCurrentUser(HunterUser.class);
        if (mHunterUser == null) {
            return false;
        }
        return true;
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mHandler.sendEmptyMessage(MSG_TOOLBAR_OFFLIN);
        setSupportActionBar(mToolbar);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Log.d(TAG, "initData");

        if (misInitData) {
            Log.d(TAG, "initData: not register server");
            return;
        }

        mRefresh.setRefreshing(true);
        onDataRefresh();

        misInitData = true;
        mHandler.sendEmptyMessage(MSG_TOOLBAR_CONNECTION);
        mData.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                Log.d(TAG, "(" + data.optString("action") + ")" + "数据：" + data);
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(data.optString("action"))) {
                    JSONObject datas = data.optJSONObject("data");
                    int id = datas.optInt("id");
                    int status = datas.optInt("status");
                    String user = datas.optString("user");
                    String image = datas.optString("image");
                    String title = datas.optString("title");
                    Log.d(TAG, "onDataChange: id=" + id + " status="
                            + status + " user=" + user + " image=" + image + " title=" + title);

                    if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(image)) {
                        if (mHunterUser != null) {
                            if (user.equals(mHunterUser.getUsername())) {
                                HunterData hunterData = new HunterData(id, status, user, title, image);
                                mHunterDatas.add(hunterData);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "onDataChange: user error :" + mHunterUser.getUsername());
                            }
                        } else {
                            Log.d(TAG, "onDataChange: mHunterUser is null");
                        }
                    } else {
                        Log.d(TAG, "onDataChange: data is null");
                    }
                }
            }

            @Override
            public void onConnectCompleted(Exception ex) {
                Log.d(TAG, "连接成功");
                if (mData.isConnected()) {
                    mHandler.sendEmptyMessage(MSG_TOOLBAR_OK);
                    // 监听表更新
                    mData.subTableUpdate(TABLE_HUNTER);
//                    // 监听表删除
//                    rtd.subTableDelete(tableName);
//                    // 监听行更新
//                    rtd.subRowUpdate(tableName, objectId);
//                    // 监听行删除
//                    rtd.subRowDelete(tableName, objectId);
                } else {
                    mHandler.sendEmptyMessage(MSG_TOOLBAR_OFFLIN);
                }
            }
        });

//        // Normal
//        HunterData Hunter1 = new HunterData(1, 0, "", "状态截图 2018-01-09 16:57:25"
//                , "http://img.hb.aicdn.com/b775abf28f82262dd5b982322a3be1a1d" +
//                "bfe6d3a7493d-xKv3BJ_fw658");
//        mHunter.add(Hunter1);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRefresh = findViewById(R.id.refresh_view);
        mRefresh.setColorSchemeResources(R.color.colorPrimary);

        mRecycler = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.addItemDecoration(new MainListItemDecoration(12));
        mAdapter = new MainListAdapter(this, mHunterDatas);
        mRecycler.setAdapter(mAdapter);
    }

    /**
     * 添加监听
     */
    private void initListener() {
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(new MainListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                intent.putExtra("Title", mHunterDatas.get(position).getTitle());
                intent.putExtra("Image", mHunterDatas.get(position).getImage());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_user:
                HunterUser userInfo = BmobUser.getCurrentUser(HunterUser.class);
                if (userInfo != null) {
                    startActivity(new Intent(this, UserActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.menu_more:
//                startActivity(new Intent(this, AboutActivity.class));
                testAddData();
                break;
        }
        return true;
    }

    private void testAddData() {
        HunterData hunterData = new HunterData();
        //注意：不能调用gameScore.setObjectId("")方法
        hunterData.setImage("http://img.hb.aicdn.com/b775abf28f82262dd5b982322a3be" +
                "1a1dbfe6d3a7493d-xKv3BJ_fw658");
        hunterData.setStatus(0);
        hunterData.setTitle("状态测试");
        hunterData.setUser("admin");
        hunterData.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    showToast("创建数据成功：" + objectId);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - mFirstTime > 2000) {
                showToast(R.string.app_exit);
                mFirstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onRefresh() {
        if (checkLogin()) {
            onDataRefresh();
        } else {
            mRefresh.setRefreshing(false);
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void onDataRefresh() {
        Log.d(TAG, "onDataRefresh");
        if (mHunterCtl == null) {
            mHunterCtl = new HunterControl();
        }
        BmobQuery<HunterData> query = new BmobQuery<HunterData>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("user", mHunterUser.getUsername());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<HunterData>() {
            @Override
            public void done(List<HunterData> object, BmobException e) {
                if (e == null) {
                    mHunterDatas.clear();
                    for (HunterData hunterData : object) {
                        mHunterDatas.add(hunterData);
                    }
                    Log.d(TAG, "done: " + mHunterDatas.size());
                } else {
                    Log.d(TAG, "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                mAdapter.notifyDataSetChanged();
                mRefresh.setRefreshing(false);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOOLBAR_OK:
                    mToolbar.setTitle(R.string.server_status_ok);
                    break;
                case MSG_TOOLBAR_CONNECTION:
                    mToolbar.setTitle(R.string.server_status_connection);
                    break;
                case MSG_TOOLBAR_OFFLIN:
                    mToolbar.setTitle(R.string.server_status_offlin);
                    break;
            }
        }
    }
}
