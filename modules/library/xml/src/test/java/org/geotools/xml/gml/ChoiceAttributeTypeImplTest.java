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
package org.geotools.xml.gml;

import static org.junit.Assert.*;

import org.geotools.feature.NameImpl;
import org.junit.Test;

public class ChoiceAttributeTypeImplTest {

    public static final int MIN = 0;
    public static final int MAX = 10;
    ChoiceAttributeTypeImpl choice =
            new ChoiceAttributeTypeImpl(
                    new NameImpl("test"),
                    new Class[] {Integer.class, Double.class},
                    Integer.class,
                    true,
                    MIN,
                    MAX,
                    null,
                    null);

    @Test
    public void testMin() {
        assertEquals(MIN, choice.getMinOccurs());
    }

    @Test
    public void testMax() {
        assertEquals(MAX, choice.getMaxOccurs());
    }
}
