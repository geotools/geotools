/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.TestData;
import org.junit.After;

/**
 * Base class for test suite.
 * <p>
 * Note: a nearly identical copy of this file exists in the {@code ext/shape} module.
 * </p>
 * 
 * @source $URL$
 * @version $Id$
 */
public class TestCaseSupport {
    
    /**
     * Stores all temporary files here - delete on tear down.
     */
    private final List<File> tmpFiles = new ArrayList<File>();

    /**
     * Deletes all temporary files created by {@link #getTempFile}. This method
     * is automatically run after each test.
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
            dieDieDIE(sibling(targetFile, "prj"));
            dieDieDIE(sibling(targetFile, "qix"));
            dieDieDIE(sibling(targetFile, "dbx"));
            
            f.remove();
        }
    }
    
    private void dieDieDIE(File file) {
        if (file.exists()) {
            if (file.delete()) {
                // dead
            } else {
                System.out.println("Couldn't delete "+file);
                file.deleteOnExit(); // dead later
            }
        }
    }
    
    /**
     * Helper method for {@link #tearDown}.
     */
    protected static File sibling(final File f, final String ext) {
        return new File(f.getParent(), sibling(f.getName(), ext));
    }
    
    /**
     * Helper method for {@link #copyShapefiles}.
     */
    private static String sibling(String name, final String ext) {
        final int s = name.lastIndexOf('.');
        if (s >= 0) {
            name = name.substring(0, s);
        }
        return name + '.' + ext;
    }
    
    /**
     * Copies the specified shape file into the {@code test-data} directory, 
     * together with its sibling ({@code .dbf}, {@code .shp}, {@code .shx} and so on).
     */
    protected static File copyShapefiles(final String name) throws IOException {
        assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "dbf")).canRead());
        assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "shp")).canRead());
        assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "shx")).canRead());
        try {
            assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "prj")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "qix")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        try {
            assertTrue(TestData.copy(TestCaseSupport.class, sibling(name, "dbx")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        return TestData.copy(TestCaseSupport.class, name);
    }
    
    /**
     * Returns the test suite for the given class.
     */
    public static Test suite(Class<?> c) {
        return new TestSuite(c);
    }
}
