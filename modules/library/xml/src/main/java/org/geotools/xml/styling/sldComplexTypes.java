/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.styling;

/**
 * This code generated using Refractions SchemaCodeGenerator For more information, view the attached licensing
 * information. CopyRight 105
 */
import java.util.Map;
import javax.naming.OperationNotSupportedException;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Mark;
import org.geotools.api.style.Stroke;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ContrastEnhancementImpl;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.impl.AttributeGT;
import org.geotools.xml.schema.impl.ChoiceGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.styling.sldComplexTypes2.ParameterValueType;
import org.geotools.xml.styling.sldComplexTypes2.SelectedChannelType;
import org.geotools.xml.styling.sldComplexTypes2._LinePlacement;
import org.geotools.xml.styling.sldComplexTypes2._LineSymbolizer;
import org.geotools.xml.styling.sldComplexTypes2._Mark;
import org.geotools.xml.styling.sldComplexTypes2._Normalize;
import org.geotools.xml.styling.sldComplexTypes2._OnlineResource;
import org.geotools.xml.styling.sldComplexTypes2._PointPlacement;
import org.geotools.xml.styling.sldComplexTypes2._PolygonSymbolizer;
import org.geotools.xml.styling.sldComplexTypes2._Rule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class sldComplexTypes {

    static class _Histogram extends sldComplexType {
        private static final ComplexType instance = new _Histogram();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = null;
        private static final ElementGrouping child = new SequenceGT(null);

        private _Histogram() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _ContrastEnhancement extends sldComplexType {
        private static final ComplexType instance = new _ContrastEnhancement();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("Normalize", _Normalize.getInstance(), null, 1, 1),
            new sldElement("Histogram", _Histogram.getInstance(), null, 1, 1),
            new sldElement(
                    "GammaValue",
                    org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance() /* simpleType name is double */,
                    null,
                    0,
                    1)
        };

        private static final int NORMALIZE = 0;
        private static final int HISTORGRAM = 1;
        private static final int GAMMAVALUE = 2;

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new ChoiceGT(null, 0, 1, new ElementGrouping[] {elems[0], elems[1]}),
                    new sldElement(
                            "GammaValue",
                            org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance() /* simpleType name is double */,
                            null,
                            0,
                            1)
                },
                1,
                1);

        private _ContrastEnhancement() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return ContrastEnhancement.class;
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            ContrastEnhancement symbol = new ContrastEnhancementImpl();

            for (ElementValue elementValue : value) {
                if (elementValue == null || elementValue.getElement() == null) {
                    continue;
                }

                Element e = elementValue.getElement();
                if (elems[NORMALIZE].getName().equals(e.getName()))
                    symbol.setMethod(ContrastMethod.NORMALIZE); // (Graphic)value[i].getValue()

                if (elems[HISTORGRAM].getName().equals(e.getName()))
                    symbol.setMethod(ContrastMethod.HISTOGRAM); // (Graphic)value[i].getValue()

                if (elems[GAMMAVALUE].getName().equals(e.getName())) {
                    FilterFactory ff = CommonFactoryFinder.getFilterFactory();
                    symbol.setGammaValue(ff.literal(((Double) elementValue.getValue()).doubleValue()));
                }
            }

            return symbol;
        }
    }

    static class _Displacement extends sldComplexType {
        private static final ComplexType instance = new _Displacement();

        public static ComplexType getInstance() {
            return instance;
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("DisplacementX", ParameterValueType.getInstance(), null, 1, 1),
            new sldElement("DisplacementY", ParameterValueType.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement("DisplacementX", ParameterValueType.getInstance(), null, 1, 1),
                    new sldElement("DisplacementY", ParameterValueType.getInstance(), null, 1, 1)
                },
                1,
                1);

        private _Displacement() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _Geometry extends sldComplexType {
        private static final ComplexType instance = new _Geometry();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement(
                    "PropertyName",
                    org.geotools.xml.filter.FilterComplexTypes.PropertyNameType.getInstance(),
                    null,
                    1,
                    1)
        };

        private static final ElementGrouping child = new SequenceGT(elems);

        private _Geometry() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            //            Geometry symbol =
            // StyleFactory.createStyleFactory().getDefaultRasterSymbolizer();
            // symbol.setGraphic(null);

            //            for (int i = 0; i < value.length; i++) {
            //                if ((value[i] == null) || value[i].getElement() == null) {
            //                    continue;
            //                }
            //
            //                Element e = value[i].getElement();
            //                if(elems[PROPERTYNAME].getName().equals(e.getName()))
            //                    symbol.setGeometryPropertyName((String)value[i].getValue());
            //            }

            //            return symbol;
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _ExternalGraphic extends sldComplexType {
        private static final ComplexType instance = new _ExternalGraphic();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("OnlineResource", _OnlineResource.getInstance(), null, 1, 1),
            new sldElement("Format", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement("OnlineResource", _OnlineResource.getInstance(), null, 1, 1),
                    new sldElement(
                            "Format",
                            org.geotools.xml.xsi.XSISimpleTypes.String.getInstance() /* simpleType name is string */,
                            null,
                            1,
                            1)
                },
                1,
                1);

        private _ExternalGraphic() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _GraphicStroke extends sldComplexType {
        private static final ComplexType instance = new _GraphicStroke();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {new sldElement("Graphic", null, null, 1, 1)};

        private static final ElementGrouping child =
                new SequenceGT(null, new ElementGrouping[] {new sldElement("Graphic", null, null, 1, 1)}, 1, 1);

        private _GraphicStroke() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _CssParameter extends sldComplexType {
        private static final ComplexType instance = new _CssParameter();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = {
            new AttributeGT(
                    null,
                    "name",
                    sldSchema.NAMESPACE,
                    org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                    Attribute.REQUIRED,
                    null,
                    null,
                    false)
        };

        private static final Element[] elems = {
            new sldElement(
                    "expression", org.geotools.xml.filter.FilterComplexTypes.ExpressionType.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement(
                            "expression",
                            org.geotools.xml.filter.FilterComplexTypes.ExpressionType.getInstance(),
                            null,
                            1,
                            1)
                },
                0,
                Element.UNBOUNDED);

        private _CssParameter() {
            super(null, child, attrs, elems, ParameterValueType.getInstance(), false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _Graphic extends sldComplexType {
        private static final ComplexType instance = new _Graphic();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("ExternalGraphic", _ExternalGraphic.getInstance(), null, 1, 1),
            new sldElement("Mark", _Mark.getInstance(), null, 1, 1),
            new sldElement("Opacity", ParameterValueType.getInstance(), null, 0, 1),
            new sldElement("Size", ParameterValueType.getInstance(), null, 0, 1),
            new sldElement("Rotation", ParameterValueType.getInstance(), null, 0, 1)
        };

        // array positions
        private static final int EXTERNALGRAPHIC = 0;
        private static final int MARK = 0;
        private static final int OPACITY = 0;
        private static final int SIZE = 0;
        private static final int ROTATION = 0;

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return Graphic.class;
        }

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new ChoiceGT(null, 0, Element.UNBOUNDED, new ElementGrouping[] {
                        new sldElement("ExternalGraphic", sldComplexTypes._ExternalGraphic.getInstance(), null, 1, 1),
                        new sldElement("Mark", _Mark.getInstance(), null, 1, 1)
                    }),
                    new SequenceGT(
                            null,
                            new ElementGrouping[] {
                                new sldElement("Opacity", ParameterValueType.getInstance(), null, 0, 1),
                                new sldElement("Size", ParameterValueType.getInstance(), null, 0, 1),
                                new sldElement("Rotation", ParameterValueType.getInstance(), null, 0, 1)
                            },
                            1,
                            1)
                },
                1,
                1);

        private _Graphic() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException {
            Graphic symbol = CommonFactoryFinder.getStyleFactory().getDefaultGraphic();

            for (ElementValue elementValue : value) {
                if (elementValue == null || elementValue.getElement() == null) {
                    continue;
                }

                Element e = elementValue.getElement();
                if (elems[EXTERNALGRAPHIC].getName().equals(e.getName()))
                    symbol.graphicalSymbols().add((ExternalGraphic) elementValue.getValue());

                if (elems[MARK].getName().equals(e.getName()))
                    symbol.graphicalSymbols().add((Mark) elementValue.getValue());

                if (elems[OPACITY].getName().equals(e.getName()))
                    symbol.setOpacity((Expression) elementValue.getValue());

                if (elems[SIZE].getName().equals(e.getName())) symbol.setSize((Expression) elementValue.getValue());

                if (elems[ROTATION].getName().equals(e.getName()))
                    symbol.setRotation((Expression) elementValue.getValue());
            }

            return symbol;
        }
    }

    static class _GraphicFill extends sldComplexType {
        private static final ComplexType instance = new _GraphicFill();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {new sldElement("Graphic", _Graphic.getInstance(), null, 1, 1)};

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {new sldElement("Graphic", sldComplexTypes._Graphic.getInstance(), null, 1, 1)},
                1,
                1);

        private _GraphicFill() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return _Graphic.getInstance().getInstanceType();
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return _Graphic.getInstance().getValue(element, value, attrs1, hints);
        }
    }

    static class _Fill extends sldComplexType {
        private static final ComplexType instance = new _Fill();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("GraphicFill", _GraphicFill.getInstance(), null, 0, 1),
            new sldElement("CssParameter", _CssParameter.getInstance(), null, 0, Element.UNBOUNDED)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement("GraphicFill", sldComplexTypes._GraphicFill.getInstance(), null, 0, 1),
                    new sldElement(
                            "CssParameter", sldComplexTypes._CssParameter.getInstance(), null, 0, Element.UNBOUNDED)
                },
                1,
                1);

        private static final int GRAPHICFILL = 0;

        private _Fill() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return Stroke.class;
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException {
            Stroke symbol = CommonFactoryFinder.getStyleFactory().getDefaultStroke();

            for (ElementValue elementValue : value) {
                if (elementValue == null || elementValue.getElement() == null) {
                    continue;
                }

                Element e = elementValue.getElement();
                if (elems[GRAPHICFILL].getName().equals(e.getName()))
                    symbol.setGraphicFill((Graphic) elementValue.getValue());

                //                if (elems[CSSPARAMETER].getName().equals(e.getName())) {
                //                    // TODO apply the css
                //                }
            }

            return symbol;
        }
    }

    static class _ColorMapEntry extends sldComplexType {
        private static final ComplexType instance = new _ColorMapEntry();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = {
            new AttributeGT(
                    null,
                    "color",
                    sldSchema.NAMESPACE,
                    org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                    Attribute.REQUIRED,
                    null,
                    null,
                    false),
            new AttributeGT(
                    null,
                    "opacity",
                    sldSchema.NAMESPACE,
                    org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance(),
                    -1,
                    null,
                    null,
                    false),
            new AttributeGT(
                    null,
                    "quantity",
                    sldSchema.NAMESPACE,
                    org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance(),
                    -1,
                    null,
                    null,
                    false),
            new AttributeGT(
                    null,
                    "label",
                    sldSchema.NAMESPACE,
                    org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                    -1,
                    null,
                    null,
                    false)
        };

        private static final Element[] elems = null;
        private static final ElementGrouping child = new SequenceGT(null);

        private _ColorMapEntry() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _LATEST_ON_TOP extends sldComplexType {
        private static final ComplexType instance = new _LATEST_ON_TOP();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = null;
        private static final ElementGrouping child = new SequenceGT(null);

        private _LATEST_ON_TOP() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _Extent extends sldComplexType {
        private static final ComplexType instance = new _Extent();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("Name", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 1, 1),
            new sldElement("Value", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement(
                            "Name",
                            org.geotools.xml.xsi.XSISimpleTypes.String.getInstance() /* simpleType name is string */,
                            null,
                            1,
                            1),
                    new sldElement(
                            "Value",
                            org.geotools.xml.xsi.XSISimpleTypes.String.getInstance() /* simpleType name is string */,
                            null,
                            1,
                            1)
                },
                1,
                1);

        private _Extent() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _FeatureTypeConstraint extends sldComplexType {
        private static final ComplexType instance = new _FeatureTypeConstraint();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement(
                    "FeatureTypeName",
                    org.geotools.xml.xsi.XSISimpleTypes.String.getInstance() /* simpleType name is string */,
                    null,
                    0,
                    1),
            new sldElement(
                    "Filter",
                    org.geotools.xml.filter.FilterOpsComplexTypes.FilterType
                            .getInstance() /* complexType name is FilterType */,
                    null,
                    0,
                    1),
            new sldElement("Extent", _Extent.getInstance(), null, 0, Element.UNBOUNDED)
        };

        private static final ElementGrouping child = new SequenceGT(elems);

        private _FeatureTypeConstraint() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _ChannelSelection extends sldComplexType {
        private static final ComplexType instance = new _ChannelSelection();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("RedChannel", SelectedChannelType.getInstance(), null, 1, 1),
            new sldElement("GreenChannel", SelectedChannelType.getInstance(), null, 1, 1),
            new sldElement("BlueChannel", SelectedChannelType.getInstance(), null, 1, 1),
            new sldElement("GrayChannel", SelectedChannelType.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new ChoiceGT(null, 1, 1, new ElementGrouping[] {
            new SequenceGT(
                    null,
                    new ElementGrouping[] {
                        new sldElement("RedChannel", SelectedChannelType.getInstance(), null, 1, 1),
                        new sldElement("GreenChannel", SelectedChannelType.getInstance(), null, 1, 1),
                        new sldElement("GreenChannel", SelectedChannelType.getInstance(), null, 1, 1)
                    },
                    1,
                    1),
            new sldElement("GrayChannel", SelectedChannelType.getInstance(), null, 1, 1)
        });

        private _ChannelSelection() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _Font extends sldComplexType {
        private static final ComplexType instance = new _Font();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("CssParameter", _CssParameter.getInstance(), null, 0, Element.UNBOUNDED)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement(
                            "CssParameter", sldComplexTypes._CssParameter.getInstance(), null, 0, Element.UNBOUNDED)
                },
                1,
                1);

        private _Font() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _ElseFilter extends sldComplexType {
        private static final ComplexType instance = new _ElseFilter();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = null;
        private static final ElementGrouping child = new SequenceGT(null);

        private _ElseFilter() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _FeatureTypeStyle extends sldComplexType {
        private static final ComplexType instance = new _FeatureTypeStyle();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("Name", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
            new sldElement("Title", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
            new sldElement("Abstract", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
            new sldElement(
                    "FeatureTypeName",
                    org.geotools.xml.xsi.XSISimpleTypes.String.getInstance() /* simpleType name is string */,
                    null,
                    0,
                    1),
            new sldElement(
                    "SemanticTypeIdentifier",
                    org.geotools.xml.xsi.XSISimpleTypes.String.getInstance() /* simpleType name is string */,
                    null,
                    0,
                    Element.UNBOUNDED),
            new sldElement("Rule", _Rule.getInstance(), null, 1, Element.UNBOUNDED)
        };

        private static final ElementGrouping child = new SequenceGT(elems);

        private _FeatureTypeStyle() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _AnchorPoint extends sldComplexType {
        private static final ComplexType instance = new _AnchorPoint();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("AnchorPointX", ParameterValueType.getInstance(), null, 1, 1),
            new sldElement("AnchorPointY", ParameterValueType.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement("AnchorPointX", ParameterValueType.getInstance(), null, 1, 1),
                    new sldElement("AnchorPointY", ParameterValueType.getInstance(), null, 1, 1)
                },
                1,
                1);

        private _AnchorPoint() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _EARLIEST_ON_TOP extends sldComplexType {
        private static final ComplexType instance = new _EARLIEST_ON_TOP();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = null;
        private static final ElementGrouping child = new SequenceGT(null);

        private _EARLIEST_ON_TOP() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _LabelPlacement extends sldComplexType {
        private static final ComplexType instance = new _LabelPlacement();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("PointPlacement", _PointPlacement.getInstance(), null, 1, 1),
            new sldElement("LinePlacement", _LinePlacement.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new ChoiceGT(null, 1, 1, new ElementGrouping[] {
            new sldElement("PointPlacement", _PointPlacement.getInstance(), null, 1, 1),
            new sldElement("LinePlacement", _LinePlacement.getInstance(), null, 1, 1)
        });

        private _LabelPlacement() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _Halo extends sldComplexType {
        private static final ComplexType instance = new _Halo();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("Radius", ParameterValueType.getInstance(), null, 0, 1),
            new sldElement("Fill", _Fill.getInstance(), null, 0, 1)
        };

        private static final ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[] {
                    new sldElement("Radius", ParameterValueType.getInstance(), null, 0, 1),
                    new sldElement("Fill", sldComplexTypes._Fill.getInstance(), null, 0, 1)
                },
                1,
                1);

        private _Halo() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _AVERAGE extends sldComplexType {
        private static final ComplexType instance = new _AVERAGE();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = null;
        private static final ElementGrouping child = new SequenceGT(null);

        private _AVERAGE() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _ImageOutline extends sldComplexType {
        private static final ComplexType instance = new _ImageOutline();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("LineSymbolizer", _LineSymbolizer.getInstance(), null, 1, 1),
            new sldElement("PolygonSymbolizer", _PolygonSymbolizer.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new ChoiceGT(null, 1, 1, new ElementGrouping[] {
            new sldElement("LineSymbolizer", _LineSymbolizer.getInstance(), null, 1, 1),
            new sldElement("PolygonSymbolizer", _PolygonSymbolizer.getInstance(), null, 1, 1)
        });

        private _ImageOutline() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _ColorMap extends sldComplexType {
        private static final ComplexType instance = new _ColorMap();

        public static ComplexType getInstance() {
            return instance;
        }

        private static final Attribute[] attrs = null;
        private static final Element[] elems = {
            new sldElement("ColorMapEntry", _ColorMapEntry.getInstance(), null, 1, 1)
        };

        private static final ElementGrouping child = new ChoiceGT(null, 0, Element.UNBOUNDED, new ElementGrouping[] {
            new sldElement("ColorMapEntry", sldComplexTypes._ColorMapEntry.getInstance(), null, 1, 1)
        });

        private _ColorMap() {
            super(null, child, attrs, elems, null, false, false);
        }

        /**
         * getInstanceType ...
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        @Override
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }

        /**
         * canEncode ...
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        @Override
        public boolean canEncode(Element element, Object value, Map<String, Object> hints) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         * encode ...
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
         *     org.geotools.xml.PrintHandler, java.util.Map)
         */
        @Override
        public void encode(Element element, Object value, PrintHandler output, Map<String, Object> hints)
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         * getValue ...
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *     org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        @Override
        public Object getValue(Element element, ElementValue[] value, Attributes attrs1, Map<String, Object> hints)
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }
}
