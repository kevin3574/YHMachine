package kzy.com.gyyengineer.engineer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;

import java.util.List;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.engineer.activity.MessageActivity;
import kzy.com.gyyengineer.leanchat.activity.MainActivity;
import kzy.com.gyyengineer.leanchat.event.ImTypeMessageEvent;
import kzy.com.gyyengineer.leanchat.fragment.DiscoverFragment;
import kzy.com.gyyengineer.leanchat.model.Room;
import kzy.com.gyyengineer.leanchat.service.ConversationManager;


/**
 * 创建日期：2017/6/20 0020 on 11:06
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class HomeFragment extends Fragment {

    private View view;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private MainActivity mainactivity;
    private FragmentManager fm;
    private ImageView iv_msg;
    private FrameLayout fl_message;
    private TextView countView;
    private ConversationManager conversationManager = ConversationManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fargment_home_layout, null);

        initViews();
        initListeners();
        setDefaultFragment();
        updateCount();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCount();
    }

    public void onEvent(ImTypeMessageEvent event) {
        updateCount();
    }

    public void updateCount() {
        conversationManager.findAndCacheRooms(new Room.MultiRoomsCallback() {
            @Override
            public void done(List<Room> roomList, AVException exception) {
                if (exception == null) {
                    int count = 0;
                    for (Room room : roomList)
                        count += room.getUnreadCount();
                    Log.e("HomeFragment", "---count---" + count);
                    if (count > 0) {
                        countView.setVisibility(View.VISIBLE);
                        countView.setText("" + count);
                    } else {
                        countView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

    }

    private void initViews() {
        mainactivity = (MainActivity) getActivity();
        fm = mainactivity.getSupportFragmentManager();


        rg = (RadioGroup) view.findViewById(R.id.rg);
        rb1 = (RadioButton) view.findViewById(R.id.rb1);
        rb2 = (RadioButton) view.findViewById(R.id.rb2);
        iv_msg = (ImageView) view.findViewById(R.id.iv_msg);
        fl_message = (FrameLayout) view.findViewById(R.id.fl_message);
        countView = (TextView) view.findViewById(R.id.countView);
    }

    private void initListeners() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        CurrentFragment mCurrentFragment = new CurrentFragment();
                        FragmentTransaction ft1 = fm.beginTransaction();
                        ft1.replace(R.id.home_fl, mCurrentFragment);
                        ft1.commit();
                        rb1.setSelected(true);
                        rb2.setSelected(false);
                        break;
                    case R.id.rb2:
                        HistoryFragment mHistoryFragment = new HistoryFragment();
                        FragmentTransaction ft2 = fm.beginTransaction();
                        ft2.replace(R.id.home_fl, mHistoryFragment);
                        ft2.commit();
                        rb1.setSelected(false);
                        rb2.setSelected(true);
                        break;
                }
            }
        });
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverFragment mCurrentFragment = new DiscoverFragment();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.home_fl, mCurrentFragment);
                fragmentTransaction.commit();
                rb1.setSelected(true);
            }
        });
        fl_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


    // 设置默认Fragment
    private void setDefaultFragment() {
        CurrentFragment mCurrentFragment = new CurrentFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.home_fl, mCurrentFragment);
        fragmentTransaction.commit();
        rb1.setSelected(true);
        rb2.setSelected(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
