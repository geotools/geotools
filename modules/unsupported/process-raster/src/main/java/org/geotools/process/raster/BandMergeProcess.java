package org.geotools.process.raster;

import it.geosolutions.jaiext.bandmerge.BandMergeCRIF;
import it.geosolutions.jaiext.bandmerge.BandMergeDescriptor;

import java.util.Collection;

import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.BandMerge;
import org.geotools.image.jai.Registry;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeResult;
import org.opengis.coverage.processing.Operation;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Process calling the {@link BandMerge} operation. This process requires:
 * <ul>
 * <li>a {@link Collection} of {@link GridCoverage2D} objects (Note that they must be in the same CRS).</li>
 * <li>an optional ROI passed as {@link SimpleFeature}.</li>
 * <li>an optional String indicating the policy for choosing the Grid To World transformation(from those of all the Coverages) to use for the final
 * coverage. The available values are: FIRST(default), for selecting the first coverage; LAST, for the last coverage; INDEX, for selecting the
 * Coverage defined by the Index Parameter.</li>
 * <li>an optional integer parameter called Index used by the Grid To World transformation policy for choosing the coverage at the "Index" position.</li>
 * </ul>
 * 
 * The output of this process is a {@link GridCoverage2D} object which contains all the input Coverages, each one stored as a Band (or multiple bands
 * if the coverage is multibanded). This process can be used also for merging coverages which are not aligned and with different resolutions.
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 * 
 */
public class BandMergeProcess implements RasterProcess {
    
    static{
        // Static registration of the BandMerge operation
        Registry.registerRIF(JAI.getDefaultInstance(), new BandMergeDescriptor(), new BandMergeCRIF(), "it.geosolutions.jaiext.roiaware");
    }
    
    /** Processor to use for executing the {@link BandMerge} operation */
    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    /** The {@link BandMerge} operation to execute */
    private static final Operation BANDMERGE = PROCESSOR.getOperation("BandMergeOp");

    @DescribeResult(name = "result", description = "Merged Rasters")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverages", description = "Coverage List", min = 1) Collection<GridCoverage2D> coverages,
            @DescribeParameter(name = "roi", description = "Geometry to use as ROI", min = 0) SimpleFeature roi,
            @DescribeParameter(name = "transformChoice", description = "Choice on which Coverage G2W transform to use", min = 0) String transformChoice,
            @DescribeParameter(name = "index", description = "Index used by the transformChoice parameter", min = 0) Integer index)
            throws ProcessException {
        // //
        //
        // Initialization: CRS checks
        //
        // //
        BaseCoverageAlgebraProcess.checkCompatibleCoveragesForMerge(coverages);

        // //
        //
        // Doing the Operation
        //
        // //
        final ParameterValueGroup param = BANDMERGE.getParameters();
        // //
        //
        // ROI extraction
        //
        // //
        Geometry geo = null;
        if (roi != null && roi.getDefaultGeometry() instanceof Geometry) {
            geo = (Geometry) roi.getDefaultGeometry();

            Object crsGeo = geo.getUserData();

            if (crsGeo != null && crsGeo instanceof CoordinateReferenceSystem) {
                CoordinateReferenceSystem geoCRS = (CoordinateReferenceSystem) crsGeo;
                GridCoverage2D cov = coverages.iterator().next();
                // CRS Check
                BaseCoverageAlgebraProcess.checkCompatibleCRS(geoCRS,
                        cov.getCoordinateReferenceSystem());
                // Setting of the ROI if present
                param.parameter(BandMerge.GEOMETRY).setValue(geo);
            } else {
                // Supposing that the Geometry has the same CRS of the Coverage
                param.parameter(BandMerge.GEOMETRY).setValue(geo);
            }
        }

        // Addition of all the Coverages as source
        param.parameter("sources").setValue(coverages);

        // Addition of the Transformation Choice parameter if present
        if (transformChoice != null && !transformChoice.isEmpty()) {
            param.parameter(BandMerge.TRANSFORM_CHOICE).setValue(transformChoice);
        }

        // Addition of the Index parameter to use by the Transformation Choice if present
        if (transformChoice != null && !transformChoice.isEmpty()) {
            param.parameter(BandMerge.COVERAGE_INDEX).setValue(index);
        }

        // Call the "BandMerge" operation
        return (GridCoverage2D) PROCESSOR.doOperation(param);
    }
}
