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

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.test.OnlineTestCase;
import org.locationtech.jts.geom.Geometry;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

public abstract class SolrTestSupport extends OnlineTestCase {

    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SolrTestSupport.class);

    static {
        // uncomment to turn up logging

        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(java.util.logging.Level.FINE);

        org.geotools.util.logging.Logging.getLogger("org.geotools.data.solr")
                .setLevel(java.util.logging.Level.FINE);
        org.geotools.util.logging.Logging.getLogger("org.geotools.data.solr").addHandler(handler);
    }

    protected SolrFeatureSource featureSource;

    protected SolrDataStore dataStore;

    protected String layerName = "active";

    protected int SOURCE_SRID = 4326;

    protected String pkField;

    private ArrayList<SolrAttribute> attributes;

    protected DateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

    // tests setup will take care of instantiating the client and closing it
    private HttpSolrClient solrClient;

    @Override
    protected void setUpInternal() throws Exception {
        // add to provided Solr core the necessary data
        String coreUrl = fixture.getProperty(SolrDataStoreFactory.URL.key);
        this.solrClient = TestsSolrUtils.instantiateClient(coreUrl);
        TestsSolrUtils.cleanIndex(solrClient);
        // make sure the needed geometry field types exist in the managed schema
        TestsSolrUtils.createWktFieldType(this.solrClient);
        TestsSolrUtils.createBboxFieldType(this.solrClient);
        // make sure geometry fields are correctly indexed
        TestsSolrUtils.createWktField(this.solrClient, "geo");
        TestsSolrUtils.createWktField(this.solrClient, "geo2");
        TestsSolrUtils.createBboxField(this.solrClient, "geo3");
        // get Solr documents from the test data
        InputStream documents = TestsSolrUtils.resourceToStream("/wifiAccessPoint.xml");
        // add the documents to the Solr core, letting Solr infer the rest of the schema
        TestsSolrUtils.runUpdateRequest(this.solrClient, documents);
    }

    @Override
    protected void connect() throws Exception {
        String url = fixture.getProperty(SolrDataStoreFactory.URL.key);

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
        String field = "status_s";

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
        SolrLayerConfiguration solrLayerConfiguration =
                new SolrLayerConfiguration(new ArrayList<SolrAttribute>());
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
    protected void disconnect() {
        dataStore.dispose();
        try {
            // make sure all HTTP connections to Solr server is closed
            this.solrClient.close();
        } catch (Exception exception) {
            // just log the exception and move on
            LOGGER.log(Level.WARNING, "Error closing Solr client.", exception);
        }
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
