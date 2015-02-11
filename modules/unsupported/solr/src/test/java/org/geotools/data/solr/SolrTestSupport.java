/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.test.OnlineTestCase;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

import com.vividsolutions.jts.geom.Geometry;

public abstract class SolrTestSupport extends OnlineTestCase {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(SolrTestSupport.class);

    static {
        // uncomment to turn up logging

        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(java.util.logging.Level.FINE);

        org.geotools.util.logging.Logging.getLogger("org.geotools.data.solr").setLevel(
                java.util.logging.Level.FINE);
        org.geotools.util.logging.Logging.getLogger("org.geotools.data.solr").addHandler(handler);
    }

    protected SolrFeatureSource featureSource;

    protected SolrDataStore dataStore;

    protected String testFile = "wifiAccessPoint.xml";

    protected String layerName = "active";

    protected int SOURCE_SRID = 4326;

    protected String pkField;

    private ArrayList<SolrAttribute> attributes;

    private static boolean setUpIsDone = false;

    protected DateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    public void setUpSolrFile(String url) throws Exception {
        if (setUpIsDone) {
            return;
        }
        // do the setup
        File testDir = (Paths.get(getClass().getResource("/" + testFile).toURI()).getParent())
                .toFile();
        ProcessBuilder pb = new ProcessBuilder("java", "-Durl=" + url + "/update", "-jar",
                "post.jar", testFile);
        pb.directory(testDir);
        LOGGER.log(Level.FINE, "Starting SOLR import");
        final Process command = pb.start();
        LOGGER.log(Level.FINE, "Started SOLR import");
        String line;
        BufferedReader bri = new BufferedReader(new InputStreamReader(command.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(command.getErrorStream()));
        while ((line = bri.readLine()) != null) {
            LOGGER.log(Level.FINE, line);
        }
        bri.close();
        while ((line = bre.readLine()) != null) {
            LOGGER.log(Level.FINE, line);
        }
        bre.close();
        int i = command.waitFor();
        assertTrue(i == 0);
        LOGGER.log(Level.FINE, "SOLR import DONE!");
        setUpIsDone = true;
    }

    @Override
    protected void connect() throws Exception {
        String url = fixture.getProperty(SolrDataStoreFactory.URL.key);
        setUpSolrFile(url);

        Map params = createConnectionParams(url, fixture);

        SolrDataStoreFactory factory = new SolrDataStoreFactory();
        dataStore = (SolrDataStore) factory.createDataStore(params);

        attributes = dataStore.getSolrAttributes(this.layerName);
        for (SolrAttribute at : attributes) {
            if (at.isPk()) {
                this.pkField = at.getName();
            }
            if (Geometry.class.isAssignableFrom(at.getType())) {
                at.setSrid(SOURCE_SRID);
            }
            at.setUse(true);
        }

    }

    protected Map createConnectionParams(String url, Properties fixture) {
        String field = fixture.getProperty(SolrDataStoreFactory.FIELD.key);

        Map params = new HashMap();
        params.put(SolrDataStoreFactory.URL.key, url);
        params.put(SolrDataStoreFactory.FIELD.key, field);
        params.put(SolrDataStoreFactory.NAMESPACE.key, SolrDataStoreFactory.NAMESPACE.sample);

        return params;
    }

    protected void init() throws Exception {
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        init(this.layerName);
    }

    protected void init(String layerName) throws Exception {
        init(layerName, "geo");
    }

    protected void init(String layerName, String geometryField) throws Exception {
        this.layerName = layerName;
        SolrLayerConfiguration solrLayerConfiguration = new SolrLayerConfiguration(
                new ArrayList<SolrAttribute>());
        solrLayerConfiguration.setLayerName(this.layerName);
        List<SolrAttribute> layerAttributes = new ArrayList<>();
        for (SolrAttribute solrAttribute : attributes) {
            if (geometryField.equals(solrAttribute.getName())) {
                SolrAttribute copy = new SolrAttribute(solrAttribute);
                copy.setDefaultGeometry(true);
                layerAttributes.add(copy);
            } else {
                layerAttributes.add(solrAttribute);
            }
        }
        solrLayerConfiguration.getAttributes().addAll(layerAttributes);
        dataStore.setSolrConfigurations(solrLayerConfiguration);
        featureSource = (SolrFeatureSource) dataStore.getFeatureSource(this.layerName);
    }

    @Override
    protected void disconnect() throws Exception {
        dataStore.dispose();
    }

    @Override
    protected String getFixtureId() {
        return SolrDataStoreFactory.NAMESPACE.sample.toString();
    }

    protected Date date(String date) throws ParseException {
        return df.parse(date);
    }

    protected Instant instant(String d) throws ParseException {
        return new DefaultInstant(new DefaultPosition(date(d)));
    }

    protected Period period(String d1, String d2) throws ParseException {
        return new DefaultPeriod(instant(d1), instant(d2));
    }
}
