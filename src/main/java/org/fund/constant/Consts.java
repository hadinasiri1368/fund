package org.fund.constant;

public class Consts {
    public static final String SCHEMAS_QUERY_FILE_NAME = "schemas_query.sql";
    public static final String SCHEMAS_LIST_FILE_NAME = "schemas_list.json";
    public static final String DEFAULT_FOLDER_ADDRESS = "classpath:";
    public static final String PREFIX_API_URL = "/api/" ;
    public static final String JDBC_URL_PREFIX = "jdbc:oracle:thin:@";
    public static final String HEADER_TENANT_PARAM_NAME = "X-TenantId";
    public static final String HEADER_UUID_PARAM_NAME = "X-UUID";
    public static final String DATASOURCE_MAP_KEY = "app_no";
    public static final int JPA_BATCH_SIZE = 50;
    public static final String GREGORIAN_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CACHE_NAME = "findAll";
    public static final String PERSIAN_DATE_REGEX = "^(1[34]\\d{2})/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])$";
}
