/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FilterCapabilities;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Intersects;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * This DataStore is based on OpenDataStore protocol using GeoJSON as the
 * encoding format loosely based on RESTFUL principles. This code expanded on 
 * from GeoREST in Unsupported Modules.
 * @author 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/sfs/src/main/java/org/geotools/data/sfs/SFSDataStore.java $
 */
public class SFSDataStore extends ContentDataStore {
    
    /**
     *  The store filter capabilities
     *  @return The filter capabilities, never <code>null</code>.
     */
    static FilterCapabilities ODS_FILTER_CAPABILITIES = new FilterCapabilities() {

        {
            
            addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);

            addType(And.class);
            
            // TODO: check these two filters are actually supported in the encoder
            addType(BBOX.class);
            addType(Intersects.class);
            
            addType(IncludeFilter.class);
            addType(ExcludeFilter.class);
            addType(PropertyIsLike.class);
        }
    };

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.simplefeatureservice");
    String baseURL;
    Map<Name, SFSLayer> layers = new LinkedHashMap<Name, SFSLayer>();
    String user;
    String password;
    int timeout;

    /**
     * Constructor -- it takes in the base URl and then appends "/capabilities"
     * to pull the list of available layers
     * @param URL 
     * 
     */
    public SFSDataStore(URL fnURL, String namespaceURI) throws IOException {
        String strURL = fnURL.toString();
        this.baseURL = strURL + (strURL.endsWith("/") ? "" : "/");
        if(baseURL.endsWith("capabilities/")) {
            baseURL = baseURL.substring(0, baseURL.length() - "capabilities/".length());
        }
        this.namespaceURI = namespaceURI;
        processCapabilities();
    }
    
    /**
     * Constructor -- it takes in the base URl and then appends "/capabilities"
     * to pull the list of available layers
     * @param URL 
     * 
     */
    public SFSDataStore(URL fnURL, String namespaceURI, String user, String password, int timeout) throws IOException {
        String strURL = fnURL.toString();
        this.baseURL = strURL + (strURL.endsWith("/") ? "" : "/");
        if(baseURL.endsWith("capabilities/")) {
            baseURL = baseURL.substring(0, baseURL.length() - "capabilities/".length());
        }
        this.namespaceURI = namespaceURI;
        this.user = user;
        this.password = password;
        this.timeout = timeout;
        processCapabilities();
    }

    /**
     * Constructor -- it simply uses the json response from the server to extract
     * the layer information. Only used for testing, not exposing it to the whole world
     * @param String
     * 
     */
    SFSDataStore(String json, String namespaceURI) throws IOException {
        this.namespaceURI = namespaceURI;
        processCapabilities(json);
    }

    /**
     * It processes the incoming URL and gets the response in the form of stream.
     * It then converts the stream into a string(it should be in json format)
     * @param 
     * 
     */
    public final void processCapabilities() throws IOException {
        processCapabilities(resourceToString("capabilities", null));
    }

    /**
     * This method processes the JSON from the server and extracts layer names
     * and populates typeNames
     * @param capabilitiesJSON
     * 
     */
    final void processCapabilities(String capabilitiesJSON) throws IOException {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(capabilitiesJSON);
            JSONArray array = (JSONArray) obj;

            Iterator itr = array.iterator();
            while (itr.hasNext()) {
                Map tmpMap = (HashMap) itr.next();
                String strName = ((String) tmpMap.get("name")).trim();
                Name name = new NameImpl(namespaceURI, strName);
                
                boolean xyOrder = (!tmpMap.containsKey("axisorder")) ? true : (tmpMap.get("axisorder").equals("xy"));
                
                String strCRS = null;
                CoordinateReferenceSystem crs = null;
                if (tmpMap.containsKey("crs")) {
                    strCRS = ((String) tmpMap.get("crs")).trim();
                    crs = SFSDataStoreUtil.decodeXY(strCRS);
                }

                Envelope envelope = null;
                if (tmpMap.containsKey("bbox")) {
                    JSONArray boundingArray = (JSONArray) tmpMap.get("bbox");
                    if (!xyOrder) {
                        SFSDataStoreUtil.flipYXInsideTheBoundingBox(boundingArray);
                    }
                    envelope = new Envelope(
                            ((Number) boundingArray.get(0)).doubleValue(), 
                            ((Number) boundingArray.get(2)).doubleValue(), 
                            ((Number) boundingArray.get(1)).doubleValue(), 
                            ((Number) boundingArray.get(3)).doubleValue()); 
                } 

                SFSLayer layer = new SFSLayer(name, xyOrder, strCRS, crs, envelope);
                layers.put(name, layer);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while parsing the capabilities", e);
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * Return the Layer Names
     * @param 
     * @return typeNames
     * @throws IOException
     * 
     */
    public List<Name> createTypeNames() throws IOException {
         return new ArrayList<Name>(layers.keySet());
    }

    /**
     * Call FeatureSource
     * @param ContentEntry
     * @return ContentFeatureSource
     * @throws IOException
     * 
     */
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {

        return new SFSFeatureSource(entry);
    }

    
    /**
     * Returns the layer definition for the given name
     * @param name
     * @return
     */
    SFSLayer getLayer(Name name) {
        return layers.get(name);
    }
    
    
    /**
     * This method requests the response from the URL either through GET/POST
     * and then converts the response to string and returns the string
     * fnData is URL encoded string
     * The request is going to be a POST one if postData is not null, a GET otherwise
     * @param is
     * @return String
     * @throws IOException
     */
    String resourceToString(String resource, String postData) throws IOException {
        InputStream is = resourceToStream(resource, postData);

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, SFSDataStoreUtil.DEFAULT_ENCODING));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } finally {
                is.close();
            }
            return sb.toString();
        }
        
        return "";
    }

    /**
     * This method requests the response from the URL either through GET/POST
     * and returns the response as a stream
     * The request is going to be a POST one if postData is not null, a GET otherwise
     * @param is
     * @return String
     * @throws IOException
     */
    InputStream resourceToStream(String resource, String postData) throws MalformedURLException,
            IOException, ProtocolException {
        boolean doPost = false;
        URL url;
        if(postData != null && (baseURL.length() + resource.length() + postData.length() < 2048)) {
            url = new URL(baseURL + resource + "?" + postData);
        } else {
            url = new URL(baseURL + resource);
        }
        
        // TODO: use commons http client + persistent connections instead
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept-Encoding", "gzip");
        
        // if we have username/password use them to setup basic auth
        if(user != null && password != null) {
            String combined = nullToEmpty(user) + ":" + nullToEmpty(password);
            byte[] authBytes = combined.getBytes("US-ASCII");
            String encoded = new String(Base64.encodeBase64(authBytes));
            urlConnection.setRequestProperty("Authorization",  "Basic " + encoded);
        }
        
        // enforce the timeout if we have one
        if(timeout > 0) {
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
        }
        
        urlConnection.setDoInput(true);
        if(doPost && postData != null && !"".equals(postData.trim())) {
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);
            
            /*write the data to server*/
            OutputStreamWriter wr = null;
            try {
                wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(postData);
                wr.flush();
            } finally {
                if(wr != null) {
                    wr.close();
                }
            }
        } else {
            urlConnection.setRequestMethod("GET");
        }
        
        /* Get the response from the server*/
        if(urlConnection.getResponseCode() != 200) {
            throw new IOException("Server reported and error with code " + 
                    urlConnection.getResponseCode() + ": " + urlConnection.getResponseMessage() + " accessing resource " + url.toExternalForm());
        } else {
            InputStream is = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            if ("gzip".equalsIgnoreCase(encoding)) {
                is = new GZIPInputStream(is);
            }
            
            return is;
        }
    }

    private String nullToEmpty(String string) {
        return string != null ? string : "";
    }

}
