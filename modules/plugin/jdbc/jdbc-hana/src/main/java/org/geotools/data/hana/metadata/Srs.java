/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import java.text.MessageFormat;

/**
 * Metadata to create a new spatial reference system in SAP HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class Srs {

    public static enum Type {
        GEOGRAPHIC,
        PROJECTED,
        FLAT
    }

    public Srs(
            String name,
            int srid,
            String organization,
            int organizationId,
            String wkt,
            String proj4,
            String linearUom,
            String angularUom,
            Type type,
            Double majorAxis,
            Double minorAxis,
            Double inverseFlattening,
            double minX,
            double maxX,
            double minY,
            double maxY) {
        checkNotEmpty(name, "name");
        if (srid < 0) {
            throw new IllegalArgumentException("srid must not be negative");
        }
        checkNotEmpty(organization, "organization");
        if (organizationId < 0) {
            throw new IllegalArgumentException("organizationId must not be negative");
        }
        checkNotEmpty(linearUom, "linearUom");
        if (type == null) {
            throw new NullPointerException("type must not be null");
        }
        if (type == Type.GEOGRAPHIC || type == Type.FLAT) {
            if (angularUom == null) {
                throw new NullPointerException(
                        "angularUom must not be null in case of geographic or flat reference systems");
            }
            if (angularUom.isEmpty()) {
                throw new IllegalArgumentException(
                        "angularUom must not be empty in case of geographic or flat reference systems");
            }
            if (majorAxis == null) {
                throw new NullPointerException(
                        "majorAxis must not be null in case of geographic or flat reference systems");
            }
            if (minorAxis == null && inverseFlattening == null) {
                throw new NullPointerException(
                        "Either minorAxis or inverseFlattening must be given in case of geographic or flat reference systems");
            }
        }
        if (minorAxis != null && inverseFlattening != null) {
            throw new IllegalArgumentException("Either minorAxis or inverseFlattening must be given, but not both");
        }
        if (majorAxis != null && majorAxis <= 0.0) {
            throw new IllegalArgumentException("majorAxis must be greater than 0");
        }
        if (minorAxis != null && minorAxis <= 0.0) {
            throw new IllegalArgumentException("minorAxis must be greater than 0");
        }
        if (inverseFlattening != null && inverseFlattening <= 0.0) {
            throw new IllegalArgumentException("inverseFlattening must be greater than 0");
        }
        if (majorAxis == null && (minorAxis != null || inverseFlattening != null)) {
            throw new IllegalArgumentException(
                    "If minorAxis or inverseFlattening is given, majorAxis must be given as well");
        }

        this.name = name;
        this.srid = srid;
        this.organization = organization;
        this.organizationId = organizationId;
        this.wkt = nullIfEmpty(wkt);
        this.proj4 = nullIfEmpty(proj4);
        this.linearUom = linearUom;
        this.angularUom = nullIfEmpty(angularUom);
        this.type = type;
        this.majorAxis = majorAxis;
        this.minorAxis = minorAxis;
        this.inverseFlattening = inverseFlattening;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    private String name;

    private int srid;

    private String organization;

    private int organizationId;

    private String wkt;

    private String proj4;

    private String linearUom;

    private String angularUom;

    private Type type;

    private Double majorAxis;

    private Double minorAxis;

    private Double inverseFlattening;

    private double minX;

    private double maxX;

    private double minY;

    private double maxY;

    public String getName() {
        return name;
    }

    public int getSrid() {
        return srid;
    }

    public String getOrganization() {
        return organization;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public String getWkt() {
        return wkt;
    }

    public String getProj4() {
        return proj4;
    }

    public String getLinearUom() {
        return linearUom;
    }

    public String getAngularUom() {
        return angularUom;
    }

    public Type getType() {
        return type;
    }

    public Double getMajorAxis() {
        return majorAxis;
    }

    public Double getMinorAxis() {
        return minorAxis;
    }

    public Double getInverseFlattening() {
        return inverseFlattening;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    private String nullIfEmpty(String s) {
        if (s == null) {
            return s;
        }
        if (s.isEmpty()) {
            return null;
        }
        return s;
    }

    private void checkNotEmpty(String s, String paramName) {
        if (s == null) {
            throw new NullPointerException(MessageFormat.format("{0} must not be null", paramName));
        }
        if (s.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format("{0} must not be empty", paramName));
        }
    }
}
