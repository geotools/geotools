/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.process.raster;

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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.feature.AbstractFeatureCollectionProcess;
import org.geotools.process.feature.AbstractFeatureCollectionProcessFactory;
import org.geotools.referencing.CRS;
import org.geotools.util.NullProgressListener;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.expression.Expression;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A Process to rasterize vector features in an input FeatureCollection.
 * <p>
 * A feature attribute is specified from which to extract the numeric
 * values that will be written to the output grid coverage.
 * At present only int or float values are written to the output grid
 * coverage. If the attribute is of type Long it will be coerced to
 * int values and a warning will be logged. Similarly if the attribute
 * is of type Double it will be coerced to float and a warning logged.
 *
 * @author Steve Ansari, NOAA
 * @author Michael Bedward
 *
 * @source $URL$
 */
public class VectorToRasterProcess extends AbstractFeatureCollectionProcess {

    private static final int COORD_GRID_CHUNK_SIZE = 1000;

    private static enum TransferType {
        INTEGRAL,
        FLOAT;
    }
    private TransferType transferType;

    private static enum ValueSource {
        PROPERTY_NAME,
        EXPRESSION;
    }
    private ValueSource valueSource;

    GridCoverage2D result;
    private Number minAttValue;
    private Number maxAttValue;
    private float nodataValue;

    private ReferencedEnvelope extent;
    private Geometry extentGeometry;

    private int[] coordGridX = new int[COORD_GRID_CHUNK_SIZE];
    private int[] coordGridY = new int[COORD_GRID_CHUNK_SIZE];
    private double cellsize;

    TiledImage image;
    Graphics2D graphics;

    /**
     * Constructor
     *
     * @param factory
     */
    public VectorToRasterProcess(VectorToRasterFactory factory) {
        super(factory);
    }

    /**
     * A static helper method that can be called directy to run the process.
     * <p>
     * The process interface is useful for advertising functionality to
     * dynamic applications, but for 'hands on' coding this method is much more
     * convenient than working via the {@linkplain org.geotools.process.Process#execute }.
     *
     * @param features the feature collection to be (wholly or partially) rasterized
     *
     * @param attribute source of values for the output grid: either a
     *        {@code String} for the name of a numeric feature property or
     *        an {@code org.opengis.filter.expression.Expression} that
     *        evaluates to a numeric value
     *
     * @param gridWidthInCells width (cell) of the output raster
     *
     * @param gridHeightInCells height (cell) of the output raster
     *
     * @param bounds bounds (world coordinates) of the output raster
     *
     * @param covName a name for the output raster
     *
     * @param monitor an optional {@code ProgressListener} (may be {@code null}
     *
     * @return a new grid coverage
     *
     * @throws org.geotools.process.raster.VectorToRasterException
     */
    public static GridCoverage2D process(
            SimpleFeatureCollection features,
            Object attribute,
            Dimension gridDim,
            Envelope bounds,
            String covName,
            ProgressListener monitor) throws VectorToRasterException {

        VectorToRasterFactory factory = new VectorToRasterFactory();
        VectorToRasterProcess process = factory.create();

        return process.convert(features, attribute, gridDim, bounds, covName, monitor);
    }

    /**
     * Retrieves the input parameters from the supplied Map, conducts some basic checking,
     * and then carries out the vector to raster conversion.
     *
     * @param input
     *          input parameters from those defined in {@linkplain VectorToRasterFactory}
     *
     * @param monitor
     *          a ProgressListener object, or null if monitoring is not required
     *
     * @return  a Map of result objects
     *
     * @throws org.geotools.process.raster.VectorToRasterException if unable to
     * rasterize the features as requested
     *
     * @see VectorToRasterFactory#getResultInfo(java.util.Map)
     */
    public Map<String, Object> execute( Map<String, Object> input, ProgressListener monitor )
        throws VectorToRasterException {

        SimpleFeatureCollection features = (SimpleFeatureCollection)
            input.get(AbstractFeatureCollectionProcessFactory.FEATURES.key);

        String attributeStr = (String) input.get(VectorToRasterFactory.ATTRIBUTE.key);
        Expression attribute = null;
        try {
        	attribute = ECQL.toExpression(attributeStr);
        } catch(CQLException e) {
        	throw new VectorToRasterException(e);
        }

        int w = (Integer) input.get(VectorToRasterFactory.RASTER_WIDTH.key);
        int h = (Integer) input.get(VectorToRasterFactory.RASTER_HEIGHT.key);
        Dimension gridDim = new Dimension(w, h);

        ReferencedEnvelope env = (ReferencedEnvelope) input.get(VectorToRasterFactory.BOUNDS.key);

        String title = (String) input.get(VectorToRasterFactory.TITLE.key);
        if(title == null) {
        	title = "raster";
        }

        GridCoverage2D cov = convert(features, attribute, gridDim, env, title, monitor);

        Map<String, Object> results = new HashMap<String, Object>();
        results.put(VectorToRasterFactory.RESULT.key, cov);
        return results;
    }

    /**
     * This method is called by {@linkplain #execute} to rasterize an individual feature.
     *
     * @param feature
     *          the feature to be rasterized
     *
     * @param input
     *          the intput parameters (ignored in this implementation)
     *
     * @throws java.lang.Exception
     */
    @Override
    protected void processFeature(SimpleFeature feature, Map<String, Object> input) throws Exception {

        Object attribute = input.get(VectorToRasterFactory.ATTRIBUTE.key);
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

            if (geometry.getClass().equals(MultiPolygon.class)) {
                MultiPolygon mp = (MultiPolygon) geometry;
                for (int n = 0; n < mp.getNumGeometries(); n++) {
                    drawGeometry(mp.getGeometryN(n));
                }

            } else if (geometry.getClass().equals(MultiLineString.class)) {
                MultiLineString mp = (MultiLineString) geometry;
                for (int n = 0; n < mp.getNumGeometries(); n++) {
                    drawGeometry(mp.getGeometryN(n));
                }

            } else if (geometry.getClass().equals(MultiPoint.class)) {
                MultiPoint mp = (MultiPoint) geometry;
                for (int n = 0; n < mp.getNumGeometries(); n++) {
                    drawGeometry(mp.getGeometryN(n));
                }

            } else {
                drawGeometry(geometry);
            }
        }

    }

    private Number getFeatureValue(SimpleFeature feature, Object attribute) {
        Class<? extends Number> rtnType = transferType == TransferType.FLOAT ? Float.class : Integer.class;
        if (valueSource == ValueSource.PROPERTY_NAME) {
            return rtnType.cast(feature.getAttribute((String)attribute));
        } else {
            return ((Expression)attribute).evaluate(feature, rtnType);
        }
    }

    private GridCoverage2D convert(
            SimpleFeatureCollection features,
            Object attribute,
            Dimension gridDim,
            Envelope bounds,
            String covName,
            ProgressListener monitor)
        throws VectorToRasterException {

        if ( monitor == null ) {
            monitor = new NullProgressListener();
        }

        initialize( features, bounds, attribute, gridDim );

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(VectorToRasterFactory.ATTRIBUTE.key, attribute);

        monitor.setTask(new SimpleInternationalString("Rasterizing features..."));

        float scale = 100.0f / features.size();
        monitor.started();

        SimpleFeatureIterator fi = features.features();
        try {
            int counter = 0;
            while( fi.hasNext() ) {
                try {
                    processFeature(fi.next(), params);
                }
                catch( Exception e ) {
                    monitor.exceptionOccurred( e );
                }

                monitor.progress( scale * counter++);
            }
        }
        finally {
            features.close( fi );
        }
        monitor.complete();

        flattenImage();

        GridCoverageFactory gcf = new GridCoverageFactory();
        return gcf.create(covName, image, extent);
    }

    private void initialize(SimpleFeatureCollection features,
            Envelope bounds, Object attribute, Dimension gridDim ) throws VectorToRasterException {

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
                Logger.getLogger(VectorToRasterProcess.class.getName()).log(Level.WARNING, "coercing double feature values to float raster values");

            } else if (Long.class.isAssignableFrom(attClass)) {
                transferType = TransferType.INTEGRAL;
                Logger.getLogger(VectorToRasterProcess.class.getName()).log(Level.WARNING, "coercing long feature values to int raster values");

            } else {
                transferType = TransferType.INTEGRAL;
            }

            valueSource = ValueSource.PROPERTY_NAME;

        } else if (attribute instanceof Expression) {
            valueSource = ValueSource.EXPRESSION;

        } else {
            throw new VectorToRasterException(
                    "value attribute must be a feature property name" +
                    "or an org.opengis.filter.expression.Expression object");
        }

        minAttValue = maxAttValue = null;

        setBounds( features, bounds, gridDim );
        createImage( gridDim );
    }

    /**
     *
     * @param env
     * @throws org.geotools.process.raster.VectorToRasterException
     */
    private void setBounds( SimpleFeatureCollection features,
            Envelope bounds, Dimension gridDim ) throws VectorToRasterException {

        ReferencedEnvelope featureBounds = features.getBounds();

        if (bounds != null) {
            ReferencedEnvelope inputBounds = new ReferencedEnvelope(bounds);
            CoordinateReferenceSystem featuresCRS = featureBounds.getCoordinateReferenceSystem();
            CoordinateReferenceSystem envCRS = bounds.getCoordinateReferenceSystem();

            ReferencedEnvelope trEnv;
            if (!CRS.equalsIgnoreMetadata(envCRS, featuresCRS)) {
                try {
                    trEnv = inputBounds.transform(featuresCRS, true);
                } catch (Exception tex) {
                    throw new VectorToRasterException(tex);
                }

            } else {
                trEnv = inputBounds;
            }

            com.vividsolutions.jts.geom.Envelope common = trEnv.intersection(features.getBounds());
            if (common == null || common.isNull()) {
                throw new VectorToRasterException(
                        "Features do not lie within the requested rasterizing bounds");
            }

            extent = new ReferencedEnvelope(common, featuresCRS);

        } else {

            /*
             * The bounds arg was null - interpreted as set bounds to
             * those of the FeatureCollection
             */
            extent = featureBounds;
        }

        GeometryFactory gf = new GeometryFactory();
        extentGeometry = gf.toGeometry(extent);

        double xInterval = extent.getWidth() / gridDim.getWidth();
        double yInterval = extent.getHeight() / gridDim.getHeight();
        cellsize = Math.max(xInterval, yInterval);
    }

    /**
     * Create the tiled image and the associated graphics object that we will be used to
     * draw the vector features into a raster.
     * <p>
     * Note, the graphics objects will be an
     * instance of TiledImageGraphics which is a sub-class of Graphics2D.
     */
    private void createImage( Dimension gridDim ) {

        ColorModel cm = ColorModel.getRGBdefault();
        SampleModel sm = cm.createCompatibleSampleModel(gridDim.width, gridDim.height);

        image = new TiledImage(0, 0, gridDim.width, gridDim.height, 0, 0, sm, cm);
        graphics = image.createGraphics();
        graphics.setPaintMode();
        graphics.setComposite(AlphaComposite.Src);
    }

    /**
     * Takes the 4-band ARGB image that we have been drawing into and
     * converts it to a single-band image.
     *
     * @todo There is probably a much easier / faster way to do this that
     * still takes advantage of image tiling (?)
     */
    private void flattenImage() {

        if (transferType == TransferType.FLOAT) {
            flattenImageToFloat();
        } else {
            flattenImageToInt();
        }
    }

    /**
     * Takes the 4-band ARGB image that we have been drawing into and
     * converts it to a single-band int image.
     */
    private void flattenImageToInt() {
        int numXTiles = image.getNumXTiles();
        int numYTiles = image.getNumYTiles();

        SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_INT, image.getWidth(), image.getHeight(), 1);

        TiledImage destImage = new TiledImage(0, 0, image.getWidth(), image.getHeight(),
                0, 0, sm, new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), 
                		false, false, Transparency.OPAQUE, DataBuffer.TYPE_INT));

        for (int yt = 0; yt < numYTiles; yt++) {
            for (int xt = 0; xt < numXTiles; xt++) {
                Raster srcTile = image.getTile(xt, yt);
                WritableRaster destTile = destImage.getWritableTile(xt, yt);

                int[] data = new int[srcTile.getDataBuffer().getSize()];
                srcTile.getDataElements(srcTile.getMinX(), srcTile.getMinY(),
                        srcTile.getWidth(), srcTile.getHeight(), data);

                Rectangle bounds = destTile.getBounds();
                destTile.setPixels(bounds.x, bounds.y, bounds.width, bounds.height, data);
                destImage.releaseWritableTile(xt, yt);
            }
        }

        image = destImage;
    }

    /**
     * Takes the 4-band ARGB image that we have been drawing into and
     * converts it to a single-band float image
     */
    private void flattenImageToFloat() {
        int numXTiles = image.getNumXTiles();
        int numYTiles = image.getNumYTiles();

        SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(DataBuffer.TYPE_FLOAT, image.getWidth(), image.getHeight(), 1);
        TiledImage destImage = new TiledImage(0, 0, image.getWidth(), image.getHeight(), 0, 0, sm, 
        		new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), 
                		false, false, Transparency.OPAQUE, DataBuffer.TYPE_FLOAT));

        for (int yt = 0; yt < numYTiles; yt++) {
            for (int xt = 0; xt < numXTiles; xt++) {
                Raster srcTile = image.getTile(xt, yt);
                WritableRaster destTile = destImage.getWritableTile(xt, yt);

                int[] data = new int[srcTile.getDataBuffer().getSize()];
                srcTile.getDataElements(srcTile.getMinX(), srcTile.getMinY(), data);

                Rectangle bounds = destTile.getBounds();

                int k = 0;
                for (int dy = bounds.y, drow = 0; drow < bounds.height; dy++, drow++) {
                    for (int dx = bounds.x, dcol = 0; dcol < bounds.width; dx++, dcol++) {
                        destTile.setSample(dx, dy, 0, Float.intBitsToFloat(data[k]));
                    }
                }

                destImage.releaseWritableTile(xt, yt);
            }
        }

        image = destImage;
    }

    private void drawGeometry(Geometry geometry) {

        Coordinate[] coords = geometry.getCoordinates();

        // enlarge if needed
        if (coords.length > coordGridX.length) {
            int n = coords.length / COORD_GRID_CHUNK_SIZE + 1;
            coordGridX = new int[n * COORD_GRID_CHUNK_SIZE];
            coordGridY = new int[n * COORD_GRID_CHUNK_SIZE];
        }

        // Go through coordinate array in order received (clockwise)
        for (int n = 0; n < coords.length; n++) {
            coordGridX[n] = (int) (((coords[n].x - extent.getMinX()) / cellsize));
            coordGridY[n] = (int) (((coords[n].y - extent.getMinY()) / cellsize));
            coordGridY[n] = image.getHeight() - coordGridY[n];
        }


        if (geometry.getClass().equals(Polygon.class)) {
            graphics.fillPolygon(coordGridX, coordGridY, coords.length);
        } else if (geometry.getClass().equals(LinearRing.class)) {
            graphics.drawPolyline(coordGridX, coordGridY, coords.length);
        } else if (geometry.getClass().equals(LineString.class)) {
            graphics.drawPolyline(coordGridX, coordGridY, coords.length);
        } else if (geometry.getClass().equals(Point.class)) {
            graphics.drawPolyline(coordGridX, coordGridY, coords.length);
        }
    }

    /**
     * Encode a value as a Color. The value will be Integer or Float.
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
