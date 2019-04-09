package jp.conpon.twittanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by mirage-residence on 2019/04/08.
 */
public class UserIconView extends AppCompatImageView {
    private int canvasSize;
    private String url = null;
    private Bitmap bitmap = null;
    private Paint paint;

    public UserIconView(Context context) {
        super(context);
    }

    public UserIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * アイコン画像のURLを設定する。
     *
     * @param urlStr アイコン画像のURL
     */
    public void setImageUrl(final String urlStr) {
        if ((urlStr != null) && !urlStr.equals(url)) { // 新規URL
            url = urlStr;
            bitmap = null;
            paint = new Paint();

            SetImageTask task = new SetImageTask(this);
            task.execute(url);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((getWidth() == 0) || (getHeight() == 0)) {
            return;
        }

        if (bitmap == null) {
            super.onDraw(canvas);
            return;
        }

        Bitmap image = getSquareBitmap(bitmap);

        canvasSize = Math.min(getWidth(), getHeight());

        paint.setShader(getShader(image));
        paint.setAntiAlias(true);

        int circleCenter = canvasSize / 2;
        canvas.drawCircle(circleCenter, circleCenter, circleCenter, paint);
    }

    private Bitmap getSquareBitmap(Bitmap srcBmp) {
        if (srcBmp.getWidth() == srcBmp.getHeight()) {
            return srcBmp;
        }

        //Rectangle to square. Equivalent to ScaleType.CENTER_CROP
        int dim = Math.min(srcBmp.getWidth(), srcBmp.getHeight());
        return Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);
    }

    private BitmapShader getShader(final Bitmap image) {
        return new BitmapShader(Bitmap.createScaledBitmap(image, canvasSize, canvasSize, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    /**
     * 画像配置用AsyncTask
     */
    static class SetImageTask extends AsyncTask<String, Void, Boolean> {
        private WeakReference<UserIconView> viewRef;

        SetImageTask(final UserIconView userIconView) {
            viewRef = new WeakReference<>(userIconView);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Bitmap bitmap;

            try {
                InputStream inputStream;
                inputStream = new URL(strings[0]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            UserIconView view = viewRef.get();
            if (view != null) {
                view.bitmap = bitmap;
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            UserIconView view = viewRef.get();
            if (view != null) {
                if (success != null) {
                    view.invalidate();
                }
            }
        }
    }
}
