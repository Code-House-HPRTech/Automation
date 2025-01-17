package com.codehouse.step;

import com.codehouse.dto.SiteInfo;
import com.codehouse.contants.Constant;
import com.codehouse.service.MediaService;

public class S3_1DownloadRequiredImage {
    public static void main(String[] args) {
        SiteDataConstant.getSiteList().forEach(S3_1DownloadRequiredImage::performAction);
    }

    public static void performAction(SiteInfo siteInfo) {
        String folderName = siteInfo.getFolderName();

        // Download Media Images
        MediaService.downloadMedia(
                String.format(Constant.REQUIRED_MEDIA_JSON_FILE_PATH, folderName),
                String.format(Constant.MEDIA_FOLDER_PATH, folderName)
        );
    }
}
