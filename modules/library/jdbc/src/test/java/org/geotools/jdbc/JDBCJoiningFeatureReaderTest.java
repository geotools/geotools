
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Join;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCJoiningFeatureReaderTest extends JDBCFeatureReaderTest {

    public void testNextHiddenPrimaryKeys() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        Query query = new Query(tname("ftjoin"));
        query.getJoins().add(new Join(tname("ft1"), 
            ff.equal(ff.property(aname("name")), ff.property(aname("stringProperty")), true)));

        query.getJoins().add(new Join(tname("ftjoin2"), 
                ff.equal(ff.property(aname("join2intProperty")), ff.property(aname("join1intProperty")), true)));

        doTestNext(query, false);
    }

    public void testNextExposedPrimaryKeys() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        Query query = new Query(tname("ftjoin"));
        query.getJoins().add(new Join(tname("ft1"), 
            ff.equal(ff.property(aname("name")), ff.property(aname("stringProperty")), true)));

        query.getJoins().add(new Join(tname("ftjoin2"), 
                ff.equal(ff.property(aname("join2intProperty")), ff.property(aname("join1intProperty")), true)));

        doTestNext(query, true);
    }
}
