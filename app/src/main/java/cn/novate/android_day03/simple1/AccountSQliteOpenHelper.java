package cn.novate.android_day03.simple1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/2 18:01
 * Version 1.0
 * Params:
 * Description:    数据库
*/

public class AccountSQliteOpenHelper extends SQLiteOpenHelper {


    /**
     * 创建数据库对象，放到内存中
     * @param context：上下文
     * @param name：数据库名称 account.db
     * @param factory：游标工厂 如果是null表示使用系统默认的游标工厂
     * @param version：版本号 至少>0
     */
    public AccountSQliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    /**
     * 创建数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table account (_id integer primary key autoincrement,name varchar(20),money varchar(20))");
    }


    /**
     * 当新版本号大于旧版本号，调用这个方法，升级数据库
     * @param db：构造方法创建的数据库对象
     * @param oldVersion：旧的版本号
     * @param newVersion：新的版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
