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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.data.complex.TotalIndexedMappingFeatureIterator;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.util.FeatureStreams;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Test;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory2;

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
        try (FeatureIterator<Feature> iterator = fcoll.features()) {
            assertTrue(iterator instanceof TotalIndexedMappingFeatureIterator);
        }
        List<Feature> features = FeatureStreams.toFeatureStream(fcoll).collect(Collectors.toList());
        assertEquals(features.size(), 1);
        assertEquals(features.get(0).getIdentifier().getID(), "13");
    }

    @Override
    protected void configFieldsSetup() {
        this.mappedTypeName = Types.typeName("StationType-f46d72da-5591-4873-b210-5ed30a6ffb0d");
        this.xmlFileName = "mappings_solr.xml";
        this.xsdFileName = "meteo.xsd";
        this.testData = "/test-data/appschema-indexes/stations_complex/";
    }

    /** Should returns 1, 2, 10, 12(11 on index) */
    private Filter totalIndexedFilterCase() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter = ff.like(ff.property(attObservationDesc), "*sky*");
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

    /** appschema_index.properties file required */
    @Override
    protected String getFixtureId() {
        return "appschema_index";
    }
}
