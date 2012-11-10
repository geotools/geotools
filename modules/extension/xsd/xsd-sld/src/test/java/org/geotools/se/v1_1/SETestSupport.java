/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1;

import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * 
 *
 * @source $URL$
 */
public class SETestSupport extends XMLTestSupport {

    @Override
    protected Configuration createConfiguration() {
        return new SEConfiguration();
    }

    protected static BufferedImage getReferenceImage(String resourceName) throws IOException {
        URL url = SETestSupport.class.getResource(resourceName);
        return ImageIO.read(url);
    }

    protected static void assertImagesEqual(BufferedImage expected, Icon icon) {
        BufferedImage actual = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = actual.createGraphics();
        try {
            icon.paintIcon(null, g, 0, 0);
        }
        finally {
            g.dispose();
        }

        assertNotNull(expected);
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getHeight(), actual.getHeight());
        int w = actual.getWidth();
        int h = actual.getHeight();
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                assertEquals("mismatch at (" + x + ", " + y + ")", expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }
}
