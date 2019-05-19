package jp.conpon.twittanu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by shigure on 2017/03/06.
 */

public class UrlImageView extends AppCompatImageView {
    private String url = null;
    private Bitmap bitmap = null;

    public UrlImageView(Context context) {
        super(context);
    }

    public UrlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImage(@NonNull final String url) {
        this.url = url;

        if(url.startsWith("http")) {
            SetImageTask task = new SetImageTask(this);
            task.execute(url);
        }
        else {
            this.bitmap = BitmapFactory.decodeFile(url);
        }

        this.bitmap = this.getSquareBitmap(this.bitmap);
        setImageBitmap(this.bitmap);
    }

    public String getUrl() {
        return this.url;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    private Bitmap getSquareBitmap(@NonNull Bitmap bitmap) {
        if(bitmap.getWidth() == bitmap.getHeight()) {
            return bitmap;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int displaySize = Resources.getSystem().getDisplayMetrics().widthPixels / 4;
        float scale;
        if(width > height) {
            scale = (float)displaySize / height;
        }
        else {
            scale = (float)displaySize / width;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(width * scale), (int)(height * scale), true);

        int x = (bitmap.getWidth() - displaySize) >= 0 ? (bitmap.getWidth() - displaySize) / 2 : 0;
        int y = (bitmap.getHeight() - displaySize) >= 0 ? (bitmap.getHeight() - displaySize) / 2 : 0;
        bitmap = Bitmap.createBitmap(bitmap, x, y, displaySize, displaySize);

        return bitmap;
    }

    /**
     * 画像配置用AsyncTask
     */
    static class SetImageTask extends AsyncTask<String, Void, Boolean> {
        private WeakReference<UrlImageView> urlImageViewWeakReference;

        SetImageTask(UrlImageView urlImageView){
            urlImageViewWeakReference = new WeakReference<>(urlImageView);
            }

        @Override
        protected Boolean doInBackground(String... strings) {
            InputStream inputStream;
            try {
                inputStream = new URL(strings[0]).openStream();
                urlImageViewWeakReference.get().bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
