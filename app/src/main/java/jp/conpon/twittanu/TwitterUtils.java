package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by Shigure on 2016/08/13.
 */
public class TwitterUtils {
    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";
    private static final String PREF_NAME = "twitter_access_token";

    private static AccessToken accessToken;
    private static Twitter twitter;
    private static Activity activity;

    /**
     * コンストラクタ
     * ツイッター情報を取得します。
     *
     * @param activity
     */
    public TwitterUtils(Activity activity) {
        this.activity = activity;
        accessToken = loadAccessToken(activity.getApplicationContext());

        if (accessToken != null) {
            twitter = getTwitterInstance(activity.getApplicationContext());
        }
    }

    /**
     * Twitterインスタンスを取得します。アクセストークンが保存されていれば自動的にセットします。
     *
     * @param context
     * @return
     */
    public static Twitter getTwitterInstance(Context context) {
        String consumerKey = context.getString(R.string.api_key);
        String consumerSecret = context.getString(R.string.api_secret);

        TwitterFactory factory = new TwitterFactory();
        twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        AccessToken accessToken = loadAccessToken(context);
        if (accessToken != null) {
            twitter.setOAuthAccessToken(accessToken);
        }
        return twitter;
    }

    /**
     * アクセストークンをプリファレンスに保存します。
     *
     * @param context
     * @param accessToken
     */
    public static void storeAccessToken(Context context, AccessToken accessToken) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, accessToken.getToken());
        editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());
        editor.apply();
    }

    /**
     * アクセストークンをプリファレンスから読み込みます。
     *
     * @param context
     * @return
     */
    public static AccessToken loadAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if (token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }

    /**
     * ログインしているかを確認します。
     *
     * @return
     */
    public static boolean isLogined() {
        return accessToken != null;
    }

    /**
     * 文章をツイートします。
     *
     * @param str
     * @return
     */
    public static void tweet(final String str) {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... strings) {
                twitter4j.Status status = null;
                try {
                    status = twitter.updateStatus(str);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        task.execute();
    }
}
