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
package org.geotools.xsd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import junit.framework.TestCase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geotools.xs.XS;
import org.geotools.xsd.impl.HTTPURIHandler;

/** Tests for {@link org.geotools.xsd.Schemas}. */
public class SchemasTest extends TestCase {

    File tmp, sub;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    protected void setUp() throws Exception {
        super.setUp();

        tmp = File.createTempFile("schemas", "xsd");
        tmp.delete();
        tmp.mkdir();
        tmp.deleteOnExit();

        sub = new File(tmp, "sub");
        sub.mkdir();
        sub.deleteOnExit();

        File f = new File(tmp, "root.xsd");
        String xsd =
                "<xsd:schema xmlns='http://geotools.org/test' "
                        + "xmlns:xsd='http://www.w3.org/2001/XMLSchema' "
                        + "targetNamespace='http://geotools.org/test'> "
                        + "<xsd:import namespace='http://geotools/org/import1' "
                        + "schemaLocation='import1.xsd'/>"
                        + "<xsd:import namespace='http://geotools/org/import2' "
                        + "schemaLocation='import2.xsd'/>"
                        + "<xsd:include location='include1.xsd'/>"
                        + "<xsd:include location='include2.xsd'/>"
                        + "</xsd:schema>";
        write(f, xsd);

        f = new File(tmp, "import1.xsd");
        xsd =
                "<xsd:schema xmlns='http://geotools.org/import1' "
                        + "xmlns:xsd='http://www.w3.org/2001/XMLSchema' "
                        + "targetNamespace='http://geotools.org/import1'> "
                        + "</xsd:schema>";
        write(f, xsd);

        f = new File(sub, "import2.xsd");
        xsd =
                "<xsd:schema xmlns='http://geotools.org/import2' "
                        + "xmlns:xsd='http://www.w3.org/2001/XMLSchema' "
                        + "targetNamespace='http://geotools.org/import2'> "
                        + "</xsd:schema>";
        write(f, xsd);

        f = new File(tmp, "include1.xsd");
        xsd =
                "<xsd:schema xmlns='http://geotools.org/test' "
                        + "xmlns:xsd='http://www.w3.org/2001/XMLSchema' "
                        + "targetNamespace='http://geotools.org/test'> "
                        + "</xsd:schema>";
        write(f, xsd);

        f = new File(sub, "include2.xsd");
        xsd =
                "<xsd:schema xmlns='http://geotools.org/test' "
                        + "xmlns:xsd='http://www.w3.org/2001/XMLSchema' "
                        + "targetNamespace='http://geotools.org/test'> "
                        + "</xsd:schema>";
        write(f, xsd);

        System.setProperty(Schemas.FORCE_SCHEMA_IMPORT, "false");
    }

    void write(File f, String xsd) throws IOException {
        f.deleteOnExit();
        f.createNewFile();
        FileWriter w = new FileWriter(f);
        w.write(xsd);
        w.flush();
        w.close();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        new File(tmp, "root.xsd").delete();
        new File(tmp, "import1.xsd").delete();
        new File(sub, "import2.xsd").delete();
        new File(tmp, "include1.xsd").delete();
        new File(sub, "include2.xsd").delete();

        sub.delete();
        tmp.delete();

        System.setProperty(Schemas.FORCE_SCHEMA_IMPORT, "false");

        executorService.shutdown();
    }

    public void testValidateImportsIncludes() throws Exception {
        String location = new File(tmp, "root.xsd").getAbsolutePath();
        List errors = Schemas.validateImportsIncludes(location);
        assertEquals(2, errors.size());

        SchemaLocationResolver resolver1 =
                new SchemaLocationResolver(XS.getInstance()) {

                    public boolean canHandle(XSDSchema schema, String uri, String location) {
                        if (location.endsWith("import2.xsd")) {
                            return true;
                        }

                        return false;
                    }

                    public String resolveSchemaLocation(
                            XSDSchema schema, String uri, String location) {
                        return new File(sub, "import2.xsd").getAbsolutePath();
                    }
                };
        SchemaLocationResolver resolver2 =
                new SchemaLocationResolver(XS.getInstance()) {

                    public boolean canHandle(XSDSchema schema, String uri, String location) {
                        if (location.endsWith("include2.xsd")) {
                            return true;
                        }

                        return false;
                    }

                    public String resolveSchemaLocation(
                            XSDSchema schema, String uri, String location) {
                        return new File(sub, "include2.xsd").getAbsolutePath();
                    }
                };

        errors =
                Schemas.validateImportsIncludes(
                        location, null, new XSDSchemaLocationResolver[] {resolver1, resolver2});
        assertEquals(0, errors.size());
    }

    /**
     * Tests that element declarations and type definitions from imported schemas are parsed, even
     * if the importing schema itself contains no element nor type.
     */
    public void testImportsOnly() throws IOException {
        XSDSchema schema =
                Schemas.parse(Schemas.class.getResource("importFacetsEmpty.xsd").toString());
        assertNotNull(schema);

        boolean elFound = hasElement(schema, "collapsedString");
        assertTrue(elFound);
    }

    /**
     * Tests that system property "org.geotools.xml.forceSchemaImport" is properly taken into
     * account.
     */
    public void testForcedSchemaImport() throws IOException {
        XSDSchema schema =
                Schemas.parse(Schemas.class.getResource("importFacetsNotEmpty.xsd").toString());
        assertNotNull(schema);

        // importing schema is not empty and system property "org.geotools.xml.forceSchemaImport" is
        // false:
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
        for (XSDElementDeclaration elDecl : elDeclList) {
            if (elQName.equals(elDecl.getQName())) {
                elFound = true;
            }
        }

        return elFound;
    }

    /**
     * {@link HTTPURIHandler} implementation mocks a remote GeoServer which synchronizes on {@link
     * org.geotools.xsd.Schemas} class, as GeoServer does. The mock uses a timeout, to avoid the
     * test to hang for ever in case of future implementation changes.
     */
    private final class MockServerBehaviour extends HTTPURIHandler {
        @Override
        public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
            try {
                return executorService.invokeAny(
                        Collections.singletonList(
                                new Callable<InputStream>() {
                                    @Override
                                    public InputStream call() throws Exception {
                                        synchronized (Schemas.class) {
                                            return Schemas.class.getResourceAsStream(
                                                    "remoteSchemaLocation.xsd");
                                        }
                                    }
                                }),
                        3,
                        TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                return null;
            } catch (ExecutionException e) {
                return null;
            } catch (TimeoutException e) {
                throw new RuntimeException("Timed out.", e);
            }
        }
    }

    /**
     * Test ensures no deadlock occurs when a remote schema is loaded, which resolves to the same
     * JVM. Deadlock used to occur in GeoServer, when schema was loaded from same GeoServer
     * instance, because schema consumer and schema server both synchronized on same {@link
     * org.geotools.xsd.Schemas} class instance.
     */
    public void testParseRemoteDoesNotBlock() throws IOException {
        URIConverter converter =
                new ExtensibleURIConverterImpl(
                        Collections.singletonList(new MockServerBehaviour()),
                        Collections.emptyList());
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setURIConverter(converter);
        XSDSchema schema =
                Schemas.parse("http://www.foo.bar/remoteSchemaLocation.xsd", resourceSet);
        assertNotNull(schema);
    }
}
