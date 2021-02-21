/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.color;

import org.junit.Assert;
import org.junit.Test;

public class PaletteTest {
    ColorBrewer brewer;

    @Test
    public void testSequential() throws Exception {
        brewer = new ColorBrewer();
        brewer.loadPalettes(ColorBrewer.SEQUENTIAL);

        String[] names = brewer.getPaletteNames();
        Assert.assertEquals(18, names.length);
        Assert.assertNotNull(brewer.getDescription()); // we have a description!

        BrewerPalette palette = brewer.getPalette("YlGnBu");
        Assert.assertNotNull(palette); // we have a palette!
        Assert.assertNotNull(palette.getDescription()); // we have another description!
    }

    @Test
    public void testDiverging() throws Exception {
        brewer = new ColorBrewer();
        brewer.loadPalettes(ColorBrewer.DIVERGING);

        String[] names = brewer.getPaletteNames();
        Assert.assertEquals(9, names.length);
        Assert.assertNotNull(brewer.getDescription()); // we have a description!

        BrewerPalette palette = brewer.getPalette("PuOr");
        Assert.assertNotNull(palette); // we have a palette!
        Assert.assertNotNull(palette.getDescription()); // we have another description!
    }

    @Test
    public void testQualitative() throws Exception {
        brewer = new ColorBrewer();
        brewer.loadPalettes(ColorBrewer.QUALITATIVE);

        String[] names = brewer.getPaletteNames();
        Assert.assertEquals(8, names.length);
        Assert.assertNotNull(brewer.getDescription()); // we have a description!

        BrewerPalette palette = brewer.getPalette("Set3");
        Assert.assertNotNull(palette); // we have a palette!
        Assert.assertNotNull(palette.getDescription()); // we have another description!
    }

    @Test
    public void testAll() throws Exception {
        // load all palettes (defaults)
        brewer = new ColorBrewer();
        brewer.loadPalettes();

        BrewerPalette[] pal = brewer.getPalettes();
        Assert.assertEquals(35, pal.length);

        // test palette filtering abilities
        BrewerPalette[] palettes = brewer.getPalettes(ColorBrewer.SUITABLE_RANGED);
        Assert.assertEquals(27, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.SUITABLE_UNIQUE);
        Assert.assertEquals(8, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.DIVERGING);
        Assert.assertEquals(9, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.SEQUENTIAL);
        Assert.assertEquals(18, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.QUALITATIVE);
        Assert.assertEquals(8, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.ALL);
        Assert.assertEquals(35, palettes.length);

        palettes = brewer.getPalettes(new PaletteType(true, false));
        Assert.assertEquals(27, palettes.length);
        palettes = brewer.getPalettes(new PaletteType());
        Assert.assertEquals(35, palettes.length);
        palettes = brewer.getPalettes(new PaletteType(false, false));
        Assert.assertEquals(0, palettes.length);

        palettes = brewer.getPalettes(ColorBrewer.QUALITATIVE, 9);
        Assert.assertEquals(4, palettes.length);
    }
}
