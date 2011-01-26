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
package org.geotools.xml.styling;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.filter.Expression;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.NamedStyle;
import org.geotools.styling.NamedStyleImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.RemoteOWS;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.StyledLayerImpl;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.impl.AttributeGT;
import org.geotools.xml.schema.impl.ChoiceGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.styling.sldComplexTypes._AVERAGE;
import org.geotools.xml.styling.sldComplexTypes._AnchorPoint;
import org.geotools.xml.styling.sldComplexTypes._ChannelSelection;
import org.geotools.xml.styling.sldComplexTypes._ColorMap;
import org.geotools.xml.styling.sldComplexTypes._ContrastEnhancement;
import org.geotools.xml.styling.sldComplexTypes._CssParameter;
import org.geotools.xml.styling.sldComplexTypes._Displacement;
import org.geotools.xml.styling.sldComplexTypes._EARLIEST_ON_TOP;
import org.geotools.xml.styling.sldComplexTypes._ElseFilter;
import org.geotools.xml.styling.sldComplexTypes._FeatureTypeConstraint;
import org.geotools.xml.styling.sldComplexTypes._FeatureTypeStyle;
import org.geotools.xml.styling.sldComplexTypes._Fill;
import org.geotools.xml.styling.sldComplexTypes._Font;
import org.geotools.xml.styling.sldComplexTypes._Geometry;
import org.geotools.xml.styling.sldComplexTypes._Graphic;
import org.geotools.xml.styling.sldComplexTypes._GraphicStroke;
import org.geotools.xml.styling.sldComplexTypes._Halo;
import org.geotools.xml.styling.sldComplexTypes._ImageOutline;
import org.geotools.xml.styling.sldComplexTypes._LATEST_ON_TOP;
import org.geotools.xml.styling.sldComplexTypes._LabelPlacement;
import org.geotools.xml.xLink.XLinkSchema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Provides ...TODO summary sentence
 * <p>
 * TODO Description
 * </p><p>
 * Responsibilities:
 * <ul>
 * <li>
 * <li>
 * </ul>
 * </p><p>
 * Example Use:<pre><code>
 * sldComplexTypes2 x = new sldComplexTypes2( ... );
 * TODO code example
 * </code></pre>
 * </p>
 * @author Leprosy
 * @since 0.3
 * @source $URL$
 */
public class sldComplexTypes2 {

    static class _LayerFeatureConstraints extends sldComplexType {
        private static ComplexType instance = new _LayerFeatureConstraints();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{new sldElement("FeatureTypeConstraint",
                _FeatureTypeConstraint.getInstance(), null, 1, Element.UNBOUNDED)};
        
        private static int FEATURETYPOECONSTRAINT = 0;
    
        private static ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[]{new sldElement("FeatureTypeConstraint",
                        _FeatureTypeConstraint.getInstance(), null, 1, Element.UNBOUNDED)},
                1, 1);
    
        private _LayerFeatureConstraints() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _LegendGraphic extends sldComplexType {
        private static ComplexType instance = new _LegendGraphic();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{new sldElement("Graphic",
                _Graphic.getInstance(), null, 1, 1)};
        
        private static int GRAPHIC = 0;
    
        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{new sldElement("Graphic", _Graphic
                        .getInstance(), null, 1, 1)}, 1, 1);
    
        private _LegendGraphic() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
        	return _Graphic.getInstance().getInstanceType();
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return _Graphic.getInstance().getValue(element,value,attrs1,hints);
        }
    }

    static class _LinePlacement extends sldComplexType {
        private static ComplexType instance = new _LinePlacement();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{new sldElement("PerpendicularOffset",
                ParameterValueType.getInstance(), null, 0, 1)};
        
        private static int PERPENDICULAROFFSET = 0;
    
        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{new sldElement("PerpendicularOffset",
                        ParameterValueType.getInstance(), null, 0, 1)}, 1, 1);
    
        private _LinePlacement() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LinePlacement.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints ){

        	Expression offset = null;
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
                Element e = value[i].getElement();
                if(elems[PERPENDICULAROFFSET].getName().equals(e.getName()))
                	offset = (Expression)value[i].getValue();
            }
        	
        	LinePlacement dlp = StyleFactoryFinder.createStyleFactory().createLinePlacement(offset);
        	return dlp;
        }
    }

    static class _LineSymbolizer extends sldComplexType {
        private static ComplexType instance = new _LineSymbolizer();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Geometry", _Geometry.getInstance(), null, 0, 1),
                new sldElement("Stroke", _Stroke.getInstance(), null, 0, 1)};
        
        // array positions
        private static int GEOMETRY = 0;
        private static int STROKE = 1;
    
        private static ElementGrouping child = new SequenceGT(elems);
    
        private _LineSymbolizer() {
            super(null, child, attrs, elems, SymbolizerType.getInstance(), false,
                    false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LineSymbolizer.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints ){
            LineSymbolizer symbol = StyleFactoryFinder.createStyleFactory().getDefaultLineSymbolizer();
            // symbol.setGraphic(null);
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[GEOMETRY].getName().equals(e.getName()))
                    symbol.setGeometryPropertyName((String)value[i].getValue());
    
                if(elems[STROKE].getName().equals(e.getName()))
                    symbol.setStroke((Stroke)value[i].getValue());
            }
            
            return symbol;
        }
    }

    static class _Mark extends sldComplexType {
        private static ComplexType instance = new _Mark();
        public static ComplexType getInstance() {
            return instance;
        }
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("WellKnownName", org.geotools.xml.xsi.XSISimpleTypes.String
                        .getInstance()/* simpleType name is string */, null, 0, 1),
                new sldElement("Fill", null, null, 0, 1),
                new sldElement("Stroke", _Stroke.getInstance(), null, 0, 1)};
    
        // array spots
        private static int WELLKNOWNNAME = 0;
        private static int FILL = 1;
        private static int STROKE = 2;
        
        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{
                        new sldElement("WellKnownName", org.geotools.xml.xsi.XSISimpleTypes.String
                                .getInstance()/* simpleType name is string */, null, 0, 1),
                        new sldElement("Fill", null, null, 0, 1),
                        new sldElement("Fill", null, null, 0, 1)}, 1, 1);
    
        private _Mark() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Mark.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints ){
            Mark symbol = StyleFactoryFinder.createStyleFactory().getDefaultMark();
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[WELLKNOWNNAME].getName().equals(e.getName()))
                    symbol.setWellKnownName((Expression)value[i].getValue());
    
                if(elems[FILL].getName().equals(e.getName()))
                    symbol.setFill((Fill)value[i].getValue());
    
                if(elems[STROKE].getName().equals(e.getName()))
                    symbol.setStroke((Stroke)value[i].getValue());
            }
            
            return symbol;
        }
    }

    static class _NamedLayer extends sldComplexType {
        private static ComplexType instance = new _NamedLayer();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Name", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 1, 1),
                new sldElement("LayerFeatureConstraints", _LayerFeatureConstraints
                        .getInstance(), null, 0, 1),
                new sldElement("NamedStyle", _NamedStyle.getInstance(), null, 1, 1),
                new sldElement("UserStyle", _UserStyle.getInstance(), null, 1, 1)};
        private static int NAME = 0;
        private static int LAYERFEATURECONSTRAINTS = 1;
        private static int NAMEDSTYLE = 2;
        private static int USERSTYLE = 3;
    
        private static ElementGrouping child = new SequenceGT(
                null,
                new ElementGrouping[]{elems[0], elems[1],
                        new ChoiceGT(null, 0, Integer.MAX_VALUE, new ElementGrouping[] { elems[2], elems[3] })},
                1, 1);
    
        private _NamedLayer() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints ){
                NamedLayer sld = new NamedLayerImpl();
            
                for (int i = 0; i < value.length; i++) {
                    if ((value[i] == null) || value[i].getElement() == null) {
                        continue;
                    }
    
                    Element e = value[i].getElement();
                    if(elems[NAME].getName().equals(e.getName()))
                        sld.setName((String)value[i].getValue());
    
                    if(elems[LAYERFEATURECONSTRAINTS].getName().equals(e.getName())) {
                        // ignore
                        continue;
                    }
                    
                    if(elems[NAMEDSTYLE].getName().equals(e.getName()))
                        sld.addStyle((NamedStyle)value[i].getValue());
                    
                    if(elems[USERSTYLE].getName().equals(e.getName()))
                        sld.addStyle((Style)value[i].getValue());
                }
                
                return sld;
        }
    }

    static class _NamedStyle extends sldComplexType {
        private static ComplexType instance = new _NamedStyle();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{new sldElement(
                "Name",
                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance()/* simpleType name is string */,
                null, 1, 1)};
        
        private static int NAME = 0;
    
        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{new sldElement("Name",
                        org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 1, 1)}, 1, 1);
    
        private _NamedStyle() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints ){
                NamedStyle sld = new NamedStyleImpl();
            
                for (int i = 0; i < value.length; i++) {
                    if ((value[i] == null) || value[i].getElement() == null) {
                        continue;
                    }
    
                    Element e = value[i].getElement();
                    if(elems[NAME].getName().equals(e.getName()))
                        sld.setName((String)value[i].getValue());
                }
                
                return sld;
        }
    }

    static class _Normalize extends sldComplexType {
        private static ComplexType instance = new _Normalize();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = null;
        private static ElementGrouping child = new SequenceGT(null);
    
        private _Normalize() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         * @throws SAXException 
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _OnlineResource extends sldComplexType {
        private static ComplexType instance = new _OnlineResource();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = XLinkSchema.SimpleLink.getInstance().getAttributes();
    
        private static Element[] elems = null;
        private static ElementGrouping child = new SequenceGT(null);
    
        private _OnlineResource() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return URL.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws SAXException {
        	String href = attrs1.getValue("",attrs[0].getName());
        	if(href == null || "".equals(href)){
        		href = attrs1.getValue(attrs[0].getNamespace().toString(),attrs[0].getName());
        	}
            try {
				return new URL(href);
			} catch (MalformedURLException e) {
				SAXException ee = new SAXException(e.getMessage());
				ee.initCause(e);
				throw ee;
			}
        }
    }

    static class _OverlapBehavior extends sldComplexType {
        private static ComplexType instance = new _OverlapBehavior();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("LATEST_ON_TOP", _LATEST_ON_TOP.getInstance(), null,
                        1, 1),
                new sldElement("EARLIEST_ON_TOP", _EARLIEST_ON_TOP.getInstance(),
                        null, 1, 1),
                new sldElement("AVERAGE", _AVERAGE.getInstance(), null, 1, 1),
                new sldElement("RANDOM", _RANDOM.getInstance(), null, 1, 1)};
        
        private static int LATESTONTOP = 0;
        private static int EARLIESTONTOP = 1;
        private static int AVERAGE = 2;
        private static int RANDOM = 3;
    
        private static ElementGrouping child = new ChoiceGT(elems);
    
        private _OverlapBehavior() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _PointPlacement extends sldComplexType {
        private static ComplexType instance = new _PointPlacement();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("AnchorPoint", _AnchorPoint.getInstance(), null, 0,
                        1),
                new sldElement("Displacement", _Displacement.getInstance(), null,
                        0, 1),
                new sldElement("Rotation", ParameterValueType.getInstance(), null,
                        0, 1)};
    
        private static ElementGrouping child = new SequenceGT(null, new ElementGrouping[]{
                new sldElement("AnchorPoint", _AnchorPoint.getInstance(), null, 0,
                        1),
                new sldElement("Displacement", _Displacement.getInstance(), null,
                        0, 1),
                new sldElement("Displacement", _Displacement.getInstance(), null,
                        0, 1)}, 1, 1);
        
        private static int ANCHOROINT = 0;
        private static int DISPLACEMENT = 1;
        private static int ROTATION = 2;
    
        private _PointPlacement() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _PointSymbolizer extends sldComplexType {
        private static ComplexType instance = new _PointSymbolizer();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Geometry", _Geometry.getInstance(), null, 0, 1),
                new sldElement("Graphic", _Graphic.getInstance(), null, 0, 1)};
        
        // array positions
        private static int GEOMETRY = 0;
        private static int GRAPHIC = 1;
        
        private static ElementGrouping child = new SequenceGT(elems);
    
        private _PointSymbolizer() {
            super(null, child, attrs, elems, SymbolizerType.getInstance(), false,
                    false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return PointSymbolizer.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException {
            PointSymbolizer symbol = StyleFactoryFinder.createStyleFactory().getDefaultPointSymbolizer();
            // symbol.setGraphic(null);
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[GEOMETRY].getName().equals(e.getName()))
                    symbol.setGeometryPropertyName((String)value[i].getValue());
    
                if(elems[GRAPHIC].getName().equals(e.getName()))
                    symbol.setGraphic((Graphic)value[i].getValue());
            }
            
            return symbol;
        }
    }

    static class _PolygonSymbolizer extends sldComplexType {
        private static ComplexType instance = new _PolygonSymbolizer();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Geometry", _Geometry.getInstance(), null, 0, 1),
                new sldElement("Fill", _Fill.getInstance(), null, 0, 1),
                new sldElement("Stroke", _Stroke.getInstance(), null, 0, 1)};
        
        // array positions
        private static int GEOMETRY = 0;
        private static int FILL = 1;
        private static int STROKE = 2;
    
        private static ElementGrouping child = new SequenceGT(elems);
    
        private _PolygonSymbolizer() {
            super(null, child, attrs, elems, SymbolizerType.getInstance(), false,
                    false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return PolygonSymbolizer.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException {
            PolygonSymbolizer symbol = StyleFactoryFinder.createStyleFactory().getDefaultPolygonSymbolizer();
            // symbol.setGraphic(null);
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[GEOMETRY].getName().equals(e.getName()))
                    symbol.setGeometryPropertyName((String)value[i].getValue());
    
                if(elems[FILL].getName().equals(e.getName()))
                    symbol.setFill((Fill)value[i].getValue());
    
                if(elems[STROKE].getName().equals(e.getName()))
                    symbol.setStroke((Stroke)value[i].getValue());
            }
            
            return symbol;
        }
    }

    static class _RANDOM extends sldComplexType {
        private static ComplexType instance = new _RANDOM();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = null;
        private static ElementGrouping child = new SequenceGT(null);
    
        private _RANDOM() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _RasterSymbolizer extends sldComplexType {
        private static ComplexType instance = new _RasterSymbolizer();
        public static ComplexType getInstance() {
            return instance;
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return RasterSymbolizer.class;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Geometry", _Geometry.getInstance(), null, 0, 1),
                new sldElement("Opacity", ParameterValueType.getInstance(), null,
                        0, 1),
                new sldElement("ChannelSelection", _ChannelSelection.getInstance(),
                        null, 0, 1),
                new sldElement("OverlapBehavior", _OverlapBehavior.getInstance(),
                        null, 0, 1),
                new sldElement("ColorMap", _ColorMap.getInstance(), null, 0, 1),
                new sldElement("ContrastEnhancement", _ContrastEnhancement
                        .getInstance(), null, 0, 1),
                new sldElement("ShadedRelief", _ShadedRelief.getInstance(), null,
                        0, 1),
                new sldElement("ImageOutline", _ImageOutline.getInstance(), null,
                        0, 1)};
        
        // array positions
        private static int GEOMETRY = 0;
        private static int OPACITY = 1;
        private static int CHANNELSELECTION = 2;
        private static int OVERLAPBEHAVIOR = 3;
        private static int COLORMAP = 4;
        private static int CONTRASTENHANCEMENT = 5;
        private static int SHADEDRELIEF = 6;
        private static int IMAGEOUTLINE = 7;
    
        private static ElementGrouping child = new SequenceGT(elems);
    
        private _RasterSymbolizer() {
            super(null, child, attrs, elems, SymbolizerType.getInstance(), false,
                    false);
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException {
            RasterSymbolizer symbol = StyleFactoryFinder.createStyleFactory().getDefaultRasterSymbolizer();
            // symbol.setGraphic(null);
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[GEOMETRY].getName().equals(e.getName()))
                    symbol.setGeometryPropertyName((String)value[i].getValue());
    
                if(elems[OPACITY].getName().equals(e.getName()))
                    symbol.setOpacity((Expression)value[i].getValue());
    
                if(elems[CHANNELSELECTION].getName().equals(e.getName()))
                    symbol.setChannelSelection((ChannelSelection)value[i].getValue());
    
                if(elems[OVERLAPBEHAVIOR].getName().equals(e.getName()))
                    symbol.setOverlap((Expression)value[i].getValue());
    
                if(elems[COLORMAP].getName().equals(e.getName()))
                    symbol.setColorMap((ColorMap)value[i].getValue());
    
                if(elems[CONTRASTENHANCEMENT].getName().equals(e.getName()))
                    symbol.setContrastEnhancement((ContrastEnhancement)value[i].getValue());
    
                if(elems[SHADEDRELIEF].getName().equals(e.getName()))
                    symbol.setShadedRelief((ShadedRelief)value[i].getValue());
    
                if(elems[IMAGEOUTLINE].getName().equals(e.getName()))
                    symbol.setImageOutline((Symbolizer)value[i].getValue());
            }
            
            return symbol;
        }
    }

    static class _RemoteOWS extends sldComplexType {
        private static ComplexType instance = new _RemoteOWS();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Service", sldSimpleTypes._Service.getInstance(), null, 1, 1),
                new sldElement("OnlineResource", _OnlineResource.getInstance(),
                        null, 1, 1)};
        
        private static int SERVICE = 0;
        private static int ONLINERESOURCE = 1;
    
        private static ElementGrouping child = new SequenceGT(null, new ElementGrouping[]{
                new sldElement("Service", sldSimpleTypes._Service.getInstance(), null, 1, 1),
                new sldElement("OnlineResource", _OnlineResource.getInstance(),
                        null, 1, 1)}, 1, 1);
    
        private _RemoteOWS() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _Rule extends sldComplexType {
        private static ComplexType instance = new _Rule();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Name", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("Title", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("Abstract", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("LegendGraphic", _LegendGraphic.getInstance(), null,
                        0, 1),
                new sldElement("Filter", org.geotools.xml.filter.FilterOpsComplexTypes.FilterType
                        .getInstance()/* complexType name is FilterType */, null, 1, 1),
                new sldElement("ElseFilter", _ElseFilter.getInstance(), null, 1, 1),
                new sldElement("MinScaleDenominator", org.geotools.xml.xsi.XSISimpleTypes.Double
                        .getInstance()/* simpleType name is double */, null, 0, 1),
                new sldElement("MaxScaleDenominator", org.geotools.xml.xsi.XSISimpleTypes.Double
                        .getInstance()/* simpleType name is double */, null, 0, 1),
                new sldElement("Symbolizer", SymbolizerType.getInstance(), null, 1,
                        Element.UNBOUNDED)};
        
        private static int NAME = 0;
        private static int TITLE = 1;
        private static int ABSTRACT = 2;
        private static int LEGENDGRAPHIC = 3;
        private static int FILTER = 4;
        private static int ELSEFILTER = 5;
        private static int MINSCALEDENOMINATOR = 6;
        private static int MAXSCALEDENOMINATOR = 7;
        private static int SYMBOLIZER = 8;
    
        private static ElementGrouping child = new SequenceGT(
                new ElementGrouping[]{
                        elems[0],
                        elems[1],
                        elems[2],
                        elems[3],
                        new ChoiceGT(new ElementGrouping[] { elems[4], elems[5] }),
                        elems[6],
                        elems[7],
                        elems[8]
                }
            );
    
        private _Rule() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _ShadedRelief extends sldComplexType {
        private static ComplexType instance = new _ShadedRelief();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("BrightnessOnly", org.geotools.xml.xsi.XSISimpleTypes.Boolean
                        .getInstance()/* simpleType name is boolean */, null, 0, 1),
                new sldElement("ReliefFactor", org.geotools.xml.xsi.XSISimpleTypes.Double
                        .getInstance()/* simpleType name is double */, null, 0, 1)};
        
        private static int BRIGHNESSONLY = 0;
        private static int RELEIFFACTOR = 1;
    
        private static ElementGrouping child = new SequenceGT(null, new ElementGrouping[]{
                new sldElement("BrightnessOnly", org.geotools.xml.xsi.XSISimpleTypes.Boolean
                        .getInstance()/* simpleType name is boolean */, null, 0, 1),
                new sldElement("ReliefFactor", org.geotools.xml.xsi.XSISimpleTypes.Double
                        .getInstance()/* simpleType name is double */, null, 0, 1)}, 1, 1);
    
        private _ShadedRelief() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class _Stroke extends sldComplexType {
        private static ComplexType instance = new _Stroke();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("GraphicFill", null, null, 1, 1),
                new sldElement("GraphicStroke", _GraphicStroke.getInstance(), null,
                        1, 1),
                new sldElement("CssParameter", _CssParameter.getInstance(), null,
                        0, Element.UNBOUNDED)};
    
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Stroke.class;
        }
        private static int GRAPHICFILL = 0;
        private static int GRAPHICSTROKE = 1;
        private static int CSSPARAMETER = 2; 
    
        private static ElementGrouping child = new SequenceGT(
                new ElementGrouping[]{
                        new ChoiceGT(null, 0, 1, new ElementGrouping[]{
                                elems[0],
                                elems[1],
                        }),
                        elems[2]
                });
    
        private _Stroke() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException {
            Stroke symbol = StyleFactoryFinder.createStyleFactory().getDefaultStroke();
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[GRAPHICFILL].getName().equals(e.getName()))
                    symbol.setGraphicFill((Graphic)value[i].getValue());
    
                if(elems[GRAPHICSTROKE].getName().equals(e.getName()))
                    symbol.setGraphicStroke((Graphic)value[i].getValue());
    
                if(elems[CSSPARAMETER].getName().equals(e.getName())){
                    Expression css = (Expression)value[i].getValue();
                    // TODO apply the css
                }
            }
            
            return symbol;
        }
    }

    static class _StyledLayerDescriptor extends sldComplexType {
        private static ComplexType instance = new _StyledLayerDescriptor();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = new Attribute[]{new AttributeGT(null, "version",
                sldSchema.NAMESPACE, org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                Attribute.REQUIRED, null, null, false)};
    
        private static Element[] elems = new Element[]{
                new sldElement("Name", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        null, 0, 1),
                new sldElement("Title", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        null, 0, 1),
                new sldElement("Abstract",
                        org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("NamedLayer", _NamedLayer.getInstance(), null, 0, Integer.MAX_VALUE),
                new sldElement("UserLayer", _UserLayer.getInstance(), null, 0, Integer.MAX_VALUE)};
        
        private static int NAME = 0;
        private static int TITLE = 1;
        private static int ABSTRACT = 2;
        private static int NAMEDLAYER = 3;
        private static int USERLAYER = 4;
    
        private static ElementGrouping child = new SequenceGT(
                new ElementGrouping[]{
                        elems[0],
                        elems[1],
                        elems[2],
                        new ChoiceGT(null, 0, Integer.MAX_VALUE, new ElementGrouping[] { elems[3], elems[4] })
                });
    
        private _StyledLayerDescriptor() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return StyledLayerDescriptor.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            StyledLayerDescriptor sld = StyleFactoryFinder.createStyleFactory().createStyledLayerDescriptor();
    
                for (int i = 0; i < value.length; i++) {
                    if ((value[i] == null) || value[i].getElement() == null) {
                        continue;
                    }
    
                    Element e = value[i].getElement();
                    if(elems[NAME].getName().equals(e.getName()))
                        sld.setName((String)value[i].getValue());
    
                    if(elems[TITLE].getName().equals(e.getName()))
                        sld.setTitle((String)value[i].getValue());
                    
                    if(elems[ABSTRACT].getName().equals(e.getName()))
                        sld.setAbstract((String)value[i].getValue());
                    
                    if(elems[NAMEDLAYER].getName().equals(e.getName()))
                        sld.addStyledLayer((StyledLayerImpl)value[i].getValue());
                    
                    if(elems[USERLAYER].getName().equals(e.getName()))
                        sld.addStyledLayer((StyledLayerImpl)value[i].getValue());
                }
                
                return sld;
        }
    }

    static class _TextSymbolizer extends sldComplexType {
        private static ComplexType instance = new _TextSymbolizer();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Geometry", _Geometry.getInstance(), null, 0, 1),
                new sldElement("Label", ParameterValueType.getInstance(), null, 0,
                        1),
                new sldElement("Font", _Font.getInstance(), null, 0, 1),
                new sldElement("LabelPlacement", _LabelPlacement.getInstance(),
                        null, 0, 1),
                new sldElement("Halo", _Halo.getInstance(), null, 0, 1),
                new sldElement("Fill", _Fill.getInstance(), null, 0, 1)};
    
        // array positions
        private static int GEOMETRY = 0;
        private static int LABEL = 1;
        private static int FONT = 2;
        private static int LABELPLACEMENT = 3;
        private static int HALO = 4;
        private static int FILL = 5;
        
        private static ElementGrouping child = new SequenceGT(elems);
    
        private _TextSymbolizer() {
            super(null, child, attrs, elems, SymbolizerType.getInstance(), false,
                    false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return TextSymbolizer.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException {
            TextSymbolizer symbol = StyleFactoryFinder.createStyleFactory().createTextSymbolizer();
            symbol.setFill(null);
            
            ArrayList fonts = new ArrayList();
            
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
                Element e = value[i].getElement();
                if(elems[GEOMETRY].getName().equals(e.getName()))
                    symbol.setGeometryPropertyName((String)value[i].getValue());
    
                if(elems[FILL].getName().equals(e.getName()))
                    symbol.setFill((Fill)value[i].getValue());
    
                if(elems[LABEL].getName().equals(e.getName()))
                    symbol.setLabel((Expression)value[i].getValue());
    
                if(elems[FONT].getName().equals(e.getName()))
                    fonts.add(value[i].getValue());
    
                if(elems[LABELPLACEMENT].getName().equals(e.getName()))
                    symbol.setFill((Fill)value[i].getValue());
    
                if(elems[LABELPLACEMENT].getName().equals(e.getName()))
                    symbol.setLabelPlacement((LabelPlacement)value[i].getValue());
    
                if(elems[HALO].getName().equals(e.getName()))
                    symbol.setHalo((Halo)value[i].getValue());
            }
            
            symbol.setFonts((Font[]) fonts.toArray(new Font[0]));
            
            return symbol;
        }
    }

    static class _UserLayer extends sldComplexType {
        private static ComplexType instance = new _UserLayer();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Name", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("RemoteOWS", _RemoteOWS.getInstance(), null, 0, 1),
                new sldElement("LayerFeatureConstraints", _LayerFeatureConstraints
                        .getInstance(), null, 1, 1),
                new sldElement("UserStyle", _UserStyle.getInstance(), null, 1,
                        Element.UNBOUNDED)};
        
        private static int NAME = 0;
        private static int REMOTEOWS = 1;
        private static int LAYERFEATURECONSTRAINTS = 2;
        private static int USERSTYLE = 3;
    
        private static ElementGrouping child = new SequenceGT(null,
                elems, 1, 1);
    
        private _UserLayer() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return UserLayer.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
                UserLayer sld = new UserLayerImpl();
            
                for (int i = 0; i < value.length; i++) {
                    if ((value[i] == null) || value[i].getElement() == null) {
                        continue;
                    }
    
                    Element e = value[i].getElement();
                    if(elems[NAME].getName().equals(e.getName()))
                        sld.setName((String)value[i].getValue());
    
                    if(elems[REMOTEOWS].getName().equals(e.getName()))
                        sld.setRemoteOWS((RemoteOWS)value[i].getValue());
                    
                    if(elems[LAYERFEATURECONSTRAINTS].getName().equals(e.getName()))
                        sld.setLayerFeatureConstraints((FeatureTypeConstraint[])value[i].getValue());
                    
                    if(elems[USERSTYLE].getName().equals(e.getName()))
                        sld.addUserStyle((Style)value[i].getValue());
                }
                
                return sld;
        }
    }

    static class _UserStyle extends sldComplexType {
        private static ComplexType instance = new _UserStyle();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("Name", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("Title", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("Abstract", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(), null, 0, 1),
                new sldElement("IsDefault", org.geotools.xml.xsi.XSISimpleTypes.Boolean
                        .getInstance()/* simpleType name is boolean */, null, 0, 1),
                new sldElement("FeatureTypeStyle", _FeatureTypeStyle.getInstance(),
                        null, 1, Element.UNBOUNDED)};
        
        private static int NAME = 0;
        private static int TITLE = 1;
        private static int ABSTRACT = 2;
        private static int ISDEFAULT = 3;
        private static int FEATURETYPESTYLE = 4;
    
        private static ElementGrouping child = new SequenceGT(elems);
    
        private _UserStyle() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
            // TODO fill me in
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            return super.getValue(element, value, attrs1, hints);
            // TODO fill me in
        }
    }

    static class ParameterValueType extends sldComplexType {
        private static ComplexType instance = new ParameterValueType();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{new sldElement("expression",
                org.geotools.xml.filter.FilterComplexTypes.ExpressionType.getInstance(), null, 1, 1)};
        
        private static int EXPRESSION = 0;
    
        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{new sldElement("expression",
                        org.geotools.xml.filter.FilterComplexTypes.ExpressionType.getInstance(), null,
                        1, 1)}, 0, Element.UNBOUNDED);
    
        private ParameterValueType() {
            super("ParameterValueType", child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return elems[EXPRESSION].getType().getInstanceType();
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints ){
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[EXPRESSION].getName().equals(e.getName()))
                    return value[i].getValue(); // TODO check this?
            }
            
            return null;
        }
    }

    static class SelectedChannelType extends sldComplexType {
        private static ComplexType instance = new SelectedChannelType();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new sldElement("SourceChannelName", org.geotools.xml.xsi.XSISimpleTypes.String
                        .getInstance()/* simpleType name is string */, null, 1, 1),
                new sldElement("ContrastEnhancement", _ContrastEnhancement
                        .getInstance(), null, 0, 1)};
    
        private static ElementGrouping child = new SequenceGT(null, new ElementGrouping[]{
                new sldElement("SourceChannelName", org.geotools.xml.xsi.XSISimpleTypes.String
                        .getInstance()/*
                                         * simpleType name is string
                                         */, null, 1, 1),
                new sldElement("ContrastEnhancement", _ContrastEnhancement
                        .getInstance(), null, 0, 1)}, 1, 1);
        
        private static int SOURCECHANNELNAME = 0;
        private static int CONTRASTENHANCEMENT = 1;
    
        private SelectedChannelType() {
            super("SelectedChannelType", child, attrs, elems, null, false, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return org.geotools.styling.SelectedChannelType.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return super.canEncode(element, value, hints);
            // TODO fill me in
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            super.encode(element, value, output, hints);
            // TODO fill me in
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints ){
            org.geotools.styling.SelectedChannelType symbol = new org.geotools.styling.SelectedChannelTypeImpl();
    
            for (int i = 0; i < value.length; i++) {
                if ((value[i] == null) || value[i].getElement() == null) {
                    continue;
                }
    
                Element e = value[i].getElement();
                if(elems[SOURCECHANNELNAME].getName().equals(e.getName()))
                    symbol.setChannelName((String)value[i].getValue());
    
                if(elems[CONTRASTENHANCEMENT].getName().equals(e.getName()))
                    symbol.setContrastEnhancement((Expression)value[i].getValue());
            }
            
            return symbol;
        }
    }

    static class SymbolizerType extends sldComplexType {
        private static ComplexType instance = new SymbolizerType();
        public static ComplexType getInstance() {
            return instance;
        }
    
        private static Attribute[] attrs = null;
        private static Element[] elems = null;
        private static ElementGrouping child = new SequenceGT(null);
    
        private SymbolizerType() {
            super("SymbolizerType", child, attrs, elems, null, true, false);
        }
        
        /**
         * 
         *  getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Symbolizer.class;
        }
        
        
        /**
         *  canEncode ...
         * 
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         * @param element
         * @param value
         * @param hints
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            // abstract type ...
            return super.canEncode(element, value, hints);
        }
        /**
         *  encode ...
         * 
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         * @param element
         * @param value
         * @param output
         * @param hints
         * @throws OperationNotSupportedException
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            // abstract type ...
            super.encode(element, value, output, hints);
        }
        /**
         *  getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs1
         * @param hints
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs1, Map hints )
                throws OperationNotSupportedException, SAXException {
            // abstract type ...
            return super.getValue(element, value, attrs1, hints);
        }
    }

}
