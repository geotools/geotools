/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vsi;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

import it.geosolutions.imageio.plugins.vrt.VRTImageReaderSpi;
import org.junit.Test;

/**
 * Tests for VSIFormatFactory class
 *
 * @author Matthew Northcott <matthewnorthcott@catalyst.net.nz>
 */
public final class VSIFormatFactoryTest {

    @Test
    public void testIsAvailable() {
        try {
            Class.forName("it.geosolutions.imageio.plugins.vrt.VRTImageReaderSpi");
        } catch (ClassNotFoundException ex) {
            assumeNoException(ex);
        }

        assumeTrue(new VRTImageReaderSpi().isAvailable());
        assertTrue(new VSIFormatFactory().isAvailable());
    }

    @Test
    public void testCreateFormat() {
        assertTrue(new VSIFormatFactory().createFormat() instanceof VSIFormat);
    }
}
