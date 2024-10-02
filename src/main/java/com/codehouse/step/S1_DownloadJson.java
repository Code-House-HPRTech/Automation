package com.codehouse.step;

import com.codehouse.contants.SiteInfo;
import com.codehouse.contants.Constant;
import com.codehouse.util.Utils;

import java.io.IOException;

public class S1_DownloadJson {
    public static void main(String[] args) throws IOException {
        performAction(SiteDataConstant.getSiteInfo());
    }

    public static void performAction(SiteInfo siteInfo) throws IOException {
        String folderName = siteInfo.getFolderName();
        String siteUrl = siteInfo.getSiteUrl();

        // Delete existing folder first
        Utils.deleteAndCreateFolder(String.format(Constant.WP_DATA_BASE_PATH, folderName));

        // Download and prepare all json files
        Utils.downloadData("posts", String.format(Constant.POSTS_JSON_FILE_PATH, folderName), SiteDataConstant.DATE_RANGE + "&_fields=id,title,content,excerpt,featured_media,categories,date,slug,tags,status", siteUrl);
//        // Utils.downloadData("pages", String.format(Constant.PAGES_JSON_FILE_PATH, folderName), "", siteUrl);
//        Utils.downloadData("categories", String.format(Constant.CATEGORIES_JSON_FILE_PATH, folderName), "&_fields=id,name", siteUrl);
//        Utils.downloadData("tags", String.format(Constant.TAGS_JSON_FILE_PATH, folderName), "&_fields=id,name", siteUrl);

    }
}
