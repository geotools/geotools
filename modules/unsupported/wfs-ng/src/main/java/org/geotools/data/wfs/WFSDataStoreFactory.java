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
package org.geotools.data.wfs;

import static org.geotools.data.wfs.protocol.http.HttpMethod.GET;
import static org.geotools.data.wfs.protocol.http.HttpMethod.POST;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.wfs.protocol.http.HTTPProtocol;
import org.geotools.data.wfs.protocol.http.HTTPResponse;
import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.data.wfs.protocol.http.SimpleHttpProtocol;
import org.geotools.data.wfs.protocol.wfs.Version;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.data.wfs.v1_0_0.WFS100ProtocolHandler;
import org.geotools.data.wfs.v1_0_0.WFS_1_0_0_DataStore;
import org.geotools.data.wfs.v1_1_0.ArcGISServerStrategy;
import org.geotools.data.wfs.v1_1_0.CubeWerxStrategy;
import org.geotools.data.wfs.v1_1_0.DefaultWFSStrategy;
import org.geotools.data.wfs.v1_1_0.GeoServerStrategy;
import org.geotools.data.wfs.v1_1_0.IonicStrategy;
import org.geotools.data.wfs.v1_1_0.WFSStrategy;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_Protocol;
import org.geotools.util.logging.Logging;
import org.geotools.wfs.WFS;
import org.geotools.wfs.protocol.ConnectionFactory;
import org.geotools.wfs.protocol.DefaultConnectionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A {@link DataStoreFactorySpi} to connect to a Web Feature Service.
 * <p>
 * Produces a {@link WFSDataStore} is the correct set of connection parameters are provided. For
 * instance, the only mandatory one is {@link #URL}.
 * </p>
 * <p>
 * As with all the DataStoreFactorySpi implementations, this one is not intended to be used directly
 * but through the {@link DataStoreFinder} mechanism, so client application should not have strong
 * dependencies over this module.
 * </p>
 * <p>
 * Upon a valid URL to a WFS GetCapabilities document, this factory will perform version negotiation
 * between the server supported protocol versions and this plugin supported ones, and will return a
 * {@link DataStore} capable of communicating with the server using the agreed WFS protocol version.
 * </p>
 * <p>
 * In the case the provided GetCapabilities URL explicitly contains a VERSION parameter and both the
 * server and client support that version, that version will be used.
 * </p>
 * <p>
 * That said, for the time being, the current default version is {@code 1.0.0} instead of {@code
 * 1.1.0}, since the former is the one that supports transactions. When further development provides
 * transaction support for the WFS 1.1.0 version, propper version negotiation capabilities will be
 * added.
 * </p>
 * <p>
 * Among feeding the wfs datastore with a {@link WFSProtocol} that can handle the WFS version agreed
 * upong the server and this client, this factory will try to provide the datastore with a
 * {@link WFSStrategy} appropriate for the WFS implementation, if that could be somehow guessed.
 * That is so the datastore itself nor the protocol need to worry about any implementation specific
 * limitation or deviation from the standard the actual server may have.
 * </p>
 * 
 * @author dzwiers
 * @author Gabriel Roldan (TOPP)
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools
 *         /data/wfs/WFSDataStoreFactory.java $
 * @see WFSDataStore
 * @see WFSProtocol
 * @see WFSStrategy
 */
@SuppressWarnings( { "unchecked", "nls" })
public class WFSDataStoreFactory extends AbstractDataStoreFactory {
    private static final Logger logger = Logging.getLogger("org.geotools.data.wfs");

    /**
     * A {@link Param} subclass that allows to provide a default value to the lookUp method.
     * 
     * @author Gabriel Roldan
     * @version $Id: WFSDataStoreFactory.java 35842 2010-07-05 21:00:38Z rbraam $
     * @since 2.5.x
     * @source $URL:
     *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools
     *         /data/wfs/WFSDataStoreFactory.java $
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
        public WFSFactoryParam(String key, Class type, String description) {
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
        public WFSFactoryParam(String key, Class type, String description, T defaultValue) {
            super(key, type, description, false);
            this.defaultValue = defaultValue;
        }

        public WFSFactoryParam(String key, Class type, String description, T defaultValue, Object... metadata) {
            super(key, type, description, false, metadata);
            this.defaultValue = defaultValue;
        }
        
        public T lookUp(final Map params) throws IOException {
            T parameter = (T) super.lookUp(params);
            return parameter == null ? defaultValue : parameter;
        }
    }

    private static final WFSFactoryParam[] parametersInfo = new WFSFactoryParam[10];
    static {
        String name;
        Class clazz;
        String description;

        name = "WFSDataStoreFactory:GET_CAPABILITIES_URL";
        clazz = URL.class;
        description = "Represents a URL to the getCapabilities document or a server instance.";
        parametersInfo[0] = new WFSFactoryParam(name, clazz, description);

        name = "WFSDataStoreFactory:PROTOCOL";
        clazz = Boolean.class;
        description = "Sets a preference for the HTTP protocol to use when requesting "
                + "WFS functionality. Set this value to Boolean.TRUE for POST, Boolean.FALSE "
                + "for GET or NULL for AUTO";
        parametersInfo[1] = new WFSFactoryParam(name, clazz, description, (Boolean) null);

        name = "WFSDataStoreFactory:USERNAME";
        clazz = String.class;
        description = "This allows the user to specify a username. This param should not "
                + "be used without the PASSWORD param.";
        parametersInfo[2] = new WFSFactoryParam(name, clazz, description, (String) null);

        name = "WFSDataStoreFactory:PASSWORD";
        clazz = String.class;
        description = "This allows the user to specify a username. This param should not"
                + " be used without the USERNAME param.";
        parametersInfo[3] = new WFSFactoryParam(name, clazz, description, (String) null, Param.IS_PASSWORD, true);

        name = "WFSDataStoreFactory:ENCODING";
        clazz = String.class;
        description = "This allows the user to specify the character encoding of the "
                + "XML-Requests sent to the Server. Defaults to UTF-8";
        parametersInfo[4] = new WFSFactoryParam(name, clazz, description, "UTF-8");

        name = "WFSDataStoreFactory:TIMEOUT";
        clazz = Integer.class;
        description = "This allows the user to specify a timeout in milliseconds. This param"
                + " has a default value of 3000ms.";
        parametersInfo[5] = new WFSFactoryParam(name, clazz, description, Integer.valueOf(3000));

        name = "WFSDataStoreFactory:BUFFER_SIZE";
        clazz = Integer.class;
        description = "This allows the user to specify a buffer size in features. This param "
                + "has a default value of 10 features.";
        parametersInfo[6] = new WFSFactoryParam(name, clazz, description, Integer.valueOf(10));

        name = "WFSDataStoreFactory:TRY_GZIP";
        clazz = Boolean.class;
        description = "Indicates that datastore should use gzip to transfer data if the server "
                + "supports it. Default is true";
        parametersInfo[7] = new WFSFactoryParam(name, clazz, description, Boolean.TRUE);

        name = "WFSDataStoreFactory:LENIENT";
        clazz = Boolean.class;
        description = "Indicates that datastore should do its best to create features from the "
                + "provided data even if it does not accurately match the schema.  Errors will "
                + "be logged but the parsing will continue if this is true.  Default is false";
        parametersInfo[8] = new WFSFactoryParam(name, clazz, description, Boolean.FALSE);

        name = "WFSDataStoreFactory:MAXFEATURES";
        clazz = Integer.class;
        description = "Positive integer used as a hard limit for the amount of Features to retrieve"
                + " for each FeatureType. A value of zero or not providing this parameter means no limit.";
        parametersInfo[9] = new WFSFactoryParam(name, clazz, description, Integer.valueOf(0));
    }

    /**
     * Mandatory DataStore parameter indicating the URL for the WFS GetCapabilities document.
     */
    public static final WFSFactoryParam<URL> URL = parametersInfo[0];

    /**
     * Optional {@code Boolean} DataStore parameter acting as a hint for the HTTP protocol to use
     * preferably against the WFS instance, with the following semantics:
     * <ul>
     * <li>{@code null} (not supplied): use "AUTO", let the DataStore decide.
     * <li>{@code Boolean.TRUE} use HTTP POST preferably.
     * <li> {@code Boolean.FALSE} use HTTP GET preferably.
     * </ul>
     */
    public static final WFSFactoryParam<Boolean> PROTOCOL = parametersInfo[1];

    /**
     * Optional {@code String} DataStore parameter supplying the user name to use when the server
     * requires HTTP authentication
     * <p>
     * Shall be used together with {@link #PASSWORD} or not used at all.
     * </p>
     * 
     * @see Authenticator
     */
    public static final WFSFactoryParam<String> USERNAME = parametersInfo[2];

    /**
     * Optional {@code String} DataStore parameter supplying the password to use when the server
     * requires HTTP authentication
     * <p>
     * Shall be used together with {@link #USERNAME} or not used at all.
     * </p>
     * 
     * @see Authenticator
     */
    public static final WFSFactoryParam<String> PASSWORD = parametersInfo[3];

    /**
     * Optional {@code String} DataStore parameter supplying a JVM supported {@link Charset charset}
     * name to use as the character encoding for XML requests sent to the server.
     */
    public static final WFSFactoryParam<String> ENCODING = parametersInfo[4];

    /**
     * Optional {@code Integer} DataStore parameter indicating a timeout in milliseconds for the
     * HTTP connections.
     * 
     * @TODO: specify if its just a connection timeout or also a read timeout
     */
    public static final WFSFactoryParam<Integer> TIMEOUT = parametersInfo[5];

    /**
     * Optional {@code Integer} parameter stating how many Feature instances to buffer at once. Only
     * implemented for WFS 1.0.0 support.
     */
    public static final WFSFactoryParam<Integer> BUFFER_SIZE = parametersInfo[6];

    /**
     * Optional {@code Boolean} data store parameter indicating whether to set the accept GZip
     * encoding on the HTTP request headers sent to the server
     */
    public static final WFSFactoryParam<Boolean> TRY_GZIP = parametersInfo[7];

    /**
     * Optional {@code Boolean} DataStore parameter indicating whether to be lenient about parsing
     * bad data
     */
    public static final WFSFactoryParam<Boolean> LENIENT = parametersInfo[8];

    /**
     * Optional positive {@code Integer} used as a hard limit for the amount of Features to retrieve
     * for each FeatureType. A value of zero or not providing this parameter means no limit.
     */
    public static final WFSFactoryParam<Integer> MAXFEATURES = parametersInfo[9];

    protected Map<Map, WFSDataStore> perParameterSetDataStoreCache = new HashMap();

    /**
     * Requests the WFS Capabilities document from the {@link WFSDataStoreFactory#URL url} parameter
     * in {@code params} and returns a {@link WFSDataStore} according to the version of the
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
    public WFSDataStore createDataStore(final Map params) throws IOException {
        if (perParameterSetDataStoreCache.containsKey(params)) {
            return perParameterSetDataStoreCache.get(params);
        }
        final URL getCapabilitiesRequest = (URL) URL.lookUp(params);
        final Boolean protocol = (Boolean) PROTOCOL.lookUp(params);
        final String user = (String) USERNAME.lookUp(params);
        final String pass = (String) PASSWORD.lookUp(params);
        final int timeoutMillis = (Integer) TIMEOUT.lookUp(params);
        final int buffer = (Integer) BUFFER_SIZE.lookUp(params);
        final boolean tryGZIP = (Boolean) TRY_GZIP.lookUp(params);
        final boolean lenient = (Boolean) LENIENT.lookUp(params);
        final String encoding = (String) ENCODING.lookUp(params);
        final Integer maxFeatures = (Integer) MAXFEATURES.lookUp(params);
        final Charset defaultEncoding = Charset.forName(encoding);

        if (((user == null) && (pass != null)) || ((pass == null) && (user != null))) {
            throw new IOException(
                    "Cannot define only one of USERNAME or PASSWORD, must define both or neither");
        }

        final WFSDataStore dataStore;

        final HTTPProtocol http = new SimpleHttpProtocol();
        http.setTryGzip(tryGZIP);
        http.setAuth(user, pass);
        http.setTimeoutMillis(timeoutMillis);

        final byte[] wfsCapabilitiesRawData = loadCapabilities(getCapabilitiesRequest, http);
        final Document capsDoc = parseCapabilities(wfsCapabilitiesRawData);
        final Element rootElement = capsDoc.getDocumentElement();

        final String capsVersion = rootElement.getAttribute("version");
        final Version version = Version.find(capsVersion);

        if (Version.v1_0_0 == version) {
            final ConnectionFactory connectionFac = new DefaultConnectionFactory(tryGZIP, user,
                    pass, defaultEncoding, timeoutMillis);
            InputStream reader = new ByteArrayInputStream(wfsCapabilitiesRawData);
            final WFS100ProtocolHandler protocolHandler = new WFS100ProtocolHandler(reader,
                    connectionFac);

            try {
                HttpMethod prefferredProtocol = Boolean.TRUE.equals(protocol) ? POST : GET;
                dataStore = new WFS_1_0_0_DataStore(prefferredProtocol, protocolHandler,
                        timeoutMillis, buffer, lenient);
            } catch (SAXException e) {
                logger.warning(e.toString());
                throw new IOException(e.toString());
            }
        } else {
            InputStream capsIn = new ByteArrayInputStream(wfsCapabilitiesRawData);

            WFS_1_1_0_Protocol wfs = new WFS_1_1_0_Protocol(capsIn, http);

            WFSStrategy strategy = determineCorrectStrategy(getCapabilitiesRequest, capsDoc);
            wfs.setStrategy(strategy);
            dataStore = new WFS_1_1_0_DataStore(wfs);
            dataStore.setMaxFeatures(maxFeatures);
            dataStore.setPreferPostOverGet(protocol);
        }

        perParameterSetDataStoreCache.put(new HashMap(params), dataStore);
        return dataStore;
    }

    private static Document parseCapabilities(final byte[] wfsCapabilitiesRawData)
            throws IOException, DataSourceException {
        Document capsDoc;
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(wfsCapabilitiesRawData);
            capsDoc = parseCapabilities(inputStream);
            Element root = capsDoc.getDocumentElement();
            String localName = root.getLocalName();
            String namespace = root.getNamespaceURI();
            if (!WFS.NAMESPACE.equals(namespace)
                    || !WFS.WFS_Capabilities.getLocalPart().equals(localName)) {
                if ("http://www.opengis.net/ows".equals(namespace)
                        && "ExceptionReport".equals(localName)) {
                    StringBuffer message = new StringBuffer();
                    Element exception = (Element) capsDoc.getElementsByTagNameNS("*", "Exception")
                            .item(0);
                    if (exception == null) {
                        throw new DataSourceException(
                                "Exception Report when requesting capabilities");
                    }
                    Node exceptionCode = exception.getAttributes().getNamedItem("exceptionCode");
                    Node locator = exception.getAttributes().getNamedItem("locator");
                    Node exceptionText = exception.getElementsByTagNameNS("*", "ExceptionText")
                            .item(0);

                    message.append("Exception Report ");
                    String text = exceptionText.getTextContent();
                    if (text != null) {
                        message.append(text.trim());
                    }
                    message.append(" Exception Code:");
                    message.append(exceptionCode == null ? "" : exceptionCode.getTextContent());
                    message.append(" Locator: ");
                    message.append(locator == null ? "" : locator.getTextContent());
                    throw new DataSourceException(message.toString());
                }
                throw new DataSourceException("Expected " + WFS.WFS_Capabilities + " but was "
                        + namespace + "#" + localName);
            }
        }
        return capsDoc;
    }

    static WFSStrategy determineCorrectStrategy(URL getCapabilitiesRequest, Document capabilitiesDoc) {
        WFSStrategy strategy = null;

        // look in comments for indication of CubeWerx server
        NodeList childNodes = capabilitiesDoc.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.COMMENT_NODE) {
                String nodeValue = child.getNodeValue();
                nodeValue = nodeValue.toLowerCase();
                if (nodeValue.contains("cubewerx")) {
                    strategy = new CubeWerxStrategy();
                    break;
                }
            }
        }

        if (strategy == null) {
            // Ionic declares its own namespace so that's our hook
            Element root = capabilitiesDoc.getDocumentElement();
            String ionicNs = root.getAttribute("xmlns:ionic");
            if (ionicNs != null) {
                if (ionicNs.equals("http://www.ionicsoft.com/versions/4")) {
                    strategy = new IonicStrategy();
                } else if (ionicNs.startsWith("http://www.ionicsoft.com/versions")) {
                    logger
                            .warning("Found a Ionic server but the version may not match the strategy "
                                    + "we have (v.4). Ionic namespace url: " + ionicNs);
                    strategy = new IonicStrategy();
                }
            }
        }

        if (strategy == null) {
            // guess server implementation from capabilities URI
            String uri = getCapabilitiesRequest.toExternalForm();
            if (uri.contains("geoserver")) {
                strategy = new GeoServerStrategy();
            }else if (uri.contains("/ArcGIS/services/")){
                strategy = new ArcGISServerStrategy();
            }
        }

        if (strategy == null) {
            // use fallback strategy
            strategy = new DefaultWFSStrategy();
        }
        logger.info("Using WFS Strategy: " + strategy.getClass().getName());
        return strategy;
    }

    /**
     * Unsupported operation, can't create a WFS service.
     * 
     * @throws UnsupportedOperationException
     *             always, as this operation is not applicable to WFS.
     * @see org.geotools.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    public DataStore createNewDataStore(final Map params) throws IOException {
        throw new UnsupportedOperationException("Operation not applicable to a WFS service");
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getDescription()
     */
    public String getDescription() {
        return "The WFSDataStore represents a connection to a Web Feature Server. This connection provides access to the Features published by the server, and the ability to perform transactions on the server (when supported / allowed).";
    }

    /**
     * Returns the set of parameter descriptors needed to connect to a WFS.
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
     * Checks whether {@code params} contains a valid set of parameters to connecto to a WFS.
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
            return false; // throw new NullPointerException("params");
        }
        try {
            URL url = (URL) URL.lookUp(params);
            if( !"http".equalsIgnoreCase(url.getProtocol()) && !"https".equalsIgnoreCase(url.getProtocol())){
                return false; // must be http or https since we use SimpleHttpProtocol class
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
        return true;
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getDisplayName()
     */
    public String getDisplayName() {
        return "Web Feature Server";
    }

    /**
     * @return {@code true}, no extra or external requisites for datastore availability.
     * @see org.geotools.data.DataStoreFactorySpi#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Creates a HTTP GET Method based WFS {@code GetCapabilities} request for the given protocol
     * version.
     * <p>
     * If the query string in the {@code host} URL already contains a VERSION number, that version
     * is <b>discarded</b>.
     * </p>
     * 
     * @param host
     *            non null URL from which to construct the WFS {@code GetCapabilities} request by
     *            discarding the query string, if any, and appending the propper query string.
     * @return
     */
    public static URL createGetCapabilitiesRequest(URL host, Version version) {
        if (host == null) {
            throw new NullPointerException("null url");
        }
        if (version == null) {
            throw new NullPointerException("version");
        }
        HTTPProtocol httpUtils = new SimpleHttpProtocol();
        Map<String, String> getCapsKvp = new HashMap<String, String>();
        getCapsKvp.put("SERVICE", "WFS");
        getCapsKvp.put("REQUEST", "GetCapabilities");
        getCapsKvp.put("VERSION", version.toString());
        URL getcapsUrl;
        try {
            getcapsUrl = httpUtils.createUrl(host, getCapsKvp);
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Can't create GetCapabilities request from " + host, e);
            throw new RuntimeException(e);
        }

        return getcapsUrl;
    }

    /**
     * Creates a HTTP GET Method based WFS {@code GetCapabilities} request.
     * <p>
     * If the query string in the {@code host} URL already contains a VERSION number, that version
     * is used, otherwise the queried version will be 1.0.0.
     * </p>
     * <p>
     * <b>NOTE</b> the default version will be 1.0.0 until the support for 1.1.0 gets stable enough
     * for general use. If you want to use a 1.1.0 WFS you'll have to explicitly provide the
     * VERSION=1.1.0 parameter in the GetCapabilities request meanwhile.
     * </p>
     * 
     * @param host
     *            non null URL pointing either to a base WFS service access point, or to a full
     *            {@code GetCapabilities} request.
     * @return
     */
    public static URL createGetCapabilitiesRequest(final URL host) {
        if (host == null) {
            throw new NullPointerException("url");
        }

        String queryString = host.getQuery();
        queryString = queryString == null || "".equals(queryString.trim()) ? "" : queryString
                .toUpperCase();

        // final Version defaultVersion = Version.highest();
        
        // We cannot use the highest vesion as the default yet
        // since v1_1_0 does not implement a read/write datastore
        // and is still having trouble with requests from
        // different projections etc...
        //
        // this is a result of the udig code sprint QA run
        final Version defaultVersion = Version.v1_0_0;
        // which version to use
        Version requestVersion = defaultVersion;

        if (queryString.length() > 0) {

            Map<String, String> params = new HashMap<String, String>();
            String[] split = queryString.split("&");
            for (String kvp : split) {
                int index = kvp.indexOf('=');
                String key = index > 0 ? kvp.substring(0, index) : kvp;
                String value = index > 0 ? kvp.substring(index + 1) : null;
                params.put(key, value);
            }

            String request = params.get("REQUEST");
            if ("GETCAPABILITIES".equals(request)) {
                String version = params.get("VERSION");
                if (version != null) {
                    requestVersion = Version.find(version);
                    if (requestVersion == null) {
                        requestVersion = defaultVersion;
                    }
                }
            }
        }
        return createGetCapabilitiesRequest(host, requestVersion);
    }

    /**
     * Package visible to be overridden by unit test.
     * 
     * @param capabilitiesUrl
     * @param tryGZIP
     * @param auth
     * @return
     * @throws IOException
     */
    byte[] loadCapabilities(final URL capabilitiesUrl, HTTPProtocol http) throws IOException {
        byte[] wfsCapabilitiesRawData;

        HTTPResponse httpResponse = http.issueGet(capabilitiesUrl, Collections.EMPTY_MAP);
        InputStream inputStream = httpResponse.getResponseStream();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int readCount;
        while ((readCount = inputStream.read(buff)) != -1) {
            out.write(buff, 0, readCount);
        }
        wfsCapabilitiesRawData = out.toByteArray();
        return wfsCapabilitiesRawData;
    }

    static Document parseCapabilities(InputStream inputStream) throws IOException,
            DataSourceException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document;
        try {
            document = documentBuilder.parse(inputStream);
        } catch (SAXException e) {
            throw new DataSourceException("Error parsing capabilities document", e);
        }
        return document;
    }
}
