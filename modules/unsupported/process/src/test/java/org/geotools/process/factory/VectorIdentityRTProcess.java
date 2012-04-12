package org.geotools.process.factory;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.ProcessException;
import org.geotools.process.gs.GSProcess;
import org.opengis.coverage.grid.GridGeometry;

/**
 * A simple Rendering Transformation process for testing aspects of how transformations are called.
 * 
 * 
 * @author Martin Davis - OpenGeo
 * 
 */
@DescribeProcess(title = "SimpleVectorRTProcess", description = "Simple test RT process taking a vector dataset as input.")
public class VectorIdentityRTProcess implements GSProcess {
    /**
     * Note: for testing purposes only. A real Rendering Transformation must never store state.
     */
    int invertQueryValue;

    @DescribeResult(name = "result", description = "The result")
    public SimpleFeatureCollection execute(
            // process data
            @DescribeParameter(name = "data", description = "Features to process") SimpleFeatureCollection data,
            @DescribeParameter(name = "value", description = "Value for testing") Integer value)
            throws ProcessException {
        if (value != invertQueryValue) {
            throw new IllegalStateException("Values do not match");
        }
        return data;
    }

    public Query invertQuery(
            @DescribeParameter(name = "value", description = "Value for testing") Integer value,
            Query targetQuery, GridGeometry targetGridGeometry) throws ProcessException {

        invertQueryValue = value;

        return targetQuery;
    }
}