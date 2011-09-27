package org.geotools.coverage.grid.imageio.geotiff.metadata;

import it.geosolutions.imageio.plugins.tiff.GeoTIFFTagSet;

import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.metadata.IIOMetadataNode;

import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffConstants;
import org.geotools.coverage.grid.io.imageio.geotiff.PixelScale;
import org.geotools.coverage.grid.io.imageio.geotiff.TiePoint;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 *
 * @source $URL$
 */
public class GeoTiffMetadataUtilities {

    /** {@link Logger}. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffMetadataUtilities.class);

    static PixelScale calculateModelPixelScales(Node rootNode) {
        final double[] pixScales = getTiffDoubles(getTiffField(rootNode,
                GeoTIFFTagSet.TAG_MODEL_PIXEL_SCALE));
        if (pixScales == null||pixScales.length<=0) {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE,"Unable to find the model pixel scale tag");
            return null;
        }
    
        PixelScale retVal = new PixelScale();
        for (int i = 0; i < pixScales.length; i++)
            switch (i) {
            case 0:
                retVal.setScaleX(pixScales[i]);
                break;
            case 1:
                retVal.setScaleY(pixScales[i]);
                break;
            case 2:
                retVal.setScaleZ(pixScales[i]);
                break;
            }
        return retVal;
    
    }

    static TiePoint[] calculateTiePoints(Node rootNode) {
        IIOMetadataNode node = getTiffField(rootNode, GeoTIFFTagSet.TAG_MODEL_TIE_POINT);
        if (node == null) {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE,"Unable to find the model tie points tag");
        }
        final double tiePoints[] = getTiffDoubles(node);
        if (tiePoints == null || tiePoints.length <= 0) {
            return null;
        }
        final int numTiePoints = tiePoints.length / 6;
        final TiePoint retVal[] = new TiePoint[numTiePoints];
        int initialIndex = 0;
        for (int i = 0; i < numTiePoints; i++) {
            initialIndex = i * 6;
            retVal[i] = new TiePoint(tiePoints[initialIndex], tiePoints[initialIndex + 1],
                    tiePoints[initialIndex + 2], tiePoints[initialIndex + 3],
                    tiePoints[initialIndex + 4], tiePoints[initialIndex + 5]);
        }
        return retVal;
    
    }

    static double calculateNoData(Node rootNode) {
        final IIOMetadataNode noDataNode = getTiffField(rootNode, GeoTiffConstants.TIFFTAG_NODATA);
        if (noDataNode == null) {
            return Double.NaN;
        }
        final String noData = getTiffAscii(noDataNode);
        if (noData == null) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(noData);
        } catch (NumberFormatException nfe) {
            if(LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO,nfe.getLocalizedMessage(),nfe);
            return Double.NaN;
        }
    
    }

    static AffineTransform calculateModelTransformation(Node rootNode) {
    final IIOMetadataNode node = getTiffField(rootNode, GeoTIFFTagSet.TAG_MODEL_TRANSFORMATION);
    if (node == null) {
        if(LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE,"Unable to find the model transfomation tag");
        return null;
    }
    final double[] modelTransformation = getTiffDoubles(node);
    if (modelTransformation == null) {
        return null;
    }
    AffineTransform transform = null;
    if (modelTransformation.length == 9) {
        transform = new AffineTransform(
                modelTransformation[0],
                modelTransformation[4],
                modelTransformation[1],
                modelTransformation[5], 
                modelTransformation[6],
                modelTransformation[7]);
    } else if (modelTransformation.length == 16) {
        transform = new AffineTransform(
                modelTransformation[0],
                modelTransformation[4],
                modelTransformation[1],
                modelTransformation[5],
                modelTransformation[3],
                modelTransformation[7]);
    }
    
    return transform;
    
    }

    /**
     * Gets the value attribute of the given Node.
     * 
     * @param node
     *            A Node containing a value attribute, for example the node &ltTIFFShort
     *            value=&quot123&quot&gt
     * 
     * @return A String containing the text from the value attribute. In the above example, the
     *         string would be 123
     */
    static String getValueAttribute(Node node) {
        return node.getAttributes().getNamedItem(GeoTiffConstants.VALUE_ATTRIBUTE).getNodeValue();
    }

    /**
     * Gets the value attribute's contents and parses it as an int
     * 
     * @param node
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    static int getIntValueAttribute(Node node) {
        return Integer.parseInt(getValueAttribute(node));
    }

    /**
     * Gets a TIFFField node with the given tag number. This is done by searching for a TIFFField
     * with attribute number whose value is the specified tag value.
     * 
     * @param tag DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    static IIOMetadataNode getTiffField(Node rootNode, final int tag) {
        Node node = rootNode.getFirstChild();
        final String tag_=Integer.toString(tag);
        if (node != null){
            node = node.getFirstChild();
            for (; node != null; node = node.getNextSibling()) {
                Node number = node.getAttributes().getNamedItem(GeoTiffConstants.NUMBER_ATTRIBUTE);
                // TODO check this optimization
//                if (number != null && tag == Integer.parseInt(number.getNodeValue())) {
                if (number != null && tag_.equalsIgnoreCase(number.getNodeValue())) {
                    return (IIOMetadataNode) node;
                }
            }
        }
        return null;
    }

    /**
     * Gets a single TIFFShort value at the given index.
     * 
     * @param tiffField
     *            An IIOMetadataNode pointing to a TIFFField element that contains a TIFFShorts
     *            element.
     * @param index
     *            The 0-based index of the desired short value
     * 
     * @return DOCUMENT ME!
     */
    static int getTiffShort(final IIOMetadataNode tiffField, final int index) {
    
        return getIntValueAttribute(((IIOMetadataNode) tiffField.getFirstChild())
                .getElementsByTagName(GeoTiffConstants.GEOTIFF_SHORT_TAG).item(index));
    
    }

    /**
     * Gets an array of double values from a TIFFDoubles TIFFField.
     * 
     * @param tiffField
     *            An IIOMetadataNode pointing to a TIFFField element that contains a TIFFDoubles
     *            element.
     * 
     * @return DOCUMENT ME!
     */
    static double[] getTiffDoubles(final IIOMetadataNode tiffField) {
    
        if (tiffField == null) {
            return null;
        }
        final NodeList doubles = ((IIOMetadataNode) tiffField.getFirstChild())
                .getElementsByTagName(GeoTiffConstants.GEOTIFF_DOUBLE_TAG);
        final int length = doubles.getLength();
        final double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = Double.parseDouble(getValueAttribute(doubles.item(i)));
        }
    
        return result;
    }

    /**
     * Gets a portion of a TIFFAscii string with the specified start character and length;
     * 
     * @param tiffField
     *            An IIOMetadataNode pointing to a TIFFField element that contains a TIFFAsciis
     *            element. This element should contain a single TiffAscii element.
     * @param start
     *            DOCUMENT ME!
     * @param length
     *            DOCUMENT ME!
     * 
     * @return A substring of the value contained in the TIFFAscii node, with the final '|'
     *         character removed.
     */
    static String getTiffAscii(final IIOMetadataNode tiffField, int start, int length) {
    
        // there should be only one, so get the first
        // GeoTIFFWritingUtilities specification places a vertical bar '|' in
        // place of \0
        // null delimiters so drop off the vertical bar for Java Strings
        final String valueAttribute = getValueAttribute(((IIOMetadataNode) tiffField
                .getFirstChild()).getElementsByTagName(GeoTiffConstants.GEOTIFF_ASCII_TAG).item(0));
        if (start == -1)
            start = 0;
        if (length == -1)
            length = valueAttribute.length() + 1;
        return valueAttribute.substring(start, start + length - 1);
    
    }

    /**
     * Gets the TIFFAscii string
     * 
     * @param tiffField
     *            An IIOMetadataNode pointing to a TIFFField element that contains a TIFFAsciis
     *            element. This element should contain a single TIFFAscii element.
     * 
     * @return The value contained in the TIFFAscii node, with the final '|' character removed.
     */
    static String getTiffAscii(final IIOMetadataNode tiffField) {
        return getTiffAscii(tiffField, -1, -1);
    }

}
