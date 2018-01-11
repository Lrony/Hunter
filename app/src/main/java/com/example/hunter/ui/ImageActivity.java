package com.example.hunter.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.hunter.R;
import com.example.hunter.base.BaseActivity;
import com.example.hunter.base.GlideApp;
import com.example.hunter.mode.HunterFile;
import com.example.hunter.utils.DateUtils;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends BaseActivity {

    private static final String TAG = "ImageActivity";

    private Toolbar mToolbar;
    private PhotoView mImageView;
    private ProgressBar mProgressBar;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    private String mTitle, mImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra("Title");
            mImage = intent.getStringExtra("Image");
        }
        initToolbar();
        initView();
        showImage();
        initListener();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (!TextUtils.isEmpty(mTitle)) {
            mToolbar.setTitle(mTitle);
        } else {
            mToolbar.setTitle(R.string.action_tile_image);
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        mImageView = findViewById(R.id.photo_view);
        mProgressBar = findViewById(R.id.photoview_progressbar);
    }

    private void showImage() {
        GlideApp.with(this)
                .load(mImage)
                .fallback(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);

    }

    private void initListener() {
        mImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });

//        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                showDialog();
//                return true;
//            }
//        });
    }

    private void showDialog() {
        final String[] itemData = new String[]{"保存图片"};
        alert = null;
        builder = new AlertDialog.Builder(this);
        alert = builder.setTitle(R.string.action_tile_image)
                .setItems(itemData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast.makeText(ImageActivity.this, "0", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                String fileName = DateUtils.getInstance().getNowDateFormat("yyyyMMddHHmmss") + ".png";
                HunterFile hunterFile = new HunterFile(fileName, "", mImage);
                downloadFile(hunterFile);
                break;
        }
        return true;
    }

    private void downloadFile(final BmobFile file) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        final File saveFile = new File(getApplicationContext().getCacheDir() + "/hunter/", file.getFilename());
        Log.d(TAG, "downloadFile: " + file.getFilename());
        file.download(new DownloadFileListener() {

            @Override
            public void onStart() {
                showToast("开始下载...");
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    showToast("下载成功,保存路径:" + savePath);

                    Uri uri = Uri.fromFile(saveFile);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                } else {
                    Log.d(TAG, "done: " + e.getErrorCode() + "," + e.getMessage());
                    showToast("下载失败：" + e.getErrorCode() + "," + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i(TAG, "下载进度：" + value + "," + newworkSpeed);
            }

        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.image, menu);
//        return true;
//    }
}
