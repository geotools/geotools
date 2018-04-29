/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * List of common color names.
 *
 * @see http://en.wikipedia.org/wiki/X11_color_names
 */
public class Colors {
    static Map<String, Color> colorMap = new HashMap<String, Color>();

    static {
        colorMap.put("aliceblue", color(240, 248, 255));
        colorMap.put("yellowgreen", color(154, 205, 50));
        colorMap.put("antiquewhite", color(250, 235, 215));
        colorMap.put("aqua", color(0, 255, 255));
        colorMap.put("aquamarine", color(127, 255, 212));
        colorMap.put("azure", color(240, 255, 255));
        colorMap.put("beige", color(245, 245, 220));
        colorMap.put("bisque", color(255, 228, 196));
        colorMap.put("black", color(0, 0, 0));
        colorMap.put("blanchedalmond", color(255, 235, 205));
        colorMap.put("blue", color(0, 0, 255));
        colorMap.put("blueviolet", color(138, 43, 226));
        colorMap.put("brown", color(165, 42, 42));
        colorMap.put("burlywood", color(222, 184, 135));
        colorMap.put("cadetblue", color(95, 158, 160));
        colorMap.put("chartreuse", color(127, 255, 0));
        colorMap.put("chocolate", color(210, 105, 30));
        colorMap.put("coral", color(255, 127, 80));
        colorMap.put("cornflowerblue", color(100, 149, 237));
        colorMap.put("cornsilk", color(255, 248, 220));
        colorMap.put("crimson", color(220, 20, 60));
        colorMap.put("cyan", color(0, 255, 255));
        colorMap.put("darkblue", color(0, 0, 139));
        colorMap.put("darkcyan", color(0, 139, 139));
        colorMap.put("darkgoldenrod", color(184, 134, 11));
        colorMap.put("darkgray", color(169, 169, 169));
        colorMap.put("darkgreen", color(0, 100, 0));
        colorMap.put("darkkhaki", color(189, 183, 107));
        colorMap.put("darkmagenta", color(139, 0, 139));
        colorMap.put("darkolivegreen", color(85, 107, 47));
        colorMap.put("darkorange", color(255, 140, 0));
        colorMap.put("darkorchid", color(153, 50, 204));
        colorMap.put("darkred", color(139, 0, 0));
        colorMap.put("darksalmon", color(233, 150, 122));
        colorMap.put("darkseagreen", color(143, 188, 143));
        colorMap.put("darkslateblue", color(72, 61, 139));
        colorMap.put("darkslategray", color(47, 79, 79));
        colorMap.put("darkturquoise", color(0, 206, 209));
        colorMap.put("darkviolet", color(148, 0, 211));
        colorMap.put("deeppink", color(255, 20, 147));
        colorMap.put("deepskyblue", color(0, 191, 255));
        colorMap.put("dimgray", color(105, 105, 105));
        colorMap.put("dodgerblue", color(30, 144, 255));
        colorMap.put("firebrick", color(178, 34, 34));
        colorMap.put("floralwhite", color(255, 250, 240));
        colorMap.put("forestgreen", color(34, 139, 34));
        colorMap.put("fuchsia", color(255, 0, 255));
        colorMap.put("gainsboro", color(220, 220, 220));
        colorMap.put("ghostwhite", color(248, 248, 255));
        colorMap.put("gold", color(255, 215, 0));
        colorMap.put("goldenrod", color(218, 165, 32));
        colorMap.put("gray", color(128, 128, 128));
        colorMap.put("green", color(0, 128, 0));
        colorMap.put("greenyellow", color(173, 255, 47));
        colorMap.put("honeydew", color(240, 255, 240));
        colorMap.put("hotpink", color(255, 105, 180));
        colorMap.put("indianred", color(205, 92, 92));
        colorMap.put("indigo", color(75, 0, 130));
        colorMap.put("ivory", color(255, 255, 240));
        colorMap.put("khaki", color(240, 230, 140));
        colorMap.put("lavender", color(230, 230, 250));
        colorMap.put("lavenderblush", color(255, 240, 245));
        colorMap.put("lawngreen", color(124, 252, 0));
        colorMap.put("lemonchiffon", color(255, 250, 205));
        colorMap.put("lightblue", color(173, 216, 230));
        colorMap.put("lightcoral", color(240, 128, 128));
        colorMap.put("lightcyan", color(224, 255, 255));
        colorMap.put("lightgoldenrodyellow", color(250, 250, 210));
        colorMap.put("lightgreen", color(144, 238, 144));
        colorMap.put("lightgrey", color(211, 211, 211));
        colorMap.put("lightpink", color(255, 182, 193));
        colorMap.put("lightsalmon", color(255, 160, 122));
        colorMap.put("lightseagreen", color(32, 178, 170));
        colorMap.put("lightskyblue", color(135, 206, 250));
        colorMap.put("lightslategray", color(119, 136, 153));
        colorMap.put("lightsteelblue", color(176, 196, 222));
        colorMap.put("lightyellow", color(255, 255, 224));
        colorMap.put("lime", color(0, 255, 0));
        colorMap.put("limegreen", color(50, 205, 50));
        colorMap.put("linen", color(250, 240, 230));
        colorMap.put("magenta", color(255, 0, 255));
        colorMap.put("maroon", color(128, 0, 0));
        colorMap.put("mediumaquamarine", color(102, 205, 170));
        colorMap.put("mediumblue", color(0, 0, 205));
        colorMap.put("mediumorchid", color(186, 85, 211));
        colorMap.put("mediumpurple", color(147, 112, 219));
        colorMap.put("mediumseagreen", color(60, 179, 113));
        colorMap.put("mediumslateblue", color(123, 104, 238));
        colorMap.put("mediumspringgreen", color(0, 250, 154));
        colorMap.put("mediumturquoise", color(72, 209, 204));
        colorMap.put("mediumvioletred", color(199, 21, 133));
        colorMap.put("midnightblue", color(25, 25, 112));
        colorMap.put("mintcream", color(245, 255, 250));
        colorMap.put("mistyrose", color(255, 228, 225));
        colorMap.put("moccasin", color(255, 228, 181));
        colorMap.put("navajowhite", color(255, 222, 173));
        colorMap.put("navy", color(0, 0, 128));
        colorMap.put("oldlace", color(253, 245, 230));
        colorMap.put("olive", color(128, 128, 0));
        colorMap.put("olivedrab", color(107, 142, 35));
        colorMap.put("orange", color(255, 165, 0));
        colorMap.put("orangered", color(255, 69, 0));
        colorMap.put("orchid", color(218, 112, 214));
        colorMap.put("palegoldenrod", color(238, 232, 170));
        colorMap.put("palegreen", color(152, 251, 152));
        colorMap.put("paleturquoise", color(175, 238, 238));
        colorMap.put("palevioletred", color(219, 112, 147));
        colorMap.put("papayawhip", color(255, 239, 213));
        colorMap.put("peachpuff", color(255, 218, 185));
        colorMap.put("peru", color(205, 133, 63));
        colorMap.put("pink", color(255, 192, 203));
        colorMap.put("plum", color(221, 160, 221));
        colorMap.put("powderblue", color(176, 224, 230));
        colorMap.put("purple", color(128, 0, 128));
        colorMap.put("red", color(255, 0, 0));
        colorMap.put("rosybrown", color(188, 143, 143));
        colorMap.put("royalblue", color(65, 105, 225));
        colorMap.put("saddlebrown", color(139, 69, 19));
        colorMap.put("salmon", color(250, 128, 114));
        colorMap.put("sandybrown", color(244, 164, 96));
        colorMap.put("seagreen", color(46, 139, 87));
        colorMap.put("seashell", color(255, 245, 238));
        colorMap.put("sienna", color(160, 82, 45));
        colorMap.put("silver", color(192, 192, 192));
        colorMap.put("skyblue", color(135, 206, 235));
        colorMap.put("slateblue", color(106, 90, 205));
        colorMap.put("slategray", color(112, 128, 144));
        colorMap.put("snow", color(255, 250, 250));
        colorMap.put("springgreen", color(0, 255, 127));
        colorMap.put("steelblue", color(70, 130, 180));
        colorMap.put("tan", color(210, 180, 140));
        colorMap.put("teal", color(0, 128, 128));
        colorMap.put("thistle", color(216, 191, 216));
        colorMap.put("tomato", color(255, 99, 71));
        colorMap.put("turquoise", color(64, 224, 208));
        colorMap.put("violet", color(238, 130, 238));
        colorMap.put("wheat", color(245, 222, 179));
        colorMap.put("white", color(255, 255, 255));
        colorMap.put("whitesmoke", color(245, 245, 245));
        colorMap.put("yellow", color(255, 255, 0));
    }

    static Color color(int r, int g, int b) {
        return new Color(r, g, b);
    }

    /** Returns the color for the specified name, or <code>null</code> if no such color exists. */
    public static Color get(String name) {
        return colorMap.get(name);
    }
}
