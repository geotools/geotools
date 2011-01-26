/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataTestCase;
import org.geotools.data.DefaultQuery;
import org.geotools.data.jdbc.JDBCFeatureSource;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.data.postgis.collection.PostgisFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.ClassificationFunction;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.ExplicitClassifier;
import org.geotools.filter.function.UniqueIntervalFunction;
import org.geotools.styling.FeatureTypeStyle;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.PropertyName;


/**
 *
 * @source $URL$
 */
public class StyleGeneratorOnlineTest extends DataTestCase {
    static boolean WKB_ENABLED = true;
    static boolean CHECK_TYPE = false;
    PostgisTests.Fixture f;
    DataStore data;
    Map remote;
    PostgisFeatureCollection fc;

    public StyleGeneratorOnlineTest(String arg0) {
        super(arg0);
    }

    public String getFixtureFile() {
        return "fixture.properties";
    }

    protected void setUp() throws Exception {
        super.setUp();

        f = PostgisTests.newFixture(getFixtureFile());

        remote = new HashMap();
        remote.put("dbtype", "postgis");
        remote.put("charset", "");
        remote.put("host", f.host);
        remote.put("port", f.port);
        remote.put("database", f.database);
        remote.put("user", f.user);
        remote.put("passwd", f.password);
        remote.put("namespace", f.namespace);

        PostgisDataStoreFactory pdsf = new PostgisDataStoreFactory();
        data = pdsf.createDataStore(remote);

        JDBCFeatureSource featureSource = (JDBCFeatureSource) data.getFeatureSource("bc_hospitals");
        fc = new PostgisFeatureCollection(featureSource, DefaultQuery.ALL);
    }

    protected void tearDown() throws Exception {
        fc = null;
        data = null;
        super.tearDown();
    }

    /**
     * Simple test to ensure unique interval function works on real data
     * containing nulls.
     *
     * @throws Exception
     */
    public void testUniqueInterval() throws Exception {
        ColorBrewer brewer = new ColorBrewer();
        brewer.loadPalettes();

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyName expr = ff.property("authority");

        String paletteName = "YlGn";

        //create the classification function
        ClassificationFunction function = new UniqueIntervalFunction();
        List params = new ArrayList();
        params.add(0, expr); //expression
        params.add(1, ff.literal(7)); //classes
        function.setParameters(params);

        Object object = function.evaluate(fc);
        assertTrue(object instanceof ExplicitClassifier);

        Classifier classifier = (Classifier) object;

        Color[] colors = brewer.getPalette(paletteName).getColors(7);

        //get the fts
        FeatureTypeStyle fts = StyleGenerator.createFeatureTypeStyle(classifier, expr, colors,
                "myfts", roadFeatures[0].getFeatureType().getGeometryDescriptor(),
                StyleGenerator.ELSEMODE_IGNORE, 0.5, null);
        assertNotNull(fts);
    }
}
