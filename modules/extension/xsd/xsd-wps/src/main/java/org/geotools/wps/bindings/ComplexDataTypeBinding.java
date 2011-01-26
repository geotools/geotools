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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.Wps10Factory;

import org.geotools.wps.WPS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

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
}
