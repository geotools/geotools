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
package org.geotools.xml.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.filter.FilterComplexTypes.BinaryOperatorType;
import org.geotools.xml.filter.FilterComplexTypes.Comparison_OperatorsType;
import org.geotools.xml.filter.FilterComplexTypes.ExpressionType;
import org.geotools.xml.filter.FilterComplexTypes.Filter_CapabilitiesType;
import org.geotools.xml.filter.FilterComplexTypes.FunctionType;
import org.geotools.xml.filter.FilterComplexTypes.Function_NameType;
import org.geotools.xml.filter.FilterComplexTypes.Function_NamesType;
import org.geotools.xml.filter.FilterComplexTypes.FunctionsType;
import org.geotools.xml.filter.FilterComplexTypes.LiteralType;
import org.geotools.xml.filter.FilterComplexTypes.PropertyNameType;
import org.geotools.xml.filter.FilterComplexTypes.Scalar_CapabilitiesType;
import org.geotools.xml.filter.FilterComplexTypes.ServiceExceptionReportType;
import org.geotools.xml.filter.FilterComplexTypes.ServiceExceptionType;
import org.geotools.xml.filter.FilterComplexTypes.Spatial_CapabilitiesType;
import org.geotools.xml.filter.FilterComplexTypes.Spatial_OperatorsType;
import org.geotools.xml.filter.FilterOpsComplexTypes.BBOXType;
import org.geotools.xml.filter.FilterOpsComplexTypes.BinaryComparisonOpType;
import org.geotools.xml.filter.FilterOpsComplexTypes.BinaryLogicOpType;
import org.geotools.xml.filter.FilterOpsComplexTypes.BinarySpatialOpType;
import org.geotools.xml.filter.FilterOpsComplexTypes.ComparisonOpsType;
import org.geotools.xml.filter.FilterOpsComplexTypes.DistanceBufferType;
import org.geotools.xml.filter.FilterOpsComplexTypes.DistanceType;
import org.geotools.xml.filter.FilterOpsComplexTypes.FeatureIdType;
import org.geotools.xml.filter.FilterOpsComplexTypes.FilterType;
import org.geotools.xml.filter.FilterOpsComplexTypes.LogicOpsType;
import org.geotools.xml.filter.FilterOpsComplexTypes.LowerBoundaryType;
import org.geotools.xml.filter.FilterOpsComplexTypes.PropertyIsBetweenType;
import org.geotools.xml.filter.FilterOpsComplexTypes.PropertyIsLikeType;
import org.geotools.xml.filter.FilterOpsComplexTypes.PropertyIsNullType;
import org.geotools.xml.filter.FilterOpsComplexTypes.SpatialOpsType;
import org.geotools.xml.filter.FilterOpsComplexTypes.UnaryLogicOpType;
import org.geotools.xml.filter.FilterOpsComplexTypes.UpperBoundaryType;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.AttributeGT;
import org.opengis.filter.FilterFactory2;


/**
 * Schema for parsing filter content.
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public class FilterSchema implements Schema {
    // hint key for FilterCapabilities
    public static final String FILTER_CAP_KEY = "FilterSchema.FilterCapabilities";
    public static final URI NAMESPACE = makeURI("http://www.opengis.net/ogc");
    private static final FilterSchema instance = new FilterSchema();
    private static Element[] elements = loadElements();
    
    /**
     * Grab provided FilterFactory from hints, or
     * use default provided by FilterFactoryFinder.createFilterFactory();
     * 
     * @param hints
     * @return FilterFactory
     */
    static FilterFactory2 filterFactory(Map map){
        Hints hints = null;
        if( map instanceof Hints){
            hints = (Hints) map;
        }        
    	return CommonFactoryFinder.getFilterFactory2( hints );
    }
    
    private static final ComplexType[] complexTypes = new ComplexType[] {
            
            // filterCapabilities
            Comparison_OperatorsType.getInstance(),
            Function_NameType.getInstance(), Function_NamesType.getInstance(),
            FunctionsType.getInstance(), Scalar_CapabilitiesType.getInstance(),
            Spatial_CapabilitiesType.getInstance(),
            Spatial_OperatorsType.getInstance(),
            

            // filter
            ComparisonOpsType.getInstance(), SpatialOpsType.getInstance(),
            LogicOpsType.getInstance(), FilterType.getInstance(),
            FeatureIdType.getInstance(), BinaryComparisonOpType.getInstance(),
            PropertyIsLikeType.getInstance(), PropertyIsNullType.getInstance(),
            PropertyIsBetweenType.getInstance(), LowerBoundaryType.getInstance(),
            UpperBoundaryType.getInstance(), BinarySpatialOpType.getInstance(),
            BBOXType.getInstance(), DistanceBufferType.getInstance(),
            DistanceType.getInstance(), BinaryLogicOpType.getInstance(),
            UnaryLogicOpType.getInstance(),
            

            // expr 
            ExpressionType.getInstance(), BinaryOperatorType.getInstance(),
            FunctionType.getInstance(), LiteralType.getInstance(),
            PropertyNameType.getInstance(),
            

            // exception
            ServiceExceptionType.getInstance()
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

    public static FilterSchema getInstance() {
        return instance;
    }

    private static Element[] loadElements() {
        Element comparisonOps = new FilterElement("comparisonOps",
                ComparisonOpsType.getInstance()) {
                public boolean isAbstract() {
                    return true;
                }
            };

        Element spatialOps = new FilterElement("spatialOps",
                SpatialOpsType.getInstance()) {
                public boolean isAbstract() {
                    return true;
                }
            };

        Element logicOps = new FilterElement("logicOps",
                LogicOpsType.getInstance()) {
                public boolean isAbstract() {
                    return true;
                }
            };

        Element expression = new FilterElement("expression",
                ExpressionType.getInstance()) {
                public boolean isAbstract() {
                    return true;
                }
            };

        elements = new Element[] {
                
                // filterCapabilities -- many labels have been excluded here
                new FilterElement("Filter_Capabilities",
                    Filter_CapabilitiesType.getInstance()), // 0

                // filter
                new FilterElement("FeatureId", FeatureIdType.getInstance(),
                    comparisonOps),
                new FilterElement("Filter", FilterType.getInstance(),
                    comparisonOps), //2

                // COMPARISON OPERATORS
                comparisonOps,
                new FilterElement("PropertyIsEqualTo",
                    BinaryComparisonOpType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsNotEqualTo",
                    BinaryComparisonOpType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsLessThan",
                    BinaryComparisonOpType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsGreaterThan",
                    BinaryComparisonOpType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsLessThanOrEqualTo",
                    BinaryComparisonOpType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsGreaterThanOrEqualTo",
                    BinaryComparisonOpType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsLike",
                    PropertyIsLikeType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsNull",
                    PropertyIsNullType.getInstance(), comparisonOps),
                new FilterElement("PropertyIsBetween",
                    PropertyIsBetweenType.getInstance(), comparisonOps), //12

                // SPATIAL OPERATORS
                spatialOps,
                new FilterElement("Equals", BinarySpatialOpType.getInstance(),
                    spatialOps),
                new FilterElement("Disjoint",
                    BinarySpatialOpType.getInstance(), spatialOps),
                new FilterElement("Touches", BinarySpatialOpType.getInstance(),
                    spatialOps),
                new FilterElement("Within", BinarySpatialOpType.getInstance(),
                    spatialOps),
                new FilterElement("Overlaps",
                    BinarySpatialOpType.getInstance(), spatialOps),
                new FilterElement("Crosses", BinarySpatialOpType.getInstance(),
                    spatialOps),
                new FilterElement("Intersects",
                    BinarySpatialOpType.getInstance(), spatialOps),
                new FilterElement("Contains",
                    BinarySpatialOpType.getInstance(), spatialOps),
                new FilterElement("DWithin", DistanceBufferType.getInstance(),
                    spatialOps),
                new FilterElement("Beyond", DistanceBufferType.getInstance(),
                    spatialOps),
                new FilterElement("BBOX", BBOXType.getInstance(), spatialOps), // 24

                // LOGICAL OPERATORS
                logicOps,
                new FilterElement("And", BinaryLogicOpType.getInstance(),
                    logicOps),
                new FilterElement("Or", BinaryLogicOpType.getInstance(),
                    logicOps),
                new FilterElement("Not", UnaryLogicOpType.getInstance(),
                    logicOps), // 28

                // expr
                expression,
                new FilterElement("Add", BinaryOperatorType.getInstance(),
                    expression),
                new FilterElement("Sub", BinaryOperatorType.getInstance(),
                    expression),
                new FilterElement("Mul", BinaryOperatorType.getInstance(),
                    expression),
                new FilterElement("Div", BinaryOperatorType.getInstance(),
                    expression),
                new FilterElement("PropertyName",
                    PropertyNameType.getInstance(), expression),
                new FilterElement("Function", FunctionType.getInstance(),
                    expression),
                new FilterElement("Literal", LiteralType.getInstance(),
                    expression), // 36

                // exception
                new FilterElement("ServiceExceptionReport",
                    ServiceExceptionReportType.getInstance())
            };

        return elements;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getAttributeGroups()
     */
    public AttributeGroup[] getAttributeGroups() {
        return null;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getAttributes()
     */
    public Attribute[] getAttributes() {
        return null;
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
        return null;
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
        return new Schema[] { GMLSchema.getInstance(), };
    }

    /**
     * @see org.geotools.xml.schema.Schema#getURI()
     */
    public URI getURI() {
        return NAMESPACE;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getPrefix()
     */
    public String getPrefix() {
        return "ogc";
    }

    /**
     * @see org.geotools.xml.schema.Schema#getSimpleTypes()
     */
    public SimpleType[] getSimpleTypes() {
        return null;
    }

    /**
     * @see org.geotools.xml.schema.Schema#getTargetNamespace()
     */
    public URI getTargetNamespace() {
        return NAMESPACE;
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
        //        if (uri.toString().toLowerCase().endsWith("filter.xsd")
        //                || uri.toString().toLowerCase().endsWith("filterCapabilities.xsd")
        //                || uri.toString().toLowerCase().endsWith("OGC-exception.xsd")
        //                || uri.toString().toLowerCase().endsWith("expr.xsd")) {
        //            return true;
        //        }
        //
        //        return false;
        // this is a spec ... we never want the def modified.
        // TODO see if this affects printing
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
        
    static class FilterAttribute extends AttributeGT {
        /**
         * DOCUMENT ME!
         *
         * @param name
         * @param type
         */
        public FilterAttribute(String name, SimpleType type) {
            super(null, name, NAMESPACE, type, 0, null, null, false);
        }

        /**
         * DOCUMENT ME!
         *
         * @param name
         * @param type
         * @param use
         */
        public FilterAttribute(String name, SimpleType type, int use) {
            super(null, name, NAMESPACE, type, use, null, null, false);
        }

        /**
         * DOCUMENT ME!
         *
         * @param name
         * @param type
         * @param use
         * @param defaulT
         * @param fixed
         * @param form
         */
        public FilterAttribute(String name, SimpleType type, int use,
            String defaulT, String fixed, boolean form) {
            super(null, name, NAMESPACE, type, use, defaulT, fixed, form);
        }        
    }

    public static class FilterElement implements Element {
        private String name;
        private Type type;
        private Element substitutionGroup;

        public FilterElement(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        public FilterElement(String name, Type type, Element substitutionGroup) {
            this.name = name;
            this.type = type;
            this.substitutionGroup = substitutionGroup;
        }

        /**
         * @see org.geotools.xml.schema.Element#isAbstract()
         */
        public boolean isAbstract() {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Element#getBlock()
         */
        public int getBlock() {
            return NONE;
        }

        /**
         * @see org.geotools.xml.schema.Element#getDefault()
         */
        public String getDefault() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Element#getFinal()
         */
        public int getFinal() {
            return NONE;
        }

        /**
         * @see org.geotools.xml.schema.Element#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Element#isForm()
         */
        public boolean isForm() {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Element#getId()
         */
        public String getId() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Element#getMaxOccurs()
         */
        public int getMaxOccurs() {
            return 1;
        }

        /**
         * @see org.geotools.xml.schema.Element#getMinOccurs()
         */
        public int getMinOccurs() {
            return 1;
        }

        /**
         * @see org.geotools.xml.schema.Element#getName()
         */
        public String getName() {
            return name;
        }

        /**
         * @see org.geotools.xml.schema.Element#getNamespace()
         */
        public URI getNamespace() {
            return NAMESPACE;
        }

        /**
         * @see org.geotools.xml.schema.Element#isNillable()
         */
        public boolean isNillable() {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Element#getSubstitutionGroup()
         */
        public Element getSubstitutionGroup() {
            return substitutionGroup;
        }

        /**
         * @see org.geotools.xml.schema.Element#getType()
         */
        public Type getType() {
            return type;
        }

        /**
         * @see org.geotools.xml.schema.ElementGrouping#getGrouping()
         */
        public int getGrouping() {
            return ELEMENT;
        }

        /**
         * @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name1) {
            return (((getName() != null) && getName().equals(name1)) ? this : null);
        }

		public Element findChildElement(String localName, URI namespaceURI) {
            return (((getName() != null) 
            		&& getName().equals(localName)
            		&& getNamespace().equals(namespaceURI)) ? this : null);
		}        
    }

    static abstract class FilterComplexType implements ComplexType {
        /**
         * @see org.geotools.xml.schema.ComplexType#getParent()
         */
        public Type getParent() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#isAbstract()
         */
        public boolean isAbstract() {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAnyAttributeNameSpace()
         */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getBlock()
         */
        public int getBlock() {
            return Schema.NONE;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getFinal()
         */
        public int getFinal() {
            return Schema.NONE;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getId()
         */
        public String getId() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#isDerived()
         */
        public boolean isDerived() {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#cache(org.geotools.xml.schema.Element,
         *      java.util.Map)
         */
        public boolean cache(Element element, Map hints) {
            return true;
        }

        /**
         * @see org.geotools.xml.schema.Type#getNamespace()
         */
        public URI getNamespace() {
            return FilterSchema.NAMESPACE;
        }

        /**
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            return (getChild() == null) ? null : getChild().findChildElement(name);
        }
        
        /**
         * Subclass must override this method to allow encoding.
         * @return <code>false</code>, subclass override to allow encoding
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }
        
        /**
         * Subclass should implement this, this implementation provides a good OperationsNotSupportedException.
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
                Map hints) throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException(element.toString()+" encode value "+value );
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return getName();
        }
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
