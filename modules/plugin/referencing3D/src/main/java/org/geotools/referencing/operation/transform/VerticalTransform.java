/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.referencing.operation.TransformException;


/**
 * Base class for transformations from a <cite>height above the ellipsoid</cite> to a
 * <cite>height above the geoid</cite>. This transform expects three-dimensional geographic
 * coordinates in (<var>longitude</var>,<var>latitude</var>,<var>height</var>) order. The
 * transformations are usually backed by some ellipsoid-dependent database.
 *
 * @since 2.3
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public abstract class VerticalTransform extends AbstractMathTransform {
    /**
     * Creates a new instance of {@code VerticalTransform}.
     */
    protected VerticalTransform() {
    }

    /**
     * Gets the dimension of input points.
     */
    public final int getSourceDimensions() {
        return 3;
    }

    /**
     * Gets the dimension of output points.
     */
    public final int getTargetDimensions() {
        return 3;
    }

    /**
     * Returns the value to add to a <cite>height above the ellipsoid</cite> in order to get a
     * <cite>height above the geoid</cite> for the specified geographic coordinate.
     *
     * @param  longitude The geodetic longitude, in decimal degrees.
     * @param  latitude  The geodetic latitude, in decimal degrees.
     * @param  height    The height above the ellipsoid in metres.
     * @return The value to add in order to get the height above the geoid (in metres).
     * @throws TransformException if the offset can't be computed for the specified coordinates.
     */
    protected abstract double heightOffset(double longitude, double latitude, double height)
            throws TransformException;

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff,
                          final float[] dstPts, int dstOff, int numPts)
            throws TransformException
    {
        final int step;
        if (srcPts == dstPts && srcOff < dstOff) {
            srcOff += 3*(numPts-1);
            dstOff += 3*(numPts-1);
            step = -3;
        } else {
            step = +3;
        }
        while (--numPts >= 0) {
            final float x,y,z;
            dstPts[dstOff + 0] =          (x = srcPts[srcOff + 0]);
            dstPts[dstOff + 1] =          (y = srcPts[srcOff + 1]);
            dstPts[dstOff + 2] = (float) ((z = srcPts[srcOff + 2]) + heightOffset(x,y,z));
            srcOff += step;
            dstOff += step;
        }
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    public void transform(final double[] srcPts, int srcOff,
                          final double[] dstPts, int dstOff, int numPts)
            throws TransformException
    {
        final int step;
        if (srcPts == dstPts && srcOff < dstOff) {
            srcOff += 3*(numPts-1);
            dstOff += 3*(numPts-1);
            step = -3;
        } else {
            step = +3;
        }
        while (--numPts >= 0) {
            final double x,y,z;
            dstPts[dstOff + 0] = (x = srcPts[srcOff + 0]);
            dstPts[dstOff + 1] = (y = srcPts[srcOff + 1]);
            dstPts[dstOff + 2] = (z = srcPts[srcOff + 2]) + heightOffset(x,y,z);
            srcOff += step;
            dstOff += step;
        }
    }
}
