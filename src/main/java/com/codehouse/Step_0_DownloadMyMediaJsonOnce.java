package com.codehouse;

import com.codehouse.contants.Constant;
import com.codehouse.service.WordpressService;
import com.codehouse.step.SiteDataConstant;
import com.codehouse.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Step_0_DownloadMyMediaJsonOnce {

    public static void main(String[] args) {
        // Download My Media for Downloading only required media
        Utils.downloadData("media",
                Constant.COMMON_MEDIA_JSON_FILE_PATH,
                "&_fields=id,guid", Main.MY_SITE_URL);
    }
}