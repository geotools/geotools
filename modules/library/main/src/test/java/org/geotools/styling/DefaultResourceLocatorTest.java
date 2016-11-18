package org.geotools.styling;

import java.io.File;
import java.net.URL;

import org.geotools.data.DataUtilities;
import org.junit.Test;

import junit.framework.TestCase;

public class DefaultResourceLocatorTest extends TestCase {
    @Test
    public void testRelativeFileURL() throws Exception {
        DefaultResourceLocator locator = new DefaultResourceLocator();
        locator.setSourceUrl(getClass().getResource("test-data/blob.gif"));

        checkURL(locator.locateResource("blob.gif"));
        checkURL(locator.locateResource("file:blob.gif"));
        checkURL(locator.locateResource("file://blob.gif"));
        checkURL(locator.locateResource("file://./blob.gif"));
    }
    
    public void testPreserveURLQuery() throws Exception{
        DefaultResourceLocator locator = new DefaultResourceLocator();
        locator.setSourceUrl(getClass().getResource("test-data/blob.gif"));
        
        // Confirm still able to resolve to a File
        URL url = locator.locateResource("blob.gif?query=parameter");
        assertEquals("query=parameter",url.getQuery());
        File file = DataUtilities.urlToFile(url);
        assertTrue( file.exists() );
    }

    void checkURL(URL url) {
        File f = DataUtilities.urlToFile(url);
        assertTrue(f.exists());
    }
    
}
