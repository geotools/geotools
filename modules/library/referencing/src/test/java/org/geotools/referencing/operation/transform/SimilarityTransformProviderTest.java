package org.geotools.referencing.operation.transform;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Tests {@link SimilarityTransformProvider}.
 *
 * @source $URL$
 * @version $Id$
 * @author Oscar Fonts
 */
public final class SimilarityTransformProviderTest {
      
    /**
     * Tests {@link SimilarityTransformProvider}, both direct and inverse transfoms.
     */
    @Test
    public void testSimilarityTransform() throws FactoryException, TransformException {
               
        // Parameters from EPSG::5166 transform
        ParameterGroup params = new ParameterGroup(SimilarityTransformProvider.PARAMETERS);
        params.parameter("8621").setValue(-129.549);
        params.parameter("8622").setValue(-208.185);
        params.parameter("8611").setValue(1.0000015504);
        params.parameter("8614").setValue(1.56504);
        
        // Transform instance
        MathTransform mt = new DefaultMathTransformFactory().createParameterizedTransform(params);

        // Data from EPSG::9621 example
        final double precision = 1E-3; // dstPoints have 3 decimal units
        final double[] srcPoints = new double[] {300000, 4500000};
        final double[] dstPoints = new double[] {299905.060, 4499796.515};
        final double[] calculatedPoints = new double[srcPoints.length];
        
        // Direct transform test
        mt.transform(srcPoints, 0, calculatedPoints, 0, srcPoints.length/2);
        for (int i=0; i<calculatedPoints.length; i++) {
            assertEquals(dstPoints[i], calculatedPoints[i], precision);
        }

        // Inverse transform test
        mt.inverse().transform(dstPoints, 0, calculatedPoints, 0, dstPoints.length/2);
        for (int i=0; i<calculatedPoints.length; i++) {
            assertEquals(srcPoints[i], calculatedPoints[i], precision);
        }
    }
    
}
