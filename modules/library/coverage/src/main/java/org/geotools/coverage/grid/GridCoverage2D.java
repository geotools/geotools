/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.measure.Unit;
import javax.media.jai.Interpolation;
import javax.media.jai.OperationNode;
import javax.media.jai.PlanarImage;
import org.geotools.api.coverage.CannotEvaluateException;
import org.geotools.api.coverage.PointOutsideCoverageException;
import org.geotools.api.coverage.SampleDimension;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.coverage.AbstractCoverage;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.util.Classes;
import org.geotools.util.factory.Hints;

/**
 * Basic access to grid data values backed by a two-dimensional {@linkplain RenderedImage rendered image}. Each band in
 * an image is represented as a {@linkplain GridSampleDimension sample dimension}.
 *
 * <p>Grid coverages are usually two-dimensional. However, {@linkplain #getEnvelope their envelope} may have more than
 * two dimensions. For example, a remote sensing image may be valid only over some time range (the time of satellite
 * pass over the observed area). Envelopes for such grid coverage can have three dimensions: the two usual ones
 * (horizontal extent along <var>x</var> and <var>y</var>), and a third one for start time and end time (time extent
 * along <var>t</var>). However, the {@linkplain GeneralGridRange grid range} for all extra-dimension
 * <strong>must</strong> have a {@linkplain GeneralGridRange#getLength size} not greater than 1. In other words, a
 * {@code GridCoverage2D} can be a slice in a 3 dimensional grid coverage. Each slice can have an arbitrary width and
 * height (like any two-dimensional images), but only 1 voxel depth (a "voxel" is a three-dimensional pixel).
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class GridCoverage2D extends AbstractGridCoverage {
    /** For compatibility during cross-version serialization. */
    private static final long serialVersionUID = 667472989475027853L;

    /**
     * Whatever default grid range computation should be performed on transform relative to pixel center or relative to
     * pixel corner. The former is OGC convention while the later is Java convention.
     */
    private static final PixelInCell PIXEL_IN_CELL = PixelInCell.CELL_CORNER;

    /** The raster data. */
    protected final transient PlanarImage image;

    /** The grid geometry. */
    protected final GridGeometry2D gridGeometry;

    /**
     * List of sample dimension information for the grid coverage. For a grid coverage, a sample dimension is a band.
     * The sample dimension information include such things as description, data type of the value (bit, byte,
     * integer...), the no data values, minimum and maximum values and a color table if one is associated with the
     * dimension. A coverage must have at least one sample dimension.
     *
     * <p>The content of this array should never be modified.
     */
    final GridSampleDimension[] sampleDimensions;

    /**
     * Constructs a new grid coverage with the same parameter than the specified coverage. This constructor is useful
     * when creating a coverage with identical data, but in which some method has been overridden in order to process
     * data differently (e.g. interpolating them).
     *
     * @param name The name for this coverage, or {@code null} for the same than {@code coverage}.
     * @param coverage The source grid coverage.
     */
    public GridCoverage2D(final CharSequence name, final GridCoverage2D coverage) {
        super(name, coverage);
        image = coverage.image;
        gridGeometry = coverage.gridGeometry;
        sampleDimensions = coverage.sampleDimensions;
        // Do not share the views, since subclasses will create different instances.
    }

    /**
     * Constructs a grid coverage with the specified {@linkplain GridGeometry2D grid geometry} and
     * {@linkplain GridSampleDimension sample dimensions}. The {@linkplain Bounds envelope} (including the
     * {@linkplain CoordinateReferenceSystem coordinate reference system}) is inferred from the grid geometry.
     *
     * <p>This constructor accepts an optional set of properties. Keys are {@link String} objects
     * ({@link javax.media.jai.util.CaselessStringKey} are accepted as well), while values may be any {@link Object}.
     *
     * @param name The grid coverage name.
     * @param image The image.
     * @param gridGeometry The grid geometry (must contains an {@linkplain GridGeometry2D#getEnvelope envelope} with its
     *     {@linkplain GridGeometry2D#getCoordinateReferenceSystem coordinate reference system} and a
     *     "{@linkplain GridGeometry2D#getGridToCoordinateSystem grid to CRS}" transform).
     * @param bands Sample dimensions for each image band, or {@code null} for default sample dimensions. If non-null,
     *     then this array's length must matches the number of bands in {@code image}.
     * @param sources The sources for this grid coverage, or {@code null} if none.
     * @param properties The set of properties for this coverage, or {@code null} none.
     * @param hints An optional set of hints, or {@code null} if none.
     * @throws IllegalArgumentException If the number of bands differs from the number of sample dimensions.
     * @since 2.5
     */
    protected GridCoverage2D(
            final CharSequence name,
            final PlanarImage image,
            GridGeometry2D gridGeometry,
            final GridSampleDimension[] bands,
            final GridCoverage[] sources,
            final Map<?, ?> properties,
            final Hints hints)
            throws IllegalArgumentException {
        super(name, gridGeometry.getCoordinateReferenceSystem(), sources, image, properties);
        this.image = image;
        /*
         * Wraps the user-supplied sample dimensions into instances of RenderedSampleDimension. This
         * process will creates default sample dimensions if the user supplied null values. Those
         * default will be inferred from image type (integers, floats...) and range of values. If
         * an inconsistency is found in user-supplied sample dimensions, an IllegalArgumentException
         * is thrown.
         */
        sampleDimensions = new GridSampleDimension[image.getNumBands()];
        RenderedSampleDimension.create(name, image, bands, sampleDimensions);
        /*
         * Computes the grid range if it was not explicitly provided. The range will be inferred
         * from the image size, if needed. The envelope computation (if needed) requires a valid
         * 'gridToCRS' transform in the GridGeometry object. In any case, the envelope must be
         * non-empty and its dimension must matches the coordinate reference system's dimension.
         */
        final int dimension = crs.getCoordinateSystem().getDimension();
        if (!gridGeometry.isDefined(GridGeometry2D.GRID_RANGE_BITMASK)) {
            final GridEnvelope r = new GeneralGridEnvelope(image, dimension);
            if (gridGeometry.isDefined(GridGeometry2D.GRID_TO_CRS_BITMASK)) {
                gridGeometry =
                        new GridGeometry2D(r, PIXEL_IN_CELL, gridGeometry.getGridToCRS(PIXEL_IN_CELL), crs, hints);
            } else {
                /*
                 * If the math transform was not explicitly specified by the user, then it will be
                 * computed from the envelope. In this case, some heuristic rules are used in order
                 * to decide if we should reverse some axis directions or swap axis.
                 */
                gridGeometry = new GridGeometry2D(r, gridGeometry.getEnvelope());
            }
        } else {
            /*
             * Makes sure that the 'gridToCRS' transform is defined.
             * An exception will be thrown otherwise.
             */
            gridGeometry.getGridToCRS();
        }
        this.gridGeometry = gridGeometry;
        assert gridGeometry.isDefined(GridGeometry2D.CRS_BITMASK
                | GridGeometry2D.ENVELOPE_BITMASK
                | GridGeometry2D.GRID_RANGE_BITMASK
                | GridGeometry2D.GRID_TO_CRS_BITMASK);
        /*
         * Last argument checks. The image size must be consistent with the grid range
         * and the envelope must be non-empty.
         */
        final String error = GridGeometry2D.checkConsistency(image, gridGeometry);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        if (dimension <= Math.max(gridGeometry.axisDimensionX, gridGeometry.axisDimensionY)
                || !(gridGeometry.envelope.getSpan(gridGeometry.axisDimensionX) > 0)
                || !(gridGeometry.envelope.getSpan(gridGeometry.axisDimensionY) > 0)) {
            throw new IllegalArgumentException(ErrorKeys.EMPTY_ENVELOPE);
        }
    }

    /**
     * Returns {@code true} if grid data can be edited. The default implementation returns {@code true} if
     * {@link #image} is an instance of {@link WritableRenderedImage}.
     */
    @Override
    public boolean isDataEditable() {
        return image instanceof WritableRenderedImage;
    }

    /**
     * Returns information for the grid coverage geometry. Grid geometry includes the valid range of grid coordinates
     * and the georeferencing.
     */
    @Override
    public GridGeometry2D getGridGeometry() {
        final String error = GridGeometry2D.checkConsistency(image, gridGeometry);
        if (error != null) {
            throw new IllegalStateException(error);
        }
        return gridGeometry;
    }

    /**
     * Returns the bounding box for the coverage domain in coordinate reference system coordinates. The returned
     * envelope have at least two dimensions. It may have more dimensions if the coverage has some extent in other
     * dimensions (for example a depth, or a start and end time).
     */
    @Override
    public Bounds getEnvelope() {
        return gridGeometry.getEnvelope();
    }

    /**
     * Returns the two-dimensional bounding box for the coverage domain in coordinate reference system coordinates. If
     * the coverage envelope has more than two dimensions, only the dimensions used in the underlying rendered image are
     * returned.
     *
     * @return The two-dimensional bounding box.
     */
    public ReferencedEnvelope getEnvelope2D() {
        return gridGeometry.getEnvelope2D();
    }

    /**
     * Returns the two-dimensional part of this grid coverage CRS. If the {@linkplain #getCoordinateReferenceSystem
     * complete CRS} is two-dimensional, then this method returns the same CRS. Otherwise it returns a CRS for the two
     * first axis having a {@linkplain GridRange#length length} greater than 1 in the grid range. Note that those axis
     * are garanteed to appears in the same order than in the complete CRS.
     *
     * @return The two-dimensional part of the grid coverage CRS.
     * @see #getCoordinateReferenceSystem
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem2D() {
        return gridGeometry.getCoordinateReferenceSystem2D();
    }

    /** Returns the number of bands in the grid coverage. */
    @Override
    public int getNumSampleDimensions() {
        return sampleDimensions.length;
    }

    /**
     * Retrieve sample dimension information for the coverage. For a grid coverage, a sample dimension is a band. The
     * sample dimension information include such things as description, data type of the value (bit, byte, integer...),
     * the no data values, minimum and maximum values and a color table if one is associated with the dimension. A
     * coverage must have at least one sample dimension.
     */
    @Override
    public GridSampleDimension getSampleDimension(final int index) {
        return sampleDimensions[index];
    }

    /**
     * Returns all sample dimensions for this grid coverage.
     *
     * @return All sample dimensions.
     */
    public GridSampleDimension[] getSampleDimensions() {
        return sampleDimensions.clone();
    }

    /**
     * Returns the interpolation used for all {@code evaluate(...)} methods. The default implementation returns
     * {@link javax.media.jai.InterpolationNearest}.
     *
     * @return The interpolation.
     */
    public Interpolation getInterpolation() {
        return Interpolation.getInstance(Interpolation.INTERP_NEAREST);
    }

    /**
     * Returns the value vector for a given location (world coordinates). A value for each sample dimension is included
     * in the vector.
     */
    @Override
    public Object evaluate(final Position point) throws CannotEvaluateException {
        final int dataType = image.getSampleModel().getDataType();
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                return evaluate(point, (byte[]) null);
            case DataBuffer.TYPE_SHORT: // Fall through
            case DataBuffer.TYPE_USHORT: // Fall through
            case DataBuffer.TYPE_INT:
                return evaluate(point, (int[]) null);
            case DataBuffer.TYPE_FLOAT:
                return evaluate(point, (float[]) null);
            case DataBuffer.TYPE_DOUBLE:
                return evaluate(point, (double[]) null);
            default:
                throw new CannotEvaluateException();
        }
    }

    /**
     * Returns a sequence of byte values for a given location (world coordinates).
     *
     * @param coord World coordinates of the location to evaluate.
     * @param dest An array in which to store values, or {@code null}.
     * @return An array containing values.
     * @throws CannotEvaluateException if the values can't be computed at the specified coordinate. More specifically,
     *     {@link PointOutsideCoverageException} is thrown if the evaluation failed because the input point has invalid
     *     coordinates.
     */
    @Override
    public byte[] evaluate(final Position coord, byte[] dest) throws CannotEvaluateException {
        final int[] array = evaluate(coord, (int[]) null);
        if (dest == null) {
            dest = new byte[array.length];
        }
        for (int i = 0; i < array.length; i++) {
            dest[i] = (byte) array[i];
        }
        return dest;
    }

    /**
     * Returns a sequence of integer values for a given location (world coordinates).
     *
     * @param coord World coordinates of the location to evaluate.
     * @param dest An array in which to store values, or {@code null}.
     * @return An array containing values.
     * @throws CannotEvaluateException if the values can't be computed at the specified coordinate. More specifically,
     *     {@link PointOutsideCoverageException} is thrown if the evaluation failed because the input point has invalid
     *     coordinates.
     */
    @Override
    public int[] evaluate(final Position coord, final int[] dest) throws CannotEvaluateException {
        return evaluate(gridGeometry.toPoint2D(coord), dest);
    }

    /**
     * Returns a sequence of float values for a given location (world coordinates).
     *
     * @param coord World coordinates of the location to evaluate.
     * @param dest An array in which to store values, or {@code null}.
     * @return An array containing values.
     * @throws CannotEvaluateException if the values can't be computed at the specified coordinate. More specifically,
     *     {@link PointOutsideCoverageException} is thrown if the evaluation failed because the input point has invalid
     *     coordinates.
     */
    @Override
    public float[] evaluate(final Position coord, final float[] dest) throws CannotEvaluateException {
        return evaluate(gridGeometry.toPoint2D(coord), dest);
    }

    /**
     * Returns a sequence of double values for a given location (world coordinates).
     *
     * @param coord World coordinates of the location to evaluate.
     * @param dest An array in which to store values, or {@code null}.
     * @return An array containing values.
     * @throws CannotEvaluateException if the values can't be computed at the specified coordinate. More specifically,
     *     {@link PointOutsideCoverageException} is thrown if the evaluation failed because the input point has invalid
     *     coordinates.
     */
    @Override
    public double[] evaluate(final Position coord, final double[] dest) throws CannotEvaluateException {
        return evaluate(gridGeometry.toPoint2D(coord), dest);
    }

    /**
     * Returns a sequence of integer values for a given location (world coordinates).
     *
     * @param coord World coordinates of the location to evaluate.
     * @param dest An array in which to store values, or {@code null}.
     * @return An array containing values.
     * @throws CannotEvaluateException if the values can't be computed at the specified coordinate. More specifically,
     *     {@link PointOutsideCoverageException} is thrown if the evaluation failed because the input point has invalid
     *     coordinates.
     */
    public int[] evaluate(final Point2D coord, final int[] dest) throws CannotEvaluateException {
        final Point2D pixel = gridGeometry.inverseTransform(coord);
        final double fx = pixel.getX();
        final double fy = pixel.getY();
        if (!Double.isNaN(fx) && !Double.isNaN(fy)) {
            final int x = (int) Math.round(fx);
            final int y = (int) Math.round(fy);
            if (image.getBounds().contains(x, y)) { // getBounds() returns a cached instance.
                return image.getTile(image.XToTileX(x), image.YToTileY(y)).getPixel(x, y, dest);
            }
        }
        throw new PointOutsideCoverageException(formatEvaluateError(coord, true));
    }

    /**
     * Returns a sequence of float values for a given location (world coordinates).
     *
     * @param coord World coordinates of the location to evaluate.
     * @param dest An array in which to store values, or {@code null}.
     * @return An array containing values.
     * @throws CannotEvaluateException if the values can't be computed at the specified coordinate. More specifically,
     *     {@link PointOutsideCoverageException} is thrown if the evaluation failed because the input point has invalid
     *     coordinates.
     */
    public float[] evaluate(final Point2D coord, final float[] dest) throws CannotEvaluateException {
        final Point2D pixel = gridGeometry.inverseTransform(coord);
        final double fx = pixel.getX();
        final double fy = pixel.getY();
        if (!Double.isNaN(fx) && !Double.isNaN(fy)) {
            final int x = (int) Math.round(fx);
            final int y = (int) Math.round(fy);
            if (image.getBounds().contains(x, y)) { // getBounds() returns a cached instance.
                return image.getTile(image.XToTileX(x), image.YToTileY(y)).getPixel(x, y, dest);
            }
        }
        throw new PointOutsideCoverageException(formatEvaluateError(coord, true));
    }

    /**
     * Returns a sequence of double values for a given location (world coordinates).
     *
     * @param coord World coordinates of the location to evaluate.
     * @param dest An array in which to store values, or {@code null}.
     * @return An array containing values.
     * @throws CannotEvaluateException if the values can't be computed at the specified coordinate. More specifically,
     *     {@link PointOutsideCoverageException} is thrown if the evaluation failed because the input point has invalid
     *     coordinates.
     */
    public double[] evaluate(final Point2D coord, final double[] dest) throws CannotEvaluateException {
        final Point2D pixel = gridGeometry.inverseTransform(coord);
        final double fx = pixel.getX();
        final double fy = pixel.getY();
        if (!Double.isNaN(fx) && !Double.isNaN(fy)) {
            final int x = (int) Math.round(fx);
            final int y = (int) Math.round(fy);
            if (image.getBounds().contains(x, y)) { // getBounds() returns a cached instance.
                return image.getTile(image.XToTileX(x), image.YToTileY(y)).getPixel(x, y, dest);
            }
        }
        throw new PointOutsideCoverageException(formatEvaluateError(coord, true));
    }

    /**
     * Return sample dimension (band) values as an array of integers for the given <b>grid</b> location. The range of
     * valid grid coordinates can be retrieved as in this example:
     *
     * <pre><code>
     * GridEnvelope2D gridBounds = coverage.getGridGeometry2D().getGridRange();
     * </code></pre>
     *
     * @param coord grid (ie. pixel) coordinates
     * @param dest an optionally pre-allocated array; if non-null, its length should be equal to the number of bands
     *     (sample dimensions)
     * @return band values for the given grid (pixel) location
     * @throws PointOutsideCoverageException if the supplied coords are outside the grid bounds
     */
    public int[] evaluate(final GridCoordinates2D coord, final int[] dest) {
        if (image.getBounds().contains(coord.x, coord.y)) {
            return image.getTile(image.XToTileX(coord.x), image.YToTileY(coord.y))
                    .getPixel(coord.x, coord.y, dest);
        }

        throw new PointOutsideCoverageException(formatEvaluateError(coord, true));
    }

    /**
     * Return sample dimension (band) values as an array of floats for the given <b>grid</b> location. The range of
     * valid grid coordinates can be retrieved as in this example:
     *
     * <pre><code>
     * GridEnvelope2D gridBounds = coverage.getGridGeometry2D().getGridRange();
     * </code></pre>
     *
     * @param coord grid (ie. pixel) coordinates
     * @param dest an optionally pre-allocated array; if non-null, its length should be equal to the number of bands
     *     (sample dimensions)
     * @return band values for the given grid (pixel) location
     * @throws PointOutsideCoverageException if the supplied coords are outside the grid bounds
     */
    public float[] evaluate(final GridCoordinates2D coord, final float[] dest) {
        if (image.getBounds().contains(coord.x, coord.y)) {
            return image.getTile(image.XToTileX(coord.x), image.YToTileY(coord.y))
                    .getPixel(coord.x, coord.y, dest);
        }

        throw new PointOutsideCoverageException(formatEvaluateError(coord, true));
    }

    /**
     * Return sample dimension (band) values as an array of doubles for the given <b>grid</b> location. The range of
     * valid grid coordinates can be retrieved as in this example:
     *
     * <pre><code>
     * GridEnvelope2D gridBounds = coverage.getGridGeometry2D().getGridRange();
     * </code></pre>
     *
     * @param coord grid (ie. pixel) coordinates
     * @param dest an optionally pre-allocated array; if non-null, its length should be equal to the number of bands
     *     (sample dimensions)
     * @return band values for the given grid (pixel) location
     * @throws PointOutsideCoverageException if the supplied coords are outside the grid bounds
     */
    public double[] evaluate(final GridCoordinates2D coord, final double[] dest) {
        if (image.getBounds().contains(coord.x, coord.y)) {
            return image.getTile(image.XToTileX(coord.x), image.YToTileY(coord.y))
                    .getPixel(coord.x, coord.y, dest);
        }

        throw new PointOutsideCoverageException(formatEvaluateError(coord, true));
    }

    /**
     * Returns a debug string for the specified coordinate. This method produces a string with pixel coordinates and
     * pixel values for all bands (with geophysics values or category name in parenthesis). Example for a 1-banded
     * image:
     *
     * <blockquote>
     *
     * <pre>(1171,1566)=[196 (29.6 °C)]</pre>
     *
     * </blockquote>
     *
     * @param coord The coordinate point where to evaluate.
     * @return A string with pixel coordinates and pixel values at the specified location, or {@code null} if
     *     {@code coord} is outside coverage.
     */
    public synchronized String getDebugString(final Position coord) {
        Point2D pixel = gridGeometry.toPoint2D(coord);
        pixel = gridGeometry.inverseTransform(pixel);
        final int x = (int) Math.round(pixel.getX());
        final int y = (int) Math.round(pixel.getY());
        if (image.getBounds().contains(x, y)) { // getBounds() returns a cached instance.
            final int numBands = image.getNumBands();
            final Raster raster = image.getTile(image.XToTileX(x), image.YToTileY(y));
            final int datatype = image.getSampleModel().getDataType();
            final StringBuilder buffer = new StringBuilder();
            buffer.append('(').append(x).append(',').append(y).append(")=[");
            for (int band = 0; band < numBands; band++) {
                if (band != 0) {
                    buffer.append(";\u00A0");
                }
                final double sample = raster.getSampleDouble(x, y, band);
                switch (datatype) {
                    case DataBuffer.TYPE_DOUBLE:
                        buffer.append(sample);
                        break;
                    case DataBuffer.TYPE_FLOAT:
                        buffer.append((float) sample);
                        break;
                    default:
                        buffer.append((int) sample);
                        break;
                }
                final String formatted = sampleDimensions[band].getLabel(sample, null);
                if (formatted != null) {
                    buffer.append("\u00A0(").append(formatted).append(')');
                }
            }
            return buffer.append(']').toString();
        }
        return null;
    }

    /**
     * Returns the optimal size to use for each dimension when accessing grid values. The default implementation returns
     * the image's tiles size.
     */
    @Override
    public int[] getOptimalDataBlockSizes() {
        final int[] size = new int[getDimension()];
        Arrays.fill(size, 1);
        size[gridGeometry.gridDimensionX] = image.getTileWidth();
        size[gridGeometry.gridDimensionY] = image.getTileHeight();
        return size;
    }

    /**
     * Returns grid data as a rendered image.
     *
     * @return The grid data as a rendered image.
     */
    @Override
    public RenderedImage getRenderedImage() {
        return image;
    }

    /**
     * Returns 2D view of this grid coverage as a renderable image. This method allows interoperability with Java2D.
     *
     * @param xAxis Dimension to use for <var>x</var> axis.
     * @param yAxis Dimension to use for <var>y</var> axis.
     * @return A 2D view of this grid coverage as a renderable image.
     */
    @Override
    public RenderableImage getRenderableImage(final int xAxis, final int yAxis) {
        if (xAxis == gridGeometry.axisDimensionX && yAxis == gridGeometry.axisDimensionY) {
            return new Renderable();
        } else {
            return super.getRenderableImage(xAxis, yAxis);
        }
    }

    /** {inheritDoc} */
    @Override
    public void show(String title, final int xAxis, final int yAxis) {
        if (title == null || (title = title.trim()).length() == 0) {
            final StringBuilder buffer = new StringBuilder(String.valueOf(getName()));
            final int visibleBandIndex = CoverageUtilities.getVisibleBand(this);
            final SampleDimension visibleBand = getSampleDimension(visibleBandIndex);
            final Unit<?> unit = visibleBand.getUnits();
            buffer.append(" - ").append(String.valueOf(visibleBand.getDescription()));
            if (unit != null) {
                buffer.append(" (").append(unit).append(')');
            }
            title = buffer.toString();
        }
        super.show(title, xAxis, yAxis);
    }

    /** {inheritDoc} */
    @Override
    public void show(final String title) {
        show(title, gridGeometry.axisDimensionX, gridGeometry.axisDimensionY);
    }

    /**
     * A view of a {@linkplain GridCoverage2D grid coverage} as a renderable image. Renderable images allow
     * interoperability with <A HREF="http://java.sun.com/products/java-media/2D/">Java2D</A> for a two-dimensional
     * slice of a grid coverage.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     * @see AbstractCoverage#getRenderableImage
     * @todo Override {@link #createRendering} and use the affine transform operation. Also uses the JAI's "Transpose"
     *     operation is x and y axis are interchanged.
     */
    protected class Renderable extends AbstractCoverage.Renderable {
        /** For compatibility during cross-version serialization. */
        private static final long serialVersionUID = 4544636336787905450L;

        /** Constructs a renderable image. */
        public Renderable() {
            super(gridGeometry.axisDimensionX, gridGeometry.axisDimensionY);
        }

        /**
         * Returns a rendered image with a default width and height in pixels.
         *
         * @return A rendered image containing the rendered data
         */
        @Override
        public RenderedImage createDefaultRendering() {
            if (xAxis == gridGeometry.axisDimensionX && yAxis == gridGeometry.axisDimensionY) {
                return getRenderedImage();
            }
            return super.createDefaultRendering();
        }
    }

    /**
     * Hints that the given area may be needed in the near future. Some implementations may spawn a thread or threads to
     * compute the tiles while others may ignore the hint.
     *
     * @param area A rectangle indicating which geographic area to prefetch. This area's coordinates must be expressed
     *     according the grid coverage's coordinate reference system, as given by {@link #getCoordinateReferenceSystem}.
     */
    public void prefetch(final Rectangle2D area) {
        final Point[] tileIndices = image.getTileIndices(gridGeometry.inverseTransform(area));
        if (tileIndices != null) {
            image.prefetchTiles(tileIndices);
        }
    }

    /**
     * Provides a hint that a coverage will no longer be accessed from a reference in user space. This method
     * {@linkplain PlanarImage#dispose disposes} the {@linkplain #image} only if at least one of the following
     * conditions is true (otherwise this method do nothing):
     *
     * <p>
     *
     * <ul>
     *   <li>{@code force} is {@code true}, <strong>or</strong>
     *   <li>The underlying {@linkplain #image} has no {@linkplain PlanarImage#getSinks sinks}.
     * </ul>
     *
     * <p>This safety check helps to prevent the disposal of an {@linkplain #image} that still used in a JAI operation
     * chain. It doesn't prevent the disposal in every cases however. When unsure about whatever a coverage is still in
     * use or not, it is safer to not invoke this method and rely on the garbage collector instead.
     *
     * @see PlanarImage#dispose
     * @since 2.4
     */
    @Override
    public synchronized boolean dispose(final boolean force) {
        if (!disposeImage(force)) {
            return false;
        }
        return super.dispose(force);
    }

    /**
     * Disposes only the {@linkplain #image}, not the views. This method is invoked by {@link ViewsManager#dispose}.
     * This method checks the set of every sinks, which may or may not be {@link RenderedImage}s. If there is no sinks,
     * we can process.
     */
    final synchronized boolean disposeImage(final boolean force) {
        if (!force) {
            Collection<?> sinks = image.getSinks();
            if (sinks != null && !sinks.isEmpty()) {
                return false;
            }
        }
        image.dispose();
        return true;
    }

    /**
     * Returns a string representation of this grid coverage. This is mostly for debugging purpose and may change in any
     * future version.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(super.toString());
        final String lineSeparator = System.getProperty("line.separator", "\n");
        buffer.append("\u2514 Image=").append(Classes.getShortClassName(image)).append('[');
        if (image instanceof OperationNode) {
            buffer.append('"')
                    .append(((OperationNode) image).getOperationName())
                    .append('"');
        }
        buffer.append(']');
        return buffer.append(lineSeparator).toString();
    }
}
