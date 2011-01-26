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
package org.geotools.data.wms.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.data.ows.LayerDescription;
import org.geotools.data.wms.xml.WMSSchema.WMSAttribute;
import org.geotools.data.wms.xml.WMSSchema.WMSComplexType;
import org.geotools.data.wms.xml.WMSSchema.WMSElement;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 *
 * @source $URL$
 */
public class WMSDescribeLayerTypes {
    public static class WMS_DescribeLayerResponse extends WMSSchema.WMSComplexType {
        private static WMSComplexType instance = new WMS_DescribeLayerResponse();
        
        public static WMSComplexType getInstance() {
            return instance;
        }
        
        private static Element[] elems = new Element[] {
                new WMSElement("LayerDescription", _LayerDescription.getInstance(), 0, Integer.MAX_VALUE)
        };
        
        private static Sequence seq = new SequenceGT(elems);
        
        private static Attribute[] attrs = new Attribute[] {
                new WMSAttribute("version", XSISimpleTypes.String.getInstance())
        };

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs, Map hints ) throws SAXException, OperationNotSupportedException {
            LayerDescription[] layerDescs = new LayerDescription[value.length];
            
            for (int i = 0; i < value.length; i++) {
                layerDescs[i] = (LayerDescription) value[i].getValue();
            }
            
            return layerDescs;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WMS_DescribeLayerResponse"; //$NON-NLS-1$
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LayerDescription[].class;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints ) throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }
    
    public static class _LayerDescription extends WMSComplexType {
        private static WMSComplexType instance = new _LayerDescription();
        
        public static WMSComplexType getInstance() {
            return instance;
        }
        
        private static Element[] elems = new Element[] {
                new WMSElement("Query", _Query.getInstance(), 0, Integer.MAX_VALUE)
        };
        
        private static Sequence seq = new SequenceGT(elems);
        
        private static Attribute[] attrs = new Attribute[] {
                new WMSAttribute(null, "name", WMSSchema.NAMESPACE, XSISimpleTypes.String.getInstance(), Attribute.REQUIRED, null, null, false),
                new WMSAttribute("wfs", XSISimpleTypes.String.getInstance()),
                new WMSAttribute("owsType", XSISimpleTypes.String.getInstance()),
                new WMSAttribute("owsURL", XSISimpleTypes.String.getInstance())
        };

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs, Map hints ) throws SAXException, OperationNotSupportedException {

            LayerDescription layerDesc = new LayerDescription();
            String[] queries = new String[value.length];
            
            for (int i = 0; i < value.length; i++) {
                queries[i] = (String) value[i].getValue();
            }
            
            layerDesc.setQueries(queries);
            
            String name = attrs.getValue("name");
            layerDesc.setName(name);
            
            String owsType = attrs.getValue("owsType");
            layerDesc.setOwsType(owsType);
            
            try {
                URL wfs = new URL(attrs.getValue("wfs"));
                layerDesc.setWfs(wfs);
            } catch (MalformedURLException e) {
            }
            
            try {
                URL owsURL = new URL(attrs.getValue("owsURL"));
                layerDesc.setOwsURL(owsURL);
            } catch (MalformedURLException e) {
            }
            
            return layerDesc;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LayerDescription";
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LayerDescription.class;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints ) throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }
    
    public static class _Query extends WMSComplexType {
        private static WMSComplexType instance = new _Query();
        public static WMSComplexType getInstance() {
            return instance;
        }
        
        public static Attribute[] attrs = new Attribute[] {
            new WMSAttribute(null, "typeName", WMSSchema.NAMESPACE, XSISimpleTypes.String.getInstance(), Attribute.REQUIRED, null, null, false)
        };
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs, Map hints ) throws SAXException, OperationNotSupportedException {
            return attrs.getValue("typeName");
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Query";
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode( Element element, Object value, Map hints ) {
            return false;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode( Element element, Object value, PrintHandler output, Map hints ) throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }
}
