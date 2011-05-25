/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.util.KeySortedList;
import org.jdom.Element;



/**
 * This class is responsible for encoding the geotiff tags into suitable
 * metadata for the ImageIO library.
 * 
 * <p>
 * Basically it is and encoder/adapter that collects all the different tags,
 * order it accordingly to the spec and then organize then into a dom tree ready
 * to be used by the ImageIO metadata mechanism.
 * 
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 * 
 *
 *
 * @source $URL$
 */
public class GeoTiffIIOMetadataEncoder {

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
	
	private StringBuffer geoTiffAsciiParams;
	
	private double noData;
	
	private boolean isNodataSet = false;

	
	/** TiffTAG Ascii metadata: KeyValue pairs where key is the TAG ID and value is the content */
	private Map<String, String> tiffTagsMetadata;

	private boolean isMetadataSet;
	
	public GeoTiffIIOMetadataEncoder() {
		this(GeoTiffConstants.DEFAULT_GEOTIFF_VERSION,
				GeoTiffConstants.DEFAULT_KEY_REVISION_MAJOR,
				GeoTiffConstants.DEFAULT_KEY_REVISION_MINOR);
	}

	public GeoTiffIIOMetadataEncoder(final int geoTIFFVersion,
			final int keyRevisionMajor, final int keyRevisionMinor) {
		geoTiffEntries = new KeySortedList<Integer, GeoKeyEntry>();
		geoTiffDoubleParams = new double[GeoTiffConstants.ARRAY_ELEM_INCREMENT];
		geoTiffAsciiParams = new StringBuffer();
		modelTiePoints = new TiePoint[GeoTiffConstants.ARRAY_ELEM_INCREMENT];
		modelPixelScale = new PixelScale();
		modelTransformation = new double[16];
		addGeoKeyEntry(geoTIFFVersion, keyRevisionMajor, 1, keyRevisionMinor);
	}

	public static boolean isTiffUShort(final int value) {
		return (value >= GeoTiffConstants.USHORT_MIN)
				&& (value <= GeoTiffConstants.USHORT_MAX);
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

	public void setModelTiePoint(double i, double j, double k, double x,
			double y, double z) {
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

	public void addModelTiePoint(double i, double j, double k, double x,
			double y, double z) {
		final int numTiePoints = numModelTiePoints;

		if (numTiePoints >= (modelTiePoints.length - 1)) {
			final TiePoint[] tiePoints = new TiePoint[numTiePoints
					+ GeoTiffConstants.ARRAY_ELEM_INCREMENT];
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
		if (it != null)
			return (GeoKeyEntry) (it);
		return null;
	}

	public GeoKeyEntry getGeoKeyEntry(int keyID) {
		GeoKeyEntry retVal = null;
		final Object o = geoTiffEntries.first(Integer.valueOf(keyID));
		if (o != null)
			retVal = (GeoKeyEntry) o;

		return retVal;
	}

	public boolean hasGeoKeyEntry(int keyID) {
		return getGeoKeyEntry(keyID) != null;
	}

	public int getGeoShortParam(int keyID) {
		final GeoKeyEntry entry = getNonNullKeyEntry(keyID);
		final int tag = entry.getTiffTagLocation();
		final int value = entry.getValueOffset();
		checkParamTag(tag, 0);

		return value;
	}

	public double getGeoDoubleParam(int keyID) {
		final GeoKeyEntry entry = getNonNullKeyEntry(keyID);
		final int tag = entry.getTiffTagLocation();
		final int offset = entry.getValueOffset();
		checkParamTag(tag, getGeoDoubleParamsTag().getNumber());

		return geoTiffDoubleParams[offset];
	}

	public double[] getGeoDoubleParams(int keyID) {
		return getGeoDoubleParams(keyID, null);
	}

	public double[] getGeoDoubleParams(int keyID, double[] values) {
		final GeoKeyEntry entry = getNonNullKeyEntry(keyID);
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

	public String getGeoAsciiParam(int keyID) {
		final GeoKeyEntry entry = getNonNullKeyEntry(keyID);
		final int tag = entry.getTiffTagLocation();
		final int count = entry.getCount();
		final int offset = entry.getValueOffset();
		checkParamTag(tag, getGeoAsciiParamsTag().getNumber());

		return geoTiffAsciiParams.substring(offset, (offset + count) - 1);
	}

	public void addGeoShortParam(int keyID, int value) {
		addGeoKeyEntry(keyID, 0, 1, value);
	}

	public void addGeoDoubleParam(int keyID, double value) {
		addGeoDoubleParamsRef(keyID, 1);
		addDoubleParam(value);
	}

	public void addGeoDoubleParams(int keyID, double[] values) {
		addGeoDoubleParamsRef(keyID, values.length);
		for (int i = 0; i < values.length; i++) {
			addDoubleParam(values[i]);
		}
	}

	public void addGeoAscii(int keyID, String value) {
		addGeoAsciiParamsRef(keyID, value.length() + 1);
		// +1 for the '|' character to be appended
		addAsciiParam(value);
	}

	private void addGeoKeyEntry(int keyID, int tag, int count, int offset) {
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
		geoTiffEntries.add(Integer.valueOf(keyID), new GeoKeyEntry(keyID, tag,
				count, offset));
		getGeoKeyEntryAt(0).setCount(numKeyEntries);
		numGeoTiffEntries++;
	}

	public void assignTo(Element element) {
		if (!element.getName().equals(
				GeoTiffConstants.GEOTIFF_IIO_ROOT_ELEMENT_NAME)) {
			throw new IllegalArgumentException("root not found: "
					+ GeoTiffConstants.GEOTIFF_IIO_ROOT_ELEMENT_NAME);
		}

		final Element ifd1 = element.getChild(GeoTiffConstants.GEOTIFF_IFD_TAG);

		if (ifd1 == null) {
			throw new IllegalArgumentException("Unable to find child "
					+ GeoTiffConstants.GEOTIFF_IFD_TAG);
		}

		final Element ifd2 = createIFD();
		ifd1.setAttribute(GeoTiffConstants.GEOTIFF_TAGSETS_ATT_NAME, ifd2
				.getAttributeValue(GeoTiffConstants.GEOTIFF_TAGSETS_ATT_NAME));

		final Element[] childElems = (Element[]) ifd2.getChildren().toArray(
				new Element[0]);
		for (int i = 0; i < childElems.length; i++) {
			final Element child = childElems[i];
			ifd2.removeContent(child);
			ifd1.addContent(child);
		}
	}

	public Element createRootTree() {
		final Element rootElement = new Element(
				GeoTiffConstants.GEOTIFF_IIO_ROOT_ELEMENT_NAME);
		rootElement.addContent(createIFD());

		return rootElement;
	}

	protected static TIFFTag getGeoKeyDirectoryTag() {
		return GeoTIFFTagSet.getInstance().getTag(
				GeoTIFFTagSet.TAG_GEO_KEY_DIRECTORY);
	}

	protected static TIFFTag getGeoDoubleParamsTag() {
		return GeoTIFFTagSet.getInstance().getTag(
				GeoTIFFTagSet.TAG_GEO_DOUBLE_PARAMS);
	}

	protected static TIFFTag getGeoAsciiParamsTag() {
		return GeoTIFFTagSet.getInstance().getTag(
				GeoTIFFTagSet.TAG_GEO_ASCII_PARAMS);
	}

	protected static TIFFTag getModelPixelScaleTag() {
		return GeoTIFFTagSet.getInstance().getTag(
				GeoTIFFTagSet.TAG_MODEL_PIXEL_SCALE);
	}

	protected static TIFFTag getModelTiePointTag() {
		return GeoTIFFTagSet.getInstance().getTag(
				GeoTIFFTagSet.TAG_MODEL_TIE_POINT);
	}

	protected static TIFFTag getModelTransformationTag() {
		return GeoTIFFTagSet.getInstance().getTag(
				GeoTIFFTagSet.TAG_MODEL_TRANSFORMATION);
	}
	
	protected static TIFFTag getAsciiTag(String set, int tagID) {
		if (set != null && set.length() > 0 ) {
			try {
				TagSet tagSet = TagSet.valueOf(set);
				if (tagSet != null){
					return tagSet.getTagSet().getTag(tagID);
				}
			} catch (Exception e){
				
			}
		}
		return null;
		
	}
	
	protected static TIFFTag getNoDataTag() {
	        return GeoTiffConstants.NODATA_TAG;
        }

	private GeoKeyEntry getNonNullKeyEntry(int keyID) {
		final GeoKeyEntry entry = getGeoKeyEntry(keyID);

		if (entry == null) {
			throw new IllegalArgumentException(
					"Unable to find an entry for the provided geo key " + keyID);
		}

		return entry;
	}

	private void checkParamTag(final int tag, final int expectedTag) {
		if (tag != expectedTag) {
			if (expectedTag == 0) {
				throw new IllegalArgumentException(
						"invalid key access, not a GeoTIFF SHORT parameter");
			} else if (expectedTag == getGeoDoubleParamsTag().getNumber()) {
				throw new IllegalArgumentException(
						"invalid key access, not a GeoTIFF DOUBLE parameter");
			} else if (expectedTag == getGeoAsciiParamsTag().getNumber()) {
				throw new IllegalArgumentException(
						"invalid key access, not a GeoTIFF ASCII parameter");
			} else {
				throw new IllegalStateException();
			}
		}
	}

	private void addDoubleParam(double param) {
		final int numDoubleParams = numGeoTiffDoubleParams;

		if (numDoubleParams >= (geoTiffDoubleParams.length - 1)) {
			final double[] doubleParams = new double[numDoubleParams
					+ GeoTiffConstants.ARRAY_ELEM_INCREMENT];
			System.arraycopy(geoTiffDoubleParams, 0, doubleParams, 0,
					numDoubleParams);
			geoTiffDoubleParams = doubleParams;
		}

		geoTiffDoubleParams[numDoubleParams] = param;
		numGeoTiffDoubleParams++;
	}

	private void addAsciiParam(String param) {
		geoTiffAsciiParams.append(param);
		geoTiffAsciiParams.append('|');
		numGeoTiffAsciiParams++;
	}

	private void addGeoDoubleParamsRef(int keyID, int count) {
		addGeoKeyEntry(keyID, getGeoDoubleParamsTag().getNumber(), count,
				getCurrentGeoDoublesOffset());
	}

	private void addGeoAsciiParamsRef(int keyID, int length) {
		addGeoKeyEntry(keyID, getGeoAsciiParamsTag().getNumber(), length,
				getCurrentGeoAsciisOffset());
	}

	private int getCurrentGeoDoublesOffset() {
		return numGeoTiffDoubleParams;
	}

	private int getCurrentGeoAsciisOffset() {
		return geoTiffAsciiParams.length();
	}

	private Element createIFD() {
		Element ifd = new Element(GeoTiffConstants.GEOTIFF_IFD_TAG);
		ifd.setAttribute(GeoTiffConstants.GEOTIFF_TAGSETS_ATT_NAME,
				BaselineTIFFTagSet.class.getName() + ","
						+ GeoTIFFTagSet.class.getName());

		if (modelPixelScale.isSet()) {
			ifd.addContent(createModelPixelScaleElement());
		}

		if (isModelTiePointsSet()) {
			ifd.addContent(createModelTiePointsElement());
		} else if (isModelTransformationSet()) {
			ifd.addContent(createModelTransformationElement());
		}

		if (getNumGeoKeyEntries() > 1) {
			ifd.addContent(createGeoKeyDirectoryElement());
		}

		if (numGeoTiffDoubleParams > 0) {
			ifd.addContent(createGeoDoubleParamsElement());
		}

		if (numGeoTiffAsciiParams > 0) {
			ifd.addContent(createGeoAsciiParamsElement());
		}
		
		if (isNodataSet) {
		    ifd.addContent(createNoDataElement());
		}
		
		if (isMetadataSet){
			createMetadataElement(ifd);
		}

		return ifd;
	}

	private boolean isModelTiePointsSet() {
		return numModelTiePoints > 0;
	}

	private boolean isModelTransformationSet() {
		for (int i = 0; i < modelTransformation.length; i++) {
			if (modelTransformation[i] != 0.0) {
				return true;
			}
		}

		return false;
	}

	public void setModelTransformation(final AffineTransform rasterToModel) {
		if (modelPixelScale!=null&&modelPixelScale.isSet())
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
		Element data = new Element(GeoTiffConstants.GEOTIFF_SHORTS_TAG);
		field.addContent(data);
		int[] values;
		
		//GeoKey directory root tag
		values = getGeoKeyEntryAt(0).getValues();
		data.addContent(createShortElement(values[0]));
		data.addContent(createShortElement(values[1]));
		data.addContent(createShortElement(values[3]));
		data.addContent(createShortElement(values[2]));
		
		//GeoKeys
		for (int i = 1; i < numGeoTiffEntries; i++) {
			values = getGeoKeyEntryAt(i).getValues();
			int lenght = values.length;
			for (int j = 0; j < lenght; j++) {
				Element GeoKeyRecord = createShortElement(values[j]);
				data.addContent(GeoKeyRecord);
			}
		}

		return field;
	}

	private Element createGeoDoubleParamsElement() {
		Element field = createFieldElement(getGeoDoubleParamsTag());
		Element data = new Element(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
		field.addContent(data);
		for (int i = 0; i < numGeoTiffDoubleParams; i++) {
			Element param = createDoubleElement(geoTiffDoubleParams[i]);
			data.addContent(param);
		}

		return field;
	}

	private Element createGeoAsciiParamsElement() {
		Element field = createFieldElement(getGeoAsciiParamsTag());
		Element data = new Element(GeoTiffConstants.GEOTIFF_ASCIIS_TAG);
		field.addContent(data);
		data.addContent(createAsciiElement(geoTiffAsciiParams.toString()));

		return field;
	}

	private Element createModelPixelScaleElement() {
		Element field = createFieldElement(getModelPixelScaleTag());
		Element data = new Element(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
		field.addContent(data);
		addDoubleElements(data, modelPixelScale.getValues());

		return field;
	}

	private Element createModelTransformationElement() {
		Element field = createFieldElement(getModelTransformationTag());
		Element data = new Element(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
		field.addContent(data);
		addDoubleElements(data, modelTransformation);

		return field;
	}

	private Element createModelTiePointsElement() {
		Element field = createFieldElement(getModelTiePointTag());
		Element data = new Element(GeoTiffConstants.GEOTIFF_DOUBLES_TAG);
		field.addContent(data);

		for (int i = 0; i < numModelTiePoints; i++) {
			addDoubleElements(data, modelTiePoints[i].getData());
		}

		return field;
	}
	
	private Element createNoDataElement() {
            Element field = createFieldElement(getNoDataTag());
            Element data = new Element(GeoTiffConstants.GEOTIFF_ASCIIS_TAG);
            field.addContent(data);
            data.addContent(createAsciiElement(Double.toString(noData)));
            return field;
        }
	
	private void createMetadataElement(Element ifd) {
		if (ifd != null && tiffTagsMetadata != null && !tiffTagsMetadata.isEmpty()){
			Iterator<String> keys = tiffTagsMetadata.keySet().iterator();
			while (keys.hasNext()){
				String key = keys.next();
				String setIdPair[] = key.split(":");
				String set = TagSet.BASELINE.toString();
				if (setIdPair.length > 1){
					set = setIdPair[0].toUpperCase();
				}
				String keyName = setIdPair[setIdPair.length - 1]; 
				if (GeoTiffConstants.isNumeric(keyName)){
					final String value = tiffTagsMetadata.get(key);
					final TIFFTag tag = getAsciiTag(set, Integer.valueOf(keyName));
					if (tag != null){
						Element field = createFieldElement(tag);
				        Element data = new Element(GeoTiffConstants.GEOTIFF_ASCIIS_TAG);
				        field.addContent(data);
				        data.addContent(createAsciiElement(value));
				        ifd.addContent(field);
					}
				}
			}
		}
    }

	private Element createFieldElement(final TIFFTag tag) {
		Element field = new Element(GeoTiffConstants.GEOTIFF_FIELD_TAG);
		field.setAttribute(GeoTiffConstants.NUMBER_ATTRIBUTE, String.valueOf(tag
				.getNumber()));
		field.setAttribute(GeoTiffConstants.NAME_ATTRIBUTE, tag.getName());

		return field;
	}

	private Element createShortElement(final int value) {
		Element GeoKeyRecord = new Element(GeoTiffConstants.GEOTIFF_SHORT_TAG);
		GeoKeyRecord.setAttribute(GeoTiffConstants.VALUE_ATTRIBUTE, String
				.valueOf(value));

		return GeoKeyRecord;
	}

	private Element createDoubleElement(final double value) {
		Element param = new Element(GeoTiffConstants.GEOTIFF_DOUBLE_TAG);
		param.setAttribute(GeoTiffConstants.VALUE_ATTRIBUTE, String.valueOf(value));

		return param;
	}

	private Element createAsciiElement(final String value) {
		Element param = new Element(GeoTiffConstants.GEOTIFF_ASCII_TAG);
		param.setAttribute(GeoTiffConstants.VALUE_ATTRIBUTE, String.valueOf(value));

		return param;
	}

	private void addDoubleElements(Element data, final double[] values) {
		final int length = values.length;
		for (int j = 0; j < length; j++) {
			Element GeoKeyRecord = createDoubleElement(values[j]);
			data.addContent(GeoKeyRecord);
		}
	}

        public double getNoData() {
            return noData;
        }
    
        public void setNoData(double noData) {
            this.noData = noData;
            isNodataSet = true;
        }

    /**
     * Allows to setup metadata by leveraging on Ascii TIFF Tags.
     * 
     * @param name
     *            is the Ascii TIFF Tag identifier. It can be a String representing: 
     *            1) a simple Integer (referring to a tag ID) (in that case it will refer to the BaselineTIFFTagSet 
     *            2) OR an identifier in the form: TIFFTagSet:TIFFTagID. As an
     *            instance: "BaselineTIFFTagSet:305" in order to add the Copyright info.
     * @param value
     *            is the value to be assigned to that metadata.
     * @see GeoTiffIIOMetadataEncoder.TagSet
     */
    public void setTiffTagsMetadata(Map<String, String> metadata) {
        this.tiffTagsMetadata = metadata;
        isMetadataSet = true;
    }

}
