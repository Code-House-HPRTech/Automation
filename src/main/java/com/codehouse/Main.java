package com.codehouse;

import com.codehouse.contants.Constant;
import com.codehouse.service.WordpressService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String MY_SITE_URL = "https://worldsports7.com";
    public static final String DATA_RANGE
            = "&after=2024-09-25T00:00:00" +
            "&before=2024-09-27T23:59:59";

    public static final Map<String, String> SITE_DIR_MAP = new HashMap<>();

    static {
        SITE_DIR_MAP.put("https://wrestlingnews.co", "wrestlingnews");
//        SITE_DIR_MAP.put("https://www.wrestlezone.com", "wrestlezone");
//        SITE_DIR_MAP.put("https://wrestlingheadlines.com", "wrestlingheadlines");
//        SITE_DIR_MAP.put("https://www.ringsidenews.com", "ringsidenews");
//        SITE_DIR_MAP.put("https://www.pwmania.com", "pwmania");
//        SITE_DIR_MAP.put("https://www.ewrestlingnews.com", "ewrestlingnews");
    }

    public static void main(String[] args) {
        /*
            Operation Type:
                _0_DOWNLOAD_ONLY_JSON
                _1_DOWNLOAD_ALL_JSON_WITH_MEDIA
                _2_DOWNLOAD_REQUIRED_MEDIA_JSON_BY_POST
                _3_DOWNLOAD_MY_MEDIA_DATA
                _4_UPDATE_POST_WITH_CTM_AND_PREPARE_CSV
                _2_1_DOWNLOAD_MEDIA_IMAGES
         */
        String operationType = Constant.OperationType._4_UPDATE_POST_WITH_CTM_AND_PREPARE_CSV;

        SITE_DIR_MAP.forEach((siteUrl, folderName) -> {
            try {
                // Create Directory if not exist
                Path folder = Path.of(String.format(Constant.WP_DATA_BASE_PATH, folderName));
                Files.createDirectories(folder);

                // Call actual Service
                WordpressService.downloadAndPrepareData(
                        siteUrl,
                        MY_SITE_URL,
                        folderName,
                        operationType,
                        DATA_RANGE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}