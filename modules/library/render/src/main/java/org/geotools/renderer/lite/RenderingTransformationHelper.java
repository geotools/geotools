/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import static org.geotools.coverage.util.FeatureUtilities.GRID_PROPERTY_NAME;
import static org.geotools.coverage.util.FeatureUtilities.PARAMS_PROPERTY_NAME;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.IOException;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * Helper class that transforms the input data via rendering transformations. Rolled out so that it
 * can be shared among {@link StreamingRenderer} and grid coverage direct rendering to {@link
 * RenderedImage}
 */
public abstract class RenderingTransformationHelper {

    private static final FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    public static final int TRANSFORM_READ_BUFFER_PIXELS = 10;

    public Object applyRenderingTransformation(
            Expression transformation,
            FeatureSource featureSource,
            Query layerQuery,
            Query renderingQuery,
            GridGeometry2D gridGeometry,
            CoordinateReferenceSystem sourceCrs,
            RenderingHints hints)
            throws IOException, SchemaException, TransformException, FactoryException {
        Object result = null;

        // check if it's a wrapper coverage or a wrapped reader
        FeatureType schema = featureSource.getSchema();
        boolean isRasterData = false;
        if (schema instanceof SimpleFeatureType) {
            SimpleFeatureType simpleSchema = (SimpleFeatureType) schema;
            GridCoverage2D coverage = null;
            if (FeatureUtilities.isWrappedCoverage(simpleSchema)
                    || FeatureUtilities.isWrappedCoverageReader(simpleSchema)) {
                isRasterData = true;

                // get the desired grid geometry
                GridGeometry2D readGG = gridGeometry;
                if (transformation instanceof RenderingTransformation) {
                    RenderingTransformation tx = (RenderingTransformation) transformation;
                    readGG = (GridGeometry2D) tx.invertGridGeometry(renderingQuery, gridGeometry);
                }

                FeatureCollection<?, ?> sample = featureSource.getFeatures();
                Feature gridWrapper = DataUtilities.first(sample);

                if (FeatureUtilities.isWrappedCoverageReader(simpleSchema)) {
                    GeneralParameterValue[] params =
                            PARAMS_PROPERTY_NAME.evaluate(
                                    gridWrapper, GeneralParameterValue[].class);
                    final GridCoverage2DReader reader =
                            (GridCoverage2DReader) GRID_PROPERTY_NAME.evaluate(gridWrapper);
                    // don't read more than the native resolution (in case we are oversampling)
                    if (CRS.equalsIgnoreMetadata(
                            reader.getCoordinateReferenceSystem(),
                            gridGeometry.getCoordinateReferenceSystem())) {
                        // GEOS-8070, changed the pixel anchor to corner. CENTER can cause issues
                        // with
                        // the BBOX calculation and cause it to be incorrect
                        MathTransform g2w = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
                        if (g2w instanceof AffineTransform2D
                                && readGG.getGridToCRS2D() instanceof AffineTransform2D) {
                            AffineTransform2D atOriginal = (AffineTransform2D) g2w;
                            AffineTransform2D atMap = (AffineTransform2D) readGG.getGridToCRS2D();
                            if (XAffineTransform.getScale(atMap)
                                    < XAffineTransform.getScale(atOriginal)) {
                                // we need to go trough some convoluted code to make sure the new
                                // grid geometry
                                // has at least one pixel

                                org.opengis.geometry.Envelope worldEnvelope =
                                        gridGeometry.getEnvelope();
                                GeneralEnvelope transformed =
                                        org.geotools.referencing.CRS.transform(
                                                atOriginal.inverse(), worldEnvelope);
                                int minx = (int) Math.floor(transformed.getMinimum(0));
                                int miny = (int) Math.floor(transformed.getMinimum(1));
                                int maxx = (int) Math.ceil(transformed.getMaximum(0));
                                int maxy = (int) Math.ceil(transformed.getMaximum(1));
                                Rectangle rect =
                                        new Rectangle(
                                                minx - TRANSFORM_READ_BUFFER_PIXELS,
                                                miny - TRANSFORM_READ_BUFFER_PIXELS,
                                                (maxx - minx) + TRANSFORM_READ_BUFFER_PIXELS * 2,
                                                (maxy - miny) + TRANSFORM_READ_BUFFER_PIXELS * 2);
                                GridEnvelope2D gridEnvelope = new GridEnvelope2D(rect);
                                readGG =
                                        new GridGeometry2D(
                                                gridEnvelope,
                                                PixelInCell.CELL_CORNER,
                                                atOriginal,
                                                worldEnvelope.getCoordinateReferenceSystem(),
                                                null);
                            }
                        }
                    }
                    if (transformation instanceof RenderingTransformation) {
                        RenderingTransformation tx = (RenderingTransformation) transformation;
                        params = tx.customizeReadParams(reader, params);
                    }
                    coverage = readCoverage(reader, params, readGG);
                } else {
                    coverage = (GridCoverage2D) GRID_PROPERTY_NAME.evaluate(gridWrapper);
                }

                // readers will return null if there is no coverage in the area
                if (coverage != null) {
                    if (readGG != null) {
                        // Crop will fail if we try to crop outside of the coverage area
                        ReferencedEnvelope renderingEnvelope =
                                new ReferencedEnvelope(readGG.getEnvelope());
                        CoordinateReferenceSystem coverageCRS =
                                coverage.getCoordinateReferenceSystem2D();
                        if (!CRS.equalsIgnoreMetadata(
                                renderingEnvelope.getCoordinateReferenceSystem(), coverageCRS)) {
                            renderingEnvelope = renderingEnvelope.transform(coverageCRS, true);
                        }

                        // Check if this is a world-spanning projection - if so, we need to consider
                        // dateline wrapping
                        GeographicBoundingBox crsLatLonBoundingBox =
                                CRS.getGeographicBoundingBox(coverageCRS);
                        if (null == crsLatLonBoundingBox
                                || crsLatLonBoundingBox.getEastBoundLongitude()
                                                - crsLatLonBoundingBox.getWestBoundLongitude()
                                        >= 360) {
                            // in this case, only crop if the rendering envelope is entirely inside
                            // the coverage
                            if (coverage.getEnvelope2D().contains(renderingEnvelope)) {
                                final ParameterValueGroup param =
                                        PROCESSOR.getOperation("CoverageCrop").getParameters();
                                param.parameter("Source").setValue(coverage);
                                param.parameter("Envelope").setValue(renderingEnvelope);
                                coverage = (GridCoverage2D) PROCESSOR.doOperation(param);
                            }
                        } else {
                            if (coverage.getEnvelope2D().intersects(renderingEnvelope)) {
                                // the resulting coverage might be larger than the readGG envelope,
                                // shall we crop it?
                                final ParameterValueGroup param =
                                        PROCESSOR.getOperation("CoverageCrop").getParameters();
                                param.parameter("Source").setValue(coverage);
                                param.parameter("Envelope").setValue(renderingEnvelope);
                                coverage = (GridCoverage2D) PROCESSOR.doOperation(param);
                            } else {
                                coverage = null;
                            }
                        }

                        if (coverage != null) {
                            // we might also need to scale the coverage to the desired resolution
                            MathTransform2D coverageTx = readGG.getGridToCRS2D();
                            if (coverageTx instanceof AffineTransform) {
                                AffineTransform coverageAt = (AffineTransform) coverageTx;
                                AffineTransform renderingAt =
                                        (AffineTransform) gridGeometry.getGridToCRS2D();
                                // we adjust the scale only if we have many more pixels than
                                // required (30% or more)
                                final double ratioX =
                                        coverageAt.getScaleX() / renderingAt.getScaleX();
                                final double ratioY =
                                        coverageAt.getScaleY() / renderingAt.getScaleY();
                                if (ratioX < 0.7 && ratioY < 0.7) {
                                    // resolution is too different
                                    final ParameterValueGroup param =
                                            PROCESSOR.getOperation("Scale").getParameters();
                                    param.parameter("Source").setValue(coverage);
                                    param.parameter("xScale").setValue(ratioX);
                                    param.parameter("yScale").setValue(ratioY);
                                    final Interpolation interpolation =
                                            (Interpolation) hints.get(JAI.KEY_INTERPOLATION);
                                    if (interpolation != null) {
                                        param.parameter("Interpolation").setValue(interpolation);
                                    }

                                    coverage = (GridCoverage2D) PROCESSOR.doOperation(param);
                                }
                            }
                        }
                    }

                    if (coverage != null) {
                        // apply the transformation
                        result = transformation.evaluate(coverage);
                    } else {
                        result = null;
                    }
                }
            }
        }

        if (result == null && !isRasterData) {
            // it's a transformation starting from vector data, let's see if we can optimize the
            // query
            FeatureCollection originalFeatures;
            Query optimizedQuery = null;
            if (transformation instanceof RenderingTransformation) {
                RenderingTransformation tx = (RenderingTransformation) transformation;
                optimizedQuery = tx.invertQuery(renderingQuery, gridGeometry);
            }
            // if we could not find an optimized query no other choice but to just limit
            // ourselves to the bbox, we don't know if the transformation alters/adds attributes :-(
            if (optimizedQuery == null) {
                Envelope bounds =
                        (Envelope)
                                renderingQuery
                                        .getFilter()
                                        .accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, null);
                Filter bbox = new FastBBOX(filterFactory.property(""), bounds, filterFactory);
                optimizedQuery = new Query(null, bbox);
                optimizedQuery.setHints(layerQuery.getHints());
            }

            // grab the original features
            Query mixedQuery = DataUtilities.mixQueries(layerQuery, optimizedQuery, null);
            originalFeatures = featureSource.getFeatures(mixedQuery);
            originalFeatures =
                    RendererUtilities.fixFeatureCollectionReferencing(originalFeatures, sourceCrs);

            // transform them
            result = transformation.evaluate(originalFeatures);
        }

        return result;
    }

    /** Subclasses will override and provide means to read the coverage */
    protected abstract GridCoverage2D readCoverage(
            GridCoverage2DReader reader, Object params, GridGeometry2D readGG) throws IOException;
}
