package emengjzs.dacon.http;

public class URLGetter {
    private String host;
    private String prefix = "";

    public URLGetter() {

    }

    public URLGetter(String host) {
        if (!host.startsWith("http://")) {
            prefix = "http://";
        }
        this.host = host;
    }

    public String getFullUrl(URLInfo url) {
        return prefix + this.host + url.getURL();
    }

    public String getFullUrl(String uri) {
        return prefix + this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
