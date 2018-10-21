/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.apache.commons.beanutils.BeanUtils;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Utilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Very simple bean to hold the configuration of the mosaic.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *     jar:file:foo.jar/bar.properties URLs
 */
public class MosaicConfigurationBean {

    private Indexer indexer;
    private Double noData;

    /** Default constructor */
    public MosaicConfigurationBean() {}

    public MosaicConfigurationBean(final MosaicConfigurationBean that) {
        Utilities.ensureNonNull("MosaicConfigurationBean", that);
        try {
            BeanUtils.copyProperties(this, that);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * <code>true</code> if we need to expand to RGB(A) the single tiles in case they use a
     * different {@link IndexColorModel}.
     */
    private boolean expandToRGB;

    /** <code>true</code> if we need to look for Auxiliary Metadata PAM XML files */
    private boolean checkAuxiliaryMetadata;

    /** OverviewLevel levels */
    private double[][] levels;

    /** name for the mosaic. */
    private String name;

    /** number of levels */
    private int levelsNum;

    /** time attribute name. <code>null</code> if absent. */
    private String timeAttribute;

    /** elevation attribute name. <code>null</code> if absent. */
    private String elevationAttribute;

    /** crs attribute name, or <code>null</code> if absent */
    private String crsAttribute;

    /** resolution attribute name, or <code>null</code> if absent */
    private String resolutionAttribute;

    /** resolutionX attribute name, or <code>null</code> if absent */
    private String resolutionXAttribute;

    /** resolutionY attribute name, or <code>null</code> if absent */
    private String resolutionYAttribute;

    /** additional domain attributes names. <code>null</code> if absent. */
    private String additionalDomainAttributes;

    private CoordinateReferenceSystem crs;

    /**
     * mosaic's dummy sample model useful to store dataType and number of bands. All the other
     * fields shouldn't be queried since they are meaningless for the whole mosaic (width, height,
     * ...)
     */
    private SampleModel sampleModel;

    private ColorModel colorModel;

    private byte[][] palette;

    /** Imposed envelope for this mosaic. If not present we need to compute from catalogue. */
    private ReferencedEnvelope envelope;

    private String auxiliaryFilePath;

    private String auxiliaryDatastorePath;

    private CatalogConfigurationBean catalogConfigurationBean;

    private String coverageNameCollectorSpi;

    public ReferencedEnvelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(ReferencedEnvelope envelope) {
        this.envelope = envelope;
    }

    public SampleModel getSampleModel() {
        return sampleModel;
    }

    public void setSampleModel(SampleModel sampleModel) {
        this.sampleModel = sampleModel;
    }

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    public byte[][] getPalette() {
        return palette;
    }

    public void setPalette(byte[][] palette) {
        this.palette = palette;
    }

    public String getElevationAttribute() {
        return elevationAttribute;
    }

    public void setElevationAttribute(final String elevationAttribute) {
        this.elevationAttribute = elevationAttribute;
    }

    /** <code>true</code> if we need to manage footprint if available. */
    private boolean footprintManagement;

    public String getTimeAttribute() {
        return timeAttribute;
    }

    public void setTimeAttribute(final String timeAttribute) {
        this.timeAttribute = timeAttribute;
    }

    public String getCRSAttribute() {
        return crsAttribute;
    }

    public void setCRSAttribute(final String crsAttribute) {
        this.crsAttribute = crsAttribute;
    }

    public String getResolutionAttribute() {
        return resolutionAttribute;
    }

    public void setResolutionAttribute(String resolutionAttribute) {
        this.resolutionAttribute = resolutionAttribute;
    }

    public String getResolutionXAttribute() {
        return resolutionXAttribute;
    }

    public void setResolutionXAttribute(String resolutionXAttribute) {
        this.resolutionXAttribute = resolutionXAttribute;
    }

    public String getResolutionYAttribute() {
        return resolutionYAttribute;
    }

    public void setResolutionYAttribute(String resolutionYAttribute) {
        this.resolutionYAttribute = resolutionYAttribute;
    }

    public String getAdditionalDomainAttributes() {
        return additionalDomainAttributes;
    }

    public void setAdditionalDomainAttributes(String additionalDomainAttributes) {
        this.additionalDomainAttributes = additionalDomainAttributes;
    }

    public boolean isExpandToRGB() {
        return expandToRGB;
    }

    public void setExpandToRGB(final boolean expandToRGB) {
        this.expandToRGB = expandToRGB;
    }

    public boolean isCheckAuxiliaryMetadata() {
        return checkAuxiliaryMetadata;
    }

    public void setCheckAuxiliaryMetadata(boolean checkAuxiliaryMetadata) {
        this.checkAuxiliaryMetadata = checkAuxiliaryMetadata;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getLevelsNum() {
        return levelsNum;
    }

    public void setLevelsNum(final int levelsNum) {
        this.levelsNum = levelsNum;
    }

    public double[][] getLevels() {
        return levels.clone();
    }

    public void setLevels(final double[][] levels) {
        this.levels = levels.clone();
    }

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public void setCrs(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    public void setFootprintManagement(final boolean footprintManagement) {
        this.footprintManagement = footprintManagement;
    }

    public boolean isFootprintManagement() {
        return footprintManagement;
    }

    public CatalogConfigurationBean getCatalogConfigurationBean() {
        return catalogConfigurationBean;
    }

    public void setCatalogConfigurationBean(CatalogConfigurationBean catalogConfigurationBean) {
        this.catalogConfigurationBean = catalogConfigurationBean;
    }

    public String getCoverageNameCollectorSpi() {
        return coverageNameCollectorSpi;
    }

    public void setCoverageNameCollectorSpi(String coverageNameCollectorSpi) {
        this.coverageNameCollectorSpi = coverageNameCollectorSpi;
    }

    public String getAuxiliaryFilePath() {
        return auxiliaryFilePath;
    }

    public void setAuxiliaryFilePath(String auxiliaryFilePath) {
        this.auxiliaryFilePath = auxiliaryFilePath;
    }

    public String getAuxiliaryDatastorePath() {
        return auxiliaryDatastorePath;
    }

    public void setAuxiliaryDatastorePath(String auxiliaryDatastorePath) {
        this.auxiliaryDatastorePath = auxiliaryDatastorePath;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public Double getNoData() {
        return noData;
    }

    public void setNoData(Double noData) {
        this.noData = noData;
    }

    @Override
    public String toString() {
        return "MosaicConfigurationBean{"
                + "noData="
                + noData
                + ", expandToRGB="
                + expandToRGB
                + ", checkAuxiliaryMetadata="
                + checkAuxiliaryMetadata
                + ", levels="
                + Arrays.toString(levels)
                + ", name='"
                + name
                + '\''
                + ", levelsNum="
                + levelsNum
                + ", timeAttribute='"
                + timeAttribute
                + '\''
                + ", elevationAttribute='"
                + elevationAttribute
                + '\''
                + ", crsAttribute='"
                + crsAttribute
                + '\''
                + ", additionalDomainAttributes='"
                + additionalDomainAttributes
                + '\''
                + ", crs="
                + crs
                + ", sampleModel="
                + sampleModel
                + ", colorModel="
                + colorModel
                + ", palette="
                + Arrays.toString(palette)
                + ", envelope="
                + envelope
                + ", auxiliaryFilePath='"
                + auxiliaryFilePath
                + '\''
                + ", auxiliaryDatastorePath='"
                + auxiliaryDatastorePath
                + '\''
                + ", coverageNameCollectorSpi='"
                + coverageNameCollectorSpi
                + '\''
                + ", footprintManagement="
                + footprintManagement
                + '}';
    }
}
