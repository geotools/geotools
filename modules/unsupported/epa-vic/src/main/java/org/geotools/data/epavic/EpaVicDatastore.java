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

package org.geotools.data.epavic;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.geotools.data.Query;
import org.geotools.data.epavic.schema.Monitors;
import org.geotools.data.epavic.schema.Sites;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * Main class of the EPA VIC data store
 *
 * @author lmorandini
 * @author William Voorsluys
 */
public class EpaVicDatastore extends ContentDataStore {

    private static final String SITES_ENDPOINT = "Sites";

    private static final String MEASUREMENTS_ENDPOINT = "Measurements";

    private static final String MONITORS_ENDPOINT = "Monitors";

    protected Map<Name, EpaVicFeatureSource> featureSources = new HashMap<>();

    public static final String GEOMETRY_ATTR = "geometry";

    public static final String MEASUREMENT = "measurement";

    protected URL namespace;

    protected URL apiUrl;

    protected URL sitesUrl;

    protected URL monitorsUrl;

    public static final String EPACRS = "EPSG:4283";

    public EpaVicDatastore(String namespaceIn, String apiEndpoint)
            throws MalformedURLException, IOException {

        super();

        try {
            this.namespace = new URL(namespaceIn);
        } catch (MalformedURLException e) {
            LOGGER.log(
                    Level.SEVERE, "Namespace \"" + namespaceIn + "\" is not properly formatted", e);
            throw (e);
        }
        try {
            this.apiUrl = new URL(apiEndpoint + "/" + MEASUREMENTS_ENDPOINT);
            this.sitesUrl = new URL(apiEndpoint + "/" + SITES_ENDPOINT);
            this.monitorsUrl = new URL(apiEndpoint + "/" + MONITORS_ENDPOINT);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "URL \"" + apiEndpoint + "\" is not properly formatted", e);
            throw (e);
        }
    }

    @Override
    protected List<Name> createTypeNames() {
        if (!entries.isEmpty()) {
            return new ArrayList<>(entries.keySet());
        }
        NameImpl dsName = new NameImpl(namespace.toString(), MEASUREMENT);
        this.entries.put(dsName, new ContentEntry(this, dsName));
        return new ArrayList<>(entries.keySet());
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {

        EpaVicFeatureSource featureSource = this.featureSources.get(entry.getName());
        if (featureSource == null) {
            featureSource = new EpaVicFeatureSource(entry, new Query());
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
     * Retrieves a list of sites from the sites end-point
     *
     * @return list of EPA sites
     * @throws IOException
     */
    public Sites retrieveSitesJSON() throws IOException {
        ObjectMapper om = new ObjectMapper();
        String sitesURL = sitesUrl.toString();
        return om.readValue(
                new BufferedInputStream(this.retrieveJSON(null, sitesURL)), Sites.class);
    }

    /**
     * Retrieves a list of monitors from the sites end-point
     *
     * @return list of EPA monitors
     * @throws IOException
     */
    public Monitors retrieveMonitorsJSON() throws IOException {
        ObjectMapper om = new ObjectMapper();
        String monitorsURL = monitorsUrl.toString();
        LOGGER.fine("Retrieving monitors from " + monitorsURL);
        return om.readValue(this.retrieveJSON(null, monitorsURL), Monitors.class);
    }

    /**
     * Retrieves JSON from the default API URL
     *
     * @param params query parameters
     * @return A stream representing the JSON, null
     * @throws IOException
     */
    public InputStream retrieveJSON(Map<String, Object> params) throws IOException {
        return this.retrieveJSON(params, apiUrl.toString());
    }

    /**
     * Helper method returning a JSON String out of a resource belongining to a ArcGIS ReST API
     * instance (via a GET). If present, it sends authorixzation.
     *
     * @param params Request parameters
     * @param url web service URL
     * @return A stream representing the JSON, null
     * @throws IOException
     */
    public InputStream retrieveJSON(Map<String, Object> params, String url) throws IOException {

        HttpClient client = new HttpClient();

        // Sets the URI, request parameters and request body (depending on method
        // type)
        GetMethod method = new GetMethod();
        URI uri = new URI(url, false);

        if (params != null && !params.isEmpty()) {
            NameValuePair[] kvps = new NameValuePair[params.size()];
            int i = 0;
            for (Entry<String, Object> entry : params.entrySet()) {
                kvps[i++] = new NameValuePair(entry.getKey(), entry.getValue().toString());
            }
            method.setQueryString(kvps);
            uri.setQuery(method.getQueryString());
        }

        this.LOGGER.log(
                Level.FINER,
                "About to query GET " + url.toString() + "?" + method.getQueryString());
        method.setURI(uri);

        // Re-tries the request if necessary
        while (true) {

            int status = client.executeMethod(method);

            // If HTTP error, throws an exception
            if (status != HttpStatus.SC_OK) {
                throw new IOException(
                        "HTTP Status: "
                                + status
                                + " for URL: "
                                + uri
                                + " response: "
                                + method.getResponseBodyAsString());
            }

            // Retrieve the wait period is returned by the server
            int wait = 0;
            Header header = method.getResponseHeader("Retry-After");
            if (header != null) {
                wait = Integer.valueOf(header.getValue());
            }

            // Exists if no retry is necessary
            if (wait == 0) {
                break;
            }

            try {
                this.LOGGER.log(
                        Level.FINE,
                        "Waiting {}s before querying the EPA Vic server again",
                        +wait * 1000);
                Thread.sleep(wait * 1000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "InterruptedException: " + e.getMessage());
                throw new IOException(e);
            }
        }

        // Extracts an returns the response
        return method.getResponseBodyAsStream();
    }
}
