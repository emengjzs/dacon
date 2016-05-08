package emengjzs.dacon.http;

import org.apache.http.client.HttpClient;

public class DefaultHostHttpService implements HttpService {

    private BasicHttpService httpService;

    private URLGetter urlGetter;

    public DefaultHostHttpService(BasicHttpService httpService, String host) {
        this.httpService = httpService;
        this.urlGetter = new URLGetter(host);
    }

    @Override
    public JSONResponse postForJSONResponse(String url, Object request, Object... urlVariables) {
        return this.httpService.postForJSONResponse(urlGetter.getFullUrl(url), request, urlVariables);
    }

    @Override
    public JSONResponse postForJSONResponse(URLInfo url, Object request, Object... urlVariables) {
        return this.httpService.postForJSONResponse(urlGetter.getFullUrl(url), request, urlVariables);
    }

    @Override
    public HttpClient getClient() {
        return this.httpService.getClient();
    }

}
