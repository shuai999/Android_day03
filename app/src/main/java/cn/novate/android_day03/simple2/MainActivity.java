package cn.novate.android_day03.simple2;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.novate.android_day03.R;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/3 7:56
 * Version 1.0
 * Params:
 * Description:    这个类就相当于第二个工程，调用第一个工程的数据库中的表
*/

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_02);
    }


    /**
     * 添加数据：
     * 调用内容提供者，给数据库中插入一条记录
     */
    public void insert(View view){
        // 1. 获取内容提供者解析器
        ContentResolver resolver = this.getContentResolver() ;
        // 2. 指定内容提供者的uri
        Uri uri = Uri.parse("content://cn.novate.android_day03.simple1.AccountContentProvider/account/insert") ;
        // 3. 创建一个ContentValues对象 用来封装需要插入的列及其值
        ContentValues values = new ContentValues() ;
        values.put("name" , "novate");
        values.put("money" , "200000000000");
        // 4. 调用解析器的插入方法（解析器的增删改查方法与内容提供者的增删改查方法一一对应，调用解析器的增删改查方法就是调用内容提供者的增删改查方法）
        resolver.insert(uri , values) ;
    }


    /**
     * 删除数据：
     * 调用内容提供者，删除一条数据
     */
    public void delete(View view){
        // 1. 获取内容提供者的解析器
        ContentResolver resolver = this.getContentResolver() ;
        // 2. 指定内容提供者的uri

    }
}
