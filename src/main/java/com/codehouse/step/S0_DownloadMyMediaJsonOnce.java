package com.codehouse.step;

import com.codehouse.Main;
import com.codehouse.contants.Constant;
import com.codehouse.util.Utils;

public class S0_DownloadMyMediaJsonOnce {

    public static void main(String[] args) {
        // Download My Media for Downloading only required media
        Utils.downloadData("media",
                Constant.COMMON_MEDIA_JSON_FILE_PATH,
                "&_fields=id,guid",
                SiteDataConstant.MY_SITE_URL);
    }
}