package org.geotools.referencing.operation.projection;

import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.*;
import static org.junit.Assert.*;

import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.Test;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;


public class CassiniSoldnerTest {

    /**
     * Tests the example provided by the EPSG guidance.
     * See "OGP Surveying and Positioning Guidance Note number 7, part 2 – April 2009", page 36
     */
    @Test
    public void testEpsgExample() throws Exception {
        MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup parameters = mtFactory.getDefaultParameters("Cassini-Soldner");
        
        // build the transformation using the guidance provided values
        final double feetToMeter = 0.3048; // ft -> mt
        final double linkToMeter = 0.66 * feetToMeter; // Clark's links -> mt
        parameter(SEMI_MAJOR, parameters).setValue(20926348 * feetToMeter);
        parameter(SEMI_MINOR, parameters).setValue(20855233 * feetToMeter);
        parameter(LATITUDE_OF_ORIGIN, parameters).setValue(dmsToDegree(10, 26, 30));
        parameter(CENTRAL_MERIDIAN, parameters).setValue(-dmsToDegree(61, 20, 0));
        parameter(FALSE_EASTING, parameters).setValue(430000.00 * linkToMeter);
        parameter(FALSE_NORTHING, parameters).setValue(325000.00 * linkToMeter);
        MathTransform transform = mtFactory.createParameterizedTransform(parameters);

        // results as provided by the EPSG guidance
        final double[] point = new double[] { -62, 10 };
        final double[] expected = new double[] { 66644.94 * linkToMeter, 82536.22 * linkToMeter };
        
        // check forward transform
        final double[] forward = new double[2];
        transform.transform(point, 0, forward, 0, 1);
        assertEquals(expected[0], forward[0], 1e-1);
        assertEquals(expected[1], forward[1], 1e-1);
        
        // check inverse transform
        final double[] inverse = new double[2];
        transform.inverse().transform(expected, 0, inverse, 0, 1);
        assertEquals(point[0], inverse[0], 1e-4);
        assertEquals(inverse[1], inverse[1], 1e-4);
    }
    
    /**
     * Extracts the {@link ParameterValue} for a certain {@link ParameterDescriptor}
     */
    ParameterValue parameter(ParameterDescriptor param, ParameterValueGroup group) {
        return group.parameter(param.getName().getCode());
    }
    
    /**
     * Converts a DMS value into degrees
     */
    double dmsToDegree(double degrees, double minutes, double seconds) {
        return degrees + (minutes + seconds / 60) / 60;
    }
}
