package cn.novate.android_day03.simple7;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.novate.android_day03.R;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/3 14:22
 * Version 1.0
 * Params:
 * Description:    给短信内容提供者注册一个观察者，用来观察提供者中数据的变化情况
*/

public class MainActivity extends AppCompatActivity {

    private ContentResolver resolver;
    private Uri uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_07);


        // 1. 获取内容提供者的解析器
        resolver = this.getContentResolver();
        // 2. 获取内容提供者指定的uri
        uri = Uri.parse("content://sms/");
        // 3. 给内容提供者注册一个观察者
        // uri：内容提供者的uri，就是给这个提供者注册一个观察者
        // notifyForDescendents：表示以uri开头 uri里面的数据发生变化时 是否通知观察者
        // observer：内容观察者
        resolver.registerContentObserver(uri, true , new MyObserver(new Handler()));
    }


    /**
     * 自定义的内容观察者
     */
    private class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }


        /**
         * 当提供者中的数据发生变化时调用这个方法
         */
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 手机接收到一条短信后，就查询出短信的内容
            // 查询短信表，使用降序排序，只要取出第一条记录就可以了
            Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, null, null, "_id desc");

            if (cursor != null && cursor.getCount() > 0){
                cursor.moveToNext() ;

                // 取出第一个记录
                String address = cursor.getString(0);
                String date = cursor.getString(1);
                String type = cursor.getString(2);
                String body = cursor.getString(3);

                System.out.println("address=="+address+";  date="+date+"; type="+type+"; body="+body);
                cursor.close();
            }
        }
    }
}
