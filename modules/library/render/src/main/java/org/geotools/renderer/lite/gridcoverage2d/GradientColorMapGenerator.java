package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.filter.FilterFactoryImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.ColorMapImpl;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.Utilities;
import org.opengis.filter.FilterFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A class mainly used to parse an SVG file and create a ColorMap on top of the 
 * LinearGradientEntry contained on it, or create a ColorMap on top of a ";" separated values list 
 * of colors such as: rgb(0,0,255);rgb(0,255,0);rgb(255,0,0);... or
 * #0000FF;#00FF00;#FF0000 as an instance.
 *  * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 */
public class GradientColorMapGenerator {

    public static final String RGB_INLINEVALUE_MARKER = "rgb(";
    
    public static final String HEX_INLINEVALUE_MARKER = "#";

    private LinearGradientEntry[] entries;

    private static SoftValueHashMap<String, GradientColorMapGenerator> cache = new SoftValueHashMap<String, GradientColorMapGenerator>();

    private GradientColorMapGenerator(LinearGradientEntry[] entries) {
        this.entries = entries;
    }

    /**
     * Generate a {@link ColorMap} object, by updating the ColorMapEntries quantities on top of the min and max values reported here.
     * @param min
     * @param max
     * @return
     */
    public ColorMap generateColorMap(double min, double max) {
        final int numEntries = entries.length;
        final double range = max - min;
        boolean intervals = false;

        // Preliminar check on intervals vs ramp
        for (int i = 0; i < numEntries - 2; i += 2) {
            if (Double.compare(entries[i + 1].percentage, entries[i + 2].percentage) == 0) {
                intervals = true;
            } else {
                intervals = false;
            }
        }
        ColorMap colorMap = new ColorMapImpl();

        // Adding transparent color entry before the min
        double start = min - (intervals ? 0 : 1E-2);
        ColorMapEntry entry = entries[0].getColorMapEntry(start);
        entry.setOpacity(filterFactory.literal(0));
        colorMap.addColorMapEntry(entry);

        if (intervals) {
            colorMap.setType(ColorMap.TYPE_INTERVALS);
            for (int i = 1; i < numEntries - 1; i += 2) {
                colorMap.addColorMapEntry(entries[i].getColorMapEntry(min, range));
            }
        } else {
            colorMap.setType(ColorMap.TYPE_RAMP);
            for (int i = 0; i < numEntries - 1; i ++) {
                colorMap.addColorMapEntry(entries[i].getColorMapEntry(min, range));
            }
        }
        colorMap.addColorMapEntry(entries[numEntries - 1].getColorMapEntry(max));

        // Adding transparent color entry after the max
        ColorMapEntry entryEnd = entries[numEntries - 1].getColorMapEntry(max + 1E-2);
        entryEnd.setOpacity(filterFactory.literal(0));
        colorMap.addColorMapEntry(entryEnd);

        return colorMap;
    }

    private static FilterFactory filterFactory = new FilterFactoryImpl();

    /**
     * A class representing an SVG LinearGradient color entry
     * 
     * A typical SVG linear gradient is structured like this:
     *  
     *  <linearGradient id="GPS-Fire-Dust-Blended" gradientUnits="objectBoundingBox" spreadMethod="pad" x1="0%" x2="100%" y1="0%" y2="0%">
     *   <stop offset="0.00%" stop-color="rgb(25,9,8)" stop-opacity="1.0000"/>
     *   <stop offset="14.14%" stop-color="rgb(51,43,43)" stop-opacity="1.0000"/>
     *   ............................
     * 
     * @author Daniele Romagnoli, GeoSolutions SAS
     */
    static class LinearGradientEntry {
        public LinearGradientEntry(double percentage, Color color, double opacity) {
            this.percentage = percentage;
            this.opacity = opacity;
            this.color = color; 
        }

        private double opacity;
        private double percentage;
        private Color color;

        private ColorMapEntry getColorMapEntry(double value) {
            return getColorMapEntry(value, Double.NaN);
        }

        private ColorMapEntry getColorMapEntry(double min, double range) {
            ColorMapEntry entry = new ColorMapEntryImpl();
            entry.setOpacity(filterFactory.literal(opacity));
            entry.setColor(filterFactory.literal(toHexColor(color)));
            entry.setQuantity(filterFactory.literal(min + (Double.isNaN(range) ? 0 : (percentage*range))));
            return entry;
        }

        /** 
         * Return an HEX representation of a Color
         * @param color
         * @return
         */
        private static String toHexColor(final Color color) {
            Utilities.ensureNonNull("color", color);
            String hexColour = Integer.toHexString(color.getRGB() & 0xffffff);
            return "#" + hexColour;
        }
    }

    /**
     * Get an SVG ColorMap generator for the specified file
     * @param file
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static GradientColorMapGenerator getColorMapGenerator(final File file) throws SAXException, IOException, ParserConfigurationException {
        GradientColorMapGenerator generator = null;
        Utilities.ensureNonNull("file", file);
        final String identifier = file.getAbsolutePath();
        synchronized (cache) {
            if (cache.containsKey(identifier)) {
                generator = cache.get(identifier);
            } else {
                // create the granule coverageDescriptor
                generator = parseSVG(file);
                cache.put(identifier, generator);
            }
        }
        return generator;
    }
    
    /**
     * Get an SVG ColorMap generator for the specified file
     * @param a ";" separated list of colors in the form rgb(r0,g0,b0);rgb(r1,g1,b1);... or #RRGGBB;#RRGGBB;#RRGGBB;...
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static GradientColorMapGenerator getColorMapGenerator(String colorValues) throws IOException, ParserConfigurationException {
        Utilities.ensureNonNull("colorValues", colorValues);
        if (colorValues.startsWith(RGB_INLINEVALUE_MARKER) || colorValues.startsWith(HEX_INLINEVALUE_MARKER)) {
            String rampType = "ramp";
            if (colorValues.contains(":")) {
                final int rampTypeIndex = colorValues.indexOf(":");
                rampType = colorValues.substring(rampTypeIndex + 1);
                colorValues = colorValues.substring(0, rampTypeIndex);
            }
            String colors[] = colorValues.split(";");
            final int numEntries = colors.length;
            LinearGradientEntry[] entries = new LinearGradientEntry[numEntries];
            final double step = 1d / (numEntries-1);
            for (int i=0; i<numEntries; i++) {
                entries[i] = new LinearGradientEntry(step*i, createColor(colors[i]), 1);
            }
            GradientColorMapGenerator generator = new GradientColorMapGenerator(entries);
            return generator;
            
        } else {
            throw new IOException("Unable to parse the specified colors: " + colorValues);
        }
    }

    /**
     * Parse an SVG xmlFile
     * @param xmlFile
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private static GradientColorMapGenerator parseSVG(final File xmlFile) throws SAXException, IOException, ParserConfigurationException {
        Utilities.ensureNonNull("xmlFile", xmlFile);
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        final Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        final NodeList nList = doc.getElementsByTagName("stop");

        final int numEntries = nList.getLength();
        double percentage;
        double opacity;
        Color color;
        // Setup the Gradient Entries
        LinearGradientEntry[] gradientEntries = new LinearGradientEntry[numEntries];
        for (int i = 0; i < numEntries; i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String offset = element.getAttribute("offset");
                        String stopColor = element.getAttribute("stop-color");
                        String stopOpacity = element.getAttribute("stop-opacity");
                        percentage = Double.parseDouble(offset.substring(0, offset.length() -1)) / 100d;
                        opacity = Double.parseDouble(stopOpacity);
                        color = createColor (stopColor); 
                        gradientEntries[i] = new LinearGradientEntry(percentage, color, opacity);
                }
        }

        // Setup a GradientColorMapGenerator
        GradientColorMapGenerator generator = new GradientColorMapGenerator(gradientEntries);
        return generator;
    }

    /** 
     * Create a {@link Color} from a color String which may be an SVG color: rgb(R0,G0,B0) or hex color: #RRGGBB
     * @param the String color representation
     * @return the {@link Color} instance related to that string definition
     */
    private static Color createColor(String color) {
        Utilities.ensureNonNull("color", color);
        if (color.contains("rgb")) {
            String colorString  = color.substring(4, color.length()-1);
            String rgbs[] = colorString.split(",");
            return new Color(Integer.parseInt(rgbs[0]), Integer.parseInt(rgbs[1]), Integer.parseInt(rgbs[2]));
        } else if (color.startsWith("#") && color.length() == 7) {
            // Try to parse it as an HEX code
            return hex2Rgb(color);
        }
        throw new UnsupportedOperationException("Support for the following color ins't currently supported: " + color);
    }

    /**
     * Convert an hex color representation to a {@link Color}
     * @param colorStr
     * @return the {@link Color} instance related to that color HEX string
     */
    public static Color hex2Rgb(String colorStr) {
        return new Color(
            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }
}

