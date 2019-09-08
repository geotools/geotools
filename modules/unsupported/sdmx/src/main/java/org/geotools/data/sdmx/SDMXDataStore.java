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

import it.bancaditalia.oss.sdmx.api.DataFlowStructure;
import it.bancaditalia.oss.sdmx.api.Dataflow;
import it.bancaditalia.oss.sdmx.api.GenericSDMXClient;
import it.bancaditalia.oss.sdmx.client.SDMXClientFactory;
import it.bancaditalia.oss.sdmx.exceptions.SdmxException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;

/**
 * Main class of the data store
 *
 * @author lmorandini
 */
public class SDMXDataStore extends ContentDataStore {

    // SDMX query constants
    public static String MEASURE_KEY = "MEASURE";
    public static String REGION_KEY = "REGION";
    public static String TIME_KEY = "TIME";
    public static String CODE_KEY = "CODE";
    public static String DESCRIPTION_KEY = "DESCRIPTION";
    public static String ALLCODES_EXP = "";
    public static String OR_EXP = "+";
    public static String SEPARATOR_EXP = ".";
    public static String SEPARATOR_DIM = "=";
    public static String GEOMETRY_ATTR = "the_geom";
    public static String SEPARATOR_FEATURETYPE = "__";
    public static String DIMENSIONS_SUFFIX = "DIMENSIONS";
    public static String FEATURETYPE_SUFFIX = "SDMX";
    public static String DIMENSIONS_EXPR = "CODE";
    public static String DIMENSIONS_EXPR_ALL = "ALL";

    // SDMX error codes
    public static int ERROR_NORESULTS = 100;

    // Cache of feature sources
    protected Map<Name, ContentFeatureSource> featureSources =
            new HashMap<Name, ContentFeatureSource>();

    protected URL namespace;
    protected URL apiUrl;
    protected String user;
    protected String password;
    protected GenericSDMXClient sdmxClient;
    protected Map<String, Dataflow> dataflows = new HashMap<String, Dataflow>();
    protected Map<String, DataFlowStructure> dataflowStructures =
            new HashMap<String, DataFlowStructure>();

    public SDMXDataStore(
            String name,
            String namespaceIn,
            String provider,
            String apiEndpoint,
            String user,
            String password)
            throws MalformedURLException, IOException, SdmxException {

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

        // FIXME: it seems ot work only with a pre-defined client
        try {
            this.sdmxClient = SDMXClientFactory.createClient(provider);
        } catch (SdmxException e) {
            LOGGER.log(Level.SEVERE, "Cannot create client", e);
            throw (e);
        }

        // TODO: add credentials support
        // this.sdmxClient = new RestSdmxClient(name, new URL(apiEndpoint), false,
        // false, false);
    }

    /**
     * Return the complete type name from the dataflow one
     *
     * @param dfName Name of the dataflow
     * @param dimName Name of the dimension
     * @return
     */
    public static String composeDataflowTypeName(String dfName) {
        return dfName + SDMXDataStore.SEPARATOR_FEATURETYPE + FEATURETYPE_SUFFIX;
    }

    /**
     * Return the complete type name from the dimension one
     *
     * @param dfName Name of the dataflow
     * @return
     */
    public static String composeDimensionTypeName(String dfName) {
        return dfName
                + SDMXDataStore.SEPARATOR_FEATURETYPE
                + SDMXDataStore.FEATURETYPE_SUFFIX
                + SDMXDataStore.SEPARATOR_FEATURETYPE
                + SDMXDataStore.DIMENSIONS_SUFFIX;
    }

    /**
     * Checks whether typeName represents an SDMX dataflow
     *
     * @param typeName Name of the typename
     * @return
     */
    public static boolean isDataflowName(String typeName) {
        return typeName.matches(
                "(.+)"
                        + SDMXDataStore.SEPARATOR_FEATURETYPE
                        + SDMXDataStore.FEATURETYPE_SUFFIX
                        + "$");
    }

    /**
     * Checks whether typeName represents an SDMX dimension
     *
     * @param typeName Name of the typename
     * @return
     */
    public static boolean isDimensionName(String typeName) {
        return typeName.matches(
                "(.+)"
                        + SDMXDataStore.SEPARATOR_FEATURETYPE
                        + SDMXDataStore.FEATURETYPE_SUFFIX
                        + SDMXDataStore.SEPARATOR_FEATURETYPE
                        + SDMXDataStore.DIMENSIONS_SUFFIX
                        + "$");
    }

    /**
     * Returns the data flow name from a type name (returns an empty string if this is not an SDMX
     * feature type)
     *
     * @param typeName Name of the typename
     * @return
     */
    public static String extractDataflowName(String typeName) {

        if (!SDMXDataStore.isDataflowName(typeName) && !SDMXDataStore.isDimensionName(typeName)) {
            return "";
        }

        String[] parts = typeName.split(SDMXDataStore.SEPARATOR_FEATURETYPE);

        return parts.length == 0 ? "" : parts[0];
    }

    @Override
    protected List<Name> createTypeNames() {

        // This is to avoid pining the SDMX server every time type names habe to be
        // created
        if (this.entries.isEmpty() == false) {
            return new ArrayList<Name>(this.entries.keySet());
        }

        try {
            dataflows = this.sdmxClient.getDataflows();
        } catch (SdmxException e) {
            e.printStackTrace(); // TODO
            return new ArrayList<Name>();
        }

        this.dataflowStructures.clear();
        dataflows.forEach(
                (s, d) -> {
                    DataFlowStructure dfs = new DataFlowStructure();
                    try {
                        dfs = this.sdmxClient.getDataFlowStructure(d.getDsdIdentifier(), true);
                        this.dataflowStructures.put(s, dfs);
                    } catch (SdmxException e) {
                        LOGGER.log(Level.SEVERE, "Error getting SDMX DSD", e);
                    }

                    // Adds the dataflow typename
                    Name name =
                            new NameImpl(
                                    namespace.toExternalForm(),
                                    SDMXDataStore.composeDataflowTypeName(s));
                    ContentEntry entry = new ContentEntry(this, name);
                    this.entries.put(name, entry);

                    // Adds the dimension typename
                    String dimName = SDMXDataStore.composeDimensionTypeName(s);
                    ContentEntry dimEntry =
                            new ContentEntry(
                                    this, new NameImpl(namespace.toExternalForm(), dimName));
                    this.entries.put(new NameImpl(namespace.toExternalForm(), dimName), dimEntry);
                });

        return new ArrayList<Name>(this.entries.keySet());
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {

        ContentFeatureSource featureSource = this.featureSources.get(entry.getName());
        if (featureSource == null) {
            try {
                Dataflow df =
                        this.dataflows.get(
                                SDMXDataStore.extractDataflowName(entry.getName().getLocalPart()));

                // Returns different feature sources depending on the the entry
                // referring to a datflow or a dimension
                if (SDMXDataStore.isDataflowName(entry.getName().getLocalPart())) {
                    featureSource = new SDMXDataflowFeatureSource(entry, df, new Query());
                } else {
                    featureSource = new SDMXDimensionFeatureSource(entry, df, new Query());
                }

            } catch (FactoryException e) {
                LOGGER.log(Level.SEVERE, "Cannot create CRS", e);
                throw (new IOException(e));
            }
            this.featureSources.put(entry.getName(), featureSource);
        }

        return featureSource;
    }

    public URL getNamespace() {
        return namespace;
    }

    public GenericSDMXClient getSDMXClient() {
        return this.sdmxClient;
    }

    public DataFlowStructure getDataFlowStructure(String name) {
        return this.dataflowStructures.get(SDMXDataStore.extractDataflowName(name));
    }

    // TODO: ?
    @Override
    public void dispose() {
        super.dispose();
    }
}
