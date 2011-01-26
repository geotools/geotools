package org.geotools.data.directory;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.junit.After;

public class TestSupport {
    
    static final URI NSURI;
    static {
        try {
            NSURI = new URI("http://www.geotools.org");
        } catch(Exception e) {
            throw new RuntimeException("Impossible...");
        }
    }
    File tempDir = null;
    
    @After
    public void tearDown() throws IOException {
        if(tempDir != null)
            FileUtils.deleteDirectory(tempDir);
    }

    /**
     * Copies the specified shape file into the {@code test-data} directory, together with its
     * sibling ({@code .dbf}, {@code .shp}, {@code .shx} and {@code .prj} files).
     */
    protected File copyShapefiles(final String name) throws IOException {
        assertTrue(TestData.copy(this, sibling(name, "dbf")).canRead());
        assertTrue(TestData.copy(this, sibling(name, "shp")).canRead());
        assertTrue(TestData.copy(this, sibling(name, "shx")).canRead());
        try {
            assertTrue(TestData.copy(this, sibling(name, "prj")).canRead());
        } catch (FileNotFoundException e) {
            // Ignore: this file is optional.
        }
        return TestData.copy(this, name);
    }
    
    /**
     * Copies the specified shape file into the {@code test-data} directory, together with its
     * sibling ({@code .dbf}, {@code .shp}, {@code .shx} and {@code .prj} files).
     */
    protected File copyFile(String name, String destDirName) throws IOException {
        File directory = TestData.file(this, null);
        InputStream is = this.getClass().getResourceAsStream(name);
        File destDir = new File(directory, destDirName);
        if(!destDir.exists())
            destDir.mkdirs();
        File file = new File(destDir, name);
        file.deleteOnExit();
        final OutputStream out = new FileOutputStream(file);
        final byte[] buffer = new byte[4096];
        int count;
        while ((count = is.read(buffer)) >= 0) {
            out.write(buffer, 0, count);
        }
        out.close();
        is.close();
        
        return file;
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
}
