package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by shigure on 2016/11/19.
 */
public class TweetActivity extends Activity {
    private static final int REQUEST_GALLEY = 0;

    private EditText tweetContent;
    private Button tweetBtn;
    private Button imageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetContent = (EditText) findViewById(R.id.tweet_content);
        tweetBtn = (Button) findViewById(R.id.tweet_button);
        imageBtn = (Button) findViewById(R.id.tweet_image);

        tweetContent.addTextChangedListener(watcher);

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterManager.INSTANCE.tweet(tweetContent.getText().toString());
                Toast.makeText(getApplicationContext(), "ツイートしたよ！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivity(intent);
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
