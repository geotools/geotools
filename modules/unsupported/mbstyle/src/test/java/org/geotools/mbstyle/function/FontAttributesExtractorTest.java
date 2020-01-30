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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
    public void testOutsideConventions() {
        FontAttributesExtractor fae = new FontAttributesExtractor("Regular Italic Bold");
        // gives up and uses name as base name
        assertThat(fae.getBaseName(), equalTo("Regular Italic Bold"));
        assertThat(fae.isBold(), is(false));
        assertThat(fae.isItalic(), is(false));
    }
}
