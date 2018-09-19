package jp.conpon.twittanu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import twitter4j.StatusUpdate;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import static jp.conpon.twittanu.BaseActivity.context;

/**
 * Created by Shigure on 2016/08/13.
 */
public enum TwitterManager {
    INSTANCE;
    private String callbackURL;
    private AccessToken accessToken;
    private RequestToken requestToken;
    private Twitter twitter;

    /**
     * コンストラクタ
     * ツイッター情報を取得
     */
    private TwitterManager() {
        callbackURL = context.getString(R.string.twitter_callback_url);

        initTwitterInstance();
        loadAccessToken();
        if (accessToken != null) {
            login();
        }
    }

    /**
     * OAuth認証によるログイン
     */
    public void startAuthorize(final Activity activity) {
        OAuthRequestTask task = new OAuthRequestTask(activity);
        task.execute();
    }

    public void callback(final Activity activity, Intent intent) {
        if (intent == null || intent.getData() == null || !intent.getData().toString().startsWith(callbackURL)) {
            return;
        }
        String verifier = intent.getData().getQueryParameter("oauth_verifier");

        AuthorizeTask task = new AuthorizeTask(activity);
        task.execute(verifier);
    }

	public ResponseList<twitter4j.Status> getHomeTimeline() {
		ResponseList<twitter4j.Status> list = null;

		try {
			list = twitter.getHomeTimeline();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		return list;
	}

    /**
     * ツイート！
     *
     * @param str
     * @return
     */
    public void tweet(final String str, final UrlImageView[] imageViews) {
        TweetTask task = new TweetTask(str, imageViews);
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
        showToast(R.string.login_message);
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
        MyPreference.INSTANCE.put(MyPreference.TWITTER_ACCESS_TOKEN, accessToken.getToken());
        MyPreference.INSTANCE.put(MyPreference.TWITTER_ACCESS_TOKEN_SECRET, accessToken.getTokenSecret());
    }

    /**
     * アクセストークンをプリファレンスから読み込み
     *
     * @return
     */
    private void loadAccessToken() {
        String token = MyPreference.INSTANCE.get(MyPreference.TWITTER_ACCESS_TOKEN, null);
        String tokenSecret = MyPreference.INSTANCE.get(MyPreference.TWITTER_ACCESS_TOKEN_SECRET, null);
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

    /**
     * toastの生成(resource idから文字列取得)
     *
     * @param id
     */
    private void showToast(int id) {
        String text = context.getResources().getString(id);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * OAuth認証用のAsyncTask
     */
    static class OAuthRequestTask extends AsyncTask<Void, Void, String> {
        private WeakReference<Activity> activity;

        OAuthRequestTask(Activity activity){
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                INSTANCE.twitter.setOAuthAccessToken(null);
                INSTANCE.requestToken = TwitterManager.INSTANCE.twitter.getOAuthRequestToken(INSTANCE.callbackURL);
                return INSTANCE.requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                activity.get().startActivity(intent);
            }
        }
    }

    /**
     * 認証実施用のAsyncTask
     */
    static class AuthorizeTask extends AsyncTask<String, Void, AccessToken> {
        private WeakReference<Activity> activity;

        AuthorizeTask(Activity activity){
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected AccessToken doInBackground(String... strings) {
            try {
                INSTANCE.accessToken = INSTANCE.twitter.getOAuthAccessToken(INSTANCE.requestToken, strings[0]);
                return INSTANCE.accessToken;
            } catch (TwitterException | NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AccessToken accessToken) {
            if (accessToken != null) {
                INSTANCE.showToast(R.string.success_authorize);
                INSTANCE.successOAuth(activity.get(), accessToken);
            } else {
                INSTANCE.showToast(R.string.failure_authorize);
            }
        }
    }

    /**
     * tweet用のAsyncTask
     */
    static class TweetTask extends AsyncTask<String, Void, Boolean> {
        private String text;
        private UrlImageView[] urlImageViews;

        TweetTask(final String text, final UrlImageView[] urlImageViews){
            this.text = text;
            this.urlImageViews = urlImageViews;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            twitter4j.Status status = null;
            StatusUpdate statusUpdate = new StatusUpdate(text);

            try {
                // 画像の添付
                ArrayList<Long> mediaIds = new ArrayList<>();
                UploadedMedia media;
                for(UrlImageView imageView : urlImageViews) {
                    if (imageView.getUrl() != null) {
                        media = INSTANCE.twitter.uploadMedia(new File(imageView.getUrl()));
                        mediaIds.add(media.getMediaId());
                    }
                }
                long[] arrayMediaIds = new long[mediaIds.size()];
                for(int i = 0; i < arrayMediaIds.length; ++i){
                    arrayMediaIds[i] = mediaIds.get(i);
                }
                statusUpdate.setMediaIds(arrayMediaIds);

                status = INSTANCE.twitter.updateStatus(statusUpdate);
                return true;
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                INSTANCE.showToast(R.string.send_tweet);
            }
            else{
                INSTANCE.showToast(R.string.failure_tweet);
            }
        }
    };
}
