package jp.conpon.twittanu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shigure on 2016/11/19.
 */
public class TweetActivity extends Activity {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_GALLERY = 0;

    private static final int MAX_TWEET_IMAGE_NUM = 4;

    private EditText tweetContent;
    private Button tweetBtn;
    private Button imageBtn;
    private ArrayList<UrlImageView> tweetImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetContent = findViewById(R.id.tweet_content);
        tweetBtn = findViewById(R.id.tweet_button);
        imageBtn = findViewById(R.id.tweet_image_button);
        tweetImages = new ArrayList<>();

        tweetContent.addTextChangedListener(watcher);

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterManager.INSTANCE.tweet(tweetContent.getText().toString(), tweetImages);
                finish();
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(TweetActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            TweetActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    return;
                }

                if(tweetImages.size() >= MAX_TWEET_IMAGE_NUM) {
                    Toast.makeText(TweetActivity.this, R.string.append_image_limit_message, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch(requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageBtn.performClick();
                }
                else {
                    Toast.makeText(TweetActivity.this, R.string.permission_off_storage, Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            Uri uri = data.getData();
            if(uri != null) {
                try (Cursor c = this.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null)) {
                    String path;
                    if (c != null && c.moveToFirst()) {
                        path = c.getString(0);
                        appendImage(path);
                        tweetBtn.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            if (tweetImages.size() == 0) {
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

    private void appendImage(@NonNull final String path) {
        GridLayout gridLayout = findViewById(R.id.tweet_images);
        if(gridLayout.getChildCount() < MAX_TWEET_IMAGE_NUM) {
            UrlImageView urlImageView = new UrlImageView(TweetActivity.this);
            urlImageView.setImage(path);

            gridLayout.addView(urlImageView);
            tweetImages.add(urlImageView);
        }
    }
}
