package dacon.test.config;

import emengjzs.dacon.http.URLInfo;

/**
 * Created by emengjzs on 2016/4/15.
 */
public enum URLSet implements URLInfo {
    ;

    private String url;

    private URLSet(String url) {
        this.url = url;
    }

    @Override
    public String getURL() {
        return url;
    }
}
