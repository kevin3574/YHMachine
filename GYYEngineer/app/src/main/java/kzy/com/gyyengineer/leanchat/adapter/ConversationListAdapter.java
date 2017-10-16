package kzy.com.gyyengineer.leanchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.controller.ConversationHelper;
import kzy.com.gyyengineer.leanchat.controller.MessageHelper;
import kzy.com.gyyengineer.leanchat.event.ConversationItemClickEvent;
import kzy.com.gyyengineer.leanchat.model.ConversationType;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.model.Room;
import kzy.com.gyyengineer.leanchat.service.CacheService;
import kzy.com.gyyengineer.leanchat.service.ConversationManager;
import kzy.com.gyyengineer.leanchat.util.PhotoUtils;


/**
 * Created by wli on 15/10/8.
 */
public class ConversationListAdapter extends ArrayAdapter<Room> {
    Context ctx;

    public ConversationListAdapter(Context context) {
        super(context, 0);
        this.ctx = context;
    }


    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        convertView = LayoutInflater.from(ctx).inflate(R.layout.conversation_item,
                null);

        vh.recentAvatarView = (ImageView) convertView
                .findViewById(R.id.iv_recent_avatar);
        vh.recentNameView = (TextView) convertView
                .findViewById(R.id.recent_time_text);
        vh.recentMsgView = (TextView) convertView
                .findViewById(R.id.recent_msg_text);
        vh.recentTimeView = (TextView) convertView
                .findViewById(R.id.recent_teim_text);
        vh.recentUnreadView = (TextView) convertView
                .findViewById(R.id.recent_unread);


        final Room room = getItem(position);
        AVIMConversation conversation = room.getConversation();

        if (ConversationType.Doctor.getValue() == Integer.parseInt(conversation.getAttribute(ConversationType.TYPE_KEY).toString()) && room.getUnreadCount() < 1) {
            if (null != room.getLastMessage() && (new Date().getTime() - room.getLastMessage().getTimestamp() > 60 * 60 * 1000)) {
                ChatManager.getInstance().getRoomsTable()
                        .deleteRoom(room.getConversationId());
            }
        }

        if (null != conversation) {


            if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single || ConversationHelper.typeOfConversation(conversation) == ConversationType.Doctor) {
                LeanchatUser user = (LeanchatUser) CacheService
                        .lookupUser(ConversationHelper.otherIdOfConversation(conversation));
                if (null != user) {
                    ImageLoader.getInstance().displayImage(user.getAvatarUrl(),
                            vh.recentAvatarView, PhotoUtils.avatarImageOptions);

        //            String name = (String) user.get("realName");
                    String name = (String) user.getUsername();
                    if ("".equals(name)) {
                        vh.recentNameView.setText(ConversationHelper
                                .nameOfConversation(conversation));
                    } else {
                        vh.recentNameView.setText(name);
                    }

                }

            } else {
                vh.recentAvatarView.setImageBitmap(ConversationManager
                        .getConversationIcon(conversation));
            }

            int num = room.getUnreadCount();
            if (num > 0) {
                vh.recentUnreadView.setVisibility(View.VISIBLE);
                vh.recentUnreadView.setText(num + "");
            } else {
                vh.recentUnreadView.setVisibility(View.GONE);
            }

            if (room.getLastMessage() != null) {
                Date date = new Date(room.getLastMessage().getTimestamp());
                SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                vh.recentTimeView.setText(format.format(date));
                vh.recentMsgView.setText(MessageHelper
                        .outlineOfMsg((AVIMTypedMessage) room.getLastMessage()));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ConversationItemClickEvent itemClickEvent = new ConversationItemClickEvent();
                    itemClickEvent.conversationId = room.getConversationId();
                    EventBus.getDefault().post(itemClickEvent);
                }
                // }
            });
        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView recentAvatarView;
        TextView recentNameView;
        TextView recentMsgView;
        TextView recentTimeView;
        TextView recentUnreadView;
    }
}
