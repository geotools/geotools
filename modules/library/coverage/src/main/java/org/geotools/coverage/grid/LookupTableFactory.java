/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

import java.awt.image.DataBuffer;
import java.util.Arrays;
import java.util.Map;
import javax.media.jai.LookupTableJAI;
import org.geotools.api.referencing.operation.MathTransform1D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.util.WeakValueHashMap;

/**
 * A factory for {@link LookupTableJAI} objects built from an array of {@link MathTransform1D}. This factory is used
 * internally by {@link GridCoverageViews#create}.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class LookupTableFactory {
    /** The pool of {@link LookupTableJAI} objects already created. */
    private static final Map<LookupTableFactory, LookupTableJAI> pool = new WeakValueHashMap<>();

    /** The source data type. Should be one of {@link DataBuffer} constants. */
    private final int sourceType;

    /** The target data type. Should be one of {@link DataBuffer} constants. */
    private final int targetType;

    /** The math transforms for this key. */
    private final MathTransform1D[] transforms;

    /**
     * Create a new objet to use as a key in the {@link #pool}.
     *
     * @param sourceType The source data type. Should be one of {@link DataBuffer} constants.
     * @param targetType The target data type. Should be one of {@link DataBuffer} constants.
     * @param transforms The math transforms to apply.
     */
    private LookupTableFactory(final int sourceType, final int targetType, final MathTransform1D[] transforms) {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.transforms = transforms;
    }

    /**
     * Gets a lookup factory
     *
     * @param sourceType The source data type. Should be one of {@link DataBuffer} constants.
     * @param targetType The target data type. Should be one of {@link DataBuffer} constants.
     * @param transforms The math transforms to apply.
     * @return The lookup table, or {@code null} if this method can't build a lookup table for the supplied parameters.
     * @throws TransformException if a transformation failed.
     */
    public static LookupTableJAI create(final int sourceType, final int targetType, final MathTransform1D[] transforms)
            throws TransformException {
        /*
         * Argument check. Null values are legal but can't be processed by this method.
         */
        final int nbands = transforms.length;
        for (MathTransform1D transform : transforms) {
            if (transform == null) {
                return null;
            }
        }
        synchronized (pool) {
            /*
             * Checks if a table is already available in the cache. Since tables may be 64 kb big,
             * sharing tables may save a significant amount of memory if there is many images.
             */
            final LookupTableFactory key = new LookupTableFactory(sourceType, targetType, transforms);
            LookupTableJAI table = pool.get(key);
            if (table != null) {
                return table;
            }
            /*
             * Computes the table's size according the source datatype. For datatype 'short' (signed
             * or unsigned), we will create the table only if the target datatype is 'byte' in order
             * to avoid to use too much memory for the table. The memory consumed for a table from
             * source datatype 'short' to target datatype 'byte' is 64 ko.
             */
            final int length;
            final int offset;
            switch (sourceType) {
                default: {
                    return null;
                }
                case DataBuffer.TYPE_BYTE: {
                    length = 0x100;
                    offset = 0;
                    break;
                }
                case DataBuffer.TYPE_SHORT:
                case DataBuffer.TYPE_USHORT: {
                    if (targetType != DataBuffer.TYPE_BYTE) {
                        // Avoid to use too much memory for the table.
                        return null;
                    }
                    length = 0x10000;
                    offset = sourceType == DataBuffer.TYPE_SHORT ? Short.MIN_VALUE : 0;
                    break;
                }
            }
            /*
             * Builds the table according the target datatype.
             */
            switch (targetType) {
                default: {
                    return null;
                }
                case DataBuffer.TYPE_DOUBLE: {
                    final double[][] data = new double[nbands][];
                    final double[] buffer = new double[length];
                    for (int i = length; --i >= 0; ) {
                        buffer[i] = i;
                    }
                    for (int i = nbands; --i >= 0; ) {
                        final double[] array = i == 0 ? buffer : buffer.clone();
                        transforms[i].transform(array, 0, array, 0, array.length);
                        data[i] = array;
                    }
                    table = new LookupTableJAI(data, offset);
                    break;
                }
                case DataBuffer.TYPE_FLOAT: {
                    final float[][] data = new float[nbands][];
                    final float[] buffer = new float[length];
                    for (int i = length; --i >= 0; ) {
                        buffer[i] = i;
                    }
                    for (int i = transforms.length; --i >= 0; ) {
                        final float[] array = i == 0 ? buffer : buffer.clone();
                        transforms[i].transform(array, 0, array, 0, length);
                        data[i] = array;
                    }
                    table = new LookupTableJAI(data, offset);
                    break;
                }
                case DataBuffer.TYPE_INT: {
                    final int[][] data = new int[nbands][];
                    for (int i = nbands; --i >= 0; ) {
                        final MathTransform1D tr = transforms[i];
                        final int[] array = new int[length];
                        for (int j = length; --j >= 0; ) {
                            long v = round(tr.transform(j + offset));
                            array[j] = (int) min(max(v, Integer.MIN_VALUE), Integer.MAX_VALUE);
                        }
                        data[i] = array;
                    }
                    table = new LookupTableJAI(data, offset);
                    break;
                }
                case DataBuffer.TYPE_SHORT:
                case DataBuffer.TYPE_USHORT: {
                    final int minimum, maximum;
                    if (targetType == DataBuffer.TYPE_SHORT) {
                        minimum = Short.MIN_VALUE;
                        maximum = Short.MAX_VALUE;
                    } else {
                        minimum = 0;
                        maximum = 0xFFFF;
                    }
                    final short[][] data = new short[nbands][];
                    for (int i = nbands; --i >= 0; ) {
                        final MathTransform1D tr = transforms[i];
                        final short[] array = new short[length];
                        for (int j = length; --j >= 0; ) {
                            long v = round(tr.transform(j + offset));
                            array[j] = (short) min(max(v, minimum), maximum);
                        }
                        data[i] = array;
                    }
                    table = new LookupTableJAI(data, offset, minimum != 0);
                    break;
                }
                case DataBuffer.TYPE_BYTE: {
                    final byte[][] data = new byte[nbands][];
                    for (int i = nbands; --i >= 0; ) {
                        final MathTransform1D tr = transforms[i];
                        final byte[] array = new byte[length];
                        for (int j = length; --j >= 0; ) {
                            array[j] = (byte) min(max(round(tr.transform(j + offset)), 0), 0xFF);
                        }
                        data[i] = array;
                    }
                    table = new LookupTableJAI(data, offset);
                    break;
                }
            }
            pool.put(key, table);
            return table;
        }
    }

    /**
     * Returns a hash code value for this key. This is for internal use by {@code LookupTableFactory} and is public only
     * as an implementation side effect.
     */
    @Override
    public int hashCode() {
        int code = sourceType + 37 * targetType;
        code += Arrays.hashCode(transforms);
        return code;
    }

    /**
     * Compares the specified object with this key for equality. This is for internal use by {@code LookupTableFactory}
     * and is public only as an implementation side effect.
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof LookupTableFactory) {
            final LookupTableFactory that = (LookupTableFactory) other;
            return this.sourceType == that.sourceType
                    && this.targetType == that.targetType
                    && Arrays.equals(this.transforms, that.transforms);
        }
        return false;
    }
}
