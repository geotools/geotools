package org.geotools.data.wfs.internal;

import java.util.Collections;
import java.util.Map;

import net.opengis.wfs.GetFeatureType;

/**
 * Holds the components needed by the data store to issue and post process a GetFeature request.
 */
public class RequestComponents {

    /**
     * The GetFeature request to issue to the WFS
     */
    private GetFeatureType serverRequest;

    private Map<String, String> kvpParameters;

    public GetFeatureType getServerRequest() {
        return serverRequest;
    }

    public void setServerRequest(GetFeatureType serverRequest) {
        this.serverRequest = serverRequest;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getKvpParameters() {
        return kvpParameters == null ? Collections.EMPTY_MAP : kvpParameters;
    }

    public void setKvpParameters(Map<String, String> kvpParameters) {
        this.kvpParameters = kvpParameters;
    }
}
