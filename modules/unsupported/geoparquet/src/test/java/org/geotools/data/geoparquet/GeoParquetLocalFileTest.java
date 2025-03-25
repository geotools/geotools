/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.ClassRule;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/** Tests for GeoParquet DataStore with local files. */
public class GeoParquetLocalFileTest extends GeoParquetTestBase {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @ClassRule
    public static final GeoParquetTestSupport support = new GeoParquetTestSupport();

    @Test
    public void testSingleFileAccess() throws IOException {
        // Create the datastore with the local file
        File pointsFile = support.getWorldgridFile("points.parquet");
        dataStore = createLocalDataStore(pointsFile.toURI().toASCIIString());

        // Verify we can get the feature type name
        String[] typeNames = dataStore.getTypeNames();

        assertEquals("Should have one type name", 1, typeNames.length);
        assertEquals("Type name should match file name without extension", "points", typeNames[0]);

        // Get a feature source and verify it has features
        SimpleFeatureSource source = dataStore.getFeatureSource(typeNames[0]);
        verifyFeatureSource(source, 65345);

        // Verify schema contains geometry
        assertNotNull(
                "Schema should have a geometry attribute", source.getSchema().getGeometryDescriptor());
        assertEquals("geometry", source.getSchema().getGeometryDescriptor().getLocalName());
    }

    @Test
    public void testDirectoryAccess() throws IOException {
        // Create the datastore with the directory
        File directory = support.getWorldgridDir();

        Set<String> expected = Arrays.stream(directory.list())
                .filter(f -> f.endsWith(".parquet"))
                .map(s -> s.substring(0, s.indexOf('.')))
                .collect(Collectors.toSet());

        assertFalse(expected.isEmpty());

        dataStore = createLocalDataStore(directory.toURI().toASCIIString());

        // Verify we can get the feature type names for all files
        String[] typeNames = dataStore.getTypeNames();
        assertEquals(expected.size(), typeNames.length);

        assertEquals(expected, Set.copyOf(Arrays.asList(typeNames)));

        // Verify each feature source has features
        for (String typeName : typeNames) {
            SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
            verifyFeatureSource(source, -1);
        }
    }

    @Test
    public void testSpatialFilter() throws IOException {
        // Create the datastore with the local file
        dataStore = createLocalDataStore(support.getWorldgridDir().toURI().toASCIIString());

        // Get the feature source
        SimpleFeatureSource source = dataStore.getFeatureSource("points");

        // Get total count
        int totalCount = source.getFeatures().size();

        Geometry geom = JTS.toGeometry(new Envelope(-10, 0, 0, 10));

        Filter filter = FF.intersects(
                FF.property(source.getSchema().getGeometryDescriptor().getLocalName()), FF.literal(geom));

        // Get filtered features
        SimpleFeatureCollection filtered = source.getFeatures(filter);
        int filteredCount = filtered.size();

        // Should have fewer features than the total
        assertTrue("Filtered count should be less than total", filteredCount < totalCount);
        assertTrue("Should have some filtered features", filteredCount > 0);
    }

    @Test
    public void testAttributeFilter() throws IOException {

        dataStore = createLocalDataStore(support.getWorldgridDir().toURI().toASCIIString());

        SimpleFeatureSource source = dataStore.getFeatureSource("polygons");

        final int totalCount = source.getFeatures().size();
        assertEquals(2489, totalCount);

        Filter filter = FF.equals(FF.property("type"), FF.literal("multipolygon"));

        // Get filtered features
        SimpleFeatureCollection filtered = source.getFeatures(filter);
        int filteredCount = filtered.size();

        assertEquals(4, filteredCount);
    }

    @Test
    public void testBoundingBoxQuery() throws IOException {
        // Create the datastore with the local file
        dataStore = createLocalDataStore(support.getWorldgridDir().toURI().toASCIIString());

        // Get the feature source
        SimpleFeatureSource source = dataStore.getFeatureSource("points");

        // Get the CRS from the feature type
        CoordinateReferenceSystem crs = source.getSchema().getCoordinateReferenceSystem();
        assertEquals("EPSG:4326", CRS.toSRS(crs));

        ReferencedEnvelope bounds = source.getBounds();
        assertNotNull(bounds);
        assertFalse(bounds.isEmpty());

        // Create a query with the bounding box
        Query query = new Query(source.getSchema().getTypeName());
        query.setFilter(FF.bbox(FF.property("geometry"), -0.5, -0.5, 0.5, 0.5, null));

        // Get filtered features
        SimpleFeatureCollection filtered = source.getFeatures(query);
        int filteredCount = filtered.size();

        // Should have some features in the bounding box
        assertTrue("Should have features in the bounding box", filteredCount > 0);
    }
}
