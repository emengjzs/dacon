package emengjzs.dacon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;

public class VMUtil {

    public VMUtil() {

    }

    @SuppressWarnings("unchecked")
    public static String render(VelocityEngine velocityEngine, String template, Object... model) {
        JSONObject o = new JSONObject();
        for (Object m : model) {
            o.putAll((Map<String, Object>) JSONObject.toJSON(m));
        }
        VelocityContext context = new VelocityContext(o);
        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(context, writer, "", template);
        return writer.toString();
    }

    public static JSONObject renderJSON(VelocityEngine velocityEngine, String template, Object... model) {
        return (JSONObject) JSON.parse(render(velocityEngine, template, model));
    }
}
