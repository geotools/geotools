/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;
import javax.media.jai.JAI;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.LogFactory;
import org.geotools.api.filter.Filter;
import org.geotools.util.NullEntityResolver;
import org.geotools.util.PreventLocalEntityResolver;
import org.geotools.util.Version;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.EntityResolver;

/**
 * Tests {@link org.geotools.util.factory.GeoTools}.
 *
 * @since 2.4
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
@SuppressWarnings("BanJNDI")
public final class GeoToolsTest {

    @Before
    public void clearJNDI() throws NamingException {
        GeoTools.clearInitialContext();
        GeoTools.setJNDINameValidator(GeoTools.DEFAULT_JNDI_VALIDATOR);
    }

    /** Makes sures that J2SE 1.4 assertions are enabled. */
    @Test
    public void testAssertionEnabled() {
        assertTrue("Assertions not enabled.", GeoToolsTest.class.desiredAssertionStatus());
    }

    /**
     * Tests the removal of keys from a hashmap. Required for {@link org.geotools.util.factory.FactoryRegistry} working.
     */
    @Test
    public void testHintsKey() {
        final Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        assertFalse(hints.isEmpty());

        Map<RenderingHints.Key, Object> map = new HashMap<>();
        assertNull(map.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        map = Collections.unmodifiableMap(map);
        assertFalse(map.isEmpty());

        final Hints remaining = new Hints(hints);
        assertTrue(remaining.keySet().removeAll(map.keySet()));
        assertTrue(remaining.isEmpty());
    }

    /** Tests addition of custom hints. */
    @Test
    public void testMyHints() {
        Hints hints = GeoTools.getDefaultHints();
        assertTrue(hints.isEmpty());
        assertNull(Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        try {
            hints = GeoTools.getDefaultHints();
            assertNotNull(hints);
            assertFalse(hints.isEmpty());
            assertEquals(1, hints.size());
            final Object value = hints.get(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            assertTrue(value instanceof Boolean);
            assertFalse(((Boolean) value).booleanValue());
            /*
             * Tests the toString() method.
             */
            String text = hints.toString().trim();
            assertTrue(text.matches("Hints:\\s+FORCE_LONGITUDE_FIRST_AXIS_ORDER = false"));

            assertEquals(hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE), Boolean.FALSE);
            text = hints.toString().trim();
            assertTrue(text.matches("Hints:\\s+FORCE_LONGITUDE_FIRST_AXIS_ORDER = true"));

            assertEquals(hints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER), Boolean.TRUE);
            text = hints.toString().trim();
            assertTrue(text.matches("Hints:\\s+System defaults:\\s+FORCE_LONGITUDE_FIRST_AXIS_ORDER = false"));
        } finally {
            assertNotNull(Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        }
        assertTrue(GeoTools.getDefaultHints().isEmpty());
    }

    /** Test Manifest version lookup */
    @Test
    public void testManifest() {
        // jar manifest lookup
        Manifest jai = GeoTools.getManifest(JAI.class);
        assertFalse("manifest metadata", jai.getMainAttributes().isEmpty());

        // this should always be generated during a maven or ide build
        Manifest metadata = GeoTools.getManifest(GeoTools.class);
        assertFalse("manifest metadata", metadata.getMainAttributes().isEmpty());
        assertEquals(
                GeoTools.getVersion().toString(), metadata.getMainAttributes().getValue("Project-Version"));

        // should be a jar durning maven build, generated during IDE build
        Manifest opengis = GeoTools.getManifest(Filter.class);
        assertFalse("manifest metadata", opengis.getMainAttributes().isEmpty());

        Manifest commons_logging = GeoTools.getManifest(LogFactory.class);
        assertNotNull(commons_logging);
        assertFalse("manifest metadata", commons_logging.getMainAttributes().isEmpty());
        assertEquals("1.3.5", commons_logging.getMainAttributes().getValue("Implementation-Version"));
    }

    /** Test version lookup */
    @Test
    public void testVersion() {

        String location = "jar:file:/Users/jody/.m2/repository/org.locationtech/jts/1.14/jts-1.14"
                + ".jar!/org.locationtech/jts/geom/Geometry.class";
        assertEquals("1.14", GeoTools.jarVersion(location));

        location =
                "jar:file:/Users/jody/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar!/org/apache/commons/logging/LogFactory.class";
        assertEquals("1.1.1", GeoTools.jarVersion(location));

        location = "jar:file:/Users/jody/Library/Java/Extensions/jai_core.jar!/javax/media/jai/JAI.class";
        assertNull(GeoTools.jarVersion(location));

        location =
                "vfs:/var/jboss/workspace/BuildSvr_FNMOC/jboss/geoserver/deployments/geoserver.war/WEB-INF/lib/gt-xsd-wcs-13.2.jar/org/geotools/wcs/WCS.class";
        assertEquals("13.2", GeoTools.jarVersion(location));

        Version version = GeoTools.getVersion(Filter.class);
        assertNotNull(version);

        version = GeoTools.getVersion(JAI.class);
        assertNotNull(version);
        assertEquals("1.1.3", version.toString());

        version = GeoTools.getVersion(LogFactory.class);
        assertNotNull(version);
        assertEquals("1.3.5", version.toString());
    }
    /** Tests the use of system properties. */
    @Test
    public void testSystemHints() {
        Hints hints = GeoTools.getDefaultHints();
        assertNotNull(hints);
        assertTrue(hints.isEmpty());
        System.setProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER, "true");
        Hints.scanSystemProperties();
        try {
            hints = GeoTools.getDefaultHints();
            assertNotNull(hints);
            assertFalse(hints.isEmpty());
            assertEquals(1, hints.size());
            final Object value = hints.get(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            assertTrue(value instanceof Boolean);
            assertTrue(((Boolean) value).booleanValue());
        } finally {
            System.clearProperty(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            assertNotNull(Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        }
        hints = GeoTools.getDefaultHints();
        assertNotNull(hints);
        assertTrue(hints.isEmpty());
    }

    /**
     * Tests {@link org.geotools.util.factory.GeoTools#fixName} using simpliest name or no context. We avoid the tests
     * that would require a real initial context.
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testFixName() {
        assertNull(GeoTools.fixName(null));
        assertEquals("simpleName", GeoTools.fixName("simpleName"));
        assertEquals("jdbc:EPSG", GeoTools.fixName(null, "jdbc:EPSG"));
        assertEquals("jdbc/EPSG", GeoTools.fixName(null, "jdbc/EPSG"));
    }

    @Test
    public void testEntityResolver() {

        // confirm instantiate works

        EntityResolver resolver = GeoTools.instantiate(
                "org.geotools.util.factory.PlaceholderEntityResolver",
                EntityResolver.class,
                PreventLocalEntityResolver.INSTANCE);
        assertTrue(resolver instanceof PlaceholderEntityResolver);

        resolver = GeoTools.instantiate(
                "org.geotools.util.NullEntityResolver", EntityResolver.class, PreventLocalEntityResolver.INSTANCE);
        assertTrue(resolver instanceof NullEntityResolver);

        resolver = GeoTools.instantiate(
                "invalid.class.reference", EntityResolver.class, PreventLocalEntityResolver.INSTANCE);
        assertTrue(resolver instanceof PreventLocalEntityResolver);

        resolver = GeoTools.instantiate(null, EntityResolver.class, PreventLocalEntityResolver.INSTANCE);
        assertTrue(resolver instanceof PreventLocalEntityResolver);

        // confirm system hints work
        try {
            Hints.putSystemDefault(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);
            assertSame(NullEntityResolver.INSTANCE, GeoTools.getEntityResolver(null));

            // test default behavor
            Hints.removeSystemDefault(Hints.ENTITY_RESOLVER);
            assertSame(PreventLocalEntityResolver.INSTANCE, GeoTools.getEntityResolver(null));

            // test system property functions with default constructor
            System.getProperties().put(GeoTools.ENTITY_RESOLVER, "org.geotools.util.factory.PlaceholderEntityResolver");
            Hints.scanSystemProperties();
            EntityResolver entityResolver = GeoTools.getEntityResolver(null);
            assertTrue(entityResolver instanceof PlaceholderEntityResolver);

            // test system property functions with INSTANCE field constructor
            System.getProperties().put(GeoTools.ENTITY_RESOLVER, "org.geotools.util.NullEntityResolver");
            Hints.scanSystemProperties();
            entityResolver = GeoTools.getEntityResolver(null);
            assertTrue(entityResolver instanceof NullEntityResolver);
        } finally {
            System.clearProperty(GeoTools.ENTITY_RESOLVER);
            Hints.removeSystemDefault(Hints.ENTITY_RESOLVER);
            Hints.scanSystemProperties();
        }
    }

    @Test
    public void testLookupValidation() throws Exception {
        // setup mock initial context (need a JNDI provider otherwise, like simple-jndi)
        InitialContext ctx = Mockito.mock(InitialContext.class);
        Object test1 = new Object();
        String name1 = "java://test1";
        Mockito.when(ctx.lookup(name1)).thenReturn(test1);
        Object test2 = new Object();
        String name2 = "ftp://test2";
        Mockito.when(ctx.lookup(name2)).thenReturn(test2);
        Object test3 = new Object();
        String name3 = "http://test3";
        Mockito.when(ctx.lookup(name3)).thenReturn(test3);
        Object test4 = new Object();
        String name4 = "java://test4{}"; // invalid URI
        Mockito.when(ctx.lookup(name4)).thenReturn(test4);

        // using default validator
        GeoTools.init(ctx);
        assertSame(test1, GeoTools.jndiLookup(name1));
        assertNull(GeoTools.jndiLookup(name2));
        assertNull(GeoTools.jndiLookup(name3));
        assertNull(GeoTools.jndiLookup(name4));

        // setup an "accept all" filter
        GeoTools.setJNDINameValidator(name -> true);
        assertSame(test1, GeoTools.jndiLookup(name1));
        assertSame(test2, GeoTools.jndiLookup(name2));
        assertSame(test3, GeoTools.jndiLookup(name3));
        assertSame(test4, GeoTools.jndiLookup(name4));
    }
}
