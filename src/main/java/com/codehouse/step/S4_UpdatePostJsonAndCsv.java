package com.codehouse.step;

import com.codehouse.dto.SiteInfo;
import com.codehouse.contants.Constant;
import com.codehouse.service.CategoryAndTagService;
import com.codehouse.service.CsvConverterService;
import com.codehouse.service.PowerShellService;

public class S4_UpdatePostJsonAndCsv {
    public static void main(String[] args) {
        SiteDataConstant.getSiteList().forEach(S4_UpdatePostJsonAndCsv::performAction);
    }

    public static void performAction(SiteInfo siteInfo) {
        String folderName = siteInfo.getFolderName();
        String siteUrl = siteInfo.getSiteUrl();
        String defaultCategory = siteInfo.getDefaultCategory();

        {
            // Mapping category to it's name
            CategoryAndTagService.updateCategoryAndTag("categories",
                    String.format(Constant.CATEGORIES_JSON_FILE_PATH, folderName),
                    String.format(Constant.POSTS_JSON_FILE_PATH, folderName));
        }

        {
            // Mapping tags to it's name
            CategoryAndTagService.updateCategoryAndTag("tags",
                    String.format(Constant.TAGS_JSON_FILE_PATH, folderName),
                    String.format(Constant.POSTS_JSON_FILE_PATH, folderName));
        }

//        {
//            // Update feature image media id
//            FeatureImageService.updateFeatureImageId(
//                    String.format(Constant.MEDIA_JSON_FILE_PATH, folderName),
//                    String.format(Constant.MY_MEDIA_JSON_FILE_PATH, folderName),
//                    String.format(Constant.POSTS_JSON_FILE_PATH, folderName));
//        }

        {
            // Create Batch CSV file
            CsvConverterService.convertBatchToCsv(
                    String.format(Constant.POSTS_JSON_FILE_PATH, folderName),
                    String.format(Constant.CSV_FOLDER_PATH, folderName),
                    defaultCategory
            );
        }

        // Replace site url to my site url
        PowerShellService.replaceStringWithMyStringInCsv(String.format(Constant.CSV_FOLDER_PATH, folderName),
                siteUrl, SiteDataConstant.MY_SITE_URL);

        PowerShellService.replaceStringWithMyStringInCsv(String.format(Constant.CSV_FOLDER_PATH, folderName),
                "wp-content/uploads/\\d{4}/\\d{2}/", "wp-content/uploads/2024/09/");

    }
}
