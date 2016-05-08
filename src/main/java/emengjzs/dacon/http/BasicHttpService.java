package emengjzs.dacon.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import emengjzs.dacon.config.WebServiceConfig;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("basicHttpService")
public class BasicHttpService implements HttpService {

    @Autowired
    protected RestTemplate restTemplate;

    protected URLGetter urlGetter;

    protected List<HTTPRequestListener> listeners = new ArrayList<HTTPRequestListener>();

    public BasicHttpService() {
        listeners.add(new HTTPRequestLogListener());
    }

    public void setDefaultHost(String host) {
        this.urlGetter = new URLGetter(host);
    }

    @Autowired
    public void setDefaultHost(WebServiceConfig config) {
        this.urlGetter = new URLGetter(config.getWebHostUrl());
    }

    public HttpService getInstance(String host) {
        BasicHttpService service = new BasicHttpService();
        service.setDefaultHost(host);
        service.restTemplate = restTemplate;
        return service;
    }

    // public void setUrlGetter(URLGetter urlGetter) {
    // this.urlGetter = urlGetter;
    // }


    public JSONResponse postForJSONResponse(String url, Object request, Object... urlVariables) {
        return new JSONResponse(postForObject(url, request, JSONObject.class, urlVariables));
    }


    public JSONResponse postForJSONResponse(URLInfo url, Object request, Object... urlVariables) {
        return new JSONResponse(postForObject(url, request, JSONObject.class, urlVariables));

    }

    public JSONObject postForObject(String url, Object request, Object... urlVariables) {
        return postForObject(url, request, JSONObject.class, urlVariables);
    }

    public <T> T postForObject(URLInfo url, Object request, Class<T> resurnType, Object... urlVariables) {
        return postForObject(urlGetter.getFullUrl(url), request, resurnType, urlVariables);
    }

    public JSONObject postForObject(URLInfo url, Object request, Object... urlVariables) {
        return postForObject(url, request, JSONObject.class, urlVariables);

    }

    @SuppressWarnings("unchecked")
    public <T> T postForObject(String url, Object request, Class<T> resurnType, Object... urlVariables) {
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
        Map<String, Object> jsonObject;
        if (request.getClass().isAssignableFrom(Map.class)) {
            jsonObject = (Map<String, Object>) request;
        } else {
            jsonObject = (Map<String, Object>) JSON.toJSON(request);
        }

        for (Entry<String, Object> item : jsonObject.entrySet()) {
            requestEntity.add(item.getKey(), String.valueOf(item.getValue()));
        }
        for (HTTPRequestListener l : listeners) {
            l.beforeRequest(url, request);
        }
        ResponseEntity<T> result = this.restTemplate.postForEntity(url, requestEntity, resurnType, urlVariables);
        for (HTTPRequestListener l : listeners) {
            l.afterResponse(url, request, result);
        }

        return result.getBody();
    }

    public List<JSONResponse> concurrentPostForJSONResponse(URLInfo url, List<Object> reqs)
            throws InterruptedException {
        return this.concurrentPostForJSONResponse(urlGetter.getFullUrl(url), reqs);
    }

    public List<JSONResponse> concurrentPostForJSONResponse(final String url, List<Object> reqs)
            throws InterruptedException {
        int threadNum = reqs.size();
        final CountDownLatch threadSignal = new CountDownLatch(threadNum);
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        final JSONResponse[] ress = new JSONResponse[threadNum];
        for (int i = 0; i < threadNum; ++i) {
            final int index = i;
            final Object req = reqs.get(i);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ress[index] = postForJSONResponse(url, req);
                    threadSignal.countDown();
                }
            });
        }
        threadSignal.await();
        executor.shutdown();
        return Arrays.asList(ress);

    }


    public HttpClient getClient() {
        return ((HttpComponentsClientHttpRequestFactory) this.restTemplate.getRequestFactory()).getHttpClient();
    }
}
