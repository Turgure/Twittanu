package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by shigure on 2016/11/19.
 */
public class TweetActivity extends Activity {
    private static final int REQUEST_GALLEY = 0;

    private EditText tweetContent;
    private Button tweetBtn;
    private Button imageBtn;
    private ImageView image;
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetContent = (EditText) findViewById(R.id.tweet_content);
        tweetBtn = (Button) findViewById(R.id.tweet_button);
        imageBtn = (Button) findViewById(R.id.tweet_image_button);
        image = (ImageView) findViewById(R.id.tweet_image);

        tweetContent.addTextChangedListener(watcher);

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterManager.INSTANCE.tweet(tweetContent.getText().toString(), imagePath);
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

        if(resultCode == RESULT_OK && requestCode == REQUEST_GALLEY){
            try{
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(data.getData(), projection, null, null, null);
                if(c.moveToFirst()) {
                    imagePath = c.getString(0);
                }

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                double scale = Resources.getSystem().getDisplayMetrics().widthPixels / 4.0 / bitmap.getWidth();
                bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * scale), (int)(bitmap.getHeight() * scale), true);
                image.setImageBitmap(bitmap);
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
