/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.attributes;

import junit.framework.TestCase;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.validation.RoadValidationResults;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * NullZeroValidationTest purpose.
 *
 * <p>Description of NullZeroValidationTest ...
 *
 * <p>Capabilities:
 *
 * <ul>
 *   <li>
 * </ul>
 *
 * Example Use:
 *
 * <pre><code>
 * NullZeroValidationTest x = new NullZeroValidationTest(...);
 * </code></pre>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class NullZeroValidationTest extends TestCase {
    private GeometryFactory gf;
    private RoadValidationResults results;
    private SimpleFeatureType type;
    NullZeroValidation test;
    /** Constructor for NullZeroValidationTest. */
    public NullZeroValidationTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        gf = new GeometryFactory();
        test = new NullZeroValidation();
        test.setAttribute("name");
        test.setTypeRef("road");
        test.setName("JUnit");
        test.setName("test used for junit test " + getName());

        type = DataUtilities.createType(getName() + ".road", "id:0,*geom:LineString,name:String");

        results = new RoadValidationResults();
    }

    private SimpleFeature road(String road, int id, String name) throws IllegalAttributeException {
        Coordinate[] coords =
                new Coordinate[] {
                    new Coordinate(1, 1),
                    new Coordinate(2, 2),
                    new Coordinate(4, 2),
                    new Coordinate(5, 1)
                };
        return SimpleFeatureBuilder.build(
                type,
                new Object[] {
                    Integer.valueOf(id), gf.createLineString(coords), name,
                },
                type.getTypeName() + "." + road);
    }
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        test = null;
        super.tearDown();
    }

    public void testValidateNumber() throws Exception {
        test.setTypeRef("road");
        test.setAttribute("id");
        assertTrue(test.validate(road("rd1", 1, "street"), type, results));
        assertFalse(test.validate(road("rd2", 0, "avenue"), type, results));
    }

    public void testValidateName() throws Exception {
        test.setTypeRef("road");
        test.setAttribute("name");
        assertTrue(test.validate(road("rd1", 1, "street"), type, results));
        assertFalse(test.validate(road("rd2", 0, ""), type, results));
    }

    public void testNameAccessors() {
        test.setName("foo");
        assertEquals("foo", test.getName());
    }

    public void testDescriptionAccessors() {
        test.setDescription("foo");
        assertEquals("foo", test.getDescription());
    }

    public void testGetPriority() {
        test.getPriority();
    }
}
