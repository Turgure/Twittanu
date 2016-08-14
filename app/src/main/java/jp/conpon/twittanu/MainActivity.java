package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import twitter4j.auth.AccessToken;

/**
 * Created by Shigure on 2016/08/12.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccessToken accessToken = TwitterUtils.loadAccessToken(this);
        if (accessToken == null) {
            Intent intent = new Intent(getApplicationContext(), TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
