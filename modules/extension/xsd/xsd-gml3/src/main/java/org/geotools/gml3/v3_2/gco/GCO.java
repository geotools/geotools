/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.v3_2.gco;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.StubbedGMLXSD;
import org.geotools.xlink.XLINK;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.isotc211.org/2005/gco schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class GCO extends StubbedGMLXSD {

    /** singleton instance */
    private static final GCO instance = new GCO();
    static {
       loadSchema( instance );
    }
    /**
     * Returns the singleton instance.
     */
    public static final GCO getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private GCO() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add( XLINK.getInstance() );
        //dependencies.add( GML.getInstance() ); JD: cycle
    }
    
    /**
     * Returns 'http://www.isotc211.org/2005/gco'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'gco.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("gco.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.isotc211.org/2005/gco";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractObject_Type = 
        new QName("http://www.isotc211.org/2005/gco","AbstractObject_Type");
    /** @generated */
    public static final QName Angle_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Angle_PropertyType");
    /** @generated */
    public static final QName Binary_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Binary_PropertyType");
    /** @generated */
    public static final QName Binary_Type = 
        new QName("http://www.isotc211.org/2005/gco","Binary_Type");
    /** @generated */
    public static final QName Boolean_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Boolean_PropertyType");
    /** @generated */
    public static final QName CharacterString_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","CharacterString_PropertyType");
    /** @generated */
    public static final QName CodeListValue_Type = 
        new QName("http://www.isotc211.org/2005/gco","CodeListValue_Type");
    /** @generated */
    public static final QName Date_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Date_PropertyType");
    /** @generated */
    public static final QName Date_Type = 
        new QName("http://www.isotc211.org/2005/gco","Date_Type");
    /** @generated */
    public static final QName DateTime_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","DateTime_PropertyType");
    /** @generated */
    public static final QName Decimal_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Decimal_PropertyType");
    /** @generated */
    public static final QName Distance_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Distance_PropertyType");
    /** @generated */
    public static final QName GenericName_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","GenericName_PropertyType");
    /** @generated */
    public static final QName Integer_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Integer_PropertyType");
    /** @generated */
    public static final QName Length_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Length_PropertyType");
    /** @generated */
    public static final QName LocalName_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","LocalName_PropertyType");
    /** @generated */
    public static final QName Measure_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Measure_PropertyType");
    /** @generated */
    public static final QName MemberName_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","MemberName_PropertyType");
    /** @generated */
    public static final QName MemberName_Type = 
        new QName("http://www.isotc211.org/2005/gco","MemberName_Type");
    /** @generated */
    public static final QName Multiplicity_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Multiplicity_PropertyType");
    /** @generated */
    public static final QName Multiplicity_Type = 
        new QName("http://www.isotc211.org/2005/gco","Multiplicity_Type");
    /** @generated */
    public static final QName MultiplicityRange_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","MultiplicityRange_PropertyType");
    /** @generated */
    public static final QName MultiplicityRange_Type = 
        new QName("http://www.isotc211.org/2005/gco","MultiplicityRange_Type");
    /** @generated */
    public static final QName Number_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Number_PropertyType");
    /** @generated */
    public static final QName ObjectReference_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","ObjectReference_PropertyType");
    /** @generated */
    public static final QName Real_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Real_PropertyType");
    /** @generated */
    public static final QName Record_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Record_PropertyType");
    /** @generated */
    public static final QName RecordType_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","RecordType_PropertyType");
    /** @generated */
    public static final QName RecordType_Type = 
        new QName("http://www.isotc211.org/2005/gco","RecordType_Type");
    /** @generated */
    public static final QName Scale_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","Scale_PropertyType");
    /** @generated */
    public static final QName ScopedName_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","ScopedName_PropertyType");
    /** @generated */
    public static final QName TypeName_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","TypeName_PropertyType");
    /** @generated */
    public static final QName TypeName_Type = 
        new QName("http://www.isotc211.org/2005/gco","TypeName_Type");
    /** @generated */
    public static final QName UnitOfMeasure_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UnitOfMeasure_PropertyType");
    /** @generated */
    public static final QName UnlimitedInteger_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UnlimitedInteger_PropertyType");
    /** @generated */
    public static final QName UnlimitedInteger_Type = 
        new QName("http://www.isotc211.org/2005/gco","UnlimitedInteger_Type");
    /** @generated */
    public static final QName UomAngle_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UomAngle_PropertyType");
    /** @generated */
    public static final QName UomArea_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UomArea_PropertyType");
    /** @generated */
    public static final QName UomLength_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UomLength_PropertyType");
    /** @generated */
    public static final QName UomScale_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UomScale_PropertyType");
    /** @generated */
    public static final QName UomTime_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UomTime_PropertyType");
    /** @generated */
    public static final QName UomVelocity_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UomVelocity_PropertyType");
    /** @generated */
    public static final QName UomVolume_PropertyType = 
        new QName("http://www.isotc211.org/2005/gco","UomVolume_PropertyType");

    /* Elements */
    /** @generated */
    public static final QName AbstractGenericName = 
        new QName("http://www.isotc211.org/2005/gco","AbstractGenericName");
    /** @generated */
    public static final QName AbstractObject = 
        new QName("http://www.isotc211.org/2005/gco","AbstractObject");
    /** @generated */
    public static final QName Angle = 
        new QName("http://www.isotc211.org/2005/gco","Angle");
    /** @generated */
    public static final QName Binary = 
        new QName("http://www.isotc211.org/2005/gco","Binary");
    /** @generated */
    public static final QName Boolean = 
        new QName("http://www.isotc211.org/2005/gco","Boolean");
    /** @generated */
    public static final QName CharacterString = 
        new QName("http://www.isotc211.org/2005/gco","CharacterString");
    /** @generated */
    public static final QName Date = 
        new QName("http://www.isotc211.org/2005/gco","Date");
    /** @generated */
    public static final QName DateTime = 
        new QName("http://www.isotc211.org/2005/gco","DateTime");
    /** @generated */
    public static final QName Decimal = 
        new QName("http://www.isotc211.org/2005/gco","Decimal");
    /** @generated */
    public static final QName Distance = 
        new QName("http://www.isotc211.org/2005/gco","Distance");
    /** @generated */
    public static final QName Integer = 
        new QName("http://www.isotc211.org/2005/gco","Integer");
    /** @generated */
    public static final QName Length = 
        new QName("http://www.isotc211.org/2005/gco","Length");
    /** @generated */
    public static final QName LocalName = 
        new QName("http://www.isotc211.org/2005/gco","LocalName");
    /** @generated */
    public static final QName Measure = 
        new QName("http://www.isotc211.org/2005/gco","Measure");
    /** @generated */
    public static final QName MemberName = 
        new QName("http://www.isotc211.org/2005/gco","MemberName");
    /** @generated */
    public static final QName Multiplicity = 
        new QName("http://www.isotc211.org/2005/gco","Multiplicity");
    /** @generated */
    public static final QName MultiplicityRange = 
        new QName("http://www.isotc211.org/2005/gco","MultiplicityRange");
    /** @generated */
    public static final QName Real = 
        new QName("http://www.isotc211.org/2005/gco","Real");
    /** @generated */
    public static final QName Record = 
        new QName("http://www.isotc211.org/2005/gco","Record");
    /** @generated */
    public static final QName RecordType = 
        new QName("http://www.isotc211.org/2005/gco","RecordType");
    /** @generated */
    public static final QName Scale = 
        new QName("http://www.isotc211.org/2005/gco","Scale");
    /** @generated */
    public static final QName ScopedName = 
        new QName("http://www.isotc211.org/2005/gco","ScopedName");
    /** @generated */
    public static final QName TypeName = 
        new QName("http://www.isotc211.org/2005/gco","TypeName");
    /** @generated */
    public static final QName UnlimitedInteger = 
        new QName("http://www.isotc211.org/2005/gco","UnlimitedInteger");

    /* Attributes */
    /** @generated */
    public static final QName isoType = 
        new QName("http://www.isotc211.org/2005/gco","isoType");
    /** @generated */
    public static final QName nilReason = 
        new QName("http://www.isotc211.org/2005/gco","nilReason");

}
    
