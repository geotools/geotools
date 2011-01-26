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
package org.geotools.styling;

import java.io.StringReader;

import junit.framework.TestCase;

import org.geotools.test.TestData;


/**
 * Tests XMLNS attributes serialization that might be missed/ignored if the proper
 * namespace is not specified. Parsing  SLD into object tree, serialization back to
 * XML and again parsing from this XML says that there is no problem.
 * 
 * @author Vitalus
 *
 *
 * @source $URL$
 */
public class XmlnsNamespaceTest extends TestCase {
    
    
    public void testXmlnsNamespaceOutput() throws Exception{
        
        java.net.URL sldUrl = TestData.getResource(this, "xmlnsNamespaces.sld");
        SLDParser parser = new SLDParser(new StyleFactoryImpl(), sldUrl);
        Style style = parser.readXML()[0];
        
        
        SLDTransformer transformer = new SLDTransformer();
        transformer.setNamespaceDeclarationEnabled(true);
//        transformer.setIndentation(2);
        String xml = transformer.transform(style);
//        System.out.println(xml);
        
        try{
            SLDParser parser2 = new SLDParser(new StyleFactoryImpl(),new StringReader(xml));
            Style style2 = parser2.readXML()[0];
            
        }catch(Exception exc){
            this.fail("Failed to persist object tree to XML and parse back: "+exc.getMessage());
            throw exc;
        }


    }

}
