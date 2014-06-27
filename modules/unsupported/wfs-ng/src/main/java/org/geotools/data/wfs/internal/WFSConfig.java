/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.wfs.WFSDataStoreFactory.BUFFER_SIZE;
import static org.geotools.data.wfs.WFSDataStoreFactory.ENCODING;
import static org.geotools.data.wfs.WFSDataStoreFactory.FILTER_COMPLIANCE;
import static org.geotools.data.wfs.WFSDataStoreFactory.LENIENT;
import static org.geotools.data.wfs.WFSDataStoreFactory.MAXFEATURES;
import static org.geotools.data.wfs.WFSDataStoreFactory.NAMESPACE;
import static org.geotools.data.wfs.WFSDataStoreFactory.PASSWORD;
import static org.geotools.data.wfs.WFSDataStoreFactory.PROTOCOL;
import static org.geotools.data.wfs.WFSDataStoreFactory.TIMEOUT;
import static org.geotools.data.wfs.WFSDataStoreFactory.TRY_GZIP;
import static org.geotools.data.wfs.WFSDataStoreFactory.USERNAME;
import static org.geotools.data.wfs.WFSDataStoreFactory.WFS_STRATEGY;
import static org.geotools.data.wfs.WFSDataStoreFactory.OUTPUTFORMAT;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @see WFSStrategy#setConfig(WFSConfig)
 */
public class WFSConfig {

    private String user;

    private String pass;

    private int timeoutMillis;

    private PreferredHttpMethod preferredMethod;

    private int buffer;

    private boolean tryGZIP;

    private boolean lenient;

    private Integer maxFeatures;

    private Charset defaultEncoding;

    private String wfsStrategy;

    private Integer filterCompliance;

    private String namespaceOverride;
    
    private String outputformatOverride;
    
    private boolean useDefaultSrs;
    
    private String axisOrder;
    
    private String axisOrderFilter;
    
    

    public static enum PreferredHttpMethod {
        AUTO, HTTP_GET, HTTP_POST
    }

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
    }

    public static WFSConfig fromParams(Map<?, ?> params) throws IOException {

        WFSConfig config = new WFSConfig();

        Boolean preferPost = (Boolean) PROTOCOL.lookUp(params);

        if (preferPost == null) {
            config.preferredMethod = PreferredHttpMethod.AUTO;
        } else {
            config.preferredMethod = preferPost.booleanValue() ? PreferredHttpMethod.HTTP_POST
                    : PreferredHttpMethod.HTTP_GET;
        }

        config.preferredMethod = PreferredHttpMethod.HTTP_POST;// remove this line

        config.user = (String) USERNAME.lookUp(params);
        config.pass = (String) PASSWORD.lookUp(params);
        config.timeoutMillis = (Integer) TIMEOUT.lookUp(params);
        config.buffer = (Integer) BUFFER_SIZE.lookUp(params);
        config.tryGZIP = (Boolean) TRY_GZIP.lookUp(params);
        config.lenient = (Boolean) LENIENT.lookUp(params);

        String encoding = (String) ENCODING.lookUp(params);
        config.defaultEncoding = Charset.forName(encoding);

        config.maxFeatures = (Integer) MAXFEATURES.lookUp(params);
        config.wfsStrategy = (String) WFS_STRATEGY.lookUp(params);
        config.filterCompliance = (Integer) FILTER_COMPLIANCE.lookUp(params);
        config.namespaceOverride = (String) NAMESPACE.lookUp(params);
        config.outputformatOverride = (String) OUTPUTFORMAT.lookUp(params);        

        return config;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the pass
     */
    public String getPassword() {
        return pass;
    }

    /**
     * @return the timeoutMillis
     */
    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    /**
     * @return the preferredMethod
     */
    public PreferredHttpMethod getPreferredMethod() {
        return preferredMethod;
    }

    /**
     * @return the buffer
     */
    public int getBuffer() {
        return buffer;
    }

    /**
     * @return the tryGZIP
     */
    public boolean isTryGZIP() {
        return tryGZIP;
    }

    /**
     * @return the lenient
     */
    public boolean isLenient() {
        return lenient;
    }

    /**
     * @return the maxFeatures
     */
    public Integer getMaxFeatures() {
        return maxFeatures;
    }

    /**
     * @return the defaultEncoding
     */
    public Charset getDefaultEncoding() {
        return defaultEncoding;
    }

    /**
     * @return the wfsStrategy
     */
    public String getWfsStrategy() {
        return wfsStrategy;
    }

    /**
     * @return the filterCompliance
     */
    public Integer getFilterCompliance() {
        return filterCompliance;
    }

    /**
     * @return the namespaceOverride
     */
    public String getNamespaceOverride() {
        return namespaceOverride;
    }

    /**
     * @return the outputformat override
     */
    public String getOutputformatOverride() {
        return outputformatOverride;
    }

    /**
     * @return if use default srs
     */
    public boolean isUseDefaultSrs() {
        return useDefaultSrs;
    }

    /**
     * @return the axis order
     */
    public String getAxisOrder() {
        return axisOrder;
    }

    /**
     * @return the axis order filter
     */
    public String getAxisOrderFilter() {
        return axisOrderFilter;
    }    
    
    /**
     * Checks if axis flipping is needed comparing axis order requested for the
     * DataStore with query crs.
     * 
     * @param axisOrder
     * @param coordinateSystem
     * @return
     */
    public static boolean invertAxisNeeded(String axisOrder,
            CoordinateReferenceSystem crs) {
        boolean invertXY;
    
        if (WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH.equals(axisOrder)) {
            invertXY = false;
        } else if (WFSDataStoreFactory.AXIS_ORDER_NORTH_EAST.equals(axisOrder)) {
            invertXY = true;
        } else {
            invertXY = CRS.getAxisOrder(crs).equals(CRS.AxisOrder.NORTH_EAST);
        }
        return invertXY;
    }
}
