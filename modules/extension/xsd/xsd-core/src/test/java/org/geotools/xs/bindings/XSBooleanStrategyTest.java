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
package org.geotools.xs.bindings;

import javax.xml.namespace.QName;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;


public class XSBooleanStrategyTest extends TestSchema {
    /**
     * An instance of a datatype that is defined as ??boolean?? can have the
     * following legal literals {true, false, 1, 0}.
     * @throws Exception
     *
     */

    /*
     * Test method for 'org.geotools.xs.strategies.XSBooleanStrategy.parse(Element, Node[], Object)'
     */
    public void testTruth() throws Exception {
        validateValues("true", Boolean.TRUE);
        validateValues("false", Boolean.FALSE);
        validateValues("1", Boolean.TRUE);
        validateValues("0", Boolean.FALSE);
    }

    public void testUntruth() throws Exception {
        try {
            validateValues("TRUE", Boolean.FALSE);
            fail("TRUTH is not absolute");
        } catch (IllegalArgumentException expected) {
            // yeah!
        }
    }

    protected QName getQName() {
        return XS.BOOLEAN;
    }
}
