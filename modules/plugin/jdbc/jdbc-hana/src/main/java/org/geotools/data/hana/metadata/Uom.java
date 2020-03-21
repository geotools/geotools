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

/**
 * Metadata of a unit-of-measure.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class Uom {

    public static enum Type {
        LINEAR,
        ANGULAR
    }

    public Uom(String name, Type type, double factor) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        if (type == null) {
            throw new NullPointerException("type must not be null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty");
        }
        if (factor <= 0.0) {
            throw new IllegalArgumentException("factor must be greater than 0");
        }
        this.name = name;
        this.type = type;
        this.factor = factor;
    }

    private String name;

    private Type type;

    private double factor;

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public double getFactor() {
        return factor;
    }
}
