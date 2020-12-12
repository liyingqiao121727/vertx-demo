package com.bgi.util;

import com.bgi.vtx.Config;
import io.vertx.core.http.HttpServerResponse;

public class CommonUtil {

    public static String object2String(Object obj) {
        if (null == obj) {
            return null;
        }
        return String.valueOf(obj);
    }

    public static void accessControlAllow(HttpServerResponse response) {
        response.putHeader("Access-Control-Allow-Origin", "*");
        response.putHeader("Access-Control-Allow-Credentials", "true");
        response.putHeader("Access-Control-Allow-Methods", "*");
        response.putHeader("Access-Control-Allow-Headers", "*");
        response.putHeader("Access-Control-Expose-Headers", "*");
    }

    public static void responseEnd(HttpServerResponse response) {
        Config.responseEnd(response);
    }

}
