/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Wraps a datum shift math transform and makes sure we are not introducing dateline jumps when
 * applying the datum shift
 *
 * @author Andrea Aime - GeoSolutions
 */
class GeographicOffsetWrapper implements MathTransform {

    MathTransform delegate;

    public GeographicOffsetWrapper(MathTransform delegate) {
        this.delegate = delegate;
    }

    public int getSourceDimensions() {
        return delegate.getSourceDimensions();
    }

    public int getTargetDimensions() {
        return delegate.getTargetDimensions();
    }

    public DirectPosition transform(DirectPosition ptSrc, DirectPosition ptDst)
            throws MismatchedDimensionException, TransformException {
        return delegate.transform(ptSrc, ptDst);
    }

    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        double[] source = srcPts;
        if (srcPts == dstPts) {
            source = new double[srcPts.length];
            System.arraycopy(srcPts, 0, source, 0, srcPts.length);
        }
        delegate.transform(srcPts, srcOff, dstPts, dstOff, numPts);

        final int srcDimension = delegate.getSourceDimensions();
        final int dstDimension = delegate.getTargetDimensions();
        double lastSrcLon = source[srcOff];
        double lastDstLon = dstPts[dstOff];
        double offset = 0;
        for (int i = srcOff + srcDimension, j = dstOff + dstDimension, ptCount = 1;
                ptCount < numPts;
                i += srcDimension, j += dstDimension, ptCount++) {
            double srcLon = source[i];
            double dstLon = dstPts[j];

            double srcDelta = srcLon - lastSrcLon;
            double dstDelta = dstLon - lastDstLon;
            double difference = srcDelta - dstDelta;
            // did we jump over the dateline?
            if (difference > 180) {
                // from west to east
                offset += 360;
            } else if (difference < -180) {
                offset -= 360;
            }

            if (offset != 0) {
                dstPts[j] += offset;
            }

            lastDstLon = dstLon;
            lastSrcLon = srcLon;
        }
    }

    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts)
            throws TransformException {
        delegate.transform(srcPts, srcOff, dstPts, dstOff, numPts);

        int dimension = (srcPts.length - srcOff) / numPts;
        double lastSrcLon = srcPts[srcOff];
        double lastDstLon = dstPts[dstOff];
        float offset = 0;
        for (int i = srcOff + dimension, j = dstOff + dimension, ptCount = 0;
                ptCount < numPts;
                i += dimension, j += dimension, ptCount++) {
            double srcLon = srcPts[i];
            double dstLon = dstPts[j];

            double srcDelta = srcLon - lastSrcLon;
            double dstDelta = dstLon - lastDstLon;
            double difference = srcDelta - dstDelta;
            // did we jump over the dateline?
            if (difference > 180) {
                // from west to east
                offset += 360;
            } else if (difference < 180) {
                offset -= 360;
            }

            if (offset != 0) {
                dstPts[j] += offset;
            }

            lastDstLon = dstLon;
            lastSrcLon = srcLon;
        }
    }

    public void transform(float[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        delegate.transform(srcPts, srcOff, dstPts, dstOff, numPts);

        int dimension = (srcPts.length - srcOff) / numPts;
        double lastSrcLon = srcPts[srcOff];
        double lastDstLon = dstPts[dstOff];
        double offset = 0;
        for (int i = srcOff + dimension, j = dstOff + dimension, ptCount = 0;
                ptCount < numPts;
                i += dimension, j += dimension, ptCount++) {
            double srcLon = srcPts[i];
            double dstLon = dstPts[j];

            double srcDelta = srcLon - lastSrcLon;
            double dstDelta = dstLon - lastDstLon;
            double difference = srcDelta - dstDelta;
            // did we jump over the dateline?
            if (difference > 180) {
                // from west to east
                offset += 360;
            } else if (difference < 180) {
                offset -= 360;
            }

            if (offset != 0) {
                dstPts[j] += offset;
            }

            lastDstLon = dstLon;
            lastSrcLon = srcLon;
        }
    }

    public void transform(double[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts)
            throws TransformException {
        delegate.transform(srcPts, srcOff, dstPts, dstOff, numPts);

        int dimension = (srcPts.length - srcOff) / numPts;
        double lastSrcLon = srcPts[srcOff];
        double lastDstLon = dstPts[dstOff];
        float offset = 0;
        for (int i = srcOff + dimension, j = dstOff + dimension, ptCount = 0;
                ptCount < numPts;
                i += dimension, j += dimension, ptCount++) {
            double srcLon = srcPts[i];
            double dstLon = dstPts[j];

            double srcDelta = srcLon - lastSrcLon;
            double dstDelta = dstLon - lastDstLon;
            double difference = srcDelta - dstDelta;
            // did we jump over the dateline?
            if (difference > 180) {
                // from west to east
                offset += 360;
            } else if (difference < 180) {
                offset -= 360;
            }

            if (offset != 0) {
                dstPts[j] += offset;
            }

            lastDstLon = dstLon;
            lastSrcLon = srcLon;
        }
    }

    public Matrix derivative(DirectPosition point)
            throws MismatchedDimensionException, TransformException {
        return delegate.derivative(point);
    }

    public MathTransform inverse() throws NoninvertibleTransformException {
        MathTransform inverse = delegate.inverse();
        if (inverse instanceof GeographicOffsetWrapper) {
            return inverse;
        } else {
            return new GeographicOffsetWrapper(inverse);
        }
    }

    public boolean isIdentity() {
        return delegate.isIdentity();
    }

    public String toWKT() throws UnsupportedOperationException {
        return delegate.toWKT();
    }

    @Override
    public String toString() {
        return "GeographicOffsetWrapper[\n" + delegate.toString() + "\n]";
    }
}
