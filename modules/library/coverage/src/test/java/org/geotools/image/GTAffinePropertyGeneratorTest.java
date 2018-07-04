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
package org.geotools.image;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.media.jai.Interpolation;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.AffineDescriptor;
import org.junit.Test;

public class GTAffinePropertyGeneratorTest {

    @Test
    public void testPropertyGeneration() {
        BufferedImage source = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
        RenderedOp affine =
                AffineDescriptor.create(
                        source,
                        new AffineTransform(),
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                        null,
                        null);

        Object property = new GTAffinePropertyGenerator().getProperty("ROI", (Object) affine);
        assertEquals(java.awt.Image.UndefinedProperty, property);
    }
}
