/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

import com.sun.org.apache.xml.internal.utils.UnImplNode;

/**
 * Sample implementation of {@link DataAccess} for testing. Create with
 * {@link SampleDataAccessFactory}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/sample-data-access
 *         /src/main/java/org/geotools/data/SampleDataAccess.java $
 * @since 2.6
 */
@SuppressWarnings("serial")
public class SampleDataAccess implements DataAccess<FeatureType, Feature> {

    /**
     * Unsupported operation.
     * 
     * @see org.geotools.data.DataAccess#createSchema(org.opengis.feature.type.FeatureType)
     */
    public void createSchema(FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();

    }

    /**
     * Nothing to dispose.
     * 
     * @see org.geotools.data.DataAccess#dispose()
     */
    public void dispose() {
        // do nothing
    }

    /**
     * @see org.geotools.data.DataAccess#getFeatureSource(org.opengis.feature.type.Name)
     */
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
     * @see org.geotools.data.DataAccess#getInfo()
     */
    public ServiceInfo getInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the feature type names provided by this {@link DataAccess}. Only
     * {@link SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME} is supported.
     * 
     * @see org.geotools.data.DataAccess#getNames()
     */
    public List<Name> getNames() throws IOException {
        return new ArrayList<Name>() {
            {
                add(SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME);
                add(SampleDataAccessData.GEOLOGICUNIT_TYPE_NAME);
            }
        };
    }

    /**
     * Return the feature type for supported type name. Only
     * {@link SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME} is supported.
     * 
     * @see org.geotools.data.DataAccess#getSchema(org.opengis.feature.type.Name)
     */
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
     * @see org.geotools.data.DataAccess#updateSchema(org.opengis.feature.type.Name,
     *      org.opengis.feature.type.FeatureType)
     */
    public void updateSchema(Name typeName, FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

}
