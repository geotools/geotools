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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Parameter;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.util.SimpleInternationalString;


/**
 *
 * @author 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/sfs/src/main/java/org/geotools/data/sfs/SFSDataStoreFactory.java $
 */
public class SFSDataStoreFactory implements DataStoreFactorySpi {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.simplefeatureservice");
    private List<Param> parameters = new ArrayList<Param>();
    
    /**
     * Optional - uri of the FeatureType's namespace
     */
    public static final Param NAMESPACEP = new Param("namespace", String.class, "Namespace prefix", false);

    /**
     * url to the service roots
     */
    public static final Param URLP = new Param("Service Url", URL.class, "Root URL of the simple feature service", true);
    
    
    /** parameter for database user */
    public static final Param USERP = new Param("user", String.class,
    "User for services protected with HTTP basic authentication", false);

    /** parameter for database password */
    public static final Param PASSWDP = new Param("passwd", String.class,
            new SimpleInternationalString("User for services protected with HTTP basic authentication"), false, null, Collections
                    .singletonMap(Parameter.IS_PASSWORD, Boolean.TRUE));
    
    /** parameter for database user */
    public static final Param TIMEOUTP = new Param("timeout", Integer.class,
    "Timeout for HTTP connections in seconds", false, 5);

    /**
     *
     */
    public SFSDataStoreFactory() {
        parameters.add(URLP);
        parameters.add(NAMESPACEP);
        parameters.add(USERP);
        parameters.add(PASSWDP);
        parameters.add(TIMEOUTP);
    }

    /**
     * Construct a opendatastore using the params.
     * @param params
     * @return DataStore
     * @throws IOException
     */
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        if (!canProcess(params)) {
            throw new IOException("Invalid parameters at createDataStore inside Factory");
        }
        
        URL url = (URL) URLP.lookUp(params);
        String namespaceURI = (String) NAMESPACEP.lookUp(params);
        
        String user = (String) USERP.lookUp(params); 
        String password = (String) PASSWDP.lookUp(params);
        Integer timeout = (Integer) TIMEOUTP.lookUp(params);
        if(timeout == null) {
            timeout = 5000;
        } else {
            timeout *= 1000;
        }
        
        SFSDataStore store = new SFSDataStore(url, namespaceURI, user, password, timeout);
        
        return store;
    }

    /**
     * OpenDatastore cannot create a new datastore
     * @param params
     * @return DataStore
     * @throws IOException
     */
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

    /**
     * Returns a string with value "OpenDataStoreFactory"
     * @return String
     */
    public String getDisplayName() {
        return "SimpleFeatureService";
    }

    /**
     * Returns the string description of the datastore constructed by this factory.
     * @return String
     */
    public String getDescription() {
        return "SimpleFeatureService";
    }

    /**
     * Describe parameters.
     * @return Param[]
     */
    public Param[] getParametersInfo() {
        return parameters.toArray(new Param[]{});
    }

    /**
     * Checks to see if the URL is present and valid
     * @param params
     * @return boolean
     */
    public boolean canProcess(Map<String, Serializable> params) {
        /* Checking if opendatastore factory is not being called with null param*/
        if (params == null) {
            return false;
        }

        for (Param p : parameters) {
            if (!params.containsKey(p.key) && p.required) {
                return false;
            }
        }

        /* Test if we are getting valid URL or not in the factory, as this is
         * the main entry point for the URL
         */
        try {
            return ((URL) URLP.lookUp(params)) != null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "MalFormed URL in Factory: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Always returns true
     * @return boolean
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     * @return Map
     */
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
