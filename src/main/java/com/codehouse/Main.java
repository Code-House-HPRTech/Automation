package com.codehouse;

import com.codehouse.contants.Constant;
import com.codehouse.steps.WordpressService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String MY_SITE_URL = "";

    public static final Map<String, String> SITE_DIR_MAP = new HashMap<>();

    static {
        SITE_DIR_MAP.put("wrestling.co", "wrestling");
        SITE_DIR_MAP.put("", "");
        SITE_DIR_MAP.put("", "");
        SITE_DIR_MAP.put("", "");
        SITE_DIR_MAP.put("", "");
    }

    public static void main(String[] args) {
        /*
        Operation Type:
        1. DOWNLOAD_DATA_AND_MEDIA
        2. DOWNLOAD_MY_MEDIA_DATA
        3. UPDATE_POST_WITH_CTM_AND_PREPARE_CSV
         */
        String operationType = Constant.OperationType._1_DOWNLOAD_DATA_AND_MEDIA;

        SITE_DIR_MAP.forEach((siteUrl, folderName) -> {
            try {
                WordpressService.downloadAndPrepareData(siteUrl, MY_SITE_URL, folderName, operationType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
