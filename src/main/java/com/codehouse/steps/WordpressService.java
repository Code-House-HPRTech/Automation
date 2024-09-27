package com.codehouse.steps;

import com.codehouse.contants.Constant;
import com.codehouse.util.Utils;

import java.io.IOException;


public class WordpressService {
    public static void downloadAndPrepareData(String siteUrl, String mySiteUrl, String folderName, String operationType) throws IOException {
        Utils.disableSSLValidation();

        if (Constant.OperationType._0_DOWNLOAD_DATA.equals(operationType) || Constant.OperationType._1_DOWNLOAD_DATA_AND_MEDIA.equals(operationType)) {

            // Download and prepare all json files
            Utils.downloadData("posts", String.format(Constant.POSTS_JSON_FILE_PATH, folderName), "&_fields=id,title,content,excerpt,featured_media,categories,date,slug,tags,status", siteUrl);
            Utils.downloadData("pages", String.format(Constant.PAGES_JSON_FILE_PATH, folderName), "", siteUrl);
            Utils.downloadData("categories", String.format(Constant.CATEGORIES_JSON_FILE_PATH, folderName), "&_fields=id,name", siteUrl);
            Utils.downloadData("tags", String.format(Constant.TAGS_JSON_FILE_PATH, folderName), "&_fields=id,name", siteUrl);

            if (operationType.contains("_MEDIA")) {
                // Download Media Json
                Utils.downloadData("media", String.format(Constant.MEDIA_JSON_FILE_PATH, folderName), "&_fields=id,guid", siteUrl);

                // Download Media Images
                MediaService.downloadMedia(String.format(Constant.MEDIA_JSON_FILE_PATH, folderName));
            }
        }

        if (Constant.OperationType._2_DOWNLOAD_REQUIRED_MEDIA_BY_POST.equals(operationType)) {
            // Download Required Media
            MediaService.downloadRequiredMediaByPost(String.format(Constant.MEDIA_JSON_FILE_PATH, folderName));
        }

        if (Constant.OperationType._3_DOWNLOAD_MY_MEDIA_DATA.equals(operationType)) {
            // Download My Media from my Site
            Utils.downloadData("media", String.format(Constant.MY_MEDIA_JSON_FILE_PATH, folderName), "&_fields=id,guid", mySiteUrl);
        }

        if (Constant.OperationType._4_UPDATE_POST_WITH_CTM_AND_PREPARE_CSV.equals(operationType)) {
            // Mapping category to it's name
            CategoryAndTagService.updateCategoryAndTag("categories",
                    String.format(Constant.CATEGORIES_JSON_FILE_PATH, folderName),
                    String.format(Constant.POSTS_JSON_FILE_PATH, folderName));

            // Mapping tags to it's name
            CategoryAndTagService.updateCategoryAndTag("tags",
                    String.format(Constant.TAGS_JSON_FILE_PATH, folderName),
                    String.format(Constant.POSTS_JSON_FILE_PATH, folderName));

            // Update feature image media id
            FeatureImageService.updateFeatureImageId(
                    String.format(Constant.MEDIA_JSON_FILE_PATH, folderName),
                    String.format(Constant.MY_MEDIA_JSON_FILE_PATH, folderName));

            // Create Batch CSV file
            CsvConverterService.convertBatchToCsv(
                    String.format(Constant.POSTS_JSON_FILE_PATH, folderName),
                    String.format(Constant.CSV_FOLDER_PATH, folderName)
            );

            // Replace site url to my site url
            PowerShellService.replaceStringWithMyStringInCsv(String.format(Constant.CSV_FOLDER_PATH, folderName),
                    siteUrl, mySiteUrl);

            // Replace site media url to my site media url
            PowerShellService.replaceStringWithMyStringInCsv(String.format(Constant.CSV_FOLDER_PATH, folderName),
                    siteUrl, mySiteUrl);
        }
    }
}