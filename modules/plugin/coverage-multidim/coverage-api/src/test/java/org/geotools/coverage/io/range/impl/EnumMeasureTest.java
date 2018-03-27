/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012 - 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.range.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Dimensionless;
import javax.measure.Quantity;

import org.geotools.feature.NameImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultLinearCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.util.SimpleInternationalString;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.coverage.SampleDimension;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.EngineeringDatum;

import si.uom.SI;
import tec.uom.se.AbstractUnit;
import tec.uom.se.unit.MetricPrefix;

/**
 * @author Simone Giannecchini, GeoSolutions
 * 
 * @source $URL$
 */
public class EnumMeasureTest extends Assert {

    /** Bands captured as an enumeration used as an example below */
    enum Band {
        BLUE, GREE, RED, NIR, SWIT, TIR, SWR2
    };

    /**
     * This test uses use the default implementations to express 7 bands of a landsat image.
     */
    @Test
    public void testLandsatAxis() {
        CoordinateSystemAxis csAxis = new DefaultCoordinateSystemAxis(
                new SimpleInternationalString("light"), "light", AxisDirection.OTHER,
                MetricPrefix.MICRO(SI.METRE));

        DefaultLinearCS lightCS = new DefaultLinearCS("light", csAxis);
        Map<String, Object> datumProperties = new HashMap<String, Object>();
        datumProperties.put("name", "light");

        EngineeringDatum lightDatum = new DefaultEngineeringDatum(datumProperties);
        DefaultEngineeringCRS lightCRS = new DefaultEngineeringCRS("wave length", lightDatum,
                lightCS);

        List<Quantity<Band, Dimensionless>> keys = EnumMeasure.valueOf(Band.class);

        DefaultAxis<Band, Dimensionless> axis = new DefaultAxis<Band, Dimensionless>(new NameImpl(
                "Bands"), new SimpleInternationalString("Landsat bands by wavelength"), keys,
                AbstractUnit.ONE);

        Map<Quantity<Integer, Dimensionless>, SampleDimension> samples = new HashMap<Quantity<Integer, Dimensionless>, SampleDimension>();
        
        // Ensure that the equals method is correct
        EnumMeasure<Band> band = EnumMeasure.valueOf(Band.BLUE);
        assertTrue(band.equals(keys.get(0)));
        
        // Check if the ordinal value is correct
        assertEquals(Band.BLUE.ordinal(), keys.get(0).doubleValue(null), 0.01d);
        
        // Check if the value is correct
        assertEquals(Band.BLUE, keys.get(0).getValue());
        
        // Check if the TO method is correct
        assertSame(keys.get(0), keys.get(0).to(null));
        
        // Ensure the Unit is one
        assertEquals(AbstractUnit.ONE, keys.get(0).getUnit());
    }

}
