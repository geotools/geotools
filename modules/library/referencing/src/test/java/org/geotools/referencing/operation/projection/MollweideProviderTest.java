/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 1999-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains formulas from the PROJ package of USGS.
 *    USGS's work is fully acknowledged here. This derived work has
 *    been relicensed under LGPL with Frank Warmerdam's permission.
 */
package org.geotools.referencing.operation.projection;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;

/** @author Marco Peters */
public class MollweideProviderTest {

    private String SEMI_MAJOR = Mollweide.MollweideProvider.SEMI_MAJOR.getName().getCode();
    private String SEMI_MINOR = Mollweide.MollweideProvider.SEMI_MINOR.getName().getCode();
    private String FALSE_NORTHING = Mollweide.MollweideProvider.FALSE_NORTHING.getName().getCode();
    private String FALSE_EASTING = Mollweide.MollweideProvider.FALSE_EASTING.getName().getCode();
    private String CENTRAL_MERIDIAN =
            Mollweide.MollweideProvider.CENTRAL_MERIDIAN.getName().getCode();

    @Test
    public void testParameters() throws Exception {
        ParameterDescriptorGroup parameters = Mollweide.MollweideProvider.PARAMETERS;
        assertNotNull(parameters.descriptor(SEMI_MAJOR));
        assertNotNull(parameters.descriptor(SEMI_MINOR));
        assertNotNull(parameters.descriptor(FALSE_NORTHING));
        assertNotNull(parameters.descriptor(FALSE_EASTING));
        assertNotNull(parameters.descriptor(CENTRAL_MERIDIAN));
    }

    @Test
    public void testMathTransformUsesSphericalParameter() throws Exception {
        Mollweide.MollweideProvider mollweideProvider = new Mollweide.MollweideProvider();
        ParameterValueGroup parameters = Mollweide.MollweideProvider.PARAMETERS.createValue();
        parameters.parameter(SEMI_MAJOR).setValue(6000);
        parameters.parameter(SEMI_MINOR).setValue(100);
        MathTransform mathTransform = mollweideProvider.createMathTransform(parameters);
        String wkt = mathTransform.toWKT();
        Assert.assertTrue(wkt.contains("\"semi_major\", 6000.0"));
        Assert.assertTrue(
                "Mollweide is only spherically defined. Axis must be equal",
                wkt.contains("\"semi_minor\", 6000.0"));
    }
}
