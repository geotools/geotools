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

import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.geotools.util.logging.Logging;

/**
 * Function object parsing a full font name and returning its base name, bold and italic modifiers
 * as properties.
 */
public class FontAttributesExtractor {

    private static final Pattern SPACE_SPLITTER = Pattern.compile("\\s+");
    private static final Logger LOGGER = Logging.getLogger(FontAttributesExtractor.class);

    private boolean bold;
    private boolean italic;
    private String baseName;

    public FontAttributesExtractor(String name) {
        String[] split = SPACE_SPLITTER.split(name);

        // remove the qualifiers such as bold/italic/regular
        int maxIdx = split.length - 1;
        for (int i = 0; i < 3; i++) {
            String term = split[maxIdx];
            if (term.equalsIgnoreCase("Bold")) {
                bold = true;
                maxIdx--;
            }
            if (term.equalsIgnoreCase("Italic")) {
                italic = true;
                maxIdx--;
            }
            if (term.equalsIgnoreCase("Regular")) {
                maxIdx--;
            }
        }
        if (maxIdx > 0) {
            StringJoiner sj = new StringJoiner(" ");
            for (int i = 0; i <= maxIdx; i++) {
                sj.add(split[i]);
            }
            baseName = sj.toString();
        } else {
            LOGGER.log(
                    Level.FINE,
                    "Seems the name was formed only with Bold, Italic and Regular, falling back to use the orignal names");
            baseName = name;
            bold = false;
            italic = false;
        }
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public String getBaseName() {
        return baseName;
    }
}
