package com.codehouse.step;

import com.codehouse.contants.SiteInfo;

public class SiteDataConstant {
    public static final String MY_SITE_URL = "https://worldsports7.com";

    public static final String DATE_RANGE
            = "&after=2024-09-30T00:00:00" +
            "&before=2024-09-30T23:59:59";

    public static SiteInfo getSiteInfo() {
        return new SiteInfo(
                "https://wrestlingnews.co",
                "wrestlingnews",
                "WWE");
    }
}
