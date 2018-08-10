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
package org.geotools.data.solr.complex;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.config.Types;
import org.geotools.data.solr.TestsSolrUtils;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.logging.Logging;
import org.geotools.xml.resolver.SchemaCache;
import org.junit.AfterClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * This class contains the integration tests (online tests) for the integration between App-Schema
 * and Apache Solr. To activate this tests a fixture file needs to be created in the user home, this
 * follows the usual geoTools conventions for fixture files. Read the README.rst file for more
 * instructions.
 */
public final class ComplexFeaturesTest extends OnlineTestCase {

    private static final Logger LOGGER = Logging.getLogger(ComplexFeaturesTest.class);

    private static final Name MAPPED_TYPE_NAME = Types.typeName("stations_solr");
    private static final String STATIONS_NAMESPACE = "http://www.stations.org/1.0";
    private static final Name STATION_NAME = new NameImpl(STATIONS_NAMESPACE, "stationName");
    private static final Name STATION_TAG = new NameImpl(STATIONS_NAMESPACE, "tag");

    private static final File TESTS_ROOT_DIR = TestsSolrUtils.createTempDirectory("complex-solr");

    private DataAccess<FeatureType, Feature> appSchemaDataStore;

    static {
        if (System.getProperty(SchemaCache.PROVIDED_CACHE_LOCATION_KEY) == null) {
            // create and set App-Schema cache directory
            System.setProperty(
                    SchemaCache.PROVIDED_CACHE_LOCATION_KEY,
                    new File(TESTS_ROOT_DIR, "app-schema-cache").getAbsolutePath());
        }
    }

    @Override
    public void setUpInternal() {
        // instantiate the Apache Solr client
        HttpSolrClient client = new HttpSolrClient.Builder(getSolrCoreURL()).build();
        // configure the target Apache Solr core
        StationsSetup.setupSolrIndex(client);
        // prepare the App-Schema configuration files
        StationsSetup.prepareAppSchemaFiles(TESTS_ROOT_DIR, getSolrCoreURL());
        instantiateAppSchemaDataStore();
    }

    @AfterClass
    public static void cleanUp() {
        try {
            // remove the root directory
            FileUtils.deleteDirectory(TESTS_ROOT_DIR);
        } catch (Exception exception) {
            // just log the exception and move on
            LOGGER.log(
                    Level.WARNING,
                    String.format(
                            "Error removing tests root directory '%s'.",
                            TESTS_ROOT_DIR.getAbsolutePath()),
                    exception);
        }
    }

    /**
     * Tests retrieving all complex feature from App-Schema store that uses Apache Solr as a data
     * store.
     */
    public void testRetrievingStationsComplexFeatures() throws Exception {
        // gets the complex features source and read all the features
        FeatureSource<FeatureType, Feature> source =
                appSchemaDataStore.getFeatureSource(MAPPED_TYPE_NAME);
        List<Feature> features = iteratorToList(source.getFeatures().features());
        // check that we got the expected number of features
        assertEquals(2, features.size());
        // check that we have the expected features
        GeometryFactory geometryFactory = new GeometryFactory();
        checkFeatureExists(
                features, "7", "Bologna", geometryFactory.createPoint(new Coordinate(11.34, 44.5)));
        checkFeatureExists(
                features,
                "13",
                "Alessandria",
                geometryFactory.createPoint(new Coordinate(8.63, 44.92)),
                "ALS_TAG_1",
                "ALS_TAG_2");
    }

    /**
     * Helper method that checks if a feature exists in the provided features list, if the feature
     * is found its attributes are compared for equality with the provided ones.
     */
    private Feature checkFeatureExists(
            List<Feature> features, String id, String name, Point position, String... tags) {
        for (Feature feature : features) {
            if (feature.getIdentifier().getID().equals(id)) {
                // check the station name
                Property nameProperty = feature.getProperty(STATION_NAME);
                assertThat(nameProperty, notNullValue());
                assertThat(nameProperty.getValue(), notNullValue());
                assertThat(nameProperty.getValue(), instanceOf(String.class));
                // check the station geometry
                assertThat(feature.getDefaultGeometryProperty(), notNullValue());
                Object geometryValue = feature.getDefaultGeometryProperty().getValue();
                assertThat(geometryValue, notNullValue());
                assertThat(geometryValue, instanceOf(Point.class));
                assertThat(geometryValue, is(position));
                // check that we got the correct tags
                Collection<Property> tagProperties = feature.getProperties(STATION_TAG);
                assertThat(tagProperties.size(), is(tags.length));
                for (Property tagProperty : tagProperties) {
                    // extract the tag value
                    assertThat(tagProperty, notNullValue());
                    Object tagValue = tagProperty.getValue();
                    assertThat(tagValue, notNullValue());
                    assertThat(tagValue, instanceOf(String.class));
                    // check that is a valid one
                    int index = Arrays.binarySearch(tags, tagValue);
                    assertThat(index >= 0, is(true));
                    assertThat(tags[index], is(tagValue));
                }
                for (String tag : tags) {}

                // feature found, we are done
                return feature;
            }
        }
        // feature not found
        fail("Feature not found");
        return null;
    }

    /** Helper method that just converts a features iterator to a list of features. */
    private List<Feature> iteratorToList(FeatureIterator<Feature> iterator) {
        List<Feature> features = new ArrayList<>();
        while (iterator.hasNext()) {
            features.add(iterator.next());
        }
        return features;
    }

    /** Create App-Schema data store. */
    private void instantiateAppSchemaDataStore() {
        // prepare the data store parameters
        URI mappingsFiles = new File(TESTS_ROOT_DIR, "mappings.xml").toURI();
        Map<String, Serializable> dataStoreParameters = new HashMap<>();
        dataStoreParameters.put("dbtype", "app-schema");
        dataStoreParameters.put("url", mappingsFiles.toString());
        try {
            // instantiate the data store
            appSchemaDataStore = DataAccessFinder.getDataStore(dataStoreParameters);
        } catch (Exception exception) {
            throw new RuntimeException("Error instantiating App-Schema data store.", exception);
        }
    }

    /**
     * Helper method that gets the Apache Solr core URL that should be used for the tests from the
     * fixture file.
     */
    private String getSolrCoreURL() {
        return fixture.getProperty("solr_url");
    }

    /** A fixture file named solr.properties is required to exist in the users home directory. */
    @Override
    protected String getFixtureId() {
        return "solr";
    }
}
