/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.pmtiles.store;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import io.tileverse.pmtiles.InvalidHeaderException;
import io.tileverse.pmtiles.PMTilesReader;
import io.tileverse.pmtiles.PMTilesTestData;
import io.tileverse.tiling.common.BoundingBox2D;
import io.tileverse.tiling.matrix.TileMatrix;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.FactoryException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

public class PMTilesDataStoreTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    private Path andorraPMTiles;

    private PMTilesReader reader;
    private PMTilesDataStore store;

    private final String namespaceURI = "https://github.com/protomaps/PMTiles/blob/main/spec/v3/spec.md";

    @Before
    public void setUp() throws IOException, InvalidHeaderException {
        this.andorraPMTiles = PMTilesTestData.andorra(tmpFolder.getRoot().toPath());
        reader = new PMTilesReader(andorraPMTiles);
        store = new PMTilesDataStore(new PMTilesDataStoreFactory(), reader);
        store.setNamespaceURI(namespaceURI);
        store.setFeatureTypeFactory(CommonFactoryFinder.getFeatureTypeFactory(null));
        store.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
    }

    @Test
    public void getTypeNames() throws IOException, InvalidHeaderException {
        List<String> typeNames = Arrays.asList(store.getTypeNames());
        List<String> expected = PMTilesTestData.andorraLayerNames();
        assertEquals(expected, typeNames);
    }

    @Test
    public void getNames() throws IOException, InvalidHeaderException {
        List<Name> typeNames = store.getNames();
        // ContentDataStore.getName(): Returns the same list of names than {@link #getTypeNames()} meaning the returned
        // Names have no namespace set.
        List<Name> expected = PMTilesTestData.andorraLayerNames().stream()
                .map(NameImpl::new)
                .map(Name.class::cast)
                .toList();
        assertEquals(expected, typeNames);
    }

    @Test
    public void getSchema() throws IOException, FactoryException {
        SimpleFeatureType schema = store.getSchema("street_polygons");
        assertNotNull(schema);

        GeometryDescriptor geom = schema.getGeometryDescriptor();
        assertNotNull(geom);
        assertEquals("the_geom", geom.getLocalName());
        assertEquals(Geometry.class, geom.getType().getBinding());
        assertEquals(CRS.decode("EPSG:3857", true), geom.getCoordinateReferenceSystem());

        Map<String, Class<?>> expected = Map.of(
                "bridge",
                Boolean.class,
                "kind",
                String.class,
                "rail",
                Boolean.class,
                "service",
                String.class,
                "surface",
                String.class,
                "tunnel",
                Boolean.class);
        Map<String, Class<?>> actual = schema.getAttributeDescriptors().stream()
                .filter(at -> !at.getLocalName().equals(geom.getLocalName()))
                .collect(Collectors.toMap(
                        AttributeDescriptor::getLocalName, at -> at.getType().getBinding()));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void getFeatureSource() throws IOException, FactoryException {
        SimpleFeatureSource buildings = store.getFeatureSource("buildings");
        assertNotNull(buildings);
        assertThat(buildings, not(instanceOf(FeatureStore.class)));
        assertThrows(IOException.class, () -> store.getFeatureSource("nonexistent"));
    }

    @Test
    public void getFeatureSourceGetFeatures() throws Exception {

        TileMatrix tileMatrix = store.getTileStore().matrixSet().getTileMatrix(12);
        BoundingBox2D tileExtent = tileMatrix.first().extent();
        Polygon geometry = JTS.toGeometry(
                new Envelope(tileExtent.minX(), tileExtent.maxX(), tileExtent.minY(), tileExtent.maxY()));

        SimpleFeatureSource buildings = store.getFeatureSource("buildings");
        assertNotNull(buildings);
        Filter filter = store.getFilterFactory().intersects("", geometry);
        SimpleFeatureCollection features = buildings.getFeatures(filter);
        assertThat(features.size(), greaterThan(0));
        try (SimpleFeatureIterator it = features.features()) {
            while (it.hasNext()) {
                SimpleFeature next = it.next();
                assertNotNull(next);
                Object defaultGeometry = next.getDefaultGeometry();
                assertThat(defaultGeometry, instanceOf(Geometry.class));
            }
        }
    }
}
