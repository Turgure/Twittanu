package jp.conpon.twittanu;

import android.content.Context;
import android.content.SharedPreferences;

import static jp.conpon.twittanu.BaseActivity.context;

/**
 * アプリの設定保存クラス
 * Created by shigure on 2016/11/22.
 */
public enum MyPreference {
    INSTANCE;

    private SharedPreferences sharedPreferences;

    // keys
    public static final String PREF_NAME = "user_data";
    public static final String TWITTER_ACCESS_TOKEN = "TWITTER_ACCESS_TOKEN";
    public static final String TWITTER_ACCESS_TOKEN_SECRET = "TWITTER_ACCESS_TOKEN_SECRET";

    /**
     * コンストラクタ
     */
    private MyPreference() {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, int value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, float value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putFloat(key, value);
        edit.apply();
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    /**
     * @param key
     * @param defvalue
     * @return キーが存在しないときデフォルト値を返す
     */
    public int get(String key, int defvalue) {
        return sharedPreferences.getInt(key, defvalue);
    }

    /**
     * @param key
     * @param defvalue
     * @return キーが存在しないときデフォルト値を返す
     */
    public float get(String key, float defvalue) {
        return sharedPreferences.getFloat(key, defvalue);
    }

    /**
     * @param key
     * @param defvalue
     * @return キーが存在しないときデフォルト値を返す
     */
    public boolean get(String key, boolean defvalue) {
        return sharedPreferences.getBoolean(key, defvalue);
    }

    /**
     * @param key
     * @param defvalue
     * @return キーが存在しないときデフォルト値を返す
     */
    public String get(String key, String defvalue) {
        return sharedPreferences.getString(key, defvalue);
    }
}
