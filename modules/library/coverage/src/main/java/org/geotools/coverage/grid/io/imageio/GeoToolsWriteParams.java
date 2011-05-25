/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.imageio;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Locale;

import javax.imageio.IIOParamController;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;

/**
 * @author Simone Giannecchini
 * @since 2.3.x
 * 
 *
 *
 * @source $URL$
 */
public abstract class GeoToolsWriteParams extends ImageWriteParam {

	protected ImageWriteParam adaptee;

	public boolean canWriteCompressed() {
		return adaptee.canWriteCompressed();
	}

	public boolean canWriteProgressive() {

		return adaptee.canWriteProgressive();
	}

	public boolean canWriteTiles() {

		return adaptee.canWriteTiles();
	}

	public float getBitRate(float quality) {

		return adaptee.getBitRate(quality);
	}

	public int getCompressionMode() {

		return adaptee.getCompressionMode();
	}

	public float getCompressionQuality() {

		return adaptee.getCompressionQuality();
	}

	public String[] getCompressionQualityDescriptions() {

		return adaptee.getCompressionQualityDescriptions();
	}

	public float[] getCompressionQualityValues() {

		return adaptee.getCompressionQualityValues();
	}

	public String getCompressionType() {

		return adaptee.getCompressionType();
	}

	public String[] getCompressionTypes() {

		return adaptee.getCompressionTypes();
	}

	public String getLocalizedCompressionTypeName() {

		return adaptee.getLocalizedCompressionTypeName();
	}

	public Dimension[] getPreferredTileSizes() {

		return adaptee.getPreferredTileSizes();
	}

	public int getProgressiveMode() {

		return adaptee.getProgressiveMode();
	}

	public int getTileHeight() {

		return adaptee.getTileHeight();
	}

	public int getTileWidth() {

		return adaptee.getTileWidth();
	}

	public int getTilingMode() {

		return adaptee.getTilingMode();
	}

	public boolean isCompressionLossless() {

		return adaptee.isCompressionLossless();
	}

	public void setCompressionMode(int mode) {

		adaptee.setCompressionMode(mode);
	}

	public void setCompressionQuality(float quality) {

		adaptee.setCompressionQuality(quality);
	}

	public void setCompressionType(String compressionType) {

		adaptee.setCompressionType(compressionType);
	}

	public void setProgressiveMode(int mode) {

		adaptee.setProgressiveMode(mode);
	}

	public void setTiling(int tileWidth, int tileHeight) {

		adaptee.setTiling(tileWidth, tileHeight, 0, 0);
	}

	public void setTilingMode(int mode) {

		adaptee.setTilingMode(mode);
	}

	public void unsetCompression() {

		adaptee.unsetCompression();
	}

	public void unsetTiling() {

		adaptee.unsetTiling();
	}

	public void setDestinationType(ImageTypeSpecifier destinationType) {

		adaptee.setDestinationType(destinationType);
	}

	public boolean canOffsetTiles() {
		return false;
	}

	public Locale getLocale() {
		return adaptee.getLocale();
	}

	public int getTileGridXOffset() {
		return adaptee.getTileGridXOffset();
	}

	public int getTileGridYOffset() {
		return adaptee.getTileGridYOffset();
	}

	public boolean activateController() {
		throw new UnsupportedOperationException(
				"This operation is not currently supported by this API");
	}

	public IIOParamController getController() {
		throw new UnsupportedOperationException(
				"This operation is not currently supported by this API");
	}

	public IIOParamController getDefaultController() {
		throw new UnsupportedOperationException(
				"This operation is not currently supported by this API");
	}

	public Point getDestinationOffset() {
		return adaptee.getDestinationOffset();
	}

	public ImageTypeSpecifier getDestinationType() {
		return adaptee.getDestinationType();
	}

	public int[] getSourceBands() {
		return adaptee.getSourceBands();
	}

	public Rectangle getSourceRegion() {
		return adaptee.getSourceRegion();
	}

	public int getSourceXSubsampling() {
		return adaptee.getSourceXSubsampling();
	}

	public int getSourceYSubsampling() {
		return adaptee.getSourceYSubsampling();
	}

	public int getSubsamplingXOffset() {
		return adaptee.getSubsamplingXOffset();
	}

	public int getSubsamplingYOffset() {
		return adaptee.getSubsamplingYOffset();
	}

	public boolean hasController() {
		return false;
	}

	public void setController(IIOParamController controller) {
		throw new UnsupportedOperationException(
				"This operation is not currently supported by this API");
	}

	public void setDestinationOffset(Point destinationOffset) {
		throw new UnsupportedOperationException(
				"This operation is not currently supported by this API");
	}

	public void setSourceBands(int[] sourceBands) {
		adaptee.setSourceBands(sourceBands);
	}

	public void setSourceRegion(Rectangle sourceRegion) {
		adaptee.setSourceRegion(sourceRegion);
	}

	public void setSourceSubsampling(int sourceXSubsampling,
			int sourceYSubsampling, int subsamplingXOffset,
			int subsamplingYOffset) {
		adaptee.setSourceSubsampling(sourceXSubsampling, sourceYSubsampling,
				subsamplingXOffset, subsamplingYOffset);
	}

	/**
	 * 
	 */
	public GeoToolsWriteParams(final ImageWriteParam adaptee) {
		this.adaptee = adaptee;
	}

	/**
	 * @param locale
	 */
	public GeoToolsWriteParams(final ImageWriteParam adaptee, Locale locale) {

		super(locale);
		this.adaptee = adaptee;
	}

	public final ImageWriteParam getAdaptee() {
		return adaptee;
	}

}
