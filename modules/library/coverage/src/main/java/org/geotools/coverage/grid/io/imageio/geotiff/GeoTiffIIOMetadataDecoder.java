/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
/*
* (c) 2004 Mike Nidel
*
* Take, Modify, Distribute freely
* Buy, Sell, Pass it off as your own
*

This code is now made available under the MIT License:

The MIT License (MIT)

Copyright (c) 2004 by Mike Nidel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

* If you feel like it, send any suggestions for improvement or
* bug fixes, or modified source code to mike 'at' gelbin.org
*
* Do not taunt Happy Fun Ball.
*
*/
package org.geotools.coverage.grid.io.imageio.geotiff;

import it.geosolutions.imageio.plugins.tiff.GeoTIFFTagSet;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffGCSCodes;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class provides an abstraction from the details of TIFF data access for the purpose of
 * retrieving GeoTIFFWritingUtilities metadata from an image.
 *
 * <p>All of the GeoKey values are included here as constants, and the portions of the
 * GeoTIFFWritingUtilities specification pertaining to each have been copied for easy access.
 *
 * <p>The majority of the possible GeoKey values and their meanings are NOT reproduced here. Only
 * the most important GeoKey code values have been copied, for others see the specification.
 *
 * <p>Convenience methods have been included to retrieve the various TIFFFields that are not part of
 * the GeoKey directory, such as the Model Transformation and Model TiePoints. Retrieving a GeoKey
 * from the GeoKey directory is a bit more specialized and requires knowledge of the correct key
 * code.
 *
 * <p>Making use of the geographic metadata still requires some basic understanding of the GeoKey
 * values that is not provided here.
 *
 * <p>For more information see the GeoTIFFWritingUtilities specification at
 * http://www.remotesensing.org/geotiff/spec/geotiffhome.html
 *
 * @author Mike Nidel
 * @author Simone Giannecchini, GeoSolutions
 */
public final class GeoTiffIIOMetadataDecoder {

    private final IIOMetadata iioMetadata;

    private final Map<Integer, GeoKeyEntry> geoKeys;

    private int geoKeyDirVersion;

    private int geoKeyRevision;

    private int geoKeyMinorRevision;

    private int geoKeyDirTagsNum;

    private final PixelScale pixelScale;

    private final TiePoint[] tiePoints;

    private final double noData;

    private final AffineTransform modelTransformation;

    /**
     * The constructor builds a metadata adapter for the image metadata root IIOMetadataNode.
     *
     * @param imageMetadata The image metadata
     */
    public GeoTiffIIOMetadataDecoder(final IIOMetadata imageMetadata) {
        if (imageMetadata == null) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "imageMetadata"));
        }
        iioMetadata = imageMetadata;
        // getting the image metadata root node.
        Node rootNode = getRootNode();
        if (rootNode == null) {
            throw new NullPointerException("Unable to retrieve metadata");
        }

        // getting the geokey ddirectory
        IIOMetadataNode geoKeyDir = getTiffField(rootNode, GeoTIFFTagSet.TAG_GEO_KEY_DIRECTORY);
        geoKeys = new HashMap<Integer, GeoKeyEntry>();
        if (geoKeyDir != null) {
            NodeList geoKeyDirEntries = geoKeyDir.getFirstChild().getChildNodes();
            int length = geoKeyDirEntries.getLength();
            for (int i = 4; i < length; i += 4) {
                int keyID = getIntValueAttribute(geoKeyDirEntries.item(i));
                GeoKeyEntry key =
                        new GeoKeyEntry(
                                keyID, // key
                                getIntValueAttribute(geoKeyDirEntries.item(i + 1)), // location
                                getIntValueAttribute(geoKeyDirEntries.item(i + 2)), // count
                                getIntValueAttribute(geoKeyDirEntries.item(i + 3))); // offset

                if (!geoKeys.containsKey(keyID)) {
                    geoKeys.put(keyID, key);
                }
            }
            // GeoKeyDirVersion and the other parameters
            geoKeyDirVersion =
                    getTiffShort(geoKeyDir, GeoTiffGCSCodes.GEO_KEY_DIRECTORY_VERSION_INDEX);
            geoKeyRevision = getTiffShort(geoKeyDir, GeoTiffGCSCodes.GEO_KEY_REVISION_INDEX);
            if (geoKeyRevision != 1) {
                geoKeyRevision = 1;
                // I had to remove this because I did not want to have wrong
                // revision numbers blocking us.
                // throw new UnsupportedOperationException("Unsupported revision");
            }
            geoKeyMinorRevision =
                    getTiffShort(geoKeyDir, GeoTiffGCSCodes.GEO_KEY_MINOR_REVISION_INDEX);
            // loading the number of geokeys inside the geokeydirectory
            geoKeyDirTagsNum = getTiffShort(geoKeyDir, GeoTiffGCSCodes.GEO_KEY_NUM_KEYS_INDEX);
        }

        pixelScale = calculateModelPixelScales(rootNode);
        tiePoints = calculateTiePoints(rootNode);
        noData = calculateNoData(rootNode);
        modelTransformation = calculateModelTransformation(rootNode);
    }

    /**
     * Gets the version of the GeoKey directory. This is typically a value of 1 and can be used to
     * check that the data is of a valid format.
     */
    public int getGeoKeyDirectoryVersion() {
        // now get the value from the correct TIFFShort location
        return geoKeyDirVersion;
    }

    /** Gets the revision number of the GeoKeys in this metadata. */
    public int getGeoKeyRevision() {
        // Get the value from the correct TIFFShort
        return geoKeyRevision;
    }

    /** Gets the minor revision number of the GeoKeys in this metadata. */
    public int getGeoKeyMinorRevision() {
        // Get the value from the correct TIFFShort
        return geoKeyMinorRevision;
    }

    /** Gets the number of GeoKeys in the geokeys directory. */
    public int getNumGeoKeys() {
        return geoKeyDirTagsNum;
    }

    /**
     * Gets a GeoKey value as a String. This implementation should be &quotquiet&quot in the sense
     * that it should not throw any exceptions but only return null in the event that the data
     * organization is not as expected.
     *
     * @param keyID The numeric ID of the GeoKey
     * @return A string representing the value, or null if the key was not found.
     */
    public String getGeoKey(final int keyID) {

        final GeoKeyEntry rec = getGeoKeyRecord(keyID);
        if (rec == null) {
            return null;
        }
        if (rec.getTiffTagLocation() == 0) {
            // value is stored directly in the GeoKey record
            return String.valueOf(rec.getValueOffset());
        }

        // value is stored externally
        // get the TIFF field where the data is actually stored
        final IIOMetadataNode field = getTiffField(getRootNode(), rec.getTiffTagLocation());
        if (field == null) {
            return null;
        }

        final Node sequence = field.getFirstChild();
        if (sequence == null) {
            return null;
        }

        return sequence.getNodeName().equals(GeoTiffConstants.GEOTIFF_ASCIIS_TAG)
                ? getTiffAscii((IIOMetadataNode) sequence, rec.getValueOffset(), rec.getCount())
                : getValueAttribute(sequence.getChildNodes().item(rec.getValueOffset()));
    }

    /**
     * Gets a record containing the four TIFFShort values for a geokey entry. For more information
     * see the GeoTIFFWritingUtilities specification.
     *
     * @return the record with the given keyID, or null if none is found
     */
    public GeoKeyEntry getGeoKeyRecord(int keyID) {
        return geoKeys.get(keyID);
    }

    /** Return the GeoKeys. */
    public Collection<GeoKeyEntry> getGeoKeys() {
        return geoKeys.values();
    }

    private PixelScale calculateModelPixelScales(Node rootNode) {
        final double[] pixScales =
                getTiffDoubles(getTiffField(rootNode, GeoTIFFTagSet.TAG_MODEL_PIXEL_SCALE));
        if (pixScales == null) {
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

    /** Gets the model pixel scales from the correct TIFFField */
    public PixelScale getModelPixelScales() {
        return pixelScale;
    }

    /**
     * Gets the model tie points from the appropriate TIFFField
     *
     * @return the tie points, or null if not found
     */
    public TiePoint[] getModelTiePoints() {
        return tiePoints;
    }

    private TiePoint[] calculateTiePoints(Node rootNode) {
        IIOMetadataNode node = getTiffField(rootNode, GeoTIFFTagSet.TAG_MODEL_TIE_POINT);
        if (node == null) {
            return null;
        }
        final double[] tiePoints = getTiffDoubles(node);
        if (tiePoints == null || tiePoints.length <= 0) {
            return null;
        }
        final int numTiePoints = tiePoints.length / 6;
        final TiePoint[] retVal = new TiePoint[numTiePoints];
        int initialIndex = 0;
        for (int i = 0; i < numTiePoints; i++) {
            initialIndex = i * 6;
            retVal[i] =
                    new TiePoint(
                            tiePoints[initialIndex],
                            tiePoints[initialIndex + 1],
                            tiePoints[initialIndex + 2],
                            tiePoints[initialIndex + 3],
                            tiePoints[initialIndex + 4],
                            tiePoints[initialIndex + 5]);
        }
        return retVal;
    }

    /**
     * Gets the noData from the related TIFFField. Check metadata has noData using {@link
     * #hasNoData()} method before calling this method.
     *
     * @return the noData value or {@link Double#NaN} in case of unable to get noData.
     */
    public double getNoData() {
        return noData;
    }

    private double calculateNoData(Node rootNode) {
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
            // TODO: Log a message.
            return Double.NaN;
        }
    }

    /**
     * Tells me if the underlying {@link IIOMetadata} contains ModelTiepointTag tag for {@link
     * org.geotools.coverage.grid.io.imageio.geotiff.TiePoint}.
     *
     * @return true if ModelTiepointTag is present, false otherwise.
     */
    public boolean hasTiePoints() {
        return tiePoints != null && tiePoints.length > 0;
    }

    /**
     * Tells me if the underlying {@link IIOMetadata} contains ModelTiepointTag tag for {@link
     * org.geotools.coverage.grid.io.imageio.geotiff.TiePoint}.
     *
     * @return true if ModelTiepointTag is present, false otherwise.
     */
    public boolean hasPixelScales() {
        if (pixelScale == null) {
            return false;
        } else {
            final double[] values = pixelScale.getValues();
            for (int i = 0; i < values.length; i++) {
                if (Double.isInfinite(values[i]) || Double.isNaN(values[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Tells me if the underlying {@link IIOMetadata} contains NoData Tag.
     *
     * @return true if NoData Tag is present, false otherwise.
     * @see GeoTiffConstants#TIFFTAG_NODATA
     */
    public boolean hasNoData() {
        return !Double.isNaN(noData);
    }

    /**
     * Gets the model tie points from the appropriate TIFFField
     *
     * <p>Attention, for the moment we support only 2D baseline transformations.
     *
     * @return the transformation, or null if not found
     */
    public AffineTransform getModelTransformation() {
        return modelTransformation;
    }

    private AffineTransform calculateModelTransformation(Node rootNode) {
        final IIOMetadataNode node = getTiffField(rootNode, GeoTIFFTagSet.TAG_MODEL_TRANSFORMATION);
        if (node == null) {
            return null;
        }
        final double[] modelTransformation = getTiffDoubles(node);
        if (modelTransformation == null) {
            return null;
        }
        AffineTransform transform = null;
        if (modelTransformation.length == 9) {
            transform =
                    new AffineTransform(
                            modelTransformation[0],
                            modelTransformation[4],
                            modelTransformation[1],
                            modelTransformation[5],
                            modelTransformation[6],
                            modelTransformation[7]);
        } else if (modelTransformation.length == 16) {
            transform =
                    new AffineTransform(
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
     * Tells me if the underlying {@link IIOMetadata} contains ModelTransformationTag tag for {@link
     * AffineTransform} that map from Raster Space to World Space.
     *
     * @return true if ModelTransformationTag is present, false otherwise.
     */
    public boolean hasModelTrasformation() {
        return modelTransformation != null;
    }

    // private utility methods

    /**
     * Gets the value attribute of the given Node.
     *
     * @param node A Node containing a value attribute, for example the node &ltTIFFShort
     *     value=&quot123&quot&gt
     * @return A String containing the text from the value attribute. In the above example, the
     *     string would be 123
     */
    private String getValueAttribute(Node node) {
        return node.getAttributes().getNamedItem(GeoTiffConstants.VALUE_ATTRIBUTE).getNodeValue();
    }

    /** Gets the value attribute's contents and parses it as an int */
    private int getIntValueAttribute(Node node) {
        return Integer.parseInt(getValueAttribute(node));
    }

    /**
     * Gets a TIFFField node with the given tag number. This is done by searching for a TIFFField
     * with attribute number whose value is the specified tag value.
     */
    private IIOMetadataNode getTiffField(Node rootNode, final int tag) {
        Node node = rootNode.getFirstChild();
        if (node != null) {
            node = node.getFirstChild();
            for (; node != null; node = node.getNextSibling()) {
                Node number = node.getAttributes().getNamedItem(GeoTiffConstants.NUMBER_ATTRIBUTE);
                if (number != null && tag == Integer.parseInt(number.getNodeValue())) {
                    return (IIOMetadataNode) node;
                }
            }
        }
        return null;
    }

    /**
     * Gets a single TIFFShort value at the given index.
     *
     * @param tiffField An IIOMetadataNode pointing to a TIFFField element that contains a
     *     TIFFShorts element.
     * @param index The 0-based index of the desired short value
     */
    private int getTiffShort(final IIOMetadataNode tiffField, final int index) {

        return getIntValueAttribute(
                ((IIOMetadataNode) tiffField.getFirstChild())
                        .getElementsByTagName(GeoTiffConstants.GEOTIFF_SHORT_TAG)
                        .item(index));
    }

    /**
     * Gets an array of double values from a TIFFDoubles TIFFField.
     *
     * @param tiffField An IIOMetadataNode pointing to a TIFFField element that contains a
     *     TIFFDoubles element.
     */
    private double[] getTiffDoubles(final IIOMetadataNode tiffField) {

        if (tiffField == null) {
            return null;
        }
        final NodeList doubles =
                ((IIOMetadataNode) tiffField.getFirstChild())
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
     * @param tiffField An IIOMetadataNode pointing to a TIFFField element that contains a
     *     TIFFAsciis element. This element should contain a single TiffAscii element.
     * @return A substring of the value contained in the TIFFAscii node, with the final '|'
     *     character removed.
     */
    private String getTiffAscii(final IIOMetadataNode tiffField, int start, int length) {

        // there should be only one, so get the first
        // GeoTIFFWritingUtilities specification places a vertical bar '|' in
        // place of \0
        // null delimiters so drop off the vertical bar for Java Strings
        final String valueAttribute =
                getValueAttribute(
                        ((IIOMetadataNode) tiffField.getFirstChild())
                                .getElementsByTagName(GeoTiffConstants.GEOTIFF_ASCII_TAG)
                                .item(0));
        if (start == -1) {
            start = 0;
        }
        if (length == -1) {
            length = valueAttribute.length() + 1;
        }
        return valueAttribute.substring(start, start + length - 1);
    }

    /**
     * Gets the TIFFAscii string
     *
     * @param tiffField An IIOMetadataNode pointing to a TIFFField element that contains a
     *     TIFFAsciis element. This element should contain a single TIFFAscii element.
     * @return The value contained in the TIFFAscii node, with the final '|' character removed.
     */
    private String getTiffAscii(final IIOMetadataNode tiffField) {
        return getTiffAscii(tiffField, -1, -1);
    }

    public IIOMetadataNode getRootNode() {
        return (IIOMetadataNode) iioMetadata.getAsTree(iioMetadata.getNativeMetadataFormatName());
    }

    /**
     * Return <code>true</code> if the geokey directory is present, <code>false</code> otherwise. In
     * case no geokey dir is present no CRS can be constructed from this set of metadata.
     *
     * <p>A prj can be used otherwise.
     *
     * @return <code>true</code> if the geokey directory is present, <code>false</code> otherwise.
     */
    public boolean hasGeoKey() {
        return !geoKeys.isEmpty();
    }

    /**
     * Returns the value of an ASCII TIFFTag referred by tagID.
     *
     * @return the tag value as a String, null if not available.
     */
    public String getAsciiTIFFTag(final String tagID) {
        if (GeoTiffConstants.isNumeric(tagID)) {
            final IIOMetadataNode metadataNode =
                    getTiffField(getRootNode(), Integer.valueOf(tagID));
            if (metadataNode != null) {
                return getTiffAscii(metadataNode);
            }
        }
        return null;
    }
} // end of class GeoTiffIIOMetadataDecoder
