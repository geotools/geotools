package org.geotools.styling;

import java.io.File;
import java.net.URL;

import org.geotools.data.DataUtilities;

import junit.framework.TestCase;

public class DefaultResourceLocatorTest extends TestCase {

    public void testRelativeFileURL() throws Exception {
        DefaultResourceLocator locator = new DefaultResourceLocator();
        locator.setSourceUrl(getClass().getResource("test-data/blob.gif"));

        checkURL(locator.locateResource("blob.gif"));
        checkURL(locator.locateResource("file:blob.gif"));
        checkURL(locator.locateResource("file://blob.gif"));
        checkURL(locator.locateResource("file://./blob.gif"));
    }

    void checkURL(URL url) {
        assertNotNull(url);
        File f = DataUtilities.urlToFile(url);
        assertTrue(f.exists());
    }
}
