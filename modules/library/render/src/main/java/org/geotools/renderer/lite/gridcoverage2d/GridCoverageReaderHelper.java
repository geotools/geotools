/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.ReadResolutionCalculator;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.EmptyIntersectionException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.renderer.crs.WrappingProjectionHandler;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * Support class that performs the actions needed to read a GridCoverage for the task of rendering
 * it at a given resolution, on a given area, taking into account projection oddities, dateline
 * crossing, and the like
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GridCoverageReaderHelper {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    private static final int DEFAULT_PADDING = 10;

    private static final Logger LOGGER = Logging.getLogger(GridCoverageReaderHelper.class);

    private GridCoverage2DReader reader;

    private ReferencedEnvelope mapExtent;

    private Rectangle mapRasterArea;

    private MathTransform worldToScreen;

    private GridGeometry2D requestedGridGeometry;

    private boolean paddingRequired;

    private boolean sameCRS;

    private int padding;

    public GridCoverageReaderHelper(
            GridCoverage2DReader reader,
            Rectangle mapRasterArea,
            ReferencedEnvelope mapExtent,
            Interpolation interpolation)
            throws FactoryException, IOException {
        this(reader, mapRasterArea, mapExtent, interpolation, null);
    }

    public GridCoverageReaderHelper(
            GridCoverage2DReader reader,
            Rectangle mapRasterArea,
            ReferencedEnvelope mapExtent,
            Interpolation interpolation,
            Hints hints)
            throws FactoryException, IOException {
        this.reader = reader;
        this.mapExtent = mapExtent;
        this.requestedGridGeometry =
                new GridGeometry2D(new GridEnvelope2D(mapRasterArea), mapExtent);
        this.worldToScreen = requestedGridGeometry.getCRSToGrid2D(PixelOrientation.UPPER_LEFT);
        this.padding = DEFAULT_PADDING;
        if (hints != null && hints.containsKey(GridCoverageRenderer.PADDING)) {
            padding = (int) hints.get(GridCoverageRenderer.PADDING);
        }
        // determine if we need a reading gutter, or not, we do if we are reprojecting, or if
        // there is an interpolation to be applied, in that case we need to expand the area
        // we are going to read
        sameCRS =
                CRS.equalsIgnoreMetadata(
                        mapExtent.getCoordinateReferenceSystem(),
                        reader.getCoordinateReferenceSystem());
        paddingRequired =
                (!sameCRS
                                || !(interpolation instanceof InterpolationNearest)
                                || isMultiCRSReader(reader))
                        && !isReprojectingReader(reader);
        if (paddingRequired) {
            // expand the map raster area
            GridEnvelope2D requestedGridEnvelope = new GridEnvelope2D(mapRasterArea);
            applyReadGutter(requestedGridEnvelope);

            // now create the final envelope accordingly
            try {
                this.requestedGridGeometry =
                        new GridGeometry2D(
                                requestedGridEnvelope,
                                PixelInCell.CELL_CORNER,
                                worldToScreen.inverse(),
                                mapExtent.getCoordinateReferenceSystem(),
                                null);
                this.mapExtent =
                        ReferencedEnvelope.reference(requestedGridGeometry.getEnvelope2D());
                this.mapRasterArea = requestedGridGeometry.getGridRange2D().getBounds();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.mapExtent = mapExtent;
            this.mapRasterArea = mapRasterArea;
        }
    }

    /**
     * Returns true if the reader is a reprojecting one, that is, one that can handle the coverage
     * reprojection on its own
     */
    public static boolean isReprojectingReader(GridCoverage2DReader reader) throws IOException {
        return "true".equals(reader.getMetadataValue(GridCoverage2DReader.REPROJECTING_READER));
    }

    /**
     * Returns true if the reader is advertising a single CRS, cannot fully perform a reproject to
     * any target CRS, but internally is working with several CRSs and could use some extra padding
     * on the requests
     */
    boolean isMultiCRSReader(GridCoverage2DReader reader) throws IOException {
        return "true".equals(reader.getMetadataValue(GridCoverage2DReader.MULTICRS_READER));
    }

    public ReferencedEnvelope getReadEnvelope() {
        return mapExtent;
    }

    private void applyReadGutter(GridEnvelope2D gridRange) {
        gridRange.setBounds(
                gridRange.x - padding,
                gridRange.y - padding,
                gridRange.width + padding * 2,
                gridRange.height + padding * 2);
    }

    private GridGeometry2D applyReadGutter(GridGeometry2D gg) {
        MathTransform gridToCRS = gg.getGridToCRS();
        GridEnvelope2D range = new GridEnvelope2D(gg.getGridRange2D());
        applyReadGutter(range);
        CoordinateReferenceSystem crs = gg.getEnvelope2D().getCoordinateReferenceSystem();
        GridGeometry2D result =
                new GridGeometry2D(range, PixelInCell.CELL_CORNER, gridToCRS, crs, null);

        return result;
    }

    /**
     * Reads a single coverage for the area specified in the constructor, the code will not attempt
     * multiple reads to manage reads across the date line, reducing the read area, splitting it
     * into parts to manage certain projections (e.g., conic) and so on
     */
    public GridCoverage2D readCoverage(final GeneralParameterValue[] params) throws IOException {
        return readSingleCoverage(params, requestedGridGeometry);
    }

    /**
     * Reads the data taking into account advanced projection handling in order to deal with date
     * line crossing, poles and other projection trouble areas. The result is a set of coverages
     * that can be either painted or reprojected safely
     */
    public List<GridCoverage2D> readCoverages(
            final GeneralParameterValue[] readParams, ProjectionHandler handler)
            throws IOException, FactoryException, TransformException {
        return readCoverages(readParams, handler, new GridCoverageFactory());
    }

    /**
     * Reads the data taking into account advanced projection handling in order to deal with date
     * line crossing, poles and other projection trouble areas. The result is a set of coverages
     * that can be either painted or reprojected safely
     */
    public List<GridCoverage2D> readCoverages(
            final GeneralParameterValue[] readParams,
            ProjectionHandler handler,
            GridCoverageFactory gridCoverageFactory)
            throws IOException, FactoryException, TransformException {
        if (handler == null) {
            GridCoverage2D readCoverage = readCoverage(readParams);
            GridCoverage2D cropped = cropCoverageOnRequestedEnvelope(readCoverage);
            if (cropped == null) {
                return Collections.emptyList();
            } else {
                return Arrays.asList(cropped);
            }
        }

        // get the areas that we are likely to have to read, and have the projection
        // handler also cut them
        List<GridCoverage2D> coverages = new ArrayList<GridCoverage2D>();
        List<ReferencedEnvelope> queryEnvelopes = handler.getQueryEnvelopes();
        for (ReferencedEnvelope envelope : queryEnvelopes) {
            List<GridCoverage2D> readCoverages =
                    readCoverageInEnvelope(envelope, readParams, handler, paddingRequired);
            if (readCoverages != null) {
                coverages.addAll(readCoverages);
            }
        }

        // it is not uncommon to find rasters whose coordinates are in the 0-360 range,
        // if that's the case, see if we can perform extra reads
        SingleCRS readerCRS = CRS.getHorizontalCRS(reader.getCoordinateReferenceSystem());
        if (readerCRS instanceof GeographicCRS) {
            ReferencedEnvelope readerEnvelope =
                    ReferencedEnvelope.reference(reader.getOriginalEnvelope());
            boolean northEast = CRS.getAxisOrder(readerCRS) == AxisOrder.NORTH_EAST;
            int lonAxis = northEast ? 1 : 0;
            if (readerEnvelope.getMaximum(lonAxis) > 180) {
                ReferencedEnvelope excess;
                double tx, ty;
                if (northEast) {
                    excess = new ReferencedEnvelope(-90, 90, 180, 360, readerCRS);
                    tx = 0;
                    ty = 360;
                } else {
                    excess = new ReferencedEnvelope(180, 360, -90, 90, readerCRS);
                    tx = 360;
                    ty = 0;
                }

                for (ReferencedEnvelope envelope : queryEnvelopes) {
                    // try to translate into the the excess area, and intersect
                    ReferencedEnvelope translated = new ReferencedEnvelope(envelope);
                    translated.translate(tx, ty);
                    ReferencedEnvelope intersection =
                            new ReferencedEnvelope(
                                    translated.intersection(excess),
                                    translated.getCoordinateReferenceSystem());
                    boolean isEmptyEnvelope =
                            intersection == null
                                    || intersection.isNull()
                                    || intersection.getHeight() == 0
                                    || intersection.getWidth() == 0;
                    if (isEmptyEnvelope) {
                        continue;
                    }
                    List<GridCoverage2D> readCoverages =
                            readCoverageInEnvelope(intersection, readParams, handler, false);
                    if (readCoverages != null) {
                        for (GridCoverage2D gc : readCoverages) {
                            GridCoverage2D displaced =
                                    GridCoverageRendererUtilities.displace(
                                            gc, -tx, -ty, gridCoverageFactory);
                            // add only if the already read bits of the source file
                            // do not contain the new one
                            if (!coveragesContainArea(coverages, displaced)) {
                                coverages.add(displaced);
                            }
                        }
                    }
                }
            }
        }

        return coverages;
    }

    /** Checks if any coverage in the list already fully contains the area of the test coverage */
    private boolean coveragesContainArea(List<GridCoverage2D> coverages, GridCoverage2D test) {
        for (GridCoverage2D coverage : coverages) {
            if (coverage.getEnvelope2D().contains((BoundingBox) test.getEnvelope2D())) {
                return true;
            }
        }
        return false;
    }

    private GridCoverage2D cropCoverageOnRequestedEnvelope(GridCoverage2D readCoverage) {
        if (readCoverage == null) {
            return null;
        }
        try {
            ReferencedEnvelope requested =
                    ReferencedEnvelope.reference(requestedGridGeometry.getEnvelope());
            ReferencedEnvelope requestedNativeCRS =
                    requested.transform(readCoverage.getCoordinateReferenceSystem(), true);

            ReferencedEnvelope coverageEnvelope =
                    ReferencedEnvelope.reference(readCoverage.getEnvelope());
            ReferencedEnvelope cropEnvelope =
                    new ReferencedEnvelope(
                            requestedNativeCRS.intersection(coverageEnvelope),
                            readCoverage.getCoordinateReferenceSystem());
            if (isNotEmpty(cropEnvelope)) {
                GridCoverage2D cropCoverage = cropCoverage(readCoverage, requestedNativeCRS);
                return cropCoverage;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.log(
                    Level.FINE,
                    "Failed to crop coverage on the requested area, using the original one",
                    e);
            return readCoverage;
        }
    }

    List<GridCoverage2D> readCoverageInEnvelope(
            ReferencedEnvelope envelope,
            GeneralParameterValue[] readParams,
            ProjectionHandler handler,
            boolean paddingRequired)
            throws TransformException, FactoryException, IOException {
        Polygon polygon = JTS.toGeometry(envelope);
        CoordinateReferenceSystem readerCRS = reader.getCoordinateReferenceSystem();

        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(mapRasterArea), mapExtent);
        GridGeometry2D readingGridGeometry =
                computeReadingGeometry(gg, readerCRS, polygon, handler, readParams);

        if (readingGridGeometry == null) {
            return null;
        }
        if (paddingRequired) {
            readingGridGeometry = applyReadGutter(readingGridGeometry);
        }
        GridCoverage2D coverage = readSingleCoverage(readParams, readingGridGeometry);
        if (coverage == null) {
            return null;
        }

        // cut and slice the geometry as required by the projection handler
        ReferencedEnvelope readingEnvelope =
                ReferencedEnvelope.reference(readingGridGeometry.getEnvelope2D());
        ReferencedEnvelope coverageEnvelope =
                ReferencedEnvelope.reference(coverage.getEnvelope2D());
        Polygon coverageFootprint = JTS.toGeometry(coverageEnvelope);
        Geometry preProcessed = handler.preProcess(coverageFootprint);
        if (preProcessed != null && !preProcessed.isEmpty()) {
            if (coverageFootprint.equals(preProcessed)) {
                // we might still have read more than requested
                if (!readingEnvelope.contains((Envelope) coverageEnvelope)) {
                    ReferencedEnvelope cropEnvelope =
                            new ReferencedEnvelope(
                                    readingEnvelope.intersection(coverageEnvelope), readerCRS);
                    GridCoverage2D cropped = cropCoverage(coverage, cropEnvelope);
                    return singleton(cropped);
                } else {
                    return singleton(coverage);
                }
            } else {
                final List<Polygon> polygons = PolygonExtractor.INSTANCE.getPolygons(preProcessed);
                final List<GridCoverage2D> coverages = new ArrayList<>();
                for (Polygon p : polygons) {
                    ReferencedEnvelope cropEnvelope =
                            new ReferencedEnvelope(p.getEnvelopeInternal(), readerCRS);
                    cropEnvelope =
                            new ReferencedEnvelope(
                                    cropEnvelope.intersection(coverageEnvelope), readerCRS);
                    cropEnvelope =
                            new ReferencedEnvelope(
                                    cropEnvelope.intersection(readingEnvelope), readerCRS);
                    GridCoverage2D cropped = cropCoverage(coverage, cropEnvelope);
                    if (cropped != null) {
                        coverages.add(cropped);
                    }
                }
                return coverages;
            }
        }

        return null;
    }

    private List<GridCoverage2D> singleton(GridCoverage2D coverage) {
        if (coverage == null) {
            return null;
        } else {
            return Collections.singletonList(coverage);
        }
    }

    private boolean isNotEmpty(ReferencedEnvelope envelope) {
        return !envelope.isEmpty()
                && !envelope.isNull()
                && envelope.getWidth() > 0
                && envelope.getHeight() > 0;
    }

    private GridCoverage2D cropCoverage(GridCoverage2D coverage, ReferencedEnvelope cropEnvelope) {
        if (isNotEmpty(cropEnvelope)) {
            final ParameterValueGroup param =
                    PROCESSOR.getOperation("CoverageCrop").getParameters();
            param.parameter("Source").setValue(coverage);
            param.parameter("Envelope").setValue(cropEnvelope);

            try {
                GridCoverage2D cropped = (GridCoverage2D) PROCESSOR.doOperation(param);
                return cropped;
            } catch (EmptyIntersectionException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private GridGeometry2D computeReadingGeometry(
            GridGeometry2D gg,
            CoordinateReferenceSystem readerCRS,
            Polygon polygon,
            ProjectionHandler handler,
            GeneralParameterValue[] readParams)
            throws TransformException, FactoryException, IOException {
        GridGeometry2D readingGridGeometry;
        MathTransform2D crsToGrid2D = gg.getCRSToGrid2D();
        MathTransform2D gridToCRS2D = gg.getGridToCRS2D();
        if (sameCRS) {
            Envelope gridEnvelope = JTS.transform(polygon, crsToGrid2D).getEnvelopeInternal();
            GridEnvelope2D gridRange =
                    new GridEnvelope2D(
                            (int) gridEnvelope.getMinX(),
                            (int) gridEnvelope.getMinY(),
                            (int) Math.round(gridEnvelope.getWidth()),
                            (int) Math.round(gridEnvelope.getHeight()));
            readingGridGeometry = new GridGeometry2D(gridRange, gridToCRS2D, readerCRS);
        } else {
            ReferencedEnvelope readEnvelope =
                    new ReferencedEnvelope(polygon.getEnvelopeInternal(), readerCRS);
            // while we want to read as much data as possible, and cut it only later
            // to avoid warping edge effects later, the resolution needs to be
            // computed against an area that's sane for the projection at hand
            ReferencedEnvelope reducedEnvelope = reduceEnvelope(readEnvelope, handler);
            if (reducedEnvelope == null) {
                return null;
            }
            ReferencedEnvelope reducedEnvelopeInRequestedCRS =
                    reducedEnvelope.transform(
                            requestedGridGeometry.getCoordinateReferenceSystem(), true);
            ReferencedEnvelope gridEnvelope =
                    ReferencedEnvelope.reference(
                            CRS.transform(crsToGrid2D, reducedEnvelopeInRequestedCRS));
            GridEnvelope2D readingGridRange =
                    new GridEnvelope2D(
                            (int) gridEnvelope.getMinX(),
                            (int) gridEnvelope.getMinY(),
                            (int) gridEnvelope.getWidth(),
                            (int) gridEnvelope.getHeight());
            GridGeometry2D localGridGeometry =
                    new GridGeometry2D(
                            readingGridRange,
                            gridToCRS2D,
                            mapExtent.getCoordinateReferenceSystem());

            double[][] resolutionLevels = reader.getResolutionLevels();
            ReadResolutionCalculator calculator =
                    new ReadResolutionCalculator(
                            localGridGeometry,
                            readerCRS,
                            resolutionLevels != null ? resolutionLevels[0] : null);
            final String name = "Accurate resolution computation";
            boolean accurateResolution = true;
            if (readParams != null) {
                for (GeneralParameterValue gParam : readParams) {
                    if (gParam != null
                            && name.equalsIgnoreCase(gParam.getDescriptor().getName().toString())) {
                        if (gParam instanceof ParameterValue<?>) {
                            final ParameterValue<?> param = (ParameterValue<?>) gParam;
                            final Object value = param.getValue();
                            if (value != null) {
                                accurateResolution = (Boolean) value;
                            }
                        }
                        break;
                    }
                }
            }
            calculator.setAccurateResolution(
                    accurateResolution && isAccurateResolutionComputationSafe(readEnvelope));
            double[] readResolution = calculator.computeRequestedResolution(reducedEnvelope);
            int width =
                    (int)
                            Math.max(
                                    1,
                                    Math.round(
                                            readEnvelope.getWidth() / Math.abs(readResolution[0])));
            int height =
                    (int)
                            Math.max(
                                    1,
                                    Math.round(
                                            readEnvelope.getHeight()
                                                    / Math.abs(readResolution[1])));
            GridEnvelope2D gridRange = new GridEnvelope2D(0, 0, width, height);
            readingGridGeometry = new GridGeometry2D(gridRange, readEnvelope);
        }
        return readingGridGeometry;
    }

    boolean isAccurateResolutionComputationSafe(ReferencedEnvelope readEnvelope)
            throws MismatchedDimensionException, FactoryException, TransformException {
        // accurate resolution computation depends on reprojection working, we need
        // to make sure the read envelope is sane for the source data at hand
        CoordinateReferenceSystem readCRS = readEnvelope.getCoordinateReferenceSystem();
        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        new ReferencedEnvelope(readCRS), DefaultGeographicCRS.WGS84, true);
        if (handler != null) {
            // if there are no limits or the projection is periodic, assume it's fine to read
            // whatever
            if (handler.getValidAreaBounds() == null
                    || handler instanceof WrappingProjectionHandler) {
                return true;
            }
            // in this case we need to make sure the area is actually safe to perform reprojections
            // on
            try {
                // when assertions are enabled accuracy tests might fail this path
                ReferencedEnvelope validBounds =
                        handler.getValidAreaBounds().transform(readCRS, true);
                return validBounds.contains((Envelope) readEnvelope);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private ReferencedEnvelope reduceEnvelope(
            ReferencedEnvelope envelope, ProjectionHandler handler)
            throws TransformException, FactoryException {
        Polygon polygon = JTS.toGeometry(envelope);
        Geometry geom = handler.preProcess(polygon);
        if (geom == null) {
            return null;
        }
        PolygonExtractor pe = new PolygonExtractor();
        Polygon largest = null;
        for (Polygon p : pe.getPolygons(geom)) {
            if (largest == null || largest.getArea() > p.getArea()) {
                largest = p;
            }
        }

        ReferencedEnvelope reduced =
                new ReferencedEnvelope(
                        largest.getEnvelopeInternal(), envelope.getCoordinateReferenceSystem());
        return reduced;
    }

    /**
     * Reads a single coverage given the specified read parameters and the grid geometry
     *
     * @param readParams (might be null)
     */
    GridCoverage2D readSingleCoverage(GeneralParameterValue[] readParams, GridGeometry2D gg)
            throws IOException {
        // setup the grid geometry param that will be passed to the reader
        final Parameter<GridGeometry2D> readGGParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        readGGParam.setValue(new GridGeometry2D(gg));

        // then I try to get read parameters associated with this
        // coverage if there are any.
        GridCoverage2D coverage = null;
        if (readParams != null) {
            // //
            //
            // Getting parameters to control how to read this coverage.
            // Remember to check to actually have them before forwarding
            // them to the reader.
            //
            // //
            final int length = readParams.length;
            if (length > 0) {
                // we have a valid number of parameters, let's check if
                // also have a READ_GRIDGEOMETRY2D. In such case we just
                // override it with the one we just build for this
                // request.
                final String name = AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString();
                int i = 0;
                for (; i < length; i++) {
                    if (readParams[i].getDescriptor().getName().toString().equalsIgnoreCase(name))
                        break;
                }
                // did we find anything?
                if (i < length) {
                    // we found another READ_GRIDGEOMETRY2D, let's override it.
                    readParams[i] = readGGParam;
                    coverage = reader.read(readParams);
                } else {
                    // add the correct read geometry to the supplied
                    // params since we did not find anything
                    GeneralParameterValue[] readParams2 = new GeneralParameterValue[length + 1];
                    System.arraycopy(readParams, 0, readParams2, 0, length);
                    readParams2[length] = readGGParam;
                    coverage = reader.read(readParams2);
                }
            } else
                // we have no parameters hence we just use the read grid
                // geometry to get a coverage
                coverage = reader.read(new GeneralParameterValue[] {readGGParam});
        } else if (gg != null) {
            coverage = reader.read(new GeneralParameterValue[] {readGGParam});
        } else {
            coverage = reader.read(null);
        }

        // try to crop on the requested area

        return coverage;
    }
}
