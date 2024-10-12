/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2.bindings;

import static org.geotools.gml2.bindings.GML2EncodingUtils.toURI;
import static org.junit.Assert.assertEquals;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.gml2.SrsSyntax;
import org.geotools.referencing.CRS;
import org.junit.Test;

public class GML2EncodingUtilsTest {

    @Test
    public void testCRS84() throws Exception {
        CoordinateReferenceSystem crs84 = CRS.decode("CRS:84");
        assertEquals("CRS:84", toURI(crs84, SrsSyntax.AUTH_CODE, false));
        assertEquals("http://www.opengis.net/def/crs/CRS/0/84", toURI(crs84, SrsSyntax.OGC_HTTP_URI, false));
        assertEquals("urn:ogc:def:crs:CRS::84", toURI(crs84, SrsSyntax.OGC_URN, false));
    }
}
