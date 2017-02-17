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
package org.geotools.mbstyle.parse;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

/**
 * Converter factory ssupporting web or css color names, requires the use of
 * {@link Hints#COLOR_NAMES} hint with "WEB", "CSS", "X11", or "HTML".
 * <p>
 * There are a few subtle differences between WEB, CSS and X11 colors. this and CSS Colors (for the
 * example the value of "gray" is lighter). The HTML colors are the the standard 16 colors from the
 * Windows VGA palette.
 * </p>
 * 
 * @see <a href=
 *      "http://en.wikipedia.org/wiki/X11_color_names">http://en.wikipedia.org/wiki/X11_color_names</a>
 */
public class NamedColorConverterFactory implements ConverterFactory {
    /** Web Colors as defined by */
    public static final Map<String, Color> WEB_COLORS;
    static {
        Map<String, Color> colors = new HashMap<>();
        WEB_COLORS = Collections.unmodifiableMap(colors);
        colors.put("aliceblue", new Color(0xf0f8ff));
        colors.put("antiquewhite", new Color(0xfaebd7));
        colors.put("aqua", new Color(0x00ffff));
        colors.put("aquamarine", new Color(0x7fffd4));
        colors.put("azure", new Color(0xf0ffff));
        colors.put("beige", new Color(0xf5f5dc));
        colors.put("bisque", new Color(0xffe4c4));
        colors.put("black", new Color(0x000000));
        colors.put("blanchedalmond", new Color(0xffebcd));
        colors.put("blue", new Color(0x0000ff));
        colors.put("blueviolet", new Color(0x8a2be2));
        colors.put("brown", new Color(0xa52a2a));
        colors.put("burlywood", new Color(0xdeb887));
        colors.put("cadetblue", new Color(0x5f9ea0));
        colors.put("chartreuse", new Color(0x7fff00));
        colors.put("chocolate", new Color(0xd2691e));
        colors.put("coral", new Color(0xff7f50));
        colors.put("cornflowerblue", new Color(0x6495ed));
        colors.put("cornsilk", new Color(0xfff8dc));
        colors.put("crimson", new Color(0xdc143c));
        colors.put("cyan", new Color(0x00ffff));
        colors.put("darkblue", new Color(0x00008b));
        colors.put("darkcyan", new Color(0x008b8b));
        colors.put("darkgoldenrod", new Color(0xb8860b));
        colors.put("darkgray", new Color(0xa9a9a9));
        colors.put("darkgreen", new Color(0x006400));
        colors.put("darkgrey", new Color(0xa9a9a9));
        colors.put("darkkhaki", new Color(0xbdb76b));
        colors.put("darkmagenta", new Color(0x8b008b));
        colors.put("darkolivegreen", new Color(0x556b2f));
        colors.put("darkorange", new Color(0xff8c00));
        colors.put("darkorchid", new Color(0x9932cc));
        colors.put("darkred", new Color(0x8b0000));
        colors.put("darksalmon", new Color(0xe9967a));
        colors.put("darkseagreen", new Color(0x8fbc8f));
        colors.put("darkslateblue", new Color(0x483d8b));
        colors.put("darkslategray", new Color(0x2f4f4f));
        colors.put("darkslategrey", new Color(0x2f4f4f));
        colors.put("darkturquoise", new Color(0x00ced1));
        colors.put("darkviolet", new Color(0x9400d3));
        colors.put("deeppink", new Color(0xff1493));
        colors.put("deepskyblue", new Color(0x00bfff));
        colors.put("dimgray", new Color(0x696969));
        colors.put("dimgrey", new Color(0x696969));
        colors.put("dodgerblue", new Color(0x1e90ff));
        colors.put("firebrick", new Color(0xb22222));
        colors.put("floralwhite", new Color(0xfffaf0));
        colors.put("forestgreen", new Color(0x228b22));
        colors.put("fuchsia", new Color(0xff00ff));
        colors.put("gainsboro", new Color(0xdcdcdc));
        colors.put("ghostwhite", new Color(0xf8f8ff));
        colors.put("gold", new Color(0xffd700));
        colors.put("goldenrod", new Color(0xdaa520));
        colors.put("gray", new Color(0x808080));
        colors.put("grey", new Color(0x808080));
        colors.put("green", new Color(0x008000));
        colors.put("greenyellow", new Color(0xadff2f));
        colors.put("honeydew", new Color(0xf0fff0));
        colors.put("hotpink", new Color(0xff69b4));
        colors.put("indianred", new Color(0xcd5c5c));
        colors.put("indigo", new Color(0x4b0082));
        colors.put("ivory", new Color(0xfffff0));
        colors.put("khaki", new Color(0xf0e68c));
        colors.put("lavender", new Color(0xe6e6fa));
        colors.put("lavenderblush", new Color(0xfff0f5));
        colors.put("lawngreen", new Color(0x7cfc00));
        colors.put("lemonchiffon", new Color(0xfffacd));
        colors.put("lightblue", new Color(0xadd8e6));
        colors.put("lightcoral", new Color(0xf08080));
        colors.put("lightcyan", new Color(0xe0ffff));
        colors.put("lightgoldenrodyellow", new Color(0xfafad2));
        colors.put("lightgray", new Color(0xd3d3d3));
        colors.put("lightgreen", new Color(0x90ee90));
        colors.put("lightgrey", new Color(0xd3d3d3));
        colors.put("lightpink", new Color(0xffb6c1));
        colors.put("lightsalmon", new Color(0xffa07a));
        colors.put("lightseagreen", new Color(0x20b2aa));
        colors.put("lightskyblue", new Color(0x87cefa));
        colors.put("lightslategray", new Color(0x778899));
        colors.put("lightslategrey", new Color(0x778899));
        colors.put("lightsteelblue", new Color(0xb0c4de));
        colors.put("lightyellow", new Color(0xffffe0));
        colors.put("lime", new Color(0x00ff00));
        colors.put("limegreen", new Color(0x32cd32));
        colors.put("linen", new Color(0xfaf0e6));
        colors.put("magenta", new Color(0xff00ff));
        colors.put("maroon", new Color(0x800000));
        colors.put("mediumaquamarine", new Color(0x66cdaa));
        colors.put("mediumblue", new Color(0x0000cd));
        colors.put("mediumorchid", new Color(0xba55d3));
        colors.put("mediumpurple", new Color(0x9370db));
        colors.put("mediumseagreen", new Color(0x3cb371));
        colors.put("mediumslateblue", new Color(0x7b68ee));
        colors.put("mediumspringgreen", new Color(0x00fa9a));
        colors.put("mediumturquoise", new Color(0x48d1cc));
        colors.put("mediumvioletred", new Color(0xc71585));
        colors.put("midnightblue", new Color(0x191970));
        colors.put("mintcream", new Color(0xf5fffa));
        colors.put("mistyrose", new Color(0xffe4e1));
        colors.put("moccasin", new Color(0xffe4b5));
        colors.put("navajowhite", new Color(0xffdead));
        colors.put("navy", new Color(0x000080));
        colors.put("oldlace", new Color(0xfdf5e6));
        colors.put("olive", new Color(0x808000));
        colors.put("olivedrab", new Color(0x6b8e23));
        colors.put("orange", new Color(0xffa500)); // CSS 2.1
        colors.put("orangered", new Color(0xff4500));
        colors.put("orchid", new Color(0xda70d6));
        colors.put("palegoldenrod", new Color(0xeee8aa));
        colors.put("palegreen", new Color(0x98fb98));
        colors.put("paleturquoise", new Color(0xafeeee));
        colors.put("palevioletred", new Color(0xdb7093));
        colors.put("papayawhip", new Color(0xffefd5));
        colors.put("peachpuff", new Color(0xffdab9));
        colors.put("peru", new Color(0xcd853f));
        colors.put("pink", new Color(0xffc0cb));
        colors.put("plum", new Color(0xdda0dd));
        colors.put("powderblue", new Color(0xb0e0e6));
        colors.put("purple", new Color(0x800080));
        colors.put("red", new Color(0xff0000));
        colors.put("rosybrown", new Color(0xbc8f8f));
        colors.put("royalblue", new Color(0x4169e1));
        colors.put("saddlebrown", new Color(0x8b4513));
        colors.put("salmon", new Color(0xfa8072));
        colors.put("sandybrown", new Color(0xf4a460));
        colors.put("seagreen", new Color(0x2e8b57));
        colors.put("seashell", new Color(0xfff5ee));
        colors.put("sienna", new Color(0xa0522d));
        colors.put("silver", new Color(0xc0c0c0));
        colors.put("skyblue", new Color(0x87ceeb));
        colors.put("slateblue", new Color(0x6a5acd));
        colors.put("slategray", new Color(0x708090));
        colors.put("slategrey", new Color(0x708090));
        colors.put("snow", new Color(0xfffafa));
        colors.put("springgreen", new Color(0x00ff7f));
        colors.put("steelblue", new Color(0x4682b4));
        colors.put("tan", new Color(0xd2b48c));
        colors.put("teal", new Color(0x008080));
        colors.put("thistle", new Color(0xd8bfd8));
        colors.put("tomato", new Color(0xff6347));
        colors.put("turquoise", new Color(0x40e0d0));
        colors.put("violet", new Color(0xee82ee));
        colors.put("wheat", new Color(0xf5deb3));
        colors.put("white", new Color(0xffffff));
        colors.put("whitesmoke", new Color(0xf5f5f5));
        colors.put("yellow", new Color(0xffff00));
        colors.put("yellowgreen", new Color(0x9acd32));
    }

    /** CSS Colors (as used by CSS language). */
    public static final Map<String, Color> CSS_COLORS;
    static {
        Map<String, Color> colors = new HashMap<>();
        CSS_COLORS = Collections.unmodifiableMap(colors);

        colors.putAll(WEB_COLORS);
        colors.put("rebeccapurple", new Color(0x663399)); // CSS4 Colors

    }

    /** HTML Colors (as used for web pages). */
    public static final Map<String, Color> HTML_COLORS;
    static {
        HashMap<String, Color> colors = new HashMap<>();
        HTML_COLORS = Collections.unmodifiableMap(colors);
        colors.put("white", new Color(0xFFFFFF));
        colors.put("silver", new Color(0xC0C0C0));
        colors.put("gray", new Color(0x808080));
        colors.put("black", new Color(0x000000));
        colors.put("red", new Color(0xFF0000));
        colors.put("maroon", new Color(0x800000));
        colors.put("yellow", new Color(0xFFFF00));
        colors.put("olive", new Color(0x808000));
        colors.put("lime", new Color(0x00FF00));
        colors.put("green", new Color(0x008000));
        colors.put("aqua", new Color(0x00FFFF));
        colors.put("teal", new Color(0x008080));
        colors.put("blue", new Color(0x0000FF));
        colors.put("navy", new Color(0x000080));
        colors.put("fuchsia", new Color(0xFF00FF));
        colors.put("purple", new Color(0x800080));
    }

    /**
     * X11 Colors (as used by SVG parameters).
     * <p>
     * There are a few subtle differences betwen this and CSS Colors (for the example the value of
     * "gray" is lighter).
     * </p>
     * 
     * @see <a href="http://en.wikipedia.org/wiki/X11_color_names">X11 color names</a> (Wikapedia)
     */
    public static final Map<String, Color> X11_COLORS;
    static {
        HashMap<String, Color> colors = new HashMap<>();
        X11_COLORS = Collections.unmodifiableMap(colors);
        colors.put("aliceblue", new Color(240, 248, 255));
        colors.put("yellowgreen", new Color(154, 205, 50));
        colors.put("antiquewhite", new Color(250, 235, 215));
        colors.put("aqua", new Color(0, 255, 255));
        colors.put("aquamarine", new Color(127, 255, 212));
        colors.put("azure", new Color(240, 255, 255));
        colors.put("beige", new Color(245, 245, 220));
        colors.put("bisque", new Color(255, 228, 196));
        colors.put("black", new Color(0, 0, 0));
        colors.put("blanchedalmond", new Color(255, 235, 205));
        colors.put("blue", new Color(0, 0, 255));
        colors.put("blueviolet", new Color(138, 43, 226));
        colors.put("brown", new Color(165, 42, 42));
        colors.put("burlywood", new Color(222, 184, 135));
        colors.put("cadetblue", new Color(95, 158, 160));
        colors.put("chartreuse", new Color(127, 255, 0));
        colors.put("chocolate", new Color(210, 105, 30));
        colors.put("coral", new Color(255, 127, 80));
        colors.put("cornflowerblue", new Color(100, 149, 237));
        colors.put("cornsilk", new Color(255, 248, 220));
        colors.put("crimson", new Color(220, 20, 60));
        colors.put("cyan", new Color(0, 255, 255));
        colors.put("darkblue", new Color(0, 0, 139));
        colors.put("darkcyan", new Color(0, 139, 139));
        colors.put("darkgoldenrod", new Color(184, 134, 11));
        colors.put("darkgray", new Color(169, 169, 169));
        colors.put("darkgreen", new Color(0, 100, 0));
        colors.put("darkkhaki", new Color(189, 183, 107));
        colors.put("darkmagenta", new Color(139, 0, 139));
        colors.put("darkolivegreen", new Color(85, 107, 47));
        colors.put("darkorange", new Color(255, 140, 0));
        colors.put("darkorchid", new Color(153, 50, 204));
        colors.put("darkred", new Color(139, 0, 0));
        colors.put("darksalmon", new Color(233, 150, 122));
        colors.put("darkseagreen", new Color(143, 188, 143));
        colors.put("darkslateblue", new Color(72, 61, 139));
        colors.put("darkslategray", new Color(47, 79, 79));
        colors.put("darkturquoise", new Color(0, 206, 209));
        colors.put("darkviolet", new Color(148, 0, 211));
        colors.put("deeppink", new Color(255, 20, 147));
        colors.put("deepskyblue", new Color(0, 191, 255));
        colors.put("dimgray", new Color(105, 105, 105));
        colors.put("dodgerblue", new Color(30, 144, 255));
        colors.put("firebrick", new Color(178, 34, 34));
        colors.put("floralwhite", new Color(255, 250, 240));
        colors.put("forestgreen", new Color(34, 139, 34));
        colors.put("fuchsia", new Color(255, 0, 255));
        colors.put("gainsboro", new Color(220, 220, 220));
        colors.put("ghostwhite", new Color(248, 248, 255));
        colors.put("gold", new Color(255, 215, 0));
        colors.put("goldenrod", new Color(218, 165, 32));
        colors.put("gray", new Color(128, 128, 128));
        colors.put("green", new Color(0, 128, 0));
        colors.put("greenyellow", new Color(173, 255, 47));
        colors.put("honeydew", new Color(240, 255, 240));
        colors.put("hotpink", new Color(255, 105, 180));
        colors.put("indianred", new Color(205, 92, 92));
        colors.put("indigo", new Color(75, 0, 130));
        colors.put("ivory", new Color(255, 255, 240));
        colors.put("khaki", new Color(240, 230, 140));
        colors.put("lavender", new Color(230, 230, 250));
        colors.put("lavenderblush", new Color(255, 240, 245));
        colors.put("lawngreen", new Color(124, 252, 0));
        colors.put("lemonchiffon", new Color(255, 250, 205));
        colors.put("lightblue", new Color(173, 216, 230));
        colors.put("lightcoral", new Color(240, 128, 128));
        colors.put("lightcyan", new Color(224, 255, 255));
        colors.put("lightgoldenrodyellow", new Color(250, 250, 210));
        colors.put("lightgreen", new Color(144, 238, 144));
        colors.put("lightgrey", new Color(211, 211, 211));
        colors.put("lightpink", new Color(255, 182, 193));
        colors.put("lightsalmon", new Color(255, 160, 122));
        colors.put("lightseagreen", new Color(32, 178, 170));
        colors.put("lightskyblue", new Color(135, 206, 250));
        colors.put("lightslategray", new Color(119, 136, 153));
        colors.put("lightsteelblue", new Color(176, 196, 222));
        colors.put("lightyellow", new Color(255, 255, 224));
        colors.put("lime", new Color(0, 255, 0));
        colors.put("limegreen", new Color(50, 205, 50));
        colors.put("linen", new Color(250, 240, 230));
        colors.put("magenta", new Color(255, 0, 255));
        colors.put("maroon", new Color(128, 0, 0));
        colors.put("mediumaquamarine", new Color(102, 205, 170));
        colors.put("mediumblue", new Color(0, 0, 205));
        colors.put("mediumorchid", new Color(186, 85, 211));
        colors.put("mediumpurple", new Color(147, 112, 219));
        colors.put("mediumseagreen", new Color(60, 179, 113));
        colors.put("mediumslateblue", new Color(123, 104, 238));
        colors.put("mediumspringgreen", new Color(0, 250, 154));
        colors.put("mediumturquoise", new Color(72, 209, 204));
        colors.put("mediumvioletred", new Color(199, 21, 133));
        colors.put("midnightblue", new Color(25, 25, 112));
        colors.put("mintcream", new Color(245, 255, 250));
        colors.put("mistyrose", new Color(255, 228, 225));
        colors.put("moccasin", new Color(255, 228, 181));
        colors.put("navajowhite", new Color(255, 222, 173));
        colors.put("navy", new Color(0, 0, 128));
        colors.put("oldlace", new Color(253, 245, 230));
        colors.put("olive", new Color(128, 128, 0));
        colors.put("olivedrab", new Color(107, 142, 35));
        colors.put("orange", new Color(255, 165, 0)); // CSS 2.1
        colors.put("orangered", new Color(255, 69, 0));
        colors.put("orchid", new Color(218, 112, 214));
        colors.put("palegoldenrod", new Color(238, 232, 170));
        colors.put("palegreen", new Color(152, 251, 152));
        colors.put("paleturquoise", new Color(175, 238, 238));
        colors.put("palevioletred", new Color(219, 112, 147));
        colors.put("papayawhip", new Color(255, 239, 213));
        colors.put("peachpuff", new Color(255, 218, 185));
        colors.put("peru", new Color(205, 133, 63));
        colors.put("pink", new Color(255, 192, 203));
        colors.put("plum", new Color(221, 160, 221));
        colors.put("powderblue", new Color(176, 224, 230));
        colors.put("purple", new Color(128, 0, 128));
        colors.put("red", new Color(255, 0, 0));
        colors.put("rosybrown", new Color(188, 143, 143));
        colors.put("royalblue", new Color(65, 105, 225));
        colors.put("saddlebrown", new Color(139, 69, 19));
        colors.put("salmon", new Color(250, 128, 114));
        colors.put("sandybrown", new Color(244, 164, 96));
        colors.put("seagreen", new Color(46, 139, 87));
        colors.put("seashell", new Color(255, 245, 238));
        colors.put("sienna", new Color(160, 82, 45));
        colors.put("silver", new Color(192, 192, 192));
        colors.put("skyblue", new Color(135, 206, 235));
        colors.put("slateblue", new Color(106, 90, 205));
        colors.put("slategray", new Color(112, 128, 144));
        colors.put("snow", new Color(255, 250, 250));
        colors.put("springgreen", new Color(0, 255, 127));
        colors.put("steelblue", new Color(70, 130, 180));
        colors.put("tan", new Color(210, 180, 140));
        colors.put("teal", new Color(0, 128, 128));
        colors.put("thistle", new Color(216, 191, 216));
        colors.put("tomato", new Color(255, 99, 71));
        colors.put("turquoise", new Color(64, 224, 208));
        colors.put("violet", new Color(238, 130, 238));
        colors.put("wheat", new Color(245, 222, 179));
        colors.put("white", new Color(255, 255, 255));
        colors.put("whitesmoke", new Color(245, 245, 245));
        colors.put("yellow", new Color(255, 255, 0));
    }

    static final Converter CONVERT_WEB = new ColorNameConverter(WEB_COLORS);

    static final Converter CONVERT_CSS = new ColorNameConverter(CSS_COLORS);

    static final Converter CONVERT_HTML = new ColorNameConverter(HTML_COLORS);

    static final Converter CONVERT_X11 = new ColorNameConverter(X11_COLORS);

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (hints == null || !hints.containsKey(Hints.COLOR_NAMES)) {
            return null; // nothing to do
        }
        String hint = (String) hints.get(Hints.COLOR_NAMES);

        if (target.equals(Color.class) && source.equals(String.class)) {
            if ("WEB".equals(hint)) {
                return CONVERT_WEB;
            } else if ("CSS".equals(hint)) {
                return CONVERT_CSS;
            } else if ("HTML".equals(hint)) {
                return CONVERT_HTML;
            } else if ("X11".equals(hint)) {
                return CONVERT_X11;
            }
        }
        return null;
    }

    /**
     * Convert making use of a map of color definition.
     * 
     * @author Jody Garnett (Boundless)
     */
    static class ColorNameConverter implements Converter {

        final private Map<String, Color> colors;

        ColorNameConverter(Map<String, Color> colors) {
            this.colors = colors;
        }

        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            String name = (String) source;
            String key = name.toLowerCase();
            if (colors.containsKey(key)) {
                return target.cast(colors.get(key));
            }
            return null; // not defined
        }
    }
}