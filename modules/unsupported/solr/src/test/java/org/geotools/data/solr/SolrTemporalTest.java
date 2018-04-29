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

import java.util.Date;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.temporal.Period;

public class SolrTemporalTest extends SolrTestSupport {

    public void testCompareDateFilter() throws Exception {
        init();
        Date testDate = df.parse("2009-06-28 00:00:00");
        FilterFactory ff = dataStore.getFilterFactory();

        Filter f = ff.lessOrEqual(ff.property("installed_tdt"), ff.literal(testDate));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(4, features.size());
        SimpleFeatureIterator it = features.features();
        while (it.hasNext()) {
            Date date = (Date) it.next().getAttribute("installed_tdt");
            assertTrue(date.before(testDate) || date.equals(testDate));
        }
        it.close();

        f = ff.greaterOrEqual(ff.property("installed_tdt"), ff.literal(testDate));
        features = featureSource.getFeatures(f);
        assertEquals(5, features.size());
        it = features.features();
        while (it.hasNext()) {
            Date date = (Date) it.next().getAttribute("installed_tdt");
            assertTrue(date.after(testDate) || date.equals(testDate));
        }
        it.close();
    }

    public void testAfterFilter() throws Exception {
        init();
        Date testDate = df.parse("2009-28-06 00:00:00");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.after(ff.property("installed_tdt"), ff.literal(testDate));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(5, features.size());
    }

    public void testAfterInterval() throws Exception {
        init();
        Period period = period("2011-21-05 00:00:00", "2011-15-09 00:00:00");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.after(ff.property("installed_tdt"), ff.literal(period));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(4, features.size());
    }

    public void testBeforeFilter() throws Exception {
        init();
        Date testDate = df.parse("2009-28-06 00:00:00");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.before(ff.property("installed_tdt"), ff.literal(testDate));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(4, features.size());
    }

    public void testBeforeInterval() throws Exception {
        init();
        Period period = period("2000-12-11 00:00:00", "2011-05-21 00:00:00");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.before(ff.property("installed_tdt"), ff.literal(period));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testBegins() throws Exception {
        init();
        Period period = period("2004-20-06 03:44:56", "2014-22-06 03:44:56");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.begins(ff.property("installed_tdt"), ff.literal(period));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testBegunBy() throws Exception {
        init();
        Period period = period("2004-20-06 03:44:56", "2014-22-06 03:44:56");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.begunBy(ff.literal(period), ff.property("installed_tdt"));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testEnds() throws Exception {
        init();
        Period period = period("2002-20-06 03:44:56", "2004-20-06 03:44:56");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.ends(ff.property("installed_tdt"), ff.literal(period));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testEndedBy() throws Exception {
        init();
        Period period = period("2004-11-06 03:44:56", "2004-20-06 03:44:56");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.endedBy(ff.literal(period), ff.property("installed_tdt"));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testDuring() throws Exception {
        init();
        Period period = period("2004-19-06 03:44:56", "2004-20-06 03:44:58");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.during(ff.property("installed_tdt"), ff.literal(period));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testTContains() throws Exception {
        init();
        Period period = period("2004-19-06 03:44:56", "2004-20-06 03:44:58");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.tcontains(ff.literal(period), ff.property("installed_tdt"));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }

    public void testTEquals() throws Exception {
        init();
        Date testDate = df.parse("2013-01-10 00:13:11");
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.tequals(ff.property("installed_tdt"), ff.literal(testDate));
        SimpleFeatureCollection features = featureSource.getFeatures(f);
        assertEquals(1, features.size());
    }
}
