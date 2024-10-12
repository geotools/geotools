/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.resolver.data;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;

/**
 * Sample implementation of {@link DataAccess} for testing. Create with {@link
 * SampleDataAccessFactory}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @since 2.6
 */
@SuppressWarnings("serial")
public class SampleDataAccess implements DataAccess<FeatureType, Feature> {

    /**
     * Unsupported operation.
     *
     * @see DataAccess#createSchema(org.geotools.api.feature.type.FeatureType)
     */
    @Override
    public void createSchema(FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Nothing to dispose.
     *
     * @see DataAccess#dispose()
     */
    @Override
    public void dispose() {
        // do nothing
    }

    /** @see DataAccess#getFeatureSource(org.geotools.api.feature.type.Name) */
    @Override
    public FeatureSource<FeatureType, Feature> getFeatureSource(Name typeName) throws IOException {
        if (typeName.equals(SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME)) {
            return new SampleDataAccessFeatureSource();
        } else if (typeName.equals(SampleDataAccessData.GEOLOGICUNIT_TYPE_NAME)) {
            throw new IllegalArgumentException("Although this DataAccess claims to provide "
                    + SampleDataAccessData.GEOLOGICUNIT_TYPE_NAME
                    + ", it does so only so that schema references"
                    + " are resolved when this type is nested inside "
                    + SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME
                    + ". Direct access to the former feature type is not supported.");
        } else {
            throw new RuntimeException("Unrecognised feature type " + typeName.toString());
        }
    }

    /**
     * Unsupported operation.
     *
     * @see DataAccess#getInfo()
     */
    @Override
    public ServiceInfo getInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the feature type names provided by this {@link DataAccess}. Only {@link
     * SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME} is supported.
     *
     * @see DataAccess#getNames()
     */
    @Override
    public List<Name> getNames() throws IOException {
        return List.of(SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME, SampleDataAccessData.GEOLOGICUNIT_TYPE_NAME);
    }

    /**
     * Return the feature type for supported type name. Only {@link
     * SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME} is supported.
     *
     * @see DataAccess#getSchema(org.geotools.api.feature.type.Name)
     */
    @Override
    public FeatureType getSchema(Name name) throws IOException {
        if (name.equals(SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME)) {
            return SampleDataAccessData.MAPPEDFEATURE_TYPE;
        } else if (name.equals(SampleDataAccessData.GEOLOGICUNIT_TYPE_NAME)) {
            return SampleDataAccessData.GEOLOGICUNIT_TYPE;
        } else {
            throw new RuntimeException("Unrecognised feature type " + name.toString());
        }
    }

    /**
     * Unsupported operation.
     *
     * @see DataAccess#updateSchema(org.geotools.api.feature.type.Name,
     *     org.geotools.api.feature.type.FeatureType)
     */
    @Override
    public void updateSchema(Name typeName, FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported operation.
     *
     * @see DataAccess#removeSchema(org.geotools.api.feature.type.Name)
     */
    @Override
    public void removeSchema(Name typeName) throws IOException {
        throw new UnsupportedOperationException("Schema removal not supported");
    }
}
