package com.example.hunter.control;

import android.util.Log;

import com.example.hunter.mode.HunterData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HunterControl {

    private static final String TAG = "HunterControl";

    private ArrayList<HunterData> mHunterDatas = new ArrayList<HunterData>();

    public ArrayList<HunterData> getAllHunterDatas(String user) {
        Log.d(TAG, "getAllHunterDatas");
        mHunterDatas.clear();
        BmobQuery<HunterData> query = new BmobQuery<HunterData>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("user", user);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<HunterData>() {
            @Override
            public void done(List<HunterData> object, BmobException e) {
                if (e == null) {
                    for (HunterData hunterData : object) {
                        mHunterDatas.add(hunterData);
                    }
                    Log.d(TAG, "done: " + mHunterDatas.size());
                } else {
                    Log.d(TAG, "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return mHunterDatas;
    }
}
