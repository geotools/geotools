/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.directory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.util.logging.Logging;

/**
 * Wraps a factory for a potential file data store, that is, a factory that has
 * a URL or File as one of its connection, and hides the parameter differences
 * to the file cache
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
class FactoryAdapter {
    static final Logger LOGGER = Logging.getLogger(DirectoryTypeCache.class);

    DataStoreFactorySpi factory;

    Param fileParam;

    Param nsParam;

    public FactoryAdapter(DataStoreFactorySpi factory, Param fileParam,
            Param nsParam) {
        this.factory = factory;
        this.fileParam = fileParam;
        this.nsParam = nsParam;
    }

    public DataStore getStore(File curr, URI namespaceURI)
            throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        if (nsParam != null) {
            if (String.class.isAssignableFrom(nsParam.type))
                params.put(nsParam.key, namespaceURI.toString());
            else if (URI.class.isAssignableFrom(nsParam.type))
                params.put(nsParam.key, namespaceURI);
            else
                throw new RuntimeException(
                        "Don't know how to handle namespace param: "
                                + nsParam.key);
        }

        if (File.class.isAssignableFrom(fileParam.type))
            params.put(fileParam.key, curr);
        else if (URL.class.isAssignableFrom(fileParam.type))
            params.put(fileParam.key, curr.toURI().toURL());

        try {
            if (factory.canProcess(params))
                return factory.createDataStore(params);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Factory " + factory.getClass()
                    + " reports it can process parameters, "
                    + "but then fails during creation", e);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return factory.getClass().toString();
    }
}
