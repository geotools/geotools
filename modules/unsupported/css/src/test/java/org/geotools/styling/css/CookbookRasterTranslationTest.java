/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.styling.css;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class CookbookRasterTranslationTest extends AbstractIntegrationTest {

    public CookbookRasterTranslationTest(String name, File file) {
        super(name, file, true);
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        List<Object[]> result = new ArrayList<>();
        File root = new File("./src/test/resources/css/cookbook/raster");
        for (File file : root.listFiles()) {
            if (file.getName().endsWith(".css")) {
                result.add(new Object[] {file.getName(), file});
            }
        }

        return result;
    }
}
