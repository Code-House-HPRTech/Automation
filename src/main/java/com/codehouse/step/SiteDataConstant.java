package com.codehouse.step;

import com.codehouse.dto.SiteInfo;

import java.util.ArrayList;
import java.util.List;

public class SiteDataConstant {
    public static final String MY_SITE_URL = "https://worldsports7.com";

    public static final String DATE_RANGE
            = "&after=2024-09-30T00:00:00" +
            "&before=2024-09-30T23:59:59";

    public static List<SiteInfo> getSiteList() {
        List<SiteInfo> SITE_LIST = new ArrayList<>();

        // Wrestling sites
        {
            SITE_LIST.add(new SiteInfo("https://wrestlingnews.co", "wrestlingnews", "WWE"));
            SITE_LIST.add(new SiteInfo("https://www.wrestlezone.com", "wrestlezone", "WWE"));
            SITE_LIST.add(new SiteInfo("https://wrestlingheadlines.com", "wrestlingheadlines", "WWE"));
            SITE_LIST.add(new SiteInfo("https://www.ringsidenews.com", "ringsidenews", "WWE"));
            SITE_LIST.add(new SiteInfo("https://www.pwmania.com", "pwmania", "WWE"));
            SITE_LIST.add(new SiteInfo("https://www.ewrestlingnews.com", "ewrestlingnews", "WWE"));
        }

        // Cricket sites
        {
            SITE_LIST.add(new SiteInfo("https://cricfit.com", "cricfit", "Cricket"));
            SITE_LIST.add(new SiteInfo("https://cricblog.net", "cricblog", "Cricket"));
            SITE_LIST.add(new SiteInfo("https://www.cricketcountry.com", "cricketcountry", "Cricket"));
        }

        // Football sites
        {
            SITE_LIST.add(new SiteInfo("https://www.caughtoffside.com", "caughtoffside", "Football"));
            SITE_LIST.add(new SiteInfo("https://football-italia.net", "footballitalia", "Football"));
            SITE_LIST.add(new SiteInfo("https://www.101greatgoals.com", "greatgoals", "Football"));
            SITE_LIST.add(new SiteInfo("https://thefootballfaithful.com", "footballfaithful", "Football"));
            SITE_LIST.add(new SiteInfo("https://totalfootballanalysis.com", "footballanalysis", "Football"));
            SITE_LIST.add(new SiteInfo("https://football-talk.co.uk", "footballtalk", "Football"));
            SITE_LIST.add(new SiteInfo("https://www.squawka.com", "squawka", "Football"));
            SITE_LIST.add(new SiteInfo("https://www.foottheball.com", "foottheball", "Football"));
        }

        // Tennis sites
        {
            SITE_LIST.add(new SiteInfo("https://www.lovetennisblog.com", "lovetennisblog", "Tennis"));
            SITE_LIST.add(new SiteInfo("https://www.tennistonic.com", "tennistonic", "Tennis"));
            SITE_LIST.add(new SiteInfo("https://www.tennishead.net", "tennishead", "Tennis"));
            SITE_LIST.add(new SiteInfo("https://www.badmintonplanet.com", "badmintonplanet", "Tennis"));
            SITE_LIST.add(new SiteInfo("https://www.badzine.net", "badzine", "Tennis"));
            SITE_LIST.add(new SiteInfo("https://shuttlesmash.com", "shuttlesmash", "Tennis"));
            SITE_LIST.add(new SiteInfo("https://badmintonfamly.com", "badmintonfamly", "Tennis"));
        }

        // Hockey sites
        {
            SITE_LIST.add(new SiteInfo("https://www.thehockeywriters.com", "hockeywriters", "Hockey"));
            SITE_LIST.add(new SiteInfo("https://calgaryhockeynow.com", "calgaryhockey", "Hockey"));
            SITE_LIST.add(new SiteInfo("https://www.litterboxcats.com", "litterboxcats", "Hockey"));
            SITE_LIST.add(new SiteInfo("https://www.ontheforecheck.com", "ontheforecheck", "Hockey"));
            SITE_LIST.add(new SiteInfo("https://www.arcticicehockey.com", "arcticicehockey", "Hockey"));
        }
        return SITE_LIST;
    }
}
