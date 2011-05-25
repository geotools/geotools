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
package org.geotools.xs;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.xml.XSD;
import org.opengis.feature.type.Schema;


/**
 * This interface contains the qualified names of all the types in the
 * http://www.w3.org/2001/XMLSchema schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class XS extends XSD {
    
    public static final String NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    public static final QName ALL = new QName("http://www.w3.org/2001/XMLSchema", "all");
    public static final QName ALLNNI = new QName("http://www.w3.org/2001/XMLSchema", "allNNI");
    public static final QName ANNOTATED = new QName("http://www.w3.org/2001/XMLSchema", "annotated");
    public static final QName ANYSIMPLETYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "anySimpleType");
    public static final QName ANYTYPE = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
    public static final QName ANYURI = new QName("http://www.w3.org/2001/XMLSchema", "anyURI");
    public static final QName ATTRIBUTE = new QName("http://www.w3.org/2001/XMLSchema", "attribute");
    public static final QName ATTRIBUTEGROUP = new QName("http://www.w3.org/2001/XMLSchema",
            "attributeGroup");
    public static final QName ATTRIBUTEGROUPREF = new QName("http://www.w3.org/2001/XMLSchema",
            "attributeGroupRef");
    public static final QName BASE64BINARY = new QName("http://www.w3.org/2001/XMLSchema",
            "base64Binary");
    public static final QName BLOCKSET = new QName("http://www.w3.org/2001/XMLSchema", "blockSet");
    public static final QName BOOLEAN = new QName("http://www.w3.org/2001/XMLSchema", "boolean");
    public static final QName BYTE = new QName("http://www.w3.org/2001/XMLSchema", "byte");
    public static final QName COMPLEXRESTRICTIONTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "complexRestrictionType");
    public static final QName COMPLEXTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "complexType");
    public static final QName DATE = new QName("http://www.w3.org/2001/XMLSchema", "date");
    public static final QName DATETIME = new QName("http://www.w3.org/2001/XMLSchema", "dateTime");
    public static final QName DECIMAL = new QName("http://www.w3.org/2001/XMLSchema", "decimal");
    public static final QName DERIVATIONCONTROL = new QName("http://www.w3.org/2001/XMLSchema",
            "derivationControl");
    public static final QName DERIVATIONSET = new QName("http://www.w3.org/2001/XMLSchema",
            "derivationSet");
    public static final QName DOUBLE = new QName("http://www.w3.org/2001/XMLSchema", "double");
    public static final QName DURATION = new QName("http://www.w3.org/2001/XMLSchema", "duration");
    public static final QName ELEMENT = new QName("http://www.w3.org/2001/XMLSchema", "element");
    public static final QName ENTITIES = new QName("http://www.w3.org/2001/XMLSchema", "ENTITIES");
    public static final QName ENTITY = new QName("http://www.w3.org/2001/XMLSchema", "ENTITY");
    public static final QName EXPLICITGROUP = new QName("http://www.w3.org/2001/XMLSchema",
            "explicitGroup");
    public static final QName EXTENSIONTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "extensionType");
    public static final QName FACET = new QName("http://www.w3.org/2001/XMLSchema", "facet");
    public static final QName FLOAT = new QName("http://www.w3.org/2001/XMLSchema", "float");
    public static final QName FORMCHOICE = new QName("http://www.w3.org/2001/XMLSchema",
            "formChoice");
    public static final QName FULLDERIVATIONSET = new QName("http://www.w3.org/2001/XMLSchema",
            "fullDerivationSet");
    public static final QName GDAY = new QName("http://www.w3.org/2001/XMLSchema", "gDay");
    public static final QName GMONTH = new QName("http://www.w3.org/2001/XMLSchema", "gMonth");
    public static final QName GMONTHDAY = new QName("http://www.w3.org/2001/XMLSchema", "gMonthDay");
    public static final QName GROUP = new QName("http://www.w3.org/2001/XMLSchema", "group");
    public static final QName GROUPREF = new QName("http://www.w3.org/2001/XMLSchema", "groupRef");
    public static final QName GYEAR = new QName("http://www.w3.org/2001/XMLSchema", "gYear");
    public static final QName GYEARMONTH = new QName("http://www.w3.org/2001/XMLSchema",
            "gYearMonth");
    public static final QName HEXBINARY = new QName("http://www.w3.org/2001/XMLSchema", "hexBinary");
    public static final QName ID = new QName("http://www.w3.org/2001/XMLSchema", "ID");
    public static final QName IDREF = new QName("http://www.w3.org/2001/XMLSchema", "IDREF");
    public static final QName IDREFS = new QName("http://www.w3.org/2001/XMLSchema", "IDREFS");
    public static final QName INT = new QName("http://www.w3.org/2001/XMLSchema", "int");
    public static final QName INTEGER = new QName("http://www.w3.org/2001/XMLSchema", "integer");
    public static final QName KEYBASE = new QName("http://www.w3.org/2001/XMLSchema", "keybase");
    public static final QName LANGUAGE = new QName("http://www.w3.org/2001/XMLSchema", "language");
    public static final QName LOCALCOMPLEXTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "localComplexType");
    public static final QName LOCALELEMENT = new QName("http://www.w3.org/2001/XMLSchema",
            "localElement");
    public static final QName LOCALSIMPLETYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "localSimpleType");
    public static final QName LONG = new QName("http://www.w3.org/2001/XMLSchema", "long");
    public static final QName NAME = new QName("http://www.w3.org/2001/XMLSchema", "Name");
    public static final QName NAMEDATTRIBUTEGROUP = new QName("http://www.w3.org/2001/XMLSchema",
            "namedAttributeGroup");
    public static final QName NAMEDGROUP = new QName("http://www.w3.org/2001/XMLSchema",
            "namedGroup");
    public static final QName NAMESPACELIST = new QName("http://www.w3.org/2001/XMLSchema",
            "namespaceList");
    public static final QName NARROWMAXMIN = new QName("http://www.w3.org/2001/XMLSchema",
            "narrowMaxMin");
    public static final QName NCNAME = new QName("http://www.w3.org/2001/XMLSchema", "NCName");
    public static final QName NEGATIVEINTEGER = new QName("http://www.w3.org/2001/XMLSchema",
            "negativeInteger");
    public static final QName NMTOKEN = new QName("http://www.w3.org/2001/XMLSchema", "NMTOKEN");
    public static final QName NMTOKENS = new QName("http://www.w3.org/2001/XMLSchema", "NMTOKENS");
    public static final QName NOFIXEDFACET = new QName("http://www.w3.org/2001/XMLSchema",
            "noFixedFacet");
    public static final QName NONNEGATIVEINTEGER = new QName("http://www.w3.org/2001/XMLSchema",
            "nonNegativeInteger");
    public static final QName NONPOSITIVEINTEGER = new QName("http://www.w3.org/2001/XMLSchema",
            "nonPositiveInteger");
    public static final QName NORMALIZEDSTRING = new QName("http://www.w3.org/2001/XMLSchema",
            "normalizedString");
    public static final QName NOTATION = new QName("http://www.w3.org/2001/XMLSchema", "NOTATION");
    public static final QName NUMFACET = new QName("http://www.w3.org/2001/XMLSchema", "numFacet");
    public static final QName OPENATTRS = new QName("http://www.w3.org/2001/XMLSchema", "openAttrs");
    public static final QName POSITIVEINTEGER = new QName("http://www.w3.org/2001/XMLSchema",
            "positiveInteger");
    public static final QName PUBLIC = new QName("http://www.w3.org/2001/XMLSchema", "public");
    public static final QName QNAME = new QName("http://www.w3.org/2001/XMLSchema", "QName");
    public static final QName REALGROUP = new QName("http://www.w3.org/2001/XMLSchema", "realGroup");
    public static final QName REDUCEDDERIVATIONCONTROL = new QName("http://www.w3.org/2001/XMLSchema",
            "reducedDerivationControl");
    public static final QName RESTRICTIONTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "restrictionType");
    public static final QName SHORT = new QName("http://www.w3.org/2001/XMLSchema", "short");
    public static final QName SIMPLEDERIVATIONSET = new QName("http://www.w3.org/2001/XMLSchema",
            "simpleDerivationSet");
    public static final QName SIMPLEEXPLICITGROUP = new QName("http://www.w3.org/2001/XMLSchema",
            "simpleExplicitGroup");
    public static final QName SIMPLEEXTENSIONTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "simpleExtensionType");
    public static final QName SIMPLERESTRICTIONTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "simpleRestrictionType");
    public static final QName SIMPLETYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "simpleType");
    public static final QName STRING = new QName("http://www.w3.org/2001/XMLSchema", "string");
    public static final QName TIME = new QName("http://www.w3.org/2001/XMLSchema", "time");
    public static final QName TOKEN = new QName("http://www.w3.org/2001/XMLSchema", "token");
    public static final QName TOPLEVELATTRIBUTE = new QName("http://www.w3.org/2001/XMLSchema",
            "topLevelAttribute");
    public static final QName TOPLEVELCOMPLEXTYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "topLevelComplexType");
    public static final QName TOPLEVELELEMENT = new QName("http://www.w3.org/2001/XMLSchema",
            "topLevelElement");
    public static final QName TOPLEVELSIMPLETYPE = new QName("http://www.w3.org/2001/XMLSchema",
            "topLevelSimpleType");
    public static final QName TYPEDERIVATIONCONTROL = new QName("http://www.w3.org/2001/XMLSchema",
            "typeDerivationControl");
    public static final QName UNSIGNEDBYTE = new QName("http://www.w3.org/2001/XMLSchema",
            "unsignedByte");
    public static final QName UNSIGNEDINT = new QName("http://www.w3.org/2001/XMLSchema",
            "unsignedInt");
    public static final QName UNSIGNEDLONG = new QName("http://www.w3.org/2001/XMLSchema",
            "unsignedLong");
    public static final QName UNSIGNEDSHORT = new QName("http://www.w3.org/2001/XMLSchema",
            "unsignedShort");
    public static final QName WILDCARD = new QName("http://www.w3.org/2001/XMLSchema", "wildcard");

    //manually added
    
    /**
     * This is a psuedo type name which allows the handling of the special case of of complex types
     * with simple content.
     */
    public static final QName SIMPLECONTENTTYPE = new QName("http://www.w3.org/2001/XMLSchema", "simpleContent");
    
    /**
     * singleton instance.
     */
    private static XS instance = new XS();
    
    private XS() {
    }

    /**
     * The singleton instance.
     */
    public static XSD getInstance() {
        return instance;
    }

    @Override
    protected Schema buildTypeSchema() {
        return new XSSchema();
    }
    
    @Override
    protected Schema buildTypeMappingProfile(Schema schema) {
        Set proper = new HashSet();
        proper.add(name(BYTE)); //Byte.class
        proper.add(name(HEXBINARY)); //byte[].class 
        proper.add(name(SHORT)); //Short.class
        proper.add(name(INT)); //Integer.class
        proper.add(name(FLOAT)); //Float.class
        proper.add(name(LONG)); //Long.class
        proper.add(name(QNAME)); //Qname.class
        proper.add(name(DATE)); //java.sql.Date.class
        proper.add(name(DATETIME)); //java.sql.Timestamp.class
        proper.add(name(TIME));     //java.sql.Time.class
        proper.add(name(BOOLEAN)); //Boolean.class
        proper.add(name(DOUBLE)); //Double.class
        proper.add(name(STRING)); //String.class
        proper.add(name(INTEGER)); //BigInteger.class
        proper.add(name(DECIMAL)); //BigDecimal.class
        proper.add(name(ANYURI)); //URI.class
        Schema profile = schema.profile( proper );
        
        return profile;
    }
    
    /**
     * Returns 'http://www.w3.org/2001/XMLSchema'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    public String getSchemaLocation() {
        return getClass().getResource("XMLSchema.xsd").toString();
    }
}
