package jp.conpon.twittanu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mirage-residence on 2017/01/25.
 */
public class TimelineItemLayout extends RecyclerView.ViewHolder {
    public UrlImageView userIcon; // アイコン画像
    public TextView userName; // ユーザー名 @ユーザーID
    public TextView content; // ツイート本文
    public TextView createdTime; // ツイート日時
    public TextView retweetCount; // RT数
    public TextView favCount; // ふぁぼ数

    public TimelineItemLayout(View itemView) {
        super(itemView);
        userIcon = (UrlImageView) itemView.findViewById(R.id.user_icon);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        content = (TextView) itemView.findViewById(R.id.content);
        createdTime = (TextView) itemView.findViewById(R.id.created_time);
        retweetCount = (TextView) itemView.findViewById(R.id.retweet_count);
        favCount = (TextView) itemView.findViewById(R.id.fav_count);
    }
}
