package org.fund.constant;

public class Consts {
    public static final String SCHEMAS_QUERY_FILE_NAME = "schemas_query.sql";
    public static final String SCHEMAS_LIST_FILE_NAME = "schemas_list.json";
    public static final String DEFAULT_FOLDER_ADDRESS = "classpath:";
    public static final String DEFAULT_VERSION_API_URL = "/v1";
    public static final String PREFIX_API_URL = "/api";
    public static final String DEFAULT_PREFIX_API_URL = PREFIX_API_URL + DEFAULT_VERSION_API_URL;
    public static final String JDBC_URL_PREFIX = "jdbc:oracle:thin:@";
    public static final String HEADER_TENANT_PARAM_NAME = "X-TenantId";
    public static final String HEADER_UUID_PARAM_NAME = "X-UUID";
    public static final String DATASOURCE_MAP_KEY = "app_no";
    public static final int JPA_BATCH_SIZE = 50;
    public static final String GREGORIAN_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CACHE_NAME = "findAll";
    public static final String PERSIAN_DATE_REGEX = "^(1[34]\\d{2})/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])$";
    public static final String MOBILE_REGEX = "^(09|989)\\d{9}$";
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@" + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
    public static final int DEFAULT_SEED_COUNTER = 100;
    public static final int SEED_COUNTER = 2000;

    //////////////////////////////////////////////////////PARAMS/////////////////////////////////////////////////////////
    public static final String PARAMS_TWO_FACTOR_LOGIN_WITH_SMS_APPUSERS = "TWO_FACTOR_LOGIN_WITH_SMS_APPUSERS";
    public static final String PARAMS_TWO_FACTOR_LOGIN_WITH_EMAIL_APPUSERS = "TWO_FACTOR_LOGIN_WITH_EMAIL_APPUSERS";
    public static final String PARAMS_TWO_FACTOR_LOGIN_WITH_PHYSICAL_APPUSERS = "TWO_FACTOR_LOGIN_WITH_PHYSICAL_APPUSERS";
    //////////////////////////////////////////////////////SMS_Type/////////////////////////////////////////////////////////
    public static final String SMS_TYPE_HIGH_PRIORITY_SENDING = "99";
    public static final String SMS_TYPE_COMMON = "2";
    //////////////////////////////////////////////////////EMAIL_Message/////////////////////////////////////////////////////////
    public static final String EMAIL_FROM_ADDRESS_NO_REPLY = "noreply@rhbroker.com";
    public static final String ALERT_EMAIL_SUBJECT_VERIFICATION_CODE = "کد اعتبار سنجی مشتری در صتدوق ";
}
