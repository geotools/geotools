package org.geotools.geopkg.wps.parser;


import org.geotools.geopkg.wps.parser.GPKG;
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