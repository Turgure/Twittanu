package jp.conpon.twittanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private String url;
    private Bitmap bitmap;

    public UrlImageView(Context context) {
        super(context);
        this.url = url;
    }

    public UrlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.url = url;
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.url = url;
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.url = url;
    }

    public void setImage(@NonNull String url) {
        setImage(url, 1.0);
    }

    public void setImage(@NonNull String url, double scale) {
        if (!url.isEmpty()) {
            if (url.startsWith("http")) {
                InputStream istream = null;
                try {
                    istream = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(istream);
                    changeScale(scale);
                    istream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeFile(url);
                changeScale(scale);
            }
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
