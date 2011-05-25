package org.geotools.gce.grassraster;

import java.io.File;
import java.net.URL;

import org.geotools.data.DataUtilities;

import junit.framework.TestCase;

/**
 * Test the {@link JGrassMapEnvironment} class and the created paths.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/grassraster/src/test/java/org/geotools/gce/grassraster/TestJGrassMapEnvironment.java $
 */
@SuppressWarnings("nls")
public class TestJGrassMapEnvironment extends TestCase {

    public void test() {
        URL pitUrl = this.getClass().getClassLoader().getResource("testlocation/test/cell/pit");
        File mapFile = DataUtilities.urlToFile(pitUrl);
        File mapsetFile = mapFile.getParentFile().getParentFile();

        JGrassMapEnvironment jME = new JGrassMapEnvironment(mapFile);
        checkEnvironment(jME);
        jME = new JGrassMapEnvironment(mapsetFile, "pit");
        checkEnvironment(jME);

    }

    private void checkEnvironment( JGrassMapEnvironment jME ) {
        File cell = jME.getCELL();
        assertTrue(cell.exists());
        assertTrue(cell.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "cell" + File.separator
                        + "pit"));

        File cellFolder = jME.getCellFolder();
        assertTrue(cellFolder.exists() && cellFolder.isDirectory());
        assertTrue(cellFolder.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "cell"));

        File fcell = jME.getFCELL();
        assertTrue(fcell.exists());
        assertTrue(fcell.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "fcell"
                        + File.separator + "pit"));

        File fcellFolder = jME.getFcellFolder();
        assertTrue(fcellFolder.exists() && fcellFolder.isDirectory());
        assertTrue(fcellFolder.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "fcell"));

        File colr = jME.getCOLR();
        assertTrue(colr.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "colr" + File.separator
                        + "pit"));

        File colrFolder = jME.getColrFolder();
        assertTrue(colrFolder.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "colr"));

        File wind = jME.getWIND();
        assertTrue(wind.exists());
        assertTrue(wind.getAbsolutePath().endsWith(
                "testlocation" + File.separator + "test" + File.separator + "WIND"));

    }
}
