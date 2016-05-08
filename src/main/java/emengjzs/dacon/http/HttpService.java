package emengjzs.dacon.http;

import org.apache.http.client.HttpClient;

public interface HttpService {

    public JSONResponse postForJSONResponse(String url, Object request, Object... urlVariables);

    public JSONResponse postForJSONResponse(URLInfo url, Object request, Object... urlVariables);

    public HttpClient getClient();

}