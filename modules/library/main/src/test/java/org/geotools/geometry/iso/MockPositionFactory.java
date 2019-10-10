/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 */

package org.geotools.geometry.iso;

import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MockPositionFactory implements PositionFactory {

    private final CoordinateReferenceSystem crs;

    public MockPositionFactory() {
        this(DefaultGeographicCRS.WGS84);
    }

    public MockPositionFactory(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    public DirectPosition createDirectPosition() {
        return new MockDirectPosition();
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    @Override
    public Precision getPrecision() {
        throw new UnsupportedOperationException();
    }

    public DirectPosition createDirectPosition(double[] coordinates) {
        return new MockDirectPosition(coordinates);
    }

    @Override
    public Position createPosition(Position position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PointArray createPointArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PointArray createPointArray(double[] coordinates, int start, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PointArray createPointArray(float[] coordinates, int start, int length) {
        throw new UnsupportedOperationException();
    }

    class MockDirectPosition implements DirectPosition {
        double[] coordinates;

        MockDirectPosition() {
            this(new double[crs.getCoordinateSystem().getDimension()]);
        }

        public MockDirectPosition(double[] coordinates) {
            this.coordinates = coordinates;
        }

        public MockDirectPosition(DirectPosition position) {
            assert position.getCoordinateReferenceSystem() == crs;
            coordinates = position.getCoordinate();
        }

        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }

        public double[] getCoordinate() {
            double copy[] = new double[crs.getCoordinateSystem().getDimension()];
            System.arraycopy(coordinates, 0, copy, 0, getDimension());
            return copy;
        }

        public int getDimension() {
            return crs.getCoordinateSystem().getDimension();
        }

        public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
            return coordinates[dimension];
        }

        public void setOrdinate(int dimension, double value) throws IndexOutOfBoundsException {
            coordinates[dimension] = value;
        }

        public DirectPosition getDirectPosition() {
            return this;
        }

        public MockDirectPosition clone() {
            return new MockDirectPosition(this);
        }
    }
}
