package org.geotools.process.dem;

import org.geotools.coverage.grid.GridCoverage2D;

/**
 * This utility class bundles up DEM tools for reuse.
 * <p>
 * This class has been marked with annotations describing extra information required for use via the
 * dynamic ProcessFactory API. For more details please see DEMProcessFactory.
 * 
 * @author Jody
 */
public class DEMTools {

    @DescribeProcess(description = "Convert from a DEM to a gradient")
    @DescribeResult(name = "slope", type = GridCoverage2D.class, description = "Gradient field for the DEM")
    public static final GridCoverage2D slope(
            @DescribeParameter( name="DEM", description = "Digital Elevation model") GridCoverage2D DEM) {        
        // processing goes here
        return DEM;
    }

}
