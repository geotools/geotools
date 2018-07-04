/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.data.ComplexTestData;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AppSchemaAttributeBuilder;
import org.geotools.feature.ValidatingFeatureFactoryImpl;
import org.geotools.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.filter.expression.Function;

/**
 * @author Gabriel Roldan
 * @deprecated use the following property expression instead: <code>@gml:id</code>
 * @source $URL$
 */
public class IDFunctionExpressionTest extends AppSchemaTestSupport {

    Feature feature;

    Function idExpr;

    public IDFunctionExpressionTest() {}

    @Before
    public void setUp() throws Exception {
        FeatureTypeFactory typeFactory = new UniqueNameFeatureTypeFactoryImpl();
        FeatureType type = ComplexTestData.createExample02MultipleMultivalued(typeFactory);
        AppSchemaAttributeBuilder ab =
                new AppSchemaAttributeBuilder(new ValidatingFeatureFactoryImpl());
        ab.setType(type);
        feature = (Feature) ab.build("test-id");
        idExpr =
                CommonFactoryFinder.getFilterFactory(null)
                        .function("getID", new org.opengis.filter.expression.Expression[0]);
    }

    @Test
    public void testGetValue() throws Exception {
        Object fid = feature.getIdentifier();
        Object found = idExpr.evaluate(feature);
        assertNotNull(found);
        assertEquals(fid, found);
    }

    /*
     * Test method for 'org.geotools.filter.IDFunctionExpression.getName()'
     */
    @Test
    public void testGetName() {
        assertEquals("getID", idExpr.getName());
    }

    /*
     * Test method for 'org.geotools.filter.IDFunctionExpression.getArgs()'
     */
    @Test
    public void testGetArgs() {
        assertNotNull(idExpr.getParameters());
        assertEquals(0, idExpr.getParameters().size());
    }
}
