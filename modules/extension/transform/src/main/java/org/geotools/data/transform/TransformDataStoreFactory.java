/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Repository;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;

/**
 * DataStoreFactory which wraps a DataStore's FeatureStore with a Transform consisting of a
 * collection of projections and new additional columns built from Expressions.
 */
public class TransformDataStoreFactory implements DataStoreFactorySpi {
    public static final Param REPOSITORY =
            new Param(
                    "Repository",
                    Repository.class,
                    "Class name for data store repository implementation",
                    true);

    public static final Param DATASTORE_NAME =
            new Param("DataStoreName", String.class, "DataStore Name", true);

    public static final Param FEATURE_TYPE_NAME =
            new Param("FeatureTypeName", String.class, "FeatureTypeName / Layer Name", true);

    public static final Param NEW_FEATURE_TYPE_NAME =
            new Param("NewFeatureTypeName", String.class, "New FeatureTypeName", true);

    public static final Param TRANSFORM_DEFINITION =
            new Param(
                    "TransformDefinition",
                    String.class,
                    "Transform definition for columns to add",
                    false);

    public static final Param ATTRIBUTE_SUBSET =
            new Param(
                    "AttributeSubset",
                    String.class,
                    "Comma-separated subset of columns to retain.  Leave empty to keep all columns.",
                    false);

    public static final Param NAMESPACEP =
            new Param("namespace", URI.class, "uri to a the namespace", false); // not required

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {

        URI namespace = (URI) NAMESPACEP.lookUp(params);
        Repository repository = (Repository) REPOSITORY.lookUp(params);

        String datastoreName = (String) DATASTORE_NAME.lookUp(params);
        String featureTypeName = (String) FEATURE_TYPE_NAME.lookUp(params);
        String newFeatureTypeName = (String) NEW_FEATURE_TYPE_NAME.lookUp(params);

        DataStore ds = repository.dataStore(new NameImpl(datastoreName));
        SimpleFeatureSource inputFS = ds.getFeatureSource(featureTypeName);

        String attributeSubset = (String) ATTRIBUTE_SUBSET.lookUp(params);

        List<Definition> definitions = null;

        if (attributeSubset != null) {

            definitions =
                    Arrays.stream(attributeSubset.split(","))
                            .map(Definition::new)
                            .collect(Collectors.toCollection(ArrayList::new));
        } else {
            definitions =
                    inputFS.getSchema()
                            .getAttributeDescriptors()
                            .stream()
                            .map(ad -> new Definition(ad.getLocalName()))
                            .collect(Collectors.toList());
        }

        String transformDefinition = (String) TRANSFORM_DEFINITION.lookUp(params);

        if (transformDefinition != null) {
            List<Definition> transforms =
                    Arrays.stream(transformDefinition.split(";"))
                            .map(
                                    string -> {
                                        String[] parts = string.split("=");
                                        String newColumn = parts[0];
                                        String expression = parts[1];
                                        try {
                                            return new Definition(
                                                    newColumn, ECQL.toExpression(expression));
                                        } catch (CQLException e) {
                                            e.printStackTrace();
                                            return null;
                                        }
                                    })
                            .collect(Collectors.toCollection(ArrayList::new));
            definitions.addAll(transforms);
        }

        SimpleFeatureSource transformedFS =
                TransformFactory.transform(inputFS, newFeatureTypeName, definitions);

        return new SingleFeatureSourceDataStore(transformedFS);
    }

    @Override
    public String getDisplayName() {
        return "Transform DataStore";
    }

    @Override
    public String getDescription() {
        return "Transform DataStore uses CQL to add columns.";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {
            REPOSITORY,
            DATASTORE_NAME,
            FEATURE_TYPE_NAME,
            NEW_FEATURE_TYPE_NAME,
            TRANSFORM_DEFINITION,
            ATTRIBUTE_SUBSET,
            NAMESPACEP
        };
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        Repository repositoryClass = null;
        try {
            repositoryClass = (Repository) REPOSITORY.lookUp(params);
        } catch (IOException ex) {
            return false;
        }

        if (repositoryClass == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException();
    }
}
