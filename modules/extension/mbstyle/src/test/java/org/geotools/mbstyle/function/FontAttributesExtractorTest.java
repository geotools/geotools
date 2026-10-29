/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class FontAttributesExtractorTest {

    @Test
    public void testBaseAlready() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Noto Sans");
        assertThat(fae.getBaseName(), equalTo("Noto Sans"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(false));
    }

    @Test
    public void testRegular() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Noto Sans Regular");
        assertThat(fae.getBaseName(), equalTo("Noto Sans"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(false));
    }

    @Test
    public void testBoldItalic() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Noto Sans Bold Italic");
        assertThat(fae.getBaseName(), equalTo("Noto Sans"));
        assertThat(fae.isBold(), is(true));
        assertThat(fae.isItalic(), is(true));
    }

    @Test
    public void testItalicBold() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Noto Sans Italic Bold");
        assertThat(fae.getBaseName(), equalTo("Noto Sans"));
        assertThat(fae.isBold(), is(true));
        assertThat(fae.isItalic(), is(true));
    }

    @Test
    public void testMedium() {
        // Medium is a weight Java2D cannot model; it must still be stripped so the base name resolves
        FontAttributesExtractor fae = new FontAttributesExtractor("Noto Sans Medium");
        assertThat(fae.getBaseName(), equalTo("Noto Sans"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(false));
    }

    @Test
    public void testBlack() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Arial Black");
        assertThat(fae.getBaseName(), equalTo("Arial"));
        assertThat(fae.isBold(), is(true));
        assertThat(fae.isItalic(), is(false));
    }

    @Test
    public void testSemiBold() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Roboto SemiBold");
        assertThat(fae.getBaseName(), equalTo("Roboto"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(false));
    }

    @Test
    public void testMediumItalic() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Noto Sans Medium Italic");
        assertThat(fae.getBaseName(), equalTo("Noto Sans"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(true));
    }

    /** "Mono" names a family, not a weight or style, so it stays in the base name. */
    @Test
    public void testMono() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Noto Sans Mono");
        assertThat(fae.getBaseName(), equalTo("Noto Sans Mono"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(false));
    }

    /** "Condensed" names a family of its own, so only the style after it is removed. */
    @Test
    public void testCondensedItalic() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Roboto Condensed Italic");
        assertThat(fae.getBaseName(), equalTo("Roboto Condensed"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(true));
    }

    @Test
    public void testOutsideConventions() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Regular Italic Bold");
        // gives up and uses name as base name
        assertThat(fae.getBaseName(), equalTo("Regular Italic Bold"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(false));
    }
}
