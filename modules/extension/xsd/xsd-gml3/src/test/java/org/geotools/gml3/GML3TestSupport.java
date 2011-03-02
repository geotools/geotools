/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3;

import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;
import org.w3c.dom.Element;

/*
 * Test bindings by extending this class with test cases that follow this pattern:
 * 
 *  Debugging hints:
 *  
 *  XMLTestSupport has javadoc on how things work.
 *  TODO - add hints for eclipse breakpoint debugging. 
 *  
 *  // parse using the binding - 
 *  // NB should have separate tests for all the type bindings supported
 *  // test methods must start with "test"
 *  
 *    test1D() throws Exception {
 *  
 *       // create a DOM representation
 *    
 *       GML3MockData.element(GML.pos, document, document);
 *       document.getDocumentElement().appendChild(document.createTextNode("1.0"));
 *  
 *  	 //  then parse (using standard XMLTestSupport parse method)
 *      
 *       DirectPosition pos = (DirectPosition) parse();
 * 
 *       // test aspects of the result
 *       
 *       assertNotNull(pos);
 *       assertTrue(pos instanceof DirectPosition1D);
 *       assertEquals(pos.getOrdinate(0), 1.0, 0);
 *    }
 *        
 *    // test encodings with something like this:
 *    
 *    public void testEncode() throws Exception {
 *        Document dom = encode(GML3MockData.bounds(), GML.Envelope);
 *    
 *        // debugging method:
 *        print(dom);
 *        
 *        assertEquals("something", dom.getElementsByTagNameNS(GML.NAMESPACE, "lowerCorner").getLength());
 *
 */
public abstract class GML3TestSupport extends XMLTestSupport {
    protected void registerNamespaces(Element root) {
        super.registerNamespaces(root);
        root.setAttribute("xmlns:gml", "http://www.opengis.net/gml");
    }

    /* 
     * binds to the GMLConfiguration in the current package 
     * i.e. this is a GML3 specific binding configuration. 
     */
    protected Configuration createConfiguration() {
        return new GMLConfiguration(enableExtendedArcSurfaceSupport());
    }
    
    /*
     * To be overriden by subclasses that require the extended arc/surface bindings
     * enabled. 
     */
    protected boolean enableExtendedArcSurfaceSupport() {
        return false;
    }
}
