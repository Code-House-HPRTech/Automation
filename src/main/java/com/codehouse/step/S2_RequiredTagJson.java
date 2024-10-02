package com.codehouse.step;

import com.codehouse.contants.Constant;
import com.codehouse.dto.SiteInfo;
import com.codehouse.service.MediaService;

public class S2_RequiredTagJson {
    public static void main(String[] args) {
        // Call actual Service
        SiteDataConstant.getSiteList().forEach(S2_RequiredTagJson::performAction);
    }

    public static void performAction(SiteInfo siteInfo) {
        String folderName = siteInfo.getFolderName();
        String siteUrl = siteInfo.getSiteUrl();

        MediaService.downloadRequiredTagByPost(
                String.format(Constant.POSTS_JSON_FILE_PATH, folderName),
                String.format(Constant.TAGS_JSON_FILE_PATH, folderName),
                siteUrl
        );
    }
}
