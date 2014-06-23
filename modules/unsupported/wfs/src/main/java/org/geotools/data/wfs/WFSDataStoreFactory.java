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

import static org.geotools.data.wfs.protocol.http.HttpUtil.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.geotools.data.Parameter;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.data.wfs.protocol.wfs.Version;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.data.wfs.v1_0_0.WFS100ProtocolHandler;
import org.geotools.data.wfs.v1_0_0.WFS_1_0_0_DataStore;
import org.geotools.data.wfs.v1_1_0.ArcGISServerStrategy;
import org.geotools.data.wfs.v1_1_0.CubeWerxStrategy;
import org.geotools.data.wfs.v1_1_0.DefaultWFSStrategy;
import org.geotools.data.wfs.v1_1_0.GeoServerStrategy;
import org.geotools.data.wfs.v1_1_0.IonicStrategy;
import org.geotools.data.wfs.v1_1_0.MapServerStrategy;
import org.geotools.data.wfs.v1_1_0.WFSStrategy;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_Protocol;
import org.geotools.util.logging.Logging;
import org.geotools.wfs.WFS;
import org.geotools.wfs.protocol.ConnectionFactory;
import org.geotools.wfs.protocol.DefaultConnectionFactory;
import org.geotools.xml.XMLHandlerHints;
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
 *
 *
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools
 *         /data/wfs/WFSDataStoreFactory.java $
 * @see WFSDataStore
 * @see WFSProtocol
 * @see WFSStrategy
 */
@SuppressWarnings( { "unchecked", "nls" })
public class WFSDataStoreFactory extends AbstractDataStoreFactory {

    private static final Logger logger = Logging.getLogger("org.geotools.data.wfs");
    private HTTPClient http = new SimpleHttpClient();
    
    /**
     * A {@link Param} subclass that allows to provide a default value to the lookUp method.
     * 
     * @author Gabriel Roldan
     * @version $Id$
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
        public WFSFactoryParam(String key, Class<T> type, String description, T defaultValue) {
            super(key, type, description, false, defaultValue);
            this.defaultValue = defaultValue;
        }

        public WFSFactoryParam(String key, Class<T> type, String description, T defaultValue,
                Object... metadata) {
            super(key, type, description, false, defaultValue, metadata);
            this.defaultValue = defaultValue;
        }
        
        public T lookUp(final Map params) throws IOException {
            T parameter = (T) super.lookUp(params);
            return parameter == null ? defaultValue : parameter;
        }
    }

    /** Access with {@link WFSDataStoreFactory#getParametersInfo() */
    private static final WFSFactoryParam<?>[] parametersInfo = new WFSFactoryParam[17];

    /**
     * Mandatory DataStore parameter indicating the URL for the WFS GetCapabilities document.
     */
    public static final WFSFactoryParam<URL> URL;
    static {
        String name = "WFSDataStoreFactory:GET_CAPABILITIES_URL";
        String description = "Represents a URL to the getCapabilities document or a server instance.";
        parametersInfo[0] = URL = new WFSFactoryParam<URL>(name, URL.class, description);
    }

    /**
     * Optional {@code Boolean} DataStore parameter acting as a hint for the HTTP protocol to use
     * preferably against the WFS instance, with the following semantics:
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
        parametersInfo[1] = PROTOCOL = new WFSFactoryParam<Boolean>(name, Boolean.class, description,null);
    }

    /**
     * Optional {@code String} DataStore parameter supplying the user name to use when the server
     * requires HTTP authentication
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
        parametersInfo[2] = USERNAME = new WFSFactoryParam<String>(name, String.class, description,
                null);
    }

    /**
     * Optional {@code String} DataStore parameter supplying the password to use when the server
     * requires HTTP authentication
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
        parametersInfo[3] = PASSWORD = new WFSFactoryParam<String>(name, String.class, description,
                null, Param.IS_PASSWORD, true);
    }

    /**
     * Optional {@code String} DataStore parameter supplying a JVM supported {@link Charset charset}
     * name to use as the character encoding for XML requests sent to the server.
     */
    public static final WFSFactoryParam<String> ENCODING;
    static {

        String name = "WFSDataStoreFactory:ENCODING";
        String description = "This allows the user to specify the character encoding of the "
                + "XML-Requests sent to the Server. Defaults to UTF-8";
        
        String defaultValue = "UTF-8";
        List<String> options = new ArrayList<String>(Charset.availableCharsets().keySet());
        Collections.sort(options);
        parametersInfo[4] = ENCODING = new WFSFactoryParam<String>(name, String.class, description,
                defaultValue, Parameter.OPTIONS, options);
    }
    
    /**
     * Optional {@code Integer} DataStore parameter indicating a timeout in milliseconds for the
     * HTTP connections.
     * <>p>
     * @TODO: specify if its just a connection timeout or also a read timeout
     */
    public static final WFSFactoryParam<Integer> TIMEOUT;
    static {
        String name = "WFSDataStoreFactory:TIMEOUT";
        String description = "This allows the user to specify a timeout in milliseconds. This param"
                + " has a default value of 3000ms.";
        parametersInfo[5] = TIMEOUT = new WFSFactoryParam<Integer>(name, Integer.class, description, 3000);
    }

    /**
     * Optional {@code Integer} parameter stating how many Feature instances to buffer at once. Only
     * implemented for WFS 1.0.0 support.
     */
    public static final WFSFactoryParam<Integer> BUFFER_SIZE;
    static {
        String name = "WFSDataStoreFactory:BUFFER_SIZE";
        String description = "This allows the user to specify a buffer size in features. This param "
                + "has a default value of 10 features.";
        parametersInfo[6] = BUFFER_SIZE = new WFSFactoryParam<Integer>(name, Integer.class, description, 10);
    }
    /**
     * Optional {@code Boolean} data store parameter indicating whether to set the accept GZip
     * encoding on the HTTP request headers sent to the server
     */
    public static final WFSFactoryParam<Boolean> TRY_GZIP;
    static {
        String name = "WFSDataStoreFactory:TRY_GZIP";
        String description = "Indicates that datastore should use gzip to transfer data if the server "
                + "supports it. Default is true";
        parametersInfo[7] = TRY_GZIP = new WFSFactoryParam<Boolean>(name, Boolean.class, description, Boolean.TRUE);
    }

    /**
     * Optional {@code Boolean} DataStore parameter indicating whether to be lenient about parsing
     * bad data
     */
    public static final WFSFactoryParam<Boolean> LENIENT;
    static {

        String name = "WFSDataStoreFactory:LENIENT";
        String description = "Indicates that datastore should do its best to create features from the "
                + "provided data even if it does not accurately match the schema.  Errors will "
                + "be logged but the parsing will continue if this is true.  Default is false";
        parametersInfo[8] = LENIENT = new WFSFactoryParam<Boolean>(name, Boolean.class,
                description, false);
    }

    /**
     * Optional positive {@code Integer} used as a hard limit for the amount of Features to retrieve
     * for each FeatureType. A value of zero or not providing this parameter means no limit.
     */
    public static final WFSFactoryParam<Integer> MAXFEATURES;
    static {
        String name = "WFSDataStoreFactory:MAXFEATURES";
        String description = "Positive integer used as a hard limit for the amount of Features to retrieve"
                + " for each FeatureType. A value of zero or not providing this parameter means no limit.";
        parametersInfo[9] = MAXFEATURES = new WFSFactoryParam<Integer>(name, Integer.class, description, 0 );
    }
    
    /**
     * Optional {@code Integer} DataStore parameter indicating level of compliance to WFS
     * specification
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
        List<Integer> options = Arrays.asList(new Integer[]{0,1,2} );

        parametersInfo[10] = FILTER_COMPLIANCE = new WFSFactoryParam<Integer>(name, Integer.class,
                description, null, Parameter.OPTIONS, options );
    }

    /**
     * Optional {@code String} DataStore parameter indicating either "mapserver", "geoserver",
     * "strict" or "nonstrict" strategy
     */
    public static final WFSFactoryParam<String> WFS_STRATEGY;
    static {
        String name = "WFSDataStoreFactory:WFS_STRATEGY";
        String description = "Override wfs stragegy with either arcgis, cubwerx, ionic, mapserver"
                + ", geoserver, strict or nonstrict strategy.";
        List<String> options = Arrays.asList(new String[] { "strict", "nonstrict",
                "mapserver", "geoserver", "arcgis", "cubewerx", "ionix" });
        parametersInfo[11] = WFS_STRATEGY = new WFSFactoryParam<String>(name, String.class,
                description, null, Parameter.OPTIONS, options);
    }

    /**
     * Optional {@code String} namespace URI to override the originial namespaces
     */
    public static final WFSFactoryParam<String> NAMESPACE;
    static {
        String name = "namespace";
        String description = "Override the original WFS type name namespaces";
        parametersInfo[12] = NAMESPACE = new WFSFactoryParam<String>(name, String.class,
                description, null);
    }
    
    /**
     * Optional {@code String} Flag to disable usage of OtherSRS in requests and
     * always use DefaultSRS (eventually reprojecting locally the results)
     */
    public static final WFSFactoryParam<Boolean> USEDEFAULTSRS;
    static {
        String name = "usedefaultsrs";
        String description = "Use always the declared DefaultSRS for requests and reproject locally if necessary";
        parametersInfo[13] = USEDEFAULTSRS = new WFSFactoryParam<Boolean>(name,
                Boolean.class, description, false);
    }

    
    /**
     * Optional {@code String} DataStore parameter indicating axis order used by the
     * remote WFS server in result coordinates.
     */
    public static final WFSFactoryParam<String> AXIS_ORDER;
    static {
        String name = "WFSDataStoreFactory:AXIS_ORDER";
        String description = "Indicates axis order used by the remote WFS server in result coordinates. It applies only to WFS 1.1.0 servers. "
                + "Default is " + WFSDataStore.AXIS_ORDER_COMPLIANT;
        List<String> options = Arrays.asList(new String[] {
                WFSDataStore.AXIS_ORDER_COMPLIANT,
                WFSDataStore.AXIS_ORDER_EAST_NORTH,
                WFSDataStore.AXIS_ORDER_NORTH_EAST });
        parametersInfo[14] = AXIS_ORDER = new WFSFactoryParam<String>(name,
                String.class, description, WFSDataStore.AXIS_ORDER_COMPLIANT,
                Parameter.OPTIONS, options);
    }
    
    public static final WFSFactoryParam<String> AXIS_ORDER_FILTER;
    static {
        String name = "WFSDataStoreFactory:AXIS_ORDER_FILTER";
        String description = "Indicates axis order used by the remote WFS server for filters. It applies only to WFS 1.1.0 servers. "
                + "Default is the same as AXIS_ORDER";
        List<String> options = Arrays.asList(new String[] {
                WFSDataStore.AXIS_ORDER_COMPLIANT,
                WFSDataStore.AXIS_ORDER_EAST_NORTH,
                WFSDataStore.AXIS_ORDER_NORTH_EAST });
        parametersInfo[15] = AXIS_ORDER_FILTER = new WFSFactoryParam<String>(name,
                String.class, description, null, Parameter.OPTIONS, options);
    }
    
    public static final WFSFactoryParam<String> OUTPUTFORMAT;
    static {
        String name = "WFSDataStoreFactory:OUTPUTFORMAT";
        String description = "This allows the user to specify an outputFormat, different from the default one.";
    
        parametersInfo[16] = OUTPUTFORMAT = new WFSFactoryParam<String>(name,
                String.class, description, null);
    }
    
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
        final String wfsStrategy = (String) WFS_STRATEGY.lookUp(params);
        final Integer filterCompliance = (Integer) FILTER_COMPLIANCE.lookUp(params);
        final String namespaceOverride = (String) NAMESPACE.lookUp(params);
        final Boolean useDefaultSRS = (Boolean) USEDEFAULTSRS.lookUp(params);
        final String axisOrder = (String) AXIS_ORDER.lookUp(params);
        final String axisOrderFilter = (String) AXIS_ORDER_FILTER.lookUp(params) == null ? (String) AXIS_ORDER
                .lookUp(params) : (String) AXIS_ORDER_FILTER.lookUp(params);
        
                final String outputFormat = (String) OUTPUTFORMAT.lookUp(params);
                
        if (((user == null) && (pass != null)) || ((pass == null) && (user != null))) {
            throw new IOException(
                    "Cannot define only one of USERNAME or PASSWORD, must define both or neither");
        }

        final WFSDataStore dataStore;

        http.setTryGzip(tryGZIP);
        http.setUser(user);
        http.setPassword(pass);
        http.setConnectTimeout(timeoutMillis / 1000);
        http.setReadTimeout(timeoutMillis / 1000);
        
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
                        timeoutMillis, buffer, lenient, wfsStrategy, filterCompliance);
            } catch (SAXException e) {
                logger.warning(e.toString());
                throw new IOException(e.toString());
            }
        } else {
            InputStream capsIn = new ByteArrayInputStream(wfsCapabilitiesRawData);

            WFSStrategy strategy = determineCorrectStrategy(getCapabilitiesRequest, capsDoc, wfsStrategy );
            
            WFS_1_1_0_Protocol wfs = new WFS_1_1_0_Protocol(capsIn, http, defaultEncoding, strategy);

            dataStore = new WFS_1_1_0_DataStore(wfs);
            dataStore.setMaxFeatures(maxFeatures);
            dataStore.setPreferPostOverGet(protocol);
            dataStore.setUseDefaultSRS(useDefaultSRS);
            ((WFS_1_1_0_DataStore) dataStore).setAxisOrder(axisOrder,
                    axisOrderFilter);
            ((WFS_1_1_0_DataStore) dataStore).setGetFeatureOutputFormat(outputFormat);
            ((WFS_1_1_0_DataStore) dataStore).setMappedURIs(strategy
                    .getNamespaceURIMappings());
        }
        dataStore.setNamespaceOverride(namespaceOverride);
        

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

    /**
     * Determine correct WFSStrategy based on capabilities document.
     * 
     * @param getCapabilitiesRequest
     * @param capabilitiesDoc
     * @param override optional override provided by user
     * @return WFSStrategy to use
     */
    static WFSStrategy determineCorrectStrategy(URL getCapabilitiesRequest, Document capabilitiesDoc, String override) {
        WFSStrategy strategy = null;
        // override
        if( override != null ){
            if( override.equalsIgnoreCase("geoserver")){
                strategy = new GeoServerStrategy();
            }
            else if( override.equalsIgnoreCase("mapserver")){
                strategy = new MapServerStrategy();
            }
            else if( override.equalsIgnoreCase("arcgis")){
                strategy = new ArcGISServerStrategy();
            }
            else if( override.equalsIgnoreCase("cubewerx")){
                strategy = new CubeWerxStrategy();
            }
            else if( override.equalsIgnoreCase("ionic")){
                strategy = new IonicStrategy();
            }
            else {
                logger.warning("Could not handle wfs strategy override "+override+" proceeding with autodetection");
            }
        }
        // auto detection
        if( strategy == null ){
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

    public void setHTTPClient(HTTPClient http) {
        this.http = http;
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
     * @see #USEDEFAULTSRS
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

        Map<String, String> getCapsKvp = new HashMap<String, String>();
        getCapsKvp.put("SERVICE", "WFS");
        getCapsKvp.put("REQUEST", "GetCapabilities");
        getCapsKvp.put("VERSION", version.toString());
        URL getcapsUrl;
        try {
            getcapsUrl = createUrl(host, getCapsKvp);
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
    byte[] loadCapabilities(final URL capabilitiesUrl, HTTPClient http) throws IOException {
        byte[] wfsCapabilitiesRawData;

        HTTPResponse httpResponse = http.get(capabilitiesUrl);
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
