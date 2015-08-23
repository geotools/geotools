/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geotools.xs.XS;

/**
 * 
 *
 * @source $URL$
 */
public class SchemasTest extends TestCase {

    File tmp,sub;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        tmp = File.createTempFile("schemas", "xsd");
        tmp.delete();
        tmp.mkdir();
        tmp.deleteOnExit();

        sub = new File( tmp, "sub" );
        sub.mkdir();
        sub.deleteOnExit();
        
        File f = new File( tmp, "root.xsd" );
        String xsd = 
            "<xsd:schema xmlns='http://geotools.org/test' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/test'> " + 
                "<xsd:import namespace='http://geotools/org/import1' " + 
                    "schemaLocation='import1.xsd'/>" + 
                "<xsd:import namespace='http://geotools/org/import2' " + 
                    "schemaLocation='import2.xsd'/>" +
                "<xsd:include location='include1.xsd'/>" +
                "<xsd:include location='include2.xsd'/>" +
            "</xsd:schema>";
        write( f, xsd );
        
        f = new File( tmp, "import1.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/import1' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/import1'> " + 
            "</xsd:schema>";
        write( f , xsd );
        
        f = new File( sub, "import2.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/import2' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/import2'> " + 
            "</xsd:schema>";
        write( f , xsd );
        
        f = new File( tmp, "include1.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/test' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/test'> " + 
            "</xsd:schema>";
        write( f, xsd );
        
        f = new File( sub, "include2.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/test' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/test'> " + 
            "</xsd:schema>";
        write( f, xsd );

        System.setProperty(Schemas.FORCE_SCHEMA_IMPORT, "false");
    }
    
    void write( File f, String xsd ) throws IOException {
        f.deleteOnExit();
        f.createNewFile();
        FileWriter w = new FileWriter( f );
        w.write( xsd );
        w.flush();
        w.close();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        new File( tmp, "root.xsd" ).delete();
        new File( tmp, "import1.xsd" ).delete();
        new File( sub, "import2.xsd" ).delete();
        new File( tmp, "include1.xsd" ).delete();
        new File( sub, "include2.xsd" ).delete();
        
        sub.delete();
        tmp.delete();

        System.setProperty(Schemas.FORCE_SCHEMA_IMPORT, "false");
    }
    
    
    public void testValidateImportsIncludes() throws Exception {
       String location = new File( tmp, "root.xsd").getAbsolutePath();
       List errors = Schemas.validateImportsIncludes( location );
       assertEquals( 2, errors.size() );
    
       SchemaLocationResolver resolver1 = new SchemaLocationResolver(XS.getInstance()) {
         
        public boolean canHandle(XSDSchema schema, String uri, String location) {
            if ( location.endsWith("import2.xsd") ) {
                return true;
            }
            
            return false;
        }
           
         public String resolveSchemaLocation(XSDSchema schema, String uri, String location) {
             return new File( sub, "import2.xsd" ).getAbsolutePath();
         }
       };
       SchemaLocationResolver resolver2 = new SchemaLocationResolver(XS.getInstance()) {
           
           public boolean canHandle(XSDSchema schema, String uri, String location) {
               if ( location.endsWith("include2.xsd") ) {
                   return true;
               }
               
               return false;
           }
              
            public String resolveSchemaLocation(XSDSchema schema, String uri, String location) {
                return new File( sub, "include2.xsd" ).getAbsolutePath();
            }
       };
          
       
      errors = Schemas.validateImportsIncludes( location, null, new XSDSchemaLocationResolver[]{resolver1,resolver2} );
      assertEquals( 0, errors.size() );
      
    }

    /**
     * Tests that element declarations and type definitions from imported schemas are parsed,
     * even if the importing schema itself contains no element nor type.
     * 
     * @throws IOException
     */
    public void testImportsOnly() throws IOException {
        XSDSchema schema = Schemas.parse(Schemas.class.getResource("importFacetsEmpty.xsd").toString());
        assertNotNull(schema);

        boolean elFound = hasElement(schema, "collapsedString");
        assertTrue(elFound);
    }

    /**
     * Tests that system property "org.geotools.xml.forceSchemaImport" is properly taken into account.
     * @throws IOException
     */
    public void testForcedSchemaImport() throws IOException {
        XSDSchema schema = Schemas.parse(Schemas.class.getResource("importFacetsNotEmpty.xsd").toString());
        assertNotNull(schema);

        // importing schema is not empty and system property "org.geotools.xml.forceSchemaImport" is false:
        // elements defined in imported schema should not be found
        boolean elFound = hasElement(schema, "collapsedString");
        assertFalse(elFound);

        // force import of external schemas in any case
        System.setProperty(Schemas.FORCE_SCHEMA_IMPORT, "true");

        schema = Schemas.parse(Schemas.class.getResource("importFacetsNotEmpty.xsd").toString());
        assertNotNull(schema);

        elFound = hasElement(schema, "collapsedString");
        assertTrue(elFound);
    }

    private boolean hasElement(XSDSchema schema, String elQName) {
        boolean elFound = false;
        EList<XSDElementDeclaration> elDeclList = schema.getElementDeclarations();
        for (XSDElementDeclaration elDecl: elDeclList) {
            if (elQName.equals(elDecl.getQName())) {
                elFound = true;
            }
        }

        return elFound;
    }
}
