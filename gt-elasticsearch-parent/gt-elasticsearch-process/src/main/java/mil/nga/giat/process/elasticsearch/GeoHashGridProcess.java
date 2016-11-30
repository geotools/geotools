/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.Operations;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.vector.VectorProcess;
import org.opengis.util.ProgressListener;

@DescribeProcess(title = "geoHashGridAgg", description = "Computes a grid from GeoHash grid aggregation buckets with values corresponding to doc_count values.")
public class GeoHashGridProcess implements VectorProcess {

    public enum Strategy {

        BASIC(BasicGeoHashGrid.class);

        private Class<? extends GeoHashGrid> clazz;

        private Strategy(Class<? extends GeoHashGrid> clazz) {
            this.clazz = clazz;
        }

        public GeoHashGrid createNewInstance() throws ReflectiveOperationException {
            return clazz.getConstructor().newInstance();
        }

    }

    @DescribeResult(name = "result", description = "Output raster")
    public GridCoverage2D execute(

            // process data
            @DescribeParameter(name = "data", description = "Input features") SimpleFeatureCollection obsFeatures,

            // process parameters
            @DescribeParameter(name = "pixelsPerCell", description = "Resolution used for upsampling (in pixels). Default = 1", defaultValue="1", min = 1) Integer argPixelsPerCell,
            @DescribeParameter(name = "gridStrategy", description = "GeoHash grid strategy", defaultValue="Basic", min = 1) String argStrategy,

            // output image parameters
            @DescribeParameter(name = "outputBBOX", description = "Bounding box of the output") ReferencedEnvelope argOutputEnv,
            @DescribeParameter(name = "outputWidth", description = "Width of output raster in pixels") Integer argOutputWidth,
            @DescribeParameter(name = "outputHeight", description = "Height of output raster in pixels") Integer argOutputHeight,

            ProgressListener monitor) throws ProcessException {

        try {
            // construct and populate grid
            final GeoHashGrid geoHashGrid = Strategy.valueOf(argStrategy.toUpperCase()).createNewInstance();
            geoHashGrid.initalize(argOutputEnv, obsFeatures);
            // convert to grid coverage
            final GridCoverage2D nativeCoverage = geoHashGrid.toGridCoverage2D();

            // reproject
            final GridCoverage2D transformedCoverage = (GridCoverage2D) Operations.DEFAULT.resample(nativeCoverage, argOutputEnv.getCoordinateReferenceSystem()); 
            // upscale to approximate output resolution
            final GridCoverage2D scaledCoverage = GridCoverageUtil.scale(transformedCoverage, argOutputWidth*argPixelsPerCell, argOutputHeight*argPixelsPerCell);
            // crop (geohash grid envelope will always contain output bbox)
            final GridCoverage2D croppedCoverage = GridCoverageUtil.crop(scaledCoverage, argOutputEnv);
            return GridCoverageUtil.scale(croppedCoverage, argOutputWidth, argOutputHeight);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            return null;
        } catch (Exception e) {
            throw new ProcessException(e);
        }
    }

}
