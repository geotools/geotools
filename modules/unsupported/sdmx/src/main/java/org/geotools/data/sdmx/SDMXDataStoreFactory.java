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

package org.geotools.data.sdmx;

import it.bancaditalia.oss.sdmx.exceptions.SdmxException;
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
import org.geotools.util.logging.Logging;

/**
 * Data store factory class
 *
 * @author lmorandini
 */
public class SDMXDataStoreFactory implements DataStoreFactorySpi {

    /** Package's logger */
    protected static final Logger LOGGER = Logging.getLogger(SDMXDataStoreFactory.class.getName());

    public static final String FACTORY_NAME = "SDMX";
    public static final String FACTORY_DESCRIPTION = "SDMX 2.0/2.1 ReST API";
    public static int CONCURRENCY = 7;

    private static List<Param> paramMetadata = new ArrayList<Param>(10);

    public static final Param NAMESPACE_PARAM = new Param("namespace", String.class, "", true);
    public static final Param PROVIDER_PARAM =
            new Param(
                    "ProviderName",
                    String.class,
                    "Well-known provider name (either 'ABS' or 'ABS2')",
                    true,
                    "ABS2");
    public static final Param CONCURRENCY_PARAM =
            new Param(
                    "Concurrency",
                    Integer.class,
                    "N. of concurrent threads",
                    false,
                    SDMXDataStoreFactory.CONCURRENCY);
    public static final Param USER_PARAM =
            new Param("Username", String.class, "Username", false, null);
    public static final Param PASSWORD_PARAM =
            new Param(
                    "Password",
                    String.class,
                    "Password associated with the username",
                    false,
                    null,
                    Collections.singletonMap(Parameter.IS_PASSWORD, Boolean.TRUE));

    static {
        paramMetadata.add(NAMESPACE_PARAM);
        paramMetadata.add(PROVIDER_PARAM);
        paramMetadata.add(CONCURRENCY_PARAM);
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
        try {
            return new SDMXDataStore(
                    (String) params.get(NAMESPACE_PARAM.key),
                    (String) params.get(PROVIDER_PARAM.key),
                    (Integer) params.get(CONCURRENCY_PARAM.key),
                    (String) params.get(USER_PARAM.key),
                    (String) params.get(PASSWORD_PARAM.key));
        } catch (SdmxException e) {
            // FIXME: re-packing an exception is not nice...
            throw new IOException(e);
        }
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
            new URL((String) params.get(SDMXDataStoreFactory.NAMESPACE_PARAM.key));
        } catch (MalformedURLException e) {
            return false;
        }

        if (params.get(SDMXDataStoreFactory.PROVIDER_PARAM.key) == null
                || ((String) params.get(SDMXDataStoreFactory.PROVIDER_PARAM.key)).length() < 1) {
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
