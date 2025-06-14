/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * SAP HANA connection parameters.
 *
 * @author Stefan Uhrig, SAP SE
 */
public final class HanaConnectionParameters {

    public static HanaConnectionParameters forPort(String host, int port) {
        return forPort(host, port, null);
    }

    public static HanaConnectionParameters forPort(String host, int port, Map<String, String> additionalOptions) {
        return new HanaConnectionParameters(host, port, additionalOptions);
    }

    public static HanaConnectionParameters forSingleContainer(String host, int instance) {
        return forSingleContainer(host, instance, null);
    }

    public static HanaConnectionParameters forSingleContainer(
            String host, int instance, Map<String, String> additionalOptions) {
        Map<String, String> options = new HashMap<>();
        if (additionalOptions != null) {
            options.putAll(additionalOptions);
        }
        options.put("instanceNumber", Integer.toString(instance));
        return new HanaConnectionParameters(host, null, options);
    }

    public static HanaConnectionParameters forMultiContainerSystemDatabase(String host, int instance) {
        return forMultiContainer(host, instance, "SYSTEMDB");
    }

    public static HanaConnectionParameters forMultiContainerSystemDatabase(
            String host, int instance, Map<String, String> additionalOptions) {
        return forMultiContainer(host, instance, "SYSTEMDB", additionalOptions);
    }

    public static HanaConnectionParameters forMultiContainer(String host, int instance, String database) {
        return forMultiContainer(host, instance, database, null);
    }

    public static HanaConnectionParameters forMultiContainer(
            String host, int instance, String database, Map<String, String> additionalOptions) {
        Map<String, String> options = new HashMap<>();
        if (additionalOptions != null) {
            options.putAll(additionalOptions);
        }
        options.put("instanceNumber", Integer.toString(instance));
        options.put("databaseName", database);
        return new HanaConnectionParameters(host, null, options);
    }

    private HanaConnectionParameters(String host, Integer port, Map<String, String> additionalOptions) {
        this.host = host;
        this.port = port;
        this.additionalOptions = additionalOptions;
    }

    private String host;

    private Integer port;

    private Map<String, String> additionalOptions;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public Map<String, String> getAdditionalOptions() {
        return additionalOptions == null ? null : Collections.unmodifiableMap(additionalOptions);
    }

    /**
     * Builds a JDBC connection URL.
     *
     * @return Returns the JDBC connection URL corresponding to these parameters.
     */
    public String buildUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sap://");
        sb.append(host);
        if (port != null) {
            sb.append(":");
            sb.append(port.toString());
        }
        if (additionalOptions != null && !additionalOptions.isEmpty()) {
            sb.append("/?");
            boolean first = true;
            for (Map.Entry<String, String> option : additionalOptions.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append('&');
                }
                try {
                    sb.append(URLEncoder.encode(option.getKey(), "UTF-8"));
                    sb.append('=');
                    sb.append(URLEncoder.encode(option.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new AssertionError(e);
                }
            }
        }
        return sb.toString();
    }
}
