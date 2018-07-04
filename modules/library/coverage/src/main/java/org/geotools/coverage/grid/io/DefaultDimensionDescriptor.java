/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

/**
 * Default implementation of the {@link DimensionDescriptor} interface
 *
 * @author Daniele Romagnoli - GeoSolutions SAS
 */
public class DefaultDimensionDescriptor implements DimensionDescriptor {

    private String name;

    private String unitSymbol;

    private String units;

    private String startAttribute;

    private String endAttribute;

    public DefaultDimensionDescriptor(
            String name,
            String units,
            String unitSymbol,
            String startAttribute,
            String endAttribute) {
        super();
        this.name = name;
        this.unitSymbol = unitSymbol;
        this.units = units;
        this.startAttribute = startAttribute;
        this.endAttribute = endAttribute;
    }

    public String getName() {
        return name;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public String getUnits() {
        return units;
    }

    public String getStartAttribute() {
        return startAttribute;
    }

    public String getEndAttribute() {
        return endAttribute;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setStartAttribute(String startAttribute) {
        this.startAttribute = startAttribute;
    }

    public void setEndAttribute(String endAttribute) {
        this.endAttribute = endAttribute;
    }

    @Override
    public String toString() {
        return "DefaultDimensionDescriptor{"
                + "name='"
                + name
                + '\''
                + ", unitSymbol='"
                + unitSymbol
                + '\''
                + ", units='"
                + units
                + '\''
                + ", startAttribute='"
                + startAttribute
                + '\''
                + ", endAttribute='"
                + endAttribute
                + '\''
                + '}';
    }
}
