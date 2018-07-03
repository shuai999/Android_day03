package cn.novate.android_day03.simple3;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.novate.android_day03.R;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/3 9:16
 * Version 1.0
 * Params:
 * Description:    通过短信提供者的解析器 - 给手机中插入一条短信
*/

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_03);
    }


    /**
     * 插入一条短信
     *      address：电话号码
     *      date：接收或发送时间
     *      type：
     *              1：接收
     *              2：发送
     *      body：短信内容
     */
    public void insert(View view){
        // 通过短信的内容提供者向手机中插入一条短信
        // 1. 获取内容提供者的解析器
        ContentResolver resolver = this.getContentResolver() ;
        // 2. 获取内容提供者的uri
        Uri uri = Uri.parse("content://sms/") ;
        // 3. 调用解析器的插入方法
        ContentValues values = new ContentValues() ;
        values.put("address", "13512345678");
        values.put("date",System.currentTimeMillis());
        values.put("type", 1);
        values.put("body", "王子文");
        resolver.insert(uri , values) ;
    }
}
