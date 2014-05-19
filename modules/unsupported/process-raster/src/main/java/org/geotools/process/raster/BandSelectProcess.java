package org.geotools.process.raster;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.SelectSampleDimension;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeResult;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.processing.Operation;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Process calling the {@link SelectSampleDimension} operation. This process requires:
 * 
 * <ul>
 * <li>a {@link GridCoverage2D} object.</li>
 * <li>an array of the Band Indexes to select.</li>
 * <li>an optional integer parameter associated to the visible sample dimension.</li>
 * </ul>
 * 
 * The output of this process is a {@link GridCoverage2D} object containing only the {@link SampleDimension}s indicated by the input array.
 * 
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 * 
 */
public class BandSelectProcess implements RasterProcess {
    /** Processor to use for executing the {@link SelectSampleDimension} operation */
    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    /** The {@link SelectSampleDimension} operation to execute */
    private static final Operation BANDSELECT = PROCESSOR.getOperation("SelectSampleDimension");

    @DescribeResult(name = "result", description = "A selection on the input rasters")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "Input GridCoverage", min = 1) GridCoverage2D coverage,
            @DescribeParameter(name = "SampleDimensions", description = "Input sample dimension indexes", min = 1) int[] sampleDims,
            @DescribeParameter(name = "VisibleSampleDimension", description = "Input visible sample dimension index", min = 0) Integer visibleSampleDim)
            throws ProcessException {

        // //
        //
        // Parameters definition
        //
        // //
        final ParameterValueGroup param = BANDSELECT.getParameters();
        // Setting of the source
        param.parameter("Source").setValue(coverage);
        // Setting of the Sample Dimension
        param.parameter("SampleDimensions").setValue(sampleDims);
        // Setting of the Sample Dimension
        param.parameter("VisibleSampleDimension").setValue(visibleSampleDim);

        // Call the "BandSelect" operation
        return (GridCoverage2D) PROCESSOR.doOperation(param);
    }
}
