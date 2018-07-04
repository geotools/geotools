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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Parameter;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;

/**
 * Data store factory class
 *
 * @author lmorandini
 */
public class ArcGISRestDataStoreFactory implements DataStoreFactorySpi {

    /** Package's logger */
    protected static final Logger LOGGER =
            Logging.getLogger(ArcGISRestDataStoreFactory.class.getName());

    public static final String FACTORY_NAME = "ArcGIS ReST";
    public static final String FACTORY_DESCRIPTION = "ESRI ArcGIS ReST API data store";

    private static List<Param> paramMetadata = new ArrayList<Param>(10);

    public static final Param NAMESPACE_PARAM = new Param("namespace", String.class, "", true);
    public static final Param URL_PARAM =
            new Param(
                    "Endpoint of the ArcGSI ReST API (either the data.json URL of an OpenData catalog, or the FeatureService URL of an ArcGIS Server API)",
                    String.class,
                    "",
                    true);
    public static final Param USER_PARAM =
            new Param("Username of the endpoint", String.class, "", false, null);
    public static final Param PASSWORD_PARAM =
            new Param(
                    "Password associated with the username.",
                    String.class,
                    "",
                    false,
                    null,
                    Collections.singletonMap(Parameter.IS_PASSWORD, Boolean.TRUE));
    public static final Param ISOPENDATA_PARAM =
            new Param(
                    "Endpoint is an OpenData catalog",
                    Boolean.class,
                    new SimpleInternationalString("is the data source an OpedData servive?"),
                    true,
                    false);

    static {
        paramMetadata.add(NAMESPACE_PARAM);
        paramMetadata.add(URL_PARAM);
        paramMetadata.add(ISOPENDATA_PARAM);
        paramMetadata.add(USER_PARAM);
        paramMetadata.add(PASSWORD_PARAM);
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        return new ArcGISRestDataStore(
                (String) params.get(NAMESPACE_PARAM.key),
                (String) params.get(URL_PARAM.key),
                Boolean.parseBoolean(params.get(ISOPENDATA_PARAM.key).toString()),
                (String) params.get(USER_PARAM.key),
                (String) params.get(PASSWORD_PARAM.key));
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
        return (Param[]) paramMetadata.toArray(new Param[paramMetadata.size()]);
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {

        try {
            new URL((String) params.get(ArcGISRestDataStoreFactory.NAMESPACE_PARAM.key));
            new URL((String) params.get(ArcGISRestDataStoreFactory.URL_PARAM.key));
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
