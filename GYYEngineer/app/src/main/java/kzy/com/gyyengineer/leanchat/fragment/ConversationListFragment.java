package kzy.com.gyyengineer.leanchat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.leanchat.view.HeaderLayout;

/**
 * Created by wli on 16/3/29.
 */
public class ConversationListFragment extends LCIMConversationListFragment {

  protected HeaderLayout headerLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
    headerLayout.showTitle(R.string.conversation_messages);
    return view;
  }
}
