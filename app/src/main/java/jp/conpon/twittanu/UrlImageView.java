package jp.conpon.twittanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by shigure on 2017/03/06.
 */
public class UrlImageView extends AppCompatImageView {
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

    public void setImage(@NonNull String url) {
        setImage(url, 1.0);
    }

    public void setImage(@NonNull String url, double scale) {
        this.url = url;
        if (url.startsWith("http")) {
            InputStream inputStream = null;
            try {
                inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                changeScale(scale);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
