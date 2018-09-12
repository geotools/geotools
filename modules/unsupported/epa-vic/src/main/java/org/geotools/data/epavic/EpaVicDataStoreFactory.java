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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

/**
 * Data store factory class
 *
 * @author lmorandini
 */
public class EpaVicDataStoreFactory implements DataStoreFactorySpi {

    public static final String FACTORY_NAME = "EPAVIC";

    public static final String FACTORY_DESCRIPTION = "EPA VIC data services datastore";

    private static List<Param> paramMetadata = new ArrayList<Param>(10);

    public static final Param NAMESPACE_PARAM = new Param("namespace", String.class, "", true);

    public static final Param URL_PARAM =
            new Param(
                    "EPA VIC web service",
                    String.class,
                    "Endpoint of the EPA VIC web service",
                    true,
                    "http://sciwebsvc.epa.vic.gov.au/aqapi");

    static {
        paramMetadata.add(NAMESPACE_PARAM);
        paramMetadata.add(URL_PARAM);
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        return new EpaVicDatastore(
                (String) params.get(NAMESPACE_PARAM.key), (String) params.get(URL_PARAM.key));
    }

    @Override
    public String getDisplayName() {
        return FACTORY_NAME;
    }

    @Override
    public String getDescription() {
        return FACTORY_DESCRIPTION;
    }

    @Override
    public Param[] getParametersInfo() {
        return paramMetadata.toArray(new Param[paramMetadata.size()]);
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {

        try {
            new URL((String) params.get(EpaVicDataStoreFactory.NAMESPACE_PARAM.key));
            new URL((String) params.get(EpaVicDataStoreFactory.URL_PARAM.key));
        } catch (MalformedURLException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return null;
    }
}
