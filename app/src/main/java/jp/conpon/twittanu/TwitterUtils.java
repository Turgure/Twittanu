package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import static jp.conpon.twittanu.BaseActivity.context;

/**
 * Created by Shigure on 2016/08/13.
 */
public class TwitterUtils {
    private static String callbackURL;

    private static TwitterUtils twitterUtils = new TwitterUtils();

    private static AccessToken accessToken;
    private static RequestToken requestToken;
    private static Twitter twitter;

    /**
     * コンストラクタ
     * ツイッター情報を取得
     */
    private TwitterUtils() {
        callbackURL = context.getString(R.string.twitter_callback_url);

        initTwitterInstance();
        loadAccessToken();
        if (accessToken != null) {
            login();
        }
    }

    /**
     * インスタンス取得
     *
     * @return
     */
    public static TwitterUtils getInstance() {
        return twitterUtils;
    }

    /**
     * OAuth認証によるログイン
     *
     * @return
     */
    public void startAuthorize(final Activity activity) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    twitter.setOAuthAccessToken(null);
                    if (requestToken == null) {
                        requestToken = twitter.getOAuthRequestToken(callbackURL);
                    }
                    return requestToken.getAuthorizationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                    activity.startActivity(intent);
                }
            }
        };
        task.execute();
    }

    public void callback(final Activity activity, Intent intent) {
        if (intent == null || intent.getData() == null || !intent.getData().toString().startsWith(callbackURL)) {
            return;
        }
        String verifier = intent.getData().getQueryParameter("oauth_verifier");

        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(String... strings) {
                try {
                    accessToken = twitter.getOAuthAccessToken(requestToken, strings[0]);
                    return accessToken;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {
                if (accessToken != null) {
                    showToast("認証成功！");
                    successOAuth(activity, accessToken);
                } else {
                    showToast("認証失敗。。。");
                }
            }
        };
        task.execute(verifier);
    }

    /**
     * ツイート！
     *
     * @param str
     * @return
     */
    public void tweet(final String str) {
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

    /**
     * ログインしているかを確認
     *
     * @return
     */
    public boolean isLoggedIn() {
        return accessToken != null;
    }

    /**
     * Twitterインスタンス初期化
     *
     * @return
     */
    private void initTwitterInstance() {
        String consumerKey = context.getString(R.string.api_key);
        String consumerSecret = context.getString(R.string.api_secret);

        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    /**
     * ログイン
     */
    private void login() {
        twitter.setOAuthAccessToken(accessToken);
        showToast("ログインしました！");
    }

    /**
     * OAuth認証が成功した後MainActivityに戻る
     *
     * @param accessToken
     */
    private void successOAuth(Activity activity, AccessToken accessToken) {
        storeAccessToken(accessToken);
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * アクセストークンをプリファレンスに保存
     *
     * @param accessToken
     */
    private void storeAccessToken(AccessToken accessToken) {
        MyPreference.getInstance().put(MyPreference.TWITTER_ACCESS_TOKEN, accessToken.getToken());
        MyPreference.getInstance().put(MyPreference.TWITTER_ACCESS_TOKEN_SECRET, accessToken.getTokenSecret());
    }

    /**
     * アクセストークンをプリファレンスから読み込み
     *
     * @return
     */
    private void loadAccessToken() {
        String token = MyPreference.getInstance().get(MyPreference.TWITTER_ACCESS_TOKEN, null);
        String tokenSecret = MyPreference.getInstance().get(MyPreference.TWITTER_ACCESS_TOKEN_SECRET, null);
        if (token != null && tokenSecret != null) {
            accessToken = new AccessToken(token, tokenSecret);
        } else {
            accessToken = null;
        }
    }

    /**
     * toastの生成
     *
     * @param text
     */
    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
