package cn.novate.android_day03.simple6;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.novate.android_day03.R;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/3 13:47
 * Version 1.0
 * Params:
 * Description:
*/

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_06);
    }


    /**
     * 给手机中插入一条联系人记录
     */
    public void insert(View view){
        // 通过 联系人的内容提供者 向手机中 插入一个联系人

        // 1. 获取内容提供者的解析器
        ContentResolver resolver = this.getContentResolver() ;
        // 2. 获取内容提供者指定的uri  这个是联系人的uri，是固定不变的
        Uri contactUri = Uri.parse("content://com.android.contacts/raw_contacts/") ;
        ContentValues values = new ContentValues() ;
        values.put("dispay_name" , "xiaowang");
        resolver.insert(contactUri , values) ;

        // 3. 获取新插入的记录id：等于这个表中的记录个数
        Cursor contactCursor = resolver.query(contactUri, null, null,
                null, null);

        if (contactCursor != null && contactCursor.getCount() > 0){
            // 4. 得到记录的个数（个数就是新插入的记录id）
            long contact_id = contactCursor.getCount() ;

            // 5. 在 data 表中插入联系人的字段记录（姓名、电话、电子邮件都作为单独的一条记录插入进来）
            Uri dataUri = Uri.parse("content://com.android.contacts/data/") ;

            // 姓名
            ContentValues nameValues = new ContentValues() ;
            nameValues.put("mimetype" , "vnd.android.cursor.item/name");
            nameValues.put("raw_contace_id" , contact_id);
            nameValues.put("data1" , "xiaowang");
            // 在data表中，插入姓名这条记录
            resolver.insert(dataUri , nameValues) ;

            // 电话号码
            ContentValues phoneValues = new ContentValues();
            phoneValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
            phoneValues.put("raw_contact_id", contact_id);
            phoneValues.put("data1", "18588888888888");
            // 在data表中，插入电话这条记录
            resolver.insert(dataUri, phoneValues);

            // 电子邮件
            ContentValues emailValues = new ContentValues();
            emailValues.put("mimetype", "vnd.android.cursor.item/email_v2");
            emailValues.put("raw_contact_id", contact_id);
            emailValues.put("data1", "cc@163.cn");
            // 在data表中，插入电子邮件这条记录
            resolver.insert(dataUri, emailValues);
        }

        contactCursor.close();
    }
}
