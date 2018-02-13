package id.co.myrepublic.salessupport.constant;

/**
 * Application Constant
 */

public interface AppConstant {
    //public static final String BASE_URL = "https://boss.myrepublic.co.id";
    public static final String BASE_URL = "https://boss-st.myrepublic.co.id";
    public static final String BASE_API_URL = BASE_URL+"/api";
    public static final String LOGIN_URL = BASE_URL+"/login";
    public static final String CHECK_SESSION_URL = BASE_API_URL+"/auth/check_session";
    public static final String CHECK_PERMISSION = BASE_API_URL+"/auth/get_permissions";
    public static final String GET_USER_INFO = BASE_API_URL+"/user/select";
    public static final String GET_CITY_API_URL = BASE_API_URL+"/homepass/get_city_list";
    public static final String GET_CLUSTER_API_URL = BASE_API_URL+"/homepass/get_cluster_list";
    public static final String GET_CLUSTERDETAIL_API_URL = BASE_API_URL+"/homepass/get_cluster_information";
    public static final String INSERT_CLUSTERINFO_API_URL = BASE_API_URL+"/homepass/insert_cluster_information";
    public static final String GET_HOMEPASS_API_URL = BASE_API_URL+"/homepass/get_address_list";
    public static final String GET_PRODUCT_CATALOG = BASE_API_URL+"/product/get_bundle_catalog";
    public static final String GET_CHANNELS_API_URL = BASE_API_URL+"/misc/get_channels";
    public static final String GET_CUSTOMER_TYPE_API_URL = BASE_API_URL+"/misc/get_customer_types_mobile";
    public static final String GET_GENDER_API_URL = BASE_API_URL+"/misc/get_gender_code";
    public static final String GET_SALUTATION_API_URL = BASE_API_URL+"/misc/get_salutations";
    public static final String GET_CUSTOMER_NATIONALITIES_API_URL = BASE_API_URL+"/misc/get_customer_nationalities";
    public static final String GET_DEALER_API_URL = BASE_API_URL+"/misc/get_dealer_info";
    public static final String GET_USER_REP_API_URL = BASE_API_URL+"/user/get_user_rep_info";
    public static final String GET_DWELLING_TYPE_API_URL = BASE_API_URL+"/misc/get_dwelling_type";
    public static final String GET_ADDRESS_PREFIX_API_URL = BASE_API_URL+"/homepass/get_address_prefix";
    public static final String GET_ORDER_CATALOG_API_URL = BASE_API_URL+"/product_catalog/get_order_catalog";
    public static final String INSERT_ORDER_API_URL = BASE_API_URL+"/order/create_new_order";
    public static final String UPLOAD_ORDER_DOCUMENT_API_URL = BASE_API_URL+"/mobile-order-upload";
    public static final String GET_OTP_API_URL = BASE_API_URL+"/sms/send_otp";
    public static final String SEND_THANKS_SMS_API_URL = BASE_API_URL+"/sms/send_sms_verified";
    public static final String SESSION_KEY = "myrepublicid";
    public static final String COOKIE_FULL_STRING = "cookieFullString";
    public static final String COOKIE_SESSION_KEY = "mySessionId";
    public static final String COOKIE_USERID_KEY = "myUserId";
    public static final String COOKIE_USERTYPE_KEY = "userType";
    public static final String COOKIE_USERNAME_KEY = "myUserName";
    public static final String COOKIE_REP_ID = "myRepId";
    public static final String COOKIE_LOGIN_NAME = "loginName";
    public static final String SESSION_VALIDATION = "session_validation";
    public static final String PERMISSION_MOBILE_APP = "ux.homepass.mobile_sales_support";
    public static final String ALACARTE_MOVIES_ID = "20025";
    public static final String ALACARTE_SPORT_ID = "20026";
    public static final String ALACARTE_XTRATV_ID = "20748";
    public static final String ALACARTE_PUBLICIP_ID = "20209";

}
