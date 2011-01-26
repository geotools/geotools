/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.TestData;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


/**
 * Base class for test suite. This class is not abstract for the purpose of
 * {@link TestCaseSupportTest}, but should not be instantiated otherwise.
 * It should be extented (which is why the constructor is protected).
 * <p>
 * Note: a nearly identical copy of this file exists in the {@code ext/shape} module.
 *
 * @source $URL$
 * @version $Id$
 * @author  Ian Schneider
 * @author  Martin Desruisseaux
 */
public abstract class TestCaseSupport extends TestCase {
	final static String STATE_POP = "shapes/statepop.shp";

	final static String MIXED = "mif/mixed.MIF";

	
    /**
     * Set to {@code true} if {@code println} are wanted during normal execution.
     * It doesn't apply to message displayed in case of errors.
     */
    //protected static boolean verbose = false;

    /**
     * Stores all temporary files here - delete on tear down.
     */
    private final List tmpFiles = new ArrayList();

    /**
     * Creates a new instance of {@code TestCaseSupport} with the given name.
     */
    protected TestCaseSupport(final String name) throws IOException {
        super(name);
    }

    /**
     * Deletes all temporary files created by {@link #getTempFile}.
     * This method is automatically run after each test.
     */
    protected void tearDown() throws Exception {
        // it seems that not all files marked as temp will get erased, perhaps
        // this is because they have been rewritten? Don't know, don't _really_
        // care, so I'll just delete everything
        final Iterator f = tmpFiles.iterator();
        while (f.hasNext()) {
            File targetFile = (File) f.next();
            
            targetFile.deleteOnExit();
            dieDieDIE( sibling(targetFile, "dbf") );
            dieDieDIE( sibling(targetFile, "shx") );
            dieDieDIE( sibling(targetFile, "qix") );
            dieDieDIE( sibling(targetFile, "fix") );
            // TODDO: r i tree must die
            dieDieDIE( sibling(targetFile, "prj") );
            dieDieDIE( sibling(targetFile, "shp.xml") );
                        
            f.remove();
        }
        super.tearDown();
    }
    
    private void dieDieDIE( File file ){
        if( file.exists() ){
            if( file.delete() ){
                // dead
            }
            else {
                file.deleteOnExit(); // dead later
            }
        }
    }
    
    /**
     * Helper method for {@link #tearDown}.
     */
    private static File sibling(final File f, final String ext) {
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
     * Read a geometry of the given name.
     *
     * @param  wktResource The resource name to load, without its {@code .wkt} extension.
     * @return The geometry.
     * @throws IOException if reading failed.
     */
    protected Geometry readGeometry(final String wktResource) throws IOException {
        final BufferedReader stream = TestData.openReader("wkt/" + wktResource + ".wkt");
        final WKTReader reader = new WKTReader();
        final Geometry geom;
        try {
            geom = reader.read(stream);
        } catch (ParseException pe) {
            IOException e = new IOException("parsing error in resource " + wktResource);
            e.initCause(pe);
            throw e;
        }
        stream.close();
        return geom;
    }

    /**
     * Returns the first feature in the given feature collection.
     */
    protected SimpleFeature firstFeature(FeatureCollection<SimpleFeatureType, SimpleFeature> fc) {
        FeatureIterator<SimpleFeature> features = fc.features();
        try {
            return features.next();
        } finally {
            features.close();
        }
    }

    /**
     * Creates a temporary file, to be automatically deleted at the end of the test suite.
     */
    protected File getTempFile() throws IOException {
        File tmpFile = File.createTempFile("test-shp", ".shp");
        assertTrue(tmpFile.isFile());
        
        // keep track of all temp files so we can delete them
        markTempFile(tmpFile);
        
        return tmpFile;
    }

    private void markTempFile( File tmpFile ) {
        tmpFiles.add(tmpFile);        
    }

    /**
     * Copies the specified shape file into the {@code test-data} directory, together with its
     * sibling ({@code .dbf}, {@code .shp}, {@code .shx} and {@code .prj} files).
     */
    protected void copy(final String name, String[] requiredExtensions, String[] optionalExtensions) throws IOException {
        for (int i = 0; i < requiredExtensions.length; i++) {
            assertTrue(TestData.copy(this, sibling(name, requiredExtensions[i])).canRead());
        }
        for (int i = 0; i < optionalExtensions.length; i++) {
            try {
                assertTrue(TestData.copy(this, sibling(name, optionalExtensions[i])).canRead());
            } catch (FileNotFoundException e) {
                // Ignore: this file is optional.
            }
        }
    }
    
    /**
     * Returns the absolute path of a test file, given its location in the test data set
     * 
     * @param testData
     * @return
     * @throws IOException
     */
    protected String getAbsolutePath(String testData) throws IOException {
        if (testData.endsWith(".shp"))
            copy(testData, new String[] { "shp", "dbf", "shx" }, new String[] { "prj" });
        else if (testData.endsWith(".MIF"))
            copy(testData, new String[] { "MIF", "MID" }, new String[0]);
        File f = new File(TestData.url(this, testData).getFile());
        return f.getAbsolutePath();
    }

    /**
     * Returns the test suite for the given class.
     */
    public static Test suite(Class c) {
        return new TestSuite(c);
    }
}
