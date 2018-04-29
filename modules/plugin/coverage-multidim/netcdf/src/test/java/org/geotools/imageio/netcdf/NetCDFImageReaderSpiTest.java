/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.UUID;
import org.junit.Test;

public final class NetCDFImageReaderSpiTest {

    @Test
    public void testFileIsProperlyClosed() throws Exception {
        // create a temporary file and write some random bytes on it
        Path path = Files.createTempFile(UUID.randomUUID().toString(), ".txt");
        byte[] content = new byte[100];
        Random random = new Random();
        random.nextBytes(content);
        Files.write(path, content);
        // invoking the reader that will not be able to read the file
        File file = path.toFile();
        NetCDFImageReaderSpi reader = new NetCDFImageReaderSpi();
        reader.canDecodeInput(file);
        // check if the file as some lock on it
        assertThat(file.delete(), is(true));
    }
}
