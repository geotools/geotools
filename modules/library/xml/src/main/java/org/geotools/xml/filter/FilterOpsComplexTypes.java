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
package org.geotools.xml.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.BetweenFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.GeometryDistanceFilter;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LikeFilter;
import org.geotools.filter.LiteralExpression;
import org.geotools.filter.LogicFilter;
import org.geotools.filter.NullFilter;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xml.filter.FilterComplexTypes.ExpressionType;
import org.geotools.xml.filter.FilterComplexTypes.LiteralType;
import org.geotools.xml.filter.FilterComplexTypes.PropertyNameType;
import org.geotools.xml.filter.FilterSchema.FilterAttribute;
import org.geotools.xml.filter.FilterSchema.FilterComplexType;
import org.geotools.xml.filter.FilterSchema.FilterElement;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.ChoiceGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.Disjoint;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.AttributesImpl;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * Encode/Decode Filter types.
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public class FilterOpsComplexTypes {    
    
    private static final int SPATIAL_TYPE = 0;
    private static final int COMPARE_TYPE = 1;
    private static final int LOGIC_TYPE = 2;
    private static final int FID_TYPE = 3;
    
    protected static void encodeFilter(org.opengis.filter.Filter filter, PrintHandler output, Map hints) throws OperationNotSupportedException, IOException{

        if (filter instanceof LogicFilter) {
            FilterType.elems[LOGIC_TYPE].getType().encode(FilterType.elems[LOGIC_TYPE], filter, output,
                    hints);
        } else if (filter instanceof CompareFilter) {
            FilterType.elems[COMPARE_TYPE].getType().encode(FilterType.elems[COMPARE_TYPE], filter, output,
                    hints);
        } else if (filter instanceof FidFilter) {
            // deal with multi instance inside the type-writer
            FilterType.elems[FID_TYPE].getType().encode(FilterType.elems[FID_TYPE], filter, output, hints);
        } else if (filter instanceof GeometryFilter) {
            FilterType.elems[SPATIAL_TYPE].getType().encode(FilterType.elems[SPATIAL_TYPE], filter, output,
                    hints);
        } else if (filter instanceof LikeFilter) {
            FilterType.elems[COMPARE_TYPE].getType().encode(FilterType.elems[COMPARE_TYPE], filter, output,
                    hints);
        } else if (filter instanceof NullFilter) {
            FilterType.elems[COMPARE_TYPE].getType().encode(FilterType.elems[COMPARE_TYPE], filter, output,
                    hints);
        } else {
            throw new OperationNotSupportedException(
                    "The Filter type is not known: please try again. " + filter == null ? "null"
                            : filter.getClass().getName());
        }
    }
    
    protected static void encodeExpr(Expression expr, PrintHandler output,
        Map hints) throws OperationNotSupportedException, IOException {
        int i = 0;

        switch (expr.getType()) {
        /* Types implemented by ExpressionLiteral */
        case org.geotools.filter.ExpressionType.LITERAL_DOUBLE:
        case org.geotools.filter.ExpressionType.LITERAL_INTEGER:
        case org.geotools.filter.ExpressionType.LITERAL_STRING:
        case org.geotools.filter.ExpressionType.LITERAL_GEOMETRY:
        case org.geotools.filter.ExpressionType.LITERAL_LONG:
            i = 36;

            break;

        /* Types implemented by ExpressionMath. */
        case org.geotools.filter.ExpressionType.MATH_ADD:
            i = 29;

            break;

        case org.geotools.filter.ExpressionType.MATH_SUBTRACT:
            i = 30;

            break;

        case org.geotools.filter.ExpressionType.MATH_MULTIPLY:
            i = 31;

            break;

        case org.geotools.filter.ExpressionType.MATH_DIVIDE:
            i = 32;

            break;

        /* Types implemented by ExpressionAttribute. */

        /**
         * Defines an attribute expression with a declared double type.
         */
        case org.geotools.filter.ExpressionType.ATTRIBUTE_DOUBLE:
        case org.geotools.filter.ExpressionType.ATTRIBUTE_INTEGER:
        case org.geotools.filter.ExpressionType.ATTRIBUTE_STRING:
        case org.geotools.filter.ExpressionType.ATTRIBUTE_GEOMETRY:
        case org.geotools.filter.ExpressionType.ATTRIBUTE_UNDECLARED:
        case org.geotools.filter.ExpressionType.ATTRIBUTE:
        	i = 34;

            break;

        case org.geotools.filter.ExpressionType.FUNCTION:
            i = 35;

            break;
        }

        if (i != 0) {
            FilterSchema.getInstance().getElements()[i].getType().
				encode(FilterSchema.getInstance().getElements()[i],
                expr, output, hints);
        }
    }

    public static class ComparisonOpsType extends FilterComplexType implements org.geotools.filter.FilterType {
        private static final ComplexType instance = new ComparisonOpsType();

        public static short findFilterType( String s ){
        	if("PropertyIsEqualTo".equalsIgnoreCase( s ) ) return COMPARE_EQUALS;
        	if("PropertyIsGreaterThan".equalsIgnoreCase( s ) ) return COMPARE_GREATER_THAN;
        	if("PropertyIsGreaterThanOrEqualTo".equalsIgnoreCase( s ) ) return COMPARE_GREATER_THAN_EQUAL;
        	if("PropertyIsLessThan".equalsIgnoreCase( s ) ) return COMPARE_LESS_THAN;
        	if("PropertyIsLessThanOrEqualTo".equalsIgnoreCase( s ) ) return COMPARE_LESS_THAN_EQUAL;
        	if("PropertyIsNotEqualTo".equalsIgnoreCase( s ) ) return COMPARE_NOT_EQUALS;
        	if("PropertyIsLike".equalsIgnoreCase( s ) ) return LIKE;
        	if("PropertyIsNull".equalsIgnoreCase( s ) ) return NULL;
        	if("PropertyIsBetween".equalsIgnoreCase( s ) ) return BETWEEN;
        	return 0;
            
        }
        public static String writeFilterType(short filterType) {
        	switch (filterType) {
	        case COMPARE_EQUALS: return "PropertyIsEqualTo";
	        case COMPARE_GREATER_THAN: return "PropertyIsGreaterThan";
	        case COMPARE_GREATER_THAN_EQUAL: return "PropertyIsGreaterThanOrEqualTo";
	        case COMPARE_LESS_THAN: return "PropertyIsLessThan";
	        case COMPARE_LESS_THAN_EQUAL: return "PropertyIsLessThanOrEqualTo";
	        case COMPARE_NOT_EQUALS: return "PropertyIsNotEqualTo";
	        case LIKE: return "PropertyIsLike";
	        case NULL: return "PropertyIsNull";
	        case BETWEEN: return "PropertyIsBetween";
	        default:
	        	return "";
	        }
        }
        	
        public static ComplexType getInstance() {
            return instance;
        }

        //    	<xsd:complexType name="ComparisonOpsType" abstract="true"/>
        public boolean isAbstract() {
            return true;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }
        
        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            FilterFactory2 factory = FilterSchema.filterFactory(hints);

            try {
                Expression expr1 = (Expression) value[0].getValue();
                Expression expr2 = (Expression) value[1].getValue();

                short type = findFilterType(element.getName());
                switch (type) {
                case FilterType.COMPARE_EQUALS:
                    return factory.equals(expr1, expr2);

                case FilterType.COMPARE_GREATER_THAN:
                    return factory.greater(expr1, expr2);

                case FilterType.COMPARE_GREATER_THAN_EQUAL:
                    return factory.greaterOrEqual(expr1, expr2);

                case FilterType.COMPARE_LESS_THAN:
                    return factory.less(expr1, expr2);

                case FilterType.COMPARE_LESS_THAN_EQUAL:
                    return factory.lessOrEqual(expr1, expr2);

                case FilterType.COMPARE_NOT_EQUALS:
                    return factory.notEqual(expr1, expr2);
                default:
                    throw new SAXException("Illegal filter for " + element);
                }
            } catch (ClassCastException filterRequired) {
                throw new SAXException("Illegal filter for " + element, filterRequired);
            } catch (IllegalFilterException e) {
                throw new SAXException("Illegal filter for " + element);
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ComparisonOpsType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return CompareFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.SIMPLE_COMPARISONS) != FilterCapabilities.SIMPLE_COMPARISONS) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && (value instanceof CompareFilter
            || value instanceof BetweenFilter || value instanceof NullFilter
            || value instanceof LikeFilter);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            Filter lf = (Filter) value;

            switch (lf.getFilterType()) {
            case COMPARE_EQUALS:
                BinaryComparisonOpType.getInstance().encode(new FilterElement(
                		"PropertyIsEqualTo",
                        BinaryComparisonOpType.getInstance(), element), value,
                    output, hints);

                return;

            case COMPARE_GREATER_THAN:
                BinaryComparisonOpType.getInstance().encode(new FilterElement(
                		"PropertyIsGreaterThan",
                        BinaryComparisonOpType.getInstance(), element), value,
                    output, hints);

                return;

            case COMPARE_GREATER_THAN_EQUAL:
                BinaryComparisonOpType.getInstance().encode(new FilterElement(
                		"PropertyIsGreaterThanOrEqualTo",
                        BinaryComparisonOpType.getInstance(), element), value,
                    output, hints);

                return;

            case COMPARE_LESS_THAN:
                BinaryComparisonOpType.getInstance().encode(new FilterElement(
                        "PropertyIsLessThan",
                        BinaryComparisonOpType.getInstance(), element), value,
                    output, hints);

                return;

            case COMPARE_LESS_THAN_EQUAL:
                BinaryComparisonOpType.getInstance().encode(new FilterElement(
                        "PropertyIsLessThanOrEqualTo",
                        BinaryComparisonOpType.getInstance(), element), value,
                    output, hints);

                return;

            case COMPARE_NOT_EQUALS:
                BinaryComparisonOpType.getInstance().encode(new FilterElement(
                        "PropertyIsNotEqualTo",
                        BinaryComparisonOpType.getInstance(), element), value,
                    output, hints);

                return;

            case LIKE:
                PropertyIsLikeType.getInstance().encode(new FilterElement(
                        "PropertyIsLike", PropertyIsLikeType.getInstance(),
                        element), value, output, hints);

                return;

            case NULL:
                PropertyIsNullType.getInstance().encode(new FilterElement(
                        "PropertyIsNull", PropertyIsNullType.getInstance(),
                        element), value, output, hints);

                return;

            case BETWEEN:
                PropertyIsBetweenType.getInstance().encode(new FilterElement(
                        "PropertyIsBetween",
                        PropertyIsBetweenType.getInstance(), element), value,
                    output, hints);

                return;
            }

            throw new OperationNotSupportedException(
                "Unknown filter type in ComparisonFilter: "
                + lf.getClass().getName());
        }
    }
    public static class SpatialOpsType extends FilterComplexType
        implements org.geotools.filter.FilterType {
        private static final ComplexType instance = new SpatialOpsType();

        public static short findFilterType( String s ){
        	if("BBOX".equalsIgnoreCase( s ) ) return GEOMETRY_BBOX;
        	if("Equals".equalsIgnoreCase( s ) ) return GEOMETRY_EQUALS;
        	if("Disjoint".equalsIgnoreCase( s ) ) return GEOMETRY_DISJOINT;
        	if("Intersects".equalsIgnoreCase( s ) ) return GEOMETRY_INTERSECTS;
        	if("Touches".equalsIgnoreCase( s ) ) return GEOMETRY_TOUCHES;
        	if("Crosses".equalsIgnoreCase( s ) ) return GEOMETRY_CROSSES;
        	if("Within".equalsIgnoreCase( s ) ) return GEOMETRY_WITHIN;
        	if("Contains".equalsIgnoreCase( s ) ) return GEOMETRY_CONTAINS;
        	if("Overlaps".equalsIgnoreCase( s ) ) return GEOMETRY_OVERLAPS;
        	if("Beyond".equalsIgnoreCase( s ) ) return GEOMETRY_BEYOND;
        	if("DWithin".equalsIgnoreCase( s ) ) return GEOMETRY_DWITHIN;
        	return 0;
            
        }
        public static String writeFilterType(short filterType) {
        	switch (filterType) {
	        case GEOMETRY_BBOX: return "BBOX";
	        case GEOMETRY_EQUALS: return "Equals";
	        case GEOMETRY_DISJOINT: return "Disjoint";
	        case GEOMETRY_INTERSECTS: return "Intersects";
	        case GEOMETRY_TOUCHES: return "Touches";
	        case GEOMETRY_CROSSES: return "Crosses";
	        case GEOMETRY_WITHIN: return "Within";
	        case GEOMETRY_CONTAINS: return "Contains";
	        case GEOMETRY_OVERLAPS: return "Overlaps";
	        case GEOMETRY_BEYOND: return "Beyond";
	        case GEOMETRY_DWITHIN: return "DWithin";
	        default:
	        	return "";
	        }
        }
        
        public static ComplexType getInstance() {
            return instance;
        }

        //    	<xsd:complexType name="SpatialOpsType" abstract="true"/>
        public boolean isAbstract() {
            return true;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @throws SAXException 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException{
        	return null; // child (BBox, BinarySpatialOpType, etc ... ) will handle this
//        	FilterFactory factory = FilterSchema.filterFactory( hints );
//        	
//        	try {
//        		short type = (short) findFilterType( element.getName() );
//        		GeometryFilter filter = factory.createGeometryFilter( type );
//				filter.addLeftGeometry( (Expression) value[0] );
//				filter.addRightGeometry( (Expression) value[1] );				
//				return filter;
//			}
//        	catch( ClassCastException filterRequired ){
//        		throw new SAXException("Illegal filter for "+element, filterRequired );
//			}
//        	catch (IllegalFilterException e) {
//				throw new SAXException("Illegal filter for "+element );
//			} 
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "SpatialOpsType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return GeometryFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if (fc.getSpatialOps() == 0) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof GeometryFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            GeometryFilter lf = (GeometryFilter) value;

            switch (lf.getFilterType()) {
            case GEOMETRY_BBOX:
                BBOXType.getInstance().encode(new FilterElement("BBOX",
                        BBOXType.getInstance(), element), value, output, hints);

                return;

            case GEOMETRY_BEYOND:
                DistanceBufferType.getInstance().encode(new FilterElement(
                        "Beyond", DistanceBufferType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_CONTAINS:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Contains", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_CROSSES:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Crosses", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_DISJOINT:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Disjoint", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_DWITHIN:
                DistanceBufferType.getInstance().encode(new FilterElement(
                        "DWithin", DistanceBufferType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_EQUALS:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Equals", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_INTERSECTS:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Intersects", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_OVERLAPS:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Overlaps", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_TOUCHES:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Touches", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;

            case GEOMETRY_WITHIN:
                BinarySpatialOpType.getInstance().encode(new FilterElement(
                        "Within", BinarySpatialOpType.getInstance(), element),
                    value, output, hints);

                return;
            }

            throw new OperationNotSupportedException(
                "Unknown filter type in ComparisonFilter: "
                + lf.getClass().getName());
        }
    }

    public static class LogicOpsType extends FilterComplexType
        implements org.geotools.filter.FilterType {
        private static final ComplexType instance = new LogicOpsType();

        public static ComplexType getInstance() {
            return instance;
        }

        //    	<xsd:complexType name="LogicOpsType" abstract="true"/>

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            return null; // subclass will do the right thing (tm)
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LogicOpsType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LogicFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.LOGICAL) != FilterCapabilities.LOGICAL) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof LogicFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            LogicFilter lf = (LogicFilter) value;

            switch (lf.getFilterType()) {
            case LOGIC_AND:
                BinaryLogicOpType.getInstance().encode(new FilterElement(
                        "And", BinaryLogicOpType.getInstance(), element),
                    value, output, hints);

                return;

            case LOGIC_OR:

                BinaryLogicOpType.getInstance().encode(new FilterElement("Or",
                        BinaryLogicOpType.getInstance(), element), value,
                    output, hints);

                return;

            case LOGIC_NOT:
                UnaryLogicOpType.getInstance().encode(new FilterElement("Not",
                        UnaryLogicOpType.getInstance(), element), value,
                    output, hints);

                return;
            }

            throw new OperationNotSupportedException(
                "Unknown filter type in LogicFilter: "
                + lf.getClass().getName());
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#isAbstract()
         */
        public boolean isAbstract() {
            return true;
        }
    }

    public static class FilterType extends FilterComplexType
        implements org.geotools.filter.FilterType {
        //    	<xsd:complexType name="FilterType">
        //		<xsd:choice>
        //			<xsd:element ref="ogc:spatialOps"/>
        //			<xsd:element ref="ogc:comparisonOps"/>
        //			<xsd:element ref="ogc:logicOps"/>
        //			<xsd:element ref="ogc:FeatureId" maxOccurs="unbounded"/>
        //		</xsd:choice>
        //		</xsd:complexType>
        final static Element[] elems = new Element[] {
                new FilterElement("spatialOps", SpatialOpsType.getInstance()),
                new FilterElement("comparisonOps",
                    ComparisonOpsType.getInstance()),
                new FilterElement("logicOps", LogicOpsType.getInstance()),
                new FilterElement("FeatureId", FeatureIdType.getInstance()) {
                        public int getMaxOccurs() {
                            return Integer.MAX_VALUE;
                        }
                    }
                ,
            };
        private static Choice choice = new ChoiceGT(elems);
        private static final ComplexType instance = new FilterType();

        public static ComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return choice;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
                if( value.length==1 )
                    return value[0].getValue();
                if( value.length==0 )
                    return Filter.EXCLUDE;
                try{
                    FilterFactory2 fac=CommonFactoryFinder.getFilterFactory2(null);
                    //LogicFilter filter=fac.createLogicFilter(FilterType.LOGIC_OR);
                    List<org.opengis.filter.Filter> filters = new ArrayList<org.opengis.filter.Filter>();
                    Set ids = new HashSet();
                    boolean isOnlyFids = true;
                    for (int i = 0; i < value.length; i++) {
                        org.opengis.filter.Filter value2 = (org.opengis.filter.Filter) value[i].getValue();
                        if( value2 == Filter.EXCLUDE) continue;
                        if( value2 instanceof Id){
                            Id idFilter = (Id) value2;
                            ids.addAll( idFilter.getIdentifiers() );
                        }
                        else {
                            isOnlyFids = false;
                        }                        
                        filters.add( value2 );
                    }                    
                    if( isOnlyFids && !ids.isEmpty()){
                        return fac.id( ids );
                    }
                    else if( filters.isEmpty() ){
                        return Filter.EXCLUDE;
                    }
                    else if( filters.size() == 1 ){
                        return filters.iterator().next();                        
                    }
                    else {
                        return fac.or( filters );                        
                    }
                    //return filter;
                }catch(IllegalFilterException e){
                    return value[0].getValue();
                }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FilterType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Filter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() == 0) && (fc.getSpatialOps() == 0)) {
                    return false;
                }
            }

            boolean r = ((element != null) && (element.getType() != null)
                && getName().equals(element.getType().getName()));
            r = (r && (value != null) && value instanceof Filter
                && (((Filter) value).getFilterType() != 0));

            return r;
        }

        /**
         * Note the assumption is that the comparison of this filter with the
         * WFS capabilities document has already been processed
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            // we may only encode one type of filter ...
            Filter filter = (Filter) value;

            if (filter == null) {
                return;
            }

            if (filter == org.geotools.filter.Filter.NONE) {
                return;
            }

            if (filter == org.geotools.filter.Filter.ALL) {
                return;
            }

            if (element != null) {
                output.startElement(element.getNamespace(), element.getName(),
                    null);
//            }else{
//                output.startElement(getNamespace(), FilterSchema.getInstance().getElements()[2].getName(),
//                    null);
            }
            FilterEncodingPreProcessor visitor=getFilterEncodingPreProcessor(hints);
            filter.accept(visitor);
            // valid filter can have either a normal "filter" defining an test 
            if( !visitor.getFilter().equals(Filter.EXCLUDE) ){
            	encodeFilter(visitor.getFilter(),output,hints);
            }
            // or it can have a "selection" of specific feature IDs
            if( !visitor.getFidFilter().getIDs().isEmpty()){
                encodeFilter(visitor.getFidFilter(), output, hints);
            }
            if (element != null) {
                output.endElement(element.getNamespace(), element.getName());
//            }else{
//                output.endElement(getNamespace(), FilterSchema.getInstance().getElements()[2].getName());
                }
        }

		private FilterEncodingPreProcessor getFilterEncodingPreProcessor(Map hints) {
			if( hints!=null && hints.containsKey(XMLHandlerHints.FILTER_COMPLIANCE_STRICTNESS) )
				return new FilterEncodingPreProcessor((Integer) hints.get(XMLHandlerHints.FILTER_COMPLIANCE_STRICTNESS));
			return new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
		}
    }

    public static class FeatureIdType extends FilterComplexType {
        //    	<xsd:complexType name="FeatureIdType">
        //    		<xsd:attribute name="fid" type="xsd:anyURI" use="required"/>
        //    	</xsd:complexType>
        private static final ComplexType instance = new FeatureIdType();
        private static Attribute[] attrs = new Attribute[] {
                new FilterAttribute("fid", XSISimpleTypes.AnyURI.getInstance(),
                    Attribute.REQUIRED),
            };

        public static ComplexType getInstance() {
            return instance;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)
                    || (element.getType() == null)) {
                throw new SAXException("Invalid parameters : null found");
            }

            if (value.length != 0) {
                throw new SAXException("Invalid children: more than 0 ... "+value.length);
            }

            if (!getName().equals(element.getType().getName())) {
                throw new SAXException("Invalid type name for element provided");
            }

            String fid = null;
            fid = attrs1.getValue("", FeatureIdType.attrs[0].getName());

            if ((fid == null) || "".equals(fid)) {
                fid = attrs1.getValue(FeatureIdType.attrs[0].getNamespace()
                                                           .toString(),
                        FeatureIdType.attrs[0].getName());
            }
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            Set<FeatureId> fids = new HashSet<FeatureId>();
            fids.add( ff.featureId(fid) );
            
            return ff.id( fids );
            //FidFilter r = FilterFactoryFinder.createFilterFactory().createFidFilter(fid);
            //return r;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FeatureIdType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Id.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof Id;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException{
            if (!canEncode(element, value, hints)) {
                return;
            }

            FidFilter ff = (FidFilter) value;
            String[] fids = ff.getFids();
            AttributesImpl att = new AttributesImpl();
            att.addAttribute(null, null, null, null, null);
            
            output.startElement(element.getNamespace(), "Filter", null);

            for (int i = 0; i < fids.length; i++) {
                att.setAttribute(0, element.getNamespace().toString(),
                    attrs[0].getName(), null, "anyUri", fids[i]);
                output.element(element.getNamespace(), element.getName(), att);
            }            
            output.endElement(element.getNamespace(), "Filter" );
        }
    }

    public static class BinaryComparisonOpType extends FilterComplexType
        implements org.geotools.filter.FilterType {
        private static final ComplexType instance = new BinaryComparisonOpType();

        //      <xsd:complexType name="BinaryComparisonOpType">
        //		<xsd:complexContent>
        //			<xsd:extension base="ogc:ComparisonOpsType">
        //				<xsd:sequence>
        //					<xsd:element ref="ogc:expression" minOccurs="2" maxOccurs="2"/>
        //				</xsd:sequence>
        //			</xsd:extension>
        //		</xsd:complexContent>
        //		</xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement("expression", ExpressionType.getInstance()) {
                        /**
                         * @see org.geotools.xml.schema.Element#getMaxOccurs()
                         */
                        public int getMaxOccurs() {
                            return 2;
                        }

                        /**
                         * @see org.geotools.xml.schema.Element#getMinOccurs()
                         */
                        public int getMinOccurs() {
                            return 2;
                        }
                    }
                ,
            };
        private static Sequence seq = new SequenceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        public Type getParent() {
            return ComparisonOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @throws OperationNotSupportedException 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            FilterFactory2 factory = FilterSchema.filterFactory(hints);

            try {
                short type = ComparisonOpsType.findFilterType(element.getName());

                Expression expr1 = (Expression) value[0].getValue();
                Expression expr2 = (Expression) value[1].getValue();

                switch (type) {
                case FilterType.COMPARE_EQUALS:
                    return factory.equals(expr1, expr2);

                case FilterType.COMPARE_NOT_EQUALS:
                    return factory.notEqual(expr1, expr2);

                case FilterType.COMPARE_GREATER_THAN:
                    return factory.greater(expr1, expr2);

                case FilterType.COMPARE_GREATER_THAN_EQUAL:
                    return factory.greaterOrEqual(expr1, expr2);

                case FilterType.COMPARE_LESS_THAN:
                    return factory.less(expr1, expr2);

                case FilterType.COMPARE_LESS_THAN_EQUAL:
                    return factory.lessOrEqual(expr1, expr2);

                default:
                    throw new SAXException("Illegal filter for " + element);

                }
            } catch (ClassCastException expressionRequired) {
                throw new SAXException("Illegal filter for " + element, expressionRequired);
            } catch (IllegalFilterException e) {
                throw new SAXException("Illegal filter for " + element);
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "BinaryComparisonOpType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return CompareFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps()
                        & (FilterCapabilities.SIMPLE_COMPARISONS
                        | FilterCapabilities.SIMPLE_ARITHMETIC)) != (FilterCapabilities.SIMPLE_COMPARISONS
                        | FilterCapabilities.SIMPLE_ARITHMETIC)) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof CompareFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            CompareFilter cf = (CompareFilter) value;

            output.startElement(element.getNamespace(), element.getName(), null);

            // TODO is this order dependant?
            encodeExpr(cf.getLeftValue(), output, hints);
            encodeExpr(cf.getRightValue(), output, hints);

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class PropertyIsLikeType extends FilterComplexType {
        private static final ComplexType instance = new PropertyIsLikeType();
        private static Element[] elems = new Element[] {
                new FilterElement("PropertyName", PropertyNameType.getInstance()),
                new FilterElement("Literal", LiteralType.getInstance()),
            };
        private static Sequence seq = new SequenceGT(elems);
        private static Attribute[] attr = new Attribute[] {
                new FilterAttribute("wildCard",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED),
                new FilterAttribute("singleChar",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED),
                new FilterAttribute("escape",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED),
            };

        public static ComplexType getInstance() {
            return instance;
        }

        //    	<xsd:complexType name="PropertyIsLikeType">
        //    		<xsd:complexContent>
        //    			<xsd:extension base="ogc:ComparisonOpsType">
        //    				<xsd:sequence>
        //    					<xsd:element ref="ogc:PropertyName"/>
        //    					<xsd:element ref="ogc:Literal"/>
        //    				</xsd:sequence>
        //    				<xsd:attribute name="wildCard" type="xsd:string" use="required"/>
        //    				<xsd:attribute name="singleChar" type="xsd:string" use="required"/>
        //    				<xsd:attribute name="escape" type="xsd:string" use="required"/>
        //    			</xsd:extension>
        //    		</xsd:complexContent>
        //    	</xsd:complexType>
        public Type getParent() {
            return ComparisonOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        public Attribute[] getAttributes() {
            return attr;
        }

        /**
         * @throws SAXException 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException{
        	FilterFactory2 factory = FilterSchema.filterFactory( hints );
            //    			<xsd:extension base="ogc:ComparisonOpsType">
            //    				<xsd:sequence>
            //    					<xsd:element ref="ogc:PropertyName"/>
            //    					<xsd:element ref="ogc:Literal"/>
            //    				</xsd:sequence>
            //    				<xsd:attribute name="wildCard" type="xsd:string" use="required"/>
            //    				<xsd:attribute name="singleChar" type="xsd:string" use="required"/>
            //    				<xsd:attribute name="escape" type="xsd:string" use="required"/>
            //    			</xsd:extension>        	
        	try {
        		
        		Expression expr = (Expression) value[0].getValue();
        		String wildCard = attrs.getValue( "wildCard" );
        		String singleChar = attrs.getValue( "singleChar" );
        		String escape = attrs.getValue( "escape" );
        		Literal pattern = (Literal) value[1].getValue();
        		
        	
        		return factory.like(expr, (String) pattern.getValue(), wildCard, singleChar, escape);
                        }
        	catch( ClassCastException expressionRequired ){
        		throw new SAXException("Illegal filter for "+element, expressionRequired );
			}
        	catch (IllegalFilterException e) {
				throw new SAXException("Illegal filter for "+element );
			} 
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "PropertyIsLikeType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LikeFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.LIKE) != FilterCapabilities.LIKE) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof LikeFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            LikeFilter lf = (LikeFilter) value;

            AttributesImpl at = new AttributesImpl();
            at.addAttribute(FilterSchema.NAMESPACE.toString(), "wildCard",
                null, "string", lf.getWildcardMulti());
            at.addAttribute(FilterSchema.NAMESPACE.toString(), "singleChar",
                null, "string", lf.getWildcardSingle());
            at.addAttribute(FilterSchema.NAMESPACE.toString(), "escape", null,
                "string", lf.getEscape());

            output.startElement(element.getNamespace(), element.getName(), at);
            elems[0].getType().encode(elems[0], lf.getValue(), output, hints); // PropertyName
            elems[1].getType().encode(elems[1], lf.getPattern(), output, hints); // Literal
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class PropertyIsNullType extends FilterComplexType {
        private static final ComplexType instance = new PropertyIsNullType();
        private static Element[] elems = new Element[] {
                new FilterElement("PropertyName", PropertyNameType.getInstance()),
                new FilterElement("Literal", LiteralType.getInstance()),
            };
        private static Choice seq = new ChoiceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        //    	<xsd:complexType name="PropertyIsNullType">
        //    		<xsd:complexContent>
        //    			<xsd:extension base="ogc:ComparisonOpsType">
        //    				<xsd:choice>
        //    					<xsd:element ref="ogc:PropertyName"/>
        //    					<xsd:element ref="ogc:Literal"/>
        //    				</xsd:choice>
        //    			</xsd:extension>
        //    		</xsd:complexContent>
        //    	</xsd:complexType>
        public Type getParent() {
            return ComparisonOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            FilterFactory2 factory = FilterSchema.filterFactory(hints);
            try {
                Expression expr = (Expression) value[0].getValue();

                return factory.isNull(expr);
            } catch (ClassCastException expressionRequired) {
                throw new SAXException("Illegal filter for " + element, expressionRequired);
            } catch (IllegalFilterException e) {
                throw new SAXException("Illegal filter for " + element);
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "PropertyIsNullType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return NullFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.NULL_CHECK) != FilterCapabilities.NULL_CHECK) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof NullFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            NullFilter lf = (NullFilter) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            elems[0].getType().encode(elems[0], lf.getNullCheckValue(), output,
                hints); // PropertyName

            //            elems[1].getType().encode(elems[1],lf.getNullCheckValue(),output,hints); // Literal
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class PropertyIsBetweenType extends FilterComplexType {
        private static final ComplexType instance = new PropertyIsBetweenType();
        private static Element[] elems = new Element[] {
                new FilterElement("expression", ExpressionType.getInstance()),
                new FilterElement("LowerBoundary",
                    LowerBoundaryType.getInstance()),
                new FilterElement("UpperBoundary",
                    UpperBoundaryType.getInstance()),
            };
        private static Sequence seq = new SequenceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        //    	<xsd:complexType name="PropertyIsBetweenType">
        //    		<xsd:complexContent>
        //    			<xsd:extension base="ogc:ComparisonOpsType">
        //    				<xsd:sequence>
        //    					<xsd:element ref="ogc:expression"/>
        //    					<xsd:element name="LowerBoundary" type="ogc:LowerBoundaryType"/>
        //    					<xsd:element name="UpperBoundary" type="ogc:UpperBoundaryType"/>
        //    				</xsd:sequence>
        //    			</xsd:extension>
        //    		</xsd:complexContent>
        //    	</xsd:complexType>
        public Type getParent() {
            return ComparisonOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            FilterFactory2 factory = FilterSchema.filterFactory(hints);
            try {
                Expression left = (Expression) value[1].getValue();
                Expression middle = (Expression) value[0].getValue();
                Expression right = (Expression) value[2].getValue();

                return factory.between(middle, left, right);

            } catch (ClassCastException expressionRequired) {
                throw new SAXException("Illegal filter for " + element, expressionRequired);
            } catch (IllegalFilterException e) {
                throw new SAXException("Illegal filter for " + element);
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "PropertyIsBetweenType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return BetweenFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.BETWEEN) != FilterCapabilities.BETWEEN) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof BetweenFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            BetweenFilter lf = (BetweenFilter) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            encodeExpr(lf.getMiddleValue(),output,hints);
            elems[1].getType().encode(elems[1], lf.getLeftValue(), output, hints); // LowerBoundary
            elems[2].getType().encode(elems[2], lf.getRightValue(), output,
                hints); // UpperBoundary
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class LowerBoundaryType extends FilterComplexType {
        private static final ComplexType instance = new LowerBoundaryType();

        //      <xsd:complexType name="LowerBoundaryType">
        //		<xsd:choice>
        //			<xsd:element ref="ogc:expression"/>
        //		</xsd:choice>
        //		</xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement("expression", ExpressionType.getInstance()),
            };
        private static Choice choice = new ChoiceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return choice;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
        	return (Expression) value[0].getValue();
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LowerBoundaryType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Expression.class;
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if (fc.getScalarOps() == FilterCapabilities.NO_OP) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof Expression;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            Expression lf = (Expression) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            encodeExpr(lf,output,hints);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class UpperBoundaryType extends FilterComplexType {
        private static final ComplexType instance = new UpperBoundaryType();

        //    	<xsd:complexType name="UpperBoundaryType">
        //    		<xsd:sequence>
        //    			<xsd:element ref="ogc:expression"/>
        //    		</xsd:sequence>
        //    	</xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement("expression", ExpressionType.getInstance()),
            };
        private static Sequence choice = new SequenceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return choice;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
        	return (Expression) value[0].getValue();
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "UpperBoundaryType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Expression.class;
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if (fc.getScalarOps() == FilterCapabilities.NO_OP) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof Expression;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            Expression lf = (Expression) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            encodeExpr(lf,output,hints);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class BinarySpatialOpType extends FilterComplexType {
        private static final ComplexType instance = new BinarySpatialOpType();

        //    	<xsd:complexType name="BinarySpatialOpType">
        //    		<xsd:complexContent>
        //    			<xsd:extension base="ogc:SpatialOpsType">
        //    				<xsd:sequence>
        //    					<xsd:element ref="ogc:PropertyName"/>
        //    					<xsd:choice>
        //    						<xsd:element ref="gml:_Geometry"/>
        //    						<xsd:element ref="gml:Box"/>
        //    					</xsd:choice>
        //    				</xsd:sequence>
        //    			</xsd:extension>
        //    		</xsd:complexContent>
        //    	</xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement("PropertyName", PropertyNameType.getInstance()),
                GMLSchema.getInstance().getElements()[29], // _Geometry
                GMLSchema.getInstance().getElements()[41]
            };
        private static Sequence child = new SequenceGT(new ElementGrouping[] {
                    elems[0],
                    new ChoiceGT(new Element[] { elems[1], elems[2] })
                });

        public static ComplexType getInstance() {
            return instance;
        }

        public Type getParent() {
            return SpatialOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @throws SAXException 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            FilterFactory2 factory = FilterSchema.filterFactory(hints);

            try {
                short type = (short) SpatialOpsType.findFilterType(element.getName());

                Expression geometry1 = (Expression) value[0].getValue();
                Expression geometry2 = (Expression) value[1].getValue();

                // GeometryFilter filter = factory.createGeometryFilter( type );
                switch (type) {
                case FilterType.GEOMETRY_EQUALS:
                    return factory.equal(geometry1, geometry2);

                case FilterType.GEOMETRY_DISJOINT:
                    return factory.disjoint(geometry1, geometry2);

                case FilterType.GEOMETRY_INTERSECTS:
                    return factory.intersects(geometry1, geometry2);

                case FilterType.GEOMETRY_CROSSES:
                    return factory.crosses(geometry1, geometry2);

                case FilterType.GEOMETRY_WITHIN:
                    return factory.within(geometry1, geometry2);

                case FilterType.GEOMETRY_CONTAINS:
                    return factory.contains(geometry1, geometry2);

                case FilterType.GEOMETRY_OVERLAPS:
                    return factory.overlaps(geometry1, geometry2);

                case FilterType.GEOMETRY_TOUCHES:
                    return factory.touches(geometry1, geometry2);

                default:
                    throw new SAXException("Illegal filter for " + element);
                }
            } catch (ClassCastException filterRequired) {
                throw new SAXException("Illegal filter for " + element, filterRequired);
            } catch (IllegalFilterException e) {
                throw new SAXException("Illegal filter for " + element);
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "BinarySpatialOpType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return GeometryFilter.class;
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);
                FilterCapabilities elementkey = FilterCapabilities.findOperation(element
                        .getName());

                if ((elementkey == null)
                        || !fc.supports(elementkey)) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof GeometryFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            GeometryFilter lf = (GeometryFilter) value;

            output.startElement(element.getNamespace(), element.getName(), null);

            if ((lf.getLeftGeometry().getType() == org.geotools.filter.ExpressionType.LITERAL_STRING)
                    || (lf.getLeftGeometry().getType() == org.geotools.filter.ExpressionType.ATTRIBUTE_STRING)
                    || (lf.getLeftGeometry().getType() == org.geotools.filter.ExpressionType.ATTRIBUTE)) {
                elems[0].getType().encode(elems[0], lf.getLeftGeometry(),
                    output, hints); // prop name

                if (lf.getRightGeometry().getType() == org.geotools.filter.ExpressionType.LITERAL_GEOMETRY) {
                    elems[1].getType().encode(elems[1],
                        ((LiteralExpression) lf.getRightGeometry()).getLiteral(),
                        output, hints); // geom
                } else {
                    elems[2].getType().encode(elems[2],
                        ((LiteralExpression) lf.getRightGeometry()).getLiteral(),
                        output, hints); // geom
                }
            } else {
                if ((lf.getRightGeometry().getType() == org.geotools.filter.ExpressionType.LITERAL_STRING)
                        || (lf.getRightGeometry().getType() == org.geotools.filter.ExpressionType.ATTRIBUTE_STRING)
                        || (lf.getRightGeometry().getType() == org.geotools.filter.ExpressionType.ATTRIBUTE)) {
                    elems[0].getType().encode(elems[0], lf.getRightGeometry(),
                        output, hints); // prop name

                    if (lf.getLeftGeometry().getType() == org.geotools.filter.ExpressionType.LITERAL_GEOMETRY) {
                        elems[1].getType().encode(elems[1],
                            ((LiteralExpression) lf.getLeftGeometry())
                            .getLiteral(), output, hints); // geom
                    } else {
                        elems[2].getType().encode(elems[2],
                            ((LiteralExpression) lf.getLeftGeometry())
                            .getLiteral(), output, hints); // geom
                    }
                } else {
                    throw new OperationNotSupportedException(
                        "Either the left or right expr must be a literal for the property name l="
                        + lf.getLeftGeometry().getType() + " r="
                        + lf.getRightGeometry().getType());
                }
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    /**
     * The <BBOX> element is defined as a convenient and more compact way
     * of encoding the very common bounding box constraint based on the
     * gml:Box geometry. It is equivalent to the spatial operation
     * <Not><Disjoint> ??? </Disjoint></Not> meaning that the <BBOX> operator
     * should identify all geometries that spatially interact with the box
     * in some manner.
     * 
     * @author jgarnett
     *
     * TODO To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Style - Code Templates
     */
    public static class BBOXType extends FilterComplexType {
        private static final ComplexType instance = new BBOXType();

        //    	<xsd:complexType name="BBOXType">
        //		<xsd:complexContent>
        //			<xsd:extension base="ogc:SpatialOpsType">
        //				<xsd:sequence>
        //					<xsd:element ref="ogc:PropertyName"/>
        //					<xsd:element ref="gml:Box"/>
        //				</xsd:sequence>
        //			</xsd:extension>
        //		</xsd:complexContent>
        //		</xsd:complexType>
        private static final Element OGC_PROPERTY_NAME = new FilterElement("PropertyName", PropertyNameType.getInstance());
        private static final Element GML_BOX = GMLSchema.getInstance().getElements()[GMLSchema.BOX];
        
        private static Element[] elems = new Element[] {OGC_PROPERTY_NAME,GML_BOX};
        
        private Sequence seq = new SequenceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        public Type getParent() {
            return SpatialOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @throws SAXException  
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {

            if (value == null || value.length != 2) {
                throw new SAXException("ogc:propertyName or gml:box required for bbox filter");
            }
            FilterFactory2 factory = FilterSchema.filterFactory(hints);
            try {
                Expression geometry1 = (Expression) value[0].getValue();
                Expression geometry2 = (Expression) value[1].getValue();
                if( geometry2 instanceof Literal ){
                    Object literal = ((Literal) geometry2).getValue();
                    if( literal instanceof Geometry){
                        Geometry geometry = (Geometry) literal;
                        Envelope env = geometry.getEnvelopeInternal();
                        return factory.bbox(geometry1, env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), null );
                    }
                }
                Disjoint disjoint = factory.disjoint(geometry1, geometry2);
                return factory.not( disjoint );
                
            } catch (ClassCastException wrong) {
                throw new SAXException("ogc:propertyName or gml:box required for bbox filter",
                        wrong);
            } catch (IllegalFilterException illegalFilterException) {
                throw new SAXException("Could not create bbox filter", illegalFilterException);
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "BBOXType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Filter.class; // was GeometryFilter.class but use of Disjoint.not() limits this to Filte
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getSpatialOps() & FilterCapabilities.SPATIAL_BBOX) != FilterCapabilities.SPATIAL_BBOX) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof GeometryFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            GeometryFilter lf = (GeometryFilter) value;

            output.startElement(element.getNamespace(), element.getName(), null);

            if (lf.getLeftGeometry().getType() == org.geotools.filter.ExpressionType.LITERAL_GEOMETRY) {
                elems[0].getType().encode(elems[0], lf.getRightGeometry(),
                    output, hints); // prop name

                Geometry g = ((Geometry) ((LiteralExpression) lf
                    .getLeftGeometry()).getLiteral()).getEnvelope();
                elems[1].getType().encode(elems[1], g, output, hints); // geom
            } else {
                if (lf.getRightGeometry().getType() == org.geotools.filter.ExpressionType.LITERAL_GEOMETRY) {
                    elems[0].getType().encode(elems[0], lf.getLeftGeometry(),
                        output, hints); // prop name

                    Geometry g = ((Geometry) ((LiteralExpression) lf
                        .getRightGeometry()).getLiteral()).getEnvelope();
                    elems[1].getType().encode(elems[1], g, output, hints); // geom
                } else {
                    throw new OperationNotSupportedException(
                        "Either the left or right expr must be a literal for the property name : BBOXType");
                }
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class DistanceBufferType extends FilterComplexType {
        private static final ComplexType instance = new DistanceBufferType();

        //    	<xsd:complexType name="DistanceBufferType">
        //    		<xsd:complexContent>
        //    			<xsd:extension base="ogc:SpatialOpsType">
        //    				<xsd:sequence>
        //    					<xsd:element ref="ogc:PropertyName"/>
        //    					<xsd:element ref="gml:_Geometry"/>
        //    					<xsd:element name="Distance" type="ogc:DistanceType"/>
        //    				</xsd:sequence>
        //    			</xsd:extension>
        //    		</xsd:complexContent>
        //    	</xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement("PropertyName", PropertyNameType.getInstance()),
                GMLSchema.getInstance().getElements()[29], // _Geometry
                new FilterElement("Distance", DistanceType.getInstance())
            };
        private Sequence seq = new SequenceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        public Type getParent() {
            return SpatialOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @throws SAXException 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
        	        	
        	FilterFactory2 factory = FilterSchema.filterFactory( hints );
        	try {
        		Expression geometry1 =  (Expression) value[0].getValue();
        		Expression geometry2 = (Expression) value[1].getValue();
        		Literal literal = (Literal) value[2];
        		double distance = ((Number)literal.getValue()).doubleValue();        		
        		return factory.beyond(geometry1, geometry2, distance, null);	
        	}
        	catch( ClassCastException wrong){
        		throw new SAXException( wrong );
        	} catch (IllegalFilterException illegalFilterException) {
        		throw new SAXException( illegalFilterException );
			} 
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "DistanceBufferType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return GeometryDistanceFilter.class;
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getSpatialOps()
                        & (FilterCapabilities.SPATIAL_BEYOND
                        | FilterCapabilities.SPATIAL_DWITHIN)) != (FilterCapabilities.SPATIAL_BEYOND
                        | FilterCapabilities.SPATIAL_DWITHIN)) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof GeometryDistanceFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            GeometryDistanceFilter lf = (GeometryDistanceFilter) value;

            output.startElement(element.getNamespace(), element.getName(), null);

            if (lf.getLeftGeometry().getType() == org.geotools.filter.ExpressionType.ATTRIBUTE) {
                elems[0].getType().encode(elems[0], lf.getLeftGeometry(),
                    output, hints); // prop name
                elems[1].getType().encode(elems[1], lf.getRightGeometry().getValue(null),
                    output, hints); // geom
                elems[2].getType().encode(elems[2], lf, output, hints); // distancetype
            } else {
                if (lf.getRightGeometry().getType() == org.geotools.filter.ExpressionType.ATTRIBUTE) {
                    elems[0].getType().encode(elems[0], lf.getRightGeometry(),
                        output, hints); // prop name
                    elems[1].getType().encode(elems[1], lf.getLeftGeometry().getValue(null),
                        output, hints); // geom
                    elems[2].getType().encode(elems[2], lf, output, hints); // distancetype
                } else {
                    throw new OperationNotSupportedException(
                        "Either the left or right expr must be a literal for the property name");
                }
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class DistanceType extends FilterComplexType {
        private static final ComplexType instance = new DistanceType();

        //    	<xsd:complexType name="DistanceType" mixed="true">
        //    		<xsd:attribute name="units" type="xsd:string" use="required"/>
        //    	</xsd:complexType>
        private static Attribute[] attrs = new Attribute[] {
                new FilterAttribute("units",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED),
            };

        public static ComplexType getInstance() {
            return instance;
        }

        public boolean isMixed() {
            return true;
        }

        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
        	
        	FilterFactory2 factory = FilterSchema.filterFactory( hints );
        	try {
        	    Expression geometry1 =  (Expression) value[0].getValue();
                    Expression geometry2 = (Expression) value[1].getValue();
                    Literal literal = (Literal) value[2];
                    double distance = ((Number)literal.getValue()).doubleValue(); 
                    return factory.dwithin(geometry1, geometry2, distance, null );
        	}
        	catch( ClassCastException wrong){
        		throw new SAXException( wrong );
        	} catch (IllegalFilterException illegalFilterException) {
        		throw new SAXException( illegalFilterException );
			}
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "DistanceType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return GeometryDistanceFilter.class;
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getSpatialOps()
                        & (FilterCapabilities.SPATIAL_BEYOND
                        | FilterCapabilities.SPATIAL_DWITHIN)) != (FilterCapabilities.SPATIAL_BEYOND
                        | FilterCapabilities.SPATIAL_DWITHIN)) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof GeometryDistanceFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException{
            if (!canEncode(element, value, hints)) {
                return;
            }

            GeometryDistanceFilter distanceFilter = (GeometryDistanceFilter) value;

            AttributesImpl ai = new AttributesImpl();

            String name = attrs[0].getName();
            String uri = getNamespace().toString();
            if (distanceFilter.getLeftGeometry().getType() == org.geotools.filter.ExpressionType.LITERAL_GEOMETRY) {
                Geometry geometry = (Geometry) distanceFilter.getLeftGeometry().getValue(null);
                if( geometry.getUserData() != null ){
                    // code assume user data is an srsName see GEOT-693
                    String srsName = String.valueOf( geometry.getUserData() );
                    ai.addAttribute(uri, name, null, "string", srsName);
                }
            } else {
                Geometry geometry = (Geometry) distanceFilter.getRightGeometry().getValue(null);
                if( geometry.getUserData() != null ){
                    // code assume user data is an srsName see GEOT-693
                    String srsName = String.valueOf( geometry.getUserData() );
                    ai.addAttribute(uri, name, null, "string", srsName);
                }
            }

            output.startElement(element.getNamespace(), element.getName(), null);
            output.characters("" + distanceFilter.getDistance());
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class BinaryLogicOpType extends FilterComplexType {
        private static final ComplexType instance = new BinaryLogicOpType();

        //    	<xsd:complexType name="BinaryLogicOpType">
        //    		<xsd:complexContent>
        //    			<xsd:extension base="ogc:LogicOpsType">
        //    				<xsd:choice minOccurs="2" maxOccurs="unbounded">
        //    					<xsd:element ref="ogc:comparisonOps"/>
        //    					<xsd:element ref="ogc:spatialOps"/>
        //    					<xsd:element ref="ogc:logicOps"/>
        //    				</xsd:choice>
        //    			</xsd:extension>
        //    		</xsd:complexContent>
        //    	</xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement("comparisonOps",
                    ComparisonOpsType.getInstance()),
                new FilterElement("spatialOps", SpatialOpsType.getInstance()),
                new FilterElement("logicOps", LogicOpsType.getInstance())
            };
        private static Choice choice = new ChoiceGT(null, 2,
                Integer.MAX_VALUE, elems);

        public static ComplexType getInstance() {
            return instance;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getParent()
         */
        public Type getParent() {
            return LogicOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return choice;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
        	
        	FilterFactory2 factory = FilterSchema.filterFactory( hints );
        	String name = element.getName();
        	short type;
        	if( "and".equalsIgnoreCase( name )){
        		type = FilterType.LOGIC_AND;
        	}
        	else if( "or".equalsIgnoreCase( name )){
        		type = FilterType.LOGIC_OR;
        	}
        	else {
        		throw new SAXException("Expected AND or OR logic filter" );
        	}
        	try {
        	    ArrayList<org.opengis.filter.Filter> children = new ArrayList<org.opengis.filter.Filter>( value.length );
        	    Set<Identifier> ids = new HashSet<Identifier>( value.length );
        	    boolean fidOnly = true;
        	    
				//LogicFilter filter = factory.createLogicFilter( type );
				for( int i=0; i<value.length; i++){
				    Filter filter = (Filter) value[i];
				    if( filter instanceof Id ){
				        Id id = (Id) filter;
				        ids.addAll( id.getIdentifiers() );
				    }
				    else {
				        fidOnly = false;
				    }
				    children.add( filter );
				}
			    if( type == FilterType.LOGIC_OR ){
				    if( fidOnly ){
				        return factory.id( ids ); 
				    }
				    else {
				        return factory.or( children );
				    }				    
				}
			    else {
			        return factory.and( children );
			    }				
			}
        	catch( ClassCastException filterRequired ){
        		throw new SAXException("Illegal filter for "+element, filterRequired );
			}
        	catch (IllegalFilterException e) {
				throw new SAXException("Illegal filter for "+element );
			}            
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "BinaryLogicOpType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LogicFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.LOGICAL) != FilterCapabilities.LOGICAL) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof LogicFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            LogicFilter lf = (LogicFilter) value;
            Iterator i = lf.getFilterIterator();
            output.startElement(element.getNamespace(), element.getName(), null);

            while (i.hasNext()){
                Filter f = (Filter)i.next();
                encodeFilter(f, output, hints);
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class UnaryLogicOpType extends FilterComplexType {
        private static final ComplexType instance = new UnaryLogicOpType();

        //    	<xsd:complexType name="UnaryLogicOpType">
        //    		<xsd:complexContent>
        //    			<xsd:extension base="ogc:LogicOpsType">
        //    				<xsd:sequence>
        //    					<xsd:choice>
        //    						<xsd:element ref="ogc:comparisonOps"/>
        //    						<xsd:element ref="ogc:spatialOps"/>
        //    						<xsd:element ref="ogc:logicOps"/>
        //    					</xsd:choice>
        //    				</xsd:sequence>
        //    			</xsd:extension>
        //    		</xsd:complexContent>
        //    	</xsd:complexType>
        private static Element[] elems = new Element[] {
                new FilterElement("comparisonOps",
                    ComparisonOpsType.getInstance()),
                new FilterElement("spatialOps", SpatialOpsType.getInstance()),
                new FilterElement("logicOps", LogicOpsType.getInstance())
            };
        private static Choice choice = new ChoiceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getParent()
         */
        public Type getParent() {
            return LogicOpsType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return choice;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @throws SAXException 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            FilterFactory2 factory = FilterSchema.filterFactory(hints);
            String name = element.getName();
            short type;
            if ("and".equalsIgnoreCase(name)) {
                type = FilterType.LOGIC_AND;
            } else if ("or".equalsIgnoreCase(name)) {
                type = FilterType.LOGIC_OR;
            } else if ("not".equalsIgnoreCase(name)) {
                type = FilterType.LOGIC_NOT;
            } else {
                throw new SAXException("Expected AND or OR logic filter");
            }
            if (value == null || value.length != 1) {
                throw new SAXException("Require a single filter for " + element);
            }
            try {

                Filter filter = (Filter) value[0].getValue();

                return factory.not(filter);
            } catch (ClassCastException filterRequired) {
                throw new SAXException("Require a single filter for " + element, filterRequired);
            } catch (IllegalFilterException e) {
                throw new SAXException("Illegal filter for " + element);
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "UnaryLogicOpType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LogicFilter.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((hints != null)
                    && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.LOGICAL) != FilterCapabilities.LOGICAL) {
                    return false;
                }
            }

            return (element.getType() != null)
            && getName().equals(element.getType().getName())
            && value instanceof LogicFilter;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            LogicFilter lf = (LogicFilter) value;
            Iterator i = lf.getFilterIterator();
            output.startElement(element.getNamespace(), element.getName(), null);

            int c = 0;

            while (i.hasNext()) {
                if (c < 1) {
                    Filter f = (Filter)i.next();
                    encodeFilter(f, output, hints);
                    c++;
                } else {
                    throw new OperationNotSupportedException(
                        "Invalid Not Filter -- more than one child filter.");
                }
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }
}
