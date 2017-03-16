package jp.conpon.twittanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by shigure on 2017/03/06.
 */

public class UrlImageView extends ImageView {
    private String url = null;
    private Bitmap bitmap;

    public UrlImageView(Context context) {
        super(context);
    }

    public UrlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setImage(@NonNull String url) {
        setImage(url, 1.0);
    }

    public void setImage(@NonNull String url, double scale) {
        this.url = url;
        if (url.startsWith("http")) {
            AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... strings) {
                    InputStream inputStream = null;
                    try {
                        inputStream = new URL(strings[0]).openStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        changeScale(Double.parseDouble(strings[1]));
                        inputStream.close();
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            };
            task.execute(new String[]{url, String.valueOf(scale)});
        } else {
            bitmap = BitmapFactory.decodeFile(url);
            changeScale(scale);
        }
    }

    public void changeScale(double scale) {
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), true);
        setImageBitmap(bitmap);
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
