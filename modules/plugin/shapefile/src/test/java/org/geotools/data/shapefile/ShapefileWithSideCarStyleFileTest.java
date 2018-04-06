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

import static org.junit.Assert.assertNull;

import org.geotools.TestData;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.ResourceInfo;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

import java.net.URL;

/**
 * @author Jorge Gustavo Rocha
 * @version $Id$
 * @source $URL$
 */
public class ShapefileWithSideCarStyleFileTest extends TestCaseSupport {

    public final String STATEPOP = "shapes/statepop.shp";
    public final String POINTTEST = "shapes/pointtest.shp";

    @Test
    public void testNoDefaultStyle() throws Exception {
        final URL url = TestData.url(POINTTEST);
        FileDataStore store = FileDataStoreFinder.getDataStore(url);
        FeatureSource featureSource = store.getFeatureSource();
        ResourceInfo rinfo = featureSource.getInfo();
        StyledLayerDescriptor styledld = rinfo.getDefaultStyle();
        assertNull(styledld);
    }

    // TRAVIS path:
    // jar:file:/home/travis/build/geotools/geotools/modules/library/sample-data/target/gt-sample-data-20-SNAPSHOT.jar!/org/geotools/test-data/shapes/statepop.shp

    /*@Test
    public void testFileExist() throws Exception {
        final URL url = TestData.url(STATEPOP);
        String shppath = url.toString().substring(5);
        String sldpath = shppath.replace(".shp", ".sld");
        File shpfile = new File(shppath);
        if(shpfile.exists() && !shpfile.isDirectory()) {
            System.out.println(shpfile + " file exist");
        } else {
            System.out.println(shpfile + " file does NOT exist");
        }
        File sldfile = new File(sldpath);
        if(sldfile.exists() && !sldfile.isDirectory()) {
            System.out.println(sldpath + " file exist");
        } else {
            System.out.println(sldpath + " file does NOT exist");
        }
        assertTrue(shpfile.exists() && !shpfile.isDirectory());
        assertTrue(sldfile.exists() && !sldfile.isDirectory());
    }

    @Test
    public void testGetNativeStyles() throws Exception {
        final URL url = TestData.url(STATEPOP);
        System.out.println(url.toString());
        FileDataStore store = FileDataStoreFinder.getDataStore(url);
        FeatureSource featureSource = store.getFeatureSource();
        ResourceInfo rinfo = featureSource.getInfo();
        List<String> nativeStylesList = rinfo.getNativeStyles();
        System.out.println(nativeStylesList.size());
        assertEquals(1, nativeStylesList.size());
        assertEquals("statepop", nativeStylesList.get(0));
    }

    @Test
    public void testGetDefaultStyle() throws Exception {
        final URL url = TestData.url(STATEPOP);
        FileDataStore store = FileDataStoreFinder.getDataStore(url);
        FeatureSource featureSource = store.getFeatureSource();
        ResourceInfo rinfo = featureSource.getInfo();
        StyledLayerDescriptor styledld = rinfo.getDefaultStyle();
        assertNotNull(styledld);
        NamedLayer nlayer = (NamedLayer) styledld.getStyledLayers()[0];
        Style style = nlayer.getStyles()[0];
        assertEquals("population", style.getName());
    }*/

}
