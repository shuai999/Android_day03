package cn.novate.android_day03.simple5;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import cn.novate.android_day03.R;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/3 11:06
 * Version 1.0
 * Params:
 * Description:
*/

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_05);
    }


    /**
     * 通过联系人的内容提供者查询联系人数据
     */
    public void query(View view){
        // 1. 获取内容提供者的解析器
        ContentResolver resolver = this.getContentResolver() ;
        // 2. 获取内容提供者的 uri
        Uri contactUri = Uri.parse("content://com.android.contacts/raw_contacts/") ;
        // 3. 调用解析器的查询方法
        Cursor cursor = resolver.query(contactUri, new String[] { "_id" },
                null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            // 4. 遍历 cursor，然后从data表中查询联系人的数据
            while (cursor.moveToNext()){
                // 5. 获取联系人id
                long contact_id = cursor.getLong(0);
                // 6. 根据联系人id 在Data表中查询联系人的记录
                Uri dataUri = Uri.parse("content://com.android.contacts/data/") ;
                Cursor dataCursor = resolver.query(dataUri, new String[] {
                                "mimetype", "raw_contact_id", "data1" },
                        "raw_contact_id = ?", new String[] { contact_id + "" },
                        null);

                // 7. 遍历 dataCursor中的数据
                while (dataCursor.moveToNext()){
                    // 8. 获取mimetype和data1
                    String mimeType = dataCursor.getString(0) ;
                    String data1 = dataCursor.getString(1);

                    // 9. 根据mimeType值判断 data表中的 当前记录是联系人姓名、电话号码还是电子邮件
                    if ("vnd.android.cursor.item/name".equals(mimeType)){
                        Log.e("TAG" , "姓名是：" + data1) ;
                    }
                    if ("vnd.android.cursor.item/phone_v2".equals(mimeType)){
                        Log.e("TAG" , "电话是：" + data1) ;
                    }
                    if ("vnd.android.cursor.item/email_v2".equals(mimeType)){
                        Log.e("TAG" , "电子邮件是：" + data1) ;
                    }
                    dataCursor.close();
                }
                cursor.close();
            }
        }
    }
}
