package jp.conpon.twittanu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import twitter4j.Status;
import twitter4j.User;

/**
 * Created by mirage-residence on 2017/01/10.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineItemLayout> {
    @NonNull
    private List<Status> statuses;

    public TimelineAdapter() {
        statuses = new ArrayList<>();
    }

    @Override
    public TimelineItemLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_timeline_item, parent, false);
        return new TimelineItemLayout(itemView);
    }

    @Override
    public void onBindViewHolder(TimelineItemLayout holder, int position) {
        Status status = statuses.get(position);

        if (status != null) {
            User user = status.getUser();
            holder.userIcon.setImageUrl(user.getProfileImageURL());

            StringBuilder builder = new StringBuilder();
            builder.append(user.getName());
            builder.append(" @");
            builder.append(user.getScreenName());
            holder.userName.setText(builder);

            holder.content.setText(status.getText());
            holder.createdTime.setText(getCreatedTimeString(status.getCreatedAt()));
            holder.retweetCount.setText(String.valueOf(status.getRetweetCount()));
            holder.favCount.setText(String.valueOf(status.getFavoriteCount()));
        }
    }

    private CharSequence getCreatedTimeString(final Date createdAt) {
        return DateFormat.format("H:mm - yyyy年M月d日", createdAt);
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    /**
     * 一覧にあるツイートをすべて追加する
     *
     * @param collection ツイートの一覧
     */
    public void addAll(@NonNull final Collection<Status> collection) {
        statuses.addAll(collection);
    }

    /**
     * ツイートをすべて削除する
     */
    public void clear() {
        statuses.clear();
    }
}
