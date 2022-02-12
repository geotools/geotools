/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2016, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.image.jai.Registry;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.NumberRange;
import org.jaitools.media.jai.contour.ContourDescriptor;
import org.jaitools.media.jai.contour.ContourRIF;
import org.jaitools.numeric.Range;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * A process to extract contours based on values in a specified band of the input {@linkplain
 * GridCoverage2D}. This is a geo-spatial wrapper around the JAITools "Contour" operation (see
 * {@linkplain ContourDescriptor} for details of the underlying algorithm).
 *
 * <p>You can specify the specific values for which contours will be generated, or alternatively the
 * interval between contour values.
 *
 * <p>Contours are returned as a feature collection, where each feature has, as its default
 * geometry, a {@linkplain LineString} for the contour ("the_geom"), and the contour value as the
 * {@code Double} attribute "value".
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 8.0
 * @version $Id$
 */
@DescribeProcess(
        title = "Contour",
        description =
                "Computes contour lines at specified intervals or levels for the values in a raster.")
public class ContourProcess implements RasterProcess {

    private static final InternationalString NO_DATA =
            Vocabulary.formatInternational(VocabularyKeys.NODATA);

    static {
        Registry.registerRIF(
                JAI.getDefaultInstance(),
                new ContourDescriptor(),
                new ContourRIF(),
                Registry.JAI_TOOLS_PRODUCT);
    }

    /**
     * Perform the contouring on the input {@linkplain GridCoverage2D} and returns the results as a
     * feature collection. You can control which contours are generated either by providing a list
     * of values via the {@code levels} argument, or by specifying the interval between contour
     * values via the {@code interval} argument. In the interval case, the resulting contour values
     * will be integer multiples of the specified interval. If both {@code levels} and {@code
     * interval} are supplied the {@code interval} argument is ignored.
     *
     * @param gc2d the input grid coverage
     * @param band the coverage band to process; defaults to 0 if {@code null}
     * @param levels the values for which contours should be generated
     * @param interval the interval between contour values (if {@code levels} is not provided)
     * @param simplify whether to simplify contour lines by removing co-linear vertices; default is
     *     to simplify
     * @param smooth whether to apply Bezier smooth to the contours; default is no smoothing
     * @param roi an optional polygonal {@code Geometry} to define the region of interest within
     *     which contours will be generated
     * @return the contours a feature collection where each feature contains a contour as a
     *     {@linkplain LineString} and the contour value as a {@code Double}
     */
    public static SimpleFeatureCollection process(
            GridCoverage2D gc2d,
            Integer band,
            double[] levels,
            Double interval,
            Boolean simplify,
            Boolean smooth,
            Geometry roi,
            ProgressListener progressListener)
            throws ProcessException {
        ContourProcess process = new ContourProcess();
        return process.execute(
                gc2d, band, levels, interval, simplify, smooth, roi, progressListener);
    }

    @DescribeResult(
            name = "result",
            description = "Contour line features.  Contour level is in value attribute.")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "data", description = "Input raster") GridCoverage2D gc2d,
            @DescribeParameter(
                            name = "band",
                            description =
                                    "Band number (zero base) to use for values to be contoured",
                            min = 0,
                            max = 1)
                    Integer band,
            @DescribeParameter(
                            name = "levels",
                            description = "Values of levels at which to generate contours")
                    double[] levels,
            @DescribeParameter(
                            name = "interval",
                            description =
                                    "Interval between contour values (ignored if levels parameter is supplied)",
                            min = 0,
                            minValue = 0)
                    Double interval,
            @DescribeParameter(
                            name = "simplify",
                            description = "Indicates whether contour lines are simplified",
                            min = 0)
                    Boolean simplify,
            @DescribeParameter(
                            name = "smooth",
                            description =
                                    "Indicates whether contour lines are smoothed using Bezier smoothing",
                            min = 0)
                    Boolean smooth,
            @DescribeParameter(
                            name = "roi",
                            description =
                                    "Geometry delineating the region of interest (in raster coordinate system)",
                            min = 0)
                    Geometry roi,
            ProgressListener progressListener)
            throws ProcessException {

        //
        // initial checks
        //
        if (gc2d == null) {
            throw new ProcessException("Invalid input, source grid coverage should be not null");
        }
        if (band != null && (band < 0 || band >= gc2d.getNumSampleDimensions())) {
            throw new ProcessException("Invalid input, invalid band number:" + band);
        }
        boolean hasValues = !(levels == null || levels.length == 0);
        if (!hasValues && interval == null) {
            throw new ProcessException("One between interval and values must be valid");
        }

        //
        // GRID TO WORLD preparation
        //
        final AffineTransform mt2D =
                (AffineTransform) gc2d.getGridGeometry().getGridToCRS2D(PixelOrientation.CENTER);

        // get the list of nodata, if any
        List<Object> noDataList = new ArrayList<>();
        NoDataContainer noDataProperty =
                org.geotools.coverage.util.CoverageUtilities.getNoDataProperty(gc2d);
        if (noDataProperty != null) {
            noDataList.add(noDataProperty.getAsSingleValue());
        }
        for (GridSampleDimension sd : gc2d.getSampleDimensions()) {
            // grab all the explicit nodata
            final double[] sdNoData = sd.getNoDataValues();
            if (sdNoData != null) {
                for (double nodata : sdNoData) {
                    noDataList.add(nodata);
                }
            }

            // handle also readers setting up nodata in a category with a specific name
            if (sd.getCategories() != null) {
                for (Category cat : sd.getCategories()) {
                    if (cat.getName().equals(NO_DATA)) {
                        final NumberRange<? extends Number> catRange = cat.getRange();
                        if (!Double.isNaN(catRange.getMinimum())) {
                            if (catRange.getMinimum() == catRange.getMaximum()) {
                                noDataList.add(catRange.getMinimum());
                            } else {
                                Range<Double> noData =
                                        new Range<>(
                                                catRange.getMinimum(),
                                                catRange.isMinIncluded(),
                                                catRange.getMaximum(),
                                                catRange.isMaxIncluded());
                                noDataList.add(noData);
                            }
                        }
                    }
                }
            }
        }

        // get the rendered image
        final RenderedImage raster = gc2d.getRenderedImage();

        // perform jai operation
        ParameterBlockJAI pb = new ParameterBlockJAI("Contour");
        pb.setSource("source0", raster);

        if (roi != null) {
            pb.setParameter("roi", CoverageUtilities.prepareROI(roi, mt2D));
        }

        if (band != null) {
            pb.setParameter("band", band);
        }

        if (levels != null && levels.length > 0) {
            final ArrayList<Double> elements = new ArrayList<>(levels.length);
            for (double level : levels) elements.add(level);
            pb.setParameter("levels", elements);
        } else {
            pb.setParameter("interval", interval);
        }

        if (simplify != null) {
            pb.setParameter("simplify", simplify);
        }

        if (smooth != null) {
            pb.setParameter("smooth", smooth);
        }

        if (!noDataList.isEmpty()) {
            pb.setParameter("nodata", noDataList);
        }

        final RenderedOp dest = JAI.create("Contour", pb);
        @SuppressWarnings("unchecked")
        final Collection<LineString> prop =
                (Collection<LineString>) dest.getProperty(ContourDescriptor.CONTOUR_PROPERTY_NAME);

        // wrap as a feature collection and return
        final SimpleFeatureType schema =
                CoverageUtilities.createFeatureType(gc2d, LineString.class);
        final SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
        int i = 0;
        final ListFeatureCollection featureCollection = new ListFeatureCollection(schema);
        final AffineTransformation jtsTransformation =
                new AffineTransformation(
                        mt2D.getScaleX(),
                        mt2D.getShearX(),
                        mt2D.getTranslateX(),
                        mt2D.getShearY(),
                        mt2D.getScaleY(),
                        mt2D.getTranslateY());
        for (LineString line : prop) {

            if (!line.isValid()) {
                continue;
            }
            // get value
            Double value = (Double) line.getUserData();
            line.setUserData(null);
            // filter coordinates in place
            line.apply(jtsTransformation);

            // create feature and add to list
            builder.set("the_geom", line);
            builder.set("value", value);

            featureCollection.add(builder.buildFeature(String.valueOf(i++)));
        }

        // return value

        return featureCollection;
    }
}
