/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.Collections;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.metadata.citation.Citation;


/**
 *
 * @since 2.4
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: PropertyExistsFunctionTest.java 24966 2007-03-30 11:33:47Z
 *          vmpazos $
 *
 * @source $URL$
 */
public class PropertyExistsFunctionTest {
    private static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    PropertyExistsFunction f;

    @Before
    public void setUp() {
        f = new PropertyExistsFunction();
    }

    @After
    public void tearDown() {
        f = null;
    }

    @Test
    public void testName() {
        Assert.assertEquals("propertyexists", f.getName().toLowerCase());
    }

    @Ignore
    public void testFind() {
        Function function = ff.function("propertyexists", ff.property("testPropName"));
        Assert.assertNotNull(function);
    }

    @Test
    public void testEvaluateFeature() throws Exception{
        SimpleFeatureType type = DataUtilities.createType("ns", "name:string,geom:Geometry");
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add("testName");
        build.add(null);
        SimpleFeature feature = build.buildFeature(null);

        f.setParameters( list(ff.property("nonExistant")));
        Assert.assertEquals(Boolean.FALSE, f.evaluate(feature));

        f.setParameters(list(ff.property("name")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(feature));

        f.setParameters(list(ff.property("geom")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(feature));
    }
    private List<Expression> list( Expression expr ){
        return Collections.singletonList(expr);
    }
    @Test
    public void testEvaluatePojo() {
        Citation pojo = new CitationImpl();

        f.setParameters( list(ff.property("edition")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(pojo));

        f.setParameters(list(ff.property("alternateTitles")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(pojo));

        // wrong case (note the first letter)
        f.setParameters(list(ff.property("AlternateTitles")));
        Assert.assertEquals(Boolean.FALSE, f.evaluate(pojo));

        f.setParameters(list(ff.property("nonExistentProperty")));
        Assert.assertEquals(Boolean.FALSE, f.evaluate(pojo));
    }

    @Test
    public void testEquals() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyExistsFunction actual = new PropertyExistsFunction();
        f.setParameters(list(ff.property("testPropName")));
        actual.setParameters(list(ff
                .property("testPropName")));
        Assert.assertEquals(f, actual);
    }
}
