/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.v3_2;

import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.gml3.GML3CurveParsingTest;

/**
 * Checks that the GML 3.2 bindings can do the work as well as the GML 3 ones
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GML32CurveParsingTest extends GML3CurveParsingTest {

    @Override
    protected void setUp() throws Exception {
        GMLConfiguration configuration = new GMLConfiguration(true);
        configuration.setGeometryFactory(new CurvedGeometryFactory(TOLERANCE));
        this.gml = configuration;
    }
}
