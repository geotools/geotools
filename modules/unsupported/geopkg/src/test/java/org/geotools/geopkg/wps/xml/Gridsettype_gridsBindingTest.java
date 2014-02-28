package org.geotools.geopkg.wps.xml;


import java.util.List;

import org.geotools.geopkg.wps.xml.GPKG;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/gpkg:geopkgtype_features.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="geopkgtype_features" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
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
 *      
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 */
public class Gridsettype_gridsBindingTest extends GPKGTestSupport {
    
    public void testType() {
        assertEquals(  List.class, binding( GPKG.gridsettype_grids ).getType() );
    }
    
    public void testExecutionMode() {
        assertEquals( Binding.OVERRIDE, binding( GPKG.gridsettype_grids ).getExecutionMode() );
    }    
    
}