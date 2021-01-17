/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import org.geotools.data.hana.metadata.Uom.Type;
import org.junit.Assert;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class UomTest {

    @Test
    public void testValidUom() {
        Uom uom = new Uom("kilometer", Type.LINEAR, 1000.0);
        Assert.assertEquals("kilometer", uom.getName());
        Assert.assertEquals(Type.LINEAR, uom.getType());
        Assert.assertEquals(1000.0, uom.getFactor(), 0d);
    }

    @Test
    public void testNullName() {
        try {
            new Uom(null, Type.LINEAR, 1000.0);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testEmptyName() {
        try {
            new Uom("", Type.LINEAR, 1000.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testNullType() {
        try {
            new Uom("meter", null, 1000.0);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testInvalidFactor() {
        try {
            new Uom("kilometer", Type.LINEAR, 0.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
