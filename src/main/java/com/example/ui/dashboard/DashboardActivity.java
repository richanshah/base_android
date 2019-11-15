package com.example.ui.dashboard;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.content.res.AppCompatResources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cisner.cisnerapp.BaseActivity;
import com.cisner.cisnerapp.Cisner;
import com.cisner.cisnerapp.R;
import com.cisner.cisnerapp.api.CommonAPI;
import com.cisner.cisnerapp.cometchat.activity.chat.CCSingleChatActivity;
import com.cisner.cisnerapp.cometchat.activity.chat.call.CCIncomingCallActivity;
import com.cisner.cisnerapp.cometchat.keys.BroadCastReceiverKeys;
import com.cisner.cisnerapp.cometchat.model.comet.Contact;
import com.cisner.cisnerapp.cometchat.model.comet.Conversation;
import com.cisner.cisnerapp.cometchat.model.comet.Groups;
import com.cisner.cisnerapp.cometchat.utils.MyLg;
import com.cisner.cisnerapp.cometchat.view.fragment.ChatFragment;
import com.cisner.cisnerapp.databinding.ActivityDashboardBinding;
import com.cisner.cisnerapp.databinding.CustomTabChatBinding;
import com.cisner.cisnerapp.databinding.CustomTabNotificationBinding;
import com.cisner.cisnerapp.enums.PostIn;
import com.cisner.cisnerapp.eventbus.InternetEvent;
import com.cisner.cisnerapp.feed.CreatePostActivity;
import com.cisner.cisnerapp.interfaces.CallbackTask;
import com.cisner.cisnerapp.model.GetWheatherEntity;
import com.cisner.cisnerapp.model.MemberShipData;
import com.cisner.cisnerapp.moments.MomentsFragment;
import com.cisner.cisnerapp.notification.NotificationTabFragment;
import com.cisner.cisnerapp.utils.AlertUtils;
import com.cisner.cisnerapp.utils.CheckPermissionUtils;
import com.cisner.cisnerapp.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inscripts.enums.SettingSubType;
import com.inscripts.enums.SettingType;
import com.inscripts.heartbeats.CCHeartbeat;
import com.inscripts.helpers.PreferenceHelper;
import com.inscripts.keys.CometChatKeys;
import com.inscripts.keys.PreferenceKeys;
import com.inscripts.plugins.VideoChat;
import com.inscripts.pojos.CCSettingMapper;
import com.inscripts.utils.CommonUtils;
import com.inscripts.utils.Logger;
import com.inscripts.utils.SessionData;
import com.inscripts.utils.StaticMembers;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cisner.cisnerapp.utils.CheckPermissionUtils.LOCATION;

public class DashboardActivity extends BaseActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    public ActivityDashboardBinding binding;
    private FeedFragment feedFragment;
    private MomentsFragment momentsFragment;
    private ChatFragment chatFragment;
    private NotificationTabFragment notificationFragment;
    private int prvTabPosition = 0, selectedTab = 0;
    public static boolean isLanguageChange = false;
    private FusedLocationProviderClient mFusedLocationClient;
    public final String DATA = "com.parse.Data";
    int type = -1;

    private InternetReceiver internetReceiver;

    public class InternetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final boolean isOnline = Utils.isNetworkAvailable();
            EventBus.getDefault().post(new InternetEvent(isOnline));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        if (internetReceiver == null) {
            internetReceiver = new InternetReceiver();
            registerReceiver(internetReceiver, new IntentFilter(Utils.CONNECTIVITY_CHANGE));
        }

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (internetReceiver == null) {
            unregisterReceiver(internetReceiver);
        }
    }

    private void init() {
        me = this;
        setFullScreen();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", -1);
        }

        //For display Reactive again Welcome Text
        if (getIntent().hasExtra("isReactive")) {
            boolean isActive = getIntent().getBooleanExtra("isReactive", false);
            if (isActive) {
                AlertUtils.showSnackBar(me, getString(R.string.happy_text));
            }
        }


        setupTabLayout();
        bindWidgetsWithAnEvent();
        updateChatBadge();

        //Check Membership
        checkMembership();

        //For CometChat Notification
        if (getIntent().getStringExtra("Type") != null) {
            if (getIntent().getStringExtra("Type").equalsIgnoreCase("cc_push")) {
                handleIntent(getBaseContext(), getIntent());
                checkUserGroupRedirection();
            }
        }


        //get Current Latitude & Longitude
        if (CheckPermissionUtils.checkPermission(me, LOCATION)) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Utils.setLatLongData(me, location.getLatitude() + "_" + location.getLongitude());
                                getWeatherAPICall();
                            }
                        }
                    });
        } else {
            getWeatherAPICall();
        }
    }

    private String groupUserId;
    private boolean isGroup;

    public void handleIntent(Context context, Intent mainIntent) {

        groupUserId = mainIntent.getStringExtra("group_user_id");
        isGroup = mainIntent.getBooleanExtra("is_group", false);
        try {
            PreferenceHelper.initialize(this);
            if (PreferenceHelper.contains(PreferenceKeys.LoginKeys.LOGGED_IN)
                    && "1".equals(PreferenceHelper.get(PreferenceKeys.LoginKeys.LOGGED_IN))) {
                String action = mainIntent.getAction();

                Logger.error(TAG, "ACTION_SEND action : " + action);

                NotificationManager notificationManager = (NotificationManager) PreferenceHelper.getContext().getSystemService(
                        Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                PreferenceHelper.removeKey(PreferenceKeys.DataKeys.NOTIFICATION_STACK);

                if (mainIntent.hasExtra("isFirebaseNotification")) {

                    String messgeStr = mainIntent.getStringExtra("m");
                    JSONObject message = new JSONObject(messgeStr);
                    long buddyId = message.getLong("fid");
                    String notificationMessage = message.getString("m");

                    Long time = message.getLong("sent") * 1000;

                    if (message.has("cid")) {
                        long chatroomId;
                        if (message.has("cid")) {
                            chatroomId = Long.parseLong("cid");
                        } else {
                            chatroomId = Long.parseLong(PreferenceHelper.get(PreferenceKeys.DataKeys.CURRENT_CHATROOM_ID));
                        }
                        Pattern pattern = Pattern.compile("@(.*?):");
                        Matcher matcher = pattern.matcher(notificationMessage);
                        matcher.find();
                        notificationMessage = matcher.group(1);

                        Groups chatroom = Groups.getGroupDetails(chatroomId);
                        if (null != chatroom) {
                            chatroom.unreadCount = 0;
                            chatroom.save();

                           /* TODO
                           Intent chatroomIntent = new Intent(context, CCGroupChatActivity.class);
                            chatroomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            chatroomIntent.putExtra(StaticMembers.INTENT_CHATROOM_ID, chatroomId);
                            chatroomIntent.putExtra(StaticMembers.INTENT_CHATROOM_NAME, notificationMessage);
                            context.startActivity(chatroomIntent);*/

                            Intent intent = new Intent(BroadCastReceiverKeys.HeartbeatKeys.CHATROOM_HEARTBEAT_UPDATAION);
                            intent.putExtra(
                                    BroadCastReceiverKeys.ListUpdatationKeys.REFRESH_FULL_CHATROOM_LIST_FRAGMENT, 1);
                            context.sendBroadcast(intent);
                        } else {
                            // Chatroom doesn't exist locally.
                            Logger.error("chatroom is not present in our db");
                            CCHeartbeat.getInstance().setForceHeartbeat();
                        }
                    } else {
                        String messageType = mainIntent.getStringExtra("t");
                        if (messageType.equals("O_AC")) {
                            if ((System.currentTimeMillis() - time) < 60000) {
                                String roomName = mainIntent.getStringExtra("grp");
                                Intent avChatIntent = new Intent(context, CCIncomingCallActivity.class);
                                avChatIntent.putExtra(CometChatKeys.AVchatKeys.CALLER_ID, buddyId);
                                avChatIntent.putExtra(CometChatKeys.AVchatKeys.ROOM_NAME, roomName);
                                avChatIntent.putExtra(CometChatKeys.AudiochatKeys.AUDIO_ONLY_CALL, true);
                                PreferenceHelper.save("FCMBuddyID", buddyId);
                                startActivity(avChatIntent);
                            }

                        } else if (messageType.equals("O_AVC")) {

                            if ((System.currentTimeMillis() - time) < 60000) {
                                String roomName = mainIntent.getStringExtra("grp");
                                Intent avChatIntent = new Intent(context, CCIncomingCallActivity.class);
                                avChatIntent.putExtra(CometChatKeys.AVchatKeys.CALLER_ID, buddyId);
                                avChatIntent.putExtra(CometChatKeys.AVchatKeys.ROOM_NAME, roomName);
                                PreferenceHelper.save("FCMBuddyID", buddyId);
                                startActivity(avChatIntent);
                            }
                        } else {
                            Contact buddy = Contact.getContactDetails(buddyId);
                            buddy.unreadCount = 0;
                            buddy.save();

                            int colonPosition = notificationMessage.indexOf(":");
                            notificationMessage = notificationMessage.substring(0, colonPosition);

                            Intent singleChatIntent = new Intent(context, CCSingleChatActivity.class);
                            /* singleChatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                            singleChatIntent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.BUDDY_ID, buddyId);
                            singleChatIntent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.BUDDY_NAME, notificationMessage);
                            context.startActivity(singleChatIntent);

                            Intent intent = new Intent(
                                    BroadCastReceiverKeys.HeartbeatKeys.ONE_ON_ONE_HEARTBEAT_NOTIFICATION);
                            intent.putExtra(BroadCastReceiverKeys.ListUpdatationKeys.REFRESH_BUDDY_LIST_FRAGMENT, 1);
                            context.sendBroadcast(intent);

                            Intent iintent = new Intent(BroadCastReceiverKeys.HeartbeatKeys.ANNOUNCEMENT_BADGE_UPDATION);
                            iintent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.NEW_MESSAGE, 1);
                            context.sendBroadcast(iintent);
                            SessionData.getInstance().setChatbadgeMissed(true);
                        }
                    }
                }

                if (mainIntent.hasExtra(DATA)) {

                    Logger.error(TAG, "ACTION_SEND mainIntent.hasExtra(DATA)");

                    JSONObject json = new JSONObject(mainIntent.getStringExtra(DATA));
                    String messgeStr = json.getString("m");
                    JSONObject message = new JSONObject(messgeStr);

                    String notificationMessage = message.getString("m");

                    if (json.has("isCR")) {
                        long chatroomId;
                        if (message.has("cid")) {
                            chatroomId = Long.parseLong(message.getString("cid"));
                        } else {
                            chatroomId = Long.parseLong(PreferenceHelper.get(PreferenceKeys.DataKeys.CURRENT_CHATROOM_ID));
                        }
                        Pattern pattern = Pattern.compile("@(.*?):");
                        Matcher matcher = pattern.matcher(notificationMessage);
                        matcher.find();
                        notificationMessage = matcher.group(1);

                        Conversation conversation = Conversation.getConversationByChatroomID(String.valueOf(chatroomId));
                        if (conversation != null) {
                            conversation.unreadCount = 0;
                            conversation.save();
                        }

                        Groups chatroom = Groups.getGroupDetails(chatroomId);
                        if (null != chatroom) {
                            chatroom.unreadCount = 0;
                            chatroom.save();

                            /* TODO
                            Intent chatroomIntent = new Intent(context, CCGroupChatActivity.class);
                            chatroomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            chatroomIntent.putExtra(StaticMembers.INTENT_CHATROOM_ID, String.valueOf(chatroomId));
                            chatroomIntent.putExtra(StaticMembers.INTENT_CHATROOM_NAME, notificationMessage);
                            context.startActivity(chatroomIntent);
                            */

                            Intent intent = new Intent(BroadCastReceiverKeys.HeartbeatKeys.CHATROOM_HEARTBEAT_UPDATAION);
                            intent.putExtra(
                                    BroadCastReceiverKeys.ListUpdatationKeys.REFRESH_FULL_CHATROOM_LIST_FRAGMENT, 1);
                            context.sendBroadcast(intent);
                        } else {
                            // Chatroom doesn't exist locally.
                            Logger.error("chatroom is not present in our db");
                            CCHeartbeat.getInstance().setForceHeartbeat();
                        }
                    } else if (json.has("isANN")) {
                        /* TODO
                        Intent announcementIntent = new Intent(context, CCAnnouncementsActivity.class);
                        announcementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(announcementIntent);
                        */
                    } else {
                        /*
                         * Directly show Incoming call screen if it is an av
                         * chat request.
                         */
                        long buddyId = message.getLong("fid");
                        if (json.has("avchat")
                                && json.getString("avchat").equals("1")) {
                            String roomName = VideoChat.getAVRoomName(message.getString("m"),
                                    false);

                            Intent avChatIntent = new Intent(context, CCIncomingCallActivity.class);
                            avChatIntent.putExtra(CometChatKeys.AVchatKeys.CALLER_ID, buddyId);
                            avChatIntent.putExtra(CometChatKeys.AVchatKeys.ROOM_NAME, roomName);
                            context.startActivity(avChatIntent);
                        } else {

                            //Logger.error("normal message");

                            Contact buddy = Contact.getContactDetails(buddyId);
                            buddy.unreadCount = 0;
                            buddy.save();

                            Conversation conversation = Conversation.getConversationByBuddyID(String.valueOf(buddyId));
                            if (0L != conversation.unreadCount) {
                                conversation.unreadCount = 0;
                                conversation.save();
                            }

                            int colonPosition = notificationMessage.indexOf(":");

                            notificationMessage = notificationMessage.substring(0, colonPosition);

                            Intent singleChatIntent = new Intent(context, CCSingleChatActivity.class);
                            singleChatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            singleChatIntent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.CONTACT_ID, buddyId);
                            singleChatIntent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.CONTACT_NAME, notificationMessage);
                            context.startActivity(singleChatIntent);

                            Intent intent = new Intent(
                                    BroadCastReceiverKeys.HeartbeatKeys.ONE_ON_ONE_HEARTBEAT_NOTIFICATION);
                            intent.putExtra(BroadCastReceiverKeys.ListUpdatationKeys.REFRESH_BUDDY_LIST_FRAGMENT, 1);
                            context.sendBroadcast(intent);

                            Intent iintent = new Intent(BroadCastReceiverKeys.HeartbeatKeys.ANNOUNCEMENT_BADGE_UPDATION);
                            iintent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.NEW_MESSAGE, 1);
                            context.sendBroadcast(iintent);
                            SessionData.getInstance().setChatbadgeMissed(true);
                            finish();
                        }
                    }
                } else if (Intent.ACTION_SEND.equals(action)) {
                    Logger.error(TAG, "ACTION_SEND action : " + action);
                    Bundle extras = mainIntent.getExtras();
                    String type = mainIntent.getType();
                    Logger.error(TAG, "ACTION_SEND type " + type);
                    if (extras != null && extras.containsKey(Intent.EXTRA_STREAM)) {
                        Uri uri = extras.getParcelable(Intent.EXTRA_STREAM);
                        Logger.error(TAG, "ACTION_SEND uri " + uri);
                        if (!type.isEmpty() && CommonUtils.checkImageType(type)) {
                            PreferenceHelper.save(PreferenceKeys.DataKeys.SHARE_IMAGE_URL, String.valueOf(uri));
                        } else if (!type.isEmpty() && CommonUtils.checkVideoType(type)) {
                            PreferenceHelper.save(PreferenceKeys.DataKeys.SHARE_VIDEO_URL, String.valueOf(uri));
                        } else if (!type.isEmpty() && CommonUtils.checkAudioType(type)) {
                            PreferenceHelper.save(PreferenceKeys.DataKeys.SHARE_AUDIO_URL, String.valueOf(uri).replace("file://", ""));
                        } else if (!type.isEmpty() && (CommonUtils.checkApplicationType(type) || CommonUtils.checkTextType(type))) {
                            PreferenceHelper.save(PreferenceKeys.DataKeys.SHARE_FILE_URL, String.valueOf(uri));
                        } else {
                            String last = uri.getLastPathSegment();
                            if (!CommonUtils.setFileType(last, uri)) {
                                Toast.makeText(getApplicationContext(), (String) cometChat.getCCSetting(new CCSettingMapper(SettingType.LANGUAGE, SettingSubType.LANG_FILE_NOT_SUPPORTED)), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), (String) cometChat.getCCSetting(new CCSettingMapper(SettingType.LANGUAGE, SettingSubType.LANG_FILE_NOT_SUPPORTED)), Toast.LENGTH_LONG).show();
                    }
                    mainIntent.removeExtra(Intent.EXTRA_STREAM);
                } else {
                    Logger.error(TAG, "ACTION_SEND no action");
                }
            } else {
                Logger.error(TAG, "User is not logged in");
                Logger.error(TAG, "ACTION_SEND User is not logged in");
                finish();
            }
        } catch (Exception e) {
            Logger.error(TAG, "ACTION_SEND e : " + e.toString());
            e.printStackTrace();
        }
    }


    private String chatroomName;

    private void checkUserGroupRedirection() {
        if (groupUserId != null) {
            if (!isGroup) {
                openUserChat();
            } else if (isGroup) {
//                Groups groups = Groups.getGroupDetails(groupUserId);
//                chatroomName = groups.name;
//                openGroupChat();
            }
            finish();
        }
    }

    private void openUserChat() {
        Contact contact = Contact.getContactDetails(groupUserId);

        Intent intent = new Intent(this, CCSingleChatActivity.class);
        intent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.CONTACT_ID, Long.valueOf(groupUserId));
        if (PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_IMAGE_URL) != null && !PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_IMAGE_URL).isEmpty()) {
            intent.putExtra("ImageUri", PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_IMAGE_URL));
        }
        if (PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_VIDEO_URL) != null && !PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_VIDEO_URL).isEmpty()) {
            intent.putExtra("VideoUri", PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_VIDEO_URL));
        }
        if (PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_AUDIO_URL) != null && !PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_AUDIO_URL).isEmpty()) {
            intent.putExtra("AudioUri", PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_AUDIO_URL));
        }
        if (PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_FILE_URL) != null && !PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_FILE_URL).isEmpty()) {
            intent.putExtra("FileUri", PreferenceHelper.get(PreferenceKeys.DataKeys.SHARE_FILE_URL));
        }
        intent.putExtra(BroadCastReceiverKeys.IntentExtrasKeys.CONTACT_NAME, contact.name);
        SessionData.getInstance().setTopFragment(StaticMembers.TOP_FRAGMENT_ONE_ON_ONE);
        startActivity(intent);
    }

    private void getWeatherAPICall() {
        CommonAPI.getWeatherAPI(me, new CallbackTask() {
            @Override
            public void onFail(Object object) {
            }

            @Override
            public void onSuccess(Object object) {
                JsonObject jObjData = (JsonObject) object;
                Gson gson = new Gson();
                GetWheatherEntity mGetWheatherEntity = gson.fromJson(jObjData, GetWheatherEntity.class);
                Utils.setWeatherData(me, mGetWheatherEntity);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void setupTabLayout() {

        feedFragment = new FeedFragment();
        momentsFragment = new MomentsFragment();
        chatFragment = new ChatFragment();
        notificationFragment = new NotificationTabFragment();

        View tabHome = getLayoutInflater().inflate(R.layout.custom_tab, null);
        final Drawable home = AppCompatResources.getDrawable(this, R.drawable.selector_home_tab);
        tabHome.findViewById(R.id.icon).setBackground(home);
        binding.tabs.addTab(binding.tabs.newTab().setCustomView(tabHome), true);

        View tabStory = getLayoutInflater().inflate(R.layout.custom_tab, null);
        tabStory.findViewById(R.id.icon).setBackgroundResource(R.drawable.selector_story_tab);
        binding.tabs.addTab(binding.tabs.newTab().setCustomView(tabStory));

        View tabCreate = getLayoutInflater().inflate(R.layout.custom_tab, null);
        tabCreate.findViewById(R.id.icon).setBackgroundResource(R.drawable.selector_create_tab);
        binding.tabs.addTab(binding.tabs.newTab().setCustomView(tabCreate));


        CustomTabChatBinding chatbinding = CustomTabChatBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, false);
        chatbinding.icon.setBackgroundResource(R.drawable.selector_chat_tab);
        binding.tabs.addTab(binding.tabs.newTab().setCustomView(chatbinding.getRoot()));

        CustomTabNotificationBinding notbinding = CustomTabNotificationBinding.inflate(getLayoutInflater(), baseBinding.frmContainer, false);
        binding.tabs.addTab(binding.tabs.newTab().setCustomView(notbinding.getRoot()));

        if (feedFragment == null) {
            feedFragment = new FeedFragment();
        }
        if (type == -1) {
            setCurrentTabFragment(0);
        } else {
            selectedTab = type;
            binding.tabs.getTabAt(type).select();
            setCurrentTabFragment(type);
            type = -1;
        }


    }

    private void bindWidgetsWithAnEvent() {
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 2) {
                    prvTabPosition = tab.getPosition();
                }
                selectedTab = tab.getPosition();
                //presentActivity(tab.getCustomView());
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (selectedTab == 0 && feedFragment != null) {
                    feedFragment.gotop();
                }

                if (selectedTab == 4 && notificationFragment != null) {
                    notificationFragment.gotop();
                }
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (selectedTab == 0 && feedFragment != null) {
            feedFragment.onActivityReenter(resultCode, data);
        } else if (selectedTab == 1 && momentsFragment != null) {
            momentsFragment.onActivityReenter(resultCode, data);
        }
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(feedFragment);
                break;
            case 1:
                replaceFragment(momentsFragment);
                break;
            case 2:
                presentActivity(((LinearLayout) binding.tabs.getTabAt(2).getCustomView()).getChildAt(0));
                if (prvTabPosition != 0)
                    replaceFragment(feedFragment);
                prvTabPosition = 0;
                //startActivity(new Intent(me, CreatePostActivity.class));
                break;
            case 3:
                replaceFragment(chatFragment);
                break;
            case 4:
                Cisner.getInstance().getNotificationCount().setNotifications(0);
                replaceFragment(notificationFragment);
                break;

        }
    }

    public void replaceFragment(Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerMain, selectedFragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if ((selectedTab == 0 || selectedTab == 2) && feedFragment != null) {
            feedFragment.onActivityResult(requestCode, resultCode, data);
        } else if (selectedTab == 1 && momentsFragment != null) {
            momentsFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (selectedTab == 2) {
            TabLayout.Tab tab = binding.tabs.getTabAt(prvTabPosition);
            tab.select();
        }

        if (isLanguageChange) {
            isLanguageChange = false;
            // recreate();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void presentActivity(View view) {
        CreatePostActivity.start(this, CreatePostActivity.CREATE,
                Utils.getUserId(this), PostIn.TIMELINE, view);
    }

    private void checkMembership() {
        CommonAPI.checkMembership(me, new CallbackTask() {
            @Override
            public void onFail(Object object) {
            }

            @Override
            public void onSuccess(Object object) {
                JsonObject jObjData = (JsonObject) object;
                Gson gson = new Gson();
                MemberShipData mCurrentData = gson.fromJson(jObjData.get("membership_data").getAsJsonObject().toString(), MemberShipData.class);
                mCurrentData.setEndDate(jObjData.get("next_due_date").getAsString());
                mCurrentData.setStartDate(jObjData.get("membership_start_date").getAsString());
                Utils.setMembershipData(me, mCurrentData);
                Cisner.instance.getProfile().setWalletBalance(jObjData.get("wallet_balance").getAsFloat());
                Cisner.instance.getProfile().setMembershipData(Utils.getMembershipData(me));

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void updateChatBadge() {
        MyLg.e(TAG, "Conversation unread count : " + Conversation.getUnreadConversationCount());
        Cisner.getInstance().getChatNotifyCount().setNotificationCount(Conversation.getUnreadConversationCount());
    }

}
