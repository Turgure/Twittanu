package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by shigure on 2016/11/23.
 */
public abstract class BaseActivity extends Activity {
    protected static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = super.getApplicationContext();
    }

    /**
     * 共通contextの取得
     *
     * @return get common context
     */
    @Override
    public Context getApplicationContext() {
        return context;
    }
}
