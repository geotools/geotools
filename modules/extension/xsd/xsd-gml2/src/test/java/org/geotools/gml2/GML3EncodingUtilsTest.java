/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2023, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;

import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.Test;

public class GML3EncodingUtilsTest {

    @Test
    public void testForceSrs() throws NoSuchAuthorityCodeException, FactoryException {
        System.setProperty(GeoTools.FORCE_SRS_STYLE, "true");
        Hints.putSystemDefault(Hints.FORCE_SRS_STYLE, true);

        CoordinateReferenceSystem crs = CRS.decode("epsg:5730");

        assertEquals(
                "http://www.opengis.net/def/crs/EPSG/0/5730",
                GML2EncodingUtils.toURI(crs, SrsSyntax.OGC_HTTP_URI));

        System.clearProperty(GeoTools.FORCE_SRS_STYLE);
        Hints.removeSystemDefault(Hints.FORCE_SRS_STYLE);
    }

    @Test
    public void testNoForceSrs() throws NoSuchAuthorityCodeException, FactoryException {
        System.setProperty(GeoTools.FORCE_SRS_STYLE, "false");
        Hints.putSystemDefault(Hints.FORCE_SRS_STYLE, false);

        CoordinateReferenceSystem crs = CRS.decode("epsg:5730");

        assertEquals(
                "http://www.opengis.net/gml/srs/epsg.xml#5730",
                GML2EncodingUtils.toURI(crs, SrsSyntax.OGC_HTTP_URI));

        System.clearProperty(GeoTools.FORCE_SRS_STYLE);
        Hints.removeSystemDefault(Hints.FORCE_SRS_STYLE);
    }
}
