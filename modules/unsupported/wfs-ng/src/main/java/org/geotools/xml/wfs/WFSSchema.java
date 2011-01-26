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
package org.geotools.xml.wfs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.xml.filter.FilterSchema;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.AttributeGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SimpleTypeGT;
import org.geotools.xml.wfs.WFSBasicComplexTypes.DescribeFeatureTypeType;
import org.geotools.xml.wfs.WFSBasicComplexTypes.FeatureCollectionType;
import org.geotools.xml.wfs.WFSBasicComplexTypes.GetCapabilitiesType;
import org.geotools.xml.wfs.WFSBasicComplexTypes.GetFeatureType;
import org.geotools.xml.wfs.WFSBasicComplexTypes.QueryType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.CapabilityType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.DCPTypeType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.EmptyType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.FeatureTypeListType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.FeatureTypeType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.GetType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.HTTPType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.LatLongBoundingBoxType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.LockFeatureTypeType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.MetadataURLType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.OperationsType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.PostType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.RequestType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.ResultFormatType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.SchemaDescriptionLanguageType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.ServiceType;
import org.geotools.xml.wfs.WFSCapabilitiesComplexTypes.WFS_CapabilitiesType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.DeleteElementType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.FeaturesLockedType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.FeaturesNotLockedType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.GetFeatureWithLockType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.InsertElementType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.InsertResultType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.LockFeatureType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.LockType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.NativeType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.PropertyType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.StatusType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.TransactionResultType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.TransactionType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.UpdateElementType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.WFS_LockFeatureResponseType;
import org.geotools.xml.wfs.WFSTransactionComplexTypes.WFS_TransactionResponseType;
import org.geotools.xml.xsi.XSISimpleTypes;


/**
 * <p>
 * This class represents a hard coded, java interpreted version  of the WFS
 * WFS-basic schema. Instances of this class should be prefered for use over a
 * parsed instance as this class will create real instances  for elements
 * who's types correspond to types defined in this schema.
 * </p>
 *
 * @author Norman Barker www.comsine.com
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/main/java/org/geotools/xml/wfs/WFSSchema.java $
 */
public class WFSSchema implements Schema {
    static Logger logger = org.geotools.util.logging.Logging.getLogger("net.refractions.xml.wfs");
    private static Schema instance = new WFSSchema();

    /** WFS target namespace */
    public static URI NAMESPACE = makeURI("http://www.opengis.net/wfs");
    static final Element[] elements = new Element[] {
            new WFSElement("GetCapabilities", GetCapabilitiesType.getInstance()),
            new WFSElement("DescribeFeatureType",
                DescribeFeatureTypeType.getInstance()),
            new WFSElement("GetFeature", GetFeatureType.getInstance()), // 2
            new WFSElement("FeatureCollection",
                FeatureCollectionType.getInstance(), 1, 1, false,
                findElement(GMLSchema.getInstance(), "_FeatureCollection")),
            new WFSElement("Query", QueryType.getInstance()),
            new WFSElement("Abstract", XSISimpleTypes.String.getInstance()),
            new WFSElement("AccessConstraints",
                XSISimpleTypes.String.getInstance()),
            new WFSElement("Fees", XSISimpleTypes.String.getInstance()),
            new WFSElement("Keywords", XSISimpleTypes.String.getInstance()),
            new WFSElement("OnlineResource", XSISimpleTypes.String.getInstance()),
            new WFSElement("SRS", XSISimpleTypes.String.getInstance()),
            new WFSElement("Title", XSISimpleTypes.String.getInstance()), // 11

            // TODO check if these should be here - from capabilities ... used in operation type
            new WFSElement("Query", EmptyType.getInstance()),
            new WFSElement("Insert", EmptyType.getInstance()),
            new WFSElement("Update", EmptyType.getInstance()),
            new WFSElement("Delete", EmptyType.getInstance()),
            new WFSElement("Lock", EmptyType.getInstance()), // 16
            new WFSElement("VendorSpecificCapabilities",
                XSISimpleTypes.String.getInstance()),
            new WFSElement("WFS_Capabilities",
                WFS_CapabilitiesType.getInstance()),
            new WFSElement("GML2", EmptyType.getInstance()),
            new WFSElement("GML2-GZIP", EmptyType.getInstance()),
            new WFSElement("XMLSCHEMA", EmptyType.getInstance()), // 21
            new WFSElement("GetFeatureWithLock",
                GetFeatureWithLockType.getInstance()),
            new WFSElement("LockFeature", LockFeatureType.getInstance()),
            new WFSElement("Transaction", TransactionType.getInstance()),
            new WFSElement("WFS_TransactionResponse",
                WFS_TransactionResponseType.getInstance()),
            new WFSElement("WFS_LockFeatureResponse",
                WFS_LockFeatureResponseType.getInstance()),
            new WFSElement("LockId", XSISimpleTypes.String.getInstance()),
            new WFSElement("Insert", InsertElementType.getInstance()),
            new WFSElement("Update", UpdateElementType.getInstance()),
            new WFSElement("Delete", DeleteElementType.getInstance()),
            new WFSElement("Native", NativeType.getInstance()),
            new WFSElement("Property", PropertyType.getInstance()),
            new WFSElement("SUCCESS", EmptyType.getInstance()),
            new WFSElement("FAILED", EmptyType.getInstance()),
            new WFSElement("PARTIAL", EmptyType.getInstance())
        };
    static final ComplexType[] complexTypes = new ComplexType[] {
            GetCapabilitiesType.getInstance(),
            DescribeFeatureTypeType.getInstance(), GetFeatureType.getInstance(),
            QueryType.getInstance(), FeatureCollectionType.getInstance(),
            WFS_CapabilitiesType.getInstance(), ServiceType.getInstance(),
            CapabilityType.getInstance(), FeatureTypeListType.getInstance(),
            RequestType.getInstance(), TransactionType.getInstance(),
            LockFeatureTypeType.getInstance(), DCPTypeType.getInstance(),
            FeatureTypeType.getInstance(), GetType.getInstance(),
            HTTPType.getInstance(), LatLongBoundingBoxType.getInstance(),
            MetadataURLType.getInstance(), OperationsType.getInstance(),
            PostType.getInstance(), ResultFormatType.getInstance(),
            SchemaDescriptionLanguageType.getInstance(), EmptyType.getInstance(),
            GetFeatureWithLockType.getInstance(), LockFeatureType.getInstance(),
            LockType.getInstance(), InsertElementType.getInstance(),
            UpdateElementType.getInstance(), DeleteElementType.getInstance(),
            NativeType.getInstance(), PropertyType.getInstance(),
            WFS_LockFeatureResponseType.getInstance(),
            FeaturesLockedType.getInstance(),
            FeaturesNotLockedType.getInstance(),
            WFS_TransactionResponseType.getInstance(),
            TransactionResultType.getInstance(), InsertResultType.getInstance(),
            StatusType.getInstance()
        };
    static final SimpleType[] simpleTypes = new SimpleType[] {
            new SimpleTypeGT(null, "AllSomeType", NAMESPACE,
                SimpleType.RESTRICTION,
                new SimpleType[] { XSISimpleTypes.String.getInstance() },
                new Facet[] {
                    new FacetGT(Facet.ENUMERATION, "ALL"),
                    new FacetGT(Facet.ENUMERATION, "SOME")
                }, SimpleType.NONE),
        };

    // convinience method to deal with the URISyntaxException
    private static URI makeURI(String s) {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            // do nothing
            return null;
        }
    }

    /**
     * @see org.geotools.xml.schema.Schema#getInstance()
     */
    public static Schema getInstance() {
        return instance;
    }

    private static Element findElement(Schema s, String name) {
        if ((name == null) || "".equals(name)) {
            return null;
        }

        Element[] elems = s.getElements();

        if (elems == null) {
            return null;
        }

        for (int i = 0; i < elems.length; i++)
            if (name.equals(elems[i].getName())) {
                return elems[i];
            }

        return null;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getAttributeGroups()
     */
    public AttributeGroup[] getAttributeGroups() {
        return new AttributeGroup[0];
    }

    /**
     * @see org.geotools.xml.schema.Schema#getAttributes()
     */
    public Attribute[] getAttributes() {
        return new Attribute[0];
    }

    /**
     * @see org.geotools.xml.schema.Schema#getBlockDefault()
     */
    public int getBlockDefault() {
        return NONE;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getComplexTypes()
     */
    public ComplexType[] getComplexTypes() {
        return complexTypes;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getElements()
     */
    public Element[] getElements() {
        return elements;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getFinalDefault()
     */
    public int getFinalDefault() {
        return NONE;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getGroups()
     */
    public Group[] getGroups() {
        return new Group[0];
    }

    /**
     * @see org.geotools.xml.schema.Schema#getId()
     */
    public String getId() {
        return null;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getImports()
     */
    public Schema[] getImports() {
        return new Schema[] { GMLSchema.getInstance(), FilterSchema.getInstance() };
    }

    /**
     * @see org.geotools.xml.schema.Schema#getSimpleTypes()
     */
    public SimpleType[] getSimpleTypes() {
        return simpleTypes;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getTargetNamespace()
     */
    public URI getTargetNamespace() {
        return NAMESPACE;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getURI()
     */
    public URI getURI() {
        try {
            return new URI("http://www.opengis.net/wfs");
        } catch (URISyntaxException e) {
            logger.warning(e.toString());

            return null;
        }
    }

    /**
     * @see org.geotools.xml.schema.Schema#getVersion()
     */
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * @see org.geotools.xml.schema.Schema#includesURI(java.net.URI)
     */
    public boolean includesURI(URI uri) {
        // this is a spec ... we never want the def modified.
        return true;
    }

    /**
     * @see org.geotools.xml.schema.Schema#isAttributeFormDefault()
     */
    public boolean isAttributeFormDefault() {
        return false;
    }

    /**
     * @see org.geotools.xml.schema.Schema#isElementFormDefault()
     */
    public boolean isElementFormDefault() {
        return true;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getPrefix()
     */
    public String getPrefix() {
        return "wfs";
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }

    /**
     * <p>
     * This abstract class represents some default and constant values
     * associated with a GML complexType.
     * </p>
     *
     * @see ComplexType
     */
    static abstract class WFSComplexType implements ComplexType {
        /**
         * @see schema.ComplexType#getBlock()
         */
        public int getBlock() {
            return Schema.NONE;
        }

        /**
         * @see schema.ComplexType#getFinal()
         */
        public int getFinal() {
            return Schema.NONE;
        }

        /**
         * @see schema.ComplexType#getId()
         */
        public String getId() {
            return null;
        }

        /**
         * @see schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return false;
        }

        /**
         * @see schema.ComplexType#getNamespace()
         */
        public URI getNamespace() {
            return WFSSchema.NAMESPACE;
        }

        /*
         * included here to deal generically with a GML complexType ...
         * part of the singleton pattern.
         */
        static WFSComplexType getInstance() {
            return null;
        }

        /**
         * @see schema.ComplexType#isDerived()
         */
        public boolean isDerived() {
            return false;
        }

        /**
         * @see schema.ComplexType#getParent()
         */
        public Type getParent() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#cache(org.geotools.xml.schema.Element,
         *      java.util.Map)
         */
        public boolean cache(Element element, Map hints) {
            return true;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAnyAttributeNameSpace()
         */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            return (getChild() == null) ? null : getChild().findChildElement(name);
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#isAbstract()
         */
        public boolean isAbstract() {
            return false;
        }
    }

    /**
     * <p>
     * Adds some common information and functionality to a base element to  be
     * used by the WFSSchema. The remaining data will be configured upon
     * creation.
     * </p>
     *
     * @author David Zwiers
     *
     * @see Element
     */
    static class WFSElement implements Element {
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
        private WFSElement() {
            // no op const
        }

        /**
         * Configures the Element for this particular WFS instance.  The
         * following params match schema definition attributes found in an
         * element declaration. Those missing have been hard coded for the gml
         * Schema.
         *
         * @param name
         * @param type
         */
        public WFSElement(String name, Type type) {
            this.max = 1;
            this.min = 1;
            this.name = name;
            this.type = type;
            this.substitutionGroup = null;
        }

        /**
         * Configures the Element for this particular WFS instance.  The
         * following params match schema definition attributes found in an
         * element declaration. Those missing have been hard coded for the gml
         * Schema.
         *
         * @param name
         * @param type
         * @param min
         * @param max
         * @param abstracT
         * @param substitutionGroup
         */
        public WFSElement(String name, Type type, int min, int max,
            boolean abstracT, Element substitutionGroup) {
            this.abstracT = abstracT;
            this.max = max;
            this.min = min;
            this.name = name;
            this.type = type;
            this.substitutionGroup = substitutionGroup;
        }

        /**
         * Creates a clone using the new min/max occurences.
         *
         * @param element
         * @param min
         * @param max
         */
        public WFSElement(Element element, int min, int max) {
            this.abstracT = element.isAbstract();
            this.max = max;
            this.min = min;
            this.name = element.getName();
            this.type = element.getType();
            this.substitutionGroup = element.getSubstitutionGroup();
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name1) {
            if (this.name != null) {
                if (this.name.equals(name1)) {
                    return this;
                }
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#getGrouping()
         */
        public int getGrouping() {
            return ELEMENT;
        }

        /**
         * @see schema.Element#isAbstract()
         */
        public boolean isAbstract() {
            return abstracT;
        }

        /**
         * @see schema.Element#getBlock()
         */
        public int getBlock() {
            return Schema.NONE;
        }

        /**
         * @see schema.Element#getDefault()
         */
        public String getDefault() {
            return null;
        }

        /**
         * @see schema.Element#getFinal()
         */
        public int getFinal() {
            return Schema.NONE;
        }

        /**
         * @see schema.Element#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Element#isForm()
         */
        public boolean isForm() {
            return false;
        }

        /**
         * @see schema.Element#getId()
         */
        public String getId() {
            return null;
        }

        /**
         * @see schema.Element#getMaxOccurs()
         */
        public int getMaxOccurs() {
            return max;
        }

        /**
         * @see schema.Element#getMinOccurs()
         */
        public int getMinOccurs() {
            return min;
        }

        /**
         * @see schema.Element#getLocalName()
         */
        public String getName() {
            return name;
        }

        /**
         * @see schema.Element#isNillable()
         */
        public boolean isNillable() {
            return false;
        }

        /**
         * @see schema.Element#getSubstitutionGroup()
         */
        public Element getSubstitutionGroup() {
            return substitutionGroup;
        }

        /**
         * @see schema.Element#getBinding()
         */
        public Type getType() {
            return type;
        }

        /**
         * @see schema.Element#getNamespace()
         */
        public URI getNamespace() {
            return WFSSchema.NAMESPACE;
        }

		public Element findChildElement(String localName, URI namespaceURI) {
			if (this.name != null) {
                if (this.name.equals(localName) && this.getNamespace().equals(namespaceURI)) {
                    return this;
                }
            }

			return null;
		}
    }

    /**
     * <p>
     * An instance of this class represents a WFS attribute. This
     * implementation contains some constant data pertinent to the WFS Schema,
     * and some configurable data depending on the WFS attribute being
     * represented.
     * </p>
     *
     * @author Norman Barker
     * @author David Zwiers
     *
     * @see Attribute
     */
    static class WFSAttribute extends AttributeGT {
        /*
         * Should never be called
         */
        private WFSAttribute() {
            super(null, null, WFSSchema.NAMESPACE, null, OPTIONAL, null, null,
                false);
        }

        /**
         * Creates a GML attribute based on the name and type provided.
         *
         * @param name
         * @param simpleType
         */
        public WFSAttribute(String name, SimpleType simpleType) {
            super(null, name, WFSSchema.NAMESPACE, simpleType, OPTIONAL, null,
                null, false);
        }

        /**
         * Creates a GML attribute based on the name, use and type provided.
         *
         * @param name
         * @param simpleType
         * @param use
         */
        public WFSAttribute(String name, SimpleType simpleType, int use) {
            super(null, name, WFSSchema.NAMESPACE, simpleType, use, null, null,
                false);
        }

        /**
         * Creates a GML attribute based on the name, use, default  and type
         * provided.
         *
         * @param name
         * @param simpleType
         * @param use
         * @param def
         */
        public WFSAttribute(String name, SimpleType simpleType, int use,
            String def) {
            super(null, name, WFSSchema.NAMESPACE, simpleType, use, def, null,
                false);
        }
    }
}
