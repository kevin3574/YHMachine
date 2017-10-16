package kzy.com.gyyengineer.leanchat;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.leanchat.activity.AVBaseActivity;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.controller.ConversationHelper;
import kzy.com.gyyengineer.leanchat.controller.MessageAgent;
import kzy.com.gyyengineer.leanchat.controller.MessageHelper;
import kzy.com.gyyengineer.leanchat.event.EmptyEvent;
import kzy.com.gyyengineer.leanchat.event.ImTypeMessageEvent;
import kzy.com.gyyengineer.leanchat.model.ConversationType;
import kzy.com.gyyengineer.leanchat.util.Constants;
import kzy.com.gyyengineer.leanchat.util.NotificationUtils;
import kzy.com.gyyengineer.leanchat.util.PathUtils;

/**
 * Created by cc112837@163.com on 16/5/18.
 */
public class ChatRoomActivity2 extends AVBaseActivity {
	public static final int LOCATION_REQUEST = 100;
	public static final int QUIT_GROUP_REQUEST = 200;
	private static final int TAKE_CAMERA_REQUEST = 2;
	private static final int GALLERY_REQUEST = 0;
	private static final int GALLERY_KITKAT_REQUEST = 3;


	protected MessageAgent messageAgent;

	protected MultipleItemAdapter itemAdapter;
	protected RecyclerView recyclerView;
	protected LinearLayoutManager layoutManager;
	protected SwipeRefreshLayout refreshLayout;
	protected InputBottomBar inputBottomBar;

	protected String localCameraPath = PathUtils.getPicturePathByCurrentTime();

	protected AVIMConversation conversation;
	protected ImageView image;
	protected TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		initView();
		initByIntent(getIntent());
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initByIntent(intent);
	}

	private void initByIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		if (null != extras) {
			if (extras.containsKey(Constants.MEMBER_ID)) {
				getConversation(extras.getString(Constants.MEMBER_ID));

			} else if (extras.containsKey(Constants.CONVERSATION_ID)) {
				String conversationId = extras.getString(Constants.CONVERSATION_ID);
				updateConversation(AVIMClient.getInstance(
						ChatManager.getInstance().getSelfId()).getConversation(
						conversationId));
			} else {
			}
		}
	}

	protected void initActionBar(String title) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			if (title != null) {
				actionBar.setTitle(title);
			}
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
		} else {

		}
	}

	public void onEvent(EmptyEvent emptyEvent) {
	}

	protected void updateConversation(AVIMConversation con) {
		if (null != con) {
			this.conversation = con;
			setConversation(con);
			showUserName(ConversationHelper.typeOfConversation(conversation) != ConversationType.Single||ConversationHelper.typeOfConversation(conversation) != ConversationType.Doctor);
			initActionBar(ConversationHelper.titleOfConversation(conversation));
			title.setText(ConversationHelper.titleOfConversation(conversation));
		}
	}

	/**
	 * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
	 * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
	 */
	private void getConversation(final String memberId) {
		ChatManager.getInstance().fetchConversationWithUserId(memberId,
				new AVIMConversationCreatedCallback() {

					@Override
					public void done(AVIMConversation arg0, AVIMException e) {
						if (e == null) {
							ChatManager.getInstance().getRoomsTable()
									.insertRoom(arg0.getConversationId());
							updateConversation(arg0);
						}
					}
				});
	}

	@Override
	protected void onResume() {
		NotificationUtils.cancelNotification(this);
		if (null != conversation) {
			NotificationUtils.addTag(conversation.getConversationId());
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat_ativity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case GALLERY_REQUEST:
				case GALLERY_KITKAT_REQUEST:
					if (data == null) {
						toast("return intent is null");
						return;
					}
					Uri uri;
					if (requestCode == GALLERY_REQUEST) {
						uri = data.getData();
					} else {
						// for Android 4.4
						uri = data.getData();
						final int takeFlags = data.getFlags()
								& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
						getContentResolver().takePersistableUriPermission(uri, takeFlags);
					}
					String localSelectPath = ProviderPathUtils.getPath(this, uri);
					inputBottomBar.hideMoreLayout();
					sendImage(localSelectPath);
					break;
				case TAKE_CAMERA_REQUEST:
					inputBottomBar.hideMoreLayout();
					sendImage(localCameraPath);
					break;

				case QUIT_GROUP_REQUEST:
					finish();
					break;
			}
		}
	}

	public void onEvent(RecyclerClickEvent event) {
		scrollToBottom();
	}

	public void onEvent(LocationItemClickEvent event) {
		if (null != event && null != event.message
				&& event.message instanceof AVIMLocationMessage) {
			AVIMLocationMessage locationMessage = (AVIMLocationMessage) event.message;
		}
	}

	public void onEvent(ImageItemClickEvent event) {
		if (null != event && null != event.message
				&& event.message instanceof AVIMImageMessage) {
			AVIMImageMessage imageMessage = (AVIMImageMessage) event.message;
			ImageBrowserActivity.go(this, MessageHelper.getFilePath(imageMessage),
					imageMessage.getFileUrl());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (null != conversation) {
			NotificationUtils.removeTag(conversation.getConversationId());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void setConversation(AVIMConversation conversation) {
		refreshLayout.setEnabled(true);
		inputBottomBar.setTag(conversation.getConversationId());
		fetchMessages();
		NotificationUtils.addTag(conversation.getConversationId());
		messageAgent = new MessageAgent(conversation);
	}

	public void showUserName(boolean isShow) {
		itemAdapter.showUserName(isShow);
	}

	/**
	 * 拉取消息，必须加入 conversation 后才能拉取消息
	 */
	private void fetchMessages() {
		conversation.queryMessages(new AVIMMessagesQueryCallback() {

			@Override
			public void done(List<AVIMMessage> list, AVIMException e) {
				if (e == null) {
					itemAdapter.setMessageList(list);
					recyclerView.setAdapter(itemAdapter);
					itemAdapter.notifyDataSetChanged();
					scrollToBottom();
				}
			}
		});
	}

	/**
	 * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
	 */
	public void onEvent(InputBottomBarTextEvent textEvent) {

		if (null != conversation && null != textEvent) {
			if (!TextUtils.isEmpty(textEvent.sendContent)
					&& conversation.getConversationId().equals(textEvent.tag)) {
				sendText(textEvent.sendContent);
			}
		}
	}

	/**
	 * 处理推送过来的消息 同理，避免无效消息，此处加了 conversation id 判断
	 */
	public void onEvent(ImTypeMessageEvent event) {
		if (null != conversation
				&& null != event
				&& conversation.getConversationId().equals(
				event.conversation.getConversationId())) {
			itemAdapter.addMessage(event.message);
			itemAdapter.notifyDataSetChanged();
			scrollToBottom();
		}
	}

	/**
	 * 重新发送已经发送失败的消息
	 */
	public void onEvent(ImTypeMessageResendEvent event) {
		if (null != conversation
				&& null != event
				&& null != event.message
				&& conversation.getConversationId().equals(
				event.message.getConversationId())) {
			if (AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed == event.message
					.getMessageStatus()
					&& conversation.getConversationId().equals(
					event.message.getConversationId())) {
				conversation.sendMessage(event.message, new AVIMConversationCallback() {

					@Override
					public void done(AVIMException arg0) {
						itemAdapter.notifyDataSetChanged();
					}
				});
				itemAdapter.notifyDataSetChanged();
			}
		}
	}

	public void onEvent(InputBottomBarEvent event) {
		if (null != conversation && null != event
				&& conversation.getConversationId().equals(event.tag)) {
			switch (event.eventAction) {
				case InputBottomBarEvent.INPUTBOTTOMBAR_IMAGE_ACTION:
					selectImageFromLocal();
					break;
				case InputBottomBarEvent.INPUTBOTTOMBAR_CAMERA_ACTION:
					selectImageFromCamera();
					break;
			}
		}
	}

	public void onEvent(InputBottomBarRecordEvent recordEvent) {
		if (null != conversation && null != recordEvent
				&& !TextUtils.isEmpty(recordEvent.audioPath)
				&& conversation.getConversationId().equals(recordEvent.tag)) {
			sendAudio(recordEvent.audioPath);
		}
	}

	public void selectImageFromLocal() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent,
							getResources().getString(R.string.chat_activity_select_picture)),
					GALLERY_REQUEST);
		} else {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, GALLERY_KITKAT_REQUEST);
		}
	}

	public void selectImageFromCamera() {
		Intent takePictureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		Uri imageUri = Uri.fromFile(new File(localCameraPath));
		takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				imageUri);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, TAKE_CAMERA_REQUEST);
		}
	}

	private void scrollToBottom() {
		layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
	}

	protected void toast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private void sendText(String content) {
		AVIMTextMessage message = new AVIMTextMessage();
		message.setText(content);
		sendMessage(message);
	}

	private void sendImage(String imagePath) {
		AVIMImageMessage imageMsg = null;
		try {
			imageMsg = new AVIMImageMessage(imagePath);
			sendMessage(imageMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAudio(String audioPath) {
		try {
			AVIMAudioMessage audioMessage = new AVIMAudioMessage(audioPath);
			sendMessage(audioMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(AVIMTypedMessage message) {
		itemAdapter.addMessage(message);
		itemAdapter.notifyDataSetChanged();
		scrollToBottom();
		conversation.sendMessage(message, new AVIMConversationCallback() {

			@Override
			public void done(AVIMException arg0) {
				ChatManager.getInstance().getRoomsTable()
						.insertRoom(conversation.getConversationId());
				itemAdapter.notifyDataSetChanged();
			}
		});
	}

	protected void initView() {
		recyclerView = (RecyclerView) findViewById(R.id.fragment_chat_rv_chat);
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.fragment_chat_srl_pullrefresh);
		refreshLayout.setEnabled(false);
		inputBottomBar = (InputBottomBar) findViewById(R.id.fragment_chat_inputbottombar);
		image = (ImageView) findViewById(R.id.leftBtn);
		title = (TextView) findViewById(R.id.titleViewOfChatRoom);
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		itemAdapter = new MultipleItemAdapter();
		itemAdapter.resetRecycledViewPoolSize(recyclerView);
		recyclerView.setAdapter(itemAdapter);
		refreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						AVIMMessage message = itemAdapter.getFirstMessage();
						if (null == message) {
							refreshLayout.setRefreshing(false);
						} else {
							conversation.queryMessages(message.getMessageId(),
									message.getTimestamp(), 20, new AVIMMessagesQueryCallback() {

										@Override
										public void done(List<AVIMMessage> list, AVIMException e) {
											refreshLayout.setRefreshing(false);
											if (e == null) {
												if (null != list && list.size() > 0) {
													itemAdapter.addMessageList(list);
													itemAdapter.notifyDataSetChanged();
													layoutManager.scrollToPositionWithOffset(
															list.size() - 1, 0);
												}
											}
										}
									});
						}
					}
				});
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});


	}

}
