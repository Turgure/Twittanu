package jp.conpon.twittanu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by shigure on 2016/11/19.
 */
public class TweetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        findViewById(R.id.tweet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterUtils.tweet(((EditText) findViewById(R.id.tweet_content)).getText().toString());
                finish();
            }
        });
    }
}
