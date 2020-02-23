/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.model;

import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A tile matrix set is composed of a collection of tile matrices, each one with a resolution
 * optimized for a particular scale and identified by a tile matrix identifier. Each tile matrix set
 * has an optional approximated bounding box but each tile matrix has an exact bounding box that is
 * deduced indirectly from other parameters.
 *
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class TileMatrix {

    private static final GeometryFactory gf = new GeometryFactory();

    String identifier;

    double denominator;

    double resolution;

    double pixelWidth = 0.00028;

    Point topLeft;

    int tileWidth = 256;

    int tileHeight = 256;

    int matrixWidth;

    int matrixHeight;

    private TileMatrixSet parent;

    /** @return the identifier */
    public String getIdentifier() {
        return identifier;
    }

    /** @param identifier the identifier to set */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /** @return the denominator */
    public double getDenominator() {
        return denominator;
    }

    public double getResolution() {
        return resolution;
    }

    /** @param denominator the denominator to set */
    public void setDenominator(double denominator) {
        this.denominator = denominator;
        resolution = denominator * pixelWidth;
    }

    /** @return the topLeft */
    public Point getTopLeft() {
        return topLeft;
    }

    /** @param topLeft the topLeft to set */
    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    /** @return the tileWidth */
    public int getTileWidth() {
        return tileWidth;
    }

    /** @param tileWidth the tileWidth to set */
    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    /** @return the tileHeight */
    public int getTileHeight() {
        return tileHeight;
    }

    /** @param tileHeight the tileHeight to set */
    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    /** @return the matrixWidth */
    public int getMatrixWidth() {
        return matrixWidth;
    }

    /** @param matrixWidth the matrixWidth to set */
    public void setMatrixWidth(int matrixWidth) {
        this.matrixWidth = matrixWidth;
    }

    /** @return the matrixHeight */
    public int getMatrixHeight() {
        return matrixHeight;
    }

    /** @param matrixHeight the matrixHeight to set */
    public void setMatrixHeight(int matrixHeight) {
        this.matrixHeight = matrixHeight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIdentifier())
                .append("\t")
                .append(getDenominator())
                .append("\t")
                .append(getResolution())
                .append("\t");
        sb.append(getTopLeft()).append("\t");
        sb.append(getTileWidth()).append("x").append(getTileHeight()).append("\n");
        return sb.toString();
    }

    /** */
    public void setTopLeft(double lon, double lat) {
        boolean isLongitudeFirstAxisOrderForced =
                Boolean.getBoolean(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER)
                        || GeoTools.getDefaultHints().get(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER)
                                == Boolean.TRUE;

        CoordinateReferenceSystem crs = getCrs();
        if (isLongitudeFirstAxisOrderForced
                || (crs != null && CRS.getAxisOrder(crs).equals(CRS.AxisOrder.EAST_NORTH))) {
            topLeft = gf.createPoint(new Coordinate(lon, lat));
            return;
        } else {
            // guess lat/lon?
            topLeft = gf.createPoint(new Coordinate(lat, lon));
        }
    }

    public TileMatrixSet getParent() {
        return parent;
    }

    public void setParent(TileMatrixSet parent) {
        this.parent = parent;
    }

    /** Retrieve the CRS from the parent TileMatrixSet */
    public CoordinateReferenceSystem getCrs() {
        return parent == null ? null : parent.getCoordinateReferenceSystem();
    }
}
