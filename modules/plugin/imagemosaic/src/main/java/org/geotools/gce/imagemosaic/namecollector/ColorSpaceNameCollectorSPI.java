/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic.namecollector;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.util.Map;
import javax.media.jai.ImageLayout;
import org.geotools.coverage.grid.io.GridCoverage2DReader;

/**
 * A {@link CoverageNameCollectorSPI} implementation which takes the coverage name from the
 * colorspace of the ImageLayout from the coverage reader.
 */
public class ColorSpaceNameCollectorSPI implements CoverageNameCollectorSPI {

    public CoverageNameCollector create(Object object, Map<String, String> properties) {
        return new ColorSpaceBasedNameCollector();
    }

    static class ColorSpaceBasedNameCollector implements CoverageNameCollector {

        private static final String GRAY = "GRAY";

        private static final String RGB = "RGB";

        public ColorSpaceBasedNameCollector() {}

        @Override
        public String getName(GridCoverage2DReader reader, Map<String, String> map) {
            ImageLayout layout;
            String coverageName = null;
            try {
                layout = reader.getImageLayout();
                ColorModel cm = layout.getColorModel(null);
                ColorSpace cs = cm.getColorSpace();
                int type = cs.getType();
                switch (type) {
                    case ColorSpace.TYPE_GRAY:
                        coverageName = GRAY;
                        break;
                    case ColorSpace.TYPE_RGB:
                        coverageName = RGB;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "The specified ColorSpace's type is not supported: " + type);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            return coverageName;
        }
    }
}
