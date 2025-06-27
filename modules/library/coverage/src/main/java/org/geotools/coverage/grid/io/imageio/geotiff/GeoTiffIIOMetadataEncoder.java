/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.imageio.geotiff;

import it.geosolutions.imageio.plugins.tiff.BaselineTIFFTagSet;
import it.geosolutions.imageio.plugins.tiff.GeoTIFFTagSet;
import it.geosolutions.imageio.plugins.tiff.TIFFTag;
import it.geosolutions.imageio.plugins.tiff.TIFFTagSet;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.metadata.IIOMetadataNode;
import org.geotools.util.KeySortedList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class is responsible for encoding the geotiff tags into suitable metadata for the ImageIO library.
 *
 * <p>Basically it is and encoder/adapter that collects all the different tags, order it accordingly to the spec and
 * then organize then into a dom tree ready to be used by the ImageIO metadata mechanism.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 */
public class GeoTiffIIOMetadataEncoder {

    /** ASCII_SEPARATOR */
    public static final String ASCII_SEPARATOR = "|";

    public enum TagSet {
        BASELINE {
            @Override
            TIFFTagSet getTagSet() {
                return BaselineTIFFTagSet.getInstance();
            }
        },

        GEOTIFF {
            @Override
            TIFFTagSet getTagSet() {
                return GeoTIFFTagSet.getInstance();
            }
        };

        abstract TIFFTagSet getTagSet();

        public static TIFFTagSet getDefault() {
            return BASELINE.getTagSet();
        }
    }

    private int numModelTiePoints;

    private TiePoint[] modelTiePoints;

    private PixelScale modelPixelScale;

    private double[] modelTransformation;

    private int numGeoTiffEntries;

    private KeySortedList<Integer, GeoKeyEntry> geoTiffEntries;

    private int numGeoTiffDoubleParams;

    private double[] geoTiffDoubleParams;

    private int numGeoTiffAsciiParams;

    private StringBuilder geoTiffAsciiParams;

    private double noData;

    private boolean isNodataSet = false;

    /** TiffTAG Ascii metadata: KeyValue pairs where key is the TAG ID and value is the content */
    private Map<String, String> tiffTagsMetadata;

    private boolean isMetadataSet;

    public GeoTiffIIOMetadataEncoder() {
        this(
                GeoTiffConstants.DEFAULT_GEOTIFF_VERSION,
                GeoTiffConstants.DEFAULT_KEY_REVISION_MAJOR,
                GeoTiffConstants.DEFAULT_KEY_REVISION_MINOR);
    }

    public GeoTiffIIOMetadataEncoder(final int geoTIFFVersion, final int keyRevisionMajor, final int keyRevisionMinor) {
        geoTiffEntries = new KeySortedList<>();
        geoTiffDoubleParams = new double[GeoTiffConstants.ARRAY_ELEM_INCREMENT];
        geoTiffAsciiParams = new StringBuilder();
        modelTiePoints = new TiePoint[GeoTiffConstants.ARRAY_ELEM_INCREMENT];
        modelPixelScale = new PixelScale();
        modelTransformation = new double[16];
        addGeoKeyEntry(geoTIFFVersion, keyRevisionMajor, 1, keyRevisionMinor);
    }

    public static boolean isTiffUShort(final int value) {
        return value >= GeoTiffConstants.USHORT_MIN && value <= GeoTiffConstants.USHORT_MAX;
    }

    public int getGeoTIFFVersion() {
        return getGeoKeyEntryAt(0).getKeyID();
    }

    public void setGeoTIFFVersion(int version) {
        getGeoKeyEntryAt(0).setKeyID(version);
    }

    public int getKeyRevisionMajor() {
        return getGeoKeyEntryAt(0).getTiffTagLocation();
    }

    public int getKeyRevisionMinor() {
        return getGeoKeyEntryAt(0).getCount();
    }

    public void setKeyRevision(int major, int minor) {
        getGeoKeyEntryAt(0).setTiffTagLocation(major);
        getGeoKeyEntryAt(0).setCount(minor);
    }

    public double getModelPixelScaleX() {
        return modelPixelScale.getScaleX();
    }

    public double getModelPixelScaleY() {
        return modelPixelScale.getScaleY();
    }

    public double getModelPixelScaleZ() {
        return modelPixelScale.getScaleZ();
    }

    public void setModelPixelScale(double x, double y) {
        setModelPixelScale(x, y, 0.0);
    }

    public void setModelPixelScale(double x, double y, double z) {
        if (isModelTransformationSet())
            throw new IllegalStateException(
                    "ModelTransformationTag already set. It is not possible to set the ModelPixelScale.");
        modelPixelScale.setScaleX(x);
        modelPixelScale.setScaleY(y);
        modelPixelScale.setScaleZ(z);
    }

    public int getNumModelTiePoints() {
        return numModelTiePoints;
    }

    public TiePoint getModelTiePoint() {
        return getModelTiePointAt(0);
    }

    public TiePoint getModelTiePointAt(int index) {
        return modelTiePoints[index];
    }

    public void setModelTiePoint(double i, double j, double x, double y) {
        setModelTiePoint(i, j, 0.0, x, y, 0.0);
    }

    public void setModelTiePoint(double i, double j, double k, double x, double y, double z) {
        if (isModelTransformationSet())
            throw new IllegalStateException(
                    "ModelTransformationTag already set. It is not possible to set the ModelTiePoint.");
        if (getNumModelTiePoints() > 0) {
            getModelTiePointAt(0).set(i, j, k, x, y, z);
        } else {
            addModelTiePoint(i, j, k, x, y, z);
        }
    }

    public void addModelTiePoint(double i, double j, double x, double y) {
        addModelTiePoint(i, j, 0.0, x, y, 0.0);
    }

    public void addModelTiePoint(double i, double j, double k, double x, double y, double z) {
        final int numTiePoints = numModelTiePoints;

        if (numTiePoints >= modelTiePoints.length - 1) {
            final TiePoint[] tiePoints = new TiePoint[numTiePoints + GeoTiffConstants.ARRAY_ELEM_INCREMENT];
            System.arraycopy(modelTiePoints, 0, tiePoints, 0, numTiePoints);
            modelTiePoints = tiePoints;
        }

        modelTiePoints[numTiePoints] = new TiePoint(i, j, k, x, y, z);
        numModelTiePoints++;
    }

    public int getNumGeoKeyEntries() {
        return numGeoTiffEntries;
    }

    public GeoKeyEntry getGeoKeyEntryAt(int index) {
        // got to retrieve the eleme at a certain index
        final Object it = this.geoTiffEntries.get(index);
        if (it != null) return (GeoKeyEntry) it;
        return null;
    }

    public GeoKeyEntry getGeoKeyEntry(int keyID) {
        GeoKeyEntry retVal = null;
        if (geoTiffEntries.count(keyID) <= 0) {
            return null;
        }
        final Object o = geoTiffEntries.first(keyID);
        if (o != null) {
            retVal = (GeoKeyEntry) o;
        }

        return retVal;
    }

    public boolean hasGeoKeyEntry(int keyID) {
        return getGeoKeyEntry(keyID) != null;
    }

    public int getGeoShortParam(int keyID) {
        final GeoKeyEntry entry = getNonNullGeoKeyEntry(keyID);
        final int tag = entry.getTiffTagLocation();
        final int value = entry.getValueOffset();
        checkParamTag(tag, 0);

        return value;
    }

    public double getGeoDoubleParam(int keyID) {
        final GeoKeyEntry entry = getNonNullGeoKeyEntry(keyID);
        final int tag = entry.getTiffTagLocation();
        final int offset = entry.getValueOffset();
        checkParamTag(tag, getGeoDoubleParamsTag().getNumber());

        return geoTiffDoubleParams[offset];
    }

    public double[] getGeoDoubleParams(int keyID) {
        return getGeoDoubleParams(keyID, null);
    }

    public double[] getGeoDoubleParams(int keyID, double[] values) {
        final GeoKeyEntry entry = getNonNullGeoKeyEntry(keyID);
        final int tag = entry.getTiffTagLocation();
        final int count = entry.getCount();
        final int offset = entry.getValueOffset();
        checkParamTag(tag, getGeoDoubleParamsTag().getNumber());

        if (values == null) {
            values = new double[count];
        }

        System.arraycopy(geoTiffDoubleParams, offset, values, 0, count);

        return values;
    }

    /**
     * Gives access to the GeoAscii tiff content for the specified GeoKey metatag.
     *
     * <p>Returns <code>null</code> in case the ascii params is not present.
     *
     * @param keyID the MetaTag to look for.
     * @return <code>null</code> in case the ascii params is not present, otherwise the metatag content.
     */
    public String getGeoAsciiParam(int keyID) {
        final GeoKeyEntry entry = getGeoKeyEntry(keyID);
        if (entry == null) {
            return null;
        }
        final int tag = entry.getTiffTagLocation();
        final int count = entry.getCount();
        final int offset = entry.getValueOffset();
        checkParamTag(tag, getGeoAsciiParamsTag().getNumber());

        return geoTiffAsciiParams.substring(offset, offset + count);
    }

    public GeoKeyEntry addGeoShortParam(int keyID, int value) {
        return addGeoKeyEntry(keyID, 0, 1, value);
    }

    public void addGeoDoubleParam(int keyID, double value) {
        addGeoDoubleParamsRef(keyID, 1);
        addDoubleParam(value);
    }

    public void addGeoDoubleParams(int keyID, double[] values) {
        addGeoDoubleParamsRef(keyID, values.length);
        for (double value : values) {
            addDoubleParam(value);
        }
    }

    public void addGeoAscii(int keyID, String value) {
        // check whether or not this is the first geoascii element
        GeoKeyEntry currentEntry = getGeoKeyEntry(keyID);
        if (currentEntry != null) {
            // get old value
            final String currentGeoAscii = getGeoAsciiParam(keyID);
            assert currentGeoAscii != null;

            // store old length
            final int currentLength = currentGeoAscii.length();
            assert currentEntry.getCount() == currentLength;

            // update value
            final StringBuilder newValue = new StringBuilder(currentGeoAscii);
            if (!currentGeoAscii.endsWith(ASCII_SEPARATOR)) {
                newValue.append(ASCII_SEPARATOR);
            }
            newValue.append(value).append(ASCII_SEPARATOR);

            // now add to the geoascii metatag
            updateGeoAsciiParamsRef(keyID, newValue.length(), currentEntry.getValueOffset());

            // +1 for the '|' character to be appended
            replaceAsciiParam(
                    newValue.toString(), currentEntry.getValueOffset(), currentEntry.getValueOffset() + currentLength);
        } else {
            // now add to the geoascii metatag
            // +1 is required in advance since we need to add the pipe which will be encoded.
            addGeoAsciiParamsRef(keyID, value.length() + 1);

            // +1 for the '|' character to be appended
            appendAsciiParam(value);
        }
    }
    /**
     * Updates the provided metatag to the GeoKey directory.
     *
     * @param keyID the GeoTiff metatag id
     * @param tag the TIFF tag to which this points to
     * @param count the new count value
     * @param offset the new offset value inside the tiff tag or 0 if the value is embeeded
     * @return the updated {@link GeoKeyEntry}.
     */
    private GeoKeyEntry updateGeoKeyEntry(int keyID, int tag, int count, int offset) {
        if (!isTiffUShort(keyID)) {
            throw new IllegalArgumentException("keyID is not a TIFF USHORT");
        }

        if (!isTiffUShort(tag)) {
            throw new IllegalArgumentException("tag is not a TIFF USHORT");
        }

        if (!isTiffUShort(count)) {
            throw new IllegalArgumentException("count is not a TIFF USHORT");
        }

        if (!isTiffUShort(offset)) {
            throw new IllegalArgumentException("offset is not a TIFF USHORT");
        }

        final GeoKeyEntry element = new GeoKeyEntry(keyID, tag, count, offset);
        // replacing
        final int valuesRemoved = geoTiffEntries.removeAll(keyID); // purge old values for this tag
        assert valuesRemoved == 1;
        geoTiffEntries.add(Integer.valueOf(keyID), element);
        return element;
    }

    /**
     * Adds the provided metatag to the GeoKey directory.
     *
     * @param keyID the GeoTiff metatag id
     * @param tag the TIFF tag to which this points to
     * @param count the new count value
     * @param offset the new offset value inside the tiff tag or 0 if the value is embeeded
     * @return the added {@link GeoKeyEntry}.
     */
    private GeoKeyEntry addGeoKeyEntry(int keyID, int tag, int count, int offset) {
        if (!isTiffUShort(keyID)) {
            throw new IllegalArgumentException("keyID is not a TIFF USHORT");
        }

        if (!isTiffUShort(tag)) {
            throw new IllegalArgumentException("tag is not a TIFF USHORT");
        }

        if (!isTiffUShort(count)) {
            throw new IllegalArgumentException("count is not a TIFF USHORT");
        }

        if (!isTiffUShort(offset)) {
            throw new IllegalArgumentException("offset is not a TIFF USHORT");
        }

        final int numKeyEntries = numGeoTiffEntries;
        final GeoKeyEntry element = new GeoKeyEntry(keyID, tag, count, offset);
        numGeoTiffEntries++;
        geoTiffEntries.add(Integer.valueOf(keyID), element);

        getGeoKeyEntryAt(0).setCount(numKeyEntries);
        return element;
    }

    public void assignTo(Element element) {
        if (!element.getLocalName().equals(GeoTiffConstants.GEOTIFF_IIO_ROOT_ELEMENT_NAME)) {
            throw new IllegalArgumentException("root not found: " + GeoTiffConstants.GEOTIFF_IIO_ROOT_ELEMENT_NAME);
        }

        final Element ifd1 = getChild(element, GeoTiffConstants.GEOTIFF_IFD_TAG);

        if (ifd1 == null) {
            throw new IllegalArgumentException("Unable to find child " + GeoTiffConstants.GEOTIFF_IFD_TAG);
        }

        final Element ifd2 = createIFD();
        String attribute = ifd2.getAttribute(GeoTiffConstants.GEOTIFF_TAGSETS_ATT_NAME);
        ifd1.setAttribute(GeoTiffConstants.GEOTIFF_TAGSETS_ATT_NAME, "".equals(attribute) ? null : attribute);

        NodeList childNodes = ifd2.getChildNodes();
        final Element[] childElems = new Element[childNodes.getLength()];
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Element child = (Element) childNodes.item(i);
            childElems[i] = child;
        }
        for (final Element child : childElems) {
            ifd2.removeChild(child);
            ifd1.appendChild(child);
        }
    }

    public Element createRootTree() {
        final Element rootElement = newElement(GeoTiffConstants.GEOTIFF_IIO_ROOT_ELEMENT_NAME);
        rootElement.appendChild(createIFD());

        return rootElement;
    }

    protected static TIFFTag getGeoKeyDirectoryTag() {
        return GeoTIFFTagSet.getInstance().getTag(GeoTIFFTagSet.TAG_GEO_KEY_DIRECTORY);
    }

    protected static TIFFTag getGeoDoubleParamsTag() {
        return GeoTIFFTagSet.getInstance().getTag(GeoTIFFTagSet.TAG_GEO_DOUBLE_PARAMS);
    }

    protected static TIFFTag getGeoAsciiParamsTag() {
        return GeoTIFFTagSet.getInstance().getTag(GeoTIFFTagSet.TAG_GEO_ASCII_PARAMS);
    }

    protected static TIFFTag getModelPixelScaleTag() {
        return GeoTIFFTagSet.getInstance().getTag(GeoTIFFTagSet.TAG_MODEL_PIXEL_SCALE);
    }

    protected static TIFFTag getModelTiePointTag() {
        return GeoTIFFTagSet.getInstance().getTag(GeoTIFFTagSet.TAG_MODEL_TIE_POINT);
    }

    protected static TIFFTag getModelTransformationTag() {
        return GeoTIFFTagSet.getInstance().getTag(GeoTIFFTagSet.TAG_MODEL_TRANSFORMATION);
    }

    protected static TIFFTag getAsciiTag(String set, int tagID) {
        if (set != null && set.length() > 0) {
            try {
                TagSet tagSet = TagSet.valueOf(set);
                if (tagSet != null) {
                    return tagSet.getTagSet().getTag(tagID);
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    protected static TIFFTag getNoDataTag() {
        return GeoTiffConstants.NODATA_TAG;
    }

    private GeoKeyEntry getNonNullGeoKeyEntry(int keyID) {
        final GeoKeyEntry entry = getGeoKeyEntry(keyID);

        if (entry == null) {
            throw new IllegalArgumentException("Unable to find an entry for the provided geo key " + keyID);
        }

        return entry;
    }

    private void checkParamTag(final int tag, final int expectedTag) {
        if (tag != expectedTag) {
            if (expectedTag == 0) {
                throw new IllegalArgumentException("invalid key access, not a GeoTIFF SHORT parameter");
            } else if (expectedTag == getGeoDoubleParamsTag().getNumber()) {
                throw new IllegalArgumentException("invalid key access, not a GeoTIFF DOUBLE parameter");
            } else if (expectedTag == getGeoAsciiParamsTag().getNumber()) {
                throw new IllegalArgumentException("invalid key access, not a GeoTIFF ASCII parameter");
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private void addDoubleParam(double param) {
        final int numDoubleParams = numGeoTiffDoubleParams;

        if (numDoubleParams >= geoTiffDoubleParams.length - 1) {
            final double[] doubleParams = new double[numDoubleParams + GeoTiffConstants.ARRAY_ELEM_INCREMENT];
            System.arraycopy(geoTiffDoubleParams, 0, doubleParams, 0, numDoubleParams);
            geoTiffDoubleParams = doubleParams;
        }

        geoTiffDoubleParams[numDoubleParams] = param;
        numGeoTiffDoubleParams++;
    }

    /**
     * Replaces part of the ascii params with the provided elements
     *
     * @param str the {@link String} for the replacement.
     */
    private void replaceAsciiParam(String str, int start, int end) {
        geoTiffAsciiParams.replace(start, end, str);
    }

    /**
     * Appends a new ascii params at the current list of elements, incrementing the size and appending a new separator
     * "|"
     *
     * @param param the {@link String} to be appended.
     */
    private void appendAsciiParam(String param) {
        geoTiffAsciiParams.append(param);
        geoTiffAsciiParams.append('|');
        numGeoTiffAsciiParams++;
    }

    private GeoKeyEntry addGeoDoubleParamsRef(int keyID, int count) {
        return addGeoKeyEntry(keyID, getGeoDoubleParamsTag().getNumber(), count, getCurrentGeoDoublesOffset());
    }

    private GeoKeyEntry addGeoAsciiParamsRef(int keyID, int length) {
        return addGeoKeyEntry(keyID, getGeoAsciiParamsTag().getNumber(), length, getCurrentGeoAsciisOffset());
    }

    private GeoKeyEntry updateGeoAsciiParamsRef(int keyID, int length, int offset) {
        return updateGeoKeyEntry(keyID, getGeoAsciiParamsTag().getNumber(), length, offset);
    }

    private int getCurrentGeoDoublesOffset() {
        return numGeoTiffDoubleParams;
    }

    private int getCurrentGeoAsciisOffset() {
        return geoTiffAsciiParams.length();
    }

    private Element createIFD() {
        Element ifd = newElement(GeoTiffConstants.GEOTIFF_IFD_TAG);
        ifd.setAttribute(
                GeoTiffConstants.GEOTIFF_TAGSETS_ATT_NAME,
                BaselineTIFFTagSet.class.getName() + "," + GeoTIFFTagSet.class.getName());

        if (modelPixelScale.isSet()) {
            ifd.appendChild(createModelPixelScaleElement());
        }

        if (isModelTiePointsSet()) {
            ifd.appendChild(createModelTiePointsElement());
        } else if (isModelTransformationSet()) {
            ifd.appendChild(createModelTransformationElement());
        }

        if (getNumGeoKeyEntries() > 1) {
            ifd.appendChild(createGeoKeyDirectoryElement());
        }

        if (numGeoTiffDoubleParams > 0) {
            ifd.appendChild(createGeoDoubleParamsElement());
        }

        if (numGeoTiffAsciiParams > 0) {
            ifd.appendChild(createGeoAsciiParamsElement());
        }

        if (isNodataSet) {
            ifd.appendChild(createNoDataElement());
        }

        if (isMetadataSet) {
            createMetadataElement(ifd);
        }

        return ifd;
    }

    private boolean isModelTiePointsSet() {
        return numModelTiePoints > 0;
    }

    private boolean isModelTransformationSet() {
        for (double v : modelTransformation) {
            if (v != 0.0) {
                return true;
            }
        }

        return false;
    }

    public void setModelTransformation(final AffineTransform rasterToModel) {
        if (modelPixelScale != null && modelPixelScale.isSet())
            throw new IllegalStateException(
                    "ModelPixelScaleTag already set. It is not possible to set the ModelTransformation.");
        if (isModelTiePointsSet())
            throw new IllegalStateException(
                    "ModelTiePointsTag already set. It is not possible to set the ModelTransformation.");

        // //
        //
        // See pag 28 of the spec for an explanation
        //
        // //
        // a
        modelTransformation[0] = rasterToModel.getScaleX();
        // b
        modelTransformation[1] = rasterToModel.getShearX();
        // c
        modelTransformation[2] = 0;
        // d
        modelTransformation[3] = rasterToModel.getTranslateX();
        // e
        modelTransformation[4] = rasterToModel.getShearY();
        // f
        modelTransformation[5] = rasterToModel.getScaleY();
        // g
        modelTransformation[6] = 0;
        // h
        modelTransformation[7] = rasterToModel.getTranslateY();
        // i
        modelTransformation[8] = 0;
        // j
        modelTransformation[9] = 0;
        // k
        modelTransformation[10] = 0;
        // l
        modelTransformation[11] = 0;
        // m
        modelTransformation[12] = 0;
        // n
        modelTransformation[13] = 0;
        // o
        modelTransformation[14] = 0;
        // p
        modelTransformation[15] = 1;
    }

    private Element createGeoKeyDirectoryElement() {
        Element field = createFieldElement(getGeoKeyDirectoryTag());
        Element data = newElement(GeoTiffConstants.GEOTIFF_SHORTS_TAG);
        field.appendChild(data);

        // GeoKey directory root tag
        int[] values = getGeoKeyEntryAt(0).getValues();
        data.appendChild(createShortElement(values[0]));
        data.appendChild(createShortElement(values[1]));
        data.appendChild(createShortElement(values[3]));
        data.appendChild(createShortElement(values[2]));

        // GeoKeys
        for (int i = 1; i < numGeoTiffEntries; i++) {
            values = getGeoKeyEntryAt(i).getValues();
            for (int value : values) {
                Element GeoKeyRecord = createShortElement(value);
                data.appendChild(GeoKeyRecord);
            }
        }

        return field;
    }

    private Element createGeoDoubleParamsElement() {
        Element field = createFieldElement(getGeoDoubleParamsTag());
        Element data = newElement(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
        field.appendChild(data);
        for (int i = 0; i < numGeoTiffDoubleParams; i++) {
            Element param = createDoubleElement(geoTiffDoubleParams[i]);
            data.appendChild(param);
        }

        return field;
    }

    private Element createGeoAsciiParamsElement() {
        Element field = createFieldElement(getGeoAsciiParamsTag());
        Element data = newElement(GeoTiffConstants.GEOTIFF_ASCIIS_TAG);
        field.appendChild(data);
        data.appendChild(createAsciiElement(geoTiffAsciiParams.toString()));

        return field;
    }

    private Element createModelPixelScaleElement() {
        Element field = createFieldElement(getModelPixelScaleTag());
        Element data = newElement(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
        field.appendChild(data);
        addDoubleElements(data, modelPixelScale.getValues());

        return field;
    }

    private Element createModelTransformationElement() {
        Element field = createFieldElement(getModelTransformationTag());
        Element data = newElement(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
        field.appendChild(data);
        addDoubleElements(data, modelTransformation);

        return field;
    }

    private Element createModelTiePointsElement() {
        Element field = createFieldElement(getModelTiePointTag());
        Element data = newElement(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
        field.appendChild(data);

        for (int i = 0; i < numModelTiePoints; i++) {
            addDoubleElements(data, modelTiePoints[i].getData());
        }

        return field;
    }

    private Element createNoDataElement() {
        Element field = createFieldElement(getNoDataTag());
        Element data = newElement(GeoTiffConstants.GEOTIFF_ASCIIS_TAG);
        field.appendChild(data);
        data.appendChild(createAsciiElement(Double.toString(noData)));
        return field;
    }

    private void createMetadataElement(Element ifd) {
        if (ifd != null && tiffTagsMetadata != null && !tiffTagsMetadata.isEmpty()) {
            Iterator<String> keys = tiffTagsMetadata.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String[] setIdPair = key.split(":");
                String set = TagSet.BASELINE.toString();
                if (setIdPair.length > 1) {
                    set = setIdPair[0].toUpperCase();
                }
                String keyName = setIdPair[setIdPair.length - 1];
                if (GeoTiffConstants.isNumeric(keyName)) {
                    final String value = tiffTagsMetadata.get(key);
                    final TIFFTag tag = getAsciiTag(set, Integer.valueOf(keyName));
                    if (tag != null) {
                        Element field = createFieldElement(tag);
                        Element data = newElement(GeoTiffConstants.GEOTIFF_ASCIIS_TAG);
                        field.appendChild(data);
                        data.appendChild(createAsciiElement(value));
                        ifd.appendChild(field);
                    }
                }
            }
        }
    }

    private Element createFieldElement(final TIFFTag tag) {
        Element field = newElement(GeoTiffConstants.GEOTIFF_FIELD_TAG);
        field.setAttribute(GeoTiffConstants.NUMBER_ATTRIBUTE, String.valueOf(tag.getNumber()));
        field.setAttribute(GeoTiffConstants.NAME_ATTRIBUTE, tag.getName());

        return field;
    }

    private Element createShortElement(final int value) {
        Element GeoKeyRecord = newElement(GeoTiffConstants.GEOTIFF_SHORT_TAG);
        GeoKeyRecord.setAttribute(GeoTiffConstants.VALUE_ATTRIBUTE, String.valueOf(value));

        return GeoKeyRecord;
    }

    private Element createDoubleElement(final double value) {
        Element param = newElement(GeoTiffConstants.GEOTIFF_DOUBLE_TAG);
        param.setAttribute(GeoTiffConstants.VALUE_ATTRIBUTE, String.valueOf(value));

        return param;
    }

    private Element createAsciiElement(final String value) {
        Element param = newElement(GeoTiffConstants.GEOTIFF_ASCII_TAG);
        param.setAttribute(GeoTiffConstants.VALUE_ATTRIBUTE, String.valueOf(value));

        return param;
    }

    private void addDoubleElements(Element data, final double[] values) {
        for (double value : values) {
            Element GeoKeyRecord = createDoubleElement(value);
            data.appendChild(GeoKeyRecord);
        }
    }

    public double getNoData() {
        return noData;
    }

    public void setNoData(double noData) {
        this.noData = noData;
        isNodataSet = true;
    }

    /** Allows to setup metadata by leveraging on Ascii TIFF Tags. */
    public void setTiffTagsMetadata(Map<String, String> metadata) {
        this.tiffTagsMetadata = metadata;
        isMetadataSet = true;
    }

    private Element newElement(String name) {
        return new IIOMetadataNode(name);
    }

    /** @return the first matching child element, or null if not found */
    private Element getChild(Element element, String childName) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element child = (Element) childNodes.item(i);
            if (childName.equals(child.getLocalName())) {
                return child;
            }
        }
        return null;
    }
}
