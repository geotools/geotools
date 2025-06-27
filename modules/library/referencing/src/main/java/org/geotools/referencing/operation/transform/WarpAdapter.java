/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.Arrays;
import javax.media.jai.Warp;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * Wraps an arbitrary {@link MathTransform2D} into an image warp operation. This warp operation is used by
 * {@link org.geotools.coverage.processing.operation.Resample} when no standard warp operation has been found
 * applicable.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class WarpAdapter extends Warp {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = -8679060848877065181L;

    /** The coverage name. Used for formatting error message. */
    private final CharSequence name;

    /**
     * The <strong>inverse</strong> of the transform to apply for projecting an image. This transform maps destination
     * pixels to source pixels.
     */
    private final MathTransform2D inverse;

    /**
     * Constructs a new {@code WarpAdapter} using the given transform.
     *
     * @param name The coverage name. Used for formatting error message.
     * @param inverse The <strong>inverse</strong> of the transformation to apply for projecting an image. This inverse
     *     transform maps destination pixels to source pixels.
     */
    public WarpAdapter(final CharSequence name, final MathTransform2D inverse) {
        this.name = name;
        this.inverse = inverse;
    }

    /** Returns the transform from image's destination pixels to source pixels. */
    public MathTransform2D getTransform() {
        return inverse;
    }

    /**
     * Computes the source pixel positions for a given rectangular destination region, subsampled with an integral
     * period.
     */
    @Override
    public float[] warpSparseRect(
            final int xmin,
            final int ymin,
            final int width,
            final int height,
            final int periodX,
            final int periodY,
            float[] destRect) {
        if (periodX < 1) throw new IllegalArgumentException(String.valueOf(periodX));
        if (periodY < 1) throw new IllegalArgumentException(String.valueOf(periodY));

        final int xmax = xmin + width;
        final int ymax = ymin + height;
        final int count = (width + periodX - 1) / periodX * ((height + periodY - 1) / periodY);
        if (destRect == null) {
            destRect = new float[2 * count];
        }

        // potentially, we'll make it throw lots of TransformExceptions, without reporting or using
        // the stack trace. Avoid the overhead of filling them.
        final float[] finalDestRect = destRect;
        return TransformException.runWithoutStackTraces(() ->
                warpSparseRectInternal(xmin, ymin, periodX, periodY, finalDestRect, ymax, xmax, finalDestRect, count));
    }

    private float[] warpSparseRectInternal(
            int xmin,
            int ymin,
            int periodX,
            int periodY,
            float[] destRect,
            int ymax,
            int xmax,
            float[] finalDestRect,
            int count) {
        int index = fillDestRect(xmin, ymin, periodX, periodY, destRect, ymax, xmax);
        try {
            inverse.transform(finalDestRect, 0, destRect, 0, count);
        } catch (TransformException exception) {
            // At least one transformation failed. Slow down and run mappings one by one
            // instead
            Arrays.fill(destRect, 0, index, 0f);
            fillDestRect(xmin, ymin, periodX, periodY, finalDestRect, ymax, xmax);
            index = 0;
            float[] pt = new float[2];
            for (int y = ymin; y < ymax; y += periodY) {
                for (int x = xmin; x < xmax; x += periodX) {
                    pt[0] = x + 0.5f;
                    pt[1] = y + 0.5f;
                    try {
                        inverse.transform(pt, 0, pt, 0, 1);
                        finalDestRect[index++] = pt[0];
                        finalDestRect[index++] = pt[1];
                    } catch (TransformException e) {
                        // tricky decision here... the source should be outside of the
                        // coverage, but Integer.MAX_VALUE makes the warp fail for some
                        // images,
                        // and Float.NaN for all of them.
                        // Setting to Short.MAX_VALUE seems to work better, and we'll
                        // rely on ROI
                        // for better output masking
                        finalDestRect[index++] = Short.MAX_VALUE;
                        finalDestRect[index++] = Short.MAX_VALUE;
                    }
                }
            }
        }
        while (--index >= 0) {
            finalDestRect[index] -= 0.5f;
        }
        return finalDestRect;
    }

    private static int fillDestRect(
            int xmin, int ymin, int periodX, int periodY, float[] destRect, int ymax, int xmax) {
        int index = 0;
        for (int y = ymin; y < ymax; y += periodY) {
            for (int x = xmin; x < xmax; x += periodX) {
                destRect[index++] = x + 0.5f;
                destRect[index++] = y + 0.5f;
            }
        }
        return index;
    }

    /**
     * Computes the source point corresponding to the supplied point.
     *
     * @param destPt The position in destination image coordinates to map to source image coordinates.
     */
    @Override
    public Point2D mapDestPoint(final Point2D destPt) {
        Point2D result = new Point2D.Double(destPt.getX() + 0.5, destPt.getY() + 0.5);
        try {
            result = inverse.transform(result, result);
        } catch (TransformException exception) {
            throw new IllegalArgumentException(
                    MessageFormat.format(ErrorKeys.BAD_PARAMETER_$2, "destPt", destPt), exception);
        }
        result.setLocation(result.getX() - 0.5, result.getY() - 0.5);
        return result;
    }

    /**
     * Computes the destination point corresponding to the supplied point.
     *
     * @param sourcePt The position in source image coordinates to map to destination image coordinates.
     */
    @Override
    public Point2D mapSourcePoint(final Point2D sourcePt) {
        Point2D result = new Point2D.Double(sourcePt.getX() + 0.5, sourcePt.getY() + 0.5);
        try {
            result = inverse.inverse().transform(result, result);
        } catch (TransformException exception) {
            throw new IllegalArgumentException(
                    MessageFormat.format(ErrorKeys.BAD_PARAMETER_$2, "sourcePt", sourcePt), exception);
        }
        result.setLocation(result.getX() - 0.5, result.getY() - 0.5);
        return result;
    }

    @Override
    public String toString() {
        return "WarpAdapter{" + "name=" + name + ", inverse=" + inverse + '}';
    }
}
