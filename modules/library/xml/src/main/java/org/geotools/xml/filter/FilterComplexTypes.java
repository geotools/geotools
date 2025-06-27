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
import java.net.URI;
import java.util.Map;
import javax.naming.OperationNotSupportedException;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.BinaryExpression;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.Filters;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.IllegalFilterException;
import org.geotools.ows.ServiceException;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.filter.FilterSchema.FilterAttribute;
import org.geotools.xml.filter.FilterSchema.FilterComplexType;
import org.geotools.xml.filter.FilterSchema.FilterElement;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Any;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeValue;
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.AnyGT;
import org.geotools.xml.schema.impl.ChoiceGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Represent complex types Bbox, Comparison etc ...
 *
 * <p>Subclass must override encode and getValue as usual.
 *
 * @author dzwiers
 */
public class FilterComplexTypes {
    public static final String CACHE_SERVICE_EXCEPTIONS = "FilterComplexTypes.CACHE_SERVICE_EXCEPTIONS";

    public static class Arithmetic_OperatorsType extends FilterComplexType {
        private static final ComplexType instance = new Arithmetic_OperatorsType();
        private static Element[] elements = {
            new FilterElement("Simple_Arithmetic", EmptyType.getInstance()),
            new FilterElement("Functions", FunctionsType.getInstance())
        };
        private static Choice choice = new ChoiceGT(elements) {
            @Override
            public int getMaxOccurs() {
                return Integer.MAX_VALUE;
            }
        };

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return choice;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (element == null || value == null) {
                throw new SAXException("Parameter missing for Comparison_OperatorsType");
            }

            if (value.length < 1) {
                throw new SAXException("Missing child element");
            }

            FilterCapabilities caps = new FilterCapabilities();

            for (ElementValue elementValue : value) {
                String name = elementValue.getElement().getName();
                if (name.equals("Functions")) caps.addAll((FilterCapabilities) elementValue.getValue());
                else caps.addAll(FilterCapabilities.findOperation(name));
            }

            return caps;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "Comparison_OperatorsType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class<?> getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException(element.toString() + " encode value " + value);
        }
    }

    public static class Comparison_OperatorsType extends FilterComplexType {
        private static final ComplexType instance = new Comparison_OperatorsType();
        private static Element[] elements = {
            new FilterElement("Simple_Comparisons", EmptyType.getInstance()),
            new FilterElement("Like", EmptyType.getInstance()),
            new FilterElement("Between", EmptyType.getInstance()),
            new FilterElement("NullCheck", EmptyType.getInstance()),
        };
        private static Choice choice = new ChoiceGT(elements) {
            @Override
            public int getMaxOccurs() {
                return Integer.MAX_VALUE;
            }
        };

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return choice;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (element == null || value == null) {
                throw new SAXException("Parameter missing for Comparison_OperatorsType");
            }

            if (value.length < 1) {
                throw new SAXException("Missing child element");
            }

            FilterCapabilities caps = new FilterCapabilities();

            for (ElementValue elementValue : value) {
                caps.addAll(FilterCapabilities.findOperation(
                        elementValue.getElement().getName()));
            }

            return caps;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "Comparison_OperatorsType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }
    }

    public static class Function_NameType extends FilterComplexType {
        private static final ComplexType instance = new Function_NameType();

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#getParent() */
        @Override
        public Type getParent() {
            return XSISimpleTypes.String.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints) {
            // TODO
            FilterCapabilities caps = new FilterCapabilities();
            String functionName = (String) value[0].getValue();
            caps.addAll(FilterCapabilities.findFunction(functionName.toLowerCase()));
            return caps;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "Function_NameType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }

        @Override
        public boolean isMixed() {
            return true;
        }
    }

    public static class Function_NamesType extends FilterComplexType {
        private static final ComplexType instance = new Function_NamesType();
        private static Element[] elements = {
            new FilterElement("Function_Name", Function_NameType.getInstance()),
        };
        private static Sequence seq = new SequenceGT(elements) {
            @Override
            public int getMaxOccurs() {
                return Integer.MAX_VALUE;
            }
        };

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints) {
            FilterCapabilities caps = new FilterCapabilities();
            for (ElementValue elementValue : value) {
                caps.addAll((FilterCapabilities) elementValue.getValue());
            }
            return caps;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "Function_NamesType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }
    }

    public static class FunctionsType extends FilterComplexType {
        private static final ComplexType instance = new FunctionsType();
        private static Element[] elements = {
            new FilterElement("Function_Names", Function_NamesType.getInstance()),
        };
        private static Sequence seq = new SequenceGT(elements);

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException {
            if (element == null || value == null || value.length != 1) {
                throw new SAXException("Invalid parameters specified for Spatial_CapabilitiesType");
            }

            if (elements[0].getName().equals(value[0].getElement().getName())) {
                return value[0].getValue();
            }

            throw new SAXException(
                    "Invalid child element: " + value[0].getElement().getName());
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "FunctionsType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }
    }

    public static class Filter_CapabilitiesType extends FilterComplexType {
        private static final ComplexType instance = new Filter_CapabilitiesType();
        private static Element[] elements = {
            new FilterElement("Spatial_Capabilities", Spatial_CapabilitiesType.getInstance()),
            new FilterElement("Scalar_Capabilities", Scalar_CapabilitiesType.getInstance())
        };
        private static Sequence seq = new SequenceGT(elements);

        public static ComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (element == null || value == null) {
                throw new SAXException("Parameter missing for Filter_Capabilities Type");
            }

            //            if (value.length != 2) {
            //                throw new SAXException(
            //                    "Either there is an extra child, or too few child elements");
            //            }

            FilterCapabilities fc = new FilterCapabilities();

            if (elements[0].getName().equals(value[0].getElement().getName())) {

                fc.addAll((FilterCapabilities) value[0].getValue());

                if (value.length > 1) {
                    if (elements[1].getName().equals(value[1].getElement().getName())) {
                        fc.addAll((FilterCapabilities) value[1].getValue());
                    } else {
                        throw new SAXException(
                                "Unknown element" + value[1].getElement().getName());
                    }
                }
            } else {
                if (elements[1].getName().equals(value[0].getElement().getName())) {
                    fc.addAll((FilterCapabilities) value[1].getValue());

                    if (value.length > 1) {
                        if (elements[0].getName().equals(value[1].getElement().getName())) {
                            fc.addAll((FilterCapabilities) value[0].getValue());
                        } else {
                            throw new SAXException(
                                    "Unknown element" + value[1].getElement().getName());
                        }
                    }
                } else {
                    // error
                    throw new SAXException(
                            "Unknown element" + value[0].getElement().getName());
                }
            }

            return fc;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            //            return "Filter_CapabilitiesType";
            return "";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }
    }

    public static class Scalar_CapabilitiesType extends FilterComplexType {
        private static final ComplexType instance = new Scalar_CapabilitiesType();
        private static Element[] elements = {
            new FilterElement("Logical_Operators", EmptyType.getInstance()),
            new FilterElement("Comparison_Operators", Comparison_OperatorsType.getInstance()),
            new FilterElement("Arithmetic_Operators", Arithmetic_OperatorsType.getInstance()),
        };
        private static Choice choice = new ChoiceGT(elements) {
            @Override
            public int getMaxOccurs() {
                return Integer.MAX_VALUE;
            }
        };

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return choice;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (element == null || value == null) {
                throw new SAXException("Missing paramters for Scalar_CapabilitiesType");
            }

            if (value.length < 1) {
                throw new SAXException("Missing child value elements");
            }

            FilterCapabilities caps = new FilterCapabilities();

            for (ElementValue elementValue : value) {
                if (elements[0].getName().equals(elementValue.getElement().getName())) {
                    // logical ops
                    caps.addType(FilterCapabilities.LOGICAL);
                } else {
                    if (elements[1].getName().equals(elementValue.getElement().getName())) {
                        // comparison ops
                        caps.addAll((FilterCapabilities) elementValue.getValue());
                    } else {
                        if (elements[2]
                                .getName()
                                .equals(elementValue.getElement().getName())) {
                            // arithmetic ops
                            caps.addAll((FilterCapabilities) elementValue.getValue());
                        } else {
                            // error
                            throw new SAXException("Invalid child element: "
                                    + elementValue.getElement().getName());
                        }
                    }
                }
            }

            return caps;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "Scalar_CapabilitiesType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }
    }

    public static class Spatial_CapabilitiesType extends FilterComplexType {
        private static final ComplexType instance = new Spatial_CapabilitiesType();
        private static Element[] elements = {
            new FilterElement("Spatial_Operators", Spatial_OperatorsType.getInstance()),
        };
        private static Sequence seq = new SequenceGT(elements);

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (element == null || value == null || value.length != 1) {
                throw new SAXException("Invalid parameters specified for Spatial_CapabilitiesType");
            }

            if (elements[0].getName().equals(value[0].getElement().getName())) {
                return value[0].getValue();
            }

            throw new SAXException(
                    "Invalid child element: " + value[0].getElement().getName());
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "Spatial_CapabilitiesType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }
    }

    public static class Spatial_OperatorsType extends FilterComplexType {
        private static final ComplexType instance = new Spatial_OperatorsType();
        private static Element[] elements = {
            new FilterElement("BBOX", EmptyType.getInstance()),
            new FilterElement("Equals", EmptyType.getInstance()),
            new FilterElement("Disjoint", EmptyType.getInstance()),
            new FilterElement("Intersect", EmptyType.getInstance()),
            new FilterElement("Touches", EmptyType.getInstance()),
            new FilterElement("Crosses", EmptyType.getInstance()),
            new FilterElement("Within", EmptyType.getInstance()),
            new FilterElement("Contains", EmptyType.getInstance()),
            new FilterElement("Overlaps", EmptyType.getInstance()),
            new FilterElement("Beyond", EmptyType.getInstance()),
            new FilterElement("DWithin", EmptyType.getInstance())
        };
        private static Choice choice = new ChoiceGT(elements) {
            @Override
            public int getMaxOccurs() {
                return Integer.MAX_VALUE;
            }
        };

        public static ComplexType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return choice;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (element == null || value == null) {
                throw new SAXException("Missing parameter for Spatial_OperatorsType");
            }

            if (value.length < 1) {
                throw new SAXException("Atleast one child element is required");
            }

            FilterCapabilities caps = new FilterCapabilities();

            for (ElementValue elementValue : value) {
                caps.addAll(FilterCapabilities.findOperation(
                        elementValue.getElement().getName()));
            }

            return caps;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "Spatial_OperatorsType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FilterCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }
    }

    public static class ExpressionType extends FilterComplexType implements org.geotools.filter.ExpressionType {
        private static final ComplexType instance = new ExpressionType();

        public static ComplexType getInstance() {
            return instance;
        }

        @Override
        public boolean isAbstract() {
            return true;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return null;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "ExpressionType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return Expression.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            if (hints != null && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if (fc.getScalarOps() == FilterCapabilities.NO_OP) {
                    return false;
                }
            }

            return element.getType() != null
                    && getName().equals(element.getType().getName())
                    && value instanceof Expression;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws IOException, OperationNotSupportedException {
            Expression e = (Expression) value;

            switch (Filters.getExpressionType(e)) {
                case ATTRIBUTE:
                case ATTRIBUTE_DOUBLE:
                case ATTRIBUTE_GEOMETRY:
                case ATTRIBUTE_INTEGER:
                case ATTRIBUTE_STRING:
                case ATTRIBUTE_UNDECLARED:
                    PropertyNameType.getInstance()
                            .encode(
                                    new FilterElement("PropertyName", PropertyNameType.getInstance()),
                                    e,
                                    output,
                                    hints);

                    return;

                case FUNCTION:
                    FunctionType.getInstance()
                            .encode(
                                    element != null
                                            ? element
                                            : new FilterElement("Function", FunctionType.getInstance()),
                                    e,
                                    output,
                                    hints);

                    return;

                case LITERAL_DOUBLE:
                case LITERAL_GEOMETRY:
                case LITERAL_INTEGER:
                case LITERAL_STRING:
                    LiteralType.getInstance()
                            .encode(
                                    element != null ? element : new FilterElement("Literal", LiteralType.getInstance()),
                                    e,
                                    output,
                                    hints);

                    return;

                case MATH_ADD:
                    BinaryOperatorType.getInstance()
                            .encode(
                                    element != null
                                            ? element
                                            : new FilterElement("Add", BinaryOperatorType.getInstance()),
                                    e,
                                    output,
                                    hints);

                    return;

                case MATH_DIVIDE:
                    BinaryOperatorType.getInstance()
                            .encode(
                                    element != null
                                            ? element
                                            : new FilterElement("Div", BinaryOperatorType.getInstance()),
                                    e,
                                    output,
                                    hints);

                    return;

                case MATH_MULTIPLY:
                    BinaryOperatorType.getInstance()
                            .encode(
                                    element != null
                                            ? element
                                            : new FilterElement("Mul", BinaryOperatorType.getInstance()),
                                    e,
                                    output,
                                    hints);

                    return;

                case MATH_SUBTRACT:
                    BinaryOperatorType.getInstance()
                            .encode(
                                    element != null
                                            ? element
                                            : new FilterElement("Sub", BinaryOperatorType.getInstance()),
                                    e,
                                    output,
                                    hints);

                    return;
            }

            throw new OperationNotSupportedException("Expression " + e + " type not found");
        }
    }

    public static class BinaryOperatorType extends FilterComplexType {
        private static final ComplexType instance = new BinaryOperatorType();

        //        <xsd:complexType name="BinaryOperatorType">
        //        <xsd:complexContent>
        //          <xsd:extension base="ogc:ExpressionType">
        //            <xsd:sequence>
        //              <xsd:element ref="ogc:expression" minOccurs="2" maxOccurs="2"/>
        //            </xsd:sequence>
        //          </xsd:extension>
        //        </xsd:complexContent>
        //      </xsd:complexType>
        private static Element[] elems = {
            new FilterElement("expression", ExpressionType.getInstance()) {
                @Override
                public int getMinOccurs() {
                    return 2;
                }

                @Override
                public int getMaxOccurs() {
                    return 2;
                }
            },
        };
        private static Sequence seq = new SequenceGT(elems);

        public static ComplexType getInstance() {
            return instance;
        }

        @Override
        public Type getParent() {
            return ExpressionType.getInstance();
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return null;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "BinaryOperatorType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return BinaryExpression.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            if (hints != null && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.SIMPLE_ARITHMETIC)
                        != FilterCapabilities.SIMPLE_ARITHMETIC) {
                    return false;
                }
            }

            return element.getType() != null
                    && getName().equals(element.getType().getName())
                    && value instanceof BinaryExpression;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            BinaryExpression me = (BinaryExpression) value;
            output.startElement(element.getNamespace(), element.getName(), null);
            elems[0].getType().encode(null, me.getExpression1(), output, hints);
            elems[0].getType().encode(null, me.getExpression2(), output, hints);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class FunctionType extends FilterComplexType {
        private static final ComplexType instance = new FunctionType();

        //        <xsd:complexType name="FunctionType">
        //          <xsd:complexContent>
        //            <xsd:extension base="ogc:ExpressionType">
        //              <xsd:sequence>
        //                <xsd:element ref="ogc:expression"
        //                             minOccurs="0" maxOccurs="unbounded"/>
        //              </xsd:sequence>
        //              <xsd:attribute name="name" type="xsd:string" use="required"/>
        //            </xsd:extension>
        //          </xsd:complexContent>
        //        </xsd:complexType>
        private static Element[] elems = {
            new FilterElement("expression", ExpressionType.getInstance()) {
                @Override
                public int getMinOccurs() {
                    return 0;
                }

                @Override
                public int getMaxOccurs() {
                    return Integer.MAX_VALUE;
                }
            },
        };
        private static Sequence seq = new SequenceGT(elems);
        private static Attribute[] attrs = {
            new FilterAttribute("name", XSISimpleTypes.String.getInstance(), Attribute.REQUIRED),
        };

        public static ComplexType getInstance() {
            return instance;
        }

        @Override
        public Attribute[] getAttributes() {
            return attrs;
        }

        @Override
        public Type getParent() {
            return ExpressionType.getInstance();
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints) {
            return null;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "FunctionType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return FunctionExpression.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            if (hints != null && hints.containsKey(FilterSchema.FILTER_CAP_KEY)) {
                FilterCapabilities fc = (FilterCapabilities) hints.get(FilterSchema.FILTER_CAP_KEY);

                if ((fc.getScalarOps() & FilterCapabilities.FUNCTIONS) != FilterCapabilities.FUNCTIONS) {
                    return false;
                }
            }

            return element.getType() != null
                    && getName().equals(element.getType().getName())
                    && value instanceof FunctionExpression;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            FunctionExpression me = (FunctionExpression) value;
            AttributesImpl ai = new AttributesImpl();
            ai.addAttribute(element.getNamespace().toString(), "name", null, "string", me.getName());
            output.startElement(element.getNamespace(), element.getName(), ai);

            for (org.geotools.api.filter.expression.Expression arg : me.getParameters()) {
                elems[0].getType().encode(null, arg, output, hints);
            }
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class LiteralType extends FilterComplexType {
        private static final ComplexType instance = new LiteralType();

        //        <xsd:complexType name="LiteralType">
        //        <xsd:complexContent mixed="true">
        //          <xsd:extension base="ogc:ExpressionType">
        //            <xsd:sequence>
        //              <xsd:any minOccurs="0"/>
        //            </xsd:sequence>
        //          </xsd:extension>
        //        </xsd:complexContent>
        //        </xsd:complexType>
        private static Any any = new AnyGT(null, 0, 1);
        private static Sequence seq = new SequenceGT(new ElementGrouping[] {
            any,
        });

        public static ComplexType getInstance() {
            return instance;
        }

        @Override
        public boolean isMixed() {
            return true;
        }

        @Override
        public Type getParent() {
            return ExpressionType.getInstance();
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException {

            FilterFactory factory = FilterSchema.filterFactory(hints);

            try {
                String string = (String) value[0].getValue(); // the spec says string!
                Object literal = string;
                try {
                    if (string.indexOf('.') != -1) {
                        literal = Double.valueOf(Double.parseDouble(string));
                    } else {
                        literal = Integer.valueOf(Integer.parseInt(string));
                    }
                } catch (NumberFormatException nonNumber) {
                    // ignore
                }
                return factory.literal(literal);
            } catch (IllegalFilterException e) {
                throw new SAXException("Illegal filter for " + element, e);
            }
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "LiteralType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return Literal.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return element.getType() != null
                    && getName().equals(element.getType().getName())
                    && (value instanceof Literal || value instanceof String);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                return;
            }

            AttributesImpl ai = new AttributesImpl();

            if (value instanceof String) {
                // short cut for single type
                output.startElement(element.getNamespace(), element.getName(), ai);
                output.characters(value.toString());
                output.endElement(element.getNamespace(), element.getName());
                return;
            }

            Literal me = (Literal) value;
            output.startElement(element.getNamespace(), element.getName(), ai);

            switch (Filters.getExpressionType(me)) {
                case org.geotools.filter.ExpressionType.LITERAL_GEOMETRY:
                    if (me.getValue() instanceof Geometry) {
                        GMLSchema.getInstance()
                                .getElements()[29]
                                .getType()
                                .encode(GMLSchema.getInstance().getElements()[29], me.getValue(), output, hints);

                        break;
                    }

                case org.geotools.filter.ExpressionType.LITERAL_DOUBLE:
                case org.geotools.filter.ExpressionType.LITERAL_INTEGER:
                case org.geotools.filter.ExpressionType.LITERAL_STRING:
                case org.geotools.filter.ExpressionType.LITERAL_LONG:
                    output.characters(me.getValue().toString());

                    break;
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class PropertyNameType extends FilterComplexType {
        private static final ComplexType instance = new PropertyNameType();

        public static ComplexType getInstance() {
            return instance;
        }

        //        <xsd:complexType name="PropertyNameType">
        //          <xsd:complexContent mixed="true">
        //            <xsd:extension base="ogc:ExpressionType"/>
        //          </xsd:complexContent>
        //        </xsd:complexType>
        @Override
        public boolean isMixed() {
            return true;
        }

        @Override
        public Type getParent() {
            return ExpressionType.getInstance();
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws SAXException {

            FilterFactory factory = FilterSchema.filterFactory(hints);

            try {
                String xpath = (String) value[0].getValue();
                return factory.property(xpath);
            } catch (ClassCastException expressionRequired) {
                throw new SAXException("Illegal xpath for property name " + element, expressionRequired);
            }
            // return (value == null) ? "" : value[0].toString();
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "PropertyNameType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return PropertyName.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return (value instanceof PropertyName || value instanceof String)
                    && element.getType() != null
                    && getName().equals(element.getType().getName());
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode "
                        + (element == null ? "null" : element.getName())
                        + " "
                        + (element == null ? "null" : element.getNamespace().toString())
                        + " "
                        + (value == null ? null : value.getClass().getName()));
            }

            output.startElement(element.getNamespace(), element.getName(), null);

            if (value instanceof String) {
                output.characters((String) value);
            } else {
                PropertyName name = (PropertyName) value;
                output.characters(name.getPropertyName());
            }

            output.endElement(element.getNamespace(), element.getName());
        }
    }

    public static class ServiceExceptionType extends FilterComplexType {
        private static final ComplexType instance = new ServiceExceptionType();

        //        <xsd:complexType name="ServiceExceptionType">
        //        <xsd:annotation>
        //           <xsd:documentation>
        //              The ServiceExceptionType type defines the ServiceException
        //              element.  The content of the element is an exception message
        //              that the service wished to convey to the client application.
        //           </xsd:documentation>
        //        </xsd:annotation>
        //        <xsd:simpleContent>
        //           <xsd:extension base="xsd:string">
        //              <xsd:attribute name="code" type="xsd:string">
        //                 <xsd:annotation>
        //                    <xsd:documentation>
        //                       A service may associate a code with an exception
        //                       by using the code attribute.
        //                    </xsd:documentation>
        //                 </xsd:annotation>
        //              </xsd:attribute>
        //              <xsd:attribute name="locator" type="xsd:string">
        //                 <xsd:annotation>
        //                    <xsd:documentation>
        //                       The locator attribute may be used by a service to
        //                       indicate to a client where in the client's request
        //                       an exception was encountered.  If the request included
        //                       a 'handle' attribute, this may be used to identify the
        //                       offending component of the request.  Otherwise the
        //                       service may try to use other means to locate the
        //                       exception such as line numbers or byte offset from the
        //                       begining of the request, etc ...
        //                    </xsd:documentation>
        //                 </xsd:annotation>
        //              </xsd:attribute>
        //           </xsd:extension>
        //        </xsd:simpleContent>
        //     </xsd:complexType>
        private static Attribute[] attrs = {
            new FilterAttribute("code", XSISimpleTypes.String.getInstance()),
            new FilterAttribute("locator", XSISimpleTypes.String.getInstance()),
        };

        public static ComplexType getInstance() {
            return instance;
        }

        @Override
        public boolean isMixed() {
            return true;
        }

        @Override
        public Attribute[] getAttributes() {
            return attrs;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#cache(org.geotools.xml.schema.Element, java.util.Map) */
        @Override
        public boolean cache(Element element, Map<String, Object> hints) {
            return hints != null && hints.containsKey(CACHE_SERVICE_EXCEPTIONS);
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (value == null
                    || element.getType() == null
                    || !getName().equals(element.getType().getName())) {
                throw new SAXNotSupportedException("wrong element type for service exception");
            }

            String msg = (String) value[0].getValue();

            String locator = attrs1.getValue(null, "locator");

            if (locator == null) {
                locator = attrs1.getValue(getNamespace().toString(), "locator");
            }

            String code = attrs1.getValue(null, "code");

            if (code == null) {
                code = attrs1.getValue(getNamespace().toString(), "code");
            }

            ServiceException se = new ServiceException(
                    msg == null ? "" : msg, code == null ? "" : code, locator == null ? "" : locator);

            if (cache(element, hints)) {
                return se;
            }

            throw se;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "ServiceExceptionType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return ServiceException.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            // TODO Auto-generated method stub
            throw new OperationNotSupportedException();
        }
    }

    public static class ServiceExceptionReportType extends FilterComplexType {
        private static final ComplexType instance = new ServiceExceptionReportType();

        //        <xsd:complexType>
        //           <xsd:sequence>
        //              <xsd:element name="ServiceException"
        //                           type="ogc:ServiceExceptionType"
        //                           minOccurs="0" maxOccurs="unbounded">
        //                 <xsd:annotation>
        //                    <xsd:documentation>
        //                       The Service exception element is used to describe
        //                       a service exception.
        //                    </xsd:documentation>
        //                 </xsd:annotation>
        //              </xsd:element>
        //           </xsd:sequence>
        //           <xsd:attribute name="version" type="xsd:string" fixed="1.2.0"/>
        //        </xsd:complexType>
        private static Element[] elems = {
            new FilterElement("ServiceException", ServiceExceptionType.getInstance()) {
                @Override
                public int getMinOccurs() {
                    return 0;
                }

                @Override
                public int getMaxOccurs() {
                    return Integer.MAX_VALUE;
                }
            },
        };
        private static Sequence seq = new SequenceGT(elems);
        private static Attribute[] attrs = {
            new FilterAttribute("version", XSISimpleTypes.String.getInstance(), 0, null, "1.2.0", false),
        };

        public static ComplexType getInstance() {
            return instance;
        }

        @Override
        public Attribute[] getAttributes() {
            return attrs;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws SAXException, SAXNotSupportedException {
            if (value == null
                    || element.getType() == null
                    || !getName().equals(element.getType().getName())) {
                throw new SAXNotSupportedException("wrong element type for service exception report");
            }

            ServiceException[] ret = new ServiceException[value.length];

            for (int i = 0; i < ret.length; i++) {
                ret[i] = (ServiceException) value[i].getValue();
            }

            return ret;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "ServiceExceptionReportType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return ServiceException[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    public static class EmptyType extends FilterComplexType {
        private static EmptyType instance = new EmptyType();

        public static EmptyType getInstance() {
            return instance;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return null;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints) {
            return null;
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return null;
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    /** This is a Filter 1.0.20 filter element. As such it has not been included in the FilterSchema */
    public static class SortByType extends FilterComplexType {
        private static SortByType instance = new SortByType();

        private SortByType() {
            // no op constructor
        }

        public static SortByType getInstance() {
            return instance;
        }

        private static Element[] elems = loadE();

        private static Element[] loadE() {
            Element exp = new FilterElement("expression", ExpressionType.getInstance()) {
                @Override
                public boolean isAbstract() {
                    return true;
                }
            };
            return new Element[] {
                new FilterElement("PropertyName", PropertyNameType.getInstance(), exp) {
                    @Override
                    public int getMaxOccurs() {
                        return ElementGrouping.UNBOUNDED;
                    }
                }
            };
        }

        private static Sequence seq = new SequenceGT(elems);
        private static Attribute[] attrs = {
            new FilterAttribute("sortOrder", SortOrderType.getInstance(), Attribute.OPTIONAL, "DESC", null, false)
        };
        /** @see org.geotools.xml.schema.ComplexType#getChild() */
        @Override
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        @Override
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * TODO summary sentence for getAttributes ...
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        @Override
        public Attribute[] getAttributes() {
            return attrs;
        }
        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /** @see org.geotools.xml.schema.Type#getName() */
        @Override
        public String getName() {
            return "SortByType";
        }

        /** @see org.geotools.xml.schema.Type#getInstanceType() */
        @Override
        public Class getInstanceType() {
            return PropertyName[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return element != null
                    && element.getType().equals(this)
                    && value != null
                    && value instanceof PropertyName[];
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) throw new IOException("Cannot encode");

            PropertyName[] pns = (PropertyName[]) value;

            AttributesImpl ai = null;
            if (hints != null && hints.containsKey(SortOrderType.SORT_ORDER_KEY)) {
                if (hints.get(SortOrderType.SORT_ORDER_KEY) == SortOrderType.SORT_ORDER_ASC) {
                    ai = new AttributesImpl();
                    ai.addAttribute(FilterSchema.NAMESPACE.toString(), "sortOrder", null, "string", "ASC");
                }
            }
            output.startElement(element.getNamespace(), element.getName(), ai);
            for (PropertyName pn : pns) {
                elems[0].getType().encode(elems[0], pn, output, hints);
            }
            output.endElement(element.getNamespace(), element.getName());
        }
    }
    /** also from the 1.0.20 version, and excluded from the Schema object */
    public static class SortOrderType implements SimpleType {
        public static final String SORT_ORDER_KEY = "org.geotools.xml.ogc.SortOrderType_KEY";
        public static final String SORT_ORDER_DESC = "org.geotools.xml.ogc.SortOrderType_DESC";
        public static final String SORT_ORDER_ASC = "org.geotools.xml.ogc.SortOrderType_ASC";
        private static SortOrderType instance = new SortOrderType();

        private SortOrderType() {
            // no op constructor
        }

        public static SortOrderType getInstance() {
            return instance;
        }
        /**
         * TODO summary sentence for getFinal ...
         *
         * @see org.geotools.xml.schema.SimpleType#getFinal()
         */
        @Override
        public int getFinal() {
            return 0;
        }
        /**
         * TODO summary sentence for getId ...
         *
         * @see org.geotools.xml.schema.SimpleType#getId()
         */
        @Override
        public String getId() {
            return null;
        }
        /**
         * TODO summary sentence for toAttribute ...
         *
         * @see org.geotools.xml.schema.SimpleType#toAttribute(org.geotools.xml.schema.Attribute, java.lang.Object,
         *     java.util.Map)
         */
        @Override
        public AttributeValue toAttribute(Attribute attribute, Object value, Map<String, Object> hints)
                throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
        /**
         * TODO summary sentence for canCreateAttributes ...
         *
         * @see org.geotools.xml.schema.SimpleType#canCreateAttributes(org.geotools.xml.schema.Attribute,
         *     java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canCreateAttributes(Attribute attribute, Object value, Map<String, Object> hints) {
            return false;
        }
        /**
         * TODO summary sentence for getChildType ...
         *
         * @see org.geotools.xml.schema.SimpleType#getChildType()
         */
        @Override
        public int getChildType() {
            return RESTRICTION;
        }
        /**
         * TODO summary sentence for getParents ...
         *
         * @see org.geotools.xml.schema.SimpleType#getParents()
         */
        @Override
        public SimpleType[] getParents() {
            return parents;
        }

        private static SimpleType[] parents = {XSISimpleTypes.String.getInstance()};
        /**
         * TODO summary sentence for getFacets ...
         *
         * @see org.geotools.xml.schema.SimpleType#getFacets()
         */
        @Override
        public Facet[] getFacets() {
            return facets;
        }

        private static Facet[] facets = {new FacetGT(Facet.ENUMERATION, "DESC"), new FacetGT(Facet.ENUMERATION, "ASC")};
        /**
         * TODO summary sentence for getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map<String, Object> hints)
                throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
        /**
         * TODO summary sentence for getName ...
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        @Override
        public String getName() {
            return "SortOrderType";
        }
        /**
         * TODO summary sentence for getNamespace ...
         *
         * @see org.geotools.xml.schema.Type#getNamespace()
         */
        @Override
        public URI getNamespace() {
            return FilterSchema.NAMESPACE;
        }
        /**
         * TODO summary sentence for getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return String.class;
        }
        /**
         * TODO summary sentence for canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return false;
        }
        /**
         * TODO summary sentence for encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
        /**
         * TODO summary sentence for findChildElement ...
         *
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        @Override
        public Element findChildElement(String name) {
            return null;
        }
    }
}
