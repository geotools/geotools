/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ws;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.ws.protocol.http.HTTPProtocol;
import org.geotools.data.ws.protocol.http.SimpleHttpProtocol;
import org.geotools.data.ws.protocol.ws.Version;
import org.geotools.data.ws.protocol.ws.WSProtocol;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author rpetty
 *
 *
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/app-schema/webservice/
 *         src/main/java/org/geotools /data/wfs/WSDataStoreFactory.java $
 * @see XmlDataStore
 * @see WSProtocol
 * @see WSStrategy
 */
@SuppressWarnings( { "unchecked", "nls" })
public class WSDataStoreFactory extends AbstractDataStoreFactory {
    private static final Logger logger = Logging.getLogger("org.geotools.data.ws");

    public static class WSFactoryParam<T> extends Param {
        private T defaultValue;

        /**
         * Creates a required parameter
         * 
         * @param key
         * @param type
         * @param description
         */
        public WSFactoryParam(String key, Class type, String description) {
            super(key, type, description, true);
        }

        /**
         * Creates an optional parameter with the supplied default value
         * 
         * @param key
         * @param type
         * @param description
         * @param required
         */
        public WSFactoryParam(String key, Class type, String description, T defaultValue) {
            super(key, type, description, false);
            this.defaultValue = defaultValue;
        }

        public T lookUp(final Map params) throws IOException {
            T parameter = (T) super.lookUp(params);
            return parameter == null ? defaultValue : parameter;
        }
    }

    private static final WSFactoryParam[] parametersInfo = new WSFactoryParam[7];
    static {
        String name;
        Class clazz;
        String description;

        name = "WSDataStoreFactory:GET_CONNECTION_URL";
        clazz = URL.class;
        description = "Represents a URL to the getCapabilities document or a server instance.";
        parametersInfo[0] = new WSFactoryParam(name, clazz, description);

        name = "WSDataStoreFactory:TIMEOUT";
        clazz = Integer.class;
        description = "This allows the user to specify a timeout in milliseconds. This param"
                + " has a default value of unlimited.";
        parametersInfo[1] = new WSFactoryParam(name, clazz, description, Integer.valueOf(-1));

        name = "WSDataStoreFactory:TRY_GZIP";
        clazz = Boolean.class;
        description = "Indicates that datastore should use gzip to transfer data if the server "
                + "supports it. Default is true";
        parametersInfo[2] = new WSFactoryParam(name, clazz, description, Boolean.TRUE);

        name = "WSDataStoreFactory:MAXFEATURES";
        clazz = Integer.class;
        description = "Positive integer used as a hard limit for the amount of Features to retrieve"
                + " for each FeatureType. A value of zero or not providing this parameter means no limit.";
        parametersInfo[3] = new WSFactoryParam(name, clazz, description, Integer.valueOf(0));

        name = "WSDataStoreFactory:TEMPLATE_NAME";
        clazz = String.class;
        description = "File name of the template used to create the XML request";
        parametersInfo[4] = new WSFactoryParam(name, clazz, description);

        name = "WSDataStoreFactory:TEMPLATE_DIRECTORY";
        clazz = URL.class;
        description = "Directory where the template used to create the XML request has been put";
        parametersInfo[5] = new WSFactoryParam(name, clazz, description);

        name = "WSDataStoreFactory:CAPABILITIES_FILE_LOCATION";
        clazz = URL.class;
        description = "The location of the capabilities file";
        parametersInfo[6] = new WSFactoryParam(name, clazz, description);
    }

    /**
     * Mandatory DataStore parameter indicating the URL for the WS GetCapabilities document.
     */
    public static final WSFactoryParam<URL> GET_CONNECTION_URL = parametersInfo[0];

    /**
     * Optional {@code Integer} DataStore parameter indicating a timeout in milliseconds for the
     * HTTP connections.
     * 
     * @TODO: specify if its just a connection timeout or also a read timeout
     */
    public static final WSFactoryParam<Integer> TIMEOUT = parametersInfo[1];

    /**
     * Optional {@code Boolean} data store parameter indicating whether to set the accept GZip
     * encoding on the HTTP request headers sent to the server
     */
    public static final WSFactoryParam<Boolean> TRY_GZIP = parametersInfo[2];

    public static final WSFactoryParam<String> TEMPLATE_NAME = parametersInfo[4];

    public static final WSFactoryParam<URL> TEMPLATE_DIRECTORY = parametersInfo[5];
    
    public static final WSFactoryParam<URL> CAPABILITIES_FILE_LOCATION = parametersInfo[6];

    protected Map<Map, XmlDataStore> perParameterSetDataStoreCache = new HashMap();

    /**
     * Requests the WS Capabilities document from the {@link WSDataStoreFactory#URL url} parameter
     * in {@code params} and returns a {@link XmlDataStore} according to the version of the
     * GetCapabilities document returned.
     * <p>
     * Note the {@code URL} provided as parameter must refer to the actual {@code GetCapabilities}
     * request. If you need to specify a preferred version or want the GetCapabilities request to be
     * generated from a base URL build the URL with the
     * {@link #createGetCapabilitiesRequest(URL, Version)} first.
     * </p>
     * 
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    public XmlDataStore createDataStore(final Map params) throws IOException {

        if (perParameterSetDataStoreCache.containsKey(params)) {
            return perParameterSetDataStoreCache.get(params);
        }
        final URL getQueryRequest = (URL) GET_CONNECTION_URL.lookUp(params);
        final int timeoutMillis = (Integer) TIMEOUT.lookUp(params);
        final boolean tryGZIP = (Boolean) TRY_GZIP.lookUp(params);
        final String templateName = (String) TEMPLATE_NAME.lookUp(params);
        final URL templateDirectory = (URL) TEMPLATE_DIRECTORY.lookUp(params);
        final URL capabilitiesDirectory = (URL) CAPABILITIES_FILE_LOCATION.lookUp(params);

        final HTTPProtocol http = new SimpleHttpProtocol();
        http.setTryGzip(tryGZIP);
        http.setTimeoutMillis(timeoutMillis);

        InputStream capsIn = new BufferedInputStream(new FileInputStream(DataUtilities.urlToFile(capabilitiesDirectory)));

        WSStrategy strategy = determineCorrectStrategy(templateDirectory, templateName);
        WS_Protocol ws = new WS_Protocol(capsIn, strategy, getQueryRequest, http);
        final XmlDataStore dataStore = new WS_DataStore(ws);

        perParameterSetDataStoreCache.put(new HashMap(params), dataStore);
        return dataStore;
    }

    static WSStrategy determineCorrectStrategy(URL templateDirectory, String templateName) {
        WSStrategy strategy = new DefaultWSStrategy(templateDirectory, templateName);

        logger.info("Using WS Strategy: " + strategy.getClass().getName());
        return strategy;
    }

    /**
     * Unsupported operation, can't create a WS service.
     * 
     * @throws UnsupportedOperationException
     *             always, as this operation is not applicable to WS.
     * @see org.geotools.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    public DataStore createNewDataStore(final Map params) throws IOException {
        throw new UnsupportedOperationException("Operation not applicable to a WS service");
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getDescription()
     */
    public String getDescription() {
        return "The XmlDataStore represents a connection to a Web Feature Server. This connection provides access to the Features published by the server, and the ability to perform transactions on the server (when supported / allowed).";
    }

    /**
     * Returns the set of parameter descriptors needed to connect to a WS.
     * 
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     * @see #URL
     * @see #PROTOCOL
     * @see #USERNAME
     * @see #PASSWORD
     * @see #TIMEOUT
     * @see #BUFFER_SIZE
     * @see #TRY_GZIP
     * @see #LENIENT
     * @see #ENCODING
     */
    public Param[] getParametersInfo() {
        int length = parametersInfo.length;
        Param[] params = new Param[length];
        System.arraycopy(parametersInfo, 0, params, 0, length);
        return params;
    }

    /**
     * Checks whether {@code params} contains a valid set of parameters to connecto to a WS.
     * <p>
     * Rules are:
     * <ul>
     * <li>the mandatory {@link #URL} is provided.
     * <li>whether both {@link #USERNAME} and {@link #PASSWORD} are provided, or none.
     * </ul>
     * Availability of the other optional parameters is not checked for existence.
     * </p>
     * 
     * @param params
     *            non null map of datastore parameters.
     * @see org.geotools.data.DataStoreFactorySpi#canProcess(java.util.Map)
     */
    public boolean canProcess(final Map params) {
        if (params == null) {
            throw new NullPointerException("params");
        }
        try {
            // mandatory fields
            GET_CONNECTION_URL.lookUp(params);
            TEMPLATE_NAME.lookUp(params);
            TEMPLATE_DIRECTORY.lookUp(params);
            CAPABILITIES_FILE_LOCATION.lookUp(params);
        } catch (Exception e) {
            return false;
        }
 
        return true;
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getDisplayName()
     */
    public String getDisplayName() {
        return "Web Service Server";
    }

    /**
     * @return {@code true}, no extra or external requisites for datastore availability.
     * @see org.geotools.data.DataStoreFactorySpi#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }
}
