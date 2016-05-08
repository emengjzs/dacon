package emengjzs.dacon.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class HTTPRequestLogListener implements HTTPRequestListener {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public HTTPRequestLogListener() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void beforeRequest(String url, Object request) {
        if (logger.isDebugEnabled()) {
            logger.debug("\nPOST [{}] \n <= PARA {}", url,
                    JSON.toJSONString(request, SerializerFeature.PrettyFormat).replace("\n", "\n    "));
        }
    }

    @Override
    public void afterResponse(String url, Object requset, ResponseEntity<?> response) {
        if (logger.isDebugEnabled()) {
            logger.debug("\nRESPONSE [{}] \n => DATA \n    {}",
                    url, JSON.toJSONString(response.getBody(),
                            SerializerFeature.PrettyFormat).replace("\n", "\n    "));

        }
    }

}
