/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.data.Query;
import org.geotools.data.complex.PartialIndexedMappingFeatureIterator;
import org.geotools.data.complex.TotalIndexedMappingFeatureIterator;
import org.geotools.data.complex.config.Types;
import org.geotools.data.util.FeatureStreams;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * AppSchema Index online test, using Solr as index and Postgresql as dataSource
 *
 * @author Fernando Miño, Geosolutions
 */
public class AppSchemaIndexIntegrationTest extends AppSchemaOnlineTestSupport {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    protected final String attId = "st:Station";
    protected final String attName = "st:Station/st:stationName";
    protected final String attComments = "st:Station/st:comments";
    protected final String attObservationDesc =
            "st:Station/st:observation/st:Observation/st:description";

    @Test
    public void testIndex() throws IOException {
        // partialindexCase();
        totalindexCase();
        // partialindexPaginationCase();
    }

    private void totalindexCase() throws IOException {
        FeatureCollection<FeatureType, Feature> fcoll =
                this.mappingDataStore
                        .getFeatureSource(this.mappedTypeName)
                        .getFeatures(totalIndexedFilterCase());
        FeatureIterator<Feature> iterator = fcoll.features();
        assertTrue(iterator instanceof TotalIndexedMappingFeatureIterator);
        List<Feature> features = FeatureStreams.toFeatureStream(fcoll).collect(Collectors.toList());
        assertEquals(features.size(), 1);
        assertEquals(features.get(0).getIdentifier().getID(), "13");
    }

    private void partialindexCase() throws IOException {
        FeatureCollection<FeatureType, Feature> fcoll =
                this.mappingDataStore
                        .getFeatureSource(this.mappedTypeName)
                        .getFeatures(partialIndexedFilter());
        FeatureIterator<Feature> iterator = fcoll.features();
        assertTrue(iterator instanceof PartialIndexedMappingFeatureIterator);
        List<Feature> features = FeatureStreams.toFeatureStream(fcoll).collect(Collectors.toList());
        assertEquals(features.size(), 6);
        assertEquals(features.get(0).getIdentifier().getID(), "1");
        assertEquals(features.get(1).getIdentifier().getID(), "2");
        assertEquals(features.get(2).getIdentifier().getID(), "5");
        assertEquals(features.get(3).getIdentifier().getID(), "6");
        assertEquals(features.get(4).getIdentifier().getID(), "10");
        assertEquals(features.get(5).getIdentifier().getID(), "11");
    }

    private void partialindexPaginationCase() throws IOException {
        // build query with limits and sort
        Query q1 = new Query(this.mappedTypeName.getLocalPart(), partialIndexedFilter());
        q1.setStartIndex(2);
        q1.setMaxFeatures(3);
        // AppSchema seems have a bug with sort?
        // q1.setSortBy(new SortBy[] {ff.sort(this.attId, SortOrder.ASCENDING)});
        // retrieve features:
        FeatureCollection<FeatureType, Feature> fcoll =
                this.mappingDataStore.getFeatureSource(this.mappedTypeName).getFeatures(q1);
        List<Feature> features = FeatureStreams.toFeatureStream(fcoll).collect(Collectors.toList());
        assertEquals(3, features.size());
        assertEquals(features.get(0).getIdentifier().getID(), "5");
        assertEquals(features.get(1).getIdentifier().getID(), "6");
        assertEquals(features.get(2).getIdentifier().getID(), "10");
    }

    @Override
    protected void configFieldsSetup() {
        this.mappedTypeName = Types.typeName("StationType-f46d72da-5591-4873-b210-5ed30a6ffb0d");
        this.xmlFileName = "mappings_solr.xml";
        this.xsdFileName = "meteo.xsd";
        this.testData = "/test-data/appschema-indexes/stations_complex/";
    }

    /** Should returns 1, 2, 5, 6, 10, 12(11 on index) */
    private Filter partialIndexedFilter() {
        List<Filter> filters =
                Arrays.asList(
                        ff.like(ff.property(attObservationDesc), "*sky*") // ,
                        //                        ff.or(
                        //                                ff.equals(ff.property(this.attName),
                        // ff.literal("station11")),
                        //                                ff.equals(ff.property(this.attId),
                        // ff.literal("1"))),
                        //                        ff.or(
                        //                                ff.equals(ff.property(this.attName),
                        // ff.literal("station10")),
                        //                                ff.equals(ff.property(this.attId),
                        // ff.literal("2")))
                        );
        Filter filter = ff.or(filters);
        return filter;
    }

    /** Should returns 1, 2, 10, 12(11 on index) */
    private Filter totalIndexedFilterCase() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        List<Filter> filters =
                Arrays.asList(
                        ff.like(ff.property(attObservationDesc), "*sky*")
                        //                        ff.or(
                        //                                ff.equals(ff.property(this.attName),
                        // ff.literal("station11")),
                        //                                ff.equals(ff.property(this.attId),
                        // ff.literal("1"))),
                        //                        ff.or(
                        //                                ff.equals(ff.property(this.attName),
                        // ff.literal("station10")),
                        //                                ff.equals(ff.property(this.attId),
                        // ff.literal("2")))
                        );
        Filter filter = ff.like(ff.property(attObservationDesc), "*sky*"); // ff.or(filters);
        return filter;
    }

    @Override
    protected void prepareFiles() throws Exception {
        copyTestData(this.xsdFileName, tempDir);
        copyTestData(this.xmlFileName, tempDir);
        copyTestData("includedTypes.xml", tempDir);
    }

    @Override
    protected void solrDataSetup() {}

    private void loadPostgresSetup() {
        String url =
                "jdbc:postgresql://localhost/test"
                        + fixture.getProperty(AppSchemaOnlineTestSupport.PG_HOST_KEY)
                        + "/"
                        + fixture.getProperty(AppSchemaOnlineTestSupport.PG_PORT_KEY);
    }

    /** appschema_index.properties file required */
    @Override
    protected String getFixtureId() {
        return "appschema_index";
    }
}
