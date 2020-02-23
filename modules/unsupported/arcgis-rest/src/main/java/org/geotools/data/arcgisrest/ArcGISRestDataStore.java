/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.geotools.data.Query;
import org.geotools.data.arcgisrest.schema.catalog.Catalog;
import org.geotools.data.arcgisrest.schema.catalog.Dataset;
import org.geotools.data.arcgisrest.schema.catalog.Error__1;
import org.geotools.data.arcgisrest.schema.services.feature.Featureserver;
import org.geotools.data.arcgisrest.schema.webservice.Webservice;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.util.UnsupportedImplementationException;
import org.opengis.feature.type.Name;

/**
 * Main class of the data store
 *
 * @author lmorandini
 */
public class ArcGISRestDataStore extends ContentDataStore {

    // API version supported
    public static final double MINIMUM_API_VERSION = 10.41;

    // Common parameters used in the API
    public static final String GEOMETRYTYPE_PARAM = "geometryType";
    public static final String GEOMETRY_PARAM = "geometry";
    public static final String COUNT_PARAM = "returnCountOnly";
    public static final String FORMAT_PARAM = "f";
    public static final String ATTRIBUTES_PARAM = "outFields";
    public static final String WITHGEOMETRY_PARAM = "returnGeometry";

    // Parameter values
    public static final String FORMAT_JSON = "json";
    public static final String FORMAT_GEOJSON = "geojson";
    public static final String FORMAT_ESRIREST = "Esri REST";
    public static final String CAPABILITIES_QUERY = "Query";

    // Request parameters
    protected static final int REQUEST_THREADS = 5;
    protected static final int REQUEST_TIMEOUT = 60;

    // Default request parameter values
    public static List<NameValuePair> DEFAULT_PARAMS =
            new ArrayList<NameValuePair>() {
                {
                    add(new BasicNameValuePair(FORMAT_PARAM, FORMAT_JSON));
                    add(new BasicNameValuePair(WITHGEOMETRY_PARAM, "true"));
                    add(new BasicNameValuePair(GEOMETRYTYPE_PARAM, "esriGeometryEnvelope"));
                }
            };

    // Default get features request parameter values
    public static List<NameValuePair> DEFAULT_GETFEATURES_PARAMS =
            new ArrayList<NameValuePair>() {
                {
                    add(new BasicNameValuePair(FORMAT_PARAM, FORMAT_GEOJSON));
                    add(new BasicNameValuePair(WITHGEOMETRY_PARAM, "true"));
                    add(new BasicNameValuePair(GEOMETRYTYPE_PARAM, "esriGeometryEnvelope"));
                }
            };

    // ArcGIS Server parameters
    public static String FEATURESERVER_SERVICE = "FeatureServer";

    // Cache of feature sources
    protected Map<Name, ArcGISRestFeatureSource> featureSources =
            new HashMap<Name, ArcGISRestFeatureSource>();

    // Default feature type geometry attribute
    public static final String GEOMETRY_ATTR = "geometry";

    protected URL namespace;
    protected URL apiUrl;
    protected boolean opendataFlag = false;
    protected String user;
    protected String password;
    protected Catalog catalog;
    protected Map<Name, Dataset> datasets = new HashMap<Name, Dataset>();

    public ArcGISRestDataStore(
            String namespaceIn,
            String apiEndpoint,
            boolean opendataFlagIn,
            String user,
            String password)
            throws MalformedURLException, JsonSyntaxException, IOException {

        super();

        try {
            this.namespace = new URL(namespaceIn);
        } catch (MalformedURLException e) {
            LOGGER.log(
                    Level.SEVERE, "Namespace \"" + namespaceIn + "\" is not properly formatted", e);
            throw (e);
        }
        try {
            this.apiUrl = new URL(apiEndpoint);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "URL \"" + apiEndpoint + "\" is not properly formatted", e);
            throw (e);
        }
        this.user = user;
        this.password = password;
        this.opendataFlag = opendataFlagIn;

        // Retrieves the catalog JSON document
        String response = null;
        Error__1 err;
        try {
            response =
                    ArcGISRestDataStore.inputStreamToString(
                            this.retrieveJSON("GET", apiUrl, DEFAULT_PARAMS));
        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Error during retrieval of service '" + apiUrl + "' " + e.getMessage(),
                    e);
            throw (e);
        }

        // Gets the catalog of web services in either the Open Data catalog, or
        // the ArcGIS Server list of services

        // If this is the Open Data catalog, it loads it
        if (this.opendataFlag == true) {
            this.catalog = (new Gson()).fromJson(response, Catalog.class);
            if (this.catalog == null) {
                throw (new JsonSyntaxException("Malformed JSON"));
            }

            // It it is an ArcGIS Server, cycles through the services list to
            // retrieve the web services URL of the FeautreServers
        } else {
            this.catalog = new Catalog();

            Featureserver featureServer = null;

            try {
                featureServer = (new Gson()).fromJson(response, Featureserver.class);
                if (featureServer == null) {
                    throw (new JsonSyntaxException("Malformed JSON"));
                }
            } catch (JsonSyntaxException e) {
                // Checks whether we have an ArcGIS error message
                Error__1 errWS = (new Gson()).fromJson(response, Error__1.class);
                LOGGER.log(
                        Level.SEVERE,
                        "Error during retrieval of feature server "
                                + errWS.getCode()
                                + " "
                                + errWS.getMessage(),
                        e);
                return;
            }

            // Checks API version and output format of the endpoint
            if (featureServer.getCurrentVersion() < MINIMUM_API_VERSION
                    || featureServer
                                    .getSupportedQueryFormats()
                                    .toString()
                                    .toLowerCase()
                                    .contains(FORMAT_JSON.toLowerCase())
                            == false) {
                UnsupportedImplementationException e =
                        new UnsupportedImplementationException(
                                "FeatureServer "
                                        + apiEndpoint
                                        + " does not support either the minimum API version required, or the GeoJSON format");
                LOGGER.log(Level.SEVERE, e.getMessage());
                throw (e);
            }

            try {
                String featureServerURLString = apiUrl.toString();
                featureServer
                        .getLayers()
                        .forEach(
                                layer -> {
                                    Dataset ds = new Dataset();
                                    ds.setWebService(featureServerURLString + "/" + layer.getId());
                                    this.catalog.getDataset().add(ds);
                                });
            } catch (JsonSyntaxException e) {
                // Checks whether we have an AercGIS error message
                err = (new Gson()).fromJson(response, Error__1.class);
                LOGGER.log(
                        Level.SEVERE,
                        "JSON syntax error " + err.getCode() + " " + err.getMessage(),
                        e);
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

        if (this.entries.isEmpty() == false) {
            return new ArrayList<Name>(this.entries.keySet());
        }

        final List<Dataset> datasetList = this.getCatalog().getDataset();
        List<Name> typeNames = new ArrayList<Name>();

        // Starts an executor with a fixed numbe rof threads
        ExecutorService executor = Executors.newFixedThreadPool(REQUEST_THREADS);

        /**
         * Since there could be many datasets in the FeatureServer, it makes sense to parallelize
         * the requests to cut down processing time
         */
        final class WsCallResult {
            public Dataset dataset;
            public Webservice webservice;

            public WsCallResult(Dataset ds, Webservice ws) {
                this.dataset = ds;
                this.webservice = ws;
            };
        }

        final class WsCall implements Callable<WsCallResult> {
            public final Dataset dataset;

            public WsCall(Dataset dsIn) {
                this.dataset = dsIn;
            }

            public WsCallResult call() throws Exception {

                Webservice ws = null;
                InputStream responseWs = null;
                String responseWSString = null;

                try {
                    responseWSString =
                            ArcGISRestDataStore.inputStreamToString(
                                    retrieveJSON(
                                            "GET",
                                            new URL(this.dataset.getWebService().toString()),
                                            ArcGISRestDataStore.DEFAULT_PARAMS));
                } catch (IOException e) {
                    LOGGER.log(
                            Level.SEVERE,
                            "Error during retrieval of dataset '"
                                    + this.dataset.getWebService()
                                    + "' "
                                    + e.getMessage(),
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
                    Error__1 errWS = (new Gson()).fromJson(responseWSString, Error__1.class);
                    LOGGER.log(
                            Level.SEVERE,
                            "Error during retrieval of dataset "
                                    + this.dataset.getWebService()
                                    + " "
                                    + errWS.getCode()
                                    + " "
                                    + errWS.getMessage(),
                            e);
                    return null;
                }

                // Checks whether the web-service API version is supported and
                // supports GeoJSON
                if (ws.getCurrentVersion() < MINIMUM_API_VERSION
                        || ws.getSupportedQueryFormats()
                                        .toString()
                                        .toLowerCase()
                                        .contains(FORMAT_JSON.toLowerCase())
                                == false) {
                    LOGGER.log(
                            Level.SEVERE,
                            "Dataset "
                                    + this.dataset.getWebService()
                                    + " does not support either the API version supported ,or the GeoJSON format");
                    return null;
                }

                return new WsCallResult(this.dataset, ws);
            }
        }

        // Builds a list of calls to be made to retrieve FeatureServer web services
        // metadata that support the ReST API (if there are not distribution
        // elements, it
        // is supposed NOT to support it)
        try {
            Collection<WsCall> calls = new ArrayList<WsCall>();
            datasetList
                    .stream()
                    .forEach(
                            (ds) -> {
                                if (ds.getWebService().toString().contains(FEATURESERVER_SERVICE)) {
                                    calls.add(new WsCall(ds));
                                }
                            });

            List<Future<WsCallResult>> futures =
                    executor.invokeAll(
                            calls,
                            (REQUEST_TIMEOUT * calls.size()) / REQUEST_THREADS,
                            TimeUnit.SECONDS);

            for (Future<WsCallResult> future : futures) {

                WsCallResult result = future.get();

                // Checks whether the layer supports query and JSON
                // TODO: I am not quite sure this catches cases in which ESRI JSON is
                // supported, but NOT GeoJSON
                if (result != null
                        && result.webservice
                                .getSupportedQueryFormats()
                                .toLowerCase()
                                .contains(FORMAT_JSON.toLowerCase())
                        && result.webservice
                                .getCapabilities()
                                .toLowerCase()
                                .contains(CAPABILITIES_QUERY.toLowerCase())) {
                    Name dsName =
                            new NameImpl(namespace.toExternalForm(), result.webservice.getName());
                    ContentEntry entry = new ContentEntry(this, dsName);
                    this.datasets.put(dsName, result.dataset);
                    this.entries.put(dsName, entry);
                }
            }
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // Shuts down the executor thread pool
        executor.shutdown();

        // Returns the list of datastore entries
        return new ArrayList<Name>(this.entries.keySet());
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
     * Helper method returning a JSON String out of a resource belonging to a ArcGIS ReST API
     * instance (via a GET).If present, it sends authorisation.
     *
     * @param methType the method (GET or POST) to use
     * @param url The endpoint of the resource
     * @param params Request parameters
     * @return A string representing the JSON, null
     */
    public InputStream retrieveJSON(String methType, URL url, List<NameValuePair> params)
            throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();

        // Instantiates the method based on the methType parameter
        HttpRequestBase meth;
        if (methType.equals("GET")) {
            meth = new HttpGet();
        } else {
            meth = new HttpPost();
        }

        // Sets the URI, request parameters and request body (depending on method
        // type)
        URI uri;

        if (methType.equals("GET")) {
            // Builds the GET request
            try {
                uri = new URIBuilder(url.toString()).setParameters(params).build();
                meth.setURI(uri);
            } catch (URISyntaxException ex) {
                throw new IOException(ex.getMessage());
            }

            this.LOGGER.log(
                    Level.FINER, "About to query GET " + url.toString() + "?" + uri.getQuery());
        } else {
            // Builds the POST request
            try {
                uri = new URIBuilder(url.toString()).build();
                meth.setURI(uri);
            } catch (URISyntaxException ex) {
                throw new IOException(ex.getMessage());
            }

            HttpEntity body = EntityBuilder.create().chunked().setParameters(params).build();
            ((HttpPost) (meth)).setEntity(body);
            meth.setHeader(
                    HttpHeaders.CONTENT_TYPE,
                    ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
            this.LOGGER.log(
                    Level.FINER,
                    "About to query POST "
                            + url.toString()
                            + " with body: "
                            + IOUtils.toString(body.getContent(), Charset.defaultCharset()));
        }

        // Adds authorization if login/password is set
        if (this.user != null && this.password != null) {
            meth.addHeader(
                    "Authentication", (new UsernamePasswordCredentials(user, password)).toString());
        }

        CloseableHttpResponse response;

        // Re-tries the request if necessary
        while (true) {

            // Executes the request (a POST, since the URL may get too long)
            response = client.execute(meth);

            // If HTTP error, throws an exception
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new IOException(
                        "HTTP Status: "
                                + response.getStatusLine().getStatusCode()
                                + " for URL: "
                                + uri
                                + " response: "
                                + response.toString());
            }

            // Retrieve the wait period is returned by the server
            int wait = 0;
            Header header = meth.getFirstHeader("Retry-After");
            if (header != null) {
                wait = Integer.valueOf(header.getValue());
            }

            // Exits if no retry is necessary
            if (wait == 0) {
                break;
            }

            try {
                Thread.sleep(wait * 1000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "InterruptedException: " + e.getMessage());
                throw new IOException(e);
            }
        }

        // Extracts and returns the response
        return response.getEntity().getContent();
    }

    /**
     * Helper method to convert an entire InputStream to a String and close the stream
     *
     * @param istream input stream to convert to a String
     * @return the converted String
     */
    public static String inputStreamToString(InputStream istream) throws IOException {
        try {
            return IOUtils.toString(istream, Charset.defaultCharset());
        } finally {
            istream.close();
        }
    }
}
