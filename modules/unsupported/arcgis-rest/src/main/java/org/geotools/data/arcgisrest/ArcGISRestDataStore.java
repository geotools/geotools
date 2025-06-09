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
 *
 */

package org.geotools.data.arcgisrest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.geotools.api.data.Query;
import org.geotools.api.feature.type.Name;
import org.geotools.data.arcgisrest.schema.catalog.Catalog;
import org.geotools.data.arcgisrest.schema.catalog.Dataset;
import org.geotools.data.arcgisrest.schema.catalog.Error_;
import org.geotools.data.arcgisrest.schema.services.feature.Featureserver;
import org.geotools.data.arcgisrest.schema.webservice.Webservice;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.util.UnsupportedImplementationException;

/**
 * Main class of the data store
 *
 * @author lmorandini
 */
public class ArcGISRestDataStore extends ContentDataStore {

    // API version supported
    public static final double MINIMUM_API_VERSION = 10.0;

    // Common parameters used in the API
    public static final String GEOMETRYTYPE_PARAM = "geometryType";
    public static final String GEOMETRY_PARAM = "geometry";
    public static final String COUNT_PARAM = "returnCountOnly";
    public static final String FORMAT_PARAM = "f";
    public static final String GEOMETRYSRS_PARAM = "inSR";
    public static final String ATTRIBUTES_PARAM = "outFields";
    public static final String WITHGEOMETRY_PARAM = "returnGeometry";
    public static final String DATASETTYPE_FEATURELAYER = "Feature Layer";

    // Parameter values
    public static final String FORMAT_JSON = "json";
    public static final String FORMAT_GEOJSON = "geojson";
    public static final String FORMAT_ESRIREST = "Esri REST";
    public static final String CAPABILITIES_QUERY = "Query";

    // Request parameters
    protected static final int REQUEST_THREADS = 5;
    protected static final int REQUEST_TIMEOUT = 60;

    // Default request parameter values
    public static Map<String, Object> DEFAULT_PARAMS = new HashMap<>();

    static {
        DEFAULT_PARAMS.put(FORMAT_PARAM, FORMAT_JSON);
        DEFAULT_PARAMS.put(WITHGEOMETRY_PARAM, "true");
        DEFAULT_PARAMS.put(GEOMETRYTYPE_PARAM, "esriGeometryEnvelope");
        DEFAULT_PARAMS.put(GEOMETRYSRS_PARAM, "inSR");
    }

    // ArcGIS Server parameters
    public static String FEATURESERVER_SERVICE = "FeatureServer";

    // Cache of feature sources
    protected Map<Name, ArcGISRestFeatureSource> featureSources = new HashMap<>();

    // Default feature type geometry attribute
    public static final String GEOMETRY_ATTR = "geometry";

    protected URL namespace;
    protected URL apiUrl;
    protected boolean opendataFlag;
    protected String user;
    protected String password;
    protected Catalog catalog;
    protected Map<Name, Dataset> datasets = new HashMap<>();

    public ArcGISRestDataStore(
            String namespaceIn, String apiEndpoint, boolean opendataFlagIn, String user, String password)
            throws JsonSyntaxException, IOException {

        super();

        try {
            this.namespace = new URL(namespaceIn);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, String.format("Namespace '%s' is not properly formatted", namespaceIn), e);
            throw (e);
        }
        try {
            this.apiUrl = new URL(apiEndpoint);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, String.format("URL '%s' is not properly formatted", apiEndpoint), e);
            throw (e);
        }
        this.user = user;
        this.password = password;
        this.opendataFlag = opendataFlagIn;

        // Retrieves the catalog JSON document
        String response;
        Error_ err;
        try {
            response = ArcGISRestDataStore.InputStreamToString(this.retrieveJSON("GET", apiUrl, DEFAULT_PARAMS));
        } catch (IOException | URISyntaxException e) {
            LOGGER.log(
                    Level.SEVERE,
                    String.format("Error during retrieval of service '%s' %s", apiUrl, e.getMessage()),
                    e);
            throw new IOException(e);
        }

        // Gets the catalog of web services in either the Open Data catalog, or
        // the ArcGIS Server list of services

        // If this is the Open Data catalog, it loads it
        if (this.opendataFlag) {
            this.catalog = (new Gson()).fromJson(response, Catalog.class);
            if (this.catalog == null) {
                throw (new JsonSyntaxException("Malformed JSON"));
            }

            // It it is an ArcGIS Server, cycles through the services list to
            // retrieve the web services URL of the FeatureServers
        } else {
            this.catalog = new Catalog();

            Featureserver featureServer;

            try {
                featureServer = (new Gson()).fromJson(response, Featureserver.class);
                if (featureServer == null) {
                    throw (new JsonSyntaxException("Malformed JSON"));
                }
            } catch (JsonSyntaxException e) {
                // Checks whether we have an ArcGIS error message
                Error_ errWS = (new Gson()).fromJson(response, Error_.class);
                LOGGER.log(
                        Level.SEVERE,
                        "Error during retrieval of feature server " + errWS.getCode() + " " + errWS.getMessage(),
                        e);
                return;
            }

            // Checks API version and output format of the endpoint
            if (featureServer.getCurrentVersion() < MINIMUM_API_VERSION
                    || featureServer.getSupportedQueryFormats().toLowerCase().contains(FORMAT_JSON.toLowerCase())
                            == false) {
                UnsupportedImplementationException e = new UnsupportedImplementationException(String.format(
                        "FeatureServer %s does not support either the minimum API version required, or the GeoJSON format",
                        apiEndpoint));
                LOGGER.log(Level.SEVERE, e.getMessage());
                throw (e);
            }

            try {
                String featureServerURLString = apiUrl.toString();
                featureServer.getLayers().forEach(layer -> {
                    Dataset ds = new Dataset();
                    ds.setWebService(String.format("%s/%s", featureServerURLString, layer.getId()));
                    this.catalog.getDataset().add(ds);
                });
            } catch (JsonSyntaxException e) {
                // Checks whether we have an AercGIS error message
                err = (new Gson()).fromJson(response, Error_.class);
                LOGGER.log(Level.SEVERE, "JSON syntax error " + err.getCode() + " " + err.getMessage(), e);
                throw (e);
            }
        }
    }

    /**
     * Returns the datastore catalog
     *
     * @return Catalog
     */
    public Catalog getCatalog() {
        return this.catalog;
    }

    /**
     * Returns the ArcGIS ReST API dataset given its name
     *
     * @param name Dataset name
     * @return Dataset
     */
    public Dataset getDataset(Name name) {
        return this.datasets.get(name);
    }

    @Override
    protected List<Name> createTypeNames() {

        if (!this.entries.isEmpty()) {
            return new ArrayList<>(this.entries.keySet());
        }

        final List<Dataset> datasetList = this.getCatalog().getDataset();

        // Starts an executor with a fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(REQUEST_THREADS);

        /*
         * Since there could be many datasets in the FeatureServer, it makes sense to parallelize the requests to cut
         * down processing time.
         */
        final class WsCallResult {
            public final Dataset dataset;
            public final Webservice webservice;

            public WsCallResult(Dataset ds, Webservice ws) {
                this.dataset = ds;
                this.webservice = ws;
            }
        }

        final class WsCall implements Callable<WsCallResult> {
            public final Dataset dataset;
            public final String webserviceUrl;

            public WsCall(Dataset dsIn, String wsUrl) {
                this.dataset = dsIn;
                this.webserviceUrl = wsUrl;
            }

            @Override
            public WsCallResult call() throws Exception {

                Webservice ws = null;
                String responseWSString = null;

                try {
                    responseWSString = ArcGISRestDataStore.InputStreamToString(
                            retrieveJSON("GET", new URL(this.webserviceUrl), ArcGISRestDataStore.DEFAULT_PARAMS));
                } catch (IOException e) {
                    LOGGER.log(
                            Level.SEVERE,
                            String.format(
                                    "Error during retrieval of dataset '%s' %s", this.webserviceUrl, e.getMessage()),
                            e);
                    return null;
                }

                try {
                    ws = (new Gson()).fromJson(responseWSString, Webservice.class);
                    if (ws == null || ws.getCurrentVersion() == null) {
                        throw (new JsonSyntaxException("Malformed JSON"));
                    }
                } catch (JsonSyntaxException e) {
                    // Checks whether we have an ArcGIS error message
                    Error_ errWS = (new Gson()).fromJson(responseWSString, Error_.class);
                    LOGGER.log(
                            Level.SEVERE,
                            String.format(
                                    "Error during retrieval of dataset '%s' %s %s ",
                                    this.webserviceUrl, errWS.getCode(), errWS.getMessage()),
                            e);
                    return null;
                }

                // Checks whether the web-service API version is supported and
                // supports GeoJSON
                if (ws.getCurrentVersion() < MINIMUM_API_VERSION
                        || ws.getSupportedQueryFormats().toLowerCase().contains(FORMAT_JSON.toLowerCase()) == false) {
                    LOGGER.log(
                            Level.SEVERE,
                            String.format(
                                    "Dataset %s does not support either the API version supported ,or the GeoJSON format",
                                    this.webserviceUrl));
                    return null;
                }

                return new WsCallResult(this.dataset, ws);
            }
        }

        // Builds a list of calls to be made to retrieve FeatureServer web services
        // metadata that support the ReST API (if there are no distribution
        // elements, return an error)
        Collection<WsCall> calls = new ArrayList<>();
        datasetList.stream().forEach((ds) -> {
            String ws = ArcGISRestDataStore.getWebServiceEndpoint(ds);
            if (ws != null) {
                calls.add(new WsCall(ds, ws));
            }
        });

        try {
            List<Future<WsCallResult>> futures = executor.invokeAll(
                    calls, ((long) REQUEST_TIMEOUT * calls.size()) / REQUEST_THREADS, TimeUnit.SECONDS);

            for (Future<WsCallResult> future : futures) {

                try {
                    WsCallResult result = future.get();

                    // Checks whether the layer supports query and JSON
                    // TODO: I am not quite sure this catches cases in which ESRI JSON is
                    // supported, but NOT GeoJSON
                    if (result != null
                            && result.webservice.getType() != null
                            && result.webservice.getType().equalsIgnoreCase(DATASETTYPE_FEATURELAYER)
                            && result.webservice
                                    .getSupportedQueryFormats()
                                    .toLowerCase()
                                    .contains(FORMAT_JSON.toLowerCase())
                            && result.webservice.getCurrentVersion() >= MINIMUM_API_VERSION
                            && result.webservice.getName().length() > 0
                            && result.webservice
                                    .getCapabilities()
                                    .toLowerCase()
                                    .contains(CAPABILITIES_QUERY.toLowerCase())) {
                        Name dsName = new NameImpl(namespace.toExternalForm(), result.webservice.getName());
                        ContentEntry entry = new ContentEntry(this, dsName);
                        this.datasets.put(dsName, result.dataset);
                        this.entries.put(dsName, entry);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, String.format("Dataset call returned %s", e.getMessage()));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("Error during webservice calls %s", e.getMessage()), e);
            return new ArrayList<>();
        }

        executor.shutdown();

        // Returns the list of datastore entries
        return new ArrayList<>(this.entries.keySet());
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {

        ArcGISRestFeatureSource featureSource = this.featureSources.get(entry.getName());
        if (featureSource == null) {
            featureSource = new ArcGISRestFeatureSource(entry, new Query());
            this.featureSources.put(entry.getName(), featureSource);
        }

        return featureSource;
    }

    public URL getNamespace() {
        return namespace;
    }

    // TODO: ?
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Helper method returning a JSON String out of a resource belongining to a ArcGIS ReST API instance (via a GET). If
     * present, it sends authorixzation.
     *
     * @param url The endpoint of the resource
     * @param params Request parameters
     * @return A string representing the JSON, null
     * @throws IOException if any error occurs during the retrieval
     */
    public InputStream retrieveJSON(String methType, URL url, Map<String, Object> params)
            throws IOException, URISyntaxException {
        List<NameValuePair> kvps = new ArrayList<>();
        params.entrySet().stream().forEach((entry) -> {
            kvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        });

        // Instanties the method based on the methType parameter
        HttpRequestBase httpRequest;
        if (methType.equals("GET")) {
            httpRequest = new HttpGet(
                    new URIBuilder(String.valueOf(url)).addParameters(kvps).build());
            this.LOGGER.log(
                    Level.FINER,
                    String.format(
                            "About to query GET '%s?%s'",
                            url.toString(), httpRequest.getURI().getQuery()));
        } else {
            httpRequest = new HttpPost(new URIBuilder(String.valueOf(url)).build());
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(kvps, Consts.UTF_8);
            entity.setChunked(true);
            ((HttpPost) (httpRequest)).setEntity(entity);
            this.LOGGER.log(Level.FINER, String.format("About to query POST '%s' with body: %s", url, params));
        }

        httpRequest.setConfig(
                RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build());

        // Adds authorization if login/password is set
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        if (this.user != null && this.password != null) {
            credsProvider.setCredentials(
                    new AuthScope(
                            httpRequest.getURI().getHost(), httpRequest.getURI().getPort()),
                    new UsernamePasswordCredentials("user", "passwd"));
        }
        try (CloseableHttpClient client = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build()) {

            // Re-tries the request if necessary
            while (true) {

                // Executes the request (a POST, since the URL may get too long)
                try (CloseableHttpResponse httpResponse = client.execute(httpRequest)) {

                    // If HTTP error, throws an exception
                    if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        throw new IOException(String.format(
                                "HTTP Status: %d for URL: %s",
                                httpResponse.getStatusLine().getStatusCode(), httpRequest.getURI()));
                    }

                    // Retrieve the wait period is returned by the server
                    int wait = 0;
                    Header header = httpResponse.getFirstHeader("Retry-After");
                    if (header != null) {
                        wait = Integer.parseInt(header.getValue());
                    }

                    // Exits if no retry is necessary
                    if (wait == 0) {
                        return httpResponse.getEntity().getContent();
                    }

                    try {
                        this.LOGGER.log(
                                Level.FINE,
                                String.format(
                                        "Waiting %d seconds before retrying request to %s",
                                        wait, httpRequest.getURI()));
                        Thread.sleep(wait * 1000);

                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "InterruptedException: " + e.getMessage());
                        throw new IOException(e);
                    }
                }
            }
        }
    }

    /**
     * Helper method to find the web service endpoint in a Dataset (if multiple desitinations apply, chooses the first)
     *
     * @param ds Dataset to find the endpoint of
     * @return URL of the web service endpoint
     */
    public static String getWebServiceEndpoint(Dataset ds) {

        List<String> wsurl = new ArrayList<>();

        if (ds.getWebService() != null && ds.getWebService().toString().contains(FEATURESERVER_SERVICE)) {
            wsurl.add(ds.getWebService().toString());
        } else {
            if (ds.getDistribution() != null) {
                ds.getDistribution().forEach((dist) -> {
                    if (dist.getFormat().toString().equalsIgnoreCase(FORMAT_ESRIREST) && dist.getAccessURL() != null) {
                        wsurl.add(dist.getAccessURL().toString());
                    }
                });
            }
        }
        return wsurl.isEmpty() ? null : wsurl.get(0);
    }

    /**
     * Helper method to convert an entire InputStream to a String and close the steeam
     *
     * @param istream input stream to convert to a String
     * @throws IOException if an error occurs during the conversion
     * @return the converted String
     */
    public static String InputStreamToString(InputStream istream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(istream, Charset.defaultCharset()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
