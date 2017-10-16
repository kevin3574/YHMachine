package kzy.com.gyyengineer.leanchat.friends;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import kzy.com.gyyengineer.App;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.leanchat.activity.AVBaseActivity;
import kzy.com.gyyengineer.leanchat.adapter.HeaderListAdapter;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.util.Constants;
import kzy.com.gyyengineer.leanchat.util.UserCacheUtils;
import kzy.com.gyyengineer.leanchat.view.RefreshableRecyclerView;
import kzy.com.gyyengineer.leanchat.viewholder.SearchUserItemHolder;

/**
 * 查找好友页面
 */
public class ContactAddFriendActivity extends AVBaseActivity {

    @Bind(R.id.search_user_rv_layout)
    protected RefreshableRecyclerView recyclerView;

    @Bind(R.id.searchNameEdit)
    EditText searchNameEdit;

    private HeaderListAdapter<LeanchatUser> adapter;
    private String searchName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add_friend_activity);
        init();
        recyclerView.refreshData();
    }

    private void init() {
        setTitle(App.ctx.getString(R.string.contact_findFriends));
        adapter = new HeaderListAdapter<>(SearchUserItemHolder.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnLoadDataListener(new RefreshableRecyclerView.OnLoadDataListener() {
            @Override
            public void onLoad(int skip, int limit, boolean isRefresh) {
                loadMoreFriend(skip, limit, isRefresh);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadMoreFriend(int skip, final int limit, final boolean isRefresh) {
        AVQuery<LeanchatUser> q = LeanchatUser.getQuery(LeanchatUser.class);
        q.whereContains(LeanchatUser.USERNAME, searchName);
        q.limit(Constants.PAGE_SIZE);
        q.skip(skip);
        LeanchatUser user = LeanchatUser.getCurrentUser();
        List<String> friendIds = new ArrayList<String>(FriendsManager.getFriendIds());
        friendIds.add(user.getObjectId());
        q.whereNotContainedIn(Constants.OBJECT_ID, friendIds);
        q.orderByDescending(Constants.UPDATED_AT);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(new FindCallback<LeanchatUser>() {
            @Override
            public void done(List<LeanchatUser> list, AVException e) {
                UserCacheUtils.cacheUsers(list);
                if (list != null) {
                    recyclerView.setLoadComplete(list.toArray(), false);
                }
            }
        });
    }

    @OnClick(R.id.searchBtn)
    public void search(View view) {
        searchName = searchNameEdit.getText().toString();
        recyclerView.refreshData();
    }
}
