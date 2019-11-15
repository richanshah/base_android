package com.example.util;

/**
 * Created by viraj.patel on 17-Sep-18
 */
public class AppConstants {


//    public static final String BASE_DOMAIN = "http://106.201.238.169:3004";   //Local
    public static final String BASE_DOMAIN = "http://106.201.238.169:3001";   //Client
//    public static final String BASE_DOMAIN = "http://www.onkout.com/";   //Server


    // Image Selection
    public static final int CAMERA = 0x50;
    public static final int GALLERY = 0x51;

    //Drawable Sides
    public static final int DRAWABLE_LEFT = 0x29;
    public static final int DRAWABLE_RIGHT = 0x2A;
    public static final int DRAWABLE_TOP = 0x2B;
    public static final int DRAWABLE_BOTTOM = 0x2C;


    //Onkout Image Type
    public static final String IMAGE_TYPE_PROFILE = "0";
    public static final String IMAGE_TYPE_ATTACHMENT = "1";
    public static final String IMAGE_TYPE_LOGO = "0";

    // Somalia CountryId
    public static final String COUNTRY_ID = "11";

    //About Us
    public static final String KEY_THIRD_PARTY_NOTICES = "AboutThirdPartyNotices";
    public static final String KEY_PRIVACY_COOKIES = "PrivacyAndCookies";
    public static final String KEY_TERMS_OF_USE = "AboutTermsOfUse";

    //Contact US
    public static final String KEY_CONTACT_PAYMENT_RESPONSE = "ContactUsQuickResponsePayment";
    public static final String KEY_CONTACT_PHONE_INFO = "ContactUsPhoneContatcInfo";
    public static final String KEY_CONTACT_VIRTUAL_BANK = "ContactUsVirtualBank";
    public static final String KEY_CONTACT_SUUQA = "ContactUse-Suuqa";
    public static final String KEY_CONTACT_PROPERTY_MANAGEMENT = "ContactUsPropertyManagement";
    public static final String KEY_CONTACT_DATA_STATUS = "ContactUsDataStatus";


    //Help Topics
    public static final String KEY_HELP_GETTING_STARTED = "HelpTopicGettingStarted";
    public static final String KEY_HELP_QUICK_PAYMENT_RESPONSE = "HelpQuickResponsePayment";
    public static final String KEY_HELP_PHONE = "HelpPhone";
    public static final String KEY_HELP_VIRTUAL_BANK = "HelpVirtualBank";
    public static final String KEY_HELP_SUUQA = "HelpESuuqa";
    public static final String KEY_HELP_PROPERTY_MANAGEMENT = "HelpPropertyManagement";
    public static final String KEY_HELP_DATA_STATUS = "HelpDataStatus";

    // Virtual Bank Tabs
    public static final String TAB_VIRTUAL_BANK = "BlueStar";
    public static final String TAB_LOAD_MONEY = "LoadMoney";
    public static final String TAB_TRANSFERS = "Transfer";
    public static final String TAB_SIGNOUT = "Signout";


    // Invoice Type
    public static final int INVOICE_TYPE_RECEIVED = 0;
    public static final int INVOICE_TYPE_PAID = 1;
    public static final int INVOICE_TYPE_TRANSFERRED = 2;
    public static final int INVOICE_TYPE_WITHDRAW = 3;
    public static final int INVOICE_TYPE_ALL = -1;

    // Update Withdraw Request Type
    public static final int REQUEST_TYPE_ACCEPT = 1;
    public static final int REQUEST_TYPE_REJECT = 2;
    public static final int REQUEST_TYPE_CANCEL = 3;
    public static final int REQUEST_TYPE_REQUESTED = 6;

    // WeekStatus
    public static final int SMART_AYUUTO_WEEK_STATUS_PENDING = 0;
    public static final int SMART_AYUUTO_WEEK_STATUS_SUCCEED = 1;
    public static final int SMART_AYUUTO_WEEK_STATUS_FAILED = 2;

    //Status Type
    public static final String STATUS_TYPE_REQUESTED = "Requested";
    public static final String STATUS_TYPE_ACCEPT = "Accept";
    public static final String STATUS_TYPE_REJECT = "Reject";
    public static final String STATUS_TYPE_PAID = "Paid";
    public static final String STATUS_TYPE_CANCEL = "Cancel";
    public static final String STATUS_TYPE_EXPIRED = "Expired";


    //Notification Types
    public static final String NOTIFICATION_TYPE_WITHDRAW_REQUEST = "WithdrawRequest";
    public static final String NOTIFICATION_TYPE_REQUEST_ACCEPTED = "RequestAccept";
    public static final String NOTIFICATION_TYPE_REQUEST_REJECTED = "RequestReject";
    public static final String NOTIFICATION_TYPE_REQUEST_CANCEL = "RequestCancel";


}
