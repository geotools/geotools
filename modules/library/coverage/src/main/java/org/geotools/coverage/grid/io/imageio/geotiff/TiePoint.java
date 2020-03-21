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
 *
 */
package org.geotools.coverage.grid.io.imageio.geotiff;

import org.geotools.util.Utilities;

/**
 * Quoting the geotiff spec:
 *
 * <pre>
 *      ModelTiepointTag:
 *      Tag = 33922 (8482.H)
 *      Type = DOUBLE (IEEE Double precision)
 *      N = 6*K,  K = number of tiepoints
 *      Alias: GeoreferenceTag
 *      Owner: Intergraph
 * </pre>
 *
 * This tag stores raster->model tiepoint pairs in the order
 *
 * <pre>
 *   ModelTiepointTag = (...,I,J,K, X,Y,Z...),
 * </pre>
 *
 * where (I,J,K) is the point at location (I,J) in raster space with pixel-value K, and (X,Y,Z) is a
 * vector in model space. In most cases the model space is only two-dimensional, in which case both
 * K and Z should be set to zero; this third dimension is provided in anticipation of future support
 * for 3D digital elevation models and vertical coordinate systems.
 *
 * <p>A raster image may be georeferenced simply by specifying its location, size and orientation in
 * the model coordinate space M. This may be done by specifying the location of three of the four
 * bounding corner points. However, tiepoints are only to be considered exact at the points
 * specified; thus defining such a set of bounding tiepoints does not imply that the model space
 * locations of the interior of the image may be exactly computed by a linear interpolation of these
 * tiepoints.
 *
 * <p>However, since the relationship between the Raster space and the model space will often be an
 * exact, affine transformation, this relationship can be defined using one set of tiepoints and the
 * "ModelPixelScaleTag", described below, which gives the vertical and horizontal raster grid cell
 * size, specified in model units.
 *
 * <p>If possible, the first tiepoint placed in this tag shall be the one establishing the location
 * of the point (0,0) in raster space. However, if this is not possible (for example, if (0,0) is
 * goes to a part of model space in which the projection is ill-defined), then there is no
 * particular order in which the tiepoints need be listed.
 *
 * <p>For orthorectification or mosaicking applications a large number of tiepoints may be specified
 * on a mesh over the raster image. However, the definition of associated grid interpolation methods
 * is not in the scope of the current GeoTIFF spec.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 */
public final class TiePoint {
    private double[] values = new double[6];

    /** Default constructor. */
    public TiePoint() {}

    public TiePoint(double i, double j, double k, double x, double y, double z) {
        set(i, j, k, x, y, z);
    }

    public void set(double i, double j, double k, double x, double y, double z) {
        values[0] = i;
        values[1] = j;
        values[2] = k;
        values[3] = x;
        values[4] = y;
        values[5] = z;
    }

    public double getValueAt(int index) {
        if (index < 0 || index > 5)
            throw new IllegalArgumentException("Provided index should be between 0 and 5");
        return values[index];
    }

    public double[] getData() {
        return values.clone();
    }

    public boolean isSet() {
        for (double val : values) if (!isComponentSet(val)) return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TiePoint)) return false;
        final TiePoint that = (TiePoint) obj;
        if (Utilities.deepEquals(this.values, that.values)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Utilities.deepHashCode(this.values);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Tie point").append("\n");
        builder.append("\tRaster Space point (")
                .append(values[0])
                .append(", ")
                .append(values[1])
                .append(", ")
                .append(values[2])
                .append(")")
                .append("\n");
        builder.append("\tModel Space point (")
                .append(values[3])
                .append(", ")
                .append(values[4])
                .append(", ")
                .append(values[5])
                .append(")");
        return builder.toString();
    }

    /** Tells me if a component of this {@link PixelScale} is set. */
    private boolean isComponentSet(double value) {
        return !Double.isInfinite(value) && !Double.isNaN(value) && Math.abs(value) > 1E-6;
    }
}
