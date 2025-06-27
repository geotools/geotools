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
package org.geotools.xml.gml;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.AttributeValue;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.AttributeValueGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.xLink.XLinkSchema;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class represents a hard coded, java interpreted version of the GML 2.1.2 schema. Instances of this class should
 * be prefered for use over a parsed instance as this class will create real instances for elements who's types
 * correspond to types defined in this schema.
 *
 * @author dzwiers www.refractions.net
 */
public class GMLSchema implements Schema {
    /** GML target namespace */
    public static final URI NAMESPACE = makeURI("http://www.opengis.net/gml");

    // static attribute set
    private static Attribute[] attributes = AttributeList.attributes1;

    // static complexType set
    private static ComplexType[] complexTypes = loadComplexTypes();

    // static element set
    private static Element[] elements = loadElements();

    // static attribute group set
    private static AttributeGroup[] attributeGroups = {
        new GMLAssociationAttributeGroup(),
    };

    // static simpleType set
    private static SimpleType[] simpleTypes = {
        new GMLNullType(),
    };
    private static Schema instance = new GMLSchema();

    /** Creates a new GMLSchema object. */
    private GMLSchema() {
        // no op constructor
    }

    public static void setLogLevel(Level l) {
        GMLComplexTypes.logger.setLevel(l);
        FCBuffer.logger.setLevel(l);
    }

    /** @see schema.Schema#getAttributeGroups() */
    @Override
    public AttributeGroup[] getAttributeGroups() {
        return attributeGroups;
    }

    /** @see schema.Schema#getAttributeDescriptors() */
    @Override
    public Attribute[] getAttributes() {
        return attributes;
    }

    /** @see schema.Schema#getBlockDefault() */
    @Override
    public int getBlockDefault() {
        return NONE;
    }

    /*
     * used to load the static class variable containing the set of complexTypes
     * associated with the GML Schema
     */
    private static final ComplexType[] loadComplexTypes() {
        ComplexType[] complexTypes1 = new ComplexType[31];
        complexTypes1[0] = GMLComplexTypes.AbstractGeometryType.getInstance();
        complexTypes1[1] = GMLComplexTypes.AbstractGeometryCollectionBaseType.getInstance();
        complexTypes1[2] = GMLComplexTypes.GeometryAssociationType.getInstance();
        complexTypes1[3] = GMLComplexTypes.PointMemberType.getInstance();
        complexTypes1[4] = GMLComplexTypes.LineStringMemberType.getInstance();
        complexTypes1[5] = GMLComplexTypes.PolygonMemberType.getInstance();
        complexTypes1[6] = GMLComplexTypes.LinearRingMemberType.getInstance();
        complexTypes1[7] = GMLComplexTypes.PointType.getInstance();
        complexTypes1[8] = GMLComplexTypes.LineStringType.getInstance();
        complexTypes1[9] = GMLComplexTypes.LinearRingType.getInstance();
        complexTypes1[10] = GMLComplexTypes.BoxType.getInstance();
        complexTypes1[11] = GMLComplexTypes.PolygonType.getInstance();
        complexTypes1[12] = GMLComplexTypes.GeometryCollectionType.getInstance();
        complexTypes1[13] = GMLComplexTypes.MultiPointType.getInstance();
        complexTypes1[14] = GMLComplexTypes.MultiLineStringType.getInstance();
        complexTypes1[15] = GMLComplexTypes.MultiPolygonType.getInstance();
        complexTypes1[16] = GMLComplexTypes.CoordType.getInstance();
        complexTypes1[17] = GMLComplexTypes.CoordinatesType.getInstance();
        complexTypes1[18] = GMLComplexTypes.AbstractFeatureType.getInstance();
        complexTypes1[19] = GMLComplexTypes.AbstractFeatureCollectionsBaseType.getInstance();
        complexTypes1[20] = GMLComplexTypes.AbstractFeatureCollectionType.getInstance();
        complexTypes1[21] = GMLComplexTypes.GeometryPropertyType.getInstance();
        complexTypes1[22] = GMLComplexTypes.FeatureAssociationType.getInstance();
        complexTypes1[23] = GMLComplexTypes.BoundingShapeType.getInstance();
        complexTypes1[24] = GMLComplexTypes.PointPropertyType.getInstance();
        complexTypes1[25] = GMLComplexTypes.PolygonPropertyType.getInstance();
        complexTypes1[26] = GMLComplexTypes.LineStringPropertyType.getInstance();
        complexTypes1[27] = GMLComplexTypes.MultiPointPropertyType.getInstance();
        complexTypes1[28] = GMLComplexTypes.MultiLineStringPropertyType.getInstance();
        complexTypes1[29] = GMLComplexTypes.MultiPolygonPropertyType.getInstance();
        complexTypes1[30] = GMLComplexTypes.MultiGeometryPropertyType.getInstance();

        return complexTypes1;
    }

    /** @see schema.Schema#getComplexTypes() */
    @Override
    public ComplexType[] getComplexTypes() {
        return complexTypes;
    }

    /** GMLSchema.getInstance().getElements()[GMLSchema.BOX] */
    public static final int BOX = 41;
    /*
     * Used to load the set of elements stored in the static class variable
     * representing the element set in the GML Schema
     */
    private static final Element[] loadElements() {
        Element[] elements1 = new Element[48];

        elements1[0] = new GMLElement(
                "_Feature",
                GMLComplexTypes.AbstractFeatureType.getInstance(),
                1,
                1,
                true,
                null); // gml:AbstractFeatureType
        elements1[1] = new GMLElement(
                "featureCollection",
                GMLComplexTypes.AbstractFeatureCollectionType.getInstance(),
                1,
                1,
                true,
                elements1[0]); // gml:AbstractFeatureCollectionType
        elements1[2] = new GMLElement(
                "featureMember",
                GMLComplexTypes.FeatureAssociationType.getInstance(),
                1,
                1,
                false,
                null); // gml:FeatureAssociationType
        elements1[3] = new GMLElement(
                "_geometryProperty",
                GMLComplexTypes.GeometryAssociationType.getInstance(),
                1,
                1,
                true,
                null); // gml:GeometryAssociationType
        elements1[4] = new GMLElement(
                "geometryProperty",
                GMLComplexTypes.GeometryAssociationType.getInstance(),
                1,
                1,
                false,
                null); // gml:GeometryAssociationType
        elements1[5] = new GMLElement(
                "boundedBy",
                GMLComplexTypes.BoundingShapeType.getInstance(),
                1,
                1,
                false,
                null); // gml:BoundingShapeType
        elements1[6] = new GMLElement(
                "pointProperty",
                GMLComplexTypes.PointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[3]); // gml:PointPropertyType
        elements1[7] = new GMLElement(
                "polygonProperty",
                GMLComplexTypes.PolygonPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[3]); // gml:PolygonPropertyType
        elements1[8] = new GMLElement(
                "lineStringProperty",
                GMLComplexTypes.LineStringPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[3]); // gml:LineStringPropertyType
        elements1[9] = new GMLElement(
                "multiPointProperty",
                GMLComplexTypes.MultiPointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[3]); // gml:MultiPointPropertyType
        elements1[10] = new GMLElement(
                "multiLineStringProperty",
                GMLComplexTypes.MultiLineStringPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[3]); // gml:MultiLineStringPropertyType
        elements1[11] = new GMLElement(
                "multiPolygonProperty",
                GMLComplexTypes.MultiPolygonPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[3]); // gml:MultiPolygonPropertyType
        elements1[12] = new GMLElement(
                "multiGeometryProperty",
                GMLComplexTypes.MultiGeometryPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[3]); // gml:MultiGeometryPropertyType
        elements1[13] = new GMLElement(
                "location",
                GMLComplexTypes.PointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[6]); // gml:PointPropertyType
        elements1[14] = new GMLElement(
                "centerOf",
                GMLComplexTypes.PointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[6]); // gml:PointPropertyType
        elements1[15] = new GMLElement(
                "position",
                GMLComplexTypes.PointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[6]); // gml:PointPropertyType
        elements1[16] = new GMLElement(
                "extentOf",
                GMLComplexTypes.PolygonPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[7]); // gml:PolygonPropertyType
        elements1[17] = new GMLElement(
                "coverage",
                GMLComplexTypes.PolygonPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[7]); // gml:PolygonPropertyType
        elements1[18] = new GMLElement(
                "edgeOf",
                GMLComplexTypes.LineStringPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[8]); // gml:LineStringPropertyType
        elements1[19] = new GMLElement(
                "centerLineOf",
                GMLComplexTypes.LineStringPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[8]); // gml:LineStringPropertyType
        elements1[20] = new GMLElement(
                "multiLocation",
                GMLComplexTypes.MultiPointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[9]); // gml:MultiPointPropertyType
        elements1[21] = new GMLElement(
                "multiCenterOf",
                GMLComplexTypes.MultiPointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[9]); // gml:MultiPointPropertyType
        elements1[22] = new GMLElement(
                "multiPosition",
                GMLComplexTypes.MultiPointPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[9]); // gml:MultiPointPropertyType
        elements1[23] = new GMLElement(
                "multiCenterLineOf",
                GMLComplexTypes.MultiLineStringPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[10]); // gml:MultiLineStringPropertyType
        elements1[24] = new GMLElement(
                "multiEdgeOf",
                GMLComplexTypes.MultiLineStringPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[10]); // gml:MultiLineStringPropertyType
        elements1[25] = new GMLElement(
                "multiCoverage",
                GMLComplexTypes.MultiPolygonPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[11]); // gml:MultiPolygonPropertyType
        elements1[26] = new GMLElement(
                "multiExtentOf",
                GMLComplexTypes.MultiPolygonPropertyType.getInstance(),
                1,
                1,
                false,
                elements1[11]); // gml:MultiPolygonPropertyType
        elements1[28] = new GMLElement("name", XSISimpleTypes.String.getInstance(), 1, 1, false, null); // xs:string
        elements1[27] =
                new GMLElement("description", XSISimpleTypes.String.getInstance(), 1, 1, false, null); // xs:string
        elements1[29] = new GMLElement(
                "_Geometry",
                GMLComplexTypes.AbstractGeometryType.getInstance(),
                1,
                1,
                true,
                null); // gml:AbstractGeometryType
        elements1[30] = new GMLElement(
                "GeometryCollection",
                GMLComplexTypes.GeometryCollectionType.getInstance(),
                1,
                1,
                true,
                elements1[29]); // gml:GeometryCollectionType
        elements1[31] = new GMLElement(
                "geometryMember",
                GMLComplexTypes.GeometryAssociationType.getInstance(),
                1,
                1,
                false,
                null); // gml:GeometryAssociationType
        elements1[32] = new GMLElement(
                "pointMember",
                GMLComplexTypes.PointMemberType.getInstance(),
                1,
                1,
                false,
                elements1[31]); // gml:PointMemberType
        elements1[33] = new GMLElement(
                "lineStringMember",
                GMLComplexTypes.PointMemberType.getInstance(),
                1,
                1,
                false,
                elements1[31]); // gml:PointMemberType
        elements1[34] = new GMLElement(
                "polygonMember",
                GMLComplexTypes.PointMemberType.getInstance(),
                1,
                1,
                false,
                elements1[31]); // gml:PointMemberType
        elements1[35] = new GMLElement(
                "outerBoundaryIs",
                GMLComplexTypes.LinearRingMemberType.getInstance(),
                1,
                1,
                false,
                null); // gml:LinearRingMemberType
        elements1[36] = new GMLElement(
                "innerBoundaryIs",
                GMLComplexTypes.LinearRingMemberType.getInstance(),
                1,
                1,
                false,
                null); // gml:LinearRingMemberType
        elements1[37] = new GMLElement(
                "Point", GMLComplexTypes.PointType.getInstance(), 1, 1, false, elements1[29]); // gml:PointType
        elements1[38] = new GMLElement(
                "LineString",
                GMLComplexTypes.LineStringType.getInstance(),
                1,
                1,
                false,
                elements1[29]); // gml:LineStringType
        elements1[39] = new GMLElement(
                "LinearRing",
                GMLComplexTypes.LinearRingType.getInstance(),
                1,
                1,
                false,
                elements1[29]); // gml:LinearRingType
        elements1[40] = new GMLElement(
                "Polygon", GMLComplexTypes.PolygonType.getInstance(), 1, 1, false, elements1[29]); // gml:PolygonType
        elements1[BOX] = new GMLElement("Box", GMLComplexTypes.BoxType.getInstance(), 1, 1, false, null); // gml:BoxType
        elements1[42] = new GMLElement(
                "MultiGeometry",
                GMLComplexTypes.GeometryCollectionType.getInstance(),
                1,
                1,
                false,
                elements1[29]); // gml:GeometryCollectionType
        elements1[43] = new GMLElement(
                "MultiPoint",
                GMLComplexTypes.MultiPointType.getInstance(),
                1,
                1,
                false,
                elements1[29]); // gml:MultiPointType
        elements1[44] = new GMLElement(
                "MultiLineString",
                GMLComplexTypes.MultiLineStringType.getInstance(),
                1,
                1,
                false,
                elements1[29]); // gml:MultiLineStringType
        elements1[45] = new GMLElement(
                "MultiPolygon",
                GMLComplexTypes.MultiPolygonType.getInstance(),
                1,
                1,
                false,
                elements1[29]); // gml:MultiPolygonType
        elements1[46] =
                new GMLElement("coord", GMLComplexTypes.CoordType.getInstance(), 1, 1, false, null); // gml:CoordType
        elements1[47] = new GMLElement(
                "coordinates", GMLComplexTypes.CoordinatesType.getInstance(), 1, 1, false, null); // gml:CoordinatesType

        return elements1;
    }

    /** @see schema.Schema#isElementFormDefault() */
    @Override
    public boolean isElementFormDefault() {
        return true;
    }

    /** @see schema.Schema#getElements() */
    @Override
    public Element[] getElements() {
        return elements;
    }

    /** @see schema.Schema#getFinalDefault() */
    @Override
    public int getFinalDefault() {
        return NONE;
    }

    /** @see schema.Schema#getGroups() */
    @Override
    public Group[] getGroups() {
        return new Group[0];
    }

    /** @see schema.Schema#getId() */
    @Override
    public String getId() {
        return null;
    }

    private static Schema[] imports = {XLinkSchema.getInstance()};
    /** @see schema.Schema#getImports() */
    @Override
    public Schema[] getImports() {
        return imports;
    }

    /** @see schema.Schema#getSimpleTypes() */
    @Override
    public SimpleType[] getSimpleTypes() {
        return simpleTypes;
    }

    /** @see schema.Schema#getTargetNamespace() */
    @Override
    public URI getTargetNamespace() {
        return NAMESPACE;
    }

    @Override
    public URI getURI() {
        return NAMESPACE;
    }

    /** @see schema.Schema#getVersion() */
    @Override
    public String getVersion() {
        return "2.1.2";
    }

    private static URI makeURI(String s) {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            // do nothing
            return null;
        }
    }

    /** @see schema.Schema#includesURI(java.net.URI) */
    @Override
    public boolean includesURI(URI uri) {
        //        if (uri.toString().toLowerCase().endsWith("geometry.xsd")
        //                || uri.toString().toLowerCase().endsWith("feature.xsd")) {
        //            return true;
        //        }
        //
        //        return false;
        // this is a spec ... we never want the def modified.
        // TODO see if this affects printing
        return true;
    }

    /** @see schema.Schema#isAttributeFormDefault() */
    @Override
    public boolean isAttributeFormDefault() {
        return false;
    }

    /** @see org.geotools.xml.schema.Schema#getPrefix() */
    @Override
    public String getPrefix() {
        return "gml";
    }

    /** @see org.geotools.xml.schema.Schema#getInstance() */
    public static Schema getInstance() {
        return instance;
    }

    static class AttributeList {
        static final Attribute[] attributes1 = {
            new GMLAttribute("remoteSchema", XSISimpleTypes.AnyURI.getInstance()),
        };
    }

    /**
     * Adds some common information and functionality to a base element to be used by the GMLSchema. The remaining data
     * will be configured upon creation.
     *
     * @author dzwiers
     * @see Element
     */
    static class GMLElement implements Element {
        // default visibily to remove the set* methods ... this class is
        // only package visible
        boolean abstracT = false;
        int max;
        int min;
        String name;
        Type type;
        Element substitutionGroup;

        /*
         * Should never be called
         */
        private GMLElement() {
            // no op constructor
        }

        /**
         * Configures the Element for this particular GML instance. The following params match schema definition
         * attributes found in an element declaration. Those missing have been hard coded for the gml Schema.
         */
        public GMLElement(String name, Type type, int min, int max, boolean abstracT, Element substitutionGroup) {
            this.abstracT = abstracT;
            this.max = max;
            this.min = min;
            this.name = name;
            this.type = type;
            this.substitutionGroup = substitutionGroup;
        }

        /** Creates a clone using the new min/max occurences. */
        public GMLElement(GMLElement element, int min, int max) {
            this.abstracT = element.isAbstract();
            this.max = max;
            this.min = min;
            this.name = element.getName();
            this.type = element.getType();
            this.substitutionGroup = element.getSubstitutionGroup();
        }

        /** @see org.geotools.xml.xsi.ElementGrouping#findChildElement(java.lang.String) */
        @Override
        public Element findChildElement(String name1) {
            if (this.name != null) {
                if (this.name.equals(name1)) {
                    return this;
                }
            }

            return null;
        }

        /** @see org.geotools.xml.xsi.ElementGrouping#getGrouping() */
        @Override
        public int getGrouping() {
            return ELEMENT;
        }

        /** @see schema.Element#isAbstract() */
        @Override
        public boolean isAbstract() {
            return abstracT;
        }

        /** @see schema.Element#getBlock() */
        @Override
        public int getBlock() {
            return Schema.NONE;
        }

        /** @see schema.Element#getDefault() */
        @Override
        public String getDefault() {
            return null;
        }

        /** @see schema.Element#getFinal() */
        @Override
        public int getFinal() {
            return Schema.NONE;
        }

        /** @see schema.Element#getFixed() */
        @Override
        public String getFixed() {
            return null;
        }

        /** @see schema.Element#isForm() */
        @Override
        public boolean isForm() {
            return false;
        }

        /** @see schema.Element#getId() */
        @Override
        public String getId() {
            return null;
        }

        /** @see schema.Element#getMaxOccurs() */
        @Override
        public int getMaxOccurs() {
            return max;
        }

        /** @see schema.Element#getMinOccurs() */
        @Override
        public int getMinOccurs() {
            return min;
        }

        /** @see schema.Element#getLocalName() */
        @Override
        public String getName() {
            return name;
        }

        /** @see schema.Element#isNillable() */
        @Override
        public boolean isNillable() {
            return false;
        }

        /** @see schema.Element#getSubstitutionGroup() */
        @Override
        public Element getSubstitutionGroup() {
            return substitutionGroup;
        }

        /** @see schema.Element#getBinding() */
        @Override
        public Type getType() {
            return type;
        }

        /** @see schema.Element#getNamespace() */
        @Override
        public URI getNamespace() {
            return GMLSchema.NAMESPACE;
        }

        @Override
        public Element findChildElement(String localName, URI namespaceURI) {
            if (this.name != null) {
                if (this.name.equals(localName) && getNamespace().equals(namespaceURI)) {
                    return this;
                }
            }

            return null;
        }
    }

    /**
     * This abstract class represents some default and constant values associated with a GML complexType.
     *
     * @author dzwiers
     * @see ComplexType
     */
    abstract static class GMLComplexType implements ComplexType {
        /** @see schema.ComplexType#getBlock() */
        @Override
        public int getBlock() {
            return Schema.NONE;
        }

        /** @see schema.ComplexType#getFinal() */
        @Override
        public int getFinal() {
            return Schema.NONE;
        }

        /** @see schema.ComplexType#getId() */
        @Override
        public String getId() {
            return null;
        }

        /** @see schema.ComplexType#isMixed() */
        @Override
        public boolean isMixed() {
            return false;
        }

        /** @see schema.ComplexType#getNamespace() */
        @Override
        public URI getNamespace() {
            return GMLSchema.NAMESPACE;
        }

        /*
         * included here to deal generically with a GML complexType ...
         * part of the singleton pattern.
         */
        static GMLComplexType getInstance() {
            return null;
        }

        /** @see schema.ComplexType#isDerived() */
        @Override
        public boolean isDerived() {
            return false;
        }

        /** @see schema.ComplexType#getParent() */
        @Override
        public Type getParent() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#cache() */
        @Override
        public boolean cache(Element e, Map<String, Object> hints) {
            return true;
        }
    }

    /**
     * An instance of this class represents a GML attribute. This implementation contains some constant data pertinent
     * to the GML Schema, and some configurable data depending on the GML attribute being represented.
     *
     * @author dzwiers
     * @see Attribute
     */
    static class GMLAttribute implements Attribute {
        // package visible class variable, used to avoid set* methods
        String name;
        String def = null;
        SimpleType simpleType;
        int use = Attribute.OPTIONAL;

        /*
         * Should never be called
         */
        private GMLAttribute() {
            // no op constructor
        }

        /** Creates a GML attribute based on the name and type provided. */
        public GMLAttribute(String name, SimpleType simpleType) {
            this.name = name;
            this.simpleType = simpleType;
        }

        /** Creates a GML attribute based on the name, use and type provided. */
        public GMLAttribute(String name, SimpleType simpleType, int use) {
            this.name = name;
            this.simpleType = simpleType;
            this.use = use;
        }

        /** Creates a GML attribute based on the name, use, default and type provided. */
        public GMLAttribute(String name, SimpleType simpleType, int use, String def) {
            this.name = name;
            this.simpleType = simpleType;
            this.use = use;
            this.def = def;
        }

        /** @see schema.Attribute#getNameSpace() */
        @Override
        public URI getNamespace() {
            return GMLSchema.NAMESPACE;
        }

        /** @see schema.Attribute#getDefault() */
        @Override
        public String getDefault() {
            return def;
        }

        /** @see schema.Attribute#getFixed() */
        @Override
        public String getFixed() {
            return null;
        }

        /** @see schema.Attribute#isForm() */
        @Override
        public boolean isForm() {
            return false;
        }

        /** @see schema.Attribute#getId() */
        @Override
        public String getId() {
            return null;
        }

        /** @see schema.Attribute#getLocalName() */
        @Override
        public String getName() {
            return name;
        }

        /** @see schema.Attribute#getUse() */
        @Override
        public int getUse() {
            return use;
        }

        /** @see schema.Attribute#getSimpleType() */
        @Override
        public SimpleType getSimpleType() {
            return simpleType;
        }
    }

    /**
     * This implementation of an Attribute Group represents the GML Association Attribute Group as specified in the GML
     * Schema.
     *
     * @author dzwiers
     * @see AttributeGroup
     */
    static class GMLAssociationAttributeGroup implements AttributeGroup {
        // package visible attribute set
        static final Attribute[] attributes1 = {
            new GMLAttribute("remoteSchema", XSISimpleTypes.AnyURI.getInstance()),
        };

        // package visible child declaration
        static final AttributeGroup child = XLinkSchema.SimpleLink.getInstance();

        /** @see schema.Attribute#getNameSpace() */
        @Override
        public URI getNamespace() {
            return GMLSchema.NAMESPACE;
        }

        /** @see schema.AttributeGroup#getAnyAttributeNameSpace() */
        @Override
        public String getAnyAttributeNameSpace() {
            return child.getAnyAttributeNameSpace();
        }

        /** @see schema.Schema#getAttributeDescriptors() */
        @Override
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /** @see schema.AttributeGroup#getId() */
        @Override
        public String getId() {
            return null;
        }

        /** @see schema.AttributeGroup#getLocalName() */
        @Override
        public String getName() {
            return "AssociationAttributeGroup";
        }
    }

    /**
     * This class represents the GML NullType declaration of a simple type.
     *
     * @author dzwiers
     * @see SimpleType
     */
    static class GMLNullType implements SimpleType {
        /** @see schema.SimpleType#getNamespace() */
        @Override
        public URI getNamespace() {
            return GMLSchema.NAMESPACE;
        }

        /** @see org.geotools.xml.xsi.Type#getParent() */
        public Type getParent() {
            return null;
        }

        /** @see schema.SimpleType#getFinal() */
        @Override
        public int getFinal() {
            return Schema.NONE;
        }

        /** @see schema.SimpleType#getId() */
        @Override
        public String getId() {
            return null;
        }

        /** @see schema.SimpleType#getLocalName() */
        @Override
        public String getName() {
            return "NullType";
        }

        /** @see schema.Type#getValue(java.lang.String) */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attr, Map<String, Object> hints)
                throws SAXException {
            if (value == null || value.length != 1 || value[0].getValue() == null) {
                return null;
            }

            if (!(value[0].getValue() instanceof String)) {
                throw new SAXException("SimpleTypes can only evaluate Strings");
            }

            String text = (String) value[0].getValue();
            text = text.trim();

            final String[] enumeration = {"inapplicable", "unknown", "unavailable", "missing"};

            if (contains(enumeration, text)) {
                return value[0].getValue();
            }

            throw new SAXException(
                    "The value passed in to gml:NullType was not one of the allowable enumerated values."); // unacceptable result
        }

        private boolean contains(String[] enumeration, String text) {
            for (String s : enumeration) {
                if (s.equalsIgnoreCase(text)) return true;
            }
            return false;
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return String.class;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#toAttribute(org.geotools.xml.schema.Attribute, java.lang.Object,
         *     java.util.Map)
         */
        @Override
        public AttributeValue toAttribute(Attribute attribute, Object value, Map<String, Object> hints) {
            final String[] enumeration = {"inapplicable", "unknown", "unavailable", "missing"};

            if (Arrays.binarySearch(enumeration, value) < 0) {
                // not found
                return new AttributeValueGT(attribute, null);
            }

            return new AttributeValueGT(attribute, value.toString());
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#canCreateAttributes(org.geotools.xml.schema.Attribute,
         *     java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canCreateAttributes(Attribute attribute, Object value, Map<String, Object> hints) {
            final String[] enumeration = {"inapplicable", "unknown", "unavailable", "missing"};

            return Arrays.binarySearch(enumeration, value) < 0;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            final String[] enumeration = {"inapplicable", "unknown", "unavailable", "missing"};

            return Arrays.binarySearch(enumeration, value) < 0;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws IOException {
            output.startElement(element.getNamespace(), element.getName(), null);
            output.characters(value.toString());
            output.endElement(element.getNamespace(), element.getName());
        }

        /** @see org.geotools.xml.schema.SimpleType#getChildType() */
        @Override
        public int getChildType() {
            return RESTRICTION;
        }

        /** @see org.geotools.xml.schema.SimpleType#getParents() */
        @Override
        public SimpleType[] getParents() {
            return new SimpleType[] {
                XSISimpleTypes.String.getInstance(),
            };
        }

        /** @see org.geotools.xml.schema.SimpleType#getFacets() */
        @Override
        public Facet[] getFacets() {
            return new Facet[] {
                new FacetGT(Facet.ENUMERATION, "inapplicable"),
                new FacetGT(Facet.ENUMERATION, "unknown"),
                new FacetGT(Facet.ENUMERATION, "unavailable"),
                new FacetGT(Facet.ENUMERATION, "missing"),
            };
        }

        /** @see org.geotools.xml.schema.Type#findChildElement(java.lang.String) */
        @Override
        public Element findChildElement(String name) {
            return null; // will never happen
        }
    }

    /** Returns the implementation hints. The default implementation returns en empty map. */
    @Override
    public Map<java.awt.RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
