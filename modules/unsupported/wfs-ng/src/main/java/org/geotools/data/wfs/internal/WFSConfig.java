/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.impl.WFSDataAccessFactory.AXIS_ORDER;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.AXIS_ORDER_FILTER;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.BUFFER_SIZE;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.ENCODING;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.ENTITY_RESOLVER;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.FILTER_COMPLIANCE;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.GML_COMPATIBLE_TYPENAMES;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.LENIENT;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.MAXFEATURES;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.MAX_CONNECTION_POOL_SIZE;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.NAMESPACE;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.OUTPUTFORMAT;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.PASSWORD;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.PROTOCOL;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.TIMEOUT;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.TRY_GZIP;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.USERNAME;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.USE_HTTP_CONNECTION_POOLING;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.WFS_STRATEGY;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.xml.sax.EntityResolver;

/** @see WFSStrategy#setConfig(WFSConfig) */
public class WFSConfig {

    private static final Logger LOGGER = Logging.getLogger(WFSConfig.class);

    protected String user;

    protected String pass;

    protected int timeoutMillis;

    protected PreferredHttpMethod preferredMethod;

    protected int buffer;

    protected boolean tryGZIP;

    protected boolean lenient;

    protected Integer maxFeatures;

    protected Charset defaultEncoding;

    protected String wfsStrategy;

    protected Integer filterCompliance;

    protected String namespaceOverride;

    protected String outputformatOverride;

    protected boolean useDefaultSrs;

    protected String axisOrder;

    protected String axisOrderFilter;

    protected boolean gmlCompatibleTypenames;

    protected boolean useHttpConnectionPooling;

    protected int maxConnectionPoolSize;

    protected EntityResolver entityResolver;

    protected Map<String, String> additionalHeaders;

    public static enum PreferredHttpMethod {
        AUTO,
        HTTP_GET,
        HTTP_POST
    }

    private static final String NAME_SEPARATOR = ":";
    private static final String NAME_SEPARATOR_GML_COMPATIBLE = "_";

    public WFSConfig() {
        preferredMethod = PreferredHttpMethod.AUTO;
        timeoutMillis = (Integer) TIMEOUT.getDefaultValue();
        buffer = (Integer) BUFFER_SIZE.getDefaultValue();
        tryGZIP = (Boolean) TRY_GZIP.getDefaultValue();
        lenient = (Boolean) LENIENT.getDefaultValue();
        String encoding = (String) ENCODING.getDefaultValue();
        defaultEncoding = Charset.forName(encoding);
        maxFeatures = (Integer) MAXFEATURES.getDefaultValue();
        wfsStrategy = (String) WFS_STRATEGY.getDefaultValue();
        filterCompliance = (Integer) FILTER_COMPLIANCE.getDefaultValue();
        namespaceOverride = (String) NAMESPACE.getDefaultValue();
        gmlCompatibleTypenames = (Boolean) GML_COMPATIBLE_TYPENAMES.getDefaultValue();
        entityResolver = (EntityResolver) ENTITY_RESOLVER.getDefaultValue();
        useHttpConnectionPooling = (Boolean) USE_HTTP_CONNECTION_POOLING.getDefaultValue();
        maxConnectionPoolSize = (Integer) MAX_CONNECTION_POOL_SIZE.getDefaultValue();
    }

    public static WFSConfig fromParams(Map<?, ?> params) throws IOException {

        WFSConfig config = new WFSConfig();

        Boolean preferPost = PROTOCOL.lookUp(params);

        if (preferPost == null) {
            config.preferredMethod = PreferredHttpMethod.AUTO;
        } else {
            config.preferredMethod =
                    preferPost.booleanValue() ? PreferredHttpMethod.HTTP_POST : PreferredHttpMethod.HTTP_GET;
        }

        config.user = USERNAME.lookUp(params);
        config.pass = PASSWORD.lookUp(params);
        config.timeoutMillis = TIMEOUT.lookUp(params);
        config.buffer = BUFFER_SIZE.lookUp(params);
        config.tryGZIP = TRY_GZIP.lookUp(params);
        config.lenient = LENIENT.lookUp(params);

        String encoding = ENCODING.lookUp(params);
        config.defaultEncoding = Charset.forName(encoding);

        config.maxFeatures = MAXFEATURES.lookUp(params);
        config.wfsStrategy = WFS_STRATEGY.lookUp(params);
        config.filterCompliance = FILTER_COMPLIANCE.lookUp(params);
        config.namespaceOverride = NAMESPACE.lookUp(params);
        config.outputformatOverride = OUTPUTFORMAT.lookUp(params);
        config.axisOrder = AXIS_ORDER.lookUp(params);
        config.axisOrderFilter =
                AXIS_ORDER_FILTER.lookUp(params) == null ? AXIS_ORDER.lookUp(params) : AXIS_ORDER_FILTER.lookUp(params);

        config.gmlCompatibleTypenames = GML_COMPATIBLE_TYPENAMES.lookUp(params) == null
                ? (Boolean) GML_COMPATIBLE_TYPENAMES.getDefaultValue()
                : GML_COMPATIBLE_TYPENAMES.lookUp(params);
        config.entityResolver = ENTITY_RESOLVER.lookUp(params);
        config.useHttpConnectionPooling = USE_HTTP_CONNECTION_POOLING.lookUp(params);
        config.maxConnectionPoolSize = MAX_CONNECTION_POOL_SIZE.lookUp(params);

        config.additionalHeaders = extractAdditionalHeaders(params);
        return config;
    }

    /**
     * Extracts headers from parameters and 1) creates a defensive copy of the map, 2) makes sure no nulls are contained
     * and 3) only strings.
     *
     * @param params
     * @return a new map of null
     * @throws IOException
     */
    private static Map<String, String> extractAdditionalHeaders(Map<?, ?> params) throws IOException {
        Map<?, ?> headersRaw = WFSDataAccessFactory.ADDITIONAL_HEADERS.lookUp(params);
        if (headersRaw != null) {
            Map<String, String> headers = new LinkedHashMap<>();
            headersRaw.forEach((key, value) -> {
                if (key instanceof String && value instanceof String) {
                    headers.put(key.toString(), value.toString());
                } else {
                    LOGGER.warning("Ignoring additional header. Not string-typed. Key: "
                            + key
                            + ", value: "
                            + value
                            + ". Key type: "
                            + (key == null ? null : key.getClass().getName())
                            + ", value type: "
                            + (value == null ? null : value.getClass().getName()));
                }
            });
            return headers;
        }
        return null;
    }

    /** @return the user */
    public String getUser() {
        return user;
    }

    /** @return the pass */
    public String getPassword() {
        return pass;
    }

    /** @return the timeoutMillis */
    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    /** @return the preferredMethod */
    public PreferredHttpMethod getPreferredMethod() {
        return preferredMethod;
    }

    /** @return the buffer */
    public int getBuffer() {
        return buffer;
    }

    /** @return the tryGZIP */
    public boolean isTryGZIP() {
        return tryGZIP;
    }

    /** @return the lenient */
    public boolean isLenient() {
        return lenient;
    }

    /** @return the maxFeatures */
    public Integer getMaxFeatures() {
        return maxFeatures;
    }

    /** @return the defaultEncoding */
    public Charset getDefaultEncoding() {
        return defaultEncoding;
    }

    /** @return the wfsStrategy */
    public String getWfsStrategy() {
        return wfsStrategy;
    }

    /** @return the filterCompliance */
    public Integer getFilterCompliance() {
        return filterCompliance;
    }

    /** @return the namespaceOverride */
    public String getNamespaceOverride() {
        return namespaceOverride;
    }

    /** @return the outputformat override */
    public String getOutputformatOverride() {
        return outputformatOverride;
    }

    /** @return if use default srs */
    public boolean isUseDefaultSrs() {
        return useDefaultSrs;
    }

    /** @return the axis order */
    public String getAxisOrder() {
        return axisOrder;
    }

    /** @return the axis order filter */
    public String getAxisOrderFilter() {
        return axisOrderFilter;
    }

    /** @return if GML compatible typenames are used */
    public boolean isGmlCompatibleTypenames() {
        return gmlCompatibleTypenames;
    }

    /** Returns the entity resolved to be used for XML parses */
    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    /** @return if http connection pooling should be used */
    public boolean isUseHttpConnectionPooling() {
        return useHttpConnectionPooling;
    }

    /**
     * @return the size of the connection pool, if {@link #isUseHttpConnectionPooling()} is <code>
     *     true</code>
     */
    public int getMaxConnectionPoolSize() {
        return maxConnectionPoolSize;
    }

    /** @return null, if the {@link #additionalHeaders} are null. An unmodifiable version of the headers otherwise. */
    public Map<String, String> getAdditionalHeaders() {
        if (additionalHeaders == null) {
            return null;
        }
        return Collections.unmodifiableMap(additionalHeaders);
    }

    /** Checks if axis flipping is needed comparing axis order requested for the DataStore with query crs. */
    public static boolean invertAxisNeeded(String axisOrder, CoordinateReferenceSystem crs) {
        CRS.AxisOrder requestedAxis = CRS.getAxisOrder(crs);
        if (requestedAxis == CRS.AxisOrder.INAPPLICABLE) {
            boolean forcedLonLat = Boolean.getBoolean("org.geotools.referencing.forceXY")
                    || Boolean.TRUE.equals(Hints.getSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
            if (forcedLonLat) {
                requestedAxis = CRS.AxisOrder.EAST_NORTH;
            } else {
                requestedAxis = CRS.AxisOrder.NORTH_EAST;
            }
        }

        if (WFSDataStoreFactory.AXIS_ORDER_NORTH_EAST.equals(axisOrder)) {
            return requestedAxis.equals(CRS.AxisOrder.EAST_NORTH);
        } else if (WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH.equals(axisOrder)) {
            return requestedAxis.equals(CRS.AxisOrder.NORTH_EAST);
        } else {
            return false; // compliant, don't do anything
        }
    }

    public String localTypeName(QName remoteTypeName) {
        String localTypeName = remoteTypeName.getLocalPart();
        if (!XMLConstants.DEFAULT_NS_PREFIX.equals(remoteTypeName.getPrefix())) {
            localTypeName = remoteTypeName.getPrefix()
                    + (gmlCompatibleTypenames ? NAME_SEPARATOR_GML_COMPATIBLE : NAME_SEPARATOR)
                    + localTypeName;
        }
        return localTypeName;
    }
}
