package jp.conpon.twittanu;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by shigure on 2016/11/19.
 */
public class TweetActivity extends Activity {
    private EditText tweetContent;
    private Button tweetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetContent = (EditText) findViewById(R.id.tweet_content);
        tweetBtn = (Button) findViewById(R.id.tweet_button);

        tweetContent.addTextChangedListener(watcher);

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterManager.INSTANCE.tweet(tweetContent.getText().toString());
                Toast.makeText(getApplicationContext(), "ツイートしたよ！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (tweetBtn.isEnabled()) {
                if (s.length() == 0) {
                    tweetBtn.setEnabled(false);
                }
            } else {
                if (s.length() > 0) {
                    tweetBtn.setEnabled(true);
                }
            }
        }
    };
}
