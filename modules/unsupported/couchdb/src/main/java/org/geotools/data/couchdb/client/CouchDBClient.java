/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb.client;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONArray;

/**
 * A CouchDBClient is the entry point to interacting with a CouchDB instance.
 * 
 * The goal of the client is to abstract much of the HTTP client interaction
 * as possible to allow higher level use and hide the details of the client.
 * 
 * The lower level HTTP public APIs should not be used except by internal code.
 * 
 * All 'path' parameters, unless otherwise specified, are relative to the
 * root URL.
 * 
 * All 'IOException's thrown in the client are due to HTTP errors, CouchDB
 * specific errors are covered by CouchDBException.
 * 
 * The Client does not perform any checking of CouchDBResponses for errors. All
 * methods that return CouchDBResponses will never return null.
 * 
 * Notes:
 * * Using older httpclient 3.1, could upgrade to 4.x, but what impact on other
 * libraries
 * 
 * @todo thread safety requirements?
 * @author Ian Schneider (OpenGeo)
 */
public class CouchDBClient {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String MIME_TYPE_JSON = "application/json";
    private final URI root;
    private final HttpClient httpClient;
    private final HttpClientParams clientParams;
    private static final Logger logger =  Logging.getLogger(CouchDBClient.class);

    /**
     * Create a new client that will connect to a CouchDB instance at the 
     * specified root URL
     * @param root URL to instance, for example: "http://127.0.0.1:5984/"
     * @throws URIException if provided URL is invalid
     */
    public CouchDBClient(String root) throws URIException {
        this.root = new URI(root, false);

        clientParams = new HttpClientParams();
        clientParams.setParameter(HttpClientParams.USER_AGENT, "gtcouchclient");

        httpClient = new HttpClient(clientParams);
    }

    /**
     * Get a list of all database names on the instance.
     * @return non-null List of database names
     * @throws IOException if an error in communication occurs
     */
    public List<String> getDatabaseNames() throws IOException {
        CouchDBResponse response = get("_all_dbs");
        JSONArray array = response.getBodyAsJSONArray();
        if (array.size() > 0) {
            assert array.get(0).getClass() == String.class;
        }
        return (List<String>) array;
    }

    /**
     * Open a connection to an existing database.
     * @param name The name of the database to connect to
     * @return the database
     * @throws CouchDBException If there is a couch specific error (db doesn't exist)
     * @throws IOException if an error in communication occurs
     */
    public CouchDBConnection openDBConnection(String name) throws CouchDBException, IOException {
        CouchDBResponse resp = get(name);
        resp.checkOK("Unable to open DB '" + name + "'");
        return new CouchDBConnection(resp, this);
    }

    /**
     * Create a new database with the given name
     * @param name The name of the database
     * @return the new database connection
     * @throws CouchDBException If there is a couch specific error (db exists or name is invalid)
     * @throws IOException if an error in communication occurs
     */
    public CouchDBConnection createDB(String name) throws CouchDBException, IOException {
        // @todo check db name compliance as per wiki:
        //A database must be named with all lowercase letters (a-z), digits (0-9), 
        //or any of the _$()+-/ characters and must end with a slash in the URL. 
        //The name has to start with a lowercase letter (a-z). 
        //Uppercase characters are NOT ALLOWED in database names
        CouchDBResponse resp = put(name);
        resp.checkOK("Unable to create DB");
        return openDBConnection(name);
    }

    /**
     * Send a DELETE request to the given relative path 
     * @param path relative path
     * @return CouchDBResponse
     * @throws IOException if an error in communication occurs
     */
    public CouchDBResponse delete(String path) throws IOException {
        DeleteMethod delete = new DeleteMethod(url(path));
        return executeMethod(delete);
    }

    /**
     * Send a POST request with body to the given relative path
     * @param path relative path
     * @param content
     * @return CouchDBResponse
     * @throws IOException  
     */
    public CouchDBResponse post(String path, String content) throws IOException {
        PostMethod put = new PostMethod(url(path));
        try {
            put.setRequestEntity(new StringRequestEntity(content, MIME_TYPE_JSON, DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return executeMethod(put);
    }
    
    /**
     * Send a streaming POST request with body to the given relative path
     * @param path relative path
     * @param content
     * @return CouchDBResponse
     * @throws IOException  
     */
    public CouchDBResponse post(String path, RequestEntity content) throws IOException {
        PostMethod post = new PostMethod(url(path));
        post.setRequestEntity(content);
        return executeMethod(post);
    }
    

    /**
     * 
     * @param path
     * @param content
     * @return
     * @throws IOException
     */
    public CouchDBResponse post(String path, File content) throws IOException {
        return post(path, CouchDBUtils.read(content));
    }

    /**
     * Send a PUT  (for creating databases)
     * @param path 
     * @return
     * @throws IOException  
     */
    public CouchDBResponse put(String path) throws IOException {
        return put(path, (String) null);
    }

    /**
     * Send a PUT with a body (for creating documents)
     * @param path
     * @param content
     * @return
     * @throws IOException  
     */
    public CouchDBResponse put(String path, String content) throws IOException {
        PutMethod put = new PutMethod(url(path));
        if (content != null) {
            try {
                put.setRequestEntity(new StringRequestEntity(content, MIME_TYPE_JSON, DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return executeMethod(put);
    }
    
    /**
     * 
     * @param path
     * @param content
     * @return
     * @throws IOException
     */
    public CouchDBResponse put(String path, File content) throws IOException {
        return put(path,CouchDBUtils.read(content));
    }

    /**
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public CouchDBResponse get(String path) throws IOException {
        return get(path,null);
    }

    /**
     * 
     * @param path
     * @param queryParams
     * @return
     * @throws IOException
     */
    public CouchDBResponse get(String path, NameValuePair[] queryParams) throws IOException {
        HttpMethod get = new GetMethod(url(path));
        if (queryParams != null) {
            get.setQueryString(queryParams);
        }
        return executeMethod(get);
    }

    /**
     * 
     * @param method
     * @return
     * @throws IOException
     */
    private CouchDBResponse executeMethod(HttpMethod method) throws IOException {
        IOException expected = null;
        int result = -1;
        try {
            result = httpClient.executeMethod(method);
        } catch (IOException ex) {
            expected = ex;
        }
        CouchDBResponse resp = new CouchDBResponse(method, result, expected);
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("Request to : " + method.getPath());
            logger.finest("Response status : " + result);
            logger.finest("Response body:\n" + method.getResponseBodyAsString());
        }
        return resp;
    }

    private String url(String path) {
        try {
            return new URI(root, path, false).toString();
        } catch (URIException ex) {
            throw new RuntimeException("Error building URL for " + root + " " + path, ex);
        }
    }
    
    // this should support the concept of parent component, otherwise
    // children components must use their parent uri function ...
    static abstract class Component {
        protected final String root;
        protected final CouchDBClient client;
        protected Component(String root,CouchDBClient client) {
            this.root = root;
            this.client = client;
        }
        
        protected final String uri(String path) {
            return root + (path.charAt(0) == '/' ? "" : "/") + path;
        }
        
    }

}
