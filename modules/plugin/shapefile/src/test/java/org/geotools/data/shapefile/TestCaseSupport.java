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
package org.geotools.data.shapefile;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.TestData;
import org.geotools.api.data.CloseableIterator;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.After;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * Base class for test suite. This class is not abstract for the purpose of {@link TestCaseSupportTest}, but should not
 * be instantiated otherwise. It should be extented (which is why the constructor is protected).
 *
 * <p>Note: a nearly identical copy of this file exists in the {@code ext/shape} module.
 *
 * @version $Id$
 * @author Ian Schneider
 * @author Martin Desruisseaux
 */
public class TestCaseSupport {

    /** References a known test file provided by sample data. */
    static final String STATE_POP = "shapes/statepop.shp";

    /** References a known test file provided by sample data. */
    static final String STREAM = "shapes/stream.shp";

    /** References a known test file provided by sample data. */
    static final String DANISH = "shapes/danish_point.shp";

    /** References a known test file provided by sample data. */
    static final String CHINESE = "shapes/chinese_poly.shp";

    /** References a known test file provided by sample data. */
    static final String RUSSIAN = "shapes/rus-windows-1251.shp";

    /** References a known test file provided by sample data. */
    static final FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Set to {@code true} if {@code println} are wanted during normal execution. It doesn't apply to message displayed
     * in case of errors.
     */
    // protected static boolean verbose = false;
    /** Stores all temporary files here - delete on tear down. */
    private final List<File> tmpFiles = new ArrayList<>();

    /**
     * Deletes all temporary files created by {@link #getTempFile}. This method is automatically run after each test.
     */
    @After
    public void tearDown() throws Exception {

        Runtime.getRuntime().runFinalization();
        // it seems that not all files marked as temp will get erased, perhaps
        // this is because they have been rewritten? Don't know, don't _really_
        // care, so I'll just delete everything
        final Iterator<File> f = tmpFiles.iterator();
        while (f.hasNext()) {
            File targetFile = f.next();

            dieDieDIE(targetFile);
            dieDieDIE(sibling(targetFile, "shp"));
            dieDieDIE(sibling(targetFile, "dbf"));
            dieDieDIE(sibling(targetFile, "shx"));
            // Quad tree index
            dieDieDIE(sibling(targetFile, "qix"));
            // Feature ID index
            dieDieDIE(sibling(targetFile, "fix"));
            // R-Tree index
            dieDieDIE(sibling(targetFile, "grx"));
            dieDieDIE(sibling(targetFile, "prj"));
            dieDieDIE(sibling(targetFile, "shp.xml"));
            dieDieDIE(sibling(targetFile, "cpg"));

            f.remove();
        }
    }

    private void dieDieDIE(File file) {
        if (file.exists()) {
            if (!file.delete()) {
                // System.out.println("Couldn't delete " + file);
                file.deleteOnExit(); // dead later
            }
        }
    }

    /** Helper method for {@link #tearDown}. */
    protected static File sibling(final File f, final String ext) {
        return new File(f.getParent(), sibling(f.getName(), ext));
    }

    /** Helper method for {@link #copyShapefiles}. */
    private static String sibling(String name, final String ext) {
        final int s = name.lastIndexOf('.');
        if (s >= 0) {
            name = name.substring(0, s);
        }
        return name + '.' + ext;
    }

    /**
     * Read a geometry of the given name.
     *
     * @param wktResource The resource name to load, without its {@code .wkt} extension.
     * @return The geometry.
     * @throws IOException if reading failed.
     */
    protected Geometry readGeometry(final String wktResource) throws IOException {
        try (final BufferedReader stream = TestData.openReader("wkt/" + wktResource + ".wkt")) {
            final WKTReader reader = new WKTReader();
            try {
                return reader.read(stream);
            } catch (ParseException pe) {
                IOException e = new IOException("parsing error in resource " + wktResource);
                e.initCause(pe);
                throw e;
            }
        }
    }

    /** Creates a temporary file, to be automatically deleted at the end of the test suite. */
    protected File getTempFile() throws IOException {
        // force in some valid but weird chars into the path to be on par with OSX that does it
        // on its own
        File tmpFile = File.createTempFile("test-+()shp", ".shp");
        tmpFile.deleteOnExit();
        assertTrue(tmpFile.isFile());

        // keep track of all temp files so we can delete them
        markTempFile(tmpFile);

        return tmpFile;
    }

    protected void markTempFile(File tmpFile) {
        tmpFiles.add(tmpFile);
    }

    /**
     * Copies the specified shape file into the {@code test-data} directory, together with its sibling ({@code .dbf},
     * {@code .shp}, {@code .shx} and {@code .prj} files).
     */
    protected File copyShapefiles(final String name) throws IOException {
        assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "dbf")).canRead());
        assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "shp")).canRead());
        try {
            assertTrue(
                    TestData.copy(TestCaseSupport.class, sibling(name, "shx")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(
                    TestData.copy(TestCaseSupport.class, sibling(name, "prj")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(
                    TestData.copy(TestCaseSupport.class, sibling(name, "fix")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(
                    TestData.copy(TestCaseSupport.class, sibling(name, "qix")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(
                    TestData.copy(TestCaseSupport.class, sibling(name, "grx")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "shp.xml"))
                    .canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(
                    TestData.copy(TestCaseSupport.class, sibling(name, "cpg")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        File copy = TestData.copy(TestCaseSupport.class, name);
        markTempFile(copy);

        return copy;
    }

    protected int countIterator(CloseableIterator<?> it) {
        int count = 0;
        while (it.hasNext()) {
            count++;
            it.next();
        }
        try {
            it.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return count;
    }
}
