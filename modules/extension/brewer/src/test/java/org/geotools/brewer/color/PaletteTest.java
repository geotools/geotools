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

import junit.framework.TestCase;


public class PaletteTest extends TestCase {
    ColorBrewer brewer;

    public void testSequential() throws Exception {
        brewer = new ColorBrewer();
        brewer.loadPalettes(ColorBrewer.SEQUENTIAL);

        String[] names = brewer.getPaletteNames();
        assertEquals(18, names.length);
        assertNotNull(brewer.getDescription()); //we have a description!

        BrewerPalette palette = brewer.getPalette("YlGnBu");
        assertNotNull(palette); //we have a palette!
        assertNotNull(palette.getDescription()); //we have another description!
    }

    public void testDiverging() throws Exception {
        brewer = new ColorBrewer();
        brewer.loadPalettes(ColorBrewer.DIVERGING);

        String[] names = brewer.getPaletteNames();
        assertEquals(9, names.length);
        assertNotNull(brewer.getDescription()); //we have a description!

        BrewerPalette palette = brewer.getPalette("PuOr");
        assertNotNull(palette); //we have a palette!
        assertNotNull(palette.getDescription()); //we have another description!
    }

    public void testQualitative() throws Exception {
        brewer = new ColorBrewer();
        brewer.loadPalettes(ColorBrewer.QUALITATIVE);

        String[] names = brewer.getPaletteNames();
        assertEquals(8, names.length);
        assertNotNull(brewer.getDescription()); //we have a description!

        BrewerPalette palette = brewer.getPalette("Set3");
        assertNotNull(palette); //we have a palette!
        assertNotNull(palette.getDescription()); //we have another description!
    }

    public void testAll() throws Exception {
        //load all palettes (defaults)
        brewer = new ColorBrewer();
        brewer.loadPalettes();

        BrewerPalette[] pal = brewer.getPalettes();
        assertEquals(35, pal.length);

        BrewerPalette[] palettes;

        //test palette filtering abilities
        palettes = brewer.getPalettes(ColorBrewer.SUITABLE_RANGED);
        assertEquals(27, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.SUITABLE_UNIQUE);
        assertEquals(8, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.DIVERGING);
        assertEquals(9, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.SEQUENTIAL);
        assertEquals(18, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.QUALITATIVE);
        assertEquals(8, palettes.length);
        palettes = brewer.getPalettes(ColorBrewer.ALL);
        assertEquals(35, palettes.length);

        palettes = brewer.getPalettes(new PaletteType(true, false));
        assertEquals(27, palettes.length);
        palettes = brewer.getPalettes(new PaletteType());
        assertEquals(35, palettes.length);
        palettes = brewer.getPalettes(new PaletteType(false, false));
        assertEquals(0, palettes.length);

        palettes = brewer.getPalettes(ColorBrewer.QUALITATIVE, 9);
        assertEquals(4, palettes.length);
    }
}
