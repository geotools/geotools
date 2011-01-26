/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.text;

import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.image.io.metadata.ImageGeometry;
import org.geotools.image.io.metadata.ImageReferencing;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;


/**
 * Default implementation of the {@link TextMetadataParser} class, providing a method
 * to put all metadata read from a text into the tree model. This current implementation
 * uses the tree structure which matches approximatively with the
 * <a href="http://www.opengeospatial.org/standards/gmljp2">GML in JPEG 2000</a> standard.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Cédric Briançon
 */
public class DefaultTextMetadataParser extends TextMetadataParser {
    /**
     * The unit for coordinate axes.
     */
    private String unit;

    /**
     * The name for the {@link CoordinateReferenceSystem coordinate reference system}.
     */
    private String crs_name;

    /**
     * The type for the {@link CoordinateReferenceSystem coordinate reference system}.
     */
    private String crs_type;

    /**
     * The name for the {@link CoordinateSystem coordinate system}.
     */
    private String cs_name;

    /**
     * The type for the {@link CoordinateSystem coordinate system}.
     */
    private String cs_type;

    /**
     * The axis direction along the X axis.
     */
    private String xdir;

    /**
     * The axis direction along the Y axis.
     */
    private String ydir;

    /**
     * The axis direction along the Z axis.
     */
    private String zdir;

    /**
     * The minimum value for the X ordinate.
     */
    private double xmin;

    /**
     * The maximum value for the X ordinate.
     */
    private double xmax;

    /**
     * The minimum value for the Y ordinate.
     */
    private double ymin;

    /**
     * The maximum value for the Y ordinate.
     */
    private double ymax;

    /**
     * The minimum value for the Z ordinate.
     */
    private double zmin;

    /**
     * The maximum value for the Z ordinate.
     */
    private double zmax;

    /**
     * The X resolution.
     */
    private double xres;

    /**
     * The Y resolution.
     */
    private double yres;

    /**
     * The Z resolution.
     */
    private double zres;

    /**
     * Creates a new instance of {@link TextMetadataParser}, with default factories.
     */
    public DefaultTextMetadataParser() {
        super();
    }

    /**
     * Creates a new instance of {@link TextMetadataParser}, with the factories specified.
     *
     * @param factories The factories to use.
     */
    public DefaultTextMetadataParser(final ReferencingFactoryContainer factories) {
        super(factories);
    }

    /**
     * {@inheritDoc}
     */
    protected void put(final Key key, final Object value) {
        final ImageGeometry    geometry    = metadata.getGeometry();
        final ImageReferencing referencing = metadata.getReferencing();
        /* Verifies if a unit has already been specified, and if not, applied it
         * to every axes already defined.
         */
        if (key.equals(UNIT) && value instanceof String) {
            unit = (String) value;
            return;
        }
        /* Fills the matching variable for ordinates with values given.
         */
        if (key.equals(X_MINIMUM)) {
            xmin = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        if (key.equals(Y_MINIMUM)) {
            ymin = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        if (key.equals(Z_MINIMUM)) {
            zmin = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        if (key.equals(X_MAXIMUM)) {
            xmax = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        if (key.equals(Y_MAXIMUM)) {
            ymax = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        if (key.equals(Z_MAXIMUM)) {
            zmax = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        /*
         */
        if (key.equals(WIDTH)) {
            geometry.setGridRange(0, 0, Integer.parseInt((String) value));
            return;
        }
        if (key.equals(HEIGHT)) {
            geometry.setGridRange(1, 0, Integer.parseInt((String) value));
            return;
        }
        if (key.equals(DEPTH)) {
            geometry.setGridRange(2, 0, Integer.parseInt((String) value));
            return;
        }
        /* Fills the matching varaible for the offset vector.
         */
        if (key.equals(X_RESOLUTION)) {
            xres = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        if (key.equals(Y_RESOLUTION)) {
            yres = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        if (key.equals(Z_RESOLUTION)) {
            zres = (value instanceof Double) ? (Double) value : Double.parseDouble((String) value);
            return;
        }
        /**
         * Fills the direction for the axes.
         */
        if (key.equals(X_DIRECTION) && value instanceof String) {
            xdir = (String) value;
            return;
        }
        if (key.equals(Y_DIRECTION) && value instanceof String) {
            ydir = (String) value;
            return;
        }
        if (key.equals(Z_DIRECTION) && value instanceof String) {
            zdir = (String) value;
            return;
        }
        /* Specifies the projection and datum name.
         */
        if (key.equals(PROJECTION) && value instanceof String) {
            referencing.setProjectionName((String) value);
            return;
        }
        if (key.equals(DATUM) && value instanceof String) {
            /* @todo: handle the datum type, here one puts null for the type, but maybe a new key
             *        can contain this information.
             */
            referencing.setDatum((String) value, null);
            return;
        }
        if (key.equals(PRIME_MERIDIAN) && value instanceof String) {
            referencing.setPrimeMeridianName((String) value);
            return;
        }
        if (key.equals(GREENWICH_LONGITUDE)) {
            referencing.setPrimeMeridianGreenwichLongitude((value instanceof Double) ?
                (Double) value : Double.parseDouble((String) value));
            return;
        }
        /* Specifies the ellipsoid value.
         */
        if (key.equals(ELLIPSOID) && value instanceof String) {
            referencing.setEllipsoidName((String) value);
            return;
        }
        if (key.equals(ELLIPSOID_UNIT) && value instanceof String) {
            referencing.setEllipsoidUnit((String) value);
            return;
        }
        if (key.equals(SEMI_MAJOR)) {
            if (value instanceof Double) {
                referencing.setSemiMajorAxis((Double) value);
            } else {
                referencing.setSemiMajorAxis(Double.parseDouble((String) value));
            }
            return;
        }
        if (key.equals(SEMI_MINOR)) {
            if (value instanceof Double) {
                referencing.setSemiMinorAxis((Double) value);
            } else {
                referencing.setSemiMinorAxis(Double.parseDouble((String) value));
            }
            return;
        }
        if (key.equals(INVERSE_FLATTENING)) {
            if (value instanceof Double) {
                referencing.setInverseFlattening((Double) value);
            } else {
                referencing.setInverseFlattening(Double.parseDouble((String) value));
            }
            return;
        }
        /* CoordinateReferenceSystem value.
         */
        if (key.equals(COORDINATE_REFERENCE_SYSTEM_TYPE) && value instanceof String) {
            crs_type = (String) value;
            return;
        }
        if (key.equals(COORDINATE_REFERENCE_SYSTEM) && value instanceof String) {
            crs_name = (String) value;
            return;
        }
        if (key.equals(COORDINATE_SYSTEM_TYPE) && value instanceof String) {
            cs_type = (String) value;
            return;
        }
        if (key.equals(COORDINATE_SYSTEM) && value instanceof String) {
            cs_name = (String) value;
            return;
        }
        /* Adds parameters using values found among these keys.
         */
        if (key.equals(CENTRAL_MERIDIAN) || key.equals(FALSE_EASTING)    ||
            key.equals(FALSE_NORTHING)   || key.equals(LATITUDE_OF_ORIGIN))
        {
            if (value instanceof Double) {
                referencing.addParameter(key.toString(), (Double) value);
            } else {
                referencing.addParameter(key.toString(), Double.parseDouble((String) value));
            }
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void putDone() {
        final ImageGeometry    geometry    = metadata.getGeometry();
        final ImageReferencing referencing = metadata.getReferencing();
        if (!Double.isNaN(xmin) && !Double.isNaN(xmax)) {
            geometry.setOrdinateRange(0, xmin, xmax);
            referencing.addAxis(null, xdir, unit);
        }
        if (!Double.isNaN(ymin) && !Double.isNaN(ymax)) {
            geometry.setOrdinateRange(1, ymin, ymax);
            referencing.addAxis(null, ydir, unit);
        }
        if (!Double.isNaN(zmin) && !Double.isNaN(zmax)) {
            geometry.setOrdinateRange(2, zmin, zmax);
            referencing.addAxis(null, zdir, unit);
        }
        final int dimension = referencing.getDimension();
        /* Create an array of offset vectors with the value stored in the specified dimension.
         * For example, if we are in a 2D system, offset vector will be like :
         * offsetVector[0] :  ||x||     0
         * offsetVector[1] :    0     ||y||
         * This process assumes that the grid is straight, meaning the offset vector
         * in the X ordinate have a value only in the X direction.
         */
        final double[] offsetVector = new double[dimension];
        if (!Double.isNaN(xres) && dimension > 0) {
            offsetVector[0] = xres;
            geometry.addOffsetVector(offsetVector);
            offsetVector[0] = 0;
        }
        if (!Double.isNaN(yres) && dimension > 1) {
            offsetVector[1] = yres;
            geometry.addOffsetVector(offsetVector);
            offsetVector[1] = 0;
        }
        if (!Double.isNaN(zres) && dimension > 2) {
            offsetVector[2] = zres;
            geometry.addOffsetVector(offsetVector);
            offsetVector[2] = 0;
        }
        /* CoordinateReferenceSystem information. 
         */
        if (crs_name != null || crs_type != null) {
            referencing.setCoordinateReferenceSystem(crs_name, crs_type);
        }
        if (cs_name != null || cs_type != null) {
            referencing.setCoordinateSystem(cs_name, cs_type);
        }
    }

    /**
     * Sets the geographic metadata and put all other variables to their default value.
     * This method should have been called before the {@link #put} and {@link #putDone}
     * one.
     */
    @Override
    public void setGeographicMetadata(final GeographicMetadata metadata) {
        super.setGeographicMetadata(metadata);
        xmin = Double.NaN; xmax = Double.NaN; xres = Double.NaN; xdir = null;
        ymin = Double.NaN; ymax = Double.NaN; yres = Double.NaN; ydir = null;
        zmin = Double.NaN; zmax = Double.NaN; zres = Double.NaN; zdir = null;
        unit = null;       crs_type = null;   crs_name = null;
                           cs_type  = null;   cs_name  = null;
    }
}
