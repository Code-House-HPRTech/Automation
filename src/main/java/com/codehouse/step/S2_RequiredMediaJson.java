package com.codehouse.step;

import com.codehouse.contants.SiteInfo;
import com.codehouse.contants.Constant;
import com.codehouse.service.MediaService;
import com.codehouse.util.Utils;
import com.codehouse.util.WordPressUtils;

public class S2_RequiredMediaJson {
    public static void main(String[] args) {
        performAction(SiteDataConstant.getSiteInfo());
    }

    public static void performAction(SiteInfo siteInfo) {
        String folderName = siteInfo.getFolderName();
        String siteUrl = siteInfo.getSiteUrl();

        MediaService.downloadRequiredMediaByPost(
                String.format(Constant.POSTS_JSON_FILE_PATH, folderName),
                String.format(Constant.MEDIA_JSON_FILE_PATH, folderName),
                siteUrl
        );

        // Download My Media for Downloading only required media
        Utils.copyFile(
                Constant.COMMON_MEDIA_JSON_FILE_PATH,
                String.format(Constant.MY_MEDIA_JSON_FILE_PATH, folderName)
        );

        // Prepare required media json
        WordPressUtils.collectOnlyRequiredMediaJson(
                String.format(Constant.MEDIA_JSON_FILE_PATH, folderName),
                String.format(Constant.MY_MEDIA_JSON_FILE_PATH, folderName),
                String.format(Constant.REQUIRED_MEDIA_JSON_FILE_PATH, folderName)
        );
    }
}
