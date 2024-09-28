package com.codehouse.contants;

public class Constant {
    public static final String WORDPRESS_URL_POSTFIX = "/wp-json/wp/v2/";

    public static final String WP_DATA_BASE_PATH = ".\\wordpress\\WORLD_SPORTS_7\\%s\\";
    public static final String MEDIA_FOLDER_PATH = WP_DATA_BASE_PATH + "media";
    public static final String CSV_FOLDER_PATH = WP_DATA_BASE_PATH + "csv";

    public static final String MEDIA_JSON_FILE_PATH = Constant.WP_DATA_BASE_PATH + "media.json";
    public static final String POSTS_JSON_FILE_PATH = Constant.WP_DATA_BASE_PATH + "posts.json";
    public static final String PAGES_JSON_FILE_PATH = Constant.WP_DATA_BASE_PATH + "pages.json";
    public static final String CATEGORIES_JSON_FILE_PATH = Constant.WP_DATA_BASE_PATH + "categories.json";
    public static final String TAGS_JSON_FILE_PATH = Constant.WP_DATA_BASE_PATH + "tags.json";
    public static final String MY_MEDIA_JSON_FILE_PATH = Constant.WP_DATA_BASE_PATH + "mymedia.json";
    public static final String REQUIRED_MEDIA_JSON_FILE_PATH = Constant.WP_DATA_BASE_PATH + "requiredmedia.json";

    public static class OperationType {
        public static String _0_DOWNLOAD_ONLY_JSON = "0_DOWNLOAD_ONLY_JSON";
        public static String _1_DOWNLOAD_ALL_JSON_WITH_MEDIA = "1_DOWNLOAD_ALL_JSON_WITH_MEDIA";
        public static String _2_DOWNLOAD_REQUIRED_MEDIA_JSON_BY_POST = "2_DOWNLOAD_REQUIRED_MEDIA_JSON_BY_POST";
        public static String _3_DOWNLOAD_MY_MEDIA_DATA = "3_DOWNLOAD_MY_MEDIA_DATA";
        public static String _4_UPDATE_POST_WITH_CTM_AND_PREPARE_CSV = "4_UPDATE_POST_WITH_CTM_AND_PREPARE_CSV";
        public static String _2_1_DOWNLOAD_MEDIA_IMAGES = "_2_1_DOWNLOAD_MEDIA_IMAGES";
        public static String COLLECT_REQUIRED_MEDIA_JSON = "COLLECT_REQUIRED_MEDIA_JSON";
    }
}
