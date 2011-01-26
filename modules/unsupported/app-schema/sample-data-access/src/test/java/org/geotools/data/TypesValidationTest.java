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

package org.geotools.data;

import junit.framework.TestCase;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.gml3.GMLSchema;

/**
 * Test case to confirm errors reporting validation errors. See GEOT-2111.
 * 
 * <p>
 * 
 * <b>This test is diabled.>/b>
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.6
 */
public class TypesValidationTest extends TestCase {

    /**
     * Disabled test.
     */
    public static void testValidate() {
        if (false) { // disabled
            // this is to test for a bug with similar results to GEOT-2111
            new AttributeImpl("demo", new AttributeDescriptorImpl(GMLSchema.STRINGORREFTYPE_TYPE,
                    new NameImpl("http://www.opengis.net/gml", "description"), 0, 1, false, null),
                    null);
        }
    }

}
