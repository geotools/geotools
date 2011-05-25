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

import java.util.Map;

import org.geotools.data.vpf.ifc.DataTypesDefinition;
import org.geotools.data.vpf.io.TripletId;
import org.geotools.data.vpf.util.DataUtils;
import org.geotools.feature.AttributeTypeBuilder;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyType;

import com.vividsolutions.jts.geom.Geometry;


/**
 * A column in a VPF File.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 *
 * @source $URL$
 */
public class VPFColumn implements AttributeDescriptor, DataTypesDefinition {
    /**
     * If the value is a short integer, that often means it has
     * an accompanying value in a string lookup table.
     */
    private boolean attemptLookup = false;
    /**
     * The contained attribute type. 
     * AttributeType operations are delegated to this object.
     */
    private final AttributeDescriptor attribute;

    /** Describe variable <code>elementsNumber</code> here. */
    private final int elementsNumber;

    /** Describe variable <code>narrTable</code> here. */
    private final String narrTable;

    /** Describe variable <code>keyType</code> here. */
    private final char keyType;

    /** Describe variable <code>colDesc</code> here. */
    private final String colDesc;

    /** Describe variable <code>thematicIdx</code> here. */
    private final String thematicIdx;

    /** Describe variable <code>type</code> here. */
    private final char typeChar;

    /** Describe variable <code>valDescTableName</code> here. */
    private final String valDescTableName;
    /**
     * Constructor with all of the elements of a VPF column
     * @param name
     * @param type
     * @param elementsNumber
     * @param keyType
     * @param colDesc
     * @param valDescTableName
     * @param thematicIdx
     * @param narrTable
     */
    public VPFColumn(String name, char type, int elementsNumber, char keyType,
        String colDesc, String valDescTableName, String thematicIdx,
        String narrTable) {
        this.typeChar = type;
        this.elementsNumber = elementsNumber;
        this.keyType = keyType;
        this.colDesc = colDesc;
        this.valDescTableName = valDescTableName;
        this.thematicIdx = thematicIdx;
        this.narrTable = narrTable;
        attribute = new AttributeTypeBuilder().length(getColumnSize())
            .binding( getColumnClass() ).nillable( true ).buildDescriptor(name);
    }
    
    /**
     * Retrieves the class for the column,
     * based on a char value.
     * @return the class
     */
    public Class getColumnClass() {
        Class columnClass;

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
     * @return the size
     */
    private int getColumnSize() {
        return DataUtils.getDataTypeSize(typeChar) * elementsNumber;
    }

    // no longer in ft
//    /* (non-Javadoc)
//     * @see org.geotools.feature.AttributeType#getFieldLength()
//     */
//    public int getFieldLength() {
//        return attribute.getFieldLength();
//    }

    /**
     * {@inheritDoc}
     */
    public String getLocalName() {
    	return attribute.getLocalName();
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

    /* (non-Javadoc)
     * @see org.geotools.feature.AttributeType#isGeometry()
     */
    public boolean isGeometry() {
        return attribute instanceof Geometry;
    }

    // no longer needed
//    /* (non-Javadoc)
//     * @see org.geotools.feature.AttributeType#isNested()
//     */
//    public boolean isNested() {
//        return attribute.isNested();
//    }

    /* (non-Javadoc)
     * @see org.geotools.feature.AttributeType#isNillable()
     */
    public boolean isNillable() {
        return attribute.isNillable();
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
     * Identifies and returns the GeometryAttributeType,
     * or null if none exists.
     * @return The <code>GeometryAttributeType</code> value
     */
    public GeometryDescriptor getGeometryAttributeType() {
        GeometryDescriptor result = null;

        if (isGeometry()) {
            result = (GeometryDescriptor) attribute;
        }

        return result;
    }
    /**
     * @return Returns the attemptLookup.
     */
    public boolean isAttemptLookup() {
        return attemptLookup;
    }
	
	/* (non-Javadoc)
	 * @see org.geotools.feature.AttributeType#getMinOccurs()
	 */
	public int getMinOccurs() {
		return 1;
	}
	/* (non-Javadoc)
	 * @see org.geotools.feature.AttributeType#getMaxOccurs()
	 */
	public int getMaxOccurs() {
		return 1;
	}

    public boolean equals(Object obj) {
	return attribute.equals(obj);
    }

    public int hashCode() {
	return attribute.hashCode();
    }
    
	public org.opengis.feature.type.AttributeType getType() {
		return attribute.getType();
	}
	public Name getName() {
		return attribute.getName();
	}
	public Object getDefaultValue() {
		return attribute.getDefaultValue();
	}
	public Map<Object, Object> getUserData() {
	    return attribute.getUserData();
	}
	public PropertyType type() {
		return attribute.getType();
	}
}
