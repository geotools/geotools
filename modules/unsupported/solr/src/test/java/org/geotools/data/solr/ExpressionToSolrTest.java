/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

public final class ExpressionToSolrTest {

    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testEnvelopeExpresion() throws ParseException {
        final ExpressionToSolr expressionToSolr = new ExpressionToSolr();
        expressionToSolr.setSpatialStrategy(SolrSpatialStrategy.DEFAULT);
        final Envelope env = new Envelope(1.0d, 2.0d, 3.0d, 4.0d);
        final Literal literal = ff.literal(env);
        Object result = expressionToSolr.visit(literal, null);
        Assert.assertNotNull(result);
        // decode Polygon WKT and compare with source Envelope
        final WKTReader reader = new WKTReader();
        Geometry geometry = reader.read(result.toString());
        Assert.assertTrue(geometry instanceof Polygon);
        Assert.assertEquals(env, geometry.getEnvelopeInternal());
    }
}
