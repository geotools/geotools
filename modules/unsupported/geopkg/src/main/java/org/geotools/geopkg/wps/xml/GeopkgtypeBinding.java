/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;


import java.net.URL;

import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gpkg:geopkgtype.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="geopkgtype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element maxOccurs="unbounded" minOccurs="0" name="features"&gt;
 *          &lt;xs:complexType name="geopkgtype_features"&gt;
 *            &lt;xs:complexContent&gt;
 *              &lt;xs:extension base="layertype"&gt;
 *                &lt;xs:sequence&gt;
 *                  &lt;xs:element name="featuretype" type="xs:string"/&gt;
 *                  &lt;xs:element minOccurs="0" name="propertynames" type="xs:string"/&gt;
 *                  &lt;xs:element minOccurs="0" name="filter" type="fes:FilterType"/&gt;
 *                &lt;/xs:sequence&gt;
 *              &lt;/xs:extension&gt;
 *            &lt;/xs:complexContent&gt;
 *          &lt;/xs:complexType&gt;
 *        &lt;/xs:element&gt;
 *        &lt;xs:element maxOccurs="unbounded" minOccurs="0" name="tiles"&gt;
 *          &lt;xs:complexType name="geopkgtype_tiles"&gt;
 *            &lt;xs:complexContent&gt;
 *              &lt;xs:extension base="layertype"&gt;
 *                &lt;xs:sequence&gt;
 *                  &lt;xs:element name="layers" type="xs:string"/&gt;
 *                  &lt;xs:choice&gt;
 *                    &lt;xs:element name="styles" type="xs:string"/&gt;
 *                    &lt;xs:element name="sld" type="xs:string"/&gt;
 *                    &lt;xs:element name="sldbody" type="xs:string"/&gt;
 *                  &lt;/xs:choice&gt;
 *                  &lt;xs:element name="format" type="xs:string"/&gt;
 *                  &lt;xs:element name="bgcolor" type="xs:string"/&gt;
 *                  &lt;xs:element name="transparent" type="xs:boolean"/&gt;
 *                  &lt;xs:element name="gridset" type="gridsettype"/&gt;
 *                  &lt;xs:element name="coverage" type="coveragetype"/&gt;
 *                &lt;/xs:sequence&gt;
 *              &lt;/xs:extension&gt;
 *            &lt;/xs:complexContent&gt;
 *          &lt;/xs:complexType&gt;
 *        &lt;/xs:element&gt;
 *      &lt;/xs:sequence&gt;
 *      &lt;xs:attribute name="name" use="required"/&gt;
 *    &lt;/xs:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class GeopkgtypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GPKG.geopkgtype;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return GeoPackageProcessRequest.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
	    
	    GeoPackageProcessRequest request = new GeoPackageProcessRequest();
	    
	    request.setName((String) node.getAttributeValue("name"));
	    String attributeValue = (String) node.getAttributeValue("path");
	    if(attributeValue != null && !attributeValue.isEmpty()){
	           URL url = new URL(attributeValue);
	            request.setPath(url);
	    }
	    
	    String removeFile = (String) node.getAttributeValue("remove");
            if(removeFile != null && !removeFile.isEmpty()){
                    request.setRemove(Boolean.parseBoolean(removeFile));
            }else{
                request.setRemove(true);
            }
	    for (Object child : node.getChildren()){
	        request.addLayer((GeoPackageProcessRequest.Layer) ((Node) child).getValue());
	    }
	    return request;
	}

}