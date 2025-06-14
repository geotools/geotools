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

import static javax.media.jai.JAI.KEY_INTERPOLATION;
import static org.geotools.coverage.util.FeatureUtilities.GRID_PROPERTY_NAME;
import static org.geotools.coverage.util.FeatureUtilities.PARAMS_PROPERTY_NAME;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.IOException;
import javax.media.jai.Interpolation;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.metadata.extent.GeographicBoundingBox;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.CoverageReadingTransformation;
import org.geotools.coverage.grid.io.CoverageReadingTransformation.ReaderAndParams;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.ClippingFeatureCollection;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;

/**
 * Helper class that transforms the input data via rendering transformations. Rolled out so that it can be shared among
 * {@link StreamingRenderer} and grid coverage direct rendering to {@link RenderedImage}
 */
public abstract class RenderingTransformationHelper {

    private static final FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    public static final int TRANSFORM_READ_BUFFER_PIXELS = 10;

    /**
     * If true, the transformation will read the coverage at a higher resolution than the native one, and then scale it
     * down to the desired resolution. This is useful when the transformation can benefit from receiving interpolated
     * data, as it will have more data to interpolate from. If false, the transformation will read the coverage at the
     * native resolution, and then scale it up to the desired resolution.
     */
    protected boolean oversample;

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
        boolean isRasterSource = false;
        if (featureSource instanceof SimpleFeatureSource) {
            SimpleFeatureType simpleSchema = (SimpleFeatureType) schema;
            if (FeatureUtilities.isWrappedCoverage(simpleSchema)
                    || FeatureUtilities.isWrappedCoverageReader(simpleSchema)) {
                isRasterSource = true;
                result = readFromRaster(
                        transformation, (SimpleFeatureSource) featureSource, renderingQuery, gridGeometry, hints);
            }
        }

        if (result == null && !isRasterSource) {
            // it's a transformation starting from vector data, see if we can optimize the query
            Query optimizedQuery = null;
            if (transformation instanceof RenderingTransformation) {
                RenderingTransformation tx = (RenderingTransformation) transformation;
                optimizedQuery = tx.invertQuery(renderingQuery, gridGeometry);
            }
            // if we could not find an optimized query no other choice but to just limit
            // ourselves to the bbox, we don't know if the transformation alters/adds attributes :-(
            if (optimizedQuery == null) {
                Envelope bounds =
                        (Envelope) renderingQuery.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, null);
                Filter bbox = new FastBBOX(filterFactory.property(""), bounds, filterFactory);
                optimizedQuery = new Query(null, bbox);
                optimizedQuery.setHints(layerQuery.getHints());
            }

            // grab the original features
            Query mixedQuery = DataUtilities.mixQueries(layerQuery, optimizedQuery, null);
            FeatureCollection originalFeatures = featureSource.getFeatures(mixedQuery);
            if (featureSource.getSupportedHints().contains(Hints.GEOMETRY_CLIP)
                    && originalFeatures instanceof SimpleFeatureCollection) {
                originalFeatures = new ClippingFeatureCollection((SimpleFeatureCollection) originalFeatures);
            }
            originalFeatures = RendererUtilities.fixFeatureCollectionReferencing(originalFeatures, sourceCrs);

            // transform them
            result = transformation.evaluate(originalFeatures);
        }

        return result;
    }

    private Object readFromRaster(
            Expression transformation,
            SimpleFeatureSource featureSource,
            Query renderingQuery,
            GridGeometry2D gridGeometry,
            RenderingHints hints)
            throws IOException, TransformException, FactoryException {
        // get the desired grid geometry
        GridGeometry2D readGG = gridGeometry;
        ReferencedEnvelope originalRendingEnvelope = readGG != null ? readGG.getEnvelope2D() : null;

        SimpleFeatureCollection sample = featureSource.getFeatures();
        Feature gridWrapper = DataUtilities.first(sample);

        final Interpolation interpolation = hints != null ? (Interpolation) hints.get(KEY_INTERPOLATION) : null;
        GridCoverage2D coverage = null;
        if (FeatureUtilities.isWrappedCoverageReader(featureSource.getSchema())) {
            GeneralParameterValue[] params = PARAMS_PROPERTY_NAME.evaluate(gridWrapper, GeneralParameterValue[].class);
            final GridCoverage2DReader reader = (GridCoverage2DReader) GRID_PROPERTY_NAME.evaluate(gridWrapper);

            // is the transformation going to do its own internal read?
            // then let it got and do its own thing
            if (transformation instanceof CoverageReadingTransformation) {
                ReaderAndParams context = new ReaderAndParams(reader, params);
                return transformation.evaluate(context);
            }

            // don't read more than the native resolution (in case we are oversampling)
            if (CRS.isEquivalent(reader.getCoordinateReferenceSystem(), gridGeometry.getCoordinateReferenceSystem())
                    && !oversample) {
                readGG = updateGridGeometryToNativeResolution(gridGeometry, reader, readGG);
            }

            if (transformation instanceof RenderingTransformation) {
                RenderingTransformation tx = (RenderingTransformation) transformation;

                Query invertQuery = new Query(renderingQuery);
                if (hints != null && hints.containsKey(KEY_INTERPOLATION))
                    invertQuery.getHints().put(KEY_INTERPOLATION, hints.get(KEY_INTERPOLATION));
                readGG = (GridGeometry2D) tx.invertGridGeometry(invertQuery, readGG);

                params = tx.customizeReadParams(reader, params);
            }

            if (params != null) {
                updateGridGeometryParam(params, readGG);
            }

            coverage = readCoverage(reader, params, readGG);
        } else {
            coverage = (GridCoverage2D) GRID_PROPERTY_NAME.evaluate(gridWrapper);
        }

        // readers will return null if there is no coverage in the area
        if (coverage != null) {
            if (readGG != null) {
                // Crop will fail if we try to crop outside of the coverage area
                ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(readGG.getEnvelope());
                CoordinateReferenceSystem coverageCRS = coverage.getCoordinateReferenceSystem2D();
                if (!CRS.isEquivalent(renderingEnvelope.getCoordinateReferenceSystem(), coverageCRS)) {
                    renderingEnvelope = renderingEnvelope.transform(coverageCRS, true);
                }

                // Check if this is a world-spanning projection - if so, we need to consider
                // dateline wrapping
                GeographicBoundingBox crsLatLonBoundingBox = CRS.getGeographicBoundingBox(coverageCRS);
                if (null == crsLatLonBoundingBox
                        || crsLatLonBoundingBox.getEastBoundLongitude() - crsLatLonBoundingBox.getWestBoundLongitude()
                                >= 360) {
                    // in this case, only crop if the rendering envelope is entirely inside
                    // the coverage
                    if (coverage.getEnvelope2D().contains((BoundingBox) renderingEnvelope)) {
                        coverage = cropCoverage(coverage, renderingEnvelope);
                    }
                } else {
                    if (coverage.getEnvelope2D().intersects((BoundingBox) renderingEnvelope)) {
                        // the resulting coverage might be larger than the readGG envelope,
                        // shall we crop it?
                        coverage = cropCoverage(coverage, renderingEnvelope);
                    } else {
                        coverage = null;
                    }
                }

                if (coverage != null) {
                    // we might also need to scale the coverage to the desired resolution
                    MathTransform2D coverageTx = readGG.getGridToCRS2D();
                    if (coverageTx instanceof AffineTransform) {
                        AffineTransform coverageAt = (AffineTransform) coverageTx;
                        AffineTransform renderingAt = (AffineTransform) gridGeometry.getGridToCRS2D();
                        // we adjust the scale only if we have many more pixels than
                        // required (30% or more)
                        final double ratioX = coverageAt.getScaleX() / renderingAt.getScaleX();
                        final double ratioY = coverageAt.getScaleY() / renderingAt.getScaleY();
                        if (ratioX < 0.7 && ratioY < 0.7) {
                            // resolution is too different
                            final ParameterValueGroup param =
                                    PROCESSOR.getOperation("Scale").getParameters();
                            param.parameter("Source").setValue(coverage);
                            param.parameter("xScale").setValue(ratioX);
                            param.parameter("yScale").setValue(ratioY);
                            if (interpolation != null) {
                                param.parameter("Interpolation").setValue(interpolation);
                            }

                            coverage = (GridCoverage2D) PROCESSOR.doOperation(param);
                        }
                    }
                }
            }

            if (transformation instanceof RenderingTransformation) {
                RenderingTransformation tx = (RenderingTransformation) transformation;
                if (tx.clipOnRenderingArea() && originalRendingEnvelope != null) {
                    // clip the coverage to the rendering area
                    coverage = cropCoverage(coverage, originalRendingEnvelope);
                }
            }

            if (coverage != null) {
                // apply the transformation
                return transformation.evaluate(coverage);
            }
        }
        return null;
    }

    private static void updateGridGeometryParam(GeneralParameterValue[] params, GridGeometry2D readGG) {
        for (GeneralParameterValue gp : params) {
            if (gp instanceof ParameterValue) {
                final ParameterValue param = (ParameterValue) gp;
                final ReferenceIdentifier name = param.getDescriptor().getName();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                    param.setValue(readGG);
                }
            }
        }
    }

    private static GridGeometry2D updateGridGeometryToNativeResolution(
            GridGeometry2D gridGeometry, GridCoverage2DReader reader, GridGeometry2D readGG) throws TransformException {
        // GEOS-8070, changed the pixel anchor to corner. CENTER can cause issues
        // with the BBOX calculation and cause it to be incorrect
        MathTransform g2w = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        if (g2w instanceof AffineTransform2D && readGG.getGridToCRS2D() instanceof AffineTransform2D) {
            AffineTransform2D atOriginal = (AffineTransform2D) g2w;
            AffineTransform2D atMap = (AffineTransform2D) readGG.getGridToCRS2D();
            if (XAffineTransform.getScale(atMap) < XAffineTransform.getScale(atOriginal)) {
                // we need to go trough some convoluted code to make sure the new
                // grid geometry has at least one pixel

                Bounds worldEnvelope = gridGeometry.getEnvelope();
                GeneralBounds transformed = CRS.transform(atOriginal.inverse(), worldEnvelope);
                int minx = (int) Math.floor(transformed.getMinimum(0));
                int miny = (int) Math.floor(transformed.getMinimum(1));
                int maxx = (int) Math.ceil(transformed.getMaximum(0));
                int maxy = (int) Math.ceil(transformed.getMaximum(1));
                Rectangle rect = new Rectangle(
                        minx - TRANSFORM_READ_BUFFER_PIXELS,
                        miny - TRANSFORM_READ_BUFFER_PIXELS,
                        maxx - minx + TRANSFORM_READ_BUFFER_PIXELS * 2,
                        maxy - miny + TRANSFORM_READ_BUFFER_PIXELS * 2);
                GridEnvelope2D gridEnvelope = new GridEnvelope2D(rect);
                readGG = new GridGeometry2D(
                        gridEnvelope,
                        PixelInCell.CELL_CORNER,
                        atOriginal,
                        worldEnvelope.getCoordinateReferenceSystem(),
                        null);
            }
        }
        return readGG;
    }

    private static GridCoverage2D cropCoverage(GridCoverage2D coverage, ReferencedEnvelope renderingEnvelope) {
        final ParameterValueGroup param = PROCESSOR.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(renderingEnvelope);
        coverage = (GridCoverage2D) PROCESSOR.doOperation(param);
        return coverage;
    }

    /** Subclasses will override and provide means to read the coverage */
    protected abstract GridCoverage2D readCoverage(GridCoverage2DReader reader, Object params, GridGeometry2D readGG)
            throws IOException;

    public void setOversampleEnabled(boolean oversample) {
        this.oversample = oversample;
    }
}
