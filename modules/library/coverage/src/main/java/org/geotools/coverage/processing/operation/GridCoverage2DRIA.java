/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2016, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014 TOPP - www.openplans.org.
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
package org.geotools.coverage.processing.operation;

import com.sun.media.jai.util.ImageUtil;
import com.sun.media.jai.util.PropertyGeneratorImpl;
import it.geosolutions.jaiext.iterators.RandomIterFactory;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.BorderExtender;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.GeometricOpImage;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RasterAccessor;
import javax.media.jai.RasterFormatTag;
import javax.media.jai.TileCache;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.operator.ConstantDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * A RenderedImage that provides values coming from a source GridCoverage2D, with a backing grid
 * addressable as the target GridCoverage2D.
 *
 * <p>The exposed Layout will be the same as the target, and each Point in the target grid can be
 * used in the resulting RenderedImage,
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class GridCoverage2DRIA extends GeometricOpImage {

    private static final Logger LOGGER = Logger.getLogger(GridCoverage2DRIA.class.getName());

    private final GridCoverage2D src;

    private final GridGeometry2D dst;

    private final MathTransform g2wd;

    private final MathTransform g2ws;

    private final MathTransform w2gd;

    private final MathTransform w2gs;

    private final MathTransform src2dstCRSTransform;

    private final MathTransform dst2srcCRSTransform;

    /** Color table representing source's IndexColorModel. */
    private byte[][] ctable = null; // ETj: just for keeping compiler quiet: let's see if we really

    private ROI roi;

    private boolean hasROI;

    private Rectangle roiBounds;

    private volatile PlanarImage roiImage;

    /**
     * Wrap the src coverage in the dst layout. <br>
     * The resulting RenderedImage will contain the data in src, and will be accessible via the grid
     * specs of dst,
     *
     * @param src the data coverage to be remapped on dst grid
     * @param dst the provider of the final grid
     * @param nodata the nodata value to set for cells not covered by src but included in dst. All
     *     bands will share the same nodata value.
     * @return an instance of Coverage2RenderedImageAdapter
     */
    public static GridCoverage2DRIA create(
            final GridCoverage2D src, final GridGeometry2D dst, final double[] nodata) {
        return create(src, dst, nodata, null, null);
    }

    public static GridCoverage2DRIA create(
            final GridCoverage2D src, final GridCoverage2D dst, final double[] nodata) {
        return create(src, dst, nodata, null, null);
    }

    public static GridCoverage2DRIA create(
            GridCoverage2D src, GridGeometry2D dst, double[] nodata, Hints hints) {
        return create(src, dst, nodata, hints, null);
    }

    /**
     * Wrap the src coverage in the dst layout. <br>
     * The resulting RenderedImage will contain the data in src, and will be accessible via the grid
     * specs of dst,
     *
     * @param src the data coverage to be remapped on dst grid
     * @param dst the provider of the final grid
     * @param nodata the nodata values to set for cells not covered by src but included in dst. All
     *     bands will use the related nodata value.
     * @param hints hints to use for the Rendering, Actually only ImageLayout is considered
     * @return an instance of Coverage2RenderedImageAdapter
     */
    public static GridCoverage2DRIA create(
            GridCoverage2D src, GridGeometry2D dst, double[] nodata, Hints hints, ROI roi) {

        Utilities.ensureNonNull("dst", dst);

        // === Create destination Layout, retaining source tiling to minimize quirks
        // TODO allow to override tiling
        final GridEnvelope2D destinationRasterDimension = dst.getGridRange2D();
        final ImageLayout imageLayout = new ImageLayout();
        imageLayout.setMinX(destinationRasterDimension.x).setMinY(destinationRasterDimension.y);
        imageLayout
                .setWidth(destinationRasterDimension.width)
                .setHeight(destinationRasterDimension.height);

        //
        // SampleModel and ColorModel are related to data itself, so we
        // copy them from the source

        imageLayout.setColorModel(src.getRenderedImage().getColorModel());
        imageLayout.setSampleModel(src.getRenderedImage().getSampleModel());

        if (hints != null && hints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
            ImageLayout l = (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);
            if (l.isValid(ImageLayout.TILE_HEIGHT_MASK) && l.isValid(ImageLayout.TILE_WIDTH_MASK)) {
                imageLayout.setTileHeight(
                        Math.min(imageLayout.getHeight(null), l.getTileHeight(null)));
                imageLayout.setTileWidth(
                        Math.min(imageLayout.getWidth(null), l.getTileWidth(null)));
            }
        }

        // === BorderExtender
        //
        // We have yet to check for it usefulness: it might be more convenient
        // to check for region overlapping and return a nodata value by hand,
        // so to avoid problems with interpolation at source raster borders.
        //
        BorderExtender extender =
                new BorderExtenderConstant(new double[] {nodata != null ? nodata[0] : 0d});

        // Check if the input coverage contains a ROI
        ROI property = CoverageUtilities.getROIProperty(src);
        if (property != null) {
            roi = roi != null ? roi.intersect(property) : property;
        }
        return new GridCoverage2DRIA(
                src,
                dst,
                vectorize(src.getRenderedImage()),
                imageLayout,
                null,
                false,
                extender,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                nodata,
                roi,
                hints);
    }

    // need it

    /**
     * Wrap the src coverage in the dst layout. <br>
     * The resulting RenderedImage will contain the data in src, and will be accessible via the grid
     * specs of dst,
     *
     * @param src the data coverage to be remapped on dst grid
     * @param dst the provider of the final grid
     * @param nodata the nodata values to set for cells not covered by src but included in dst. All
     *     bands will use the related nodata value.
     * @return an instance of Coverage2RenderedImageAdapter
     */
    public static GridCoverage2DRIA create(
            final GridCoverage2D src,
            final GridCoverage2D dst,
            final double[] nodata,
            Hints hints,
            ROI roi) {

        // === Create Layout
        final ImageLayout imageLayout = new ImageLayout(dst.getRenderedImage());

        //
        // SampleModel and ColorModel are related to data itself, so we
        // copy them from the source

        imageLayout.setColorModel(src.getRenderedImage().getColorModel());
        imageLayout.setSampleModel(src.getRenderedImage().getSampleModel());

        // === BorderExtender
        //
        // We have yet to check for it usefulness: it might be more convenient
        // to check for region overlapping and return a nodata value by hand,
        // so to avoid problems with interpolation at source raster borders.
        //
        BorderExtender extender =
                new BorderExtenderConstant(new double[] {nodata != null ? nodata[0] : 0d});

        // Check if the input coverage contains a ROI
        ROI property = CoverageUtilities.getROIProperty(src);
        if (property != null) {
            roi = roi != null ? roi.intersect(property) : property;
        }

        return new GridCoverage2DRIA(
                src,
                dst.getGridGeometry(),
                vectorize(src.getRenderedImage()),
                imageLayout,
                null,
                false,
                extender,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                nodata,
                roi,
                hints);
    }

    protected GridCoverage2DRIA(
            final GridCoverage2D src,
            final GridGeometry2D dst,
            final Vector sources,
            final ImageLayout layout,
            final Map configuration,
            final boolean cobbleSources,
            final BorderExtender extender,
            final Interpolation interp,
            final double[] nodata,
            ROI roi,
            Hints hints) {

        super(sources, layout, configuration, cobbleSources, extender, interp, nodata);

        this.src = src;
        this.dst = dst;

        // === Take one for all all the transformation we need to pass from
        // model, sample, src, target and viceversa.
        g2wd = dst.getGridToCRS2D(PixelOrientation.UPPER_LEFT);

        try {
            w2gd = g2wd.inverse();
        } catch (org.opengis.referencing.operation.NoninvertibleTransformException e) {
            throw new IllegalArgumentException("Can't compute destination W2G", e);
        }

        g2ws = src.getGridGeometry().getGridToCRS2D(PixelOrientation.UPPER_LEFT);

        try {
            w2gs = g2ws.inverse();
        } catch (org.opengis.referencing.operation.NoninvertibleTransformException e) {
            throw new IllegalArgumentException("Can't compute source W2G", e);
        }

        try {
            CoordinateReferenceSystem sourceCRS = src.getCoordinateReferenceSystem2D();
            CoordinateReferenceSystem targetCRS = dst.getCoordinateReferenceSystem2D();

            src2dstCRSTransform = CRS.findMathTransform(sourceCRS, targetCRS, true);
            dst2srcCRSTransform = src2dstCRSTransform.inverse();
        } catch (FactoryException e) {
            throw new IllegalArgumentException("Can't create a transform between CRS", e);
        } catch (NoninvertibleTransformException e) {
            throw new IllegalArgumentException("Can't create a transform between CRS", e);
        }

        // Input ROI
        this.roi = roi;
        this.hasROI = roi != null;
        if (hasROI) {
            this.roiBounds = roi.getBounds();
            setProperty("roi", roi);
        }

        // ensure we have tile caching
        if (hints != null) {
            TileCache tc = (TileCache) hints.get(JAI.KEY_TILE_CACHE);
            if (tc != null) {
                setTileCache(tc);
            } else {
                setTileCache(JAI.getDefaultInstance().getTileCache());
            }
        }
    }

    @Override
    public Raster getTile(int tileX, int tileY) {
        return super.getTile(tileX, tileY);
    }

    @Override
    public Point2D mapSourcePoint(Point2D srcPt, int sourceIndex) {
        if (srcPt == null) {
            throw new IllegalArgumentException("Bad dest pt"); // JaiI18N.getString("Generic0"));
        } else if (sourceIndex < 0 || sourceIndex >= getNumSources()) {
            throw new IndexOutOfBoundsException("Bad src"); // JaiI18N.getString("Generic1"));
        }

        double[] coords = new double[] {srcPt.getX(), srcPt.getY()};

        try {
            mapSrcPoint(coords);
        } catch (TransformException e) {
            LOGGER.log(Level.WARNING, "Error transforming coords", e);
            return null;
        }

        Point2D ret = ((Point2D) srcPt.clone());
        ret.setLocation(coords[0], coords[1]);
        if (inside(ret, src.getRenderedImage())) return ret;
        else {
            LOGGER.log(
                    Level.WARNING,
                    "{0} mapped to {1} lies outside {2},{3}+{4}x{5}",
                    new Object[] {
                        srcPt,
                        ret,
                        src.getRenderedImage().getMinX(),
                        src.getRenderedImage().getMinX(),
                        src.getRenderedImage().getWidth(),
                        src.getRenderedImage().getHeight()
                    });
            return null;
        }
    }

    private static boolean inside(Point2D point, RenderedImage ri) {
        double x = point.getX();
        double y = point.getY();
        return x >= ri.getMinX()
                && x <= ri.getMinX() + ri.getWidth()
                && y >= ri.getMinY()
                && y <= ri.getMinY() + ri.getHeight();
    }

    /**
     * Returns the minimum bounding box of the region of the destination to which a particular
     * <code>Rectangle</code> of the specified source will be mapped.
     *
     * <p>The integral source rectangle coordinates should be considered pixel indices. The "energy"
     * of each pixel is defined to be concentrated in the continuous plane of pixels at an offset of
     * (0.5,&nbsp;0.5) from the index of the pixel. Forward mappings must take this (0.5,&nbsp;0.5)
     * pixel center into account. Thus given integral source pixel indices as input, the fractional
     * destination location, as calculated by functions Xf(xSrc,&nbsp;ySrc), Yf(xSrc,&nbsp;ySrc), is
     * given by:
     *
     * <pre>
     *
     *     xDst = Xf(xSrc+0.5, ySrc+0.5) - 0.5
     *     yDst = Yf(xSrc+0.5, ySrc+0.5) - 0.5
     *
     * </pre>
     *
     * @param pxRect the <code>Rectangle</code> in source coordinates.
     * @param sourceIndex the index of the source image.
     * @return a <code>Rectangle</code> indicating the destination bounding box, or <code>null
     *     </code> if the bounding box is unknown.
     * @throws IllegalArgumentException if <code>sourceIndex</code> is negative or greater than the
     *     index of the last source.
     * @throws IllegalArgumentException if <code>sourceRect</code> is <code>null</code>.
     */
    @Override
    protected Rectangle forwardMapRect(Rectangle pxRect, int sourceIndex) {
        // transformation from out target coverage toward the source one.
        // note that source/target names from OpImage are reversed with respect to our
        // definitions

        // i is not used, only one source raster

        float[] pts = rect2PointArr(pxRect);

        try {
            g2wd.transform(pts, 0, pts, 0, 4);
            dst2srcCRSTransform.transform(pts, 0, pts, 0, 4);
            w2gs.transform(pts, 0, pts, 0, 4);
        } catch (TransformException e) {
            LOGGER.log(Level.WARNING, "Error transforming coords", e);
            return null;
        }

        Rectangle srcRect = pointArr2Rect(pts);
        return srcRect; // .intersection(src.getGridGeometry().getGridRange2D());
    }

    @Override
    public Point2D mapDestPoint(Point2D destPt, int sourceIndex) {
        if (destPt == null) {
            throw new IllegalArgumentException("Bad dest pt"); // JaiI18N.getString("Generic0"));
        } else if (sourceIndex < 0 || sourceIndex >= getNumSources()) {
            throw new IndexOutOfBoundsException("Bad src"); // JaiI18N.getString("Generic1"));
        }

        double[] coords = new double[] {destPt.getX(), destPt.getY()};

        try {
            mapDestPoint(coords);
        } catch (TransformException e) {
            LOGGER.log(Level.WARNING, "Error transforming coords", e);
            return null;
        }

        Point2D ret = ((Point2D) destPt.clone());
        ret.setLocation(coords[0], coords[1]);
        if (dst.getEnvelope2D().contains(ret)) return ret;
        else return null;
    }

    private void mapDestPoint(double[] coords) throws TransformException {
        final int npoints = coords.length / 2;
        g2ws.transform(coords, 0, coords, 0, npoints);
        src2dstCRSTransform.transform(coords, 0, coords, 0, npoints);
        w2gd.transform(coords, 0, coords, 0, npoints);
    }

    private void mapSrcPoint(double[] coords) throws TransformException {
        final int npoints = coords.length / 2;
        // StringBuilder sb = new StringBuilder();
        // sb.append("SRC[").append(coords[0]).append(',').append(coords[1]).append(']').append("--g2wd->");
        g2wd.transform(coords, 0, coords, 0, npoints);
        // sb.append("[").append(coords[0]).append(',').append(coords[1]).append(']').append("--d2sCRS->");
        dst2srcCRSTransform.transform(coords, 0, coords, 0, npoints);
        // sb.append('[').append(coords[0]).append(',').append(coords[1]).append(']').append("--w2gs->");
        w2gs.transform(coords, 0, coords, 0, npoints);
        // sb.append('[').append(coords[0]).append(',').append(coords[1]).append(']');
        // System.out.println(sb);
    }

    /**
     * Returns the minimum bounding box of the region of the specified source to which a particular
     * <code>Rectangle</code> of the destination will be mapped.
     *
     * <p>The integral destination rectangle coordinates should be considered pixel indices. The
     * "energy" of each pixel is defined to be concentrated in the continuous plane of pixels at an
     * offset of (0.5,&nbsp;0.5) from the index of the pixel. Backward mappings must take this
     * (0.5,&nbsp;0.5) pixel center into account. Thus given integral destination pixel indices as
     * input, the fractional source location, as calculated by functions Xb(xDst,&nbsp;yDst),
     * Yb(xDst,&nbsp;yDst), is given by:
     *
     * <pre>
     *
     *     xSrc = Xb(xDst+0.5, yDst+0.5) - 0.5
     *     ySrc = Yb(xDst+0.5, yDst+0.5) - 0.5
     *
     * </pre>
     *
     * @param destRect the <code>Rectangle</code> in destination coordinates.
     * @param sourceIndex the index of the source image.
     * @return a <code>Rectangle</code> indicating the source bounding box, or <code>null</code> if
     *     the bounding box is unknown.
     * @throws IllegalArgumentException if <code>sourceIndex</code> is negative or greater than the
     *     index of the last source.
     * @throws IllegalArgumentException if <code>destRect</code> is <code>null</code>.
     */
    @Override
    protected Rectangle backwardMapRect(Rectangle destRect, int sourceIndex) {
        float[] pts = rect2PointArr(destRect);

        try {
            g2ws.transform(pts, 0, pts, 0, 4);
            src2dstCRSTransform.transform(pts, 0, pts, 0, 4);
            w2gd.transform(pts, 0, pts, 0, 4);
        } catch (TransformException e) {
            LOGGER.log(Level.WARNING, "Error transforming coords", e);
            return null;
        }

        Rectangle pxRect = pointArr2Rect(pts);
        return pxRect;
    }

    private static float[] rect2PointArr(Rectangle rect) {
        float dx0 = rect.x;
        float dy0 = rect.y;
        float dw = (rect.width);
        float dh = (rect.height);

        return new float[] {dx0, dy0, (dx0 + dw), dy0, (dx0 + dw), (dy0 + dh), dx0, (dy0 + dh)};
    }

    private Rectangle pointArr2Rect(float[] points) {
        float f_sx0 = Float.MAX_VALUE;
        float f_sy0 = Float.MAX_VALUE;
        float f_sx1 = -Float.MAX_VALUE;
        float f_sy1 = -Float.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            float px = points[i * 2];
            float py = points[i * 2 + 1];

            f_sx0 = Math.min(f_sx0, px);
            f_sy0 = Math.min(f_sy0, py);
            f_sx1 = Math.max(f_sx1, px);
            f_sy1 = Math.max(f_sy1, py);
        }

        int s_x0 = 0, s_y0 = 0, s_x1 = 0, s_y1 = 0;

        // Find the bounding box of the source rectangle
        if (interp instanceof InterpolationNearest) {
            s_x0 = (int) Math.floor(f_sx0);
            s_y0 = (int) Math.floor(f_sy0);

            // Fix for bug 4485920 was to add " + 0.05" to the following
            // two lines. It should be noted that the fix was made based
            // on empirical evidence and tested thoroughly, but it is not
            // known whether this is the root cause.
            s_x1 = (int) Math.ceil(f_sx1 + 0.5);
            s_y1 = (int) Math.ceil(f_sy1 + 0.5);
        } else {
            s_x0 = (int) Math.floor(f_sx0 - 0.5);
            s_y0 = (int) Math.floor(f_sy0 - 0.5);
            s_x1 = (int) Math.ceil(f_sx1);
            s_y1 = (int) Math.ceil(f_sy1);
        }

        //
        // Return the new rectangle
        //
        return new Rectangle(s_x0, s_y0, s_x1 - s_x0, s_y1 - s_y0);
    }

    /**
     * Warps a rectangle.
     *
     * <p>Offers Improved performance with support for no data and region of interest.
     */
    @Override
    protected void computeRect(PlanarImage[] sources, WritableRaster dest, Rectangle destRect) {
        RasterFormatTag formatTag = getFormatTags()[1];
        RasterAccessor d = new RasterAccessor(dest, destRect, formatTag, getColorModel());

        RandomIter roiIter = null;

        boolean roiContainsTile = false;
        boolean roiDisjointTile = false;

        // If a ROI is present, then only the part contained inside the current tile bounds is
        // taken.
        if (hasROI) {
            Rectangle srcRectExpanded = null;
            int x = (int) destRect.getMinX();
            int y = (int) destRect.getMinY();
            int w = (int) destRect.getWidth();
            int h = (int) destRect.getHeight();
            float[] src = new float[w * h * 2];
            warpRect(x, y, w, h, src);

            double minX = Double.POSITIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;
            double maxX = Double.NEGATIVE_INFINITY;
            double maxY = Double.NEGATIVE_INFINITY;
            int numP = src.length;
            for (int i = 0; i < numP; i = i + 2) {
                float xi = src[i];
                float yi = src[i + 1];
                minX = xi < minX ? xi : minX;
                minY = yi < minY ? yi : minY;
                maxX = xi > maxX ? xi : maxX;
                maxY = yi > maxY ? yi : maxY;
            }
            srcRectExpanded =
                    new Rectangle(
                            (int) minX,
                            (int) minY,
                            (int) (maxX - minX) + 1,
                            (int) (maxY - minY) + 1);

            // The tile dimension is extended for avoiding border errors
            srcRectExpanded.setRect(
                    srcRectExpanded.getMinX() - interp.getLeftPadding(),
                    srcRectExpanded.getMinY() - interp.getTopPadding(),
                    srcRectExpanded.getWidth() + interp.getRightPadding() + interp.getLeftPadding(),
                    srcRectExpanded.getHeight()
                            + interp.getBottomPadding()
                            + interp.getTopPadding());

            if (!roiBounds.intersects(srcRectExpanded)) {
                roiDisjointTile = true;
            } else {
                roiContainsTile = roi.contains(srcRectExpanded);
                if (!roiContainsTile) {
                    if (!roi.intersects(srcRectExpanded)) {
                        roiDisjointTile = true;
                    } else {
                        PlanarImage roiIMG = getImage();
                        roiIter = RandomIterFactory.create(roiIMG, null, true, true);
                    }
                }
            }
        }

        if (roiDisjointTile) {
            double[] bkg = setBackground ? backgroundValues : new double[dest.getNumBands()];
            ImageUtil.fillBackground(dest, destRect, bkg);
            return;
        }

        computeRect(sources[0], d, roiIter, roiContainsTile);

        if (d.isDataCopy()) {
            d.clampDataArrays();
            d.copyDataToRaster();
        }
    }

    private void computeRect(
            PlanarImage source, RasterAccessor d, RandomIter roiIter, boolean roiContainsTile) {
        switch (d.getDataType()) {
            case DataBuffer.TYPE_BYTE:
                computeRectByte(source, d, roiIter, roiContainsTile);
                break;
            case DataBuffer.TYPE_USHORT:
                computeRectUShort(source, d, roiIter, roiContainsTile);
                break;
            case DataBuffer.TYPE_SHORT:
                computeRectShort(source, d, roiIter, roiContainsTile);
                break;
            case DataBuffer.TYPE_INT:
                computeRectInt(source, d, roiIter, roiContainsTile);
                break;
            case DataBuffer.TYPE_FLOAT:
                computeRectFloat(source, d, roiIter, roiContainsTile);
                break;
            case DataBuffer.TYPE_DOUBLE:
                computeRectDouble(source, d, roiIter, roiContainsTile);
                break;
        }
    }

    private void computeRectByte(
            PlanarImage src, RasterAccessor dst, RandomIter roiIter, boolean roiContainsTile) {
        Interpolation interp =
                this.interp != null
                        ? this.interp
                        : Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        int lpad = interp.getLeftPadding();
        int rpad = interp.getRightPadding();
        int tpad = interp.getTopPadding();
        int bpad = interp.getBottomPadding();

        int minX, maxX, minY, maxY;
        RandomIter iter = null;
        try {
            if (extender != null) {
                minX = src.getMinX();
                maxX = src.getMaxX();
                minY = src.getMinY();
                maxY = src.getMaxY();
                iter = getRandomIterator(src, lpad, rpad, tpad, bpad, extender);
            } else {
                minX = src.getMinX() + lpad;
                maxX = src.getMaxX() - rpad;
                minY = src.getMinY() + tpad;
                maxY = src.getMaxY() - bpad;
                iter = getRandomIterator(src);
            }

            int kwidth = interp.getWidth();
            int kheight = interp.getHeight();

            int dstWidth = dst.getWidth();
            int dstHeight = dst.getHeight();
            int dstBands = dst.getNumBands();

            int lineStride = dst.getScanlineStride();
            int pixelStride = dst.getPixelStride();
            int[] bandOffsets = dst.getBandOffsets();
            byte[][] data = dst.getByteDataArrays();

            int precH = 1 << interp.getSubsampleBitsH();
            int precV = 1 << interp.getSubsampleBitsV();

            float[] warpData = new float[2 * dstWidth];

            int[][] samples = new int[kheight][kwidth];

            int lineOffset = 0;

            byte[] backgroundByte = new byte[dstBands];
            for (int i = 0; i < dstBands; i++) {
                backgroundByte[i] = (byte) backgroundValues[i];
            }

            if (ctable == null) { // source does not have IndexColorModel
                for (int h = 0; h < dstHeight; h++) {
                    int pixelOffset = lineOffset;
                    lineOffset += lineStride;

                    warpRect(dst.getX(), dst.getY() + h, dstWidth, 1, warpData);

                    int count = 0;
                    for (int w = 0; w < dstWidth; w++) {
                        float sx = warpData[count++];
                        float sy = warpData[count++];

                        int xint = floor(sx);
                        int yint = floor(sy);
                        int xfrac = (int) ((sx - xint) * precH);
                        int yfrac = (int) ((sy - yint) * precV);

                        if (xint < minX
                                || xint >= maxX
                                || yint < minY
                                || yint >= maxY
                                || !inROI(xint, yint, roiIter, roiContainsTile)) {
                            /* Fill with a background color. */
                            if (setBackground) {
                                for (int b = 0; b < dstBands; b++) {
                                    data[b][pixelOffset + bandOffsets[b]] = backgroundByte[b];
                                }
                            }
                        } else {
                            xint -= lpad;
                            yint -= tpad;

                            for (int b = 0; b < dstBands; b++) {
                                for (int j = 0; j < kheight; j++) {
                                    for (int i = 0; i < kwidth; i++) {
                                        samples[j][i] =
                                                iter.getSample(xint + i, yint + j, b) & 0xFF;
                                    }
                                }

                                data[b][pixelOffset + bandOffsets[b]] =
                                        ImageUtil.clampByte(
                                                interp.interpolate(samples, xfrac, yfrac));
                            }
                        }

                        pixelOffset += pixelStride;
                    }
                }
            } else { // source has IndexColorModel
                for (int h = 0; h < dstHeight; h++) {
                    int pixelOffset = lineOffset;
                    lineOffset += lineStride;

                    warpRect(dst.getX(), dst.getY() + h, dstWidth, 1, warpData);

                    int count = 0;
                    for (int w = 0; w < dstWidth; w++) {
                        float sx = warpData[count++];
                        float sy = warpData[count++];

                        int xint = floor(sx);
                        int yint = floor(sy);
                        int xfrac = (int) ((sx - xint) * precH);
                        int yfrac = (int) ((sy - yint) * precV);

                        if (xint < minX
                                || xint >= maxX
                                || yint < minY
                                || yint >= maxY
                                || !inROI(xint, yint, roiIter, roiContainsTile)) {
                            /* Fill with a background color. */
                            if (setBackground) {
                                for (int b = 0; b < dstBands; b++) {
                                    data[b][pixelOffset + bandOffsets[b]] = backgroundByte[b];
                                }
                            }
                        } else {
                            xint -= lpad;
                            yint -= tpad;

                            for (int b = 0; b < dstBands; b++) {
                                byte[] t = ctable[b];

                                for (int j = 0; j < kheight; j++) {
                                    for (int i = 0; i < kwidth; i++) {
                                        samples[j][i] =
                                                t[iter.getSample(xint + i, yint + j, 0) & 0xFF]
                                                        & 0xFF;
                                    }
                                }

                                data[b][pixelOffset + bandOffsets[b]] =
                                        ImageUtil.clampByte(
                                                interp.interpolate(samples, xfrac, yfrac));
                            }
                        }

                        pixelOffset += pixelStride;
                    }
                }
            }
        } finally {
            if (iter != null) {
                iter.done();
            }
        }
    }

    private void computeRectUShort(
            PlanarImage src, RasterAccessor dst, RandomIter roiIter, boolean roiContainsTile) {
        Interpolation interp =
                this.interp != null
                        ? this.interp
                        : Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        int lpad = interp.getLeftPadding();
        int rpad = interp.getRightPadding();
        int tpad = interp.getTopPadding();
        int bpad = interp.getBottomPadding();

        int minX, maxX, minY, maxY;
        RandomIter iter = null;
        try {
            if (extender != null) {
                minX = src.getMinX();
                maxX = src.getMaxX();
                minY = src.getMinY();
                maxY = src.getMaxY();
                iter = getRandomIterator(src, lpad, rpad, tpad, bpad, extender);
            } else {
                minX = src.getMinX() + lpad;
                maxX = src.getMaxX() - rpad;
                minY = src.getMinY() + tpad;
                maxY = src.getMaxY() - bpad;
                iter = getRandomIterator(src);
            }

            int kwidth = interp.getWidth();
            int kheight = interp.getHeight();

            int dstWidth = dst.getWidth();
            int dstHeight = dst.getHeight();
            int dstBands = dst.getNumBands();

            int lineStride = dst.getScanlineStride();
            int pixelStride = dst.getPixelStride();
            int[] bandOffsets = dst.getBandOffsets();
            short[][] data = dst.getShortDataArrays();

            int precH = 1 << interp.getSubsampleBitsH();
            int precV = 1 << interp.getSubsampleBitsV();

            float[] warpData = new float[2 * dstWidth];

            int[][] samples = new int[kheight][kwidth];

            int lineOffset = 0;

            short[] backgroundUShort = new short[dstBands];
            for (int i = 0; i < dstBands; i++) {
                backgroundUShort[i] = (short) backgroundValues[i];
            }

            for (int h = 0; h < dstHeight; h++) {
                int pixelOffset = lineOffset;
                lineOffset += lineStride;

                warpRect(dst.getX(), dst.getY() + h, dstWidth, 1, warpData);

                int count = 0;
                for (int w = 0; w < dstWidth; w++) {
                    float sx = warpData[count++];
                    float sy = warpData[count++];

                    int xint = floor(sx);
                    int yint = floor(sy);
                    int xfrac = (int) ((sx - xint) * precH);
                    int yfrac = (int) ((sy - yint) * precV);

                    if (xint < minX
                            || xint >= maxX
                            || yint < minY
                            || yint >= maxY
                            || !inROI(xint, yint, roiIter, roiContainsTile)) {
                        /* Fill with a background color. */
                        if (setBackground) {
                            for (int b = 0; b < dstBands; b++) {
                                data[b][pixelOffset + bandOffsets[b]] = backgroundUShort[b];
                            }
                        }
                    } else {
                        xint -= lpad;
                        yint -= tpad;

                        for (int b = 0; b < dstBands; b++) {
                            for (int j = 0; j < kheight; j++) {
                                for (int i = 0; i < kwidth; i++) {
                                    samples[j][i] = iter.getSample(xint + i, yint + j, b) & 0xFFFF;
                                }
                            }

                            data[b][pixelOffset + bandOffsets[b]] =
                                    ImageUtil.clampUShort(
                                            interp.interpolate(samples, xfrac, yfrac));
                        }
                    }

                    pixelOffset += pixelStride;
                }
            }
        } finally {
            if (iter != null) {
                iter.done();
            }
        }
    }

    private void computeRectShort(
            PlanarImage src, RasterAccessor dst, RandomIter roiIter, boolean roiContainsTile) {
        Interpolation interp =
                this.interp != null
                        ? this.interp
                        : Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        int lpad = interp.getLeftPadding();
        int rpad = interp.getRightPadding();
        int tpad = interp.getTopPadding();
        int bpad = interp.getBottomPadding();

        int minX, maxX, minY, maxY;
        RandomIter iter = null;
        try {
            if (extender != null) {
                minX = src.getMinX();
                maxX = src.getMaxX();
                minY = src.getMinY();
                maxY = src.getMaxY();
                iter = getRandomIterator(src, lpad, rpad, tpad, bpad, extender);
            } else {
                minX = src.getMinX() + lpad;
                maxX = src.getMaxX() - rpad;
                minY = src.getMinY() + tpad;
                maxY = src.getMaxY() - bpad;
                iter = getRandomIterator(src);
            }

            int kwidth = interp.getWidth();
            int kheight = interp.getHeight();

            int dstWidth = dst.getWidth();
            int dstHeight = dst.getHeight();
            int dstBands = dst.getNumBands();

            int lineStride = dst.getScanlineStride();
            int pixelStride = dst.getPixelStride();
            int[] bandOffsets = dst.getBandOffsets();
            short[][] data = dst.getShortDataArrays();

            int precH = 1 << interp.getSubsampleBitsH();
            int precV = 1 << interp.getSubsampleBitsV();

            float[] warpData = new float[2 * dstWidth];

            int[][] samples = new int[kheight][kwidth];

            int lineOffset = 0;

            short[] backgroundShort = new short[dstBands];
            for (int i = 0; i < dstBands; i++) {
                backgroundShort[i] = (short) backgroundValues[i];
            }

            for (int h = 0; h < dstHeight; h++) {
                int pixelOffset = lineOffset;
                lineOffset += lineStride;

                warpRect(dst.getX(), dst.getY() + h, dstWidth, 1, warpData);

                int count = 0;
                for (int w = 0; w < dstWidth; w++) {
                    float sx = warpData[count++];
                    float sy = warpData[count++];

                    int xint = floor(sx);
                    int yint = floor(sy);
                    int xfrac = (int) ((sx - xint) * precH);
                    int yfrac = (int) ((sy - yint) * precV);

                    if (xint < minX
                            || xint >= maxX
                            || yint < minY
                            || yint >= maxY
                            || !inROI(xint, yint, roiIter, roiContainsTile)) {
                        /* Fill with a background color. */
                        if (setBackground) {
                            for (int b = 0; b < dstBands; b++) {
                                data[b][pixelOffset + bandOffsets[b]] = backgroundShort[b];
                            }
                        }
                    } else {
                        xint -= lpad;
                        yint -= tpad;

                        for (int b = 0; b < dstBands; b++) {
                            for (int j = 0; j < kheight; j++) {
                                for (int i = 0; i < kwidth; i++) {
                                    samples[j][i] = iter.getSample(xint + i, yint + j, b);
                                }
                            }

                            data[b][pixelOffset + bandOffsets[b]] =
                                    ImageUtil.clampShort(interp.interpolate(samples, xfrac, yfrac));
                        }
                    }

                    pixelOffset += pixelStride;
                }
            }
        } finally {
            if (iter != null) {
                iter.done();
            }
        }
    }

    private void computeRectInt(
            PlanarImage src, RasterAccessor dst, RandomIter roiIter, boolean roiContainsTile) {
        Interpolation interp =
                this.interp != null
                        ? this.interp
                        : Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        int lpad = interp.getLeftPadding();
        int rpad = interp.getRightPadding();
        int tpad = interp.getTopPadding();
        int bpad = interp.getBottomPadding();

        int minX, maxX, minY, maxY;
        RandomIter iter = null;
        try {
            if (extender != null) {
                minX = src.getMinX();
                maxX = src.getMaxX();
                minY = src.getMinY();
                maxY = src.getMaxY();
                iter = getRandomIterator(src, lpad, rpad, tpad, bpad, extender);
            } else {
                minX = src.getMinX() + lpad;
                maxX = src.getMaxX() - rpad;
                minY = src.getMinY() + tpad;
                maxY = src.getMaxY() - bpad;
                iter = getRandomIterator(src);
            }

            int kwidth = interp.getWidth();
            int kheight = interp.getHeight();

            int dstWidth = dst.getWidth();
            int dstHeight = dst.getHeight();
            int dstBands = dst.getNumBands();

            int lineStride = dst.getScanlineStride();
            int pixelStride = dst.getPixelStride();
            int[] bandOffsets = dst.getBandOffsets();
            int[][] data = dst.getIntDataArrays();

            int precH = 1 << interp.getSubsampleBitsH();
            int precV = 1 << interp.getSubsampleBitsV();

            float[] warpData = new float[2 * dstWidth];

            int[][] samples = new int[kheight][kwidth];

            int lineOffset = 0;

            int[] backgroundInt = new int[dstBands];
            for (int i = 0; i < dstBands; i++) {
                backgroundInt[i] = (int) backgroundValues[i];
            }

            for (int h = 0; h < dstHeight; h++) {
                int pixelOffset = lineOffset;
                lineOffset += lineStride;

                warpRect(dst.getX(), dst.getY() + h, dstWidth, 1, warpData);

                int count = 0;
                for (int w = 0; w < dstWidth; w++) {
                    float sx = warpData[count++];
                    float sy = warpData[count++];

                    int xint = floor(sx);
                    int yint = floor(sy);
                    int xfrac = (int) ((sx - xint) * precH);
                    int yfrac = (int) ((sy - yint) * precV);

                    if (xint < minX
                            || xint >= maxX
                            || yint < minY
                            || yint >= maxY
                            || !inROI(xint, yint, roiIter, roiContainsTile)) {
                        /* Fill with a background color. */
                        if (setBackground) {
                            for (int b = 0; b < dstBands; b++) {
                                data[b][pixelOffset + bandOffsets[b]] = backgroundInt[b];
                            }
                        }
                    } else {
                        xint -= lpad;
                        yint -= tpad;

                        for (int b = 0; b < dstBands; b++) {
                            for (int j = 0; j < kheight; j++) {
                                for (int i = 0; i < kwidth; i++) {
                                    samples[j][i] = iter.getSample(xint + i, yint + j, b);
                                }
                            }

                            data[b][pixelOffset + bandOffsets[b]] =
                                    interp.interpolate(samples, xfrac, yfrac);
                        }
                    }

                    pixelOffset += pixelStride;
                }
            }
        } finally {
            if (iter != null) {
                iter.done();
            }
        }
    }

    private void computeRectFloat(
            PlanarImage src, RasterAccessor dst, RandomIter roiIter, boolean roiContainsTile) {
        Interpolation interp =
                this.interp != null
                        ? this.interp
                        : Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        int lpad = interp.getLeftPadding();
        int rpad = interp.getRightPadding();
        int tpad = interp.getTopPadding();
        int bpad = interp.getBottomPadding();

        int minX, maxX, minY, maxY;
        RandomIter iter = null;
        try {
            if (extender != null) {
                minX = src.getMinX();
                maxX = src.getMaxX();
                minY = src.getMinY();
                maxY = src.getMaxY();
                iter = getRandomIterator(src, lpad, rpad, tpad, bpad, extender);
            } else {
                minX = src.getMinX() + lpad;
                maxX = src.getMaxX() - rpad;
                minY = src.getMinY() + tpad;
                maxY = src.getMaxY() - bpad;
                iter = getRandomIterator(src);
            }

            int kwidth = interp.getWidth();
            int kheight = interp.getHeight();

            int dstWidth = dst.getWidth();
            int dstHeight = dst.getHeight();
            int dstBands = dst.getNumBands();

            int lineStride = dst.getScanlineStride();
            int pixelStride = dst.getPixelStride();
            int[] bandOffsets = dst.getBandOffsets();
            float[][] data = dst.getFloatDataArrays();

            float[] warpData = new float[2 * dstWidth];

            float[][] samples = new float[kheight][kwidth];

            int lineOffset = 0;

            float[] backgroundFloat = new float[dstBands];
            for (int i = 0; i < dstBands; i++) {
                backgroundFloat[i] = (float) backgroundValues[i];
            }

            for (int h = 0; h < dstHeight; h++) {
                int pixelOffset = lineOffset;
                lineOffset += lineStride;

                warpRect(dst.getX(), dst.getY() + h, dstWidth, 1, warpData);

                int count = 0;
                for (int w = 0; w < dstWidth; w++) {
                    float sx = warpData[count++];
                    float sy = warpData[count++];

                    int xint = floor(sx);
                    int yint = floor(sy);
                    float xfrac = sx - xint;
                    float yfrac = sy - yint;

                    if (xint < minX
                            || xint >= maxX
                            || yint < minY
                            || yint >= maxY
                            || !inROI(xint, yint, roiIter, roiContainsTile)) {
                        /* Fill with a background color. */
                        if (setBackground) {
                            for (int b = 0; b < dstBands; b++) {
                                data[b][pixelOffset + bandOffsets[b]] = backgroundFloat[b];
                            }
                        }
                    } else {
                        xint -= lpad;
                        yint -= tpad;

                        for (int b = 0; b < dstBands; b++) {
                            for (int j = 0; j < kheight; j++) {
                                for (int i = 0; i < kwidth; i++) {
                                    samples[j][i] = iter.getSampleFloat(xint + i, yint + j, b);
                                }
                            }

                            data[b][pixelOffset + bandOffsets[b]] =
                                    interp.interpolate(samples, xfrac, yfrac);
                        }
                    }

                    pixelOffset += pixelStride;
                }
            }
        } finally {
            if (iter != null) {
                iter.done();
            }
        }
    }

    private void computeRectDouble(
            PlanarImage src, RasterAccessor dst, RandomIter roiIter, boolean roiContainsTile) {
        Interpolation interp =
                this.interp != null
                        ? this.interp
                        : Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        int lpad = interp.getLeftPadding();
        int rpad = interp.getRightPadding();
        int tpad = interp.getTopPadding();
        int bpad = interp.getBottomPadding();

        int minX, maxX, minY, maxY;
        RandomIter iter = null;
        try {
            if (extender != null) {
                minX = src.getMinX();
                maxX = src.getMaxX();
                minY = src.getMinY();
                maxY = src.getMaxY();
                iter = getRandomIterator(src, lpad, rpad, tpad, bpad, extender);
            } else {
                minX = src.getMinX() + lpad;
                maxX = src.getMaxX() - rpad;
                minY = src.getMinY() + tpad;
                maxY = src.getMaxY() - bpad;
                iter = getRandomIterator(src);
            }

            int kwidth = interp.getWidth();
            int kheight = interp.getHeight();

            int dstWidth = dst.getWidth();
            int dstHeight = dst.getHeight();
            int dstBands = dst.getNumBands();

            int lineStride = dst.getScanlineStride();
            int pixelStride = dst.getPixelStride();
            int[] bandOffsets = dst.getBandOffsets();
            double[][] data = dst.getDoubleDataArrays();

            float[] warpData = new float[2 * dstWidth];

            double[][] samples = new double[kheight][kwidth];

            int lineOffset = 0;

            for (int h = 0; h < dstHeight; h++) {
                int pixelOffset = lineOffset;
                lineOffset += lineStride;

                warpRect(dst.getX(), dst.getY() + h, dstWidth, 1, warpData);

                int count = 0;
                for (int w = 0; w < dstWidth; w++) {
                    float sx = warpData[count++];
                    float sy = warpData[count++];

                    int xint = floor(sx);
                    int yint = floor(sy);
                    float xfrac = sx - xint;
                    float yfrac = sy - yint;

                    if (xint < minX
                            || xint >= maxX
                            || yint < minY
                            || yint >= maxY
                            || !inROI(xint, yint, roiIter, roiContainsTile)) {
                        /* Fill with a background color. */
                        if (setBackground) {
                            for (int b = 0; b < dstBands; b++) {
                                data[b][pixelOffset + bandOffsets[b]] = backgroundValues[b];
                            }
                        }
                    } else {
                        xint -= lpad;
                        yint -= tpad;

                        for (int b = 0; b < dstBands; b++) {
                            for (int j = 0; j < kheight; j++) {
                                for (int i = 0; i < kwidth; i++) {
                                    samples[j][i] = iter.getSampleDouble(xint + i, yint + j, b);
                                }
                            }

                            data[b][pixelOffset + bandOffsets[b]] =
                                    interp.interpolate(samples, xfrac, yfrac);
                        }
                    }

                    pixelOffset += pixelStride;
                }
            }
        } finally {
            if (iter != null) {
                iter.done();
            }
        }
    }

    /** Returns the "floor" value of a float. */
    private static final int floor(float f) {
        return f >= 0 ? (int) f : (int) f - 1;
    }

    public float[] warpRect(int x, int y, int width, int height, float[] destRect) {
        if (destRect != null && destRect.length < (width * height * 2)) {
            throw new IllegalArgumentException(
                    "warpRect: bad destRect"); // JaiI18N.getString("Warp0"));
        }
        return warpSparseRect(x, y, width, height, 1, 1, destRect);
    }

    /**
     * @param x0 The minimum X coordinate of the destination region.
     * @param y0 The minimum Y coordinate of the destination region.
     * @param width The width of the destination region.
     * @param height The height of the destination region.
     * @param periodX The horizontal sampling period.
     * @param periodY The vertical sampling period.
     * @param destRect A <code>float</code> array containing at least <code>
     *     2*((width+periodX-1)/periodX)*
     *                ((height+periodY-1)/periodY)</code> elements, or <code>null</code>. If <code>
     *     null</code>, a new array will be constructed.
     * @return A reference to the <code>destRect</code> parameter if it is non-<code>null</code>, or
     *     a new <code>float</code> array otherwise.
     */
    public float[] warpSparseRect(
            int x0, int y0, int width, int height, int periodX, int periodY, float[] destRect) {

        // XXX: This method should do its calculations in doubles
        if (destRect == null) {
            destRect =
                    new float
                            [((width + periodX - 1) / periodX)
                                    * ((height + periodY - 1) / periodY)
                                    * 2];
        }

        width += x0;
        height += y0;
        int index = 0; // destRect index

        double[] xy = new double[2];

        for (int y = y0; y < height; y += periodY) {
            for (int x = x0; x < width; x += periodX) {
                xy[0] = x;
                xy[1] = y;
                try {
                    mapSrcPoint(xy);
                    destRect[index++] = (float) xy[0];
                    destRect[index++] = (float) xy[1];
                } catch (TransformException e) {
                    LOGGER.log(Level.WARNING, "Error transforming {0}", xy);
                    destRect[index++] = Float.NaN; // ???
                    destRect[index++] = Float.NaN; // ???
                }
            }
        }

        return destRect;
    }

    private RandomIter getRandomIterator(final PlanarImage src) {
        return getRandomIterator(src, 0, 1, 0, 1, extender);
    }

    private RandomIter getRandomIterator(
            final PlanarImage src,
            int leftPad,
            int rightPad,
            int topPad,
            int bottomPad,
            BorderExtender extender) {
        return ExtendedRandomIter.getRandomIterator(
                src, leftPad, rightPad, topPad, bottomPad, extender);
    }

    /**
     * This method provides a lazy initialization of the image associated to the ROI. The method
     * uses the Double-checked locking in order to maintain thread-safety
     */
    private PlanarImage getImage() {
        if (roiImage == null) {
            synchronized (this) {
                if (roiImage == null) {
                    roiImage = roi.getAsImage();
                }
            }
        }
        return roiImage;
    }

    private boolean inROI(int x, int y, RandomIter roiIter, boolean roiContainsTile) {
        if (hasROI) {
            if (roiContainsTile) {
                return true;
            }
            if (!roiBounds.contains(x, y)) {
                return false;
            } else {
                final int sample = roiIter.getSample(x, y, 0);
                return sample > 0;
            }
        } else {
            return true;
        }
    }

    /**
     * This property generator computes the properties for the operation "GridCoverage2DRIA"
     * dynamically.
     */
    static class GridCoverage2DRIAPropertyGenerator extends PropertyGeneratorImpl {

        /** Constructor. */
        public GridCoverage2DRIAPropertyGenerator() {
            super(
                    new String[] {"ROI"},
                    new Class[] {ROI.class},
                    new Class[] {GridCoverage2DRIA.class});
        }

        /**
         * Returns the specified property.
         *
         * @param name Property name.
         * @param opNode Operation node.
         */
        public Object getProperty(String name, Object opNode) {
            validate(name, opNode);

            if (opNode instanceof GridCoverage2DRIA && name.equalsIgnoreCase("roi")) {
                GridCoverage2DRIA op = (GridCoverage2DRIA) opNode;

                // Retrieve the rendered source image and its ROI.
                RenderedImage src = op.src.getRenderedImage();
                Object property = op.getProperty("ROI");
                if (property == null
                        || property.equals(java.awt.Image.UndefinedProperty)
                        || !(property instanceof ROI)) {
                    return java.awt.Image.UndefinedProperty;
                }

                // Return undefined also if source ROI is empty.
                ROI srcROI = (ROI) property;
                if (srcROI.getBounds().isEmpty()) {
                    return java.awt.Image.UndefinedProperty;
                }

                // Retrieve the Interpolation object.
                InterpolationNearest interp =
                        (InterpolationNearest)
                                Interpolation.getInstance(Interpolation.INTERP_NEAREST);

                // Determine the effective source bounds.
                Rectangle srcBounds = null;
                PlanarImage dst = op;
                if (dst instanceof GeometricOpImage
                        && ((GeometricOpImage) dst).getBorderExtender() == null) {
                    srcBounds =
                            new Rectangle(
                                    src.getMinX() + interp.getLeftPadding(),
                                    src.getMinY() + interp.getTopPadding(),
                                    src.getWidth() - interp.getWidth() + 1,
                                    src.getHeight() - interp.getHeight() + 1);
                } else {
                    srcBounds =
                            new Rectangle(
                                    src.getMinX(), src.getMinY(), src.getWidth(), src.getHeight());
                }

                // If necessary, clip the ROI to the effective source bounds.
                if (!srcBounds.contains(srcROI.getBounds())) {
                    srcROI = srcROI.intersect(new ROIShape(srcBounds));
                }

                // Setting constant image to be warped as a ROI
                Rectangle dstBounds = op.getBounds();

                // Setting layout of the constant image
                ImageLayout2 layout = new ImageLayout2();
                int minx = (int) srcBounds.getMinX();
                int miny = (int) srcBounds.getMinY();
                int w = (int) srcBounds.getWidth();
                int h = (int) srcBounds.getHeight();
                layout.setMinX(minx);
                layout.setMinY(miny);
                layout.setWidth(w);
                layout.setHeight(h);
                RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

                final PlanarImage constantImage =
                        ConstantDescriptor.create(
                                Float.valueOf(w), Float.valueOf(h), new Byte[] {(byte) 255}, hints);

                GridCoverage2D input =
                        new GridCoverageFactory(GeoTools.getDefaultHints())
                                .create(name, constantImage, op.src.getEnvelope());
                PlanarImage roiImage = null;

                // Creating warped roi by the same way (Warp, Interpolation, source ROI) we warped
                // the
                // input image.
                roiImage = create(input, op.dst, new double[] {0d}, null, srcROI);

                ROI dstROI = new ROI(roiImage, 1);

                // If necessary, clip the warped ROI to the destination bounds.
                if (!dstBounds.contains(dstROI.getBounds())) {
                    dstROI = dstROI.intersect(new ROIShape(dstBounds));
                }

                // Return the warped and possibly clipped ROI.
                return dstROI;
            }

            return java.awt.Image.UndefinedProperty;
        }
    }
}
