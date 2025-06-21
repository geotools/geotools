/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    NOTICE REGARDING STATUS AS PUBLIC DOMAIN WORK AND ABSENCE OF ANY WARRANTIES
 *
 *    The work (source code) was prepared by an officer or employee of the
 *    United States Government as part of that person's official duties, thus
 *    it is a "work of the U.S. Government," which is in the public domain and
 *    not elegible for copyright protection.  See, 17 U.S.C. § 105.  No warranty
 *    of any kind is given regarding the work.
 */

package org.geotools.process.vector;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.util.ProgressListener;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.NullProgressListener;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.util.SimpleInternationalString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * A Process to rasterize vector features in an input FeatureCollection.
 *
 * <p>A feature attribute is specified from which to extract the numeric values that will be written to the output grid
 * coverage. At present only int or float values are written to the output grid coverage. If the attribute is of type
 * Long it will be coerced to int values and a warning will be logged. Similarly if the attribute is of type Double it
 * will be coerced to float and a warning logged.
 *
 * @author Steve Ansari, NOAA
 * @author Michael Bedward
 * @since 2.6
 * @version $Id$
 */
@DescribeProcess(
        title = "Transform",
        description =
                "Converts some or all of a feature collection to a raster grid, using an attribute to specify cell values.")
public class VectorToRasterProcess implements VectorProcess {

    private static final int COORD_GRID_CHUNK_SIZE = 1000;

    private static enum TransferType {
        INTEGRAL,
        FLOAT
    }

    private TransferType transferType;

    private static enum ValueSource {
        PROPERTY_NAME,
        EXPRESSION
    }

    private ValueSource valueSource;

    GridCoverage2D result;
    private Number minAttValue;
    private Number maxAttValue;

    private ReferencedEnvelope extent;
    private Geometry extentGeometry;
    private GridGeometry2D gridGeom;

    private boolean transformFeatures;
    private MathTransform featureToRasterTransform;

    private int[] coordGridX = new int[COORD_GRID_CHUNK_SIZE];
    private int[] coordGridY = new int[COORD_GRID_CHUNK_SIZE];

    TiledImage image;
    Graphics2D graphics;

    /**
     * A static helper method that can be called directy to run the process.
     *
     * <p>The process interface is useful for advertising functionality to dynamic applications, but for 'hands on'
     * coding this method is much more convenient than working via the {@linkplain org.geotools.process.Process#execute
     * }.
     *
     * @param features the feature collection to be (wholly or partially) rasterized
     * @param attribute source of values for the output grid: either a {@code String} for the name of a numeric feature
     *     property or an {@code org.geotools.api.filter.expression.Expression} that evaluates to a numeric value
     * @param gridDim size of the output raster
     * @param bounds bounds (world coordinates) of the output raster
     * @param covName a name for the output raster
     * @param monitor an optional {@code ProgressListener} (may be {@code null}
     * @return a new grid coverage
     * @throws org.geotools.process.raster.VectorToRasterException
     */
    public static GridCoverage2D process(
            SimpleFeatureCollection features,
            Object attribute,
            Dimension gridDim,
            Bounds bounds,
            String covName,
            ProgressListener monitor)
            throws VectorToRasterException {

        VectorToRasterProcess process = new VectorToRasterProcess();
        return process.convert(features, attribute, gridDim, bounds, covName, monitor);
    }

    @DescribeResult(name = "result", description = "Rasterized grid")
    public GridCoverage2D execute(
            @DescribeParameter(name = "features", description = "Features to process", min = 1, max = 1)
                    SimpleFeatureCollection features,
            @DescribeParameter(
                            name = "rasterWidth",
                            description = "Width of the output grid in pixels",
                            min = 1,
                            max = 1,
                            minValue = 1)
                    Integer rasterWidth,
            @DescribeParameter(
                            name = "rasterHeight",
                            description = "Height of the output grid in pixels",
                            min = 1,
                            max = 1,
                            minValue = 1)
                    Integer rasterHeight,
            @DescribeParameter(
                            name = "title",
                            description = "Title to use for the output grid",
                            min = 0,
                            max = 1,
                            defaultValue = "raster")
                    String title,
            @DescribeParameter(
                            name = "attribute",
                            description = "Attribute name to use for the raster cell values",
                            min = 1,
                            max = 1)
                    String attribute,
            @DescribeParameter(name = "bounds", description = "Bounding box of the area to rasterize", min = 0, max = 1)
                    Bounds bounds,
            ProgressListener progressListener) {

        Expression attributeExpr = null;
        try {
            attributeExpr = ECQL.toExpression(attribute);
        } catch (CQLException e) {
            throw new VectorToRasterException(e);
        }
        return convert(
                features, attributeExpr, new Dimension(rasterWidth, rasterHeight), bounds, title, progressListener);
    }

    /**
     * This method is called by {@linkplain #execute} to rasterize an individual feature.
     *
     * @param feature the feature to be rasterized
     * @throws java.lang.Exception
     */
    protected void processFeature(SimpleFeature feature, Object attribute) throws Exception {

        Geometry geometry = (Geometry) feature.getDefaultGeometry();

        if (geometry.intersects(extentGeometry)) {

            Number value = getFeatureValue(feature, attribute);
            switch (transferType) {
                case FLOAT:
                    if (minAttValue == null) {
                        minAttValue = maxAttValue = Float.valueOf(value.floatValue());
                    } else if (Float.compare(value.floatValue(), minAttValue.floatValue()) < 0) {
                        minAttValue = value.floatValue();
                    } else if (Float.compare(value.floatValue(), maxAttValue.floatValue()) > 0) {
                        maxAttValue = value.floatValue();
                    }

                    break;

                case INTEGRAL:
                    if (minAttValue == null) {
                        minAttValue = maxAttValue = Integer.valueOf(value.intValue());
                    } else if (value.intValue() < minAttValue.intValue()) {
                        minAttValue = value.intValue();
                    } else if (value.intValue() > maxAttValue.intValue()) {
                        maxAttValue = value.intValue();
                    }

                    break;
            }

            graphics.setColor(valueToColor(value));

            Geometries geomType = Geometries.get(geometry);
            switch (geomType) {
                case MULTIPOLYGON:
                case MULTILINESTRING:
                case MULTIPOINT:
                    final int numGeom = geometry.getNumGeometries();
                    for (int i = 0; i < numGeom; i++) {
                        Geometry geomN = geometry.getGeometryN(i);
                        drawGeometry(Geometries.get(geomN), geomN);
                    }
                    break;

                case POLYGON:
                case LINESTRING:
                case POINT:
                    drawGeometry(geomType, geometry);
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported geometry type: " + geomType.getName());
            }
        }
    }

    private Number getFeatureValue(SimpleFeature feature, Object attribute) {
        Class<? extends Number> rtnType = transferType == TransferType.FLOAT ? Float.class : Integer.class;
        if (valueSource == ValueSource.PROPERTY_NAME) {
            return rtnType.cast(feature.getAttribute((String) attribute));
        } else {
            return ((Expression) attribute).evaluate(feature, rtnType);
        }
    }

    private GridCoverage2D convert(
            SimpleFeatureCollection features,
            Object attribute,
            Dimension gridDim,
            Bounds bounds,
            String covName,
            ProgressListener monitor)
            throws VectorToRasterException {

        if (monitor == null) {
            monitor = new NullProgressListener();
        }

        initialize(features, bounds, attribute, gridDim);

        monitor.setTask(new SimpleInternationalString("Rasterizing features..."));

        float scale = 100.0f / features.size();
        monitor.started();

        try (SimpleFeatureIterator fi = features.features()) {
            int counter = 0;
            while (fi.hasNext()) {
                try {
                    processFeature(fi.next(), attribute);
                } catch (Exception e) {
                    monitor.exceptionOccurred(e);
                }

                monitor.progress(scale * counter++);
            }
        }
        monitor.complete();

        flattenImage();

        GridCoverageFactory gcf = new GridCoverageFactory();
        return gcf.create(covName, image, extent);
    }

    private void initialize(SimpleFeatureCollection features, Bounds bounds, Object attribute, Dimension gridDim)
            throws VectorToRasterException {

        // check the attribute argument
        if (attribute instanceof String) {
            String propName = (String) attribute;
            AttributeDescriptor attDesc = features.getSchema().getDescriptor(propName);
            if (attDesc == null) {
                throw new VectorToRasterException(propName + " not found");
            }

            Class<?> attClass = attDesc.getType().getBinding();
            if (!Number.class.isAssignableFrom(attClass)) {
                throw new VectorToRasterException(propName + " is not numeric");
            }

            if (Float.class.isAssignableFrom(attClass)) {
                transferType = TransferType.FLOAT;

            } else if (Double.class.isAssignableFrom(attClass)) {
                transferType = TransferType.FLOAT;
                Logger.getLogger(VectorToRasterProcess.class.getName())
                        .log(Level.WARNING, "coercing double feature values to float raster values");

            } else if (Long.class.isAssignableFrom(attClass)) {
                transferType = TransferType.INTEGRAL;
                Logger.getLogger(VectorToRasterProcess.class.getName())
                        .log(Level.WARNING, "coercing long feature values to int raster values");

            } else {
                transferType = TransferType.INTEGRAL;
            }

            valueSource = ValueSource.PROPERTY_NAME;

        } else if (attribute instanceof Expression) {
            valueSource = ValueSource.EXPRESSION;

            SimpleFeature feature = DataUtilities.first(features);
            Object value = ((Expression) attribute).evaluate(feature);

            // if the expression evaluates to a string check if the
            // value can be cast to a Number
            if (value.getClass().equals(String.class)) {
                Boolean hasException = false;
                try {
                    Integer.valueOf((String) value);
                    transferType = TransferType.INTEGRAL;
                } catch (NumberFormatException e) {
                    hasException = true;
                }
                if (hasException) {
                    hasException = false;
                    try {
                        Float.valueOf((String) value);
                        transferType = TransferType.FLOAT;
                    } catch (NumberFormatException e) {
                        hasException = true;
                    }
                }
                if (hasException) {
                    throw new VectorToRasterException(attribute.toString() + " does not evaluate to a number");
                }
            } else if (!Number.class.isAssignableFrom(value.getClass())) {
                throw new VectorToRasterException(attribute.toString() + " does not evaluate to a number");
            } else if (Float.class.isAssignableFrom(value.getClass())) {
                transferType = TransferType.FLOAT;
            } else if (Double.class.isAssignableFrom(value.getClass())) {
                transferType = TransferType.FLOAT;
                Logger.getLogger(VectorToRasterProcess.class.getName())
                        .log(Level.WARNING, "coercing double feature values to float raster values");
            } else if (Long.class.isAssignableFrom(value.getClass())) {
                transferType = TransferType.INTEGRAL;
                Logger.getLogger(VectorToRasterProcess.class.getName())
                        .log(Level.WARNING, "coercing long feature values to int raster values");
            } else {
                transferType = TransferType.INTEGRAL;
            }

        } else {
            throw new VectorToRasterException("value attribute must be a feature property name"
                    + "or an org.geotools.api.filter.expression.Expression object");
        }

        minAttValue = maxAttValue = null;

        try {
            setBounds(features, bounds);
        } catch (TransformException ex) {
            throw new VectorToRasterException(ex);
        }

        createImage(gridDim);

        gridGeom = new GridGeometry2D(new GridEnvelope2D(0, 0, gridDim.width, gridDim.height), extent);
    }

    /**
     * Sets the output coverage bounds and checks whether features need to be transformed into the output CRS.
     *
     * @throws org.geotools.process.raster.VectorToRasterException
     */
    private void setBounds(SimpleFeatureCollection features, Bounds bounds) throws TransformException {

        ReferencedEnvelope featureBounds = features.getBounds();

        if (bounds == null) {
            extent = featureBounds;

        } else {
            extent = new ReferencedEnvelope(bounds);
        }

        extentGeometry = new GeometryFactory().toGeometry(extent);

        // Compare the CRS of faetures and requested output bounds. If they
        // are different (and both non-null) flag that we need to transform
        // features to the output CRS prior to rasterizing them.

        CoordinateReferenceSystem featuresCRS = featureBounds.getCoordinateReferenceSystem();
        CoordinateReferenceSystem boundsCRS = extent.getCoordinateReferenceSystem();

        transformFeatures = false;
        if (featuresCRS != null && boundsCRS != null && !CRS.equalsIgnoreMetadata(boundsCRS, featuresCRS)) {

            try {
                featureToRasterTransform = CRS.findMathTransform(featuresCRS, boundsCRS, true);

            } catch (Exception ex) {
                throw new TransformException(
                        "Unable to transform features into output coordinate reference system", ex);
            }

            transformFeatures = true;
        }
    }

    /**
     * Create the tiled image and the associated graphics object that we will be used to draw the vector features into a
     * raster.
     *
     * <p>Note, the graphics objects will be an instance of TiledImageGraphics which is a sub-class of Graphics2D.
     */
    private void createImage(Dimension gridDim) {

        ColorModel cm = ColorModel.getRGBdefault();
        SampleModel sm = cm.createCompatibleSampleModel(gridDim.width, gridDim.height);

        image = new TiledImage(0, 0, gridDim.width, gridDim.height, 0, 0, sm, cm);
        graphics = image.createGraphics();
        graphics.setPaintMode();
        graphics.setComposite(AlphaComposite.Src);
    }

    /**
     * Takes the 4-band ARGB image that we have been drawing into and converts it to a single-band image.
     *
     * @todo There is probably a much easier / faster way to do this that still takes advantage of image tiling (?)
     */
    private void flattenImage() {

        if (transferType == TransferType.FLOAT) {
            flattenImageToFloat();
        } else {
            flattenImageToInt();
        }
    }

    /** Takes the 4-band ARGB image that we have been drawing into and converts it to a single-band int image. */
    private void flattenImageToInt() {
        int numXTiles = image.getNumXTiles();
        int numYTiles = image.getNumYTiles();

        SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_INT, image.getWidth(), image.getHeight(), 1);

        TiledImage destImage = new TiledImage(
                0,
                0,
                image.getWidth(),
                image.getHeight(),
                0,
                0,
                sm,
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_INT));

        for (int yt = 0; yt < numYTiles; yt++) {
            for (int xt = 0; xt < numXTiles; xt++) {
                Raster srcTile = image.getTile(xt, yt);
                WritableRaster destTile = destImage.getWritableTile(xt, yt);

                int[] data = new int[srcTile.getDataBuffer().getSize()];
                srcTile.getDataElements(
                        srcTile.getMinX(), srcTile.getMinY(), srcTile.getWidth(), srcTile.getHeight(), data);

                Rectangle bounds = destTile.getBounds();
                destTile.setPixels(bounds.x, bounds.y, bounds.width, bounds.height, data);
                destImage.releaseWritableTile(xt, yt);
            }
        }

        image = destImage;
    }

    /** Takes the 4-band ARGB image that we have been drawing into and converts it to a single-band float image */
    private void flattenImageToFloat() {
        int numXTiles = image.getNumXTiles();
        int numYTiles = image.getNumYTiles();

        SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_FLOAT, image.getWidth(), image.getHeight(), 1);
        TiledImage destImage = new TiledImage(
                0,
                0,
                image.getWidth(),
                image.getHeight(),
                0,
                0,
                sm,
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_FLOAT));

        for (int yt = 0; yt < numYTiles; yt++) {
            for (int xt = 0; xt < numXTiles; xt++) {
                Raster srcTile = image.getTile(xt, yt);
                WritableRaster destTile = destImage.getWritableTile(xt, yt);

                int[] data = new int[srcTile.getDataBuffer().getSize()];
                srcTile.getDataElements(
                        srcTile.getMinX(), srcTile.getMinY(), srcTile.getWidth(), srcTile.getHeight(), data);

                Rectangle bounds = destTile.getBounds();

                int k = 0;
                for (int dy = bounds.y, drow = 0; drow < bounds.height; dy++, drow++) {
                    for (int dx = bounds.x, dcol = 0; dcol < bounds.width; dx++, dcol++, k++) {
                        destTile.setSample(dx, dy, 0, Float.intBitsToFloat(data[k]));
                    }
                }

                destImage.releaseWritableTile(xt, yt);
            }
        }

        image = destImage;
    }

    private void drawGeometry(Geometries geomType, Geometry geometry) throws TransformException {
        if (transformFeatures) {
            try {
                JTS.transform(geometry, featureToRasterTransform);
            } catch (TransformException ex) {
                throw ex;
            } catch (MismatchedDimensionException ex) {
                throw new RuntimeException(ex);
            }
        }

        Coordinate[] coords = geometry.getCoordinates();

        // enlarge if needed
        if (coords.length > coordGridX.length) {
            int n = coords.length / COORD_GRID_CHUNK_SIZE + 1;
            coordGridX = new int[n * COORD_GRID_CHUNK_SIZE];
            coordGridY = new int[n * COORD_GRID_CHUNK_SIZE];
        }

        // Go through coordinate array in order received
        Position2D worldPos = new Position2D();
        for (int n = 0; n < coords.length; n++) {
            worldPos.setLocation(coords[n].x, coords[n].y);
            GridCoordinates2D gridPos = gridGeom.worldToGrid(worldPos);
            coordGridX[n] = gridPos.x;
            coordGridY[n] = gridPos.y;
        }

        switch (geomType) {
            case POLYGON:
                graphics.fillPolygon(coordGridX, coordGridY, coords.length);
                break;

            case LINESTRING: // includes LinearRing
                graphics.drawPolyline(coordGridX, coordGridY, coords.length);
                break;

            case POINT:
                graphics.fillRect(coordGridX[0], coordGridY[0], 1, 1);
                break;

            default:
                throw new IllegalArgumentException("Invalid geometry type: " + geomType.getName());
        }
    }

    /**
     * Encode a value as a Color. The value will be Integer or Float.
     *
     * @param value the value to enclode
     * @return the resulting sRGB Color
     */
    private Color valueToColor(Number value) {
        int intBits;
        if (transferType == TransferType.FLOAT) {
            intBits = Float.floatToIntBits(value.floatValue());
        } else {
            intBits = value.intValue();
        }

        return new Color(intBits, true);
    }
}
