package com.codehouse;

import com.codehouse.contants.Constant;
import com.codehouse.contants.SiteInfo;
import com.codehouse.service.WordpressService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static final String MY_SITE_URL = "https://worldsports7.com";
    public static final String DATA_RANGE
            = "&after=2024-09-30T00:00:00" +
            "&before=2024-09-30T23:59:59";

    public static void main(String[] args) {
        /*
            Operation Type:
                _0_DOWNLOAD_ONLY_JSON
                _1_DOWNLOAD_ALL_JSON_WITH_MEDIA
                _2_DOWNLOAD_REQUIRED_MEDIA_JSON_BY_POST
                _2_1_DOWNLOAD_MEDIA_IMAGES
                _3_DOWNLOAD_MY_MEDIA_DATA
                _4_UPDATE_POST_WITH_CTM_AND_PREPARE_CSV

         */
        String operationType = Constant.OperationType
                ._2_DOWNLOAD_REQUIRED_MEDIA_JSON_BY_POST;

        SiteInfo.SITE_LIST.forEach(siteInfo -> {
            System.out.println("----> Processing Url: " + siteInfo.getSiteUrl());
            try {
                // Create Directory if not exist
                Path folder = Path.of(String.format(Constant.WP_DATA_BASE_PATH, siteInfo.getFolderName()));
                Files.createDirectories(folder);

                // Call actual Service
                WordpressService.downloadAndPrepareData(
                        siteInfo.getSiteUrl(),
                        MY_SITE_URL,
                        siteInfo.getFolderName(),
                        siteInfo.getDefaultCategory(),
                        operationType,
                        DATA_RANGE);
            } catch (IOException e) {
                System.out.println("Exception Occurred: " + e.getMessage());
            }
        });
    }
}