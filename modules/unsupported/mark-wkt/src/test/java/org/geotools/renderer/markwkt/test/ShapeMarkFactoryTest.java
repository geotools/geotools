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

package org.geotools.renderer.markwkt.test;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.filter.FilterFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.markwkt.MeteoMarkFactory;

import com.vividsolutions.jts.geom.LineString;

import junit.framework.TestCase;

/**
 * Unit tests for shape mark factory
 * 
 * @author Luca Morandini lmorandini@ieee.org
 * 
 * @source $URL:$
 */
public class ShapeMarkFactoryTest extends TestCase {

    private SimpleFeature feature;
    private Expression exp;
    private FilterFactory ff;

    {
	try {
	    ff = CommonFactoryFinder.getFilterFactory(null);
	    SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
	    featureTypeBuilder.setName("TestType");
	    featureTypeBuilder.add("geom", LineString.class,
		    DefaultGeographicCRS.WGS84);
	    SimpleFeatureType featureType = featureTypeBuilder
		    .buildFeatureType();
	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(
		    featureType);
	    this.feature = featureBuilder.buildFeature(null);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void testWellKnownName() {
        MeteoMarkFactory smf = new MeteoMarkFactory();
	try {
	    this.exp = ff.literal("shape://triangle");
	    smf.getShape(null, this.exp, this.feature);
	} catch (Exception e) {
	    assertTrue(false);
	    return;
	}

	assertTrue(true);
    }

    public void testUnknownProtocol() {
        MeteoMarkFactory smf = new MeteoMarkFactory();
	try {
	    this.exp = ff
		    .literal("xxx://triangle");
	    if (smf.getShape(null, this.exp, this.feature) == null) {
		assertTrue(true);
		return;
	    }
	} catch (Exception e) {
	    assertTrue(false);
	    return;
	}

	assertTrue(false);
    }
}
