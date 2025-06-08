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

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.referencing.operation.Transformation;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.referencing.operation.matrix.Matrix2;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;

/**
 * Transform a set of coordinate points using a grid of localization. Input coordinates are index in this
 * two-dimensional array. Those input coordinates (or index) should be in the range <code>
 * x</sub>input</sub>&nbsp;=&nbsp;[0..width-1]</code> and <code>
 * y</sub>input</sub>&nbsp;=&nbsp;[0..height-1]</code> inclusive,
 *
 * <p>where {@code width} and {@code height} are the number of columns and rows in the grid of localization. Output
 * coordinates are the values stored in the grid of localization at the specified index. If input coordinates (index)
 * are non-integer values, then output coordinates are interpolated using a bilinear interpolation. If input coordinates
 * are outside the grid range, then output coordinates are extrapolated.
 *
 * @since 2.0
 * @version $Id$
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 * @todo This class should extends {@link WarpTransform2D} and constructs a {@link javax.media.jai.WarpGrid} the first
 *     time the {@link WarpTransform2D#getWarp()} method is invoked (GEOT-522).
 */
final class LocalizationGridTransform2D extends AbstractMathTransform implements MathTransform2D, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 1067560328828441295L;

    /** Maximal number of iterations to try before to fail during an inverse transformation. */
    private static final int MAX_ITER = 40;

    /** Set to {@code true} for a conservative (and maybe slower) algorithm in {@link #inverseTransform}. */
    private static final boolean CONSERVATIVE = true;

    /**
     * Set to {@code true} for forcing {@link #inverseTransform} to returns a value instead of throwing an exception if
     * the transform do not converge. This is a temporary flag until we find why the inverse transform fails to converge
     * in some case.
     */
    private static final boolean MASK_NON_CONVERGENCE;

    static {
        String property;
        try {
            property = System.getProperty("org.geotools.referencing.forceConvergence", "false");
        } catch (SecurityException exception) {
            // We are running in an applet.
            property = "false";
        }
        MASK_NON_CONVERGENCE = property.equalsIgnoreCase("true");
    }

    /**
     * <var>x</var> (usually longitude) offset relative to an entry. Points are stored in {@link #grid} as {@code (x,y)}
     * pairs.
     */
    static final int X_OFFSET = 0;

    /**
     * <var>y</var> (usually latitude) offset relative to an entry. Points are stored in {@link #grid} as {@code (x,y)}
     * pairs.
     */
    static final int Y_OFFSET = 1;

    /**
     * Length of an entry in the {@link #grid} array. This lenght is equals to the dimension of output coordinate
     * points.
     */
    static final int CP_LENGTH = 2;

    /** Number of grid's columns. */
    private final int width;

    /** Number of grid's rows. */
    private final int height;

    /** Grid of coordinate points. Points are stored as {@code (x,y)} pairs. */
    private final double[] grid;

    /** A global affine transform for the whole grid. */
    private final AffineTransform global;

    /** The inverse math transform. Will be constructed only when first requested. */
    private transient MathTransform2D inverse;

    /**
     * Constructs a localization grid using the specified data.
     *
     * @param width Number of grid's columns.
     * @param height Number of grid's rows.
     * @param grid The localization grid as an array of {@code (x,y)} coordinates. This array is not cloned; this is the
     *     caller's responsability to ensure that it will not be modified as long as this transformation is strongly
     *     reachable.
     * @param global A global affine transform for the whole grid.
     */
    LocalizationGridTransform2D(final int width, final int height, final double[] grid, final AffineTransform global) {
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.global = global;
    }

    /** Returns the parameter descriptors for this math transform. */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /** Returns the dimension of input points. */
    @Override
    public int getSourceDimensions() {
        return 2;
    }

    /** Returns the dimension of output points. */
    @Override
    public int getTargetDimensions() {
        return 2;
    }

    /** Tests if this transform is the identity transform. */
    @Override
    public boolean isIdentity() {
        return false;
    }

    /** Gets the derivative of this transform at a point. */
    @Override
    public Matrix derivative(final Point2D point) {
        final AffineTransform tr = new AffineTransform();
        getAffineTransform(point.getX(), point.getY(), tr);
        return new Matrix2(
                tr.getScaleX(), tr.getShearX(),
                tr.getShearY(), tr.getScaleY());
    }

    /**
     * Transforme des coordonnées sources (généralement des index de pixels) en coordonnées destinations (généralement
     * des degrés de longitude et latitude). Les transformations feront intervenir des interpolations linéaires si les
     * coordonnées sources ne sont pas entières.
     *
     * @param srcPts Points d'entrée.
     * @param srcOff Index du premier point d'entrée à transformer.
     * @param dstPts Points de sortie.
     * @param dstOff Index du premier point de sortie.
     * @param numPts Nombre de points à transformer.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff, final float[] dstPts, int dstOff, int numPts) {
        transform(srcPts, null, srcOff, dstPts, null, dstOff, numPts);
    }

    /**
     * Transforme des coordonnées sources (généralement des index de pixels) en coordonnées destinations (généralement
     * des degrés de longitude et latitude). Les transformations feront intervenir des interpolations linéaires si les
     * coordonnées sources ne sont pas entières.
     *
     * @param srcPts Points d'entrée.
     * @param srcOff Index du premier point d'entrée à transformer.
     * @param dstPts Points de sortie.
     * @param dstOff Index du premier point de sortie.
     * @param numPts Nombre de points à transformer.
     */
    @Override
    public void transform(final double[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts) {
        transform(null, srcPts, srcOff, null, dstPts, dstOff, numPts);
    }

    /** Implementation of direct transformation. */
    private void transform(
            final float[] srcPts1,
            final double[] srcPts2,
            int srcOff,
            final float[] dstPts1,
            final double[] dstPts2,
            int dstOff,
            int numPts) {
        final int minCol = 0;
        final int minRow = 0;
        final int maxCol = width - 2;
        final int maxRow = height - 2;
        int postIncrement = 0;
        if (srcOff < dstOff) {
            if (srcPts2 != null ? srcPts2 == dstPts2 : srcPts1 == dstPts1) {
                srcOff += (numPts - 1) * 2;
                dstOff += (numPts - 1) * 2;
                postIncrement = -4;
            }
        }
        while (--numPts >= 0) {
            final double xi, yi;
            if (srcPts2 != null) {
                xi = srcPts2[srcOff++];
                yi = srcPts2[srcOff++];
            } else {
                xi = srcPts1[srcOff++];
                yi = srcPts1[srcOff++];
            }
            final int col = Math.max(Math.min((int) xi, maxCol), minCol);
            final int row = Math.max(Math.min((int) yi, maxRow), minRow);
            final int offset00 = (col + row * width) * CP_LENGTH;
            final int offset01 = offset00 + CP_LENGTH * width; // Une ligne plus bas
            final int offset10 = offset00 + CP_LENGTH; // Une colonne à droite
            final int offset11 = offset01 + CP_LENGTH; // Une colonne à droite, une ligne plus bas
            /*
             * Interpole les coordonnées de destination        [00]--.(x0,y0)----[10]
             * sur la ligne courante (x0,y0)  ainsi que         |                  |
             * sur la ligne suivante (x1,y1).   Exemple         |    .(x,y)        |
             * ci-contre:  les coordonnées sources sont         |                  |
             * entre crochets, et les coordonnées de la        [01]--.(x1,y1)----[11]
             * sortie (à calculer) sont entre parenthèses.
             */
            final double x0 =
                    linearInterpolation(col + 0, grid[offset00 + X_OFFSET], col + 1, grid[offset10 + X_OFFSET], xi);
            final double y0 =
                    linearInterpolation(col + 0, grid[offset00 + Y_OFFSET], col + 1, grid[offset10 + Y_OFFSET], xi);
            final double x1 =
                    linearInterpolation(col + 0, grid[offset01 + X_OFFSET], col + 1, grid[offset11 + X_OFFSET], xi);
            final double y1 =
                    linearInterpolation(col + 0, grid[offset01 + Y_OFFSET], col + 1, grid[offset11 + Y_OFFSET], xi);
            /*
             * Interpole maintenant les coordonnées (x,y) entre les deux lignes.
             */
            final double xf = linearInterpolation(row, x0, row + 1, x1, yi);
            final double yf = linearInterpolation(row, y0, row + 1, y1, yi);
            if (dstPts2 != null) {
                dstPts2[dstOff++] = xf;
                dstPts2[dstOff++] = yf;
            } else {
                dstPts1[dstOff++] = (float) xf;
                dstPts1[dstOff++] = (float) yf;
            }
            srcOff += postIncrement;
            dstOff += postIncrement;
        }
    }

    /**
     * Interpole/extrapole entre deux points.
     *
     * @param x1 Coordonnee <var>x</var> du premier point.
     * @param y1 Coordonnee <var>y</var> du premier point.
     * @param x2 Coordonnee <var>x</var> du second point.
     * @param y2 Coordonnee <var>y</var> du second point.
     * @param x Position <var>x</var> à laquelle calculer la valeur de <var>y</var>.
     * @return La valeur <var>y</var> interpolée entre les deux points.
     */
    private static double linearInterpolation(
            final double x1, final double y1, final double x2, final double y2, final double x) {
        return y1 + (y2 - y1) / (x2 - x1) * (x - x1);
    }

    /**
     * Retourne une approximation de la transformation affine à la position indiquée.
     *
     * @param x Coordonnee <var>x</var> du point.
     * @param y Coordonnee <var>y</var> du point.
     * @param dest Matrice dans laquelle écrire la transformation affine.
     */
    private void getAffineTransform(double x, double y, final AffineTransform dest) {
        int col = (int) x;
        int row = (int) y;
        if (col > width - 2) col = width - 2;
        if (row > height - 2) row = height - 2;
        if (col < 0) col = 0;
        if (row < 0) row = 0;
        final int sgnCol;
        final int sgnRow;
        if (x - col > 0.5) {
            sgnCol = -1;
            col++;
        } else sgnCol = +1;
        if (y - row > 0.5) {
            sgnRow = -1;
            row++;
        } else sgnRow = +1;
        /*
         * Le calcul de la transformation affine  comprend 6        P00------P10
         * inconnues. Sa solution recquiert donc 6 équations.        |        |
         * Nous les obtenons en utilisant 3 points,   chaque         |        |
         * points ayant 2 coordonnées. Voir exemple ci-contre:      P01----(ignoré)
         */
        final int offset00 = (col + row * width) * CP_LENGTH;
        final int offset01 = offset00 + sgnRow * CP_LENGTH * width;
        final int offset10 = offset00 + sgnCol * CP_LENGTH;
        x = grid[offset00 + X_OFFSET];
        y = grid[offset00 + Y_OFFSET];
        final double dxCol = (grid[offset10 + X_OFFSET] - x) * sgnCol;
        final double dyCol = (grid[offset10 + Y_OFFSET] - y) * sgnCol;
        final double dxRow = (grid[offset01 + X_OFFSET] - x) * sgnRow;
        final double dyRow = (grid[offset01 + Y_OFFSET] - y) * sgnRow;
        dest.setTransform(dxCol, dyCol, dxRow, dyRow, x - dxCol * col - dxRow * row, y - dyCol * col - dyRow * row);
        /*
         * Si l'on transforme les 3 points qui ont servit à déterminer la transformation
         * affine, on devrait obtenir un résultat identique (aux erreurs d'arrondissement
         * près) peu importe que l'on utilise la transformation affine ou la grille de
         * localisation.
         */
        assert distance(new Point(col, row), dest) < 1E-5;
        assert distance(new Point(col + sgnCol, row), dest) < 1E-5;
        assert distance(new Point(col, row + sgnRow), dest) < 1E-5;
    }

    /**
     * Transform a point using the localization grid, transform it back using the inverse of the specified affine
     * transform, and returns the distance between the source and the resulting point. This is used for assertions only.
     *
     * @param index The source point to test.
     * @param tr The affine transform to test.
     * @return The distance in grid coordinate. Should be close to 0.
     */
    private double distance(final Point2D index, final AffineTransform tr) {
        try {
            Point2D geoCoord = transform(index, null);
            geoCoord = tr.inverseTransform(geoCoord, geoCoord);
            return geoCoord.distance(index);
        } catch (TransformException | NoninvertibleTransformException exception) {
            // Should not happen
            throw new AssertionError(exception);
        } // Not impossible. What should we do? Open question...
    }

    /**
     * Apply the inverse transform to a set of points. More specifically, this method transform "real world" coordinates
     * to grid coordinates. This method use an iterative algorithm for that purpose. A {@link TransformException} is
     * thrown in the computation do not converge. The algorithm applied by this method and its callers is:
     *
     * <ul>
     *   <li>Transform the first point using a "global" affine transform (i.e. the affine transformed computed using the
     *       "least squares" method in LocalizationGrid). Other points will be transformed using the last successful
     *       affine transform, since we assume that the points to transform are close to each other.
     *   <li>Next, compute a local affine transform and use if for transforming the point again. Recompute again the
     *       local affine transform and continue until the cell (x0,y0) doesn't change.
     * </ul>
     *
     * @param source The "real world" coordinate to transform.
     * @param target A pre-allocated destination point. <strong>This point can't be the same than
     *     {@code source}!<strong>
     * @param tr In input, the affine transform to use for the first step. In output, the last affine transform used for
     *     the transformation.
     */
    final void inverseTransform(final Point2D source, final Point2D.Double target, final AffineTransform tr)
            throws TransformException {
        if (CONSERVATIVE) {
            // In an optimal approach, we should reuse the same affine transform than the one used
            // in the last transformation, since it is likely to converge faster for a point close
            // to the previous one. However, it may lead to strange and hard to predict
            // discontinuity in transformations.
            tr.setTransform(global);
        }
        try {
            tr.inverseTransform(source, target);
            int previousX = (int) target.x;
            int previousY = (int) target.y;
            for (int iter = 0; iter < MAX_ITER; iter++) {
                getAffineTransform(target.x, target.y, tr);
                tr.inverseTransform(source, target);
                final int ix = (int) target.x;
                final int iy = (int) target.y;
                if (previousX == ix && previousY == iy) {
                    // Computation converged.
                    if (target.x >= 0 && target.x < width && target.y >= 0 && target.y < height) {
                        // Point is inside the grid. Check the precision.
                        assert transform(target, null).distanceSq(source) < 1E-3 : target;
                    } else {
                        // Point is outside the grid. Use the global transform for uniformity.
                        inverseTransform(source, target);
                    }
                    return;
                }
                previousX = ix;
                previousY = iy;
            }
            /*
             * No convergence found in the "ordinary" loop. The following code checks if
             * we are stuck in a never-ending loop. If yes, then it will try to minimize
             * the following function:
             *
             *     {@code transform(target).distance(source)}.
             */
            final int x0 = previousX;
            final int y0 = previousY;
            global.inverseTransform(source, target);
            double x, y;
            double bestX = x = target.x;
            double bestY = y = target.y;
            double minSq = Double.POSITIVE_INFINITY;
            for (int iter = 1 - MAX_ITER; iter < MAX_ITER; iter++) {
                previousX = (int) x;
                previousY = (int) y;
                getAffineTransform(x, y, tr);
                tr.inverseTransform(source, target);
                x = target.x;
                y = target.y;
                final int ix = (int) x;
                final int iy = (int) y;
                if (previousX == ix && previousY == iy) {
                    // Computation converged.
                    assert iter >= 0;
                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        // Point is inside the grid. Check the precision.
                        assert transform(target, null).distanceSq(source) < 1E-3 : target;
                    } else {
                        // Point is outside the grid. Use the global transform for uniformity.
                        inverseTransform(source, target);
                    }
                    return;
                }
                if (iter == 0) {
                    assert x0 == ix && y0 == iy;
                } else if (x0 == ix && y0 == iy) {
                    // Loop detected.
                    if (bestX >= 0 && bestX < width && bestY >= 0 && bestY < height) {
                        target.x = bestX;
                        target.y = bestY;
                    } else {
                        inverseTransform(source, target);
                    }
                    return;
                }
                transform(target, target);
                final double distanceSq = target.distanceSq(source);
                if (distanceSq < minSq) {
                    minSq = distanceSq;
                    bestX = x;
                    bestY = y;
                }
            }
            /*
             * The transformation didn't converge, and no loop has been found.
             * If the following block is enabled (true), then the best point
             * will be returned. It may not be the best approach since we don't
             * know if this point is valid. Otherwise, an exception is thrown.
             */
            if (MASK_NON_CONVERGENCE) {
                Logging.getLogger(LocalizationGridTransform2D.class).fine("No convergence");
                if (bestX >= 0 && bestX < width && bestY >= 0 && bestY < height) {
                    target.x = bestX;
                    target.y = bestY;
                } else {
                    inverseTransform(source, target);
                }
                return;
            }
        } catch (NoninvertibleTransformException exception) {
            final TransformException e = new TransformException(ErrorKeys.NONINVERTIBLE_TRANSFORM);
            e.initCause(exception);
            throw e;
        }
        throw new TransformException(ErrorKeys.NO_CONVERGENCE);
    }

    /**
     * Inverse transforms a point using the {@link #global} affine transform, and make sure that the result point is
     * outside the grid. This method is used for the transformation of a point which shouldn't be found in the grid.
     *
     * @param source The source coordinate point.
     * @param target The target coordinate point (should not be {@code null}).
     * @throws NoninvertibleTransformException if the transform is non-invertible.
     * @todo Current implementation project an inside point on the nearest border. Could we do something better?
     */
    private void inverseTransform(final Point2D source, final Point2D.Double target)
            throws NoninvertibleTransformException {
        if (global.inverseTransform(source, target) != target) {
            throw new AssertionError(); // Should not happen.
        }
        double x = target.x;
        double y = target.y;
        if (x >= 0 && x < width && y >= 0 && y < height) {
            // Project on the nearest border. TODO: Could we do something better here?
            x -= 0.5 * width;
            y -= 0.5 * height;
            if (Math.abs(x) < Math.abs(y)) {
                target.x = x > 0 ? width : -1;
            } else {
                target.y = y > 0 ? height : -1;
            }
        }
    }

    /** Returns the inverse transform. */
    @Override
    public MathTransform2D inverse() {
        if (inverse == null) {
            inverse = new Inverse();
        }
        return inverse;
    }

    /**
     * The inverse transform. This inner class is the inverse of the enclosing math transform.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    private final class Inverse extends AbstractMathTransform.Inverse implements MathTransform2D, Serializable {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = 4876426825123740986L;

        /** Default constructor. */
        public Inverse() {
            LocalizationGridTransform2D.this.super();
        }

        /** Transform a "real world" coordinate into a grid coordinate. */
        @Override
        public Point2D transform(final Point2D ptSrc, final Point2D ptDst) throws TransformException {
            final AffineTransform tr = new AffineTransform(global);
            if (ptDst == null) {
                final Point2D.Double target = new Point2D.Double();
                inverseTransform(ptSrc, target, tr);
                return target;
            }
            if (ptDst != ptSrc && ptDst instanceof Point2D.Double) {
                inverseTransform(ptSrc, (Point2D.Double) ptDst, tr);
                return ptDst;
            }
            final Point2D.Double target = new Point2D.Double();
            inverseTransform(ptSrc, target, tr);
            ptDst.setLocation(target);
            return ptDst;
        }

        /**
         * Apply the inverse transform to a set of points. More specifically, this method transform "real world"
         * coordinates to grid coordinates. This method use an iterative algorithm for that purpose. A
         * {@link TransformException} is thrown in the computation do not converge.
         *
         * @param srcPts the array containing the source point coordinates.
         * @param srcOff the offset to the first point to be transformed in the source array.
         * @param dstPts the array into which the transformed point coordinates are returned. May be the same than
         *     {@code srcPts}.
         * @param dstOff the offset to the location of the first transformed point that is stored in the destination
         *     array.
         * @param numPts the number of point objects to be transformed.
         * @throws TransformException if a point can't be transformed.
         */
        @Override
        public void transform(final float[] srcPts, int srcOff, final float[] dstPts, int dstOff, int numPts)
                throws TransformException {
            int postIncrement = 0;
            if (srcPts == dstPts && srcOff < dstOff) {
                srcOff += (numPts - 1) * 2;
                dstOff += (numPts - 1) * 2;
                postIncrement = -4;
            }
            final Point2D.Double source = new Point2D.Double();
            final Point2D.Double target = new Point2D.Double();
            final AffineTransform tr = new AffineTransform(global);
            while (--numPts >= 0) {
                source.x = srcPts[srcOff++];
                source.y = srcPts[srcOff++];
                inverseTransform(source, target, tr);
                dstPts[dstOff++] = (float) target.x;
                dstPts[dstOff++] = (float) target.y;
                srcOff += postIncrement;
                dstOff += postIncrement;
            }
        }

        /**
         * Apply the inverse transform to a set of points. More specifically, this method transform "real world"
         * coordinates to grid coordinates. This method use an iterative algorithm for that purpose. A
         * {@link TransformException} is thrown in the computation do not converge.
         *
         * @param srcPts the array containing the source point coordinates.
         * @param srcOff the offset to the first point to be transformed in the source array.
         * @param dstPts the array into which the transformed point coordinates are returned. May be the same than
         *     {@code srcPts}.
         * @param dstOff the offset to the location of the first transformed point that is stored in the destination
         *     array.
         * @param numPts the number of point objects to be transformed.
         * @throws TransformException if a point can't be transformed.
         */
        @Override
        public void transform(final double[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts)
                throws TransformException {
            int postIncrement = 0;
            if (srcPts == dstPts && srcOff < dstOff) {
                srcOff += (numPts - 1) * 2;
                dstOff += (numPts - 1) * 2;
                postIncrement = -4;
            }
            final Point2D.Double source = new Point2D.Double();
            final Point2D.Double target = new Point2D.Double();
            final AffineTransform tr = new AffineTransform(global);
            while (--numPts >= 0) {
                source.x = srcPts[srcOff++];
                source.y = srcPts[srcOff++];
                inverseTransform(source, target, tr);
                dstPts[dstOff++] = target.x;
                dstPts[dstOff++] = target.y;
                srcOff += postIncrement;
                dstOff += postIncrement;
            }
        }

        /** Returns the original localization grid transform. */
        @Override
        public MathTransform2D inverse() {
            return (MathTransform2D) super.inverse();
        }

        /** Restore reference to this object after deserialization. */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            LocalizationGridTransform2D.this.inverse = this;
        }
    }

    /** Returns a hash value for this transform. */
    @Override
    public int hashCode() {
        return (int) serialVersionUID ^ super.hashCode() ^ global.hashCode();
    }

    /** Compares this transform with the specified object for equality. */
    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            final LocalizationGridTransform2D that = (LocalizationGridTransform2D) object;
            return this.width == that.width
                    && this.height == that.height
                    && Utilities.equals(this.global, that.global)
                    && Arrays.equals(this.grid, that.grid);
        }
        return false;
    }

    /**
     * The provider for the {@link LocalizationGridTransform2D}.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     * @todo Not yet fully implemented. Once it is implemented, we need to add a getParameterValues() method in
     *     LocalizationGridTransform2D.
     */
    private static class Provider extends MathTransformProvider {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -8263439392080019340L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {new NamedIdentifier(Citations.GEOTOOLS, "WarpPolynomial")},
                new ParameterDescriptor[] {});

        /** Create a provider for warp transforms. */
        public Provider() {
            super(2, 2, PARAMETERS);
        }

        /** Returns the operation type. */
        @Override
        public Class<Transformation> getOperationType() {
            return Transformation.class;
        }

        /**
         * Creates a warp transform from the specified group of parameter values.
         *
         * @param values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException, FactoryException {
            throw new FactoryException("Not yet implemented");
        }
    }
}
