/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import org.geotools.feature.Types;
import org.geotools.gml3.GMLSchema;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 
 * @author Gabriel Roldan (Axios Engineering)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 * @since 2.4
 */
public class XPathTest extends AppSchemaTestSupport {

    /**
     * Test that some simple-content and non-simple-content types are correctly detected.
     */
    @Test
    public void testIsSimpleContentType() {
        assertTrue(Types.isSimpleContentType(GMLSchema.CODETYPE_TYPE));
        assertTrue(Types.isSimpleContentType(GMLSchema.MEASURETYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.POINTTYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.POINTPROPERTYTYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.ABSTRACTFEATURETYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.ABSTRACTFEATURECOLLECTIONTYPE_TYPE));
    }

}
