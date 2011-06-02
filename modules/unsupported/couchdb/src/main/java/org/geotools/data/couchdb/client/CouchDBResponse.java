/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.couchdb.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Ian Schneider (OpenGeo)
 */
public final class CouchDBResponse {
    private final HttpMethod request;
    private final int result;
    private final IOException exception;
    private final Object json;
    private final boolean isArray;

    public CouchDBResponse(HttpMethod request, int result, IOException exception) throws IOException {
        this.request = request;
        this.result = result;
        this.exception = exception;
        
        boolean err = !isHttpOK();
        
        InputStream response = request.getResponseBodyAsStream();
        if (err) {
            if (exception != null) {
                throw new IOException("HTTP error",exception);
            }
            if (response == null) {
                throw new IOException("HTTP error : " + result);
            }
        }
        json = JSONValue.parse(new InputStreamReader(request.getResponseBodyAsStream()));
        if (err) {
            isArray = false;
        } else {
            isArray = json instanceof JSONArray;
        }
    }
    
    public void checkOK(String msg) throws CouchDBException {
        if (!isHttpOK()) {
            String fullMsg = msg + "," + getErrorAndReason();
            throw new CouchDBException(fullMsg,exception);
        } 
    }
    
    private String getErrorAndReason() {
        if (!isHttpOK()) {
            JSONObject err = (JSONObject) json;
            String res = null;
            if (err.containsKey("error")) {
                res = err.get("error").toString();
            }
            if (err.containsKey("reason")) {
                res = res == null ? "" : res + ",";
                res = res + err.get("reason").toString();
            }
            if (res == null) {
                res = err.toJSONString();
            }
            return res;
        } else {
            throw new IllegalStateException("not an error");
        }
    }

    public boolean isHttpOK() {
        return result >= 200 && result < 300;
    }
    
    public boolean isCouchOK() {
        boolean ok = false;
        JSONObject obj = (JSONObject) json;
        return obj.get("ok").equals("true");
    }

    public Throwable getException() {
        return exception;
    }
    
    public InputStream getResponseStream() throws IOException {
        return request.getResponseBodyAsStream();
    }
    
    public String getBody() throws IOException {
        return request.getResponseBodyAsString();
    }

    public JSONArray getBodyAsJSONArray() throws IOException {
        if (!isArray) {
            throw new IllegalStateException("Response is not an array");
        }
        return (JSONArray) json;
    }

    JSONObject getBodyAsJSONObject() throws IOException {
        if (isArray) {
            throw new IllegalStateException("Response is not an object");
        }
        return (JSONObject) json;
    }
    
    

    
}
