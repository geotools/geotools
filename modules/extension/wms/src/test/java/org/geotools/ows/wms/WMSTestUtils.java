package org.geotools.ows.wms;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.WWWFormCodec;

public class WMSTestUtils {

    public static Map<String, String> parseParams(String query) {
        List<NameValuePair> params = WWWFormCodec.parse(query, StandardCharsets.UTF_8);
        Map<String, String> result = new HashMap<>();
        for (NameValuePair param : params) {
            result.put(param.getName().toUpperCase(), param.getValue());
        }
        return result;
    }
}
