package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by shigure on 2016/11/19.
 */
public class TweetActivity extends Activity {
    private static final int REQUEST_GALLEY = 0;

    private EditText tweetContent;
    private Button tweetBtn;
    private Button imageBtn;
    private UrlImageView[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetContent = (EditText) findViewById(R.id.tweet_content);
        tweetBtn = (Button) findViewById(R.id.tweet_button);
        imageBtn = (Button) findViewById(R.id.tweet_image_button);
        images = new UrlImageView[4];
        for (int i = 0; i < 4; ++i) {
            images[i] = (UrlImageView) findViewById(getResources().getIdentifier("tweet_image" + (i + 1), "id", getPackageName()));
        }

        tweetContent.addTextChangedListener(watcher);

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterManager.INSTANCE.tweet(tweetContent.getText().toString(), images);
                finish();
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLEY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLEY) {
            try {
                String path;
                Cursor c = this.getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (c.moveToFirst()) {
                    path = c.getString(0);
                    for (UrlImageView image : images) {
                        if (image.getUrl() == null) {
                            image.setImage(path);
                            image.changeScale(Resources.getSystem().getDisplayMetrics().widthPixels / 4.0 / image.getBitmap().getWidth());
                            break;
                        }
                    }
                    tweetBtn.setEnabled(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            if (images[0].getUrl() == null) {
                if (tweetBtn.isEnabled()) {
                    if (s.length() == 0) {
                        tweetBtn.setEnabled(false);
                    }
                } else {
                    if (s.length() > 0) {
                        tweetBtn.setEnabled(true);
                    }
                }
            } else {
                tweetBtn.setEnabled(true);
            }
        }
    };
}
