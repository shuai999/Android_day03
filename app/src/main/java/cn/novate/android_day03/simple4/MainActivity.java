package cn.novate.android_day03.simple4;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.novate.android_day03.R;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/3 9:38
 * Version 1.0
 * Params:
 * Description:
*/

public class MainActivity extends AppCompatActivity {

    private List<SmsInfo> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_04);
    }


    /**
     * 备份短信
     */
    public void backup(View view){
        // 1. 通过短信的内容提供者 把短信数据查询出来
        list = querySms();
        try {
            // 2. 遍历cursor，把短信数据写到xml文件上
            XmlSerializer serializer = Xml.newSerializer();
            // sd卡路径下的 smsbackup.xml文件

            // 初始化解析器
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/smsbackup.xml") ;
            serializer.setOutput(fos , "UTF-8");

            // 写开头一行和根元素标签
            serializer.startDocument("UTF-8" , true);
            serializer.startTag(null , "smss") ;


            for (SmsInfo info : list) {

                // 给xml文件中写这一条短信的标签
                serializer.startTag(null , "sms") ;  // <sms>

                serializer.startTag(null , "address") ;
                serializer.text(info.getAddress()) ;
                serializer.endTag(null , "address") ;

                serializer.startTag(null , "date") ;
                serializer.text(info.getDate()) ;
                serializer.endTag(null , "date") ;

                serializer.startTag(null , "type") ;
                serializer.text(info.getType()) ;
                serializer.endTag(null , "type") ;

                serializer.startTag(null , "body") ;
                serializer.text(info.getBody()) ;
                serializer.endTag(null , "body") ;

                serializer.endTag(null , "sms") ;  // </sms>
            }

            // 写根元素的结束标签
            serializer.endTag(null , "smss") ;
            // 写文档的结束部分
            serializer.endDocument();

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 通过短信内容提供者把短信数据查询出来
     */
    private List<SmsInfo> querySms() {
        List<SmsInfo> list = new ArrayList<>() ;

        // 1. 获取短信内容提供者的解析器
        ContentResolver resolver = this.getContentResolver() ;
        // 2. 获取内容提供者的uri
        Uri uri = Uri.parse("content://sms/") ;
        // 3. 通过解析器查询短信的address、date、type、body字段
        Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, null, null, null);

        if (cursor != null && cursor.getCount() > 0){
            // 每遍历出一条短信就写入到xml文件中
            while (cursor.moveToNext()){
                // 从cursor中取出短信数据
                String address = cursor.getString(0);
                String date = cursor.getString(1);
                String type = cursor.getString(2);
                String body = cursor.getString(3);

                SmsInfo info = new SmsInfo() ;
                info.setAddress(address);
                info.setDate(date);
                info.setType(type);
                info.setBody(body);

                list.add(info) ;
            }
        }
        return list;
    }


    /**
     * 恢复短信
     */
    public void restore(View view){
        List<SmsInfo> list = new ArrayList<>() ;

        // 从SD卡根目录下读取xml数据，解析数据，插入数据到短信数据库中
        try {

            // 1. 读取xml数据，解析xml数据
            FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/smsbackup.xml") ;

            // 1.1 创建解析器对象
            XmlPullParser parse = Xml.newPullParser();
            // 1.2 初始化解析对象
            parse.setInput(fis , "UTF-8");
            // 1.3 得到当前解析事件的类型
            int type = parse.getEventType();
            SmsInfo info = null ;

            while (type != XmlPullParser.END_DOCUMENT){
                switch (type){
                    case XmlPullParser.START_TAG:  // 开始标签
                        String name = parse.getName();

                        if ("sms".equals(name)){
                            info = new SmsInfo() ;
                        }else if ("address".equals(name)){
                            String address = parse.nextText();
                            info.setAddress(address);
                        }else if ("date".equals(name)){
                            String date = parse.nextText() ;
                            info.setDate(date);
                        }else if ("type".equals(name)){
                            String smsType = parse.nextText() ;
                            info.setType(smsType);
                        }else if ("body".equals(name)){
                            String body = parse.nextText() ;
                            info.setBody(body);
                        }
                        break;
                    case XmlPullParser.END_TAG:  // 结束标签
                        // 获取结束标签的名称
                        String endName = parse.getName() ;
                        if ("sms".equals(endName)){
                            // 调用短信内容提供者的 insert() 方法，插入一条短信记录，
                            // 把刚才解析出来的短信数据插入到 数据库中
                            list.add(info) ;
                            info = null ;
                        }
                        break;
                }


                // 解析到下一个事件
                type = parse.next() ;
            }


            // 2. 通过短信内容提供者把数据 list 中的数据插入到 sms 表中
            insertSms(list) ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过短信内容提供者把数据 list 中的数据插入到 sms 表中
     */
    private void insertSms(List<SmsInfo> list) {
        // 1. 获取内容提供者的解析器
        ContentResolver resolver = this.getContentResolver() ;
        // 2. 获取内容提供者的uri
        Uri uri = Uri.parse("content://sms/") ;
        // 还原短信之前把老的数据清空
        resolver.delete(uri , null , null) ;

        // 开始还原短信
        for (SmsInfo info : list) {
            ContentValues values = new ContentValues() ;

            values.put("address", info.getAddress());
            values.put("date", info.getDate());
            values.put("type", info.getType());
            values.put("body", info.getBody());

            resolver.insert(uri , values) ;
        }

        Toast.makeText(this , "还原成功" , Toast.LENGTH_SHORT).show();

    }

}
