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


import org.geotools.geopkg.wps.xml.GPKG;
import org.geotools.xml.Binding;

/**
 * Binding test case for http://www.opengis.net/gpkg:gridsettype.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="gridsettype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:choice&gt;
 *        &lt;xs:element name="name" type="xs:string"/&gt;
 *        &lt;xs:element name="grids"&gt;
 *          &lt;xs:complexType name="gridsettype_grids"&gt;
 *            &lt;xs:sequence&gt;
 *              &lt;xs:element maxOccurs="unbounded" name="grid" type="gridtype"/&gt;
 *            &lt;/xs:sequence&gt;
 *          &lt;/xs:complexType&gt;
 *        &lt;/xs:element&gt;
 *      &lt;/xs:choice&gt;
 *    &lt;/xs:complexType&gt; 
 *      
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 */
public class GridsettypeBindingTest extends GPKGTestSupport {

    public void testType() {
        assertEquals(  Object.class, binding( GPKG.gridsettype ).getType() );
    }
    
    public void testExecutionMode() {
        assertEquals( Binding.OVERRIDE, binding( GPKG.gridsettype ).getExecutionMode() );
    }
    
}