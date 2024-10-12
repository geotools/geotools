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

import java.util.Objects;

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
            String name, String units, String unitSymbol, String startAttribute, String endAttribute) {
        super();
        this.name = name;
        this.unitSymbol = unitSymbol;
        this.units = units;
        this.startAttribute = startAttribute;
        this.endAttribute = endAttribute;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnitSymbol() {
        return unitSymbol;
    }

    @Override
    public String getUnits() {
        return units;
    }

    @Override
    public String getStartAttribute() {
        return startAttribute;
    }

    @Override
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultDimensionDescriptor that = (DefaultDimensionDescriptor) o;
        return Objects.equals(name, that.name)
                && Objects.equals(unitSymbol, that.unitSymbol)
                && Objects.equals(units, that.units)
                && Objects.equals(startAttribute, that.startAttribute)
                && Objects.equals(endAttribute, that.endAttribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unitSymbol, units, startAttribute, endAttribute);
    }
}
