/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.raster;

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.image.jai.Registry;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.jaitools.media.jai.vectorize.VectorizeDescriptor;
import org.jaitools.media.jai.vectorize.VectorizeRIF;
import org.jaitools.numeric.Range;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.util.ProgressListener;

/**
 * A process for raster to vector conversion. Regions of uniform value in an input {@linkplain
 * GridCoverage2D} are converted into {@linkplain Polygon}s by tracing the cell boundaries. Results
 * are returned as a {@linkplain SimpleFeatureCollection} in which each feature corresponds to a
 * raster region with the boundary {@code Polygon} as its default geometry ("the_geom") and the
 * value of the raster region cells as an attribute ("value").
 *
 * <p>Optionally, a list of classification ranges ({@linkplain org.jaitools.numeric.Range} objects)
 * can be provided to pre-classify the input coverage values into intervals. Vectorizing can also be
 * restricted to a sub-area of the coverage and/or a subset of raster values (by defining values to
 * treat as no-data).
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 8.0
 * @version $Id$
 */
@DescribeProcess(
    title = "Polygon Extraction",
    description =
            "Extracts vector polygons from a raster, based on regions which are equal or in given ranges"
)
public class PolygonExtractionProcess implements RasterProcess {

    static {
        Registry.registerRIF(
                JAI.getDefaultInstance(),
                new VectorizeDescriptor(),
                new VectorizeRIF(),
                Registry.JAI_TOOLS_PRODUCT);
    }

    /**
     * Executes the raster to vector process.
     *
     * @param coverage the input grid coverage
     * @param band the coverage band to process; defaults to 0 if {@code null}
     * @param insideEdges whether boundaries between raster regions with data values (ie. not
     *     NODATA) should be returned; defaults to {@code true} if {@code null}
     * @param roi optional polygonal {@code Geometry} to define a sub-area within which vectorizing
     *     will be done
     * @param noDataValues optional list of values to treat as NODATA; regions with these values
     *     will not be represented in the returned features; if {@code null}, 0 is used as the
     *     single NODATA value; ignored if {@code classificationRanges} is provided
     * @param classificationRanges optional list of {@code Range} objects to pre-classify the input
     *     coverage prior to vectorizing; values not included in the list will be treated as NODATA;
     *     values in the first {@code Range} are classified to 1, those in the second {@code Range}
     *     to 2 etc.
     * @param progressListener an optional listener
     * @return a feature collection where each feature has a {@code Polygon} ("the_geom") and an
     *     attribute "value" with value of the corresponding region in either {@code coverage} or
     *     the classified coverage (when {@code classificationRanges} is used)
     */
    @DescribeResult(name = "result", description = "The extracted polygon features")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "data", description = "Source raster")
                    GridCoverage2D coverage,
            @DescribeParameter(
                        name = "band",
                        description = "Source band to use (default = 0)",
                        min = 0,
                        defaultValue = "0"
                    )
                    Integer band,
            @DescribeParameter(
                        name = "insideEdges",
                        description =
                                "Indicates whether to vectorize boundaries between adjacent regions with non-outside values",
                        min = 0
                    )
                    Boolean insideEdges,
            @DescribeParameter(
                        name = "roi",
                        description =
                                "Geometry delineating the region of interest (in raster coordinate system)",
                        min = 0
                    )
                    Geometry roi,
            @DescribeParameter(
                        name = "nodata",
                        description = "Value to treat as NODATA (default is 0)",
                        collectionType = Number.class,
                        min = 0
                    )
                    Collection<Number> noDataValues,
            @DescribeParameter(
                        name = "ranges",
                        description =
                                "Specifier for a value range in the format ( START ; END ).  START and END values are optional. [ and ] can also be used as brackets, to indicate inclusion of the relevant range endpoint.",
                        collectionType = Range.class,
                        min = 0
                    )
                    List<Range> classificationRanges,
            ProgressListener progressListener)
            throws ProcessException {

        //
        // initial checks
        //
        if (coverage == null) {
            throw new ProcessException("Invalid input, source grid coverage should be not null");
        }

        if (band == null) {
            band = 0;
        } else if (band < 0 || band >= coverage.getNumSampleDimensions()) {
            throw new ProcessException("Invalid input, invalid band number:" + band);
        }

        // do we have classification ranges?
        boolean hasClassificationRanges =
                classificationRanges != null && classificationRanges.size() > 0;

        // apply the classification by setting 0 as the default value and using 1, ..., numClasses
        // for the other classes.
        // we use 0 also as the noData for the resulting coverage.
        if (hasClassificationRanges) {

            final RangeLookupProcess lookup = new RangeLookupProcess();
            coverage = lookup.execute(coverage, band, classificationRanges, progressListener);
        }

        // Use noDataValues to set the "outsideValues" parameter of the Vectorize
        // operation unless classificationRanges are in use, in which case the
        // noDataValues arg is ignored.
        List<Number> outsideValues = new ArrayList<Number>();
        if (noDataValues != null && !hasClassificationRanges) {
            outsideValues.addAll(noDataValues);
        } else {
            outsideValues.add(0);
        }

        //
        // GRID TO WORLD preparation
        //
        final AffineTransform mt2D =
                (AffineTransform)
                        coverage.getGridGeometry().getGridToCRS2D(PixelOrientation.UPPER_LEFT);

        // get the rendered image
        final RenderedImage raster = coverage.getRenderedImage();

        // perform jai operation
        ParameterBlockJAI pb = new ParameterBlockJAI("Vectorize");
        pb.setSource("source0", raster);

        if (roi != null) {
            pb.setParameter("roi", CoverageUtilities.prepareROI(roi, mt2D));
        }
        pb.setParameter("band", band);
        pb.setParameter("outsideValues", outsideValues);
        if (insideEdges != null) {
            pb.setParameter("insideEdges", insideEdges);
        }
        // pb.setParameter("removeCollinear", false);

        final RenderedOp dest = JAI.create("Vectorize", pb);
        @SuppressWarnings("unchecked")
        final Collection<Polygon> prop =
                (Collection<Polygon>) dest.getProperty(VectorizeDescriptor.VECTOR_PROPERTY_NAME);

        // wrap as a feature collection and return
        final SimpleFeatureType featureType =
                CoverageUtilities.createFeatureType(coverage, Polygon.class);
        final SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        int i = 0;
        final ListFeatureCollection featureCollection = new ListFeatureCollection(featureType);
        final AffineTransformation jtsTransformation =
                new AffineTransformation(
                        mt2D.getScaleX(),
                        mt2D.getShearX(),
                        mt2D.getTranslateX(),
                        mt2D.getShearY(),
                        mt2D.getScaleY(),
                        mt2D.getTranslateY());
        for (Polygon polygon : prop) {
            // get value
            Double value = (Double) polygon.getUserData();
            polygon.setUserData(null);
            // filter coordinates in place
            polygon.apply(jtsTransformation);

            // create feature and add to list
            builder.set("the_geom", polygon);
            builder.set("value", value);

            featureCollection.add(builder.buildFeature(String.valueOf(i++)));
        }

        // return value
        return featureCollection;
    }
}
