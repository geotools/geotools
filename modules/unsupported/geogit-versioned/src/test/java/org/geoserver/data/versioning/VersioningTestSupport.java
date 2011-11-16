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
package org.geoserver.data.versioning;

import org.geogit.test.RepositoryTestCase;
import org.geoserver.data.versioning.decorator.DataStoreDecorator;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureStore;

/**
 * Base class for versioning tests.
 * <p>
 * {@link #setUpInternal()} leaves {@link #unversionedStore} with two features,
 * {@link RepositoryTestCase#lines1} and {@link RepositoryTestCase#lines2}
 * 
 * @author groldan
 */
public abstract class VersioningTestSupport extends RepositoryTestCase {

    protected DataStore unversionedStore;

    protected DataStoreDecorator versioningStore;

    protected SimpleFeatureStore lines, points;

    @SuppressWarnings({ "rawtypes" })
    @Override
    protected void setUpInternal() throws Exception {
        unversionedStore = new SimpleMemoryDataAccess();
        versioningStore = new DataStoreDecorator(unversionedStore, super.repo);

        versioningStore.createSchema(linesType);
        versioningStore.createSchema(pointsType);

        lines = (SimpleFeatureStore) versioningStore
                .getFeatureSource(linesTypeName);
        points = (SimpleFeatureStore) versioningStore
                .getFeatureSource(pointsTypeName);

    }
}
