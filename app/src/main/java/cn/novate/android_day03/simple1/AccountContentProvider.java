package cn.novate.android_day03.simple1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/2 18:05
 * Version 1.0
 * Params:
 * Description:    自定义内容提供者，继承系统内容提供者
*/

public class AccountContentProvider extends ContentProvider {


    private AccountSQliteOpenHelper helper;

    // 创建一个uriMatcher，用来匹配用户传递的uri是否合法，如果不合法，就返回NO_MATCH（-1）
    private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH) ;

    private static final String authority = "cn.novate.android_day03.simple1.AccountContentProvider";

    private static final int QUERY_CODE = 0;
    private static final int UPDATE_CODE = 1;
    private static final int DELETE_CODE = 2;
    private static final int INSERT_CODE = 3;

    /**
     * 注册uri
     * authority：内容提供者的主机名，可以在清单文件中找到
     * path：路径名，通常是 表名/方法名
     * code：匹配码，使用 mUriMatcher匹配用户传递过来的 uri，成功时返回这个code
     * 匹配成功使用的uri字符串：content://cn.novate.android_day03.simple1.AccountContentProvider/account/insert
     */
    static {
        // 添加合法的uri，如：content://cn.novate.android_day03.simple1.AccountContentProvider/account/insert
        mUriMatcher.addURI(authority , "account/insert" , INSERT_CODE);
        mUriMatcher.addURI(authority, "account/query", QUERY_CODE);
        mUriMatcher.addURI(authority, "account/update", UPDATE_CODE);
        mUriMatcher.addURI(authority, "account/delete", DELETE_CODE);
    }


    /**
     * 获取数据库的实例对象
     */
    @Override
    public boolean onCreate() {
        helper = new AccountSQliteOpenHelper(getContext() , "account.db" , null , 1);

        return false;
    }


    /**
     * 插入数据
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // 判断用户传递过来的uri , 是不是插入的uri：content://cn.novate.android_day03.simple1.AccountContentProvider/account/insert
        if (INSERT_CODE == mUriMatcher.match(uri)){
            Log.e("TAG" , "insert") ;
            SQLiteDatabase db = helper.getWritableDatabase();
            long id = db.insert("account" , null , values) ;

            // 返回 uri：content://cn.novate.android_day03.simple1.AccountContentProvider/account/insert/1
            return Uri.withAppendedPath(uri , id + "") ;
        }
        return null;
    }


    /**
     * 删除数据
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // 判断用户传递过来的uri ，是不是删除的uri：content://cn.novate.android_day03.simple1.AccountContentProvider/account/delete
        if (DELETE_CODE == mUriMatcher.match(uri)){
            SQLiteDatabase db = helper.getWritableDatabase();
            db.delete("account" , selection , selectionArgs) ;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // 判断用户传递过来的uri ，是不是更新的uri：content://cn.novate.android_day03.simple1.AccountContentProvider/account/update
        if (UPDATE_CODE == mUriMatcher.match(uri)){
            SQLiteDatabase db = helper.getWritableDatabase();
            db.update("account", values, selection, selectionArgs);
        }
        return 0;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (QUERY_CODE == mUriMatcher.match(uri)){
            SQLiteDatabase db = helper.getWritableDatabase();
            return db.query("account", projection, selection, selectionArgs,
                    null, null, sortOrder);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
