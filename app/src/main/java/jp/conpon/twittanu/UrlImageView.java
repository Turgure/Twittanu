package jp.conpon.twittanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by shigure on 2017/03/06.
 */

public class UrlImageView extends android.support.v7.widget.AppCompatImageView {
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
            SetImageTask task = new SetImageTask(this);
            task.execute(url, String.valueOf(scale));
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

    /**
     * 画像配置用AsyncTask
     */
    static class SetImageTask extends AsyncTask<String, Void, Double> {
        private WeakReference<UrlImageView> urlImageViewWeakReference;

        SetImageTask(UrlImageView urlImageView){
            urlImageViewWeakReference = new WeakReference<>(urlImageView);
        }

        @Override
        protected Double doInBackground(String... strings) {
            InputStream inputStream;
            try {
                inputStream = new URL(strings[0]).openStream();
                urlImageViewWeakReference.get().bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return Double.parseDouble(strings[1]);
            } catch (Exception e) {
                e.printStackTrace();
                return 1.0;
            }
        }

        @Override
        protected void onPostExecute(Double scale){
            urlImageViewWeakReference.get().changeScale(scale);
        }
    }
}
