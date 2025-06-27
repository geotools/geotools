/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.WritableRaster;
import java.io.Serializable;
import java.util.Arrays;
import javax.media.jai.RasterFactory;
import javax.media.jai.Warp;
import javax.media.jai.WarpGrid;
import javax.media.jai.WarpPolynomial;
import org.geotools.api.coverage.grid.GridGeometry;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.referencing.crs.DefaultDerivedCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.referencing.operation.transform.WarpTransform2D;

/**
 * A factory for {@link MathTransform2D} backed by a <cite>grid of localization</cite>. A grid of localization is a
 * two-dimensional array of coordinate points. The grid size is {@code width}&nbsp;&times;&nbsp;{@code height}. Input
 * coordinates are (<var>i</var>,<var>j</var>) index in the grid, where <var>i</var> must be in the range
 * {@code [0..width-1]} and <var>j</var> in the range {@code [0..height-1]} inclusive. Output coordinates are the values
 * stored in the grid of localization at the specified index.
 *
 * <p>The {@code LocalizationGrid} class is usefull when the "{@linkplain GridGeometry#getGridToCoordinateSystem grid to
 * coordinate system}" transform for a coverage is not some kind of global mathematical relationship like an
 * {@linkplain AffineTransform affine transform}. Instead, the "real world" coordinates are explicitly specified for
 * each pixels. If the real world coordinates are know only for some pixels at a fixed interval, then a transformation
 * can be constructed by the concatenation of an affine transform with a grid of localization.
 *
 * <p>After a {@code LocalizationGrid} object has been fully constructed (i.e. real world coordinates have been
 * specified for all grid cells), a transformation from grid coordinates to "real world" coordinates can be obtained
 * with the {@link #getMathTransform} method. If this transformation is close enough to an affine transform, then an
 * instance of {@link AffineTransform} is returned. Otherwise, a transform backed by the localization grid is returned.
 *
 * <p>The example below goes through the steps of constructing a coordinate reference system for a grid coverage from
 * its grid of localization. This example assumes that the "real world" coordinates are longitudes and latitudes on the
 * {@linkplain DefaultGeodeticDatum#WGS84 WGS84} ellipsoid.
 *
 * <blockquote>
 *
 * <table border='2' cellpadding='6'><tr><td><pre>
 * <FONT color='#008000'>//
 * // Constructs a localization grid of size 10&times;10.
 * //</FONT>
 * LocalizationGrid grid = new LocalizationGrid(10,10);
 * for (int j=0; j<10; j++) {
 *     for (int i=0; i<10; i++) {
 *         double x = ...; <FONT color='#008000'>// Set longitude here</FONT>
 *         double y = ...; <FONT color='#008000'>// Set latitude here</FONT>
 *         grid.{@linkplain #setLocalizationPoint(int,int,double,double) setLocalizationPoint}(i,j,x,y);
 *     }
 * }
 * <FONT color='#008000'>//
 * // Constructs the grid coordinate reference system. <var>degree</var> is the polynomial
 * // degree (e.g. 2) for a math transform that approximately map the grid of localization.
 * // For a more accurate (but not always better) math transform backed by the whole grid,
 * // invokes {@linkplain #getMathTransform()} instead, or use the special value of 0 for the degree
 * // argument.
 * //</FONT>
 * MathTransform2D        realToGrid = grid.{@linkplain #getPolynomialTransform(int) getPolynomialTransform}(degree).inverse();
 * CoordinateReferenceSystem realCRS = DefaultGeographicCRS.WGS84;
 * CoordinateReferenceSystem gridCRS = new {@linkplain DefaultDerivedCRS}("The grid CRS",
 *         new {@linkplain DefaultOperationMethod#DefaultOperationMethod(MathTransform) DefaultOperationMethod}(realToGrid),
 *         realCRS,     <FONT color='#008000'>// The target ("real world") CRS</FONT>
 *         realToGrid,  <FONT color='#008000'>// How the grid CRS relates to the "real world" CRS</FONT>
 *         {@linkplain DefaultCartesianCS#GRID});
 *
 * <FONT color='#008000'>//
 * // Constructs the grid coverage using the grid coordinate system (not the "real world"
 * // one). It is usefull to display the coverage in its native CRS before we resample it.
 * // Note that if the grid of localization does not define the geographic location for
 * // all pixels, then we need to specify some affine transform in place of the call to
 * // IdentityTransform. For example if the grid of localization defines the location of
 * // 1 pixel, then skip 3, then defines the location of 1 pixel, etc., then the affine
 * // transform should be AffineTransform.getScaleInstance(0.25, 0.25).
 * //</FONT>
 * {@linkplain WritableRaster} raster = {@linkplain RasterFactory}.createBandedRaster(DataBuffer.TYPE_FLOAT,
 *                                                          width, height, 1, null);
 * for (int y=0; y<height; y++) {
 *     for (int x=0; x<width; x++) {
 *         raster.setSample(x, y, 0, <cite>some_value</cite>);
 *     }
 * }
 * GridCoverageFactory factory = FactoryFinder.getGridCoverageFactory(null);
 * GridCoverage coverage = factory.create("My grayscale coverage", raster, gridCRS,
 *                          IdentityTransform.create(2), null, null, null, null, null);
 * coverage.show();
 * <FONT color='#008000'>//
 * // Projects the coverage from its current 'gridCS' to the 'realCS'. If the grid of
 * // localization was built from the orbit of some satellite, then the projected
 * // coverage will tpypically have a curved aspect.
 * //</FONT>
 * coverage = (Coverage2D) Operations.DEFAULT.resample(coverage, realCRS);
 * coverage.show();
 * </pre></td></tr></table>
 *
 * </blockquote>
 *
 * @since 2.4
 * @version $Id$
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 * @author Alessio Fabiani
 * @see org.geotools.api.referencing.crs.DerivedCRS
 */
public class LocalizationGrid implements Serializable {
    /**
     * <var>x</var> (usually longitude) offset relative to an entry. Points are stored in {@link #grid} as {@code (x,y)}
     * pairs.
     */
    private static final int X_OFFSET = LocalizationGridTransform2D.X_OFFSET;

    /**
     * <var>y</var> (usually latitude) offset relative to an entry. Points are stored in {@link #grid} as {@code (x,y)}
     * pairs.
     */
    private static final int Y_OFFSET = LocalizationGridTransform2D.Y_OFFSET;

    /**
     * Length of an entry in the {@link #grid} array. This lenght is equals to the dimension of output coordinate
     * points.
     */
    private static final int CP_LENGTH = LocalizationGridTransform2D.CP_LENGTH;

    /** Number of grid's columns. */
    private final int width;

    /** Number of grid's rows. */
    private final int height;

    /** Grid of coordinate points. Points are stored as {@code (x,y)} pairs. */
    private double[] grid;

    /**
     * A global affine transform for the whole grid. This affine transform will be computed when first requested using a
     * "least squares" fitting.
     */
    private transient AffineTransform global;

    /**
     * Math transforms from grid to "real world" data for various degrees. By convention, {@code transforms[0]} is the
     * transform backed by the whole grid. Other index are fittings using different polynomial degrees
     * ({@code transforms[1]} for affine, {@code transforms[2]} for quadratic, <cite>etc.</cite>). Will be computed only
     * when first needed.
     */
    private transient MathTransform2D[] transforms;

    /**
     * Constructs an initially empty localization grid. All "real worlds" coordinates are initially set to
     * {@code (NaN,NaN)}.
     *
     * @param width Number of grid's columns.
     * @param height Number of grid's rows.
     */
    public LocalizationGrid(final int width, final int height) {
        if (width < 2) {
            throw new IllegalArgumentException(String.valueOf(width));
        }
        if (height < 2) {
            throw new IllegalArgumentException(String.valueOf(height));
        }
        this.width = width;
        this.height = height;
        this.grid = new double[width * height * CP_LENGTH];
        Arrays.fill(grid, Float.NaN);
    }

    /**
     * Calcule l'indice d'un enregistrement dans la grille.
     *
     * @param row Coordonnee x du point.
     * @param col Coordonnee y du point.
     * @return l'indice de l'enregistrement ou du point dans la matrice.
     */
    private int computeOffset(final int col, final int row) {
        if (col < 0 || col >= width) {
            throw new IndexOutOfBoundsException(String.valueOf(col));
        }
        if (row < 0 || row >= height) {
            throw new IndexOutOfBoundsException(String.valueOf(row));
        }
        return (col + row * width) * CP_LENGTH;
    }

    /**
     * Returns the grid size. Grid coordinates are always in the range <code>
     * x<sub>input</sub>&nbsp;=&nbsp;[0..width-1]</code> and <code>
     * y<sub>input</sub>&nbsp;=&nbsp;[0..height-1]</code> inclusive.
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * Returns the "real world" coordinates for the specified grid coordinates. Grid coordinates must be integers inside
     * this grid's range. For general transformations involving non-integer grid coordinates and/or coordinates outside
     * this grid's range, use {@link #getMathTransform} instead.
     *
     * @param source The point in grid coordinates.
     * @return target The corresponding point in "real world" coordinates.
     * @throws IndexOutOfBoundsException If the source point is not in this grid's range.
     */
    public synchronized Point2D getLocalizationPoint(final Point source) {
        final int offset = computeOffset(source.x, source.y);
        return new Point2D.Double(grid[offset + X_OFFSET], grid[offset + Y_OFFSET]);
    }

    /**
     * Set a point in this localization grid.
     *
     * @param source The point in grid coordinates.
     * @param target The corresponding point in "real world" coordinates.
     * @throws IndexOutOfBoundsException If the source point is not in this grid's range.
     */
    public void setLocalizationPoint(final Point source, final Point2D target) {
        setLocalizationPoint(source.x, source.y, target.getX(), target.getY());
    }

    /**
     * Set a point in this localization grid.
     *
     * @param sourceX <var>x</var> coordinates in grid coordinates, in the range {@code [0..width-1]} inclusive.
     * @param sourceY <var>y</var> coordinates in grid coordinates. in the range {@code [0..height-1]} inclusive.
     * @param targetX <var>x</var> coordinates in "real world" coordinates.
     * @param targetY <var>y</var> coordinates in "real world" coordinates.
     * @throws IndexOutOfBoundsException If the source coordinates is not in this grid's range.
     */
    public synchronized void setLocalizationPoint(int sourceX, int sourceY, double targetX, double targetY) {
        final int offset = computeOffset(sourceX, sourceY);
        notifyChange();
        global = null;
        grid[offset + X_OFFSET] = targetX;
        grid[offset + Y_OFFSET] = targetY;
    }

    /**
     * Apply a transformation to every "real world" coordinate points in a sub-region of this grid.
     *
     * @param transform The transform to apply.
     * @param region The bounding rectangle (in grid coordinate) for region where to apply the transform, or
     *     {@code null} to transform the whole grid.
     */
    public synchronized void transform(final AffineTransform transform, final Rectangle region) {
        assert X_OFFSET == 0 : X_OFFSET;
        assert Y_OFFSET == 1 : Y_OFFSET;
        assert CP_LENGTH == 2 : CP_LENGTH;
        if (region == null) {
            transform.transform(grid, 0, grid, 0, width * height);
            return;
        }
        computeOffset(region.x, region.y); // Range check.
        int j = region.x + region.width;
        if (j > width) {
            throw new IndexOutOfBoundsException(String.valueOf(j));
        }
        j = region.y + region.height; // Range check performed in the loop.
        while (--j >= region.y) {
            final int offset = computeOffset(region.x, j);
            notifyChange();
            transform.transform(grid, offset, grid, offset, region.width);
        }
        global = null;
    }

    /** Returns {@code true} if this localization grid contains at least one {@code NaN} value. */
    public synchronized boolean isNaN() {
        for (int i = grid.length; --i >= 0; ) {
            if (Double.isNaN(grid[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if all coordinates in this grid are increasing or decreasing. More specifically, returns
     * {@code true} if the following conditions are meets:
     *
     * <ul>
     *   <li>Coordinates in a row must be increasing or decreasing. If {@code strict} is {@code true}, then coordinates
     *       must be strictly increasing or decreasing (i.e. equals value are not accepted). {@code NaN} values are
     *       always ignored.
     *   <li>Coordinates in all rows must be increasing, or coordinates in all rows must be decreasing.
     *   <li>Idem for columns (Coordinates in a columns must be increasing or decreasing, etc.).
     * </ul>
     *
     * <var>x</var> and <var>y</var> coordinates are tested independently.
     *
     * @param strict {@code true} to require strictly increasing or decreasing order, or {@code false} to accept values
     *     that are equals.
     * @return {@code true} if coordinates are increasing or decreasing in the same direction for all rows and columns.
     */
    public synchronized boolean isMonotonic(final boolean strict) {
        int orderX = INCREASING | DECREASING;
        int orderY = INCREASING | DECREASING;
        if (!strict) {
            orderX |= EQUALS;
            orderY |= EQUALS;
        }
        for (int i = 0; i < width; i++) {
            final int offset = computeOffset(i, 0);
            final int s = CP_LENGTH * width;
            if ((orderX = testOrder(grid, offset + X_OFFSET, height, s, orderX)) == 0) return false;
            if ((orderY = testOrder(grid, offset + Y_OFFSET, height, s, orderY)) == 0) return false;
        }
        orderX = INCREASING | DECREASING;
        orderY = INCREASING | DECREASING;
        if (!strict) {
            orderX |= EQUALS;
            orderY |= EQUALS;
        }
        for (int j = 0; j < height; j++) {
            final int offset = computeOffset(0, j);
            final int s = CP_LENGTH;
            if ((orderX = testOrder(grid, offset + X_OFFSET, width, s, orderX)) == 0) return false;
            if ((orderY = testOrder(grid, offset + Y_OFFSET, width, s, orderY)) == 0) return false;
        }
        return true;
    }

    /** Constant for {@link #testOrder}. */
    private static final int INCREASING = 1;
    /** Constant for {@link #testOrder}. */
    private static final int DECREASING = 2;
    /** Constant for {@link #testOrder}. */
    private static final int EQUALS = 4;

    /**
     * Checks the ordering of elements in a sub-array. {@link Float#NaN} values are ignored.
     *
     * @param grid The {link #grid} array.
     * @param offset The first element to test.
     * @param num The number of elements to test.
     * @param step The amount to increment {@code offset} in order to reach the next element.
     * @param flags A combination of {@link #INCREASING}, {@link #DECREASING} and {@link #EQUALS} that specify which
     *     ordering are accepted.
     * @return 0 if the array is unordered. Otherwise, returns {@code flags} with maybe one of {@link #INCREASING} or
     *     {@link #DECREASING} flags cleared.
     */
    private static int testOrder(final double[] grid, int offset, int num, final int step, int flags) {
        // We will check (num-1) combinations of coordinates.
        for (--num; --num >= 0; offset += step) {
            final double v1 = grid[offset];
            if (Double.isNaN(v1)) continue;
            while (true) {
                final double v2 = grid[offset + step];
                final int required, clear;
                if (v1 == v2) {
                    required = EQUALS; // "equals" must be accepted.
                    clear = ~0; // Do not clear anything.
                } else if (v2 > v1) {
                    required = INCREASING; // "increasing" must be accepted.
                    clear = ~DECREASING; // do not accepts "decreasing" anymore.
                } else if (v2 < v1) {
                    required = DECREASING; // "decreasing" must be accepted.
                    clear = ~INCREASING; // do not accepts "increasing" anymore.
                } else {
                    // 'v2' is NaN. Search for the next element.
                    if (--num < 0) {
                        return flags;
                    }
                    offset += step;
                    continue; // Mimic the "goto" statement.
                }
                if ((flags & required) == 0) {
                    return 0;
                }
                flags &= clear;
                break;
            }
        }
        return flags;
    }

    /**
     * Makes sure that the grid doesn't contains identical consecutive ordinates. If many consecutives ordinates are
     * found to be identical in a row or in a column, then the first one is left inchanged and the other ones are
     * linearly interpolated.
     */
    public void removeSingularities() {
        removeSingularities(X_OFFSET, false);
        removeSingularities(X_OFFSET, true);
        removeSingularities(Y_OFFSET, false);
        removeSingularities(Y_OFFSET, true);
    }

    /**
     * Applies a linear interpolation on consecutive identical ordinates.
     *
     * @param index The offset of the ordinate to test. Should be {@link #X_OFFSET} or {@link #Y_OFFSET}.
     * @param vertical {@code true} to scan the grid vertically, or {@code false} to scan the grid horizontally.
     */
    private void removeSingularities(final int index, final boolean vertical) {
        final int step, val1, val2;
        if (vertical) {
            step = CP_LENGTH * width;
            val1 = width;
            val2 = height;
        } else {
            step = CP_LENGTH;
            val1 = height;
            val2 = width;
        }
        for (int i = 0; i < val1; i++) {
            final int offset;
            if (vertical) {
                offset = computeOffset(i, 0) + index;
            } else {
                offset = computeOffset(0, i) + index;
            }
            int singularityOffset = -1;
            for (int j = 1; j < val2; j++) {
                final int previousOffset = offset + step * (j - 1);
                final int currentOffset = previousOffset + step;
                if (grid[previousOffset] == grid[currentOffset]) {
                    if (singularityOffset == -1) {
                        singularityOffset = previousOffset == offset ? previousOffset : previousOffset - step;
                    }
                } else if (singularityOffset != -1) {
                    final int num = (currentOffset - singularityOffset) / step + 1;
                    replaceSingularity(grid, singularityOffset, num, step);
                    singularityOffset = -1;
                }
            }
            if (singularityOffset != -1) {
                final int currentOffset = offset + step * (val2 - 1);
                final int num = (currentOffset - singularityOffset) / step + 1;
                replaceSingularity(grid, singularityOffset, num, step);
            }
        }
    }

    /**
     * Replace consecutive singularity by linear values in sub-array.
     *
     * <p>Example (we consider a grid of five element with singularity) :
     *
     * <p>before *--*--*--*--*--* |07|08|08|08|11| *--*--*--*--*--*
     *
     * <p>Params are : offset = 0, num = 5, step = 1
     *
     * <p>after *--*--*--*--*--* |07|08|09|10|11| *--*--*--*--*--* | | | | linear values are computed with these values
     *
     * @param grid The {link #grid} array.
     * @param offset The first element.
     * @param num The number of element.
     * @param step The amount to increment {@code offset} in order to reach the next element.
     */
    private static void replaceSingularity(final double[] grid, int offset, int num, final int step) {
        final double increment = (grid[offset + (num - 1) * step] - grid[offset]) / (num - 1);
        final double value = grid[offset];
        offset += step;
        for (int i = 0; i < num - 2; i++, offset += step) {
            grid[offset] = value + increment * (i + 1);
        }
    }

    /**
     * Returns an affine transform for the whole grid. This transform is only an approximation for this localization
     * grid. It is fitted (like "curve fitting") to grid data using the "least squares" method.
     *
     * @return A global affine transform as an approximation for the whole localization grid.
     */
    public synchronized AffineTransform getAffineTransform() {
        if (global == null) {
            final double[] matrix = new double[6];
            fitPlane(X_OFFSET, matrix);
            assert X_OFFSET == 0 : X_OFFSET;
            fitPlane(Y_OFFSET, matrix);
            assert Y_OFFSET == 1 : Y_OFFSET;
            global = new AffineTransform(matrix);
        }
        return (AffineTransform) global.clone();
    }

    /**
     * Fit a plane through the longitude or latitude values. More specifically, find coefficients <var>c</var>,
     * <var>cx</var> and <var>cy</var> for the following equation:
     *
     * <pre>[longitude or latitude] = c + cx*x + cy*y</pre>
     *
     * .
     *
     * <p>where <var>x</var> and <var>cx</var> are grid coordinates. Coefficients are computed using the least-squares
     * method.
     *
     * @param offset {@link X_OFFSET} for fitting longitude values, or {@link X_OFFSET} for fitting latitude values
     *     (assuming tha "real world" coordinates are longitude and latitude values).
     * @param coeff An array of length 6 in which to store plane's coefficients. Coefficients will be store in the
     *     following order:
     *     <p>{@code coeff[0 + offset] = cx;} {@code coeff[2 + offset] = cy;} {@code coeff[4 + offset] = c;}
     */
    private void fitPlane(final int offset, final double[] coeff) {
        /*
         * Compute the sum of x, y and z values. Compute also the sum of x*x, y*y, x*y, z*x and z*y
         * values. When possible, we will avoid to compute the sum inside the loop and use the
         * following identities instead:
         *
         *           1 + 2 + 3 ... + n    =    n*(n+1)/2              (arithmetic series)
         *        1² + 2² + 3² ... + n²   =    n*(n+0.5)*(n+1)/3
         */
        double z, zx, zy;
        z = zx = zy = 0; // To be computed in the loop.
        int n = offset;
        for (int yi = 0; yi < height; yi++) {
            for (int xi = 0; xi < width; xi++) {
                assert computeOffset(xi, yi) + offset == n : n;
                final double zi = grid[n];
                z += zi;
                zx += zi * xi;
                zy += zi * yi;
                n += CP_LENGTH;
            }
        }
        n = (n - offset) / CP_LENGTH;
        assert n == width * height : n;
        double x = n * (double) (width - 1) / 2;
        double y = n * (double) (height - 1) / 2;
        double xx = n * (width - 0.5) * (width - 1) / 3;
        double yy = n * (height - 0.5) * (height - 1) / 3;
        double xy = n * (double) ((height - 1) * (width - 1)) / 4;
        /*
         * Solve the following equations for cx and cy:
         *
         *    ( zx - z*x )  =  cx*(xx - x*x) + cy*(xy - x*y)
         *    ( zy - z*y )  =  cx*(xy - x*y) + cy*(yy - y*y)
         */
        zx -= z * x / n;
        zy -= z * y / n;
        xx -= x * x / n;
        xy -= x * y / n;
        yy -= y * y / n;
        final double den = xy * xy - xx * yy;
        final double cy = (zx * xy - zy * xx) / den;
        final double cx = (zy * xy - zx * yy) / den;
        final double c = (z - (cx * x + cy * y)) / n;
        coeff[0 + offset] = cx;
        coeff[2 + offset] = cy;
        coeff[4 + offset] = c;
    }

    /**
     * Returns this localization grid and its inverse as warp objects. This method tries to fit a
     * {@linkplain WarpPolynomial polynomial warp} to the gridded coordinates. The resulting Warp is wrapped into a
     * {@link WarpTransform2D}.
     */
    private MathTransform2D fitWarps(final int degree) {
        final float[] srcCoords = new float[width * height * 2];
        final float[] dstCoords = new float[srcCoords.length];
        int gridOffset = 0;
        int warpOffset = 0;
        for (int yi = 0; yi < height; yi++) {
            for (int xi = 0; xi < width; xi++) {
                assert gridOffset == computeOffset(xi, yi);
                final float x = (float) grid[gridOffset + X_OFFSET];
                final float y = (float) grid[gridOffset + Y_OFFSET];
                if (!Float.isNaN(x) && !Float.isNaN(y)) {
                    srcCoords[warpOffset] = xi;
                    srcCoords[warpOffset + 1] = yi;
                    dstCoords[warpOffset] = x;
                    dstCoords[warpOffset + 1] = y;
                    warpOffset += 2;
                }
                gridOffset += CP_LENGTH;
            }
        }
        return new WarpTransform2D(null, srcCoords, 0, null, dstCoords, 0, warpOffset / 2, degree);
    }

    /**
     * Returns a math transform from grid to "real world" coordinates using a polynomial fitting of the specified
     * degree. By convention, a {@code degree} of 0 will returns the {@linkplain #getMathTransform() math transform
     * backed by the whole grid}. Greater values will use a fitted polynomial ({@linkplain #getAffineTransform affine
     * transform} for degree 1, quadratic transform for degree 2, cubic transform for degree 3, etc.).
     *
     * @param degree The polynomial degree for the fitting, or 0 for a transform backed by the whole grid.
     */
    public synchronized MathTransform2D getPolynomialTransform(final int degree) {
        if (degree < 0 || degree >= WarpTransform2D.MAX_DEGREE + 1) {
            // TODO: provides a localized error message.
            throw new IllegalArgumentException();
        }
        if (transforms == null) {
            transforms = new MathTransform2D[WarpTransform2D.MAX_DEGREE + 1];
        }
        if (transforms[degree] == null) {
            final MathTransform2D tr;
            switch (degree) {
                case 0: {
                    // NOTE: 'grid' is not cloned. This GridLocalization's grid will need to be
                    // cloned if a "set" method is invoked after the math transform creation.
                    tr = new LocalizationGridTransform2D(width, height, grid, getAffineTransform());
                    break;
                }
                case 1: {
                    tr = (MathTransform2D) ProjectiveTransform.create(getAffineTransform());
                    break;
                }
                default: {
                    tr = fitWarps(degree);
                    break;
                }
            }
            transforms[degree] = tr;
        }
        return transforms[degree];
    }

    /**
     * Returns a math transform from grid to "real world" coordinates. The math transform is backed by the full grid of
     * localization. In terms of JAI's {@linkplain Warp image warp} operations, this math transform is backed by a
     * {@link WarpGrid} while the previous methods return math transforms backed by {@link WarpPolynomial}.
     */
    public final MathTransform2D getMathTransform() {
        return getPolynomialTransform(0);
    }

    /**
     * Notify this localization grid that a coordinate is about to be changed. This method invalidate any transforms
     * previously created.
     */
    private void notifyChange() {
        if (transforms != null) {
            if (transforms[0] != null) {
                // Clones is required only for the grid-backed transform.
                grid = grid.clone();
            }
            // Signal that all transforms need to be recomputed.
            transforms = null;
        }
    }
}
