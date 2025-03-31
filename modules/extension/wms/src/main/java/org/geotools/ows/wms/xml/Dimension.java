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
 */
package org.geotools.ows.wms.xml;

/**
 * Property class for holding and handling of property values declared in Dimension-element of a layer. In WMS 1.3.0
 * this is expanded to include Extent information documenting the valid data values for this range.
 *
 * <p><a href="http://schemas.opengis.net/wms/1.1.1/WMS_MS_Capabilities.dtd">
 * http://schemas.opengis.net/wms/1.1.1/WMS_MS_Capabilities.dtd</a>:
 *
 * <pre>{@code
 * <!-- The Dimension element declares the _existence_ of a dimension. -->
 * <!ELEMENT Dimension EMPTY >
 * <!ATTLIST Dimension
 *           name CDATA #REQUIRED
 *           units CDATA #REQUIRED
 *           unitSymbol CDATA #IMPLIED>
 * }</pre>
 *
 * <p><a href="http://schemas.opengis.net/wms/1.3.0/capabilities_1_3_0.xsd">
 * http://schemas.opengis.net/wms/1.3.0/capabilities_1_3_0.xsd</a>:
 *
 * <pre>{@code
 * <element name="Dimension">
 *   <complexType>
 *     <simpleContent>
 *       <extension base="string">
 *         <attribute name="name" type="string" use="required"/>
 *         <attribute name="units" type="string" use="required"/>
 *         <attribute name="unitSymbol" type="string"/>
 *         <attribute name="default" type="string"/>
 *         <attribute name="multipleValues" type="boolean"/>
 *         <attribute name="nearestValue" type="boolean"/>
 *         <attribute name="current" type="boolean"/>
 *       </extension>
 *     </simpleContent>
 *   </complexType>
 * </element>
 * }</pre>
 *
 * <p>According to <a href="https://portal.ogc.org/files/?artifact_id=14416">OpenGIS Web Map Service WMS Implementation
 * Specification - C.2 Declaring dimensions and their allowed value (page 52)</a>, the 'units' attribute must not be
 * missing, but its value is allowed to be empty:
 *
 * <blockquote cite="https://portal.ogc.org/files/?artifact_id=14416">
 *
 * If the dimensional quantity has no units (e.g. band number in a multi-wavelength sensor), use the null string:
 * units="".
 *
 * </blockquote>
 *
 * @version SVN $Id$
 * @author Per Engstrom, Curalia AB, pereng@gmail.com
 */
public class Dimension {
    /** This name is often used as a lookup key */
    protected String name;

    /** Must not be missing, but can be empty according to WMS spec 1.3, page 52 */
    protected String units;

    protected String unitSymbol;

    protected boolean current;

    /** Optional Extent as supplied by WMS 1.3.0 */
    protected Extent extent = null;

    public Dimension(String name, String units, String unitSymbol) {
        // check arguments and throw IllegalArgumentException if not valid
        validateArguments(name, units);
        this.name = name;
        this.units = units;
        this.unitSymbol = unitSymbol;
    }

    public Dimension(String name, String units) {
        // check arguments and throw IllegalArgumentException if not valid
        validateArguments(name, units);
        this.name = name;
        this.units = units;
    }

    /**
     * Check the two input arguments 'name' and 'units' for validity.
     *
     * <ul>
     *   <li>'name': must neither be {@code null} nor empty
     *   <li>'units': must not be {@code null}
     * </ul>
     *
     * @param name the name attribute
     * @param units the units attribute
     */
    private static void validateArguments(String name, String units) throws IllegalArgumentException {
        // 'name' must not be null
        if (name == null) {
            throw new IllegalArgumentException("Error creating Dimension: parameter 'name' must not be null!");
        }
        // 'name' must not be empty
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Error creating Dimension: parameter 'name' must not be empty!");
        }
        // 'units' must not be null, but can be empty according to WMS spec 1.3, page 52
        if (units == null) {
            throw new IllegalArgumentException("Error creating Dimension: parameter 'units' must not be null!");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public Extent getExtent() {
        return extent;
    }

    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    @Override
    public String toString() {
        return name + ", " + units + "(" + unitSymbol + ")";
    }
}
