package org.geotools.process.raster;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;

/**
 * This utility class bundles up DEM tools for reuse.
 * <p>
 * This class has been marked with annotations describing extra information required for use via the
 * dynamic ProcessFactory API. For more details please see DEMProcessFactory.
 * 
 * @author Jody
 * @since 2.7
 * @version 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/process-raster/src/main/java/org/geotools/process/dem/DEMTools.java $
 */
public class DEMTools {
    @DescribeProcess(title = "slope", description = "Convert from a DEM to a gradient")
    @DescribeResult(name = "slope", type = GridCoverage2D.class, description = "Gradient field for the DEM")
    public static final GridCoverage2D slope(
            @DescribeParameter( name="DEM", description = "Digital Elevation model") GridCoverage2D DEM) {        
        // processing goes here
        return DEM;
    }

}
