package com.android.fusion.example.Utils;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FCM_TOKEN = "fcm_token";

    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";
    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";
    public static final String API_KEY_SERVER = "YOUR_API_KEY_SERVER";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAA4PHjCIE:APA91bFmOQip2IiJnm00F8II5YoT8iRNHnyH1eIz4WxedyEpl8HwiTx_d885qx3114bPwG-TegRhlNz-dQgh5vloi94gbW8f0gwf4xtH2dHHbf0Z1g8WxLcIu67VoH5zWeEuohiW0u4M"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");

        return headers;
    }
    public static String
            USER_MOBILE = "mobile",
            USER_ID = "uid",
            USER_IMAGE = "image",
            USER_STATUS = "status",
            USER_BIO = "bio",
            USER_NAME = "name",
            LOGIN_STATE = "login_state",
            TYPING_STATUS ="typing_status",
            CHAT_MESSAGE = "message",
            CHAT_STATUS = "status",
            TIMESTAMP = "timestamp",
            CHAT_MESSAGE_ID = "mid",
            CHAT_TYPE = "type",
            CHAT_SENDER_ID = "sender",
            CHAT_RECEIVER_ID = "receiver",
            CHAT_IMAGE ="image",
            STATUS_SENDER_ID="sender_id",
            STATUS_IMAGE = "image",
            STATUS_CONTENT="status_content",
            STATUS_ID = "status_id",
            STATUS_SENDER_NAME ="name",
            GROUP_NAME="name",
            GROUP_ID ="group_id",
            GROUP_IMAGE ="image",
            GROUP_DESCRIPTION = "description",
            GROUP_CREATOR = "creator",
            GROUP_MEMBER_NAME = "m_name",
            GROUP_MEMBER_ID = "m_id",
            GROUP_MEMBER_ROLE = "m_role",
            GROUP_MEMBER_JOINING_TIME = "m_joining_time",
            CHATLIST = "ChatList",
            CHATS = "Chats",
            MEMBERS = "Members",
            GROUPS = "Groups",
            STATUS = "Status",
            USERS = "Users";

}
