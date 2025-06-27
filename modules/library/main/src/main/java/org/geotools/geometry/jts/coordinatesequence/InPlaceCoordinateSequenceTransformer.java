/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.coordinatesequence;

import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.CoordinateSequenceTransformer;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence;

/**
 * A JTS CoordinateSequenceTransformer which transforms the values in place.
 *
 * <p>Paragraph ...
 *
 * <p>Responsibilities:
 *
 * <ul>
 *   <li>
 *   <li>
 * </ul>
 *
 * <p>Example:
 *
 * <pre><code>
 * InPlaceCoordinateSequenceTransformer x = new InPlaceCoordinateSequenceTransformer( ... );
 * TODO code example
 * </code></pre>
 *
 * @author jeichar
 * @since 0.6.0
 */
public class InPlaceCoordinateSequenceTransformer implements CoordinateSequenceTransformer {

    /**
     * @see
     *     org.geotools.geometry.jts.CoordinateSequenceTransformer#transform(org.locationtech.jts.geom.CoordinateSequence,
     *     org.geotools.api.referencing.operation.MathTransform)
     */
    @Override
    public CoordinateSequence transform(CoordinateSequence cs, MathTransform transform) throws TransformException {
        if (cs instanceof PackedCoordinateSequence) {
            return transformInternal((PackedCoordinateSequence) cs, transform);
        }
        throw new TransformException(
                cs.getClass().getName() + " is not a implementation that is known to be transformable in place");
    }

    FlyWeightDirectPosition start = new FlyWeightDirectPosition(2);

    private CoordinateSequence transformInternal(PackedCoordinateSequence sequence, MathTransform transform)
            throws TransformException {

        start.setSequence(sequence);
        for (int i = 0; i < sequence.size(); i++) {
            start.setOffset(i);
            try {
                transform.transform(start, start);
            } catch (MismatchedDimensionException e) {
                throw new TransformException("", e);
            }
        }
        return sequence;
    }

    private static class FlyWeightDirectPosition implements Position {
        PackedCoordinateSequence sequence;
        int offset = 0;
        private int dimension;

        /** Construct <code>InPlaceCoordinateSequenceTransformer.FlyWeightDirectPosition</code>. */
        public FlyWeightDirectPosition(int dim) {
            dimension = dim;
        }

        /** @param offset The offset to set. */
        public void setOffset(int offset) {
            this.offset = offset;
        }

        /** @param sequence The sequence to set. */
        public void setSequence(PackedCoordinateSequence sequence) {
            this.sequence = sequence;
        }

        /** @see org.geotools.api.geometry.coordinate.DirectPosition#getDimension() */
        @Override
        public int getDimension() {
            return dimension;
        }

        /** @see org.geotools.api.geometry.coordinate.DirectPosition#getCoordinate() */
        @Override
        public double[] getCoordinate() {
            return new double[] {
                sequence.getX(offset), sequence.getY(offset), sequence.getOrdinate(offset, CoordinateSequence.Z)
            };
        }

        /** @see org.geotools.api.geometry.coordinate.DirectPosition#getOrdinate(int) */
        @Override
        public double getOrdinate(int arg0) throws IndexOutOfBoundsException {
            return sequence.getOrdinate(offset, arg0);
        }

        /** @see org.geotools.api.geometry.coordinate.DirectPosition#setOrdinate(int, double) */
        @Override
        public void setOrdinate(int arg0, double arg1) throws IndexOutOfBoundsException {
            sequence.setOrdinate(offset, arg0, arg1);
        }

        /** @see org.geotools.api.geometry.coordinate.DirectPosition#getCoordinateReferenceSystem() */
        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            // TODO implement method body
            throw new UnsupportedOperationException();
        }

        /** @see org.geotools.api.geometry.coordinate.DirectPosition#clone() */
        @Override
        public FlyWeightDirectPosition clone() {
            throw new UnsupportedOperationException();
        }

        /** @see Position */
        @Override
        public Position getDirectPosition() {
            return this;
        }
    }
}
