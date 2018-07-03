package cn.novate.android_day03.simple1;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.novate.android_day03.R;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/7/3 7:53
 * Version 1.0
 * Params:
 * Description:
*/

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountSQliteOpenHelper helper = new AccountSQliteOpenHelper(this, "account.db", null, 1) ;
        helper.getWritableDatabase() ;

    }
}
