/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Repository;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentState;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;

/** {@link DataStore} for a vector mosaic. */
public class VectorMosaicStore extends ContentDataStore {
    static final Logger LOGGER = Logging.getLogger(VectorMosaicStore.class);

    private final Repository repository;

    private final String delegateStoreName;

    private Properties commonParameters;

    private String preferredSPI;

    public static final String CONNECTION_PARAMETER_KEY_URL = "url";

    public static final String CONNECTION_PARAMETER_KEY_FILE = "file";

    private String connectionParameterKey = CONNECTION_PARAMETER_KEY_URL;
    public static final String MOSAIC_TYPE_SUFFIX = "_mosaic";

    private static final String POLYGON_TYPE_NAME = "org.locationtech.jts.geom.Polygon";
    private static final String MULTI_POLYGON_TYPE_NAME = "org.locationtech.jts.geom.MultiPolygon";

    public String getDelegateStoreName() {
        return delegateStoreName;
    }

    public Repository getRepository() {
        return repository;
    }

    public String getConnectionParameterKey() {
        return connectionParameterKey;
    }

    public void setConnectionParameterKey(String connectionParameterKey) {
        this.connectionParameterKey = connectionParameterKey;
    }

    public String getPreferredSPI() {
        return preferredSPI;
    }

    public void setPreferredSPI(String preferredSPI) {
        this.preferredSPI = preferredSPI;
    }

    /**
     * Creates a new instance of VectorMosaicStore
     *
     * @param delegateStoreName the name of the delegate store. Delegate store must be registered in the repository and
     *     must point to vector featuretypes with identical attributes.
     * @param repository the repository to use for lookup of the delegate store.
     */
    public VectorMosaicStore(String delegateStoreName, Repository repository) {
        this.repository = repository;
        this.delegateStoreName = delegateStoreName;
    }

    /**
     * Validates the schema of the delegate store.
     *
     * @param delegateStoreName the name of the delegate store.
     * @param typeName the name of the type to validate.
     * @param delegateSchema the schema of the type to validate.
     * @return true if the schema is valid.
     */
    private boolean validateSchema(String delegateStoreName, String typeName, SimpleFeatureType delegateSchema) {
        if (delegateSchema.getDescriptor(VectorMosaicGranule.CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT) == null) {
            LOGGER.log(
                    Level.INFO,
                    "Delegate store "
                            + delegateStoreName
                            + " schema with type name "
                            + typeName
                            + " does not contain required field "
                            + VectorMosaicGranule.CONNECTION_PARAMETERS_DELEGATE_FIELD_DEFAULT);
            return false;
        }
        if (delegateSchema.getGeometryDescriptor() == null) {
            LOGGER.log(
                    Level.INFO,
                    "Delegate store "
                            + delegateStoreName
                            + " schema with type name "
                            + typeName
                            + " does not contain a geometry field");
            return false;
        }
        GeometryType geometryType = delegateSchema.getGeometryDescriptor().getType();
        if (geometryType != null
                && geometryType.getBinding() != null
                && !geometryType.getBinding().isAssignableFrom(GeometryType.class)) {
            String geometryTypeName = geometryType.getBinding().getName();
            if (!geometryTypeName.equals(POLYGON_TYPE_NAME) && !geometryTypeName.equals(MULTI_POLYGON_TYPE_NAME)) {
                LOGGER.log(
                        Level.INFO,
                        "Delegate store "
                                + delegateStoreName
                                + " schema with type name "
                                + typeName
                                + " contains geometry but is not a polygon or multipolygon");
                return false;
            }
        } else {
            LOGGER.log(
                    Level.INFO,
                    "Delegate store "
                            + delegateStoreName
                            + " schema with type name "
                            + typeName
                            + " does not contain a valid geometry field");
            return false;
        }

        return true;
    }

    public Properties getCommonParameters() {
        return commonParameters;
    }

    public void setCommonParameters(Properties commonParameters) {
        this.commonParameters = commonParameters;
    }

    /**
     * Builds a qualified name from a name containing the ":" separator, otherwise the given name will be used as the
     * local part
     *
     * @param name the name to build a qualified name from
     * @return the qualified name
     */
    public static Name buildName(String name) {
        int idx = name.indexOf(":");
        if (idx == -1) {
            return new NameImpl(name);
        } else {
            String ns = name.substring(0, idx);
            String local = name.substring(idx + 1);
            return new NameImpl(ns, local);
        }
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {

        Name dsname = buildName(delegateStoreName);
        DataStore delegateDataStore = repository.dataStore(dsname);
        if (delegateDataStore == null) {
            throw new IOException("Could not find delegate store " + delegateStoreName);
        }
        List<String> delegateTypes = null;
        try {
            delegateTypes = Arrays.stream(delegateDataStore.getTypeNames())
                    .filter(t -> {
                        try {
                            return validateSchema(delegateStoreName, t, delegateDataStore.getSchema(t));
                        } catch (IOException e) {
                            LOGGER.log(Level.WARNING, "Failed to validate schema for type " + t, e);
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            LOGGER.log(
                    Level.WARNING,
                    "Failed to get type names for vector mosaic delegate data store " + delegateStoreName,
                    e);
        }
        List<Name> result = new ArrayList<>();
        if (delegateTypes != null) {
            for (String delegateName : delegateTypes) {
                result.add(buildName(delegateName + MOSAIC_TYPE_SUFFIX));
            }
        }
        return result;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        String name = entry.getName().getLocalPart();
        if (!Arrays.stream(getTypeNames()).anyMatch(name::equals)) {
            throw new IOException("No Vector Mosaic configuration found for type " + name);
        }
        return new VectorMosaicFeatureSource(entry, this);
    }

    @Override
    protected ContentState createContentState(ContentEntry entry) {
        return new VectorMosaicState(entry);
    }
}
