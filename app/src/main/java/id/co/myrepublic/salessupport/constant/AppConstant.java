package id.co.myrepublic.salessupport.constant;

/**
 * Created by myrepublicid on 27/9/17.
 */

public interface AppConstant {
    public static final String BASE_URL = "https://boss.myrepublic.co.id";
    public static final String BASE_API_URL = BASE_URL+"/api";
    public static final String LOGIN_URL = BASE_URL+"/login";
    public static final String GET_CITY_API_URL = BASE_API_URL+"/homepass/get_city_list";
    public static final String GET_CLUSTER_API_URL = BASE_API_URL+"/homepass/get_cluster_list";
    public static final String GET_CLUSTERDETAIL_API_URL = BASE_API_URL+"/homepass/get_cluster_information";
    public static final String INSERT_CLUSTERINFO_API_URL = BASE_API_URL+"/homepass/insert_cluster_information";
    public static final String SESSION_KEY = "myrepublicid";
    public static final String COOKIE_SESSION_KEY = "mySessionId";
    public static final String COOKIE_USERID_KEY = "myUserId";
    public static final String COOKIE_USERTYPE_KEY = "userType";
}
