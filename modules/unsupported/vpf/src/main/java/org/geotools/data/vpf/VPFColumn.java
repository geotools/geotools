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
package org.geotools.data.vpf;

import static org.geotools.data.vpf.ifc.DataTypesDefinition.*;

import org.geotools.data.vpf.io.TripletId;
import org.geotools.data.vpf.util.DataUtils;
import org.geotools.feature.AttributeTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * A column in a VPF File.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFColumn {
    /**
     * If the value is a short integer, that often means it has an accompanying value in a string
     * lookup table.
     */
    private boolean attemptLookup = false;

    /** Attribute descriptor generated from this column definition. */
    private final AttributeDescriptor descriptor;

    private final String name;

    private final int elementsNumber;

    private final String narrTable;

    private final char keyType;

    private final String colDesc;

    private final String thematicIdx;

    private final char typeChar;

    /** Describe variable <code>valDescTableName</code> here. */
    private final String valDescTableName;

    /** Constructor with all of the elements of a VPF column */
    public VPFColumn(
            String name,
            char type,
            int elementsNumber,
            char keyType,
            String colDesc,
            String valDescTableName,
            String thematicIdx,
            String narrTable) {
        this.name = name;
        this.typeChar = type;
        this.elementsNumber = elementsNumber;
        this.keyType = keyType;
        this.colDesc = colDesc;
        this.valDescTableName = valDescTableName;
        this.thematicIdx = thematicIdx;
        this.narrTable = narrTable;
        // VPFLogger.log("buildDescriptor: " + name);
        descriptor =
                new AttributeTypeBuilder()
                        .length(getColumnSize())
                        .description(colDesc)
                        .binding(getColumnClass())
                        .nillable(true)
                        .buildDescriptor(name);
        descriptor.getUserData().put("column", this);
    }

    public VPFColumn(String name, AttributeDescriptor descriptor) {

        this.name = name;
        this.descriptor = descriptor;

        this.typeChar = DATA_2_COORD_F;
        this.elementsNumber = 0;
        this.keyType = 0;
        this.colDesc = "geometry column placeholder";
        this.thematicIdx = null;
        this.narrTable = null;
        this.valDescTableName = null;
    }

    public static VPFColumn toVPFColumn(AttributeDescriptor descriptor) {
        return (VPFColumn) descriptor.getUserData().get("column");
    }

    public AttributeDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Retrieves the class for the column, based on a char value.
     *
     * @return the class
     */
    public Class<?> getColumnClass() {
        Class<?> columnClass;

        switch (typeChar) {
            case DATA_LONG_INTEGER:
                columnClass = Integer.class;

                break;

            case DATA_SHORT_FLOAT:
                columnClass = Float.class;

                break;

            case DATA_LONG_FLOAT:
                columnClass = Double.class;

                break;

            case DATA_2_COORD_F:
            case DATA_2_COORD_R:
            case DATA_3_COORD_F:
            case DATA_3_COORD_R:
                columnClass = Geometry.class;

                break;

            case DATA_TRIPLET_ID:
                columnClass = TripletId.class;

                break;

                // Short integers are usually coded values
            case DATA_SHORT_INTEGER:
                attemptLookup = true;
                // Fall through
            case DATA_TEXT:
            case DATA_NULL_FIELD:
            case DATA_LEVEL1_TEXT:
            case DATA_LEVEL2_TEXT:
            case DATA_LEVEL3_TEXT:

            default:
                columnClass = String.class;

                break;
        }

        return columnClass;
    }

    /**
     * Gets the size of the column in bytes
     *
     * @return the size
     */
    private int getColumnSize() {
        return DataUtils.getDataTypeSize(typeChar) * elementsNumber;
    }

    /**
     * Gets the value of narrTable
     *
     * @return the value of narrTable
     */
    public String getNarrTable() {
        return this.narrTable;
    }

    /**
     * Gets the value of thematicIdx
     *
     * @return the value of thematicIdx
     */
    public String getThematicIdx() {
        return this.thematicIdx;
    }

    /**
     * Gets the value of valDescTableName
     *
     * @return the value of valDescTableName
     */
    public String getValDescTableName() {
        return valDescTableName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.AttributeType#isGeometry()
     */
    public boolean isGeometry() {
        switch (typeChar) {
            case DATA_2_COORD_F:
            case DATA_2_COORD_R:
            case DATA_3_COORD_F:
            case DATA_3_COORD_R:
                return true;

            default:
                return false;
        }
    }

    /**
     * Returns the typeChar field
     *
     * @return Returns the typeChar.
     */
    public char getTypeChar() {
        return typeChar;
    }

    /**
     * Returns the elementsNumber field
     *
     * @return Returns the elementsNumber.
     */
    public int getElementsNumber() {
        return elementsNumber;
    }

    /**
     * Identifies and returns the GeometryAttributeType, or null if none exists.
     *
     * @return The <code>GeometryAttributeType</code> value
     */
    public GeometryDescriptor getGeometryAttributeType() {
        GeometryDescriptor result = null;

        if (isGeometry()) {
            result = (GeometryDescriptor) descriptor;
        }

        return result;
    }

    /** @return Returns the attemptLookup. */
    public boolean isAttemptLookup() {
        return attemptLookup;
    }

    public String getName() {
        return name;
    }

    public char getKeyType() {
        return keyType;
    }

    public String getColDesc() {
        return colDesc;
    }
}
