package com.example.superb.yy4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * desc:测试录音权限.
 * steps:
 * 检测是否有权限--有--执行相关操作
 * --无权限--
 * <p>
 * --判断系统版本
 * --小于6.0 直接获取
 * --大于6.0 动态申请权限
 * --对申请结果的处理回调
 * <p>
 * --允许
 * <p>
 * --拒绝
 * <p>
 * test:
 * test1 build.gradle minsdk <23    真机android7.1 清单文件中配置了录音权限
 * test2 build.gradle minsdk >=23    真机android7.1 清单文件中配置了录音权限
 *
 * @author xuzhuyun
 * @date 2018/5/10
 */
public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //检测是否有录音权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "默认无录音权限");
            if (Build.VERSION.SDK_INT >= 23) {
                Log.i(TAG, "系统版本不低于android6.0 ，需要动态申请权限");
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1001);
            }
        } else {
            Log.i(TAG, "默认有录音权限");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            //方式一校验
            boolean isHasAudioPermission = CheckAudioPermission.isHasPermission(this);
            Log.i(TAG, "申请权限完毕,当前录音权限:" + isHasAudioPermission);
            //方式二校验
            int result = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    result++;
                }
            }
            if (result != permissions.length) {
                //没有权限
                Log.i(TAG, "onRequestPermissionsResult: 申请权限完毕,当前录音权限:false");
                return;
            }
            //有权限
            Log.i(TAG, "onRequestPermissionsResult: 申请后，是否有权限:true");
        }
    }

    public void ccc(View view) {
        startActivity(new Intent(this,MainActivity.class));

    }
}
