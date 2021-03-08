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
 */
public abstract class GeoToolsWriteParams extends ImageWriteParam {

    protected ImageWriteParam adaptee;

    @Override
    public boolean canWriteCompressed() {
        return adaptee.canWriteCompressed();
    }

    @Override
    public boolean canWriteProgressive() {

        return adaptee.canWriteProgressive();
    }

    @Override
    public boolean canWriteTiles() {

        return adaptee.canWriteTiles();
    }

    @Override
    public float getBitRate(float quality) {

        return adaptee.getBitRate(quality);
    }

    @Override
    public int getCompressionMode() {

        return adaptee.getCompressionMode();
    }

    @Override
    public float getCompressionQuality() {

        return adaptee.getCompressionQuality();
    }

    @Override
    public String[] getCompressionQualityDescriptions() {

        return adaptee.getCompressionQualityDescriptions();
    }

    @Override
    public float[] getCompressionQualityValues() {

        return adaptee.getCompressionQualityValues();
    }

    @Override
    public String getCompressionType() {

        return adaptee.getCompressionType();
    }

    @Override
    public String[] getCompressionTypes() {

        return adaptee.getCompressionTypes();
    }

    @Override
    public String getLocalizedCompressionTypeName() {

        return adaptee.getLocalizedCompressionTypeName();
    }

    @Override
    public Dimension[] getPreferredTileSizes() {

        return adaptee.getPreferredTileSizes();
    }

    @Override
    public int getProgressiveMode() {

        return adaptee.getProgressiveMode();
    }

    @Override
    public int getTileHeight() {

        return adaptee.getTileHeight();
    }

    @Override
    public int getTileWidth() {

        return adaptee.getTileWidth();
    }

    @Override
    public int getTilingMode() {

        return adaptee.getTilingMode();
    }

    @Override
    public boolean isCompressionLossless() {

        return adaptee.isCompressionLossless();
    }

    @Override
    public void setCompressionMode(int mode) {

        adaptee.setCompressionMode(mode);
    }

    @Override
    public void setCompressionQuality(float quality) {

        adaptee.setCompressionQuality(quality);
    }

    @Override
    public void setCompressionType(String compressionType) {

        adaptee.setCompressionType(compressionType);
    }

    @Override
    public void setProgressiveMode(int mode) {

        adaptee.setProgressiveMode(mode);
    }

    public void setTiling(int tileWidth, int tileHeight) {

        adaptee.setTiling(tileWidth, tileHeight, 0, 0);
    }

    @Override
    public void setTilingMode(int mode) {

        adaptee.setTilingMode(mode);
    }

    @Override
    public void unsetCompression() {

        adaptee.unsetCompression();
    }

    @Override
    public void unsetTiling() {

        adaptee.unsetTiling();
    }

    @Override
    public void setDestinationType(ImageTypeSpecifier destinationType) {

        adaptee.setDestinationType(destinationType);
    }

    @Override
    public boolean canOffsetTiles() {
        return false;
    }

    @Override
    public Locale getLocale() {
        return adaptee.getLocale();
    }

    @Override
    public int getTileGridXOffset() {
        return adaptee.getTileGridXOffset();
    }

    @Override
    public int getTileGridYOffset() {
        return adaptee.getTileGridYOffset();
    }

    @Override
    public boolean activateController() {
        throw new UnsupportedOperationException(
                "This operation is not currently supported by this API");
    }

    @Override
    public IIOParamController getController() {
        throw new UnsupportedOperationException(
                "This operation is not currently supported by this API");
    }

    @Override
    public IIOParamController getDefaultController() {
        throw new UnsupportedOperationException(
                "This operation is not currently supported by this API");
    }

    @Override
    public Point getDestinationOffset() {
        return adaptee.getDestinationOffset();
    }

    @Override
    public ImageTypeSpecifier getDestinationType() {
        return adaptee.getDestinationType();
    }

    @Override
    public int[] getSourceBands() {
        return adaptee.getSourceBands();
    }

    @Override
    public Rectangle getSourceRegion() {
        return adaptee.getSourceRegion();
    }

    @Override
    public int getSourceXSubsampling() {
        return adaptee.getSourceXSubsampling();
    }

    @Override
    public int getSourceYSubsampling() {
        return adaptee.getSourceYSubsampling();
    }

    @Override
    public int getSubsamplingXOffset() {
        return adaptee.getSubsamplingXOffset();
    }

    @Override
    public int getSubsamplingYOffset() {
        return adaptee.getSubsamplingYOffset();
    }

    @Override
    public boolean hasController() {
        return false;
    }

    @Override
    public void setController(IIOParamController controller) {
        throw new UnsupportedOperationException(
                "This operation is not currently supported by this API");
    }

    @Override
    public void setDestinationOffset(Point destinationOffset) {
        throw new UnsupportedOperationException(
                "This operation is not currently supported by this API");
    }

    @Override
    public void setSourceBands(int[] sourceBands) {
        adaptee.setSourceBands(sourceBands);
    }

    @Override
    public void setSourceRegion(Rectangle sourceRegion) {
        adaptee.setSourceRegion(sourceRegion);
    }

    @Override
    public void setSourceSubsampling(
            int sourceXSubsampling,
            int sourceYSubsampling,
            int subsamplingXOffset,
            int subsamplingYOffset) {
        adaptee.setSourceSubsampling(
                sourceXSubsampling, sourceYSubsampling, subsamplingXOffset, subsamplingYOffset);
    }

    /** */
    public GeoToolsWriteParams(final ImageWriteParam adaptee) {
        this.adaptee = adaptee;
    }

    /** @param locale */
    public GeoToolsWriteParams(final ImageWriteParam adaptee, Locale locale) {

        super(locale);
        this.adaptee = adaptee;
    }

    public final ImageWriteParam getAdaptee() {
        return adaptee;
    }
}
