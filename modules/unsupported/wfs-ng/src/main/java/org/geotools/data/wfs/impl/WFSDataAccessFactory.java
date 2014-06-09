/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Authenticator;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.geotools.data.AbstractDataAccessFactory;
import org.geotools.data.DataAccess;
import org.geotools.data.Parameter;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.ows.ServiceException;
import org.geotools.xml.XMLHandlerHints;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * The factory responsible for creating WFSDataAccess objects based on their capabilities and the configuration file used. This file is included as a
 * candidate for DataAccessFinder by virtue of the fact that its name is present in the file gt-wfs-ng > src/main/resources > META-INF > services
 * > org.geotools.data.DataAccessFactory.
 * 
 * @author Adam Brown (Curtin University of Technology)
 *
 */
public class WFSDataAccessFactory extends AbstractDataAccessFactory {

    /**
	 * A {@link Param} subclass that allows us to provide a default value to the
	 * lookUp method.
	 */
    public static class WFSFactoryParam<T> extends Param {
        private T defaultValue;

        /**
         * Creates a required parameter
         * 
         * @param key
         * @param type
         * @param description
         */
        public WFSFactoryParam(String key, Class<T> type, String description) {
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
		public WFSFactoryParam(String key, Class<T> type, String description,
				T defaultValue) {
            super(key, type, description, false, defaultValue);
            this.defaultValue = defaultValue;
        }

		public WFSFactoryParam(String key, Class<T> type, String description,
				T defaultValue, Object... metadata) {
            super(key, type, description, false, defaultValue, metadata);
            this.defaultValue = defaultValue;
        }

        public T lookUp(final Map params) throws IOException {
            T parameter = (T) super.lookUp(params);
            return parameter == null ? defaultValue : parameter;
        }
    }

    /** Access with {@link WFSDataStoreFactory#getParametersInfo()  */
    private static final WFSFactoryParam<?>[] parametersInfo = new WFSFactoryParam[14];

    private static final int GMLComplianceLevel = 2;

    /**
	 * Mandatory DataStore parameter indicating the URL for the WFS
	 * GetCapabilities document.
     */
    public static final WFSFactoryParam<URL> URL;
    static {
        String name = "WFSDataStoreFactory:WFS_GET_CAPABILITIES_URL";
        String description = "Represents a URL to the getCapabilities document or a server instance.";
		parametersInfo[0] = URL = new WFSFactoryParam<URL>(name, URL.class,
				description);
    }

    /**
	 * Optional {@code Boolean} DataStore parameter acting as a hint for the
	 * HTTP protocol to use preferably against the WFS instance, with the
     * following semantics:
     * <ul>
     * <li>{@code null} (not supplied): use "AUTO", let the DataStore decide.
     * <li>{@code Boolean.TRUE} use HTTP POST preferably.
     * <li {@code Boolean.FALSE} use HTTP GET preferably.
     * </ul>
     */
    public static final WFSFactoryParam<Boolean> PROTOCOL;
    static {
        String name = "WFSDataStoreFactory:PROTOCOL";
        String description = "Sets a preference for the HTTP protocol to use when requesting "
                + "WFS functionality. Set this value to Boolean.TRUE for POST, Boolean.FALSE "
                + "for GET or NULL for AUTO";
		parametersInfo[1] = PROTOCOL = new WFSFactoryParam<Boolean>(name,
				Boolean.class, description, null);
    }

    /**
	 * Optional {@code String} DataStore parameter supplying the user name to
	 * use when the server requires HTTP authentication
     * <p>
     * Shall be used together with {@link #PASSWORD} or not used at all.
     * </p>
     * 
     * @see Authenticator
     */
    public static final WFSFactoryParam<String> USERNAME;
    static {
        String name = "WFSDataStoreFactory:USERNAME";
        String description = "This allows the user to specify a username. This param should not "
                + "be used without the PASSWORD param.";
		parametersInfo[2] = USERNAME = new WFSFactoryParam<String>(name,
				String.class, description, null);
    }

    /**
	 * Optional {@code String} DataStore parameter supplying the password to use
	 * when the server requires HTTP authentication
     * <p>
     * Shall be used together with {@link #USERNAME} or not used at all.
     * </p>
     * 
     * @see Authenticator
     */
    public static final WFSFactoryParam<String> PASSWORD;
    static {
        String name = "WFSDataStoreFactory:PASSWORD";
        String description = "This allows the user to specify a username. This param should not"
                + " be used without the USERNAME param.";
		parametersInfo[3] = PASSWORD = new WFSFactoryParam<String>(name,
				String.class, description, null, Param.IS_PASSWORD, true);
    }

    /**
	 * Optional {@code String} DataStore parameter supplying a JVM supported
	 * {@link Charset charset} name to use as the character encoding for XML
     * requests sent to the server.
     */
    public static final WFSFactoryParam<String> ENCODING;
    static {

        String name = "WFSDataStoreFactory:ENCODING";
        String description = "This allows the user to specify the character encoding of the "
                + "XML-Requests sent to the Server. Defaults to UTF-8";

        String defaultValue = "UTF-8";
		List<String> options = new ArrayList<String>(Charset
				.availableCharsets().keySet());
		Collections.sort(options);
		parametersInfo[4] = ENCODING = new WFSFactoryParam<String>(name,
				String.class, description, defaultValue, Parameter.OPTIONS,
				options);
    }

    /**
	 * Optional {@code Integer} DataStore parameter indicating a timeout in
	 * milliseconds for the HTTP connections. <>p>
     * 
     * @TODO: specify if its just a connection timeout or also a read timeout
     */
    public static final WFSFactoryParam<Integer> TIMEOUT;
    static {
        String name = "WFSDataStoreFactory:TIMEOUT";
        String description = "This allows the user to specify a timeout in milliseconds. This param"
                + " has a default value of 3000ms.";
		parametersInfo[5] = TIMEOUT = new WFSFactoryParam<Integer>(name,
				Integer.class, description, 3000);
    }

    /**
	 * Optional {@code Integer} parameter stating how many Feature instances to
	 * buffer at once. Only implemented for WFS 1.0.0 support.
     */
    public static final WFSFactoryParam<Integer> BUFFER_SIZE;
    static {
        String name = "WFSDataStoreFactory:BUFFER_SIZE";
        String description = "This allows the user to specify a buffer size in features. This param "
                + "has a default value of 10 features.";
		parametersInfo[6] = BUFFER_SIZE = new WFSFactoryParam<Integer>(name,
				Integer.class, description, 10);
    }

    /**
	 * Optional {@code Boolean} data store parameter indicating whether to set
	 * the accept GZip encoding on the HTTP request headers sent to the server
     */
    public static final WFSFactoryParam<Boolean> TRY_GZIP;
    static {
        String name = "WFSDataStoreFactory:TRY_GZIP";
        String description = "Indicates that datastore should use gzip to transfer data if the server "
                + "supports it. Default is true";
		parametersInfo[7] = TRY_GZIP = new WFSFactoryParam<Boolean>(name,
				Boolean.class, description, Boolean.TRUE);
    }

    /**
	 * Optional {@code Boolean} DataStore parameter indicating whether to be
	 * lenient about parsing bad data
     */
    public static final WFSFactoryParam<Boolean> LENIENT;
    static {

        String name = "WFSDataStoreFactory:LENIENT";
        String description = "Indicates that datastore should do its best to create features from the "
                + "provided data even if it does not accurately match the schema.  Errors will "
                + "be logged but the parsing will continue if this is true.  Default is false";
		parametersInfo[8] = LENIENT = new WFSFactoryParam<Boolean>(name,
				Boolean.class, description, false);
	}

    /**
	 * Optional positive {@code Integer} used as a hard limit for the amount of
	 * Features to retrieve for each FeatureType. A value of zero or not
     * providing this parameter means no limit.
     */
    public static final WFSFactoryParam<Integer> MAXFEATURES;
    static {
        String name = "WFSDataStoreFactory:MAXFEATURES";
        String description = "Positive integer used as a hard limit for the amount of Features to retrieve"
                + " for each FeatureType. A value of zero or not providing this parameter means no limit.";
		parametersInfo[9] = MAXFEATURES = new WFSFactoryParam<Integer>(name,
				Integer.class, description, 0);
    }

    /**
	 * Optional {@code Integer} DataStore parameter indicating level of
	 * compliance to WFS specification
     * <ul>
     * <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_LOW}</li>
     * <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_MEDIUM}</li>
     * <li>{@link XMLHandlerHints#VALUE_FILTER_COMPLIANCE_HIGH}</li>
     * </ul>
     */
    public static final WFSFactoryParam<Integer> FILTER_COMPLIANCE;;
    static {

        String name = "WFSDataStoreFactory:FILTER_COMPLIANCE";
        String description = "Level of compliance to WFS specification (0-low,1-medium,2-high)";
        List<Integer> options = Arrays.asList(new Integer[] { 0, 1, 2 });

		parametersInfo[10] = FILTER_COMPLIANCE = new WFSFactoryParam<Integer>(
				name, Integer.class, description, null, Parameter.OPTIONS,
				options);
    }

    /**
	 * Optional {@code String} DataStore parameter indicating either
	 * "mapserver", "geoserver", "strict" or "nonstrict" strategy
     */
    public static final WFSFactoryParam<String> WFS_STRATEGY;
    static {
        String name = "WFSDataStoreFactory:WFS_STRATEGY";
        String description = "Override wfs stragegy with either cubwerx, ionic, mapserver"
                + ", geoserver, or nonstrict strategy.";
		List<String> options = Arrays.asList(new String[] { "auto", "strict",
				"nonstrict", "mapserver", "geoserver", "cubewerx", "ionic" });
		parametersInfo[11] = WFS_STRATEGY = new WFSFactoryParam<String>(name,
				String.class, description, "auto", Parameter.OPTIONS, options);
    }

    /**
     * Optional {@code String} namespace URI to override the original namespaces
     */
    public static final WFSFactoryParam<String> NAMESPACE;
    static {
        String name = "namespace";
        String description = "Override the original WFS type name namespaces";
		parametersInfo[12] = NAMESPACE = new WFSFactoryParam<String>(name,
				String.class, description, null);
    }

    /**
	 * Optional {@code Integer} OCG GML compliance level. i.e. (simple feature)
	 * 0, 1 or 2
     */
    public static final WFSFactoryParam<Integer> GML_COMPLIANCE_LEVEL;
    static {
        String name = "WFSDataStoreFactory:GML_COMPLIANCE_LEVEL";
        String description = "Optional OGC GML compliance level required.";
		parametersInfo[13] = GML_COMPLIANCE_LEVEL = new WFSFactoryParam<Integer>(
				name, Integer.class, description, 0);
    }

    /**
	 * Checks whether {@code params} contains a valid set of parameters to
	 * connect to a WFS.
     * <p>
     * Rules are:
     * <ul>
     * <li>The mandatory {@link #URL} is provided.
	 * <li>Either both {@link #USERNAME} and {@link #PASSWORD} are provided, or
	 * none.
     * </ul>
     * </p>
     */
    @Override
    public boolean canProcess(@SuppressWarnings("rawtypes") final Map params) {
        /*
         * check required params exist and are of the correct type
         */
        boolean canProcess = super.canProcess(params);
        if (!canProcess) {
            return false;
        }
        try {
            URL url = (URL) URL.lookUp(params);
            if (!"http".equalsIgnoreCase(url.getProtocol())
                    && !"https".equalsIgnoreCase(url.getProtocol())) {
                if (!Boolean.TRUE.equals(params.get("TESTING"))) {
					Loggers.MODULE
							.finest("Can't process non http or https GetCapabilities URLs");
                    return false; // must be http or https since we use
                                  // SimpleHTTPClient class
                }
            }
        } catch (Exception e) {
            return false;
        }

        // check password / username
        if (params.containsKey(USERNAME.key)) {
            if (!params.containsKey(PASSWORD.key)) {
                return false; // must have both
            }
        } else {
            if (params.containsKey(PASSWORD.key)) {
                return false; // must have both
            }
        }

        // Check compliance level
        if (params.containsKey(GML_COMPLIANCE_LEVEL.key)) {
            if ((Integer) params.get(GML_COMPLIANCE_LEVEL.key) > GMLComplianceLevel) {
                return false;
            }
        }

        return true;
    }

    @Override
    public DataAccess<? extends FeatureType, ? extends Feature> createDataStore(
            Map<String, Serializable> params) throws IOException {

		WFSContentDataAccess dataAccess = new WFSContentDataAccess(
				getWFSClient(params));
				
        String cacheLocationKey = "WFSDataStoreFactory:SCHEMA_CACHE_LOCATION";

        if (params.containsKey(cacheLocationKey)) {
            String cacheLocation = (String) params.get(cacheLocationKey);
            dataAccess.setCacheLocation(new File(cacheLocation));
        }

        return dataAccess;
    }

    @Override
    public String getDescription() {
        return "Provides access to the Features published a Web Feature Service, "
                + "and the ability to perform transactions on the server (when supported / allowed).";
    }

    /**
     * Returns the set of parameter descriptors needed to connect to a WFS.
     * 
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     * @see #URL
     * @see #NAMESPACE
     * @see #PROTOCOL
     * @see #USERNAME
     * @see #PASSWORD
     * @see #TIMEOUT
     * @see #BUFFER_SIZE
     * @see #TRY_GZIP
     * @see #LENIENT
     * @see #ENCODING
     * @see #FILTER_COMPLIANCE
     * @see #MAXFEATURES
     * @see #WFS_STRATEGY
     */
    @Override
    public Param[] getParametersInfo() {
        int length = parametersInfo.length;
        Param[] params = new Param[length];
        System.arraycopy(parametersInfo, 0, params, 0, length);
        return params;
    }

	protected WFSClient getWFSClient(final Map<String, Serializable> params)
			throws IOException {
		
        final WFSConfig config = WFSConfig.fromParams(params);
        {
            String user = config.getUser();
            String password = config.getPassword();
            if (((user == null) && (password != null))
                    || ((config.getPassword() == null) && (config.getUser() != null))) {
                throw new IOException(
                        "Cannot define only one of USERNAME or PASSWORD, must define both or neither");
            }
        }

        final HTTPClient http = new SimpleHttpClient(); // new
                                                        // MultithreadedHttpClient();

        // TODO: let HTTPClient be configured for gzip
        // http.setTryGzip(tryGZIP);
        http.setUser(config.getUser());
        http.setPassword(config.getPassword());
        int timeoutMillis = config.getTimeoutMillis();
        http.setConnectTimeout(timeoutMillis / 1000);

        final URL capabilitiesURL = (URL)URL.lookUp(params);

        // WFSClient performs version negotiation and selects the correct
        // strategy
        WFSClient wfsClient;
        try {
            wfsClient = new WFSClient(capabilitiesURL, http, config);
        } catch (ServiceException e) {
            throw new IOException(e);
        }

        return wfsClient;
    }
}
