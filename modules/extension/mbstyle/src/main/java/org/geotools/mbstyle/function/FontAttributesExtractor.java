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

import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.geotools.util.logging.Logging;

/** Function object parsing a full font name and returning its base name, bold and italic modifiers as properties. */
public class FontAttributesExtractor {

    private static final Pattern SPACE_SPLITTER = Pattern.compile("\\s+");
    private static final Logger LOGGER = Logging.getLogger(FontAttributesExtractor.class);

    /**
     * Weight and style words removed from the end of a font name, so the family name finds its faces for other scripts,
     * such as "Noto Sans Arabic" for "Noto Sans Medium". Width words such as "Condensed" stay: Java lists "Roboto
     * Condensed" as a family of its own.
     */
    private static final Set<String> QUALIFIERS = Set.of(
            "regular",
            "bold",
            "italic",
            "oblique",
            "medium",
            "light",
            "black",
            "thin",
            "semibold",
            "semilight",
            "extrabold",
            "extralight",
            "book",
            "heavy");

    /** Qualifiers that make the base name bold. Semibold and medium stay regular. */
    private static final Set<String> BOLD_QUALIFIERS = Set.of("bold", "extrabold", "black", "heavy");

    private boolean bold;
    private boolean italic;
    private final String baseName;

    public FontAttributesExtractor(String name) {
        String[] split = SPACE_SPLITTER.split(name);

        // strip trailing weight/width/style qualifiers, remembering the two Java2D can model (bold, italic)
        int maxIdx = split.length - 1;
        while (maxIdx > 0 && QUALIFIERS.contains(split[maxIdx].toLowerCase())) {
            String term = split[maxIdx].toLowerCase();
            if (BOLD_QUALIFIERS.contains(term)) {
                bold = true;
            } else if (term.equals("italic") || term.equals("oblique")) {
                italic = true;
            }
            maxIdx--;
        }
        if (maxIdx > 0 || !QUALIFIERS.contains(split[0].toLowerCase())) {
            StringJoiner sj = new StringJoiner(" ");
            for (int i = 0; i <= maxIdx; i++) {
                sj.add(split[i]);
            }
            baseName = sj.toString();
        } else {
            LOGGER.log(Level.FINE, "Font name formed only of qualifiers, falling back to the original name");
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
