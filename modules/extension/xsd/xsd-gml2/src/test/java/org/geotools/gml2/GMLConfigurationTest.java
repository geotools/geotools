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
package org.geotools.gml2;

import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xs.XSConfiguration;
import org.geotools.xsd.SchemaLocationResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class GMLConfigurationTest {
    GMLConfiguration configuration;

    @Before
    public void setUp() throws Exception {

        configuration = new GMLConfiguration();
    }

    @Test
    public void testGetNamespaceURI() {
        Assert.assertEquals(GML.NAMESPACE, configuration.getNamespaceURI());
    }

    @Test
    public void testGetSchemaLocation() {
        Assert.assertEquals(
                GMLConfiguration.class.getResource("feature.xsd").toString(),
                configuration.getXSD().getSchemaLocation());
    }

    @Test
    public void testDependencies() {
        Assert.assertEquals(2, configuration.getDependencies().size());
        Assert.assertTrue(configuration.getDependencies().contains(new XLINKConfiguration()));
        Assert.assertTrue(configuration.getDependencies().contains(new XSConfiguration()));
    }

    @Test
    public void testSchemaLocationResolver() {
        Assert.assertEquals(
                GMLConfiguration.class.getResource("feature.xsd").toString(),
                new SchemaLocationResolver(configuration.getXSD())
                        .resolveSchemaLocation(null, GML.NAMESPACE, "feature.xsd"));
        Assert.assertEquals(
                GMLConfiguration.class.getResource("geometry.xsd").toString(),
                new SchemaLocationResolver(configuration.getXSD())
                        .resolveSchemaLocation(null, GML.NAMESPACE, "geometry.xsd"));
    }

    @Test
    public void testContext() {
        MutablePicoContainer container = new DefaultPicoContainer();
        configuration.configureContext(container);

        Assert.assertNotNull(container.getComponentInstanceOfType(FeatureTypeCache.class));
        Assert.assertNotNull(container.getComponentAdapter(CoordinateSequenceFactory.class));
        Assert.assertNotNull(container.getComponentAdapterOfType(GeometryFactory.class));
    }
}
