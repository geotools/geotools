/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.wps.bindings;

import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.Wps10Factory;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.geotools.wps.WPS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.EMFUtils;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.EncoderDelegate;
import org.geotools.xml.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

/**
 *&lt;complexType name="ComplexDataType" mixed="true">
 *   &lt;annotation>
 *      &lt;documentation>Complex data (such as an image), including a definition of the complex value data structure (i.e., schema, format, and encoding).  May be an ows:Manifest data structure.&lt;/documentation>
 *   &lt;/annotation>
 *   &lt;complexContent mixed="true">
 *     &lt;extension base="anyType">
 *        &lt;attributeGroup ref="wps:ComplexDataEncoding"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 *
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 *
 * @source $URL$
 */
public class ComplexDataTypeBinding extends AbstractComplexBinding
{
    private Wps10Factory factory;

    public ComplexDataTypeBinding(Wps10Factory factory)
    {
        this.factory = factory;
    }
    
    public QName getTarget()
    {
        return WPS.ComplexDataType;
    }

    public Class<?> getType()
    {
        return ComplexDataType.class;
    }

    public int getExecutionMode() {
        return OVERRIDE;
    }
    
    
    public Object getProperty(Object object, QName name) throws Exception {
        ComplexDataType data = (ComplexDataType) object;
        
        if ( "schema".equals( name.getLocalPart() ) ) {
            return data.getSchema();
        }
        
        if ( "mimeType".equals( name.getLocalPart() ) ) {
            return data.getMimeType();
        }
        
        if ( "encoding".equals( name.getLocalPart() ) ) {
            return data.getEncoding();
        }
        
        return super.getProperty(object, name);
    }
    
    /*
        NodeImpl -> JTS.Polygon
    */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception
    {
        ComplexDataType data = factory.createComplexDataType();
        
        if ( node.hasAttribute( "schema" ) ) {
            data.setSchema( node.getAttributeValue( "schema").toString() );
        }
        if ( node.hasAttribute( "mimeType" ) ) {
            data.setMimeType( node.getAttributeValue( "mimeType").toString() );
        }
        if ( node.hasAttribute( "encoding" ) ) {
            data.setEncoding( node.getAttributeValue( "encoding").toString() );
        }
        
        for ( Iterator i = node.getChildren().iterator(); i.hasNext(); ) {
            Node c = (Node) i.next();
            data.getData().add( c.getValue() );
        }
        
        return data;
    }
    
    @Override
    public Element encode(Object object, Document document, Element value)
                    throws Exception {
            EObject eobject = (EObject) object;
            if (EMFUtils.has(eobject, "data")) {
                    Object v = EMFUtils.get(((EObject) object), "data");
                    if (v != null && v instanceof EList) {
                            EList data = (EList) v;
                            if (!data.isEmpty() && data.get(0) instanceof EncoderDelegate) {
                                    EncoderDelegate encoder = (EncoderDelegate) data.get(0);
                                    StringWriter output = new StringWriter();
                                    Transformer transformer = ((SAXTransformerFactory)SAXTransformerFactory.newInstance()).newTransformer();
                                    SerializationHandler outputHandler = ((TransformerImpl)transformer).getOutputHandler(new StreamResult(output));
                                    encoder.encode(outputHandler);
                                    
                                    value.appendChild(document.createTextNode(cleanUpHeader(output.toString())));
                            }
                    }
            }
            return value;
    }

    /**
     * Remove <?xml version='1.0' encoding='UTF-8'?> from header
     * leaving the responsibility to the encoder
     *   
     * @param string
     * @return
     */
    private String cleanUpHeader(String content) {
            
            if (content.startsWith("<?xml")) {
                    content = content.substring(content.indexOf(">") + 1);
            }
            
            return content;
    }
}
